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
package org.richfaces.tests.metamer.ftest.abstractions.validations;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;

/**
 * Crate for validation message test case.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ValidationMessageCase {

    private final String name;
    private final RichFacesMessage message;
    private final WebElement buttonCorrect;
    private final WebElement buttonWrong;
    private final WebElement output;
    private final String expectedOutputString;
    private final String defaultOutputString;
    private final Set<String> possibleValidationMessages;

    public ValidationMessageCase(String name, RichFacesMessage message, WebElement buttonCorrect, WebElement buttonWrong, WebElement output, String expectedOutputString, String defaultOutputString, Set<String> possibleValidationMessages) {
        this.name = name;
        this.message = message;
        this.buttonCorrect = buttonCorrect;
        this.buttonWrong = buttonWrong;
        this.output = output;
        this.expectedOutputString = expectedOutputString;
        this.defaultOutputString = defaultOutputString;
        this.possibleValidationMessages = possibleValidationMessages;
    }

    public void assertDefaultOutput() {
        assertEquals(output.getText().trim(), defaultOutputString);
    }

    public void assertMessageDetailIsCorrect() {
        for (String string : possibleValidationMessages) {
            if (string.equals(message.getDetail())) {
                return;
            }
        }
        throw new AssertionError("The message detail is invalid. Have: '" + message.getDetail() + "'. Expected some of " + possibleValidationMessages);
    }

    public void assertMessageIsDisplayed() {
        assertTrue(Utils.isVisible(message.advanced().getRootElement()), "Message should be displayed.");
    }

    public void assertMessageIsHidden() {
        assertFalse(Utils.isVisible(message.advanced().getRootElement()), "Message should be hidden.");
    }

    public void assertOutput(Object expected) {
        assertEquals(output.getText().trim(), expected.toString());
    }

    public void assertValidOutput() {
        assertEquals(output.getText().trim(), expectedOutputString);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidationMessageCase other = (ValidationMessageCase) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public WebElement getButtonCorrect() {
        return buttonCorrect;
    }

    public WebElement getButtonWrong() {
        return buttonWrong;
    }

    public RichFacesMessage getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public WebElement getOutput() {
        return output;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public void setCorrect() {
        MetamerPage.waitRequest(buttonCorrect, WaitRequestType.XHR).click();
    }

    public void setWrong() {
        MetamerPage.waitRequest(buttonWrong, WaitRequestType.XHR).click();
    }

    @Override
    public String toString() {
        return name;
    }

    public void waitForMessageHide(WebDriverWait wait) {
        message.advanced().waitUntilMessageIsNotVisible().perform();
    }

    public void waitForMessageShow(WebDriverWait wait) {
        message.advanced().waitUntilMessageIsVisible().perform();
    }
}
