/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes.ignoreDupResponses;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes.timeout;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.cheiron.halt.XHRHalter;
import org.jboss.cheiron.halt.XHRState;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.SeleniumException;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23037 $
 */
public class TestGlobalQueue extends AbstractAjocadoTest {

    QueueModel queue = new QueueModel();
    Attributes<QueueAttributes> attributes = new Attributes<QueueAttributes>();

    @Inject
    @Use(empty = false)
    Integer requestDelay;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jQueue/globalQueue.xhtml");
    }

    /**
     * Tests delay between time last event occurs and time when event triggers request (begin).
     */
    @Test
    @Use(field = "requestDelay", ints = { 0, 750, 1500, 5000 })
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
     * Events from one source should be stacked as occurs, while last event isn't delayed by configured requestDelay.
     */
    @Test
    public void testMultipleRequestsWithDelay() {
        attributes.set(QueueAttributes.requestDelay, 3000);

        queue.initializeCounts();

        XHRHalter.enable();

        queue.fireEvent(4);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        handle.complete();

        queue.checkCounts(4, 1, 1);

        queue.fireEvent(3);
        handle.waitForOpen();
        handle.send();
        handle.complete();

        queue.checkCounts(7, 2, 2);

        XHRHalter.disable();
    }

    /**
     * <p>
     * When no requestDelay (0) is set, events should fire request immediately.
     * </p>
     * 
     * <p>
     * However, when one event is waiting in queue for processing of previous request, events should be stacked.
     * </p>
     */
    @Test
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1194")
    public void testMultipleRequestsWithNoDelay() {
        attributes.set(QueueAttributes.requestDelay, 0);

        queue.initializeCounts();

        XHRHalter.enable();

        queue.fireEvent(1);
        queue.checkCounts(1, 1, 0);

        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        queue.fireEvent(1);
        queue.checkCounts(2, 1, 0);

        handle.complete();
        queue.checkCounts(2, 2, 1);

        handle.waitForOpen();
        handle.send();
        queue.fireEvent(4);
        queue.checkCounts(6, 2, 1);

        handle.complete();
        queue.checkCounts(6, 3, 2);

        handle.waitForOpen();
        handle.send();
        queue.fireEvent(1);
        queue.checkCounts(7, 3, 2);

        handle.complete();
        queue.checkCounts(7, 4, 3);

        handle.waitForOpen();
        handle.send();
        queue.checkCounts(7, 4, 3);

        handle.complete();
        queue.checkCounts(7, 4, 4);

        XHRHalter.disable();
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

    // TODO not implemented yet
    public void testTimeout() {
        attributes.set(QueueAttributes.requestDelay, 0);
        attributes.set(timeout, 1000);

        XHRHalter.enable();

        queue.fireEvent(1);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.continueBefore(XHRState.COMPLETE);
        queue.fireEvent(10);

        XHRHalter.disable();

        // fireEvents(1);
        // fireEvents(1);
    }

    @Test
    public void testIgnoreDuplicatedResponsesFalse() {
        attributes.set(ignoreDupResponses, false);

        XHRHalter.enable();
        queue.type("a");
        queue.fireEvent(1);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        queue.type("b");
        queue.fireEvent(1);
        handle.complete();
        waitGui.dontFail().waitForChange("", retrieveText.locator(queue.repeatedText));
        assertEquals(queue.getRepeatedText(), "a");
        handle.waitForOpen();
        handle.complete();
        waitGui.dontFail().waitForChange("a", retrieveText.locator(queue.repeatedText));
        assertEquals(queue.getRepeatedText(), "b");
    }

    @Test
    public void testIgnoreDuplicatedResponsesTrue() {
        attributes.set(ignoreDupResponses, true);

        XHRHalter.enable();
        queue.type("c");
        queue.fireEvent(1);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        queue.type("d");
        queue.fireEvent(1);
        handle.complete();
        try {
            waitGui.dontFail().waitForChange("", retrieveText.locator(queue.repeatedText));
        } catch (SeleniumException e) {
            // expected timeout
        }
        assertEquals(queue.getRepeatedText(), "");
        handle.waitForOpen();
        handle.complete();
        waitGui.dontFail().waitForChange("", retrieveText.locator(queue.repeatedText));
        assertEquals(queue.getRepeatedText(), "d");
    }
}
