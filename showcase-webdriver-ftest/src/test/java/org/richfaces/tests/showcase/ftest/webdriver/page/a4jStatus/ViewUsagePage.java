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
package org.richfaces.tests.showcase.ftest.webdriver.page.a4jStatus;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ViewUsagePage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='Address:']/../td/input")
    private WebElement addressInput;
    @FindBy(xpath = "//*[@class='example-cnt']//span[@class='rf-st-start']/img")
    private WebElement requestImage;
    @FindBy(xpath = "//*[@class='example-cnt']//div[text()='Simple search panel']/..//input[@type='text']")
    private WebElement searchInput;
    @FindBy(xpath = "//*[@class='example-cnt']//div[text()='Simple search panel']/..//input[@type='button']")
    private WebElement searchSubmit;
    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='User name:']/../td/input")
    private WebElement usernameInput;
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'out')]")
    private WebElement userOutput;
    @FindBy(xpath = "//*[@class='example-cnt']//div[text()='User Details Panel']/..//input[@type='button']")
    private WebElement userSubmit;
    
    @Override
    public String getDemoName() {
        return "status";
    }

    @Override
    public String getSampleName() {
        return "viewusage";
    }

    public WebElement getAddressInput() {
        return addressInput;
    }    
    
    public WebElement getSearchInput() {
        return searchInput;
    }
    
    public WebElement getSearchSubmit() {
        return searchSubmit;
    }
    
    public WebElement getRequestImage() {
        return requestImage;
    }

    public WebElement getUsernameInput() {
        return usernameInput;
    }

    public WebElement getUserOutput() {
        return userOutput;
    }
    
    public WebElement getUserSubmit() {
        return userSubmit;
    }
}
