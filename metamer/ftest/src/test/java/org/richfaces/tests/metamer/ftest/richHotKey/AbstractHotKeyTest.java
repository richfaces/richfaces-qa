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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.hotkey.RichFacesHotkey;
import org.richfaces.fragment.log.LogEntry;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richHotKey.TestHotKeyAttributes.KeysEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;

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
    protected RichFacesHotkey hotkey1;
    @FindBy(css = "span[id$=richHotKey2]")
    protected RichFacesHotkey hotkey2;
    @FindBy(css = "div[id='richfaces.log']")
    protected RichFacesLog log;
    @FindBy(tagName = "body")
    protected WebElement body;
    //
    protected Attributes<HotKeyAttributes> firstHotkeyAttributes = getAttributes("attributes1");
    protected Attributes<HotKeyAttributes> secondHotkeyAttributes = getAttributes("attributes2");
    //
    private static final String ONKEYDOWN_TEMPLATE = "hotkey %d : onkeydown";
    private static final String ONKEYUP_TEMPLATE = "hotkey %d : onkeyup";

    protected void clearHotKeyEvents() {
        log.clear();
    }

    protected void checkEvent(String text, int number) {
        List<? extends LogEntry> items = log.getLogEntries().getItems(ChoicePickerHelper.byVisibleText().contains(text));
        assertEquals(items.size(), number, "The number of hotkey events doesn't match.");
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

    @BeforeMethod
    public void setHotKeyAndSelector() {
        hotkey1.setupHotkey(KeysEnum.CTRL_X.toString());
        hotkey2.setupHotkey("alt+x");
    }
}
