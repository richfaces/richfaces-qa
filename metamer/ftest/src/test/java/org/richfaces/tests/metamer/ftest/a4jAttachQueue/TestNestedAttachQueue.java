/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.a4jAttachQueue;

import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.dom.Event.KEYPRESS;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.AbstractGrapheneTest.pjq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.attachQueueAttributes;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.attachQueueAttrs1;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.attachQueueAttrs2;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.queueAttributes;
import static org.testng.Assert.assertEquals;

import com.thoughtworks.selenium.SeleniumException;
import java.net.URL;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueModel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNestedAttachQueue extends AbstractAttachQueueTest {

    QueueModel queue = new QueueModel();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jAttachQueue/nested.xhtml");
    }

    @BeforeMethod
    public void setupDelays() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, DELAY_A);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, DELAY_B);
        queueAttributes.set(QueueAttributes.requestDelay, GLOBAL_DELAY);
    }

    @Test
    public void testDelay() {
        super.testDelay();
    }

    @Test
    public void testNoDelay() {
        super.testNoDelay();
    }

    @Test
    public void testTimingOneQueueTwoEvents() {
        super.testTimingOneQueueTwoEvents();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testQueueAndDequeueEvents() {
        super.testQueueAndDequeueEvents();
    }

    @Test
    public void testIgnoreDuplicatedResponsesFalse() {
        attachQueueAttributes.set(AttachQueueAttributes.ignoreDupResponses, Boolean.FALSE);

        XHRHalter.enable();
        queue.type("a");
        queue.fireEvent(1);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        queue.type("b");
        queue.fireEvent(1);
        handle.complete();
        waitGui.dontFail().waitForChange("", retrieveText.locator(pjq("span[id$=output1]")));
        assertEquals(selenium.getText(pjq("span[id$=output1]")), "a");
        handle.waitForOpen();
        handle.complete();
        waitGui.dontFail().waitForChange("a", retrieveText.locator(pjq("span[id$=output1]")));
        assertEquals(selenium.getText(pjq("span[id$=output1]")), "b");
    }

    @Test
    public void testIgnoreDuplicatedResponsesTrue() {
        attachQueueAttributes.set(AttachQueueAttributes.ignoreDupResponses, Boolean.TRUE);

        XHRHalter.enable();
        queue.type("c");
        queue.fireEvent(1);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        queue.type("d");
        queue.fireEvent(1);
        handle.complete();
        try {
            waitGui.dontFail().waitForChange("", retrieveText.locator(pjq("span[id$=output1]")));
        } catch (SeleniumException e) {
            // expected timeout
        }
        assertEquals(selenium.getText(pjq("span[id$=output1]")), "");
        handle.waitForOpen();
        handle.complete();
        waitGui.dontFail().waitForChange("", retrieveText.locator(pjq("span[id$=output1]")));
        assertEquals(selenium.getText(pjq("span[id$=output1]")), "d");
    }

    /**
     * Groups two queues to one. Fires events on two inputs, where first
     * input has higher @requestDelay, and if the second input's request will be
     * processed by the queue in meantime, then the first input's request will be
     * ignored/replaced.
     */
    @Test
    public void testRequestGroupingId() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 20000);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, 100);
        attachQueueAttrs1.set(AttachQueueAttributes.requestGroupingId, "group1");
        attachQueueAttrs2.set(AttachQueueAttributes.requestGroupingId, "group1");
        queue.initializeCounts();
        XHRHalter.enable();
        selenium.type(pjq("input[id$=input1]"), "test1");
        selenium.fireEvent(pjq("input[id$=input1]"), KEYPRESS);
        queue.checkCounts(1, 0, 0, 0);
        selenium.type(pjq("input[id$=input2]"), "test2");
        selenium.fireEvent(pjq("input[id$=input2]"), KEYPRESS);
        queue.checkCounts(1, 1, 1, 0);//1 event from both inputs, 1 request and 0 dom updates
        XHRHalter halter = XHRHalter.getHandleBlocking();
        halter.complete();//complete request
        waitGui.dontFail().waitForChange("", retrieveText.locator(pjq("span[id$=output2]")));
        queue.checkCounts(1, 1, 1, 1);//only 1 dom update should be triggered
        assertEquals(selenium.getText(pjq("span[id$=output1]")), "", "The faster (lower delay) request haven't replaced the slower");
        assertEquals(selenium.getText(pjq("span[id$=output2]")), "test2", "The faster (lower delay) request haven't replaced the slower.");
    }
}
