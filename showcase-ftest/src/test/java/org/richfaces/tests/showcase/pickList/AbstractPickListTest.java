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
package org.richfaces.tests.showcase.pickList;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.javascript.KeyCode;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.orderingList.AbstractOrderingTest;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 */
public class AbstractPickListTest extends AbstractOrderingTest {

    /* *******************************************************************************************
     * Locators ****************************************************************** ************************
     */

    protected JQueryLocator addAllButton = jq(".rf-pick-add-all:eq({0})");
    protected JQueryLocator removeAllButton = jq(".rf-pick-rem-all:eq({0})");

    protected JQueryLocator addButton = jq(".rf-pick-add:eq({0})");
    protected JQueryLocator removeButton = jq(".rf-pick-rem:eq({0})");

    protected JQueryLocator sourceItemsSimple = jq("[id$='SourceItems']:eq(0)");
    protected JQueryLocator targetItemsSimple = jq("[id$='TargetItems']:eq(0)");

    protected String optionToPick = ".rf-pick-opt";
    protected JQueryLocator optionToOrder = jq(targetItemsSimple.getRawLocator() + " " + optionToPick + "{0}");

    /* *************************************************************************************************
     * Tests ********************************************************************* ***************************
     */

    public void testAddAllButtonSimplePickList(boolean... hotKey) {

        String[] availableCities = selenium.getText(sourceItemsSimple).split("\n");

        if ((hotKey.length != 0) && (hotKey[0] == true)) {

            JQueryLocator oneCity = jq(sourceItemsSimple.format(0).getRawLocator() + " " + optionToPick + ":eq(0)");

            selenium.click(oneCity);
            selenium.focus(oneCity);

            selenium.keyPressNative(KeyEvent.VK_END);
        } else {
            selenium.click(addAllButton.format(0));
        }

        checkThatListContains(availableCities, targetItemsSimple);
    }

    public void testRemoveAllButtonSimplePickList(boolean... hotKey) {

        checkRemoveAllButton(0, targetItemsSimple, 0, hotKey);

    }

    public void testRemoveButton(boolean... hotKey) {

        checkRemoveButton(0, 0, hotKey);
    }

    public void testAddButtonSimplePickList(boolean... hotKey) {

        checkAddButton(sourceItemsSimple, addButton.format(0), targetItemsSimple, hotKey);
    }

    public void testFirstButtonOrdering() {

        selenium.click(addAllButton.format(0));

        checkFirstButton(optionToOrder, firstButton.format(0));

    }

    public void testUpButtonOrdering() {

        selenium.click(addAllButton.format(0));

        checkUpButton(optionToOrder, upButton.format(0));
    }

    public void testDownButtonOrdering() {

        selenium.click(addAllButton.format(0));

        checkDownButton(optionToOrder, downButton.format(0));
    }

    public void testLastButtonOrdering() {

        selenium.click(addAllButton.format(0));

        checkLastButton(optionToOrder, lastButton.format(0));
    }

    /* **************************************************************************************************************************
     * Help methods **************************************************************
     * ************************************************************
     */

    /**
     * At first adds all to the list, checks whether someting was added and then removes all and again checks whether it
     * was removed all
     *
     * @param whichRemoveAllButton
     */
    protected void checkRemoveAllButton(int whichAddAllButton, JQueryLocator whichTargetList, int whichRemoveAllButton,
        boolean... hotKey) {

        selenium.click(addAllButton.format(whichAddAllButton));

        areThereSomeSelectedCities(whichTargetList, true);

        if (hotKey.length != 0 && hotKey[0] == true) {

            JQueryLocator oneOption = optionToOrder.format(":eq(0)");

            selenium.click(oneOption);
            selenium.focus(oneOption);

            selenium.keyPressNative(KeyEvent.VK_HOME);
        } else {

            selenium.click(removeAllButton.format(whichRemoveAllButton));
        }

        areThereSomeSelectedCities(whichTargetList, false);
    }

    protected void checkRemoveButton(int whichAddAllButton, int whichRemoveButton, boolean... hotKey) {

        selenium.click(addAllButton.format(whichAddAllButton));

        JQueryLocator cityToRemove = optionToOrder.format("");
        int numberOfCities = selenium.getCount(cityToRemove);

        for (int i = 0; i < numberOfCities; i++) {

            selenium.click(cityToRemove.format("eq(0)"));
            String expectedRemovedCity = selenium.getText(cityToRemove.format("eq(0)"));

            if ((hotKey.length != 0) && (hotKey[0] == true)) {

                selenium.focus(cityToRemove);
                selenium.keyPressNative(Integer.valueOf(KeyCode.LEFT_ARROW.getCode()));
            } else {

                selenium.click(removeButton.format(whichRemoveButton));
            }

            String actualRemovedCity = selenium.getText(jq(sourceItemsSimple.format(0).getRawLocator() + " "
                + optionToPick + ":last"));

            assertEquals(actualRemovedCity, expectedRemovedCity, "Removing the city by does not work");
        }
    }

    /**
     * Check add button, select one city, adds it via add button and checks whether the city is then on selected cities
     * list.
     *
     * @param whichSourceItems
     * @param whichAddButton
     * @param whichTargetItems
     */
    protected void checkAddButton(JQueryLocator whichSourceItems, JQueryLocator whichAddButton,
        JQueryLocator whichTargetItems, boolean... hotKey) {

        JQueryLocator cities = jq(whichSourceItems.getRawLocator() + " " + optionToPick);
        int numberOfCities = selenium.getCount(cities);

        for (int i = 0; i < numberOfCities; i++) {

            JQueryLocator cityToAdd = jq(cities.getRawLocator() + ":eq(0)");
            String cityToAddString = selenium.getText(cityToAdd);

            selenium.click(cityToAdd);

            if (hotKey.length != 0 && hotKey[0] == true) {

                selenium.focus(cityToAdd);
                selenium.keyPressNative(Integer.valueOf(KeyCode.RIGHT_ARROW.getCode()));

            } else {
                selenium.click(whichAddButton);
            }

            JQueryLocator addedCity = jq(whichTargetItems.getRawLocator() + " " + optionToPick + ":contains('"
                + cityToAddString.split(" ")[0] + "')");

            assertTrue(selenium.isElementPresent(addedCity), "The city: " + cityToAddString
                + " should be added to selected cities");
        }
    }

    /**
     * Checks that the list from sample contains everything in the same order as it is in the parameter cities. The list
     * is determined by which list parameter.
     */
    protected void checkThatListContains(String[] cities, JQueryLocator whichList) {

        String[] citiesFromWhichList = selenium.getText(whichList).split("\n");

        assertEquals(cities.length, citiesFromWhichList.length,
            "The number of selected cities is different as it should be!");

        for (int i = 0; i < cities.length; i++) {

            assertEquals(cities[i].trim(), citiesFromWhichList[i].trim(),
                "The order of selected cities is different as should be, or there is other difference!");

        }
    }

    /**
     * Simple test whether the list for instance after selection is not empty
     *
     * @param where
     *            determines which list is checked
     * @param shouldThey
     *            determines whether the list has to be empty or not
     */
    protected void areThereSomeSelectedCities(JQueryLocator where, boolean shouldThey) {

        String cities = selenium.getText(where).trim();

        if (shouldThey) {

            assertFalse(cities.equals(""),
                "Some cities should be selected, in other words they should be in the list: " + where);
        } else {

            assertTrue(cities.equals(""), "There should be no selected cities, in other words in the list: " + where
                + " should be nothing");
        }

    }

}
