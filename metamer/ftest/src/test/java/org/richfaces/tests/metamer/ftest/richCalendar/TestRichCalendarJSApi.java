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
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.calendarAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.calendar.common.HeaderControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDay;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth.YearAndMonthEditor;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopup;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichCalendarJSApi extends AbstractCalendarTest<MetamerPage> {

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
    //
    @FindBy(id = "setValue")
    private WebElement setValue;
    @FindBy(id = "resetValue")
    private WebElement resetValue;
    @FindBy(id = "today")
    private WebElement today;
    @FindBy(id = "showSelectedDate")
    private WebElement showSelectedDate;
    //
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
        getCurrentMonth.click();
        assertEquals(getGettersValue(), String.valueOf(todayMidday.getMonthOfYear() - 1));
    }

    @Test
    public void testGetCurrentYear() {
        setTodaysDate();
        getCurrentYear.click();
        assertEquals(getGettersValue(), String.valueOf(todayMidday.getYear()));
    }

    @Test
    public void testGetValue() {
        String datePattern = "EEE MMM d yyyy HH:mm:ss";
        DateTimeFormatter dtf = DateTimeFormat.forPattern(datePattern);
        setTodaysDate();
        getValue.click();
        String date=getGettersValue();
        date = date.substring(0, date.lastIndexOf(":00") + 3);
        DateTime parsedDateTime = dtf.parseDateTime(date);
        assertEquals(parsedDateTime.getYear(), todayMidday.getYear());
        assertEquals(parsedDateTime.getMonthOfYear(), todayMidday.getMonthOfYear());
        assertEquals(parsedDateTime.getDayOfMonth(), todayMidday.getDayOfMonth());
    }

    @Test
    public void testGetValueAsString() {
        setTodaysDate();
        getValueAsString.click();
        assertEquals(getGettersValue(), calendar.getInputValue());
    }

    @Test
    public void testResetValue() {
        setTodaysDate();
        CalendarDay selectedDay = calendar.openPopup().getDayPicker().getSelectedDay();
        assertNotNull(selectedDay);
        calendar.openPopup();
        executeJSFromElement(resetValue);
        selectedDay = calendar.openPopup().getDayPicker().getSelectedDay();
        assertNull(selectedDay);
    }

    @Test
    public void testSetValue() {
        //setValue sets the date to 10 Oct of 2012
        String datePattern = calendarAttributes.get(CalendarAttributes.datePattern);
        DateTimeFormatter dtf = DateTimeFormat.forPattern(datePattern);
        setValue.click();
        CalendarDay selectedDay = calendar.openPopup().getDayPicker().getSelectedDay();
        assertNotNull(selectedDay);
        assertEquals(selectedDay.getDayNumber().intValue(), 10);
    }

    @Test
    public void testShowAndHideDateEditor() {
        YearAndMonthEditor yearAndMonthEditor = calendar.getPopup().getHeaderControls().getYearAndMonthEditor();
        executeJSFromElement(showDateEditor);
        assertTrue(Graphene.waitGui().until(yearAndMonthEditor.isVisibleCondition()).booleanValue());
        executeJSFromElement(hideDateEditor);
        assertTrue(Graphene.waitGui().until(yearAndMonthEditor.isNotVisibleCondition()).booleanValue());
    }

    @Test
    public void testShowAndHidePopup() {
        CalendarPopup popup = calendar.getPopup();
        executeJSFromElement(showPopup);
        assertTrue(Graphene.waitGui().until(popup.isVisibleCondition()).booleanValue());
        executeJSFromElement(hidePopup);
        assertTrue(Graphene.waitGui().until(popup.isNotVisibleCondition()).booleanValue());
    }

    @Test
    public void testShowAndHideTimeEditor() {
        calendar.setDateTime(todayMidday.plusMonths(1));
        TimeEditor timeEditor = calendar.getPopup().getFooterControls().getTimeEditor();
        executeJSFromElement(showTimeEditor);
        assertTrue(Graphene.waitGui().until(timeEditor.isVisibleCondition()).booleanValue());
        executeJSFromElement(hideTimeEditor);
        assertTrue(Graphene.waitGui().until(timeEditor.isNotVisibleCondition()).booleanValue());
    }

    @Test
    public void testShowSelectedDate() {
        setTodaysDate();
        HeaderControls proxiedHeaderControls = calendar.openPopup().getProxiedHeaderControls();
        proxiedHeaderControls.nextMonth();
        proxiedHeaderControls.nextMonth();
        executeJSFromElement(showSelectedDate);
        DateTime yearAndMonth = proxiedHeaderControls.getYearAndMonth();
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.getMonthOfYear());
    }

    @Test
    public void testSwitchPopup() {
        CalendarPopup popup = calendar.getPopup();
        executeJSFromElement(switchPopup);
        assertTrue(Graphene.waitGui().until(popup.isVisibleCondition()).booleanValue());
        executeJSFromElement(switchPopup);
        assertTrue(Graphene.waitGui().until(popup.isNotVisibleCondition()).booleanValue());
        executeJSFromElement(switchPopup);
        assertTrue(Graphene.waitGui().until(popup.isVisibleCondition()).booleanValue());
    }

    @Test
    public void testToday() {
        calendar.openPopup();
        executeJSFromElement(today);
        CalendarDay selectedDay = calendar.openPopup().getDayPicker().getSelectedDay();
        assertEquals(selectedDay.getDayNumber().intValue(), todayMidday.getDayOfMonth());
    }

    /**
     * Executes script, which is saved in attribute "onclick" or "onmouseover" of chosen element
     * @param element chosen element
     * @return
     */
    private Object executeJSFromElement(WebElement element) {
        return executeJS(element.getAttribute("onclick") != null
                ? element.getAttribute("onclick")
                : element.getAttribute("onmouseover"));
    }

    private String getGettersValue() {
        Graphene.waitGui().until(Graphene.attribute(gettersValue, "value").not().valueEquals(""));
        return gettersValue.getAttribute("value");
    }

    private void setTodaysDate() {
        Graphene.guardXhr(calendar.openPopup().getFooterControls()).setTodaysDate();
    }
}
