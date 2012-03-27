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
package org.richfaces.tests.showcase.graphValidator;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestGraphValidator extends AbstractAjocadoTest {

    /* *****************************************************************************
     * Locators ****************************************************************** ***********
     */

    private JQueryLocator newPasswordInput = jq("input[id$=pass]");
    private JQueryLocator confirmNewPasswordInput = jq("input[id$=conf]");
    private JQueryLocator storeChangesButton = jq("input[type=submit]");
    private JQueryLocator passwordError = jq("span[id$=pass] span[class=rf-msg-det]");
    private JQueryLocator differentPasswordError = jq("span:contains('Different passwords entered!')");
    private JQueryLocator successfullyChangedInfo = jq("span:contains('Successfully changed!')");

    private final String ERROR_MESSAGE_WRONG_SIZE_FOR_PASSWORD = "Password length must be between 5 and 15 characters.";
    private final String ERROR_MESSAGE_DIFFERENT_PASSWORD_ENTERED = "Different passwords entered!";
    private final String INFO_MESSAGE_SUCCESSFULLY_CHANGED = "Successfully changed!";

    private final int MIN_CORRECT_LENGTH_OF_PASSWORD = 5;
    private final int MAX_CORRECT_LEGTH_OF_PASSWORD = 15;

    /* ******************************************************************************
     * Tests ********************************************************************* *********
     */

    @Test
    public void testCorrectPasswords() {

        fillInCorrectPasswordsClickOnTheButtonAndCheckForMessages(MIN_CORRECT_LENGTH_OF_PASSWORD);

        eraseAllInputsAndClickOnTheButton();

        fillInCorrectPasswordsClickOnTheButtonAndCheckForMessages(MAX_CORRECT_LEGTH_OF_PASSWORD);
    }

    @Test
    public void testNewPasswordTooShort() {

        fillInInputsCheckForMessages(MIN_CORRECT_LENGTH_OF_PASSWORD - 1, MIN_CORRECT_LENGTH_OF_PASSWORD, true, false,
            false);

    }

    @Test
    public void testNewPasswordTooLong() {

        fillInInputsCheckForMessages(MAX_CORRECT_LEGTH_OF_PASSWORD + 1, MIN_CORRECT_LENGTH_OF_PASSWORD, true, false,
            false);
    }

    @Test
    public void testDifferentPasswords() {

        fillAnyInput(newPasswordInput, "12345");
        fillAnyInput(confirmNewPasswordInput, "ThisIsDifferent");

        guardXhr(selenium).click(storeChangesButton);

        checkForAllMessages(false, true, false);

        eraseAllInputsAndClickOnTheButton();

        fillInCorrectPasswordsClickOnTheButtonAndCheckForMessages(MIN_CORRECT_LENGTH_OF_PASSWORD);
    }

    /* ******************************************************************************
     * Help methods ************************************************************** ****************
     */

    private void fillInInputsCheckForMessages(int lengthOfNewPassword, int lenthOfConfirmedPassword,
        boolean shouldBeThereLenghtPasswordError, boolean souldBeThereDifferentPasswordError,
        boolean shouldBeThereInfoSuccessfullyChanged) {

        fillInputWithStringOfLength(newPasswordInput, lengthOfNewPassword);
        fillInputWithStringOfLength(confirmNewPasswordInput, lenthOfConfirmedPassword);

        guardXhr(selenium).click(storeChangesButton);

        checkForAllMessages(shouldBeThereLenghtPasswordError, souldBeThereDifferentPasswordError,
            shouldBeThereInfoSuccessfullyChanged);

        eraseAllInputsAndClickOnTheButton();

        fillInCorrectPasswordsClickOnTheButtonAndCheckForMessages(MIN_CORRECT_LENGTH_OF_PASSWORD);

    }

    private void fillInCorrectPasswordsClickOnTheButtonAndCheckForMessages(int lengthOfPasswords) {

        fillInputWithStringOfLength(newPasswordInput, lengthOfPasswords);
        fillInputWithStringOfLength(confirmNewPasswordInput, lengthOfPasswords);

        guardXhr(selenium).click(storeChangesButton);

        checkForAllMessages(false, false, true);

    }

    private void checkForAllMessages(boolean shouldBeThereLengthPasswordError,
        boolean souldBeThereDifferentPasswordError, boolean shouldBeThereInfoSuccessfullyChanged) {

        isThereErrorMessage(passwordError, ERROR_MESSAGE_WRONG_SIZE_FOR_PASSWORD, shouldBeThereLengthPasswordError);
        isThereErrorMessage(differentPasswordError, ERROR_MESSAGE_DIFFERENT_PASSWORD_ENTERED,
            souldBeThereDifferentPasswordError);
        isThereInfoMessage(successfullyChangedInfo, INFO_MESSAGE_SUCCESSFULLY_CHANGED,
            shouldBeThereInfoSuccessfullyChanged);
    }

    private void eraseAllInputsAndClickOnTheButton() {

        eraseInput(newPasswordInput);
        eraseInput(confirmNewPasswordInput);
        guardXhr(selenium).click(storeChangesButton);
    }

}
