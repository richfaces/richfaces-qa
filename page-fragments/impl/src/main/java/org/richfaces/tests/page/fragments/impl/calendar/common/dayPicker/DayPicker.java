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
package org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker;

import java.util.List;
import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;

/**
 * Component for picking dates in Calendar.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface DayPicker extends VisibleComponent {

    /**
     * Returns all boundary days (not this month days) as list of Calendar days.
     * @return
     */
    List<CalendarDay> getBoundaryDays();

    /**
     * Returns all month's days in current DayPicker view.
     * @return
     */
    List<CalendarDay> getMonthDays();

    /**
     * Returns the selected day in this DayPicker view. If no found than null is returned.
     * @return
     */
    CalendarDay getSelectedDay();

    /**
     * Returns specific days of all weeks (with boundary days) in this DayPicker view.
     * @param weekDayPosition position of day in week. I.e. 1 = sunday (with standard calendar settings)
     * @return
     */
    List<CalendarDay> getSpecificDays(int weekDayPosition);

    /**
     * Returns today CalendarDay from current DayPicker view. If no found than null is returned.
     * @return
     */
    CalendarDay getTodayDay();

    /**
     * Returns week <1;6> from current DayPicker view.
     * @param weekFromActCalendar <1;6>
     * @return
     */
    CalendarWeek getWeek(int weekFromActCalendar);

    /**
     * Returns short names of weekdays (Sun, Mon...)
     * @return
     */
    List<String> getWeekDayShortNames();

    WebElement getWeekDaysBarElement();

    /**
     * Returns all weeks from current DayPicker view
     * @return
     */
    List<CalendarWeek> getWeeks();

    /**
     * Returns all week numbers from current DayPicker view.
     * @return
     */
    List<Integer> getWeeksNumbers();

    /**
     * Set day in current view. Indexed from 1.
     */
    void setDayInMonth(int day);

    /**
     * Set day in current view. Indexed from 1;
     * @param dateTime
     */
    void setDayInMonth(DateTime dateTime);
}
