/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static java.text.MessageFormat.format;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.testng.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTableFiltering extends AbstractCollapsibleSubTableTest {

    private static final Boolean BY_NAME = Boolean.TRUE;
    private static final Boolean BY_TITLE = Boolean.FALSE;
    private static final String sampleBuiltIn = "builtInFilteringAndSorting";

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithBuiltInFilteringHeader dataTableWithCSTWithBuiltInFilteringHeader;
    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithFilteringHeader dataTableWithCSTWithFilteringHeader;

    private final int rows = 7;

    private String sample;
    private final String[] samples = { sampleBuiltIn, "filtering" };

    @Override
    public String getComponentTestPagePath() {
        return format("richCollapsibleSubTable/{0}.xhtml", sample);
    }

    @Override
    public FilteringDT getDataTable() {
        return sample.equals(sampleBuiltIn) ? dataTableWithCSTWithBuiltInFilteringHeader : dataTableWithCSTWithFilteringHeader;
    }

    private List<Employee> getExpectedEmployees(final boolean byName, boolean isMaleTable) {
        final String nameFilter = getDataTable().getNameFilterText();
        final String titleFilter = getDataTable().getTitleFilterText();
        List<Employee> result = new LinkedList<Employee>(Collections2.filter(getEmployees(isMaleTable), new Predicate<Employee>() {
            @Override
            public boolean apply(Employee employee) {
                String filterValue = byName ? nameFilter : titleFilter;
                String employeeValue = byName ? employee.getName() : employee.getTitle();
                return byName ? employeeValue.toLowerCase().contains(filterValue.toLowerCase()) : employeeValue.equals(filterValue);
            }
        }));
        if (result.size() > rows) {
            result = result.subList(0, rows);
        }
        return result;
    }

    @BeforeMethod
    public void prepare() {
        attributes.set(CollapsibleSubTableAttributes.rows, rows);
    }

    @Test
    @UseWithField(field = "sample", valuesFrom = ValuesFrom.FROM_FIELD, value = "samples")
    @CoversAttributes("filterVar")
    @RegressionTest("https://issues.jboss.org/browse/RF-12673")
    public void testFilteringExpressionContainsIgnoreCase() {
        verifyFilteringInBothSubTables(BY_NAME, "Alexander");
        verifyFilteringInBothSubTables(BY_NAME, "aLEX");
    }

    @Test
    @UseWithField(field = "sample", valuesFrom = ValuesFrom.FROM_FIELD, value = "samples")
    @CoversAttributes("filterVar")
    @RegressionTest("https://issues.jboss.org/browse/RF-12673")
    public void testFilteringExpressionEquals() {
        verifyFilteringInBothSubTables(BY_TITLE, "Director");
        verifyFilteringInBothSubTables(BY_TITLE, "director");
        verifyFilteringInBothSubTables(BY_TITLE, "direct");
    }

    public void verifyFilteringInBothSubTables(boolean byName, String filter) {
        getDataTable().filterBy(byName, filter);
        verifyFilteringInSubTable(byName, Boolean.TRUE);
        verifyFilteringInSubTable(byName, Boolean.FALSE);
    }

    private void verifyFilteringInSubTable(boolean byName, boolean isMaleTable) {
        List<Employee> expectedEmployees = getExpectedEmployees(byName, isMaleTable);
        CollapsibleSubTableWithEmployees table = getSubTable(isMaleTable);

        assertEquals(table.advanced().getNumberOfVisibleRows(), expectedEmployees.size());
        for (int i = 0; i < expectedEmployees.size(); i++) {
            assertEquals(table.getRow(i).getName(), expectedEmployees.get(i).getName());
            assertEquals(table.getRow(i).getTitle(), expectedEmployees.get(i).getTitle());
        }
    }

    public static class DataTableWithCSTWithBuiltInFilteringHeader extends FilteringDT {

        @FindByJQuery(".rf-dt-flt input:eq(0)")
        private TextInputComponentImpl nameInput;
        @FindByJQuery(".rf-dt-flt:eq(1) input:eq(0)")
        private TextInputComponentImpl nameInput2;
        @FindByJQuery(value = ".rf-dt-flt input:eq(1)")
        private TextInputComponentImpl titleInput;
        @FindByJQuery(".rf-dt-flt:eq(1) input:eq(1)")
        private TextInputComponentImpl titleInput2;

        public void filterBy(boolean byName, String value) {
            // the built-in filtering input creates for each sub table, we have to fill in the values for both inputs
            (byName ? nameInput : titleInput).clear().sendKeys(value);
            makeBlur();
            (byName ? nameInput2 : titleInput2).clear().sendKeys(value);
            makeBlur();
        }

        public String getNameFilterText() {
            return nameInput.getStringValue();
        }

        public TextInputComponentImpl getNameInput() {
            return nameInput;
        }

        @Override
        public TextInputComponentImpl getNameInput2() {
            return nameInput2;
        }

        public String getTitleFilterText() {
            return titleInput.getStringValue();
        }

        public TextInputComponentImpl getTitleInput() {
            return titleInput;
        }

        @Override
        public TextInputComponentImpl getTitleInput2() {
            return titleInput2;
        }

    }

    public static class DataTableWithCSTWithFilteringHeader extends FilteringDT {

        @FindByJQuery(".rf-dt-hdr:eq(1) input:eq(0)")
        private TextInputComponentImpl nameInput;
        @FindByJQuery(".rf-dt-hdr:eq(1) input:eq(1)")
        private TextInputComponentImpl titleInput;

        public void filterBy(boolean byName, String value) {
            (byName ? nameInput : titleInput).clear().sendKeys(value);
            makeBlur();
        }

        public String getNameFilterText() {
            return nameInput.getStringValue();
        }

        public TextInputComponentImpl getNameInput() {
            return nameInput;
        }

        @Override
        public TextInputComponentImpl getNameInput2() {
            throw new UnsupportedOperationException("Not supported.");
        }

        public String getTitleFilterText() {
            return titleInput.getStringValue();
        }

        public TextInputComponentImpl getTitleInput() {
            return titleInput;
        }

        @Override
        public TextInputComponentImpl getTitleInput2() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    public abstract static class FilteringDT extends DataTableWithCST {

        @ArquillianResource
        private WebDriver driver;

        public abstract void filterBy(boolean byName, String value);

        public abstract String getNameFilterText();

        public abstract TextInputComponentImpl getNameInput();

        public abstract TextInputComponentImpl getNameInput2();

        public abstract String getTitleFilterText();

        public abstract TextInputComponentImpl getTitleInput();

        public abstract TextInputComponentImpl getTitleInput2();

        public void makeBlur() {
            guardAjax(driver.findElement(By.cssSelector("input[id$=metamerResponseDelayInput]"))).click();
        }
    }
}
