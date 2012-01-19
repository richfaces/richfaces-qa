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

import static org.richfaces.tests.metamer.ftest.BasicAttributes.finishClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.initialClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.progressClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.remainingClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richProgressBar/static.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestProgressBarStatic extends AbstractAjocadoTest {

    private JQueryLocator progressBar = pjq("div[id$=progressBar]");
    private JQueryLocator initialOutput = pjq("div.rf-pb-init > span");
    private JQueryLocator finishOutput = pjq("div.rf-pb-fin > span");
    private JQueryLocator remain = pjq("div.rf-pb-rmng");
    private JQueryLocator progress = pjq("div.rf-pb-prgs");
    private JQueryLocator complete = pjq("div[id$=complete]");
    private JQueryLocator label = pjq("div.rf-pb-lbl");
    private JQueryLocator childrenRenderedCheckbox = pjq("input[id$=childrenRendered]");
    private JQueryLocator initialFacetRenderedCheckbox = pjq("input[id$=initialFacetRendered]");
    private JQueryLocator finishFacetRenderedCheckbox = pjq("input[id$=finishFacetRendered]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/static.xhtml");
    }

    @Test
    public void testInitialFacet() {
        assertTrue(selenium.isElementPresent(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(progressBar), "Progress bar should be visible on the page.");
        assertTrue(selenium.isVisible(initialOutput), "Initial output should be present on the page.");
        assertFalse(selenium.isVisible(finishOutput), "Finish output should not be present on the page.");
        assertEquals(selenium.getText(initialOutput), "Initial", "Content of initial facet.");

        assertFalse(selenium.isVisible(remain), "Progress bar should not show progress.");
        assertFalse(selenium.isVisible(progress), "Progress bar should not show progress.");
        assertFalse(selenium.isVisible(label), "Progress bar should not show progress.");

        selenium.click(initialFacetRenderedCheckbox);
        selenium.waitForPageToLoad();

        assertTrue(selenium.isElementPresent(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(progressBar), "Progress bar should be visible on the page.");
        assertFalse(selenium.isElementPresent(initialOutput), "Initial output should not be present on the page.");
        assertFalse(selenium.isVisible(finishOutput), "Finish output should not be present on the page.");

        assertTrue(selenium.isVisible(remain), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(progress), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(label), "Progress bar should show progress.");
    }

    @Test
    public void testFinishFacet() {
        selenium.type(pjq("input[id$=valueInput]"), "100");
        selenium.waitForPageToLoad();

        assertTrue(selenium.isElementPresent(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(progressBar), "Progress bar should be visible on the page.");
        assertFalse(selenium.isVisible(initialOutput), "Initial output should not be present on the page.");
        assertTrue(selenium.isVisible(finishOutput), "Finish output should be present on the page.");
        assertEquals(selenium.getText(finishOutput), "Finish", "Content of finish facet.");

        assertFalse(selenium.isVisible(remain), "Progress bar should not show progress.");
        assertFalse(selenium.isVisible(progress), "Progress bar should not show progress.");
        assertFalse(selenium.isVisible(label), "Progress bar should not show progress.");

        selenium.click(finishFacetRenderedCheckbox);
        selenium.waitForPageToLoad();

        assertTrue(selenium.isElementPresent(progressBar), "Progress bar is not present on the page.");
        assertTrue(selenium.isVisible(progressBar), "Progress bar should be visible on the page.");
        assertFalse(selenium.isVisible(initialOutput), "Initial output should not be present on the page.");
        assertFalse(selenium.isElementPresent(finishOutput), "Finish output should not be present on the page.");

        assertTrue(selenium.isVisible(remain), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(progress), "Progress bar should show progress.");
        assertTrue(selenium.isVisible(label), "Progress bar should show progress.");
    }

    @Test
    public void testFinishClass() {
        testStyleClass(pjq("div.rf-pb-fin"), finishClass);
    }

    @Test
    public void testInitialClass() {
        testStyleClass(pjq("div.rf-pb-init"), initialClass);
    }

    @Test
    public void testLabel() {
        selenium.click(initialFacetRenderedCheckbox);
        selenium.waitForPageToLoad();
        String labelValue = selenium.getText(label);
        assertEquals(labelValue, "", "Label when not set.");

        selenium.type(pjq("input[id$=labelInput]"), "metamer");
        selenium.waitForPageToLoad();
        labelValue = selenium.getText(label);
        assertEquals(labelValue, "metamer", "Label when set to metamer.");

        selenium.click(childrenRenderedCheckbox);
        selenium.waitForPageToLoad();
        labelValue = selenium.getText(label);
        assertEquals(labelValue, "child + metamer", "Label when set to metamer and children are rendered too.");
    }

    @Test
    public void testMaxValue() {
        selenium.type(pjq("input[id$=maxValueInput]"), "1000");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[id$=valueInput]"), "100");
        selenium.waitForPageToLoad();

        assertEquals(getProgress(), 10, "Progress when value=100 and maxValue=1000.");
    }

    @Test
    public void testMinValue() {
        selenium.type(pjq("input[id$=minValueInput]"), "90");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[id$=valueInput]"), "95");
        selenium.waitForPageToLoad();

        assertEquals(getProgress(), 50, "Progress when value=95 and minValue=90.");
    }

    @Test
    public void testProgressClass() {
        testStyleClass(progress, progressClass);
    }

    @Test
    public void testRemainingClass() {
        testStyleClass(remain, remainingClass);
    }

    @Test
    public void testStyle() {
        testStyle(progressBar);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(progressBar);
    }

    @Test
    public void testValue() {
        selenium.type(pjq("input[id$=valueInput]"), "0");
        selenium.waitForPageToLoad();
        assertEquals(getProgress(), 0, "Progress when value=0.");
        
        selenium.type(pjq("input[id$=valueInput]"), "37");
        selenium.waitForPageToLoad();
        assertEquals(getProgress(), 37, "Progress when value=37.");
        
        selenium.type(pjq("input[id$=valueInput]"), "100");
        selenium.waitForPageToLoad();
        assertEquals(getProgress(), 100, "Progress when value=100.");

        selenium.type(pjq("input[id$=valueInput]"), "-345");
        selenium.waitForPageToLoad();
        assertEquals(getProgress(), 0, "Progress when value=-345.");
        assertTrue(selenium.isVisible(initialOutput), "Initial output should be visible on the page.");
        assertFalse(selenium.isVisible(finishOutput), "Finish output should not be visible on the page.");

        selenium.type(pjq("input[id$=valueInput]"), "456");
        selenium.waitForPageToLoad();
        assertEquals(getProgress(), 100, "Progress when value=456.");
        assertFalse(selenium.isVisible(initialOutput), "Initial output should not be visible on the page.");
        assertTrue(selenium.isVisible(finishOutput), "Finish output should be visible on the page.");
    }

    private int getProgress() {
        String width = selenium.getAttribute(progress.getAttribute(Attribute.STYLE));
        width = width.replace("%", "").replace("width:", "").replace(";", "").trim();
        return Integer.parseInt(width);
    }
}
