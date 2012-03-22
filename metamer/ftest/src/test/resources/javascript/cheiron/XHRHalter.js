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
var Graphene = Graphene || {};

Graphene.Page = Graphene.Page || {};
 
Graphene.Page.XHRHalter = function(xhr) {
	Graphene.Page.XHRHalter._associations[xhr] = this;
	if (this.id == undefined) {
		Graphene.Page.XHRHalter._instances.push(this);
		this.id = Graphene.Page.XHRHalter._instances.length - 1;
	}
	this.currentState = Graphene.Page.XHRHalter.STATE_OPEN;
	this.lastAvailableState = Graphene.Page.XHRHalter.STATE_OPEN;
	this.availableStates = new Array();
	this.continueToState = Graphene.Page.XHRHalter.STATE_OPEN;
	this.xhr = xhr;
	this.sendParams = {};
	this.xhrParams = new Array();
	this.saveXhrParams();
	this.callback = function(xhr) {
		Graphene.Page.XHRHalter.XHRWrapperInjection.onreadystatechangeCallback.call(xhr);
	};
};

Graphene.Page.XHRHalter.STATE_OPEN = -2;
Graphene.Page.XHRHalter.STATE_SEND = -1;
Graphene.Page.XHRHalter.STATE_UNITIALIZED = 0;
Graphene.Page.XHRHalter.STATE_LOADING = 1;
Graphene.Page.XHRHalter.STATE_LOADED = 2;
Graphene.Page.XHRHalter.STATE_INTERACTIVE = 3;
Graphene.Page.XHRHalter.STATE_COMPLETE = 4;

Graphene.Page.XHRHalter._instances = new Array();
Graphene.Page.XHRHalter._associations = {};
Graphene.Page.XHRHalter._haltCounter = 0;
Graphene.Page.XHRHalter._enabled = true;

Graphene.Page.XHRHalter.setEnabled = function(enabled) {
	Graphene.Page.XHRHalter._enabled = enabled;
};

Graphene.Page.XHRHalter.isEnabled = function(enabled) {
	return Graphene.Page.XHRHalter._enabled;
};

Graphene.Page.XHRHalter.getHandle = function() {
	if (Graphene.Page.XHRHalter.isHandleAvailable()) {
		return Graphene.Page.XHRHalter._haltCounter++;
	}
	return -1;
};

Graphene.Page.XHRHalter.XHRWrapperInjection = {
		onreadystatechangeCallback : Graphene.XHRWrapper.prototype.onreadystatechangeCallback,
		open : Graphene.XHRWrapper.prototype.open,
		send : Graphene.XHRWrapper.prototype.send
};

/* ###################################################### */
Graphene.Page.XHRHalter._repeatWait = function(id) {
	var halter = Graphene.Page.XHRHalter._instances[id];
	halter.tryProcessStates();
	if (!halter.isXhrCompleted()) {
		setTimeout("Graphene.Page.XHRHalter._repeatWait(" + id + ")", 100);
	}
};

Graphene.Page.XHRHalter.prototype.isXhrCompleted = function() {
	return this.currentState == Graphene.Page.XHRHalter.STATE_COMPLETE;
};

Graphene.Page.XHRHalter.prototype.getLastAvailableState = function() {
	return this.availableStates.length - 1;
};

/* ###################################################### */

Graphene.Page.XHRHalter.continueTo = function(id, state) {
	var halter = Graphene.Page.XHRHalter._instances[id];
	if (state < halter.continueToState) {
		throw new Error("cannot continue to state (" + state + ") before the actual state (" + halter.continueToState + ")");
	}
	halter.continueToState = state;
};

/* ###################################################### */
Graphene.Page.XHRHalter.isWaitingForSend = function(id) {
	var halter = Graphene.Page.XHRHalter._instances[id];
	return halter.currentState == Graphene.Page.XHRHalter.STATE_OPEN && halter.continueToState == Graphene.Page.XHRHalter.STATE_OPEN;
};

Graphene.Page.XHRHalter.prototype.tryProcessStates = function() {
	while (this.currentState < this.continueToState && this.currentState < this.getLastAvailableState()) {
		this.currentState += 1;
		this.processState(this.currentState);
	}
};

Graphene.Page.XHRHalter.prototype.processState = function(state) {
	if (this.currentState > 0 && this.availableStates[state] === undefined) {
		return;
	}
	this.loadXhrParams(state);
	switch (state) {
		case Graphene.Page.XHRHalter.STATE_SEND :
			Graphene.Page.XHRHalter.XHRWrapperInjection.send.call(this.xhr, this.sendParams['content']);
			break;
		case Graphene.Page.XHRHalter.STATE_UNITIALIZED :
			break;
		default :
			this.callback(this.xhr);
	}
	this.loadXhrParams(this.lastAvailableState);
};

Graphene.Page.XHRHalter.prototype.loadXhrParams = function(state) {
	state = Math.max(state, Graphene.Page.XHRHalter.STATE_UNITIALIZED);
	var holder = this.availableStates[state];
	this.xhr.readyState = state;
	this.xhr.responseText = holder.responseText;
	this.xhr.responseXML = holder.responseXML;
	this.xhr.status = holder.status;
	this.xhr.statusText = holder.statusText;
	this.xhr.onreadystatechange = holder.onreadystatechange;
};

Graphene.Page.XHRHalter.prototype.saveXhrParams = function() {
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

Graphene.Page.XHRHalter.prototype.wait = function() {
	Graphene.Page.XHRHalter._repeatWait(this.id);
};

Graphene.Page.XHRHalter.isHandleAvailable = function() {
	
	return Graphene.Page.XHRHalter._instances.length - 1 >= Graphene.Page.XHRHalter._haltCounter;
};

Graphene.XHRWrapper.prototype.onreadystatechangeCallback = function() {
	if (Graphene.Page.XHRHalter.isEnabled()) {
		var halter = Graphene.Page.XHRHalter._associations[this];
		halter.saveXhrParams();
	} else {
		Graphene.Page.XHRHalter.XHRWrapperInjection.onreadystatechangeCallback.call(this);
	}
};

Graphene.XHRWrapper.prototype.open = function(method, url, asyncFlag, userName, password) {
	if (Graphene.Page.XHRHalter.isEnabled()) {
		var halter = Graphene.Page.XHRHalter._associations[this];
		if (halter === undefined) {
			halter = new Graphene.Page.XHRHalter(this);
		} else {
			Graphene.Page.XHRHalter.call(halter, this);
		}
		halter.wait();
	}
	asyncFlag = (asyncFlag !== false);
	return Graphene.Page.XHRHalter.XHRWrapperInjection.open.call(this, method, url, asyncFlag, userName, password);
};

Graphene.XHRWrapper.prototype.send = function(content) {
	if (Graphene.Page.XHRHalter.isEnabled()) {
		var halter = Graphene.Page.XHRHalter._associations[this];
		halter.sendParams['content'] = content;
		halter.saveXhrParams();
		halter.wait();
	} else {
		return Graphene.Page.XHRHalter.XHRWrapperInjection.send.call(this, content);
	}
};


