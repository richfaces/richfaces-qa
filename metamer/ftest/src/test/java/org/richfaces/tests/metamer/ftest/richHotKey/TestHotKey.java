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

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHotKey extends AbstractHotKeyTest {

    private static final int NUMBER_OF_TESTS = 3;

    @Test
    public void testFirstAndSecondPair() {
        for (int i = 1; i <= NUMBER_OF_TESTS; i++) {
            for (int j = 0; j < i; j++) {
                pressHotkeyOnElement(HOTKEY_CTRL_X, firstInput.getInput());
                pressHotkeyOnElement(HOTKEY_ALT_X, firstInput.getInput());
            }
            checkEvents(i, i);
            clearHotKeyEvents();
        }
    }

    @Test
    public void testFirstPair() {
        testPair(HOTKEY_CTRL_X, 1);
    }

    private void testPair(String keys, int hotkeyNum) {
        for (int i = 1; i <= NUMBER_OF_TESTS; i++) {
            for (int j = 0; j < i; j++) {
                pressHotkeyOnElement(keys, firstInput.getInput());
            }
            if (hotkeyNum == 1) {
                checkEvents(i, 0);
            } else {//2
                checkEvents(0, i);
            }
            clearHotKeyEvents();
        }
    }

    @Test
    public void testSecondPair() {
        testPair(HOTKEY_ALT_X, 2);
    }
}
