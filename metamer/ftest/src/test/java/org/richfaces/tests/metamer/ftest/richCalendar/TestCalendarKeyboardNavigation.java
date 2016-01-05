/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Keyboard;
import org.richfaces.fragment.calendar.PopupCalendar;
import org.richfaces.fragment.calendar.TimeEditor;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RegressionTest({ "https://issues.jboss.org/browse/RF-14078", "https://issues.jboss.org/browse/RF-14166" })
public class TestCalendarKeyboardNavigation extends AbstractCalendarTest {

    private static final String CLEAN_KEY = "c";
    private static final Keys CLOSE_POPUP_KEY = Keys.ESCAPE;
    private static final Keys NEXT_DAY_KEY = Keys.ARROW_RIGHT;
    private static final Keys NEXT_MONTH_KEY = Keys.PAGE_DOWN;
    private static final Keys NEXT_WEEK_KEY = Keys.ARROW_DOWN;
    private static final String NEXT_YEAR_KEY = Keys.chord(Keys.SHIFT, Keys.PAGE_DOWN);
    private static final String OPEN_TIME_EDITOR_KEY = "h";
    private static final Keys PREVIOUS_DAY_KEY = Keys.ARROW_LEFT;
    private static final Keys PREVIOUS_MONTH_KEY = Keys.PAGE_UP;
    private static final Keys PREVIOUS_WEEK_KEY = Keys.ARROW_UP;
    private static final String PREVIOUS_YEAR_KEY = Keys.chord(Keys.SHIFT, Keys.PAGE_UP);
    private static final Keys TIME_SPINNER_DECREASE_KEY = Keys.ARROW_DOWN;
    private static final Keys TIME_SPINNER_INCREASE_KEY = Keys.ARROW_UP;
    private static final String TODAY_KEY = "t";

    @ArquillianResource
    private Keyboard keyboard;

    private void assertCalendarInputEqualsTo(String expected) {
        assertEquals(popupCalendar.getInput().getStringValue(), expected);
    }

    private void assertHighlightedDay(PopupCalendar calendarPopup, Integer expectedDay) {
        assertEquals(calendarPopup.getDayPicker().getSelectedDay().getDayNumber(), expectedDay);
    }

    private void assertYearAndMonth(PopupCalendar calendarPopup, DateTime expectedYearAndMonth) {
        DateTime actualYearAndMonth = calendarPopup.getHeaderControls().getYearAndMonth();
        assertEquals(actualYearAndMonth.getMonthOfYear(), expectedYearAndMonth.getMonthOfYear());
        assertEquals(actualYearAndMonth.getYear(), expectedYearAndMonth.getYear());
    }

    @Override
    public String getComponentTestPagePath() {
        return "richCalendar/simple.xhtml";
    }

    private PopupCalendar setReferenceDate_openPopup_check() {
        // set reference date
        Graphene.guardAjax(popupCalendar).setDateTime(firstOfJanuary2012);
        //open popup
        PopupCalendar calendarPopup = popupCalendar.openPopup();
        // check actual position
        assertYearAndMonth(calendarPopup, firstOfJanuary2012);
        assertHighlightedDay(calendarPopup, 1);

        return calendarPopup;
    }

    @Test
    public void testClean() {
        // set some date, so we can clean it
        setReferenceDate_openPopup_check();
        assertCalendarInputEqualsTo("Jan 1, 2012 12:00");

        // invoke clean key
        keyboard.sendKeys(CLEAN_KEY);
        // apply
        Graphene.guardAjax(keyboard).sendKeys(Keys.ENTER);
        // the input should be empty
        assertCalendarInputEqualsTo("");
    }

    @Test
    public void testClose() {
        assertCalendarInputEqualsTo("");
        // open calendar
        popupCalendar.openPopup();
        // invoke close key
        keyboard.sendKeys(CLOSE_POPUP_KEY);
        popupCalendar.getPopup().waitUntilIsNotVisible().perform();
        // calendar value should stay unchanged
        assertCalendarInputEqualsTo("");

        // set some date
        setReferenceDate_openPopup_check();
        assertCalendarInputEqualsTo("Jan 1, 2012 12:00");
        // open popup
        popupCalendar.openPopup();
        // scroll to previous day
        keyboard.sendKeys(PREVIOUS_DAY_KEY);
        // invoke close key
        keyboard.sendKeys(CLOSE_POPUP_KEY);
        popupCalendar.getPopup().waitUntilIsNotVisible().perform();
        // calendar value should stay unchanged
        assertCalendarInputEqualsTo("Jan 1, 2012 12:00");
    }

