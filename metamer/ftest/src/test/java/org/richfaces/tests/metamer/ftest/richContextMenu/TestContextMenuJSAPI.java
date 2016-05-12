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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestContextMenuJSAPI extends AbstractWebDriverTest {

    private final Attributes<ContextMenuAttributes> contextMenuAttributes = getAttributes();

    @FindBy(css = "[id$=activateItem1]")
    private WebElement activateItem1Button;
    @FindBy(css = "[id$=activateItem42]")
    private WebElement activateItem42Button;
    @FindBy(css = "[id$=hide]")
    private WebElement hideButton;
    @FindBy(css = "div[id$=ctxMenu]")
    private RichFacesContextMenu menu;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=show]")
    private WebElement showButton;

    @Override
    public String getComponentTestPagePath() {
        return "richContextMenu/simple.xhtml";
    }

    @Test
    public void testActivateItem() {
        contextMenuAttributes.set(ContextMenuAttributes.mode, "ajax");
        getMetamerPage().performJSClickOnButton(activateItem1Button, WaitRequestType.XHR);
        assertEquals(output.getText(), "New", "Menu action was not performed.");
        getMetamerPage().performJSClickOnButton(activateItem42Button, WaitRequestType.XHR);
        assertEquals(output.getText(), "Save All", "Menu action was not performed.");
    }

    @Test
    public void testShowAndHide() {
        menu.advanced().waitUntilIsNotVisible().perform();
        getMetamerPage().performJSClickOnButton(showButton, WaitRequestType.NONE);
        menu.advanced().waitUntilIsVisible().perform();
        getMetamerPage().performJSClickOnButton(hideButton, WaitRequestType.NONE);
        menu.advanced().waitUntilIsNotVisible().perform();
    }
}
