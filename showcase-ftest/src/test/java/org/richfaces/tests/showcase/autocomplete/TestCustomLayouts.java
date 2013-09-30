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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.page.fragments.impl.autocomplete.RichFacesAutocomplete;
import org.richfaces.tests.page.fragments.impl.autocomplete.SelectOrConfirm;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.autocomplete.page.CustomLayoutsPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestCustomLayouts extends AbstractWebDriverTest {

    @Page
    public CustomLayoutsPage page;

    /* ********************************************************************************************************************
     * Constants *********************************************************************************************************
     * ***********
     */

    private final String EXP_SUGG_AFTER_V_FST_INPUT = "Vermont Montpelier";
    private final String EXP_SUGG_AFTER_ALA_FST_INPUT = "Alabama Montgomery";
    private final String EXP_SUGG_AFTER_V_SC_INPUT = "Vermont - (Montpelier)";
    private final String EXP_SUGG_AFTER_ALA_SC_INPUT = "Alabama - (Montgomery)";

    /* **********************************************************************************************************************
     * Tests *************************************************************************************************************
     * ********
     */

    @Test
    public void testAutocompletionOfInputWithTableLayout() {

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(page.getAutocomplete1(), "v", EXP_SUGG_AFTER_V_FST_INPUT,
            EXP_SUGG_AFTER_V_FST_INPUT.split(" ")[0]);

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(page.getAutocomplete1(), "ala", EXP_SUGG_AFTER_ALA_FST_INPUT,
            EXP_SUGG_AFTER_ALA_FST_INPUT.split(" ")[0]);
    }

    @Test
    public void testAutocompletionOfInputWithDivlayput() {

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(page.getAutocomplete2(), "v", EXP_SUGG_AFTER_V_SC_INPUT,
            EXP_SUGG_AFTER_V_SC_INPUT.split(" ")[0]);

        typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(page.getAutocomplete2(), "ala", EXP_SUGG_AFTER_ALA_SC_INPUT,
            EXP_SUGG_AFTER_ALA_SC_INPUT.split(" ")[0]);

    }

    /* ******************************************************************************************************************
     * Help methods ******************************************************************************************************
     * ************
     */

    private void typeSomethingToInputCheckThePoppupPressEnterCheckTheInputValue(RichFacesAutocomplete autocomplete,
        String whatTotype, String expectedValueInPopup, String expectedValueInInputAfterEnter) {

        SelectOrConfirm suggestions = autocomplete.type(whatTotype);

        String firstSuggestion = autocomplete.advanced().getSuggestionsElements().get(0).getText();
        assertTrue(expectedValueInPopup.equals(firstSuggestion), "The first row of popup should suggest "
            + expectedValueInPopup + " " + "when " + whatTotype + " is typed in input");

        suggestions.select(ChoicePickerHelper.byVisibleText().contains(firstSuggestion));

        String valueInInput = autocomplete.advanced().getInput().getStringValue();
        assertEquals(valueInInput, expectedValueInInputAfterEnter, "The value in input should be different!");

        autocomplete.advanced().getInput().clear();
    }

}
