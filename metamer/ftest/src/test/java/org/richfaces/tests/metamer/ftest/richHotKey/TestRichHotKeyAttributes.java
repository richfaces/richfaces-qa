/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import java.awt.event.KeyEvent;
import java.util.List;

import org.testng.annotations.Test;



/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichHotKeyAttributes extends AbstractRichHotKeyTest {

    @Test
    public void enabledInInput() {
        ATTRIBUTES_FIRST.set(HotKeyAttributes.key, "x");
        // true
        selenium.focus(INPUT_1);
        selenium.keyPressNative(KeyEvent.VK_X);
        checkEvent("hotkey 1", 2);
        clearHotKeyEvents();
        // false
        ATTRIBUTES_FIRST.set(HotKeyAttributes.enabledInInput, false);
        selenium.focus(INPUT_1);
        selenium.keyPressNative(KeyEvent.VK_X);
        checkEvent("hotkey 1", 0);
    }

    @Test
    public void testOnkeydownAndOnkeyup() {
        ATTRIBUTES_FIRST.set(HotKeyAttributes.key, "x");
        selenium.keyPressNative(KeyEvent.VK_X);
        checkEvent("hotkey 1", 2);
        List<String> events = getEvents();
        assertEquals(events.get(0), "hotkey 1 : onkeydown");
        assertEquals(events.get(1), "hotkey 1 : onkeyup");
    }

    @Test
    public void testPreventDefaultFalse() {
        ATTRIBUTES_FIRST.set(HotKeyAttributes.preventDefault, false);
        try {
            testKeyForPreventDefault(KeyEvent.VK_CONTROL, KeyEvent.VK_F, "ctrl+f", 1);
        } finally {
            selenium.keyPressNative(KeyEvent.VK_ESCAPE);
        }
    }

    @Test
    public void testPreventDefaultTrue() {
        testKeyForPreventDefault(KeyEvent.VK_CONTROL, KeyEvent.VK_F, "ctrl+f", 2);
        testKeyForPreventDefault(KeyEvent.VK_CONTROL, KeyEvent.VK_H, "ctrl+h", 2);
        testKeyForPreventDefault(KeyEvent.VK_CONTROL, KeyEvent.VK_U, "ctrl+u", 2);
    }

    @Test
    public void testRendered() {
        ATTRIBUTES_FIRST.set(HotKeyAttributes.rendered, false);
        ATTRIBUTES_FIRST.set(HotKeyAttributes.key, "x");
        selenium.keyPressNative(KeyEvent.VK_X);
        checkEvent("hotkey 1", 0);
    }

    @Test
    public void testSelector() {
        ATTRIBUTES_FIRST.set(HotKeyAttributes.key, "x");
        ATTRIBUTES_FIRST.set(HotKeyAttributes.selector, INPUT_1_LOCATOR);
        selenium.keyPressNative(KeyEvent.VK_X);
        checkEvent("hotkey 1", 0);
        selenium.focus(INPUT_2);
        selenium.keyPressNative(KeyEvent.VK_X);
        checkEvent("hotkey 1", 0);
        selenium.focus(INPUT_1);
        selenium.keyPressNative(KeyEvent.VK_X);
        checkEvent("hotkey 1", 2);
    }

    private void testKeyForPreventDefault(int firstKey, int secondKey, String keyString, int expectedNum) {
        clearHotKeyEvents();
        ATTRIBUTES_FIRST.set(HotKeyAttributes.key, keyString);
        selenium.keyDownNative(firstKey);
        selenium.keyPressNative(secondKey);
        selenium.keyPressNative(secondKey);
        selenium.keyUpNative(firstKey);
        checkEvent("onkeydown", expectedNum);
    }

}
