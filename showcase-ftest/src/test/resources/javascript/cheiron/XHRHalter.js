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
var XHRHalter = function(xhr) {
	XHRHalter._associations[xhr] = this;
	if (this.id == undefined) {
		XHRHalter._instances.push(this);
		this.id = XHRHalter._instances.length - 1;
	}
	this.currentState = XHRHalter.STATE_OPEN;
	this.lastAvailableState = XHRHalter.STATE_OPEN;
	this.availableStates = new Array();
	this.continueToState = XHRHalter.STATE_OPEN;
	this.xhr = xhr;
	this.sendParams = {};
	this.xhrParams = new Array();
	this.saveXhrParams();
	this.callback = function(xhr) {
		XHRHalter.XHRWrapperInjection.onreadystatechangeCallback.call(xhr);
	};
};

XHRHalter.STATE_OPEN = -2;
XHRHalter.STATE_SEND = -1;
XHRHalter.STATE_UNITIALIZED = 0;
XHRHalter.STATE_LOADING = 1;
XHRHalter.STATE_LOADED = 2;
XHRHalter.STATE_INTERACTIVE = 3;
XHRHalter.STATE_COMPLETE = 4;

XHRHalter._instances = new Array();
XHRHalter._associations = {};
XHRHalter._haltCounter = 0;
XHRHalter._enabled = true;

XHRHalter.setEnabled = function(enabled) {
	XHRHalter._enabled = enabled;
};

XHRHalter.isEnabled = function(enabled) {
	return XHRHalter._enabled;
};

XHRHalter.getHandle = function() {
	if (XHRHalter.isHandleAvailable()) {
		return XHRHalter._haltCounter++;
	}
	return -1;
};

XHRHalter.XHRWrapperInjection = {
	onreadystatechangeCallback : Ajocado.Page.XHRHalter.XHRWrapper.prototype.onreadystatechangeCallback,
	open : Ajocado.Page.XHRHalter.XHRWrapper.prototype.open,
	send : Ajocado.Page.XHRHalter.XHRWrapper.prototype.send
};

XHRHalter._repeatWait = function(id) {
	var halter = XHRHalter._instances[id];
	halter.tryProcessStates();
	if (!halter.isXhrCompleted()) {
		setTimeout("XHRHalter._repeatWait(" + id + ")", 100);
	}
};

XHRHalter.prototype.isXhrCompleted = function() {
	return this.currentState == XHRHalter.STATE_COMPLETE;
};

XHRHalter.prototype.getLastAvailableState = function() {
	return this.availableStates.length - 1;
};

XHRHalter.continueTo = function(id, state) {
	var halter = XHRHalter._instances[id];
	if (state < halter.continueToState) {
		throw new Error("cannot continue to state (" + state + ") before the actual state (" + halter.continueToState + ")");
	}
	halter.continueToState = state;
};

XHRHalter.isWaitingForSend = function(id) {
	var halter = XHRHalter._instances[id];
	return halter.currentState == XHRHalter.STATE_OPEN && halter.continueToState == XHRHalter.STATE_OPEN;
};

XHRHalter.prototype.tryProcessStates = function() {
	while (this.currentState < this.continueToState && this.currentState < this.getLastAvailableState()) {
		this.currentState += 1;
		this.processState(this.currentState);
	}
};

XHRHalter.prototype.processState = function(state) {
	if (this.currentState > 0 && this.availableStates[state] === undefined) {
		return;
	}
	this.loadXhrParams(state);
	switch (state) {
		case XHRHalter.STATE_SEND :
			XHRHalter.XHRWrapperInjection.send.call(this.xhr, this.sendParams['content']);
			break;
		case XHRHalter.STATE_UNITIALIZED :
			break;
		default :
			this.callback(this.xhr);
	}
	this.loadXhrParams(this.lastAvailableState);
};

XHRHalter.prototype.loadXhrParams = function(state) {
	state = Math.max(state, XHRHalter.STATE_UNITIALIZED);
	var holder = this.availableStates[state];
	this.xhr.readyState = state;
	this.xhr.responseText = holder.responseText;
	this.xhr.responseXML = holder.responseXML;
	this.xhr.status = holder.status;
	this.xhr.statusText = holder.statusText;
	this.xhr.onreadystatechange = holder.onreadystatechange;
};

XHRHalter.prototype.saveXhrParams = function() {
	this.lastAvailableState = Math.max(this.lastAvailableState, this.xhr.readyState);
	this.availableStates[this.xhr.readyState] = {};
	var holder = this.availableStates[this.xhr.readyState];
	holder.responseText = this.xhr.responseText;
	holder.responseXML = this.xhr.responseXML;
	holder.status = this.xhr.status;
	holder.statusText = this.xhr.statusText;
	holder.onreadystatechange = this.xhr.onreadystatechange;
};

XHRHalter.prototype.wait = function() {
	XHRHalter._repeatWait(this.id);
};

XHRHalter.isHandleAvailable = function() {
	return XHRHalter._instances.length - 1 >= XHRHalter._haltCounter;
};

Ajocado.Page.XHRHalter.XHRWrapper.prototype.onreadystatechangeCallback = function() {
	if (XHRHalter.isEnabled()) {
		var halter = XHRHalter._associations[this];
		halter.saveXhrParams();
	} else {
		XHRHalter.XHRWrapperInjection.onreadystatechangeCallback.call(this);
	}
};

Ajocado.Page.XHRHalter.XHRWrapper.prototype.open = function(method, url, asyncFlag, userName, password) {
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

Ajocado.Page.XHRHalter.XHRWrapper.prototype.send = function(content) {
	if (XHRHalter.isEnabled()) {
		var halter = XHRHalter._associations[this];
		halter.sendParams['content'] = content;
		halter.saveXhrParams();
		halter.wait();
	} else {
		return XHRHalter.XHRWrapperInjection.send.call(this, content);
	}
};
