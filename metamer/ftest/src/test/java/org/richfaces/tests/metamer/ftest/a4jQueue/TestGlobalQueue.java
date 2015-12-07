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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.AjaxRequestHalter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter.HaltedRequest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestGlobalQueue extends AbstractWebDriverTest {

    private final Attributes<QueueAttributes> attributes = getAttributes();

    @FindBy(tagName = "body")
    private QueueFragment queue;

    private Integer requestDelay;
    private Integer[] requestDelays = { 500, 1500 };

    @Override
    public String getComponentTestPagePath() {
        return "a4jQueue/globalQueue.xhtml";
    }

    @Test
    @Skip(On.JSF.MyFaces.class)// https://issues.jboss.org/browse/RFPL-3998
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

        halter.completeFollowingRequests(1);
        queue.waitForChange("a", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "ab");
    }

    @Test
    @Skip(On.JSF.MyFaces.class)// https://issues.jboss.org/browse/RFPL-3998
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
        } catch (Exception ignored) {
            // expected timeout
        }

        assertEquals(queue.getRepeatedText(), "");

        halter.completeFollowingRequests(1);

        queue.waitForChange("", queue.getRepeatedTextElement());
        assertEquals(queue.getRepeatedText(), "cd");
    }

    /**
     * Events from one source should be stacked as occurs, while last event isn't delayed by configured requestDelay.
     */
    @Test
    public void testMultipleRequestsWithDelayStacking() {
        long delay = 2000;
        attributes.set(QueueAttributes.requestDelay, delay);

        Halter halter = AjaxRequestHalter.getHalter();

        queue.fireEvents(4);
        queue.waitAndCheckEventsCounts(4, 1, 0);

        halter.completeFollowingRequests(1);
        queue.waitAndCheckEventsCounts(4, 1, 1);

        queue.fireEvents(3);
        queue.waitAndCheckEventsCounts(7, 2, 1);

        halter.completeFollowingRequests(1);
        queue.waitAndCheckEventsCounts(7, 2, 2);
    }

    @Test
    public void testMultipleRequestsWithDelayTiming() {
        long delay = 2000;
        attributes.set(QueueAttributes.requestDelay, delay);
        queue.fireEvents(4);
        queue.waitForNumberOfDelaysEqualsTo(1);
        queue.checkLastDelay(delay);
        queue.checkMedian(delay);
    }

    /**
     * When no requestDelay (0) is set, events should fire request immediately.
     *
     * However, when one event is waiting in queue for processing of previous request, events should be stacked.
     */
    @Test
    @Skip(On.JSF.MyFaces.class)// https://issues.jboss.org/browse/RFPL-3998
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1194")
    public void testMultipleRequestsWithNoDelayStacking() {
        long delay = 0;
        attributes.set(QueueAttributes.requestDelay, delay);

        Halter halter = AjaxRequestHalter.getHalter();

        queue.fireEvents(1);
        queue.waitAndCheckEventsCounts(1, 1, 0);

        HaltedRequest req = halter.nextRequest().continueToPhaseAfter().opened();

        queue.fireEvents(1);
        queue.waitAndCheckEventsCounts(2, 1, 0);

        req.completeRequest();
        queue.waitAndCheckEventsCounts(2, 2, 1);

        req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.fireEvents(4);
        queue.waitAndCheckEventsCounts(6, 2, 1);

        req.completeRequest();
        queue.waitAndCheckEventsCounts(6, 3, 2);

        req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.fireEvents(1);
        queue.waitAndCheckEventsCounts(7, 3, 2);

        req.completeRequest();
        queue.waitAndCheckEventsCounts(7, 4, 3);

        req = halter.nextRequest().continueToPhaseAfter().opened();
        queue.waitAndCheckEventsCounts(7, 4, 3);

        req.completeRequest();
        queue.waitAndCheckEventsCounts(7, 4, 4);
    }

    @Test
    public void testMultipleRequestsWithNoDelayTiming() {
        long delay = 0;
        attributes.set(QueueAttributes.requestDelay, delay);
        queue.fireEvents(2);
        queue.waitAndCheckEventsCounts(2, 2, 2);
        queue.waitForNumberOfDelaysEqualsTo(2);
        queue.checkLastDelay(0);
        queue.checkMedian(0);
    }

    @Test
    @CoversAttributes("rendered")
    @RegressionTest("https://issues.jboss.org/browse/RF-9328")
    public void testRendered() {
        attsSetter()
            .setAttribute(QueueAttributes.requestDelay).toValue(2000)
            .setAttribute(QueueAttributes.onrequestqueue).toValue("alert('requestQueued')")
            .setAttribute(QueueAttributes.onrequestdequeue).toValue("alert('requestDequeued')")
            .setAttribute(QueueAttributes.rendered).toValue(false)
            .asSingleAction().perform();

        Graphene.guardAjax(queue).fireEvents(1);

        // check that no requestDelay is applied while renderer=false
        queue.checkLastDelay(0);
        try {
            Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).until(ExpectedConditions.alertIsPresent());
            fail("No alert should be present!");
        } catch (Exception e) {
            //ok
        }
    }

    /**
     * Tests delay between time last event occurs and time when event triggers request (begin).
     */
    @Test
    @Unstable
    @CoversAttributes("requestDelay")
    @UseWithField(field = "requestDelay", valuesFrom = FROM_FIELD, value = "requestDelays")
    public void testRequestDelay() {
        attributes.set(QueueAttributes.requestDelay, requestDelay);
        final int numberOfChecks = 5;

        for (int i = 0; i < numberOfChecks; i++) {
            Graphene.guardAjax(queue).fireEvents(1);
        }
        queue.waitAndCheckEventsCounts(5, 5, 5);
        queue.waitForNumberOfDelaysEqualsTo(numberOfChecks);
        queue.checkMedian(requestDelay);
    }
}
