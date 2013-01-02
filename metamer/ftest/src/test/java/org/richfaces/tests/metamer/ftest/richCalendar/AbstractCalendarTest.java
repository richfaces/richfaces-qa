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

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.calendarAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth.YearAndMonthEditor;
import org.richfaces.tests.page.fragments.impl.calendar.inline.RichFacesCalendarInlineComponent;
import org.richfaces.tests.page.fragments.impl.calendar.popup.CalendarPopupComponent.OpenedBy;
import org.richfaces.tests.page.fragments.impl.calendar.popup.RichFacesCalendarPopupComponent;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopup;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.PopupFooterControls;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.PopupHeaderControls;
import org.testng.annotations.BeforeMethod;

/**
 * Abstract test case for calendar.
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractCalendarTest extends AbstractWebDriverTest {

    @Page
    protected MetamerPage page;

    protected static final DateTime firstOfJanuary2012 = new DateTime(2012, 1, 1, 12, 0);
    protected DateTime todayMidday = new DateTime().withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);

    @FindBy(css = "span[id$=calendar]")
    protected RichFacesCalendarPopupComponent calendar;
    @FindBy(css = "span[id$=calendar]")
    protected RichFacesCalendarInlineComponent inlineCalendar;

    @BeforeMethod
    public void init() {
        todayMidday = new DateTime().withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
    }

    public void testOpenPopupClickOnInput() {
        CalendarPopup openedPopup = Graphene.guardNoRequest(calendar).openPopup(OpenedBy.INPUT_CLICKING);
        assertTrue(openedPopup.isVisible(), "Popup should be visible.");
    }

    public void testOpenPopupClickOnImage() {
        CalendarPopup openedPopup = Graphene.guardNoRequest(calendar).openPopup(OpenedBy.OPEN_BUTTON_CLICKING);
        assertTrue(openedPopup.isVisible(), "Popup should be visible.");
    }

    public void testHeaderButtons() {
        CalendarPopup openedPopup = calendar.openPopup();
        assertTrue(openedPopup.getHeaderControls().isVisible());
        PopupHeaderControls headerControls = openedPopup.getProxiedHeaderControls();

        assertTrue(isVisible(headerControls.getPreviousYearElement()), "Previous year button should be visible.");
        assertEquals(headerControls.getPreviousYearElement().getText(), "<<", "Button's text");
        assertTrue(isVisible(headerControls.getPreviousMonthElement()), "Previous month button should be visible.");
        assertEquals(headerControls.getPreviousMonthElement().getText(), "<", "Button's text");

        assertTrue(isVisible(headerControls.getNextMonthElement()), "Next month button should be visible.");
        assertEquals(headerControls.getNextMonthElement().getText(), ">", "Button's text");
        assertTrue(isVisible(headerControls.getNextYearElement()), "Next year button should be visible.");
        assertEquals(headerControls.getNextYearElement().getText(), ">>", "Button's text");

        assertTrue(isVisible(headerControls.getYearAndMonthEditorOpenerElement()), "Year and month editor button should be visible.");

        assertTrue(isVisible(headerControls.getCloseButtonElement()), "Close button should be visible.");
        assertEquals(headerControls.getCloseButtonElement().getText(), "x", "Button's text");

        YearAndMonthEditor yearAndMonthEditor = headerControls.openYearAndMonthEditor();
        assertTrue(yearAndMonthEditor.isVisible());
        headerControls.closePopup();
        assertFalse(openedPopup.isVisible());
    }

    private boolean isVisible(WebElement element) {
        return Graphene.element(element).isVisible().apply(driver);
    }

    public void testFooterButtons() {
        CalendarPopup openedPopup = calendar.openPopup();
        assertTrue(openedPopup.getFooterControls().isVisible());
        PopupFooterControls footerControls = openedPopup.getProxiedFooterControls();

        assertTrue(isVisible(footerControls.getTodayButtonElement()), "Today button should be visible.");
        assertEquals(footerControls.getTodayButtonElement().getText(), "Today", "Button's text");

        assertTrue(isVisible(footerControls.getApplyButtonElement()), "Apply button should be visible.");
        assertEquals(footerControls.getApplyButtonElement().getText(), "Apply", "Button's text");

        assertFalse(isVisible(footerControls.getCleanButtonElement()), "Clean button should not be visible.");

        assertFalse(isVisible(footerControls.getTimeEditorOpenerElement()), "Time button should not be visible.");

        footerControls.setTodaysDate();

        assertTrue(isVisible(footerControls.getCleanButtonElement()), "Clean button should be visible.");
        assertEquals(footerControls.getCleanButtonElement().getText(), "Clean", "Button's text");

        assertTrue(isVisible(footerControls.getTimeEditorOpenerElement()), "Time button should be visible.");
        assertEquals(footerControls.getTimeEditorOpenerElement().getText(), "12:00", "Button's text");
    }

    public void testApplyButton() {
        String datePattern = calendarAttributes.get(CalendarAttributes.datePattern);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(datePattern);
        DateTime today = new DateTime();

        CalendarPopup openedPopup = calendar.openPopup();
        PopupFooterControls footerControls = openedPopup.getProxiedFooterControls();

        MetamerPage.waitRequest(footerControls, WaitRequestType.XHR).setTodaysDate();

        assertFalse(openedPopup.isVisible(), "Popup should not be displayed.");

        String dateSetInCalendar = calendar.getInputValue();
        DateTime setTime = formatter.parseDateTime(dateSetInCalendar);

        assertEquals(setTime.getDayOfMonth(), today.getDayOfMonth());
        assertEquals(setTime.getMonthOfYear(), today.getMonthOfYear());
        assertEquals(setTime.getYear(), today.getYear());

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> " + dateSetInCalendar);
    }
}
