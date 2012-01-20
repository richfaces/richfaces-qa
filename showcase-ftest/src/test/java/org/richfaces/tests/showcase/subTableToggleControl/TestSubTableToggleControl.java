/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.subTableToggleControl;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSubTableToggleControl extends AbstractDataIterationWithCars {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    private JQueryLocator chevroletToggler = jq("span.rf-csttg:eq(0)");
    private JQueryLocator gMCToggler = jq("span.rf-csttg:eq(2)");
    private JQueryLocator nissanToggler = jq("span.rf-csttg:eq(4)");

    private JQueryLocator bodyOfChevroletSubtable = jq("tbody.rf-cst:eq(0)");
    private JQueryLocator bodyOfGMCSubtable = jq("tbody.rf-cst:eq(2)");
    private JQueryLocator bodyOfNissanSubtable = jq("tbody.rf-cst:eq(4)");

    /* *****************************************************************************************************************
     * Tests
     * *****************************************************************************************************************
     */

    @Test
    public void testTogglers() {

        checkTogglersFunctionality(chevroletToggler, bodyOfChevroletSubtable);

        checkTogglersFunctionality(gMCToggler, bodyOfGMCSubtable);

        checkTogglersFunctionality(nissanToggler, bodyOfNissanSubtable);
    }

    @Test
    public void testTotalCountOfCars() {

        checksCountingOfRows(chevroletToggler, bodyOfChevroletSubtable);

        checksCountingOfRows(gMCToggler, bodyOfGMCSubtable);

        checksCountingOfRows(nissanToggler, bodyOfNissanSubtable);
    }

    /* *******************************************************************************************************************
     * Help methods
     * ******************************************************************************************************
     * *************
     */

    /**
     * Checks whether there is more than 0 rows with some data, also checks whether the counting of rows works
     *
     * @param toggler
     *            the toggler where user click in order to hide/show subtable
     * @param bodyOfTable
     *            the body of table
     */
    private void checksCountingOfRows(JQueryLocator toggler, JQueryLocator bodyOfTable) {

        selenium.click(toggler);

        String style = selenium.getAttribute(bodyOfTable.getAttribute(Attribute.STYLE));

        if (style.contains("display: none;")) {

            selenium.click(toggler);
        }

        JQueryLocator trs = jq(bodyOfTable.getRawLocator() + " > tr");

        int numberOfVisibleTrs = 0;

        for (Iterator<JQueryLocator> i = trs.iterator(); i.hasNext();) {

            if (selenium.isVisible(i.next())) {

                numberOfVisibleTrs++;
            }
        }

        String expectedNumberOfTrs = selenium.getText(bodyOfTable.getChild(jq("tr:visible:last"))).trim();

        String[] partsOfLine = expectedNumberOfTrs.split(":");
        int expectedNumberOfTrsInt = Integer.valueOf(partsOfLine[1].trim()).intValue();

        assertTrue(expectedNumberOfTrsInt > 0, "There should be some rows!");

        // there is -1 since there is one row with information about total number
        assertEquals(expectedNumberOfTrsInt, numberOfVisibleTrs - 1, "The information about total rows is incorrect!");
    }

    /**
     * Checks whether the subtable is displayed when it should be and otherwise
     *
     * @param toggler
     *            the toggler on which user click the subtable is displayed/hidden
     */
    private void checkTogglersFunctionality(JQueryLocator toggler, JQueryLocator subtable) {

        selenium.click(toggler);

        String style = selenium.getAttribute(subtable.getAttribute(Attribute.STYLE));

        boolean isSubTableDisplayedAfterFirstClick = !(style.contains("display: none;"));

        selenium.click(toggler);

        style = selenium.getAttribute(subtable.getAttribute(Attribute.STYLE));

        boolean isSubTableDisplayedAfterSecondClick = !(style.contains("display: none;"));

        assertEquals(isSubTableDisplayedAfterFirstClick, !isSubTableDisplayedAfterSecondClick,
            "The subtable should be displayed, or hidden, "
                + "it depends on whether the subtable was diplayed before clicking on toggler or not!");
    }

}
