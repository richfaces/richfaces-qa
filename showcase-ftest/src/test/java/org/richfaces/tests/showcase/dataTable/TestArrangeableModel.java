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
package org.richfaces.tests.showcase.dataTable;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestArrangeableModel extends AbstractGrapheneTest {

    /* *******************************************************************************************
     * Locators ****************************************************************** *************************
     */

    protected JQueryLocator firstNameFilterInput = jq("input[type=text]:eq(0)");
    protected JQueryLocator secondNameFilterInput = jq("input[type=text]:eq(1)");
    protected JQueryLocator emailFilterInput = jq("input[type=text]:eq(2)");
    protected JQueryLocator table = jq("tbody.rf-dt-b");
    // these are links for filtering rows in a ascending, descending way
    // 0 is for first name column, 1 surname, 2 email
    protected JQueryLocator unsortedLink = jq("a[onClick*='RichFaces']:eq({0})");
    protected JQueryLocator ascendingLink = jq("a:contains(ascending)");
    protected JQueryLocator descendingLink = jq("a:contains(descending)");
    protected JQueryLocator firstRowSomeColumn = jq(table.getRawLocator() + " > tr:eq(0) > td:eq({0})");

    /* *******************************************************************************************
     * Tests ********************************************************************* **********************
     */

    @Test
    public void testTableIsNotEmpty() {

        testWhetherTableContainsNonEmptyStrings(table);
    }

    @Test
    public void testFirstNameFilter() {

        filterAnColumn(firstNameFilterInput, "as", "td:eq(0)");
        eraseInput(firstNameFilterInput);
        guardXhr(selenium).fireEvent(firstNameFilterInput, Event.KEYUP);
    }

    @Test
    public void testSurnameFilter() {

        filterAnColumn(secondNameFilterInput, "al", "td:eq(1)");
        eraseInput(secondNameFilterInput);
        guardXhr(selenium).fireEvent(secondNameFilterInput, Event.KEYUP);
    }

    @Test
    public void testEmailFilter() {

        filterAnColumn(emailFilterInput, "ac", "td:eq(2)");
        eraseInput(emailFilterInput);
        guardXhr(selenium).fireEvent(emailFilterInput, Event.KEYUP);

    }

    @Test
    public void testFirstNameSorting() {

        ascendingDescendingSortingOnColumn(0, "Z");
    }

    @Test
    public void testSurnameSorting() {

        ascendingDescendingSortingOnColumn(1, "Z");
    }

    @Test
    public void testEmailSorting() {

        ascendingDescendingSortingOnColumn(2, "v");
    }

    /* **********************************************************************************************
     * Help methods ************************************************************** ********************************
     */

    private boolean doesColumnContainsOnlyRowsWithData(String column, String data) {

        JQueryLocator table = jq(this.table.getRawLocator() + " > tr");

        for (Iterator<JQueryLocator> i = table.iterator(); i.hasNext();) {

            JQueryLocator td = jq(i.next().getRawLocator() + " > " + column);

            String tdText = selenium.getText(td);

            if (!tdText.toLowerCase().contains(data)) {

                return false;
            }

        }

        return true;
    }

    private void filterAnColumn(JQueryLocator filterInput, String filterValue, String column) {

        selenium.type(filterInput, filterValue);
        guardXhr(selenium).fireEvent(filterInput, Event.KEYUP);

        boolean result = doesColumnContainsOnlyRowsWithData(column, filterValue);

        assertTrue(result, "The table should contains only rows, which column " + column + " contains only data "
            + filterValue);
    }

    private void ascendingDescendingSortingOnColumn(int column, String firstCharOfRowWhenDescending) {

        // ascending
        guardXhr(selenium).click(unsortedLink.format(column));

        JQueryLocator td = firstRowSomeColumn.format(column);
        String checkedValue = selenium.getText(td);

        assertEquals(String.valueOf(checkedValue.charAt(0)), "A",
            "Rows should be sorted in an ascending order, by column " + td.getRawLocator());

        // descending
        guardXhr(selenium).click(ascendingLink);

        checkedValue = selenium.getText(td);

        assertEquals(String.valueOf(checkedValue.charAt(0)), firstCharOfRowWhenDescending,
            "Rows should be sorted in an descending order, by column " + td.getRawLocator());
    }

    @Override
    protected String getSampleLabel() {
        return "Arrangeable";
    }
}
