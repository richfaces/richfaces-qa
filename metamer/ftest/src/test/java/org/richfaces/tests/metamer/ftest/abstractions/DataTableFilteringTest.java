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

import static org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton.LAST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.richfaces.fragment.dataTable.AbstractTable;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.FilteringHeaderInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.FilteringRowInterface;
import org.richfaces.tests.metamer.model.Employee;
import org.richfaces.tests.metamer.model.Employee.Sex;
import org.testng.annotations.BeforeMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableFilteringTest extends AbstractDataTableTest {

    private static final String[] FILTER_NAMES = new String[] { "ivan", "Гог", null, "Š" };
    private static final Integer[] FILTER_NUMBER_OF_KIDS = new Integer[] { 2, 100, 0, 5 };
    private static final String[] FILTER_TITLES = new String[] { "Director", null, "CEO" };
    private static final int MAX_VISIBLE_ROWS = 5;

    private List<Employee> expectedEmployees;
    private ExpectedEmployee filterEmployee;
    private int numberOfVisibleRows;
    final Predicate<Employee> predicateFilter = new Predicate<Employee>() {
        @Override
        public boolean apply(Employee employee) {
            boolean result = true;
            if (filterEmployee.sex != null) {
                result &= employee.getSex() == filterEmployee.sex;
            }
            if (filterEmployee.name != null) {
                result &= employee.getName().toLowerCase().contains(filterEmployee.name.toLowerCase());
            }
            if (filterEmployee.title != null) {
                result &= employee.getTitle().equals(filterEmployee.title);
            }
            if (filterEmployee.numberOfKids1 != null) {
                result &= employee.getNumberOfKids() >= filterEmployee.numberOfKids1;
            }
            return result;
        }
    };

    @SuppressWarnings("unchecked")
    private <E, T extends Collection<E>> T filter(T collection, Predicate<E> filter) {
        return (T) Lists.newArrayList(Iterables.filter(collection, filter));
    }

    private Predicate<Employee> getFilter() {
        return predicateFilter;
    }

    protected abstract AbstractTable<? extends FilteringHeaderInterface, ? extends FilteringRowInterface, ?> getTable();

    @BeforeMethod
    public void setup() {
        filterEmployee = new ExpectedEmployee();
        getUnsafeAttributes("").set("rows", MAX_VISIBLE_ROWS);// speedup testing
    }

    public void testFilterCombinations(boolean isBuiltIn) {
        getTable().getHeader().filterTitle("Technology", isBuiltIn);

        filterEmployee.title = "Technology";
        verifyFiltering();

        getTable().getHeader().filterName("9", isBuiltIn);
        filterEmployee.name = "9";
        verifyFiltering();

        if (isBuiltIn) {
            getTable().getHeader().filterNumberOfKidsBuiltIn(0);
            filterEmployee.numberOfKids1 = 0;
            verifyFiltering();

            getTable().getHeader().filterNumberOfKidsBuiltIn(1);
            filterEmployee.numberOfKids1 = 1;
            verifyFiltering();
        } else {
            getTable().getHeader().filterNumberOfKidsWithSpinner(1);
            filterEmployee.numberOfKids1 = 1;
            verifyFiltering();

            getTable().getHeader().filterSex(Sex.MALE);
            filterEmployee.sex = Sex.MALE;
            verifyFiltering();

            getTable().getHeader().filterNumberOfKidsWithSpinner(0);
            filterEmployee.numberOfKids1 = 0;
            verifyFiltering();

            getTable().getHeader().filterSex(Sex.FEMALE);
            filterEmployee.sex = Sex.FEMALE;
            verifyFiltering();
        }
    }

    public void testFilterName(boolean isBuiltIn) {
        for (String filterName : FILTER_NAMES) {
            getTable().getHeader().filterName(filterName, isBuiltIn);
            filterEmployee.name = filterName;
            verifyFiltering();
        }
    }

    public void testFilterNumberOfKidsWithSpinner() {
        for (Integer filterNumberOfKids : FILTER_NUMBER_OF_KIDS) {
            getTable().getHeader().filterNumberOfKidsWithSpinner(filterNumberOfKids);
            filterEmployee.numberOfKids1 = filterNumberOfKids;
            verifyFiltering();
        }
    }

    public void testFilterNumberOfKindsBuiltIn() {
        for (Integer filterNumberOfKids : FILTER_NUMBER_OF_KIDS) {
            getTable().getHeader().filterNumberOfKidsBuiltIn(filterNumberOfKids);
            filterEmployee.numberOfKids1 = filterNumberOfKids;
            verifyFiltering();
        }
    }

    public void testFilterSex() {
        getTable().getHeader().filterSex(Sex.MALE);
        filterEmployee.sex = Sex.MALE;
        verifyFiltering();

        getTable().getHeader().filterSex(Sex.FEMALE);
        filterEmployee.sex = Sex.FEMALE;
        verifyFiltering();

        getTable().getHeader().filterSex(null);
        filterEmployee.sex = null;
        verifyFiltering();
    }

    public void testFilterTitle(boolean isBuiltIn) {
        for (String filterTitle : FILTER_TITLES) {
            getTable().getHeader().filterTitle(filterTitle, isBuiltIn);
            filterEmployee.title = filterTitle;
            verifyFiltering();
        }
    }

    public void testFullPageRefresh(boolean isBuiltIn) {
        dataScroller2.switchTo(1);

        getTable().getHeader().filterName("an", isBuiltIn);
        filterEmployee.name = "an";

        expectedEmployees = filter(EMPLOYEES, getFilter());

        dataScroller2.switchTo(LAST);
        int lastPage = dataScroller2.getActivePageNumber();
        assertTrue(lastPage > 1);

        getMetamerPage().fullPageRefresh();
        assertEquals(dataScroller2.getActivePageNumber(), lastPage);
        assertTrue(dataScroller2.advanced().isLastPage());
        verifyPageContent(lastPage);

        dataScroller2.switchTo(1);
        verifyPageContent(1);
    }

    public void testRerenderAll(boolean isBuiltIn) {
        dataScroller2.switchTo(1);

        getTable().getHeader().filterName("an", isBuiltIn);
        filterEmployee.name = "an";

        expectedEmployees = filter(EMPLOYEES, getFilter());

        dataScroller2.switchTo(LAST);
        int lastPage = dataScroller2.getActivePageNumber();
        assertTrue(lastPage > 1);

        getMetamerPage().rerenderAll();
        assertEquals(dataScroller2.getActivePageNumber(), lastPage);
        assertTrue(dataScroller2.advanced().isLastPage());
        verifyPageContent(lastPage);

        dataScroller2.switchTo(1);
        verifyPageContent(1);
    }

    public void verifyFiltering() {
        // prepare expected employees
        expectedEmployees = filter(EMPLOYEES, getFilter());
        verifyPageContent(1); // verify first page

        if (dataScroller2.advanced().getCountOfVisiblePages() > 1) {
            dataScroller2.switchTo(LAST);
            int lastPage = dataScroller2.getActivePageNumber();
            verifyPageContent(lastPage); // verify last page
            if (lastPage > 2) {
                verifyPageContent(lastPage - 1);// verify a page before last page
            }
            if (lastPage > 3) {
                verifyPageContent(lastPage / 2);// verify some page in the middle
            }
        }
    }

    public void verifyPageContent(int page) {
        if (dataScroller2.advanced().getCountOfVisiblePages() > 0) {
            dataScroller2.switchTo(page);
        }
        numberOfVisibleRows = getTable().advanced().getNumberOfVisibleRows();
        if (expectedEmployees.isEmpty()) {
            Graphene.waitModel().until(new Predicate<WebDriver>() {
                private Throwable lastException;

                @Override
                public boolean apply(WebDriver t) {
                    try {
                        assertEquals(getTable().advanced().getNumberOfVisibleRows(), 0);
                        assertTrue(getTable().advanced().isNoData());
                    } catch (Throwable exc) {
                        lastException = exc;
                        return false;
                    }
                    return true;
                }

                @Override
                public String toString() {
                    return MessageFormat.format("table to be empty. Last caught exception: {0}", lastException);
                }
            });
        } else {
            // check all visible rows
            for (int rowNumber = 0; rowNumber < numberOfVisibleRows; rowNumber++) {
                verifyRow(expectedEmployees.get((page - 1) * MAX_VISIBLE_ROWS + rowNumber), rowNumber);
            }
        }
    }

    protected void verifyRow(final Employee expectedEmployee, final int rowNumber) {
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            private Throwable lastException;

            @Override
            public boolean apply(WebDriver t) {
                FilteringRowInterface actualRow = getTable().getRow(rowNumber);
                try {
                    assertEquals(actualRow.getSexColumnValue(), expectedEmployee.getSex());
                    assertEquals(actualRow.getNameColumnValue(), expectedEmployee.getName());
                    assertEquals(actualRow.getTitleColumnValue(), expectedEmployee.getTitle());
                    assertEquals(actualRow.getNumberOfKids1ColumnValue(), expectedEmployee.getNumberOfKids());
                    assertEquals(actualRow.getNumberOfKids2ColumnValue(), expectedEmployee.getNumberOfKids());
                } catch (Throwable exc) {
                    lastException = exc;
                    return false;
                }
                return true;
            }

            @Override
            public String toString() {
                return MessageFormat.format("row to be same as expected. Last caught exception: {0}", lastException);
            }
        });
    }

    private class ExpectedEmployee {

        Sex sex;
        String name;
        String title;
        Integer numberOfKids1;
    }
}
