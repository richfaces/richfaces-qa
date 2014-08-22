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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.arquillian.graphene.findby.FindByJQuery;

import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.jboss.arquillian.graphene.page.interception.AjaxHalter;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22680 $
 */
// https://issues.jboss.org/browse/RF-9430
@Templates("plain")
public class TestFormQueue extends AbstractWebDriverTest {

    static final Long GLOBAL_DELAY = 10000L;
    static final Long DELAY_A = 3000L;
    static final Long DELAY_B = 5000L;

    @FindByJQuery("div.rf-p[id$=formQueue1Panel]")
    QueueFragment queueA;

    @FindByJQuery("div.rf-p[id$=formQueue2Panel]")
    QueueFragment queueB;

    @FindByJQuery("div.rf-p[id$=globalQueuePanel]")
    QueueFragment globalQueue;

    private final Attributes<QueueAttributes> attributesQueueA = getAttributes("attributeForm:formQueue1Attributes");
    private final Attributes<QueueAttributes> attributesQueueB = getAttributes("attributeForm:formQueue2Attributes");
    private final Attributes<QueueAttributes> attributesGlobalQueue = getAttributes("attributeForm:globalQueueAttributes");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jQueue/formQueue.xhtml");
    }

    /**
     * <p>
     * Tests request delays for one form queue.
     * </p>
     *
     * <p>
     * Uses one form queue, which controls events from two distinct event sources.
     * </p>
     * <p>
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     * </p>
     */
    @Test
    public void testTimingOneQueueTwoEvents() {
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        queueA.initializeTimes();

        AjaxHalter.enable();

        queueA.fireEvent(Input.FIRST, 2);
        queueA.fireEvent(Input.SECOND, 3);

        AjaxHalter halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        halter = AjaxHalter.getHandleBlocking();
        halter.complete();

        queueA.checkTimes(Input.SECOND, DELAY_A);
    }

    /**
     * <p>
     * Tests the event handlers producing count of events (count of events, requests and updates).
     *
     * <p>
     * Uses one form queue, which controls events from two distinct event sources.
     * </p>
     *
     * <p>
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     * </p>
     */
    @Test
    public void testCountsOneQueueTwoEvents() {
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        AjaxHalter.enable();

        queueA.fireEvent(Input.FIRST, 2);
        queueA.checkCounts(2, 0, 0, 0);
        queueA.fireEvent(Input.SECOND, 3);
        queueA.checkCounts(2, 3, 1, 0);

        AjaxHalter halter = AjaxHalter.getHandleBlocking();
        queueA.checkCounts(2, 3, 1, 0);
        halter.complete();
        queueA.checkCounts(2, 3, 2, 1);
        halter = AjaxHalter.getHandleBlocking();
        queueA.checkCounts(2, 3, 2, 1);
        halter.complete();
        queueA.checkCounts(2, 3, 2, 2);
    }

    /**
     * <p>
     * Tests request delays for two form queues.
     * </p>
     *
     * <p>
     * Uses two form queues, controlling events from four distinct event sources (two for each queue).
     * </p>
     *
     * <p>
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     * </p>
     */
    @Test
    public void testTimingTwoQueuesFourEvents() throws InterruptedException {
//        Thread.sleep(1000); // FIXME workaround for Jenkins
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
//        Thread.sleep(1000); // FIXME workaround for Jenkins
        attributesQueueB.set(QueueAttributes.requestDelay, DELAY_B);
//        Thread.sleep(1000); // FIXME workaround for Jenkins
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        queueA.initializeTimes();
        queueB.initializeTimes();

        AjaxHalter.enable();

        queueA.fireEvent(Input.FIRST, 1);
        queueA.fireEvent(Input.SECOND, 1);
        queueB.fireEvent(Input.FIRST, 1);
        queueB.fireEvent(Input.SECOND, 1);

        AjaxHalter halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        halter = AjaxHalter.getHandleBlocking();
        halter.complete();

        queueB.checkTimes(Input.SECOND, DELAY_B);

        assertTrue(queueA.getBeginTime() - queueA.getEvent1Time() < 1000);
        assertTrue(queueA.getBeginTime() - queueA.getEvent2Time() < 1000);
        assertTrue(queueB.getBeginTime() - queueB.getEvent1Time() > 3000);
    }

    /**
     * <p>
     * Tests the event handlers producing count of events (count of events, requests and updates).
     *
     * <p>
     * Uses two form queues, controlling events from four distinct event sources (two for each queue).
     * </p>
     *
     * <p>
     * When one source waits for delay and another source produces event, events from first source should be immediately
     * processed.
     * </p>
     */
    @Test
    public void testCountsTwoQueuesThreeEvents() {
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        attributesQueueB.set(QueueAttributes.requestDelay, DELAY_B);
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        AjaxHalter.enable();

        queueA.fireEvent(Input.FIRST, 1);
        queueA.checkCounts(1, 0, 0, 0);
        queueA.fireEvent(Input.SECOND, 1);
        queueA.checkCounts(1, 1, 1, 0);
        queueB.fireEvent(Input.FIRST, 1);
        queueB.checkCounts(1, 0, 0, 0);
        queueB.fireEvent(Input.SECOND, 1);

        queueA.checkCounts(1, 1, 1, 0);
        queueB.checkCounts(1, 1, 0, 0);

        AjaxHalter halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        queueA.checkCounts(1, 1, 2, 1);
        halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        queueA.checkCounts(1, 1, 2, 2);
        queueB.checkCounts(1, 1, 1, 0);
        halter = AjaxHalter.getHandleBlocking();
        halter.complete();
        halter = AjaxHalter.getHandleBlocking();
        queueB.checkCounts(1, 1, 2, 1);
        halter.complete();

        queueA.checkCounts(1, 1, 2, 2);
        queueB.checkCounts(1, 1, 2, 2);
    }

}
