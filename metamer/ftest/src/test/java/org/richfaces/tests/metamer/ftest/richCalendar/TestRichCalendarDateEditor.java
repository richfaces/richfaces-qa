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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.calendar.common.HeaderControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth.YearAndMonthEditor;
import org.testng.annotations.Test;

/**
 * Test case for date editor of a calendar on page faces/components/richCalendar/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichCalendarDateEditor extends AbstractCalendarTest<MetamerPage> {

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
        MetamerPage.waitRequest(calendar.openPopup().getFooterControls(), WaitRequestType.XHR).setTodaysDate();

        HeaderControls hc = calendar.openPopup().getProxiedHeaderControls();

        DateTime canceledDate = todayMidday.plusMonths(2).plusYears(2);
        YearAndMonthEditor openYearAndMonthEditor = hc.openYearAndMonthEditor();
        MetamerPage.waitRequest(openYearAndMonthEditor, WaitRequestType.NONE).selectDate(canceledDate).cancelDate();
        assertFalse(openYearAndMonthEditor.isVisible());

        DateTime yearAndMonthAfter = hc.getYearAndMonth();
        assertEquals(yearAndMonthAfter.getYear(), todayMidday.getYear());
        assertEquals(yearAndMonthAfter.getMonthOfYear(), todayMidday.getMonthOfYear());
    }

    @Test
    public void testNextDecadeButton() {
        HeaderControls hc = calendar.openPopup().getProxiedHeaderControls();
        List<Integer> displayedYears = hc.openYearAndMonthEditor().getDisplayedYears();
        MetamerPage.waitRequest(hc.openYearAndMonthEditor(), WaitRequestType.NONE).nextDecade();

        assertNotEquals(hc.openYearAndMonthEditor().getDisplayedYears(), displayedYears);
        Integer selectedYear = hc.openYearAndMonthEditor().getSelectedYear();
        assertNull(selectedYear);//no selected Year found

        MetamerPage.waitRequest(hc.openYearAndMonthEditor(), WaitRequestType.NONE).confirmDate();
        DateTime yearAndMonth = hc.getYearAndMonth();
        assertEquals(yearAndMonth.getYear(), todayMidday.getYear());
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.plusYears(1).getMonthOfYear());
    }

    @Test
    public void testPreviousDecadeButton() {
        HeaderControls hc = calendar.openPopup().getProxiedHeaderControls();
        List<Integer> displayedYears = hc.openYearAndMonthEditor().getDisplayedYears();
        MetamerPage.waitRequest(hc.openYearAndMonthEditor(), WaitRequestType.NONE).previousDecade();

        assertNotEquals(hc.openYearAndMonthEditor().getDisplayedYears(), displayedYears);
        Integer selectedYear = hc.openYearAndMonthEditor().getSelectedYear();
        assertNull(selectedYear);//no selected Year found
        //confirm previously selected date
        MetamerPage.waitRequest(hc.openYearAndMonthEditor(), WaitRequestType.NONE).confirmDate();
        //so the date stays at previous value, check it
        DateTime yearAndMonth = hc.getYearAndMonth();
        assertEquals(yearAndMonth.getYear(), todayMidday.getYear());
        assertEquals(yearAndMonth.getMonthOfYear(), todayMidday.getMonthOfYear());
    }

    @Test
    public void testSelectMonthAndYear() {
        DateTime someFutureDate = todayMidday.plusMonths(1).plusYears(1);
        HeaderControls hc = calendar.openPopup().getProxiedHeaderControls();
        YearAndMonthEditor yearAndMonthEditor = hc.openYearAndMonthEditor();
        MetamerPage.waitRequest(yearAndMonthEditor, WaitRequestType.NONE).selectDate(someFutureDate).confirmDate();

        assertFalse(yearAndMonthEditor.isVisible());
        DateTime yearAndMonthAfter = hc.getYearAndMonth();
        assertEquals(yearAndMonthAfter.getYear(), someFutureDate.getYear());
        assertEquals(yearAndMonthAfter.getMonthOfYear(), someFutureDate.getMonthOfYear());
    }

    @Test
    public void testShowDateEditor() {
        YearAndMonthEditor yearAndMonthEditor = Graphene.guardNoRequest(calendar.openPopup().getHeaderControls()).openYearAndMonthEditor();
        assertTrue(yearAndMonthEditor.isVisible());
        List<String> shortMonthLabels = yearAndMonthEditor.getShortMonthsLabels();
        assertEquals(shortMonthLabels, Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        List<Integer> displayedYears = yearAndMonthEditor.getDisplayedYears();
        assertTrue(displayedYears.contains(todayMidday.getYear()));
    }
}
