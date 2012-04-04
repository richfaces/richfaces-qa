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

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.repeat.AbstractDataIterationWithStates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTableSorting extends AbstractDataIterationWithStates {

    /*
     * Locators****************************************************************************************************
     */
    private JQueryLocator sortByCapitalName = jq("a:contains('Sort by Capital Name')");
    private JQueryLocator sortByStateName = jq("a:contains('Sort by State Name')");
    private JQueryLocator sortByTimeZone = jq("a:contains('Sort by Time Zone')");
    private JQueryLocator firstRow = jq("tr[class='rf-dt-r rf-dt-fst-r']");

    /*
     * Constants*****************************************************************************************************
     */
    private final StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_CAPITAL_ASCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "New York", "Albany", "GMT-5");

    private StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_CAPITAL_DESCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "New Jersey", "Trenton", "GMT-5");

    private StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_STATE_ASCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Alabama", "Montgomery", "GMT-6");

    private StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_STATE_DESCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Wyoming", "Cheyenne", "GMT-7");

    private StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_TIME_ZONE_ASCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Hawaii", "Honolulu", "GMT-10");

    private StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_TIME_ZONE_DESCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Connecticut", "Hartford", "GMT-5");

    /*
     * Tests*******************************************************************************************************
     */

    @Test
    public void testSortByCapitalName() {

        clickOnParticularSortAnchorCheckFirstRow(sortByCapitalName, FIRST_STATE_SORTED_BY_CAPITAL_ASCENDING_ORDER,
            FIRST_STATE_SORTED_BY_CAPITAL_DESCENDING_ORDER,
            "The table should be ordered by capital name in ascending order",
            "The table should be ordered by capital name in descending order");

    }

    @Test
    public void testSortByStateName() {

        clickOnParticularSortAnchorCheckFirstRow(sortByStateName, FIRST_STATE_SORTED_BY_STATE_ASCENDING_ORDER,
            FIRST_STATE_SORTED_BY_STATE_DESCENDING_ORDER,
            "The table shoould be ordered by state name in ascending order",
            "The table should be ordered by state name in descending order");

    }

    @Test
    public void testSortByTimeZone() {

        clickOnParticularSortAnchorCheckFirstRow(sortByTimeZone, FIRST_STATE_SORTED_BY_TIME_ZONE_ASCENDING_ORDER,
            FIRST_STATE_SORTED_BY_TIME_ZONE_DESCENDING_ORDER,
            "The table sould be ordered by time zone in ascending order",
            "The table should be ordered by time zone in descending order");

    }

    /**
     * Sorts table and checks the first row according to expected first row
     *
     * @param sortBy
     * @param ascendingState
     *            expected state
     * @param descendingState
     *            expected state
     * @param ascendingError
     * @param descendingError
     */
    private void clickOnParticularSortAnchorCheckFirstRow(JQueryLocator sortBy,
        StateWithCapitalAndTimeZone ascendingState, StateWithCapitalAndTimeZone descendingState, String ascendingError,
        String descendingError) {

        guardXhr(selenium).click(sortBy);

        StateWithCapitalAndTimeZone actualState = initializeStateDataFromRow();

        assertEquals(actualState, ascendingState, ascendingError);

        guardXhr(selenium).click(sortBy);

        actualState = initializeStateDataFromRow();

        assertEquals(actualState, descendingState, descendingError);

    }

    /**
     * returns new StateWithCapitalAndTimeZone, which is initialized byt the data in the partilucal row
     *
     * @param row
     * @return
     */
    private StateWithCapitalAndTimeZone initializeStateDataFromRow() {

        JQueryLocator tds = jq(firstRow.getRawLocator() + " > td");

        String capitalName = null;
        String stateName = null;
        String timeZone = null;

        int i = 0;
        for (Iterator<JQueryLocator> iterator = tds.iterator(); iterator.hasNext();) {

            JQueryLocator currentTd = iterator.next();

            switch (i) {
                case 1:
                    capitalName = selenium.getText(currentTd);
                    break;
                case 2:
                    stateName = selenium.getText(currentTd);
                    break;
                case 3:
                    timeZone = selenium.getText(currentTd);
                    break;
                default:
                    break;
            }

            i++;
        }

        return new StateWithCapitalAndTimeZone(stateName, capitalName, timeZone);
    }

}
