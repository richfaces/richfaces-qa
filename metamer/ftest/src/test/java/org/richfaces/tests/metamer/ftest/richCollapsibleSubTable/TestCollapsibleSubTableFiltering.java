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
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
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

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithFilteringHeader dataTable;
    private final int rows = 7;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/filtering.xhtml");
    }

    @BeforeMethod
    public void prepare() {
        attributes.set(CollapsibleSubTableAttributes.rows, rows);
    }

    @Test
    @Templates(exclude = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testFilteringExpressionContainsIgnoreCase() {
        verifyFiltering(true, "Alexander");
        verifyFiltering(true, "aLEX");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12673")
    @Templates(value = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testFilteringExpressionContainsIgnoreCaseInSwitchablePanels() {
        testFilteringExpressionContainsIgnoreCase();
    }

    @Test
    @Templates(exclude = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testFilteringExpressionEquals() {
        verifyFiltering(false, "Director");
        verifyFiltering(false, "director");
        verifyFiltering(false, "direct");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12673")
    @Templates(value = { "richAccordion", "richCollapsiblePanel", "richTabPanel", "richTogglePanel" })
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testFilteringExpressionEqualsInSwitchablePanels() {
        testFilteringExpressionEquals();
    }

    public void verifyFiltering(boolean byName, String filter) {
        getDataTable().filterBy(byName, filter);
        List<Employee> visibleEmployees = filterCurrentEmployees(byName);
        if (visibleEmployees.size() > rows) {
            visibleEmployees = visibleEmployees.subList(0, rows);
        }
        CollapsibleSubTableWithEmployees table = getSubTable(isMale);

        assertEquals(table.advanced().getNumberOfVisibleRows(), visibleEmployees.size());
        for (int i = 0; i < visibleEmployees.size(); i++) {
            assertEquals(table.getRow(i).getName(), visibleEmployees.get(i).getName());
            assertEquals(table.getRow(i).getTitle(), visibleEmployees.get(i).getTitle());
        }
    }

    private List<Employee> filterCurrentEmployees(final boolean byName) {
        final String nameFilter = getDataTable().getNameFilterText();
        final String titleFilter = getDataTable().getTitleFilterText();
        return new LinkedList<Employee>(Collections2.filter(getEmployees(isMale), new Predicate<Employee>() {
            @Override
            public boolean apply(Employee employee) {
                String filterValue = byName ? nameFilter : titleFilter;
                String employeeValue = byName ? employee.getName() : employee.getTitle();
                return byName ? employeeValue.toLowerCase().contains(filterValue.toLowerCase()) : employeeValue.equals(filterValue);
            }
        }));
    }

    @Override
    public DataTableWithCSTWithFilteringHeader getDataTable() {
        return dataTable;
    }

    public static class DataTableWithCSTWithFilteringHeader extends DataTableWithCST {

        @FindByJQuery(".rf-dt-hdr:eq(1) input:eq(0)")
        private TextInputComponentImpl nameInput;
        @FindByJQuery(".rf-dt-hdr:eq(1) input:eq(1)")
        private TextInputComponentImpl titleInput;

        public void filterBy(boolean byName, String value) {
            Graphene.guardAjax((byName ? nameInput : titleInput).clear().sendKeys(value).advanced()).trigger("change");
        }

        public String getNameFilterText() {
            return nameInput.getStringValue();
        }

        public String getTitleFilterText() {
            return titleInput.getStringValue();
        }
    }
}
