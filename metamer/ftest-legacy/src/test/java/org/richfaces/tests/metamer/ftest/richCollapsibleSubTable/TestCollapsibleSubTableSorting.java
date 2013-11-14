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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.richfaces.model.SortMode;
import org.richfaces.model.SortOrder;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.DataTable;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22958 $
 */
public class TestCollapsibleSubTableSorting extends AbstractCollapsibleSubTableTest {

    @Inject
    @Use(enumeration = true)
    SortMode sortMode;

    int rows = 0;

    List<Column> sortPriority = new LinkedList<Column>();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/sorting-using-column.xhtml");
    }

    @BeforeMethod
    public void prepare() {
        sortPriority.clear();
        Column.resetAll();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11302")
    public void testSorting() {
        collapsibleSubTabAttributes.set(CollapsibleSubTableAttributes.rows, rows);
        collapsibleSubTabAttributes.set(CollapsibleSubTableAttributes.sortMode, sortMode);

        sortBy(Column.NAME);
        verifySorting();

        sortBy(Column.TITLE);
        verifySorting();

        sortBy(Column.NAME);
        verifySorting();

        sortBy(Column.TITLE);
        verifySorting();
    }

    private void sortBy(Column column) {
        if (sortMode == SortMode.single) {
            Column.resetAll();
            resetEmployees();
            sortPriority.clear();
        }
        column.sort(dataTable);
        sortPriority.remove(column);
        sortPriority.add(column);
    }

    public void verifySorting() {
        sortEmployees();

        List<Employee> visibleEmployees = employees.subList(0, Math.max(rows, employees.size()));

        int rowCount = subtable.getRowCount();
        assertEquals(rowCount, visibleEmployees.size());

        for (int i = 0; i < visibleEmployees.size(); i++) {
            String name = selenium.getText(subtable.getCell(1, i + 1));
            String title = selenium.getText(subtable.getCell(2, i + 1));

            assertEquals(name, visibleEmployees.get(i).getName());
            assertEquals(title, visibleEmployees.get(i).getTitle());
        }
    }

    public void sortEmployees() {
        employees = new LinkedList<Employee>(employees);
        Collections.sort(employees, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                if (sortMode == SortMode.single) {
                    Column lastColumn = sortPriority.get(sortPriority.size() - 1);
                    return lastColumn.compare(o1, o2);
                } else {
                    for (Column column : sortPriority) {
                        if (column.compare(o1, o2) != 0) {
                            return column.compare(o1, o2);
                        }
                    }
                    return 0;
                }
            }
        });
    }

    public enum Column {
        NAME, TITLE, BIRTHDAY;

        GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();
        SortOrder sortOrder = SortOrder.unsorted;

        public static void resetAll() {
            for (Column value : values()) {
                value.reset();
            }
        }

        public void reset() {
            sortOrder = SortOrder.unsorted;
        }

        public void sort(DataTable dataTable) {
            sortOrder = (sortOrder == SortOrder.ascending) ? SortOrder.descending : SortOrder.ascending;
            int headerIndex = this.ordinal() + 2; // 1 for index; 1 for additional header on top of links
            guardXhr(selenium).click(dataTable.getHeader().get(headerIndex).getChild(jq("a")));
        }

        public int compare(Employee o1, Employee o2) {
            int result;

            switch (this) {
                case NAME:
                    result = o1.getName().compareToIgnoreCase(o2.getName());
                    break;
                case TITLE:
                    result = o1.getTitle().compareToIgnoreCase(o2.getTitle());
                    break;
                case BIRTHDAY:
                    result = o1.getBirthdate().compareTo(o2.getBirthdate());
                    break;
                default:
                    throw new IllegalStateException();
            }

            switch (sortOrder) {
                case ascending:
                    return result;
                case descending:
                    return 0 - result;
                default:
                    throw new IllegalStateException();
            }
        }
    }

}
