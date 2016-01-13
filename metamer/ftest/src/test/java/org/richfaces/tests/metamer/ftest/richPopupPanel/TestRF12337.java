/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richPopupPanel;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.popupPanel.TextualRichFacesPopupPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12337 extends AbstractWebDriverTest {

    private static final int TOLERANCE = 5;// px

    @FindBy(css = "[id$=openPanelButton]")
    private WebElement openPanelButton;
    @FindBy(css = "[id$=outputText]")
    private WebElement outputText;
    @FindBy(css = "[id$=popupPanel_container]")
    private TextualRichFacesPopupPanel popupPanel;

    private void checkOutputTextAndCommandButtonAreOnTheSameLine() {
        // check the elements are on the same line (y axis)
        assertEquals(outputText.getLocation().y, openPanelButton.getLocation().y, TOLERANCE,
            "OutputText and commandButton are not on the same line!");
    }

    @Override
    public String getComponentTestPagePath() {
        return "richPopupPanel/rf-12337.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12337")
    public void testPopupPanelDoesNotInfluenceLayout() {
        checkOutputTextAndCommandButtonAreOnTheSameLine();

        // open panel
        openPanelButton.click();
        popupPanel.advanced().waitUntilPopupIsVisible().perform();
        checkOutputTextAndCommandButtonAreOnTheSameLine();

        // hide panel
        popupPanel.advanced().getHeaderControlsElement().findElement(By.tagName("a")).click();
        popupPanel.advanced().waitUntilPopupIsNotVisible().perform();
        checkOutputTextAndCommandButtonAreOnTheSameLine();
    }
}
