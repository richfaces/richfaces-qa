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

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richProgressBar/static.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M3
 */
public class TestProgressBarStatic extends AbstractWebDriverTest {

    private final Attributes<ProgressBarAttributes> progressBarAttributes = getAttributes();

    @Page
    private ProgressBarPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richProgressBar/static.xhtml");
    }

    @Test
    public void testInitialFacet() {
        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertVisible(page.getInitialOutputElement(), "Initial output should be visible on the page.");
        assertNotVisible(page.getFinishOutputElement(), "Finish output should not be visible on the page.");
        assertEquals(page.getInitialOutputElement().getText(), "Initial", "Content of initial facet.");

        assertNotVisible(page.getRemainElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getProgressElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getLabelElement(), "Progress bar should not show progress.");

        MetamerPage.waitRequest(page.getInitialFacetRenderedCheckboxElement(), WaitRequestType.HTTP).click();

        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertNotPresent(page.getInitialOutputElement(), "Initial output should not be present on the page.");
        assertNotVisible(page.getFinishOutputElement(), "Finish output should not be visible on the page.");

        assertPresent(page.getRemainElement(), "Progress bar should show progress.");
        assertPresent(page.getProgressElement(), "Progress bar should show progress.");
        assertPresent(page.getLabelElement(), "Progress bar should show progress.");
    }

    @Test(groups = "smoke")
    public void testFinishFacet() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 100);
        progressBarAttributes.set(ProgressBarAttributes.value, 100);

        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertNotVisible(page.getInitialOutputElement(), "Initial output should not be visible on the page.");
        assertVisible(page.getFinishOutputElement(), "Finish output should be visible on the page.");
        assertEquals(page.getFinishOutputElement().getText(), "Finish", "Content of finish facet.");

        assertNotVisible(page.getRemainElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getProgressElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getLabelElement(), "Progress bar should not show progress.");

        MetamerPage.waitRequest(page.getFinishFacetRenderedCheckboxElement(), WaitRequestType.HTTP).click();

        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertNotVisible(page.getInitialOutputElement(), "Initial output should not be visible on the page.");
        assertNotPresent(page.getFinishOutputElement(), "Finish output should not be present on the page.");

        assertVisible(page.getRemainElement(), "Progress bar should show progress.");
        assertVisible(page.getProgressElement(), "Progress bar should show progress.");
        assertNotVisible(page.getLabelElement(), "Progress bar should not show label.");
    }

    @Test
    @Templates(value = "plain")
    public void testFinishClass() {
        testStyleClass(page.getFinishElement(), BasicAttributes.finishClass);
    }

    @Test
    @Templates(value = "plain")
    public void testInitialClass() {
        testStyleClass(page.getInitElement(), BasicAttributes.initialClass);
    }

    @Test
    @Templates("plain")
    public void testLabel() {
        MetamerPage.waitRequest(page.getInitialFacetRenderedCheckboxElement(), WaitRequestType.HTTP).click();

        assertEquals(page.getLabelElement().getText(), "", "Label when not set.");

        progressBarAttributes.set(ProgressBarAttributes.label, "metamer");
        assertEquals(page.getLabelElement().getText(), "metamer", "Label when set to metamer.");

        MetamerPage.waitRequest(page.getChildrenRenderedCheckboxElement(), WaitRequestType.HTTP).click();
        assertEquals(page.getLabelElement().getText(), "child + metamer",
            "Label when set to metamer and children are rendered too.");
    }

    @Test
    @Templates("plain")
    public void testMaxValue() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 1000);
        progressBarAttributes.set(ProgressBarAttributes.value, 100);
        assertEquals(getProgress(), 10, "Progress when value=100 and maxValue=1000.");
    }

    @Test
    @Templates("plain")
    public void testMinValue() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 100);
        progressBarAttributes.set(ProgressBarAttributes.minValue, 90);
        progressBarAttributes.set(ProgressBarAttributes.value, 95);
        assertEquals(getProgress(), 50, "Progress when value=95 and minValue=90.");
    }

    @Test
    @Templates(value = "plain")
    public void testProgressClass() {
        testStyleClass(page.getProgressElement(), BasicAttributes.progressClass);
    }

    @Test
    @Templates(value = "plain")
    public void testRemainingClass() {
        testStyleClass(page.getRemainElement(), BasicAttributes.remainingClass);
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getProgressBarElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getProgressBarElement());
    }

    @Test
    @Templates("plain")
    public void testValue() {
        progressBarAttributes.set(ProgressBarAttributes.maxValue, 100);
        progressBarAttributes.set(ProgressBarAttributes.value, 0);
        assertEquals(getProgress(), 0, "Progress when value=0.");

        progressBarAttributes.set(ProgressBarAttributes.value, 37);
        assertEquals(getProgress(), 37, "Progress when value=37.");

        progressBarAttributes.set(ProgressBarAttributes.value, 100);
        assertEquals(getProgress(), 100, "Progress when value=100.");

        progressBarAttributes.set(ProgressBarAttributes.value, -345);
        assertEquals(getProgress(), 0, "Progress when value=-345.");
        assertTrue(page.getInitialOutputElement().isDisplayed(), "Initial output should be visible on the page.");
        assertFalse(page.getFinishOutputElement().isDisplayed(), "Finish output should not be visible on the page.");

        progressBarAttributes.set(ProgressBarAttributes.value, 456);
        assertEquals(getProgress(), 100, "Progress when value=456.");
        assertFalse(page.getInitialOutputElement().isDisplayed(), "Initial output should not be visible on the page.");
        assertTrue(page.getFinishOutputElement().isDisplayed(), "Finish output should be visible on the page.");
    }

    /**
     * @return progress size in %
     */
    private int getProgress() {
        String width = page.getProgressElement().getCssValue("width");
        if (width.contains("%")) {
            return Integer.parseInt(width.replace("%", ""));
        } else {
            float widthInPixels = Float.parseFloat(width.replace("px", "")) / 2.0f;
            // round the decimal number to integer and divide by 2 to obtain per cents because progress bar width is 200px
            return Math.round(widthInPixels);
        }
    }
}
