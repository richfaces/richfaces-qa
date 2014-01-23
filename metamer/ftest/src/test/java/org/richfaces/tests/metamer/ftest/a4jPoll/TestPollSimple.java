/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestPollSimple extends AbstractWebDriverTest {

    private static final int INTERVAL = 2500;// ms

    private final Attributes<PollAttributes> pollAttributes = getAttributes();

    @Page
    private MetamerPage metamerPage;

    private Integer interval;
    private Integer[] ints = { 1500, 2500 };

    private String event;
    private String[] events = new String[]{ "timer", "begin", "beforedomupdate", "complete" };

    private final Action intervalTestingAction = new Action() {
        private String value = "";

        @Override
        public void perform() {
            Graphene.waitModel().until().element(metamerPage.getRequestTimeElement()).text().not().equalTo(value);
            value = metamerPage.getRequestTimeElement().getText();
        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPoll/simple.xhtml");
    }

    @BeforeMethod
    public void enablePoll() {
        pollAttributes.set(PollAttributes.interval, INTERVAL);
        pollAttributes.set(PollAttributes.enabled, true);
    }

    @Test
    public void testAction() {
        waitForNSubsequentRequests(2);
        metamerPage.assertListener(INVOKE_APPLICATION, "action invoked");
        metamerPage.assertPhases(ANY_PHASE);
    }

    @Test
    public void testActionListener() {
        waitForNSubsequentRequests(2);
        metamerPage.assertListener(INVOKE_APPLICATION, "action listener invoked");
        metamerPage.assertPhases(ANY_PHASE);
    }

    @Test
    public void testBypassUpdates() {
        pollAttributes.set(PollAttributes.bypassUpdates, true);
        waitForNSubsequentRequests(2);
        metamerPage.assertListener(PROCESS_VALIDATIONS, "action listener");
        metamerPage.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, RENDER_RESPONSE);
    }

    @Test
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "events")
    public void testClientSideEvent() {
        testClientSideEventHandlers(event);
    }

    private void testClientSideEventHandlers(String... events) {
        pollAttributes.set(PollAttributes.enabled, false);
        super.testRequestEventsBefore(events);
        pollAttributes.set(PollAttributes.enabled, true);
        super.testRequestEventsAfter(events);
    }

    @Test
    public void testClientSideEventsOrder() {
        testClientSideEventHandlers(events);
    }

    @Test
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                waitForNSubsequentRequests(1);
            }
        });
    }

    @Test(groups = "smoke")
    public void testEnabled() {
        pollAttributes.set(PollAttributes.enabled, false);

        String time = metamerPage.getRequestTimeElement().getText();
        waiting(2 * INTERVAL);
        assertTrue(time.equals(metamerPage.getRequestTimeElement().getText()));

        pollAttributes.set(PollAttributes.enabled, true);
        waitForNSubsequentRequests(2);
    }

    @Test
    public void testExecute() {
        pollAttributes.set(PollAttributes.execute, "executeChecker");

        waitForNSubsequentRequests(2);

        metamerPage.assertListener(UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test
    public void testImmediate() {
        pollAttributes.set(PollAttributes.immediate, true);
        waitForNSubsequentRequests(2);
        metamerPage.assertListener(APPLY_REQUEST_VALUES, "action invoked");
        metamerPage.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test
    public void testInterval_inAllTemplates() {
        testDelay(intervalTestingAction, intervalTestingAction, "interval", 1500);
    }

    @Test
    @UseWithField(field = "interval", valuesFrom = FROM_FIELD, value = "ints")
    @Templates("plain")
    public void testIntervals() {
        testDelay(intervalTestingAction, intervalTestingAction, "interval", interval);
    }

    @Test
    public void testLimitRender() {
        pollAttributes.set(PollAttributes.limitRender, true);
        pollAttributes.set(PollAttributes.render, "renderChecker");
        String render = metamerPage.getRenderCheckerOutputElement().getText();
        String time = metamerPage.getRequestTimeElement().getText();
        Graphene.waitModel()
            .until()
            .element(metamerPage.getRenderCheckerOutputElement())
            .text().not().equalTo(render);
        assertTrue(time.equals(metamerPage.getRequestTimeElement().getText()));
    }

    @Test
    public void testRender() {
        pollAttributes.set(PollAttributes.render, "renderChecker");
        String render = metamerPage.getRenderCheckerOutputElement().getText();
        String time = metamerPage.getRequestTimeElement().getText();

        Graphene.waitModel()
            .until()
            .element(metamerPage.getRenderCheckerOutputElement())
            .text().not().equalTo(render);
        assertFalse(time.equals(metamerPage.getRequestTimeElement().getText()));
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        pollAttributes.set(PollAttributes.enabled, true);
        pollAttributes.set(PollAttributes.rendered, false);

        String time = metamerPage.getRequestTimeElement().getText();
        waiting(2 * INTERVAL);
        assertTrue(time.equals(metamerPage.getRequestTimeElement().getText()));

        pollAttributes.set(PollAttributes.rendered, true);
        waitForNSubsequentRequests(2);
    }

    @Test
    public void testStatus() {
        testStatus(null);
    }

    private void waitForNSubsequentRequests(int n) {
        for (int i = 0; i < n; i++) {
            String time = metamerPage.getRequestTimeElement().getText();
            Graphene.waitModel()
                .until()
                .element(metamerPage.getRequestTimeElement())
                .text().not().equalTo(time);
        }
    }

    @JavaScript("window")
    public interface Window {

        String getData();
    }
}
