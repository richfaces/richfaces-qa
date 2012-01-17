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
package org.richfaces.tests.metamer.ftest.a4jLog;

import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/a4jLog/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22499 $
 */
public class TestA4JLog extends AbstractAjocadoTest {

    /**
     * Enumeration representing all possible levels for a4j:log.
     */
    public enum LogLevel {

        DEBUG, NULL, INFO, WARN, ERROR;
    }
    private JQueryLocator input = pjq("input[id$=nameInput]");
    private JQueryLocator submitButton = pjq("input[id$=submitButton]");
    private JQueryLocator output = pjq("span[id$=out]");
    private JQueryLocator log = pjq("div.rf-log");
    private JQueryLocator levelSelect = pjq("div.rf-log select");
    private JQueryLocator logMsg = pjq("div.rf-log div.rf-log-contents div");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jLog/simple.xhtml");
    }

    @Test
    public void testSubmit() {
        selenium.typeKeys(input, "RichFaces 4");
        selenium.click(submitButton);

        waitGui.until(textEquals.locator(output).text("Hello RichFaces 4!"));

        int count = selenium.getCount(logMsg);
        assertTrue(count > 0, "There should be at least one message in log after submit button was clicked.");
    }

    @Test
    public void testSubmitUnicode() {
        selenium.typeKeys(input, "ľščťžýáíéôúäň");
        selenium.click(submitButton);

        waitGui.until(textEquals.locator(output).text("Hello ľščťžýáíéôúäň!"));

        int count = selenium.getCount(logMsg);
        assertTrue(count > 0, "There should be at least one message in log after submit button was clicked.");
    }

    @Test
    public void testClearButton() {
        JQueryLocator clearButton = pjq("div.rf-log button");

        selenium.typeKeys(input, "RichFaces 4");
        selenium.click(submitButton);

        waitGui.until(textEquals.locator(output).text("Hello RichFaces 4!"));

        int count = selenium.getCount(logMsg);
        assertTrue(count > 0, "There should be at least one message in log after submit button was clicked.");

        // test clear
        selenium.click(clearButton);
        count = selenium.getCount(logMsg);
        assertEquals(count, 0, "There should be no messages in log after clear button was clicked.");
    }

    @Test
    public void testRendered() {
        JQueryLocator renderedInput = pjq("input[type=radio][name$=renderedInput][value=false]");

        selenium.click(renderedInput);
        selenium.waitForPageToLoad(TIMEOUT);

        assertFalse(selenium.isElementPresent(log), "Log should not be displayed.");
    }

    @Test
    public void testDebugFilterNull() {
        testLogging(LogLevel.DEBUG, LogLevel.NULL);
    }

    @Test
    public void testDebugFilterDebug() {
        testLogging(LogLevel.DEBUG, LogLevel.DEBUG);
    }

    @Test
    public void testDebugFilterInfo() {
        testLogging(LogLevel.DEBUG, LogLevel.INFO);
    }

    @Test
    public void testDebugFilterWarn() {
        testLogging(LogLevel.DEBUG, LogLevel.WARN);
    }

    @Test
    public void testDebugFilterError() {
        testLogging(LogLevel.DEBUG, LogLevel.ERROR);
    }

    @Test
    public void testInfoFilterNull() {
        testLogging(LogLevel.INFO, LogLevel.NULL);
    }

    @Test
    public void testInfoFilterDebug() {
        testLogging(LogLevel.INFO, LogLevel.DEBUG);
    }

    @Test
    public void testInfoFilterInfo() {
        testLogging(LogLevel.INFO, LogLevel.INFO);
    }

    @Test
    public void testInfoFilterWarn() {
        testLogging(LogLevel.INFO, LogLevel.WARN);
    }

    @Test
    public void testInfoFilterError() {
        testLogging(LogLevel.INFO, LogLevel.ERROR);
    }

    @Test
    public void testWarnFilterNull() {
        testLogging(LogLevel.WARN, LogLevel.NULL);
    }

    @Test
    public void testWarnFilterDebug() {
        testLogging(LogLevel.WARN, LogLevel.DEBUG);
    }

    @Test
    public void testWarnFilterInfo() {
        testLogging(LogLevel.WARN, LogLevel.INFO);
    }

    @Test
    public void testWarnFilterWarn() {
        testLogging(LogLevel.WARN, LogLevel.WARN);
    }

    @Test
    public void testWarnFilterError() {
        testLogging(LogLevel.WARN, LogLevel.ERROR);
    }

    @Test
    public void testErrorFilterNull() {
        testLogging(LogLevel.ERROR, LogLevel.NULL);
    }

    @Test
    public void testErrorFilterDebug() {
        testLogging(LogLevel.ERROR, LogLevel.DEBUG);
    }

    @Test
    public void testErrorFilterInfo() {
        testLogging(LogLevel.ERROR, LogLevel.INFO);
    }

    @Test
    public void testErrorFilterWarn() {
        testLogging(LogLevel.ERROR, LogLevel.WARN);
    }

    @Test
    public void testErrorFilterError() {
        testLogging(LogLevel.ERROR, LogLevel.ERROR);
    }

    private void testLogging(LogLevel logLevel, LogLevel filterLevel) {
        JQueryLocator logButton = pjq("input[id$=" + logLevel.toString().toLowerCase() + "Button]");
        JQueryLocator msgType = logMsg.getChild(jq("span.rf-log-entry-lbl"));
        JQueryLocator msgContent = logMsg.getChild(jq("span.rf-log-entry-msg"));

        if (filterLevel != LogLevel.DEBUG) {
            selenium.select(pjq("select[name$=levelInput]"), optionLabel(filterLevel.toString().toLowerCase()));
            selenium.waitForPageToLoad();
        }

        String selectedLevel = selenium.getSelectedLabel(levelSelect);
        if (filterLevel == LogLevel.NULL) {
            assertEquals(selectedLevel, "info", "Log level in select wasn't changed.");
        } else {
            assertEquals(selectedLevel, filterLevel.toString().toLowerCase(), "Log level in select wasn't changed.");
        }

        selenium.typeKeys(input, logLevel.toString());
        selenium.click(logButton);

        int count = selenium.getCount(logMsg);
        assertEquals(count, filterLevel.ordinal() <= logLevel.ordinal() ? 1 : 0,
                "There should be only one message in log.");

        if (count == 0) {
            return;
        }

        String loggedValue = selenium.getText(msgType).replaceAll(" *\\[.*\\]:$", "");
        assertEquals(loggedValue, logLevel.toString().toLowerCase(), "Message type in log.");
        loggedValue = selenium.getText(msgContent);
        assertEquals(loggedValue, logLevel.toString(), "Message content.");
    }
}
