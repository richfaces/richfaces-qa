/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsiblePanel.TextualRichFacesCollapsiblePanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richCollapsiblePanel/facets.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFacets extends AbstractWebDriverTest {

    @FindBy(css = ".rf-cp[id$=collapsiblePanel]")
    protected TextualRichFacesCollapsiblePanel panel;

    protected final Attributes<CollapsiblePanelAttributes> collapsiblePanelAttributes = getAttributes();

    private String switchType;

    protected String getExpectedHeaderAfterCollapsion() {
        return "header collapsed";
    }

    protected String getExpectedHeaderAfterExpansion() {
        return "header expanded";
    }

    private TextualRichFacesCollapsiblePanel getGuardedPanel() {
        if ("null".equals(switchType) || "ajax".equals(switchType)) {
            return Graphene.guardAjax(panel);
        } else if ("client".equals(switchType)) {
            return Graphene.guardNoRequest(panel);
        } else {
            return Graphene.guardHttp(panel);
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsiblePanel/facets.xhtml");
    }

    @Test
    @UseWithField(field = "switchType", valuesFrom = STRINGS, value = { "null", "ajax", "client", "server" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10368")
    public void testSwitchType() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.switchType, switchType);
        verifyStateAfterExpansion();
        getGuardedPanel().collapse();
        verifyStateAfterCollapsion();
        getGuardedPanel().expand();
        verifyStateAfterExpansion();
    }

    protected void verifyStateAfterCollapsion() {
        assertTrue(panel.advanced().isCollapsed(), "Panel should be collapsed.");
        assertEquals(panel.getHeaderText(), getExpectedHeaderAfterCollapsion(), "Header of the panel.");
    }

    protected void verifyStateAfterExpansion() {
        assertFalse(panel.advanced().isCollapsed(), "Panel should be expanded.");
        assertEquals(panel.getHeaderText(), getExpectedHeaderAfterExpansion(), "Header of the panel.");
        assertTrue(panel.getBodyText().startsWith("Lorem ipsum"), "Panel content doesn't start with 'Lorem ipsum'.");
    }

}
