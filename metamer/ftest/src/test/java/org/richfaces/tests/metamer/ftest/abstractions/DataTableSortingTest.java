/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableAttributes.sortMode;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.dataTableAttributes;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.richfaces.model.SortMode;
import org.richfaces.tests.metamer.model.Employee;
import org.richfaces.tests.metamer.model.Employee.Sex;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22685 $
 */
public abstract class DataTableSortingTest extends AbstractDataTableTest {

    int rowIndex;
    int modelIndex;
    List<Employee> sortedEmployees;

    public void testSortModeSingle() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        sortByColumn(COLUMN_SEX);
        verifySortingByColumns("sex");

        sortByColumn(COLUMN_NAME);
        verifySortingByColumns("name");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
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

    public void testSortModeSingleDoesntRememberOrder() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_NAME);
        sortByColumn(COLUMN_TITLE);
        sortByColumn(COLUMN_NAME);
        verifySortingByColumns("name");
    }

    public void testSortModeSingleRerenderAll() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_NAME);
        verifySortingByColumns("name");

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("numberOfKids");

        rerenderAll();
        verifySortingByColumns("numberOfKids");
    }

    public void testSortModeSingleFullPageRefresh() {
        dataTableAttributes.set(sortMode, SortMode.single);

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns("numberOfKids");

        sortByColumn(COLUMN_TITLE);
        verifySortingByColumns("title");

        fullPageRefresh();
        verifySortingByColumns("title");
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

        rerenderAll();
        verifySortingByColumns("numberOfKids-", "title-");
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

        fullPageRefresh();
        verifySortingByColumns("numberOfKids-", "title-");
    }

    public void sortByColumn(int column) {
        guardXhr(selenium).click(model.getColumnHeader(column).getDescendant(jq("a")));
    }

    public void verifySortingByColumns(String... columns) {
        Comparator<Employee> employeeComparator = getPropertyComparator(Employee.class, columns);
        sortedEmployees = new ArrayList<Employee>(EMPLOYEES);
        Collections.sort(sortedEmployees, employeeComparator);
        
        dataScroller2.gotoFirstPage();
        
        int firstPageRows = model.getRows();

        for (rowIndex = 0; rowIndex < model.getRows(); rowIndex++) {
            modelIndex = rowIndex;
            verifyRow(rowIndex, modelIndex);
        }

        dataScroller2.gotoPage(2);

        for (rowIndex = 0; rowIndex < model.getRows(); rowIndex++) {
            modelIndex = firstPageRows + rowIndex;
            verifyRow(rowIndex, modelIndex);
        }

        dataScroller2.gotoLastPage();

        for (rowIndex = 0; rowIndex < model.getRows(); rowIndex++) {
            modelIndex = EMPLOYEES.size() - model.getRows() + rowIndex;
            verifyRow(rowIndex, modelIndex);
        }
    }

    public void verifyRow(int rowIndex, int modelIndex) {
        Employee employee = sortedEmployees.get(modelIndex);

        Sex sex = employees.getSex(rowIndex + 1);
        String name = employees.getName(rowIndex + 1);
        String title = employees.getTitle(rowIndex + 1);
        int numberOfKids = employees.getNumberOfKids(rowIndex + 1);

        String message = format(
            "model: {0}; row: {1}; employee: {2}; found: sex '{3}', name '{4}', title '{5}', numberOfKids '{6}'",
            modelIndex, rowIndex, employee, sex, name, title, numberOfKids);

        assertEquals(sex, employee.getSex(), message);
        assertEquals(name, employee.getName(), message);
        assertEquals(title, employee.getTitle(), message);
        assertEquals(numberOfKids, employee.getNumberOfKids(), message);
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
}
