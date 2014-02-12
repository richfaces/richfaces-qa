/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.archetypes.kitchensink.ftest.common.page;

import static org.testng.Assert.assertTrue;

import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class RegisterForm {

    private final String NAME_LOC = "//input[contains(@id,'name')]";
    private final String EMAIL_LOC = "//input[contains(@id,'email')]";
    private final String PHONE_LOC = "//input[contains(@id,'phoneNumber')]";
    private final String REGISTER_BUTTON_LOC = "//input[@type='submit']";
    private final String ERROR_MSGS_LOC = "rf-msg-err";

    @Drone
    private WebDriver browser;

    @FindBy(xpath = NAME_LOC)
    private WebElement nameInput;

    @FindBy(xpath = EMAIL_LOC)
    private WebElement emailInput;

    @FindBy(xpath = PHONE_LOC)
    private WebElement phoneInput;

    @FindBy(xpath = REGISTER_BUTTON_LOC)
    private WebElement registerButton;

    @FindBy(className = ERROR_MSGS_LOC)
    private List<WebElement> errorMessages;

    @FindBy(tagName="span")
    private WebElement toBlur;

    private String CORRECT_NAME = "Andrasi Oliver";
    private String CORRECT_EMAIL = "ado@gmaul.xor";
    private String CORRECT_PHONE = "12345678910";

    private String INCORRECT_NAME_PATTERN = "1234";
    private String INCORRECT_EMAIL_PATTERN = "wrong";
    private String INCORRECT_PHONE_PATTERN = "wrong";

    private String INCORRECT_NAME_TOO_SHORT = "";

    public void clickOnRegisterButton() {
//        Graphene.guardXhr(registerButton).click();
        registerButton.click();
    }

    public void switchOffAutocompleteOnInputs() {
        JavascriptExecutor js = (JavascriptExecutor) browser;
        js.executeScript("document.getElementsByName('mobileForm:memberForm:name')[0].setAttribute('autocomplete','off');");
        js.executeScript("document.getElementsByName('mobileForm:memberForm:email')[0].setAttribute('autocomplete','off');");
        js.executeScript("document.getElementsByName('mobileForm:memberForm:phoneNumber')[0].setAttribute('autocomplete','off');");
    }

    public void areAllErrorMessagesRendered(String... errMsgs) {
        List<WebElement> errorMsgsElements = getErrorMessages();
        boolean flag;

        for (String i : errMsgs) {
            flag = false;

            for (WebElement j : errorMsgsElements) {
                if (i.equals(j.getText())) {
                    flag = true;
                }
            }
            assertTrue(flag, "Error message: (" + i + ") was not rendered!");
        }
    }

    public void isAnyErrorMessageRendered(String... errMsgs) {
        List<WebElement> errorMsgsElements = getErrorMessages();
        boolean flag = false;

        for (String i : errMsgs) {

            for (WebElement j : errorMsgsElements) {
                if (i.equals(j.getText().trim())) {
                    flag = true;
                }
            }
        }
        assertTrue(flag, "None of messages: (" + errMsgs.toString() + ") were rendered!");
    }

    public void waitForErrorMessages(final String message, final int numberOfExpectedErrorMessagesBefore) {

        Graphene.waitAjax().withMessage(message).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return numberOfExpectedErrorMessagesBefore == getErrorMessages().size();
            }
        });
    }

    public void setEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void setName(String name) {
        nameInput.sendKeys(name);
    }

    public void setIncorrectNameTooShort() {
        nameInput.sendKeys(INCORRECT_NAME_TOO_SHORT);
    }

    public String setCorrectName() {
        nameInput.sendKeys(CORRECT_NAME);
        return CORRECT_NAME;
    }

    public String setCorrectEmail() {
        emailInput.sendKeys(CORRECT_EMAIL);
        return CORRECT_EMAIL;
    }

    public String setCorrectPhone() {
        phoneInput.sendKeys(CORRECT_PHONE);
        return CORRECT_PHONE;
    }

    public String setIncorrectNamePatternViolation() {
        nameInput.sendKeys(INCORRECT_NAME_PATTERN);
        return INCORRECT_NAME_PATTERN;
    }

    public String setIncorrectPhonePatternViolation() {
        phoneInput.sendKeys(INCORRECT_PHONE_PATTERN);
        return INCORRECT_PHONE_PATTERN;
    }

    public String setIncorrectEmailPatternViolation() {
        emailInput.sendKeys(INCORRECT_EMAIL_PATTERN);
        return INCORRECT_EMAIL_PATTERN;
    }

    public List<WebElement> getErrorMessages() {
        return errorMessages;
    }

    public void blur() {
        emailInput.click();
        nameInput.click();
    }

}
