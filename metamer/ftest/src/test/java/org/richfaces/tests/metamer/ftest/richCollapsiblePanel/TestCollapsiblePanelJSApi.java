/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
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
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richCollapsiblePanel;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

public class TestCollapsiblePanelJSApi extends AbstractWebDriverTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsiblePanel/simple.xhtml");
    }

    @FindByJQuery(value = "div[id$='collapsiblePanel:content']")
    private WebElement content;

    @FindByJQuery(value = "div[id$='collapsiblePanel']")
    private WebElement panel;

    @Test
    @Templates(value = {"plain"})
    public void testSwitchPanel() {
        String JScode = "RichFaces.$('" + panel.getAttribute("id") + "').switchPanel()";
        // assert content is visible after page is loaded
        assertVisible(content, "Content of collapsible panel should be visible!");

        executeJS(JScode);

        // wait until content is NOT visible
        waitGui(driver).withMessage("Content of collapsible panel should NOT be visible!").until().element(content).is().not()
            .visible();
        // assertNotVisible(content, "Content of collapsible panel should NOT be visible!");

        executeJS(JScode);

        // wait until content is visible again
        waitGui(driver).withMessage("Content of collapsible panel should be visible!").until().element(content).is().visible();
        // assertVisible(content, "Content of collapsible panel should be visible again!");
    }
}
