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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.EnumSet;
import java.util.Locale;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.panel.TextualFragmentPart;
import org.richfaces.fragment.popupPanel.RichFacesPopupPanel;
import org.richfaces.fragment.popupPanel.PopupPanel.ResizerLocation;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richPopupPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPopupPanel extends AbstractWebDriverTest {

    private final Attributes<PopupPanelAttributes> popupPanelAttributes = getAttributes();

    @FindBy(css = "input[id$=openPanelButton]")
    private WebElement openButton;
    @FindBy(css = "div.rf-pp-shade[id$=popupPanel_shade]")
    private WebElement shade;
    @FindBy(css = "div.rf-pp-cntr[id$=popupPanel_container]")
    private TestedPopupPanel panel;
    @FindBy(css = "input[id$=resize]")
    private WebElement resize;

    @Use(empty = false)
    @Inject
    private ResizerLocation resizer;

    private void checkCssValueOf(String cssValue, double value, WebElement element) {
        int tolerance = 5;
        assertEquals(Double.valueOf(element.getCssValue(cssValue).replace("px", "")),
            value,
            tolerance,
            cssValue + " of the panel");
    }

    private void checkCssValueOf(String cssValue, double value, double tolerance, WebElement element) {
        assertEquals(Double.valueOf(element.getCssValue(cssValue).replace("px", "")),
            value,
            tolerance,
            cssValue + " of the panel");
    }

    private void checkCssValueOfPanel(String cssValue, double value) {
        checkCssValueOf(cssValue, value, panel.advanced().getRootElement());
    }

    private void checkCssValueOfPanelShadow(String cssValue, double value) {
        checkCssValueOf(cssValue, value, panel.advanced().getShadowElement());
    }

    private void checkMove(int byX, int byY) {
        int tolerance = 5;
        Locations shadowLocationsBefore = Utils.getLocations(panel.advanced().getShadowElement());
        Locations panelLocationsBefore = panel.advanced().getLocations();
        assertEquals(panel.advanced().getHeaderElement().getCssValue("cursor"), "move", "Cursor used when mouse is over panel's header.");

        panel.advanced().moveByOffset(byX, byY);
        Locations shadowLocationsAfter = Utils.getLocations(panel.advanced().getShadowElement());
        Locations panelLocationsAfter = panel.advanced().getLocations();
        Utils.tolerantAssertLocationsEquals(panelLocationsBefore.moveAllBy(byX, byY), panelLocationsAfter, tolerance, tolerance, "Locations after move");
        Utils.tolerantAssertLocationsEquals(shadowLocationsBefore.moveAllBy(byX, byY), shadowLocationsAfter, tolerance, tolerance, "Locations after move");
    }

    private void checkResize(ResizerLocation fromLocation, int byX, int byY) {
        openPopupPanel();
        int tolerance = 5;
        Locations panelLocationsBefore = panel.advanced().getLocations();
        Locations shadowLocationsBefore = Utils.getLocations(panel.advanced().getShadowElement());
        int panelWidthBefore = panelLocationsBefore.getWidth();
        int panelHeightBefore = panelLocationsBefore.getHeight();
        int shadowWidthBefore = shadowLocationsBefore.getWidth();
        int shadowHeightBefore = shadowLocationsBefore.getHeight();
        panel.advanced().resizeFromLocation(fromLocation, byX, byY);
        Locations shadowLocationsAfter = Utils.getLocations(panel.advanced().getShadowElement());
        Locations panelLocationsAfter = panel.advanced().getLocations();
        assertNotEquals(shadowLocationsAfter, shadowLocationsBefore);
        int heightModifier = byY;
        int widthModifier = byX;
        if (EnumSet.of(ResizerLocation.SW, ResizerLocation.W, ResizerLocation.NW).contains(fromLocation)) {
            widthModifier *= -1;
        }
        if (EnumSet.of(ResizerLocation.NW, ResizerLocation.N, ResizerLocation.NE).contains(fromLocation)) {
            heightModifier *= -1;
        }
        assertEquals(shadowHeightBefore + heightModifier, shadowLocationsAfter.getHeight(), tolerance);
        assertEquals(panelHeightBefore + heightModifier, panelLocationsAfter.getHeight(), tolerance);
        assertEquals(shadowWidthBefore + widthModifier, shadowLocationsAfter.getWidth(), tolerance);
        assertEquals(panelWidthBefore + widthModifier, panelLocationsAfter.getWidth(), tolerance);
        hidePopup();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPopupPanel/simple.xhtml");
    }

    private void hidePopup() {
        panel.getBodyContent().hide();
        panel.advanced().waitUntilPopupIsNotVisible().perform();
    }

    private void openPopupPanel() {
        openButton.click();
        panel.advanced().waitUntilPopupIsVisible().perform();
    }

    @Test
    public void testAutosized() {
        popupPanelAttributes.set(PopupPanelAttributes.height, -1);// set value to default (as if the attibute is not present)
        popupPanelAttributes.set(PopupPanelAttributes.width, -1);// set value to default (as if the attibute is not present)
        popupPanelAttributes.set(PopupPanelAttributes.resizeable, Boolean.FALSE);// need to be turned off to set @autosized=true
        popupPanelAttributes.set(PopupPanelAttributes.autosized, Boolean.FALSE);
        int contentWidth = 100;
        int panelPadding = 30;
        int tolerance = 10;
        setAttribute("textWidth", contentWidth);
        openPopupPanel();
        assertEquals(panel.getBodyContent().getParagraphElement().getCssValue("width"), contentWidth + "px",
            "Paragraph's width.");
        assertEquals(panel.advanced().getLocations().getWidth(),
            Integer.valueOf(popupPanelAttributes.get(PopupPanelAttributes.minWidth)).intValue(),
            tolerance,
            "Panel's width should be the same as its minWidth, when its content is smaller.");

        popupPanelAttributes.set(PopupPanelAttributes.autosized, Boolean.TRUE);
        // when @autosized=true, then @min/max-width/height attributes are ignored
        openPopupPanel();
        assertEquals(panel.advanced().getLocations().getWidth(),
            contentWidth + panelPadding,
            tolerance,
            "Panel's width should be autosized to content.");
        assertTrue(panel.advanced().getLocations().getWidth() < Integer.valueOf(popupPanelAttributes.get(PopupPanelAttributes.minWidth)).intValue(),
            "In this case panel's width should be lesser than its @minWidth when @autosized=true");
        assertTrue(panel.advanced().getLocations().getHeight() > Integer.valueOf(popupPanelAttributes.get(PopupPanelAttributes.maxHeight)).intValue(),
            "In this case panel's height should be greater than its @maxHeight when @autosized=true");
    }

    @Test
    @Templates(value = "plain")
    public void testControlsClass() {
        testStyleClass(panel.advanced().getHeaderControlsElement(), BasicAttributes.controlsClass);
    }

    @Test(groups = "smoke")
    @RegressionTest("https://issues.jboss.org/browse/RF-10249")
    public void testDomElementAttachment() {
        popupPanelAttributes.set(PopupPanelAttributes.domElementAttachment, "");
        openPopupPanel();
        assertPresent(driver.findElement(By.cssSelector("body > div.rf-pp-cntr")), "Panel container should be attached to the body element.");

        popupPanelAttributes.set(PopupPanelAttributes.domElementAttachment, "body");
        openPopupPanel();
        assertPresent(driver.findElement(By.cssSelector("body > div.rf-pp-cntr")), "Panel container should be attached to the body element.");

        popupPanelAttributes.set(PopupPanelAttributes.domElementAttachment, "parent");
        openPopupPanel();
        assertPresent(driver.findElement(By.cssSelector("div[id$=popupPanel] > div.rf-pp-cntr")), "Panel container should be attached to the body element.");

        popupPanelAttributes.set(PopupPanelAttributes.domElementAttachment, "form");
        openPopupPanel();
        assertPresent(driver.findElement(By.cssSelector("form[id=form] > div.rf-pp-cntr")), "Panel container should be attached to the body element.");
    }

    @Test
    public void testFollowByScroll() {
        popupPanelAttributes.set(PopupPanelAttributes.followByScroll, Boolean.FALSE);
        openPopupPanel();
        assertEquals(panel.advanced().getRootElement().getCssValue("position"), "absolute");
        popupPanelAttributes.set(PopupPanelAttributes.followByScroll, Boolean.TRUE);
        openPopupPanel();
        assertEquals(panel.advanced().getRootElement().getCssValue("position"), "fixed");
    }

    @Test
    @Templates(value = "plain")
    public void testHeader() {
        String value = "new header";
        popupPanelAttributes.set(PopupPanelAttributes.header, value);
        openPopupPanel();
        assertEquals(panel.advanced().getHeaderContentElement().getText(), value, "Header of the popup panel.");
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderClass() {
        testStyleClass(panel.advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testHeight() {
        openPopupPanel();
        checkCssValueOfPanel("height", 300);

        popupPanelAttributes.set(PopupPanelAttributes.height, 400);
        openPopupPanel();
        checkCssValueOfPanel("height", 400);
        checkMove(50, 50);
        checkMove(-50, -50);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-10251")
    public void testHeightZero() {
        popupPanelAttributes.set(PopupPanelAttributes.minHeight, 300);
        popupPanelAttributes.set(PopupPanelAttributes.height, 0);
        openPopupPanel();
        checkCssValueOfPanel("height", 300);
        checkMove(50, 50);
        checkMove(-50, -50);
    }

    @Test
    public void testHidePanel() {
        openPopupPanel();
        assertVisible(panel.advanced().getRootElement(), "Popup panel is not visible.");

        panel.getHeaderControlsContent().close();
        assertNotVisible(panel.advanced().getRootElement(), "Popup panel is visible.");

        openPopupPanel();
        assertVisible(panel.advanced().getRootElement(), "Popup panel is not visible.");
        panel.getBodyContent().hide();
        assertNotVisible(panel.advanced().getRootElement(), "Popup panel is visible.");
    }

    @Test(groups = "smoke")
    public void testInit() {
        assertPresent(openButton, "Button for opening popup should be on the page.");
        assertNotVisible(panel.advanced().getRootElement(), "Popup panel is visible.");

        openPopupPanel();
        assertVisible(panel.advanced().getRootElement(), "Popup panel should be visible.");
        assertVisible(panel.advanced().getHeaderContentElement(), "Popup panel's header content should be visible.");
        assertVisible(panel.advanced().getHeaderControlsElement(), "Popup panel's header contols should be visible.");
        assertVisible(panel.advanced().getHeaderElement(), "Popup panel's header should be visible.");
        assertVisible(panel.advanced().getContentScrollerElement(), "Popup panel's scroller should be visible.");
        assertVisible(panel.advanced().getShadowElement(), "Popup panel's shadow should be visible.");
        WebElement resizerElement;
        for (ResizerLocation l : ResizerLocation.values()) {
            resizerElement = panel.advanced().getResizerElement(l);
            assertVisible(resizerElement, "Resizer" + l + " should be visible.");
            assertEquals(resizerElement.getCssValue("cursor"), l.toString().toLowerCase(Locale.ENGLISH) + "-resize");
        }
        assertNotPresent(shade, "Mask should not be visible.");
        assertEquals(panel.advanced().getHeaderContentElement().getText(), "popup panel header", "Header's text");
        assertTrue(panel.getBodyContent().getContentString().startsWith("Lorem ipsum"), "Panel's content should start with 'Lorem ipsum'.");
        assertTrue(panel.getBodyContent().getContentString().endsWith("hide this panel"), "Panel's content should end with 'hide this panel'.");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-10697")
    public void testKeepVisualState() {
        int tolerance = 10;
        int moveBy = 150;
        // we need to do this to get the submit button working
        popupPanelAttributes.set(PopupPanelAttributes.domElementAttachment, "form");

        popupPanelAttributes.set(PopupPanelAttributes.keepVisualState, Boolean.FALSE);
        openPopupPanel();
        Locations locationsBefore = panel.advanced().getLocations();
        panel.advanced().moveByOffset(moveBy, moveBy);
        panel.getBodyContent().submit();
        openPopupPanel();
        Utils.tolerantAssertLocationsEquals(panel.advanced().getLocations(), locationsBefore, tolerance, tolerance, "Panel's position should be the same as before");

        popupPanelAttributes.set(PopupPanelAttributes.keepVisualState, Boolean.TRUE);
        openPopupPanel();
        locationsBefore = panel.advanced().getLocations();
        panel.advanced().moveByOffset(moveBy, moveBy);
        panel.getBodyContent().submit();
        openPopupPanel();
        Utils.tolerantAssertLocationsEquals(panel.advanced().getLocations(), locationsBefore.moveAllBy(moveBy, moveBy), tolerance, tolerance, "Panel's position should the moved as before submit.");
    }

    @Test
    public void testLeft() {
        int defaultPanelWidth = 500;
        int width = Integer.valueOf(String.valueOf(executeJS("return window.innerWidth")));
        openPopupPanel();
        checkCssValueOfPanel("left", Math.round((width - defaultPanelWidth) / 2));

        popupPanelAttributes.set(PopupPanelAttributes.left, 200);
        openPopupPanel();
        checkCssValueOfPanel("left", 200);
    }

    @Test
    public void testMaxheight() {
        popupPanelAttributes.set(PopupPanelAttributes.minHeight, 200);
        popupPanelAttributes.set(PopupPanelAttributes.height, 390);
        popupPanelAttributes.set(PopupPanelAttributes.maxHeight, 400);
        openPopupPanel();
        // following is not working reliable, replacing with JS API function
//        panel.resizeFromLocation(ResizerLocation.S, 0, +120);// resize panel to maximum
        resize.click();// increases height and width by 10px
        resize.click();// increases height and width by 10px
        checkCssValueOfPanel("height", 400);
    }

    @Test
    public void testMaxwidth() {
        popupPanelAttributes.set(PopupPanelAttributes.minWidth, 200);
        popupPanelAttributes.set(PopupPanelAttributes.width, 390);
        popupPanelAttributes.set(PopupPanelAttributes.maxWidth, 400);
        openPopupPanel();
        // following is not working reliable, replacing with JS API function
//        panel.resizeFromLocation(ResizerLocation.E, +120, 0);// resize panel to maximum
        resize.click();// increases height and width by 10px
        resize.click();// increases height and width by 10px

        checkCssValueOfPanel("width", 400);
    }

    @Test
    public void testMinheight() {
        popupPanelAttributes.set(PopupPanelAttributes.minHeight, 200);
        popupPanelAttributes.set(PopupPanelAttributes.height, 300);
        popupPanelAttributes.set(PopupPanelAttributes.maxHeight, 400);
        openPopupPanel();
        panel.advanced().resizeFromLocation(ResizerLocation.S, 1, -120);// resize panel to minimum
        checkCssValueOfPanel("height", 200);
    }

    @Test
    public void testMinwidth() {
        popupPanelAttributes.set(PopupPanelAttributes.minWidth, 200);
        popupPanelAttributes.set(PopupPanelAttributes.width, 300);
        popupPanelAttributes.set(PopupPanelAttributes.maxWidth, 400);
        openPopupPanel();
        panel.advanced().resizeFromLocation(ResizerLocation.E, -120, 1);// resize panel to minimum
        checkCssValueOfPanel("width", 200);
    }

    @Test(groups = "smoke")
    @Templates(value = "plain")
    public void testModal() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.FALSE);
        openPopupPanel();
        assertNotVisible(shade, "Shade should be visible.");

        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        openPopupPanel();
        assertVisible(shade, "Shade should be visible.");
    }

    @Test()
    public void testMovable() {
        popupPanelAttributes.set(PopupPanelAttributes.moveable, Boolean.FALSE);
        openPopupPanel();
        assertEquals(panel.advanced().getHeaderElement().getCssValue("cursor"), "default", "Cursor used when mouse is over panel's header.");
        try {
            checkMove(50, 50);
        } catch (AssertionError e) {
            return;
        }
        Assert.fail("Panel should not be moveable.");
    }

    @Test
    public void testMove() {
        int value = 50;
        int zero = 0;
        openPopupPanel();
        checkMove(value, value);
        checkMove(-value, -value);
        checkMove(value, zero);
        checkMove(-value, zero);
        checkMove(zero, value);
        checkMove(zero, -value);
    }

    @Test
    public void testOnbeforehide() {
        testFireEvent("beforehide", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
                panel.getHeaderControlsContent().close();
            }
        });
    }

    @Test
    public void testOnbeforeshow() {
        testFireEvent("beforeshow", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
            }
        });
    }

    @Test
    public void testOnhide() {
        testFireEvent("hide", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
                hidePopup();
            }
        });
        testFireEvent("hide", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
                panel.getHeaderControlsContent().close();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskclick() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.CLICK, shade, "maskclick");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskcontextmenu() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(new Event("contextmenu"), shade, "maskcontextmenu");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskdblclick() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.DBLCLICK, shade, "maskdblclick");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskmousedown() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEDOWN, shade, "maskmousedown");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskmousemove() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEMOVE, shade, "maskmousemove");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskmouseout() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEOUT, shade, "maskmouseout");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskmouseover() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEOVER, shade, "maskmouseover");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmaskmouseup() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEUP, shade, "maskmouseup");
    }

    @Test
    public void testOnmove() {
        testFireEvent("move", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
                panel.advanced().moveByOffset(50, 50);
            }
        });
    }

    @Test
    public void testOnresize() {
        testFireEvent("resize", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
                panel.advanced().resizeFromLocation(ResizerLocation.N, 0, 50);
            }
        });
    }

    @Test
    public void testOnshow() {
        testFireEvent("show", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        popupPanelAttributes.set(PopupPanelAttributes.rendered, Boolean.TRUE);
        assertPresent(panel.advanced().getRootElement(), "Panel should berendered.");
        popupPanelAttributes.set(PopupPanelAttributes.rendered, Boolean.FALSE);
        assertNotPresent(panel.advanced().getRootElement(), "Popup panel should not be rendered.");
    }

    @Test
    @Use(enumeration = true, field = "resizer")
    public void testResize() {
        driver.manage().window().setSize(new Dimension(1024, 768));// to stabilize job on Jenkins
        popupPanelAttributes.set(PopupPanelAttributes.height, 400);
        popupPanelAttributes.set(PopupPanelAttributes.width, 400);
        popupPanelAttributes.set(PopupPanelAttributes.minHeight, 100);
        popupPanelAttributes.set(PopupPanelAttributes.minWidth, 100);
        popupPanelAttributes.set(PopupPanelAttributes.maxHeight, 800);
        popupPanelAttributes.set(PopupPanelAttributes.maxWidth, 800);
        int resizeBy = 100;
        checkResize(resizer,
            EnumSet.of(ResizerLocation.N, ResizerLocation.S).contains(resizer) ? 0 : resizeBy,
            EnumSet.of(ResizerLocation.E, ResizerLocation.W).contains(resizer) ? 0 : resizeBy);
    }

    @Test
    public void testResizeable() {
        popupPanelAttributes.set(PopupPanelAttributes.resizeable, Boolean.FALSE);
        for (ResizerLocation r : ResizerLocation.values()) {
            assertNotPresent(panel.advanced().getResizerElement(r), "Resizer " + r + " should not be present");
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10504")
    public void testShadowDepth() {
        int tolerance = 5;

        int value = 15;
        popupPanelAttributes.set(PopupPanelAttributes.shadowDepth, value);
        openPopupPanel();
        Locations shadowLocations = Utils.getLocations(panel.advanced().getShadowElement());
        Locations panelLocations = panel.advanced().getLocations();
        Utils.tolerantAssertLocationsEquals(panelLocations.moveAllBy(value, value), shadowLocations, tolerance, tolerance, "Locations of shadow.");

        value = 0;
        popupPanelAttributes.set(PopupPanelAttributes.shadowDepth, value);
        openPopupPanel();
        shadowLocations = Utils.getLocations(panel.advanced().getShadowElement());
        panelLocations = panel.advanced().getLocations();
        Utils.tolerantAssertLocationsEquals(panelLocations.moveAllBy(value, value), shadowLocations, tolerance, tolerance, "Locations of shadow.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10504")
    public void testShadowOpacity() {
        openPopupPanel();
        checkCssValueOfPanelShadow("opacity", 0.1);

        popupPanelAttributes.set(PopupPanelAttributes.shadowOpacity, 0.7);
        openPopupPanel();
        checkCssValueOfPanelShadow("opacity", 0.7);
    }

    @Test
    public void testShow() {
        popupPanelAttributes.set(PopupPanelAttributes.show, Boolean.FALSE);
        assertNotVisible(panel.advanced().getRootElement(), "Panel should not be visible");
        popupPanelAttributes.set(PopupPanelAttributes.show, Boolean.TRUE);
        assertVisible(panel.advanced().getRootElement(), "Panel should be visible");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10245")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(panel.advanced().getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10245")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(panel.advanced().getRootElement());
    }

    @Test
    public void testTop() {
        int defaultPanelHeight = 300;
        int height = Integer.valueOf(String.valueOf(executeJS("return window.innerHeight")));
        openPopupPanel();
        //more tolerant check, tolerance 20
        checkCssValueOf("top", Math.round((height - defaultPanelHeight) / 2), 20, panel.advanced().getRootElement());

        popupPanelAttributes.set(PopupPanelAttributes.top, 200);
        openPopupPanel();
        checkCssValueOfPanel("top", 200);
    }

    @Test
    public void testWidth() {
        openPopupPanel();
        checkCssValueOfPanel("width", 500);

        popupPanelAttributes.set(PopupPanelAttributes.width, 300);
        openPopupPanel();
        checkCssValueOfPanel("width", 300);
        checkMove(50, 50);
        checkMove(-50, -50);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-10251")
    public void testWidthZero() {
        popupPanelAttributes.set(PopupPanelAttributes.minWidth, 200);
        popupPanelAttributes.set(PopupPanelAttributes.width, 0);
        openPopupPanel();
        checkCssValueOfPanel("width", 200);
        checkMove(50, 50);
        checkMove(-50, -50);
    }

    @Test
    @Templates(value = "plain")
    public void testZindex() {
        openPopupPanel();
        checkCssValueOf("z-index", 4, panel.advanced().getRootElement());

        popupPanelAttributes.set(PopupPanelAttributes.zindex, 30);
        openPopupPanel();
        checkCssValueOf("z-index", 30, panel.advanced().getRootElement());
    }

    public static class TestedPopupPanel extends RichFacesPopupPanel<TextualFragmentPart, TestedPopupPanelHeaderControls, TestedPopupPanelContent> {
    }

    public static class TestedPopupPanelHeaderControls {

        @Root
        private WebElement root;

        @FindBy(tagName = "a")
        private WebElement hideLinkElement;

        public WebElement getHideLinkElement() {
            return hideLinkElement;
        }

        public void close() {
            hideLinkElement.click();
            Graphene.waitGui().until().element(root).is().not().visible();
        }
    }

    public static class TestedPopupPanelContent {

        @Root
        private WebElement root;

        @FindBy(tagName = "p")
        private WebElement paragraphElement;
        @FindBy(css = "input[id$='submitButton']")
        private WebElement submitButton;
        @FindBy(css = "a[id$='contentHideLink']")
        private WebElement hideLinkElement;

        public String getContentString() {
            return root.getText();
        }

        public WebElement getHideLinkElement() {
            return hideLinkElement;
        }

        public WebElement getParagraphElement() {
            return paragraphElement;
        }

        public WebElement getSubmitButton() {
            return submitButton;
        }

        public void hide() {
            hideLinkElement.click();
            Graphene.waitGui().until().element(root).is().not().visible();
        }

        public void submit() {
            MetamerPage.waitRequest(submitButton, WaitRequestType.HTTP).click();
        }
    }
}
