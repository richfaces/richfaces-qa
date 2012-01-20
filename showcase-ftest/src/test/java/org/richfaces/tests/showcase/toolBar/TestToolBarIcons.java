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
package org.richfaces.tests.showcase.toolBar;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestToolBarIcons extends AbstractAjocadoTest {

    /* ***********************************************************************************
     * Constants***********************************************************************************
     */

    protected final int NUMBER_OF_GROUP_SEP = 1;
    protected final int NUMBER_OF_ITEMS_SEP = 4;

    /* ***********************************************************************************
     * Locators***********************************************************************************
     */

    protected JQueryLocator lineGroupSep = jq("a:contains('Line'):eq(0)");
    protected JQueryLocator gridGroupSep = jq("a:contains('Grid'):eq(0)");
    protected JQueryLocator discGroupSep = jq("a:contains('Disc'):eq(0)");
    protected JQueryLocator squareGroupSep = jq("a:contains('Square'):eq(0)");
    protected JQueryLocator noneGroupSep = jq("a:contains('None'):eq(0)");

    protected JQueryLocator lineItemSeparator = jq("a:contains('Line'):eq(1)");
    protected JQueryLocator gridItemSep = jq("a:contains('Grid'):eq(1)");
    protected JQueryLocator discItemSep = jq("a:contains('Disc'):eq(1)");
    protected JQueryLocator squareItemSep = jq("a:contains('Square'):eq(1)");
    protected JQueryLocator noneItemSep = jq("a:contains('None'):eq(1)");

    protected JQueryLocator lineSep = jq("div.rf-tb-sep-line");
    protected JQueryLocator gridSep = jq("div.rf-tb-sep-grid");
    protected JQueryLocator discSep = jq("div.rf-tb-sep-disc");
    protected JQueryLocator squareSep = jq("div.rf-tb-sep-square");

    /* **********************************************************************************
     * Tests**********************************************************************************
     */

    @Test
    public void testGroupSeparators() {

        guardXhr(selenium).click(noneGroupSep);
        guardXhr(selenium).click(noneItemSep);

        checkNumberOfAllGroupsOrItemsSeparators(0, lineGroupSep, gridGroupSep, discGroupSep, squareGroupSep);

        checkNumberOfAllGroupsOrItemsSeparators(NUMBER_OF_GROUP_SEP, lineGroupSep, gridGroupSep, discGroupSep,
            squareGroupSep);
    }

    @Test
    public void testItemSeparators() {

        guardXhr(selenium).click(noneGroupSep);
        guardXhr(selenium).click(noneItemSep);

        checkNumberOfAllGroupsOrItemsSeparators(0, lineItemSeparator, gridItemSep, discItemSep, squareItemSep);

        checkNumberOfAllGroupsOrItemsSeparators(NUMBER_OF_ITEMS_SEP, lineItemSeparator, gridItemSep, discItemSep,
            squareItemSep);
    }

    /* ***********************************************************************************************
     * Help methods***********************************************************************************************
     */

    /**
     * Checks for number of all groups/items separators
     *
     * @param numberOfSeparators
     *            the expected number of all groups/items separators
     */
    private void checkNumberOfAllGroupsOrItemsSeparators(int numberOfSeparators, JQueryLocator whichLineSep,
        JQueryLocator whichGridSep, JQueryLocator whichDicsSep, JQueryLocator whichSquareSep) {

        if (numberOfSeparators != 0)
            guardXhr(selenium).click(whichLineSep);
        int actualNumberOfSeparators = selenium.getCount(lineSep);
        assertEquals(actualNumberOfSeparators, numberOfSeparators, "Wrong number of line groups/items separators");

        if (numberOfSeparators != 0)
            guardXhr(selenium).click(whichGridSep);
        numberOfSeparators = selenium.getCount(gridSep);
        assertEquals(actualNumberOfSeparators, numberOfSeparators, "Wrong number of grid groups/items separators");

        if (numberOfSeparators != 0)
            guardXhr(selenium).click(whichDicsSep);
        numberOfSeparators = selenium.getCount(discSep);
        assertEquals(actualNumberOfSeparators, numberOfSeparators, "Wrong number of disc groups/items separators");

        if (numberOfSeparators != 0)
            guardXhr(selenium).click(whichSquareSep);
        numberOfSeparators = selenium.getCount(squareSep);
        assertEquals(actualNumberOfSeparators, numberOfSeparators, "Wrong number of square groups/items separators");

    }
}
