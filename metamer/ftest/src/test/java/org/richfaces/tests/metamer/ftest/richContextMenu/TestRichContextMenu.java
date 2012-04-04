/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.jboss.arquillian.ajocado.Graphene.attributeEquals;
import static org.jboss.arquillian.ajocado.Graphene.elementNotVisible;
import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.contextMenuAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Test for rich:contextMenu component at faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.2.0.Final
 */
public class TestRichContextMenu extends AbstractGrapheneTest {

    private static final Logger log = LoggerFactory.getLogger(TestRichContextMenu.class);

    private JQueryLocator targetPanel1 = pjq("div[id$=targetPanel1]");
    private JQueryLocator targetPanel2 = pjq("div[id$=targetPanel2]");
    // contextMenu elem is always present (and selenium consider it as displayed and visible as well)
    private JQueryLocator contextMenu = jq("div[id$=:ctxMenu]");
    private JQueryLocator ctxMenuItemFormat = jq("div.rf-ctx-itm:eq({0})");

    // content display is triggered by action
    private JQueryLocator contextMenuContent = jq("div.rf-ctx-lst");
    private JQueryLocator output = jq("span[id$=output]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richContextMenu/simple.xhtml");
    }

    private void clickOnTarget(JQueryLocator target) {
        // mouseDownRight doesn't work, so have to use left click for common cases
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, Event.CLICK);

        Point position = selenium.getElementPosition(target);
        selenium.clickAt(target, position.substract(new Point(0, 70)));
    }

    private void testFireContextMenuEvent(Event event, ElementLocator<?> element) {
        testFireContextMenuEvent(event, element, event.getEventName());
    }

    private void testFireContextMenuEvent(Event event, ElementLocator<?> element, String attributeName) {
        ElementLocator<?> eventInput = pjq("input[id$=on" + attributeName + "Input]");
        String value = "metamerEvents += \"" + event.getEventName() + " \"";

        guardHttp(selenium).type(eventInput, value);

        clickOnTarget(targetPanel1);

        waitModel.until(elementVisible.locator(contextMenuContent));

        selenium.fireEvent(element, event);

        waitGui.failWith("Attribute on" + attributeName + " does not work correctly").until(
            new EventFiredCondition(event));
    }

    @Test
    public void testDir() {
        contextMenuAttributes.set(ContextMenuAttributes.dir, "rtl");
        log.debug(selenium.getAttribute(contextMenu.getAttribute(new Attribute("dir"))));
    }

    // @Test
    public void testDirection() {

    }

    @Test
    public void testDisabled() {
        assertTrue(selenium.isElementPresent(contextMenu));
        assertTrue(selenium.isElementPresent(contextMenuContent));

        contextMenuAttributes.set(ContextMenuAttributes.disabled, true);

        // contextMenu is present
        assertTrue(selenium.isElementPresent(contextMenu));
        // but content not, when disabled
        assertFalse(selenium.isElementPresent(contextMenuContent));
    }

    @Test
    public void testHideDelay() {
        contextMenuAttributes.set(ContextMenuAttributes.hideDelay, "500");
        clickOnTarget(targetPanel1);
        assertTrue(selenium.isVisible(contextMenuContent));
        assertTrue(selenium.isVisible(contextMenuContent));

        selenium.fireEvent(targetPanel1, Event.MOUSEUP);
        waitModel.until(elementNotVisible.locator(contextMenuContent));
    }

    @Test
    public void testHorizontalOffset() {

        int offset = 11;

        clickOnTarget(targetPanel1);
        int positionBefore = selenium.getElementPositionLeft(contextMenuContent);

        contextMenuAttributes.set(ContextMenuAttributes.horizontalOffset, offset);
        clickOnTarget(targetPanel1);

        int positionAfter = selenium.getElementPositionLeft(contextMenuContent);

        assertEquals(positionAfter, positionBefore + offset);

    }

    // @Test
    public void testJointPoint() {

    }

    @Test
    public void testLang() {
        String langVal = "cs";
        contextMenuAttributes.set(ContextMenuAttributes.lang, langVal);
        // clickOnTarget(menuElem);
        waitModel.until(attributeEquals.locator(contextMenu.getAttribute(Attribute.LANG)).text(langVal));
    }

    @Test
    public void testMode() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, Event.CLICK);
        Point position = selenium.getElementPosition(targetPanel1);

        // ajax
        contextMenuAttributes.set(ContextMenuAttributes.mode, "ajax");
        selenium.clickAt(targetPanel1, position.substract(new Point(0, 70)));
        guardXhr(selenium).click(ctxMenuItemFormat.format("0"));
        assertEquals(selenium.getText(output), "Open", "Menu action was not performed.");

        // server
        contextMenuAttributes.set(ContextMenuAttributes.mode, "server");
        selenium.clickAt(targetPanel1, position.substract(new Point(0, 70)));
        guardHttp(selenium).click(ctxMenuItemFormat.format("8"));
        assertEquals(selenium.getText(output), "Exit", "Menu action was not performed.");

        // client
        contextMenuAttributes.set(ContextMenuAttributes.mode, "client");
        selenium.clickAt(targetPanel1, position.substract(new Point(0, 70)));
        guardNoRequest(selenium).click(ctxMenuItemFormat.format("0"));
    }

    @Test
    public void testOnclick() {
        testFireContextMenuEvent(Event.CLICK, contextMenu);
    }

    @Test
    public void testOndblclick() {
        testFireContextMenuEvent(Event.DBLCLICK, contextMenu);
    }

    // @Test
    public void testOngrouphide() {

    }

    // @Test
    public void testOngroupshow() {

    }

    @Test
    public void testOnhide() {
        contextMenuAttributes.set(ContextMenuAttributes.hideDelay, "20");
        contextMenuAttributes.set(ContextMenuAttributes.onhide, "metamerEvents += \"hide\"");
        clickOnTarget(targetPanel1);
        clickOnTarget(targetPanel1);
        // selenium.clickAt(menuElem, new Point(-10, -100));

        waitGui.failWith("Attribute onhide does not work correctly").until(new EventFiredCondition(new Event("hide")));

    }

    @Test
    public void testOnitemclick() {
        testFireContextMenuEvent(Event.CLICK, ctxMenuItemFormat.format("0"), "itemclick");
    }

    @Test
    public void testOnkeydown() {
        testFireContextMenuEvent(Event.KEYDOWN, contextMenu);
    }

    @Test
    public void testOnkeypress() {
        testFireContextMenuEvent(Event.KEYPRESS, contextMenu);
    }

    @Test
    public void testOnkeyup() {
        testFireContextMenuEvent(Event.KEYUP, contextMenu);
    }

    @Test
    public void testOnmousedown() {
        testFireContextMenuEvent(Event.MOUSEDOWN, contextMenu);
    }

    @Test
    public void testOnmousemove() {
        testFireContextMenuEvent(Event.MOUSEMOVE, contextMenu);
    }

    @Test
    public void testOnmouseout() {
        testFireContextMenuEvent(Event.MOUSEOUT, contextMenu);
    }

    @Test
    public void testOnmouseover() {
        testFireContextMenuEvent(Event.MOUSEOVER, contextMenu);
    }

    @Test
    public void testOnmouseup() {
        testFireContextMenuEvent(Event.MOUSEUP, contextMenu);
    }

    @Test
    public void testOnshow() {
        contextMenuAttributes.set(ContextMenuAttributes.onshow, "metamerEvents += \"show\"");

        clickOnTarget(targetPanel1);

        // verify appropriate event fired
        waitGui.failWith("Attribute onshow does not work correctly").until(new EventFiredCondition(new Event("show")));
    }

    @Test
    public void testPopupWidth() {
        String minWidth = "333";
        contextMenuAttributes.set(ContextMenuAttributes.popupWidth, minWidth);
        clickOnTarget(targetPanel1);
        String style = selenium.getStyle(contextMenuContent, new CssProperty("min-width"));
        assertEquals(style, minWidth + "px");
    }

    @Test
    public void testRendered() {
        contextMenuAttributes.set(ContextMenuAttributes.rendered, Boolean.FALSE);
        clickOnTarget(targetPanel1);
        assertFalse(selenium.isElementPresent(contextMenuContent));
    }

    // @Test
    public void testShowDelay() {

    }

    // @Test
    public void testShowEvent() {

    }

    @Test
    public void testStyle() {
        String color = "yellow";
        String styleVal = "background-color: " + color + ";";
        contextMenuAttributes.set(ContextMenuAttributes.style, styleVal);
        clickOnTarget(targetPanel1);
        String backgroundColor = selenium.getStyle(contextMenu, CssProperty.BACKGROUND_COLOR);
        // don't know why exactly selenium retrieve bg color in RGB format
        assertTrue(backgroundColor.equals(color) || backgroundColor.equals("rgb(255, 255, 0)"));
    }

    @Test
    public void testStyleClass() {
        String styleClassVal = "test-style-class";
        contextMenuAttributes.set(ContextMenuAttributes.styleClass, styleClassVal);
        String styleClass = selenium.getAttribute(contextMenu.getAttribute(Attribute.CLASS));
        assertTrue(styleClass.contains(styleClassVal));
    }

    @Test
    public void testTarget() {
        // contextMenu element is present always. Check if is displayed
        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel2");
        assertFalse(selenium.isVisible(contextMenuContent));
        clickOnTarget(targetPanel2);
        assertTrue(selenium.isVisible(contextMenuContent));

        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel1");
        assertFalse(selenium.isVisible(contextMenuContent));
        clickOnTarget(targetPanel1);
        assertTrue(selenium.isVisible(contextMenuContent));
    }

    @Test
    public void testTitle() {
        String titleVal = "test title";
        contextMenuAttributes.set(ContextMenuAttributes.title, titleVal);
        assertEquals(selenium.getAttribute(contextMenu.getAttribute(Attribute.TITLE)), titleVal);
    }

    @Test
    public void testVerticalOffset() {
        int offset = 11;

        clickOnTarget(targetPanel1);
        int positionBefore = selenium.getElementPositionTop(contextMenuContent);

        contextMenuAttributes.set(ContextMenuAttributes.verticalOffset, offset);
        clickOnTarget(targetPanel1);

        int positionAfter = selenium.getElementPositionTop(contextMenuContent);

        assertEquals(positionAfter, positionBefore - offset);
    }

}
