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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;

import static org.testng.Assert.assertEquals;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.dataGrid.RichFacesDataGrid;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richDataGrid.fragment.GridRecordInterface;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public abstract class AbstractDataGridTest extends AbstractWebDriverTest {

    protected static final int ELEMENTS_TOTAL = 50;

    protected static final Integer[] COUNTS1 = { 1, 3, 11, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1,
        ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 };

    protected static final Integer[] COUNTS2 = { 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1,
        ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 };

    List<Capital> capitals;

    @FindByJQuery("input:checkbox[id$=noDataCheckbox]")
    protected WebElement attributeShowData;

    public abstract RichFacesDataGrid<? extends GridRecordInterface> getDataGrid();

    final Attributes<DataGridAttributes> dataGridAttributes = getAttributes();

    Integer columns = 3;

    Integer elements;

    Integer first;

    int expectedFirst;
    int expectedElements;
    int expectedColumns;
    double expectedRows;
    int page = 1;
    int lastPage = 1;

    public AbstractDataGridTest() throws JAXBException {
        capitals = Model.unmarshallCapitals();
    }

    @BeforeMethod
    public void prepareAttributes() {
        if (columns != null) {
            dataGridAttributes.set(DataGridAttributes.columns, columns.toString());
        } else {
            dataGridAttributes.reset(DataGridAttributes.columns);
        }
        if (elements != null) {
            dataGridAttributes.set(DataGridAttributes.elements, elements.toString());
        } else {
            dataGridAttributes.reset(DataGridAttributes.elements);
        }
        if (first != null) {
            dataGridAttributes.set(DataGridAttributes.first, first.toString());
        } else {
            dataGridAttributes.reset(DataGridAttributes.first);
        }
    }

    private void prepareAttribute(WebElement inputElement, Object value) {
        String v = value == null ? "" : value.toString();
        Graphene.guardHttp(inputElement).sendKeys(v);
    }

    protected void verifyGrid() {
        countExpectedValues();
        verifyCounts();
        verifyElements();
    }

    protected void verifyCounts() {
        try {
            assertEquals(getDataGrid().getNumberOfRecords(), expectedElements, "elements");
            assertEquals(getDataGrid().getNumberOfColumns(), expectedColumns, "columns");
            assertEquals(getDataGrid().getNumberOfRows(), (int) expectedRows, "rows");
        } catch (AssertionError e) {
            throw e;
        }
    }

    protected void verifyElements() {
        List<Capital> expectedCapitals = getExpectedCapitals();
        List<? extends GridRecordInterface> records = getDataGrid().getAllVisibleRecords();
        assertEquals(expectedCapitals.size(), records.size(), "Size of expected capitals and visible capitals does not match.");
        for (int i = 0; i < expectedCapitals.size(); i += 2) {
            assertEquals(records.get(i).getRecordText(), expectedCapitals.get(i).getState());
        }
    }

    protected void countExpectedValues() {
        if (elements == null) {
            elements = ELEMENTS_TOTAL;
        }

        if (first == null) {
            first = 0;
        }

        int firstOnPage = ((page - 1) * elements);
        int firstInRange = min(ELEMENTS_TOTAL, max(0, first));

        expectedFirst = firstOnPage + firstInRange;

        int elementsInRange = min(elements, ELEMENTS_TOTAL);
        expectedElements = elementsInRange;
        if (page == lastPage) {
            if (elements == 0) {
                expectedElements = ELEMENTS_TOTAL - expectedFirst;
            } else {
                expectedElements = min(elements, ELEMENTS_TOTAL - expectedFirst);
            }
        }

        expectedRows = ceil((float) expectedElements / columns);
        expectedColumns = columns;

        if (first >= ELEMENTS_TOTAL) {
            expectedColumns = 0;
        }
    }

    private List<Capital> getExpectedCapitals() {
        return capitals.subList(expectedFirst, expectedFirst + expectedElements);
    }
}
