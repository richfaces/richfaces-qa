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
import java.util.List;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesDayPicker implements DayPicker {

    @Root
    private WebElement root;
    @Drone
    private WebDriver driver;
    //
    @FindBy(css = "tr[id$=WeekDay]")
    private WebElement weekDaysBarElement;
    @FindBy(css = "tr[id$=WeekDay] > td")
    private List<WebElement> weekDaysLabels;
    @FindBy(css = "tr[id*=WeekNum]")
    private List<CalendarWeek> weeks;
    @FindBy(css = "td[id*=DayCell]:not(.rf-cal-boundary-day):not(.rf-cal-day-lbl)")
    private List<WebElement> monthDays;
    @FindBy(css = "td[id*=DayCell].rf-cal-boundary-day")
    private List<WebElement> boundaryDays;
    @FindBy(css = "td[id*=DayCell].rf-cal-sel")
    private WebElement selectedDay;
    @FindBy(css = "td[id*=DayCell].rf-cal-today")
    private WebElement todayDay;
    //

    @Override
    public CalendarDays getBoundaryDays() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        return new CalendarDays(boundaryDays);
    }

    @Override
    public CalendarDays getMonthDays() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        return new CalendarDays(monthDays);
    }

    @Override
    public CalendarDay getSelectedDay() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        if (Graphene.element(selectedDay).isPresent().apply(driver)) {
            return new CalendarDay(selectedDay);
        }
        return null;
    }

    @Override
    public CalendarDays getSpecificDays(int weekDayPosition) {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        Validate.isTrue(weekDayPosition > 0 && weekDayPosition < 8);
        CalendarDays result = new CalendarDays(6);
        for (CalendarWeek calendarWeek : getWeeks()) {
            result.add(calendarWeek.getCalendarDays().get(weekDayPosition - 1));
        }
        return result;
    }

    @Override
    public CalendarDay getTodayDay() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        if (Graphene.element(todayDay).isPresent().apply(driver)) {
            return new CalendarDay(todayDay);
        }
        return null;
    }

    @Override
    public CalendarWeek getWeek(int weekFromActCalendar) {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        Validate.isTrue(weekFromActCalendar > 0 && weekFromActCalendar < 7);
        return getWeeks().get(weekFromActCalendar - 1);
    }

    @Override
    public List<String> getWeekDayShortNames() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        if (Graphene.element(weekDaysBarElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Week days bar is not visible");
        }
        List<String> result = new ArrayList<String>(8);

        for (WebElement label : weekDaysLabels) {
            result.add(label.getText().trim());
        }
        result.remove(0);
        return result;
    }

    @Override
    public WebElement getWeekDaysBarElement() {
        return weekDaysBarElement;
    }

    @Override
    public List<CalendarWeek> getWeeks() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        return weeks;
    }

    @Override
    public List<Integer> getWeeksNumbers() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        List<Integer> result = new ArrayList<Integer>(6);

        for (CalendarWeek week : getWeeks()) {
            result.add(week.getWeekNumber());
        }
        return result;
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    @Override
    public void selectDayInMonth(DateTime dateTime) {
        selectDayInMonth(dateTime.getDayOfMonth());
    }

    @Override
    public void selectDayInMonth(int day) {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        Validate.isTrue(day > 0 && day < 32);
        Validate.isTrue(monthDays.size() >= day);

        String jq = "td[id*=DayCell]:not('.rf-cal-boundary-day'):not('.rf-cal-day-lbl'):contains('" + day + "')";
        new CalendarDay(root.findElement(ByJQuery.jquerySelector(jq)))
                .select();
    }
}
