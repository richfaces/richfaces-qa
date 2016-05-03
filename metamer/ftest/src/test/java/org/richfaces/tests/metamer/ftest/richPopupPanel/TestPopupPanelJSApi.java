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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richPopupPanel.TestPopupPanel.TestedPopupPanel;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richPopupPanel/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPopupPanelJSApi extends AbstractWebDriverTest {

    private static final int TOLERANCE = 5; // px

    @FindBy(css = "input[id$=getLeft]")
    private WebElement getLeft;
    @FindBy(css = "input[id$=getTop]")
    private WebElement getTop;
    @FindBy(css = "input[id$=hide]")
    private WebElement hide;
    @FindBy(css = "input[id$=moveTo]")
    private WebElement moveTo;
    @FindBy(css = "input[id$=openPanelButton]")
    private WebElement openButton;
    @FindBy(css = "div.rf-pp-cntr[id$=popupPanel_container]")
    private TestedPopupPanel panel;
    @FindBy(css = "input[id$=resize]")
    private WebElement resize;
    @FindBy(css = "input[id$=show]")
    private WebElement show;
    @FindBy(css = "input[id$=value]")
    private TextInputComponentImpl value;

    @Override
    public String getComponentTestPagePath() {
        return "richPopupPanel/simple.xhtml";
    }

    private int getValue() {
        return Integer.valueOf(value.getStringValue().replaceAll("px", ""));
    }

    private void openPopupPanel() {
        performJSClickOnButton(openButton);
        panel.advanced().waitUntilPopupIsVisible().perform();
    }

    /**
     * The button can be hidden by the popup panel and so it cannot be clicked. This method workarounds the problem without a
     * need of moving the panel.
     */
    private void performJSClickOnButton(WebElement button) {
        getMetamerPage().performJSClickOnButton(button, MetamerPage.WaitRequestType.NONE);
    }

    @Test
    public void testGetLeft() {
        openPopupPanel();
        getLeft.click();
        performJSClickOnButton(getLeft);
        assertEquals(getValue(), panel.advanced().getLocations().getTopLeft().x, TOLERANCE);
    }

    @Test
    public void testGetTop() {
        openPopupPanel();
        performJSClickOnButton(getTop);
        assertEquals(getValue(), panel.advanced().getLocations().getTopLeft().y, TOLERANCE);
    }

    @Test
    public void testHide() {
        openPopupPanel();
        performJSClickOnButton(hide);
        panel.advanced().waitUntilPopupIsNotVisible().perform();
    }

    @Test
    public void testMoveTo() {
        openPopupPanel();
        performJSClickOnButton(moveTo);// moves to [0, 0]
        assertEquals(panel.advanced().getLocations().getTopLeft().x, 0, TOLERANCE);
        assertEquals(panel.advanced().getLocations().getTopLeft().y, 0, TOLERANCE);
    }

    @Test
    public void testResize() {
        attsSetter()
            .setAttribute(PopupPanelAttributes.maxHeight).toValue(420)
            .setAttribute(PopupPanelAttributes.maxWidth).toValue(420)
            .setAttribute(PopupPanelAttributes.height).toValue(400)
            .setAttribute(PopupPanelAttributes.width).toValue(400)
            .asSingleAction().perform();
        openPopupPanel();
        int widthBefore = panel.advanced().getLocations().getWidth();
        int heightBefore = panel.advanced().getLocations().getHeight();
        assertEquals(widthBefore, 400, TOLERANCE);
        assertEquals(heightBefore, 400, TOLERANCE);
        performJSClickOnButton(resize);// resizes by 10x10
        assertEquals(panel.advanced().getLocations().getWidth(), 410, TOLERANCE);
        assertEquals(panel.advanced().getLocations().getHeight(), 410, TOLERANCE);
        performJSClickOnButton(resize);// resizes by 10x10
        assertEquals(panel.advanced().getLocations().getWidth(), 420, TOLERANCE);
        assertEquals(panel.advanced().getLocations().getHeight(), 420, TOLERANCE);
        performJSClickOnButton(resize);// resizes by 10x10, but the maximum h/w is reached
        assertEquals(panel.advanced().getLocations().getWidth(), 420, TOLERANCE);
        assertEquals(panel.advanced().getLocations().getHeight(), 420, TOLERANCE);
    }

    @Test
    public void testShow() {
        assertNotVisible(panel.advanced().getRootElement(), "Panel should not be visible.");
        performJSClickOnButton(show);
        panel.advanced().waitUntilPopupIsVisible().perform();
    }
}
