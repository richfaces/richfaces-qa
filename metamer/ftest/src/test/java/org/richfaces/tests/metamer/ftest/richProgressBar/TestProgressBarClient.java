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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.joda.time.DateTime;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.bean.rich.RichProgressBarBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richProgressBar/clientMode.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M3
 */
public class TestProgressBarClient extends AbstractWebDriverTest {

    private final Attributes<ProgressBarAttributes> progressBarAttributes = getAttributes();

    @Page
    private ProgressBarPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/clientMode.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(new WebElementConditionFactory(page.getProgressBarElement()).isPresent().apply(driver),
                "Progress bar is not present on the page.");
        assertTrue(page.getProgressBarElement().isDisplayed(), "Progress bar should be visible on the page.");
        assertTrue(page.getInitialOutputElement().isDisplayed(), "Initial output should be present on the page.");
        assertFalse(page.getFinishOutputElement().isDisplayed(), "Finish output should not be present on the page.");
        assertTrue(page.getStartButtonClientElement().isDisplayed(), "Start button is not present on the page.");
        assertTrue(page.getPauseButtonElement().isDisplayed(), "Pause button is not present on the page.");

        if (new WebElementConditionFactory(page.getRemainElement()).isPresent().apply(driver)) {
            assertFalse(page.getRemainElement().isDisplayed(), "Progress bar should not show progress.");
        }
        if (new WebElementConditionFactory(page.getProgressElement()).isPresent().apply(driver)) {
            assertFalse(page.getProgressElement().isDisplayed(), "Progress bar should not show progress.");
        }
        if (new WebElementConditionFactory(page.getLabelElement()).isPresent().apply(driver)) {
            assertFalse(page.getLabelElement().isDisplayed(), "Progress bar should not show progress.");
        }
        assertEquals(page.getIsEnabledElement().getText(), "false");
    }

    @Test
    public void testStart() {
        Graphene.guardNoRequest(page.getStartButtonClientElement()).click();
        waiting(1500);

        assertTrue(page.getProgressBarElement().isDisplayed(), "Progress bar should be visible on the page.");
        assertFalse(page.getInitialOutputElement().isDisplayed(), "Initial output should not be present on the page.");
        assertFalse(page.getFinishOutputElement().isDisplayed(), "Finish output should not be present on the page.");
        assertTrue(page.getStartButtonClientElement().isDisplayed(), "Start button should be present on the page.");
        assertTrue(page.getPauseButtonElement().isDisplayed(), "Pause button should be present on the page.");

        assertTrue(page.getRemainElement().isDisplayed(), "Progress bar should show progress.");
        assertTrue(page.getProgressElement().isDisplayed(), "Progress bar should show progress.");
        assertFalse(new WebElementConditionFactory(page.getCompleteOutputElement()).isVisible().apply(driver),
                "Progress bar should not show progress.");
        assertEquals(page.getIsEnabledElement().getText(), "true");
    }

    @Test
    public void testProgress() throws ParseException {
        testOneRunOfProgressBar(1400);
    }

    @Test
    public void testPause() {
        progressBarAttributes.set(ProgressBarAttributes.interval, 1000);
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 50);
        page.getStartButtonClientElement().click();
        waiting(3000);

        page.getPauseButtonElement().click();
        int value = getProgress();
        assertTrue(value > 0, "Progress bar should show non-null progress after 3 seconds.");

        waiting(2000);
        int value2 = getProgress();
        assertEquals(value2, value, "Progress bar should not be updated when paused.");
        page.getStartButtonClientElement().click();

        waiting(2000);
        value = getProgress();
        assertTrue(value > value2, "Progress bar's value should increase after pause.");
    }

    @Test
    public void testOnfinish() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 10);
        progressBarAttributes.set(ProgressBarAttributes.interval, 100);
        testFireEvent("finish", new Action() {
            @Override
            public void perform() {
                page.getStartButtonClientElement().click();
                Graphene.waitAjax().withTimeout(getExpectedRunTime(), TimeUnit.SECONDS)
                        .withMessage("Progress bar should disappear after it finishes.")
                        .until().element(page.getFinishElement()).is().visible();
            }
        });
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
        double maximumTime = interval * max;//s
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
        String progress = Utils.getTextFromHiddenElement(page.getActualProgressElement());
        return Integer.parseInt(progress);
    }

    private void testOneRunOfProgressBar(int interval) {
        progressBarAttributes.set(ProgressBarAttributes.interval, interval);
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 10);

        long delta = (long) (interval * 0.3);
        long maxWaitTime = interval + delta;

        List<Integer> progressList = new ArrayList<Integer>();
        long expectedRunTime = getExpectedRunTime();
        int expectedNumberOfUpdates = getExpectedNumberOfUpdates();
        List<DateTime> timesList = new ArrayList<DateTime>(expectedNumberOfUpdates);

        assertEquals(page.getIsEnabledElement().getText(), "false");
        MetamerPage.requestTimeNotChangesWaiting(page.getStartButtonClientElement()).click();
        waiting(2000);
        assertEquals(page.getIsEnabledElement().getText(), "true");

        int progress = getProgress();
        for (int i = 0; i < expectedNumberOfUpdates; i++) {
            Graphene.waitGui().withTimeout(maxWaitTime, TimeUnit.MILLISECONDS)
                    .withMessage("Progress was not updated")
                    .until().element(page.getActualProgressElement()).text().not().equalTo(String.valueOf(progress));
            progress = getProgress();
            progressList.add(progress);
            timesList.add(new DateTime());
        }

        long average = countAverage(timesList);
        assertEquals(average, interval, delta);

        int first, second;
        for (int i = 0; i < progressList.size() - 1; i++) {
            first = progressList.get(i);
            second = progressList.get(i + 1);
            assertTrue(progressList.get(i) <= progressList.get(i + 1), "Progress of progress bar should be increasing: " + first + "!<= " + second);
        }

        Graphene.waitGui()
                .withTimeout(expectedRunTime, TimeUnit.SECONDS)
                .withMessage("Progress bar should disappear after it finishes.")
                .until().element(page.getFinishOutputElement()).is().visible();
        assertVisible(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertNotVisible(page.getInitialOutputElement(), "Initial output should not be present on the page.");
        assertVisible(page.getFinishOutputElement(), "Complete output should be present on the page.");
        assertNotVisible(page.getRemainElement(), "Progress bar should not show progress.");
        assertEquals(page.getIsEnabledElement().getText(), "false");
    }
}
