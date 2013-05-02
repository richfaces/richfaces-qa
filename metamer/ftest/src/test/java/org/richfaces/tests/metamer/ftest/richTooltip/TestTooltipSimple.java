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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static org.jboss.arquillian.ajocado.dom.Event.CLICK;
import static org.jboss.arquillian.ajocado.dom.Event.DBLCLICK;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEMOVE;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOUT;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOVER;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEUP;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.tooltipAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.TooltipLayout;
import org.richfaces.TooltipMode;
import org.richfaces.component.Positioning;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22733 $
 */
public class TestTooltipSimple extends AbstractWebDriverTest {

    private static final int EVENT_OFFSET = 10;
    private static final int PRESET_OFFSET = 5;

    private static final String ATTR_INPUT_LOC_FORMAT = "input[id$=on{0}Input]";

    @Page
    TooltipPage page;

    Point eventPosition;
    @Inject
    @Use(empty = true)
    Positioning direction;
    Integer[] offsets = new Integer[] { 0, PRESET_OFFSET, -PRESET_OFFSET };
    @Inject
    @Use(ints = 0)
    Integer verticalOffset;
    @Inject
    @Use(ints = 0)
    Integer horizontalOffset;
    @Inject
    @Use(empty = true)
    Event domEvent;
    Event[] domEvents = { CLICK, DBLCLICK, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };
    @Inject
    @Use(empty = true)
    Boolean followMouse = true;
    @Inject
    @Use(empty = true)
    Integer presetDelay;
    @Inject
    @Use(empty = true)
    TooltipLayout layout;
    @Inject
    @Use(empty = true)
    TooltipMode mode;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTooltip/simple.xhtml");
    }

    @BeforeMethod
    public void setupAttributes() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "mouseover");
        tooltipAttributes.set(TooltipAttributes.hideEvent, "mouseout");
    }

    @Test
    public void testLifecycle() {
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.tooltip.setMode(TooltipMode.ajax);
        MetamerPage.requestTimeChangesWaiting(page.tooltip).recall(page.panel);
        page.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test
    public void testData() {
        tooltipAttributes.set(TooltipAttributes.data, "RichFaces 4");
        tooltipAttributes.set(TooltipAttributes.oncomplete, "data = event.data");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.tooltip.setMode(TooltipMode.ajax);

        MetamerPage.requestTimeChangesWaiting(page.tooltip).recall(page.panel);

        assertEquals(expectedReturnJS("return window.data;", "RichFaces 4"), "RichFaces 4"); // retrieveWindowData.retrieve(), "RichFaces 4");
    }

    @Test
    public void testRequestEventHandlers() {
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.tooltip.setMode(TooltipMode.ajax);

        testRequestEventsBefore("begin", "beforedomupdate", "complete");
        MetamerPage.requestTimeChangesWaiting(page.tooltip).recall(page.panel);
        testRequestEventsAfter("begin", "beforedomupdate", "complete");
    }

    @Test
    public void testDir() {
        super.testDir(page.tooltip.root);
    }

    @Test
    @Uses({ @Use(field = "direction", enumeration = true), @Use(field = "verticalOffset", value = "offsets"),
        @Use(field = "horizontalOffset", value = "offsets") })
    @Templates(value = { "plain", "richCollapsibleSubTable", "richExtendedDataTable", "richPopupPanel" })
    public void testPositioning() {
        tooltipAttributes.set(TooltipAttributes.direction, direction);
        tooltipAttributes.set(TooltipAttributes.horizontalOffset, horizontalOffset);
        tooltipAttributes.set(TooltipAttributes.verticalOffset, verticalOffset);

        if (direction == Positioning.auto) {
            direction = Positioning.topRight;
        }

        recallTooltipInRightBottomCornerOfPanel(0, 0);

        final Point tooltipPosition = page.tooltip.root.getLocation();
        final Dimension tooltipDimension = page.tooltip.root.getSize();

        if (getHorizontalAlignment() != null) {
            switch (getHorizontalAlignment()) {
                case RIGHT:
                    assertEquals(tooltipPosition.getX(), eventPosition.getX() + horizontalOffset);
                    break;
                case LEFT:
                    assertEquals(tooltipPosition.getX() + tooltipDimension.getWidth(), eventPosition.getX()
                        - horizontalOffset);
                    break;
                default:
                    break;
            }
        }

        if (getVerticalAlignment() != null) {
            switch (getVerticalAlignment()) {
                case BOTTOM:
                    assertEquals(tooltipPosition.getY(), eventPosition.getY() + verticalOffset);
                    break;
                case TOP:
                    assertEquals(tooltipPosition.getY() + tooltipDimension.getHeight(), eventPosition.getY()
                        - verticalOffset);
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    @Use(field = "followMouse", booleans = { true, false })
    public void testFollowMouse() {
        tooltipAttributes.set(TooltipAttributes.followMouse, followMouse);

        recallTooltipInRightBottomCornerOfPanel(0, 0);

        Point initialPosition = page.tooltip.root.getLocation();

        recallTooltipInRightBottomCornerOfPanel(-5, -5);

        Point nextPosition = page.tooltip.root.getLocation();

        int expectedOffset = (followMouse) ? -5 : 0;

        assertEquals(nextPosition.getX() - initialPosition.getX(), expectedOffset);
        assertEquals(nextPosition.getY() - initialPosition.getY(), expectedOffset);
    }

    @Test
    @Use(field = "presetDelay", ints = { 0, 1, 5 })
    public void testHideDelay() {

        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        tooltipAttributes.set(TooltipAttributes.hideDelay, presetDelay);

        page.tooltip.setMode(TooltipMode.ajax);

        page.tooltip.recall(page.panel);
        page.tooltip.hide(page.panel);
        WebDriverWait<Void> wait = new WebDriverWait<Void>(null, driver, presetDelay + 2);
        wait.until("Tooltip didn't disappears!").element(page.tooltip.root).is().not().visible();
    }

    @Test
    public void testHideEvent() {
        tooltipAttributes.set(TooltipAttributes.hideEvent, "mouseup");

        page.tooltip.recall(page.panel);

        new Actions(driver).moveToElement(page.panel).build().perform();
        Graphene.waitModel().until("Page was not updated").element(page.tooltip.root).is().present();
    }

    @Test
    public void testLang() {
        super.testAttributeLang(page.tooltip.root);
    }

    @Test
    @Use(field = "layout", enumeration = true)
    public void testLayout() {
        tooltipAttributes.set(TooltipAttributes.layout, layout);

        String expectedTagName = (layout == TooltipLayout.block) ? "div" : "span";

        String actualTagName = page.tooltip.root.getTagName();
        actualTagName = actualTagName.toLowerCase();

        assertEquals(actualTagName, expectedTagName);
    }

    @Test
    public void testLimitRender() {
        tooltipAttributes.set(TooltipAttributes.limitRender, true);
        tooltipAttributes.set(TooltipAttributes.render, "@this renderChecker");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.tooltip.setMode(TooltipMode.ajax);

        String renderCheckerText = page.renderCheckerOutput.getText();

        MetamerPage.requestTimeNotChangesWaiting(page.tooltip).recall(page.panel);
        Graphene.waitGui().until().element(page.renderCheckerOutput).text().not().equalTo(renderCheckerText);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testMode() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);

        page.tooltip.setMode(mode);

        if (mode == TooltipMode.ajax) {
            MetamerPage.requestTimeChangesWaiting(page.tooltip).recall(page.panel);
        } else {
            page.tooltip.recall(page.panel);
        }

        MetamerPage.requestTimeNotChangesWaiting(page.tooltip).hide(page.panel);

        Graphene.waitGui().until().element(page.tooltip.root).is().not().visible();
    }

    @Test
    public void testOnBefereDOMUpdate() {
        // in client mode no DOM update triggered
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.tooltip.setMode(TooltipMode.ajax);

        testRequestEventsBefore("beforedomupdate");
        page.tooltip.recall(page.panel);
        testRequestEventsAfter("beforedomupdate");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnBeforeHide() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);

        testRequestEventsBefore("beforehide");

        // Setting handler cause tooltip disappear,
        // so invoke them between event handler setting
        page.tooltip.recall(page.panel);
        page.tooltip.hide(page.panel);

        testRequestEventsAfter("beforehide");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnBeforeShow() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);

        testRequestEventsBefore("beforeshow");
        page.tooltip.recall(page.panel);
        testRequestEventsAfter("beforeshow");

        page.tooltip.hide(page.panel);
    }

    @Test
    public void testOnBegin() {
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.tooltip.setMode(TooltipMode.ajax);

        testRequestEventsBefore("begin");
        page.tooltip.recall(page.panel);
        testRequestEventsAfter("begin");

        page.tooltip.hide(page.panel);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnClick() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);
        Action click = new Actions(driver).click(page.tooltip.root).build();
        verifyEventHandler(TooltipAttributes.onclick, click);
    }

    @Test
    public void testOnComplete() {
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.tooltip.setMode(TooltipMode.ajax);

        testRequestEventsBefore("complete");
        page.tooltip.recall(page.panel);
        testRequestEventsAfter("complete");

        page.tooltip.hide(page.panel);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnDblClick() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);
        Action dblClick = new Actions(driver).doubleClick(page.tooltip.root).build();
        verifyEventHandler(TooltipAttributes.ondblclick, dblClick);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnHide() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);

        testRequestEventsBefore("hide");
        page.tooltip.recall(page.panel);
        page.tooltip.hide(page.panel);
        testRequestEventsAfter("hide");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnShow() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);

        testRequestEventsBefore("show");
        page.tooltip.recall(page.panel);
        testRequestEventsAfter("show");

        page.tooltip.hide(page.panel);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnMouseDown() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);
        Action action = new Actions(driver).clickAndHold(page.tooltip.root).build();
        verifyEventHandler(TooltipAttributes.onmousedown, action);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnMouseMove() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);
        Action action = new Actions(driver).moveToElement(page.tooltip.root, 1, 1).build();
        verifyEventHandler(TooltipAttributes.onmousemove, action);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnMouseOut() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);

        String attributeName = "mouseout";

        tooltipAttributes.set(TooltipAttributes.onmouseout, "metamerEvents += \"" + TooltipAttributes.onmouseout + " \"");
        Event e = new Event(attributeName);
        page.tooltip.recall(page.panel);
        executeJS("metamerEvents = \"\";");

        fireEvent(page.tooltip.root, e);
        String returnedString = expectedReturnJS("return metamerEvents", TooltipAttributes.onmouseout.toString());
        assertEquals(returnedString, TooltipAttributes.onmouseout.toString(), "Event " + e + " does not work.");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnMouseOver() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);
        Action action = new Actions(driver).moveToElement(page.tooltip.root).build();
        verifyEventHandler(TooltipAttributes.onmouseover, action);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnMouseUp() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.tooltip.setMode(mode);
        Action action = new Actions(driver).clickAndHold(page.tooltip.root).release(page.tooltip.root).build();
        verifyEventHandler(TooltipAttributes.onmouseup, action);
    }

    @Test
    public void testRendered() {
        tooltipAttributes.set(TooltipAttributes.rendered, false);
        page.tooltip.recall(page.panel);
        assertFalse(Graphene.element(page.tooltip.root).isPresent().apply(driver));
    }

    @Test
    @Use(field = "presetDelay", ints = { 0, 1000, 5000 })
    @RegressionTest("https://issues.jboss.org/browse/RF-10522")
    public void testShowDelay() {

        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.client);
        tooltipAttributes.set(TooltipAttributes.showDelay, presetDelay);

        page.tooltip.setMode(TooltipMode.client);

        page.tooltip.recall(page.panel);

        if (presetDelay > 4000) {
            // make sure that tooltip not displayed earlier than after 2 sec (when  showDelay 5 sec)
            waiting(2000);
            assertFalse(Graphene.element(page.tooltip.root).isVisible().apply(driver), "Tooltip shouldn't be displayed before deplay timeout (" + presetDelay + ") is over.");
        }
        WebDriverWait<Void> wait = new WebDriverWait<Void>(null, driver, presetDelay/1000 + 2);
        wait.until().element(page.tooltip.root).is().present();
        wait.until().element(page.tooltip.root).is().visible();
        page.tooltip.hide(page.panel);

    }

    @Test
    public void testShowEvent() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "mouseup");

        new Actions(driver).clickAndHold(page.panel).release(page.panel).build().perform();
        Graphene.waitGui().until().element(page.tooltip.root).is().visible();
    }

    @Test
    public void testStatus() {
        tooltipAttributes.set(TooltipAttributes.status, "statusChecker");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.tooltip.setMode(TooltipMode.ajax);

        String statusChecker = page.statusCheckerOutput.getText();
        page.tooltip.recall(page.panel);
        Graphene.waitAjax().until().element(page.statusCheckerOutput).text().not().equalTo(statusChecker);
    }

    @Test
    public void testStyle() {
        super.testStyle(page.tooltip.root);
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass(page.tooltip.root);
    }

    @Test
    public void testTitle() {
        super.testTitle(page.tooltip.root);
    }

    @Test
    public void testZindex() {
        tooltipAttributes.set(TooltipAttributes.zindex, 10);

        String zindex = page.tooltip.root.getCssValue("z-index"); // selenium.getStyle(page.tooltip.root, CssProperty.Z_INDEX);
        assertEquals(zindex, "10");
    }

    @Test
    public void testJsAPIbyClick() {
        page.jsAPIshowClick.click();
        Graphene.waitAjax().until(Graphene.element(page.tooltip.root).isVisible());

        page.jsAPIhideClick.click();
        Graphene.waitAjax().until(Graphene.element(page.tooltip.root).not().isVisible());
    }

    @Test
    public void testJsAPIbyMouseOver() {
        new Actions(driver).moveToElement(page.jsAPIshowMouseOver).build().perform();
        Graphene.waitAjax().until(Graphene.element(page.tooltip.root).isVisible());

        new Actions(driver).moveToElement(page.jsAPIhideMouseOver).build().perform();
        Graphene.waitAjax().until(Graphene.element(page.tooltip.root).not().isVisible());
    }

    private void recallTooltipInRightBottomCornerOfPanel(int offsetX, int offsetY) {
        final Point panelPosition = page.panel.getLocation();
        final Dimension panelDimension = page.panel.getSize();

        eventPosition = new Point(panelPosition.getX() + panelDimension.getWidth() - EVENT_OFFSET, panelPosition.getY()
            + panelDimension.getHeight() - EVENT_OFFSET);

        page.tooltip.recall(page.panel, panelDimension.getWidth() - EVENT_OFFSET + offsetX,
            panelDimension.getHeight() - EVENT_OFFSET + offsetY);
    }

    private HorizontalAlignment getHorizontalAlignment() {
        if (direction != null) {
            if (direction.toString().toLowerCase().contains("left")) {
                return HorizontalAlignment.LEFT;
            }
            if (direction.toString().toLowerCase().contains("right")) {
                return HorizontalAlignment.RIGHT;
            }
        }
        return null;
    }

    private VerticalAlignment getVerticalAlignment() {
        if (direction != null) {
            if (direction.toString().contains("top")) {
                return VerticalAlignment.TOP;
            }
            if (direction.toString().contains("bottom")) {
                return VerticalAlignment.BOTTOM;
            }
        }
        return null;
    }

    private enum HorizontalAlignment {

        LEFT, RIGHT
    }

    private enum VerticalAlignment {

        TOP, BOTTOM
    }

    private void verifyEventHandler(TooltipAttributes attr, Action eventFiringAction) {
        String attributeName = attr.toString().substring(2);
        tooltipAttributes.set(attr, "metamerEvents += \"" + attributeName + "\"");
        page.tooltip.recall(page.panel);
        executeJS("window.metamerEvents = \"\";");

        eventFiringAction.perform();
        assertEquals(expectedReturnJS("return window.metamerEvents", attr.toString()), attributeName, "Attribute " + attributeName + " does not work.");
    }
}
