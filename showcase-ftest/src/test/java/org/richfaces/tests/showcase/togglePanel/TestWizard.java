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
package org.richfaces.tests.showcase.togglePanel;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.SeleniumException;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestWizard extends AbstractAjocadoTest {

    /* ***********************************************************************************
     * Constants ***************************************************************** ******************
     */

    protected final String ERROR_MSG_VALUE_REQUIRED = "Validation Error: Value is required.";
    protected final String ERROR_MSG_FIRST_NAME = "First Name: " + ERROR_MSG_VALUE_REQUIRED;
    protected final String ERROR_MSG_LAST_NAME = "Last Name: " + ERROR_MSG_VALUE_REQUIRED;
    protected final String ERROR_MSG_COMPANY = "Company: " + ERROR_MSG_VALUE_REQUIRED;
    protected final String ERROR_MSG_NOTES = "Notes: " + ERROR_MSG_VALUE_REQUIRED;

    /* ***********************************************************************************
     * Locators ****************************************************************** *****************
     */

    JQueryLocator firstNameInput = jq("input[type=text]:eq(0)");
    JQueryLocator lastNameInput = jq("input[type=text]:eq(1)");
    JQueryLocator companyInput = jq("input[type=text]:eq(2)");
    JQueryLocator notesInput = jq("textarea");
    JQueryLocator summaryOfAllSteps = jq("div[class*=rf-p wizard] div[class*=rf-tgp] table:visible tbody:visible");
    JQueryLocator nextButton = jq("input[value*=Next]:visible");
    JQueryLocator previousButton = jq("input[value*=Previous]:visible");
    JQueryLocator errorMessageFirstName = jq("span[class=rf-msg-det]:contains('First Name: Validation Error'):visible");
    JQueryLocator errorMessageLastName = jq("span[class=rf-msg-det]:contains('Last Name: Validation Error'):visible");
    JQueryLocator errorMessageCompany = jq("span[class=rf-msg-det]:contains('Company: Validation Error'):visible");
    JQueryLocator errorMessageNotes = jq("span[class=rf-msg-det]:contains('Notes: Validation Error: Value is required.'):visible");
    JQueryLocator errorMessage = jq("span[class=rf-msg-det]:contains('Validation Error'):visible");

    /* ***************************************************************************
     * Tests ********************************************************************* ******
     */

    @Test
    public void testStep1ValidationEmptyInputs() {

        guardXhr(selenium).click(nextButton);

        checkForAllErrorMessagesFromFirstStep(true);

        fillAnyInput(firstNameInput, " ");
        fillAnyInput(lastNameInput, " ");
        fillAnyInput(companyInput, " ");

        guardXhr(selenium).click(nextButton);

        try {

            checkForAllErrorMessagesFromFirstStep(false);

            isThereErrorMessage(errorMessage, ERROR_MSG_VALUE_REQUIRED, false);

        } catch (SeleniumException ex) {

            fail("Has to change the test, since last time JSF considered white spaces as characters when there was restriction required!");
        }
    }

    @Test
    public void testStep1ValidationCorrectValues() {

        fillInputWithStringOfLength(firstNameInput, 5);
        fillInputWithStringOfLength(lastNameInput, 5);
        fillInputWithStringOfLength(companyInput, 5);

        guardXhr(selenium).click(nextButton);

        checkForAllErrorMessagesFromFirstStep(false);
    }

    @Test
    public void testStep2ValidationEmptyInput() {

        fillInputWithStringOfLength(firstNameInput, 5);
        fillInputWithStringOfLength(lastNameInput, 5);
        fillInputWithStringOfLength(companyInput, 5);

        guardXhr(selenium).click(nextButton);

        guardXhr(selenium).click(nextButton);

        isThereErrorMessage(errorMessageNotes, ERROR_MSG_NOTES, true);

        fillAnyInput(notesInput, " ");

        guardXhr(selenium).click(nextButton);

        try {

            isThereErrorMessage(errorMessageNotes, ERROR_MSG_NOTES, false);

        } catch (SeleniumException ex) {

            fail("Has to change the test, since last time JSF considered white spaces as characters when there was restriction required!");
        }

    }

    // @Test
    public void testStep2ValidationCorrectValue() {
        // TODO
    }

    // @Test
    public void testStep3CheckSummaryOfPreviousSteps() {
        // TODO
    }

    /* ***************************************************************************************
     * Help methods ************************************************************** *************************
     */

    private void checkForAllErrorMessagesFromFirstStep(boolean shouldErrorMessagePresented) {

        isThereErrorMessage(errorMessageFirstName, ERROR_MSG_FIRST_NAME, shouldErrorMessagePresented);

        isThereErrorMessage(errorMessageLastName, ERROR_MSG_LAST_NAME, shouldErrorMessagePresented);

        isThereErrorMessage(errorMessageCompany, ERROR_MSG_COMPANY, shouldErrorMessagePresented);
    }

}
