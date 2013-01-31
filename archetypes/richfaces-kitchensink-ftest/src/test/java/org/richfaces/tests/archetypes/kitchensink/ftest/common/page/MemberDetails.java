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
package org.richfaces.tests.archetypes.kitchensink.ftest.common.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MemberDetails {

    @FindBy(xpath="//*[@class='rf-pp-cnt']/descendant::input[contains(@id,'email')]")
    private WebElement emailOnDesktop;
    
    @FindBy(xpath="//span[contains(@id, 'email')]")
    private WebElement emailOnMobile;
    
    @FindBy(xpath="//*[@class='rf-pp-hdr-cntrls ']/a")
    private WebElement backToFormDesktop;
    
    @FindBy(id="back-button")
    private WebElement backToMenuMobile;

    public void waitMemberDetailsAreAvailableOnDesktop(int timeoutInSeconds, WebDriver webDriver) {
        (new WebDriverWait(webDriver, timeoutInSeconds)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {
                return backToFormDesktop.isDisplayed();
            }
        });
    }
    
    public WebElement getBackToMenuMobile() {
        return backToMenuMobile;
    }

    public void setBackToMenuMobile(WebElement backToMenuMobile) {
        this.backToMenuMobile = backToMenuMobile;
    }

    public WebElement getBackToFormDesktop() {
        return backToFormDesktop;
    }

    public void setBackToFormDesktop(WebElement backToFormDesktop) {
        this.backToFormDesktop = backToFormDesktop;
    }

    public WebElement getEmailOnDesktop() {
        return emailOnDesktop;
    }

    public void setEmailOnDesktop(WebElement emailOnDesktop) {
        this.emailOnDesktop = emailOnDesktop;
    }

    public WebElement getEmailOnMobile() {
        return emailOnMobile;
    }

    public void setEmailOnMobile(WebElement emailOnMobile) {
        this.emailOnMobile = emailOnMobile;
    }
    
    
}
