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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.dataTable.AbstractTable;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleFooterInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleHeaderInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleRowInterface;
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

    protected abstract AbstractTable<? extends SimpleHeaderInterface, ? extends SimpleRowInterface, ? extends SimpleFooterInterface> getTable();

    public void testRendered() {
        assertTrue(getTable().advanced().isVisible());

        dataTableAttributes.set(DataTableAttributes.rendered, false);

        assertFalse(getTable().advanced().isVisible());
        assertFalse(getTable().advanced().isNoData());
        assertEquals(getTable().advanced().getNumberOfColumns(), 0);
        assertEquals(getTable().advanced().getNumberOfVisibleRows(), 0);
    }

    public void testNoDataLabel() {
        assertTrue(getTable().advanced().isVisible());
        assertFalse(getTable().advanced().isNoData());

        dataTableAttributes.set(DataTableAttributes.noDataLabel, NO_DATA);
        enableShowData(false);

        assertTrue(getTable().advanced().isVisible());
        assertTrue(getTable().advanced().isNoData());
        assertEquals(getTable().advanced().getNumberOfColumns(), 0);
        assertEquals(getTable().advanced().getNumberOfVisibleRows(), 0);
        assertEquals(getTable().advanced().getNoDataElement().getText(), NO_DATA);
    }

    public void testOnrowclick() {
        testFireEvent("onrowclick", new Action() {
            @Override
            public void perform() {
                getTable().getFirstRow().getRootElement().click();
            }
        });
    }

    public void testOnrowdblclick() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.DBLCLICK, dataTableAttributes, DataTableAttributes.onrowdblclick);
    }

    public void testOnrowkeydown() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.KEYDOWN, dataTableAttributes, DataTableAttributes.onrowkeydown);
    }

    public void testOnrowkeypress() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.KEYPRESS, dataTableAttributes, DataTableAttributes.onrowkeypress);
    }

    public void testOnrowkeyup() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.KEYUP, dataTableAttributes, DataTableAttributes.onrowkeyup);
    }

    public void testOnrowmousedown() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.MOUSEDOWN, dataTableAttributes, DataTableAttributes.onrowmousedown);
    }

    public void testOnrowmousemove() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.MOUSEMOVE, dataTableAttributes, DataTableAttributes.onrowmousemove);
    }

    public void testOnrowmouseout() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.MOUSEOUT, dataTableAttributes, DataTableAttributes.onrowmouseout);
    }

    public void testOnrowmouseover() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.MOUSEOVER, dataTableAttributes, DataTableAttributes.onrowmouseover);
    }

    public void testOnrowmouseup() {
        testFireEventWithJS(getTable().getFirstRow().getStateColumn(), Event.MOUSEUP, dataTableAttributes, DataTableAttributes.onrowmouseup);
    }

    public void testRowClass() {
        dataTableAttributes.set(DataTableAttributes.rowClass, "metamer-ftest-class");
        for (int i = 0; i < getTable().advanced().getNumberOfVisibleRows(); i += 2) {
            assertTrue(getTable().getRow(i).getRootElement().getAttribute("class").contains("metamer-ftest-class"));
        }
    }

    public void testRowClasses() {
        dataTableAttributes.set(DataTableAttributes.rows, 13);
        dataTableAttributes.set(DataTableAttributes.rowClasses, "row1,row2,row3");
        int rowCount = getTable().advanced().getNumberOfVisibleRows();
        assertEquals(rowCount, 13);
        List<WebElement> tableRows = getTable().advanced().getTableRowsElements();

        for (int i = 0; i < rowCount; i++) {
            WebElement row = tableRows.get(i);
            assertTrue(row.getAttribute("class").contains("row" + (i % 3 + 1)));
        }
    }

    public void testFirst() {
        setFirstAttribute();
        rows = null;
        setRowsAttribute();
        verifyTable();
    }

    public void testRows() {
        setRowsAttribute();
        first = null;
        setFirstAttribute();
        verifyTable();
    }

    public void verifyTable() {
        assertTrue(getTable().advanced().isVisible());
        assertEquals(getTable().advanced().getNumberOfColumns(), getExpectedColumns());
        assertEquals(getTable().advanced().getNumberOfVisibleRows(), getExpectedRows());
        assertFalse(getTable().advanced().isNoData());
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
