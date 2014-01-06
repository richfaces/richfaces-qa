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
package org.richfaces.tests.showcase.status;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;

/**
 * @author pmensik
 * @author jhuska
 */
public abstract class AbstractStatusTest extends AbstractWebDriverTest {

    @ArquillianResource
    private JavascriptExecutor jsExecutor;

    protected void checkTypingIntoInputAndItsStatus(WebElement input, String statusLocator) {
        registerKeyPressHandlerToInput(input, statusLocator);
        for (int i = 0; i < 15; i++) {
            input.sendKeys("abcdefg");
        }
        assertProgressBarWasVisibility();
    }

    protected void checkClickingOnSubmitButtonAndItsStatus(WebElement submitButton, String progressLocator) {
        registerClickHandlerToButton(submitButton, progressLocator);
        for (int i = 0; i < 15; i++) {
            submitButton.click();
        }
        assertProgressBarWasVisibility();
    }

    private void registerKeyPressHandlerToInput(WebElement input, String progressLocator) {
        registerHandlerToElementWhichFindsOutProgressVisibility(input, progressLocator, "keypress");
    }

    private void registerClickHandlerToButton(WebElement button, String progressLocator) {
        registerHandlerToElementWhichFindsOutProgressVisibility(button, progressLocator, "click");
    }

    private void assertProgressBarWasVisibility() {
        assertEquals(jsExecutor.executeScript("return window.document.progressVisibility"), true);
    }

    private void registerHandlerToElementWhichFindsOutProgressVisibility(WebElement element, String progressLocator,
        String whichHandler) {
        jsExecutor.executeScript("window.document.progressVisibility = false;" + "$(arguments[0])." + whichHandler
            + "(function() {" + "window.document.progressVisibility = $(\"" + progressLocator + "\").is(':visible');" + "});",
            element);
    }
}
