/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.showcase.extendedDataTable;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestExTableSelection extends AbstractExtendedTableTest {

    JQueryLocator table = jq("tbody[id$=tbf]");
    JQueryLocator selectedRowInfo = jq("li[class$=itm]");
    JQueryLocator firstColumn = jq("div[class$=hdr] tr td:eq(1)");
    JQueryLocator secondColumn = jq("div[class$=hdr] tr td:eq(2)");
    JQueryLocator thirdColumnd = jq("div[class$=hdr] tr td:eq(4)");
    JQueryLocator fourthColumn = jq("div[class$=hdr] tr td:eq(5)");
    JQueryLocator fifthColumn = jq("div[class$=hdr] tr td:eq(6)");
    JQueryLocator firstResize = jq("div[class$=rsz]:first");
    JQueryLocator secondResize = jq("div[class$=rsz]:eq(1)");
    JQueryLocator thirdResize = jq("div[class$=rsz]:eq(2)");

    /*
     * ********************************************************************************************************
     * Tests I also have to test manually the expanding of columns and that
     * there is a possibility to change the order of columns, it should be
     * implemented in the future too.
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

    /**
     * Tests multiple selection with keyboard. First selects a few rows in table
     * and then deselects them with each step checking.
     */
    @Test
    public void testMultipleSelectionWithKeyboard() {
        //set selection mode, unnecessary -- it is default value
        JQueryLocator selectionMode = jq("input[value=multiple]");
        guardXhr(selenium).click(selectionMode);

        //select few rows and check them
        selectFewRowsAndDeselectThemWithChecking(new MultiSelectionModeWithKeyboard());
    }

    @Test
    public void testMultipleSelectionWithMouse() {
        //set selection mode
        JQueryLocator selectionMode = jq("input[value=multipleKeyboardFree]");
        guardXhr(selenium).click(selectionMode);

        //select few rows and check them
        selectFewRowsAndDeselectThemWithChecking(new MultiSelectionModeWithMouse());
    }

    /*
     * ***********************************************************************************************************
     * Help methods
     * ***********************************************************************************************************
     */
    /**
     * Selects few rows and deselect them with ckecking if the selection is
     * correct.
     *
     * @param mode selection mode
     */
    private void selectFewRowsAndDeselectThemWithChecking(ISelectionMode mode) {
        JQueryLocator firstRow = table.getChild(jq("tr:first"));
        JQueryLocator lastRow = table.getChild(jq("tr:last"));
        JQueryLocator someRow = table.getChild(jq("tr:eq(33)"));
        JQueryLocator someOtherRow = table.getChild(jq("tr:eq(20)"));

        //add more rows if you wish
        //BEWARE THE ORDER
        JQueryLocator[] selection = new JQueryLocator[]{firstRow, someRow, someOtherRow, lastRow};
        //select all rows with chosen mode
        mode.select(selection);
        //check if all selected cars are correctly chosen
        checkSelectedCars(selection);
        //deselect a car
        mode.select(someRow);
        //check if deselected car disappeared
        checkSelectedCars(firstRow, someOtherRow, lastRow);
        //deselect another car
        mode.select(someOtherRow);
        //check if deselected car disappeared
        checkSelectedCars(firstRow, lastRow);
        //deselect another car
        mode.select(firstRow);
        //check if deselected car disappeared
        checkSelectedCars(lastRow);
        //deselect last car
        mode.select(lastRow);
        //check if deselected car disappeared
        checkSelectedCars();
    }

    /**
     * Checks if selected cars are in "Selected Rows" list and checks if the
     * list does not contain more cars
     *
     * @param rows jquerry selector of rows with selected cars
     */
    private void checkSelectedCars(JQueryLocator... rows) {
        List<Car> cars = new ArrayList<Car>();
        for (JQueryLocator row : rows) {
            cars.add(retrieveCarFromRow(row, 0, 1));
        }

        List<Car> parsedCars = new ArrayList<Car>();
        JQueryLocator selectedParent = jq("ul.rf-ulst");
        for (int i = 0; i < cars.size(); i++) {
            String childString = ":nth-child(" + (i + 1) + ")";
            JQueryLocator child = selectedParent.getChild(jq(childString));
            parsedCars.add(parseSimplifiedCarFromListItem(child));
        }
        //try to find a non existing child
        try {
            JQueryLocator nonExistingChild = selectedParent.getChild(jq(":nth-child(" + (rows.length + 1) + ")"));
            parsedCars.add(parseSimplifiedCarFromListItem(nonExistingChild));
        } catch (Exception expected) {//no car found == this is correct
            assertEquals(cars, parsedCars);
            return;
        }
        fail("List contains more elements that it should");
    }

    /**
     * Checks whether both info from table and selected wors info are the same
     *
     * @param row
     */
    private void checkRowAndSelectedRowInfo(JQueryLocator row) {

        Car actualCarInTheRow = retrieveCarFromRow(row, 0, 1);

        guardXhr(selenium).click(row);

        Car actualCarInTheSelectedCarInfo = parseSimplifiedCarFromListItem(selectedRowInfo);

        assertEquals(actualCarInTheRow, actualCarInTheSelectedCarInfo,
                "The car from selected row can not differ from the " + "car in the selected rows info");

    }

    /**
     * Checks whether there is class attribute of particular row, and when it is
     * there retrieves it, returns null otherwise
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

    private interface ISelectionMode {

        void select(JQueryLocator... rows);
    }

    private class MultiSelectionModeWithKeyboard implements ISelectionMode {

        @Override
        public void select(JQueryLocator... rows) {
            selenium.controlKeyDown();
            for (JQueryLocator row : rows) {
                guardXhr(selenium).click(row);
            }
            selenium.controlKeyUp();
        }
    }

    private class MultiSelectionModeWithMouse implements ISelectionMode {

        @Override
        public void select(JQueryLocator... rows) {
            for (JQueryLocator row : rows) {
                guardXhr(selenium).click(row);
            }
        }
    }

    protected String getSampleLabel() {
        return "Selection Management";
    }
}
