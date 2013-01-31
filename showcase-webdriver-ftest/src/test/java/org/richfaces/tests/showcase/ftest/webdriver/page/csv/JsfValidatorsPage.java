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
package org.richfaces.tests.showcase.ftest.webdriver.page.csv;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class JsfValidatorsPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//span[@class='rf-msg-err'][contains(@id, 'age')]")
    private WebElement ageErrorArea;
    @FindBy(xpath = "//*[@class='example-cnt']//input[contains(@id, 'age')]")
    private WebElement ageInput;
    @FindBy(xpath = "//*[@class='example-cnt']//span[@class='rf-msg-err'][contains(@id, 'email')]")
    private WebElement emailErrorArea;
    @FindBy(xpath = "//*[@class='example-cnt']//input[contains(@id, 'email')]")
    private WebElement emailInput;
    @FindBy(xpath = "//*[@class='example-cnt']//span[@class='rf-msg-err'][contains(@id, 'name')]")
    private WebElement nameErrorArea;
    @FindBy(xpath = "//*[@class='example-cnt']//input[contains(@id, 'name')]")
    private WebElement nameInput;
    @FindBy(xpath = "//*[@class='example-cnt']//div[contains(@class, 'rf-p-hdr')]")
    private WebElement clickToLoseFocus;

    @Override
    public String getDemoName() {
        return "clientValidation";
    }
    @Override
    public String getSampleName() {
        return "jsfValidators";
    }

    public WebElement getAgeErrorArea() {
        return ageErrorArea;
    }
    public WebElement getAgeInput() {
        return ageInput;
    }
    public WebElement getEmailErrorArea() {
        return emailErrorArea;
    }
    public WebElement getEmailInput() {
        return emailInput;
    }
    public WebElement getNameErrorArea() {
        return nameErrorArea;
    }
    public WebElement getNameInput() {
        return nameInput;
    }

    public void loseFocus() {
        clickToLoseFocus.click();
    }

}
