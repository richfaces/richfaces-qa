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
package org.richfaces.tests.showcase.message;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractTestMessage extends AbstractAjocadoTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator nameInput = jq("input[id$=name]");
    protected JQueryLocator jobInput = jq("input[id$=job]");
    protected JQueryLocator addressInput = jq("input[id$=address]");
    protected JQueryLocator zipInput = jq("input[id$=zip]");

    protected JQueryLocator ajaxValidateButton = jq("input[type=submit]");

    protected JQueryLocator nameError = jq("span:contains('Name: Validation Error:')");
    protected JQueryLocator jobError = jq("span:contains('Job: Validation Error:')");
    protected JQueryLocator addressError = jq("span:contains('Address: Validation Error:')");
    protected JQueryLocator zipError = jq("span:contains('Zip: Validation Error:')");

    /* *****************************************************************************************************
     * Constants ***************************************************************** ************************************
     */

    protected final int MINIMUM_OF_NAME = 3;
    protected final int MINIMUM_OF_JOB = 3;
    protected final int MINIMUM_OF_ADDRESS = 10;
    protected final int MINIMUM_OF_ZIP = 4;

    protected final int MAXIMUM_OF_JOB = 50;
    protected final int MAXIMUM_OF_ZIP = 9;

    protected final String NAME_ERROR_VALUE_REQUIRED = "Name: Validation Error: Value is required.";
    protected final String JOB_ERROR__VALUE_REQUIRED = "Job: Validation Error: Value is required.";
    protected final String ADDRESS_ERROR__VALUE_REQUIRED = "Address: Validation Error: Value is required.";
    protected final String ZIP_ERROR__VALUE_REQUIRED = "Zip: Validation Error: Value is required.";

    protected final String NAME_ERROR_LESS_THAN_MINIMUM = "Name: Validation Error: Value is less than allowable minimum of '"
        + MINIMUM_OF_NAME + "'";
    protected final String JOB_ERROR_LESS_THAN_MINIMUM = "Job: Validation Error: Value is less than allowable minimum of '"
        + MINIMUM_OF_JOB + "'";
    protected final String ADDRESS_ERROR_LESS_THAN_MINIMUM = "Address: Validation Error: Value is less than allowable minimum of '"
        + MINIMUM_OF_ADDRESS + "'";
    protected final String ZIP_ERROR_LESS_THAN_MINIMUM = "Zip: Validation Error: Value is less than allowable minimum of '"
        + MINIMUM_OF_ZIP + "'";

    protected final String JOB_ERROR_GREATER_THAN_MAXIMUM = "Job: Validation Error: Value is greater than allowable maximum of '"
        + MAXIMUM_OF_JOB + "'";
    protected final String ZIP_ERROR_GREATER_THAN_MAXIMUM = "Zip: Validation Error: Value is greater than allowable maximum of '"
        + MAXIMUM_OF_ZIP + "'";

    /* ****************************************************************************************************************************
     * Abstract tests ************************************************************
     * ***************************************************************
     */

    protected void abstractTestCorrectValues() {

        fillInCorrectValuesAndCheckForErrorMessages();
    }

    protected void abstractTestEmptyInputs() {

        eraseAllInputsAndClickOnTheSubmit();

        isThereErrorMessage(nameError, NAME_ERROR_VALUE_REQUIRED, true);
        isThereErrorMessage(jobError, JOB_ERROR__VALUE_REQUIRED, true);
        isThereErrorMessage(addressError, ADDRESS_ERROR__VALUE_REQUIRED, true);
        isThereErrorMessage(zipError, ZIP_ERROR__VALUE_REQUIRED, true);

        fillInCorrectValuesAndCheckForErrorMessages();
    }

    protected void abstractTestLessThanMinimum() {

        fillInputWithStringOfLength(nameInput, MINIMUM_OF_NAME - 1);
        fillInputWithStringOfLength(jobInput, MINIMUM_OF_JOB - 1);
        fillInputWithStringOfLength(addressInput, MINIMUM_OF_ADDRESS - 1);
        fillInputWithStringOfLength(zipInput, MINIMUM_OF_ZIP - 1);

        clickOnTheSubmitButton();

        isThereErrorMessage(nameError, NAME_ERROR_LESS_THAN_MINIMUM, true);
        isThereErrorMessage(jobError, JOB_ERROR_LESS_THAN_MINIMUM, true);
        isThereErrorMessage(addressError, ADDRESS_ERROR_LESS_THAN_MINIMUM, true);
        isThereErrorMessage(zipError, ZIP_ERROR_LESS_THAN_MINIMUM, true);

        eraseAllInputsAndClickOnTheSubmit();

        fillInCorrectValuesAndCheckForErrorMessages();
    }

    protected void abstractTestGreaterThanMaximum() {

        fillInputWithStringOfLength(jobInput, MAXIMUM_OF_JOB + 1);
        fillInputWithStringOfLength(zipInput, MAXIMUM_OF_ZIP + 1);

        clickOnTheSubmitButton();

        isThereErrorMessage(jobError, JOB_ERROR_GREATER_THAN_MAXIMUM, true);
        isThereErrorMessage(zipError, ZIP_ERROR_GREATER_THAN_MAXIMUM, true);

        eraseAllInputsAndClickOnTheSubmit();

        fillInCorrectValuesAndCheckForErrorMessages();
    }

    /* **********************************************************************
     * Methods *******************************************************************
     * *******************************************************************
     */
    private void eraseAllInputsAndClickOnTheSubmit() {

        eraseInput(nameInput);
        eraseInput(jobInput);
        eraseInput(addressInput);
        eraseInput(zipInput);

        clickOnTheSubmitButton();
    }

    private void clickOnTheSubmitButton() {

        guardXhr(selenium).click(ajaxValidateButton);
    }

    private void fillInCorrectValuesAndCheckForErrorMessages() {

        // minimum values
        fillInputWithStringOfLength(nameInput, MINIMUM_OF_NAME);
        fillInputWithStringOfLength(jobInput, MINIMUM_OF_JOB);
        fillInputWithStringOfLength(addressInput, MINIMUM_OF_ADDRESS);
        fillInputWithStringOfLength(zipInput, MINIMUM_OF_ZIP);

        clickOnTheSubmitButton();

        // it is not important in this case which error message is there, see
        // the source of isThereErrorMessage()
        isThereErrorMessage(nameError, NAME_ERROR_LESS_THAN_MINIMUM, false);
        isThereErrorMessage(jobError, JOB_ERROR_LESS_THAN_MINIMUM, false);
        isThereErrorMessage(addressError, ADDRESS_ERROR_LESS_THAN_MINIMUM, false);
        isThereErrorMessage(zipError, ZIP_ERROR_LESS_THAN_MINIMUM, false);

        eraseAllInputsAndClickOnTheSubmit();

        // maximal values
        fillInputWithStringOfLength(jobInput, MAXIMUM_OF_JOB);
        fillInputWithStringOfLength(zipInput, MAXIMUM_OF_ZIP);

        clickOnTheSubmitButton();

        isThereErrorMessage(jobError, JOB_ERROR_GREATER_THAN_MAXIMUM, false);
        isThereErrorMessage(zipError, ZIP_ERROR_GREATER_THAN_MAXIMUM, false);
    }

}
