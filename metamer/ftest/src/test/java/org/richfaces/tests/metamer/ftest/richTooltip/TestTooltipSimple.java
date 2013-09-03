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
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.tooltipAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.ui.common.Positioning;
import org.richfaces.ui.output.tooltip.TooltipLayout;
import org.richfaces.ui.output.tooltip.TooltipMode;
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
    Event[] domEvents = { Event.CLICK, Event.DBLCLICK, Event.MOUSEDOWN, Event.MOUSEMOVE, Event.MOUSEOUT, Event.MOUSEOVER, Event.MOUSEUP };
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
        page.getTooltip().setMode(TooltipMode.ajax);
        MetamerPage.requestTimeChangesWaiting(page.getTooltip()).recall(page.getPanel());
        page.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test
    public void testData() {
        tooltipAttributes.set(TooltipAttributes.data, "RichFaces 4");
        tooltipAttributes.set(TooltipAttributes.oncomplete, "data = event.data");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.getTooltip().setMode(TooltipMode.ajax);

        MetamerPage.requestTimeChangesWaiting(page.getTooltip()).recall(page.getPanel());

        assertEquals(expectedReturnJS("return window.data;", "RichFaces 4"), "RichFaces 4"); // retrieveWindowData.retrieve(),
                                                                                             // "RichFaces 4");
    }

    @Test
    public void testRequestEventHandlers() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.getTooltip().setMode(TooltipMode.ajax);

        testRequestEventsBefore("begin", "beforedomupdate", "complete");
        MetamerPage.requestTimeChangesWaiting(page.getPanel()).click();
        testRequestEventsAfter("begin", "beforedomupdate", "complete");
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        super.testDir(page.getTooltip().getRoot());
    }

    @Test
    @Uses({ @Use(field = "direction", enumeration = true), @Use(field = "verticalOffset", value = "offsets"),
        @Use(field = "horizontalOffset", value = "offsets") })
    @Templates(value = { "plain", "richCollapsibleSubTable", "richExtendedDataTable", "richPopupPanel" })
    public void testPositioning() {
        int tolerance = 5;
        tooltipAttributes.set(TooltipAttributes.direction, direction);
        tooltipAttributes.set(TooltipAttributes.horizontalOffset, horizontalOffset);
        tooltipAttributes.set(TooltipAttributes.verticalOffset, verticalOffset);

        if (direction == Positioning.auto) {
            direction = Positioning.topRight;
        }

        recallTooltipInRightBottomCornerOfPanel(0, 0);

        final Point tooltipPosition = page.getTooltip().getRoot().getLocation();
        final Dimension tooltipDimension = page.getTooltip().getRoot().getSize();

        if (getHorizontalAlignment() != null) {
            switch (getHorizontalAlignment()) {
                case RIGHT:
                    assertEquals(tooltipPosition.getX(), eventPosition.getX() + horizontalOffset, tolerance);
                    break;
                case LEFT:
                    assertEquals(tooltipPosition.getX() + tooltipDimension.getWidth(), eventPosition.getX()
                        - horizontalOffset, tolerance);
                    break;
                default:
                    break;
            }
        }

        if (getVerticalAlignment() != null) {
            switch (getVerticalAlignment()) {
                case BOTTOM:
                    assertEquals(tooltipPosition.getY(), eventPosition.getY() + verticalOffset, tolerance);
                    break;
                case TOP:
                    assertEquals(tooltipPosition.getY() + tooltipDimension.getHeight(), eventPosition.getY()
                        - verticalOffset, tolerance);
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

        Point initialPosition = page.getTooltip().getRoot().getLocation();

        recallTooltipInRightBottomCornerOfPanel(-5, -5);

        Point nextPosition = page.getTooltip().getRoot().getLocation();

        int expectedOffset = (followMouse) ? -5 : 0;

        assertEquals(nextPosition.getX() - initialPosition.getX(), expectedOffset);
        assertEquals(nextPosition.getY() - initialPosition.getY(), expectedOffset);
    }

    @Test
    @Use(field = "presetDelay", ints = { 0, 1, 5 })
    public void testHideDelay() {

        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        tooltipAttributes.set(TooltipAttributes.hideDelay, presetDelay);

        page.getTooltip().setMode(TooltipMode.ajax);

        page.getTooltip().recall(page.getPanel());
        page.getTooltip().hide(page.getPanel());
        WebDriverWait<Void> wait = new WebDriverWait<Void>(null, driver, presetDelay + 2);
        wait.until("Tooltip didn't disappears!").element(page.getTooltip().getRoot()).is().not().visible();
    }

    @Test
    public void testHideEvent() {
        tooltipAttributes.set(TooltipAttributes.hideEvent, "mouseup");

        page.getTooltip().recall(page.getPanel());

        new Actions(driver).moveToElement(page.getPanel()).build().perform();
        Graphene.waitModel().until("Page was not updated").element(page.getTooltip().getRoot()).is().present();
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        super.testAttributeLang(page.getTooltip().getRoot());
    }

    @Test
    @Use(field = "layout", enumeration = true)
    @Templates(value = "plain")
    public void testLayout() {
        tooltipAttributes.set(TooltipAttributes.layout, layout);

        String expectedTagName = (layout == TooltipLayout.block) ? "div" : "span";

        String actualTagName = page.getTooltip().getRoot().getTagName();
        actualTagName = actualTagName.toLowerCase();

        assertEquals(actualTagName, expectedTagName);
    }

    @Test
    public void testLimitRender() {
        tooltipAttributes.set(TooltipAttributes.limitRender, true);
        tooltipAttributes.set(TooltipAttributes.render, "@this renderChecker");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.getTooltip().setMode(TooltipMode.ajax);

        String renderCheckerText = page.getRenderCheckerOutputElement().getText();

        MetamerPage.requestTimeNotChangesWaiting(page.getTooltip()).recall(page.getPanel());
        Graphene.waitGui().until().element(page.getRenderCheckerOutputElement()).text().not()
            .equalTo(renderCheckerText);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testMode() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);

        page.getTooltip().setMode(mode);

        if (mode == TooltipMode.ajax) {
            MetamerPage.requestTimeChangesWaiting(page.getTooltip()).recall(page.getPanel());
        } else {
            page.getTooltip().recall(page.getPanel());
        }

        MetamerPage.requestTimeNotChangesWaiting(page.getTooltip()).hide(page.getPanel());

        Graphene.waitGui().until().element(page.getTooltip().getRoot()).is().not().visible();
    }

    @Test
    public void testOnBeforeDOMUpdate() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        // in client mode no DOM update triggered
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.getTooltip().setMode(TooltipMode.ajax);

        testRequestEventsBefore("beforedomupdate");
        page.getPanel().click();
        testRequestEventsAfter("beforedomupdate");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnBeforeHide() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);

        testRequestEventsBefore("beforehide");

        // Setting handler cause tooltip disappear,
        // so invoke them between event handler setting
        page.getPanel().click();
        page.getTooltip().hide(page.getPanel());

        testRequestEventsAfter("beforehide");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnBeforeShow() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);

        testRequestEventsBefore("beforeshow");
        page.getPanel().click();
        testRequestEventsAfter("beforeshow");

        page.getTooltip().hide(page.getPanel());
    }

    @Test
    public void testOnBegin() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.getTooltip().setMode(TooltipMode.ajax);

        testRequestEventsBefore("begin");
        page.getPanel().click();
        testRequestEventsAfter("begin");

        page.getTooltip().hide(page.getPanel());
    }

    @Test
    @Use(field = "mode", enumeration = true)
    @Templates(value = "plain")
    public void testOnClick() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);
        Action click = new Actions(driver).click(page.getTooltip().getRoot()).build();
        verifyEventHandler(TooltipAttributes.onclick, click);
    }

    @Test
    public void testOnComplete() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);
        page.getTooltip().setMode(TooltipMode.ajax);

        testRequestEventsBefore("complete");
        page.getPanel().click();
        testRequestEventsAfter("complete");

        page.getTooltip().hide(page.getPanel());
    }

    @Test
    @Use(field = "mode", enumeration = true)
    @Templates(value = "plain")
    public void testOnDblClick() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);
        Action dblClick = new Actions(driver).doubleClick(page.getTooltip().getRoot()).build();
        verifyEventHandler(TooltipAttributes.ondblclick, dblClick);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnHide() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        page.getTooltip().setMode(mode);

        testRequestEventsBefore("hide");
        page.getPanel().click();
        page.getTooltip().hide(page.getPanel());
        testRequestEventsAfter("hide");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testOnShow() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        page.getTooltip().setMode(mode);

        testRequestEventsBefore("show");
        page.getPanel().click();
        testRequestEventsAfter("show");

        page.getTooltip().hide(page.getPanel());
    }

    @Test
    @Use(field = "mode", enumeration = true)
    @Templates(value = "plain")
    public void testOnMouseDown() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);
        Action action = new Actions(driver).clickAndHold(page.getTooltip().getRoot()).build();
        verifyEventHandler(TooltipAttributes.onmousedown, action);
        new Actions(driver).release().perform();
    }

    @Test
    @Use(field = "mode", enumeration = true)
    @Templates(value = "plain")
    public void testOnMouseMove() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);
        Action action = new Actions(driver).moveToElement(page.getTooltip().getRoot(), 1, 1).build();
        verifyEventHandler(TooltipAttributes.onmousemove, action);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    @Templates(value = "plain")
    public void testOnMouseOut() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);

        String attributeName = "mouseout";

        tooltipAttributes.set(TooltipAttributes.onmouseout, "metamerEvents += \"" + TooltipAttributes.onmouseout
            + " \"");
        Event e = new Event(attributeName);
        page.getTooltip().recall(page.getPanel());
        executeJS("metamerEvents = \"\";");

        fireEvent(page.getTooltip().getRoot(), e);
        String returnedString = expectedReturnJS("return metamerEvents", TooltipAttributes.onmouseout.toString());
        assertEquals(returnedString, TooltipAttributes.onmouseout.toString(), "Event " + e + " does not work.");
    }

    @Test
    @Use(field = "mode", enumeration = true)
    @Templates(value = "plain")
    public void testOnMouseOver() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);
        Action action = new Actions(driver).moveToElement(page.getTooltip().getRoot()).build();
        verifyEventHandler(TooltipAttributes.onmouseover, action);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    @Templates(value = "plain")
    public void testOnMouseUp() {
        tooltipAttributes.set(TooltipAttributes.mode, mode);
        page.getTooltip().setMode(mode);
        Action action = new Actions(driver).clickAndHold(page.getTooltip().getRoot())
            .release(page.getTooltip().getRoot()).build();
        verifyEventHandler(TooltipAttributes.onmouseup, action);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        tooltipAttributes.set(TooltipAttributes.rendered, false);
        page.getTooltip().recall(page.getPanel());
        Graphene.waitGui().until().element(page.getTooltip().getRoot()).is().not().present();
    }

    @Test
    @Use(field = "presetDelay", ints = { 0, 1000, 5000 })
    @RegressionTest("https://issues.jboss.org/browse/RF-10522")
    public void testShowDelay() {

        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.client);
        tooltipAttributes.set(TooltipAttributes.showDelay, presetDelay);

        page.getTooltip().setMode(TooltipMode.client);

        page.getTooltip().recall(page.getPanel());

        if (presetDelay > 4000) {
            // make sure that tooltip not displayed earlier than after 2 sec (when showDelay 5 sec)
            waiting(2000);
            Graphene.waitGui()
                .withMessage("Tooltip shouldn't be displayed before deplay timeout (" + presetDelay + ") is over.")
                .until().element(page.getTooltip().getRoot()).is().not().visible();
        }
        WebDriverWait<Void> wait = new WebDriverWait<Void>(null, driver, presetDelay / 1000 + 2);
        wait.until().element(page.getTooltip().getRoot()).is().present();
        wait.until().element(page.getTooltip().getRoot()).is().visible();
        page.getTooltip().hide(page.getPanel());

    }

    @Test
    public void testShowEvent() {
        tooltipAttributes.set(TooltipAttributes.showEvent, "mouseup");

        new Actions(driver).clickAndHold(page.getPanel()).release(page.getPanel()).build().perform();
        Graphene.waitGui().until().element(page.getTooltip().getRoot()).is().visible();
    }

    @Test
    public void testStatus() {
        tooltipAttributes.set(TooltipAttributes.status, "statusChecker");
        tooltipAttributes.set(TooltipAttributes.mode, TooltipMode.ajax);

        page.getTooltip().setMode(TooltipMode.ajax);

        String statusChecker = page.getStatusCheckerOutputElement().getText();
        page.getTooltip().recall(page.getPanel());
        Graphene.waitAjax().until().element(page.getStatusCheckerOutputElement()).text().not().equalTo(statusChecker);
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        super.testStyle(page.getTooltip().getRoot());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        super.testStyleClass(page.getTooltip().getRoot());
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        super.testTitle(page.getTooltip().getRoot());
    }

    @Test
    @Templates(value = "plain")
    public void testZindex() {
        tooltipAttributes.set(TooltipAttributes.zindex, 10);

        String zindex = page.getTooltip().getRoot().getCssValue("z-index"); // selenium.getStyle(page.getTooltip().root,
                                                                            // CssProperty.Z_INDEX);
        assertEquals(zindex, "10");
    }

    @Test
    public void testJsAPIbyClick() {
        page.jsAPIshowClick.click();
        Graphene.waitAjax().until().element(page.getTooltip().getRoot()).is().visible();

        page.jsAPIhideClick.click();
        Graphene.waitAjax().until().element(page.getTooltip().getRoot()).is().not().visible();
    }

    @Test
    public void testJsAPIbyMouseOver() {
        new Actions(driver).moveToElement(page.jsAPIshowMouseOver).build().perform();
        Graphene.waitAjax().until().element(page.getTooltip().getRoot()).is().visible();

        new Actions(driver).moveToElement(page.jsAPIhideMouseOver).build().perform();
        Graphene.waitAjax().until().element(page.getTooltip().getRoot()).is().not().visible();
    }

    public void testRequestEventsBefore(String... events) {
        for (String event : events) {
            String inputExp = format(ATTR_INPUT_LOC_FORMAT, event);
            WebElement input = page.getAttributesTable().findElement(By.cssSelector(inputExp));
            String inputVal = format("metamerEvents += \"{0} \"", event);
            // even there would be some events (in params) twice, don't expect handle routine to be executed twice
            input.clear();
            waiting(1000);
            input = page.getAttributesTable().findElement(By.cssSelector(inputExp));
            input.sendKeys(inputVal);
            // sendKeys triggers page reload automatically
            waiting(300);
            Graphene.waitAjax().until().element(page.getAttributesTable()).is().present();
            input = page.getAttributesTable().findElement(By.cssSelector(inputExp));
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
        final Point panelPosition = page.getPanel().getLocation();
        final Dimension panelDimension = page.getPanel().getSize();

        eventPosition = new Point(panelPosition.getX() + panelDimension.getWidth() - EVENT_OFFSET, panelPosition.getY()
            + panelDimension.getHeight() - EVENT_OFFSET);

        page.getTooltip().recall(page.getPanel(), panelDimension.getWidth() - EVENT_OFFSET + offsetX,
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
        page.getTooltip().recall(page.getPanel());
        executeJS("window.metamerEvents = \"\";");

        eventFiringAction.perform();
        assertEquals(expectedReturnJS("return window.metamerEvents", attr.toString()), attributeName, "Attribute "
            + attributeName + " does not work.");
    }
}
