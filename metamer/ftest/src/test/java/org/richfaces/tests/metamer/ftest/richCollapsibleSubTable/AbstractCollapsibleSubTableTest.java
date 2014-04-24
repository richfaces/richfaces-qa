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

import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.ExpandMode.client;
import static org.testng.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsibleSubTable.RichFacesCollapsibleSubTable;
import org.richfaces.fragment.common.CheckboxInputComponentImpl;
import org.richfaces.fragment.common.NullFragment;
import org.richfaces.fragment.dataTable.RichFacesDataTable;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.model.Employee;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractCollapsibleSubTableTest extends AbstractWebDriverTest {

    private static final List<Employee> EMPLOYEES = Model.unmarshallEmployees();
    private static final List<Employee> employeesFemale = new LinkedList<Employee>(Collections2.filter(EMPLOYEES, new Predicate<Employee>() {
        @Override
        public boolean apply(Employee input) {
            return input.getSex().equals(Employee.Sex.FEMALE);
        }
    }));
    private static final List<Employee> employeesMale = new LinkedList<Employee>(Collections2.filter(EMPLOYEES, new Predicate<Employee>() {
        @Override
        public boolean apply(Employee input) {
            return input.getSex().equals(Employee.Sex.MALE);
        }
    }));

    protected final Attributes<CollapsibleSubTableAttributes> attributes = getAttributes();

    @FindBy(className = "rf-dt")
    private DataTableWithCST dataTable;

    @FindBy(css = "input[id$=noDataCheckbox]")
    private CheckboxInputComponentImpl showDataElement;

    protected Boolean isMale = Boolean.TRUE;

    protected ExpandMode expandMode = ExpandMode.client;

    public <T extends RichFacesDataTable<?,? extends CollapsibleSubTableWithEmployees,?>> DataTableWithCST getDataTable() {
        return dataTable;
    }

    protected <T extends CollapsibleSubTableWithEmployees> T getSubTable(boolean isMale) {
        return (T) getDataTable().getRow(isMale ? 0 : 1);
    }

    protected List<Employee> getFemaleEmployees() {
        return new LinkedList(employeesFemale);
    }

    protected List<Employee> getMaleEmployees() {
        return new LinkedList(employeesMale);
    }

    protected List<Employee> getEmployees(boolean isMale) {
        return isMale ? getMaleEmployees() : getFemaleEmployees();
    }

    public void showDataInTable(final boolean showData) {
        if (showData) {
            Graphene.guardAjax(showDataElement).check();
        } else {
            Graphene.guardAjax(showDataElement).uncheck();
        }
    }

    protected void toggleSubTable(boolean expand, ExpandMode mode, CollapsibleSubTableWithEmployees subtable) {
        switch (mode) {
            case client:
                if (expand) {
                    Graphene.guardNoRequest(subtable).expand();
                } else {
                    Graphene.guardNoRequest(subtable).collapse();
                }
                break;
            case none:
                try {
                    if (expand) {
                        Graphene.guardNoRequest(subtable).expand();
                    } else {
                        Graphene.guardNoRequest(subtable).collapse();
                    }
                    fail("The table was collapsed/expanded, but the @expandMode was none");
                } catch (TimeoutException ex) {
                }
                break;
            case ajax:
                if (expand) {
                    Graphene.guardAjax(subtable).expand();
                } else {
                    Graphene.guardAjax(subtable).collapse();
                }
                break;
            case server:
                if (expand) {
                    Graphene.guardHttp(subtable).expand();
                } else {
                    Graphene.guardHttp(subtable).collapse();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown expandMode " + expandMode);
        }
    }

    public static class DataTableWithCST extends RichFacesDataTable<NullFragment, CollapsibleSubTableWithEmployees, NullFragment> {

    }

    public static class CollapsibleSubTableWithEmployees extends RichFacesCollapsibleSubTable<NullFragment, EmployeeRecord, NullFragment> {
    }

    public static class EmployeeRecord {

        @Root
        private WebElement rootElement;

        @FindByJQuery(".rf-cst-c:eq(0)")
        private WebElement nameElement;
        @FindByJQuery(".rf-cst-c:eq(1)")
        private WebElement titleElement;
        @FindByJQuery(".rf-cst-c:eq(2)")
        private WebElement birthdateElement;

        public String getBirthdate() {
            return getBirthdateElement().getText();
        }

        public WebElement getBirthdateElement() {
            return birthdateElement;
        }

        public String getName() {
            return getNameElement().getText();
        }

        public WebElement getNameElement() {
            return nameElement;
        }

        public WebElement getRootElement() {
            return rootElement;
        }

        public String getTitle() {
            return getTitleElement().getText();
        }

        public WebElement getTitleElement() {
            return titleElement;
        }

        @Override
        public String toString() {
            return "Employee{" + getName() + '|' + getTitle() + '|' + getBirthdate() + '}';
        }
    }
}
