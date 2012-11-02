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

import java.util.Iterator;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Component for calendar's weeks
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CalendarWeek implements Iterable<CalendarDay> {

    @Root
    private WebElement root;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    //
    @FindBy(css = "td[id*=WeekNumCell]")
    private WebElement weekNumberElement;
    @FindBy(css = "td[id*=DayCell]")
    private List<WebElement> days;

    /**
     * Returns calendar days in this week.
     */
    public CalendarDays getCalendarDays() {
        CalendarDays result = new CalendarDays(7);
        for (WebElement day : days) {
            result.add(new CalendarDay(day));
        }
        return result;
    }

    /**
     * Returns week number.
     */
    public Integer getWeekNumber() {
        if (Graphene.element(weekNumberElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Week numbers are not displayed");
        }
        return Integer.parseInt(weekNumberElement.getText());
    }

    public WebElement getWeekNumberElement() {
        return weekNumberElement;
    }

    @Override
    public Iterator<CalendarDay> iterator() {
        return getCalendarDays().iterator();
    }
}
