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

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.buttonClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.inputClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.inputStyle;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.calendarAttributes;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.boundaryDatesMode;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.buttonDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.buttonIcon;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.buttonLabel;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.converterMessage;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.datePattern;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.dayClassFunction;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.defaultTime;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.enableManualInput;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.firstWeekDay;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.immediate;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.inputSize;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.locale;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.mode;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.monthLabels;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.monthLabelsShort;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.onbeforetimeselect;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.onchange;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.onclean;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.ondatemouseout;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.ondatemouseover;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.ondateselect;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.onhide;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.oninputchange;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.onshow;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.ontimeselect;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.showApplyButton;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.showFooter;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.showHeader;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.showInput;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.showWeekDaysBar;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.showWeeksBar;
import static org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes.zindex;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;


/**
 * Test case for attributes of a calendar on page faces/components/richCalendar/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22786 $
 */
public class TestRichCalendarAttributes extends AbstractCalendarTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Test
    public void testBoundaryDatesModeNull() {
        selenium.click(input);

        String month = selenium.getText(monthLabel);
        guardNoRequest(selenium).click(cellWeekDay.format(6, 6));
        String newMonth = selenium.getText(monthLabel);
        assertEquals(newMonth, month, "Month should not change.");

        // the most top-left column might be 1st day of month
        while (selenium.getText(cellWeekDay.format(1, 1)).equals("1")) {
            selenium.click(prevMonthButton);
        }

        month = selenium.getText(monthLabel);
        guardNoRequest(selenium).click(cellWeekDay.format(1, 1));
        newMonth = selenium.getText(monthLabel);
        assertEquals(newMonth, month, "Month should not change.");
    }

    @Test
    public void testBoundaryDatesModeInactive() {
        calendarAttributes.set(boundaryDatesMode, "inactive");

        testBoundaryDatesModeNull();
    }

    @Test
    public void testBoundaryDatesModeScroll() {
        calendarAttributes.set(boundaryDatesMode, "scroll");
        selenium.click(input);

        String thisMonth = selenium.getText(monthLabel);
        // November, 2010 -> November
        thisMonth = thisMonth.substring(0, thisMonth.indexOf(","));
        guardNoRequest(selenium).click(cellWeekDay.format(6, 6));
        String newMonth = selenium.getText(monthLabel);
        newMonth = newMonth.substring(0, newMonth.indexOf(","));
        assertEquals(Month.valueOf(newMonth), Month.valueOf(thisMonth).next(), "Month did not change correctly.");

        assertNoDateSelected();

        // the most top-left column might be 1st day of month
        while (selenium.getText(cellWeekDay.format(1, 1)).equals("1")) {
            selenium.click(prevMonthButton);
        }

        thisMonth = selenium.getText(monthLabel);
        // November, 2010 -> November
        thisMonth = thisMonth.substring(0, thisMonth.indexOf(","));
        guardNoRequest(selenium).click(cellWeekDay.format(1, 1));
        newMonth = selenium.getText(monthLabel);
        newMonth = newMonth.substring(0, newMonth.indexOf(","));

        assertEquals(Month.valueOf(newMonth), Month.valueOf(thisMonth).previous(), "Month did not change correctly.");

        assertNoDateSelected();
    }

    @Test
    public void testBoundaryDatesModeSelect() {
        calendarAttributes.set(boundaryDatesMode, "select");
        selenium.click(input);

        String thisMonth = selenium.getText(monthLabel);
        String selectedDate = selenium.getText(cellWeekDay.format(6, 6));
        // November, 2010 -> November
        thisMonth = thisMonth.substring(0, thisMonth.indexOf(","));
        guardNoRequest(selenium).click(cellWeekDay.format(6, 6));
        String newMonth = selenium.getText(monthLabel);
        newMonth = newMonth.substring(0, newMonth.indexOf(","));
        assertEquals(Month.valueOf(newMonth), Month.valueOf(thisMonth).next(), "Month did not change correctly.");

        assertSelected(selectedDate);

        // the most top-left column might be 1st day of month
        while (selenium.getText(cellWeekDay.format(1, 1)).equals("1")) {
            selenium.click(prevMonthButton);
        }

        thisMonth = selenium.getText(monthLabel);
        selectedDate = selenium.getText(cellWeekDay.format(1, 1));
        // November, 2010 -> November
        thisMonth = thisMonth.substring(0, thisMonth.indexOf(","));
        guardNoRequest(selenium).click(cellWeekDay.format(1, 1));
        newMonth = selenium.getText(monthLabel);
        newMonth = newMonth.substring(0, newMonth.indexOf(","));

        assertEquals(Month.valueOf(newMonth), Month.valueOf(thisMonth).previous(), "Month did not change correctly.");

        assertSelected(selectedDate);
    }

    @Test
    public void testButtonClass() {
        testStyleClass(image, buttonClass);
    }

    @Test
    public void testButtonClassLabel() {
        calendarAttributes.set(buttonLabel, "label");

        testStyleClass(button, buttonClass);
    }

    @Test
    public void testButtonClassIcon() {
        calendarAttributes.set(buttonIcon, "heart");

        testStyleClass(image, buttonClass);
    }

    @Test
    public void testButtonIcon() {
        calendarAttributes.set(buttonIcon, "star");

        AttributeLocator<JQueryLocator> attr = image.getAttribute(Attribute.SRC);
        String src = selenium.getAttribute(attr);
        assertTrue(src.contains("star.png"), "Calendar's icon was not updated.");

        calendarAttributes.reset(buttonIcon);

        src = selenium.getAttribute(attr);
        assertTrue(src.contains("calendarIcon.png"), "Calendar's icon was not updated.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10255")
    public void testButtonDisabledIcon() {
        calendarAttributes.set(disabled, Boolean.TRUE);

        calendarAttributes.set(buttonDisabledIcon, "heart");

        AttributeLocator<JQueryLocator> attr = image.getAttribute(Attribute.SRC);
        String src = selenium.getAttribute(attr);
        assertTrue(src.contains("heart.png"), "Calendar's icon was not updated.");

        calendarAttributes.reset(buttonDisabledIcon);

        src = selenium.getAttribute(attr);
        assertTrue(src.contains("disabledCalendarIcon.png"), "Calendar's icon was not updated.");
    }

    @Test
    public void testButtonLabel() {
        calendarAttributes.set(buttonLabel, "label");

        assertTrue(selenium.isVisible(button), "Button should be displayed.");
        assertEquals(selenium.getText(button), "label", "Label of the button.");
        if (selenium.isElementPresent(image)) {
            assertFalse(selenium.isVisible(image), "Image should not be displayed.");
        }

        calendarAttributes.set(buttonIcon, "star");

        if (selenium.isElementPresent(image)) {
            assertFalse(selenium.isVisible(image), "Image should not be displayed.");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11313")
    public void testConverterMessage() {
        JQueryLocator message = pjq("span[id$=msg] .rf-msg-det");

        calendarAttributes.set(enableManualInput, Boolean.TRUE);
        calendarAttributes.set(converterMessage, "conversion error");

        selenium.type(input, "xxx");
        guardXhr(selenium).click(pjq("input[id$=a4jButton]"));
        waitGui.until(elementVisible.locator(message));

        assertEquals(selenium.getText(message), "conversion error");
    }

    @Test
    public void testCurrentDate () {
        // TODO JJamrich 2012-02-10
    }

    @Test
    public void testDatePattern() {
        calendarAttributes.set(datePattern, "hh:mm:ss a MMMM d, yyyy");

        selenium.click(input);

        selenium.click(cellDay.format(6));
        String day = selenium.getText(cellDay.format(6));
        String month = selenium.getText(monthLabel);

        String selectedDate = null;
        try {
            Date date = new SimpleDateFormat("d MMMM, yyyy hh:mm a").parse(day + " " + month + " 12:00 PM");
            selectedDate = new SimpleDateFormat("hh:mm:ss a MMMM d, yyyy").format(date);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }

        selenium.click(applyButton);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        String inputDate = selenium.getValue(input);
        assertEquals(inputDate, selectedDate, "Input doesn't contain selected date.");
    }

    @Test
    public void testDayClassFunction() {
        calendarAttributes.set(dayClassFunction, "yellowTuesdays");

        selenium.click(input);

        for (int i = 2; i < 42; i += 7) {
            if (!selenium.belongsClass(cellDay.format(i), "rf-cal-boundary-day")) {
                assertTrue(selenium.belongsClass(cellDay.format(i), "yellowDay"), "Cell nr. " + i
                    + " should be yellow.");
            }
        }

        calendarAttributes.reset(dayClassFunction);

        selenium.click(input);

        for (int i = 0; i < 42; i++) {
            assertFalse(selenium.belongsClass(cellDay.format(i), "yellowDay"), "Cell nr. " + i
                + " should not be yellow.");
        }
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-9837", "https://issues.jboss.org/browse/RF-10085" })
    public void testDefaultTime() {
        calendarAttributes.set(defaultTime, "21:24");

        selenium.click(input);
        selenium.click(cellWeekDay.format(3, 3));

        boolean displayed = selenium.isVisible(timeButton);
        assertTrue(displayed, "Time button should be visible.");
        String buttonText = selenium.getText(timeButton);
        assertEquals(buttonText, "21:24", "Default time");
    }

    @Test
    public void testDisabled() {
        calendarAttributes.set(disabled, Boolean.TRUE);

        AttributeLocator<JQueryLocator> disabledAttr = input.getAttribute(new Attribute("disabled"));
        assertTrue(selenium.isAttributePresent(disabledAttr), "Disabled attribute of input should be defined.");
        assertEquals(selenium.getAttribute(disabledAttr), "true", "Input should be disabled.");

        selenium.click(input);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        selenium.click(image);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
    }

    @Test
    public void testEnableManualInput() {
        AttributeLocator<JQueryLocator> readonlyAttr = input.getAttribute(new Attribute("readonly"));
        assertTrue(selenium.isAttributePresent(readonlyAttr), "Readonly attribute of input should be defined.");
        assertEquals(selenium.getAttribute(readonlyAttr), "true", "Input should be read-only.");

        calendarAttributes.set(enableManualInput, Boolean.TRUE);

        assertFalse(selenium.isAttributePresent(readonlyAttr), "Readonly attribute of input should not be defined.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9646")
    public void testFirstWeekDay() {
        calendarAttributes.set(firstWeekDay, "6");

        selenium.click(input);

        String[] labels = { "", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri" };

        for (int i = 0; i < 8; i++) {
            String label = selenium.getText(weekDayLabel.format(i));
            assertEquals(label, labels[i], "Week day label " + i);
        }

        // wrong input - throws a server-side exception
        // selenium.type(pjq("input[type=text][id$=firstWeekDayInput]"), "9");
        // selenium.waitForPageToLoad();
        //
        // selenium.click(input);
        //
        // labels = new String[]{"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        //
        // for (int i = 0; i < 8; i++) {
        // String label = selenium.getText(weekDayLabel.format(i));
        // assertEquals(label, labels[i], "Week day label " + i);
        // }
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10821")
    public void testImmediate() {
        calendarAttributes.set(immediate, Boolean.TRUE);

        selenium.click(input);

        selenium.click(cellDay.format(6));
        String day = selenium.getText(cellDay.format(6));
        String month = selenium.getText(monthLabel);

        String selectedDate = null;
        try {
            Date date = new SimpleDateFormat("d MMMM, yyyy hh:mm").parse(day + " " + month + " 12:00");
            selectedDate = new SimpleDateFormat("MMM d, yyyy hh:mm").format(date);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }

        guardXhr(selenium).click(applyButton);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        String inputDate = selenium.getValue(input);
        assertEquals(inputDate, selectedDate, "Input doesn't contain selected date.");
        assertEquals(selenium.getText(output), selectedDate, "Input doesn't contain selected date.");

        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: null -> " + selectedDate);
    }

    @Test
    public void testInputClass() {
        testStyleClass(input, inputClass);
    }

    @Test
    public void testInputSize() {
        calendarAttributes.set(inputSize, "30");

        AttributeLocator<JQueryLocator> sizeAttr = input.getAttribute(Attribute.SIZE);
        assertTrue(selenium.isAttributePresent(sizeAttr), "Size attribute of input should be defined.");
        assertEquals(selenium.getAttribute(sizeAttr), "30", "Input should be disabled.");
    }

    @Test
    public void testInputStyle() {
        testStyle(input, inputStyle);
    }

    @Test
    public void testLocale() {
        calendarAttributes.set(locale, "ru");

        selenium.click(input);

        String[] labels = { "", "Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб" };

        for (int i = 0; i < 8; i++) {
            String label = selenium.getText(weekDayLabel.format(i));
            assertEquals(label, labels[i], "Week day label " + i);
        }

        selenium.click(cellDay.format(6));
        String day = selenium.getText(cellDay.format(6));
        String month = selenium.getText(monthLabel);

        String selectedDate = null;
        try {
            Date date = new SimpleDateFormat("d MMMM, yyyy hh:mm", new Locale("ru"))
                .parse(day + " " + month + " 12:00");
            selectedDate = new SimpleDateFormat("MMM d, yyyy hh:mm", new Locale("ru")).format(date);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }

        selenium.click(applyButton);
        String inputDate = selenium.getValue(input);
        assertEquals(inputDate, selectedDate, "Input doesn't contain selected date.");
    }

    @Test
    public void testMonthLabels() {
        String[] labels = { "január", "február", "marec", "apríl", "máj", "jún", "júl", "august", "september",
            "október", "november", "december" };
        String labelsString = "január,február,marec,apríl,máj,jún,   júl,august,september,október,november,december";

        calendarAttributes.set(monthLabels, labelsString);

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        String month = null;

        selenium.click(input);

        for (int i = 0; i < 12; i++) {
            month = selenium.getText(monthLabel);
            month = month.substring(0, month.indexOf(","));
            assertEquals(month, labels[(currentMonth + i) % 12], "Month label in calendar");
            selenium.click(nextMonthButton);
        }
    }

    @Test
    public void testMonthLabelsShort() {
        String[] labels = { "jan", "feb", "mar", "apr", "máj", "jún", "júl", "aug", "sep", "okt", "nov", "dec" };
        String labelsString = "jan,feb,mar,apr,máj,jún,   júl,aug,sep,okt,nov,dec";

        calendarAttributes.set(monthLabelsShort, labelsString);

        selenium.click(input);
        selenium.click(monthLabel);

        for (int i = 0; i < 12; i++) {
            assertEquals(selenium.getText(dateEditorMonths.format(i)), labels[i], "Short month label in calendar");
        }
    }

    @Test
    public void testOnbeforetimeselectOntimeselect() {
        calendarAttributes.set(onbeforetimeselect, "metamerEvents += \"beforetimeselect \"");
        calendarAttributes.set(ontimeselect, "metamerEvents += \"timeselect \"");

        selenium.click(input);
        selenium.click(cellDay.format(18));
        selenium.click(timeButton);
        selenium.runScript(new JavaScript("jQuery(\"" + hoursInputUp.getRawLocator() + "\").mousedown().mouseup()"));
        selenium.click(timeEditorOk);

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 2, "2 events should be fired.");
        assertEquals(events[0], "beforetimeselect", "Attribute onbeforetimeselect doesn't work");
        assertEquals(events[1], "timeselect", "Attribute ontimeselect doesn't work");
    }

    @Test
    public void testOnchange() {
        calendarAttributes.set(onchange, "metamerEvents += \"change \"");

        selenium.click(input);
        selenium.click(cellDay.format(18));
        selenium.click(applyButton);

        waitGui.failWith("Attribute onchange does not work correctly").until(
            new EventFiredCondition(new Event("change")));
    }

    @Test
    public void testOnclean() {
        calendarAttributes.set(onclean, "metamerEvents += \"clean \"");

        selenium.click(input);
        selenium.click(cellDay.format(18));
        selenium.click(cleanButton);

        waitGui.failWith("Attribute onclean does not work correctly")
            .until(new EventFiredCondition(new Event("clean")));
    }

    @Test
    public void testOncomplete() {
        calendarAttributes.set(mode, "ajax");

        calendarAttributes.set(oncomplete, "metamerEvents += \"complete \"");

        selenium.click(input);
        guardXhr(selenium).click(nextMonthButton);
        waitGui.failWith("Attribute oncomplete does not work correctly").until(
            new EventFiredCondition(new Event("complete")));
    }

    @Test
    public void testOndatemouseout() {
        calendarAttributes.set(ondatemouseout, "metamerEvents += \"datemouseout \"");

        selenium.click(input);
        selenium.fireEvent(cellDay.format(18), Event.MOUSEOUT);

        waitGui.failWith("Attribute ondatemouseout does not work correctly").until(
            new EventFiredCondition(new Event("datemouseout")));
    }

    @Test
    public void testOndatemouseover() {
        calendarAttributes.set(ondatemouseover, "metamerEvents += \"datemouseover \"");

        selenium.click(input);
        selenium.fireEvent(cellDay.format(18), Event.MOUSEOVER);

        waitGui.failWith("Attribute ondatemouseover does not work correctly").until(
            new EventFiredCondition(new Event("datemouseover")));
    }

    @Test
    public void testOndateselect() {
        calendarAttributes.set(ondateselect, "metamerEvents += \"dateselect \"");

        selenium.click(input);
        selenium.click(cellDay.format(18));

        waitGui.failWith("Attribute ondateselect does not work correctly").until(
            new EventFiredCondition(new Event("dateselect")));
    }

    @Test
    public void testOnhide() {
        calendarAttributes.set(onhide, "metamerEvents += \"hide \"");

        selenium.click(input);
        selenium.click(input);

        waitGui.failWith("Attribute onhide does not work correctly").until(new EventFiredCondition(new Event("hide")));
    }

    @Test
    public void testOninputblur() {
        testFireEvent(Event.BLUR, input, "inputblur");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9602")
    public void testOninputchange() {
        calendarAttributes.set(enableManualInput, Boolean.TRUE);

        calendarAttributes.set(oninputchange, "metamerEvents += \"inputchange \"");

        selenium.type(input, "Dec 23, 2010 19:27");

        waitGui.failWith("Attribute oninputchange does not work correctly").until(
            new EventFiredCondition(new Event("inputchange")));
    }

    @Test
    public void testOninputclick() {
        testFireEvent(Event.CLICK, input, "inputclick");
    }

    @Test
    public void testOninputdblclick() {
        testFireEvent(Event.DBLCLICK, input, "inputdblclick");
    }

    @Test
    public void testOninputfocus() {
        testFireEvent(Event.FOCUS, input, "inputfocus");
    }

    @Test
    public void testOninputkeydown() {
        testFireEvent(Event.KEYDOWN, input, "inputkeydown");
    }

    @Test
    public void testOninputkeypress() {
        testFireEvent(Event.KEYPRESS, input, "inputkeypress");
    }

    @Test
    public void testOninputkeyup() {
        testFireEvent(Event.KEYUP, input, "inputkeyup");
    }

    @Test
    public void testOninputmousedown() {
        testFireEvent(Event.MOUSEDOWN, input, "inputmousedown");
    }

    @Test
    public void testOninputmousemove() {
        testFireEvent(Event.MOUSEMOVE, input, "inputmousemove");
    }

    @Test
    public void testOninputmouseout() {
        testFireEvent(Event.MOUSEOUT, input, "inputmouseout");
    }

    @Test
    public void testOninputmouseover() {
        testFireEvent(Event.MOUSEOVER, input, "inputmouseover");
    }

    @Test
    public void testOninputmouseup() {
        testFireEvent(Event.MOUSEUP, input, "inputmouseup");
    }

    @Test
    public void testOninputselect() {
        testFireEvent(Event.SELECT, input, "inputselect");
    }

    @Test
    public void testOnshow() {
        calendarAttributes.set(onshow, "metamerEvents += \"show \"");

        selenium.click(input);
        selenium.click(input);

        waitGui.failWith("Attribute onshow does not work correctly").until(new EventFiredCondition(new Event("show")));
    }

    @Test
    public void testPopup() {
        calendarAttributes.set(CalendarAttributes.popup, Boolean.FALSE);

        boolean displayed = selenium.isVisible(calendar);
        assertTrue(displayed, "Calendar is not present on the page.");

        if (selenium.isElementPresent(input)) {
            displayed = selenium.isVisible(input);
            assertFalse(displayed, "Calendar's input should not be visible.");
        }

        if (selenium.isElementPresent(image)) {
            displayed = selenium.isVisible(image);
            assertFalse(displayed, "Calendar's image should not be visible.");
        }

        displayed = selenium.isVisible(popup);
        assertTrue(displayed, "Popup should be visible.");

        displayed = selenium.isElementPresent(button);
        assertFalse(displayed, "Calendar's button should not be visible.");
    }

    @Test
    public void testRendered() {
        calendarAttributes.set(rendered, Boolean.FALSE);

        assertFalse(selenium.isElementPresent(calendar), "Panel should not be rendered when rendered=false.");
    }

    @Test
    public void testShowApplyButton() {
        calendarAttributes.set(showApplyButton, Boolean.FALSE);

        selenium.click(input);
        if (selenium.isElementPresent(applyButton)) {
            assertFalse(selenium.isVisible(applyButton), "Apply button should not be displayed.");
        }

        guardXhr(selenium).click(cellDay.format(6));
        String day = selenium.getText(cellDay.format(6));
        String month = selenium.getText(monthLabel);

        String selectedDate = null;
        try {
            Date date = new SimpleDateFormat("d MMMM, yyyy hh:mm").parse(day + " " + month + " 12:00");
            selectedDate = new SimpleDateFormat("MMM d, yyyy hh:mm").format(date);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }

        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        String inputDate = selenium.getValue(input);
        assertEquals(inputDate, selectedDate, "Input doesn't contain selected date.");
    }

    @Test
    public void testShowFooter() {
        calendarAttributes.set(showFooter, Boolean.FALSE);

        selenium.click(input);
        boolean displayed = true;

        if (selenium.isElementPresent(todayButton)) {
            displayed = selenium.isVisible(todayButton);
            assertFalse(displayed, "Today button should not be visible.");
        }

        if (selenium.isElementPresent(applyButton)) {
            displayed = selenium.isVisible(applyButton);
            assertFalse(displayed, "Apply button should not be visible.");
        }

        displayed = selenium.isElementPresent(cleanButton);
        assertFalse(displayed, "Clean button should not be visible.");

        displayed = selenium.isElementPresent(timeButton);
        assertFalse(displayed, "Time button should not be visible.");

        selenium.click(cellWeekDay.format(3, 3));

        if (selenium.isElementPresent(cleanButton)) {
            displayed = selenium.isVisible(cleanButton);
            assertFalse(displayed, "Clean button should not be visible.");
        }

        if (selenium.isElementPresent(timeButton)) {
            displayed = selenium.isVisible(timeButton);
            assertFalse(displayed, "Time button should not be visible.");
        }
    }

    @Test
    public void testShowHeader() {
        calendarAttributes.set(showHeader, Boolean.FALSE);

        selenium.click(input);
        boolean displayed = true;

        if (selenium.isElementPresent(prevYearButton)) {
            displayed = selenium.isVisible(prevYearButton);
            assertFalse(displayed, "Previous year button should not be visible.");
        }

        if (selenium.isElementPresent(prevMonthButton)) {
            displayed = selenium.isVisible(prevMonthButton);
            assertFalse(displayed, "Previous month button should not be visible.");
        }

        if (selenium.isElementPresent(nextMonthButton)) {
            displayed = selenium.isVisible(nextMonthButton);
            assertFalse(displayed, "Next month button should not be visible.");
        }

        if (selenium.isElementPresent(nextYearButton)) {
            displayed = selenium.isVisible(nextYearButton);
            assertFalse(displayed, "Next year button should not be visible.");
        }

        if (selenium.isElementPresent(closeButton)) {
            displayed = selenium.isVisible(closeButton);
            assertFalse(displayed, "Close button should not be visible.");
        }

        if (selenium.isElementPresent(monthLabel)) {
            displayed = selenium.isVisible(monthLabel);
            assertFalse(displayed, "Month label should not be visible.");
        }
    }

    @Test
    public void testShowInput() {
        calendarAttributes.set(showInput, Boolean.FALSE);

        if (selenium.isElementPresent(input)) {
            boolean displayed = selenium.isVisible(input);
            assertFalse(displayed, "Input should not be visible.");
        }
    }

    @Test
    public void testShowWeekDaysBar() {
        calendarAttributes.set(showWeekDaysBar, Boolean.FALSE);

        for (int i = 0; i < 8; i++) {
            if (selenium.isElementPresent(weekDayLabel.format(i))) {
                boolean displayed = selenium.isVisible(weekDayLabel.format(i));
                assertFalse(displayed, "Bar with week days should not be visible.");
            }
        }
    }

    @Test
    public void testShowWeeksBar() {
        calendarAttributes.set(showWeeksBar, Boolean.FALSE);

        for (int i = 0; i < 6; i++) {
            if (selenium.isElementPresent(week.format(i))) {
                boolean displayed = selenium.isVisible(week.format(i));
                assertFalse(displayed, "Bar with week numbers should not be visible.");
            }
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9655")
    public void testStyle() {
        testStyle(calendar);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(calendar);
    }

    @Test
    @Templates(exclude = "richPopupPanel")
    public void testTabindexInput() {
        testHtmlAttribute(input, "tabindex", "99");
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "richPopupPanel")
    public void testTabindexInputInPopupPanel() {
        testHtmlAttribute(input, "tabindex", "99");
    }

    @Test
    public void testTabindexButton() {
        testHtmlAttribute(image, "tabindex", "99");
    }

    @Test
    public void testValueChangeListener() {
        String time1Value = selenium.getText(time);
        selenium.click(input);
        selenium.click(cellDay.format(6));
        guardXhr(selenium).click(applyButton);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String selectedDate1 = selenium.getValue(input);
        String selectedDate2 = selenium.getText(output);

        assertEquals(selectedDate1, selectedDate2, "Output and calendar's input should be the same.");
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> " + selectedDate1);
    }

    @Test
    public void testZindex() {
        calendarAttributes.set(zindex, "30");

        assertEquals(selenium.getStyle(popup, new CssProperty("z-index")), "30", "Z-index of the popup");
    }

    /**
     * Checks that no date in the open month is selected.
     */
    private void assertNoDateSelected() {
        for (int i = 0; i < 42; i++) {
            assertFalse(selenium.belongsClass(cellDay.format(i), "rf-cal-sel"), "Cell nr. " + i
                + " should not be selected.");
        }
    }

    /**
     * Checks that no date in the open month is selected except of one passed as argument.
     *
     * @param exceptOfDate
     *            date that should be selected (e.g. "13")
     */
    private void assertSelected(String exceptOfDate) {
        int lowerBoundary = 0;
        int upperBoundary = 42;

        if (Integer.parseInt(exceptOfDate) < 15) {
            upperBoundary = 21;
        } else {
            lowerBoundary = 21;
        }

        // check 3 lines of cells that contain selected date
        for (int i = lowerBoundary; i < upperBoundary; i++) {
            if (exceptOfDate.equals(selenium.getText(cellDay.format(i)))) {
                assertTrue(selenium.belongsClass(cellDay.format(i), "rf-cal-sel"), "Cell nr. " + i
                    + " should not be selected.");
            } else {
                assertFalse(selenium.belongsClass(cellDay.format(i), "rf-cal-sel"), "Cell nr. " + i
                    + " should not be selected.");
            }
        }

        lowerBoundary = lowerBoundary == 0 ? 21 : 0;
        upperBoundary = upperBoundary == 21 ? 42 : 21;

        // check other 3 lines of cells
        for (int i = lowerBoundary; i < upperBoundary; i++) {
            assertFalse(selenium.belongsClass(cellDay.format(i), "rf-cal-sel"), "Cell nr. " + i
                + " should not be selected.");
        }
    }
}