    @Test
    public void testScrollingDaysAndWeeks() {
        PopupCalendar calendarPopup = setReferenceDate_openPopup_check();

        // verify days scrolling
        keyboard.sendKeys(PREVIOUS_DAY_KEY);
        assertHighlightedDay(calendarPopup, 31);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.minusDays(1));
        keyboard.sendKeys(PREVIOUS_DAY_KEY);
        assertHighlightedDay(calendarPopup, 30);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.minusDays(2));
        keyboard.sendKeys(NEXT_DAY_KEY);
        assertHighlightedDay(calendarPopup, 31);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.minusDays(1));
        keyboard.sendKeys(NEXT_DAY_KEY);
        assertHighlightedDay(calendarPopup, 1);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012);
        keyboard.sendKeys(NEXT_DAY_KEY);
        assertHighlightedDay(calendarPopup, 2);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusDays(1));

        // verify weeks scrolling
        keyboard.sendKeys(NEXT_WEEK_KEY);
        assertHighlightedDay(calendarPopup, 9);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusWeeks(1));
        keyboard.sendKeys(NEXT_WEEK_KEY);
        assertHighlightedDay(calendarPopup, 16);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusWeeks(2));
        keyboard.sendKeys(NEXT_WEEK_KEY);
        assertHighlightedDay(calendarPopup, 23);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusWeeks(3));
        keyboard.sendKeys(NEXT_WEEK_KEY);
        assertHighlightedDay(calendarPopup, 30);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusWeeks(4));
        keyboard.sendKeys(NEXT_WEEK_KEY);
        assertHighlightedDay(calendarPopup, 6);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusWeeks(5));
        keyboard.sendKeys(PREVIOUS_WEEK_KEY);
        assertHighlightedDay(calendarPopup, 30);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusWeeks(4));
        keyboard.sendKeys(PREVIOUS_WEEK_KEY);
        assertHighlightedDay(calendarPopup, 23);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusWeeks(3));

        // apply
        Graphene.guardAjax(keyboard).sendKeys(Keys.ENTER);
        assertCalendarInputEqualsTo("Jan 23, 2012 12:00");
    }

    @Test
    public void testScrollingMonths() {
        PopupCalendar calendarPopup = setReferenceDate_openPopup_check();

        // verify months scrolling
        keyboard.sendKeys(PREVIOUS_MONTH_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.minusMonths(1));
        keyboard.sendKeys(PREVIOUS_MONTH_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.minusMonths(2));
        keyboard.sendKeys(NEXT_MONTH_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.minusMonths(1));
        keyboard.sendKeys(NEXT_MONTH_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012);
        keyboard.sendKeys(NEXT_MONTH_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusMonths(1));

        // apply
        Graphene.guardAjax(keyboard).sendKeys(Keys.ENTER);
        assertCalendarInputEqualsTo("Feb 1, 2012 12:00");
    }

    @Test
    public void testScrollingYears() {
        PopupCalendar calendarPopup = setReferenceDate_openPopup_check();

        // verify years scrolling
        keyboard.sendKeys(NEXT_YEAR_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusYears(1));
        keyboard.sendKeys(NEXT_YEAR_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusYears(2));
        keyboard.sendKeys(PREVIOUS_YEAR_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.plusYears(1));
        keyboard.sendKeys(PREVIOUS_YEAR_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012);
        keyboard.sendKeys(PREVIOUS_YEAR_KEY);
        assertYearAndMonth(calendarPopup, firstOfJanuary2012.minusYears(1));

        // apply
        Graphene.guardAjax(keyboard).sendKeys(Keys.ENTER);
        assertCalendarInputEqualsTo("Jan 1, 2011 12:00");
    }

    @Test
    public void testTimeEditor() {
        String datePattern = "dd/M/yy hh:mm:ss a";
        calendarAttributes.set(CalendarAttributes.datePattern, datePattern);

        PopupCalendar calendarPopup = setReferenceDate_openPopup_check();
        assertCalendarInputEqualsTo("01/1/12 12:00:00 PM");

        keyboard.sendKeys(OPEN_TIME_EDITOR_KEY);
        TimeEditor timeEditor = calendarPopup.getFooterControls().getTimeEditor();
        timeEditor.waitUntilIsVisible().perform();

        // increase hours by 2
        keyboard.sendKeys(TIME_SPINNER_INCREASE_KEY);
        keyboard.sendKeys(TIME_SPINNER_INCREASE_KEY);
        // switch to minutes
        keyboard.sendKeys(Keys.TAB);
        // decrease minutes by 3
        keyboard.sendKeys(TIME_SPINNER_DECREASE_KEY);
        keyboard.sendKeys(TIME_SPINNER_DECREASE_KEY);
        keyboard.sendKeys(TIME_SPINNER_DECREASE_KEY);
        // switch to seconds
        keyboard.sendKeys(Keys.TAB);
        // increase seconds by 1
        keyboard.sendKeys(TIME_SPINNER_INCREASE_KEY);
        // switch to time sign
        keyboard.sendKeys(Keys.TAB);
        // change time sign
        keyboard.sendKeys(TIME_SPINNER_INCREASE_KEY);
        // switch to hours again
        keyboard.sendKeys(Keys.TAB);
        // increase hours by 1
        keyboard.sendKeys(TIME_SPINNER_INCREASE_KEY);
        // switch to minutes again
        keyboard.sendKeys(Keys.TAB);
        // switch to seconds again
        keyboard.sendKeys(Keys.TAB);
        // increase seconds by 1
        keyboard.sendKeys(TIME_SPINNER_INCREASE_KEY);
        // confirm
        keyboard.sendKeys(Keys.ENTER);
        // apply
        Graphene.guardAjax(keyboard).sendKeys(Keys.ENTER);
        assertCalendarInputEqualsTo("01/1/12 03:57:02 AM");
    }

    @Test
    public void testToday() {
        setReferenceDate_openPopup_check();

        keyboard.sendKeys(TODAY_KEY);
        assertHighlightedDay(popupCalendar.openPopup(), todayMidday.getDayOfMonth());
        // apply
        Graphene.guardAjax(keyboard).sendKeys(Keys.ENTER);
        assertCalendarInputEqualsTo(todayMidday.toString(DateTimeFormat.forPattern(calendarAttributes.get(CalendarAttributes.datePattern))));
    }
}
