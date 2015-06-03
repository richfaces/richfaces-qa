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
package org.richfaces.tests.metamer.ftest.a4jAttachQueue;

import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input.FIRST;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input.SECOND;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.AjaxRequestHalter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractAttachQueueTest extends AbstractWebDriverTest {

    protected static final Long DELAY_A = 1000L;
    protected static final Long DELAY_B = 2000L;
    protected static final Long GLOBAL_DELAY = 4000L;

    protected final Attributes<AttachQueueAttributes> attachQueueAttributes = getAttributes();
    protected final Attributes<AttachQueueAttributes> attachQueueAttrs1 = getAttributes("afterForm:attributes1");
    protected final Attributes<AttachQueueAttributes> attachQueueAttrs2 = getAttributes("afterForm:attributes2");
    protected final Attributes<QueueAttributes> queueAttributes = getAttributes("afterForm:queueAttributes");

    @ArquillianResource
    private JavascriptExecutor jsExecutor;
    @FindBy(tagName = "body")
    private QueueFragment queue;

    public QueueFragment getQueue() {
        return queue;
    }

    protected void testDelay() {
        queue.initializeTimes();
        queue.fireEvent(FIRST, 1);
        queue.waitForTimeChangeAndCheckDeviation(FIRST, DELAY_A);
    }

    protected void testNoDelay() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 0);

        Halter halter = AjaxRequestHalter.getHalter();

        queue.fireEvent(FIRST, 4);
        queue.checkCounts(4, 0, 1, 0);
        halter.completeFollowingRequests(1);
        queue.checkCounts(4, 0, 2, 1);
        halter.completeFollowingRequests(1);
        queue.checkCounts(4, 0, 2, 2);
    }

    @CoversAttributes({ "onrequestqueue", "onrequestdequeue" })
    protected void testQueueAndDequeueEvents() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 0);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, 0);

        attachQueueAttrs1.set(AttachQueueAttributes.onrequestqueue, "metamerEvents += \"requestqueue \"");
        attachQueueAttrs1.set(AttachQueueAttributes.onrequestdequeue, "metamerEvents += \"requestdequeue \"");
        jsExecutor.executeScript("window.metamerEvents = \"\";");

        Graphene.guardAjax(queue).fireEvent(FIRST, 1);
        String[] events = ((String) jsExecutor.executeScript("return window.metamerEvents")).split(" ");
        assertEquals(events.length, 2, "2 events should be fired, was [" + jsExecutor.executeScript("window.metamerEvents = \"\";") + "]");
        assertEquals(events[0], "requestqueue", "Attribute onrequestqueue doesn't work");
        assertEquals(events[1], "requestdequeue", "Attribute onrequestdequeue doesn't work");
    }

    @CoversAttributes("rendered")
    protected void testRendered() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 2000);
        attachQueueAttrs1.set(AttachQueueAttributes.onrequestqueue, "alert('requestQueued')");
        attachQueueAttrs1.set(AttachQueueAttributes.onrequestdequeue, "alert('requestDequeued')");
        attachQueueAttrs1.set(AttachQueueAttributes.rendered, false);

        queue.initializeTimes();
        Graphene.guardAjax(queue).fireEvent(1);

        // check that no requestDelay is applied while renderer=false
        queue.checkDeviation(QueueFragment.Input.FIRST, 0);
        try {
            Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).until(ExpectedConditions.alertIsPresent());
            fail("No alert should be present!");
        } catch (Exception e) {
            //ok
        }
    }

    protected void testTimingBetweenTwoQueues() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, DELAY_A);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, DELAY_B);
        queue.initializeTimes();

        Halter halter = AjaxRequestHalter.getHalter();

        // fire some events on first input and immediatly some events on the second input
        // this will lead to 2 requests
        queue.fireEvent(FIRST, 3);
        queue.fireEvent(SECOND, 1);

        // complete both requests
        halter.completeFollowingRequests(2);
        // first one with no delay
        queue.checkNoDelayBetweenEvents();
        // second one with specified delay
        queue.checkDeviation(SECOND, DELAY_B);
    }
}
