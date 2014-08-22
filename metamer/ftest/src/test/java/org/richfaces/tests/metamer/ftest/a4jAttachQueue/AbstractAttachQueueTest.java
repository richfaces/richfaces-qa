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
package org.richfaces.tests.metamer.ftest.a4jAttachQueue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.interception.AjaxHalter;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;

import static org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes.onrequestdequeue;
import static org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes.onrequestqueue;
import static org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes.rendered;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input.FIRST;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input.SECOND;
import static org.testng.Assert.fail;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractAttachQueueTest extends AbstractWebDriverTest {

    protected static final Long GLOBAL_DELAY = 10000L;
    protected static final Long DELAY_A = 3000L;
    protected static final Long DELAY_B = 5000L;

    @FindBy(tagName = "body")
    private QueueFragment queue;

    @ArquillianResource
    private JavascriptExecutor jsExecutor;

    protected final Attributes<AttachQueueAttributes> attachQueueAttrs1
            = getAttributes("afterForm:attributes1");

    protected final Attributes<AttachQueueAttributes> attachQueueAttrs2
            = getAttributes("afterForm:attributes2");

    protected final Attributes<QueueAttributes> queueAttributes =
            getAttributes("afterForm:queueAttributes");

    protected final Attributes<AttachQueueAttributes> attachQueueAttributes =
            getAttributes();

    public QueueFragment getQueue() {
        return queue;
    }

    protected void testDelay() {
        queue.initializeTimes();
        queue.fireEvent(FIRST, 1);
        queue.checkTimes(FIRST, DELAY_A);
    }

    protected void testNoDelay() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 0);

        AjaxHalter.enable();

        queue.fireEvent(FIRST, 4);
        queue.checkCounts(4, 0, 1, 0);

        AjaxHalter halter = AjaxHalter.getHandleBlocking();
        queue.checkCounts(4, 0, 1, 0);

        halter.complete();
        queue.checkCounts(4, 0, 2, 1);

        halter = AjaxHalter.getHandleBlocking();
        queue.checkCounts(4, 0, 2, 1);

        halter.complete();
        queue.checkCounts(4, 0, 2, 2);
    }

    protected void testTimingOneQueueTwoEvents() {
        queue.initializeTimes();

        AjaxHalter.enable();

        queue.fireEvent(FIRST, 3);
        queue.fireEvent(SECOND, 1);

        AjaxHalter halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        halter = AjaxHalter.getHandleBlocking();
        halter.complete();

        queue.checkTimes(SECOND, DELAY_B);
        queue.checkNoDelayBetweenEvents();
    }

    protected void testRendered() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 1500);
        attachQueueAttrs1.set(onrequestqueue, "alert('requestQueued')");
        attachQueueAttrs1.set(onrequestdequeue, "alert('requestDequeued')");
        attachQueueAttrs1.set(rendered, false);

        queue.initializeTimes();
        queue.fireEvent(1);

        // check that no requestDelay is applied while renderer=false
        queue.checkTimes(0);
        try {
            Graphene.waitGui().until(ExpectedConditions.alertIsPresent());
            fail("No alert should be present!");
        } catch (Exception e) {
            //ok
        }
    }

    protected void testQueueAndDequeueEvents() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 0);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, 0);

        attachQueueAttrs1.set(AttachQueueAttributes.onrequestqueue, "metamerEvents += \"requestqueue \"");
        attachQueueAttrs1.set(AttachQueueAttributes.onrequestdequeue, "metamerEvents += \"requestdequeue \"");
        jsExecutor.executeScript("window.metamerEvents = \"\";");

        queue.fireEvent(FIRST, 1);

        String[] events = ((String) jsExecutor.executeScript("return window.metamerEvents")).split(" ");
        assertEquals(events.length, 2, "2 events should be fired, was [" + jsExecutor.executeScript("window.metamerEvents = \"\";") + "]");
        assertEquals(events[0], "requestqueue", "Attribute onrequestqueue doesn't work");
        assertEquals(events[1], "requestdequeue", "Attribute onrequestdequeue doesn't work");
    }
}
