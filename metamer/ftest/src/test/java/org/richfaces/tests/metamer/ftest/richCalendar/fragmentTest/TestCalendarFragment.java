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
package org.richfaces.tests.metamer.ftest.richCalendar.fragmentTest;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.openqa.selenium.Dimension;
import org.richfaces.tests.metamer.ftest.richCalendar.AbstractCalendarTest;
import org.richfaces.tests.metamer.ftest.richCalendar.CalendarAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCalendarFragment extends AbstractCalendarTest {

    private void checkDateTime(DateTime parsedDateTime) {
        assertEquals(parsedDateTime.getYear(), todayMidday.getYear());
        assertEquals(parsedDateTime.getMonthOfYear(), todayMidday.getMonthOfYear());
        assertEquals(parsedDateTime.getDayOfMonth(), todayMidday.getDayOfMonth());
        assertEquals(parsedDateTime.getHourOfDay(), todayMidday.getHourOfDay());
        assertEquals(parsedDateTime.getMinuteOfHour(), todayMidday.getMinuteOfHour());
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @BeforeMethod
    private void manageWindow() {
        driver.manage().window().setSize(new Dimension(1024, 768));
        calendar.advanced().setupDatePattern(calendarAttributes.get(CalendarAttributes.datePattern));
    }

    @Test
    public void testGetDateByJS() {
        calendar.advanced().setupJavaScriptStrategy();
        calendar.setDate(todayMidday);
        checkDateTime(calendar.getDate());
    }

    @Test
    public void testGetDateByWD() {
        calendar.advanced().setupJavaScriptStrategy();
        calendar.setDate(todayMidday);
        calendar.advanced().setupInteractiveStrategy();
        checkDateTime(calendar.getDate());
    }

    @Test
    public void testSetDateByJS() {
        calendar.advanced().setupJavaScriptStrategy();
        calendar.setDate(todayMidday);
        checkDateTime(DateTimeFormat
            .forPattern(calendarAttributes.get(CalendarAttributes.datePattern))
            .parseDateTime(calendar.advanced().getPopupCalendar().getInput().getStringValue()));
    }

    @Test
    public void testSetDateByWD() {
        calendar.advanced().setupInteractiveStrategy();
        calendar.setDate(todayMidday);
        checkDateTime(DateTimeFormat
            .forPattern(calendarAttributes.get(CalendarAttributes.datePattern))
            .parseDateTime(calendar.advanced().getPopupCalendar().getInput().getStringValue()));
    }
}
