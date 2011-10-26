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
package org.richfaces.tests.showcase.ftest.webdriver.page.richDataScroller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.model.Car;
import org.richfaces.tests.showcase.ftest.webdriver.page.DataScrollablePage;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SimpleScrollingPage extends DataScrollablePage implements ShowcasePage {
    
    @FindBy(xpath = "//table[contains(@id, 'table')]/tbody[1]/tr[1]")
    private WebElement firstCarRow;
    @FindBy(xpath = "//table[contains(@id, 'table')]/tbody[1]/tr[last()]")
    private WebElement lastCarRow;
    
    public SimpleScrollingPage(WebDriver webDriver) {
        super(webDriver);
    }
    
    @Override
    public String getDemoName() {
        return "dataScroller";
    }

    @Override
    public String getSampleName() {
        return "simpleScrolling";
    }

    public Car getFirstCar() {
        return createCarFromRow(firstCarRow);
    }
    
    public Car getLastCar() {
        return createCarFromRow(lastCarRow);
    }
    
    private Car createCarFromRow(WebElement row) {
        return new Car(
            row.findElement(By.xpath("td[1]")).getText(),
            row.findElement(By.xpath("td[2]")).getText()
        );
    }
    
}
