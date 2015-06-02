/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Keyboard;
import org.richfaces.fragment.calendar.DayPicker;
import org.richfaces.fragment.calendar.PopupCalendar;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14032 extends AbstractCalendarTest {

    @ArquillianResource
    private Keyboard keyboard;

    @Override
    public String getComponentTestPagePath() {
        return "richCalendar/simple.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14032")
    public void testSelectedYearWillChangeOnlyByArrowsPressing() {
        // set some reference date
        popupCalendar.setDateTime(firstOfJanuary2012);
        PopupCalendar popup = popupCalendar.openPopup();
        // set a different year and month than the reference date
        popup.getHeaderControls().openYearAndMonthEditor().selectDate(todayMidday).confirmDate();
        DayPicker dayPicker = popup.getDayPicker();
        // key press >>> selected day in day picker will NOT change
        keyboard.sendKeys("a");
        assertNull(dayPicker.getSelectedDay());
        // key press >>> selected day in day picker will NOT change
        keyboard.sendKeys(Keys.CONTROL);
        assertNull(dayPicker.getSelectedDay());
        // arrow press >>> selected day in day picker will change
        keyboard.sendKeys(Keys.ARROW_RIGHT);
        assertNotNull(dayPicker.getSelectedDay());
        // 1st january + arrow right >>> 2nd January
        assertEquals(dayPicker.getSelectedDay().getDayNumber(), Integer.valueOf(2));
    }
}
