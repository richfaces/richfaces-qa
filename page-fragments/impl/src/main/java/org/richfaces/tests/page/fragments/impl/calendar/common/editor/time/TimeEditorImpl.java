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
package org.richfaces.tests.page.fragments.impl.calendar.common.editor.time;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.spinner.HoursSpinner12;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.spinner.HoursSpinner24;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.spinner.TimeSignSpinner;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.spinner.TimeSignSpinner.TimeSign;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.spinner.TimeSpinner;

/**
 * Component for editing calendar's time
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TimeEditorImpl implements TimeEditor {

    @Root
    private WebElement root;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    @FindBy(xpath = "//td[@class='rf-cal-timepicker-inp']/table/tbody/tr/td/table[tbody/tr/td/input[contains(@id, 'TimeHours')]]")
    private HoursSpinner12 hoursSpinner12;
    @FindBy(xpath = "//td[@class='rf-cal-timepicker-inp']/table/tbody/tr/td/table[tbody/tr/td/input[contains(@id, 'TimeHours')]]")
    private HoursSpinner24 hoursSpinner24;
    @FindBy(xpath = "//td[@class='rf-cal-timepicker-inp']/table/tbody/tr/td/table[tbody/tr/td/input[contains(@id, 'TimeMinutes')]]")
    private TimeSpinner minutesSpinner;
    @FindBy(xpath = "//td[@class='rf-cal-timepicker-inp']/table/tbody/tr/td/table[tbody/tr/td/input[contains(@id, 'TimeSeconds')]]")
    private TimeSpinner secondsSpinner;
    @FindBy(xpath = "//td[@class='rf-cal-timepicker-inp']/table/tbody/tr/td/table[tbody/tr/td/input[contains(@id, 'TimeSign')]]")
    private TimeSignSpinner timeSignSpinner;
    //
    @FindBy(css = "div[id$=TimeEditorButtonOk]")
    private WebElement okButtonElement;
    @FindBy(css = "div[id$=TimeEditorButtonCancel]")
    private WebElement cancelButtonEleme;
    //
    private static final int defaultHours = 12;
    private static final int defaultMinutes = 0;
    private static final int defaultSeconds = 0;

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public void confirmTime() {
        okButtonElement.click();
    }

    @Override
    public void cancelTime() {
        cancelButtonEleme.click();
    }

    @Override
    public DateTime getTime() {
        int seconds = (getSecondsSpinner() != null ? getSecondsSpinner().getValue() : defaultSeconds);
        int minutes = (getMinutesSpinner() != null ? getMinutesSpinner().getValue() : defaultMinutes);
        int hours = (getHoursSpinner() != null ? getHoursSpinner().getValue() : defaultHours);
        DateTime result = new DateTime()
                .withHourOfDay(hours)
                .withMinuteOfHour(minutes)
                .withSecondOfMinute(seconds);
        TimeSignSpinner tss = getTimeSignSpinner();
        if (tss != null) {
            switch (tss.getValue()) {
                case AM:
                    if (result.getHourOfDay() == 12) {//12:xx am -> 00:xx
                        result = result.minusHours(12);
                    }
                    break;
                case PM:
                    if (result.getHourOfDay() != 12) {
                        result = result.plusHours(12);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown switch");
            }
        }
        return result;
    }

    @Override
    public TimeEditor setTime(DateTime time, SetValueBy inputType) {
        return setTime(time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), inputType);
    }

    private TimeEditor setTime(int hours, int minutes, int seconds, SetValueBy by) {
        TimeSign timeSign = null;
        TimeSpinner actSecondsSpinner = getSecondsSpinner();
        TimeSignSpinner acttimeSignSpinner = getTimeSignSpinner();
        if (acttimeSignSpinner != null) {//there is a time sign spinner, --> fix the hours
            timeSign = TimeSign.AM;
            if (hours >= 12) {
                timeSign = TimeSign.PM;
            }
            if (hours > 12) {//>12h -> XXh pm
                hours -= 12;
            }
            if (hours == 0) {//00:xx -> 12:xx am
                hours = 12;
            }
        }
        getHoursSpinner().setValueBy(hours, by);
        getMinutesSpinner().setValueBy(minutes, by);
        if (actSecondsSpinner != null) {
            actSecondsSpinner.setValueBy(seconds, by);
        }
        if (acttimeSignSpinner != null) {
            acttimeSignSpinner.setValueBy(timeSign, by);
        }
        return this;
    }

    private TimeSpinner getSecondsSpinner() {
        if (secondsSpinner.isVisible()) {
            return secondsSpinner;
        }
        return null;
    }

    private TimeSpinner getMinutesSpinner() {
        if (minutesSpinner.isVisible()) {
            return minutesSpinner;
        }
        return null;
    }

    private TimeSpinner getHoursSpinner() {
        if (getTimeSignSpinner() == null) {
            if (hoursSpinner24.isVisible()) {
                return hoursSpinner24;
            }
        } else {
            if (hoursSpinner12.isVisible()) {
                return hoursSpinner12;
            }
        }
        return null;
    }

    private TimeSignSpinner getTimeSignSpinner() {
        if (timeSignSpinner.isVisible()) {
            return timeSignSpinner;
        }
        return null;
    }
}
