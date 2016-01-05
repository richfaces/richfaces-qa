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
package org.richfaces.tests.metamer.ftest.richCollapsiblePanel;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCollapsiblePanelJSApi extends AbstractWebDriverTest {

    @Override
    public String getComponentTestPagePath() {
        return "richCollapsiblePanel/simple.xhtml";
    }

    @FindBy(css = "div[id$='collapsiblePanel:content']")
    private WebElement collapsiblePanelContent;

    @FindBy(css = "[id$=switch]")
    private WebElement switchPanelButton;

    @FindBy(css = "[id$=collapse]")
    private WebElement collapseButton;

    @FindBy(css = "[id$=expand]")
    private WebElement expandButton;

    @Test(groups = "smoke")
    @Templates(value = { "plain" })
    public void testSwitchPanel() {
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible!");

        Graphene.guardAjax(switchPanelButton).click();
        assertNotVisible(collapsiblePanelContent, "Content of collapsible panel should NOT be visible!");

        Graphene.guardAjax(switchPanelButton).click();
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible again!");
    }

    @Test
    @Templates(value = { "plain" })
    public void testCollapseExpand() {
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible!");

        Graphene.guardAjax(collapseButton).click();
        assertNotVisible(collapsiblePanelContent, "Content of collapsible panel should NOT be visible!");

        Graphene.guardAjax(expandButton).click();
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible again!");
    }

    @Test
    @Templates(value = { "plain" })
    public void testCollapseSwitchPanel() {
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible!");

        Graphene.guardAjax(collapseButton).click();
        assertNotVisible(collapsiblePanelContent, "Content of collapsible panel should NOT be visible!");

        Graphene.guardAjax(switchPanelButton).click();
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible again!");
    }

    @Test
    @Templates(value = { "plain" })
    public void testSwitchPanelExpand() {
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible!");

        Graphene.guardAjax(switchPanelButton).click();
        assertNotVisible(collapsiblePanelContent, "Content of collapsible panel should NOT be visible!");

        Graphene.guardAjax(expandButton).click();
        assertVisible(collapsiblePanelContent, "Content of collapsible panel should be visible again!");
    }

    @Test
    @Templates(value = { "plain" })
    public void testIsExpanded() {
        Boolean expanded = (Boolean) executeJS("return RichFaces.component('form:collapsiblePanel').isExpanded()");
        Assert.assertTrue(expanded, "value of isExpanded() when panel is expanded");

        Graphene.guardAjax(switchPanelButton).click();

        expanded = (Boolean) executeJS("return RichFaces.component('form:collapsiblePanel').isExpanded()");
        Assert.assertFalse(expanded, "value of isExpanded() when panel is collapsed");
    }
}
