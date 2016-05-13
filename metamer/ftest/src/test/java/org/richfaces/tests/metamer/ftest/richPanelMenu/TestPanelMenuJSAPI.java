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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@IssueTracking("https://issues.jboss.org/browse/RF-12778")
public class TestPanelMenuJSAPI extends AbstractPanelMenuTest {

    @FindBy(css = "[id$=collapseAll]")
    private WebElement collapseAllButton;
    @FindBy(css = "[id$=collapseAllCC]")
    private WebElement collapseAllCCButton;
    @FindBy(css = "[id$=expandAll]")
    private WebElement expandAllButton;
    @FindBy(css = "[id$=expandAllCC]")
    private WebElement expandAllCCButton;
    @FindBy(css = "[id$=current]")
    private WebElement output;
    @FindBy(css = "[id$=selectItem23]")
    private WebElement selectItem23Button;
    @FindBy(css = "[id$=selectItem23CC]")
    private WebElement selectItem23CCButton;

    @UseForAllTests(valuesFrom = ValuesFrom.FROM_ENUM, value = "")
    private TestedButtonsSet testedButtons;

    public WebElement getCollapseButton() {
        return testedButtons.equals(TestedButtonsSet.js) ? collapseAllButton : collapseAllCCButton;
    }

    public WebElement getExpandButton() {
        return testedButtons.equals(TestedButtonsSet.js) ? expandAllButton : expandAllCCButton;
    }

    public WebElement getSelectButton() {
        return testedButtons.equals(TestedButtonsSet.js) ? selectItem23Button : selectItem23CCButton;
    }

    @Test
    public void testCollapseAndExpand() {
        List<WebElement> allExpandedGroups = getPage().getPanelMenu().advanced().getAllExpandedGroups();
        assertEquals(allExpandedGroups.size(), 0);
        getMetamerPage().performJSClickOnButton(getExpandButton(), WaitRequestType.NONE);
        allExpandedGroups = getPage().getPanelMenu().advanced().getAllExpandedGroups();
        assertEquals(allExpandedGroups.size(), 6);
        getMetamerPage().performJSClickOnButton(getCollapseButton(), WaitRequestType.NONE);
        allExpandedGroups = getPage().getPanelMenu().advanced().getAllExpandedGroups();
        assertEquals(allExpandedGroups.size(), 0);
        getMetamerPage().performJSClickOnButton(getExpandButton(), WaitRequestType.NONE);
        allExpandedGroups = getPage().getPanelMenu().advanced().getAllExpandedGroups();
        assertEquals(allExpandedGroups.size(), 6);
    }

    @Test
    public void testSelect() {
        getMetamerPage().performJSClickOnButton(getSelectButton(), WaitRequestType.XHR);
        assertEquals(output.getText(), "item23");
    }

    private enum TestedButtonsSet {

        componentControl, js
    }
}
