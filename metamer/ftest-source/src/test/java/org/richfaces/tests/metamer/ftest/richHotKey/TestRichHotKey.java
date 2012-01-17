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

import java.awt.event.KeyEvent;

import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichHotKey extends AbstractRichHotKeyTest {

    private static final int NUMBER_OF_TESTS = 5;
    
    @Test
    public void testFirstAndSecondPair() {
        for (int i=1; i<=NUMBER_OF_TESTS; i++) {
            for (int j=0; j<i; j++) {
                selenium.keyPressNative(KeyEvent.VK_CONTROL);
                selenium.keyPressNative(KeyEvent.VK_X);
                selenium.keyDownNative(KeyEvent.VK_CONTROL);
                selenium.keyPressNative(KeyEvent.VK_X);
                selenium.keyUpNative(KeyEvent.VK_CONTROL);
                selenium.keyPressNative(KeyEvent.VK_ALT);
                selenium.keyPressNative(KeyEvent.VK_X);
                selenium.keyDownNative(KeyEvent.VK_ALT);
                selenium.keyPressNative(KeyEvent.VK_X);
                selenium.keyUpNative(KeyEvent.VK_ALT);                
            }
            checkEvent("hotkey 1 : onkeydown", i);
            checkEvent("hotkey 2 : onkeydown", i);
            checkEvent("'onkeydown'", 2 * i);
            clearHotKeyEvents();
        }
    }
    
    @Test
    public void testFirstOne() {
        ATTRIBUTES_FIRST.set(HotKeyAttributes.key, "x");
        for (int i=1; i<=NUMBER_OF_TESTS; i++) {
            for (int j=0; j<i; j++) {
                selenium.keyPressNative(KeyEvent.VK_X);
                selenium.keyPressNative(KeyEvent.VK_A);
            }
            checkEvent("hotkey 1 : onkeydown", i);
            checkEvent("onkeydown", i);
            clearHotKeyEvents();
        }
    }
    
    @Test
    public void testFirstPair() {
        testPair(KeyEvent.VK_CONTROL, KeyEvent.VK_X, "hotkey 1 : onkeydown");
    }
    
    @Test
    public void testSecondPair() {
        testPair(KeyEvent.VK_ALT, KeyEvent.VK_X, "hotkey 2 : onkeydown");
    }

    private void testPair(int firstKey, int secondKey, String text) {
        for (int i=1; i<=NUMBER_OF_TESTS; i++) {
            for (int j=0; j<i; j++) {
                selenium.keyPressNative(firstKey);
                selenium.keyPressNative(secondKey);
                selenium.keyDownNative(firstKey);
                selenium.keyPressNative(secondKey);
                selenium.keyUpNative(firstKey);
            }
            checkEvent(text, i);
            checkEvent("onkeydown", i);
            clearHotKeyEvents();
        }        
    }    
    
}
