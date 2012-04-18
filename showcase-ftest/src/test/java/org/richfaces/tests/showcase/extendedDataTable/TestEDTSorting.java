/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
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
 */
package org.richfaces.tests.showcase.extendedDataTable;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestEDTSorting extends AbstractExtendedTableTest {

    @Override
    protected String getAdditionToContextRoot() {
        String sampleName = "edt-sorting";

        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        demoName = StringUtils.substringAfterLast(demoName, ".");

        String addition = SimplifiedFormat.format("richfaces/component-sample.jsf?skin=blueSky&demo={0}&sample={1}",
                demoName, sampleName);

        return addition;
    }

    /**
     * Test table's single sorting by price and by model.
     */
    public void testSingleSorting() {
        //sort by price
        //first reset the sorting
        guardXhr(selenium).click(jq("input[value='Reset Sorting']"));
        guardXhr(selenium).click(SortBy.Price.sortButton); //sort values by price
        Double firstrowValue, secondRowValue;
        //check the price values
        firstrowValue = Double.parseDouble(selenium.getText(SortBy.Price.firstRow));
        secondRowValue = Double.parseDouble(selenium.getText(SortBy.Price.secondRow));
        assertTrue(firstrowValue <= secondRowValue, "The price in first row should be lesser or equal to the price in second row.");

        guardXhr(selenium).click(SortBy.Price.sortButton); //sort values reverse order

        firstrowValue = Double.parseDouble(selenium.getText(SortBy.Price.firstRow));
        secondRowValue = Double.parseDouble(selenium.getText(SortBy.Price.secondRow));
        assertTrue(firstrowValue >= secondRowValue, "The price in first row should be greater or equal to the price in second row.");

        //sort by model
        //first reset the sorting
        guardXhr(selenium).click(jq("input[value='Reset Sorting']"));
        guardXhr(selenium).click(SortBy.Model.sortButton);

        assertEquals(selenium.getText(SortBy.Model.firstRow), "4-Runner", "The first model should be 4-Runner");
        checkStrings(selenium.getText(SortBy.Model.firstRow), selenium.getText(SortBy.Model.secondRow));

        guardXhr(selenium).click(SortBy.Model.sortButton); //sort values reverse order

        assertEquals(selenium.getText(SortBy.Model.secondRow), "Yukon", "The first model should be Yukon");
        checkStrings(selenium.getText(SortBy.Model.secondRow), selenium.getText(SortBy.Model.firstRow));
    }

    /**
     * Tests EDT's multiple sorting by model and by price
     */
    @Test
    public void testMultipleSorting() {
        //set multiple sorting
        selenium.click(jq("input[type=checkbox]"));
        //sort by model
        guardXhr(selenium).click(SortBy.Model.sortButton);
        //check if table is sorted by model
        assertEquals(selenium.getText(SortBy.Model.firstRow), "4-Runner", "The first model should be 4-Runner");
        //sort by price
        guardXhr(selenium).click(SortBy.Price.sortButton);
        //check if table is still sorted by model
        assertEquals(selenium.getText(SortBy.Model.firstRow), "4-Runner", "The first model should be 4-Runner");
        //table is sorted by model, check if price is also sorted
        assertTrue(Double.parseDouble(selenium.getText(SortBy.Price.firstRow))
                <= Double.parseDouble(selenium.getText(SortBy.Price.secondRow)),
                "The price in first row should be lesser or equal to the price in second row.");
    }

    /**
     * Asserts that bigger string is bigger or equal the other string
     *
     * @param bigger
     * @param lowerOrEqual
     */
    private void checkStrings(String bigger, String lowerOrEqual) {
        assertTrue(bigger.compareTo(lowerOrEqual) >= 0);
    }

    public enum SortBy {

        Vendor("tbody[id$=tbf] > tr:eq(0) td:eq(0)", "tbody[id$=tbf] > tr:eq(1) td:eq(0)"),
        Model("tbody[id$=tbf] > tr:eq(0) td:eq(1)", "tbody[id$=tbf] > tr:eq(1) td:eq(1)"),
        Price("table[id$=tbtn] tr:eq(0) td:eq(0)", "table[id$=tbtn] tr:eq(1) td:eq(0)"),
        Mileage("table[id$=tbtn] tr:eq(0) td:eq(1)", "table[id$=tbtn] tr:eq(1) td:eq(1)");
        private final JQueryLocator firstRow;
        private final JQueryLocator secondRow;
        private final JQueryLocator sortButton;

        private SortBy(String firstRow, String secondRow) {
            this.firstRow = jq(firstRow);
            this.secondRow = jq(secondRow);
            this.sortButton = jq("a:contains(" + name() + ")");
        }
    }
}
