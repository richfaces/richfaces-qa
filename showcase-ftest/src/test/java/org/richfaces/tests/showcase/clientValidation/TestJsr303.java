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
public class TestJsr303 extends AbstractClientValidationTest {

    /* *************************************************************************************
     * Locators*************************************************************************************
     */

    private JQueryLocator nameInput = jq("input[id$=name]");
    private JQueryLocator emailInput = jq("input[id$=email]");
    private JQueryLocator ageInput = jq("input[id$=age]");
    private JQueryLocator iAgreeTheTermsCheckBox = jq("input[id$=agreed]");
    private JQueryLocator errorMessageAboutSizeOfName = jq("span:contains('size must be between')");
    private JQueryLocator errorMessageAboutEmail = jq("span:contains('bad email')");
    private JQueryLocator errorMessageAboutAgeNumber = jq("span:contains"
        + "('must be a number between -2147483648 and 2147483647')");
    private JQueryLocator errorMessageAboutAgeMustBeGreaterThan = jq("span:contains('must be greater than')");
    private JQueryLocator errorMessageAboutAgeMustBeLessThan = jq("span:contains('must be less than')");
    private JQueryLocator errorMessageAboutIAgreeTheTerms = jq("span:contains('must be true')");

    /* ******************************************************************************************************
     * Constants*****************************************************************************************************
     */

    private final String ERROR_MESSAGE_ABOUT_SIZE_OF_NAME = "size must be between 3 and 12";

    private final String ERROR_MESSAGE_ABOUT_EMAIL = "bad email";

    private final String ERROR_MESSAGE_ABOUT_AGE_NUMBER = "must be a number between -2147483648 and 2147483647 Example: 9346";

    private final String ERROR_MESSAGE_ABOUT_AGE_MUST_BE_GREATER_THAN = "must be greater than or equal to 18";

    private final String ERROR_MESSAGE_ABOUT_AGE_MUST_BE_LESS_THAN = "must be less than or equal to 99";

    private final String ERROR_MESSAGE_ABOUT_I_AGREE_THE_TERMS = "must be true";

    /* ***************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testNameInputCorrectValues() {

        fillNameInputWithCorrectValues(nameInput, 3, 7, 12);
    }

    @Test
    public void testNameInputIncorrectValues() {

        fillNameInputWithIncorrectValues(nameInput, 3, 12, false);
    }

    @Test
    public void testEmailInputCorrectValues() {

        fillEmailInputWithCorrectValues(emailInput);
    }

    @Test
    public void testEmailInputIncorrectValues() {

        fillEmailInputWithIncorrectValues(emailInput);
    }

    @Test
    public void testAgeInputCorrectValues() {

        fillAnyInput(ageInput, "18");
        guardNoRequest(selenium).fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(false);
        isThereErrorMessageAboutAgeGreaterThan(false);
        isThereErrorMessageAboutAgeLessThan(false);

        eraseInput(ageInput);

        fillAnyInput(ageInput, "55");
        selenium.fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(false);
        isThereErrorMessageAboutAgeGreaterThan(false);
        isThereErrorMessageAboutAgeLessThan(false);

        eraseInput(ageInput);

        fillAnyInput(ageInput, "99");
        selenium.fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(false);
        isThereErrorMessageAboutAgeGreaterThan(false);
        isThereErrorMessageAboutAgeLessThan(false);

        eraseInput(ageInput);

    }

    @Test
    public void testAgeInputIncorrectValues() {

        fillAnyInput(ageInput, "x");
        guardNoRequest(selenium).fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(true);
        isThereErrorMessageAboutAgeGreaterThan(false);
        isThereErrorMessageAboutAgeLessThan(false);

        eraseInput(ageInput);

        fillAnyInput(ageInput, "0");
        selenium.fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(false);
        isThereErrorMessageAboutAgeGreaterThan(true);
        isThereErrorMessageAboutAgeLessThan(false);

        eraseInput(ageInput);

        fillAnyInput(ageInput, " ");
        selenium.fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(true);
        isThereErrorMessageAboutAgeGreaterThan(false);
        isThereErrorMessageAboutAgeLessThan(false);

        eraseInput(ageInput);

        fillAnyInput(ageInput, "15");
        selenium.fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(false);
        isThereErrorMessageAboutAgeGreaterThan(true);
        isThereErrorMessageAboutAgeLessThan(false);

        eraseInput(ageInput);

        fillAnyInput(ageInput, "100");
        selenium.fireEvent(ageInput, Event.BLUR);
        isThereErrorMessageAboutAgeNumber(false);
        isThereErrorMessageAboutAgeGreaterThan(false);
        isThereErrorMessageAboutAgeLessThan(true);

        eraseInput(ageInput);
    }

    @Test
    public void testIAgreeTheTermsCheckBoxCorrectValue() {

        checkTheCheckBoxAndCheckThereIsNoRequest();

        isThereErrorMessageAboutCheckBox(false);

        uncheckTheCheckBoxAndCheckThereIsNoRequest();

        checkTheCheckBoxAndCheckThereIsNoRequest();

        isThereErrorMessageAboutCheckBox(false);
    }

    @Test
    public void testIAgreeTheTermsCheckBoxIncorrectValue() {

        uncheckTheCheckBoxAndCheckThereIsNoRequest();

        isThereErrorMessageAboutCheckBox(true);

        checkTheCheckBoxAndCheckThereIsNoRequest();

        uncheckTheCheckBoxAndCheckThereIsNoRequest();

        isThereErrorMessageAboutCheckBox(true);
    }

    /* *********************************************************************************************************************
     * Help methods
     * ******************************************************************************************************
     * ***************
     */

