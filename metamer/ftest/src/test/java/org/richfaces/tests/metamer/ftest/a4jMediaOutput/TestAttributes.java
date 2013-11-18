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
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.mediaOutputAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.net.URL;

import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
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
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.accesskey, "r");
    }

    @Test
    public void testAlign() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.align, "left");
    }

    @Test
    public void testBorder() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.border, "3");
    }

    @Test
    public void testCharset() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.charset, "utf-8");
    }

    @Test
    public void testCoords() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.coords, "circle: 150, 60, 60");
    }

    @Test
    public void testDir() {
        testDir(mediaOutput);
    }

    @Test
    public void testHreflang() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.hreflang, "sk");
    }

    @Test
    public void testIsmap() {
        String attribute = mediaOutput.getAttribute("ismap");
        assertNull(attribute, "Attribute ismap should not be present.");

        mediaOutputAttributes.set(MediaOutputAttributes.ismap, true);

        attribute = mediaOutput.getAttribute("ismap");
        assertNotNull(attribute, "Attribute ismap should be present.");
        if (attribute.contains("ismap")) {
            assertEquals(attribute, "ismap", "Attribute ismap");
        } else {
            assertEquals(attribute, "true", "Attribute ismap");
       }
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13070")
    public void testOnblur() {
        testFireEvent(Event.BLUR, mediaOutput);
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, mediaOutput);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, mediaOutput);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13070")
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, mediaOutput);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, mediaOutput);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, mediaOutput);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, mediaOutput);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, mediaOutput);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, mediaOutput);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, mediaOutput);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, mediaOutput);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, mediaOutput);
    }

    @Test
    public void testRel() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.rel, "metamer");
    }

    @Test
    public void testRendered() {
        mediaOutputAttributes.set(MediaOutputAttributes.rendered, false);
        assertFalse(mediaOutput.isPresent(), "Image should not be rendered when rendered=false.");
    }

    @Test
    public void testRev() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.rev, "metamer");
    }

    @Test
    public void testShape() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.shape, "default");
    }

    @Test
    public void testStyle() {
        testStyle(mediaOutput);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(mediaOutput);
    }

    @Test
    public void testTabindex() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.tabindex, "50");
    }

    @Test
    public void testTarget() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.target, "_blank");
    }

    @Test
    public void testTitle() {
        testTitle(mediaOutput);
    }

    @Test
    public void testUsemap() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.usemap, "metamer");
    }

}
