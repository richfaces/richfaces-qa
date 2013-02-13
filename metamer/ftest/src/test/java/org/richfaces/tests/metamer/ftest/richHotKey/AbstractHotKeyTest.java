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
package org.richfaces.tests.metamer.ftest.richHotKey;

import static org.testng.Assert.assertEquals;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import com.google.common.base.Predicate;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.log.Log.LogEntryLevel;
import org.richfaces.tests.page.fragments.impl.log.LogEntries;
import org.richfaces.tests.page.fragments.impl.log.RichFacesLog;
import org.richfaces.tests.page.fragments.impl.log.RichFacesLogFilterBuilder;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractHotKeyTest extends AbstractWebDriverTest {

    @FindBy(css = "input.first-input")
    protected TextInputComponentImpl firstInput;
    @FindBy(css = "input.second-input")
    protected TextInputComponentImpl secondInput;
    @FindBy(css = "span[id$=richHotKey1]")
    protected WebElement hotkey1;
    @FindBy(css = "span[id$=richHotKey2]")
    protected WebElement hotkey2;
    @FindBy(css = "div[id='richfaces.log']")
    protected RichFacesLog log;
    @FindBy(tagName = "body")
    protected WebElement body;
    //
    protected static final Attributes<HotKeyAttributes> ATTRIBUTES_FIRST = new Attributes<HotKeyAttributes>("attributes1");
    protected static final Attributes<HotKeyAttributes> ATTRIBUTES_SECOND = new Attributes<HotKeyAttributes>("attributes2");
    //
    private static final String ONKEYDOWN_TEMPLATE = "hotkey %d : onkeydown";
    private static final String ONKEYUP_TEMPLATE = "hotkey %d : onkeyup";
    protected static final String HOTKEY_CTRL_X = Keys.chord(Keys.CONTROL, "x");
    protected static final String HOTKEY_ALT_X = Keys.chord(Keys.ALT, "x");

    protected void clearHotKeyEvents() {
        log.clear();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return log.getLogEntries(LogEntryLevel.ALL).isEmpty();
            }
        });
    }

    protected void checkEvent(String text, int number) {
        LogEntries filter = log.getLogEntries(LogEntryLevel.INFO)
                .filter(new RichFacesLogFilterBuilder().filterToContentContains(text));
        int logEntriesWithContent = filter.size();
        assertEquals(logEntriesWithContent, number, "The number of hotkey events doesn't match.");
    }

    protected void checkEvents(int expectedCountOfEvents1, int expectedCountOfEvents2) {
        checkEvent(String.format(ONKEYDOWN_TEMPLATE, 1), expectedCountOfEvents1);
        checkEvent(String.format(ONKEYUP_TEMPLATE, 1), expectedCountOfEvents1);
        checkEvent(String.format(ONKEYDOWN_TEMPLATE, 2), expectedCountOfEvents2);
        checkEvent(String.format(ONKEYUP_TEMPLATE, 2), expectedCountOfEvents2);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richHotKey/simple.xhtml");
    }

    public void pressHotkeyOnElement(String hotkey, WebElement element) {
        new Actions(driver).sendKeys(element, hotkey).perform();
    }
}
