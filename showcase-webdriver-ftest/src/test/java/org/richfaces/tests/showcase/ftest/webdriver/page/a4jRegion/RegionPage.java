/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.page.a4jRegion;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RegionPage implements ShowcasePage {
   
    @FindBy(xpath = "//*[@class='example-cnt']//table[contains(@id, 'userInfoPanel1')]//td[contains(text(), 'User email')]/..//input")
    private WebElement brokenEmailInput;
    @FindBy(xpath = "//*[@class='example-cnt']//table[contains(@id, 'userInfoPanel1')]//td[contains(text(), 'User Name')]/..//input")
    private WebElement brokenNameInput;
    @FindBy(xpath = "//*[@class='example-cnt']//table[contains(@id, 'echopanel1')]//td[text()='Entered email:']/../td[position()=2]")
    private WebElement brokenEmailOutput;
    @FindBy(xpath = "//*[@class='example-cnt']//table[contains(@id, 'echopanel1')]//td[text()='Entered name:']/../td[position()=2]")
    private WebElement brokenNameOutput;
    @FindBy(xpath = "//*[@class='example-cnt']//input[@type='submit'][@value='broken submit']")
    private WebElement brokenSubmit;
    @FindBy(xpath = "//*[@class='example-cnt']//th[contains(text(), 'User Info Panel with Region')]/../../..//td[contains(text(), 'User email')]/..//input")
    private WebElement emailInput;
    @FindBy(xpath = "//*[@class='example-cnt']//th[contains(text(), 'User Info Panel with Region')]/../../..//td[contains(text(), 'User Name')]/..//input")
    private WebElement nameInput;
    @FindBy(xpath = "//*[@class='example-cnt']//table[contains(@id, 'echopanel2')]//td[text()='Entered email:']/../td[position()=2]")
    private WebElement emailOutput;
    @FindBy(xpath = "//*[@class='example-cnt']//table[contains(@id, 'echopanel2')]//td[text()='Entered name:']/../td[position()=2]")
    private WebElement nameOutput;
    @FindBy(xpath = "//*[@class='example-cnt']//input[@type='submit'][@value='submit']")
    private WebElement submit;
    
    @Override
    public String getDemoName() {
        return "region";
    }

    @Override
    public String getSampleName() {
        return "region";
    }

    public WebElement getBrokenEmailInput() {
        return brokenEmailInput;
    }

    public WebElement getBrokenNameInput() {
        return brokenNameInput;
    }

    public WebElement getBrokenEmailOutput() {
        return brokenEmailOutput;
    }

    public WebElement getBrokenNameOutput() {
        return brokenNameOutput;
    }

    public WebElement getBrokenSubmit() {
        return brokenSubmit;
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getNameInput() {
        return nameInput;
    }

    public WebElement getEmailOutput() {
        return emailOutput;
    }

    public WebElement getNameOutput() {
        return nameOutput;
    }

    public WebElement getSubmit() {
        return submit;
    }

}
