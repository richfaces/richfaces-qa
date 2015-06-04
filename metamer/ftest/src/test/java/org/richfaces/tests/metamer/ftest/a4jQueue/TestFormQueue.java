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

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.AjaxRequestHalter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
// https://issues.jboss.org/browse/RF-9430
@Templates("plain")
public class TestFormQueue extends AbstractWebDriverTest {

    private static final Long DELAY_A = 1000L;
    private static final Long DELAY_B = 2000L;
    private static final Long GLOBAL_DELAY = 4000L;
    private static final String KEYPRESS = "keypress";

    private final Attributes<QueueAttributes> attributesGlobalQueue = getAttributes("attributeForm:globalQueueAttributes");
    private final Attributes<QueueAttributes> attributesQueueA = getAttributes("attributeForm:formQueue1Attributes");
    private final Attributes<QueueAttributes> attributesQueueB = getAttributes("attributeForm:formQueue2Attributes");

    @FindBy(css = "div.rf-p[id$=formQueue1Panel]")
    private QueueFragment queueA;
    @FindBy(css = "div.rf-p[id$=formQueue2Panel]")
    private QueueFragment queueB;

    @Override
    public String getComponentTestPagePath() {
        return "a4jQueue/formQueue.xhtml";
    }

    /**
     * Tests the event handlers producing count of events (count of events, requests and updates).
     * Uses one form queue, which controls events from two distinct event sources.
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     */
    @Test
    public void testCountsOneQueueTwoEvents() {
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        Halter halter = AjaxRequestHalter.getHalter();

        queueA.fireEvent(Input.FIRST, 2);
        queueA.waitAndCheckEventsCounts(2, 0, 0, 0);
        queueA.fireEvent(Input.SECOND, 3);
        queueA.waitAndCheckEventsCounts(2, 3, 1, 0);

        queueA.waitAndCheckEventsCounts(2, 3, 1, 0);
        halter.completeFollowingRequests(1);
        queueA.waitAndCheckEventsCounts(2, 3, 2, 1);
        halter.completeFollowingRequests(1);
        queueA.waitAndCheckEventsCounts(2, 3, 2, 2);
    }

    /**
     * Tests the event handlers producing count of events (count of events, requests and updates).
     * Uses two form queues, controlling events from four distinct event sources (two for each queue).
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     */
    @Test
    public void testCountsTwoQueuesThreeEvents() {
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        attributesQueueB.set(QueueAttributes.requestDelay, DELAY_B);
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        Halter halter = AjaxRequestHalter.getHalter();

        queueA.fireEvent(Input.FIRST, 1);
        queueA.waitAndCheckEventsCounts(1, 0, 0, 0);
        queueA.fireEvent(Input.SECOND, 1);
        queueA.waitAndCheckEventsCounts(1, 1, 1, 0);
        queueB.fireEvent(Input.FIRST, 1);
        queueB.waitAndCheckEventsCounts(1, 0, 0, 0);
        queueB.fireEvent(Input.SECOND, 1);

        queueA.waitAndCheckEventsCounts(1, 1, 1, 0);
        queueB.waitAndCheckEventsCounts(1, 1, 0, 0);

        halter.completeFollowingRequests(1);
        queueA.waitAndCheckEventsCounts(1, 1, 2, 1);

        halter.completeFollowingRequests(1);
        queueA.waitAndCheckEventsCounts(1, 1, 2, 2);
        queueB.waitAndCheckEventsCounts(1, 1, 1, 0);

        halter.completeFollowingRequests(1);
        queueB.waitAndCheckEventsCounts(1, 1, 2, 1);

        halter.completeFollowingRequests(1);
        queueA.waitAndCheckEventsCounts(1, 1, 2, 2);
        queueB.waitAndCheckEventsCounts(1, 1, 2, 2);
    }

    /**
     * Tests request delays for one form queue.
     * Uses one form queue, which controls events from two distinct event sources.
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     */
    @Test
    public void testTimingOneQueueTwoEvents() {
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        getMultipleEventsFirerer()
            .addEvent(queueA.getInput1(), 1, KEYPRESS)
            .addEvent(queueA.getInput2(), 1, KEYPRESS)
            .perform();

        queueA.waitForNumberOfDelaysEqualsTo(2);
        queueA.checkDelayAtIndexIs(1, 0);
        queueA.checkLastDelay(DELAY_A);
    }

    /**
     * Tests request delays for two form queues.
     * Uses two form queues, controlling events from four distinct event sources (two for each queue).
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     */
    @Test
    public void testTimingTwoQueuesFourEvents() {
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        attributesQueueB.set(QueueAttributes.requestDelay, DELAY_B);
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        getMultipleEventsFirerer()
            .addEvent(queueA.getInput1(), 1, KEYPRESS)
            .addEvent(queueA.getInput2(), 1, KEYPRESS)
            .addEvent(queueB.getInput1(), 1, KEYPRESS)
            .addEvent(queueB.getInput2(), 1, KEYPRESS)
            .perform();

        queueA.waitForNumberOfDelaysEqualsTo(2);
        queueA.checkDelayAtIndexIs(0, 0);
        queueA.checkDelayAtIndexIs(1, 0);
        queueA.checkMedian(0);

        queueB.waitForNumberOfDelaysEqualsTo(2);
        queueB.checkDelayAtIndexIs(0, DELAY_B);
        queueB.checkDelayAtIndexIs(1, 0);
        queueB.checkMedian(DELAY_B / 2);

    }
}
