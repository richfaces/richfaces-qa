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

import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.model.CalendarModel;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDay;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDay.DayType;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDays;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.DayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopup;
import org.testng.annotations.Test;

/**
 * Test case for basic functionality of calendar on page faces/components/richCalendar/dataModel.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichCalendarModel extends AbstractCalendarTest<MetamerPage> {

    private static final String datePattern = "MMM dd, yyyy";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/dataModel.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Calendar", "Data Model");
    }

    @Test(groups = { "4.0.0.Final" })
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1222")
    public void testApplyButton() {
        int wednesdays = 4;
        CalendarPopup popup = calendar.getPopup();
        DayPicker proxiedDayPicker = calendar.getPopup().getProxiedDayPicker();

        List<CalendarDay> wednesdayDays = proxiedDayPicker.getSpecificDays(wednesdays).removeSpecificDays(DayType.boundaryDay);
        CalendarDay dayToBeSelected = wednesdayDays.get(0);
        DateTime referenceDate = todayMidday.withDayOfMonth(dayToBeSelected.getDayNumber());
        MetamerPage.waitRequest(dayToBeSelected, WaitRequestType.XHR).select();
        Graphene.waitAjax().until(popup.isNotVisibleCondition());

        assertFalse(popup.isVisible(), "Popup should not be visible now.");
        DateTimeFormatter dtf = DateTimeFormat.forPattern(datePattern);
        DateTime selectedDate = dtf.parseDateTime(calendar.getInputValue());
        assertEquals(selectedDate.getYear(), referenceDate.getYear());
        assertEquals(selectedDate.getMonthOfYear(), referenceDate.getMonthOfYear());
        assertEquals(selectedDate.getDayOfMonth(), referenceDate.getDayOfMonth());
    }

    @Test(groups = { "4.0.0.Final" })
    @RegressionTest("https://issues.jboss.org/browse/RFPL-1222")
    public void testClasses() {
        int sundays = 1;
        int tuesdays = 3;
        int thursdays = 5;
        int saturdays = 7;

        DayPicker proxiedDayPicker = calendar.getPopup().getProxiedDayPicker();
        CalendarDays weekends = proxiedDayPicker.getSpecificDays(sundays);
        weekends.addAll(proxiedDayPicker.getSpecificDays(saturdays));
        weekends.removeSpecificDays(DayType.boundaryDay);
        for (CalendarDay day : weekends) {
            assertTrue(day.containsStyleClass(CalendarModel.WEEKEND_DAY_CLASS), "Weekends are not marked correctly (yellow color).");
            assertFalse(day.is(DayType.clickable), "Weekends should not be selectable.");
        }

        CalendarDays busyDays = proxiedDayPicker.getSpecificDays(tuesdays);
        busyDays.addAll(proxiedDayPicker.getSpecificDays(thursdays));
        busyDays.removeSpecificDays(DayType.boundaryDay);
        for (CalendarDay day : busyDays) {
            assertTrue(day.containsStyleClass(CalendarModel.BUSY_DAY_CLASS), "Tuesdays and thursdays should be busy (aqua color).");
            assertFalse(day.is(DayType.clickable), "Busy days should not be selectable.");
        }
    }
}
