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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDay;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.DayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopup;
import org.testng.annotations.Test;

/**
 * Test case for basic functionality of calendar on page faces/components/richCalendar/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichCalendarBasic extends AbstractCalendarTest<MetamerPage> {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Test
    public void testApplyButton() {
        super.testApplyButton();
    }

    @Test
    public void testCleanButton() {
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        proxiedDayPicker.getWeek(3).getCalendarDays().get(3).select();
        MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.NONE).cleanDate();

        CalendarDay selectedDay = proxiedDayPicker.getSelectedDay();
        assertNull(selectedDay);
    }

    @Test
    public void testCloseButton() {
        CalendarPopup popup = calendar.openPopup();
        assertTrue(popup.isVisible());

        MetamerPage.waitRequest(calendar.openPopup().getHeaderControls(), WaitRequestType.NONE).closePopup();
        assertFalse(popup.isVisible());
    }

    @Test
    public void testFooterButtons() {
        super.testFooterButtons();
    }

    @Test
    public void testHeaderButtons() {
        super.testHeaderButtons();
    }

    @Test
    public void testHeaderMonth() {
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(todayMidday);

        DateTime yearAndMonth = calendar.openPopup().getHeaderControls().getYearAndMonth();
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.getMonthOfYear(), "Calendar shows wrong month in its header.");
        assertEquals(yearAndMonth.getYear(), todayMidday.getYear(), "Calendar shows wrong year in its header.");
    }

    @Test
    public void testInit() {
        assertTrue(calendar.isVisible(), "Calendar is not present on the page.");
        assertTrue(Graphene.element(calendar.getInput()).isVisible().apply(driver), "Calendar's input should be visible.");
        assertTrue(Graphene.element(calendar.getPopupButton()).isVisible().apply(driver), "Calendar's image should be visible.");
        assertTrue(calendar.getPopupButton().getTagName().equalsIgnoreCase("img"), "Calendar's image should be visible.");
        assertFalse(calendar.getPopupButton().getTagName().equalsIgnoreCase("button"), "Calendar's popup button should not be visible.");
        CalendarPopup popup = calendar.openPopup();
        assertTrue(popup.isVisible());
        calendar.closePopup();
        assertFalse(popup.isVisible());
    }

    @Test
    public void testNextMonthButton() {
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(todayMidday);

        calendar.openPopup().getHeaderControls().nextMonth();
        DateTime yearAndMonth = calendar.openPopup().getHeaderControls().getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), todayMidday.plusMonths(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.plusMonths(1).getMonthOfYear(), "Month did not change correctly.");
    }

    @Test
    public void testNextYearButton() {
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(todayMidday);

        calendar.openPopup().getHeaderControls().nextYear();
        DateTime yearAndMonth = calendar.openPopup().getHeaderControls().getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), todayMidday.plusYears(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.plusYears(1).getMonthOfYear(), "Month did not change correctly.");
    }

    @Test
    public void testOpenPopupClickOnImage() {
        super.testOpenPopupClickOnImage();
    }

    @Test
    public void testOpenPopupClickOnInput() {
        super.testOpenPopupClickOnInput();
    }

    @Test
    public void testPrevMonthButton() {
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(todayMidday);

        calendar.openPopup().getHeaderControls().previousMonth();
        DateTime yearAndMonth = calendar.openPopup().getHeaderControls().getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), todayMidday.minusMonths(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.minusMonths(1).getMonthOfYear(), "Month did not change correctly.");
    }

    @Test
    public void testPrevYearButton() {
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(todayMidday);

        calendar.openPopup().getHeaderControls().previousYear();
        DateTime yearAndMonth = calendar.openPopup().getHeaderControls().getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), todayMidday.minusYears(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.minusYears(1).getMonthOfYear(), "Month did not change correctly.");
    }

    @Test
    public void testSelectDate() {
        CalendarDay selectedDay = calendar.openPopup().getDayPicker().getSelectedDay();
        assertNull(selectedDay);

        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        MetamerPage.waitRequest(proxiedDayPicker.getWeek(3).getCalendarDays().get(3), WaitRequestType.NONE).select();

        selectedDay = proxiedDayPicker.getSelectedDay();
        assertNotNull(selectedDay);
    }

    @Test
    public void testTodayButton() {
        MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();

        CalendarDay selectedDay = calendar.openPopup().getDayPicker().getSelectedDay();
        CalendarDay todayDay = calendar.openPopup().getDayPicker().getTodayDay();
        assertEquals(selectedDay.getDayNumber(), todayDay.getDayNumber());
    }

    @Test
    public void testTodayIsSelected() {
        CalendarDay todayDay = calendar.openPopup().getDayPicker().getTodayDay();
        assertEquals(todayDay.getDayNumber().intValue(), todayMidday.getDayOfMonth());
    }

    @Test
    public void testWeekDaysLabels() {
        List<String> weekDayShortNames = calendar.openPopup().getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
    }

    @Test
    public void testWeekNumbers() {
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(firstOfJanuary2012);

        DayPicker dayPicker = calendar.openPopup().getDayPicker();
        List<Integer> weeksNumbers = dayPicker.getWeeksNumbers();
        assertEquals(weeksNumbers, Arrays.asList(1, 2, 3, 4, 5, 6));
    }
}