    /**
     * Finds out whether particular error message is presented
     *
     * @param shouldErrorMessagePresented
     */
    @Override
    protected void isThereErrorMessageAboutSizeOfName(boolean shouldBeErrorMessagePresented) {

        isThereErrorMessage(errorMessageAboutSizeOfName, ERROR_MESSAGE_ABOUT_SIZE_OF_NAME,
            shouldBeErrorMessagePresented);
    }

    /**
     * Finds out whether particular error message is presented
     *
     * @param shouldErrorMessagePresented
     */
    @Override
    protected void isThereErrorMessageAboutEmail(boolean shouldBeErrorMessagePresented) {

        isThereErrorMessage(errorMessageAboutEmail, ERROR_MESSAGE_ABOUT_EMAIL, shouldBeErrorMessagePresented);
    }

    /**
     * Finds out whether particular error message is presented
     *
     * @param shouldErrorMessagePresented
     */
    private void isThereErrorMessageAboutAgeNumber(boolean shouldBeErrorMessagePresented) {

        isThereErrorMessage(errorMessageAboutAgeNumber, ERROR_MESSAGE_ABOUT_AGE_NUMBER, shouldBeErrorMessagePresented);
    }

    private void isThereErrorMessageAboutAgeGreaterThan(boolean shouldBeErrorMessagePresented) {

        isThereErrorMessage(errorMessageAboutAgeMustBeGreaterThan, ERROR_MESSAGE_ABOUT_AGE_MUST_BE_GREATER_THAN,
            shouldBeErrorMessagePresented);
    }

    /**
     * Finds out whether particular error message is presented
     *
     * @param shouldErrorMessagePresented
     */
    private void isThereErrorMessageAboutAgeLessThan(boolean shouldBeErrorMessagePresented) {

        isThereErrorMessage(errorMessageAboutAgeMustBeLessThan, ERROR_MESSAGE_ABOUT_AGE_MUST_BE_LESS_THAN,
            shouldBeErrorMessagePresented);
    }

    /**
     * Finds out whether particular error message is presented
     *
     * @param shouldErrorMessagePresented
     */
    private void isThereErrorMessageAboutCheckBox(boolean shouldBeErrorMessagePresented) {

        isThereErrorMessage(errorMessageAboutIAgreeTheTerms, ERROR_MESSAGE_ABOUT_I_AGREE_THE_TERMS,
            shouldBeErrorMessagePresented);
    }

    /**
     * Checks the check box and waits for XHR request
     */
    private void checkTheCheckBoxAndCheckThereIsNoRequest() {

        selenium.check(iAgreeTheTermsCheckBox);
        guardNoRequest(selenium).fireEvent(iAgreeTheTermsCheckBox, Event.CHANGE);
        guardNoRequest(selenium).fireEvent(iAgreeTheTermsCheckBox, Event.CLICK);
    }

    /**
     * Unchecks the check box and waits for XHR request
     */
    private void uncheckTheCheckBoxAndCheckThereIsNoRequest() {

        selenium.uncheck(iAgreeTheTermsCheckBox);
        guardNoRequest(selenium).fireEvent(iAgreeTheTermsCheckBox, Event.CHANGE);
        guardNoRequest(selenium).fireEvent(iAgreeTheTermsCheckBox, Event.CLICK);
    }
}
