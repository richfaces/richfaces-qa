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

import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionValue;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionValueLocator;
import org.richfaces.model.Filter;
import org.richfaces.tests.metamer.model.Employee;
import org.richfaces.tests.metamer.model.Employee.Sex;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22875 $
 */
public abstract class DataTableFilteringTest extends AbstractDataTableTest {

    private static final String[] FILTER_NAMES = new String[] { "ivan", "Гог", null, "Š" };
    private static final String[] FILTER_TITLES = new String[] { "Director", null, "CEO" };
    private static final Integer[] FILTER_NUMBER_OF_KIDS = new Integer[] { 2, 100, 0, 5 };

    JQueryLocator selectSex = jq("select");
    JQueryLocator inputName = jq("input");
    JQueryLocator inputTitle = jq("input");
    JQueryLocator inputNumberOfKids1 = jq("input");
    JQueryLocator inputNumberOfKids2 = jq("input");

    FilteringDataTable filtering = new FilteringDataTable();

    private ExpectedEmployee filterEmployee;
    private List<Employee> expectedEmployees;
    private int rows;

    @BeforeMethod
    public void setup() {
        filterEmployee = new ExpectedEmployee();
    }

    public void testFilterSex() {
        filtering.selectSex(Sex.MALE);
        filterEmployee.sex = Sex.MALE;
        verifyFiltering();

        filtering.selectSex(Sex.FEMALE);
        filterEmployee.sex = Sex.FEMALE;
        verifyFiltering();

        filtering.selectSex(null);
        filterEmployee.sex = null;
        verifyFiltering();
    }

    public void testFilterName() {
        for (String filterName : FILTER_NAMES) {
            filtering.selectName(filterName);
            filterEmployee.name = filterName;
            verifyFiltering();
        }
    }

    public void testFilterTitle() {
        for (String filterTitle : FILTER_TITLES) {
            filtering.selectTitle(filterTitle);
            filterEmployee.title = filterTitle;
            verifyFiltering();
        }
    }

    public void testFilterNumberOfKids1() {
        for (Integer filterNumberOfKids : FILTER_NUMBER_OF_KIDS) {
            filtering.selectNumberOfKids1(filterNumberOfKids);
            filterEmployee.numberOfKids1 = filterNumberOfKids;
            verifyFiltering();
        }
    }

    public void testFilterCombinations() {
        filtering.selectTitle("Technology");
        filterEmployee.title = "Technology";
        verifyFiltering();

        filtering.selectNumberOfKids1(1);
        filterEmployee.numberOfKids1 = 1;
        verifyFiltering();

        filtering.selectSex(Sex.MALE);
        filterEmployee.sex = Sex.MALE;
        verifyFiltering();

        filtering.selectName("9");
        filterEmployee.name = "9";
        verifyFiltering();

        filtering.selectNumberOfKids1(0);
        filterEmployee.numberOfKids1 = 0;
        verifyFiltering();

        filtering.selectSex(Sex.FEMALE);
        filterEmployee.sex = Sex.FEMALE;
        verifyFiltering();
    }

    public void testRerenderAll() {
        dataScroller2.setLastPage(dataScroller2.obtainLastPage());
        dataScroller2.gotoFirstPage();
        rows = model.getRows();

        filtering.selectName("an");
        filterEmployee.name = "an";

        expectedEmployees = filter(EMPLOYEES, getFilter());

        dataScroller2.gotoLastPage();
        int lastPage = dataScroller2.getCurrentPage();
        assertTrue(lastPage > 1);

        rerenderAll();
        assertEquals(dataScroller2.getCurrentPage(), lastPage);
        assertTrue(dataScroller2.isLastPage());
        verifyPageContent(lastPage);

        dataScroller2.gotoFirstPage();
        verifyPageContent(1);
    }

