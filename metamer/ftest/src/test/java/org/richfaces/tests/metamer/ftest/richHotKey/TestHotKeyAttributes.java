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

import org.openqa.selenium.Keys;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHotKeyAttributes extends AbstractHotKeyTest {

    @Inject
    @Use(empty = false)
    private KeysEnum key;

    public enum KeysEnum {

        CONTROL_Z("ctrl+z"),
        T("t"),
        CTRL_X("ctrl+x"),
        ALT_CONTROL_X("alt+ctrl+x");

        private final String keysString;

        private KeysEnum(String keysString) {
            this.keysString = keysString;
        }

        @Override
        public String toString() {
            return keysString;
        }
    }

    @Test
    public void enabledInInput() {
        // true
        hotkey1.invoke(firstInput.advanced().getInputElement());
        checkEvents(1, 0);
        clearHotKeyEvents();
        // false
        firstHotkeyAttributes.set(HotKeyAttributes.enabledInInput, false);
        hotkey1.invoke(firstInput.advanced().getInputElement());
        checkEvents(0, 0);
    }

    @Test
    @Use(field = "key", enumeration = true)
    public void testKey() {
        firstHotkeyAttributes.set(HotKeyAttributes.key, key.keysString);
        hotkey1.setupHotkey(key.toString());
        hotkey1.invoke();
        checkEvents(1, 0);
        clearHotKeyEvents();
    }

    private void testKeyForPreventDefault(String keyString, int expectedNum) {
        firstHotkeyAttributes.set(HotKeyAttributes.key, keyString);
        hotkey1.setupHotkey(keyString);
        hotkey1.invoke();
        hotkey1.invoke();
        checkEvent("onkeydown", expectedNum);
        clearHotKeyEvents();
    }

    @Test
    public void testOnkeydownOnkeyup() {
        // these events are already binded, they add message to a4j:log on the page
        hotkey1.invoke();
        checkEvents(1, 0);
        hotkey2.invoke();
        checkEvents(1, 1);
    }

    @Test
    public void testPreventDefaultFalse() {
        firstHotkeyAttributes.set(HotKeyAttributes.preventDefault, Boolean.FALSE);
        try {
            testKeyForPreventDefault("ctrl+f", 1);
        } finally {
            firstInput.advanced().getInputElement().sendKeys(Keys.ESCAPE);
        }
    }

    @Test
    public void testPreventDefaultTrue() {
        firstHotkeyAttributes.set(HotKeyAttributes.preventDefault, Boolean.TRUE);
        testKeyForPreventDefault("ctrl+f", 2);
    }

    @Test
    public void testRendered() {
        firstHotkeyAttributes.set(HotKeyAttributes.rendered, false);
        assertNotPresent(hotkey1.advanced().getRootElement(), "Hotkey 1 should not be present on page.");
        assertPresent(hotkey2.advanced().getRootElement(), "Hotkey 2 should be present on page.");
    }

    @Test
    public void testSelector() {
        firstHotkeyAttributes.set(HotKeyAttributes.selector, "input.first-input");
        hotkey1.setupSelector("input.first-input");
        hotkey1.invoke();// invoke on element found by selector
        checkEvents(1, 0);
        hotkey1.invoke(secondInput.advanced().getInputElement());
        checkEvents(1, 0);// no change
        hotkey1.invoke();// invoke on element found by selector
        checkEvents(2, 0);
    }
}