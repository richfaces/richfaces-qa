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
package org.richfaces.tests.showcase.ftest.webdriver.page.richInputNumberSpinner;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.android.Key;
import org.jboss.test.selenium.android.ToolKit;
import org.jboss.test.selenium.android.ToolKitException;
import org.jboss.test.selenium.android.support.ui.AbstractComponent;
import org.jboss.test.selenium.support.ui.TextEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Spinner extends AbstractComponent {

    private WebElement downArrow;
    private WebElement input;
    private int step;
    private WebElement upArrow;
    
    public Spinner(WebDriver webDriver, ToolKit toolKit, WebElement input, WebElement downArrow, WebElement upArrow, int step) {
        super(webDriver, toolKit, input);
        Validate.notNull(downArrow);
        Validate.notNull(upArrow);
        this.input = input;
        this.downArrow = downArrow;
        this.upArrow = upArrow;
        this.step = step;
    }

    public void decrease() {
        final int before = getNumber();
        getDownArrow().click();
        new WebDriverWait(getWebDriver())
            .failWith("The number can't be decreased. Expected <" + (before - step) + ">, found <" + getNumber() + ">.")
            .until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver arg0) {
                    return getNumber() == before - step;
                }
            });
    }

    public int getNumber() {
        return Integer.valueOf(getInput().getAttribute("value"));
    }
    
    public void increase() {
        final int before = getNumber();
        getUpArrow().click();
        new WebDriverWait(getWebDriver())
            .failWith("The number can't be increased. Expected <" + (before + step) + ">, found <" + getNumber() + ">.")
            .until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver arg0) {
                    return getNumber() == before + step;
                }
            });
    }    
    
    public void setNumber(int number) throws ToolKitException {
        getInput().click();
        getInput().clear();
        // HACK:
        getToolKit().sendKey(Key.DELETE);
        new WebDriverWait(getWebDriver())
            .failWith("The input can't be cleared.")
            .until(TextEquals.getInstance().element(getInput()).text(""));
        getInput().sendKeys(String.valueOf(number));
    }
    
    private WebElement getDownArrow() {
        return downArrow;
    }
    
    private WebElement getInput() {
        return input;
    }

    private WebElement getUpArrow() {
        return upArrow;
    }
}
