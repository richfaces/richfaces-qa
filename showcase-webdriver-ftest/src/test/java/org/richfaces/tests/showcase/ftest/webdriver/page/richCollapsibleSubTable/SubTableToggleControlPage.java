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
package org.richfaces.tests.showcase.ftest.webdriver.page.richCollapsibleSubTable;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.ElementNotDisplayed;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SubTableToggleControlPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//table[@class='rf-dt']/tbody[1]//span[not(contains(@style, 'none'))]/img")
    private WebElement chevroletToggler;
    @FindBy(xpath = "//*[@class='example-cnt']//table[@class='rf-dt']/tbody[3]//span[not(contains(@style, 'none'))]/img")
    private WebElement fordToggler;
    @FindBy(xpath = "//*[@class='example-cnt']//table[@class='rf-dt']/tbody[5]//span[not(contains(@style, 'none'))]/img")
    private WebElement gmcToggler;
    @FindBy(xpath = "//*[@class='example-cnt']//table[@class='rf-dt']/tbody[7]//span[not(contains(@style, 'none'))]/img")
    private WebElement infinityToggler;
    @FindBy(xpath = "//*[@class='example-cnt']//table[@class='rf-dt']/tbody[9]//span[not(contains(@style, 'none'))]/img")
    private WebElement nissanToggler;
    @FindBy(xpath = "//*[@class='example-cnt']//table[@class='rf-dt']/tbody[11]//span[not(contains(@style, 'none'))]/img")
    private WebElement toyotaToggler;

    private WebDriver webDriver;

    public SubTableToggleControlPage(WebDriver webDriver) {
        Validate.notNull(webDriver);
        this.webDriver = webDriver;
    }

    @Override
    public String getDemoName() {
        return "subTableToggleControl";
    }

    @Override
    public String getSampleName() {
        return "subTableToggleControl";
    }

    public void toggleChevrolet() {
        toggleSubTable(chevroletToggler);
    }

    public void toggleFord() {
        toggleSubTable(fordToggler);
    }

    public void toggleGmc() {
        toggleSubTable(gmcToggler);
    }

    public void toggleInfinity() {
        toggleSubTable(infinityToggler);
    }

    public void toggleNissan() {
        toggleSubTable(nissanToggler);
    }

    public void toggleToyota() {
        toggleSubTable(toyotaToggler);
    }

    private WebElement createContentElement(WebElement toggler) {
        return toggler.findElement(By.xpath("following::tbody[1]"));
    }

    private void toggleSubTable(WebElement toggler) {
        WebElement content = createContentElement(toggler);
        boolean contentDisplayed = content.isDisplayed();
        toggler.click();
        if (contentDisplayed) {
            new WebDriverWait(webDriver)
                .failWith("After clicking on the toggler, the content subtable shouldn't be displayed.")
                .until(ElementNotDisplayed.getInstance().element(content));
        } else {
            new WebDriverWait(webDriver)
                .failWith("After clicking on the toggler, the content subtable should be displayed.")
                .until(ElementDisplayed.getInstance().element(content));
        }


    }
}