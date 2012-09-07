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
package org.richfaces.tests.showcase.ftest.webdriver.page.richSelect;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractWebDriverPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SelectPage extends AbstractWebDriverPage {

    private static final String FIRST_SELECT_LOCATOR = "//*[@class='example-cnt']//div[@class='rf-p '][1]//div[@class='rf-sel']";
    private static final String SECOND_SELECT_LOCATOR = "//*[@class='example-cnt']//div[@class='rf-p '][2]//div[@class='rf-sel']";

    @FindBy(xpath = FIRST_SELECT_LOCATOR + "//span[@class='rf-sel-btn-arrow']")
    private WebElement firstSelectArrow;
    @FindBy(xpath = FIRST_SELECT_LOCATOR + "//input[@type='text']")
    private WebElement firstSelectInput;
    private Select firstSelect;

    @FindBy(xpath = SECOND_SELECT_LOCATOR + "//span[@class='rf-sel-btn-arrow']")
    private WebElement secondSelectArrow;
    @FindBy(xpath = SECOND_SELECT_LOCATOR + "//input[@type='text']")
    private WebElement secondSelectInput;
    private Select secondSelect;

    @FindBy(xpath = "//body/div[@class='rf-sel-lst-cord']")
    private WebElement popup;

    public SelectPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getDemoName() {
        return "select";
    }

    @Override
    public String getSampleName() {
        return "select";
    }

    public Select getFirstSelect() {
        if (firstSelect == null) {
            firstSelect = new Select(getWebDriver(), firstSelectInput, popup, firstSelectArrow);
        }
        return firstSelect;
    }

    public Select getSecondSelect() {
        if (secondSelect == null) {
            secondSelect = new Select(getWebDriver(), secondSelectInput, popup, secondSelectArrow);
        }
        return secondSelect;
    }
}
