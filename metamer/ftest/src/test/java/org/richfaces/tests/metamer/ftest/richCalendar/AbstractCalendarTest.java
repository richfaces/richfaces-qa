/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;

/**
 * Abstract test case for calendar.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21691 $
 */
public abstract class AbstractCalendarTest extends AbstractAjocadoTest {

    public enum Month {

        January, February, March, April, May, June, July, August, September, October, November, December;

        public Month previous() {
            if (ordinal() == 0) {
                return December;
            }
            return Month.values()[ordinal() - 1];
        }

        public Month next() {
            if (ordinal() == 11) {
                return January;
            }
            return Month.values()[ordinal() + 1];
        }
    }
    // basic parts
    protected JQueryLocator calendar = pjq("span[id$=calendar]");
    protected JQueryLocator inputs = pjq("span[id$=calendarPopup]");
    protected JQueryLocator input = pjq("input.rf-cal-inp");
    protected JQueryLocator image = pjq("img.rf-cal-btn");
    protected JQueryLocator button = pjq("button.rf-cal-btn");
    // popup
    protected JQueryLocator popup = pjq("table[id$=calendarContent]");
    protected JQueryLocator prevYearButton = pjq("td.rf-cal-tl:eq(0) > div");
    protected JQueryLocator nextYearButton = pjq("td.rf-cal-tl:eq(3) > div");
    protected JQueryLocator prevMonthButton = pjq("td.rf-cal-tl:eq(1) > div");
    protected JQueryLocator nextMonthButton = pjq("td.rf-cal-tl:eq(2) > div");
    protected JQueryLocator closeButton = pjq("td.rf-cal-tl:eq(4) > div");
    protected JQueryLocator monthLabel = pjq("td.rf-cal-hdr-month > div");
    // 0 = blank, 1 = Sun, 2 = Mon, 3 = Tue ...
    protected JQueryLocator weekDayLabel = pjq("td.rf-cal-day-lbl:eq({0})");
    // week = 1..6, day = 0..6
    protected JQueryLocator cellWeekDay = pjq("tr[id$=calendarWeekNum{0}] > td:eq({1})");
    // day = 0..41
    protected JQueryLocator cellDay = pjq("td.rf-cal-c:eq({0})");
    // 0..6
    protected JQueryLocator week = pjq("td.rf-cal-week:eq({0})");
    protected JQueryLocator cleanButton = pjq("td.rf-cal-tl-ftr:eq(1) > div");
    protected JQueryLocator timeButton = pjq("td.rf-cal-tl-ftr:eq(2) > div");
    protected JQueryLocator todayButton = pjq("td.rf-cal-tl-ftr:eq(4) > div");
    protected JQueryLocator applyButton = pjq("td.rf-cal-tl-ftr:eq(5) > div");
    // time panel
    protected JQueryLocator timeEditor = pjq("table[id$=calendarTimeEditorLayout]");
    protected JQueryLocator timeEditorOk = pjq("div[id$=calendarTimeEditorButtonOk]");
    protected JQueryLocator timeEditorCancel = pjq("div[id$=calendarTimeEditorButtonCancel]");
    protected JQueryLocator hoursInput = pjq("input[id$=calendarTimeHours]");
    protected JQueryLocator hoursInputUp = pjq("div[id$=calendarTimeHoursBtnUp]");
    protected JQueryLocator hoursInputDown = pjq("div[id$=calendarTimeHoursBtnDown]");
    protected JQueryLocator minutesInput = pjq("input[id$=calendarTimeMinutes]");
    protected JQueryLocator minutesInputUp = pjq("div[id$=calendarTimeMinutesBtnUp]");
    protected JQueryLocator minutesInputDown = pjq("div[id$=calendarTimeMinutesBtnDown]");
    protected JQueryLocator secondsInput = pjq("input[id$=calendarTimeSeconds]");
    protected JQueryLocator secondsInputUp = pjq("div[id$=calendarTimeSecondsBtnUp]");
    protected JQueryLocator secondsInputDown = pjq("div[id$=calendarTimeSecondsBtnDown]");
    // date panel
    protected JQueryLocator dateEditor = pjq("table[id$=calendarDateEditorLayout]");
    protected JQueryLocator dateEditorOk = pjq("div[id$=calendarDateEditorButtonOk]");
    protected JQueryLocator dateEditorCancel = pjq("div[id$=calendarDateEditorButtonCancel]");
    protected JQueryLocator dateEditorLeftArrow = pjq("tr[id$=calendarDateEditorLayoutTR] div:eq(2)");
    protected JQueryLocator dateEditorRightArrow = pjq("tr[id$=calendarDateEditorLayoutTR] div:eq(3)");
    protected JQueryLocator dateEditorMonths = pjq("div[id$=calendarDateEditorLayoutM{0}]");
    protected JQueryLocator dateEditorYears = pjq("div[id$=calendarDateEditorLayoutY{0}]");
    // other
    protected JQueryLocator output = pjq("span[id$=output]");

