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

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.progressBarAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richProgressBar/clientMode.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21055 $
 */
public class TestProgressBarClient extends AbstractGrapheneTest {

    private JQueryLocator progressBar = pjq("div[id$=progressBar]");
    private JQueryLocator initialOutput = pjq("div.rf-pb-init > span");
    private JQueryLocator completeOutput = pjq("div.rf-pb-fin > span");
    private JQueryLocator startButton = pjq("input[id$=startButton]");
    private JQueryLocator pauseButton = pjq("input[id$=pauseButton]");
    private JQueryLocator remain = pjq("div.rf-pb-rmng");
    private JQueryLocator progress = pjq("div.rf-pb-prgs");
    private JQueryLocator complete = pjq("div[id$=complete]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/clientMode.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(initialOutput), "Initial output is not present on the page.");
        assertFalse(selenium.isVisible(completeOutput), "Complete output should not be present on the page.");
        assertTrue(selenium.isVisible(startButton), "Start button should be present on the page.");
        assertTrue(selenium.isVisible(pauseButton), "Pause button should be present on the page.");

        assertFalse(selenium.isVisible(remain), "Progress bar should not show progress.");
        assertFalse(selenium.isVisible(progress), "Progress bar should not show progress.");
    }

    @Test
    public void testStart() {
        guardNoRequest(selenium).click(startButton);

        assertTrue(selenium.isVisible(progressBar), "Progress bar is not present on the page.");
        assertFalse(selenium.isVisible(initialOutput), "Initial output should not be present on the page.");
        assertFalse(selenium.isVisible(completeOutput), "Complete output should not be present on the page.");
        assertTrue(selenium.isVisible(startButton), "Start button should be present on the page.");
        assertTrue(selenium.isVisible(pauseButton), "Restart button should be present on the page.");

        assertTrue(selenium.isElementPresent(remain), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(remain), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(progress), "Progress bar should not show progress.");
        assertFalse(selenium.isElementPresent(complete), "Progress bar should not show progress.");
    }

    @Test
    public void testProgress() throws ParseException {
        testOneRunOfProgressBar(startButton, 1000);
        testOneRunOfProgressBar(startButton, 1000);
    }

    @Test
    public void testPause() {
        selenium.click(startButton);
        waitFor(4000);

        selenium.click(pauseButton);
        int value = getProgress();
        assertTrue(value > 0, "Progress bar should show non-null progress after 4 seconds.");

        waitFor(4000);
        int value2 = getProgress();
        assertEquals(value2, value, "Progress bar should not be updated when paused.");
        selenium.click(startButton);

        value = getProgress();
        assertTrue(value > value2, "Progress bar's value should increase after pause.");
    }

    @Test
    public void testOnfinish() {
        progressBarAttributes.set(ProgressBarAttributes.onfinish, "metamerEvents += \"finish \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        selenium.click(startButton);
        waitGui.timeout(60000).failWith("Progress bar should disappear after it finishes.").until(elementVisible.locator(pjq("div.rf-pb-fin")));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 1, "Only one event should be fired.");
        assertEquals(events[0], "finish", "Onfinish doesn't work.");
    }

    private void testOneRunOfProgressBar(JQueryLocator button, int interval) throws ParseException {
        long delta = (long) (interval * 0.5);
        List<Integer> progressList = new ArrayList<Integer>();

        selenium.click(button);

        for (int i = 0; i < 40; i++) {
            waitFor(delta);
            progressList.add(getProgress());
        }

        int first,second;

        for (int i = 0; i < progressList.size() - 1; i++) {
            first = progressList.get(i);
            second = progressList.get(i + 1);
            assertTrue(first <= second, "Progress of progress bar should be increasing: " + first + "!<= " + second);
        }

        waitGui.timeout(40000).failWith("Progress bar should disappear after it finishes.").until(elementVisible.locator(pjq("div.rf-pb-fin")));
        assertTrue(selenium.isElementPresent(completeOutput), "Complete output should be present on the page.");

        assertTrue(selenium.isVisible(progressBar), "Progress bar is not present on the page.");
        assertFalse(selenium.isVisible(initialOutput), "Initial output should not be present on the page.");
        assertTrue(selenium.isVisible(completeOutput), "Complete output should be present on the page.");
        assertTrue(selenium.isVisible(startButton), "Start button should be present on the page.");
        assertTrue(selenium.isVisible(pauseButton), "Restart button should be present on the page.");

        assertFalse(selenium.isVisible(remain), "Progress bar should not show progress.");
    }

    private int getProgress() {
        String width = selenium.getAttribute(progress.getAttribute(Attribute.STYLE));
        width = width.replace("%", "").replace("width:", "").replace(";", "").trim();
        return Integer.parseInt(width);
    }
}
