/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton.FIRST;
import static org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton.LAST;
import static org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes.sortMode;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.richfaces.fragment.dataTable.AbstractTable;
import org.richfaces.model.SortMode;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.FilteringRowInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SortingHeaderInterface;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.model.Employee;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableSortingTest extends AbstractDataTableTest {

    private final Attributes<DataTableAttributes> dataTableAttributes = getAttributes();
    private boolean isBuiltIn = false;
    private int modelIndex;
    private List<Employee> sortedEmployees;

    /**
     * Created a list containing five number of rows to be tested. These number are relative to the amount of rows visible on
     * page.
     *
     * @param visiblePageRows number of visible table rows on page
     * @return List containing five numbers (int) of rows to test. These numbers are relative to number of rows on page.
     */
    private List<Integer> getListWithTestPages(int visiblePageRows) {
        List<Integer> rowsToTest = new ArrayList<Integer>();
        rowsToTest.add(0); // first item
        rowsToTest.add((int) Math.round((visiblePageRows - 1) / 2 - 0.5 * (visiblePageRows - 1) / 2)); // item in first quarter
        rowsToTest.add(Math.round((visiblePageRows - 1) / 2)); // item in half
        rowsToTest.add((int) Math.round((visiblePageRows - 1) / 2 + 0.5 * (visiblePageRows - 1) / 2)); // item in third quarter
        rowsToTest.add(visiblePageRows - 1); // last item
        return Collections.unmodifiableList(rowsToTest);
    }

    public <T> Comparator<T> getPropertyComparator(final Class<T> classT, final String... properties) {
        return new Comparator<T>() {

            @Override
            public int compare(T o1, T o2) {
                for (String property : properties) {
                    boolean reverse = property.endsWith("-");
                    String getterName = "get" + StringUtils.capitalize(property.replace("-", ""));
                    try {
                        Method getter = classT.getMethod(getterName);

                        Object got1 = getter.invoke(o1);
                        Object got2 = getter.invoke(o2);
                        int result;

                        if (String.class.equals(getter.getReturnType())) {
                            Method comparecompareToIgnoreCase = got1.getClass().getMethod("compareToIgnoreCase",
                                got2.getClass());
                            result = (Integer) comparecompareToIgnoreCase.invoke(got1, got2);
                        } else if (got1 instanceof Comparable<?> && got1 instanceof Comparable<?>) {
                            result = ((Comparable) got1).compareTo(got2);
                            // Method compareTo = got1.getClass().getMethod("compareTo", got2.getClass());
                            // result = (Integer) compareTo.invoke(got1, got2);
                        } else {
                            throw new IllegalStateException("Cannot compare values");
                        }

                        if (result != 0) {
                            if (reverse) {
                                return -result;
                            } else {
                                return result;
                            }
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Cannot obtain property '" + property + "'", e);
                    }
                }
                return 0;
            }
        };
    }

    protected abstract AbstractTable<? extends SortingHeaderInterface, ? extends FilteringRowInterface, ?> getTable();

    public void setBuiltIn(boolean isBuiltIn) {
        this.isBuiltIn = isBuiltIn;
    }

    public void sortByColumn(int column) {
        switch (column) {
            case COLUMN_SEX:
                getTable().getHeader().sortBySex(isBuiltIn);
                break;
            case COLUMN_NAME:
                getTable().getHeader().sortByName(isBuiltIn);
                break;
            case COLUMN_TITLE:
                getTable().getHeader().sortByTitle(isBuiltIn);
                break;
            case COLUMN_NUMBER_OF_KIDS1:
                getTable().getHeader().sortByNumberOfKids(isBuiltIn);
                break;
            default:
                throw new IllegalArgumentException("Wrong number of column passed! Such does not exist!");
        }
    }

    public void testSortModeMulti() {
        dataTableAttributes.set(sortMode, SortMode.multi);

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        sortByColumn(COLUMN_SEX);
        verifySortingByColumns("title", "sex");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("title", "sex", "numberOfKids");

        sortByColumn(COLUMN_NAME);
        verifySortingByColumns("title", "sex", "numberOfKids", "name");
    }

    public void testSortModeMultiFullPageRefresh() {
        dataTableAttributes.set(sortMode, SortMode.multi);

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("title", "numberOfKids-");

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("numberOfKids-", "title-");

        getMetamerPage().fullPageRefresh();
        verifySortingByColumns("numberOfKids-", "title-");
    }

    public void testSortModeMultiReplacingOldOccurences() {
        dataTableAttributes.set(sortMode, SortMode.multi);

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("title", "numberOfKids-");

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("numberOfKids-", "title-");
    }

    public void testSortModeMultiRerenderAll() {
        dataTableAttributes.set(sortMode, SortMode.multi);

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("title", "numberOfKids-");

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("numberOfKids-", "title-");

        getMetamerPage().rerenderAll();
        verifySortingByColumns("numberOfKids-", "title-");
    }

    public void testSortModeMultiReverse() {
        dataTableAttributes.set(sortMode, SortMode.multi);

        sortByColumn(COLUMN_TITLE);
        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title-");

        sortByColumn(COLUMN_SEX);
        sortByColumn(COLUMN_SEX);
        verifySortingByColumns("title-", "sex-");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("title-", "sex-", "numberOfKids-");

        sortByColumn(COLUMN_NAME);
        sortByColumn(COLUMN_NAME);
        verifySortingByColumns("title-", "sex-", "numberOfKids-", "name-");
    }

    public void testSortModeSingle() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        sortByColumn(COLUMN_SEX);
        verifySortingByColumns("sex");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("numberOfKids");

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");
    }

    public void testSortModeSingleFullPageRefresh() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("numberOfKids");

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        getMetamerPage().fullPageRefresh();
        verifySortingByColumns("title");
    }

    public void testSortModeSingleRerenderAll() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_NAME);
        verifySortingByColumns("name");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("numberOfKids");

        getMetamerPage().rerenderAll();
        verifySortingByColumns("numberOfKids");
    }

    public void testSortModeSingleReverse() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_SEX);
        sortByColumn(COLUMN_SEX);
        verifySortingByColumns("sex-");

        sortByColumn(COLUMN_TITLE);
        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title-");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("numberOfKids-");

        sortByColumn(COLUMN_NAME);
        sortByColumn(COLUMN_NAME);
        verifySortingByColumns("name-");
    }

    public void verifyRow(int rowIndex, int modelIndex) {
        verifyRow(rowIndex, modelIndex, Collections.<Integer>emptySet());
    }

    public void verifyRow(int rowIndex, int modelIndex, Set<Integer> hiddenColumns) {
        Employee employee = sortedEmployees.get(modelIndex);
        FilteringRowInterface row = getTable().getRow(rowIndex);

        if (hiddenColumns.contains(0)) {
            assertNotVisible(row.getSexColumnElement(), "Column should not be visible.");
        } else {
            assertEquals(row.getSexColumnValue(), employee.getSex());
        }
        if (hiddenColumns.contains(1)) {
            assertNotVisible(row.getNameColumnElement(), "Column should not be visible.");
        } else {
            assertEquals(row.getNameColumnValue(), employee.getName());
        }
        if (hiddenColumns.contains(2)) {
            assertNotVisible(row.getTitleColumnElement(), "Column should not be visible.");
        } else {
            assertEquals(row.getTitleColumnValue(), employee.getTitle());
        }
        if (hiddenColumns.contains(3)) {
            assertNotVisible(row.getNumberOfKids1ColumnElement(), "Column should not be visible.");
        } else {
            assertEquals(row.getNumberOfKids1ColumnValue(), employee.getNumberOfKids());
        }
    }

    public void verifySortingByColumns(String... columns) {
        verifySortingByColumns(Collections.<Integer>emptySet(), columns);
    }

    public void verifySortingByColumns(Set<Integer> hiddenColumns, String... columns) {
        Comparator<Employee> employeeComparator = getPropertyComparator(Employee.class, columns);
        sortedEmployees = new ArrayList<Employee>(EMPLOYEES);
        Collections.sort(sortedEmployees, employeeComparator);

        if (dataScroller2.hasPages() && dataScroller2.getActivePageNumber() != 1) {
            jsUtils.scrollToView(dataScroller2.advanced().getButtonElement(FIRST));
            dataScroller2.switchTo(FIRST);
        }

        int pageRows = getTable().advanced().getNumberOfVisibleRows();

        for (Integer row : getListWithTestPages(pageRows)) {
            verifyRow(row, row, hiddenColumns);
        }

        jsUtils.scrollToView(dataScroller2.advanced().getButtonElement(LAST));
        dataScroller2.switchTo(LAST);

        pageRows = getTable().advanced().getNumberOfVisibleRows();

        for (Integer row : getListWithTestPages(pageRows)) {
            modelIndex = EMPLOYEES.size() - pageRows + row;
            verifyRow(row, modelIndex, hiddenColumns);
        }
    }
}
