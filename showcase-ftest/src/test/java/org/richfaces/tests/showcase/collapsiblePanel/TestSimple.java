/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.collapsiblePanel;

import org.jboss.arquillian.graphene.Graphene;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.richfaces.tests.showcase.panel.AbstractPanelTest;
import org.testng.annotations.Test;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.showcase.collapsiblePanel.page.SimplePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimple extends AbstractPanelTest {

    @Page
    private SimplePage page;

    @Test
    public void testCollapseAndShowPanels() {
        if (!page.firstPanelContent.isDisplayed()) {
            page.firstPanel.click();
        }
        checkContentOfPanel(page.firstPanelContent, RICH_FACES_INFO);
        page.firstPanel.click();
        page.secondPanel.click();
        assertFalse(page.firstPanelContent.isDisplayed(), "The content of the first panel should not be visible, since the panel is collapsed!");

        Graphene.waitAjax(webDriver).until()
                .element(page.secondPanel)
                .is()
                .visible();
        checkContentOfPanel(page.secondPanelContent, RICH_FACES_JSF_INFO);
        page.secondPanel.click();
        page.firstPanel.click();
        /* This is workaround because page.secondPanelContent is not reachable at this time, there is only element with class rf-cp-b*/
        int size = webDriver.findElements(ByJQuery.selector("div[class='rf-cp-b']")).size();
        assertTrue(size == 1, "The content of the second panel should not be visible, since the panel is collapsed!");
    }
}
