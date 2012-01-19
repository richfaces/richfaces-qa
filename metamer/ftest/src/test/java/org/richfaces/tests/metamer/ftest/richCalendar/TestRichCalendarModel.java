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

import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;


/**
 * Test case for basic functionality of calendar on page faces/components/richCalendar/dataModel.xhtml.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22661 $
 */
public class TestRichCalendarModel extends AbstractCalendarTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/dataModel.xhtml");
    }

    @Test(groups = { "4.0.0.Final", "4.2" })
    @IssueTracking("https://issues.jboss.org/browse/RFPL-1222")
    public void testClasses() {
        selenium.click(input);
        String month = selenium.getText(monthLabel);
        selenium.click(nextMonthButton);
        waitGui.failWith("Month did not change.").waitForChange(month, retrieveText.locator(monthLabel));

        for (int i = 7; i < 28; i++) {
            switch (i % 7) {
                case 0:
                case 6:
                    assertTrue(selenium.belongsClass(cellDay.format(i), "yellowDay"),
                        "Weekends should be yellow (cell " + i + ")");
                    break;
                case 2:
                    assertTrue(selenium.belongsClass(cellDay.format(i), "aquaDay"), "Tuesdays should be blue (cell "
                        + i + ")");
                    break;
                case 4:
                    assertTrue(selenium.belongsClass(cellDay.format(i), "aquaDay"), "Thursdays should be blue (cell "
                        + i + ")");
                    break;
                default:
                    assertFalse(selenium.belongsClass(cellDay.format(i), "aquaDay"),
                        "Mondays, Wednesdays and Fridays should not be styled (cell " + i + ")");
                    assertFalse(selenium.belongsClass(cellDay.format(i), "yellowDay"),
                        "Mondays, Wednesdays and Fridays should not be styled (cell " + i + ")");
            }
        }
    }

    @Test(groups = { "4.0.0.Final", "4.2" })
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RFPL-1222")
    public void testApplyButton() {
        selenium.click(input);
        String month = selenium.getText(monthLabel);
        selenium.click(nextMonthButton);
        waitGui.failWith("Month did not change.").waitForChange(month, retrieveText.locator(monthLabel));

        selenium.click(cellDay.format(17));
        String day = selenium.getText(cellDay.format(17));
        month = selenium.getText(monthLabel);

        @SuppressWarnings("unused")
        String selectedDate = null;
        try {
            Date date = new SimpleDateFormat("d MMMM, yyyy hh:mm").parse(day + " " + month + " 12:00");
            selectedDate = new SimpleDateFormat("MMM d, yyyy hh:mm").format(date);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }

        selenium.click(cellDay.format(17));
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
    }
}