    public void testFullPageRefresh() {
        dataScroller2.setLastPage(dataScroller2.obtainLastPage());
        dataScroller2.gotoFirstPage();
        rows = model.getRows();

        filtering.selectName("an");
        filterEmployee.name = "an";

        expectedEmployees = filter(EMPLOYEES, getFilter());

        dataScroller2.gotoLastPage();
        int lastPage = dataScroller2.getCurrentPage();
        assertTrue(lastPage > 1);

        fullPageRefresh();
        assertEquals(dataScroller2.getCurrentPage(), lastPage);
        assertTrue(dataScroller2.isLastPage());
        verifyPageContent(lastPage);

        dataScroller2.gotoFirstPage();
        verifyPageContent(1);
    }

    public void verifyFiltering() {
        expectedEmployees = filter(EMPLOYEES, getFilter());

        dataScroller2.setLastPage(dataScroller2.obtainLastPage());

        dataScroller2.gotoFirstPage();
        rows = model.getRows();
        verifyPageContent(1);

        if (dataScroller2.getLastPage() > 1) {
            dataScroller2.gotoLastPage();
            int lastPage = dataScroller2.getCurrentPage();
            verifyPageContent(lastPage);

            if (dataScroller2.getLastPage() > 2) {
                dataScroller2.gotoPage(2);
                verifyPageContent(2);
            }

            if (dataScroller2.getLastPage() > 3) {
                dataScroller2.gotoPage(lastPage - 1);
                verifyPageContent(lastPage - 1);
            }
        }

    }

    public void verifyPageContent(int page) {
        if (expectedEmployees.size() == 0) {
            assertEquals(model.getRows(), 0);
            assertTrue(model.isNoData());
        } else {
            for (int row = 0; row < model.getRows(); row++) {
                int index = (page - 1) * rows + row;
                Employee expectedEmployee = expectedEmployees.get(index);
                filtering.verifyRow(expectedEmployee, row + 1);
            }
        }
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

    private class FilteringDataTable {
        public void verifyRow(Employee expectedEmployee, int row) {
            verifySex(expectedEmployee.getSex(), row);
            verifyElement(COLUMN_NAME, row, expectedEmployee.getName());
            verifyElement(COLUMN_TITLE, row, expectedEmployee.getTitle());
            verifyElement(COLUMN_NUMBER_OF_KIDS1, row, expectedEmployee.getNumberOfKids());
            verifyElement(COLUMN_NUMBER_OF_KIDS2, row, expectedEmployee.getNumberOfKids());
        }

        public void selectSex(Sex sex) {
            JQueryLocator select = model.getColumnHeader(COLUMN_SEX).getDescendant(selectSex);
            OptionValueLocator option = sex == null ? optionValue("ALL") : optionValue(sex.toString());
            guardXhr(selenium).select(select, option);
        }

        public void verifySex(Sex expectedSex, int row) {
            JQueryLocator rowLocator = model.getElement(COLUMN_SEX, row);
            AttributeLocator<?> sexLocator = rowLocator.getDescendant(jq("img")).getAttribute(Attribute.ALT);
            String sexString = selenium.getAttribute(sexLocator);
            Sex actualSex = Sex.valueOf(sexString);
            assertEquals(actualSex, expectedSex);
        }

        public void selectName(String name) {
            JQueryLocator input = model.getColumnHeader(COLUMN_NAME).getDescendant(inputName);
            guardXhr(selenium).type(input, name == null ? "" : name);
        }

        public void selectTitle(String title) {
            JQueryLocator input = model.getColumnHeader(COLUMN_TITLE).getDescendant(inputTitle);
            guardXhr(selenium).type(input, title == null ? "" : title);
        }

        public void selectNumberOfKids1(int numberOfKids) {
            JQueryLocator input = model.getColumnHeader(COLUMN_NUMBER_OF_KIDS1).getDescendant(inputNumberOfKids1);
            guardXhr(selenium).type(input, Integer.toString(numberOfKids));
        }

        public void verifyElement(int column, int row, Object expectedValue) {
            JQueryLocator locator = model.getElement(column, row);
            String text = selenium.getText(locator);
            assertEquals(text, expectedValue.toString());
        }

    }
}
