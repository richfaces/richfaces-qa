/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPanel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.BasicAttributes.bodyClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richPanel/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichPanel extends AbstractAjocadoTest {

    private JQueryLocator[] panels = {pjq("div[id$=panelWithHeader]"), pjq("div[id$=panelWithoutHeader]")};
    private JQueryLocator[] headers = {pjq("div[id$=panelWithHeader] div.rf-p-hdr"), pjq("div[id$=panelWithoutHeader] div.rf-p-hdr")};
    private JQueryLocator[] bodies = {pjq("div[id$=panelWithHeader] div.rf-p-b"), pjq("div[id$=panelWithoutHeader] div.rf-p-b")};
    @Inject
    @Use(empty = true)
    private JQueryLocator panel;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanel/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(panels[0]), "Panel with header should be present on the page.");
        assertTrue(selenium.isVisible(panels[0]), "Panel with header should be present on the page.");
        assertTrue(selenium.isElementPresent(panels[1]), "Panel without header should be present on the page.");
        assertTrue(selenium.isVisible(panels[1]), "Panel without header should be present on the page.");

        assertTrue(selenium.isElementPresent(headers[0]), "The first panel should have a header.");
        assertTrue(selenium.isVisible(headers[0]), "The first panel should have a header.");
        assertFalse(selenium.isElementPresent(headers[1]), "The second panel should not have any header.");

        assertTrue(selenium.isElementPresent(bodies[0]), "The first panel should have a body.");
        assertTrue(selenium.isVisible(bodies[0]), "The first panel should have a body.");
        assertTrue(selenium.isElementPresent(bodies[1]), "The second panel should have a body.");
        assertTrue(selenium.isVisible(bodies[1]), "The second panel should have a body.");

        assertEquals(selenium.getText(headers[0]), "header of panel", "Header of the first panel.");
        assertTrue(selenium.getText(bodies[0]).startsWith("Lorem ipsum"), "First panel's body should start with \"Lorem ipsum\".");
        assertTrue(selenium.getText(bodies[1]).startsWith("Nulla ornare"), "Second panel's body should start with \"Nulla ornare\".");
    }

    @Test
    @Use(field = "panel", value = "bodies")
    public void testBodyClass() {
        testStyleClass(panel, bodyClass);
    }

    @Test
    public void testHeader() {
        selenium.type(pjq("input[type=text][id$=headerInput]"), "new header");
        selenium.waitForPageToLoad();

        assertEquals(selenium.getText(headers[0]), "header of panel", "Header of the first panel should not change (facet defined).");
        assertEquals(selenium.getText(headers[1]), "new header", "Header of the second panel.");
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(headers[0], headerClass);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnclick() {
        testFireEvent(Event.CLICK, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, panel);
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(panels[0]), "First panel should not be rendered when rendered=false.");
        assertFalse(selenium.isElementPresent(panels[0]), "Second panel should not be rendered when rendered=false.");
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testStyle() {
        testStyle(panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testStyleClass() {
        testStyleClass(panel);
    }

    @Test
    @Use(field = "panel", value = "panels")
    public void testTitle() {
        testTitle(panel);
    }
}
