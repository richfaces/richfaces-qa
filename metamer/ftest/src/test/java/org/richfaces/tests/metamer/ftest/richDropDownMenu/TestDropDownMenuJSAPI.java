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
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestDropDownMenuJSAPI extends AbstractDropDownMenuTest {

    @FindBy(css = "[id$=activateItem1]")
    private WebElement activateItem1Button;
    @FindBy(css = "[id$=activateItem42]")
    private WebElement activateItem42Button;
    @FindBy(css = "[id$=hide]")
    private WebElement hideButton;
    @FindBy(css = "[id$=show]")
    private WebElement showButton;

    @Override
    public String getComponentTestPagePath() {
        return "richDropDownMenu/topMenu.xhtml";
    }

    @Test
    public void testActivateItem() {
        getMetamerPage().performJSClickOnButton(activateItem1Button, WaitRequestType.XHR);
        assertEquals(getPage().getOutput().getText(), "New", "Menu action was not performed.");
        getMetamerPage().performJSClickOnButton(activateItem42Button, WaitRequestType.XHR);
        assertEquals(getPage().getOutput().getText(), "Save All", "Menu action was not performed.");
    }

    @Test
    public void testShowAndHide() {
        getCurrentMenu().advanced().waitUntilIsNotVisible().perform();
        getMetamerPage().performJSClickOnButton(showButton, WaitRequestType.NONE);
        getCurrentMenu().advanced().waitUntilIsVisible().perform();
        getMetamerPage().performJSClickOnButton(hideButton, WaitRequestType.NONE);
        getCurrentMenu().advanced().waitUntilIsNotVisible().perform();
    }
}
