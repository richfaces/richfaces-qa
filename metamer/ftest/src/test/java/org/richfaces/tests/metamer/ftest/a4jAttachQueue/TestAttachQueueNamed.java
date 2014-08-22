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
package org.richfaces.tests.metamer.ftest.a4jAttachQueue;

import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes.requestDelay;

import java.net.URL;
import org.openqa.selenium.support.FindBy;

import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueAttributes;
import org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input.FIRST;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input.SECOND;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22680 $
 */
public class TestAttachQueueNamed extends AbstractWebDriverTest {

    private static final Long DELAY = 5000L;

    @FindBy(tagName = "body")
    private QueueFragment queue;

    private final Attributes<QueueAttributes> queueAttributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jAttachQueue/namedQueue.xhtml");
    }

    /**
     * Tests that queue is referenced by name from attachQueues by watching their requestDelay.
     */
    @Test
    public void testNameReferencing() {
        queueAttributes.set(requestDelay, DELAY);

        queue.initializeTimes();

        queue.fireEvent(FIRST, 1);
        queue.checkTimes(FIRST, DELAY);

        queue.fireEvent(SECOND, 1);
        queue.checkTimes(SECOND, DELAY);
    }
}
