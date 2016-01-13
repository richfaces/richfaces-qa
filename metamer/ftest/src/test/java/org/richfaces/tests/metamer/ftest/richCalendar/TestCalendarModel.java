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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Keyboard;
import org.richfaces.fragment.calendar.DayPicker;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay.DayType;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.model.CalendarModel;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * Test case for basic functionality of calendar on page faces/components/richCalendar/dataModel.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarModel extends AbstractCalendarTest {

    private static final String datePattern = "MMM dd, yyyy";
    private static final DateTimeFormatter DTF = DateTimeFormat.forPattern(datePattern);

    @ArquillianResource
    private Keyboard keyboard;

    private void checkSelectedDate(DateTime referenceDate) {
        DateTime selectedDate = DTF.parseDateTime(popupCalendar.getInput().getStringValue());
        assertEquals(selectedDate.getYear(), referenceDate.getYear());
        assertEquals(selectedDate.getMonthOfYear(), referenceDate.getMonthOfYear());
        assertEquals(selectedDate.getDayOfMonth(), referenceDate.getDayOfMonth());
    }

    @Override
    public String getComponentTestPagePath() {
        return "richCalendar/dataModel.xhtml";
    }

    private List<CalendarDay> getFewDaysFrom(List<CalendarDay> days, int count) {
        if (count < 1 && count > 10) {
            throw new IllegalArgumentException("The count can be only from interval <1;9>");
        }
        List<CalendarDay> result = Lists.newArrayList();

        result.add(days.get(0));
        result.add(days.get(days.size() - 1));
        result.add(days.get(days.size() / 2));

        result.add(days.get(1));
        result.add(days.get(days.size() / 2 - 1));
        result.add(days.get(days.size() - 2));

        result.add(days.get(2));
        result.add(days.get(days.size() / 2 + 1));
        result.add(days.get(days.size() - 3));

        return result.subList(0, count);
    }

    @Test
    @CoversAttributes("dataModel")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1222")
    @Templates(value = "plain")
    public void testClasses() {
        int sundays = 1;
        int tuesdays = 3;
        int thursdays = 5;
        int saturdays = 7;

        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        List<CalendarDay> weekends = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(sundays, saturdays)));
        for (CalendarDay day : weekends) {
            assertTrue(day.containsStyleClass(CalendarModel.WEEKEND_DAY_CLASS), "Weekends are not marked correctly (yellow color).");
            assertFalse(day.is(DayType.selectableDay), "Weekends should not be selectable.");
        }

        List<CalendarDay> busyDays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(tuesdays, thursdays)));
        for (CalendarDay day : busyDays) {
            assertTrue(day.containsStyleClass(CalendarModel.BUSY_DAY_CLASS), "Tuesdays and thursdays should be busy (aqua color).");
            assertFalse(day.is(DayType.selectableDay), "Busy days should not be selectable.");
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14199")
    public void testDisabledDayCannotBePickedByKeyboard() {
        DateTime referenceDate = new DateTime().withYear(2015).withMonthOfYear(11).withDayOfMonth(2).withHourOfDay(12).withMinuteOfHour(0);
        // set reference date
        Graphene.guardAjax(popupCalendar).setDateTime(referenceDate);
        performStabilizationWorkaround();

        popupCalendar.openPopup();
        checkSelectedDate(referenceDate);

        // try to select tuesday (disabled)
        keyboard.sendKeys(Keys.ARROW_RIGHT);
        keyboard.sendKeys(Keys.ENTER);
        assertTrue(popupCalendar.getPopup().isVisible());
        // check date is still the same (no move through enabled and already not selected days)
        checkSelectedDate(referenceDate);

        // try to select sunday (disabled)
        keyboard.sendKeys(Keys.ARROW_LEFT);
        keyboard.sendKeys(Keys.ARROW_LEFT);
        keyboard.sendKeys(Keys.ENTER);
        assertTrue(popupCalendar.getPopup().isVisible());
        // check date is still the same (no move through enabled and already not selected days)
        checkSelectedDate(referenceDate);

        // try to select saturday from previous month (disabled)
        Graphene.guardAjax(keyboard).sendKeys(Keys.ARROW_LEFT);// move to previous month will trigger an ajax request
        keyboard.sendKeys(Keys.ENTER);
        assertTrue(popupCalendar.getPopup().isVisible());
        // check date is still the same (no move through enabled and already not selected days)
        checkSelectedDate(referenceDate);

        // try to select thursday from previous year (disabled)
        Graphene.guardAjax(keyboard).sendKeys(Keys.chord(Keys.SHIFT, Keys.PAGE_UP));// move to previous year will trigger an ajax request
        keyboard.sendKeys(Keys.ARROW_LEFT);
        keyboard.sendKeys(Keys.ENTER);
        assertTrue(popupCalendar.getPopup().isVisible());
        // check selected date has changed (day moved to friday when the year was switched)
        // 2x to the left and 1x to previous year
        referenceDate = referenceDate.minusDays(2).minusYears(1);
        checkSelectedDate(referenceDate);

        // finally, select wednesday (enabled)
        Graphene.guardAjax(keyboard).sendKeys(Keys.ARROW_LEFT);
        keyboard.sendKeys(Keys.ENTER);
        popupCalendar.getPopup().waitUntilIsNotVisible().perform();

        referenceDate = referenceDate.withYear(2014).withMonthOfYear(10).withDayOfMonth(29);
        checkSelectedDate(referenceDate);
    }

    @Test
    @CoversAttributes("dataModel")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1222")
    public void testPickADate() {
        DateTime referenceDate = new DateTime();

        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();

        List<CalendarDay> enabledDays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(2, 4, 6)));
        List<CalendarDay> disabledDays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(1, 3, 5, 7)));

        // check disabled days
        for (CalendarDay day : getFewDaysFrom(disabledDays, 9)) {
            String previousValue = popupCalendar.getInput().getStringValue();
            day.getDayElement().click();
            assertEquals(popupCalendar.getInput().getStringValue(), previousValue);
        }

        // check enabled days
        for (CalendarDay day : getFewDaysFrom(enabledDays, 5)) {
            popupCalendar.openPopup();
            referenceDate = referenceDate.withDayOfMonth(day.getDayNumber());

            Graphene.guardAjax(day).select();
            popupCalendar.getPopup().waitUntilIsNotVisible().perform();
            performStabilizationWorkaround();
            checkSelectedDate(referenceDate);
        }
    }
}
