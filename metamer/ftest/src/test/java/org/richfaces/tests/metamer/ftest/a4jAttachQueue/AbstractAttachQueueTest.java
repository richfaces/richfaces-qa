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
package org.richfaces.tests.metamer.ftest.a4jAttachQueue;

import static org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes.onrequestdequeue;
import static org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes.onrequestqueue;
import static org.richfaces.tests.metamer.ftest.a4jAttachQueue.AttachQueueAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.attachQueueAttrs1;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.attachQueueAttrs2;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueModel;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueModel.Input;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractAttachQueueTest extends AbstractAjocadoTest {

    protected static final Long GLOBAL_DELAY = 10000L;
    protected static final Long DELAY_A = 3000L;
    protected static final Long DELAY_B = 5000L;

    private QueueModel queue = new QueueModel();
    
    protected QueueModel getQueue() {
        return queue;
    }
    
    protected void testDelay() {
        queue.initializeTimes();
        queue.fireEvent(Input.FIRST, 1);
        queue.checkTimes(Input.FIRST, DELAY_A);        
    }
    
    protected void testNoDelay() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 0);

        queue.initializeCounts();

        XHRHalter.enable();

        queue.fireEvent(Input.FIRST, 4);
        queue.checkCounts(4, 0, 1, 0);

        XHRHalter halter = XHRHalter.getHandleBlocking();
        queue.checkCounts(4, 0, 1, 0);

        halter.complete();
        queue.checkCounts(4, 0, 2, 1);

        halter.waitForOpen();
        queue.checkCounts(4, 0, 2, 1);

        halter.complete();
        queue.checkCounts(4, 0, 2, 2);
    }   
    
    protected void testTimingOneQueueTwoEvents() {
        queue.initializeTimes();

        XHRHalter.enable();

        queue.fireEvent(Input.FIRST, 3);
        queue.fireEvent(Input.SECOND, 1);

        XHRHalter halter = XHRHalter.getHandleBlocking();
        halter.complete();
        halter.waitForOpen();
        halter.complete();

        queue.checkTimes(Input.SECOND, DELAY_B);
        queue.checkNoDelayBetweenEvents();
    } 
    
    protected void testRendered() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 1500);
        attachQueueAttrs1.set(onrequestqueue, JavaScript.js("alert('requestQueued')"));
        attachQueueAttrs1.set(onrequestdequeue, JavaScript.js("alert('requestDequeued')"));
        attachQueueAttrs1.set(rendered, false);

        queue.initializeTimes();
        queue.fireEvent(1);

        // check that no requestDelay is applied while renderer=false
        queue.checkTimes(0);
        assertFalse(selenium.isAlertPresent());
    }    
    
    protected void testQueueAndDequeueEvents() {
        attachQueueAttrs1.set(AttachQueueAttributes.requestDelay, 0);
        attachQueueAttrs2.set(AttachQueueAttributes.requestDelay, 0);
        
        attachQueueAttrs1.set(AttachQueueAttributes.onrequestqueue, "metamerEvents += \"requestqueue \"");
        attachQueueAttrs1.set(AttachQueueAttributes.onrequestdequeue, "metamerEvents += \"requestdequeue \"");
        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        
        queue.fireEvent(Input.FIRST, 1);    
                
        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");
        assertEquals(events.length, 2, "2 events should be fired, was [" + selenium.getEval(new JavaScript("window.metamerEvents")) + "]");
        assertEquals(events[0], "requestqueue", "Attribute onrequestqueue doesn't work");
        assertEquals(events[1], "requestdequeue", "Attribute onrequestdequeue doesn't work");        
    }
}
