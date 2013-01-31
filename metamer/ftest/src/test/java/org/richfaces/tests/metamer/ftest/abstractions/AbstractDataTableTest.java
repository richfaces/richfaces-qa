/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.dom.Attribute.ALT;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import java.util.List;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.test.selenium.GuardRequest;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.model.AssertingDataScroller;
import org.richfaces.tests.metamer.ftest.model.DataScroller;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.metamer.model.Employee;
import org.richfaces.tests.metamer.model.Employee.Sex;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22688 $
 */
public abstract class AbstractDataTableTest extends AbstractGrapheneTest {
    protected static final List<Capital> CAPITALS = Model.unmarshallCapitals();
    protected static final List<Employee> EMPLOYEES = Model.unmarshallEmployees();
    protected static final int ELEMENTS_TOTAL = 50;

    protected static final int COLUMN_STATE = 1;
    protected static final int COLUMN_CAPITAL = 2;

    protected static final int COLUMN_SEX = 1;
    protected static final int COLUMN_NAME = 2;
    protected static final int COLUMN_TITLE = 3;
    protected static final int COLUMN_NUMBER_OF_KIDS1 = 4;
    protected static final int COLUMN_NUMBER_OF_KIDS2 = 5;

    protected static final Integer[] COUNTS = new Integer[] { null, 1, 3, 11, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1,
            ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 };

    protected DataTable model;
    protected EmployeeTableModel employees = new EmployeeTableModel();

    protected DataScroller dataScroller1 = new AssertingDataScroller("outside-table", pjq("span.rf-ds[id$=scroller1]"));
    protected DataScroller dataScroller2 = new AssertingDataScroller("inside-table-footer", pjq("span.rf-ds[id$=scroller2]"));

    JQueryLocator showDataLocator = pjq("input[id$=noDataCheckbox]");

    public class EmployeeTableModel {
        public Sex getSex(int row) {
            JQueryLocator element = model.getElement(COLUMN_SEX, row).getDescendant(jq("img"));
            String sex = selenium.getAttribute(element.getAttribute(ALT));
            return Sex.valueOf(sex.toUpperCase());
        }

        public String getName(int row) {
            return selenium.getText(model.getElement(COLUMN_NAME, row));
        }

        public String getTitle(int row) {
            return selenium.getText(model.getElement(COLUMN_TITLE, row));
        }

        public int getNumberOfKids(int row) {
            return Integer.valueOf(selenium.getText(model.getElement(COLUMN_NUMBER_OF_KIDS1, row)));
        }
    }

    protected void enableShowData(final boolean showData) {
        new GuardRequest(RequestType.XHR) {
            public void command() {
                selenium.check(showDataLocator, showData);
                selenium.fireEvent(showDataLocator, Event.CLICK);
                selenium.fireEvent(showDataLocator, Event.CHANGE);
            }
        }.waitRequest();
    }
}
