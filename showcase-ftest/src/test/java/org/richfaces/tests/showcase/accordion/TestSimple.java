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
package org.richfaces.tests.showcase.accordion;

import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.showcase.accordion.page.SimplePage;
import org.richfaces.tests.showcase.panel.AbstractPanelTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimple extends AbstractPanelTest {

    @Page
    private SimplePage page;

    @Test
    public void testAccordionAndContent() {
        if (!page.firstPanelContent.isDisplayed()) {
            page.firstPanel.click();
        }
        checkContentOfPanel(page.firstPanelContent, RICH_FACES_INFO);
        page.secondPanel.click();
        assertFalse(page.firstPanelContent.isDisplayed(), "The body of the first panel should not be visible, since the panel is hidden!");
        checkContentOfPanel(page.secondPanelContent, RICH_FACES_JSF_INFO);
        page.firstPanel.click();
        assertFalse(page.secondPanelContent.isDisplayed(), "The body of the second panel should not be visible, since the panel is hidden!");

    }

}
