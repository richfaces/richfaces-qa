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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton.FIRST;
import static org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton.LAST;

import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.fragment.dataTable.RichFacesDataTableWithHeaderAndFooter;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleFooterInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleHeaderInterface;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;

import static org.testng.Assert.assertEquals;

import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableScrollerTest extends AbstractDataTableTest {

    protected Integer rows;

    private final Attributes<DataTableAttributes> dataTableAttributes = getAttributes();

    public void testRowCountFooterScroller() {
        testRowCount(dataScroller2);
    }

    @Templates(exclude = {"a4jRepeat", "hDataTable", "richDataTable", "uiRepeat"})
    public void testRowCountOutsideTable() {
        testRowCount(dataScroller1);
    }

    public abstract RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface, ?, ? extends SimpleFooterInterface> getTable();

    private void testRowCount(RichFacesDataScroller dataScroller) {
        if (rows != null) {
            dataTableAttributes.set(DataTableAttributes.rows, rows);
        }

        if (dataScroller.hasPages() && dataScroller.getActivePageNumber() != 1) {
            dataScroller.switchTo(FIRST);
        }
        int rowCountPreset = Integer.parseInt(dataTableAttributes.get(DataTableAttributes.rows));
        int rowCountActual = getTable().advanced().getNumberOfRows();
        assertEquals(rowCountActual, Math.min(ELEMENTS_TOTAL, rowCountPreset));

        assertEquals(dataScroller.hasPages(), rowCountActual < ELEMENTS_TOTAL);
        if (dataScroller.hasPages()) {
            dataScroller.switchTo(LAST);

            int pagesExpected = pageCountActualExpected(rowCountActual);
            int countOfVisiblePages = dataScroller.advanced().getCountOfVisiblePages();

            if (countOfVisiblePages < pagesExpected) {
                int lastVisiblePage = dataScroller.advanced().getLastVisiblePageNumber();
                assertEquals(lastVisiblePage, pageCountActualExpected(rowCountActual));
            } else {
                assertEquals(countOfVisiblePages, pagesExpected);
            }

            int rowCountExpected = rowCountLastPageExpected(rowCountPreset);
            rowCountActual = getTable().advanced().getNumberOfRows();
            assertEquals(rowCountActual, rowCountExpected);
        }
    }

    private int pageCountActualExpected(int rowCountActual) {
        return Double.valueOf(Math.ceil((double) ELEMENTS_TOTAL / rowCountActual)).intValue();
    }

    private int rowCountLastPageExpected(int rowCountPreset) {
        int result = ELEMENTS_TOTAL % rowCountPreset;
        return (result == 0) ? rowCountPreset : result;
    }
}
