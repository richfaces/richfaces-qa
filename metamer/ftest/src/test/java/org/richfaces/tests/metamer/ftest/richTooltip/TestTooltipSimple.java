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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static org.jboss.arquillian.ajocado.Graphene.elementNotVisible;
import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.dom.Event.CLICK;
import static org.jboss.arquillian.ajocado.dom.Event.DBLCLICK;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEMOVE;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOUT;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOVER;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEUP;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.tooltipAttributes;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.data;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideDelay;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.render;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.showDelay;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.showEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.status;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.zindex;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.geometry.Dimension;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.javascript.JQueryScript;
import org.richfaces.TooltipLayout;
import org.richfaces.TooltipMode;
import org.richfaces.component.Positioning;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.DelayTester;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22733 $
 */
public class TestTooltipSimple extends AbstractGrapheneTest {

    private static final int EVENT_OFFSET = 10;
    private static final int PRESET_OFFSET = 5;
    // TooltipAttributes attributes = new TooltipAttributes();
    JQueryLocator panel = pjq("div[id$=panel]");
    TooltipModel tooltip = new TooltipModel(jq(".rf-tt"), panel);
    Point eventPosition;
    @Inject
    @Use(empty = true)
    Positioning direction;
    Integer[] offsets = new Integer[]{0, PRESET_OFFSET, -PRESET_OFFSET};
    @Inject
    @Use(ints = 0)
    Integer verticalOffset;
    @Inject
    @Use(ints = 0)
    Integer horizontalOffset;
    @Inject
    @Use(empty = true)
    Event domEvent;
    Event[] domEvents = {CLICK, DBLCLICK, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP};
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
        tooltipAttributes.set(showEvent, "mouseover");
        tooltipAttributes.set(hideEvent, "mouseout");
    }

    @Test
    public void testLifecycle() {
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        retrieveRequestTime.initializeValue();
        tooltip.recall();
        waitGui.waitForChange(retrieveRequestTime);
        phaseInfo.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test
    public void testData() {
        tooltipAttributes.set(data, "RichFaces 4");
        tooltipAttributes.set(oncomplete, "data = event.data");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        retrieveRequestTime.initializeValue();
        tooltip.recall();
        waitGui.waitForChange(retrieveRequestTime);

        assertEquals(retrieveWindowData.retrieve(), "RichFaces 4");
    }

    @Test
    public void testRequestEventHandlers() {
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        super.testRequestEventsBefore("begin", "beforedomupdate", "complete");
        retrieveRequestTime.initializeValue();
        tooltip.recall();
        waitGui.waitForChange(retrieveRequestTime);
        super.testRequestEventsAfter("begin", "beforedomupdate", "complete");
    }

    @Test
    public void testDir() {
        super.testDir(tooltip);
    }

    @Test
    @Uses({
        @Use(field = "direction", enumeration = true),
        @Use(field = "verticalOffset", value = "offsets"),
        @Use(field = "horizontalOffset", value = "offsets")})
    public void testPositioning() {
        tooltipAttributes.set(TooltipAttributes.direction, direction);
        tooltipAttributes.set(TooltipAttributes.horizontalOffset, horizontalOffset);
        tooltipAttributes.set(TooltipAttributes.verticalOffset, verticalOffset);

        if (direction == Positioning.auto) {
            direction = Positioning.topRight;
        }

        recallTooltipInRightBottomCornerOfPanel(0, 0);

        final Point tooltipPosition = selenium.getElementPosition(tooltip);
        final Dimension tooltipDimension = selenium.getElementDimension(tooltip);

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
    @Use(field = "followMouse", booleans = {true, false})
    public void testFollowMouse() {
        tooltipAttributes.set(TooltipAttributes.followMouse, followMouse);

        recallTooltipInRightBottomCornerOfPanel(0, 0);

        Point initialPosition = selenium.getElementPosition(tooltip);

        recallTooltipInRightBottomCornerOfPanel(-5, -5);

        Point nextPosition = selenium.getElementPosition(tooltip);

        int expectedOffset = (followMouse) ? -5 : 0;

        assertEquals(nextPosition.getX() - initialPosition.getX(), expectedOffset);
        assertEquals(nextPosition.getY() - initialPosition.getY(), expectedOffset);
    }

    @Test
    @Use(field = "presetDelay", ints = {0, 1000, 5000})
    public void testHideDelay() {

        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        tooltipAttributes.set(hideDelay, presetDelay);

        new DelayTester(presetDelay) {

            public void beforeAction() {
                tooltip.recall();
            }

            public void action() {
                tooltip.hide();
                waitGui.timeout(presetDelay + 2000).until(elementNotVisible.locator(tooltip));
            }
        }.run();
    }

    @Test
    public void testHideEvent() {
        tooltipAttributes.set(hideEvent, "mouseup");

        tooltip.recall();

        selenium.mouseUpAt(panel, new Point(5, 5));
        waitGui.until(elementNotVisible.locator(tooltip));
    }

    @Test
    public void testLang() {
        super.testLang(tooltip);
    }

    @Test
    @Use(field = "layout", enumeration = true)
    public void testLayout() {
        tooltipAttributes.set(TooltipAttributes.layout, layout);

        String expectedTagName = (layout == TooltipLayout.block) ? "div" : "span";

        String actualTagName = selenium.getEval(JQueryScript.jqScript(tooltip, "get(0).tagName"));
        actualTagName = actualTagName.toLowerCase();

        assertEquals(actualTagName, expectedTagName);
    }

    @Test
    public void testLimitRender() {
        tooltipAttributes.set(limitRender, true);
        tooltipAttributes.set(render, "@this renderChecker");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        retrieveRenderChecker.initializeValue();
        retrieveRequestTime.initializeValue();

        tooltip.recall();
        waitAjax.waitForChange(retrieveRenderChecker);
        assertFalse(retrieveRequestTime.isValueChanged());
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testMode() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);

        retrieveRequestTime.initializeValue();

        tooltip.recall();
        if (mode == TooltipMode.ajax) {
            waitGui.waitForChange(retrieveRequestTime);
        }

        retrieveRequestTime.initializeValue();
        tooltip.hide();
        waitGui.until(elementNotVisible.locator(tooltip));
        assertFalse(retrieveRequestTime.isValueChanged());
    }

    @Test
    @Use(field = "domEvent", value = "domEvents")
    public void testDomEvents() {
        tooltip.recall();

        testFireEvent(domEvent, tooltip);
    }

    @Test
    public void testRendered() {
        tooltipAttributes.set(TooltipAttributes.rendered, false);

        assertFalse(selenium.isElementPresent(tooltip));
    }

    @Test
    @Use(field = "presetDelay", ints = {0, 1000, 5000})
    @RegressionTest("https://issues.jboss.org/browse/RF-10522")
    public void testShowDelay() {

        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.client);
        tooltipAttributes.set(showDelay, presetDelay);

        new DelayTester(presetDelay) {

            public void action() {
                tooltip.recall();
                waitGui.timeout(presetDelay + 2000).until(elementVisible.locator(tooltip));
            }

            public void afterAction() {
                tooltip.hide();
            }
        }.run();
    }

    @Test
    public void testShowEvent() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "mouseup");

        selenium.mouseUpAt(panel, new Point(5, 5));
        waitGui.until(elementVisible.locator(tooltip));
    }

    @Test
    public void testStatus() {
        tooltipAttributes.set(status, "statusChecker");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        retrieveStatusChecker.initializeValue();
        tooltip.recall();
        waitAjax.waitForChange(retrieveStatusChecker);
    }

    @Test
    public void testStyle() {
        super.testStyle(tooltip);
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass(tooltip);
    }

    @Test
    public void testTitle() {
        super.testTitle(tooltip);
    }

    @Test
    public void testZindex() {
        tooltipAttributes.set(zindex, 10);

        String zindex = selenium.getStyle(tooltip, CssProperty.Z_INDEX);
        assertEquals(zindex, "10");
    }

    private void recallTooltipInRightBottomCornerOfPanel(int offsetX, int offsetY) {
        final Point panelPosition = selenium.getElementPosition(panel);
        final Dimension panelDimension = selenium.getElementDimension(panel);

        eventPosition = new Point(panelPosition.getX() + panelDimension.getWidth() - EVENT_OFFSET, panelPosition.getY()
                + panelDimension.getHeight() - EVENT_OFFSET);

        tooltip.recall(panelDimension.getWidth() - EVENT_OFFSET + offsetX, panelDimension.getHeight() - EVENT_OFFSET
                + offsetY);
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
}
