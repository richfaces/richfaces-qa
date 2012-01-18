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
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.testng.annotations.Test;


/**
 * Tests for attributes (page /faces/components/a4jMediaOutput/imagePng.xhtml)
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAttributes extends AbstractMediaOutputTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jMediaOutput/imagePng.xhtml");
    }    
    
    @Test
    public void testAccesskey() {
        testHtmlAttribute(MEDIA_OUTPUT, "accesskey", "r");
    }

    @Test
    public void testAlign() {
        testHtmlAttribute(MEDIA_OUTPUT, "align", "left");
    }

    @Test
    public void testBorder() {
        testHtmlAttribute(MEDIA_OUTPUT, "border", "3");
    }

    @Test
    public void testCharset() {
        testHtmlAttribute(MEDIA_OUTPUT, "charset", "utf-8");
    }

    @Test
    public void testCoords() {
        testHtmlAttribute(MEDIA_OUTPUT, "coords", "circle: 150, 60, 60");
    }

    @Test
    public void testDir() {
        testDir(MEDIA_OUTPUT);
    }

    @Test
    public void testHreflang() {
        testHtmlAttribute(MEDIA_OUTPUT, "hreflang", "sk");
    }

    @Test
    public void testIsmap() {
        AttributeLocator<?> attr = MEDIA_OUTPUT.getAttribute(new Attribute("ismap"));
        assertFalse(selenium.isAttributePresent(attr), "Attribute ismap should not be present.");

        selenium.click(pjq("input[type=radio][name$=ismapInput][value=true]"));
        selenium.waitForPageToLoad();

        assertTrue(selenium.isAttributePresent(attr), "Attribute ismap should be present.");
        assertEquals(selenium.getAttribute(attr), "true", "Attribute dir");
    }

    @Test
    public void testOnblur() {
        testFireEvent(Event.BLUR, MEDIA_OUTPUT);
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, MEDIA_OUTPUT);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, MEDIA_OUTPUT);
    }

    @Test
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, MEDIA_OUTPUT);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, MEDIA_OUTPUT);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, MEDIA_OUTPUT);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, MEDIA_OUTPUT);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, MEDIA_OUTPUT);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, MEDIA_OUTPUT);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, MEDIA_OUTPUT);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, MEDIA_OUTPUT);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, MEDIA_OUTPUT);
    }

    @Test
    public void testRel() {
        testHtmlAttribute(MEDIA_OUTPUT, "rel", "metamer");
    }

    @Test
    public void testRendered() {
        JQueryLocator input = pjq("input[type=radio][name$=renderedInput][value=false]");
        selenium.click(input);
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(MEDIA_OUTPUT), "Image should not be rendered when rendered=false.");
    }

    @Test
    public void testRev() {
        testHtmlAttribute(MEDIA_OUTPUT, "rev", "metamer");
    }

    @Test
    public void testShape() {
        testHtmlAttribute(MEDIA_OUTPUT, "shape", "default");
    }

    @Test
    public void testStyle() {
        testStyle(MEDIA_OUTPUT);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(MEDIA_OUTPUT);
    }

    @Test
    public void testTabindex() {
        testHtmlAttribute(MEDIA_OUTPUT, "tabindex", "50");
    }

    @Test
    public void testTarget() {
        testHtmlAttribute(MEDIA_OUTPUT, "target", "_blank");
    }

    @Test
    public void testTitle() {
        testTitle(MEDIA_OUTPUT);
    }

    @Test
    public void testUsemap() {
        testHtmlAttribute(MEDIA_OUTPUT, "usemap", "metamer");
    }    
    
}
