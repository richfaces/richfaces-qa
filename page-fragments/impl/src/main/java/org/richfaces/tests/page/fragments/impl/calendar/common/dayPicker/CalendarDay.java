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

import org.openqa.selenium.WebElement;

/**
 * Component for calendar's days.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CalendarDay {

    private final WebElement day;

    public enum DayType {

        boundaryDay("rf-cal-boundary-day"),
        holidayDay("rf-cal-holiday"),
        weekendDay("rf-cal-holiday"),
        clickable("rf-cal-btn"),
        todayDay("rf-cal-today"),
        selectedDay("rf-cal-sel");
        private final String styleClass;

        private DayType(String styleClass) {
            this.styleClass = styleClass;
        }

        private boolean isType(WebElement day) {
            return check(day, styleClass);
        }
    }

    public CalendarDay(WebElement day) {
        this.day = day;
    }

    /**
     * Checks if this day contains chosen styleClass
     * @param styleClass
     */
    public boolean containsStyleClass(String styleClass) {
        return check(day, styleClass);
    }

    /**
     * Parses and returns the element's text representing day number.
     */
    public Integer getDayNumber() {
        return Integer.parseInt(day.getText().trim());
    }

    public WebElement getElement() {
        return day;
    }

    public boolean is(DayType type) {
        return type.isType(day);
    }

    /**
     * Clicks on this day element. Without waiting,
     */
    public void select() {
        day.click();
    }

    private static boolean check(WebElement day, String styleClass) {
        String attribute = day.getAttribute("class");
        if (attribute == null) {
            return false;
        }
        return attribute.contains(styleClass);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.day != null ? this.day.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CalendarDay other = (CalendarDay) obj;
        if (this.day != other.day && (this.day == null || !this.day.equals(other.day))) {
            return false;
        }
        return true;
    }
}
