/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richColumn;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataTable.RichFacesDataTable;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractColumnTest extends AbstractWebDriverTest {

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTable table;

    protected List<Capital> capitals;

    protected Attributes<ColumnAttributes> columnAttributes = getAttributes();

    public DataTable getTable() {
        return table;
    }

    @BeforeMethod
    public void prepareModel() {
        capitals = Model.unmarshallCapitals();
    }

    public static class DataTable extends RichFacesDataTable<Header, StateCapitalRow, Footer> {

        @FindBy(css = ".rf-dt-sftr>td")
        private WebElement columnFooterElement;
        @FindBy(css = ".rf-dt-shdr>th")
        private WebElement columnHeaderElement;

        public WebElement getColumnFooterElement() {
            return columnFooterElement;
        }

        public WebElement getColumnHeaderElement() {
            return columnHeaderElement;
        }
    }

    public static class Header {

        @Root
        private WebElement rootElement;

        @FindBy(className = "rf-dt-hdr")
        private List<WebElement> rows;

        public WebElement getCell(int row, int cell) {
            return getRow(row).findElements(By.className("rf-dt-hdr-c")).get(cell);
        }

        public WebElement getRootElement() {
            return rootElement;
        }

        public WebElement getRow(int row) {
            return getRows().get(row);
        }

        public List<WebElement> getRows() {
            return rows;
        }
    }

    public static class StateCapitalRow extends Capital {

        @Root
        private WebElement rootElement;

        @FindByJQuery("td:eq(1)")
        private WebElement nameElement;
        @FindByJQuery("td:eq(0)")
        private WebElement stateElement;

        @Override
        public String getName() {
            return getNameElement().getText();
        }

        public WebElement getNameElement() {
            return nameElement;
        }

        public WebElement getRootElement() {
            return rootElement;
        }

        public int getRowElementsCount() {
            return getRootElement().findElements(By.tagName("td")).size();
        }

        @Override
        public String getState() {
            return getStateElement().getText();
        }

        public WebElement getStateElement() {
            return stateElement;
        }

        @Override
        public String toString() {
            return getName() + " (" + getState() + ")";
        }
    }

    public static class Footer {

        @Root
        private WebElement rootElement;

        @FindBy(tagName = "td")
        private WebElement footer;
        @FindBy(className = "rf-dt-ftr")
        private List<WebElement> rows;

        public WebElement getCell(int row, int cell) {
            return getRow(row).findElements(By.className("rf-dt-ftr-c")).get(cell);
        }

        public String getFooterClass() {
            return footer.getAttribute("class");
        }

        public WebElement getRootElement() {
            return rootElement;
        }

        public WebElement getRow(int row) {
            return getRows().get(row);
        }

        public List<WebElement> getRows() {
            return rows;
        }

    }
}
