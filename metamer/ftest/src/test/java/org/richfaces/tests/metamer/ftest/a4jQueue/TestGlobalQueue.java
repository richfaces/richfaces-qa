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
package org.richfaces.tests.metamer.ftest.a4jQueue;


import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes.ignoreDupResponses;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes.rendered;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

import org.jboss.arquillian.graphene.page.interception.AjaxHalter;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

import static org.jboss.arquillian.graphene.halter.AjaxState.OPENED;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.fail;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestGlobalQueue extends AbstractWebDriverTest {

    @FindBy(tagName = "body")
    private QueueFragment queue;

    private final Attributes<QueueAttributes> attributes = getAttributes();

    Integer requestDelay;
    Integer[] requestDelays = {3000, 1200};

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jQueue/globalQueue.xhtml");
    }

    /**
     * Tests delay between time last event occurs and time when event triggers
     * request (begin).
     */
    @Test
    @UseWithField(field = "requestDelay", valuesFrom = FROM_FIELD, value = "requestDelays")
    public void testRequestDelay() {
        attributes.set(QueueAttributes.requestDelay, requestDelay);

        queue.initializeTimes();

        for (int i = 0; i < 5; i++) {
            queue.fireEvent(1);
            queue.checkTimes(requestDelay);
        }

        queue.checkDeviationMedian(requestDelay);
    }

    /**
     * Events from one source should be stacked as occurs, while last event
     * isn't delayed by configured requestDelay.
     */
    @Test
    public void testMultipleRequestsWithDelay() {
        attributes.set(QueueAttributes.requestDelay, 3000);

        AjaxHalter.enable();

        queue.fireEvent(4);
        AjaxHalter handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        handle.complete();

        queue.checkCounts(4, 1, 1);

        queue.fireEvent(3);
        handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        handle.complete();

        queue.checkCounts(7, 2, 2);

        AjaxHalter.disable();
    }

    /**
     * <p> When no requestDelay (0) is set, events should fire request
     * immediately. </p>
     *
     * <p> However, when one event is waiting in queue for processing of
     * previous request, events should be stacked. </p>
     */
    @Test
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1194")
    public void testMultipleRequestsWithNoDelay() {
        attributes.set(QueueAttributes.requestDelay, 0);

        AjaxHalter.enable();

        queue.fireEvent(1);
        queue.checkCounts(1, 1, 0);

        AjaxHalter handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);

        queue.fireEvent(1);
        queue.checkCounts(2, 1, 0);

        handle.complete();
        queue.checkCounts(2, 2, 1);

        handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        queue.fireEvent(4);
        queue.checkCounts(6, 2, 1);

        handle.complete();
        queue.checkCounts(6, 3, 2);

        handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        queue.fireEvent(1);
        queue.checkCounts(7, 3, 2);

        handle.complete();
        queue.checkCounts(7, 4, 3);

        handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        queue.checkCounts(7, 4, 3);

        handle.complete();
        queue.checkCounts(7, 4, 4);

        AjaxHalter.disable();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9328")
    public void testRendered() {
        attributes.set(QueueAttributes.requestDelay, 1500);
        attributes.set(rendered, false);

        queue.initializeTimes();
        queue.fireEvent(1);

        // check that no requestDelay is applied while renderer=false
        queue.checkTimes(0);
        // TODO should check that no attributes is applied with renderes=false
    }

    @Test
    public void testIgnoreDuplicatedResponsesFalse() throws InterruptedException {
        attributes.set(ignoreDupResponses, false);

        AjaxHalter.enable();
        queue.type("a");
        AjaxHalter handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        queue.type("b");
        handle.complete();
        queue.waitForChange("", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "a");
        handle = AjaxHalter.getHandleBlocking();
        handle.complete();
        queue.waitForChange("a", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "ab");
    }

    @Test
    public void testIgnoreDuplicatedResponsesTrue() {
        attributes.set(ignoreDupResponses, true);

        AjaxHalter.enable();
        queue.type("c");
        AjaxHalter handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        queue.type("d");
        handle.complete();

        try {
            queue.waitForChange("", queue.getRepeatedTextElement());
            fail("should timeout here!");
        } catch(TimeoutException ex) {
            //OK
        }

        assertEquals(queue.getRepeatedText(), "");

        handle = AjaxHalter.getHandleBlocking();
        handle.complete();

        queue.waitForChange("", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "cd");
    }
}
