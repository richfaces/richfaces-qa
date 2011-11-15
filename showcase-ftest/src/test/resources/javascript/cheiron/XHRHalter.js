/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
var Ajocado = Ajocado || {};

Ajocado.Page = Ajocado.Page || {};
 
Ajocado.Page.XHRHalter = function(xhr) {
	Ajocado.Page.XHRHalter._associations[xhr] = this;
	if (this.id == undefined) {
		Ajocado.Page.XHRHalter._instances.push(this);
		this.id = Ajocado.Page.XHRHalter._instances.length - 1;
	}
	this.currentState = Ajocado.Page.XHRHalter.STATE_OPEN;
	this.lastAvailableState = Ajocado.Page.XHRHalter.STATE_OPEN;
	this.availableStates = new Array();
	this.continueToState = Ajocado.Page.XHRHalter.STATE_OPEN;
	this.xhr = xhr;
	this.sendParams = {};
	this.xhrParams = new Array();
	this.saveXhrParams();
	this.callback = function(xhr) {
		Ajocado.Page.XHRHalter.XHRWrapperInjection.onreadystatechangeCallback.call(xhr);
	};
};

Ajocado.Page.XHRHalter.STATE_OPEN = -2;
Ajocado.Page.XHRHalter.STATE_SEND = -1;
Ajocado.Page.XHRHalter.STATE_UNITIALIZED = 0;
Ajocado.Page.XHRHalter.STATE_LOADING = 1;
Ajocado.Page.XHRHalter.STATE_LOADED = 2;
Ajocado.Page.XHRHalter.STATE_INTERACTIVE = 3;
Ajocado.Page.XHRHalter.STATE_COMPLETE = 4;

Ajocado.Page.XHRHalter._instances = new Array();
Ajocado.Page.XHRHalter._associations = {};
Ajocado.Page.XHRHalter._haltCounter = 0;
Ajocado.Page.XHRHalter._enabled = true;

Ajocado.Page.XHRHalter.setEnabled = function(enabled) {
	Ajocado.Page.XHRHalter._enabled = enabled;
};

Ajocado.Page.XHRHalter.isEnabled = function(enabled) {
	return Ajocado.Page.XHRHalter._enabled;
};

Ajocado.Page.XHRHalter.getHandle = function() {
	if (Ajocado.Page.XHRHalter.isHandleAvailable()) {
		return Ajocado.Page.XHRHalter._haltCounter++;
	}
	return -1;
};

Ajocado.Page.XHRHalter.XHRWrapperInjection = {
		onreadystatechangeCallback : Ajocado.XHRWrapper.prototype.onreadystatechangeCallback,
		open : Ajocado.XHRWrapper.prototype.open,
		send : Ajocado.XHRWrapper.prototype.send
};

Ajocado.Page.XHRHalter.continueTo = function(id, state) {
	var halter = Ajocado.Page.XHRHalter._instances[id];
	if (state < halter.continueToState) {
		throw new Error("cannot continue to state (" + state + ") before the actual state (" + halter.continueToState + ")");
	}
	halter.continueToState = state;
};

Ajocado.Page.XHRHalter.prototype.wait = function() {
	Ajocado.Page.XHRHalter._repeatWait(this.id);
};

Ajocado.Page.XHRHalter.isHandleAvailable = function() {
	return Ajocado.Page.XHRHalter._instances.length - 1 >= Ajocado.Page.XHRHalter._haltCounter;
};

Ajocado.RequestGuard.XHRWrapper.prototype.onreadystatechangeCallback = function() {
	alert("sds");
	if (XHRHalter.isEnabled()) {
		var halter = XHRHalter._associations[this];
		halter.saveXhrParams();
	} else {
		XHRHalter.XHRWrapperInjection.onreadystatechangeCallback.call(this);
	}
};

Ajocado.RequestGuard.XHRWrapper.prototype.open = function(method, url, asyncFlag, userName, password) {
	if (XHRHalter.isEnabled()) {
		var halter = XHRHalter._associations[this];
		if (halter === undefined) {
			halter = new XHRHalter(this);
		} else {
			XHRHalter.call(halter, this);
		}
		halter.wait();
	}
	asyncFlag = (asyncFlag !== false);
	return XHRHalter.XHRWrapperInjection.open.call(this, method, url, asyncFlag, userName, password);
};

Ajocado.RequestGuard.XHRWrapper.prototype.send = function(content) {
	if (XHRHalter.isEnabled()) {
		var halter = XHRHalter._associations[this];
		halter.sendParams['content'] = content;
		halter.saveXhrParams();
		halter.wait();
	} else {
		return XHRHalter.XHRWrapperInjection.send.call(this, content);
	}
};


