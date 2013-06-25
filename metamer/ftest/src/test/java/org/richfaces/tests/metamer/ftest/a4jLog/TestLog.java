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
package org.richfaces.tests.metamer.ftest.a4jLog;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.logAttributes;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jLog/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class TestLog extends AbstractWebDriverTest {

    @Page
    private LogPage page;

    /**
     * Enumeration representing all possible levels for a4j:log.
     */
    public enum LogLevel {
        DEBUG, NULL, INFO, WARN, ERROR;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jLog/simple.xhtml");
    }

    @Test
    public void testSubmit() {
        page.input.clear();
        page.input.sendKeys("RichFaces 4");
        page.submitButton.click();

        Graphene.waitGui().until().element(page.output).text().equalTo("Hello RichFaces 4!");

        assertTrue(page.logMsg.size() > 0,
            "There should be at least one message in log after submit button was clicked.");
    }

    @Test
    public void testSubmitUnicode() {
        page.input.clear();
        page.input.sendKeys("ľščťžýáíéôúäň");
        page.submitButton.click();

        Graphene.waitGui().until().element(page.output).text().equalTo("Hello ľščťžýáíéôúäň!");

        assertTrue(page.logMsg.size() > 0,
            "There should be at least one message in log after submit button was clicked.");
    }

    @Test
    public void testClearButton() {
        testSubmit();

        // test clear
        page.clearButton.click();
        assertEquals(page.logMsg.size(), 0, "There should be no messages in log after clear button was clicked.");
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        logAttributes.set(LogAttributes.rendered, false);
        try {
            page.log.isDisplayed();
            fail("Log should not be rendered when rendered=false.");
        } catch (NoSuchElementException ex) {
            // expected
        }
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
        if (filterLevel != LogLevel.DEBUG) {
            logAttributes.set(LogAttributes.level, filterLevel.toString().toLowerCase());
        }

        String selectedLevel = (new Select(page.levelSelect)).getFirstSelectedOption().getText();
        if (filterLevel == LogLevel.NULL) {
            assertEquals(selectedLevel, "info", "Log level in select wasn't changed.");
        } else {
            assertEquals(selectedLevel, filterLevel.toString().toLowerCase(), "Log level in select wasn't changed.");
        }

        page.input.clear();
        page.input.sendKeys(logLevel.toString());
        switch (logLevel) {
            case DEBUG:
                page.debugButton.click();
                break;
            case INFO:
                page.infoButton.click();
                break;
            case WARN:
                page.warnButton.click();
                break;
            case ERROR:
                page.errorButton.click();
                break;
            default:
                break;
        }

        assertEquals(page.logMsg.size(), filterLevel.ordinal() <= logLevel.ordinal() ? 1 : 0,
            "There should be only one message in log.");

        if (page.logMsg.size() == 0) {
            return;
        }

        String loggedValue = page.msgType.get(0).getText().replaceAll(" *\\[.*\\]:$", "");
        assertEquals(loggedValue, logLevel.toString().toLowerCase(), "Message type in log.");
        assertEquals(page.msgContent.get(0).getText(), logLevel.toString(), "Message content.");
    }
}
