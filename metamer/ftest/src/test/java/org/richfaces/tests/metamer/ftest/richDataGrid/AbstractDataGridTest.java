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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.DataGrid;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public abstract class AbstractDataGridTest extends AbstractAjocadoTest {

    protected static final int ELEMENTS_TOTAL = 50;

    List<Capital> capitals;

    JQueryLocator attributeColumns = pjq("input[id$=columnsInput]");
    JQueryLocator attributeElements = pjq("input[id$=elementsInput]");
    JQueryLocator attributeFirst = pjq("input[id$=firstInput]");
    JQueryLocator attributeShowData = pjq("input:checkbox[id$=noDataCheckbox]");

    DataGrid dataGrid = new DataGrid(jq("table.rf-dg[id$=richDataGrid]"));

    @Inject
    @Use(ints = { 3 })
    Integer columns;

    @Inject
    @Use(empty = true)
    Integer elements;

    @Inject
    @Use(empty = true)
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
        prepareAttribute(attributeColumns, columns);
        prepareAttribute(attributeElements, elements);
        prepareAttribute(attributeFirst, first);
    }

    private void prepareAttribute(ElementLocator<?> inputLocator, Object value) {
        String v = value == null ? "" : value.toString();
        guardHttp(selenium).type(inputLocator, v);
    }

    protected void verifyGrid() {
        countExpectedValues();
        verifyCounts();
        verifyElements();
    }

    protected void verifyCounts() {
        try {
            assertEquals(dataGrid.getElementCount(), (int) expectedElements, "elements");
            assertEquals(dataGrid.getColumnCount(), (int) expectedColumns, "columns");
            assertEquals(dataGrid.getRowCount(), (int) expectedRows, "rows");
        } catch (AssertionError e) {
            throw e;
        }
    }

    protected void verifyElements() {
        int elementNumber;
        try {
            Iterator<Capital> capitalIterator = getExpectedCapitalsIterator();
            Iterator<JQueryLocator> elementIterator = dataGrid.iterateElements();

            elementNumber = 1;
            while (capitalIterator.hasNext()) {
                final Capital capital = capitalIterator.next();
                if (!elementIterator.hasNext()) {
                    fail("there should be next element for state name: " + capital.getState());
                }
                elementNumber += 1;
                final JQueryLocator element = elementIterator.next().getChild(jq("span"));
                assertEquals(selenium.getText(element), capital.getState());
            }
        } catch (AssertionError e) {
            throw e;
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

    private Iterator<Capital> getExpectedCapitalsIterator() {
        return capitals.subList(expectedFirst, expectedFirst + expectedElements).iterator();
    }
}
