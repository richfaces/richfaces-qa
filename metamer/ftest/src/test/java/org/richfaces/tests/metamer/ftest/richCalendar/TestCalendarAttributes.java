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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import com.google.common.collect.Iterables;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.PhaseId;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.calendar.common.HeaderControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDay;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDay.DayType;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDays;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarWeek;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.DayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor.SetValueBy;
import org.richfaces.tests.page.fragments.impl.calendar.popup.CalendarPopupComponent.OpenedBy;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.PopupFooterControls;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.PopupHeaderControls;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarAttributes extends AbstractCalendarTest {

    private DateTimeFormatter dateTimeFormatter;
    @Inject
    @Use(empty = false)
    private BoundaryDatesMode boundaryDatesMode;
    @Inject
    @Use(empty = false)
    private Direction direction;
    @Inject
    @Use(empty = false)
    private Mode mode;
    @Inject
    @Use(empty = false)
    private TodayControlMode todayControlMode;
    @Inject
    @Use(empty = false)
    private Boolean booleanValue;
    @FindBy(css = "input[id$=a4jButton]")
    private WebElement a4jbutton;
    @FindBy(css = "span[id$=msg]")
    private RichFacesMessage message;
    //
    private final Action setTimeAction = new SetTimeAction();
    private final Action setCurrentDateWithCalendarsTodayButtonAction = new SetCurrentDateWithCalendarsTodayButtonAction();
    private final Action setTodayAndThenClickToNextMonthAction = new SetTodayAndThenClickToNextMonthAction();
    private final DateTime firstOfNovember2012 = new DateTime(2012, 11, 1, 12, 0, 0, 0);

    private enum BoundaryDatesMode {

        NULL("null"),
        INACTIVE("inactive"),
        SCROLL("scroll"),
        SELECT("select");
        private final String value;

        private BoundaryDatesMode(String value) {
            this.value = value;
        }
    }

    private enum Direction {

        AUTO("auto"),
        TOPLEFT("topLeft"),
        TOPRIGHT("topRight"),
        BOTTOMLEFT("bottomLeft"),
        BOTTOMRIGHT("bottomRight"),
        NULL("null");
        private final String value;

        private Direction(String value) {
            this.value = value;
        }
    }

    private enum Mode {

        AJAX("ajax"),
        CLIENT("client"),
        NULL("null");
        private final String value;

        private Mode(String value) {
            this.value = value;
        }
    }

    private enum TodayControlMode {

        HIDDEN("hidden"),
        NULL("null"),
        SELECT("select"),
        SCROLL("scroll");
        private final String value;

        private TodayControlMode(String value) {
            this.value = value;
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @BeforeMethod
    public void initDateFormatter() {
        this.dateTimeFormatter = DateTimeFormat.forPattern(calendarAttributes.get(CalendarAttributes.datePattern));
    }

    @Test
    @Use(field = "boundaryDatesMode", enumeration = true)
    public void testBoundaryDatesMode() {
        calendarAttributes.set(CalendarAttributes.boundaryDatesMode, boundaryDatesMode.value);
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(firstOfNovember2012);
        PopupFooterControls proxiedFooterControls = calendar.openPopup().getProxiedFooterControls();
        HeaderControls proxiedHeaderControls = calendar.openPopup().getProxiedHeaderControls();
        DateTime yearAndMonth;
        String firstOfNovember2012String = firstOfNovember2012.toString(dateTimeFormatter);
        switch (boundaryDatesMode) {
            case INACTIVE:
            case NULL:
                MetamerPage.waitRequest(proxiedDayPicker.getBoundaryDays().get(0), WaitRequestType.NONE).select();
                //apply and check, that the date has not changed
                MetamerPage.waitRequest(proxiedFooterControls, WaitRequestType.NONE).applyDate();
                assertEquals(calendar.getInputValue(), firstOfNovember2012String);
                break;
            case SCROLL:
                //scroll to 28th of October 2012
                MetamerPage.waitRequest(proxiedDayPicker.getBoundaryDays().get(0), WaitRequestType.NONE).select();
                yearAndMonth = proxiedHeaderControls.getYearAndMonth();
                assertEquals(yearAndMonth.getYear(), 2012);
                assertEquals(yearAndMonth.getMonthOfYear(), 10);
                //apply and check, that the date has not changed
                MetamerPage.waitRequest(proxiedFooterControls, WaitRequestType.NONE).applyDate();
                assertEquals(calendar.getInputValue(), firstOfNovember2012String);
                break;
            case SELECT:
                //select 28th of October 2012
                MetamerPage.waitRequest(proxiedDayPicker.getBoundaryDays().get(0), WaitRequestType.NONE).select();
                yearAndMonth = proxiedHeaderControls.getYearAndMonth();
                assertEquals(yearAndMonth.getYear(), 2012);
                assertEquals(yearAndMonth.getMonthOfYear(), 10);

                MetamerPage.waitRequest(proxiedFooterControls, WaitRequestType.XHR).applyDate();
                DateTime parsedDateTime = dateTimeFormatter.parseDateTime(calendar.getInputValue());
                assertEquals(parsedDateTime.getYear(), 2012);
                assertEquals(parsedDateTime.getMonthOfYear(), 10);
                assertEquals(parsedDateTime.getDayOfMonth(), 28);
                break;
        }
    }

    @Test
    public void testButtonClass() {
        testStyleClass(calendar.getPopupButton(), BasicAttributes.buttonClass);
    }

    @Test
    public void testButtonClassLabel() {
        calendarAttributes.set(CalendarAttributes.buttonLabel, "label");
        testStyleClass(calendar.getPopupButton(), BasicAttributes.buttonClass);
    }

    @Test
    public void testButtonClassIcon() {
        calendarAttributes.set(CalendarAttributes.buttonIcon, "heart");
        testStyleClass(calendar.getPopupButton(), BasicAttributes.buttonClass);
    }

    @Test
    public void testButtonIcon() {
        calendarAttributes.set(CalendarAttributes.buttonIcon, "star");
        String src = calendar.getPopupButton().getAttribute("src");
        assertTrue(src.contains("star.png"), "Calendar's icon was not updated.");

        calendarAttributes.set(CalendarAttributes.buttonIcon, "null");
        src = calendar.getPopupButton().getAttribute("src");
        assertTrue(src.contains("calendarIcon.png"), "Calendar's icon was not updated.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10255")
    public void testButtonDisabledIcon() {
        calendarAttributes.set(CalendarAttributes.buttonDisabledIcon, "heart");
        calendarAttributes.set(CalendarAttributes.disabled, Boolean.TRUE);

        String src = calendar.getPopupButton().getAttribute("src");
        assertTrue(src.endsWith("heart.png"), "Calendar's icon was not updated.");

        calendarAttributes.set(CalendarAttributes.buttonDisabledIcon, "null");

        src = calendar.getPopupButton().getAttribute("src");
        assertTrue(src.contains("disabledCalendarIcon.png"), "Calendar's icon was not updated.");
    }

    @Test
    public void testButtonLabel() {
        calendarAttributes.set(CalendarAttributes.buttonLabel, "label");

        assertTrue(Graphene.element(calendar.getPopupButton()).isVisible().apply(driver), "Button should be displayed.");
        assertEquals(calendar.getPopupButton().getText(), "label", "Label of the button.");
        assertNotEquals(calendar.getPopupButton().getTagName(), "img", "Image should not be displayed.");

        calendarAttributes.set(CalendarAttributes.buttonIcon, "star");
        assertNotEquals(calendar.getPopupButton().getTagName(), "img", "Image should not be displayed.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11313")
    public void testConverterMessage() {
        String errorMsg = "conversion error";
        calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
        calendarAttributes.set(CalendarAttributes.converterMessage, errorMsg);

        calendar.getInput().sendKeys("RF 4");
        submitWithA4jSubmitBtn();
        Graphene.waitAjax().until(message.isVisibleCondition());

        assertEquals(message.getDetail(), errorMsg);
    }

    @Test
    public void testDatePattern() {
        String pattern = "hh:mm:ss a MMMM d, yyyy";
        calendarAttributes.set(CalendarAttributes.datePattern, pattern);

        setCurrentDateWithCalendarsTodayButtonAction.perform();
        String calendarInputText = calendar.getInputValue();
        dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        DateTime dt = null;
        try {
            dt = dateTimeFormatter.parseDateTime(calendarInputText);
        } catch (IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
        assertEquals(dt.getDayOfMonth(), todayMidday.getDayOfMonth());
        assertEquals(dt.getMonthOfYear(), todayMidday.getMonthOfYear());
        assertEquals(dt.getYear(), todayMidday.getYear());
    }

    @Test
    public void testDayClassFunction() {
        int tuesdayDay = 3;
        calendarAttributes.set(CalendarAttributes.dayClassFunction, "yellowTuesdays");
        //switch to next month to refresh classes
        calendar.openPopup().getHeaderControls().nextMonth();
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        CalendarDays tuesdays = proxiedDayPicker.getSpecificDays(tuesdayDay);
        tuesdays.removeSpecificDays(DayType.boundaryDay);

        for (CalendarDay tuesday : tuesdays) {
            assertTrue(tuesday.containsStyleClass("yellowDay"), "All tuesdays should be yellow.");
        }

        calendarAttributes.set(CalendarAttributes.dayClassFunction, "null");

        tuesdays = proxiedDayPicker.getSpecificDays(tuesdayDay);
        tuesdays.removeSpecificDays(DayType.boundaryDay);

        for (CalendarDay tuesday : tuesdays) {
            assertFalse(tuesday.containsStyleClass("yellowDay"), "All tuesdays should not be yellow.");
        }
    }

    @Test
    public void testDayDisableFunction() {
        calendarAttributes.set(CalendarAttributes.dayDisableFunction, "disableTuesdays");
        int tuesdayDay = 3;
        //switch to next month to refresh classes
        calendar.openPopup().getHeaderControls().nextMonth();
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        CalendarDays tuesdays = proxiedDayPicker.getSpecificDays(tuesdayDay);
        tuesdays.removeSpecificDays(DayType.boundaryDay);
        for (CalendarDay tuesday : tuesdays) {
            assertFalse(tuesday.is(DayType.clickable), "All tuesdays should not be enabled.");
        }

        calendarAttributes.set(CalendarAttributes.dayDisableFunction, "null");

        tuesdays = proxiedDayPicker.getSpecificDays(tuesdayDay);
        tuesdays.removeSpecificDays(DayType.boundaryDay);

        for (CalendarDay tuesday : tuesdays) {
            assertTrue(tuesday.is(DayType.clickable), "All tuesdays should be enabled.");
        }
    }

    @Test
    @Use(field = "direction", enumeration = true)
    public void testDirection() {
        int tolerance = 4;
        calendarAttributes.set(CalendarAttributes.direction, direction.value);
        calendarAttributes.set(CalendarAttributes.jointPoint, Direction.BOTTOMLEFT.value);
        //scrolls page down
        Point locationInput, locationPopup = null;
        locationInput = calendar.getLocations().getBottomLeft();
        Locations popupLocations = calendar.openPopup().getLocations();

        switch (direction) {
            case TOPLEFT:
                locationPopup = popupLocations.getBottomRight();
                break;
            case TOPRIGHT:
                locationPopup = popupLocations.getBottomLeft();
                break;
            case BOTTOMLEFT:
                locationPopup = popupLocations.getTopRight();
                break;
            case NULL:
            case AUTO:// auto (direction depends on browser/screen resolution)
            case BOTTOMRIGHT:
                locationPopup = popupLocations.getTopLeft();
                break;

        }
        tolerantAssertLocations(locationPopup, locationInput, tolerance);
    }

    @Test
    public void testDefaultLabel() {
        String defaultLabel = "RichFaces 4";
        calendarAttributes.set(CalendarAttributes.defaultLabel, defaultLabel);
        assertEquals(calendar.getInputValue(), defaultLabel);
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-9837", "https://issues.jboss.org/browse/RF-10085" })
    public void testDefaultTime() {
        final String t = "06:06";
        calendarAttributes.set(CalendarAttributes.defaultTime, t);
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        String text = calendar.openPopup().getFooterControls().getTimeEditorOpenerElement().getText();
        assertTrue(text.equals(t), "Default time");

        //another check in time editor
        TimeEditor timeEditor = calendar.openPopup().getFooterControls().openTimeEditor();
        DateTime setTime = timeEditor.getTime();
        DateTime reference = todayMidday.withHourOfDay(6).withMinuteOfHour(6);
        assertEquals(setTime.getHourOfDay(), reference.getHourOfDay());
        assertEquals(setTime.getMinuteOfHour(), reference.getMinuteOfHour());
    }

    @Test
    public void testDisabled() {
        calendarAttributes.set(CalendarAttributes.disabled, Boolean.TRUE);
        assertTrue(Graphene.attribute(calendar.getInput(), "disabled").valueEquals("true").apply(driver));

        //Popup should not be displayed
        int catched = 0;
        try {
            Graphene.guardNoRequest(calendar).openPopup(OpenedBy.INPUT_CLICKING);
        } catch (TimeoutException ex) {
            catched++;
        }
        try {
            Graphene.guardNoRequest(calendar).openPopup(OpenedBy.OPEN_BUTTON_CLICKING);
        } catch (TimeoutException ex) {
            catched++;
        }
        assertTrue(catched == 2);
    }

    @Test
    public void testEnableManualInput() {
        assertTrue(Graphene.attribute(calendar.getInput(), "readonly").valueEquals("true").apply(driver));

        calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
        assertTrue(Graphene.attribute(calendar.getInput(), "readonly").not().isPresent().apply(driver), "Readonly attribute of input should not be defined.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9646")
    public void testFirstWeekDay() {
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        List<String> weekDays = Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

        assertEquals(proxiedDayPicker.getWeekDayShortNames(), weekDays);

        // wrong input, nothing changes, RF-9646
        calendarAttributes.set(CalendarAttributes.firstWeekDay, 7);
        assertEquals(proxiedDayPicker.getWeekDayShortNames(), weekDays);

        calendarAttributes.set(CalendarAttributes.firstWeekDay, 1);
        Collections.rotate(weekDays, -1);
        assertEquals(proxiedDayPicker.getWeekDayShortNames(), weekDays);
    }

    @Test
    public void testHorizontalOffset() {
        int tolerance = 3;
        int horizontalOffset = 15;

        // should help stabilizing test on Jenkins
        driver.manage().window().setSize(new Dimension(1280, 960));

        Locations before = calendar.openPopup().getLocations();
        calendarAttributes.set(CalendarAttributes.horizontalOffset, horizontalOffset);
        new Actions(driver).moveToElement(page.fullPageRefreshIcon).build().perform();

        Locations after = calendar.openPopup().getLocations();

        Iterator<Point> itAfter = after.iterator();
        Locations movedFromBefore = before.moveAllBy(horizontalOffset, 0);
        Iterator<Point> itMovedBefore = movedFromBefore.iterator();
        //FIXME delete these logs after test method stabilized
        System.out.println(after);
        System.out.println(movedFromBefore);
        while (itAfter.hasNext()) {
            tolerantAssertLocations(itAfter.next(), itMovedBefore.next(), tolerance);
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10821")
    public void testImmediate() {
        calendarAttributes.set(CalendarAttributes.immediate, Boolean.TRUE);
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: null -> " + calendar.getInputValue());
        page.assertPhases(PhaseId.ANY_PHASE);
    }

    @Test
    public void testInputClass() {
        testStyleClass(calendar.getInput(), BasicAttributes.inputClass);
    }

    @Test
    public void testInputSize() {
        calendarAttributes.set(CalendarAttributes.inputSize, "30");
        assertTrue(Graphene.attribute(calendar.getInput(), "size").valueEquals("30").apply(driver), "Size attribute of input should be defined.");
    }

    @Test
    public void testInputStyle() {
        testStyle(calendar.getInput(), BasicAttributes.inputStyle);
    }

    @Test
    @Use(field = "direction", enumeration = true)
    public void testJointPoint() {
        int tolerance = 4;
        calendarAttributes.set(CalendarAttributes.jointPoint, direction.value);
        Locations inputLocations = calendar.getLocations();
        Point locationInput = null;
        Point locationPopup = calendar.openPopup().getLocations().getTopLeft();
        switch (direction) {
            case NULL:
            case AUTO:
            // auto (direction depends on browser/screen resolution)
            case BOTTOMLEFT:
                locationInput = inputLocations.getBottomLeft();
                break;
            case BOTTOMRIGHT:
                locationInput = inputLocations.getBottomRight();
                break;
            case TOPLEFT:
                locationInput = inputLocations.getTopLeft();
                break;
            case TOPRIGHT:
                locationInput = inputLocations.getTopRight();
                break;
        }
        tolerantAssertLocations(locationInput, locationPopup, tolerance);
    }

    @Test
    public void testLocale() {
        String locale = "ru";
        calendarAttributes.set(CalendarAttributes.locale, locale);
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        List<String> weekDayShortNames = proxiedDayPicker.getWeekDayShortNames();
        List<String> expectedShortNames = Arrays.asList("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб");
        assertEquals(weekDayShortNames, expectedShortNames);

        setCurrentDateWithCalendarsTodayButtonAction.perform();
        DateTime parsedDateTime = dateTimeFormatter.withLocale(new Locale(locale)).parseDateTime(calendar.getInputValue());

        assertEquals(parsedDateTime.getDayOfMonth(), todayMidday.getDayOfMonth(), "Input doesn't contain selected date.");
        assertEquals(parsedDateTime.getMonthOfYear(), todayMidday.getMonthOfYear(), "Input doesn't contain selected date.");
        assertEquals(parsedDateTime.getYear(), todayMidday.getYear(), "Input doesn't contain selected date.");
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12552")
    @Test(groups = "4.Future")
    public void testMinDaysInFirstWeek() {
        calendarAttributes.set(CalendarAttributes.minDaysInFirstWeek, 1);
        //1.1.2011 starts with saturday => only 1 day in first weak
        DateTime firstOf2011 = firstOfJanuary2012.withYear(2011);
        calendar.setDateTime(firstOf2011);
        CalendarWeek firstWeek = calendar.openPopup().getDayPicker().getWeek(1);
        List<CalendarDay> days = firstWeek.getCalendarDays();
        days.removeAll(calendar.openPopup().getDayPicker().getBoundaryDays());

        assertTrue(days.size() >= 1, "The first week should contain at least 1 day");

        calendarAttributes.set(CalendarAttributes.minDaysInFirstWeek, 2);
        calendar.setDateTime(firstOf2011);
        firstWeek = calendar.openPopup().getDayPicker().getWeek(1);
        days = firstWeek.getCalendarDays();
        days.removeAll(calendar.openPopup().getDayPicker().getBoundaryDays());

        assertTrue(days.size() >= 2, "The first week should contain at least 2 days");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testMode() {
        calendarAttributes.set(CalendarAttributes.mode, mode.value);
        HeaderControls proxiedHeaderControls = calendar.openPopup().getProxiedHeaderControls();
        switch (mode) {
            case AJAX:
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.XHR).nextMonth();
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.XHR).nextYear();
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.XHR).previousMonth();
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.XHR).previousYear();
                break;
            case CLIENT:
            case NULL:
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.NONE).nextMonth();
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.NONE).nextYear();
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.NONE).previousMonth();
                MetamerPage.waitRequest(proxiedHeaderControls, WaitRequestType.NONE).previousYear();
                break;
        }
    }

    @Test
    public void testMonthLabels() {
        String labelsString = "január, február, marec, apríl, máj, jún, júl, august, september, október, november, december";
        calendarAttributes.set(CalendarAttributes.monthLabels, labelsString);

        //set date to 1st day of year
        calendar.setDateTime(todayMidday.withMonthOfYear(1).withDayOfMonth(1));

        List<String> expectedLabels = Arrays.asList(labelsString.split(", "));
        HeaderControls proxiedHeaderControls = calendar.openPopup().getProxiedHeaderControls();
        List<String> monthsLabels = new ArrayList<String>(12);
        for (int i = 0; i < 12; i++) {
            String monthAndYear = proxiedHeaderControls.getYearAndMonthEditorOpenerElement().getText();
            String month = monthAndYear.substring(0, monthAndYear.indexOf(","));
            monthsLabels.add(month);
            proxiedHeaderControls.nextMonth();
        }
        assertEquals(monthsLabels, expectedLabels, "Month label in calendar");
    }

    @Test
    public void testMonthLabelsShort() {
        String labelsString = "jan, feb, mar, apr, máj, jún, júl, aug, sep, okt, nov, dec";
        calendarAttributes.set(CalendarAttributes.monthLabelsShort, labelsString);

        List<String> expectedLabels = Arrays.asList(labelsString.split(", "));
        List<String> shortMonthsLabels = calendar.openPopup().getHeaderControls()
                .openYearAndMonthEditor().getShortMonthsLabels();

        assertEquals(shortMonthsLabels, expectedLabels, "Month label in calendar");
    }

    @Test
    public void testOnTimeSelect() {
        testFireEvent(calendarAttributes, CalendarAttributes.ontimeselect,
                setTimeAction);
    }

    @Test
    public void testOnbeforetimeselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.onbeforetimeselect,
                setTimeAction);
    }

    @Test
    public void testOnchange() {
        testFireEvent(calendarAttributes, CalendarAttributes.onchange,
                setCurrentDateWithCalendarsTodayButtonAction);
    }

    @Test
    public void testOnclean() {
        testFireEvent(calendarAttributes, CalendarAttributes.onclean, new Action() {
            @Override
            public void perform() {
                PopupFooterControls proxiedFooterControls = calendar.openPopup()
                        .getProxiedFooterControls();
                MetamerPage.waitRequest(proxiedFooterControls, WaitRequestType.NONE).todayDate();
                Graphene.waitGui().until(Graphene.element(proxiedFooterControls
                        .getCleanButtonElement()).isVisible());
                MetamerPage.waitRequest(proxiedFooterControls, WaitRequestType.NONE).cleanDate();
            }
        });
    }

    @Test
    public void testOncomplete() {
        calendarAttributes.set(CalendarAttributes.mode, "ajax");
        testFireEvent(calendarAttributes, CalendarAttributes.oncomplete, new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(calendar.openPopup().getHeaderControls(), WaitRequestType.XHR).nextMonth();
            }
        });
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12505")
    @Test(groups = "4.Future")
    public void testOncurrentdateselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.oncurrentdateselect, setTodayAndThenClickToNextMonthAction);
    }

    @Test
    public void testOndatemouseout() {
        testFireEvent(calendarAttributes, CalendarAttributes.ondatemouseout, new Action() {
            @Override
            public void perform() {
                fireEvent(calendar.openPopup().getDayPicker().getTodayDay().getElement(), Event.MOUSEOUT);
            }
        });
    }

    @Test
    public void testOndatemouseover() {
        testFireEvent(calendarAttributes, CalendarAttributes.ondatemouseover, new Action() {
            @Override
            public void perform() {
                fireEvent(calendar.openPopup().getDayPicker().getTodayDay().getElement(), Event.MOUSEOVER);
            }
        });
    }

    @Test
    public void testOndateselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.ondateselect,
                setCurrentDateWithCalendarsTodayButtonAction);
    }

    @Test
    public void testOnhide() {
        testFireEvent(calendarAttributes, CalendarAttributes.onhide,
                new Actions(driver).click(calendar.getInput()).click(calendar.getInput()).build());
    }

    @Test
    public void testOninputblur() {
        //this throws the condition 2x
        //testFireEventWithJS(calendar.getInput(), Event.BLUR, calendarAttributes, CalendarAttributes.oninputblur);
        testFireEvent(calendarAttributes, CalendarAttributes.oninputblur,
                new Actions(driver).click(calendar.getInput()).click(page.requestTime).build());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9602")
    public void testOninputchange() {
        calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
        testFireEvent(calendarAttributes, CalendarAttributes.oninputchange, new Action() {
            @Override
            public void perform() {
                calendar.getInput().sendKeys("0");
                submitWithA4jSubmitBtn();
            }
        });
    }

    @Test
    public void testOninputclick() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputclick,
                new Actions(driver).click(calendar.getInput()).build());
    }

    @Test
    public void testOninputdblclick() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputdblclick,
                new Actions(driver).doubleClick(calendar.getInput()).build());
    }

    @Test
    public void testOninputfocus() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputfocus,
                new Actions(driver).click(calendar.getInput()).build());
    }

    @Test
    public void testOninputkeydown() {
        testFireEventWithJS(calendar.getInput(), Event.KEYDOWN, calendarAttributes, CalendarAttributes.oninputkeydown);
    }

    @Test
    public void testOninputkeypress() {
        testFireEventWithJS(calendar.getInput(), Event.KEYPRESS, calendarAttributes, CalendarAttributes.oninputkeypress);
    }

    @Test
    public void testOninputkeyup() {
        testFireEventWithJS(calendar.getInput(), Event.KEYUP, calendarAttributes, CalendarAttributes.oninputkeyup);
    }

    @Test
    public void testOninputmousedown() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmousedown,
                new Actions(driver).clickAndHold(calendar.getInput()).build());
    }

    @Test
    public void testOninputmousemove() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmousemove,
                new Actions(driver).moveToElement(calendar.getInput()).build());
    }

    @Test
    public void testOninputmouseout() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmouseout,
                new Actions(driver).click(calendar.getInput()).moveToElement(page.requestTime).build());
    }

    @Test
    public void testOninputmouseover() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmouseover,
                new Actions(driver).moveToElement(calendar.getInput()).build());
    }

    @Test
    public void testOninputmouseup() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmouseup,
                new Actions(driver).click(calendar.getInput()).build());
    }

    @Test
    public void testOninputselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmouseup,
                new Actions(driver).click(calendar.getInput()).build());
    }

    @Test
    public void testOnshow() {
        testFireEvent(calendarAttributes, CalendarAttributes.onshow, new Action() {
            @Override
            public void perform() {
                calendar.openPopup();
            }
        });
    }

    @Test
    public void testOnbeforecurrentdateselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.onbeforecurrentdateselect, setTodayAndThenClickToNextMonthAction);
    }

    @Test
    public void testOnbeforedateselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.onbeforedateselect, setCurrentDateWithCalendarsTodayButtonAction);
    }

    @Test
    public void testPopup() {
        calendarAttributes.set(CalendarAttributes.popup, Boolean.FALSE);
        assertTrue(inlineCalendar.isVisible(), "Inline calendar should be visible.");
        assertListOfWebElementsNotVisible(Arrays.asList(calendar.getInput(), calendar.getPopupButton()));
    }

    @Test
    public void testPopupClass() {
        testHTMLAttribute(calendar.openPopup().getRoot(), calendarAttributes, CalendarAttributes.popupClass, "metamer-ftest-class");
    }

    @Test
    public void testPopupStyle() {
        testHTMLAttribute(calendar.openPopup().getRoot(), calendarAttributes, CalendarAttributes.popupStyle, "background-color: yellow; font-size: 1.5em;");
    }

    @Test
    public void testRendered() {
        calendarAttributes.set(CalendarAttributes.rendered, Boolean.FALSE);
        assertFalse(calendar.isVisible());
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testRequired() {
        calendarAttributes.set(CalendarAttributes.required, booleanValue);
        submitWithA4jSubmitBtn();
        if (booleanValue) {
            assertTrue(message.isVisible());
            assertEquals(message.getDetail(), "value is required");
        } else {
            assertFalse(message.isVisible());
        }
    }

    @Test
    public void testRequiredMessage() {
        String msg = "RichFaces 4";
        calendarAttributes.set(CalendarAttributes.requiredMessage, msg);
        calendarAttributes.set(CalendarAttributes.required, Boolean.TRUE);
        submitWithA4jSubmitBtn();

        assertTrue(message.isVisible());
        assertEquals(message.getDetail(), msg);
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testResetTimeOnDateSelect() {
        calendarAttributes.set(CalendarAttributes.resetTimeOnDateSelect, booleanValue);
        int minutes = 33;

        //set yesterday with some minutes
        MetamerPage.waitRequest(calendar, WaitRequestType.XHR).setDateTime(todayMidday.plusMinutes(minutes).minusDays(1));
        //second time, but without minutes setting, to see if the minutes will reset
        setCurrentDateWithCalendarsTodayButtonAction.perform();

        int minutesAfterReseting = dateTimeFormatter.parseDateTime(calendar.getInputValue()).getMinuteOfHour();

        if (booleanValue) {
            assertEquals(minutesAfterReseting, 0);
        } else {
            assertEquals(minutesAfterReseting, minutes);
        }
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testShowApplyButton() {
        calendarAttributes.set(CalendarAttributes.showApplyButton, booleanValue);
        PopupFooterControls proxiedFooterControls = calendar.openPopup().getProxiedFooterControls();
        if (booleanValue) {
            assertVisible(proxiedFooterControls.getApplyButtonElement());
        } else {
            assertNotVisible(proxiedFooterControls.getApplyButtonElement());
            setCurrentDateWithCalendarsTodayButtonAction.perform();
            DateTime inputTime = dateTimeFormatter.parseDateTime(calendar.getInputValue());
            assertEquals(inputTime.getDayOfMonth(), todayMidday.getDayOfMonth());
            assertEquals(inputTime.getMonthOfYear(), todayMidday.getMonthOfYear());
            assertEquals(inputTime.getYear(), todayMidday.getYear());
        }
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testShowFooter() {
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        calendarAttributes.set(CalendarAttributes.showFooter, booleanValue);
        PopupFooterControls proxiedFooterControls = calendar.openPopup().getProxiedFooterControls();
        if (booleanValue) {
            assertListOfWebElementsVisible(Arrays.asList(
                    proxiedFooterControls.getApplyButtonElement(),
                    proxiedFooterControls.getCleanButtonElement(),
                    proxiedFooterControls.getTimeEditorOpenerElement(),
                    proxiedFooterControls.getTodayButtonElement()));

        } else {
            assertListOfWebElementsNotVisible(Arrays.asList(
                    proxiedFooterControls.getApplyButtonElement(),
                    proxiedFooterControls.getCleanButtonElement(),
                    proxiedFooterControls.getTimeEditorOpenerElement(),
                    proxiedFooterControls.getTodayButtonElement()));
        }
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testShowHeader() {
        calendarAttributes.set(CalendarAttributes.showHeader, booleanValue);
        PopupHeaderControls proxiedHeaderControls = calendar.openPopup().getProxiedHeaderControls();
        if (booleanValue) {
            assertListOfWebElementsVisible(Arrays.asList(
                    proxiedHeaderControls.getCloseButtonElement(),
                    proxiedHeaderControls.getNextMonthElement(),
                    proxiedHeaderControls.getNextYearElement(),
                    proxiedHeaderControls.getPreviousMonthElement(),
                    proxiedHeaderControls.getPreviousYearElement(),
                    proxiedHeaderControls.getYearAndMonthEditorOpenerElement()));
        } else {
            assertListOfWebElementsNotVisible(Arrays.asList(
                    proxiedHeaderControls.getCloseButtonElement(),
                    proxiedHeaderControls.getNextMonthElement(),
                    proxiedHeaderControls.getNextYearElement(),
                    proxiedHeaderControls.getPreviousMonthElement(),
                    proxiedHeaderControls.getPreviousYearElement(),
                    proxiedHeaderControls.getYearAndMonthEditorOpenerElement()));
        }
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testShowInput() {
        calendarAttributes.set(CalendarAttributes.showInput, booleanValue);
        if (booleanValue) {
            assertVisible(calendar.getInput());
        } else {
            assertNotVisible(calendar.getInput());
        }
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testShowWeekDaysBar() {
        calendarAttributes.set(CalendarAttributes.showWeekDaysBar, booleanValue);
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        if (booleanValue) {
            assertVisible(proxiedDayPicker.getWeekDaysBarElement());
        } else {
            assertNotVisible(proxiedDayPicker.getWeekDaysBarElement());
        }
    }

    @Test
    @Use(field = "booleanValue", booleans = { false, true })
    public void testShowWeeksBar() {
        calendarAttributes.set(CalendarAttributes.showWeeksBar, booleanValue);
        DayPicker proxiedDayPicker = calendar.openPopup().getProxiedDayPicker();
        if (booleanValue) {
            assertVisible(proxiedDayPicker.getWeek(1).getWeekNumberElement());
        } else {
            assertNotVisible(proxiedDayPicker.getWeek(1).getWeekNumberElement());
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9655")
    public void testStyle() {
        testStyle(calendar.getRoot());
    }

    @Test
    public void testStyleClass() {
        testStyleClass(calendar.getRoot());
    }

    @Test
    @Templates(exclude = "richPopupPanel")
    public void testTabindexInput() {
        testHTMLAttribute(calendar.getInput(), calendarAttributes, CalendarAttributes.tabindex, "101");
    }

    @Test(groups = { "4.3" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "richPopupPanel")
    public void testTabindexInputInPopupPanel() {
        testHTMLAttribute(calendar.getInput(), calendarAttributes, CalendarAttributes.tabindex, "101");
    }

    @Test
    public void testTabindexButton() {
        testHTMLAttribute(calendar.getPopupButton(), calendarAttributes, CalendarAttributes.tabindex, "101");
    }

    @Test
    @Use(field = "todayControlMode", enumeration = true)
    public void testTodayControlMode() {
        calendarAttributes.set(CalendarAttributes.todayControlMode, todayControlMode.value);
        switch (todayControlMode) {
            case HIDDEN:
                assertNotVisible(calendar.openPopup().getFooterControls().getTodayButtonElement());
                break;
            case NULL:
            case SELECT:
                //set date to tomorrow
                calendar.setDateTime(todayMidday.plusDays(1));
                //set date with calendar's 'Today' button,
                //this will scroll and select todays day
                MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
                CalendarDay selectedDay = calendar.openPopup().getDayPicker().getSelectedDay();
                assertNotNull(selectedDay);
                assertTrue(selectedDay.is(DayType.todayDay));
                break;
            case SCROLL:
                calendar.setDateTime(todayMidday.plusMonths(1));
                //set date with calendar's 'Today' button,
                //this will only scroll to today but will not select it
                MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
                //no selected day should be in calendar
                assertNull(calendar.openPopup().getDayPicker().getSelectedDay());
                //but view of day picker should will change to current month
                assertEquals(calendar.openPopup().getHeaderControls().getYearAndMonth().getMonthOfYear(), todayMidday.getMonthOfYear());
                break;
        }
    }

    @Test
    public void testValueChangeListener() {
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> " + calendar.getInputValue());
    }

    @Test
    public void testVerticalOffset() {
        int tolerance = 3;
        int verticalOffset = 15;

        // should help stabilizing test on Jenkins
        driver.manage().window().setSize(new Dimension(1280, 960));

        Locations before = calendar.openPopup().getLocations();
        calendarAttributes.set(CalendarAttributes.verticalOffset, verticalOffset);
        new Actions(driver).moveToElement(page.fullPageRefreshIcon).build().perform();

        Locations after = calendar.openPopup().getLocations();
        Locations movedFromBefore = before.moveAllBy(0, verticalOffset);
        Iterator<Point> itAfter = after.iterator();
        Iterator<Point> itMovedBefore = movedFromBefore.iterator();
        //FIXME delete these logs after test method stabilized
        System.out.println(after);
        System.out.println(movedFromBefore);

        while (itAfter.hasNext()) {
            tolerantAssertLocations(itAfter.next(), itMovedBefore.next(), tolerance);
        }
    }

    @Test
    public void testWeekDayLabelsShort() {
        List<String> originalValues = Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
        List<String> weekDayShortNames = calendar.openPopup().getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, originalValues);

        String expectedWeekDayShortNames = "ne, po, ut, st, ct, pa, so";
        calendarAttributes.set(CalendarAttributes.weekDayLabelsShort, expectedWeekDayShortNames);
        List<String> expectedList = Arrays.asList(expectedWeekDayShortNames.split(", "));
        weekDayShortNames = calendar.openPopup().getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, expectedList);
    }

    @Test
    public void testZindex() {
        final String zindex = "30";
        calendarAttributes.set(CalendarAttributes.zindex, zindex);
        String contentZindex = calendar.openPopup().getRoot().getCssValue("z-index");
        assertEquals(contentZindex, zindex, "Z-index of the popup");
    }

    private void tolerantAssertLocations(Point actual, Point expected, int tolerance) {
        assertTrue(_tolerantAssertLocations(actual, expected, tolerance), "actual " + actual + ", expected " + expected);
    }

    private boolean _tolerantAssertLocations(Point actual, Point expected, int tolerance) {
        return (Math.abs(actual.x - expected.x) <= tolerance && Math.abs(actual.y - expected.y) <= tolerance);
    }

    private void assertVisible(WebElement element) {
        assertTrue(Graphene.element(element).isVisible().apply(driver));
    }

    private void assertNotVisible(WebElement element) {
        assertTrue(Graphene.element(element).not().isVisible().apply(driver));
    }

    private void assertListOfWebElementsVisible(List<WebElement> list) {
        for (WebElement webElement : list) {
            assertVisible(webElement);
        }
    }

    private void assertListOfWebElementsNotVisible(List<WebElement> list) {
        for (WebElement webElement : list) {
            assertNotVisible(webElement);
        }
    }

    private void submitWithA4jSubmitBtn() {
        MetamerPage.waitRequest(a4jbutton, WaitRequestType.XHR).click();
    }

    private class SetTimeAction implements Action {

        @Override
        public void perform() {
            MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
            calendar.openPopup().getFooterControls().openTimeEditor().setTime(todayMidday.plusMinutes(5),
                    SetValueBy.BUTTONS).confirmTime();
        }
    }

    private class SetCurrentDateWithCalendarsTodayButtonAction implements Action {

        @Override
        public void perform() {
            MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();
        }
    }

    private class SetTodayAndThenClickToNextMonthAction implements Action {

        @Override
        public void perform() {
            MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
            calendar.openPopup().getHeaderControls().nextMonth();
        }
    }
}
