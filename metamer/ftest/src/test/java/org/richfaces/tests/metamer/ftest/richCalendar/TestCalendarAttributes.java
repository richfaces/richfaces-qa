/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012-2013, Red Hat, Inc. and individual contributors
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
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.calendar.DayPicker;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay;
import org.richfaces.fragment.calendar.DayPicker.CalendarDay.DayType;
import org.richfaces.fragment.calendar.DayPicker.CalendarWeek;
import org.richfaces.fragment.calendar.HeaderControls;
import org.richfaces.fragment.calendar.PopupCalendar.PopupFooterControls;
import org.richfaces.fragment.calendar.PopupCalendar.PopupHeaderControls;
import org.richfaces.fragment.calendar.RichFacesAdvancedPopupCalendar.OpenedBy;
import org.richfaces.fragment.calendar.TimeEditor;
import org.richfaces.fragment.calendar.TimeEditor.SetValueBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarAttributes extends AbstractCalendarTest {

    private DateTimeFormatter dateTimeFormatter;

    private BoundaryDatesMode boundaryDatesMode;
    private Mode mode;
    private TodayControlMode todayControlMode;
    private Boolean booleanValue;

    @FindBy(css = "input[id$=a4jButton]")
    private WebElement a4jbutton;
    @FindBy(css = "span[id$=msg]")
    private RichFacesMessage message;

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
    public void initPage() {
        this.dateTimeFormatter = DateTimeFormat.forPattern(calendarAttributes.get(CalendarAttributes.datePattern));
        // should help stabilizing offset tests on Jenkins
        driver.manage().window().setSize(new Dimension(1280, 960));
    }

    @Test
    @UseWithField(field = "boundaryDatesMode", valuesFrom = FROM_ENUM, value = "")
    @Templates("plain")
    public void testBoundaryDatesMode() {
        calendarAttributes.set(CalendarAttributes.boundaryDatesMode, boundaryDatesMode.value);
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        MetamerPage.waitRequest(popupCalendar, WaitRequestType.XHR).setDateTime(firstOfNovember2012);
        PopupFooterControls footerControls = popupCalendar.openPopup().getFooterControls();
        HeaderControls headerControls = popupCalendar.openPopup().getHeaderControls();
        DateTime yearAndMonth;
        String firstOfNovember2012String = firstOfNovember2012.toString(dateTimeFormatter);
        switch (boundaryDatesMode) {
            case INACTIVE:
            case NULL:
                try {
                    MetamerPage.waitRequest(dayPicker.getBoundaryDays().get(0), WaitRequestType.NONE).select();
                    fail("Item should not be selected.");
                } catch (TimeoutException ex) {// ok
                }
                // apply and check, that the date has not changed
                footerControls = popupCalendar.openPopup().getFooterControls();
                MetamerPage.waitRequest(footerControls, WaitRequestType.NONE).applyDate();
                assertEquals(popupCalendar.getInput().getStringValue(), firstOfNovember2012String);
                break;
            case SCROLL:
                // scroll to 28th of October 2012
                try {
                    MetamerPage.waitRequest(dayPicker.getBoundaryDays().get(0), WaitRequestType.NONE).select();
                    fail("Item should not be selected.");
                } catch (TimeoutException ex) {// ok
                }
                yearAndMonth = headerControls.getYearAndMonth();
                assertEquals(yearAndMonth.getYear(), 2012);
                assertEquals(yearAndMonth.getMonthOfYear(), 10);
                // apply and check, that the date has not changed
                footerControls = popupCalendar.openPopup().getFooterControls();
                MetamerPage.waitRequest(footerControls, WaitRequestType.NONE).applyDate();
                assertEquals(popupCalendar.getInput().getStringValue(), firstOfNovember2012String);
                break;
            case SELECT:
                // select 28th of October 2012
                CalendarDay day = dayPicker.getBoundaryDays().get(0);
                MetamerPage.waitRequest(day.getDayElement(), WaitRequestType.NONE).click();
                yearAndMonth = headerControls.getYearAndMonth();
                assertEquals(yearAndMonth.getYear(), 2012);
                assertEquals(yearAndMonth.getMonthOfYear(), 10);

                MetamerPage.waitRequest(footerControls, WaitRequestType.XHR).applyDate();
                DateTime parsedDateTime = dateTimeFormatter.parseDateTime(popupCalendar.getInput().getStringValue());
                assertEquals(parsedDateTime.getYear(), 2012);
                assertEquals(parsedDateTime.getMonthOfYear(), 10);
                assertEquals(parsedDateTime.getDayOfMonth(), 28);
                break;
        }
    }

    @Test
    @Templates(value = "plain")
    public void testButtonClass() {
        testStyleClass(popupCalendar.getPopupButtonElement(), BasicAttributes.buttonClass);
    }

    @Test
    @Templates(value = "plain")
    public void testButtonClassLabel() {
        calendarAttributes.set(CalendarAttributes.buttonLabel, "label");
        testStyleClass(popupCalendar.getPopupButtonElement(), BasicAttributes.buttonClass);
    }

    @Test
    @Templates(value = "plain")
    public void testButtonClassIcon() {
        calendarAttributes.set(CalendarAttributes.buttonIcon, "heart");
        testStyleClass(popupCalendar.getPopupButtonElement(), BasicAttributes.buttonClass);
    }

    @Test
    @Templates(value = "plain")
    public void testButtonIcon() {
        calendarAttributes.set(CalendarAttributes.buttonIcon, "star");
        String src = popupCalendar.getPopupButtonElement().getAttribute("src");
        assertTrue(src.contains("star.png"), "Calendar's icon was not updated.");

        calendarAttributes.set(CalendarAttributes.buttonIcon, "null");
        src = popupCalendar.getPopupButtonElement().getAttribute("src");
        assertTrue(src.contains("calendarIcon.png"), "Calendar's icon was not updated.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10255")
    @Templates(value = "plain")
    public void testButtonDisabledIcon() {
        calendarAttributes.set(CalendarAttributes.buttonDisabledIcon, "heart");
        calendarAttributes.set(CalendarAttributes.disabled, Boolean.TRUE);

        String src = popupCalendar.getPopupButtonElement().getAttribute("src");
        assertTrue(src.endsWith("heart.png"), "Calendar's icon was not updated.");

        calendarAttributes.set(CalendarAttributes.buttonDisabledIcon, "null");

        src = popupCalendar.getPopupButtonElement().getAttribute("src");
        assertTrue(src.contains("disabledCalendarIcon.png"), "Calendar's icon was not updated.");
    }

    @Test
    @Templates("plain")
    public void testButtonLabel() {
        calendarAttributes.set(CalendarAttributes.buttonLabel, "label");

        assertTrue(new WebElementConditionFactory(popupCalendar.getPopupButtonElement()).isVisible().apply(driver),
            "Button should be displayed.");
        assertEquals(popupCalendar.getPopupButtonElement().getText(), "label", "Label of the button.");
        assertNotEquals(popupCalendar.getPopupButtonElement().getTagName(), "img", "Image should not be displayed.");

        calendarAttributes.set(CalendarAttributes.buttonIcon, "star");
        assertNotEquals(popupCalendar.getPopupButtonElement().getTagName(), "img", "Image should not be displayed.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11313")
    public void testConverterMessage() {
        String errorMsg = "conversion error";
        calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
        calendarAttributes.set(CalendarAttributes.converterMessage, errorMsg);

        popupCalendar.getInput().sendKeys("RF 4");
        submitWithA4jSubmitBtn();
        message.advanced().waitUntilMessageIsVisible().perform();

        assertEquals(message.getDetail(), errorMsg);
    }

    @Test
    public void testDatePattern() {
        String pattern = "hh:mm:ss a MMMM d, yyyy";
        calendarAttributes.set(CalendarAttributes.datePattern, pattern);

        setCurrentDateWithCalendarsTodayButtonAction.perform();
        String calendarInputText = popupCalendar.getInput().getStringValue();
        dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        try {
            DateTime dt = dateTimeFormatter.parseDateTime(calendarInputText);
            assertEquals(dt.getDayOfMonth(), todayMidday.getDayOfMonth());
            assertEquals(dt.getMonthOfYear(), todayMidday.getMonthOfYear());
            assertEquals(dt.getYear(), todayMidday.getYear());
        } catch (IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testDayClassFunction() {
        int tuesdayDay = 3;
        calendarAttributes.set(CalendarAttributes.dayClassFunction, "yellowTuesdays");
        // switch to next month to refresh classes
        popupCalendar.openPopup().getHeaderControls().nextMonth();
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        List<CalendarDay> tuesdays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(tuesdayDay)));
        for (CalendarDay tuesday : tuesdays) {
            assertTrue(tuesday.containsStyleClass("yellowDay"), "All tuesdays should be yellow.");
        }

        calendarAttributes.set(CalendarAttributes.dayClassFunction, "null");
        dayPicker = popupCalendar.openPopup().getDayPicker();
        tuesdays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(tuesdayDay)));
        for (CalendarDay tuesday : tuesdays) {
            assertFalse(tuesday.containsStyleClass("yellowDay"), "All tuesdays should not be yellow.");
        }
    }

    @Test
    public void testDayDisableFunction() {
        calendarAttributes.set(CalendarAttributes.dayDisableFunction, "disableTuesdays");
        int tuesdayDay = 3;
        // switch to next month to refresh classes
        popupCalendar.openPopup().getHeaderControls().nextMonth();
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();

        List<CalendarDay> tuesdays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(tuesdayDay)));
        for (CalendarDay tuesday : tuesdays) {
            assertFalse(tuesday.is(DayType.selectableDay), "All tuesdays should not be enabled.");
        }

        calendarAttributes.set(CalendarAttributes.dayDisableFunction, "null");
        dayPicker = popupCalendar.openPopup().getDayPicker();
        tuesdays = filterOutBoundaryDays(Lists.newArrayList(dayPicker.getSpecificDays(tuesdayDay)));
        for (CalendarDay tuesday : tuesdays) {
            assertTrue(tuesday.is(DayType.selectableDay), "All tuesdays should be enabled.");
        }
    }

    @Test
    @Templates("plain")
    @UseWithField(field = "positioning", valuesFrom = FROM_ENUM, value = "")
    public void testDirection() {
        testDirection(new ShowElementAndReturnAction() {
            @Override
            public WebElement perform() {
                return popupCalendar.openPopup().getRoot();
            }
        });
    }

    @Test
    @Templates("plain")
    public void testDefaultLabel() {
        String defaultLabel = "RichFaces 4";
        calendarAttributes.set(CalendarAttributes.defaultLabel, defaultLabel);
        assertEquals(popupCalendar.getInput().getStringValue(), defaultLabel);
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-9837", "https://issues.jboss.org/browse/RF-10085" })
    public void testDefaultTime() {
        final String t = "06:06";
        calendarAttributes.set(CalendarAttributes.defaultTime, t);
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        String text = popupCalendar.openPopup().getFooterControls().getTimeEditorOpenerElement().getText();
        assertTrue(text.equals(t), "Default time");

        // another check in time editor
        TimeEditor timeEditor = popupCalendar.openPopup().getFooterControls().openTimeEditor();
        DateTime setTime = timeEditor.getTime();
        DateTime reference = todayMidday.withHourOfDay(6).withMinuteOfHour(6);
        assertEquals(setTime.getHourOfDay(), reference.getHourOfDay());
        assertEquals(setTime.getMinuteOfHour(), reference.getMinuteOfHour());
    }

    @Test
    public void testDisabled() {
        calendarAttributes.set(CalendarAttributes.disabled, Boolean.TRUE);
        assertEquals(popupCalendar.getInput().advanced().getInputElement().getAttribute("disabled"), "true");

        // Popup should not be displayed
        int catched = 0;
        try {
            Graphene.guardNoRequest(popupCalendar).openPopup(OpenedBy.INPUT_CLICKING);
        } catch (TimeoutException ex) {
            catched++;
        }
        try {
            Graphene.guardNoRequest(popupCalendar).openPopup(OpenedBy.OPEN_BUTTON_CLICKING);
        } catch (TimeoutException ex) {
            catched++;
        }
        assertEquals(catched, 2);
    }

    @Test
    @Templates("plain")
    public void testEnableManualInput() {
        assertEquals(popupCalendar.getInput().advanced().getInputElement().getAttribute("readonly"), "true");

        calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
        assertTrue(new WebElementConditionFactory(popupCalendar.getInput().advanced().getInputElement()).attribute("readonly")
            .not().isPresent().apply(driver), "Readonly attribute of input should not be defined.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9646")
    public void testFirstWeekDay() {
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        List<String> weekDays = Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

        assertEquals(dayPicker.getWeekDayShortNames(), weekDays);

        // wrong input, nothing changes, RF-9646
        calendarAttributes.set(CalendarAttributes.firstWeekDay, 7);
        dayPicker = popupCalendar.openPopup().getDayPicker();
        assertEquals(dayPicker.getWeekDayShortNames(), weekDays);

        calendarAttributes.set(CalendarAttributes.firstWeekDay, 1);
        dayPicker = popupCalendar.openPopup().getDayPicker();
        Collections.rotate(weekDays, -1);
        assertEquals(dayPicker.getWeekDayShortNames(), weekDays);
    }

    @Test
    @Templates("plain")
    public void testHorizontalOffset() {
        testHorizontalOffset(new ShowElementAndReturnAction() {
            @Override
            public WebElement perform() {
                return popupCalendar.openPopup().getRoot();
            }
        });
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10821")
    public void testImmediate() {
        calendarAttributes.set(CalendarAttributes.immediate, Boolean.TRUE);
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: null -> " + popupCalendar.getInput().getStringValue());
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
    }

    @Test
    @Templates(value = "plain")
    public void testInputClass() {
        testStyleClass(popupCalendar.getInput().advanced().getInputElement(), BasicAttributes.inputClass);
    }

    @Test
    @Templates("plain")
    public void testInputSize() {
        calendarAttributes.set(CalendarAttributes.inputSize, "30");
        assertEquals(popupCalendar.getInput().advanced().getInputElement().getAttribute("size"), "30",
            "Size attribute of input should be defined.");
    }

    @Test
    @Templates(value = "plain")
    public void testInputStyle() {
        testStyle(popupCalendar.getInput().advanced().getInputElement(), BasicAttributes.inputStyle);
    }

    @Test
    @Templates("plain")
    @UseWithField(field = "positioning", valuesFrom = FROM_ENUM, value = "")
    public void testJointPoint() {
        Locations l = Utils.getLocations(popupCalendar.getRootElement());
        testJointPoint(l.getWidth(), l.getHeight(), new ShowElementAndReturnAction() {
            @Override
            public WebElement perform() {
                return popupCalendar.openPopup().getRoot();
            }
        });
    }

    @Test
    @Templates("plain")
    public void testLocale() {
        String locale = "ru";
        calendarAttributes.set(CalendarAttributes.locale, locale);
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        List<String> weekDayShortNames = dayPicker.getWeekDayShortNames();
        List<String> expectedShortNames = Arrays.asList("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб");
        assertEquals(weekDayShortNames, expectedShortNames);

        setCurrentDateWithCalendarsTodayButtonAction.perform();
        DateTime parsedDateTime = dateTimeFormatter.withLocale(new Locale(locale)).parseDateTime(
            popupCalendar.getInput().getStringValue());

        assertEquals(parsedDateTime.getDayOfMonth(), todayMidday.getDayOfMonth(), "Input doesn't contain selected date.");
        assertEquals(parsedDateTime.getMonthOfYear(), todayMidday.getMonthOfYear(), "Input doesn't contain selected date.");
        assertEquals(parsedDateTime.getYear(), todayMidday.getYear(), "Input doesn't contain selected date.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12552")
    public void testMinDaysInFirstWeek() {
        calendarAttributes.set(CalendarAttributes.minDaysInFirstWeek, 1);
        // 1.1.2011 starts with saturday => only 1 day in first weak
        DateTime firstOf2011 = firstOfJanuary2012.withYear(2011);
        popupCalendar.setDateTime(firstOf2011);
        CalendarWeek firstDisplayedWeek = popupCalendar.openPopup().getDayPicker().getWeek(1);
        int secondDisplayedWeekNumber = popupCalendar.openPopup().getDayPicker().getWeek(2).getWeekNumber();
        List<CalendarDay> days = filterOutBoundaryDays(Lists.newArrayList(firstDisplayedWeek.getCalendarDays()));

        assertEquals(days.size(), 1, "Month days in first displayed week.");
        assertEquals(firstDisplayedWeek.getWeekNumber().intValue(), 1, "First displayed week number.");
        assertEquals(secondDisplayedWeekNumber, 2, "Second displayed week number.");

        calendarAttributes.set(CalendarAttributes.minDaysInFirstWeek, 2);

        firstDisplayedWeek = popupCalendar.openPopup().getDayPicker().getWeek(1);
        secondDisplayedWeekNumber = popupCalendar.openPopup().getDayPicker().getWeek(2).getWeekNumber();
        days = filterOutBoundaryDays(Lists.newArrayList(firstDisplayedWeek.getCalendarDays()));

        assertEquals(days.size(), 1, "Month days in first displayed week.");
        assertEquals(firstDisplayedWeek.getWeekNumber().intValue(), 53, "First displayed week number.");
        assertEquals(secondDisplayedWeekNumber, 1, "Second displayed week number.");
    }

    @Test
    @UseWithField(field = "mode", valuesFrom = FROM_ENUM, value = "")
    public void testMode() {
        calendarAttributes.set(CalendarAttributes.mode, mode.value);
        HeaderControls hc = popupCalendar.openPopup().getHeaderControls();
        switch (mode) {
            case AJAX:
                MetamerPage.waitRequest(hc, WaitRequestType.XHR).nextMonth();
                MetamerPage.waitRequest(hc, WaitRequestType.XHR).nextYear();
                MetamerPage.waitRequest(hc, WaitRequestType.XHR).previousMonth();
                MetamerPage.waitRequest(hc, WaitRequestType.XHR).previousYear();
                break;
            case CLIENT:
            case NULL:
                MetamerPage.waitRequest(hc, WaitRequestType.NONE).nextMonth();
                MetamerPage.waitRequest(hc, WaitRequestType.NONE).nextYear();
                MetamerPage.waitRequest(hc, WaitRequestType.NONE).previousMonth();
                MetamerPage.waitRequest(hc, WaitRequestType.NONE).previousYear();
                break;
        }
    }

    @Test
    @Templates("plain")
    public void testMonthLabels() {
        String labelsString = "január, február, marec, apríl, máj, jún, júl, august, september, október, november, december";
        calendarAttributes.set(CalendarAttributes.monthLabels, labelsString);

        // set date to 1st day of year
        popupCalendar.setDateTime(todayMidday.withMonthOfYear(1).withDayOfMonth(1));

        List<String> expectedLabels = Arrays.asList(labelsString.split(", "));
        HeaderControls hc = popupCalendar.openPopup().getHeaderControls();
        List<String> monthsLabels = new ArrayList<String>(12);
        for (int i = 0; i < 12; i++) {
            String monthAndYear = hc.getYearAndMonthEditorOpenerElement().getText();
            String month = monthAndYear.substring(0, monthAndYear.indexOf(","));
            monthsLabels.add(month);
            hc.nextMonth();
        }
        assertEquals(monthsLabels, expectedLabels, "Month label in calendar");
    }

    @Test
    @Templates("plain")
    public void testMonthLabelsShort() {
        String labelsString = "jan, feb, mar, apr, máj, jún, júl, aug, sep, okt, nov, dec";
        calendarAttributes.set(CalendarAttributes.monthLabelsShort, labelsString);

        List<String> expectedLabels = Arrays.asList(labelsString.split(", "));
        List<String> shortMonthsLabels = popupCalendar.openPopup().getHeaderControls().openYearAndMonthEditor()
            .getShortMonthsLabels();

        assertEquals(shortMonthsLabels, expectedLabels, "Month label in calendar");
    }

    @Test
    public void testOnTimeSelect() {
        testFireEvent(calendarAttributes, CalendarAttributes.ontimeselect, setTimeAction);
    }

    @Test
    public void testOnbeforetimeselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.onbeforetimeselect, setTimeAction);
    }

    @Test
    public void testOnchange() {
        testFireEvent(calendarAttributes, CalendarAttributes.onchange, setCurrentDateWithCalendarsTodayButtonAction);
    }

    @Test
    public void testOnclean() {
        testFireEvent(calendarAttributes, CalendarAttributes.onclean, new Action() {
            @Override
            public void perform() {
                PopupFooterControls fc = popupCalendar.openPopup().getFooterControls();
                MetamerPage.waitRequest(fc, WaitRequestType.NONE).todayDate();
                Graphene.waitGui().until().element(fc.getCleanButtonElement()).is().visible();
                MetamerPage.waitRequest(fc, WaitRequestType.NONE).cleanDate();
            }
        });
    }

    @Test
    public void testOncomplete() {
        calendarAttributes.set(CalendarAttributes.mode, "ajax");
        testFireEvent(calendarAttributes, CalendarAttributes.oncomplete, new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(popupCalendar.openPopup().getHeaderControls(), WaitRequestType.XHR).nextMonth();
            }
        });
    }

    @RegressionTest("https://issues.jboss.org/browse/RF-12505")
    @Test
    public void testOncurrentdateselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.oncurrentdateselect, setTodayAndThenClickToNextMonthAction);
    }

    @Test
    @Templates(value = "plain")
    public void testOndatemouseout() {
        testFireEvent(calendarAttributes, CalendarAttributes.ondatemouseout, new Action() {
            @Override
            public void perform() {
                fireEvent(popupCalendar.openPopup().getDayPicker().getTodayDay().getDayElement(), Event.MOUSEOUT);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOndatemouseover() {
        testFireEvent(calendarAttributes, CalendarAttributes.ondatemouseover, new Action() {
            @Override
            public void perform() {
                fireEvent(popupCalendar.openPopup().getDayPicker().getTodayDay().getDayElement(), Event.MOUSEOVER);
            }
        });
    }

    @Test
    public void testOndateselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.ondateselect, setCurrentDateWithCalendarsTodayButtonAction);
    }

    @Test
    public void testOnhide() {
        testFireEvent(
            calendarAttributes,
            CalendarAttributes.onhide,
            new Actions(driver).click(popupCalendar.getInput().advanced().getInputElement())
            .click(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    public void testOninputblur() {
        // this throws the condition 2x
        // testFireEventWithJS(calendar.getInput(), Event.BLUR, calendarAttributes, CalendarAttributes.oninputblur);
        testFireEvent(calendarAttributes, CalendarAttributes.oninputblur,
            new Actions(driver).click(popupCalendar.getInput().advanced().getInputElement())
            .click(getMetamerPage().getRequestTimeElement()).build());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9602")
    public void testOninputchange() {
        calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
        testFireEvent(calendarAttributes, CalendarAttributes.oninputchange, new Action() {
            @Override
            public void perform() {
                popupCalendar.getInput().sendKeys("0");
                submitWithA4jSubmitBtn();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputclick,
            new Actions(driver).click(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputdblclick,
            new Actions(driver).doubleClick(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    public void testOninputfocus() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputfocus,
            new Actions(driver).click(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEventWithJS(popupCalendar.getInput().advanced().getInputElement(), Event.KEYDOWN, calendarAttributes,
            CalendarAttributes.oninputkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEventWithJS(popupCalendar.getInput().advanced().getInputElement(), Event.KEYPRESS, calendarAttributes,
            CalendarAttributes.oninputkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEventWithJS(popupCalendar.getInput().advanced().getInputElement(), Event.KEYUP, calendarAttributes,
            CalendarAttributes.oninputkeyup);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmousedown,
            new Actions(driver).clickAndHold(popupCalendar.getInput().advanced().getInputElement()).build());
        // cleanup
        new Actions(driver).release().perform();
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousemove() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmousemove,
            new Actions(driver).moveToElement(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEvent(
            calendarAttributes,
            CalendarAttributes.oninputmouseout,
            new Actions(driver).click(popupCalendar.getInput().advanced().getInputElement())
            .moveToElement(getMetamerPage().getRequestTimeElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmouseover,
            new Actions(driver).moveToElement(getMetamerPage().getRequestTimeElement()).moveToElement(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmouseup,
            new Actions(driver).click(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    public void testOninputselect() {
        testFireEvent(calendarAttributes, CalendarAttributes.oninputmouseup,
            new Actions(driver).click(popupCalendar.getInput().advanced().getInputElement()).build());
    }

    @Test
    public void testOnshow() {
        testFireEvent(calendarAttributes, CalendarAttributes.onshow, new Action() {
            @Override
            public void perform() {
                popupCalendar.openPopup();
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
        assertListOfWebElementsNotVisible(Arrays.asList(popupCalendar.getInput().advanced().getInputElement(),
            popupCalendar.getPopupButtonElement()));
    }

    @Test
    @Templates(value = "plain")
    public void testPopupClass() {
        testHTMLAttribute(popupCalendar.openPopup().getRoot(), calendarAttributes, CalendarAttributes.popupClass,
            "metamer-ftest-class");
    }

    @Test
    @Templates(value = "plain")
    public void testPopupStyle() {
        testHTMLAttribute(popupCalendar.openPopup().getRoot(), calendarAttributes, CalendarAttributes.popupStyle,
            "background-color: yellow;");
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        calendarAttributes.set(CalendarAttributes.rendered, Boolean.FALSE);
        assertFalse(popupCalendar.isVisible());
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    public void testRequired() {
        calendarAttributes.set(CalendarAttributes.required, booleanValue);
        submitWithA4jSubmitBtn();
        if (booleanValue) {
            assertTrue(message.advanced().isVisible());
            assertEquals(message.getDetail(), "value is required");
        } else {
            assertFalse(message.advanced().isVisible());
        }
    }

    @Test
    public void testRequiredMessage() {
        String msg = "RichFaces 4";
        calendarAttributes.set(CalendarAttributes.requiredMessage, msg);
        calendarAttributes.set(CalendarAttributes.required, Boolean.TRUE);
        submitWithA4jSubmitBtn();

        assertTrue(message.advanced().isVisible());
        assertEquals(message.getDetail(), msg);
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    public void testResetTimeOnDateSelect() {
        calendarAttributes.set(CalendarAttributes.resetTimeOnDateSelect, booleanValue);
        int minutes = 33;

        // set yesterday with some minutes
        MetamerPage.waitRequest(popupCalendar, WaitRequestType.XHR).setDateTime(todayMidday.plusMinutes(minutes).minusDays(1));
        // second time, but without minutes setting, to see if the minutes will reset
        setCurrentDateWithCalendarsTodayButtonAction.perform();

        int minutesAfterReseting = dateTimeFormatter.parseDateTime(popupCalendar.getInput().getStringValue()).getMinuteOfHour();

        if (booleanValue) {
            assertEquals(minutesAfterReseting, 0);
        } else {
            assertEquals(minutesAfterReseting, minutes);
        }
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    public void testShowApplyButton() {
        calendarAttributes.set(CalendarAttributes.showApplyButton, booleanValue);
        PopupFooterControls fc = popupCalendar.openPopup().getFooterControls();
        if (booleanValue) {
            assertVisible(fc.getApplyButtonElement());
        } else {
            assertNotVisible(fc.getApplyButtonElement());
            setCurrentDateWithCalendarsTodayButtonAction.perform();
            DateTime inputTime = dateTimeFormatter.parseDateTime(popupCalendar.getInput().getStringValue());
            assertEquals(inputTime.getDayOfMonth(), todayMidday.getDayOfMonth());
            assertEquals(inputTime.getMonthOfYear(), todayMidday.getMonthOfYear());
            assertEquals(inputTime.getYear(), todayMidday.getYear());
        }
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    @Templates("plain")
    public void testShowFooter() {
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        calendarAttributes.set(CalendarAttributes.showFooter, booleanValue);
        PopupFooterControls fc = popupCalendar.openPopup().getFooterControls();
        if (booleanValue) {
            assertTrue(fc.isVisible(), "Footer elements should be visible, when footer is rendered");
            assertListOfWebElementsVisible(Arrays.asList(fc.getApplyButtonElement(), fc.getCleanButtonElement(),
                fc.getTimeEditorOpenerElement(), fc.getTodayButtonElement()));

        } else {
            assertFalse(fc.isVisible(), "Footer elements should not be visible, when footer is not rendered");
        }
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    @Templates("plain")
    public void testShowHeader() {
        calendarAttributes.set(CalendarAttributes.showHeader, booleanValue);
        PopupHeaderControls hc = popupCalendar.openPopup().getHeaderControls();
        if (booleanValue) {
            assertListOfWebElementsVisible(Arrays.asList(hc.getCloseButtonElement(), hc.getNextMonthElement(),
                hc.getNextYearElement(), hc.getPreviousMonthElement(), hc.getPreviousYearElement(),
                hc.getYearAndMonthEditorOpenerElement()));
        } else {
            assertListOfWebElementsNotVisible(Arrays.asList(hc.getCloseButtonElement(), hc.getNextMonthElement(),
                hc.getNextYearElement(), hc.getPreviousMonthElement(), hc.getPreviousYearElement(),
                hc.getYearAndMonthEditorOpenerElement()));
        }
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    @Templates("plain")
    public void testShowInput() {
        calendarAttributes.set(CalendarAttributes.showInput, booleanValue);
        if (booleanValue) {
            assertVisible(popupCalendar.getInput().advanced().getInputElement());
        } else {
            assertNotVisible(popupCalendar.getInput().advanced().getInputElement());
        }
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    @Templates("plain")
    public void testShowWeekDaysBar() {
        calendarAttributes.set(CalendarAttributes.showWeekDaysBar, booleanValue);
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        if (booleanValue) {
            assertVisible(dayPicker.getWeekDaysBarElement());
        } else {
            assertNotVisible(dayPicker.getWeekDaysBarElement());
        }
    }

    @Test
    @UseWithField(field = "booleanValue", valuesFrom = FROM_FIELD, value = "booleans")
    public void testShowWeeksBar() {
        calendarAttributes.set(CalendarAttributes.showWeeksBar, booleanValue);
        DayPicker dayPicker = popupCalendar.openPopup().getDayPicker();
        if (booleanValue) {
            assertVisible(dayPicker.getWeek(1).getWeekNumberElement());
        } else {
            assertNotVisible(dayPicker.getWeek(1).getWeekNumberElement());
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9655")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(popupCalendar.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(popupCalendar.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testTabindexInput() {
        testHTMLAttribute(popupCalendar.getInput().advanced().getInputElement(), calendarAttributes,
            CalendarAttributes.tabindex, "101");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "richPopupPanel")
    public void testTabindexInputInPopupPanel() {
        testHTMLAttribute(popupCalendar.getInput().advanced().getInputElement(), calendarAttributes,
            CalendarAttributes.tabindex, "101");
    }

    @Test
    @Templates("plain")
    public void testTabindexButton() {
        testHTMLAttribute(popupCalendar.getPopupButtonElement(), calendarAttributes, CalendarAttributes.tabindex, "101");
    }

    @Test
    @UseWithField(field = "todayControlMode", valuesFrom = FROM_ENUM, value = "")
    public void testTodayControlMode() {
        calendarAttributes.set(CalendarAttributes.todayControlMode, todayControlMode.value);
        switch (todayControlMode) {
            case HIDDEN:
                assertNotVisible(popupCalendar.openPopup().getFooterControls().getTodayButtonElement());
                break;
            case NULL:
            case SELECT:
                // set date to tomorrow
                popupCalendar.setDateTime(todayMidday.plusDays(1));
                // set date with calendar's 'Today' button,
                // this will scroll and select todays day
                MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
                CalendarDay selectedDay = popupCalendar.openPopup().getDayPicker().getSelectedDay();
                assertNotNull(selectedDay);
                assertTrue(selectedDay.is(DayType.todayDay));
                break;
            case SCROLL:
                popupCalendar.setDateTime(todayMidday.plusMonths(1));
                // set date with calendar's 'Today' button,
                // this will only scroll to today but will not select it
                MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
                // no selected day should be in calendar
                assertNull(popupCalendar.openPopup().getDayPicker().getSelectedDay());
                // but view of day picker should will change to current month
                assertEquals(popupCalendar.openPopup().getHeaderControls().getYearAndMonth().getMonthOfYear(),
                    todayMidday.getMonthOfYear());
                break;
        }
    }

    @Test
    public void testValueChangeListener() {
        setCurrentDateWithCalendarsTodayButtonAction.perform();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> " + popupCalendar.getInput().getStringValue());
    }

    @Test
    @Templates("plain")
    public void testVerticalOffset() {
        testVerticalOffset(new ShowElementAndReturnAction() {
            @Override
            public WebElement perform() {
                return popupCalendar.openPopup().getRoot();
            }
        });
    }

    @Test
    @Templates("plain")
    public void testWeekDayLabelsShort() {
        List<String> originalValues = Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
        List<String> weekDayShortNames = popupCalendar.openPopup().getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, originalValues);

        String expectedWeekDayShortNames = "ne, po, ut, st, ct, pa, so";
        calendarAttributes.set(CalendarAttributes.weekDayLabelsShort, expectedWeekDayShortNames);
        List<String> expectedList = Arrays.asList(expectedWeekDayShortNames.split(", "));
        weekDayShortNames = popupCalendar.openPopup().getDayPicker().getWeekDayShortNames();
        assertEquals(weekDayShortNames, expectedList);
    }

    @Test
    @Templates(value = "plain")
    public void testZindex() {
        final String zindex = "30";
        calendarAttributes.set(CalendarAttributes.zindex, zindex);
        String contentZindex = popupCalendar.openPopup().getRoot().getCssValue("z-index");
        assertEquals(contentZindex, zindex, "Z-index of the popup");
    }

    private void assertVisible(WebElement element) {
        assertTrue(new WebElementConditionFactory(element).isVisible().apply(driver));
    }

    private void assertNotVisible(WebElement element) {
        assertTrue(new WebElementConditionFactory(element).not().isVisible().apply(driver));
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
            MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
            popupCalendar.openPopup().getFooterControls().openTimeEditor()
                .setTime(todayMidday.plusMinutes(5), SetValueBy.BUTTONS).confirmTime();
        }
    }

    private class SetCurrentDateWithCalendarsTodayButtonAction implements Action {

        @Override
        public void perform() {
            MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();
        }
    }

    private class SetTodayAndThenClickToNextMonthAction implements Action {

        @Override
        public void perform() {
            MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.NONE).todayDate();
            popupCalendar.openPopup().getHeaderControls().nextMonth();
        }
    }
}
