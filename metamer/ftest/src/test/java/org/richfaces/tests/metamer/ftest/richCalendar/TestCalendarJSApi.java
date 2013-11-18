/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay;
import org.richfaces.fragment.calendar.HeaderControls;
import org.richfaces.fragment.calendar.PopupCalendar;
import org.richfaces.fragment.calendar.TimeEditor;
import org.richfaces.fragment.calendar.YearAndMonthEditor;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarJSApi extends AbstractCalendarTest {

    @FindBy(css = "[id$=value]")
    private WebElement gettersValue;
    @FindBy(id = "getValue")
    private WebElement getValue;
    @FindBy(id = "getValueAsString")
    private WebElement getValueAsString;
    @FindBy(id = "getCurrentMonth")
    private WebElement getCurrentMonth;
    @FindBy(id = "getCurrentYear")
    private WebElement getCurrentYear;

    @FindBy(id = "setValue")
    private WebElement setValue;
    @FindBy(id = "resetValue")
    private WebElement resetValue;
    @FindBy(id = "today")
    private WebElement today;
    @FindBy(id = "showSelectedDate")
    private WebElement showSelectedDate;

    @FindBy(id = "showPopup")
    private WebElement showPopup;
    @FindBy(id = "hidePopup")
    private WebElement hidePopup;
    @FindBy(id = "switchPopup")
    private WebElement switchPopup;
    @FindBy(id = "showDateEditor")
    private WebElement showDateEditor;
    @FindBy(id = "hideDateEditor")
    private WebElement hideDateEditor;
    @FindBy(id = "showTimeEditor")
    private WebElement showTimeEditor;
    @FindBy(id = "hideTimeEditor")
    private WebElement hideTimeEditor;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Test
    public void testGetCurrentMonth() {
        setTodaysDate();
        MetamerPage.waitRequest(getCurrentMonth, MetamerPage.WaitRequestType.NONE).click();
        assertEquals(getGettersValue(), String.valueOf(todayMidday.getMonthOfYear() - 1));
    }

    @Test
    public void testGetCurrentYear() {
        setTodaysDate();
        MetamerPage.waitRequest(getCurrentYear, MetamerPage.WaitRequestType.NONE).click();
        assertEquals(getGettersValue(), String.valueOf(todayMidday.getYear()));
    }

    @Test
    public void testGetValue() {
        String datePattern = "EEE MMM d yyyy HH:mm:ss";
        DateTimeFormatter dtf = DateTimeFormat.forPattern(datePattern);
        setTodaysDate();
        MetamerPage.waitRequest(getValue, MetamerPage.WaitRequestType.NONE).click();
        String date = getGettersValue();
        date = date.substring(0, date.lastIndexOf(":00") + 3);
        DateTime parsedDateTime = dtf.parseDateTime(date);
        assertEquals(parsedDateTime.getYear(), todayMidday.getYear());
        assertEquals(parsedDateTime.getMonthOfYear(), todayMidday.getMonthOfYear());
        assertEquals(parsedDateTime.getDayOfMonth(), todayMidday.getDayOfMonth());
    }

    @Test
    public void testGetValueAsString() {
        setTodaysDate();
        MetamerPage.waitRequest(getValueAsString, MetamerPage.WaitRequestType.NONE).click();
        assertEquals(getGettersValue(), popupCalendar.getInput().getStringValue());
    }

    @Test
    public void testResetValue() {
        setTodaysDate();
        CalendarDay selectedDay = popupCalendar.openPopup().getDayPicker().getSelectedDay();
        assertNotNull(selectedDay);
        popupCalendar.openPopup();
        executeJSFromElement(resetValue);
        selectedDay = popupCalendar.openPopup().getDayPicker().getSelectedDay();
        assertNull(selectedDay);
    }

    @Test
    public void testSetValue() {
        calendarAttributes.set(CalendarAttributes.showApplyButton, Boolean.FALSE);
        // setValue sets the date to 10 Oct of 2012
        MetamerPage.waitRequest(setValue, MetamerPage.WaitRequestType.XHR).click();
        CalendarDay selectedDay = popupCalendar.openPopup().getDayPicker().getSelectedDay();
        assertNotNull(selectedDay);
        assertEquals(selectedDay.getDayNumber().intValue(), 10);
    }

    @Test
    public void testShowAndHideDateEditor() {
        YearAndMonthEditor yearAndMonthEditor = popupCalendar.getPopup().getHeaderControls().getYearAndMonthEditor();
        executeJSFromElement(showDateEditor);
        yearAndMonthEditor.waitUntilIsVisible().perform();
        executeJSFromElement(hideDateEditor);
        yearAndMonthEditor.waitUntilIsNotVisible().perform();
    }

    @Test
    public void testShowAndHidePopup() {
        PopupCalendar popup = popupCalendar.getPopup();
        executeJSFromElement(showPopup);
        popup.waitUntilIsVisible().perform();
        executeJSFromElement(hidePopup);
        popup.waitUntilIsNotVisible().perform();
    }

    @Test
    public void testShowAndHideTimeEditor() {
        setTodaysDate();
        TimeEditor timeEditor = popupCalendar.getPopup().getFooterControls().getTimeEditor();
        executeJSFromElement(showTimeEditor);
        timeEditor.waitUntilIsVisible().perform();
        executeJSFromElement(hideTimeEditor);
        timeEditor.waitUntilIsNotVisible().perform();
    }

    @Test
    public void testShowSelectedDate() {
        setTodaysDate();
        HeaderControls headerControls = popupCalendar.openPopup().getHeaderControls();
        headerControls.nextMonth();
        headerControls.nextMonth();
        executeJSFromElement(showSelectedDate);
        DateTime yearAndMonth = headerControls.getYearAndMonth();
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.getMonthOfYear());
    }

    @Test
    public void testSwitchPopup() {
        PopupCalendar popup = popupCalendar.getPopup();
        executeJSFromElement(switchPopup);
        popup.waitUntilIsVisible().perform();
        executeJSFromElement(switchPopup);
        popup.waitUntilIsNotVisible().perform();
        executeJSFromElement(switchPopup);
        popup.waitUntilIsVisible().perform();
    }

    @Test
    public void testToday() {
        popupCalendar.openPopup();
        executeJSFromElement(today);
        CalendarDay selectedDay = popupCalendar.openPopup().getDayPicker().getSelectedDay();
        assertEquals(selectedDay.getDayNumber().intValue(), todayMidday.getDayOfMonth());
    }

    /**
     * Executes script, which is saved in attribute "onclick" or "onmouseover" of chosen element
     *
     * @param element
     *            chosen element
     * @return
     */
    private Object executeJSFromElement(WebElement element) {
        return executeJS(element.getAttribute("onclick") != null ? element.getAttribute("onclick") : element
            .getAttribute("onmouseover"));
    }

    private String getGettersValue() {
        Graphene.waitGui().until().element(gettersValue).attribute("value").not().equalTo("");
        return gettersValue.getAttribute("value");
    }

    private void setTodaysDate() {
        MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), MetamerPage.WaitRequestType.XHR)
            .setTodaysDate();
    }
}
