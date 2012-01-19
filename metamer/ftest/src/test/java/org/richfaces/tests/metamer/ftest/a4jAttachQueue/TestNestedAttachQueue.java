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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.attachQueueAttrs1;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.attachQueueAttrs2;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.queueAttributes;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestNestedAttachQueue extends AbstractAttachQueueTest {
    
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
}
