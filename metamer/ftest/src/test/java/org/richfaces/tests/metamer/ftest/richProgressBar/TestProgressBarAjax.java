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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.progressBarAttributes;
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
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richProgressBar/ajaxMode.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M3
 */
public class TestProgressBarAjax extends AbstractWebDriverTest<ProgressBarPage> {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/ajaxMode.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Progress Bar", "Ajax Mode");
    }

    @Test
    public void testInit() {
        assertTrue(ElementPresent.getInstance().element(page.progressBar).apply(driver),
            "Progress bar is not present on the page.");
        assertTrue(page.progressBar.isDisplayed(), "Progress bar should be visible on the page.");
        assertTrue(page.initialOutput.isDisplayed(), "Initial output should be present on the page.");

        assertFalse(ElementPresent.getInstance().element(page.finishOutput).apply(driver),
            "Finish output should not be present on the page.");
        assertTrue(page.startButton.isDisplayed(), "Start button is not present on the page.");
        assertFalse(ElementPresent.getInstance().element(page.restartButton).apply(driver),
            "Restart button should not be present on the page.");

        if (ElementPresent.getInstance().element(page.remain).apply(driver)) {
            assertFalse(page.remain.isDisplayed(), "Progress bar should not show progress.");
        }
        if (ElementPresent.getInstance().element(page.progress).apply(driver)) {
            assertFalse(page.progress.isDisplayed(), "Progress bar should not show progress.");
        }
        if (ElementPresent.getInstance().element(page.label).apply(driver)) {
            assertFalse(page.label.isDisplayed(), "Progress bar should not show progress.");
        }
    }

    @Test
    public void testStart() {
        MetamerPage.waitRequest(page.startButton, WaitRequestType.XHR).click();

        assertTrue(page.progressBar.isDisplayed(), "Progress bar should be visible on the page.");
        assertFalse(ElementPresent.getInstance().element(page.initialOutput).apply(driver),
            "Initial output should not be present on the page.");
        assertFalse(ElementPresent.getInstance().element(page.finishOutput).apply(driver),
            "Complete output should not be present on the page.");
        assertFalse(ElementPresent.getInstance().element(page.startButton).apply(driver),
            "Start button should not be present on the page.");
        assertFalse(ElementPresent.getInstance().element(page.restartButton).apply(driver),
            "Restart button should not be present on the page.");

        assertTrue(page.remain.isDisplayed(), "Progress bar should show progress.");
        assertTrue(page.progress.isDisplayed(), "Progress bar should not show progress.");

        String labelValue = page.label.getText();
        assertTrue("1 %".equals(labelValue) || "2 %".equals(labelValue),
            "Progress bar's label after start should be \"1 %\" or \"2 %\".");
    }

    @Test
    public void testProgress() throws ParseException {
        testOneRunOfProgressBar(page.startButton, 500);
        testOneRunOfProgressBar(page.restartButton, 500);
    }

    @Test
    public void testData() {
        progressBarAttributes.set(ProgressBarAttributes.data, "RichFaces 4");
        progressBarAttributes.set(ProgressBarAttributes.oncomplete, "data = event.data");

        MetamerPage.waitRequest(page.startButton, WaitRequestType.XHR).click();
        String reqTime = page.requestTime.getText();
        Graphene.waitGui().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        String data = (String) ((JavascriptExecutor) driver).executeScript("return data");
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    @Test
    public void testInterval() throws ParseException {
        progressBarAttributes.set(ProgressBarAttributes.interval, 1000);
        testOneRunOfProgressBar(page.startButton, 1000);
    }

    @Test
    public void testEvents() {
        progressBarAttributes.set(ProgressBarAttributes.onbegin, "metamerEvents += \"begin \"");
        progressBarAttributes.set(ProgressBarAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        progressBarAttributes.set(ProgressBarAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");

        MetamerPage.waitRequest(page.startButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().withTimeout(55, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until(ElementPresent.getInstance().element(page.restartButton));

        String[] events = ((String) ((JavascriptExecutor) driver).executeScript("return metamerEvents")).split(" ");

        assertEquals(events.length % 3, 0, "Number of events should be a multiple of 3.");
        for (int i = 0; i < events.length; i += 3) {
            assertEquals(events[i], "begin", "Event nr." + i + " should be begin.");
            assertEquals(events[i + 1], "beforedomupdate", "Event nr." + (i + 1) + " should be beforedomupdate.");
            assertEquals(events[i + 2], "complete", "Event nr." + (i + 2) + " should be complete.");
        }
    }

    @Test
    public void testOnclick() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onclick, new Actions(driver).click(page.progressBar)
            .build());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.ondblclick,
            new Actions(driver).doubleClick(page.progressBar).build());
    }

    @Test
    public void testOnfinish() {
        progressBarAttributes.set(ProgressBarAttributes.onfinish, "metamerEvents += \"finish \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");

        MetamerPage.waitRequest(page.startButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().withTimeout(55, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until(ElementPresent.getInstance().element(page.restartButton));

        String[] events = ((String) ((JavascriptExecutor) driver).executeScript("return metamerEvents")).split(" ");

        assertEquals(events.length, 1, "Only one event should be fired.");
        assertEquals(events[0], "finish", "Onfinish doesn't work.");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmousedown,
            new Actions(driver).clickAndHold(page.progressBar).build());
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmousemove,
            new Actions(driver).moveToElement(page.progressBar).build());
    }

    @Test
    public void testOnmouseout() {
        testFireEventWithJS(page.progressBar, progressBarAttributes, ProgressBarAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmouseover,
            new Actions(driver).moveToElement(page.progressBar).build());
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmouseup,
            new Actions(driver).click(page.progressBar).build());
    }

    @Test
    public void testRendered() {
        progressBarAttributes.set(ProgressBarAttributes.rendered, false);
        assertFalse(ElementPresent.getInstance().element(page.progressBar).apply(driver),
            "Progress bar should not be rendered when rendered=false.");
    }

    private void testOneRunOfProgressBar(WebElement button, int interval) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
        long delta = (long) (interval * 0.5);
        Set<String> timesSet = new TreeSet<String>();
        List<String> labelsList = new ArrayList<String>();
        List<String> progressList = new ArrayList<String>();

        Graphene.guardXhr(button).click();

        for (int i = 0; i < 40; i++) {
            MetamerPage.waiting(delta);
            timesSet.add(page.requestTime.getText());
            labelsList.add(page.label.getText().replace(" %", ""));
            String width = page.progress.getCssValue("width").replace("%", "");
            progressList.add(width.replace("px", "").trim());
        }

        Date[] timesArray = new Date[timesSet.size()];
        List<String> timesList = new ArrayList<String>(timesSet);

        for (int i = 1; i < timesList.size(); i++) {
            timesArray[i] = sdf.parse(timesList.get(i));
        }

        long average = countAverage(timesArray);
        assertTrue(Math.abs(average - interval) < delta, "Average interval " + average + " is too far from set value ("
            + interval + ")");
        assertFalse(average < interval, "Average interval " + average + " cannot be smaller than set value ("
            + interval + ")");

        int first, second;
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

        Graphene.waitGui().withTimeout(40, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until(ElementPresent.getInstance().element(page.restartButton));
        assertTrue(ElementPresent.getInstance().element(page.finishOutput).apply(driver),
            "Complete output should be present on the page.");

        assertTrue(page.progressBar.isDisplayed(), "Progress bar is not present on the page.");
        assertFalse(ElementPresent.getInstance().element(page.initialOutput).apply(driver),
            "Initial output should not be present on the page.");
        assertTrue(page.finishOutput.isDisplayed(), "Complete output should be present on the page.");
        assertFalse(ElementPresent.getInstance().element(page.startButton).apply(driver),
            "Start button should not be present on the page.");
        assertTrue(page.restartButton.isDisplayed(), "Restart button should be present on the page.");

        assertFalse(page.remain.isDisplayed(), "Progress bar should not show progress.");
    }

    private long countAverage(Date[] times) {
        long total = 0L;
        for (int i = 1; i < times.length - 1; i++) {
            total += (times[i].getTime() - times[i + 1].getTime());
        }

        return Math.abs(total / (times.length - 2));
    }
}
