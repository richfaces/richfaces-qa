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
package org.richfaces.tests.archetypes.kitchensink.ftest.common.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MembersTable {

    @FindBy(xpath = "//*[@class='rf-dt-b']")
    private WebElement table;

    @FindBy(xpath = "//*[@class='rf-dt-b']/tr")
    private WebElement tableRow;

    @FindBy(xpath = "//*[@class='rf-dt-b']/tr")
    private List<WebElement> tableRows;

    @FindBy(xpath = "//*[@class='rf-dt-ftr-c']/a")
    private WebElement urlAllMembersRestData;

    public WebElement getUrlAllMembersRestData() {
        return urlAllMembersRestData;
    }

    public void setUrlAllMembersRestData(WebElement urlAllMembersRestData) {
        this.urlAllMembersRestData = urlAllMembersRestData;
    }

    public List<WebElement> getTableRows() {
        return tableRows;
    }

    public void setTableRows(List<WebElement> tableRows) {
        this.tableRows = tableRows;
    }

    public WebElement getTable() {
        return table;
    }

    public void setTable(WebElement table) {
        this.table = table;
    }

    public WebElement getTableRow() {
        return tableRow;
    }

    public void setTableRow(WebElement tableRow) {
        this.tableRow = tableRow;
    }

    public int getNumberOfRows() {
        return table.findElements(By.xpath("//*[@class='rf-dt-b']/tr")).size();
    }

    public void waitUntilNumberOfRowsChanged(int timeoutInSeconds, WebDriver webDriver, final int numberOfRowsBefore) {
        (new WebDriverWait(webDriver, timeoutInSeconds)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {

                int numberOfRowsAfter = getNumberOfRows();
                return (numberOfRowsBefore < numberOfRowsAfter);
            }
        });
    }

}
