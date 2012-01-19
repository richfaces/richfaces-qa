/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.model.DataTable;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22685 $
 */
public class TestCollapsibleSubTableFiltering extends AbstractCollapsibleSubTableTest {

    int rows = 0;
    
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/filtering.xhtml");
    }

    @BeforeMethod
    public void prepare() {
        collapsibleSubTabAttributes.set(CollapsibleSubTableAttributes.rows, rows);
        Column.resetAll();
    }

    @Test
    public void testFilteringExpressionContainsIgnoreCase() {
        filterBy(Column.NAME, "Alexander");
        verifyFiltering();

        filterBy(Column.NAME, "aLEX");
        verifyFiltering();
    }

    @Test
    public void testFilteringExpressionEquals() {
        filterBy(Column.TITLE, "Director");
        verifyFiltering();

        filterBy(Column.TITLE, "director");
        verifyFiltering();

        filterBy(Column.TITLE, "direct");
        verifyFiltering();
    }

    public void verifyFiltering() {
        filterEmployees();

        int length = (rows <= 0) ? employees.size() : Math.min(rows, employees.size());
        List<Employee> visibleEmployees = employees.subList(0, length);

        int rowCount = subtable.getRowCount();
        assertEquals(rowCount, visibleEmployees.size());

        for (int i = 0; i < visibleEmployees.size(); i++) {
            String name = selenium.getText(subtable.getCell(1, i + 1));
            String title = selenium.getText(subtable.getCell(2, i + 1));

            assertEquals(name, visibleEmployees.get(i).getName());
            assertEquals(title, visibleEmployees.get(i).getTitle());
        }
    }

    public void filterBy(Column column, String filter) {
        resetEmployees();
        column.filter(dataTable, filter);
    }

    public enum Column {
        NAME, TITLE, BIRTHDAY;

AjaxSelenium selenium = AjaxSeleniumContext.getProxy();
        String filter = "";

        public static void resetAll() {
            for (Column value : values()) {
                value.reset();
            }
        }

        public void reset() {
            filter = "";
        }

        public void filter(DataTable dataTable, String filter) {
            this.filter = filter;
            int headerIndex = this.ordinal() + 2; // 1 for index; 1 for additional header on top of links
            JQueryLocator input = dataTable.getHeader().get(headerIndex).getChild(jq("input:text"));
            guardXhr(selenium).type(input, filter);
        }

        public boolean apply(Employee employee) {
            switch (this) {
                case NAME:
                    boolean result = isBlank(filter) || employee.getName().toLowerCase().contains(filter.toLowerCase());
                    return result;
                case TITLE:
                    boolean result1 = isBlank(filter) || employee.getTitle().equals(filter);
                    return result1;
                case BIRTHDAY:
                    return true;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    public void filterEmployees() {
        employees = new LinkedList<Employee>(Collections2.filter(employees, new Predicate<Employee>() {
            @Override
            public boolean apply(Employee input) {
                for (Column column : Column.values()) {
                    if (!column.apply(input)) {
                        return false;
                    }
                }
                return true;
            }
        }));
    }
}
