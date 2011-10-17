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
package org.richfaces.tests.showcase.clientValidation;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestJsfValidators extends AbstractClientValidationTest {
	
	/* *************************************************************************************
	 * Locators
	 ***************************************************************************************/
	
	private JQueryLocator nameInput = jq("input[id$=name]");
	private JQueryLocator emailInput = jq("input[id$=email]");
	private JQueryLocator ageInput = jq("input[id$=age]");
	private JQueryLocator errorMessageAboutEmail = jq("span:contains('Invalid email address')");
	private JQueryLocator errorMessageAboutNumber = jq("span:contains('must be a number between')");
	private JQueryLocator errorMessageAboutRange = jq("span:contains('Validation Error: Specified " +
			"attribute is not between the expected values of')");
	private JQueryLocator errorMessageAboutValueRequired = jq("span:contains('Validation Error: Value is required')");
	
	/* ********************************************************************************************************
	 * Constants
	 *******************************************************************************************************/
	
	private final String ERROR_MESSAGE_ABOUT_NAME = 
		"name: Validation Error: Specified attribute is not between the expected values of 3 and 8.";
	
	private final String ERROR_MESSAGE_ABOUT_EMAIL = 
		"Validation error: Invalid email address";
	
	private final String ERROR_MESSAGE_ABOUT_AGE =
		"must be a number between -2147483648 and 2147483647 Example: 9346";
	
	private final String ERROR_MESSAGE_ABOUT_AGE_SIZE = 
		"Validation Error: Specified attribute is not between the expected values of 18 and 99.";
	
	private final String ERROR_MESSAGE_ABOUT_VALUE_REQUIRED = 
		"Validation Error: Value is required.";
	
	/* **************************************************************************************************
	 * Tests
	 ****************************************************************************************************/
	
	@Test
	public void testCorrectNameInput() {
		
		fillNameInputWithCorrectValues(nameInput, 3, 5, 8);
	}
	
	@Test
	public void testCorrectEmail() {
		
		fillEmailInputWithCorrectValues(emailInput);
	}
	
	@Test
	public void testCorrectAge() {
		
		fillAnyInput(ageInput, "18"); //bottom border
		guardNoRequest(selenium).fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(false);
		isThereErrorMessageAboutAgeSize(false);
		
		eraseInput(ageInput);
		
		fillAnyInput(ageInput, "55");
		selenium.fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(false);
		isThereErrorMessageAboutAgeSize(false);
		
		fillAnyInput(ageInput, "99");
		selenium.fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(false);
		isThereErrorMessageAboutAgeSize(false);
	}
	
	/*
	 * Testing filling in incorrect values
	 */
	@Test
	public void testIncorrectName() {

		fillNameInputWithIncorrectValues(nameInput, 3, 8, true);
	}
	
	@Test
	public void testIncorrectEmail() {
		
		fillEmailInputWithIncorrectValues(emailInput);
	}
	
	@Test
	public void testIncorrectAge() {
		
		fillAnyInput(ageInput, "character");
		guardNoRequest(selenium).fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(true);
		isThereErrorMessageAboutAgeSize(false);
		
		eraseInput(ageInput);
		
		fillAnyInput(ageInput, "0");
		selenium.fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(false);
		isThereErrorMessageAboutAgeSize(true);
		
		eraseInput(ageInput);
		
		fillAnyInput(ageInput, " ");
		selenium.fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(true);
		isThereErrorMessageAboutAgeSize(false);
		
		
		eraseInput(ageInput);
		
		fillAnyInput(ageInput, "17");
		selenium.fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(false);
		isThereErrorMessageAboutAgeSize(true);
		
		eraseInput(ageInput);
		
		fillAnyInput(ageInput, "100");
		selenium.fireEvent(ageInput, Event.BLUR);
		isThereErrorMessageAboutAge(false);
		isThereErrorMessageAboutAgeSize(true);
		
		eraseInput(ageInput);
		
		
		
	}
	
	/* *************************************************************************************
	 * Help methods
	 ***************************************************************************************/
	
	/**
	 * Finds out whether particular error message is presented
	 * @param shouldErrorMessagePresented
	 */
	private void isThereErrorMessageAboutAgeSize(boolean shouldErrorMessagePresented) {
		
		isThereErrorMessage(errorMessageAboutRange, ERROR_MESSAGE_ABOUT_AGE_SIZE, shouldErrorMessagePresented);
	}
	
	/**
	 * Finds out whether particular error message is presented
	 * @param shouldErrorMessagePresented
	 */
	private void isThereErrorMessageAboutAge(boolean shouldErrorMessagePresented) {
		
		isThereErrorMessage(errorMessageAboutNumber, ERROR_MESSAGE_ABOUT_AGE, shouldErrorMessagePresented);
	}
	
	/**
	 * Finds out whether particular error message is presented
	 * @param shouldErrorMessagePresented
	 */
	@Override
	protected void isThereErrorMessageAboutEmail(boolean shouldErrorMessagePresented) {
		
		isThereErrorMessage(errorMessageAboutEmail, ERROR_MESSAGE_ABOUT_EMAIL, shouldErrorMessagePresented);
	}
	
	/**
	 * Finds out whether particular error message is presented
	 * @param shouldErrorMessagePresented
	 */
	@Override
	protected void isThereErrorMessageAboutSizeOfName(boolean shouldBeErrorMessagePresneted) {
		
		isThereErrorMessage(errorMessageAboutRange, ERROR_MESSAGE_ABOUT_NAME, shouldBeErrorMessagePresneted);
	}
	
	@Override
	protected void isThereErrorMessageAboutValueRequeired(boolean shouldBeErrorMessagePresented) {
		
		isThereErrorMessage(errorMessageAboutValueRequired, ERROR_MESSAGE_ABOUT_VALUE_REQUIRED, shouldBeErrorMessagePresented);
	}
	
	

}
