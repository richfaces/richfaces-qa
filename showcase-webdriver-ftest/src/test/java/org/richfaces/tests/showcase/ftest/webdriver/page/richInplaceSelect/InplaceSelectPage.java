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
package org.richfaces.tests.showcase.ftest.webdriver.page.richInplaceSelect;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractWebDriverPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class InplaceSelectPage extends AbstractWebDriverPage {

    private static final String SELECT_LOCATOR = "//*[@class='example-cnt']//span[contains(@class, 'rf-is')]";

    @FindBy(xpath = SELECT_LOCATOR + "//span[contains(@class, 'rf-is-lbl')]")
    private WebElement label;
    @FindBy(xpath = SELECT_LOCATOR +  "//span[contains(@class,'rf-is-lbl')]")
    private WebElement openPopupArea;
    @FindBy(xpath = "//body/span[@class='rf-is-lst-cord']")
    private WebElement popup;

    private Select select;

    public InplaceSelectPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getDemoName() {
        return "inplaceSelect";
    }
    @Override
    public String getSampleName() {
        return "inplaceSelect";
    }

    public Select getSelect() {
        if (select == null) {
            select = new Select(getWebDriver(), label, popup, openPopupArea);
        }
        return select;
    }

}
