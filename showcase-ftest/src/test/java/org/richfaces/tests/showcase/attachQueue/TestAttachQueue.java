/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.Ajocado.*;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestAttachQueue extends AbstractAjocadoTest {

    /* *************************************************************************************
     * Locators*************************************************************************************
     */

    protected JQueryLocator input = jq("input[type=text]:visible");
    protected JQueryLocator submit = jq("input[type=submit]");
    protected JQueryLocator ajaxRequestProcessing = jq("span[class=rf-st-start]");

    /* ***************************************************************************************
     * Constants***************************************************************************************
     */
    private final int DELAY_IN_MILISECONDS = 2000;
    private final int NO_DELAY = 0;

    /* *****************************************************************************************
     * Fields****************************************************************************************
     */

    protected XHRHalter handle;

    /* ***************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testInput() {

        for (int i = 0; i < 5; i++) {
            typeToTheInputAndCheckTheDelay();
        }
    }

    @Test
    public void testButton() {

        for (int i = 0; i < 5; i++) {
            clickOnTheButtonAndCheckTheDelay();
        }

    }

    /*
     * types a character to the input and check whether delay after which the ajax processing is visible is between
     * DELAY_IN_MILISECONDS and DELAY_IN_MILISECONDS + 1000
     */
    private void typeToTheInputAndCheckTheDelay() {

        XHRHalter.enable();

        selenium.type(input, "a");
        selenium.fireEvent(input, Event.KEYUP);

        long timeBeforePressingKey = System.currentTimeMillis();

        waitGui.until(elementVisible.locator(ajaxRequestProcessing));

        long timeAfterAjaxRequestIsPresent = System.currentTimeMillis();

        if (handle == null) {
            handle = XHRHalter.getHandleBlocking();
        } else {
            handle.waitForOpen();
        }
        handle.complete();

        waitGui.until(elementNotVisible.locator(ajaxRequestProcessing));

        long actualDelay = timeAfterAjaxRequestIsPresent - timeBeforePressingKey;

        assertTrue((actualDelay >= DELAY_IN_MILISECONDS) && (actualDelay <= DELAY_IN_MILISECONDS + 1000),
            "The delay should be between " + DELAY_IN_MILISECONDS + "ms and " + (DELAY_IN_MILISECONDS + 1000)
                + "ms but was:" + actualDelay);
    }

    /*
     * clicks on the button and check whether delay after which the ajax processing is visible is NO_DELAY
     */
    private void clickOnTheButtonAndCheckTheDelay() {

        XHRHalter.enable();

        selenium.click(submit);

        long timeBeforePressingKey = System.currentTimeMillis();

        waitGui.until(elementVisible.locator(ajaxRequestProcessing));

        long timeAfterAjaxRequestIsPresent = System.currentTimeMillis();

        if (handle == null) {
            handle = XHRHalter.getHandleBlocking();
        } else {
            handle.waitForOpen();
        }
        handle.complete();

        waitGui.until(elementNotVisible.locator(ajaxRequestProcessing));

        long actualDelay = timeAfterAjaxRequestIsPresent - timeBeforePressingKey;

        assertTrue((actualDelay >= NO_DELAY) && (actualDelay <= NO_DELAY + 500), "The delay should be between "
            + NO_DELAY + "ms and " + (NO_DELAY + 500) + "ms but was:!" + actualDelay);
    }
}
