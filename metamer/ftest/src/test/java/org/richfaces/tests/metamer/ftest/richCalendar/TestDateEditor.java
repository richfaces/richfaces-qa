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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;


/**
 * Test case for date editro of a calendar on page faces/components/richCalendar/simple.xhtml.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23122 $
 */
public class TestDateEditor extends AbstractCalendarTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Test
    public void testShowDateEditor() {
        String currentMonth = new SimpleDateFormat("MMMM, yyyy").format(new Date()).substring(0, 3);
        String currentYear = new SimpleDateFormat("yyyy").format(new Date());

        selenium.click(input);
        guardNoRequest(selenium).click(monthLabel);

        assertTrue(selenium.isElementPresent(dateEditor), "Date editor should be present on the page.");
        assertTrue(selenium.isVisible(dateEditor), "Date editor should be visible.");

        for (int i = 0; i < 12; i++) {
            assertTrue(selenium.isElementPresent(dateEditorMonths.format(i)), "Date editor doesn't contain label for month " + Month.values()[i]);
            assertEquals(selenium.getText(dateEditorMonths.format(i)), Month.values()[i].toString().substring(0, 3));

            if (currentMonth.equals(selenium.getText(dateEditorMonths.format(i)))) {
                assertTrue(selenium.belongsClass(dateEditorMonths.format(i), "rf-cal-edtr-btn-sel"), "");
            }
        }

        for (int i = 0; i < 10; i++) {
            assertTrue(selenium.isElementPresent(dateEditorYears.format(i)), "Date editor doesn't contain label for year " + (2008 + i));
            assertEquals(selenium.getText(dateEditorYears.format(i)), Integer.toString(2008 + i), "Label of year");

            if (currentYear.equals(selenium.getText(dateEditorYears.format(i)))) {
                assertTrue(selenium.belongsClass(dateEditorYears.format(i), "rf-cal-edtr-btn-sel"), "Current year should be selected.");
            }
        }
    }

    @Test
    public void testCancelButton() {
        selenium.click(input);
        selenium.click(monthLabel);

        String oldLabel = selenium.getText(monthLabel);
        assertTrue(selenium.isElementPresent(dateEditor), "Date editor should be present on the page.");
        assertTrue(selenium.isVisible(dateEditor), "Date editor should be visible.");

        selenium.click(dateEditorRightArrow);
        selenium.click(dateEditorYears.format(0));

        selenium.click(dateEditorCancel);
        assertFalse(selenium.isVisible(dateEditor), "Date editor should not be visible.");
        assertEquals(selenium.getText(monthLabel), oldLabel, "Month and year in calendar's popup should not change.");
    }

    @Test
    public void testLeftButton() {
        selenium.click(input);
        selenium.click(monthLabel);

        int minYear = Integer.parseInt(selenium.getText(dateEditorYears.format(0)));

        selenium.click(dateEditorLeftArrow);
        selenium.click(dateEditorLeftArrow);
        selenium.click(dateEditorLeftArrow);

        for (int i = 0; i < 10; i++) {
            assertTrue(selenium.isElementPresent(dateEditorYears.format(i)), "Date editor doesn't contain label for year " + (minYear - 30 + i));
            assertEquals(selenium.getText(dateEditorYears.format(i)), Integer.toString(minYear - 30 + i), "Label of year");
            assertFalse(selenium.belongsClass(dateEditorYears.format(i), "rf-cal-edtr-btn-sel"), "No year should be selected.");
        }
    }

    @Test
    public void testRightButton() {
        selenium.click(input);
        selenium.click(monthLabel);

        int minYear = Integer.parseInt(selenium.getText(dateEditorYears.format(0)));

        selenium.click(dateEditorRightArrow);
        selenium.click(dateEditorRightArrow);
        selenium.click(dateEditorRightArrow);

        for (int i = 0; i < 10; i++) {
            assertTrue(selenium.isElementPresent(dateEditorYears.format(i)), "Date editor doesn't contain label for year " + (minYear + 30 + i));
            assertEquals(selenium.getText(dateEditorYears.format(i)), Integer.toString(minYear + 30 + i), "Label of year");
            assertFalse(selenium.belongsClass(dateEditorYears.format(i), "rf-cal-edtr-btn-sel"), "No year should be selected.");
        }
    }

    @Test
    public void testSelectMonth() {
        String currentMonth = new SimpleDateFormat("MMMM").format(new Date());
        String currentYear = new SimpleDateFormat("yyyy").format(new Date());

        selenium.click(input);
        selenium.click(monthLabel);

        if (currentMonth.equals(Month.January.toString())) {
            selenium.click(dateEditorMonths.format(Month.December.ordinal()));
            selenium.click(dateEditorOk);
            assertEquals(selenium.getText(monthLabel), "December, " + currentYear, "Month and year in calendar's popup should change.");
        } else {
            selenium.click(dateEditorMonths.format(Month.January.ordinal()));
            selenium.click(dateEditorOk);
            assertEquals(selenium.getText(monthLabel), "January, " + currentYear, "Month and year in calendar's popup should change.");
        }

        assertFalse(selenium.isVisible(dateEditor), "Date editor should not be visible.");
    }

    @Test
    public void testSelectYear() {
        selenium.click(input);
        selenium.click(monthLabel);

        String currentMonth = new SimpleDateFormat("MMMM").format(new Date());
        int minYear = Integer.parseInt(selenium.getText(dateEditorYears.format(0)));
        selenium.click(dateEditorRightArrow);

        selenium.click(dateEditorYears.format(0));
        selenium.click(dateEditorOk);
        assertFalse(selenium.isVisible(dateEditor), "Date editor should not be visible.");
        assertEquals(selenium.getText(monthLabel), currentMonth + ", " + (minYear + 10), "Month and year in calendar's popup should change.");

    }
}
