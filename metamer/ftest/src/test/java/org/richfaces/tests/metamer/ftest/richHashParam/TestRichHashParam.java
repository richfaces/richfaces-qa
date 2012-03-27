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
package org.richfaces.tests.metamer.ftest.richHashParam;

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.geometry.Offset;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richHashParam/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22798 $
 */
public class TestRichHashParam extends AbstractAjocadoTest {

    private JQueryLocator openButton = pjq("input[id$=openPanelButton]");
    private JQueryLocator panel = pjq("div[id$=popupPanel]");
    private JQueryLocator panelContainer = jq("div.rf-pp-cntr");
    private JQueryLocator header = jq("div.rf-pp-hdr");
    private JQueryLocator controls = jq("div.rf-pp-hdr-cntrls a");
    private JQueryLocator scroller = jq("div.rf-pp-cnt-scrlr");
    private JQueryLocator shadow = jq("div.rf-pp-shdw");
    private JQueryLocator resizerW = jq("div.rf-pp-hndlr-l");
    private JQueryLocator resizerN = jq("div.rf-pp-hndlr-t");
    private JQueryLocator resizerE = jq("div.rf-pp-hndlr-r");
    private JQueryLocator resizerS = jq("div.rf-pp-hndlr-b");
    private JQueryLocator resizerNW = jq("div.rf-pp-hndlr-tl");
    private JQueryLocator resizerNE = jq("div.rf-pp-hndlr-tr");
    private JQueryLocator resizerSW = jq("div.rf-pp-hndlr-bl");
    private JQueryLocator resizerSE = jq("div.rf-pp-hndlr-br");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richHashParam/simple.xhtml");
    }

    @Test
    public void testPanelInit() {
        assertTrue(selenium.isElementPresent(openButton), "Button for opening popup should be on the page.");
        assertTrue(selenium.isElementPresent(panel), "Popup panel is not present on the page.");
        assertFalse(selenium.isVisible(panel), "Popup panel is visible.");

        selenium.click(openButton);
        assertTrue(selenium.isVisible(panel), "Popup panel should be visible.");
        assertTrue(selenium.isVisible(panelContainer), "Popup panel's container should be visible.");
        assertTrue(selenium.isVisible(header), "Popup panel's header should be visible.");
        assertTrue(selenium.isVisible(controls), "Popup panel's controls should be visible.");
        assertTrue(selenium.isVisible(scroller), "Popup panel's scroller should be visible.");
        assertTrue(selenium.isVisible(shadow), "Popup panel's shadow should be visible.");
        assertTrue(selenium.isVisible(resizerE), "Popup panel's right resizer should be visible.");
        assertTrue(selenium.isVisible(resizerN), "Popup panel's top resizer should be visible.");
        assertTrue(selenium.isVisible(resizerS), "Popup panel's bottom resizer should be visible.");
        assertTrue(selenium.isVisible(resizerW), "Popup panel's left resizer should be visible.");
        assertTrue(selenium.isVisible(resizerNE), "Popup panel's top-right resizer should be visible.");
        assertTrue(selenium.isVisible(resizerNW), "Popup panel's top-left resizer should be visible.");
        assertTrue(selenium.isVisible(resizerSE), "Popup panel's bottom-right resizer should be visible.");
        assertTrue(selenium.isVisible(resizerSW), "Popup panel's bottom-left resizer should be visible.");
    }

    @Test
    public void testPanelMove() {
        selenium.click(openButton);
        int panelLeft = selenium.getElementPositionLeft(panelContainer);
        int shadowLeft = selenium.getElementPositionLeft(shadow);

        assertEquals(selenium.getStyle(header, new CssProperty("cursor")), "move",
            "Cursor used when mouse is over panel's header.");
        selenium.dragAndDrop(header, new Offset(20, 0));

        assertEquals(selenium.getElementPositionLeft(panelContainer), panelLeft + 20,
            "Panel's position after move to the right (20px).");
        assertEquals(selenium.getElementPositionLeft(shadow), shadowLeft + 20,
            "Shadow's position after move to the right (20px).");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testPanelResizeHorizontal() {
        selenium.click(openButton);
        int panelWidth = selenium.getElementWidth(panelContainer);
        int panelHeight = selenium.getElementHeight(panelContainer);
        int shadowWidth = selenium.getElementWidth(shadow);
        int shadowHeight = selenium.getElementHeight(shadow);

        assertEquals(selenium.getStyle(resizerW, new CssProperty("cursor")), "w-resize",
            "Cursor used when mouse is over panel's left resizer.");
        assertEquals(selenium.getStyle(resizerE, new CssProperty("cursor")), "e-resize",
            "Cursor used when mouse is over panel's right resizer.");

        selenium.dragAndDrop(resizerW, new Offset(100, 0));

        assertEquals(selenium.getElementWidth(panelContainer), panelWidth - 100,
            "Panel's width after resizing horizontally (-100px).");
        assertEquals(selenium.getElementHeight(panelContainer), panelHeight,
            "Panel's height after resizing horizontally.");
        assertEquals(selenium.getElementWidth(shadow), shadowWidth - 100,
            "Shadow's width after resizing horizontally (-100px).");
        assertEquals(selenium.getElementHeight(shadow), shadowHeight, "Shadow's height after resizing horizontally.");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testPanelHeight() {
        selenium.click(openButton);
        waitGui.failWith("Panel was not opened.").until(elementVisible.locator(panel));
        assertEquals(selenium.getStyle(panelContainer, CssProperty.HEIGHT), "345px", "Height of the panel");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testPanelLeft() {
        selenium.click(openButton);
        waitGui.failWith("Panel was not opened.").until(elementVisible.locator(panel));
        assertEquals(selenium.getElementPositionLeft(panelContainer), 77, "Left margin of the panel");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testPanelTop() {
        selenium.click(openButton);
        waitGui.failWith("Panel was not opened.").until(elementVisible.locator(panel));
        assertEquals(selenium.getElementPositionTop(panelContainer), 22, "Top margin of the panel");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testPanelWidth() {
        selenium.click(openButton);
        waitGui.failWith("Panel was not opened.").until(elementVisible.locator(panel));
        assertEquals(selenium.getStyle(panelContainer, CssProperty.WIDTH), "543px", "Width of the panel");
    }
}
