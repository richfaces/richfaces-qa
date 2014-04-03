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
import static org.testng.Assert.assertTrue;
import static org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton.LAST;

import java.util.Collection;
import java.util.List;
import org.richfaces.fragment.dataTable.RichFacesDataTableWithHeaderAndFooter;

import org.richfaces.model.Filter;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.FilteringTableHeaderInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.FilteringTableRowInterface;
import org.richfaces.tests.metamer.model.Employee;
import org.richfaces.tests.metamer.model.Employee.Sex;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableFilteringTest extends AbstractDataTableTest {

    private static final String[] FILTER_NAMES = new String[] { "ivan", "Гог", null, "Š" };
    private static final String[] FILTER_TITLES = new String[] { "Director", null, "CEO" };
    private static final Integer[] FILTER_NUMBER_OF_KIDS = new Integer[] { 2, 100, 0, 5 };

    private ExpectedEmployee filterEmployee;
    private List<Employee> expectedEmployees;
    private int rows;

    @BeforeMethod
    public void setup() {
        filterEmployee = new ExpectedEmployee();
    }

    public void testFilterSex(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        table.getHeader().filterSex(Sex.MALE);
        filterEmployee.sex = Sex.MALE;
        verifyFiltering(table);

        table.getHeader().filterSex(Sex.FEMALE);
        filterEmployee.sex = Sex.FEMALE;
        verifyFiltering(table);

        table.getHeader().filterSex(null);
        filterEmployee.sex = null;
        verifyFiltering(table);
    }

    public void testFilterName(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        for (String filterName : FILTER_NAMES) {
            table.getHeader().getFilterNameInput().clear();
            table.getHeader().filterName(filterName);
            filterEmployee.name = filterName;
            verifyFiltering(table);
        }
    }

    public void testFilterTitle(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        for (String filterTitle : FILTER_TITLES) {
            table.getHeader().getFilterTitleInput().clear();
            table.getHeader().filterTitle(filterTitle);
            filterEmployee.title = filterTitle;
            verifyFiltering(table);
        }
    }

    public void testFilterNumberOfKidsWithSpinner(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        for (Integer filterNumberOfKids : FILTER_NUMBER_OF_KIDS) {
            table.getHeader().filterNumberOfKidsWithSpinner(filterNumberOfKids);
            filterEmployee.numberOfKids1 = filterNumberOfKids;
            verifyFiltering(table);
        }
    }

    public void testFilterCombinations(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        table.getHeader().filterTitle("Technology");
        filterEmployee.title = "Technology";
        verifyFiltering(table);

        table.getHeader().filterNumberOfKidsWithSpinner(1);
        filterEmployee.numberOfKids1 = 1;
        verifyFiltering(table);

        table.getHeader().filterSex(Sex.MALE);
        filterEmployee.sex = Sex.MALE;
        verifyFiltering(table);

        table.getHeader().filterName("9");
        filterEmployee.name = "9";
        verifyFiltering(table);

        table.getHeader().filterNumberOfKidsWithSpinner(0);
        filterEmployee.numberOfKids1 = 0;
        verifyFiltering(table);

        table.getHeader().filterSex(Sex.FEMALE);
        filterEmployee.sex = Sex.FEMALE;
        verifyFiltering(table);
    }

    public void testRerenderAll(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        dataScroller2.switchTo(1);
        rows = table.advanced().getNumberOfRows();

        table.getHeader().filterName("an");
        filterEmployee.name = "an";

        expectedEmployees = filter(EMPLOYEES, getFilter());

        dataScroller2.switchTo(LAST);
        int lastPage = dataScroller2.getActivePageNumber();
        assertTrue(lastPage > 1);

        getMetamerPage().rerenderAll();
        assertEquals(dataScroller2.getActivePageNumber(), lastPage);
        assertTrue(dataScroller2.advanced().isLastPage());
        verifyPageContent(lastPage, table);

        dataScroller2.switchTo(1);
        verifyPageContent(1, table);
    }

    public void testFullPageRefresh(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        dataScroller2.switchTo(1);
        rows = table.advanced().getNumberOfRows();

        table.getHeader().filterName("an");
        filterEmployee.name = "an";

        expectedEmployees = filter(EMPLOYEES, getFilter());

        dataScroller2.switchTo(LAST);
        int lastPage = dataScroller2.getActivePageNumber();
        assertTrue(lastPage > 1);

        getMetamerPage().fullPageRefresh();
        assertEquals(dataScroller2.getActivePageNumber(), lastPage);
        assertTrue(dataScroller2.advanced().isLastPage());
        verifyPageContent(lastPage, table);

        dataScroller2.switchTo(1);
        verifyPageContent(1, table);
    }

    public void verifyFiltering(
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        expectedEmployees = filter(EMPLOYEES, getFilter());

        if(dataScroller2.advanced().getCountOfVisiblePages() > 1) {
            dataScroller2.switchTo(1);
        }
        rows = table.advanced().getNumberOfRows();
        verifyPageContent(1, table);

        if (dataScroller2.advanced().getCountOfVisiblePages() > 1) {
            int lastVisiblePageNumber = dataScroller2.advanced().getLastVisiblePageNumber();
            dataScroller2.switchTo(LAST);
            int lastPage = dataScroller2.getActivePageNumber();
            verifyPageContent(lastPage, table);

            if (lastVisiblePageNumber > 2) {
                dataScroller2.switchTo(3);
                verifyPageContent(3, table);
            }

            if (lastVisiblePageNumber > 3) {
                dataScroller2.switchTo(lastPage - 1);
                verifyPageContent(lastPage - 1, table);
            }
        }

    }

    public void verifyPageContent(int page, RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        if (expectedEmployees.size() == 0) {
            assertEquals(table.advanced().getNumberOfRows(), 0);
            assertTrue(table.advanced().isNoData());
        } else {
            for (int row = 0; row < table.advanced().getNumberOfRows(); row++) {
                int index = (page - 1) * rows + row;
                Employee expectedEmployee = expectedEmployees.get(index);
                verifyRow(expectedEmployee, row, table);
            }
        }
    }

    public void verifyRow(Employee expectedEmployee, int row,
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        verifySex(expectedEmployee.getSex(), row, table);
        FilteringTableRowInterface actualRow = table.getRow(row);
        assertEquals(actualRow.getNameColumnValue(), expectedEmployee.getName());
        assertEquals(actualRow.getTitleColumnValue(), expectedEmployee.getTitle());
        assertEquals(actualRow.getNumberOfKids1ColumnValue(), expectedEmployee.getNumberOfKids());
        assertEquals(actualRow.getNumberOfKids2ColumnValue(), expectedEmployee.getNumberOfKids());
    }

    public void verifySex(Employee.Sex expectedSex, int row,
            RichFacesDataTableWithHeaderAndFooter<? extends FilteringTableHeaderInterface,? extends FilteringTableRowInterface,?> table) {
        Employee.Sex actualSex = table.getRow(row).getSexColumnValue();
        assertEquals(actualSex, expectedSex);
    }

    private class ExpectedEmployee {
        Sex sex;
        String name;
        String title;
        Integer numberOfKids1;
    }

    private Filter<Employee> getFilter() {
        return new Filter<Employee>() {
            @Override
            public boolean accept(Employee employee) {
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
    }

    @SuppressWarnings("unchecked")
    private <E, T extends Collection<E>> T filter(T collection, Filter<E> filter) {
        T filteredCollection;
        try {
            filteredCollection = (T) collection.getClass().newInstance();

            for (E element : collection) {
                if (filter.accept(element)) {
                    filteredCollection.add(element);
                }
            }

            return (T) filteredCollection;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot construct new collection", e);
        }
    }
}
