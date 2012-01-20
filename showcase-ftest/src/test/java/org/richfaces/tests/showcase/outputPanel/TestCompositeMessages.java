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
package org.richfaces.tests.showcase.outputPanel;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestCompositeMessages extends AbstractAjocadoTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator userNameInput = jq("input[type=text]:first");
    protected JQueryLocator addressInput = jq("textarea:first");
    protected JQueryLocator button = jq("input[type=button]");
    protected JQueryLocator errorMessageStartWithName = jq("li:textStartsWith('Name:')");
    protected JQueryLocator errorMessageStartWithAddress = jq("li:textStartsWith('Address:')");
    protected JQueryLocator userSuccessfullyAdded = jq("span[id$=out]");

    /* *****************************************************************************************************
     * Constants*****************************************************************************************************
     */

    private final int LENGTH_OF_USER_NAME_WRONG_LESS_THAN_MINIMAL = 2;
    private final int LENGTH_OF_USER_NAME_WRONG_EMPTY = 0;
    private final int LENGTH_OF_USER_NAME_WRONG_TOO_LONG = 13;
    private final int LENGTH_OF_USER_NAME_CORRECT_MIDDLE_VALUE = 7;
    private final int LENGTH_OF_USER_NAME_CORRECT_BOTTOM_BORDER_VALUE = 3;
    private final int LENGTH_OF_USER_NAME_CORRECT_UPPER_BORDER_VALUE = 12;
    private final int LENGTH_OF_ADDRESS_WRONG_EMPTY = 0;
    private final int LENGTH_OF_ADDRESS_WRONG_TOO_LONG = 101;
    private final int LENGTH_OF_ADDRESS_CORRECT_MIDDLE_VALUE = 30;
    private final int LENGTH_OF_ADDRESS_CORRECT_BOTTOM_BORDER_VALUE = 1;
    private final int LENGTH_OF_ADDRESS_CORRECT_UPPER_BORDER_VALUE = 100;

    public static final String ERROR_MESSAGE_NAME_LENGTH_TOO_SHORT_BUT_NOT_EMPTY = "Name: Validation Error: Value is less than allowable minimum of '3'";

    public static final String ERROR_MESSAGE_NAME_TOO_LONG = "Name: Validation Error: Value is greater than allowable maximum of '12'";

    public static final String ERROR_MESSAGE_NAME_EMPTY = "Name: Validation Error: Value is required.";

    public static final String ERROR_MESSAGE_ADDRESS_TOO_LONG = "Address: Validation Error: Value is greater than allowable maximum of '100'";

    public static final String ERROR_MESSAGE_ADDRESS_EMPTY = "Address: Validation Error: Value is required.";

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testDataAboutUserAreFilledCorrectly() {
        // minimal values
        fillUseDataCorrectlyAndCheckErrorMessages(LENGTH_OF_USER_NAME_CORRECT_BOTTOM_BORDER_VALUE,
            LENGTH_OF_ADDRESS_CORRECT_BOTTOM_BORDER_VALUE);

        // middle values
        fillUseDataCorrectlyAndCheckErrorMessages(LENGTH_OF_USER_NAME_CORRECT_MIDDLE_VALUE,
            LENGTH_OF_ADDRESS_CORRECT_MIDDLE_VALUE);

        // maximal values
        fillUseDataCorrectlyAndCheckErrorMessages(LENGTH_OF_USER_NAME_CORRECT_UPPER_BORDER_VALUE,
            LENGTH_OF_ADDRESS_CORRECT_UPPER_BORDER_VALUE);

    }

    @Test(groups = { "4.2" })
    public void testUserNameFilledIncorrectlyMoreThan12() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_TOO_LONG,
            LENGTH_OF_ADDRESS_CORRECT_MIDDLE_VALUE);

        isErrorMessageStartingWithNamePresent(true);

        isErrorMessageStartingWithAddressPresent(false);

        String actualErrorMessage = selenium.getText(errorMessageStartWithName).trim();

        assertEquals(actualErrorMessage, ERROR_MESSAGE_NAME_TOO_LONG);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test(groups = { "4.2" })
    public void testUserNameFilledIncorrectlyLessThan3MoreThan0() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_LESS_THAN_MINIMAL,
            LENGTH_OF_ADDRESS_CORRECT_MIDDLE_VALUE);

        isErrorMessageStartingWithNamePresent(true);

        isErrorMessageStartingWithAddressPresent(false);

        String actualErrorMessage = selenium.getText(errorMessageStartWithName);

        assertEquals(actualErrorMessage, ERROR_MESSAGE_NAME_LENGTH_TOO_SHORT_BUT_NOT_EMPTY);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test
    public void testUserNameFilledIncorrectlyLength0() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_EMPTY, LENGTH_OF_ADDRESS_CORRECT_MIDDLE_VALUE);

        isErrorMessageStartingWithAddressPresent(false);

        isErrorMessageStartingWithNamePresent(true);

        String actualErrorMessage = selenium.getText(errorMessageStartWithName);

        assertEquals(actualErrorMessage, ERROR_MESSAGE_NAME_EMPTY);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test
    public void testAddressFilledIncorrectlyLength0() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_CORRECT_MIDDLE_VALUE, LENGTH_OF_ADDRESS_WRONG_EMPTY);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithNamePresent(false);

        String actualErrorMessage = selenium.getText(errorMessageStartWithAddress);

        assertEquals(actualErrorMessage, ERROR_MESSAGE_ADDRESS_EMPTY);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test(groups = { "4.2" })
    public void testAddressFilledIncorrectlyLengthMoreThan100() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_CORRECT_MIDDLE_VALUE,
            LENGTH_OF_ADDRESS_WRONG_TOO_LONG);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithNamePresent(false);

        String actualErrorMessage = selenium.getText(errorMessageStartWithAddress);

        assertEquals(actualErrorMessage, ERROR_MESSAGE_ADDRESS_TOO_LONG);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test(groups = { "4.2" })
    public void testNameIncorrectlyMoreThan12AddressIncorrectlyLength0() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_TOO_LONG, LENGTH_OF_ADDRESS_WRONG_EMPTY);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithNamePresent(true);

        String actualNameErrorMessage = selenium.getText(errorMessageStartWithName);
        assertEquals(actualNameErrorMessage, ERROR_MESSAGE_NAME_TOO_LONG);

        String actualAddressErrorMessage = selenium.getText(errorMessageStartWithAddress);
        assertEquals(actualAddressErrorMessage, ERROR_MESSAGE_ADDRESS_EMPTY);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test(groups = { "4.2" })
    public void testNameIncorrectlyMoreThan12AddressIncorrectlyMoreThan100() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_TOO_LONG, LENGTH_OF_ADDRESS_WRONG_TOO_LONG);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithNamePresent(true);

        String actualNameErrorMessage = selenium.getText(errorMessageStartWithName);
        assertEquals(actualNameErrorMessage, ERROR_MESSAGE_NAME_TOO_LONG);

        String actualAddressErrorMessage = selenium.getText(errorMessageStartWithAddress);
        assertEquals(actualAddressErrorMessage, ERROR_MESSAGE_ADDRESS_TOO_LONG);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test(groups = { "4.2" })
    public void testNameIncorrectlyLessThan3MoreThan0AddressIncorrectlyLength0() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_LESS_THAN_MINIMAL,
            LENGTH_OF_ADDRESS_WRONG_EMPTY);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithAddressPresent(true);

        String actualNameErrorMessage = selenium.getText(errorMessageStartWithName);
        assertEquals(actualNameErrorMessage, ERROR_MESSAGE_NAME_LENGTH_TOO_SHORT_BUT_NOT_EMPTY);

        String actualAddressErrorMessage = selenium.getText(errorMessageStartWithAddress);
        assertEquals(actualAddressErrorMessage, ERROR_MESSAGE_ADDRESS_EMPTY);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test(groups = { "4.2" })
    public void testNameIncorrectlyLessThan3MoreThan0AddressIncorrectlyMoreThan100() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_LESS_THAN_MINIMAL,
            LENGTH_OF_ADDRESS_WRONG_TOO_LONG);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithNamePresent(true);

        String actualNameErrorMessage = selenium.getText(errorMessageStartWithName);
        assertEquals(actualNameErrorMessage, ERROR_MESSAGE_NAME_LENGTH_TOO_SHORT_BUT_NOT_EMPTY);

        String actualAddressMessage = selenium.getText(errorMessageStartWithAddress);
        assertEquals(actualAddressMessage, ERROR_MESSAGE_ADDRESS_TOO_LONG);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test
    public void testNameIncorrectlyLength0AddressIncorrectlyLength0() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_EMPTY, LENGTH_OF_ADDRESS_WRONG_EMPTY);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithNamePresent(true);

        String actualNameErrorMessage = selenium.getText(errorMessageStartWithName);
        assertEquals(actualNameErrorMessage, ERROR_MESSAGE_NAME_EMPTY);

        String actualAddressErrorMessage = selenium.getText(errorMessageStartWithAddress);
        assertEquals(actualAddressErrorMessage, ERROR_MESSAGE_ADDRESS_EMPTY);

        isUserSuccessfullyAddedPresent(false);
    }

    @Test(groups = { "4.2" })
    public void testNameIncorrectlyLength0AddressIncorrectlyMoreThan100() {

        prepareStringBuildersClickOnTheButton(LENGTH_OF_USER_NAME_WRONG_EMPTY, LENGTH_OF_ADDRESS_WRONG_TOO_LONG);

        isErrorMessageStartingWithAddressPresent(true);

        isErrorMessageStartingWithNamePresent(true);

        String actualNameErrorMessage = selenium.getText(errorMessageStartWithName);
        assertEquals(actualNameErrorMessage, ERROR_MESSAGE_NAME_EMPTY);

        String actualAddressErrorMessage = selenium.getText(errorMessageStartWithAddress);
        assertEquals(actualAddressErrorMessage, ERROR_MESSAGE_ADDRESS_TOO_LONG);

        isUserSuccessfullyAddedPresent(false);
    }

    /* ********************************************************************************************************
     * Help methods *********************************************************************
     * ***********************************
     */

    /**
     * Fills in correct name and correct address and check error messages, lengths are for checking various length
     * values and they should be asssigned correctly
     *
     * @param lengthOfUserName
     *            A numember which is bigger than 2 and less than 13
     * @param lengthOfAddress
     *            A number which is bigger than 0 and less than 101
     */
    private void fillUseDataCorrectlyAndCheckErrorMessages(int lengthOfUserName, int lengthOfAddress) {

        prepareStringBuildersClickOnTheButton(lengthOfUserName, lengthOfAddress);

        isErrorMessageStartingWithAddressPresent(false);

        isErrorMessageStartingWithNamePresent(false);

        isUserSuccessfullyAddedPresent(true);
    }

    /**
     * Fill in the user name and address according to the params and ten clicks on the button
     *
     * @param lengthOfUserName
     *            length of username which will be filled in
     * @param lengthOfAddress
     *            length of address which will be filled in
     */
    private void prepareStringBuildersClickOnTheButton(int lengthOfUserName, int lengthOfAddress) {

        StringBuilder userName = new StringBuilder();

        for (int i = 1; i <= lengthOfUserName; i++) {
            userName.append("a");
        }

        selenium.typeKeys(userNameInput, userName.toString());

        StringBuilder address = new StringBuilder();

        for (int i = 1; i <= lengthOfAddress; i++) {
            address.append("a");
        }

        selenium.typeKeys(addressInput, address.toString());

        guardXhr(selenium).click(button);
    }

    /**
     * Find out whether the specific message is present according to the params
     *
     * @param shouldBeElementPresent
     */
    private void isUserSuccessfullyAddedPresent(boolean shouldBeElementPresent) {

        if (shouldBeElementPresent) {
            assertTrue(selenium.isElementPresent(userSuccessfullyAdded),
                "There should appear this message: User 'username' stored succesfully, because"
                    + " user data was filled in correctly!");
        } else {
            assertFalse(selenium.isElementPresent(userSuccessfullyAdded),
                "There should not appear this message: User 'username' stored succesfully, because"
                    + " user data was not filled in correctly!");
        }
    }

    /**
     * This method should catch messages other than are declared as locators and also checks for messages starting with
     * name
     */
    private void isErrorMessageStartingWithNamePresent(boolean shouldBeElementPresent) {

        if (shouldBeElementPresent) {
            assertTrue(selenium.isElementPresent(errorMessageStartWithName), "There should ne an error starting with "
                + "Name");
        } else {
            assertFalse(selenium.isElementPresent(errorMessageStartWithName),
                "There should not be an error starting with " + "Name");
        }
    }

    /**
     * This method should catch messages other than are declared as locators and also checks for messages starting with
     * adress
     */
    private void isErrorMessageStartingWithAddressPresent(boolean shouldBeElementPresent) {

        if (shouldBeElementPresent) {
            assertTrue(selenium.isElementPresent(errorMessageStartWithAddress),
                "There should be an error message starting with " + "Adress");
        } else {
            assertFalse(selenium.isElementPresent(errorMessageStartWithAddress),
                "There should not be an error message starting with " + "Adress");

        }
    }
}
