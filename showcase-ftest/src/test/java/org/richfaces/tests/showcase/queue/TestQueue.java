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
package org.richfaces.tests.showcase.queue;

import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.dom.Event.KEYUP;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.WaitTimeoutException;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestQueue extends AbstractAjocadoTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator inputQueue = jq("fieldset table input[id$=myinput]");
    protected JQueryLocator repeatedText = jq("fieldset table span[id$=outtext]");
    protected JQueryLocator eventCounts = jq("fieldset table span[id$=events]");
    protected JQueryLocator requestCounts = jq("fieldset table span[id$=requests]");
    protected JQueryLocator DOMupdatesCounts = jq("fieldset table span[id$=updates]");
    protected JQueryLocator requestDelay = jq("fieldset table input[name$=delay]");
    protected JQueryLocator ignoreDuplicates = jq("fieldset table input[type=checkbox]");
    protected JQueryLocator applyButton = jq("fieldset table input[type=submit]");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testRequestDelay500() {
        testRequestDelay("500");
    }

    @Test
    public void testRequestDelay1000() {
        testRequestDelay("1000");
    }

    @Test
    public void testRequestDelay5000() {
        testRequestDelay("5000");
    }

    @Test
    public void testQueueIgnoreDuplicatesDisabled() {

        selenium.uncheck(ignoreDuplicates);
        guardHttp(selenium).click(applyButton);

        XHRHalter.enable();

        selenium.type(inputQueue, "a");
        selenium.fireEvent(inputQueue, KEYUP);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        selenium.type(inputQueue, "b");
        selenium.fireEvent(inputQueue, KEYUP);

        handle.complete();

        waitGui.dontFail().waitForChange("", retrieveText.locator(repeatedText));
        assertEquals(getTextFromElementRepeatedText(), "a", "The text in the repeated text should be 'a'!");

        handle.waitForOpen();
        handle.complete();

        waitGui.dontFail().waitForChange("a", retrieveText.locator(repeatedText));
        assertEquals(getTextFromElementRepeatedText(), "b", "The text in the repeated text should be 'b'!");

    }

    @Test
    public void testQueueIgnoreDuplicatesEnabled() {

        selenium.check(ignoreDuplicates);
        guardHttp(selenium).click(applyButton);

        XHRHalter.enable();

        selenium.type(inputQueue, "a");
        selenium.fireEvent(inputQueue, KEYUP);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        selenium.type(inputQueue, "b");
        selenium.fireEvent(inputQueue, KEYUP);

        handle.complete();

        try {
            waitGui.waitForChange("", retrieveText.locator(repeatedText));
        } catch (WaitTimeoutException e) {
            // expected timeout
        }

        assertEquals(getTextFromElementRepeatedText(), "", "The text should be empty string!");

        handle.waitForOpen();
        handle.complete();

        waitGui.waitForChange("", retrieveText.locator(repeatedText));
        assertEquals(getTextFromElementRepeatedText(), "b", "The text in the repeated text should be 'b'!");

    }

    /* ********************************************************************************************************
     * Help methods *********************************************************************
     * ***********************************
     */

    /**
     * Set delay according to delayInMiliSeconds and then test the actual delay with use of System.currentTimeMillis(),
     * the actual delay has to be in the range of delayInMiliSeconds <= actual delay <= delayInMiliSeconds + 1000
     */
    private void testRequestDelay(String delayInMiliSeconds) {

        selenium.type(requestDelay, delayInMiliSeconds);
        guardHttp(selenium).click(applyButton);

        TextRetriever requestCountRetriever = retrieveText.locator(requestCounts);
        requestCountRetriever.initializeValue();

        selenium.type(inputQueue, "a");
        selenium.fireEvent(inputQueue, KEYUP);
        long currentTimeBeforeRequest = System.currentTimeMillis();

        waitGui.interval(50).timeout(Long.valueOf(delayInMiliSeconds) + 2000)
            .waitForChangeAndReturn(requestCountRetriever);

        long currentTimeAfterRequest = System.currentTimeMillis();

        long actualDelay = currentTimeAfterRequest - currentTimeBeforeRequest;
        long bottomBorderOfDelay = Long.valueOf(delayInMiliSeconds);
        long upperBorderOfDelay = Long.valueOf(delayInMiliSeconds) + 1000;

        assertTrue((actualDelay >= bottomBorderOfDelay) && (actualDelay <= upperBorderOfDelay),
            "The delay should be between " + bottomBorderOfDelay + "ms and " + upperBorderOfDelay + "ms but was:!"
                + actualDelay);
    }

    private String getTextFromElementRepeatedText() {

        return selenium.getText(repeatedText);
    }

}
