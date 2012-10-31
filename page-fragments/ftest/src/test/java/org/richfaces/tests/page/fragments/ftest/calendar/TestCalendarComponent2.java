/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.page.fragments.ftest.calendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.joda.time.DateTime;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.ftest.AbstractTest;
import org.richfaces.tests.page.fragments.impl.calendar.common.FooterControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.HeaderControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.DayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor.SetValueBy;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth.YearAndMonthEditor;
import org.richfaces.tests.page.fragments.impl.calendar.inline.CalendarInlineComponentImpl;
import org.richfaces.tests.page.fragments.impl.calendar.popup.CalendarPopupComponent.OpenedBy;
import org.richfaces.tests.page.fragments.impl.calendar.popup.CalendarPopupComponentImpl;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopup;
import org.testng.annotations.Test;

public class TestCalendarComponent2 extends AbstractTest {

    private static final DateTime firstOfNovember2012 = new DateTime(2012, 11, 1, 12, 0);
    private static final DateTime firstOfJanuary2012 = new DateTime(2012, 1, 1, 12, 0);
    //
    @FindBy(css = "span[id$=calendar]")
    private CalendarPopupComponentImpl calendar;
    @FindBy(css = "span[id$=calendarWithApplyButton]")
    private CalendarPopupComponentImpl calendarWithApplyButton;
    @FindBy(css = "span[id$=calendarWithoutFooter]")
    private CalendarPopupComponentImpl calendarWithoutFooter;
    @FindBy(css = "span[id$=calendarWithoutHeader]")
    private CalendarPopupComponentImpl calendarWithoutHeader;
    @FindBy(css = "span[id$=calendarWithoutInput]")
    private CalendarPopupComponentImpl calendarWithoutInput;
    @FindBy(css = "span[id$=calendarWithoutWeekDaysBar]")
    private CalendarPopupComponentImpl calendarWithoutWeekDaysBar;
    @FindBy(css = "span[id$=calendarWithoutWeeksBar]")
    private CalendarPopupComponentImpl calendarWithoutWeeksBar;
    @FindBy(css = "span[id$=calendarInline]")
    private CalendarInlineComponentImpl calendarInline;
    //
    private FooterControls footerControls;
    private HeaderControls headerControls;
    private CalendarPopup popup;
    private DayPicker dayPicker;

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return createDeployment(TestCalendarComponent2.class).addClass(CalendarBean.class);
    }

    @Test
    public void closePopup_openedPopup_popupWillDisappear() {
        popup = calendar.openPopup(OpenedBy.inputClicking);
        calendar.closePopup();
        assertFalse(popup.isVisible());
    }

    @Test
    public void closePopup_noPopupDisplayed_popupWillStayHidden() {
        popup = calendar.openPopup(OpenedBy.inputClicking);
        calendar.closePopup();
        calendar.closePopup();//
        assertFalse(popup.isVisible());
    }

    @Test
    public void dayPicker_selectDateInPrevMonth_dateWillNotChange() {
        popup = calendar.getProxiedPopup();
        popup.getHeaderControls().openYearAndMonthEditor().selectDate(firstOfNovember2012).confirmDate();
        String inputValue = calendar.getInputValue();
        dayPicker = popup.getDayPicker();
        dayPicker.getBoundaryDays().get(0).select();
        String inputValueAfter = calendar.getInputValue();
        assertEquals(inputValueAfter, inputValue);
    }

    @Test
    public void dayPicker_selectDateInThisMonth_dateWillChange() {
        popup = calendar.getProxiedPopup();
        popup.getFooterControls().setTodaysDate();
        String inputValue = calendar.getInputValue();
        dayPicker = popup.getDayPicker();
        dayPicker.getMonthDays().get(0).select();
        String inputValueAfter = calendar.getInputValue();
        assertNotEquals(inputValueAfter, inputValue);
    }

    @Test
    public void dayPicker_getWeekNumbers_returnsCorrectNumbers() {
        popup = calendar.getProxiedPopup();
        popup.getHeaderControls().openYearAndMonthEditor().selectDate(firstOfJanuary2012).confirmDate();
        dayPicker = popup.getDayPicker();
        List<Integer> weeksNumbers = dayPicker.getWeeksNumbers();
        assertEquals(weeksNumbers, Arrays.asList(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void dayPicker_inlineMode_getWeekNumbers_returnsCorrectNumbers() {
        calendarInline.getHeaderControls().openYearAndMonthEditor().selectDate(firstOfJanuary2012).confirmDate();
        dayPicker = calendarInline.getDayPicker();
        List<Integer> weeksNumbers = dayPicker.getWeeksNumbers();
        assertEquals(weeksNumbers, Arrays.asList(1, 2, 3, 4, 5, 6));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void dayPicker_getWeekNumbersWhenBarIsNotDisplayed_throwsException() {
        popup = calendarWithoutWeeksBar.getProxiedPopup();
        popup.getHeaderControls().openYearAndMonthEditor().selectDate(firstOfJanuary2012).confirmDate();
        dayPicker = popup.getDayPicker();
        dayPicker.getWeeksNumbers();
    }

    @Test
    public void dayPicker_getWeekDayShortNames_returnsCorrectValues() {
        popup = calendar.getProxiedPopup();
        List<String> weekDayShortNames = popup.getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
    }

    @Test
    public void dayPicker_inlineMode_getWeekDayShortNames_returnsCorrectValues() {
        List<String> weekDayShortNames = calendarInline.getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void dayPicker_getWeekDayShortNamesWhenBarIsNotDisplayed_throwsException() {
        popup = calendarWithoutWeekDaysBar.getProxiedPopup();
        popup.getDayPicker().getWeekDayShortNames();
    }

    public void dateSetting_withApplyButton_dateWillChange() {
        popup = calendarWithApplyButton.getProxiedPopup();
        popup.getFooterControls().setTodaysDate();
        String inputValue = calendarWithApplyButton.getInputValue();
        assertFalse(inputValue.isEmpty());
    }

    @Test
    public void interactingWithHeaderControls_whenControlsNotDisplayed_throwsException() {
        headerControls = calendarWithoutHeader.openPopup(OpenedBy.inputClicking).getHeaderControls();
        assertFalse(headerControls.isVisible());
        int counter = 0;
        try {
            headerControls.previousMonth();
        } catch (RuntimeException e) {
            counter++;
        }
        try {
            headerControls.previousYear();
        } catch (RuntimeException e) {
            counter++;
        }
        try {
            headerControls.nextMonth();
        } catch (RuntimeException e) {
            counter++;
        }
        try {
            headerControls.nextYear();
        } catch (RuntimeException e) {
            counter++;
        }
        try {
            headerControls.openYearAndMonthEditor();
        } catch (RuntimeException e) {
            counter++;
        }
        assertEquals(counter, 5);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void interactingWithFooterControls_whenControlsNotDisplayed_throwsException() {
        footerControls = calendarWithoutFooter.openPopup(OpenedBy.inputClicking).getFooterControls();
        assertFalse(footerControls.isVisible());
        footerControls.setTodaysDate();
    }

    @Test
    public void openPopup_byInputClicking_popupWillShow() {
        popup = calendar.openPopup(OpenedBy.inputClicking);
        assertTrue(popup.isVisible());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void openPopup_calendarWithoutInputByInputClicking_throwsException() {
        calendarWithoutInput.openPopup(OpenedBy.inputClicking);
    }

    @Test
    public void openPopup_byOpenButtonClicking_popupWillShow() {
        popup = calendar.openPopup(OpenedBy.openButtonClicking);
        assertTrue(popup.isVisible());
    }

    @Test
    public void openPopup_alreadyOpenedPopup_popupWillStayDisplayed() {
        popup = calendar.openPopup(OpenedBy.openButtonClicking);
        calendar.openPopup();
        assertTrue(popup.isVisible());
    }

    @Test
    public void timeEditor_setTimeByButtonsThenConfirm_timeWillChange() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime today = new DateTime();
        openedTimeEditor.setTime(today, SetValueBy.buttons).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), today.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), today.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), today.getSecondOfMinute());
    }

    @Test
    public void timeEditor_setTimeByWritingThenConfirm_timeWillChange() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime today = new DateTime();
        openedTimeEditor.setTime(today, SetValueBy.typing).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), today.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), today.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), today.getSecondOfMinute());
    }

    @Test
    public void timeEditor_setTimeByWritingThenCancel_timeWillNotChange() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime today = new DateTime();
        openedTimeEditor.setTime(today, SetValueBy.typing).cancelTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), 12);
        assertEquals(time.getMinuteOfHour(), 0);
        assertEquals(time.getSecondOfMinute(), 0);
    }

    @Test
    public void timeEditor_24hFormat_00_00_timeWillBeSetAndGetCorrectly() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime testedTime = new DateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        openedTimeEditor.setTime(testedTime, SetValueBy.buttons).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), testedTime.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), testedTime.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), testedTime.getSecondOfMinute());
    }

    @Test
    public void timeEditor_24hFormat_00_01_timeWillBeSetAndGetCorrectly() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime testedTime = new DateTime().withHourOfDay(0).withMinuteOfHour(1).withSecondOfMinute(0);
        openedTimeEditor.setTime(testedTime, SetValueBy.buttons).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), testedTime.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), testedTime.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), testedTime.getSecondOfMinute());
    }

    @Test
    public void timeEditor_24hFormat_23_59_timeWillBeSetAndGetCorrectly() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime testedTime = new DateTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(0);
        openedTimeEditor.setTime(testedTime, SetValueBy.buttons).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), testedTime.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), testedTime.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), testedTime.getSecondOfMinute());
    }

    @Test
    public void timeEditor_24hFormat_12_00_timeWillBeSetAndGetCorrectly() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime testedTime = new DateTime().withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
        openedTimeEditor.setTime(testedTime, SetValueBy.buttons).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), testedTime.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), testedTime.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), testedTime.getSecondOfMinute());
    }

    @Test
    public void timeEditor_24hFormat_11_59_timeWillBeSetAndGetCorrectly() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime testedTime = new DateTime().withHourOfDay(11).withMinuteOfHour(59).withSecondOfMinute(0);
        openedTimeEditor.setTime(testedTime, SetValueBy.buttons).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), testedTime.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), testedTime.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), testedTime.getSecondOfMinute());
    }

    @Test
    public void timeEditor_24hFormat_12_01_timeWillBeSetAndGetCorrectly() {
        popup = calendar.getProxiedPopup();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        TimeEditor openedTimeEditor = footerControls.openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime testedTime = new DateTime().withHourOfDay(12).withMinuteOfHour(1).withSecondOfMinute(0);
        openedTimeEditor.setTime(testedTime, SetValueBy.buttons).confirmTime();
        DateTime time = footerControls.openTimeEditor().getTime();
        assertEquals(time.getHourOfDay(), testedTime.getHourOfDay());
        assertEquals(time.getMinuteOfHour(), testedTime.getMinuteOfHour());
        assertEquals(time.getSecondOfMinute(), testedTime.getSecondOfMinute());
    }

    @Test
    public void yearAndMonthEditor_selectDateInPastThenConfirm_dateWillChange() {
        int testedMonth = 6;
        int someYearsInPast = 16;
        popup = calendar.getProxiedPopup();
        headerControls = popup.getProxiedHeaderControls();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(headerControls.isVisible());
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        YearAndMonthEditor openedDateEditor = headerControls.openYearAndMonthEditor();
        assertTrue(openedDateEditor.isVisible());
        DateTime today = new DateTime();
        DateTime expected = today.minusYears(someYearsInPast).withMonthOfYear(testedMonth);
        openedDateEditor.selectDate(expected).confirmDate();
        openedDateEditor = headerControls.openYearAndMonthEditor();
        DateTime selectedMonthAndYear = openedDateEditor.getDate();
        assertEquals(selectedMonthAndYear.getYear(), expected.getYear());
        assertEquals(selectedMonthAndYear.getMonthOfYear(), testedMonth);
    }

    @Test
    public void yearAndMonthEditor_selectDateInFutureThenConfirm_dateWillChange() {
        int testedMonth = 6;
        int someYearsInFuture = 16;
        popup = calendar.getProxiedPopup();
        headerControls = popup.getProxiedHeaderControls();
        footerControls = popup.getProxiedFooterControls();
        footerControls.setTodaysDate();
        YearAndMonthEditor openedDateEditor = headerControls.openYearAndMonthEditor();
        assertTrue(openedDateEditor.isVisible());
        DateTime today = new DateTime();
        DateTime expected = today.plusYears(someYearsInFuture).withMonthOfYear(testedMonth);
        openedDateEditor.selectDate(expected).confirmDate();
        openedDateEditor = headerControls.openYearAndMonthEditor();
        DateTime selectedMonthAndYear = openedDateEditor.getDate();
        assertEquals(selectedMonthAndYear.getYear(), expected.getYear());
        assertEquals(selectedMonthAndYear.getMonthOfYear(), testedMonth);
    }

    @Test
    public void yearAndMonthEditor_selectDateThenCancel_dateWillNotChange() {
        int testedMonth = 6;
        int someYearsInFuture = 16;
        popup = calendar.getProxiedPopup();
        headerControls = popup.getProxiedHeaderControls();
        footerControls = popup.getProxiedFooterControls();
        assertTrue(headerControls.isVisible());
        assertTrue(footerControls.isVisible());
        footerControls.setTodaysDate();
        YearAndMonthEditor openedDateEditor = headerControls.openYearAndMonthEditor();
        assertTrue(openedDateEditor.isVisible());
        DateTime today = new DateTime();
        DateTime tested = today.plusYears(someYearsInFuture).withMonthOfYear(testedMonth);
        openedDateEditor.selectDate(tested).cancelDate();
        openedDateEditor = headerControls.openYearAndMonthEditor();
        DateTime selectedMonthAndYear = openedDateEditor.getDate();
        assertNotEquals(selectedMonthAndYear.getYear(), tested.getYear());
        assertNotEquals(selectedMonthAndYear.getMonthOfYear(), testedMonth);
        assertEquals(selectedMonthAndYear.getYear(), today.getYear());
        assertEquals(selectedMonthAndYear.getMonthOfYear(), today.getMonthOfYear());
    }
}
