/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.attachQueue;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.attachQueue.page.AttachQueuePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestAttachQueue extends AbstractWebDriverTest {

    private static final int INPUT_DELAY = 2500;
    private static final int BUTTON_NO_DELAY = 500;
    private static final int DELTA = 500;

    @Page
    private AttachQueuePage page;

    @Test
    public void testInput() {
        //warm up
        for(int i = 0; i < 5; i++) {
            guardAjax(page.input).sendKeys("a");
        }
        for (int i = 0; i < 5; i++) {
            typeToTheInputAndCheckTheDelay();
        }
    }

    @Test
    public void testButton() {
        //warm up
        for(int i = 0; i < 5; i++) {
            guardAjax(page.submit).click();
        }
        for (int i = 0; i < 5; i++) {
            clickOnTheButtonAndCheckTheDelay();
        }
    }

    /*
     * types a character to the input and check whether delay after which the ajax processing is visible is between
     * DELAY_IN_MILISECONDS and DELAY_IN_MILISECONDS + 1000
     */
    private void typeToTheInputAndCheckTheDelay() {
        long timeBeforePressingKey = System.currentTimeMillis();
        guardAjax(page.input).sendKeys("a");
        long timeAfterAjaxRequestIsPresent = System.currentTimeMillis();
        long actualDelay = timeAfterAjaxRequestIsPresent - timeBeforePressingKey;
        assertEquals(actualDelay, INPUT_DELAY, DELTA);
    }

    /*
     * clicks on the button and check whether delay after which the ajax processing is visible is NO_DELAY
     * */
    private void clickOnTheButtonAndCheckTheDelay() {
        long timeBeforePressingKey = System.currentTimeMillis();
        guardAjax(page.submit).click();
        long actualDelay = System.currentTimeMillis() - timeBeforePressingKey;
        assertEquals(actualDelay, BUTTON_NO_DELAY, DELTA);
    }
}