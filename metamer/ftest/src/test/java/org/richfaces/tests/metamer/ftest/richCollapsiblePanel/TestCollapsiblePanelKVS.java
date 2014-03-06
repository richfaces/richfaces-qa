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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.support.url.URLUtils;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsiblePanel.TextualRichFacesCollapsiblePanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * Test keeping visual state for page
 * /faces/components/richCollapsiblePanel/simple.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsiblePanelKVS extends AbstractWebDriverTest {

    @FindBy(css = ".rf-cp[id$=collapsiblePanel]")
    private TextualRichFacesCollapsiblePanel panel;

    private final CollapsiblePanelReloadTester reloadTester = new CollapsiblePanelReloadTester();

    @Override
    public URL getTestUrl() {
        return URLUtils.buildUrl(contextPath, "faces/components/richCollapsiblePanel/simple.xhtml");
    }

    @Test
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richExtendedDataTable" })
    public void testRefreshFullPage() {
        reloadTester.testFullPageRefresh();
    }

    @Test
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richExtendedDataTable" })
    @RegressionTest("https://issues.jboss.org/browse/RF-12131")
    public void testRefreshFullPageInIterationComponents() {
        testRefreshFullPage();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12035")
    public void testRenderAll() {
        reloadTester.testRerenderAll();
    }

    private class CollapsiblePanelReloadTester extends ReloadTester<Boolean> {

        @Override
        public void doRequest(Boolean willBeCollapsed) {
            if (willBeCollapsed) {
                Graphene.guardAjax(panel).collapse();
            } else {
                Graphene.guardAjax(panel).expand();
            }
        }

        @Override
        public void verifyResponse(Boolean willBeCollapsed) {
            if (willBeCollapsed) {
                assertTrue(panel.advanced().isCollapsed(), "Panel should be collapsed");
            } else {
                assertFalse(panel.advanced().isCollapsed(), "Panel should be expanded");
            }
        }

        @Override
        public Boolean[] getInputValues() {
            return new Boolean[]{ Boolean.TRUE, Boolean.FALSE };
        }
    }
}
