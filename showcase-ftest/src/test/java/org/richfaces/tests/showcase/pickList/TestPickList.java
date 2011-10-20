/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.richfaces.tests.showcase.orderingList.AbstractOrderingTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestPickList extends AbstractOrderingTest {

	/* *******************************************************************************************
	 * Locators
	 ********************************************************************************************/
	
	JQueryLocator addAllButton = jq(".rf-pick-add-all:eq({0})");
	JQueryLocator removeAllButton = jq(".rf-pick-rem-all:eq({0})");
	
	JQueryLocator addButton = jq(".rf-pick-add:eq({0})");
	JQueryLocator removeButton = jq(".rf-pick-rem:eq({0})");
	
	JQueryLocator sourceItemsSimple = jq("[id$='SourceItems']:eq(0)");
	JQueryLocator sourceItemsWithColumns = jq("[id$='SourceItems']:eq(1)");
	
	JQueryLocator targetItemsSimple = jq("[id$='TargetItems']:eq(0)");
	JQueryLocator targetItemsWithColumns = jq("[id$='TargetItems']:eq(1)");
	
	String optionToPick = ".rf-pick-opt";
	
	/* *************************************************************************************************
	 * Tests
	 **************************************************************************************************/
	
	@Test
	public void testAddAllButtonSimplePickList() {
		
		String[] availableCities = selenium.getText(sourceItemsSimple).split("\n");
		
		selenium.click(addAllButton.format(0));
		
		checkThatListContains(availableCities, targetItemsSimple);
	}
	
	@Test
	public void testRemoveAllButtonSimplePickList() {
		
		checkRemoveAllButton(0, targetItemsSimple, 0);
		
	}
	
	@Test
	public void testAddAllButtonWithColumnsPickList() {
		
		String[] availableCities = selenium.getText(sourceItemsWithColumns).split("\n");
		int numberOfFlagsInAvailableCities = selenium.getCount(jq(sourceItemsWithColumns.getRawLocator() + " img"));
		
		selenium.click(addAllButton.format(1));
		
		areThereSomeSelectedCities(targetItemsWithColumns, true);
		
		checkThatListContains(availableCities, targetItemsWithColumns);
		
		int numberOfFlagsInSelectedCities = selenium.getCount(jq(targetItemsWithColumns.getRawLocator() + " img"));
		
		assertEquals( numberOfFlagsInAvailableCities, numberOfFlagsInSelectedCities, 
				"the number of flags should be the same in the avaiable cities and selected cities!");
	}
	
	@Test
	public void testRemoveAllButtonWithColumnsPickList() {
		
		checkRemoveAllButton(1, targetItemsWithColumns, 1);
	}
	
	@Test
	public void testAddButtonSimplePickList() {
		
		checkAddButton(sourceItemsSimple, addButton.format(0), targetItemsSimple);
	}
	
	@Test
	public void testAddButtonWithColumnsPickList() {
		
		checkAddButton(sourceItemsWithColumns, addButton.format(1), targetItemsWithColumns);
	}
	
	@Test
	public void testOrderingFunction() {
		
		fail("Implement me please after ordering list is implemented, to reuse what is there, thank you");
	}
	
	/* **************************************************************************************************************************
	 * Help methods
	 ****************************************************************************************************************************/
	
	/**
	 * At first adds all to the list, checks whether someting was added and then removes all and again checks whether it was removed all
	 * 
	 * @param whichRemoveAllButton
	 */
	private void checkRemoveAllButton(int whichAddAllButton, JQueryLocator whichTargetList, int whichRemoveAllButton) {
		
		selenium.click(addAllButton.format(whichAddAllButton));
		
		areThereSomeSelectedCities( whichTargetList, true);
		
		selenium.click(removeAllButton.format(whichRemoveAllButton));
		
		areThereSomeSelectedCities(whichTargetList, false);
	}
	
	/**
	 * Check add button, select one city, adds it via add button and checks whether the city is then on selected cities list. 
	 * 
	 * @param whichSourceItems
	 * @param whichAddButton
	 * @param whichTargetItems
	 */
	private void checkAddButton( JQueryLocator whichSourceItems, JQueryLocator whichAddButton, JQueryLocator whichTargetItems ) {
		
		JQueryLocator cities = jq( whichSourceItems.getRawLocator() + " " + optionToPick);
		int numberOfCities = selenium.getCount(cities);
		
		for( int i = 0; i < numberOfCities; i++) {
		
			JQueryLocator cityToAdd = jq(cities.getRawLocator() + ":eq(0)");
			String cityToAddString = selenium.getText(cityToAdd);
			
			selenium.click( cityToAdd );
			
			selenium.click(whichAddButton);
			
			JQueryLocator addedCity = jq( whichTargetItems.getRawLocator() + " " 
					+ optionToPick + ":contains('" + cityToAddString.split(" ")[0] + "')");
			
			assertTrue( selenium.isElementPresent(addedCity), "The city: " + cityToAddString + " should be added to selected cities");
		}
	}
	
	/**
	 * Checks that the list from sample contains everything in the same order as it is in the parameter cities.
	 * The list is determined by which list parameter. 
	 */
	private void checkThatListContains(String[] cities, JQueryLocator whichList) {
		
		String[] citiesFromWhichList = selenium.getText(whichList).split("\n");
		
		assertEquals( cities.length, citiesFromWhichList.length, "The number of selected cities is different as it should be!" );
		
		for( int i = 0; i < cities.length; i++ ) {
			
			assertEquals( cities[i].trim(), citiesFromWhichList[i].trim(), 
					"The order of selected cities is different as should be, or there is other difference!");
			
		}
	}
	
	/**
	 * Simple test whether the list for instance after selection is not empty
	 * 
	 * @param where determines which list is checked
	 * @param shouldThey determines whether the list has to be empty or not
	 */
	private void areThereSomeSelectedCities( JQueryLocator where, boolean shouldThey ) {
		
		String cities = selenium.getText( where ).trim();
		
		if( shouldThey ) {
			
			assertFalse( cities.equals(""), "Some cities should be selected, in other words they should be in the list: " + where);
		} else {
			
			assertTrue( cities.equals(""), 
					"There should be no selected cities, in other words in the list" + where + " should ne nothing");
		}
		
	}
}
