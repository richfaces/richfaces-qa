/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2015, Red Hat, Inc., and individual contributors
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsiblePanel.TextualRichFacesCollapsiblePanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13913 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=collapse]")
    private WebElement collapseAll;
    @FindBy(css = "[id$=expand]")
    private WebElement expandAll;

    @FindBy(css = "[id$=panel1]")
    private TextualRichFacesCollapsiblePanel panel1;
    @FindBy(css = "[id$=panel2]")
    private TextualRichFacesCollapsiblePanel panel2;
    @FindBy(css = "[id$=panel3]")
    private TextualRichFacesCollapsiblePanel panel3;
    @FindBy(css = "[id$=panel4]")
    private TextualRichFacesCollapsiblePanel panel4;

    private void checkAllCollapsed() {
        int i = 0;
        for (TextualRichFacesCollapsiblePanel panel : getPanels()) {
            assertTrue(panel.advanced().isCollapsed(), MessageFormat.format("Panel #{0} should be collapsed.", ++i));
            assertNotVisible(panel.advanced().getBodyElement(), MessageFormat.format("Panel #{0} body should not be visible.", i));
        }
    }

    private void checkAllExpanded() {
        int i = 0;
        for (TextualRichFacesCollapsiblePanel panel : getPanels()) {
            assertFalse(panel.advanced().isCollapsed(), MessageFormat.format("Panel #{0} should be expanded.", ++i));
            assertEquals(panel.getBodyText(), MessageFormat.format("output {0}", i));
        }
    }

    private List<TextualRichFacesCollapsiblePanel> getPanels() {
        return Lists.newArrayList(panel1, panel2, panel3, panel4);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsiblePanel/rf-13913.xhtml");
    }

    @Test
    public void testCollapseAndExpandAllByButton() {
        checkAllExpanded();
        // collapse all
        Graphene.guardAjax(collapseAll).click();
        checkAllCollapsed();
        // expand all
        Graphene.guardAjax(expandAll).click();
        checkAllExpanded();

        // collapse one panel and then collapse all
        Graphene.guardAjax(panel2).collapse();
        assertFalse(panel1.advanced().isCollapsed(), "Panel #1 should be expanded.");
        assertTrue(panel2.advanced().isCollapsed(), "Panel #2 should be collapsed.");
        assertNotVisible(panel2.advanced().getBodyElement(), "Panel #2 body should not be visible.");
        assertFalse(panel3.advanced().isCollapsed(), "Panel #3 should be expanded.");
        assertFalse(panel4.advanced().isCollapsed(), "Panel #4 should be expanded.");
        // collapse all
        Graphene.guardAjax(collapseAll).click();
        checkAllCollapsed();

        // expand all
        Graphene.guardAjax(expandAll).click();
        checkAllExpanded();

        // collapse one panel and then expand all
        Graphene.guardAjax(panel3).collapse();
        assertFalse(panel1.advanced().isCollapsed(), "Panel #1 should be expanded.");
        assertFalse(panel2.advanced().isCollapsed(), "Panel #2 should be expanded.");
        assertTrue(panel3.advanced().isCollapsed(), "Panel #3 should be collapsed.");
        assertNotVisible(panel3.advanced().getBodyElement(), "Panel #3 body should not be visible.");
        assertFalse(panel4.advanced().isCollapsed(), "Panel #4 should be expanded.");
        Graphene.guardAjax(expandAll).click();
        checkAllExpanded();
    }
}
