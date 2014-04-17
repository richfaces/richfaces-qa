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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.richfaces.fragment.dataTable.RichFacesDataTable;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.model.Capital;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableSimpleTest extends AbstractDataTableTest {

    protected static final int COLUMNS_TOTAL = 2;
    private static final String NO_DATA = "There is no data.";

    protected Integer first = null;

    protected Integer rows = null;

    private final Attributes<DataTableAttributes> dataTableAttributes = getAttributes();

    public void testRendered(RichFacesDataTable<?> table) {
        assertTrue(table.advanced().isVisible());

        dataTableAttributes.set(DataTableAttributes.rendered, false);

        assertFalse(table.advanced().isVisible());
        assertFalse(table.advanced().isNoData());
        assertEquals(table.advanced().getNumberOfColumns(), 0);
        assertEquals(table.advanced().getNumberOfRows(), 0);
    }

    public void testNoDataLabel(RichFacesDataTable<?> table) {
        assertTrue(table.advanced().isVisible());
        assertFalse(table.advanced().isNoData());

        dataTableAttributes.set(DataTableAttributes.noDataLabel, NO_DATA);
        enableShowData(false);

        assertTrue(table.advanced().isVisible());
        assertTrue(table.advanced().isNoData());
        assertEquals(table.advanced().getNumberOfColumns(), 0);
        assertEquals(table.advanced().getNumberOfRows(), 0);
        assertEquals(table.advanced().getNoDataElement().getText(), NO_DATA);
    }

    public void testFirst(RichFacesDataTable<?> table) {
        setFirstAttribute();
        rows = null;
        setRowsAttribute();
        verifyTable(table);
    }

    public void testRows(RichFacesDataTable<?> table) {
        setRowsAttribute();
        first = null;
        setFirstAttribute();
        verifyTable(table);
    }

    public void verifyTable(RichFacesDataTable<?> table) {
        assertTrue(table.advanced().isVisible());
        assertEquals(table.advanced().getNumberOfColumns(), getExpectedColumns());
        assertEquals(table.advanced().getNumberOfRows(), getExpectedRows());
        assertFalse(table.advanced().isNoData());
    }

    private List<Capital> getExpectedElements() {
        return CAPITALS.subList(getExpectedFirst(), getExpectedFirst() + getExpectedRows());
    }

    private void setFirstAttribute() {
        if (first == null) {
            dataTableAttributes.reset(DataTableAttributes.first);
        } else {
            dataTableAttributes.set(DataTableAttributes.first, first);
        }
    }

    private void setRowsAttribute() {
        if (rows == null) {
            dataTableAttributes.reset(DataTableAttributes.rows);
        } else {
            dataTableAttributes.set(DataTableAttributes.rows, rows);
        }
    }

    private int getExpectedFirst() {
        if (first == null) {
            return 0;
        } else {
            return Math.min(ELEMENTS_TOTAL, first);
        }
    }

    private int getExpectedRows() {
        if (rows == null) {
            return ELEMENTS_TOTAL - getExpectedFirst();
        } else {
            return Math.min(rows, ELEMENTS_TOTAL) - getExpectedFirst();
        }
    }

    private int getExpectedColumns() {
        if (getExpectedRows() > 0) {
            return COLUMNS_TOTAL;
        } else {
            return 0;
        }
    }
}