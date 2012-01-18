/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.testng.annotations.Test;


/**
 * Test case for time editor of a calendar on page faces/components/richCalendar/simple.xhtml.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21568 $
 */
public class TestTimeEditor extends AbstractCalendarTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/simple.xhtml");
    }

    @Test
    public void testShowTimeEditor() {
        selenium.click(input);
        selenium.click(cellDay.format(18));

        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:00", "Button's text");
        guardNoRequest(selenium).click(timeButton);

        assertTrue(selenium.isElementPresent(timeEditor), "Date editor should be present on the page.");
        assertTrue(selenium.isVisible(timeEditor), "Date editor should be visible.");

        assertEquals(selenium.getValue(hoursInput), "12", "Value of the spinner for hours");
        assertEquals(selenium.getValue(minutesInput), "00", "Value of the spinner for hours");
    }

    @Test
    public void testCancelButton() {
        selenium.click(input);
        selenium.click(cellDay.format(18));
        selenium.click(timeButton);

        clickArrow(hoursInputUp, 2);
        assertEquals(selenium.getValue(hoursInput), "14", "Value of the spinner for hours");
        assertEquals(selenium.getValue(minutesInput), "00", "Value of the spinner for hours");

        selenium.click(timeEditorCancel);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:00", "Button's text");
    }

    @Test
    public void testHoursInputClick() {
        selenium.click(input);
        selenium.click(cellDay.format(18));

        selenium.click(timeButton);
        clickArrow(hoursInputUp, 2);
        assertEquals(selenium.getValue(hoursInput), "14", "Value of the spinner for hours");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "14:00", "Button's text");

        selenium.click(timeButton);
        clickArrow(hoursInputDown, 15);
        assertEquals(selenium.getValue(hoursInput), "23", "Value of the spinner for hours");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "23:00", "Button's text");
    }

    @Test
    public void testHoursInputType() {
        selenium.click(input);
        selenium.click(cellDay.format(18));

        selenium.click(timeButton);
        selenium.typeKeys(hoursInput, "14");
        assertEquals(selenium.getValue(hoursInput), "14", "Value of the spinner for hours");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "14:00", "Button's text");

        selenium.click(timeButton);
        selenium.typeKeys(hoursInput, "9");
        assertEquals(selenium.getValue(hoursInput), "09", "Value of the spinner for hours");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "09:00", "Button's text");
    }

    @Test
    public void testMinutesInputClick() {
        selenium.click(input);
        selenium.click(cellDay.format(18));

        selenium.click(timeButton);
        clickArrow(minutesInputUp, 5);
        assertEquals(selenium.getValue(minutesInput), "05", "Value of the spinner for minutes");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:05", "Button's text");

        selenium.click(timeButton);
        clickArrow(minutesInputDown, 15);
        assertEquals(selenium.getValue(minutesInput), "50", "Value of the spinner for minutes");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:50", "Button's text");
    }

    @Test
    public void testMinutesInputType() {
        selenium.click(input);
        selenium.click(cellDay.format(18));

        selenium.click(timeButton);
        selenium.typeKeys(minutesInput, "14");
        assertEquals(selenium.getValue(minutesInput), "14", "Value of the spinner for minutes");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:14", "Button's text");

        selenium.click(timeButton);
        selenium.typeKeys(minutesInput, "9");
        assertEquals(selenium.getValue(minutesInput), "09", "Value of the spinner for minutes");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:09", "Button's text");
    }

    @Test
    public void testSecondsInputClick() {
        selenium.type(pjq("input[type=text][id$=datePatternInput]"), "HH:mm:ss MMMM d, yyyy");
        selenium.waitForPageToLoad();
        selenium.click(input);
        selenium.click(cellDay.format(18));

        selenium.click(timeButton);
        clickArrow(secondsInputUp, 5);
        assertEquals(selenium.getValue(secondsInput), "05", "Value of the spinner for seconds");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:00:05", "Button's text");

        selenium.click(timeButton);
        clickArrow(secondsInputDown, 15);
        assertEquals(selenium.getValue(secondsInput), "50", "Value of the spinner for seconds");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:00:50", "Button's text");
    }

    @Test
    public void testSecondsInputType() {
        selenium.type(pjq("input[type=text][id$=datePatternInput]"), "HH:mm:ss MMMM d, yyyy");
        selenium.waitForPageToLoad();
        selenium.click(input);
        selenium.click(cellDay.format(18));

        selenium.click(timeButton);
        selenium.typeKeys(secondsInput, "14");
        assertEquals(selenium.getValue(secondsInput), "14", "Value of the spinner for seconds");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:00:14", "Button's text");

        selenium.click(timeButton);
        selenium.typeKeys(secondsInput, "9");
        assertEquals(selenium.getValue(secondsInput), "09", "Value of the spinner for seconds");
        selenium.click(timeEditorOk);
        assertTrue(selenium.isVisible(timeButton), "Time button should be visible.");
        assertEquals(selenium.getText(timeButton), "12:00:09", "Button's text");
    }

    /**
     * Clicks on spinner's arrow.
     * @param arrow spinner's up or down arrow locator
     * @param clicks how many times should it be clicked
     */
    private void clickArrow(ElementLocator<?> arrow, int clicks) {
        for (int i = 0; i < clicks; i++) {
            selenium.runScript(new JavaScript("jQuery(\"" + arrow.getRawLocator() + "\").mousedown().mouseup()"));
        }
    }
}
