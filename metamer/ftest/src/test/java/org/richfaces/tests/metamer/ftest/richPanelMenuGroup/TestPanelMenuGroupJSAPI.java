/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenuGroup;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPanelMenuGroupJSAPI extends AbstractWebDriverTest {

    @FindBy(css = "[id$=collapse]")
    private WebElement collapseButton;
    @FindBy(css = "[id$=expand]")
    private WebElement expandButton;
    @FindBy(css = "[id$=current]")
    private WebElement output;
    @FindBy(css = "[id$=group23]")
    private RichFacesPanelMenuGroup pmg;
    @FindBy(css = "[id$=select]")
    private WebElement selectButton;

    @Override
    public String getComponentTestPagePath() {
        return "richPanelMenuGroup/simple.xhtml";
    }

    @Test
    public void testCollapseAndExpand() {
        assertTrue(pmg.advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(collapseButton, WaitRequestType.XHR);
        assertFalse(pmg.advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(expandButton, WaitRequestType.XHR);
        assertTrue(pmg.advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(collapseButton, WaitRequestType.XHR);
        assertFalse(pmg.advanced().isExpanded());
    }

    @Test
    public void testSelect() {
        getMetamerPage().performJSClickOnButton(selectButton, WaitRequestType.XHR);
        assertEquals(output.getText(), "group23 - sample name");
    }
}
