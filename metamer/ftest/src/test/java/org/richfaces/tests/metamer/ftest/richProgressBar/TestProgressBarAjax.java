/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.bean.rich.RichProgressBarBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richProgressBar/ajaxMode.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @since 4.3.0.M3
 */
public class TestProgressBarAjax extends AbstractWebDriverTest {

    private final Attributes<ProgressBarAttributes> progressBarAttributes = getAttributes();

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("HH:mm:ss.SSS");
    private final Integer[] ints = { 1200, 1600, 2200 };

    private Integer testInterval;
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

    /**
     * @return in seconds
     */
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
        testData(new Action() {
            @Override
            public void perform() {
                MetamerPage.requestTimeChangesWaiting(page.startButton).click();
                // waiting for next request, which will carry the data
                String reqTime = page.getRequestTimeElement().getText();
                Graphene.waitAjax().withMessage("Page was not updated")
                    .until().element(page.getRequestTimeElement()).text().not().equalTo(reqTime);
            }
        });
    }

    @Test(groups = "smoke")
    public void testEnabled() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 100);//longer progress
        int timeout = Integer.parseInt(progressBarAttributes.get(ProgressBarAttributes.interval)) + 300;
        MetamerPage.requestTimeChangesWaiting(page.startButton).click();
        waiting(timeout * 2);//wait for 2 requests
        //waiting for next request, which will carry the data
        MetamerPage.requestTimeChangesWaiting(page.stopPollingButton).click();
        waiting(timeout);//wait for the last request
        String reqTime = page.getRequestTimeElement().getText();
        try {
            Graphene.waitModel()
                .withTimeout(timeout, TimeUnit.MILLISECONDS)
                .until().element(page.getRequestTimeElement()).text().not().equalTo(reqTime);
        } catch (TimeoutException e) {// OK
            String progress = Utils.getTextFromHiddenElement(page.label).replace("%", "").trim();
            Assert.assertTrue(Integer.valueOf(progress) < 100, "Progress should be lower than 100 %");
            return;
        }
        Assert.fail("The request time should not change after polling is stopped.");
    }

    @Test
    public void testEvents() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 5);//the test will be quicker
        progressBarAttributes.set(ProgressBarAttributes.onbegin, "metamerEvents += \"begin \"");
        progressBarAttributes.set(ProgressBarAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        progressBarAttributes.set(ProgressBarAttributes.oncomplete, "metamerEvents += \"complete \"");

        long expectedRunTime = getExpectedRunTime();
        executeJS("metamerEvents = \"\"");

        MetamerPage.requestTimeChangesWaiting(page.startButton).click();
        Graphene.waitAjax()
            .withTimeout(expectedRunTime, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until()
            .element(page.restartButton)
            .is().present();

        String[] events = ((String) executeJS("return metamerEvents")).split(" ");

        assertEquals(events.length % 3, 0, "Number of events should be a multiple of 3.");
        for (int i = 0; i < events.length; i += 3) {
            assertEquals(events[i], "begin", "Event nr." + i + " should be begin.");
            assertEquals(events[i + 1], "beforedomupdate", "Event nr." + (i + 1) + " should be beforedomupdate.");
            assertEquals(events[i + 2], "complete", "Event nr." + (i + 2) + " should be complete.");
        }
    }

    @Test
    @Templates(value = "plain")
    public void testFinishClass() {
        testStyleClass(page.finish, BasicAttributes.finishClass);
    }

    @Test
    public void testInit() {
        assertPresent(page.progressBar, "Progress bar is not present on the page.");
        assertVisible(page.progressBar, "Progress bar should be visible on the page.");
        assertVisible(page.initialOutput, "Initial output should be visible on the page.");
        assertNotPresent(page.finishOutput, "Finish output should not be present on the page.");

        assertVisible(page.startButton, "Start button is not visible on the page.");
        assertNotPresent(page.restartButton, "Restart button should not be present on the page.");

        assertNotVisible(page.remain, "Progress bar should not show progress.");
        assertNotVisible(page.progress, "Progress bar should not show progress.");
        assertNotVisible(page.label, "Progress bar should not show progress.");
    }

    @Test
    @Templates(value = "plain")
    public void testInitialClass() {
        testStyleClass(page.init, BasicAttributes.initialClass);
    }

    @Test
    @UseWithField(field = "testInterval", valuesFrom = FROM_FIELD, value = "ints")
    public void testInterval() {
        testOneRunOfProgressBar(page.startButton, testInterval);
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onclick, new Actions(driver).click(page.progressBar)
            .build());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.ondblclick,
            new Actions(driver).doubleClick(page.progressBar).build());
    }

    private void testOneRunOfProgressBar(WebElement button, int interval) {
        progressBarAttributes.set(ProgressBarAttributes.interval, interval);

        long delta = (long) (interval * 0.3);
        long maxWaitTime = interval + delta;

        List<String> timesString = new ArrayList<String>();
        List<String> labelsList = new ArrayList<String>();
        List<Integer> progressList = new ArrayList<Integer>();
        long expectedRunTime = getExpectedRunTime();
        int expectedNumberOfUpdates = getExpectedNumberOfUpdates();

        MetamerPage.requestTimeChangesWaiting(button).click();
        String timeString = page.getRequestTimeElement().getText();
        timesString.add(timeString);
        for (int i = 0; i < expectedNumberOfUpdates; i++) {
            Graphene.waitGui().withTimeout(maxWaitTime, TimeUnit.MILLISECONDS)
                .withMessage("Page was not updated")
                .until().element(page.getRequestTimeElement()).text().not().equalTo(timeString);
            timeString = page.getRequestTimeElement().getText();
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

        Graphene.waitGui()
            .withTimeout(expectedRunTime, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until()
            .element(page.restartButton)
            .is().present();
        assertPresent(page.finishOutput, "Complete output should be present on the page.");
        assertPresent(page.progressBar, "Progress bar is not present on the page.");
        assertNotPresent(page.initialOutput, "Initial output should not be present on the page.");
        assertPresent(page.finishOutput, "Complete output should be present on the page.");
        assertNotPresent(page.startButton, "Start button should not be present on the page.");
        assertPresent(page.restartButton, "Restart button should be present on the page.");
        assertNotVisible(page.remain, "Progress bar should not show progress.");
    }

    @Test
    public void testOnfinish() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 5);//the test will be quicker
        progressBarAttributes.set(ProgressBarAttributes.onfinish, "metamerEvents += \"finish \"");

        executeJS("metamerEvents = \"\"");
        long expectedRunTime = getExpectedRunTime();

        MetamerPage.requestTimeChangesWaiting(page.startButton).click();
        Graphene.waitAjax()
            .withTimeout(expectedRunTime, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until()
            .element(page.restartButton)
            .is().present();

        String[] events = ((String) executeJS("return metamerEvents")).split(" ");

        assertEquals(events.length, 1, "Only one event should be fired.");
        assertEquals(events[0], "finish", "Onfinish doesn't work.");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmousedown,
            new Actions(driver).clickAndHold(page.progressBar).build());
        // release mouse button (necessary since Selenium 2.35)
        new Actions(driver).release().perform();
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmousemove,
            new Actions(driver).moveToElement(page.progressBar).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(page.progressBar, progressBarAttributes, ProgressBarAttributes.onmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        new Actions(driver).moveToElement(page.getRequestTimeElement()).perform();

        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmouseover,
            new Actions(driver).moveToElement(page.progressBar).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(progressBarAttributes, ProgressBarAttributes.onmouseup,
            new Actions(driver).click(page.progressBar).build());
    }

    @Test
    public void testProgress() {
        testOneRunOfProgressBar(page.startButton, 1400);
        testOneRunOfProgressBar(page.restartButton, 1400);
    }

    @Test
    @Templates(value = "plain")
    public void testProgressClass() {
        testStyleClass(page.progress, BasicAttributes.progressClass);
    }

    @Test
    @Templates(value = "plain")
    public void testRemainingClass() {
        testStyleClass(page.remain, BasicAttributes.remainingClass);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        progressBarAttributes.set(ProgressBarAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.progressBar, "Progress bar should not be rendered when rendered=false.");
    }

    @Test
    public void testStart() {
        MetamerPage.requestTimeChangesWaiting(page.startButton).click();

        String labelValue = page.label.getText();
        assertTrue("1 %".equals(labelValue) || "2 %".equals(labelValue),
            "Progress bar's label after start should be \"1 %\" or \"2 %\".");

        assertVisible(page.remain, "Progress bar should show progress.");
        assertVisible(page.progress, "Progress bar should not show progress.");

        assertVisible(page.progressBar, "Progress bar should be visible on the page.");
        assertNotPresent(page.initialOutput, "Initial output should not be present on the page.");
        assertNotPresent(page.finishOutput, "Complete output should not be present on the page.");
        assertNotPresent(page.startButton, "Start button should not be present on the page.");
        assertNotPresent(page.restartButton, "Restart button should not be present on the page.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.progressBar);
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.progressBar);
    }
}
