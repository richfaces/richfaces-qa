/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.calendarAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.testng.annotations.Test;

/**
 *  Test keeping visual state (KVS) for rich:calendar
 *  on page faces/components/richCalendar/simple.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestCalendarKVS extends AbstractCalendarTest {

    CalendarInputReloadTester reloadTesterInput = new CalendarInputReloadTester();
    CalendarReloadTester reloadTesterPopup = new CalendarReloadTester();

    @Override
    public URL getTestUrl() {
        return URLUtils.buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Test(groups = {"keepVisualStateTesting"})
    public void testRenderAll() {
        reloadTesterInput.testRerenderAll();
    }

    @Test(groups = {"keepVisualStateTesting"})
    public void testFullPageRefresh() {
        reloadTesterInput.testFullPageRefresh();
    }

    @Test(groups = {"keepVisualStateTesting"})
    public void testRenderAllPopupApplied() {
        reloadTesterPopup.testRerenderAll();
    }

    @Test(groups = {"keepVisualStateTesting"})
    public void testFullPageRefresPopupApplied() {
        reloadTesterPopup.testFullPageRefresh();
    }

    /** Test visual state get by typing date into input */
    private class CalendarInputReloadTester extends ReloadTester<String> {
        @Override
        public void doRequest(String inputValue) {
            calendarAttributes.set(CalendarAttributes.enableManualInput, Boolean.TRUE);
            selenium.type(input, inputValue);
            selenium.click(pjq("input[id$=a4jButton]"));
        }

        @Override
        public void verifyResponse(String inputValue) {
            String inputDate = selenium.getValue(input);
            assertEquals(inputDate, inputValue);
        }

        @Override
        public String[] getInputValues() {
            return new String[] {"Feb 17, 2012 12:00", "Jan 1, 2012 12:00"};
        }
    }

    /** Test visual state get by selection from popup and confirmed by apply button */
    private class CalendarReloadTester extends ReloadTester<String> {

        private String selectedDate = null;

        @Override
        public void doRequest(String inputValue) {
            selenium.click(input);

            selenium.click(cellDay.format(6));
            String day = selenium.getText(cellDay.format(6));
            String month = selenium.getText(monthLabel);
            try {
                Date date = new SimpleDateFormat("d MMMM, yyyy hh:mm").parse(day + " " + month + " 12:00");
                selectedDate = new SimpleDateFormat("MMM d, yyyy hh:mm").format(date);
            } catch (ParseException ex) {
                fail(ex.getMessage());
            }
            guardXhr(selenium).click(applyButton);
        }

        @Override
        public void verifyResponse(String inputValue) {
            String inputDate = selenium.getValue(input);
            assertEquals(inputDate, selectedDate, "Input doesn't contain selected date.");
        }

        @Override
        public String[] getInputValues() {
            return new String[] {""};
        }
    }
}
