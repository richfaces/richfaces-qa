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
package org.richfaces.tests.showcase.autocomplete;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestModes extends AbstractGrapheneTest {

    /* **************************************************************************************
     * Locators**************************************************************************************
     */

    private JQueryLocator minCharInput = jq("input[type=text]:eq(0)");
    private JQueryLocator multipleSelectionInput = jq("input[type=text]:eq(1)");
    private JQueryLocator selectFirstFalseInput = jq("input[type=text]:eq(2)");
    private JQueryLocator selection = jq("div.rf-au-itm");

    /* **************************************************************************************
     * Tests**************************************************************************************
     */

    @Test
    public void testMinCharInput() {

        selenium.keyPress(minCharInput, 'a');

        assertFalse(selenium.isElementPresent(selection), "The selection should not be visible, since there is only one char!");

        String keys = "ar";
        selenium.focus(minCharInput);
        selenium.type(minCharInput, keys);
        guardXhr(selenium).fireEvent(minCharInput, Event.KEYPRESS);

        assertTrue(selenium.isVisible(selection), "The selection should be visible, since there are two chars!");

        String actualArizona = selenium.getText(jq(selection.getRawLocator() + ":eq(0)"));
        assertEquals(actualArizona, "Arizona", "The provided option should be Arizona");

        String actualArkansas = selenium.getText(jq(selection.getRawLocator() + ":eq(1)"));
        assertEquals(actualArkansas, "Arkansas", "The provided option should be Arkansas");

    }

    @Test(groups = "4.Future")
    public void testMultipleSelectionInput() {

        char key = 'a';
        selenium.focus(multipleSelectionInput);
        guardXhr(selenium).keyPress(multipleSelectionInput, key);

        assertTrue(selenium.isVisible(selection), "The selection should be visible, since there is correct starting char!");

        selenium.keyPressNative(KeyEvent.VK_ENTER);

        key = ' ';
        selenium.keyPress(multipleSelectionInput, key);

        key = 'w';

        selenium.focus(multipleSelectionInput);
        guardXhr(selenium).keyPress(multipleSelectionInput, key);

        assertTrue(selenium.isVisible(selection), "The selection should be visible, since there is correct starting char!");

        selenium.keyPressNative(KeyEvent.VK_ENTER);

        String actualContentOfInput = selenium.getValue(multipleSelectionInput);
        assertEquals(actualContentOfInput, "Alabama Washington", "The input should contain something else!");
    }

    @Test(groups = "4.Future")
    public void testSelectFirstFalseInput() {

        char key = 'a';
        selenium.focus(selectFirstFalseInput);
        guardXhr(selenium).keyPress(selectFirstFalseInput, key);

        assertTrue(selenium.isVisible(selection), "The selection should be visible, since there is correct starting char!");

        selenium.keyPressNative(KeyEvent.VK_ENTER);

        String actualValueOfInput = selenium.getValue(selectFirstFalseInput);
        assertEquals(actualValueOfInput, "a",
            "The content should be the letter which was types and not something else, since selectFirst is false!");
    }

}
