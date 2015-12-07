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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.AjaxRequestHalter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter;
import org.richfaces.tests.metamer.ftest.extension.ajaxhalter.Halter.HaltedRequest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNestedAttachQueue extends AbstractAttachQueueTest {

    @Page
    private AttachQueuePage page;

    @Override
    public String getComponentTestPagePath() {
        return "a4jAttachQueue/nested.xhtml";
    }

    @Test
    @Skip(On.JSF.MyFaces.class)// https://issues.jboss.org/browse/RFPL-3998
    @CoversAttributes("ignoreDupResponses")
    public void testIgnoreDuplicatedResponsesFalse() {
        attachQueueAttributes.set(AttachQueueAttributes.ignoreDupResponses, Boolean.FALSE);

        Halter halter = AjaxRequestHalter.getHalter();
        getQueue().type("a");
        HaltedRequest req = halter.nextRequest().continueToPhaseAfter().opened();
        getQueue().type("b");
        req.completeRequest();

        getQueue().waitForChange("", page.getOutput1());
        assertEquals(page.getOutput1().getText(), "a");

        halter.completeFollowingRequests(1);
        getQueue().waitForChange("a", page.getOutput1());
        assertEquals(page.getOutput1().getText(), "ab");
    }

    @Test
    @Skip(On.JSF.MyFaces.class)// https://issues.jboss.org/browse/RFPL-3998
    @CoversAttributes("ignoreDupResponses")
    public void testIgnoreDuplicatedResponsesTrue() {
        attachQueueAttributes.set(AttachQueueAttributes.ignoreDupResponses, Boolean.TRUE);

        Halter halter = AjaxRequestHalter.getHalter();
        getQueue().type("c");
        HaltedRequest req = halter.nextRequest().continueToPhaseAfter().opened();
        getQueue().type("d");
        req.completeRequest();

        try {
            getQueue().waitForChange("", page.getOutput1());
            fail("should timeout here!");
        } catch (Exception ignored) {
            // expected timeout
        }
        assertEquals(page.getOutput1().getText(), "");

        halter.completeFollowingRequests(1);

        getQueue().waitForChange("", page.getOutput1());
        assertEquals(page.getOutput1().getText(), "cd");
    }

    @Test
    @Unstable
    public void testNoRequestDelay() {
        super.testNoRequestDelay();
    }

    @Test
    public void testQueueAndDequeueEvents() {
        super.testQueueAndDequeueEvents();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @Unstable
    public void testRequestDelay() {
        super.testRequestDelay();
    }

    /**
     * Groups two queues to one. Fires events on two inputs, where first input has higher @requestDelay, and if the second
     * input's request will be processed by the queue in meantime, then the first input's request will be ignored/replaced.
     */
    @Test
    @CoversAttributes("requestGroupingId")
    public void testRequestGroupingId() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 20000);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, 100);
        attachQueueAttrs1.set(AttachQueueAttributes.requestGroupingId, "group1");
        attachQueueAttrs2.set(AttachQueueAttributes.requestGroupingId, "group1");
        Halter halter = AjaxRequestHalter.getHalter();
        page.getInput1().sendKeys("a");
        getQueue().waitAndCheckEventsCounts(1, 0, 0, 0);
        page.getInput2().sendKeys("b");
        getQueue().waitAndCheckEventsCounts(1, 1, 1, 0);//1 event from both inputs, 1 request and 0 dom updates

        halter.completeFollowingRequests(1);
        getQueue().waitForChange("", page.getOutput2());
        getQueue().waitAndCheckEventsCounts(1, 1, 1, 1);//only 1 dom update should be triggered

        assertEquals(page.getOutput1().getText(), "", "The faster (lower delay) request haven't replaced the slower");
        assertEquals(page.getOutput2().getText(), "b", "The faster (lower delay) request haven't replaced the slower.");
    }

    @Test
    public void testTwoQueuesTwoEvents() {
        super.testTwoQueuesTwoEvents();
    }
}
