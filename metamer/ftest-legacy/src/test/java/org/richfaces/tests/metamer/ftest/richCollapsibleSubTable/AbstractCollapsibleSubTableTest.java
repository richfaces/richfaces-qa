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

import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.test.selenium.GuardRequest;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;
import org.richfaces.tests.metamer.ftest.model.CollapsibleSubTable;
import org.richfaces.tests.metamer.ftest.model.CollapsibleSubTableToggler;
import org.richfaces.tests.metamer.ftest.model.DataTable;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.BeforeMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22688 $
 */
public abstract class AbstractCollapsibleSubTableTest extends AbstractGrapheneTest {

    private static final List<Employee> EMPLOYEES = Model.unmarshallEmployees();

    Attributes<CollapsibleSubTableAttributes> collapsibleSubTabAttributes = new Attributes<CollapsibleSubTableAttributes>();
    DataTable dataTable = new DataTable(pjq("table[id$=richDataTable]"));;

    JQueryLocator showDataLocator = pjq("input[id$=noDataCheckbox]");

    @Inject
    @Use(value = "configuration*")
    Configuration configuration;
    Configuration configurationMen = new Configuration(1, "Men");
    Configuration configurationWomen = new Configuration(2, "Women");

    CollapsibleSubTable subtable;
    CollapsibleSubTableToggler toggler;
    List<Employee> employees;

    Configuration secondConfiguration;
    CollapsibleSubTable secondSubtable;
    CollapsibleSubTableToggler secondToggler;

    @Inject
    @Use(empty = true)
    ExpandMode expandMode;

    @BeforeMethod
    public void configure() {
        if (configuration != null) {
            subtable = configuration.subtable;
            toggler = configuration.toggler;
            employees = configuration.employees;

            secondConfiguration = configuration.name.equals("Men") ? configurationWomen : configurationMen;
            secondSubtable = secondConfiguration.subtable;
            secondToggler = secondConfiguration.toggler;
        }
    }

    protected void resetEmployees() {
        employees = configuration.employees;
    }

    public class Configuration {
        CollapsibleSubTable subtable;
        CollapsibleSubTableToggler toggler;
        List<Employee> employees;
        String name;

        public Configuration(int i, String name) {
            this.name = name;
            subtable = dataTable.getSubtable(i);
            toggler = dataTable.getToggler(i);
            final Employee.Sex sex = (i == 1) ? Employee.Sex.MALE : Employee.Sex.FEMALE;

            employees = new LinkedList<Employee>(Collections2.filter(EMPLOYEES, new Predicate<Employee>() {
                @Override
                public boolean apply(Employee input) {
                    return sex.equals(input.getSex());
                }
            }));
        }

        @Override
        public String toString() {
            return name;
        }
    }

    protected RequestType getRequestTypeForExpandMode() {
        switch (expandMode) {
            case ajax:
                return RequestType.XHR;
            case server:
                return RequestType.HTTP;
            default:
                return RequestType.NONE;
        }
    }

    public void enableShowData(final boolean showData) {
        new GuardRequest(RequestType.XHR) {
            public void command() {
                selenium.check(showDataLocator, showData);
                selenium.fireEvent(showDataLocator, Event.CLICK);
                selenium.fireEvent(showDataLocator, Event.CHANGE);
            }
        }.waitRequest();
    }

}
