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
package org.richfaces.tests.showcase.autocomplete;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestCustomLayouts extends AbstractAjocadoTest {

    /* *********************************************************************************************************************
     * Locators
     * **********************************************************************************************************
     * **********
     */

    private JQueryLocator inputWithTableLayout = jq("input[type=text]:eq(0)");
    private JQueryLocator inputWithDivLayout = jq("input[type=text]:eq(1)");
    private JQueryLocator firstRowOfSelectOfFirstInput = jq("tr.rf-au-itm:eq(0):visible");
    private JQueryLocator firstRowOfSelectOfSecondInput = jq("div.rf-au-itm:eq(0):visible");

    /* ********************************************************************************************************************
     * Constants
     * *********************************************************************************************************
     * ***********
     */

    private final String EXP_SUGG_AFTER_V_FST_INPUT = "Vermont Montpelier";
    private final String EXP_SUGG_AFTER_ALA_FST_INPUT = "Alabama Montgomery";
    private final String EXP_SUGG_AFTER_V_SC_INPUT = "Vermont - (Montpelier)";
    private final String EXP_SUGG_AFTER_ALA_SC_INPUT = "Alabama - (Montgomery)";

    /* **********************************************************************************************************************
     * Tests
     * *************************************************************************************************************
     * ********
     */

    @Test(groups = { "4.Future" })
    public void testAutocompletionOfInputWithTableLayout() {

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(inputWithTableLayout,
            firstRowOfSelectOfFirstInput, "v", EXP_SUGG_AFTER_V_FST_INPUT, EXP_SUGG_AFTER_V_FST_INPUT.split(" ")[0]);

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(inputWithTableLayout,
            firstRowOfSelectOfFirstInput, "ala", EXP_SUGG_AFTER_ALA_FST_INPUT,
            EXP_SUGG_AFTER_ALA_FST_INPUT.split(" ")[0]);
    }

    @Test(groups = { "4.Future" })
    public void testAutocompletionOfInputWithDivlayput() {

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(inputWithDivLayout,
            firstRowOfSelectOfSecondInput, "v", EXP_SUGG_AFTER_V_SC_INPUT, EXP_SUGG_AFTER_V_SC_INPUT.split(" ")[0]);

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(inputWithDivLayout,
            firstRowOfSelectOfSecondInput, "ala", EXP_SUGG_AFTER_ALA_SC_INPUT,
            EXP_SUGG_AFTER_ALA_SC_INPUT.split(" ")[0]);

    }

    @Test
    public void testFirstInputPopupIsCreatedFromTable() {

        selenium.type(inputWithTableLayout, "a");
        guardXhr(selenium).fireEvent(inputWithTableLayout, Event.KEYPRESS);

        assertTrue(selenium.isElementPresent(firstRowOfSelectOfFirstInput),
            "The poppup with suggestions should be visible!");

        JQueryLocator tdState = jq(firstRowOfSelectOfFirstInput.getRawLocator() + " > td:eq(1)");

        assertTrue(selenium.isElementPresent(tdState), "The popup should be built from table!");

        eraseInput(inputWithTableLayout);
    }

    /* ******************************************************************************************************************
     * Help methods
     * ******************************************************************************************************
     * ************
     */

    private void typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(JQueryLocator input,
        JQueryLocator firstRow, String whatTotype, String expectedValueInPopup, String expectedValueInInputAfterEnter) {

        selenium.type(input, whatTotype);
        guardXhr(selenium).fireEvent(input, Event.KEYPRESS);

        assertTrue(selenium.isElementPresent(firstRow));

        String firstRowContent = selenium.getText(firstRow).trim();

        assertTrue(expectedValueInPopup.equals(firstRowContent), "The first row of popup should suggest "
            + expectedValueInPopup + " " + "when " + whatTotype + " is typed in input");

        guardXhr(selenium).keyPressNative(KeyEvent.VK_ENTER);

        String valueInInput = selenium.getValue(input);

        assertEquals(valueInInput, expectedValueInInputAfterEnter, "The value in input should be different!");

        eraseInput(input);
    }

}
