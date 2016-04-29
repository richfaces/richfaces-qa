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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.contextMenu.AbstractPopupMenu;
import org.richfaces.fragment.dropDownMenu.RichFacesDropDownMenu;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12845WithDropDownMenu extends TestRF12845WithContextMenu {

    @FindBy(css = "[id$=menu]")
    private RichFacesDropDownMenu menu;
    @FindBy(css = "[id$=menu]")
    private WebElement menuRoot;

    @Override
    public String getComponentTestPagePath() {
        return "richMenuItem/rf-12845-ddm.xhtml";
    }

    @Override
    public AbstractPopupMenu getMenu() {
        return menu;
    }

    @Override
    public WebElement getTargetPanel() {
        return menuRoot;
    }

    @Test
    @Skip(On.JSF.VersionLowerThan22.class)
    @IssueTracking("https://issues.jboss.org/browse/RF-14266")
    @RegressionTest("https://issues.jboss.org/browse/RF-12845")
    public void testMenuItemsCanBeCompositeComponents() {
        super.testMenuItemsCanBeCompositeComponents();
    }
}
