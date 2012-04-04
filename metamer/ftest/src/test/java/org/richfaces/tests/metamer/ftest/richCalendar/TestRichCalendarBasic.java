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

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;


/**
 * Test case for basic functionality of calendar on page faces/components/richCalendar/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21887 $
 */
public class TestRichCalendarBasic extends AbstractCalendarTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Test
    public void testInit() {
        boolean displayed = selenium.isVisible(calendar);
        assertTrue(displayed, "Calendar is not present on the page.");

        displayed = selenium.isVisible(input);
        assertTrue(displayed, "Calendar's input should be visible.");

        displayed = selenium.isVisible(image);
        assertTrue(displayed, "Calendar's image should be visible.");

        displayed = selenium.isVisible(popup);
        assertFalse(displayed, "Popup should not be visible.");

        displayed = selenium.isElementPresent(button);
        assertFalse(displayed, "Calendar's button should not be visible.");
    }

    @Test
    @Override
    public void testOpenPopupClickOnInput() {
        super.testOpenPopupClickOnInput();
    }

    @Test
    @Override
    public void testOpenPopupClickOnImage() {
        super.testOpenPopupClickOnImage();
    }

    @Test
    @Override
    public void testHeaderButtons() {
        super.testHeaderButtons();
    }

    @Test
    public void testHeaderMonth() {
        String month = new SimpleDateFormat("MMMM, yyyy").format(new Date());

        selenium.click(input);
        String month2 = selenium.getText(monthLabel);
        assertEquals(month2, month, "Calendar shows wrong month or year in its header.");
    }

    @Test
    @Override
    public void testFooterButtons() {
        super.testFooterButtons();
    }

    @Test
    public void testWeekDaysLabels() {
        selenium.click(input);

        String[] labels = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (int i = 0; i < 8; i++) {
            String label = selenium.getText(weekDayLabel.format(i));
            assertEquals(label, labels[i], "Week day label " + i);
        }
    }

    @Test
    public void testTodayIsSelected() {
        SimpleDateFormat formatter = new SimpleDateFormat("d");
        String today = formatter.format(new Date());

        int lowerBoundary = 0;
        int upperBoundary = 42;

        // go through first half of popup or second half of popup depending on today's date
        // it is necessary because of dates from other months are also displayed
        if (Integer.parseInt(today) < 15) {
            upperBoundary = 28;
        } else {
            lowerBoundary = 14;
        }

        selenium.click(input);

        for (int i = lowerBoundary; i < upperBoundary; i++) {
            String day = selenium.getText(cellDay.format(i));
            if (day.equals(today)) {
                assertTrue(selenium.belongsClass(cellDay.format(i), "rf-cal-today"), "Today's date is not styled correctly.");
                return;
            }
        }

        fail("Today's date is not styled correctly.");
    }

    @Test
    public void testWeekNumbers() {
        selenium.click(input);
        String month = selenium.getText(monthLabel);
        String day = selenium.getText(cellDay.format(6));

        Date date = null;
        try {
            date = new SimpleDateFormat("d MMMM, yyyy").parse(day + " " + month);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }
        String weekNumber = new SimpleDateFormat("w").format(date);

        assertEquals(selenium.getText(week.format(0)), weekNumber, "Week number on the first line.");
    }

    @Test
    public void testSelectDate() {
        selenium.click(input);

        guardNoRequest(selenium).click(cellDay.format(6));
        assertTrue(selenium.belongsClass(cellDay.format(6), "rf-cal-sel"), "Last date in the first week is not selected.");

        selenium.click(cellDay.format(8));
        assertFalse(selenium.belongsClass(cellDay.format(6), "rf-cal-sel"), "Last date in the first week should not be selected.");
    }

    @Test
    public void testCleanButton() {
        selenium.click(input);
        selenium.click(cellDay.format(6));

        guardNoRequest(selenium).click(cleanButton);
        assertFalse(selenium.belongsClass(cellDay.format(6), "rf-cal-sel"), "Last date in the first week should not be selected.");
    }

    @Test
    public void testCloseButton() {
        selenium.click(input);
        boolean displayed = selenium.isVisible(popup);
        assertTrue(displayed, "Popup should be visible.");

        guardNoRequest(selenium).click(closeButton);
        displayed = selenium.isVisible(popup);
        assertFalse(displayed, "Popup should not be visible.");
    }

    @Test
    public void testPrevYearButton() {
        selenium.click(input);
        String thisYear = selenium.getText(monthLabel);
        // November, 2010 -> 2010
        thisYear = thisYear.substring(thisYear.indexOf(" ") + 1, thisYear.length());

        guardNoRequest(selenium).click(prevYearButton);
        String prevYear = selenium.getText(monthLabel);
        prevYear = prevYear.substring(prevYear.indexOf(" ") + 1, prevYear.length());

        assertEquals(Integer.parseInt(prevYear), Integer.parseInt(thisYear) - 1, "Year did not change correctly.");
    }

    @Test
    public void testNextYearButton() {
        selenium.click(input);
        String thisYear = selenium.getText(monthLabel);
        // November, 2010 -> 2010
        thisYear = thisYear.substring(thisYear.indexOf(" ") + 1, thisYear.length());

        guardNoRequest(selenium).click(nextYearButton);
        String nextYear = selenium.getText(monthLabel);
        nextYear = nextYear.substring(nextYear.indexOf(" ") + 1, nextYear.length());

        assertEquals(Integer.parseInt(nextYear), Integer.parseInt(thisYear) + 1, "Year did not change correctly.");
    }

    @Test
    public void testPrevMonthButton() {
        selenium.click(input);
        String thisMonth = selenium.getText(monthLabel);
        // November, 2010 -> November
        thisMonth = thisMonth.substring(0, thisMonth.indexOf(","));

        guardNoRequest(selenium).click(prevMonthButton);
        String prevMonth = selenium.getText(monthLabel);
        prevMonth = prevMonth.substring(0, prevMonth.indexOf(","));

        assertEquals(Month.valueOf(prevMonth), Month.valueOf(thisMonth).previous(), "Month did not change correctly.");
    }

    @Test
    public void testNextMonthButton() {
        selenium.click(input);
        String thisMonth = selenium.getText(monthLabel);
        // November, 2010 -> November
        thisMonth = thisMonth.substring(0, thisMonth.indexOf(","));

        guardNoRequest(selenium).click(nextMonthButton);
        String nextMonth = selenium.getText(monthLabel);
        nextMonth = nextMonth.substring(0, nextMonth.indexOf(","));

        assertEquals(Month.valueOf(nextMonth), Month.valueOf(thisMonth).next(), "Month did not change correctly.");
    }

    @Test
    public void testTodayButton() {
        selenium.click(input);
        String thisMonth = selenium.getText(monthLabel);

        selenium.click(nextMonthButton);
        selenium.click(prevYearButton);
        guardNoRequest(selenium).click(todayButton);
        String thisMonth2 = selenium.getText(monthLabel);

        assertEquals(thisMonth2, thisMonth, "Today button does not work.");
    }

    @Test
    @Override
    public void testApplyButton() {
        super.testApplyButton();
    }
}
