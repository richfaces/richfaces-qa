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
package org.richfaces.tests.showcase.ftest.webdriver.page.richInplaceInput;

import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractWebDriverPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class InplaceInputPage extends AbstractWebDriverPage {

    @FindBy(xpath = "//*[@class='example-cnt']//td[contains(text(), 'Email')]/../td[2]/span")
    private WebElement emailInput;
    @FindBy(xpath = "//*[@class='example-cnt']//td[contains(text(), 'Name')]/../td[2]/span")
    private WebElement nameInput;

    public InplaceInputPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getDemoName() {
        return "inplaceInput";
    }

    @Override
    public String getSampleName() {
        return "inplaceInput";
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getNameInput() {
        return nameInput;
    }

    public boolean isEmailInputFocused() {
        return isFocused(emailInput);
    }

    public boolean isNameInputFocused() {
        return isFocused(nameInput);
    }

    public void waitUntilFocused(WebElement input) {
        new WebDriverWait(getWebDriver())
            .failWith("The input should be focused.)")
            .until(ElementPresent.getInstance().element(input));
    }

    public void waitUntilNotFocused(WebElement input) {
        new WebDriverWait(getWebDriver())
            .failWith("The input shouldn't be focused.)")
            .until(ElementNotPresent.getInstance().element(input));
    }

    private boolean isFocused(WebElement input) {
        return input.getAttribute("class").contains("rf-ii-act");
    }

}
