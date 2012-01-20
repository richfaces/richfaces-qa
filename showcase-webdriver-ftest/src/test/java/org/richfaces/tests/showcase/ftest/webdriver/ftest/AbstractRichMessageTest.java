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
package org.richfaces.tests.showcase.ftest.webdriver.ftest;

import static org.testng.Assert.assertTrue;

import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractRichMessagePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractRichMessageTest<Page extends AbstractRichMessagePage> extends AbstractWebDriverTest<Page> {

    protected void checkErrorNotPresent(WebElement errorArea) {
        new WebDriverWait(getWebDriver())
            .failWith("Error message should disappear.")
            .until(ElementNotPresent.getInstance().element(errorArea));
    }

    protected void checkErrorPresent(WebElement errorArea) {
        new WebDriverWait(getWebDriver())
            .failWith("Error message should appear.")
            .until(ElementPresent.getInstance().element(errorArea));
        assertTrue(errorArea.getText().contains("Validation Error"));
    }

    protected void setAddressCorrect() {
        setInputText(getPage().getAddressInput(), "1234567890");
    }

    protected void setAddressWrong() {
        setInputText(getPage().getAddressInput(), "123456789");
    }

    protected void setJobCorrect() {
        setInputText(getPage().getJobInput(), "123");
    }

    protected void setJobWrong() {
        setInputText(getPage().getJobInput(), "12");
    }

    protected void setInputText(WebElement input, String text) {
        input.click();
        input.clear();
        input.sendKeys(text);
    }

    protected void setNameCorrect() {
        setInputText(getPage().getNameInput(), "123");
    }

    protected void setNameWrong() {
        setInputText(getPage().getNameInput(), "12");
    }

    protected void setZipCorrect() {
        setInputText(getPage().getZipInput(), "1234");
    }

    protected void setZipWrong() {
        setInputText(getPage().getZipInput(), "123");
    }

    protected void testAddressWrongAndCorrect() {
        setAddressWrong();
        getPage().getSubmitButton().click();
        checkErrorPresent(getPage().getAddressErrorMessageArea());
        setAddressCorrect();
        getPage().getSubmitButton().click();
        checkErrorNotPresent(getPage().getAddressErrorMessageArea());
    }

    protected void testAllWrongAndCorrect() {
        setAddressWrong();
        setJobWrong();
        setNameWrong();
        setZipWrong();
        getPage().getSubmitButton().click();
        checkErrorPresent(getPage().getAddressErrorMessageArea());
        checkErrorPresent(getPage().getJobErrorMessageArea());
        checkErrorPresent(getPage().getNameErrorMessageArea());
        checkErrorPresent(getPage().getZipErrorMessageArea());
        setAddressCorrect();
        setJobCorrect();
        setNameCorrect();
        setZipCorrect();
        getPage().getSubmitButton().click();
        checkErrorNotPresent(getPage().getAddressErrorMessageArea());
        checkErrorNotPresent(getPage().getJobErrorMessageArea());
        checkErrorNotPresent(getPage().getNameErrorMessageArea());
        checkErrorNotPresent(getPage().getZipErrorMessageArea());
    }

    protected void testJobWrongAndCorrect() {
        setJobWrong();
        getPage().getSubmitButton().click();
        checkErrorPresent(getPage().getJobErrorMessageArea());
        setJobCorrect();
        getPage().getSubmitButton().click();
        checkErrorNotPresent(getPage().getJobErrorMessageArea());
    }

    protected void testNameWrongAndCorrect() {
        setNameWrong();
        getPage().getSubmitButton().click();
        checkErrorPresent(getPage().getNameErrorMessageArea());
        setNameCorrect();
        getPage().getSubmitButton().click();
        checkErrorNotPresent(getPage().getNameErrorMessageArea());
    }

    protected void testZipWrongAndCorrect() {
        setZipWrong();
        getPage().getSubmitButton().click();
        checkErrorPresent(getPage().getZipErrorMessageArea());
        setZipCorrect();
        getPage().getSubmitButton().click();
        checkErrorNotPresent(getPage().getZipErrorMessageArea());
    }

}
