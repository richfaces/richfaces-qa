/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richFileUpload;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.fileUploadAttributes;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richFileUpload/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichFileUpload extends AbstractAjocadoTest {

    private JQueryLocator fileUpload = pjq("div[id$=fileUpload]");
    private JQueryLocator input = pjq("input.rf-fu-inp:nth-child({0})");
    private JQueryLocator addButton = pjq("span.rf-fu-btn-add");
    private JQueryLocator addButtonDisabled = pjq("span.rf-fu-btn-add-dis");
    private JQueryLocator uploadButton = pjq("span.rf-fu-btn-upl");
    private JQueryLocator clearAllButton = pjq("span.rf-fu-btn-clr");
    private JQueryLocator list = pjq("div.rf-fu-lst");
    private JQueryLocator item = pjq("div.rf-fu-itm:nth-child({0})");
    private JQueryLocator itemLabel = pjq("div.rf-fu-itm:nth-child({0}) .rf-fu-itm-lbl");
    private JQueryLocator itemStatus = pjq("div.rf-fu-itm:nth-child({0}) .rf-fu-itm-st");
    private JQueryLocator itemClear = pjq("div.rf-fu-itm:nth-child({0}) .rf-fu-itm-rgh a");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(fileUpload), "File upload is not on the page.");
        assertTrue(selenium.isVisible(addButton), "Add button should be on the page.");
        assertFalse(selenium.isElementPresent(addButtonDisabled), "Disabled add button should not be on the page.");
        assertFalse(selenium.isVisible(uploadButton), "Upload button should not be on the page.");
        assertFalse(selenium.isVisible(clearAllButton), "Clear all button should not be on the page.");
    }

    @Test
    public void testDir() {
        testDir(fileUpload);
    }

    @Test
    public void testDisabled() {
        fileUploadAttributes.set(FileUploadAttributes.disabled, Boolean.TRUE);
        assertTrue(selenium.isElementPresent(fileUpload), "File upload is not on the page.");
        assertFalse(selenium.isElementPresent(addButton), "Add button should not be on the page.");
        assertTrue(selenium.isVisible(addButtonDisabled), "Disabled add button should be on the page.");
        assertFalse(selenium.isElementPresent(uploadButton), "Upload button should not be on the page.");
        assertFalse(selenium.isElementPresent(clearAllButton), "Clear all button should not be on the page.");
    }

    @Test
    public void testLang() {
        testLang(fileUpload);
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, fileUpload);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, fileUpload);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, fileUpload);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, fileUpload);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, fileUpload);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, fileUpload);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, fileUpload);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, fileUpload);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, fileUpload);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, fileUpload);
    }

    @Test
    public void testRendered() {
        fileUploadAttributes.set(FileUploadAttributes.rendered, Boolean.FALSE);
        assertFalse(selenium.isElementPresent(fileUpload), "Component should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(fileUpload);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(fileUpload);
    }

    @Test
    public void testTitle() {
        testTitle(fileUpload);
    }
}
