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
package org.richfaces.tests.metamer.ftest.richProgressBar;

import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richProgressBar/ajaxMode.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22367 $
 */
public class TestProgressBarAjax extends AbstractAjocadoTest {

    private JQueryLocator progressBar = pjq("div[id$=progressBar]");
    private JQueryLocator initialOutput = pjq("div.rf-pb-init > span");
    private JQueryLocator completeOutput = pjq("div.rf-pb-fin > span");
    private JQueryLocator startButton = pjq("div.rf-pb-init > input");
    private JQueryLocator restartButton = pjq("div.rf-pb-fin > input");
    private JQueryLocator remain = pjq("div.rf-pb-rmng");
    private JQueryLocator progress = pjq("div.rf-pb-prgs");
    private JQueryLocator complete = pjq("div[id$=complete]");
    private JQueryLocator label = pjq("div.rf-pb-lbl");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/ajaxMode.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(initialOutput), "Initial output is not present on the page.");
        assertFalse(selenium.isElementPresent(completeOutput), "Complete output should not be present on the page.");
        assertTrue(selenium.isVisible(startButton), "Start button is not present on the page.");
        assertFalse(selenium.isElementPresent(restartButton), "Restart button should not be present on the page.");

        if (selenium.isElementPresent(remain)) {
            assertFalse(selenium.isVisible(remain), "Progress bar should not show progress.");
        }
        if (selenium.isElementPresent(progress)) {
            assertFalse(selenium.isVisible(progress), "Progress bar should not show progress.");
        }
        if (selenium.isElementPresent(label)) {
            assertFalse(selenium.isVisible(label), "Progress bar should not show progress.");
        }
    }

    @Test
    public void testStart() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(startButton);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertTrue(selenium.isVisible(progressBar), "Progress bar is not present on the page.");
        assertFalse(selenium.isElementPresent(initialOutput), "Initial output should not be present on the page.");
        assertFalse(selenium.isElementPresent(completeOutput), "Complete output should not be present on the page.");
        assertFalse(selenium.isElementPresent(startButton), "Start button should not be present on the page.");
        assertFalse(selenium.isElementPresent(restartButton), "Restart button should not be present on the page.");

        assertTrue(selenium.isElementPresent(remain), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(remain), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(progress), "Progress bar should not show progress.");
        assertFalse(selenium.isElementPresent(complete), "Progress bar should not show progress.");

        String labelValue = selenium.getText(label);
        assertTrue("1 %".equals(labelValue) || "2 %".equals(labelValue), "Progress bar's label after start should be \"1 %\" or \"2 %\".");
    }

    @Test
    public void testProgress() throws ParseException {
        testOneRunOfProgressBar(startButton, 500);
        testOneRunOfProgressBar(restartButton, 500);
    }

    @Test
    public void testData() {
        selenium.type(pjq("input[type=text][id$=dataInput]"), "RichFaces 4");
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "data = event.data");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(startButton);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        reqTime = selenium.getText(time);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    @Test
    public void testInterval() throws ParseException {
        selenium.type(pjq("input[type=text][id$=intervalInput]"), "1000");
        selenium.waitForPageToLoad();

        testOneRunOfProgressBar(startButton, 1000);
    }

    @Test
    public void testEvents() {
        selenium.type(pjq("input[type=text][id$=onbeginInput]"), "metamerEvents += \"begin \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeforedomupdateInput]"), "metamerEvents += \"beforedomupdate \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "metamerEvents += \"complete \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        guardXhr(selenium).click(startButton);
        waitGui.timeout(55000).failWith("Progress bar should disappear after it finishes.").until(elementPresent.locator(restartButton));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length % 3, 0, "Number of events should be a multiple of 3.");
        for (int i = 0; i < events.length; i += 3) {
            assertEquals(events[i], "begin", "Event nr." + i + " should be begin.");
            assertEquals(events[i + 1], "beforedomupdate", "Event nr." + (i + 1) + " should be beforedomupdate.");
            assertEquals(events[i + 2], "complete", "Event nr." + (i + 2) + " should be complete.");
        }
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, progressBar);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, progressBar);
    }

    @Test
    public void testOnfinish() {
        selenium.type(pjq("input[type=text][id$=onfinishInput]"), "metamerEvents += \"finish \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        guardXhr(selenium).click(startButton);
        waitGui.timeout(55000).failWith("Progress bar should disappear after it finishes.").until(elementPresent.locator(restartButton));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 1, "Only one event should be fired.");
        assertEquals(events[0], "finish", "Onfinish doesn't work.");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, progressBar);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, progressBar);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, progressBar);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, progressBar);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, progressBar);
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(progressBar), "Progress bar should not be rendered when rendered=false.");
    }

    private void testOneRunOfProgressBar(JQueryLocator button, int interval) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
        long delta = (long) (interval * 0.5);
        Set<String> timesSet = new TreeSet<String>();
        List<String> labelsList = new ArrayList<String>();
        List<String> progressList = new ArrayList<String>();

        guardXhr(selenium).click(button);

        for (int i = 0; i < 40; i++) {
            waitFor(delta);
            timesSet.add(selenium.getText(time));
            labelsList.add(selenium.getText(label).replace(" %", ""));
            String width = selenium.getAttribute(progress.getAttribute(Attribute.STYLE)).replace("%", "").replace("width:", "");
            progressList.add(width.replace(";", "").trim());
        }

        Date[] timesArray = new Date[timesSet.size()];
        List<String> timesList = new ArrayList<String>(timesSet);

        for (int i = 1; i < timesList.size(); i++) {
            timesArray[i] = sdf.parse(timesList.get(i));
        }

        long average = countAverage(timesArray);
        assertTrue(Math.abs(average - interval) < delta, "Average interval " + average + " is too far from set value (" + interval + ")");
        assertFalse(average < interval, "Average interval " + average + " cannot be smaller than set value (" + interval + ")");

        int first = 0;
        int second = -1;

        for (int i = 0; i < labelsList.size() - 1; i++) {
            first = Integer.parseInt(labelsList.get(i));
            second = Integer.parseInt(labelsList.get(i + 1));
            assertTrue(first <= second, "Number of percent in label should be increasing: " + first + "!<= " + second);
        }

        for (int i = 0; i < progressList.size() - 1; i++) {
            first = Integer.parseInt(progressList.get(i));
            second = Integer.parseInt(progressList.get(i + 1));
            assertTrue(first <= second, "Progress of progress bar should be increasing: " + first + "!<= " + second);
        }

        waitGui.timeout(40000).failWith("Progress bar should disappear after it finishes.").until(elementPresent.locator(restartButton));
        assertTrue(selenium.isElementPresent(completeOutput), "Complete output should be present on the page.");

        assertTrue(selenium.isVisible(progressBar), "Progress bar is not present on the page.");
        assertFalse(selenium.isElementPresent(initialOutput), "Initial output should not be present on the page.");
        assertTrue(selenium.isVisible(completeOutput), "Complete output should be present on the page.");
        assertFalse(selenium.isElementPresent(startButton), "Start button should not be present on the page.");
        assertTrue(selenium.isVisible(restartButton), "Restart button should be present on the page.");

        assertFalse(selenium.isVisible(remain), "Progress bar should not show progress.");
    }

    private long countAverage(Date[] times) {
        long total = 0L;
        for (int i = 1; i < times.length - 1; i++) {
            total += (times[i].getTime() - times[i + 1].getTime());
        }

        return Math.abs(total / (times.length - 2));
    }
}
