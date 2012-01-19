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
package org.richfaces.tests.showcase.ftest.webdriver.page.richInplaceSelect;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.android.ToolKit;
import org.jboss.test.selenium.android.support.ui.AbstractComponent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Select extends AbstractComponent {

    private WebElement label;
    private WebElement openPopupArea;
    private WebElement popup;
    
    public Select(WebDriver webDriver, ToolKit toolKit, WebElement label, WebElement popup, WebElement openPopupArea) {
        super(webDriver, toolKit, label);
        Validate.notNull(popup);
        Validate.notNull(openPopupArea);
        this.label = label;
        this.popup = popup;
        this.openPopupArea = openPopupArea;
    }

    public String getText() {
        return getLabel().getText();
    }
    
    public boolean isPopupPresent() {
        try {
            popup.isDisplayed();
            return true;
        } catch(NoSuchElementException ignored) {
            return false;
        }
    }    
    
    public void selectFromPopupByIndex(int index) {
        if (!isPopupPresent()) {
            throw new IllegalStateException("The popup is not present, so the user can't select an option.");
        }
        WebElement option = getPopup().findElement(By.xpath("span//span[@class='rf-is-opt'][" + (index + 1) + "]"));
        final String expectedValue = option.getText();
        option.click();
        new WebDriverWait(getWebDriver())
            .failWith("The option <" + index + "> can't be selected.")
            .until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver arg0) {
                    return getText().equals(expectedValue);
                }
            });
    }
    
    public void showPopupByClick() {
        getOpenPopupArea().click();
        waitUntilPopupPresent();
    }    
    
    private WebElement getLabel() {
        return label;
    }

    private WebElement getOpenPopupArea() {
        return openPopupArea;
    }
    
    private WebElement getPopup() {
        return popup;
    }
    
    private void waitUntilPopupPresent() {
        new WebDriverWait(getWebDriver())
            .failWith("The popup isn't present.")
            .until(ElementPresent.getInstance().element(getPopup()));
    } 
    
}
