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
package org.richfaces.tests.showcase.clientValidation;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public abstract class AbstractClientValidationTest extends AbstractGrapheneTest {

    /* *********************************************************************************************************************
     * Locators ******************************************************************
     * ***************************************************
     */
    private JQueryLocator errorMessageAboutValueRequired = jq("span:contains('Validation Error: Value is required')");

    /* *********************************************************************************************************************
     * Constants *****************************************************************
     * ***************************************************
     */

    private final String ERROR_MESSAGE_ABOUT_VALUE_REQUIRED = "Validation Error: Value is required.";

    /* ************************************************************************************
     * Help methods **********************************************************************
     */

    /**
     * Finds out whether there is error message about email, it is implemented in the subclass
     *
     * @param isThereErrorMessageAboutEmail
     */
    protected abstract void isThereErrorMessageAboutEmail(boolean isThereErrorMessageAboutEmail);

    /**
     * Finds out whether there is error message about size of name, it is implemented in the subclass
     *
     * @param isThereErrorMessageAboutSizeOfName
     */
    protected abstract void isThereErrorMessageAboutSizeOfName(boolean isThereErrorMessageAboutSizeOfName);

    protected void isThereErrorMessageAboutValueRequeired(boolean shouldBeErrorMessagePresented) {

        isThereErrorMessage(errorMessageAboutValueRequired, ERROR_MESSAGE_ABOUT_VALUE_REQUIRED,
            shouldBeErrorMessagePresented);
    }

    /**
     * Fills in the name input incorrect value according to the borderValues, it tries to fill various values. Checks
     * whether there are error messages.
     *
     * @param nameInput
     * @param bottomBorderValue
     * @param topBorderValue
     */
    public void fillNameInputWithIncorrectValues(JQueryLocator nameInput, int bottomBorderValue, int topBorderValue,
        boolean valueRequiredErrorMessage) {

        fillInputWithStringOfLength(nameInput, 0);
        guardNoRequest(selenium).fireEvent(nameInput, Event.BLUR);
        isThereErrorMessageAboutValueRequeired(valueRequiredErrorMessage);

        eraseInput(nameInput);

        fillInputWithStringOfLength(nameInput, bottomBorderValue - 1);
        guardNoRequest(selenium).fireEvent(nameInput, Event.BLUR);
        isThereErrorMessageAboutSizeOfName(true);

        eraseInput(nameInput);

        fillInputWithStringOfLength(nameInput, topBorderValue + 1);
        guardNoRequest(selenium).fireEvent(nameInput, Event.BLUR);
        isThereErrorMessageAboutSizeOfName(true);

        eraseInput(nameInput);

        fillInputWithStringOfLength(nameInput, topBorderValue * 3);
        guardNoRequest(selenium).fireEvent(nameInput, Event.BLUR);
        isThereErrorMessageAboutSizeOfName(true);
    }

    /**
     * Fills in the name input correct value according to the borderValues, it tries to fill various values. Checks
     * whether there are error messages.
     *
     * @param nameInput
     * @param bottomBorderValue
     * @param middleValue
     * @param topBorderValue
     */
    public void fillNameInputWithCorrectValues(JQueryLocator nameInput, int bottomBorderValue, int middleValue,
        int topBorderValue) {

        fillInputWithStringOfLength(nameInput, bottomBorderValue);
        guardNoRequest(selenium).fireEvent(nameInput, Event.BLUR);
        isThereErrorMessageAboutSizeOfName(false);

        eraseInput(nameInput);

        fillInputWithStringOfLength(nameInput, middleValue);
        guardNoRequest(selenium).fireEvent(nameInput, Event.BLUR);
        isThereErrorMessageAboutSizeOfName(false);

        eraseInput(nameInput);

        fillInputWithStringOfLength(nameInput, topBorderValue);
        guardNoRequest(selenium).fireEvent(nameInput, Event.BLUR);
        isThereErrorMessageAboutSizeOfName(false);

        eraseInput(nameInput);
    }

    /**
     * Fills in the email input incorrect values, it tries various incorrect values. Checks for error messages.
     *
     * @param emailInput
     */
    public void fillEmailInputWithIncorrectValues(JQueryLocator emailInput) {

        fillAnyInput(emailInput, "");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(true);

        fillAnyInput(emailInput, "jhuska");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(true);

        eraseInput(emailInput);

        fillAnyInput(emailInput, "jhuska@");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(true);

        eraseInput(emailInput);

        fillAnyInput(emailInput, "jhuska@redhat");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(true);

        eraseInput(emailInput);

        fillAnyInput(emailInput, "jhuska@redhat.");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(true);

        eraseInput(emailInput);

        fillAnyInput(emailInput, "jhuska@redhat.c");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(true);

        eraseInput(emailInput);

        fillAnyInput(emailInput, "jhuska@redhat.com");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(false);
    }

    /**
     * Fills in the email input correct values, it tries various incorrect values. Checks for error messages.
     *
     * @param emailInput
     */
    public void fillEmailInputWithCorrectValues(JQueryLocator emailInput) {

        fillAnyInput(emailInput, "jhuska@redhat.com");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(false);

        eraseInput(emailInput);

        fillAnyInput(emailInput, "xxx@foo.sk");
        guardNoRequest(selenium).fireEvent(emailInput, Event.BLUR);
        isThereErrorMessageAboutEmail(false);
    }

}
