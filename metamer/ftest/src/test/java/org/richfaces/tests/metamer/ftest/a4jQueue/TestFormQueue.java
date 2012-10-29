/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import java.net.URL;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueModel.Input;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22680 $
 */
// https://issues.jboss.org/browse/RF-9430
@Templates("plain")
public class TestFormQueue extends AbstractGrapheneTest {

    static final Long GLOBAL_DELAY = 10000L;
    static final Long DELAY_A = 3000L;
    static final Long DELAY_B = 5000L;

    QueueModel queueA = prepareLocators("formQueue1");
    QueueModel queueB = prepareLocators("formQueue2");
    QueueModel globalQueue = prepareLocators("globalQueue");

    Attributes<QueueAttributes> attributesQueueA = prepareAttributes("formQueue1");
    Attributes<QueueAttributes> attributesQueueB = prepareAttributes("formQueue2");
    Attributes<QueueAttributes> attributesGlobalQueue = prepareAttributes("globalQueue");

    private static QueueModel prepareLocators(String identifier) {
        return new QueueModel(pjq("div.rf-p[id$={0}Panel]").format(identifier));
    }

    private static Attributes<QueueAttributes> prepareAttributes(String identifier) {
        return new Attributes<QueueAttributes>(pjq("div.rf-p[id$={0}AttributesPanel]").format(identifier));
    }

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

        XHRHalter.enable();

        queueA.fireEvent(Input.FIRST, 2);
        queueA.fireEvent(Input.SECOND, 3);

        XHRHalter halter = XHRHalter.getHandleBlocking();
        halter.complete();
        halter.waitForOpen();
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

        queueA.initializeCounts();

        XHRHalter.enable();

        queueA.fireEvent(Input.FIRST, 2);
        queueA.checkCounts(2, 0, 0, 0);
        queueA.fireEvent(Input.SECOND, 3);
        queueA.checkCounts(2, 3, 1, 0);

        XHRHalter halter = XHRHalter.getHandleBlocking();
        queueA.checkCounts(2, 3, 1, 0);
        halter.complete();
        queueA.checkCounts(2, 3, 2, 1);
        halter.waitForOpen();
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
    public void testTimingTwoQueuesFourEvents() {
        waitFor(1000); // FIXME workaround for Jenkins
        attributesQueueA.set(QueueAttributes.requestDelay, DELAY_A);
        waitFor(1000); // FIXME workaround for Jenkins
        attributesQueueB.set(QueueAttributes.requestDelay, DELAY_B);
        waitFor(1000); // FIXME workaround for Jenkins
        attributesGlobalQueue.set(QueueAttributes.requestDelay, GLOBAL_DELAY);

        queueA.initializeTimes();
        queueB.initializeTimes();

        XHRHalter.enable();

        queueA.fireEvent(Input.FIRST, 1);
        queueA.fireEvent(Input.SECOND, 1);
        queueB.fireEvent(Input.FIRST, 1);
        queueB.fireEvent(Input.SECOND, 1);

        XHRHalter halter = XHRHalter.getHandleBlocking();
        halter.complete();
        halter.waitForOpen();
        halter.complete();
        halter.waitForOpen();
        halter.complete();
        halter.waitForOpen();
        halter.complete();

        queueB.checkTimes(Input.SECOND, DELAY_B);

        assertTrue(queueA.retrieveBeginTime.retrieve() - queueA.retrieveEvent1Time.retrieve() < 1000);
        assertTrue(queueA.retrieveBeginTime.retrieve() - queueA.retrieveEvent2Time.retrieve() < 1000);
        assertTrue(queueB.retrieveBeginTime.retrieve() - queueB.retrieveEvent1Time.retrieve() > 3000);
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

        queueA.initializeCounts();
        queueB.initializeCounts();

        XHRHalter.enable();

        queueA.fireEvent(Input.FIRST, 1);
        queueA.checkCounts(1, 0, 0, 0);
        queueA.fireEvent(Input.SECOND, 1);
        queueA.checkCounts(1, 1, 1, 0);
        queueB.fireEvent(Input.FIRST, 1);
        queueB.checkCounts(1, 0, 0, 0);
        queueB.fireEvent(Input.SECOND, 1);

        queueA.checkCounts(1, 1, 1, 0);
        queueB.checkCounts(1, 1, 0, 0);

        XHRHalter halter = XHRHalter.getHandleBlocking();
        halter.complete();
        queueA.checkCounts(1, 1, 2, 1);
        halter.waitForOpen();
        halter.complete();
        queueA.checkCounts(1, 1, 2, 2);
        queueB.checkCounts(1, 1, 1, 0);
        halter.waitForOpen();
        halter.complete();
        halter.waitForOpen();
        queueB.checkCounts(1, 1, 2, 1);
        halter.complete();

        queueA.checkCounts(1, 1, 2, 2);
        queueB.checkCounts(1, 1, 2, 2);
    }

}
