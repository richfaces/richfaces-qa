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
package org.richfaces.tests.metamer.ftest.richHashParam;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.popupPanel.RichFacesPopupPanel;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richHashParam/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHashParam extends AbstractWebDriverTest {

    //params set on page with hashParam
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 150;
    private static final int DEFAULT_HEIGHT = 345;
    private static final int DEFAULT_WIDTH = 543;
    private static final int DEFAULT_LEFT_MARGIN = 77;
    private static final int DEFAULT_TOP_MARGIN = 22;
    private static final int TOLERANCE = 2;
    //
    @FindBy(css = "div[id$=popupPanel_container]")
    private RichFacesPopupPanel popupPanel;
    //
    @FindBy(css = "input[id$=openPanelButton]")
    private WebElement openButton;
    @FindBy(css = "div[id$=popupPanel]")
    private WebElement panel;
    @FindBy(css = "div[id$=popupPanel_container].rf-pp-cntr")
    private WebElement panelContainer;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hdr")
    private WebElement header;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hdr-cntrls a")
    private WebElement controls;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-cnt-scrlr")
    private WebElement scroller;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-shdw")
    private WebElement shadow;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-l")
    private WebElement resizerL;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-t")
    private WebElement resizerT;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-r")
    private WebElement resizerR;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-b")
    private WebElement resizerB;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-tl")
    private WebElement resizerTL;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-tr")
    private WebElement resizerTR;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-bl")
    private WebElement resizerBL;
    @FindBy(css = "div[id$=popupPanel_container] div.rf-pp-hndlr-br")
    private WebElement resizerBR;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richHashParam/simple.xhtml");
    }

    private void openPopup() {
        openButton.click();
        Graphene.waitGui().until(popupPanel.isVisibleCondition());
    }

    @Test
    public void testPanelHeight() {
        openPopup();
        assertEquals(Utils.getLocations(panelContainer).getHeight(), DEFAULT_HEIGHT, TOLERANCE, "Panel height.");
    }

    @Test
    public void testPanelInit() {
        assertPresent(openButton, "Button for opening popup should be on the page.");
        assertPresent(panel, "Popup panel is not present on the page.");
        assertNotVisible(panel, "Popup panel is visible.");
        assertFalse(popupPanel.isVisible(), "Popup panel is visible.");

        openPopup();
        assertTrue(popupPanel.isVisible(), "Popup panel is visible.");
        assertVisible(panel, "Popup panel should be visible.");
        assertVisible(panelContainer, "Popup panel's container should be visible.");
        assertVisible(header, "Popup panel's header should be visible.");
        assertVisible(controls, "Popup panel's controls should be visible.");
        assertVisible(scroller, "Popup panel's scroller should be visible.");
        assertVisible(shadow, "Popup panel's shadow should be visible.");

        assertVisible(resizerR, "Popup panel's right resizer should be visible.");
        assertEquals(resizerR.getCssValue("cursor"), "e-resize", "Cursor used when mouse is over panel's right resizer.");
        assertVisible(resizerT, "Popup panel's top resizer should be visible.");
        assertEquals(resizerT.getCssValue("cursor"), "n-resize", "Cursor used when mouse is over panel's top resizer.");
        assertVisible(resizerB, "Popup panel's bottom resizer should be visible.");
        assertEquals(resizerB.getCssValue("cursor"), "s-resize", "Cursor used when mouse is over panel's bottom resizer.");
        assertVisible(resizerL, "Popup panel's left resizer should be visible.");
        assertEquals(resizerL.getCssValue("cursor"), "w-resize", "Cursor used when mouse is over panel's left resizer.");
        assertVisible(resizerTL, "Popup panel's top-left resizer should be visible.");
        assertEquals(resizerTL.getCssValue("cursor"), "nw-resize", "Cursor used when mouse is over panel's top-left resizer.");
        assertVisible(resizerTR, "Popup panel's top-right resizer should be visible.");
        assertEquals(resizerTR.getCssValue("cursor"), "ne-resize", "Cursor used when mouse is over panel's top-right resizer.");
        assertVisible(resizerBL, "Popup panel's bottom-left resizer should be visible.");
        assertEquals(resizerBL.getCssValue("cursor"), "sw-resize", "Cursor used when mouse is over panel's bottom-left resizer.");
        assertVisible(resizerBR, "Popup panel's bottom-right resizer should be visible.");
        assertEquals(resizerBR.getCssValue("cursor"), "se-resize", "Cursor used when mouse is over panel's bottom-right resizer.");
    }

    @Test
    public void testPanelLeftMargin() {
        openPopup();
        assertEquals(panelContainer.getLocation().x, DEFAULT_LEFT_MARGIN, TOLERANCE, "Left margin of the panel.");
    }

    @Test
    public void testPanelMove() {
        int tolerance = 2;//px
        int movementDistance = 100;//px
        openPopup();
        Locations panelLocations = Utils.getLocations(panelContainer);
        Locations shadowLocations = Utils.getLocations(shadow);
        assertEquals(header.getCssValue("cursor"), "move", "Cursor used when mouse is over panel's header.");

        popupPanel.moveByOffset(movementDistance, movementDistance);
        Utils.tolerantAssertLocationsEquals(panelContainer, panelLocations.moveAllBy(movementDistance, movementDistance), tolerance, tolerance, "Panel's position after move to the right (20px).");
        Utils.tolerantAssertLocationsEquals(shadow, shadowLocations.moveAllBy(movementDistance, movementDistance), tolerance, tolerance, "Shadow's position after move to the right (20px).");

        //return to default position
        popupPanel.moveByOffset(-movementDistance, -movementDistance);
        Utils.tolerantAssertLocationsEquals(panelContainer, panelLocations, tolerance, tolerance, "Panel's position after move to the right (20px).");
        Utils.tolerantAssertLocationsEquals(shadow, shadowLocations, tolerance, tolerance, "Shadow's position after move to the right (20px).");
    }

    @Test
    public void testPanelResize() {
        int resizeX = 60;//px
        int resizeY = 100;//px
        openPopup();
        Locations panelLocations = Utils.getLocations(panelContainer);
        Locations shadowLocations = Utils.getLocations(shadow);
        popupPanel.resize(resizeX, resizeY);

        Utils.tolerantAssertLocationsEquals(panelContainer, panelLocations.resizeFromBottomRight(resizeX, resizeY), TOLERANCE, TOLERANCE, "Panel's position after resize.");
        Utils.tolerantAssertLocationsEquals(shadow, shadowLocations.resizeFromBottomRight(resizeX, resizeY), TOLERANCE, TOLERANCE, "Shadow's position after resize.");

        //resize to minimum width height by resizing it to 60x100 (example that is below min height and width, from previous resize)
        popupPanel.resize(-DEFAULT_WIDTH, -DEFAULT_HEIGHT);//this should resize the panel to
        assertEquals(Utils.getLocations(panelContainer).getHeight(), MIN_HEIGHT, TOLERANCE);
        assertEquals(Utils.getLocations(panelContainer).getWidth(), MIN_WIDTH, TOLERANCE);
        assertEquals(Utils.getLocations(shadow).getHeight(), MIN_HEIGHT, TOLERANCE);
        assertEquals(Utils.getLocations(shadow).getWidth(), MIN_WIDTH, TOLERANCE);
    }

    @Test
    public void testPanelTopMargin() {
        openPopup();
        assertEquals(panelContainer.getLocation().y, DEFAULT_TOP_MARGIN, TOLERANCE, "Top margin of the panel.");
    }

    @Test
    public void testPanelWidth() {
        openPopup();
        assertEquals(Utils.getLocations(panelContainer).getWidth(), DEFAULT_WIDTH, TOLERANCE, "Panel width.");
    }
}
