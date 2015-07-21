/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jPoll;

import static javax.faces.event.PhaseId.ANY_PHASE;
import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.status.Status.StatusState;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestPollSimple extends AbstractWebDriverTest {

    private static final int INTERVAL = 2500;// ms

    private final Attributes<PollAttributes> pollAttributes = getAttributes();

    private Integer interval;
    private Integer[] ints = { 1500, 2500 };

    private String event;
    private String[] events = new String[] { "timer", "begin", "beforedomupdate", "complete" };

    private final Action intervalTestingAction = new Action() {
        private String value = "";

        @Override
        public void perform() {
            Graphene.waitModel().until().element(getRequestTimeElement()).text().not().equalTo(value);
            value = getRequestTimeElement().getText();
        }
    };

    private WebElement getRequestTimeElement() {
        return getMetamerPage().getRequestTimeElement();
    }

    @Override
    public String getComponentTestPagePath() {
        return "a4jPoll/simple.xhtml";
    }

    @BeforeMethod
    @CoversAttributes({ "interval", "enabled" })
    public void enablePoll() {
        pollAttributes.set(PollAttributes.interval, INTERVAL);
        pollAttributes.set(PollAttributes.enabled, true);
    }

    @Test
    @CoversAttributes("action")
    public void testAction() {
        waitForNSubsequentRequests(2);
        getMetamerPage().assertListener(INVOKE_APPLICATION, "action invoked");
        getMetamerPage().assertPhases(ANY_PHASE);
    }

    @Test
    @CoversAttributes("actionListener")
    public void testActionListener() {
        waitForNSubsequentRequests(2);
        getMetamerPage().assertListener(INVOKE_APPLICATION, "action listener invoked");
        getMetamerPage().assertPhases(ANY_PHASE);
    }

    @Test
    @CoversAttributes("bypassUpdates")
    public void testBypassUpdates() {
        pollAttributes.set(PollAttributes.bypassUpdates, true);
        waitForNSubsequentRequests(2);
        getMetamerPage().assertListener(PROCESS_VALIDATIONS, "action listener");
        getMetamerPage().assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, RENDER_RESPONSE);
    }

    @Test
    @CoversAttributes({ "ontimer", "onbegin", "onbeforedomupdate", "oncomplete" })
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "events")
    public void testClientSideEvent() {
        testClientSideEventHandlers(event);
    }

    private void testClientSideEventHandlers(String... events) {
        pollAttributes.set(PollAttributes.enabled, false);
        testRequestEventsBefore(events);
        pollAttributes.set(PollAttributes.enabled, true);
        testRequestEventsAfter(events);
    }

    @Test
    @CoversAttributes({ "ontimer", "onbegin", "onbeforedomupdate", "oncomplete" })
    public void testClientSideEventsOrder() {
        testClientSideEventHandlers(events);
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                waitForNSubsequentRequests(1);
            }
        });
    }

    @Test(groups = "smoke")
    @CoversAttributes("enabled")
    public void testEnabled() {
        pollAttributes.set(PollAttributes.enabled, false);

        String time = getRequestTimeElement().getText();
        waiting(2 * INTERVAL);
        assertTrue(time.equals(getRequestTimeElement().getText()));

        pollAttributes.set(PollAttributes.enabled, true);
        waitForNSubsequentRequests(2);
    }

    @Test
    @CoversAttributes("execute")
    public void testExecute() {
        pollAttributes.set(PollAttributes.execute, "executeChecker");

        waitForNSubsequentRequests(2);

        getMetamerPage().assertListener(UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        pollAttributes.set(PollAttributes.immediate, true);
        waitForNSubsequentRequests(2);
        getMetamerPage().assertListener(APPLY_REQUEST_VALUES, "action invoked");
        getMetamerPage().assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test
    @CoversAttributes("interval")
    public void testInterval_inAllTemplates() {
        testDelay(intervalTestingAction, intervalTestingAction, "interval", 1500);
    }

    @Test
    @CoversAttributes("interval")
    @UseWithField(field = "interval", valuesFrom = FROM_FIELD, value = "ints")
    @Templates("plain")
    public void testIntervals() {
        testDelay(intervalTestingAction, intervalTestingAction, "interval", interval);
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        pollAttributes.set(PollAttributes.limitRender, true);
        pollAttributes.set(PollAttributes.render, "renderChecker");
        String render = getMetamerPage().getRenderCheckerOutputElement().getText();
        String time = getRequestTimeElement().getText();
        Graphene.waitModel()
            .until()
            .element(getMetamerPage().getRenderCheckerOutputElement())
            .text().not().equalTo(render);
        assertTrue(time.equals(getRequestTimeElement().getText()));
    }

    @Test
    @CoversAttributes("onerror")
    public void testOnerror() {
        pollAttributes.set(PollAttributes.enabled, false);
        pollAttributes.set(PollAttributes.action, "causeAjaxErrorAction");
        testFireEvent("onerror", new Action() {
            @Override
            public void perform() {
                pollAttributes.set(PollAttributes.enabled, true);
                getMetamerPage().getStatus().advanced().waitUntilStatusStateChanges(StatusState.ERROR).withTimeout(3, TimeUnit.SECONDS).perform();
            }
        });
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        pollAttributes.set(PollAttributes.render, "renderChecker");
        String render = getMetamerPage().getRenderCheckerOutputElement().getText();
        String time = getRequestTimeElement().getText();

        Graphene.waitModel()
            .until()
            .element(getMetamerPage().getRenderCheckerOutputElement())
            .text().not().equalTo(render);
        assertFalse(time.equals(getRequestTimeElement().getText()));
    }

    @Test
    @CoversAttributes({ "rendered", "enabled" })
    @Templates("plain")
    public void testRendered() {
        pollAttributes.set(PollAttributes.enabled, true);
        pollAttributes.set(PollAttributes.rendered, false);

        String time = getRequestTimeElement().getText();
        waiting(2 * INTERVAL);
        assertTrue(time.equals(getRequestTimeElement().getText()));

        pollAttributes.set(PollAttributes.rendered, true);
        waitForNSubsequentRequests(2);
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        testStatus(null);
    }

    private void waitForNSubsequentRequests(int n) {
        for (int i = 0; i < n; i++) {
            String time = getRequestTimeElement().getText();
            Graphene.waitModel()
                .until()
                .element(getRequestTimeElement())
                .text().not().equalTo(time);
        }
    }

    @JavaScript("window")
    public interface Window {

        String getData();
    }
}
