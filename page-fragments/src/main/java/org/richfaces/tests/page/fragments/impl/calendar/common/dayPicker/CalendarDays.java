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

import java.util.ArrayList;

/**
 * Helper container for easier and quicker filtering of list of CalendarDays.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CalendarDays extends ArrayList<CalendarDay> {

    private static final long serialVersionUID = -7732611777601734043L;

    public CalendarDays(int initialCapacity) {
        super(initialCapacity);
    }

    public CalendarDays() {
    }

    public CalendarDays removeSpecificDays(Integer... dayNumber) {
        if (dayNumber == null || dayNumber.length == 0 || this.size() == 0) {
            return this;
        }
        ArrayList<CalendarDay> l = new ArrayList<CalendarDay>();
        for (CalendarDay day : this) {
            for (Integer i : dayNumber) {
                if (day.getDayNumber().equals(i)) {
                    l.add(day);
                    break;
                }
            }
        }
        removeAll(l);
        return this;
    }

    public CalendarDays removeSpecificDays(CalendarDay.DayType... types) {
        if (types == null || types.length == 0 || this.size() == 0) {
            return this;
        }
        ArrayList<CalendarDay> l = new ArrayList<CalendarDay>();
        for (CalendarDay day : this) {
            for (CalendarDay.DayType dayType : types) {
                if (day.is(dayType)) {
                    l.add(day);
                    break;
                }
            }
        }
        removeAll(l);
        return this;
    }

    public CalendarDay getSpecificDay(CalendarDay.DayType type) {
        if (type == null || this.size() == 0) {
            return null;
        }
        for (CalendarDay day : this) {
            if (day.is(type)) {
                return day;
            }
        }
        return null;
    }
}
