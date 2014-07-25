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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTable extends AbstractCollapsibleSubTableTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/simple.xhtml");
    }

    private WebElement getTestedRow() {
        return getSubTable(isMale).advanced().getTableRowsElements().get(0);
    }

    @Test
    @Templates("plain")
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testColumnClasses() {
        attributes.set(CollapsibleSubTableAttributes.columnClasses, "col1,col2,col3");
        CollapsibleSubTableWithEmployees subtable = getSubTable(isMale);
        EmployeeRecord entry;
        for (int i = 0; i < subtable.advanced().getNumberOfVisibleRows(); i += 2) {
            entry = subtable.getRow(i);
            assertTrue(entry.getNameElement().getAttribute("class").contains("col1"));
            assertTrue(entry.getTitleElement().getAttribute("class").contains("col2"));
            assertTrue(entry.getBirthdateElement().getAttribute("class").contains("col3"));
        }
    }

    @Test
    @UseWithField(field = "expandMode", valuesFrom = FROM_ENUM, value = "")
    public void testExpandMode() {
        attributes.set(CollapsibleSubTableAttributes.expandMode, expandMode);

        CollapsibleSubTableWithEmployees subTable = getSubTable(isMale);
        CollapsibleSubTableWithEmployees secondSubtable = getSubTable(!isMale);

        assertTrue(subTable.advanced().isExpanded());
        assertTrue(secondSubtable.advanced().isExpanded());
        if (expandMode.equals(ExpandMode.none)) {
            // when @expandMode=none, then the table cannot be expanded/collapsed
            toggleSubTable(false, expandMode, subTable);
            assertTrue(subTable.advanced().isExpanded());
            assertTrue(secondSubtable.advanced().isExpanded());
        } else {
            toggleSubTable(false, expandMode, subTable);
            assertFalse(subTable.advanced().isExpanded());
            assertTrue(secondSubtable.advanced().isExpanded());

            toggleSubTable(true, expandMode, subTable);
            assertTrue(subTable.advanced().isExpanded());
            assertTrue(secondSubtable.advanced().isExpanded());

            toggleSubTable(false, expandMode, secondSubtable);
            assertTrue(subTable.advanced().isExpanded());
            assertFalse(secondSubtable.advanced().isExpanded());

            toggleSubTable(true, expandMode, secondSubtable);
            assertTrue(subTable.advanced().isExpanded());
            assertTrue(secondSubtable.advanced().isExpanded());
        }
    }

    @Test
    @Templates(exclude = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    public void testFirst() {
        verifyFirst(Boolean.TRUE);
        verifyFirst(Boolean.FALSE);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12673")
    @Templates(value = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    public void testFirstInSwitchablePanels() {
        testFirst();
    }

    private void verifyFirst(boolean isMaleTable) {
        attributes.set(CollapsibleSubTableAttributes.first, 2);
        CollapsibleSubTableWithEmployees subTable = getSubTable(isMaleTable);

        List<Employee> visibleEmployees = getEmployees(isMaleTable).subList(2, subTable.advanced().getNumberOfVisibleRows() + 2);

        for (int i = 0; i < visibleEmployees.size(); i++) {
            assertEquals(subTable.getRow(i).getName(), visibleEmployees.get(i).getName());
            assertEquals(subTable.getRow(i).getTitle(), visibleEmployees.get(i).getTitle());
        }
    }

    @Test
    @Templates("plain")
    public void testNoDataLabel() {
        final String label = "new no data label";
        attributes.set(CollapsibleSubTableAttributes.noDataLabel, label);
        showDataInTable(false);
        assertEquals(getSubTable(isMale).advanced().getNoDataElement().getText(), label);
        assertEquals(getSubTable(!isMale).advanced().getNoDataElement().getText(), label);
    }

    @Test
    @Templates("plain")
    public void testOnrowclick() {
        testFireEvent("onrowclick", new Actions(driver).click(getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowdblclick() {
        testFireEvent("onrowdblclick", new Actions(driver).doubleClick(getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowkeydown() {
        testFireEvent("onrowkeydown", new Actions(driver).triggerEventByJS(Event.KEYDOWN, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowkeypress() {
        testFireEvent("onrowkeypress", new Actions(driver).triggerEventByJS(Event.KEYPRESS, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowkeyup() {
        testFireEvent("onrowkeyup", new Actions(driver).triggerEventByJS(Event.KEYUP, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowmousedown() {
        testFireEvent("onrowmousedown", new Actions(driver).triggerEventByJS(Event.MOUSEDOWN, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowmousemove() {
        testFireEvent("onrowmousemove", new Actions(driver).triggerEventByJS(Event.MOUSEMOVE, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowmouseout() {
        testFireEvent("onrowmouseout", new Actions(driver).triggerEventByJS(Event.MOUSEOUT, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowmouseover() {
        testFireEvent("onrowmouseover", new Actions(driver).triggerEventByJS(Event.MOUSEOVER, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testOnrowmouseup() {
        testFireEvent("onrowmouseup", new Actions(driver).triggerEventByJS(Event.MOUSEUP, getTestedRow()).build());
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        attributes.set(CollapsibleSubTableAttributes.rendered, false);
        CollapsibleSubTableWithEmployees subtable = getSubTable(true);
        CollapsibleSubTableWithEmployees subtable2 = getSubTable(false);
        assertFalse(subtable.advanced().isVisible());
        assertFalse(subtable2.advanced().isVisible());

        attributes.set(CollapsibleSubTableAttributes.rendered, true);

        assertTrue(subtable.advanced().isVisible());
        assertTrue(subtable2.advanced().isVisible());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10212")
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    @Templates("plain")
    public void testRowClass() {
        attributes.set(CollapsibleSubTableAttributes.rows, 13);
        attributes.set(CollapsibleSubTableAttributes.rowClass, "rowClass");
        CollapsibleSubTableWithEmployees subtable = getSubTable(isMale);

        int rowCount = subtable.advanced().getNumberOfVisibleRows();
        assertEquals(rowCount, 13);
        List<WebElement> tableRows = subtable.advanced().getTableRowsElements();

        for (int i = 0; i < rowCount; i++) {
            WebElement row = tableRows.get(i);
            assertTrue(row.getAttribute("class").contains("rowClass"));
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10212")
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    @Templates("plain")
    public void testRowClasses() {
        attributes.set(CollapsibleSubTableAttributes.rows, 13);
        attributes.set(CollapsibleSubTableAttributes.rowClasses, "row1,row2,row3");
        CollapsibleSubTableWithEmployees subtable = getSubTable(isMale);

        int rowCount = subtable.advanced().getNumberOfVisibleRows();
        assertEquals(rowCount, 13);
        List<WebElement> tableRows = subtable.advanced().getTableRowsElements();

        for (int i = 0; i < rowCount; i++) {
            WebElement row = tableRows.get(i);
            assertTrue(row.getAttribute("class").contains("row" + (i % 3 + 1)));
        }
    }

    @Test
    @Templates(exclude = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    public void testRows() {
        verifyRows(Boolean.TRUE);
        verifyRows(Boolean.FALSE);
    }

    @Test(groups = "Future")
    @RegressionTest("https://issues.jboss.org/browse/RF-12673")
    @Templates(value = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    public void testRowsInSwitchablePanels() {
        testRows();
    }

    private void verifyRows(boolean isMaleTable) {
        attributes.set(CollapsibleSubTableAttributes.rows, 11);
        List<Employee> visibleEmployees = getEmployees(isMaleTable).subList(0, 11);

        assertEquals(getSubTable(isMaleTable).advanced().getNumberOfVisibleRows(), 11);

        for (int i = 0; i < visibleEmployees.size(); i += 2) {
            assertEquals(getSubTable(isMaleTable).getRow(i).getName(), visibleEmployees.get(i).getName());
            assertEquals(getSubTable(isMaleTable).getRow(i).getTitle(), visibleEmployees.get(i).getTitle());
        }
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        testStyle(getSubTable(isMale).advanced().getTableRootElement());
        testStyle(getSubTable(!isMale).advanced().getTableRootElement());
    }
}
