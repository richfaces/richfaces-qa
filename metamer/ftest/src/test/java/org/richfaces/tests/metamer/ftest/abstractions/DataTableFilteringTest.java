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

import java.util.Collection;
import java.util.List;

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
    private static final String[] FILTER_TITLES = new String[] { "Director", null, "CEO" };
    private static final Integer[] FILTER_NUMBER_OF_KIDS = new Integer[] { 2, 100, 0, 5 };
    private static final int MAX_VISIBLE_ROWS = 5;

    private ExpectedEmployee filterEmployee;
    private List<Employee> expectedEmployees;
    private int numberOfVisibleRows;

    final Predicate<Employee> filter = new Predicate<Employee>() {
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

    @BeforeMethod
    public void setup() {
        filterEmployee = new ExpectedEmployee();
        getUnsafeAttributes("").set("rows", MAX_VISIBLE_ROWS);// speedup testing
    }

    protected abstract AbstractTable<? extends FilteringHeaderInterface, ? extends FilteringRowInterface, ?> getTable();

    public void testFilterSex() {
        getTable().getHeader().filterSex(Sex.MALE);
        waiting(500);
        filterEmployee.sex = Sex.MALE;
        verifyFiltering();

        getTable().getHeader().filterSex(Sex.FEMALE);
        waiting(500);
        filterEmployee.sex = Sex.FEMALE;
        verifyFiltering();

        getTable().getHeader().filterSex(null);
        waiting(500);
        filterEmployee.sex = null;
        verifyFiltering();
    }

    public void testFilterName(boolean isBuiltIn) {
        for (String filterName : FILTER_NAMES) {
            if (isBuiltIn) {
                getTable().getHeader().getFilterNameBuiltInInput().clear();
            } else {
                getTable().getHeader().getFilterNameInput().clear();
            }
            waiting(800);
            getTable().getHeader().filterName(filterName, isBuiltIn);
            waiting(800);
            filterEmployee.name = filterName;
            verifyFiltering();
        }
    }

    public void testFilterTitle(boolean isBuiltIn) {
        for (String filterTitle : FILTER_TITLES) {
            if (isBuiltIn) {
                getTable().getHeader().getFilterTitleBuiltInInput().clear();
            } else {
                getTable().getHeader().getFilterTitleInput().clear();
            }
            // Wait for complete Ajax request and display data
            waiting(500);
            getTable().getHeader().filterTitle(filterTitle, isBuiltIn);
            // Wait for complete Ajax request and display data
            waiting(500);
            filterEmployee.title = filterTitle;
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

    public void testFilterCombinations(Boolean isBuiltIn) {
        getTable().getHeader().filterTitle("Technology", isBuiltIn);
        filterEmployee.title = "Technology";
        verifyFiltering();

        getTable().getHeader().filterName("9", isBuiltIn);
        filterEmployee.name = "9";
        verifyFiltering();

        if (!isBuiltIn) {
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
        } else {
            getTable().getHeader().filterNumberOfKidsBuiltIn(0);
            filterEmployee.numberOfKids1 = 0;
            verifyFiltering();

            getTable().getHeader().filterNumberOfKidsBuiltIn(1);
            filterEmployee.numberOfKids1 = 1;
            verifyFiltering();
        }
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

    public void testFilterNumberOfKindsBuiltIn() {
        for (Integer filterNumberOfKids : FILTER_NUMBER_OF_KIDS) {
            getTable().getHeader().filterNumberOfKidsBuiltIn(filterNumberOfKids);
            filterEmployee.numberOfKids1 = filterNumberOfKids;
            verifyFiltering();
        }
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
            assertEquals(getTable().advanced().getNumberOfVisibleRows(), 0);
            assertTrue(getTable().advanced().isNoData());
        } else {
            // check all visible rows
            for (int rowNumber = 0; rowNumber < numberOfVisibleRows; rowNumber++) {
                verifyRow(expectedEmployees.get((page - 1) * MAX_VISIBLE_ROWS + rowNumber), rowNumber);
            }
        }
    }

    public void verifyRow(Employee expectedEmployee, int rowNumber) {
        FilteringRowInterface actualRow = getTable().getRow(rowNumber);
        assertEquals(actualRow.getSexColumnValue(), expectedEmployee.getSex());
        assertEquals(actualRow.getNameColumnValue(), expectedEmployee.getName());
        assertEquals(actualRow.getTitleColumnValue(), expectedEmployee.getTitle());
        assertEquals(actualRow.getNumberOfKids1ColumnValue(), expectedEmployee.getNumberOfKids());
        assertEquals(actualRow.getNumberOfKids2ColumnValue(), expectedEmployee.getNumberOfKids());
    }

    private Predicate<Employee> getFilter() {
        return filter;
    }

    private class ExpectedEmployee {

        Sex sex;
        String name;
        String title;
        Integer numberOfKids1;
    }

    @SuppressWarnings("unchecked")
    private <E, T extends Collection<E>> T filter(T collection, Predicate<E> filter) {
        return (T) Lists.newArrayList(Iterables.filter(collection, filter));
    }
}
