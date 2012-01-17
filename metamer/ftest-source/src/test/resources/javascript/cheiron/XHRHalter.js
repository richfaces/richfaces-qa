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

/* ###################################################### */
Ajocado.Page.XHRHalter._repeatWait = function(id) {
	var halter = Ajocado.Page.XHRHalter._instances[id];
	halter.tryProcessStates();
	if (!halter.isXhrCompleted()) {
		setTimeout("Ajocado.Page.XHRHalter._repeatWait(" + id + ")", 100);
	}
};

Ajocado.Page.XHRHalter.prototype.isXhrCompleted = function() {
	return this.currentState == Ajocado.Page.XHRHalter.STATE_COMPLETE;
};

Ajocado.Page.XHRHalter.prototype.getLastAvailableState = function() {
	return this.availableStates.length - 1;
};

/* ###################################################### */

Ajocado.Page.XHRHalter.continueTo = function(id, state) {
	var halter = Ajocado.Page.XHRHalter._instances[id];
	if (state < halter.continueToState) {
		throw new Error("cannot continue to state (" + state + ") before the actual state (" + halter.continueToState + ")");
	}
	halter.continueToState = state;
};

/* ###################################################### */
Ajocado.Page.XHRHalter.isWaitingForSend = function(id) {
	var halter = Ajocado.Page.XHRHalter._instances[id];
	return halter.currentState == Ajocado.Page.XHRHalter.STATE_OPEN && halter.continueToState == Ajocado.Page.XHRHalter.STATE_OPEN;
};

Ajocado.Page.XHRHalter.prototype.tryProcessStates = function() {
	while (this.currentState < this.continueToState && this.currentState < this.getLastAvailableState()) {
		this.currentState += 1;
		this.processState(this.currentState);
	}
};

Ajocado.Page.XHRHalter.prototype.processState = function(state) {
	if (this.currentState > 0 && this.availableStates[state] === undefined) {
		return;
	}
	this.loadXhrParams(state);
	switch (state) {
		case Ajocado.Page.XHRHalter.STATE_SEND :
			Ajocado.Page.XHRHalter.XHRWrapperInjection.send.call(this.xhr, this.sendParams['content']);
			break;
		case Ajocado.Page.XHRHalter.STATE_UNITIALIZED :
			break;
		default :
			this.callback(this.xhr);
	}
	this.loadXhrParams(this.lastAvailableState);
};

Ajocado.Page.XHRHalter.prototype.loadXhrParams = function(state) {
	state = Math.max(state, Ajocado.Page.XHRHalter.STATE_UNITIALIZED);
	var holder = this.availableStates[state];
	this.xhr.readyState = state;
	this.xhr.responseText = holder.responseText;
	this.xhr.responseXML = holder.responseXML;
	this.xhr.status = holder.status;
	this.xhr.statusText = holder.statusText;
	this.xhr.onreadystatechange = holder.onreadystatechange;
};

Ajocado.Page.XHRHalter.prototype.saveXhrParams = function() {
	this.lastAvailableState = Math.max(this.lastAvailableState, this.xhr.readyState);
	this.availableStates[this.xhr.readyState] = {};
	var holder = this.availableStates[this.xhr.readyState];
	holder.responseText = this.xhr.responseText;
	holder.responseXML = this.xhr.responseXML;
	holder.status = this.xhr.status;
	holder.statusText = this.xhr.statusText;
	holder.onreadystatechange = this.xhr.onreadystatechange;
};

/* ###################################################### */

Ajocado.Page.XHRHalter.prototype.wait = function() {
	Ajocado.Page.XHRHalter._repeatWait(this.id);
};

Ajocado.Page.XHRHalter.isHandleAvailable = function() {
	
	return Ajocado.Page.XHRHalter._instances.length - 1 >= Ajocado.Page.XHRHalter._haltCounter;
};

Ajocado.XHRWrapper.prototype.onreadystatechangeCallback = function() {
	if (Ajocado.Page.XHRHalter.isEnabled()) {
		var halter = Ajocado.Page.XHRHalter._associations[this];
		halter.saveXhrParams();
	} else {
		Ajocado.Page.XHRHalter.XHRWrapperInjection.onreadystatechangeCallback.call(this);
	}
};

Ajocado.XHRWrapper.prototype.open = function(method, url, asyncFlag, userName, password) {
	if (Ajocado.Page.XHRHalter.isEnabled()) {
		var halter = Ajocado.Page.XHRHalter._associations[this];
		if (halter === undefined) {
			halter = new Ajocado.Page.XHRHalter(this);
		} else {
			Ajocado.Page.XHRHalter.call(halter, this);
		}
		halter.wait();
	}
	asyncFlag = (asyncFlag !== false);
	return Ajocado.Page.XHRHalter.XHRWrapperInjection.open.call(this, method, url, asyncFlag, userName, password);
};

Ajocado.XHRWrapper.prototype.send = function(content) {
	if (Ajocado.Page.XHRHalter.isEnabled()) {
		var halter = Ajocado.Page.XHRHalter._associations[this];
		halter.sendParams['content'] = content;
		halter.saveXhrParams();
		halter.wait();
	} else {
		return Ajocado.Page.XHRHalter.XHRWrapperInjection.send.call(this, content);
	}
};


