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

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.model.SortMode;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTableSorting extends AbstractCollapsibleSubTableTest {

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithSortingHeader dataTable;

    private final int rows = 15;

    private SortMode sortMode;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/sorting-using-column.xhtml");
    }

    @Test
    @Uses({
        @UseWithField(field = "sortMode", valuesFrom = FROM_ENUM, value = ""),
        @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    })
    @RegressionTest("https://issues.jboss.org/browse/RF-11302")
    public void testSorting() {
        attributes.set(CollapsibleSubTableAttributes.rows, rows);
        attributes.set(CollapsibleSubTableAttributes.sortMode, sortMode);

        SortingState sortingState = new SortingState(getEmployees(isMale));
        verifySorting(sortingState, sortMode, SortBy.NAME);
        verifySorting(sortingState, sortMode, SortBy.TITLE);
        verifySorting(sortingState, sortMode, SortBy.NAME);
        verifySorting(sortingState, sortMode, SortBy.TITLE);
    }

    @Override
    public DataTableWithCSTWithSortingHeader getDataTable() {
        return dataTable;
    }

    private void verifySorting(final SortingState state, final SortMode sortMode, final SortBy by) {
        state.sortEmployees(sortMode, by);
        List<Employee> expectedSortedEmployees = state.getSortedEmployees().subList(0, rows);
        getDataTable().sortBy(by);
        CollapsibleSubTableWithEmployees table = getSubTable(isMale);
        int rowCount = table.advanced().getNumberOfVisibleRows();
        assertEquals(rowCount, expectedSortedEmployees.size());

        EmployeeRecord entry;
        for (int i = 0; i < rowCount; i++) {
            entry = table.getRow(i);
            assertEquals(entry.getName(), expectedSortedEmployees.get(i).getName());
            assertEquals(entry.getTitle(), expectedSortedEmployees.get(i).getTitle());
        }
    }

    public static enum SortBy {

        NAME, TITLE, BIRTHDAY;
    }

    public static class DataTableWithCSTWithSortingHeader extends DataTableWithCST {

        @FindByJQuery(".rf-dt-hdr a:eq(0)")
        private WebElement sortByName;
        @FindByJQuery(".rf-dt-hdr a:eq(1)")
        private WebElement sortByTitle;
        @FindByJQuery(".rf-dt-hdr a:eq(2)")
        private WebElement sortByDate;

        public void sortBy(SortBy by) {
            switch (by) {
                case NAME:
                    Graphene.guardAjax(sortByName).click();
                    break;
                case TITLE:
                    Graphene.guardAjax(sortByTitle).click();
                    break;
                case BIRTHDAY:
                    Graphene.guardAjax(sortByDate).click();
                    break;
                default:
                    throw new UnsupportedOperationException("unknown sortBy " + by);
            }
        }
    }

    public static class SortingState {

        private final List<Employee> initialEmployees;
        private List<Employee> sortedEmployees;
        private final LinkedHashMap<SortBy, Boolean> sortPriority = new LinkedHashMap<SortBy, Boolean>();

        public SortingState(List<Employee> initialEmployees) {
            this.initialEmployees = initialEmployees;
        }

        public List<Employee> sortEmployees(final SortMode sortMode, final SortBy by) {
            sortedEmployees = Lists.newArrayList(initialEmployees);
            if (sortMode.equals(SortMode.single) && !sortPriority.containsKey(by)) {
                sortPriority.clear();
            }

            if (sortPriority.containsKey(by)) {
                Boolean val = sortPriority.get(by);
                if (sortPriority.size() > 1) {// need to add it to the end of the collection if the value is there already
                    sortPriority.remove(by);
                    sortPriority.put(by, !val);
                }
            } else {
                sortPriority.put(by, !sortPriority.containsKey(by));
            }
            sort(sortedEmployees, sortMode);
            return sortedEmployees;
        }

        public List<Employee> getSortedEmployees() {
            return sortedEmployees;
        }

        private void sort(List<Employee> list, final SortMode sortMode) {
            Collections.sort(list, new Comparator<Employee>() {
                @Override
                public int compare(Employee e1, Employee e2) {
                    if (sortMode == SortMode.single) {
                        SortBy sortBy = (SortBy) sortPriority.keySet().toArray()[0];
                        return _compare(sortPriority.get(sortBy), sortBy, e1, e2);
                    } else {
                        int result;
                        for (SortBy sortBy : sortPriority.keySet()) {
                            result = _compare(sortPriority.get(sortBy), sortBy, e1, e2);
                            if (result != 0) {
                                return result;
                            }
                        }
                        return 0;
                    }
                }
            });
        }

        private int _compare(boolean isAscending, final SortBy by, Employee e1, Employee e2) {
            int result;
            switch (by) {
                case BIRTHDAY:
                    Date d1 = e1.getBirthdate();
                    Date d2 = e2.getBirthdate();
                    d1 = (d1 == null ? new Date(0) : d1);
                    d2 = (d2 == null ? new Date(0) : d2);
                    result = d1.compareTo(d2);
                    break;
                case NAME:
                    result = e1.getName().compareToIgnoreCase(e2.getName());
                    break;
                case TITLE:
                    result = e1.getTitle().compareToIgnoreCase(e2.getTitle());
                    break;
                default:
                    throw new UnsupportedOperationException("unknown sortBy " + by);
            }
            return isAscending ? result : -result;
        }
    }
}
