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
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractAttachQueueTest extends AbstractWebDriverTest {

    protected static final Long DELAY_A = 1000L;
    protected static final Long DELAY_B = 2000L;
    protected static final Long GLOBAL_DELAY = 4000L;

    protected final Attributes<AttachQueueAttributes> attachQueueAttributes = getAttributes();
    protected final Attributes<AttachQueueAttributes> attachQueueAttrs1 = getAttributes("afterForm:attributes1");
    protected final Attributes<AttachQueueAttributes> attachQueueAttrs2 = getAttributes("afterForm:attributes2");

    @ArquillianResource
    private JavascriptExecutor jsExecutor;

    @FindBy(tagName = "body")
    private QueueFragment queue;

    protected final Attributes<QueueAttributes> queueAttributes = getAttributes("afterForm:queueAttributes");

    public QueueFragment getQueue() {
        return queue;
    }

    @BeforeMethod
    public void setupDelays() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, DELAY_A);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, DELAY_B);
        queueAttributes.set(QueueAttributes.requestDelay, GLOBAL_DELAY);
    }

    @CoversAttributes("requestDelay")
    protected void testNoRequestDelay() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 0);

        queue.fireEvent(FIRST, 2);
        queue.waitAndCheckEventsCounts(2, 2, 2);
        queue.waitForNumberOfDelaysEqualsTo(2);
        queue.checkLastDelay(0);
        queue.checkMedian(0);
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

    @CoversAttributes("requestDelay")
    protected void testRequestDelay() {
        // test first attachQueue
        queue.fireEvent(Input.FIRST, 1);
        queue.waitForNumberOfDelaysEqualsTo(1);
        queue.checkLastDelay(DELAY_A);
        queue.fireEvent(Input.FIRST, 1);
        queue.waitForNumberOfDelaysEqualsTo(2);
        queue.checkLastDelay(DELAY_A);
        queue.fireEvent(Input.FIRST, 1);
        queue.waitForNumberOfDelaysEqualsTo(3);
        queue.checkLastDelay(DELAY_A);
        queue.checkMedian(DELAY_A);

        queue.resetDelays();
        // test second attachQueue
        queue.fireEvent(Input.SECOND, 1);
        queue.waitForNumberOfDelaysEqualsTo(1);
        queue.checkLastDelay(DELAY_B);
        queue.fireEvent(Input.SECOND, 1);
        queue.waitForNumberOfDelaysEqualsTo(2);
        queue.checkLastDelay(DELAY_B);
        queue.fireEvent(Input.SECOND, 1);
        queue.waitForNumberOfDelaysEqualsTo(3);
        queue.checkLastDelay(DELAY_B);
        queue.checkMedian(DELAY_B);
    }

    /**
     * Tests request delays for 2 queues.
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     */
    protected void testTwoQueuesTwoEvents() {
        // fire an event on first input and immediatly an event on the second input
        getMultipleEventsFirerer()
            .addEvent(queue.getInput1(), 1, "keypress")
            .addEvent(queue.getInput2(), 1, "keypress")
            .perform();

        // this will lead to 2 requests
        queue.waitAndCheckEventsCounts(1, 1, 2, 2);

        // first one has no delay
        queue.checkDelayAtIndexIs(1, 0);
        // second one has specified delay
        queue.checkLastDelay(DELAY_B);
    }
}
