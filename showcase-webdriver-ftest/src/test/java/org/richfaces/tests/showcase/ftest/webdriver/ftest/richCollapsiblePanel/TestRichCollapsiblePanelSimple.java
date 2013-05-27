/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
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
 */
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richCollapsiblePanel;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richCollapsiblePanel.SimplePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichCollapsiblePanelSimple extends AbstractWebDriverTest {

    @Page
    private SimplePage page;

    @Test
    public void testInit() {
        assertTrue(getPage().isAjaxModePanelCollapsed());
        assertTrue(getPage().isClientModePanelExpanded());
        assertTrue(getPage().isContentOfClientModePanelThere());
        assertFalse(getPage().isClientModePanelCollapsed());
        assertFalse(getPage().isAjaxModePanelExpanded());
        assertFalse(getPage().isContentOfAjaxModePanelThere());
    }

    @Test
    public void testCloseAndOpenClientModePanel() {
        boolean wasAjaxModePanelCollapsed = getPage().isAjaxModePanelCollapsed();
        assertTrue(getPage().isClientModePanelExpanded());

        getPage().closeClientModePanel();

        assertTrue(getPage().isClientModePanelCollapsed());
        assertFalse(getPage().isContentOfClientModePanelThere());
        //check if the state of second panel stays same as before
        assertTrue(wasAjaxModePanelCollapsed == getPage().isAjaxModePanelCollapsed());

        getPage().openClientModePanel();

        assertTrue(getPage().isClientModePanelExpanded());
        assertTrue(getPage().isContentOfClientModePanelThere());
        //check if the state of second panel stays same as before
        assertTrue(wasAjaxModePanelCollapsed == getPage().isAjaxModePanelCollapsed());
    }

    @Test
    public void testOpenAndCloseAjaxModePanel() {
        boolean wasClientModePanelCollapsed = getPage().isClientModePanelCollapsed();
        assertTrue(getPage().isAjaxModePanelCollapsed());

        getPage().openAjaxModePanel();

        assertTrue(getPage().isAjaxModePanelExpanded());
        assertTrue(getPage().isContentOfAjaxModePanelThere());
        //check if the state of second panel stays same as before
        assertTrue(wasClientModePanelCollapsed == getPage().isClientModePanelCollapsed());


        getPage().closeAjaxModePanel();

        assertTrue(getPage().isAjaxModePanelCollapsed());
        assertFalse(getPage().isContentOfAjaxModePanelThere());
        //check if the state of second panel stays same as before
        assertTrue(wasClientModePanelCollapsed == getPage().isClientModePanelCollapsed());
    }

    @Override
    protected SimplePage getPage() {
        return page;
    }
}
