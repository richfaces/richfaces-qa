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

import java.net.URL;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 *  Test keeping visual state (KVS) for rich:calendar
 *  on page faces/components/richCalendar/simple.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichCalendarKVS extends AbstractCalendarTest {

    @FindBy(css = "input[id$=a4jButton]")
    private WebElement a4jbutton;

    @Override
    public URL getTestUrl() {
        return URLUtils.buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    private void submitWithA4jSubmitBtn() {
        MetamerPage.waitRequest(a4jbutton, WaitRequestType.XHR).click();
    }

    @Test(groups = { "keepVisualStateTesting" })
    public void testFullPageRefresPopupApplied() {
        CalendarPopupReloadTester reloadTesterPopup = new CalendarPopupReloadTester();
        reloadTesterPopup.testFullPageRefresh();
    }

    @Test(groups = { "keepVisualStateTesting" })
    public void testFullPageRefresh() {
        CalendarInputReloadTester reloadTesterInput = new CalendarInputReloadTester();
        reloadTesterInput.testFullPageRefresh();
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12300")
    @Test(groups = { "keepVisualStateTesting", "4.Future" })
    public void testRenderAll() {
        CalendarInputReloadTester reloadTesterInput = new CalendarInputReloadTester();
        reloadTesterInput.testRerenderAll();
    }

    @Test(groups = { "keepVisualStateTesting" })
    public void testRenderAllPopupApplied() {
        CalendarPopupReloadTester reloadTesterPopup = new CalendarPopupReloadTester();
        reloadTesterPopup.testRerenderAll();
    }

    /** Test visual state get by typing date into input */
    private class CalendarInputReloadTester extends ReloadTester<String> {

        public CalendarInputReloadTester() {
            super(page);
        }

        @Override
        public void doRequest(String inputValue) {
            calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
            calendar.getInput().clear();
            calendar.getInput().sendKeys(inputValue);
            submitWithA4jSubmitBtn();
        }

        @Override
        public void verifyResponse(String inputValue) {
            assertEquals(calendar.getInputValue(), inputValue);
        }

        @Override
        public String[] getInputValues() {
            return new String[]{ "Feb 17, 2012 12:00", "Jan 1, 2012 12:00" };
        }
    }

    /** Test visual state get by selection from popup and confirmed by apply button */
    private class CalendarPopupReloadTester extends ReloadTester<String> {

        public CalendarPopupReloadTester() {
            super(page);
        }

        private String selectedDate = null;

        @Override
        public void doRequest(String inputValue) {
            MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();
            selectedDate = calendar.getInputValue();
        }

        @Override
        public void verifyResponse(String inputValue) {
            assertEquals(calendar.getInputValue(), selectedDate, "Input doesn't contain selected date.");
        }

        @Override
        public String[] getInputValues() {
            return new String[]{ "" };
        }
    }
}
