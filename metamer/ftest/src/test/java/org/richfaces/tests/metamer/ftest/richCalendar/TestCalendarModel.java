/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.fragment.calendar.DayPicker;
import org.richfaces.fragment.calendar.PopupCalendar;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay.DayType;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
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

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/dataModel.xhtml");
    }

    @Override
    @Test
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1222")
    public void testApplyButton() {
        int wednesdays = 4;
        PopupCalendar popup = popupCalendar.getPopup();
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();

        List<CalendarDay> wednesdayDays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(wednesdays)));
        wednesdayDays.removeAll(dayPicker.getBoundaryDays());
        CalendarDay dayToBeSelected = wednesdayDays.get(0);
        DateTime referenceDate = todayMidday.withDayOfMonth(dayToBeSelected.getDayNumber());
        MetamerPage.waitRequest(dayToBeSelected, WaitRequestType.XHR).select();
        popup.waitUntilIsNotVisible().perform();

        assertFalse(popup.isVisible(), "Popup should not be visible now.");
        DateTimeFormatter dtf = DateTimeFormat.forPattern(datePattern);
        DateTime selectedDate = dtf.parseDateTime(popupCalendar.getInput().getStringValue());
        assertEquals(selectedDate.getYear(), referenceDate.getYear());
        assertEquals(selectedDate.getMonthOfYear(), referenceDate.getMonthOfYear());
        assertEquals(selectedDate.getDayOfMonth(), referenceDate.getDayOfMonth());
    }

    @Test
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
}
