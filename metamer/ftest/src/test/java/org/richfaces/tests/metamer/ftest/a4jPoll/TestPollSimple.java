/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.a4jPoll;

import static javax.faces.event.PhaseId.ANY_PHASE;
import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.bypassUpdates;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.data;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.enabled;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.execute;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.immediate;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.interval;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.render;
import static org.richfaces.tests.metamer.ftest.a4jPoll.PollAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.pollAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22681 $
 */
public class TestPollSimple extends AbstractGrapheneTest {

    JQueryLocator outputCounter = pjq("span[id$=outputCounter]");

    @Inject
    @Use(empty = true)
    String event;
    String[] events = new String[] { "timer", "begin", "beforedomupdate", "complete" };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPoll/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("A4J", "A4J Poll", "Simple");
    }

    @BeforeMethod
    public void enablePoll() {
        pollAttributes.set(interval, 2500);
        pollAttributes.set(enabled, true);

    }

    @Test
    public void testAction() {
        waitForTwoSubsequentRequests();
        phaseInfo.assertListener(INVOKE_APPLICATION, "action invoked");
        phaseInfo.assertPhases(ANY_PHASE);
    }

    @Test
    public void testActionListener() {
        waitForTwoSubsequentRequests();
        phaseInfo.assertListener(INVOKE_APPLICATION, "action listener invoked");
        phaseInfo.assertPhases(ANY_PHASE);
    }

    @Test
    public void testBypassUpdates() {
        pollAttributes.set(bypassUpdates, true);
        waitForTwoSubsequentRequests();
        phaseInfo.assertListener(PROCESS_VALIDATIONS, "action invoked");
        phaseInfo.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, RENDER_RESPONSE);
    }

    @Test
    public void testData() {
        pollAttributes.set(data, "RichFaces 4");
        pollAttributes.set(oncomplete, "data = event.data");

        retrieveRequestTime.initializeValue();
        waitGui.waitForChange(retrieveRequestTime);

        assertEquals(retrieveWindowData.retrieve(), "RichFaces 4");
    }

    @Test
    public void testImmediate() {
        pollAttributes.set(immediate, true);
        waitForTwoSubsequentRequests();
        phaseInfo.assertListener(APPLY_REQUEST_VALUES, "action invoked");
        phaseInfo.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test
    @Use(field = "event", value = "events")
    public void testClientSideEvent() {
        testClientSideEventHandlers(event);
    }

    @Test
    public void testClientSideEventsOrder() {
        testClientSideEventHandlers(events);
    }

    @Test
    public void testRendered() {
        pollAttributes.set(enabled, true);
        pollAttributes.set(rendered, false);

        retrieveRequestTime.initializeValue();
        waitModel.timeout(5000).interval(1000).waitForTimeout();
        assertFalse(retrieveRequestTime.isValueChanged());

        pollAttributes.set(rendered, true);
        waitForTwoSubsequentRequests();
    }

    @Test
    public void testExecute() {
        pollAttributes.set(execute, "executeChecker");

        waitForTwoSubsequentRequests();

        phaseInfo.assertListener(UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test
    public void testEnabled() {
        pollAttributes.set(enabled, false);

        retrieveRequestTime.initializeValue();
        waitModel.timeout(5000).interval(1000).waitForTimeout();
        assertFalse(retrieveRequestTime.isValueChanged());

        pollAttributes.set(enabled, true);
        waitForTwoSubsequentRequests();
    }

    @Test
    public void testRender() {
        pollAttributes.set(render, "renderChecker");
        retrieveRenderChecker.initializeValue();
        retrieveRequestTime.initializeValue();
        waitAjax.waitForChange(retrieveRenderChecker);
        assertTrue(retrieveRequestTime.isValueChanged());
    }

    @Test
    public void testLimitRender() {
        pollAttributes.set(limitRender, true);
        pollAttributes.set(render, "renderChecker");
        retrieveRenderChecker.initializeValue();
        retrieveRequestTime.initializeValue();
        waitGui.waitForChange(retrieveRenderChecker);
        assertFalse(retrieveRequestTime.isValueChanged());
    }

    private void testClientSideEventHandlers(String... events) {
        pollAttributes.set(enabled, false);
        super.testRequestEventsBefore(events);
        pollAttributes.set(enabled, true);
        retrieveRequestTime.initializeValue();
        waitAjax.waitForChange(retrieveRequestTime);

        super.testRequestEventsAfter(events);
    }

    private void waitForTwoSubsequentRequests() {
        retrieveRequestTime.initializeValue();

        waitAjax.waitForChange(retrieveRequestTime);
        waitAjax.waitForChange(retrieveRequestTime);
    }
}
