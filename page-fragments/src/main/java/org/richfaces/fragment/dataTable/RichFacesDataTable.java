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
package org.richfaces.fragment.dataTable;

import java.util.List;
import org.jboss.arquillian.graphene.findby.ByJQuery;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class RichFacesDataTable<HEADER, ROW, FOOTER> extends AbstractTable<HEADER, ROW, FOOTER> {

    @FindBy(css = ".rf-dt-b .rf-dt-r")
    private List<WebElement> tableRows;

    @FindByJQuery(".rf-dt-b .rf-dt-r:eq(0) .rf-dt-c")
    private List<WebElement> firstRowCells;

    @FindBy(css = ".rf-dt-nd > .rf-dt-nd-c")
    private WebElement noDataElement;

    @FindBy(className = "rf-dt-thd")
    private WebElement wholeTableHeader;

    @FindBy(className = "rf-dt-tft")
    private WebElement wholeTableFooter;

    @FindBy(css = "th.rf-dt-hdr-c")
    private WebElement headerElement;

    @FindBy(className = "rf-dt-ftr-c")
    private WebElement footerElement;

    @FindBy(className = "rf-dt-shdr-c")
    private List<WebElement> columnHeaders;

    @FindBy(className = "rf-dt-sftr-c")
    private List<WebElement> columnFooters;

    protected List<WebElement> getTableRows() {
        return tableRows;
    }

    protected List<WebElement> getFirstRowCells() {
        return firstRowCells;
    }

    protected ByJQuery getSelectorForCell(int column) {
        return ByJQuery.selector(".rf-dt-c:eq(" + column + ")");
    }

    protected WebElement protectedGetNoData() {
        return noDataElement;
    }

    protected WebElement getWholeTableHeader() {
        return wholeTableHeader;
    }

    protected WebElement getWholeTableFooter() {
        return wholeTableFooter;
    }

    protected WebElement getHeaderElement() {
        return headerElement;
    }

    protected WebElement getFooterElement() {
        return footerElement;
    }

    protected List<WebElement> getColumnHeaderElements() {
        return columnHeaders;
    }

    protected List<WebElement> getColumnFooterElements() {
        return columnFooters;
    }
}