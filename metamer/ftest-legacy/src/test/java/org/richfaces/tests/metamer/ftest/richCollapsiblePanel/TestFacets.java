/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richCollapsiblePanel;

import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.collapsiblePanelAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richCollapsiblePanel/facets.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21412 $
 */
public class TestFacets extends AbstractGrapheneTest {

    private JQueryLocator panel = pjq("div[id$=collapsiblePanel]");
    private JQueryLocator header = pjq("div[id$=collapsiblePanel:header]");
    private JQueryLocator headerExp = pjq("div[id$=collapsiblePanel:header] div.rf-cp-lbl-exp");
    private JQueryLocator headerColps = pjq("div[id$=collapsiblePanel:header] div.rf-cp-lbl-colps");
    private JQueryLocator content = pjq("div[id$=collapsiblePanel:content]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsiblePanel/facets.xhtml");
    }

    @Test
    public void testInit() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Collapsible panel is not present on the page.");

        verifyBeforeClick();
    }

    @Test
    public void testSwitchTypeNull() {
        // click to collapse
        String timeValue = selenium.getText(time);
        guardXhr(selenium).click(header);
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));

        verifyAfterClick();

        // click to expand
        timeValue = selenium.getText(time);
        guardXhr(selenium).click(header);
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));

        verifyBeforeClick();
    }

    @Test
    public void testSwitchTypeAjax() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.switchType, "ajax");

        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.switchType, "client");

        // click to collapse
        guardNoRequest(selenium).click(header);
        verifyAfterClick();

        // click to expand
        guardNoRequest(selenium).click(header);
        verifyBeforeClick();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10368")
    public void testSwitchTypeServer() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.switchType, "server");

        // click to collapse
        guardHttp(selenium).click(header);
        verifyAfterClick();

        // click to expand
        guardHttp(selenium).click(header);
        verifyBeforeClick();
    }

    private void verifyBeforeClick() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Collapsible panel is not present on the page.");

        displayed = selenium.isVisible(headerExp);
        assertTrue(displayed, "Expanded header should be visible.");

        displayed = selenium.isVisible(headerColps);
        assertFalse(displayed, "Collapsed header should not be visible.");

        displayed = selenium.isVisible(content);
        assertTrue(displayed, "Panel's content should be visible.");

        String text = selenium.getText(headerExp);
        assertEquals(text, "header expanded", "Header of the panel.");

        text = selenium.getText(content);
        assertTrue(text.startsWith("Lorem ipsum"), "Panel doesn't contain Lorem ipsum in its content.");
    }

    private void verifyAfterClick() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Collapsible panel is not present on the page.");

        displayed = selenium.isVisible(headerExp);
        assertFalse(displayed, "Expanded header should not be visible.");

        displayed = selenium.isVisible(headerColps);
        assertTrue(displayed, "Collapsed header should be visible.");

        if (selenium.isElementPresent(content)) {
            displayed = selenium.isVisible(content);
            assertFalse(displayed, "Panel's content should not be visible.");
        }

        String text = selenium.getText(headerColps);
        assertEquals(text, "header collapsed", "Header of the panel.");
    }
}
