/**
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
 */
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.joda.time.DateTime;
import org.openqa.selenium.Dimension;
import org.richfaces.fragment.calendar.DayPicker;
import org.richfaces.fragment.calendar.PopupCalendar;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay;
import org.richfaces.fragment.calendar.PopupCalendar.PopupHeaderControls;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case for basic functionality of calendar on page faces/components/richCalendar/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarBasic extends AbstractCalendarTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @BeforeMethod
    private void manageWindow() {
        driver.manage().window().setSize(new Dimension(1024, 768));
    }

    @Test
    public void testApplyButton() {
        super.testApplyButton();
    }

    @Test
    public void testCleanButton() {
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        dayPicker.getWeek(3).getCalendarDays().get(3).select();
        MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.NONE).cleanDate();

        CalendarDay selectedDay = dayPicker.getSelectedDay();
        assertNull(selectedDay);
    }

    @Test
    public void testCloseButton() {
        PopupCalendar popup = popupCalendar.openPopup();
        assertTrue(popup.isVisible());

        MetamerPage.waitRequest(popupCalendar.openPopup().getHeaderControls(), WaitRequestType.NONE).closePopup();
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
        DateTime yearAndMonth = popupCalendar.openPopup().getHeaderControls().getYearAndMonth();
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.getMonthOfYear(), "Calendar shows wrong month in its header.");
        assertEquals(yearAndMonth.getYear(), todayMidday.getYear(), "Calendar shows wrong year in its header.");
    }

    @Test
    public void testInit() {
        assertTrue(popupCalendar.isVisible(), "Calendar is not present on the page.");
        assertTrue(new WebElementConditionFactory(popupCalendar.getInput().advanced().getInputElement()).isVisible().apply(driver), "Calendar's input should be visible.");
        assertTrue(new WebElementConditionFactory(popupCalendar.getPopupButtonElement()).isVisible().apply(driver), "Calendar's image should be visible.");
        assertTrue(popupCalendar.getPopupButtonElement().getTagName().equalsIgnoreCase("img"), "Calendar's image should be visible.");
        assertFalse(popupCalendar.getPopupButtonElement().getTagName().equalsIgnoreCase("button"), "Calendar's popup button should not be visible.");
        PopupCalendar popup = popupCalendar.openPopup();
        assertTrue(popup.isVisible());
        popupCalendar.closePopup();
        assertFalse(popup.isVisible());
    }

    @Test
    public void testNextMonthButton() {
        PopupHeaderControls headerControls = popupCalendar.openPopup().getHeaderControls();
        DateTime previousYearAndMonth = headerControls.getYearAndMonth();
        headerControls.nextMonth();
        DateTime yearAndMonth = headerControls.getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), previousYearAndMonth.plusMonths(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), previousYearAndMonth.plusMonths(1).getMonthOfYear(), "Month did not change correctly.");
    }

    @Test
    public void testNextYearButton() {
        PopupHeaderControls headerControls = popupCalendar.openPopup().getHeaderControls();
        DateTime previousYearAndMonth = headerControls.getYearAndMonth();
        headerControls.nextYear();
        DateTime yearAndMonth = headerControls.getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), previousYearAndMonth.plusYears(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), previousYearAndMonth.plusYears(1).getMonthOfYear(), "Month did not change correctly.");
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
        PopupHeaderControls headerControls = popupCalendar.openPopup().getHeaderControls();
        DateTime previousYearAndMonth = headerControls.getYearAndMonth();
        headerControls.previousMonth();
        DateTime yearAndMonth = headerControls.getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), previousYearAndMonth.minusMonths(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), previousYearAndMonth.minusMonths(1).getMonthOfYear(), "Month did not change correctly.");
    }

    @Test
    public void testPrevYearButton() {
        PopupHeaderControls headerControls = popupCalendar.openPopup().getHeaderControls();
        DateTime previousYearAndMonth = headerControls.getYearAndMonth();
        headerControls.previousYear();
        DateTime yearAndMonth = headerControls.getYearAndMonth();

        assertEquals(yearAndMonth.getYear(), previousYearAndMonth.minusYears(1).getYear(), "Year did not change correctly.");
        assertEquals(yearAndMonth.getMonthOfYear(), previousYearAndMonth.minusYears(1).getMonthOfYear(), "Month did not change correctly.");
    }

    @Test
    public void testSelectDate() {
        CalendarDay selectedDay = popupCalendar.openPopup().getDayPicker().getSelectedDay();
        assertNull(selectedDay);

        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        MetamerPage.waitRequest(dayPicker.getWeek(3).getCalendarDays().get(3), WaitRequestType.NONE).select();

        selectedDay = dayPicker.getSelectedDay();
        assertNotNull(selectedDay);
    }

    @Test
    public void testTodayButton() {
        MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();

        CalendarDay selectedDay = popupCalendar.openPopup().getDayPicker().getSelectedDay();
        CalendarDay todayDay = popupCalendar.openPopup().getDayPicker().getTodayDay();
        assertEquals(selectedDay.getDayNumber(), todayDay.getDayNumber());
    }

    @Test
    public void testTodayIsSelected() {
        CalendarDay todayDay = popupCalendar.openPopup().getDayPicker().getTodayDay();
        assertEquals(todayDay.getDayNumber().intValue(), todayMidday.getDayOfMonth());
    }

    @Test
    public void testWeekDaysLabels() {
        List<String> weekDayShortNames = popupCalendar.openPopup().getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
    }

    @Test
    public void testWeekNumbers() {
        MetamerPage.waitRequest(popupCalendar, WaitRequestType.XHR).setDateTime(firstOfJanuary2012);

        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        List<Integer> weeksNumbers = dayPicker.getWeeksNumbers();
        assertEquals(weeksNumbers, Arrays.asList(1, 2, 3, 4, 5, 6));
    }
}
