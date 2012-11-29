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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richProgressBar/clientMode.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M3
 */
public class TestProgressBarClient extends AbstractWebDriverTest<ProgressBarPage> {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/clientMode.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Progress Bar", "Client Mode");
    }

    @Test
    public void testInit() {
        assertTrue(ElementPresent.getInstance().element(page.progressBar).apply(driver),
            "Progress bar is not present on the page.");
        assertTrue(page.progressBar.isDisplayed(), "Progress bar should be visible on the page.");
        assertTrue(page.initialOutput.isDisplayed(), "Initial output should be present on the page.");
        assertFalse(page.finishOutput.isDisplayed(), "Finish output should not be present on the page.");
        assertTrue(page.startButtonClient.isDisplayed(), "Start button is not present on the page.");
        assertTrue(page.pauseButton.isDisplayed(), "Pause button is not present on the page.");

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
        Graphene.guardNoRequest(page.startButtonClient).click();

        assertTrue(page.progressBar.isDisplayed(), "Progress bar should be visible on the page.");
        assertFalse(page.initialOutput.isDisplayed(), "Initial output should not be present on the page.");
        assertFalse(page.finishOutput.isDisplayed(), "Finish output should not be present on the page.");
        assertTrue(page.startButtonClient.isDisplayed(), "Start button should be present on the page.");
        assertTrue(page.pauseButton.isDisplayed(), "Pause button should be present on the page.");

        assertTrue(page.remain.isDisplayed(), "Progress bar should show progress.");
        assertTrue(page.progress.isDisplayed(), "Progress bar should not show progress.");
        assertFalse(ElementPresent.getInstance().element(page.complete).apply(driver),
            "Progress bar should not show progress.");
    }

    @Test
    public void testProgress() throws ParseException {
        testOneRunOfProgressBar(page.startButtonClient, 1000);
        testOneRunOfProgressBar(page.startButtonClient, 1000);
    }

    @Test
    public void testPause() {
        page.startButtonClient.click();
        waiting(3000);

        page.pauseButton.click();
        int value = getProgress();
        assertTrue(value > 0, "Progress bar should show non-null progress after 4 seconds.");

        waiting(3000);
        int value2 = getProgress();
        assertEquals(value2, value, "Progress bar should not be updated when paused.");
        page.startButtonClient.click();

        value = getProgress();
        assertTrue(value > value2, "Progress bar's value should increase after pause.");
    }

    @Test
    public void testOnfinish() {
        progressBarAttributes.set(ProgressBarAttributes.onfinish, "metamerEvents += \"finish \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");

        page.startButtonClient.click();
        Graphene.waitAjax().withTimeout(60, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until(Graphene.element(page.finish).isVisible());

        String[] events = ((String) ((JavascriptExecutor) driver).executeScript("return metamerEvents")).split(" ");

        assertEquals(events.length, 1, "Only one event should be fired.");
        assertEquals(events[0], "finish", "Onfinish doesn't work.");
    }

    private void testOneRunOfProgressBar(WebElement button, long interval) throws ParseException {
        long delta = (long) (interval * 0.5);
        List<Integer> progressList = new ArrayList<Integer>();

        button.click();

        for (int i = 0; i < 40; i++) {
            waiting(delta);
            progressList.add(getProgress());
        }

        int first, second;

        for (int i = 0; i < progressList.size() - 1; i++) {
            first = progressList.get(i);
            second = progressList.get(i + 1);
            assertTrue(first <= second, "Progress of progress bar should be increasing: " + first + "!<= " + second);
        }

        Graphene.waitAjax().withTimeout(40, TimeUnit.SECONDS)
            .withMessage("Progress bar should disappear after it finishes.")
            .until(Graphene.element(page.finish).isVisible());
        assertTrue(ElementPresent.getInstance().element(page.finishOutput).apply(driver),
            "Complete output should be present on the page.");

        assertTrue(page.progressBar.isDisplayed(), "Progress bar should be visible on the page.");
        assertFalse(page.initialOutput.isDisplayed(), "Initial output should not be present on the page.");
        assertTrue(page.finishOutput.isDisplayed(), "Finish output should be present on the page.");
        assertTrue(page.startButtonClient.isDisplayed(), "Start button should be present on the page.");
        assertTrue(page.pauseButton.isDisplayed(), "Pause button should be present on the page.");

        assertFalse(page.remain.isDisplayed(), "Progress bar should not show progress.");
    }

    private int getProgress() {
        String width = page.progress.getCssValue("width");
        if (width.contains("%")) {
            return Integer.parseInt(width.replace("%", ""));
        } else {
            return Integer.parseInt(width.replace("px", "")) / 2; // progress bar width is 200px
        }
    }
}
