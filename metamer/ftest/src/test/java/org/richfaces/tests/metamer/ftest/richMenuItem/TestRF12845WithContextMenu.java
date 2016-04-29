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
package org.richfaces.tests.metamer.ftest.richMenuItem;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.contextMenu.AbstractPopupMenu;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12845WithContextMenu extends AbstractWebDriverTest {

    @FindBy(css = "[id$=menu]")
    private RichFacesContextMenu menu;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=targetPanel]")
    private WebElement targetPanel;

    private void assertOutputEqualsTo(String expectedText) {
        Graphene.waitAjax().until().element(getOutput()).text().equalTo(expectedText);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richMenuItem/rf-12845-cm.xhtml";
    }

    public AbstractPopupMenu getMenu() {
        return menu;
    }

    public WebElement getOutput() {
        return output;
    }

    public WebElement getTargetPanel() {
        return targetPanel;
    }

    @Test
    @Skip(On.JSF.VersionLowerThan22.class)
    @IssueTracking("https://issues.jboss.org/browse/RF-14266")
    @RegressionTest("https://issues.jboss.org/browse/RF-12845")
    public void testMenuItemsCanBeCompositeComponents() {
        // setup
        getMenu().advanced().setShowEvent(Event.CLICK);
        getMenu().advanced().setTarget(getTargetPanel());
        jsUtils.scrollToView(getMenu().advanced().getTargetElement());

        // select first item
        Graphene.guardAjax(getMenu()).selectItem(0);
        assertOutputEqualsTo("Item 1");

        // select second item
        Graphene.guardAjax(getMenu()).selectItem(1);
        assertOutputEqualsTo("Item 2");

        // select first item from expanded group
        Graphene.guardAjax(getMenu().expandGroup("Group")).selectItem(0);
        assertOutputEqualsTo("Item 3");

        // select second item from expanded group
        Graphene.guardAjax(getMenu().expandGroup("Group")).selectItem(1);
        assertOutputEqualsTo("Item 4");
    }
}
