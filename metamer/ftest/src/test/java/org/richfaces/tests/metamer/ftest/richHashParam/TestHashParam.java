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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.popupPanel.TextualRichFacesPopupPanel;
import org.richfaces.fragment.popupPanel.PopupPanel.ResizerLocation;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richHashParam/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHashParam extends AbstractWebDriverTest {

    private final Attributes<HashParamAttributes> hashParamAttributes = getAttributes();

    //params set on page with hashParam
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 150;
    private static final int DEFAULT_HEIGHT = 345;
    private static final int DEFAULT_WIDTH = 543;
    private static final int DEFAULT_LEFT_MARGIN = 77;
    private static final int DEFAULT_TOP_MARGIN = 22;
    private static final int TOLERANCE = 2;

    @FindBy(css = "div[id$=popupPanel_container]")
    private TextualRichFacesPopupPanel panel;

    @FindBy(css = "input[id$=openPanelButton]")
    private WebElement openButton;
    @FindBy(css = "a[id$=hiddenLink]")
    private WebElement hiddenLink;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richHashParam/simple.xhtml");
    }

    private void openPopup() {
        openButton.click();
        panel.advanced().waitUntilPopupIsVisible().perform();
    }

    @Test
    public void testName() {
        String generatedOnClickFunctionPartTemplate = "{\"%s\":{\"param1\":\"1\",\"param2\":\"2\"} }";
        String testedName = "RFCustomName";
        hashParamAttributes.set(HashParamAttributes.name, testedName);
        String onClick = hiddenLink.getAttribute("onclick");// hidden link that contains hashParam with @name
        assertTrue(onClick.contains(String.format(generatedOnClickFunctionPartTemplate, testedName)), "The attribute @name does not work.");
    }

    @Test
    public void testPanelHeight() {
        openPopup();
        assertEquals(panel.advanced().getLocations().getHeight(), DEFAULT_HEIGHT, TOLERANCE, "Panel height.");
    }

    @Test
    public void testPanelInit() {
        assertPresent(openButton, "Button for opening popup should be on the page.");
        assertPresent(panel.advanced().getRootElement(), "Popup panel is not present on the page.");
        assertNotVisible(panel.advanced().getRootElement(), "Popup panel is visible.");

        openPopup();
        assertVisible(panel.advanced().getRootElement(), "Popup panel should be visible.");
        assertVisible(panel.advanced().getHeaderElement(), "Popup panel's header should be visible.");
        assertVisible(panel.advanced().getHeaderControlsElement(), "Popup panel's controls should be visible.");
        assertVisible(panel.advanced().getContentScrollerElement(), "Popup panel's scroller should be visible.");
        assertVisible(panel.advanced().getShadowElement(), "Popup panel's shadow should be visible.");
    }

    @Test
    public void testPanelLeftMargin() {
        openPopup();
        assertEquals(panel.advanced().getRootElement().getLocation().x, DEFAULT_LEFT_MARGIN, TOLERANCE, "Left margin of the panel.");
    }

    @Test
    public void testPanelMove() {
        int tolerance = 2;//px
        int movementDistance = 100;//px
        openPopup();
        Locations panelLocations = Utils.getLocations(panel.advanced().getRootElement());
        Locations shadowLocations = Utils.getLocations(panel.advanced().getShadowElement());
        assertEquals(panel.advanced().getHeaderElement().getCssValue("cursor"), "move", "Cursor used when mouse is over panel's header.");

        panel.advanced().moveByOffset(movementDistance, movementDistance);
        Utils.tolerantAssertLocationsEquals(panel.advanced().getRootElement(), panelLocations.moveAllBy(movementDistance, movementDistance), tolerance, tolerance, "Panel's position after move to the right (20px).");
        Utils.tolerantAssertLocationsEquals(panel.advanced().getShadowElement(), shadowLocations.moveAllBy(movementDistance, movementDistance), tolerance, tolerance, "Shadow's position after move to the right (20px).");

        //return to default position
        panel.advanced().moveByOffset(-movementDistance, -movementDistance);
        Utils.tolerantAssertLocationsEquals(panel.advanced().getRootElement(), panelLocations, tolerance, tolerance, "Panel's position after move to the right (20px).");
        Utils.tolerantAssertLocationsEquals(panel.advanced().getShadowElement(), shadowLocations, tolerance, tolerance, "Shadow's position after move to the right (20px).");
    }

    @Test
    public void testPanelResize() {
        int resizeX = 60;//px
        int resizeY = 100;//px
        openPopup();
        Locations panelLocations = Utils.getLocations(panel.advanced().getRootElement());
        Locations shadowLocations = Utils.getLocations(panel.advanced().getShadowElement());
        panel.advanced().resizeFromLocation(ResizerLocation.SE, resizeX, resizeY);

        Utils.tolerantAssertLocationsEquals(panel.advanced().getRootElement(), panelLocations.resizeFromBottomRight(resizeX, resizeY), TOLERANCE, TOLERANCE, "Panel's position after resize.");
        Utils.tolerantAssertLocationsEquals(panel.advanced().getShadowElement(), shadowLocations.resizeFromBottomRight(resizeX, resizeY), TOLERANCE, TOLERANCE, "Shadow's position after resize.");

        //resize to minimum width height by resizing it to 60x100 (example that is below min height and width, from previous resize)
        panel.advanced().resizeFromLocation(ResizerLocation.SE, -DEFAULT_WIDTH, -DEFAULT_HEIGHT);//this should resize the panel to
        assertEquals(Utils.getLocations(panel.advanced().getRootElement()).getHeight(), MIN_HEIGHT, TOLERANCE);
        assertEquals(Utils.getLocations(panel.advanced().getRootElement()).getWidth(), MIN_WIDTH, TOLERANCE);
        assertEquals(Utils.getLocations(panel.advanced().getShadowElement()).getHeight(), MIN_HEIGHT, TOLERANCE);
        assertEquals(Utils.getLocations(panel.advanced().getShadowElement()).getWidth(), MIN_WIDTH, TOLERANCE);
    }

    @Test
    public void testPanelTopMargin() {
        openPopup();
        assertEquals(panel.advanced().getRootElement().getLocation().y, DEFAULT_TOP_MARGIN, TOLERANCE, "Top margin of the panel.");
    }

    @Test
    public void testPanelWidth() {
        openPopup();
        assertEquals(Utils.getLocations(panel.advanced().getRootElement()).getWidth(), DEFAULT_WIDTH, TOLERANCE, "Panel width.");
    }
}
