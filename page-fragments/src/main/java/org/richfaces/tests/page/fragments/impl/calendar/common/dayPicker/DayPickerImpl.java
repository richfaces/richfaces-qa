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
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.CalendarDay.DayType;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class DayPickerImpl implements DayPicker {

    @Root
    private WebElement root;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    //
    @FindBy(css = "tr[id$=WeekDay]")
    private WebElement weekDaysBarElement;
    @FindBy(css = "tr[id$=WeekDay] > td")
    private List<WebElement> weekDaysLabels;
    //TODO: should be rewritten as List of CalendarWeeks after Arquillian can handle it
//  @FindBy(css = "tr[id*=calendarWeekNum]")
//  private List<CalendarWeek> weeks;
    @FindBy(css = "tr[id$=WeekNum1]")
    private CalendarWeek week1;
    @FindBy(css = "tr[id$=WeekNum2]")
    private CalendarWeek week2;
    @FindBy(css = "tr[id$=WeekNum3]")
    private CalendarWeek week3;
    @FindBy(css = "tr[id$=WeekNum4]")
    private CalendarWeek week4;
    @FindBy(css = "tr[id$=WeekNum5]")
    private CalendarWeek week5;
    @FindBy(css = "tr[id$=WeekNum6]")
    private CalendarWeek week6;
    //

    @Override
    public CalendarDays getBoundaryDays() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        CalendarDays result = new CalendarDays(14);
        for (CalendarWeek calendarWeek : getWeeks()) {
            for (CalendarDay day : calendarWeek.getCalendarDays()) {
                if (day.is(DayType.boundaryDay)) {
                    result.add(day);
                }
            }
        }
        return result;
    }

    @Override
    public CalendarDays getMonthDays() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        CalendarDays result = new CalendarDays(42);
        for (CalendarWeek calendarWeek : getWeeks()) {
            result.addAll(calendarWeek.getCalendarDays().removeSpecificDays(DayType.boundaryDay));
        }
        return result;
    }

    @Override
    public CalendarDay getSelectedDay() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot interact with DayPicker.");
        }
        for (CalendarWeek calendarWeek : getWeeks()) {
            for (CalendarDay day : calendarWeek) {
                if (day.is(DayType.selectedDay)) {
                    return day;
                }
            }
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
        List<CalendarWeek> weeks = getWeeks();
        for (CalendarWeek calendarWeek : weeks) {
            CalendarDay today = calendarWeek.getCalendarDays().getSpecificDay(DayType.todayDay);
            if (today != null) {
                return today;
            }
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
        return Arrays.asList(week1, week2, week3, week4, week5, week6);
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
        List<CalendarDay> monthDays = getMonthDays();
        Validate.isTrue(monthDays.size() >= day);
        monthDays.get(day - 1).select();
    }
}
