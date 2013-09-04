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
package org.richfaces.tests.showcase.ftest.webdriver.page.richSelect;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.test.selenium.android.support.ui.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Select extends AbstractComponent {

    private WebElement arrow;
    private WebElement input;
    private WebElement popup;

    public Select(WebDriver webDriver, WebElement input, WebElement popup, WebElement arrow) {
        super(webDriver, input);
        Validate.notNull(popup);
        Validate.notNull(arrow);
        this.input = input;
        this.popup = popup;
        this.arrow = arrow;
    }

    public String getText() {
        return getInput().getAttribute("value");
    }

    public boolean isPopupPresent() {
        try {
            popup.isDisplayed();
            return true;
        } catch(NoSuchElementException ignored) {
            return false;
        }
    }

    public void showPopupByArrow() {
        getArrow().click();
        waitUntilPopupPresent();
    }

    public void showPopupByClick() {
        getInput().click();
        waitUntilPopupPresent();
    }

    public void selectFromPopupByIndex(int index) {
        if (!isPopupPresent()) {
            throw new IllegalStateException("The popup is not present, so the user can't select an option.");
        }
        WebElement option = getPopup().findElement(By.xpath("div[@class='rf-sel-shdw']//div[@class='rf-sel-opt'][" + (index + 1) + "]"));
        final String expectedValue = option.getText();
        option.click();
        Graphene.waitAjax()
            .withMessage("The option <" + index + "> can't be selected.")
            .until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver arg0) {
                    return getText().equals(expectedValue);
                }
            });
    }

    public void setText(String text) {
        getInput().click();
        getInput().clear();
        getInput().sendKeys(text);
    }

    private WebElement getArrow() {
        return arrow;
    }

    private WebElement getInput() {
        return input;
    }

    private WebElement getPopup() {
        return popup;
    }


    private void waitUntilPopupPresent() {
        Graphene.waitAjax()
            .withMessage("The popup isn't present.")
            .until(new WebElementConditionFactory(getPopup()).isPresent());
    }

}
