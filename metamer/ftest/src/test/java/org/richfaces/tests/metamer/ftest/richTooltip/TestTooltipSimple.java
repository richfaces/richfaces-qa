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

import static java.text.MessageFormat.format;
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
import java.util.Arrays;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
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
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
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
    // TooltipAttributes attributes = new TooltipAttributes();
    // JQueryLocator panel = pjq("div[id$=panel]");
    // TooltipModel tooltip = new TooltipModel(jq(".rf-tt"), panel);

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

        // retrieveRequestTime.initializeValue();
        MetamerPage.requestTimeChangesWaiting(page.tooltip).recall(page.panel);
        // waitGui.waitForChange(retrieveRequestTime);

        assertEquals(expectedReturnJS("return window.data;", "RichFaces 4"), "RichFaces 4"); // retrieveWindowData.retrieve(), "RichFaces 4");
    }

    @Test
    public void testRequestEventHandlers() {
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.tooltip.setMode(TooltipMode.ajax);

        testRequestEventsBefore("begin", "beforedomupdate", "complete");
        // retrieveRequestTime.initializeValue();
        MetamerPage.requestTimeChangesWaiting(page.tooltip).recall(page.panel);
        // waitGui.waitForChange(retrieveRequestTime);
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

        /*
        new DelayTester(presetDelay) {

            public void beforeAction() {
                page.tooltip.recall(page.panel);
            }

            public void action() {
                page.tooltip.hide(page.panel);
                // waitGui.timeout(presetDelay + 2000).until(elementNotVisible.locator(page.tooltip));
                // Graphene.waitModel() .until("Page was not updated").element(page.tooltip.root).is().not().visible();
                WebDriverWait<Void> wait = new WebDriverWait<Void>(null, driver, presetDelay + 2000);
                wait.until("Page was not updated").element(page.tooltip.root).is().not().visible();
            }
        }.run();
        */

        page.tooltip.recall(page.panel);
        page.tooltip.hide(page.panel);
        // waitGui.timeout(presetDelay + 2000).until(elementNotVisible.locator(page.tooltip));
        // Graphene.waitModel() .until("Page was not updated").element(page.tooltip.root).is().not().visible();
        WebDriverWait<Void> wait = new WebDriverWait<Void>(null, driver, presetDelay + 2);
        wait.until("Tooltip didn't disappears!").element(page.tooltip.root).is().not().visible();
    }

    @Test
    public void testHideEvent() {
        tooltipAttributes.set(TooltipAttributes.hideEvent, "mouseup");

        page.tooltip.recall(page.panel);

        // selenium.mouseUpAt(page.panel, new Point(5, 5));
        new Actions(driver).moveToElement(page.panel).build().perform();
        // waitGui.until(elementNotVisible.locator(page.tooltip));
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

        // String actualTagName = selenium.getEval(JQueryScript.jqScript(page.tooltip, "get(0).tagName"));
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

        // retrieveRenderChecker.initializeValue();
        String renderCheckerText = page.renderCheckerOutput.getText();
        // retrieveRequestTime.initializeValue();

        MetamerPage.requestTimeNotChangesWaiting(page.tooltip).recall(page.panel);
        // waitAjax.waitForChange(retrieveRenderChecker);
        Graphene.waitGui().until(Graphene.element(page.renderCheckerOutput).text().not().equalTo(renderCheckerText));
        // assertFalse(retrieveRequestTime.isValueChanged());
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testMode() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);

        page.tooltip.setMode(mode);

        // retrieveRequestTime.initializeValue();

        if (mode == TooltipMode.ajax) {
            // waitGui.waitForChange(retrieveRequestTime);
            MetamerPage.requestTimeChangesWaiting(page.tooltip).recall(page.panel);
        } else {
            page.tooltip.recall(page.panel);
        }

        // retrieveRequestTime.initializeValue();
        // page.tooltip.hide(page.panel);
        MetamerPage.requestTimeNotChangesWaiting(page.tooltip).hide(page.panel);

        // waitGui.until(elementNotVisible.locator(page.tooltip));
        Graphene.waitGui().until(Graphene.element(page.tooltip.root).not().isVisible());
        // assertFalse(retrieveRequestTime.isValueChanged());
    }

    // @Test excluded since doesn't pass :-(
    @Use(field = "domEvent", value = "domEvents")
    public void testDomEvents() {
        page.tooltip.setMode(TooltipMode.client);

        page.tooltip.recall(page.panel);

        // testFireEvent(tooltipAttributes, page.tooltip.root);
        // testFireEventWithJS(page.tooltip, domEvent, tooltipAttributes, TooltipAttributes.???);
    }

    @Test
    public void testOnBefereDOMUpdate() {
        // in client mode no DOM update triggered
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.tooltip.setMode(TooltipMode.ajax);

        // verifyEventHandler(TooltipAttributes.onbeforedomupdate, null);
        page.tooltip.recall(page.panel);

        Action eventFiringAction = new Actions(driver).moveByOffset(1, 1).build();
        testFireEvent(tooltipAttributes, TooltipAttributes.onbeforedomupdate, eventFiringAction);
    }

    @Test
    public void testRendered() {
        tooltipAttributes.set(TooltipAttributes.rendered, false);
        // TODO JJa: I think this require trigger tooltip render first
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
            // WebDriverWait<Void> waitBeforeDisplay = new WebDriverWait<Void>(null, driver, 2);
            // waitBeforeDisplay.until().element(page.tooltip.root).is().not().present();
            waiting(2000);
            assertFalse(Graphene.element(page.tooltip.root).isVisible().apply(driver), "Tooltip shouldn't be displayed before deplay timeout (" + presetDelay + ") is over.");
        }
        WebDriverWait<Void> wait = new WebDriverWait<Void>(null, driver, presetDelay/1000 + 2);
        wait.until(Graphene.element(page.tooltip.root).isPresent());
        wait.until(Graphene.element(page.tooltip.root).isVisible());
        page.tooltip.hide(page.panel);

        /*
        new DelayTester(presetDelay) {

            public void action() {
                page.tooltip.recall(page.panel);
                // waitGui.timeout(presetDelay + 2000).until(elementVisible.locator(page.tooltip));
                Graphene.waitModel().until(Graphene.element(page.tooltip.root).isPresent());
            }

            public void afterAction() {
                page.tooltip.hide(page.panel);
            }
        }.run();
        */
    }

    @Test
    public void testShowEvent() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "mouseup");

        // selenium.mouseUpAt(page.panel, new Point(5, 5));
        new Actions(driver).clickAndHold(page.panel).release(page.panel).build().perform();
        // waitGui.until(elementVisible.locator(page.tooltip));
        Graphene.waitGui().until(Graphene.element(page.tooltip.root).isVisible());
    }

    @Test
    public void testStatus() {
        tooltipAttributes.set(TooltipAttributes.status, "statusChecker");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.tooltip.setMode(TooltipMode.ajax);

        // retrieveStatusChecker.initializeValue();
        String statusChecker = page.statusCheckerOutput.getText();
        page.tooltip.recall(page.panel);
        // waitAjax.waitForChange(retrieveStatusChecker);
        Graphene.waitAjax().until(Graphene.element(page.statusCheckerOutput).text().not().equalTo(statusChecker));
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

    public void testRequestEventsBefore(String... events) {
        for (String event : events) {
            String inputExp = format(ATTR_INPUT_LOC_FORMAT, event);
            WebElement input = page.attributesTable.findElement(By.cssSelector(inputExp));
            String inputVal = format("metamerEvents += \"{0} \"", event);
            // even there would be some events (in params) twice, don't expect handle routine to be executed twice
            input.clear();
            waiting(1000);
            input = page.attributesTable.findElement(By.cssSelector(inputExp));
            input.sendKeys(inputVal);
            // sendKeys triggers page reload automatically
            waiting(300);
            Graphene.waitAjax().until(ElementPresent.getInstance().element(page.attributesTable));
            input = page.attributesTable.findElement(By.cssSelector(inputExp));
            MetamerPage.waitRequest(input, WaitRequestType.HTTP).submit();
        }
        executeJS("window.metamerEvents = \"\";");
    }

    public void testRequestEventsAfter(String... events) {
        String[] actualEvents = ((String)executeJS("return window.metamerEvents")).split(" ");
        assertEquals(actualEvents, events, format("The events ({0}) don't came in right order ({1})",
            Arrays.deepToString(actualEvents), Arrays.deepToString(events)));
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
        tooltipAttributes.set(attr, "metamerEvents += " + attributeName);
        executeJS("metamerEvents = \"\";");

        page.tooltip.recall(page.panel);

        assertEquals(expectedReturnJS("return metamerEvents", attributeName), attributeName, "Attribute " + attributeName + " does not work.");
    }

}
