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
package org.richfaces.tests.showcase.ftest.webdriver.page.richGraphValidator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class PasswordValidationPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//input[contains(@id, 'pass')]")
    private WebElement confirmation;
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'gv')]")
    private WebElement graphValidatorMessageArea;
    @FindBy(xpath = "//*[@class='example-cnt']//span[@class='rf-msgs-inf']")
    private WebElement infoMessageArea;
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'pass')][@class='rf-msg-err']")
    private WebElement validatorMessageArea;
    @FindBy(xpath = "//*[@class='example-cnt']//input[contains(@id, 'conf')]")
    private WebElement password;
    @FindBy(xpath = "//*[@class='example-cnt']//input[@type='submit']")
    private WebElement submit;

    @Override
    public String getDemoName() {
        return "graphValidator";
    }

    @Override
    public String getSampleName() {
        return "passwordValidation";
    }

    public WebElement getConfirmation() {
        return confirmation;
    }

    public WebElement getGraphValidatorMessageArea() {
        return graphValidatorMessageArea;
    }

    public WebElement getInfoMessageArea() {
        return infoMessageArea;
    }

    public WebElement getValidatorMessageArea() {
        return validatorMessageArea;
    }

    public WebElement getPassword() {
        return password;
    }

    public WebElement getSubmit() {
        return submit;
    }



}
