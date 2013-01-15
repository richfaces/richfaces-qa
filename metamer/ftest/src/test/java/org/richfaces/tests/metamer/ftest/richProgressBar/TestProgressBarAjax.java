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

import com.google.common.base.Function;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.bean.rich.RichProgressBarBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richProgressBar/ajaxMode.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @since 4.3.0.M3
 */
public class TestProgressBarAjax extends AbstractWebDriverTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("HH:mm:ss.SSS");
    //
    @Inject
    @Use(empty = false)
    private Integer testInterval;
    //
    @Page
    private ProgressBarPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/ajaxMode.xhtml");
    }

    private long countAverage(List<DateTime> times) {
        long total = 0;
        for (int i = 0; i < times.size() - 1; i++) {
            total += times.get(i + 1).getMillis() - times.get(i).getMillis();
        }
        return Math.abs(total / (times.size() - 1));
    }

    private int getExpectedNumberOfUpdates() {
        int max = Integer.valueOf(progressBarAttributes.get(ProgressBarAttributes.maxValue));
        double interval = Integer.valueOf(progressBarAttributes.get(ProgressBarAttributes.interval)) / 1000.0;//s
        double maximumTime = (RichProgressBarBean.UPDATE_INTERVAL / 1000.0) * max;//s
        int updates = (int) (maximumTime / interval) - 1; //-1 to be sure that no invalid values will be gathered
        if (updates < 2) {
            throw new RuntimeException("The measurement will not be possible. "
                    + "Reduce the @interval or increase the @maxValue.");
        }
        return updates;
    }

    private int getExpectedRunTime() {
        int max = Integer.valueOf(progressBarAttributes.get(ProgressBarAttributes.maxValue));
        double maximumTime = (RichProgressBarBean.UPDATE_INTERVAL / 1000.0) * max;//s
        return (int) (maximumTime * 1.5);
    }

    private int getProgress() {
        String width = page.progress.getCssValue("width");
        float result;
        if (width.contains("%")) {
            result = Float.parseFloat(width.replace("%", ""));
        } else {
            result = Float.parseFloat(width.replace("px", "")) / 2; // progress bar width is 200px
        }
        result *= Integer.valueOf(progressBarAttributes.get(ProgressBarAttributes.maxValue)) / 100.0;//normalize
        return (int) result;
    }

    @Test
    public void testData() {
        progressBarAttributes.set(ProgressBarAttributes.data, "RichFaces 4");
        progressBarAttributes.set(ProgressBarAttributes.oncomplete, "data = event.data");

        MetamerPage.waitRequest(page.startButton, WaitRequestType.XHR).click();
        String reqTime = page.requestTime.getText();
        Graphene.waitGui().withMessage("Page was not updated")
                .until(Graphene.element(page.requestTime).not().text().equalTo(reqTime));

        String data = (String) ((JavascriptExecutor) driver).executeScript("return data");
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    @Test
    public void testEvents() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 5);//the test will be quicker
        progressBarAttributes.set(ProgressBarAttributes.onbegin, "metamerEvents += \"begin \"");
        progressBarAttributes.set(ProgressBarAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        progressBarAttributes.set(ProgressBarAttributes.oncomplete, "metamerEvents += \"complete \"");

        executeJS("metamerEvents = \"\"");

        MetamerPage.waitRequest(page.startButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().withTimeout(getExpectedRunTime(), TimeUnit.SECONDS)
                .withMessage("Progress bar should disappear after it finishes.")
                .until(ElementPresent.getInstance().element(page.restartButton));

        String[] events = ((String) executeJS("return metamerEvents")).split(" ");

        assertEquals(events.length % 3, 0, "Number of events should be a multiple of 3.");
        for (int i = 0; i < events.length; i += 3) {
            assertEquals(events[i], "begin", "Event nr." + i + " should be begin.");
            assertEquals(events[i + 1], "beforedomupdate", "Event nr." + (i + 1) + " should be beforedomupdate.");
            assertEquals(events[i + 2], "complete", "Event nr." + (i + 2) + " should be complete.");
        }
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
    @Use(field = "testInterval", ints = { 1200, 1600, 2200 })
    public void testInterval() {
        testOneRunOfProgressBar(page.startButton, testInterval);
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

    private void testOneRunOfProgressBar(WebElement button, int interval) {
        progressBarAttributes.set(ProgressBarAttributes.interval, interval);

        long delta = (long) (interval * 0.3);
        List<String> timesString = new ArrayList<String>();
        List<String> labelsList = new ArrayList<String>();
        List<Integer> progressList = new ArrayList<Integer>();

        MetamerPage.waitRequest(button, WaitRequestType.XHR).click();
        String timeString = page.requestTime.getText();
        timesString.add(timeString);
        for (int i = 0; i < getExpectedNumberOfUpdates(); i++) {
            Graphene.waitAjax().withTimeout(delta, TimeUnit.SECONDS).until(new StringFunction(timeString));
            timeString = page.requestTime.getText();
            timesString.add(timeString);
            labelsList.add(page.label.getText().replace(" %", ""));
            progressList.add(getProgress());
        }
        labelsList.remove("");//there can be empty string from label after progress finishes

        List<DateTime> timesList = new ArrayList<DateTime>(timesString.size());
        for (String s : timesString) {
            timesList.add(FORMATTER.parseDateTime(s));
        }

        long average = countAverage(timesList);
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
            first = progressList.get(i);
            second = progressList.get(i + 1);
            assertTrue(progressList.get(i) <= progressList.get(i + 1), "Progress of progress bar should be increasing: " + first + "!<= " + second);
        }

        Graphene.waitGui().withTimeout(getExpectedRunTime(), TimeUnit.SECONDS)
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

    @Test
    public void testOnfinish() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 5);//the test will be quicker
        progressBarAttributes.set(ProgressBarAttributes.onfinish, "metamerEvents += \"finish \"");

        executeJS("metamerEvents = \"\"");

        MetamerPage.waitRequest(page.startButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().withTimeout(getExpectedRunTime(), TimeUnit.SECONDS)
                .withMessage("Progress bar should disappear after it finishes.")
                .until(ElementPresent.getInstance().element(page.restartButton));

        String[] events = ((String) executeJS("return metamerEvents")).split(" ");

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
    public void testProgress() throws ParseException {
        testOneRunOfProgressBar(page.startButton, 1400);
        testOneRunOfProgressBar(page.restartButton, 1400);
    }

    @Test
    public void testRendered() {
        progressBarAttributes.set(ProgressBarAttributes.rendered, false);
        assertFalse(ElementPresent.getInstance().element(page.progressBar).apply(driver),
                "Progress bar should not be rendered when rendered=false.");
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

    private class StringFunction implements Function<WebDriver, Boolean> {

        private final String from;

        public StringFunction(String from) {
            this.from = from;
        }

        @Override
        public Boolean apply(WebDriver v) {
            try {
                return !page.requestTime.getText().equals(this.from);
            } catch (StaleElementReferenceException ex) {
                return false;
            }
        }
    }
}
