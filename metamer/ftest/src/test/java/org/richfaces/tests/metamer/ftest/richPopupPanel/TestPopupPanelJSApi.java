/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPopupPanel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.popupPanelAttributes;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richPopupPanel.TestPopupPanel.TestedPopupPanel;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richPopupPanel/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPopupPanelJSApi extends AbstractWebDriverTest {

    @FindBy(css = "input[id$=value]")
    private TextInputComponentImpl value;
    @FindBy(css = "input[id$=getTop]")
    private WebElement getTop;
    @FindBy(css = "input[id$=getLeft]")
    private WebElement getLeft;
    @FindBy(css = "input[id$=moveTo]")
    private WebElement moveTo;
    @FindBy(css = "input[id$=resize]")
    private WebElement resize;
    @FindBy(css = "input[id$=show]")
    private WebElement show;
    @FindBy(css = "input[id$=hide]")
    private WebElement hide;
    @FindBy(css = "input[id$=openPanelButton]")
    private WebElement openButton;
    @FindBy(css = "div.rf-pp-cntr")
    private TestedPopupPanel panel;
    final int TOLERANCE = 5;// px

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPopupPanel/simple.xhtml");
    }

    private void openPopupPanel() {
        openButton.click();
        Graphene.waitGui().until(panel.isVisibleCondition());
    }

    private int getValue() {
        return Integer.valueOf(value.getStringValue().replaceAll("px", ""));
    }

    @Test
    public void testHide() {
        openPopupPanel();
        hide.click();
        Graphene.waitGui().until(panel.isNotVisibleCondition());
    }

    @Test
    public void testLeft() {
        openPopupPanel();
        getLeft.click();
        Assert.assertEquals(getValue(), panel.getLocations().getTopLeft().x, TOLERANCE);
    }

    @Test
    public void testMoveTo() {
        openPopupPanel();
        moveTo.click();// moves to [0, 0]
        Assert.assertEquals(panel.getLocations().getTopLeft().x, 0, TOLERANCE);
        Assert.assertEquals(panel.getLocations().getTopLeft().y, 0, TOLERANCE);
    }

    @Test
    public void testResize() {
        popupPanelAttributes.set(PopupPanelAttributes.maxHeight, 420);
        popupPanelAttributes.set(PopupPanelAttributes.maxWidth, 420);
        popupPanelAttributes.set(PopupPanelAttributes.height, 400);
        popupPanelAttributes.set(PopupPanelAttributes.width, 400);
        openPopupPanel();
        int widthBefore = panel.getLocations().getWidth();
        int heightBefore = panel.getLocations().getHeight();
        Assert.assertEquals(widthBefore, 400, TOLERANCE);
        Assert.assertEquals(heightBefore, 400, TOLERANCE);
        resize.click();// resizes by 10x10
        Assert.assertEquals(panel.getLocations().getWidth(), 410, TOLERANCE);
        Assert.assertEquals(panel.getLocations().getHeight(), 410, TOLERANCE);
        resize.click();// resizes by 10x10
        Assert.assertEquals(panel.getLocations().getWidth(), 420, TOLERANCE);
        Assert.assertEquals(panel.getLocations().getHeight(), 420, TOLERANCE);
        resize.click();// resizes by 10x10, resize over max width/height, width/height stays at its maximum
        Assert.assertEquals(panel.getLocations().getWidth(), 420, TOLERANCE);
        Assert.assertEquals(panel.getLocations().getHeight(), 420, TOLERANCE);
    }

    @Test
    public void testShow() {
        assertNotVisible(panel, "Panel should not be visible.");
        show.click();
        Graphene.waitGui().until(panel.isVisibleCondition());
    }

    @Test
    public void testTop() {
        openPopupPanel();
        getTop.click();
        Assert.assertEquals(getValue(), panel.getLocations().getTopLeft().y, TOLERANCE);
    }
}
