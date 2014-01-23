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
package org.richfaces.tests.metamer.ftest.a4jLog;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.fragment.common.ClearType.DELETE;
import static org.richfaces.fragment.log.Log.LogEntryLevel.INFO;
import static org.richfaces.fragment.log.Log.LogEntryLevel.values;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.log.Log.LogEntryLevel;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jLog/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class TestLog extends AbstractWebDriverTest {

    @FindBy(css = "input[id$=nameInput]")
    private TextInputComponentImpl input;
    @FindBy(css = "input[id$=submitButton]")
    private WebElement submitButton;
    @FindBy(css = "span[id$=out]")
    private WebElement output;
    @FindBy(css = "input[id$=debugButton]")
    private WebElement debugButton;
    @FindBy(css = "input[id$=infoButton]")
    private WebElement infoButton;
    @FindBy(css = "input[id$=warnButton]")
    private WebElement warnButton;
    @FindBy(css = "input[id$=errorButton]")
    private WebElement errorButton;
    @FindBy(css = "div.rf-log select")
    private Select levelSelect;
    @FindBy(css = "div.rf-log")
    private RichFacesLog log;

    private final Attributes<LogAttributes> attributes = getAttributes();

    private LogEntryLevel levelToSet;
    private Boolean setLevelByAttribute;

    private void checkForEachLevel() {
        for (LogEntryLevel levelToTrigger : values()) {
            log.clear();
            input.advanced().clear(DELETE).sendKeys(levelToTrigger.toString());
            triggerMessage(levelToTrigger);
            assertEquals(log.getLogEntries().size(), levelToTrigger.ordinal() >= levelToSet.ordinal() ? 1 : 0);
            if (!log.getLogEntries().isEmpty()) {
                assertEquals(log.getLogEntries().getItem(0).getLevel(), levelToTrigger, "Message type in log.");
                assertEquals(log.getLogEntries().getItem(0).getContent(), levelToTrigger.toString(), "Message content.");
            }
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jLog/simple.xhtml");
    }

    private void submit() {
        Graphene.guardAjax(submitButton).click();
    }

    @Test
    @Templates(value = "plain")
    public void testClearButton() {
        testSubmit();
        log.clear();
        assertTrue(log.getLogEntries().isEmpty(), "There should be no messages in log after clear button was clicked.");
    }

    @Test(groups = "smoke")
    @Templates(value = "plain")
    @Uses({
        @UseWithField(field = "levelToSet", valuesFrom = FROM_ENUM, value = ""),
        @UseWithField(field = "setLevelByAttribute", valuesFrom = FROM_FIELD, value = "booleans")
    })
    public void testLevel() {
        if (setLevelByAttribute) {
            attributes.set(LogAttributes.level, levelToSet.toString().toLowerCase());
        } else {
            log.changeLevel(levelToSet);
        }

        String selectedLevel = levelSelect.getFirstSelectedOption().getText();
        assertEquals(selectedLevel, levelToSet.toString().toLowerCase(), "Log level in select wasn't changed.");
        checkForEachLevel();
    }

    @Test
    @Templates(value = "plain")
    public void testLevelDefault() {
        levelToSet = INFO;
        attributes.set(LogAttributes.level, "");

        String selectedLevel = levelSelect.getFirstSelectedOption().getText();
        assertEquals(selectedLevel, levelToSet.toString().toLowerCase(), "Log level in select wasn't changed.");
        checkForEachLevel();
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        attributes.set(LogAttributes.rendered, false);
        assertNotVisible(log.advanced().getRootElement(), "Log should be not rendered.");
    }

    @Test
    public void testSubmit() {
        input.advanced().clear(ClearType.DELETE).sendKeys("RichFaces 4");
        submit();

        Graphene.waitGui().until().element(output).text().equalTo("Hello RichFaces 4!");

        assertTrue(log.getLogEntries().size() > 0,
            "There should be at least one message in log after submit button was clicked.");
    }

    @Test
    public void testSubmitUnicode() {
        input.advanced().clear(DELETE).sendKeys("ľščťžýáíéôúäň");
        submit();

        Graphene.waitGui().until().element(output).text().equalTo("Hello ľščťžýáíéôúäň!");

        assertTrue(log.getLogEntries().size() > 0,
            "There should be at least one message in log after submit button was clicked.");
    }

    private void triggerMessage(LogEntryLevel levelToTrigger) {
        switch (levelToTrigger) {
            case DEBUG:
                debugButton.click();
                break;
            case INFO:
                infoButton.click();
                break;
            case WARN:
                warnButton.click();
                break;
            case ERROR:
                errorButton.click();
                break;
            default:
                break;
        }
    }
}
