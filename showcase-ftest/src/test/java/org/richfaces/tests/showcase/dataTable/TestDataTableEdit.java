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
package org.richfaces.tests.showcase.dataTable;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestDataTableEdit extends AbstractDataIterationWithCars {
	
	private JQueryLocator deleteButtonInpopup = jq("input[value=Delete]:visible");
	private JQueryLocator cancelButtonInpopup = jq("input[value=Cancel]:visible");
	private JQueryLocator storeButtonInpopup = jq("input[value=Store]:visible");
	
	private JQueryLocator vendorpopup = jq("table[id$=editGrid] tbody tr:eq(0) td:eq(1)");
	private JQueryLocator modelpopup = jq("table[id$=editGrid] tbody tr:eq(1) td:eq(1)");
	private JQueryLocator priceInputpopup = jq("input[id$=price]");
	private JQueryLocator mileageInputpopup = jq("input[id$=mage]");
	private JQueryLocator vinInputpopup = jq("input[id$=vin]");
	
	private JQueryLocator errorMsgPrice = jq("span[id$=price] span");
	private JQueryLocator errorMsgMileage = jq("span[id$=mage] span");
	private JQueryLocator errorMsgVin = jq("span[id$=vin] span");
	
	private JQueryLocator table = jq("tbody[class=rf-dt-b]");
	
	/* *****************************************************************************************************************
	 *Constants
	 *******************************************************************************************************************/
	
	private final String ERROR_MSG_PRICE = "Should be a valid price";
	private final String ERROR_MSG_MILEAGE = "Should be a valid mileage";
	private final String ERROR_MSG_VIN = "Not a valid 17-digit VIN";
	private final String ERROR_MSG_PRICE_RQ = "Price is required";
	private final String ERROR_MSG_VIN_RQ = "VIN is required";
	
	/* *************************************************************************************************************************
	 *Tests
	 **************************************************************************************************************************/
	
	@Test
	public void testDeleteCarCancelButton() {
		
		JQueryLocator row = table.getChild(jq("tr:eq(0)"));
		JQueryLocator rowDeleteButton = row.getChild(jq("td a:first"));
		
		Car car = retrieveCarFromRow(row, 1, 5);
		
		guardXhr(selenium).click(rowDeleteButton);
		
		guardNoRequest(selenium).click(cancelButtonInpopup);
		
		assertTrue(containsTableParticularCar(car), "The table should contain this car " + car + ", since there was click on the cancel button");
		
	}
	
	@Test
	public void testDeleteCarDeleteButton() {
		
		Car car = deleteCarFromRow(0);
		
		assertFalse(containsTableParticularCar(car), "The car " + car + " should be deleted");
		
		car = deleteCarFromRow(10);
		
		assertFalse(containsTableParticularCar(car), "The car " + car + " should be deleted");
		
		car = deleteCarFromRow(5);
		
		assertFalse(containsTableParticularCar(car), "The car" + car + " should be deleted");
	}
	
	@Test
	public void testInsertCarCancelButton() {
		
		Car carWhichWillNotBeChanged = callTheInsertpopup(7);
		
		eraseInput(priceInputpopup);
		eraseInput(mileageInputpopup);
		eraseInput(vinInputpopup);
		
		fillAnyInput(priceInputpopup, "1");
		fillAnyInput(mileageInputpopup, "2");
		fillInputWithStringOfLength(vinInputpopup, 17);
		
		guardNoRequest(selenium).click(cancelButtonInpopup);
		
		assertTrue(containsTableParticularCar(carWhichWillNotBeChanged), "This car should be in the table, since " +
				"the cancel button was pressed!");
	}
	
	@Test
	public void testInsertCarCheckThepopupCarInformation() {
		
		checkTheCarInThepopupAccordingToTheTable(0);
		
		checkTheCarInThepopupAccordingToTheTable(5);
		
		checkTheCarInThepopupAccordingToTheTable(10);
	}
	
	@Test
	public void testPopupValidationStringInsteadOfDouble() {
		
		callTheInsertpopup(2);
		
		fillInputWithStringOfLength(priceInputpopup, 5);
		fillInputWithStringOfLength(mileageInputpopup, 5);
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		isThereErrorMessage(errorMsgPrice, ERROR_MSG_PRICE, true);
		isThereErrorMessage(errorMsgMileage, ERROR_MSG_MILEAGE, true);
	
		guardNoRequest(selenium).click(cancelButtonInpopup);
	}
	
	@Test
	public void testPopupValidationTooBigNumber() {
	
		callTheInsertpopup(3);
		
		fillAnyInput(priceInputpopup, "1111111111111111111111111111111111111111111");
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		isThereErrorMessage(errorMsgPrice, ERROR_MSG_PRICE, true);
		
		guardNoRequest(selenium).click(cancelButtonInpopup);
	}
	
	@Test
	public void testPopupValidationValuesRequired() {
	
		callTheInsertpopup(1);
		
		eraseInput(priceInputpopup);
		eraseInput(vinInputpopup);
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		isThereErrorMessage(errorMsgPrice, ERROR_MSG_PRICE_RQ, true);
		isThereErrorMessage(errorMsgVin, ERROR_MSG_VIN_RQ, true);
		
		guardNoRequest(selenium).click(cancelButtonInpopup);
	}
		
	@Test
	public void testPopupValidationVinLength() {	
	
		callTheInsertpopup(4);
		
		fillInputWithStringOfLength(vinInputpopup, 16);
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		isThereErrorMessage(errorMsgVin, ERROR_MSG_VIN, true);
		
		eraseInput(vinInputpopup);
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		isThereErrorMessage(errorMsgVin, ERROR_MSG_VIN_RQ, true);
		
		fillInputWithStringOfLength(vinInputpopup, 18);
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		isThereInfoMessage(errorMsgVin, ERROR_MSG_VIN, true);
		
		guardNoRequest(selenium).click(cancelButtonInpopup);
		
	}
	
	@Test
	public void testPopupValidationNegativeValues() {
		
		callTheInsertpopup(5);
		
		fillAnyInput(priceInputpopup, "-1");
		fillAnyInput(mileageInputpopup, "-1");
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		isThereErrorMessage(errorMsgPrice, ERROR_MSG_PRICE, true);
		isThereErrorMessage(errorMsgMileage, ERROR_MSG_MILEAGE, true);
		
		guardNoRequest(selenium).click(cancelButtonInpopup);
	}
	
	@Test
	public void testPopupCorrectValues() {
		
		Car carWhichWillBeChanged = callTheInsertpopup(6);
		
		eraseInput(priceInputpopup);
		eraseInput(mileageInputpopup);
		eraseInput(vinInputpopup);
		
		fillAnyInput(priceInputpopup, "1");
		fillAnyInput(mileageInputpopup, "2");
		fillInputWithStringOfLength(vinInputpopup, 17);
		
		Car alteredCar = retrieveCarFrompopup();
		
		guardXhr(selenium).click(storeButtonInpopup);
		
		assertTrue(containsTableParticularCar(alteredCar), "The table should contains now the altered car");
		assertFalse(containsTableParticularCar(carWhichWillBeChanged), "This car should not be in the table" +
				", since it was changed");
	}
	
	/* *************************************************************************************************************************
	 * help methods
	 ***************************************************************************************************************************/
	/**
	 * 
	 * Finds out whether the table contains a row with an information about particular car
	 * 
	 * @param car this is the car for which method finds out if it is in the table
	 * @return true if the car is in the table, false otherwise
	 */
	private boolean containsTableParticularCar( Car car ) {
		
		JQueryLocator row = jq(table.getRawLocator() + " > tr");
		
		for( Iterator<JQueryLocator> i = row.iterator(); i.hasNext(); ) {
			
			Car actualCar = retrieveCarFromRow(i.next(), 1, 5);
			
			if( actualCar.equals(car) ) {
				return true;
			}
		}
		
		return false;
	
	}
	
	/**
	 * Deletes a row with particular index and returns the car
	 * 
	 * @param indexOfRow the index of rowin the table, starting from 0
	 * @return the car which was deleted
	 */
	private Car deleteCarFromRow( int indexOfRow ) {
		
		JQueryLocator row = returnRow(indexOfRow);
		JQueryLocator rowDeleteButton = row.getChild(jq("td a:first"));
		
		Car car = retrieveCarFromRow(row, 1, 5);
		
		guardXhr(selenium).click(rowDeleteButton);
		
		guardXhr(selenium).click(deleteButtonInpopup);
		
		return car;
	}
	
	/**
	 * Finds the locator of the row with the indexOfRow
	 * 
	 * @param indexOfRow the index of row in the table, starting from 0
	 */
	private JQueryLocator returnRow( int indexOfRow ) {
		
		JQueryLocator row = table.getChild(jq("tr:eq(" + indexOfRow + ")"));
		
		return row;
	}
	
	/**
	 * Retrieves car from the popup
	 * 
	 * @return car which is in the popup
	 */
	private Car retrieveCarFrompopup() {
		
		Car car = new Car();
		
		car.setVendor(selenium.getText(vendorpopup));
		car.setModel(selenium.getText(modelpopup));
		car.setPrice(selenium.getValue(priceInputpopup));
		car.setMileage(selenium.getValue(mileageInputpopup));
		car.setVin(selenium.getValue(vinInputpopup));
		
		return car;
	}
	
	/**
	 * Checks whether the information about car is the same as the information in the table
	 * 
	 * @param indexOfRow the row which will be checked in the popup
	 */
	private void checkTheCarInThepopupAccordingToTheTable( int indexOfRow ) {
		
		Car car = callTheInsertpopup(indexOfRow);
		
		Car carFrompopup = retrieveCarFrompopup();
		
		assertEquals(carFrompopup, car, "The car information differs in the popup!");
		
		guardNoRequest(selenium).click(cancelButtonInpopup);
	}
	
	/**
	 * Calls the insert popup menu for the particular row
	 * 
	 * @param indexOfRow the row from which the menu will be called
	 */
	private Car callTheInsertpopup( int indexOfRow ) {
		
		JQueryLocator row = returnRow(indexOfRow);
		Car car = retrieveCarFromRow(row, 1, 5);
		JQueryLocator rowInsertButton = row.getChild(jq("td a:last"));
		
		guardXhr(selenium).click(rowInsertButton);
		
		return car;
	}
	
	
	
	

}
