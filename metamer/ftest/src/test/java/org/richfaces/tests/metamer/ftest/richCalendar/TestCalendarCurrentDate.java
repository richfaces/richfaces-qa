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

import java.net.URL;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarCurrentDate extends AbstractCalendarTest {

    private DateTimeFormatter dateTimeFormatter;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/currentDate.xhtml");
    }

    @BeforeMethod
    public void initDateFormatter() {
        this.dateTimeFormatter = DateTimeFormat.forPattern(calendarAttributes.get(CalendarAttributes.datePattern));
    }

    @Test
    public void testCurrentDateSet() {
        //set some future date
        int someMonthsInFuture = 21;
        DateTime newReferenceTime = todayMidday.plusMonths(someMonthsInFuture);
        calendarAttributes.set(CalendarAttributes.currentDate, newReferenceTime.toString(dateTimeFormatter));
        //get actual date in popup and parse it (pattern is Month, year)
        DateTime yearAndMonth = popupCalendar.openPopup().getHeaderControls().getYearAndMonth();

        assertEquals(yearAndMonth.getMonthOfYear(), newReferenceTime.getMonthOfYear());
        assertEquals(yearAndMonth.getYear(), newReferenceTime.getYear());
    }

    @Test
    public void testCurrentDateGet() {
        //set date to today
        MetamerPage.waitRequest(popupCalendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();

        //parse the value saved to attribute @currentDate
        DateTime currentDateValue = dateTimeFormatter.parseDateTime(calendarAttributes.get(CalendarAttributes.currentDate));

        assertEquals(currentDateValue.getDayOfMonth(), 1, "The day should be the first day of the month");
        assertEquals(currentDateValue.getMonthOfYear(), todayMidday.getMonthOfYear());
        assertEquals(currentDateValue.getYear(), todayMidday.getYear());
    }
}
