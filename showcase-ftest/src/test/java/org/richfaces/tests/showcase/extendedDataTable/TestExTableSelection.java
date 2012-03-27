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
package org.richfaces.tests.showcase.extendedDataTable;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestExTableSelection extends AbstractExtendedTableTest {

    JQueryLocator table = jq("tbody[id$=tbf]");
    JQueryLocator selectedRowsInfo = jq("li[class$=itm]");

    JQueryLocator firstColumn = jq("div[class$=hdr] tr td:eq(1)");
    JQueryLocator secondColumn = jq("div[class$=hdr] tr td:eq(2)");
    JQueryLocator thirdColumnd = jq("div[class$=hdr] tr td:eq(4)");
    JQueryLocator fourthColumn = jq("div[class$=hdr] tr td:eq(5)");
    JQueryLocator fifthColumn = jq("div[class$=hdr] tr td:eq(6)");

    JQueryLocator firstResize = jq("div[class$=rsz]:first");
    JQueryLocator secondResize = jq("div[class$=rsz]:eq(1)");
    JQueryLocator thirdResize = jq("div[class$=rsz]:eq(2)");

    /* ********************************************************************************************************
     * Tests I also have to test manually the expanding of columns and that there is a possibility to change the order
     * of columns, it should be implemented in the future too.
     * ********************************************************************************************************
     */

    @Test
    public void testFirstRow() {

        JQueryLocator row = table.getChild(jq("tr:eq(0)"));

        checkTheRow("Chevrolet", "Corvette", row);
    }

    @Test
    public void testLastRow() {

        JQueryLocator row = table.getChild(jq("tr:last"));

        checkTheRow("Infiniti", "EX35", row);
    }

    @Test
    public void testRowAndSelectedRowsInfo() {

        JQueryLocator row = table.getChild(jq("tr:eq(0)"));

        checkRowAndSelectedRowInfo(row);

        row = table.getChild(jq("tr:eq(15)"));

        checkRowAndSelectedRowInfo(row);

        row = table.getChild(jq("tr:eq(50)"));

        checkRowAndSelectedRowInfo(row);
    }

    @Test
    public void testRowhighlightingWhenSelected() {

        JQueryLocator row1 = table.getChild(jq("tr:eq(0)"));

        String classAttribute = checkClassAttributePresentAndRetrievesIt(row1);

        if (classAttribute != null) {

            assertFalse(classAttribute.contains("act"), "No rows should be selected");
        }

        guardXhr(selenium).click(row1);

        classAttribute = checkClassAttributePresentAndRetrievesIt(row1);

        if (classAttribute != null) {

            assertTrue(classAttribute.contains("act"), "First row should be selected");
        } else {

            fail("First row should be selected");
        }

        JQueryLocator row2 = table.getChild(jq("tr:eq(1)"));

        guardXhr(selenium).click(row2);

        classAttribute = checkClassAttributePresentAndRetrievesIt(row1);

        if (classAttribute != null) {

            assertFalse(classAttribute.contains("act"), "First row is no more selected");
        }

        classAttribute = checkClassAttributePresentAndRetrievesIt(row2);

        if (classAttribute != null) {

            assertTrue(classAttribute.contains("act"), "Second row should be selected");
        } else {

            fail("Second row should be selected");
        }
    }

    /* ***********************************************************************************************************
     * Help methods
     * ***********************************************************************************************************
     */

    /**
     * Retrieves car info from selected rows info
     */
    private Car retrieveCarFromSelectedRowsInfo() {

        Car car = new Car();

        String contentOfSelectedRowsInfo = selenium.getText(selectedRowsInfo);

        String[] partsOfContent = contentOfSelectedRowsInfo.split("-");

        car.setVendor(partsOfContent[0].trim()).setModel(partsOfContent[1].trim());

        return car;
    }

    /**
     * Checks whether both info from table and selected wors info are the same
     *
     * @param row
     */
    private void checkRowAndSelectedRowInfo(JQueryLocator row) {

        Car actualCarInTheRow = retrieveCarFromRow(row, 0, 1);

        guardXhr(selenium).click(row);

        Car actualCarInTheSelectedCarInfo = retrieveCarFromSelectedRowsInfo();

        assertEquals(actualCarInTheRow, actualCarInTheSelectedCarInfo,
            "The car from selected row can not differ from the " + "car in the selected rows info");

    }

    /**
     * Checks whether there is class attribute of particular row, and when it is there retrieves it, returns null
     * otherwise
     *
     * @param row
     * @return
     */
    private String checkClassAttributePresentAndRetrievesIt(JQueryLocator row) {

        if (selenium.isAttributePresent(row.getAttribute(Attribute.CLASS))) {

            return selenium.getAttribute(row.getAttribute(Attribute.CLASS));
        }

        return null;
    }
}
