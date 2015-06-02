/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richMenuGroup;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richMenuGroup/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestMenuGroupJSApi extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=menuGroup4_list]")
    private WebElement groupList;
    @FindBy(id = "hide")
    private WebElement hideButton;
    @FindBy(css = "div[id$=menuItem41]")
    private WebElement menuItem41;
    @FindBy(id = "show")
    private WebElement showButton;

    private void clickButton(WebElement btn) {
        // cannot use: btn.click()
        // manually it works, but with Selenium it triggers unwanted events on menu
        // e.g. clicking 'hide', then 'show' will cause menu to hide after a little delay
        // workaround: trigger the click with jQuery
        Utils.triggerJQ("click", btn);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richMenuGroup/simple.xhtml";
    }

    @Test
    public void testHide() {
        clickButton(hideButton);
        Graphene.waitGui().withMessage("Save button should not be visible.").until().element(menuItem41).is().not().visible();
        Graphene.waitGui().withMessage("Group list should not be visible.").until().element(groupList).is().not().visible();
        testShow();// show the group
        clickButton(hideButton);
        Graphene.waitGui().withMessage("Save button should not be visible.").until().element(menuItem41).is().not().visible();
        Graphene.waitGui().withMessage("Group list should not be visible.").until().element(groupList).is().not().visible();
    }

    @Test
    public void testShow() {
        clickButton(showButton);
        Graphene.waitGui().withMessage("Save button should be visible.").until().element(menuItem41).is().visible();
        Graphene.waitGui().withMessage("Group list should be visible.").until().element(groupList).is().visible();
    }
}
