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
package org.richfaces.tests.metamer.ftest.a4jQueue;

import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes.ignoreDupResponses;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.AjaxRequestHalter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter.HaltedRequest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestGlobalQueue extends AbstractWebDriverTest {

    private final Attributes<QueueAttributes> attributes = getAttributes();

    @FindBy(tagName = "body")
    private QueueFragment queue;

    private Integer requestDelay;
    private Integer[] requestDelays = { 1900, 1000 };

    @Override
    public String getComponentTestPagePath() {
        return "a4jQueue/globalQueue.xhtml";
    }

    @Test
    @CoversAttributes("ignoreDupResponses")
    public void testIgnoreDuplicatedResponsesFalse() throws InterruptedException {
        attributes.set(ignoreDupResponses, false);

        Halter halter = AjaxRequestHalter.getHalter();
        queue.type("a");
        HaltedRequest req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.type("b");
        req.completeRequest();

        queue.waitForChange("", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "a");

        halter.nextRequest().completeRequest();
        queue.waitForChange("a", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "ab");
    }

    @Test
    @CoversAttributes("ignoreDupResponses")
    public void testIgnoreDuplicatedResponsesTrue() {
        attributes.set(ignoreDupResponses, true);

        Halter halter = AjaxRequestHalter.getHalter();
        queue.type("c");
        HaltedRequest req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.type("d");
        req.completeRequest();

        try {
            queue.waitForChange("", queue.getRepeatedTextElement());
            fail("should timeout here!");
        } catch (TimeoutException ex) {
            //OK
        }

        assertEquals(queue.getRepeatedText(), "");

        halter.nextRequest().completeRequest();

        queue.waitForChange("", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "cd");
    }

    /**
     * Events from one source should be stacked as occurs, while last event
     * isn't delayed by configured requestDelay.
     */
    @Test
    public void testMultipleRequestsWithDelay() {
        attributes.set(QueueAttributes.requestDelay, 3000);

        Halter halter = AjaxRequestHalter.getHalter();

        queue.fireEvent(4);
        halter.nextRequest().completeRequest();

        queue.checkCounts(4, 1, 1);

        queue.fireEvent(3);
        halter.nextRequest().completeRequest();

        queue.checkCounts(7, 2, 2);
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

        Halter halter = AjaxRequestHalter.getHalter();

        queue.fireEvent(1);
        queue.checkCounts(1, 1, 0);

        HaltedRequest req = halter.nextRequest().continueToPhaseAfter().opened();

        queue.fireEvent(1);
        queue.checkCounts(2, 1, 0);

        req.completeRequest();
        queue.checkCounts(2, 2, 1);

        req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.fireEvent(4);
        queue.checkCounts(6, 2, 1);

        req.completeRequest();
        queue.checkCounts(6, 3, 2);

        req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.fireEvent(1);
        queue.checkCounts(7, 3, 2);

        req.completeRequest();
        queue.checkCounts(7, 4, 3);

        req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.checkCounts(7, 4, 3);

        req.completeRequest();
        queue.checkCounts(7, 4, 4);
    }

    @Test
    @CoversAttributes("rendered")
    @RegressionTest("https://issues.jboss.org/browse/RF-9328")
    public void testRendered() {
        attributes.set(QueueAttributes.requestDelay, 2000);
        attributes.set(QueueAttributes.onrequestqueue, "alert('requestQueued')");
        attributes.set(QueueAttributes.onrequestdequeue, "alert('requestDequeued')");
        attributes.set(QueueAttributes.rendered, false);

        queue.initializeTimes();
        Graphene.guardAjax(queue).fireEvent(1);

        // check that no requestDelay is applied while renderer=false
        queue.checkDeviation(Input.FIRST, 0);
        try {
            Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).until(ExpectedConditions.alertIsPresent());
            fail("No alert should be present!");
        } catch (Exception e) {
            //ok
        }
    }

    /**
     * Tests delay between time last event occurs and time when event triggers
     * request (begin).
     */
    @Test
    @CoversAttributes("requestDelay")
    @UseWithField(field = "requestDelay", valuesFrom = FROM_FIELD, value = "requestDelays")
    public void testRequestDelay() {
        attributes.set(QueueAttributes.requestDelay, requestDelay);

        queue.initializeTimes();

        for (int i = 0; i < 5; i++) {
            Graphene.guardAjax(queue).fireEvent(1);
            queue.checkDeviation(Input.FIRST, requestDelay);
        }

        queue.checkDeviationMedian(requestDelay);
    }
}
