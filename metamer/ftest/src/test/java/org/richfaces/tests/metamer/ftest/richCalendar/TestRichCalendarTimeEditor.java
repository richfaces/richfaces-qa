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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.calendarAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.joda.time.DateTime;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor.SetValueBy;
import org.testng.annotations.Test;

/**
 * Test case for time editor of a calendar on page faces/components/richCalendar/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21568 $
 */
public class TestRichCalendarTimeEditor extends AbstractCalendarTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Calendar", "Simple");
    }

    @Test
    public void testCancelButton() {
        int plusMinutes = 5;
        MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();
        TimeEditor openedTimeEditor = calendar.openPopup().getFooterControls().openTimeEditor();
        MetamerPage.waitRequest(openedTimeEditor, WaitRequestType.NONE).setTime(todayMidday.plusMinutes(plusMinutes), SetValueBy.buttons);
        DateTime time1 = openedTimeEditor.getTime();
        assertEquals(time1.getMinuteOfHour(), plusMinutes);

        MetamerPage.waitRequest(openedTimeEditor, WaitRequestType.NONE).cancelTime();
        assertFalse(openedTimeEditor.isVisible());
        openedTimeEditor = calendar.openPopup().getFooterControls().openTimeEditor();
        time1 = openedTimeEditor.getTime();
        assertEquals(time1.getHourOfDay(), 12);//default value
        assertEquals(time1.getMinuteOfHour(), 0);//default value
    }

    @Test
    public void testHoursInputClick() {
        testTimeSet(new int[]{ 2, 15 }, Time.hours, SetValueBy.buttons);
    }

    @Test
    public void testHoursInputType() {
        testTimeSet(new int[]{ 2, 15 }, Time.hours, SetValueBy.typing);
    }

    @Test
    public void testMinutesInputClick() {
        testTimeSet(new int[]{ 1, 59 }, Time.minutes, SetValueBy.buttons);
    }

    @Test
    public void testMinutesInputType() {
        testTimeSet(new int[]{ 1, 59 }, Time.minutes, SetValueBy.typing);
    }

    @Test
    public void testSecondsInputClick() {
        calendarAttributes.set(CalendarAttributes.datePattern, "MMM d, yyyy HH:mm:ss");
        testTimeSet(new int[]{ 1, 59 }, Time.seconds, SetValueBy.buttons);
    }

    @Test
    public void testSecondsInputType() {
        calendarAttributes.set(CalendarAttributes.datePattern, "MMM d, yyyy HH:mm:ss");
        testTimeSet(new int[]{ 1, 59 }, Time.seconds, SetValueBy.typing);
    }

    @Test
    public void testShowTimeEditor() {
        MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();
        TimeEditor openedTimeEditor = calendar.openPopup().getFooterControls().openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime time1 = openedTimeEditor.getTime();
        assertEquals(time1.getHourOfDay(), 12);
        assertEquals(time1.getMinuteOfHour(), 0);
    }

    private void testTimeSet(int[] valuesToTest, Time time, SetValueBy interaction) {
        for (int value : valuesToTest) {
            MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();
            TimeEditor openedTimeEditor = calendar.openPopup().getFooterControls().openTimeEditor();
            DateTime changedTime = time.change(todayMidday, value);
            openedTimeEditor.setTime(changedTime, interaction).confirmTime();
            openedTimeEditor = calendar.openPopup().getFooterControls().openTimeEditor();
            DateTime time1 = openedTimeEditor.getTime();
            time.checkTimeChanged(changedTime, time1);
        }
    }

    private enum Time {

        hours {
            @Override
            public DateTime change(DateTime time, int value) {
                return time.plusHours(value);
            }

            @Override
            public void checkTimeChanged(DateTime referenceTime, DateTime changedTime) {
                assertEquals(changedTime.getHourOfDay(), referenceTime.getHourOfDay());
            }
        },
        minutes {
            @Override
            public DateTime change(DateTime time, int value) {
                return time.plusMinutes(value);
            }

            @Override
            public void checkTimeChanged(DateTime referenceTime, DateTime changedTime) {
                assertEquals(changedTime.getMinuteOfHour(), referenceTime.getMinuteOfHour());
            }
        },
        seconds {
            @Override
            public DateTime change(DateTime time, int value) {
                return time.plusSeconds(value);
            }

            @Override
            public void checkTimeChanged(DateTime referenceTime, DateTime changedTime) {
                assertEquals(changedTime.getSecondOfMinute(), referenceTime.getSecondOfMinute());
            }
        };

        public abstract DateTime change(DateTime time, int value);

        public abstract void checkTimeChanged(DateTime referenceTime, DateTime changedTime);
    }
}
