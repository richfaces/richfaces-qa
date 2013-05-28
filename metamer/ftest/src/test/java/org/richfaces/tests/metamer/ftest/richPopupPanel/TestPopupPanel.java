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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.EnumSet;
import java.util.Locale;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.popupPanel.PopupPanel.ResizerLocation;
import org.richfaces.tests.page.fragments.impl.popupPanel.RichFacesPopupPanel;
import org.richfaces.tests.page.fragments.impl.popupPanel.RichFacesSimplePopupPanelContent;
import org.richfaces.tests.page.fragments.impl.popupPanel.RichFacesSimplePopupPanelControls;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richPopupPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPopupPanel extends AbstractWebDriverTest {

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
    ResizerLocation resizer;

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
        checkCssValueOf(cssValue, value, panel.getRootElement());
    }

    private void checkCssValueOfPanelShadow(String cssValue, double value) {
        checkCssValueOf(cssValue, value, panel.getShadowElement());
    }

    private void checkMove(int byX, int byY) {
        int tolerance = 5;
        Locations shadowLocationsBefore = Utils.getLocations(panel.getShadowElement());
        Locations panelLocationsBefore = panel.getLocations();
        assertEquals(panel.getHeaderElement().getCssValue("cursor"), "move", "Cursor used when mouse is over panel's header.");

        panel.moveByOffset(byX, byY);
        Locations shadowLocationsAfter = Utils.getLocations(panel.getShadowElement());
        Locations panelLocationsAfter = panel.getLocations();
        Utils.tolerantAssertLocationsEquals(panelLocationsBefore.moveAllBy(byX, byY), panelLocationsAfter, tolerance, tolerance, "Locations after move");
        Utils.tolerantAssertLocationsEquals(shadowLocationsBefore.moveAllBy(byX, byY), shadowLocationsAfter, tolerance, tolerance, "Locations after move");
    }

    private void checkResize(ResizerLocation fromLocation, int byX, int byY) {
        openPopupPanel();
        int tolerance = 5;
        Locations panelLocationsBefore = panel.getLocations();
        Locations shadowLocationsBefore = Utils.getLocations(panel.getShadowElement());
        int panelWidthBefore = panelLocationsBefore.getWidth();
        int panelHeightBefore = panelLocationsBefore.getHeight();
        int shadowWidthBefore = shadowLocationsBefore.getWidth();
        int shadowHeightBefore = shadowLocationsBefore.getHeight();
        panel.resizeFromLocation(fromLocation, byX, byY);
        Locations shadowLocationsAfter = Utils.getLocations(panel.getShadowElement());
        Locations panelLocationsAfter = panel.getLocations();
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
        panel.content().hide();
        Graphene.waitGui().until(panel.isNotVisibleCondition());
    }

    private void openPopupPanel() {
        openButton.click();
        Graphene.waitGui().until(panel.isVisibleCondition());
    }

    @Test
    public void testControlsClass() {
        testStyleClass(panel.getHeaderControlsElement(), BasicAttributes.controlsClass);
    }

    @Test
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
        assertEquals(panel.getRootElement().getCssValue("position"), "absolute");
        popupPanelAttributes.set(PopupPanelAttributes.followByScroll, Boolean.TRUE);
        openPopupPanel();
        assertEquals(panel.getRootElement().getCssValue("position"), "fixed");
    }

    @Test
    public void testHeader() {
        String value = "new header";
        popupPanelAttributes.set(PopupPanelAttributes.header, value);
        openPopupPanel();
        assertEquals(panel.getHeaderContentElement().getText(), value, "Header of the popup panel.");
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(panel.getHeaderElement(), BasicAttributes.headerClass);
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
        assertVisible(panel, "Popup panel is not visible.");

        panel.controls().hide();
        assertNotVisible(panel, "Popup panel is visible.");

        openPopupPanel();
        assertVisible(panel, "Popup panel is not visible.");
        panel.content().hide();
        assertNotVisible(panel, "Popup panel is visible.");
    }

    @Test
    public void testInit() {
        assertPresent(openButton, "Button for opening popup should be on the page.");
        assertNotVisible(panel, "Popup panel is visible.");

        openPopupPanel();
        assertVisible(panel, "Popup panel should be visible.");
        assertVisible(panel.getHeaderContentElement(), "Popup panel's header content should be visible.");
        assertVisible(panel.getHeaderControlsElement(), "Popup panel's header contols should be visible.");
        assertVisible(panel.getHeaderElement(), "Popup panel's header should be visible.");
        assertVisible(panel.getContentScrollerElement(), "Popup panel's scroller should be visible.");
        assertVisible(panel.getShadowElement(), "Popup panel's shadow should be visible.");
        WebElement resizerElement;
        for (ResizerLocation l : ResizerLocation.values()) {
            resizerElement = panel.getResizerElement(l);
            assertVisible(resizerElement, "Resizer" + l + " should be visible.");
            assertEquals(resizerElement.getCssValue("cursor"), l.toString().toLowerCase(Locale.ENGLISH) + "-resize");
        }
        assertNotPresent(shade, "Mask should not be visible.");
        assertEquals(panel.getHeaderContentElement().getText(), "popup panel header", "Header's text");
        assertTrue(panel.content().getContentString().startsWith("Lorem ipsum"), "Panel's content should start with 'Lorem ipsum'.");
        assertTrue(panel.content().getContentString().endsWith("hide this panel"), "Panel's content should end with 'hide this panel'.");
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
        Locations locationsBefore = panel.getLocations();
        panel.moveByOffset(moveBy, moveBy);
        panel.content().submit();
        openPopupPanel();
        Utils.tolerantAssertLocationsEquals(panel.getLocations(), locationsBefore, tolerance, tolerance, "Panel's position should be the same as before");

        popupPanelAttributes.set(PopupPanelAttributes.keepVisualState, Boolean.TRUE);
        openPopupPanel();
        locationsBefore = panel.getLocations();
        panel.moveByOffset(moveBy, moveBy);
        panel.content().submit();
        openPopupPanel();
        Utils.tolerantAssertLocationsEquals(panel.getLocations(), locationsBefore.moveAllBy(moveBy, moveBy), tolerance, tolerance, "Panel's position should the moved as before submit.");
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
        panel.resizeFromLocation(ResizerLocation.S, 1, -120);// resize panel to minimum
        checkCssValueOfPanel("height", 200);
    }

    @Test
    public void testMinwidth() {
        popupPanelAttributes.set(PopupPanelAttributes.minWidth, 200);
        popupPanelAttributes.set(PopupPanelAttributes.width, 300);
        popupPanelAttributes.set(PopupPanelAttributes.maxWidth, 400);
        openPopupPanel();
        panel.resizeFromLocation(ResizerLocation.E, -120, 1);// resize panel to minimum
        checkCssValueOfPanel("width", 200);
    }

    @Test
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
        assertEquals(panel.getHeaderElement().getCssValue("cursor"), "default", "Cursor used when mouse is over panel's header.");
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
                panel.controls().hide();
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
                panel.controls().hide();
            }
        });
    }

    @Test
    public void testOnmaskclick() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.CLICK, shade, "maskclick");
    }

    @Test
    public void testOnmaskcontextmenu() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(new Event("contextmenu"), shade, "maskcontextmenu");
    }

    @Test
    public void testOnmaskdblclick() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.DBLCLICK, shade, "maskdblclick");
    }

    @Test
    public void testOnmaskmousedown() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEDOWN, shade, "maskmousedown");
    }

    @Test
    public void testOnmaskmousemove() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEMOVE, shade, "maskmousemove");
    }

    @Test
    public void testOnmaskmouseout() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEOUT, shade, "maskmouseout");
    }

    @Test
    public void testOnmaskmouseover() {
        popupPanelAttributes.set(PopupPanelAttributes.modal, Boolean.TRUE);
        testFireEvent(Event.MOUSEOVER, shade, "maskmouseover");
    }

    @Test
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
                panel.moveByOffset(50, 50);
            }
        });
    }

    @Test
    public void testOnresize() {
        testFireEvent("resize", new Action() {
            @Override
            public void perform() {
                openPopupPanel();
                panel.resizeFromLocation(ResizerLocation.N, 0, 50);
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
    public void testRendered() {
        popupPanelAttributes.set(PopupPanelAttributes.rendered, Boolean.TRUE);
        assertPresent(panel.getRootElement(), "Panel should berendered.");
        popupPanelAttributes.set(PopupPanelAttributes.rendered, Boolean.FALSE);
        assertNotPresent(panel.getRootElement(), "Popup panel should not be rendered.");
    }

    @Test
    @Use(enumeration = true, field = "resizer")
    public void testResize() {
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
            assertNotPresent(panel.getResizerElement(r), "Resizer " + r + " should not be present");
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10504")
    public void testShadowDepth() {
        int tolerance = 5;

        int value = 15;
        popupPanelAttributes.set(PopupPanelAttributes.shadowDepth, value);
        openPopupPanel();
        Locations shadowLocations = Utils.getLocations(panel.getShadowElement());
        Locations panelLocations = panel.getLocations();
        Utils.tolerantAssertLocationsEquals(panelLocations.moveAllBy(value, value), shadowLocations, tolerance, tolerance, "Locations of shadow.");

        value = 0;
        popupPanelAttributes.set(PopupPanelAttributes.shadowDepth, value);
        openPopupPanel();
        shadowLocations = Utils.getLocations(panel.getShadowElement());
        panelLocations = panel.getLocations();
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
        assertNotVisible(panel, "Panel should not be visible");
        popupPanelAttributes.set(PopupPanelAttributes.show, Boolean.TRUE);
        assertVisible(panel, "Panel should be visible");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10245")
    public void testStyle() {
        testStyle(panel.getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10245")
    public void testStyleClass() {
        testStyleClass(panel.getRootElement());
    }

    @Test
    public void testTop() {
        int defaultPanelHeight = 300;
        int height = Integer.valueOf(String.valueOf(executeJS("return window.innerHeight")));
        openPopupPanel();
        //more tolerant check, tolerance 20
        checkCssValueOf("top", Math.round((height - defaultPanelHeight) / 2), 20, panel.getRootElement());

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
    public void testZindex() {
        openPopupPanel();
        checkCssValueOf("z-index", 4, panel.getRootElement());

        popupPanelAttributes.set(PopupPanelAttributes.zindex, 30);
        openPopupPanel();
        checkCssValueOf("z-index", 30, panel.getRootElement());
    }

    public static class TestedPopupPanel extends RichFacesPopupPanel<TestedPopupPanelControls, TestedPopupPanelContent> {

        @Override
        protected Class<TestedPopupPanelContent> getContentType() {
            return TestedPopupPanelContent.class;
        }

        @Override
        protected Class<TestedPopupPanelControls> getControlsType() {
            return TestedPopupPanelControls.class;
        }
    }

    public static class TestedPopupPanelControls extends RichFacesSimplePopupPanelControls {

        @FindBy(css = "a[id$='controlsHideLink']")
        private WebElement hideLinkElement;

        public WebElement getHideLinkElement() {
            return hideLinkElement;
        }

        public void hide() {
            hideLinkElement.click();
            Graphene.waitGui().until(isNotVisibleCondition());
        }
    }

    public static class TestedPopupPanelContent extends RichFacesSimplePopupPanelContent {

        @FindBy(css = "input[id$='submitButton']")
        private WebElement submitButton;
        @FindBy(css = "a[id$='contentHideLink']")
        private WebElement hideLinkElement;

        public WebElement getSubmitButton() {
            return submitButton;
        }

        public WebElement getHideLinkElement() {
            return hideLinkElement;
        }

        public String getContentString() {
            return getRootElement().getText();
        }

        public void hide() {
            hideLinkElement.click();
            Graphene.waitGui().until(isNotVisibleCondition());
        }

        public void submit() {
            MetamerPage.waitRequest(submitButton, WaitRequestType.HTTP).click();
        }
    }
}
