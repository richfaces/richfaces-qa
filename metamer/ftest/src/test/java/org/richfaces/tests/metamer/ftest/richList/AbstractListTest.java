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
package org.richfaces.tests.metamer.ftest.richList;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.list.RichFacesList;
import org.richfaces.fragment.list.RichFacesListItem;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.model.Employee;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractListTest extends AbstractWebDriverTest {

    static final List<Employee> employees = Model.unmarshallEmployees();
    static final int ELEMENTS_TOTAL = employees.size();
    static final Integer[] INTS = { -1, 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL,
        ELEMENTS_TOTAL + 1 };

    @FindBy(css = "[id$=richList]")
    protected RichFacesList list;

    @Inject
    @Use(empty = true)
    protected Integer first;
    @Inject
    @Use(empty = true)
    protected Integer rows;
    protected int expectedBegin;
    protected int displayedRows;
    protected int expectedEnd;
    List<Employee> expectedEmployees;

    private void countExpectedValues() {
        // expected begin
        if (first == null || first < 0) {
            expectedBegin = 0;
        } else {
            expectedBegin = first;
        }
        expectedBegin = minMax(0, expectedBegin, ELEMENTS_TOTAL);
        // expected displayed rows
        if (rows == null || rows < 1 || rows > ELEMENTS_TOTAL) {
            displayedRows = ELEMENTS_TOTAL;
        } else {
            displayedRows = rows;
        }
        if (first != null && first < 0) {
            displayedRows = 0;
        }
        displayedRows = min(displayedRows, ELEMENTS_TOTAL - expectedBegin);
        // expected end
        if (rows == null || rows < 1) {
            expectedEnd = ELEMENTS_TOTAL - 1;
        } else {
            expectedEnd = rows - 1;
        }
        expectedEmployees = employees.subList(expectedBegin, expectedBegin + displayedRows);
    }

    private int minMax(int min, int value, int max) {
        return max(min, min(max, value));
    }

    private void verifyCounts() {
        List<RichFacesListItem> items = list.getItems();
        assertEquals(list.getItems().size(), displayedRows);
    }

    protected void verifyList() {
        countExpectedValues();
        verifyCounts();
        verifyRows();
    }

    private void verifyRows() {
        List<RichFacesListItem> items = list.getItems();
        int rowCount = items.size();
        for (int position = 0; position < rowCount; position += 2) {
            Employee employee = expectedEmployees.get(position);
            assertEquals(items.get(position).getText(), employee.getName());
        }
    }
}