    public void testOpenPopupClickOnInput() {
        guardNoRequest(selenium).click(input);
        assertTrue(selenium.isVisible(popup), "Popup should be visible.");
    }

    public void testOpenPopupClickOnImage() {
        guardNoRequest(selenium).click(image);
        assertTrue(selenium.isVisible(popup), "Popup should be visible.");
    }

    public void testHeaderButtons() {
        selenium.click(input);

        boolean displayed = selenium.isVisible(prevYearButton);
        assertTrue(displayed, "Previous year button should be visible.");
        String buttonText = selenium.getText(prevYearButton);
        assertEquals(buttonText, "<<", "Previous year button");

        displayed = selenium.isVisible(prevMonthButton);
        assertTrue(displayed, "Previous month button should be visible.");
        buttonText = selenium.getText(prevMonthButton);
        assertEquals(buttonText, "<", "Previous month button");

        displayed = selenium.isVisible(nextMonthButton);
        assertTrue(displayed, "Next month button should be visible.");
        buttonText = selenium.getText(nextMonthButton);
        assertEquals(buttonText, ">", "Next month button");

        displayed = selenium.isVisible(nextYearButton);
        assertTrue(displayed, "Next year button should be visible.");
        buttonText = selenium.getText(nextYearButton);
        assertEquals(buttonText, ">>", "Next year button");

        displayed = selenium.isVisible(closeButton);
        assertTrue(displayed, "Close button should be visible.");
        buttonText = selenium.getText(closeButton);
        assertEquals(buttonText, "x", "Close button");
    }

    public void testFooterButtons() {
        selenium.click(input);

        boolean displayed = selenium.isVisible(todayButton);
        assertTrue(displayed, "Today button should be visible.");
        String buttonText = selenium.getText(todayButton);
        assertEquals(buttonText, "Today", "Button's text");

        displayed = selenium.isVisible(applyButton);
        assertTrue(displayed, "Apply button should be visible.");
        buttonText = selenium.getText(applyButton);
        assertEquals(buttonText, "Apply", "Button's text");

        displayed = selenium.isElementPresent(cleanButton);
        assertFalse(displayed, "Clean button should not be visible.");

        displayed = selenium.isElementPresent(timeButton);
        assertFalse(displayed, "Time button should not be visible.");

        selenium.click(cellWeekDay.format(3, 3));

        displayed = selenium.isVisible(cleanButton);
        assertTrue(displayed, "Clean button should be visible.");
        buttonText = selenium.getText(cleanButton);
        assertEquals(buttonText, "Clean", "Button's text");

        displayed = selenium.isVisible(timeButton);
        assertTrue(displayed, "Time button should be visible.");
        buttonText = selenium.getText(timeButton);
        assertEquals(buttonText, "12:00", "Button's text");
    }

    public void testApplyButton() {
        selenium.click(input);

        selenium.click(cellDay.format(6));
        String day = selenium.getText(cellDay.format(6));
        String month = selenium.getText(monthLabel);

        String selectedDate = null;
        try {
            Date date = new SimpleDateFormat("d MMMM, yyyy hh:mm").parse(day + " " + month + " 12:00");
            selectedDate = new SimpleDateFormat("MMM d, yyyy hh:mm").format(date);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }

        guardXhr(selenium).click(applyButton);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        String inputDate = selenium.getValue(input);
        assertEquals(inputDate, selectedDate, "Input doesn't contain selected date.");

        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> " + selectedDate);
    }
}
