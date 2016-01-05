/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.extension.ajaxhalter;

import org.jboss.arquillian.graphene.halter.AjaxState;
import org.jboss.arquillian.graphene.page.interception.AjaxHalter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter.HaltedRequest;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter.WaitForRequestPhase;

/**
 * Wraps Graphene's AjaxHalter in redesigned API.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AjaxRequestHalter implements Halter, HaltedRequest, WaitForRequestPhase {

    private AjaxHalter halter;
    private boolean continueToPhaseAfter;

    private AjaxRequestHalter() {
        AjaxHalter.enable();
    }

    public static Halter getHalter() {
        return new AjaxRequestHalter();
    }

    @Override
    public Halter completeFollowingRequests(int requestsCount) {
        if (requestsCount <= 0) {
            throw new IllegalArgumentException("The requestsCount has to be greater than 0");
        }

        for (int i = 0; i < requestsCount; i++) {
            nextRequest().completeRequest();
        }
        return this;
    }

    @Override
    public Halter completeRequest() {
        return continueToPhaseAfter().done();
    }

    @Override
    public WaitForRequestPhase continueToPhaseAfter() {
        continueToPhaseAfter = true;
        return this;
    }

    @Override
    public WaitForRequestPhase continueToPhaseBefore() {
        continueToPhaseAfter = false;
        return this;
    }

    private void continueToState(AjaxState state) {
        if (continueToPhaseAfter) {
            halter.continueAfter(state);
        } else {
            halter.continueBefore(state);
        }
    }

    @Override
    public Halter done() {
        continueToState(AjaxState.DONE);
        return this;
    }

    @Override
    public HaltedRequest headersReceived() {
        continueToState(AjaxState.HEADERS_RECEIVED);
        return this;
    }

    @Override
    public HaltedRequest loading() {
        continueToState(AjaxState.LOADING);
        return this;
    }

    @Override
    public HaltedRequest nextRequest() {
        halter = AjaxHalter.getHandleBlocking();
        return this;
    }

    @Override
    public HaltedRequest opened() {
        continueToState(AjaxState.OPENED);
        return this;
    }

    @Override
    public HaltedRequest sameRequest() {
        halter = AjaxHalter.getHandle();
        return this;
    }

    @Override
    public HaltedRequest unsent() {
        continueToState(AjaxState.UNSENT);
        return this;
    }
}
