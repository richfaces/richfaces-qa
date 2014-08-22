/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc.
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

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.graphene.page.interception.AjaxHalter;

import static org.jboss.arquillian.graphene.halter.AjaxState.OPENED;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNestedAttachQueue extends AbstractAttachQueueTest {

    @Page
    private AttachQueuePage page;

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

        AjaxHalter.enable();
        getQueue().type("a");
        AjaxHalter handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        getQueue().type("b");
        handle.complete();
        getQueue().waitForChange("", page.getOutput1());
        assertEquals(page.getOutput1().getText(), "a");
        handle = AjaxHalter.getHandleBlocking();
        handle.complete();
        getQueue().waitForChange("a", page.getOutput1());
        assertEquals(page.getOutput1().getText(), "ab");
    }

    @Test
    public void testIgnoreDuplicatedResponsesTrue() {
        attachQueueAttributes.set(AttachQueueAttributes.ignoreDupResponses, Boolean.TRUE);

        AjaxHalter.enable();
        getQueue().type("c");
        AjaxHalter handle = AjaxHalter.getHandleBlocking();
        handle.continueAfter(OPENED);
        getQueue().type("d");
        handle.complete();
        try {
            getQueue().waitForChange("", page.getOutput1());
        } catch (Exception e) {
            // expected timeout
        }
        assertEquals(page.getOutput1().getText(), "");
        handle = AjaxHalter.getHandleBlocking();
        handle.complete();
        getQueue().waitForChange("", page.getOutput1());
        assertEquals(page.getOutput1().getText(), "cd");
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
        AjaxHalter.enable();
        page.getInput1().sendKeys("a");
        getQueue().checkCounts(1, 0, 0, 0);
        page.getInput2().sendKeys("b");
        getQueue().checkCounts(1, 1, 1, 0);//1 event from both inputs, 1 request and 0 dom updates
        AjaxHalter halter = AjaxHalter.getHandleBlocking();
        halter.complete();//complete request
        getQueue().waitForChange("", page.getOutput2());
        getQueue().checkCounts(1, 1, 1, 1);//only 1 dom update should be triggered
        assertEquals(page.getOutput1().getText(), "", "The faster (lower delay) request haven't replaced the slower");
        assertEquals(page.getOutput2().getText(), "b", "The faster (lower delay) request haven't replaced the slower.");
    }
}
