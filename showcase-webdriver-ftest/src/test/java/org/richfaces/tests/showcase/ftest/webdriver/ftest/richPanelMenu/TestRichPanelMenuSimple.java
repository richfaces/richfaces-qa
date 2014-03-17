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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richPanelMenu;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richPanelMenu.PanelMenuPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichPanelMenuSimple extends AbstractWebDriverTest {

    @Page
    private PanelMenuPage page;

    @Test(groups = {"RF-12146"})
    public void testExpandFirst() {
        getPage().getPanelMenu().toggleFirstLevelGroup(0);
    }

    @Test(groups = {"RF-12146"})
    public void testExpandAndCollapseFirst() {
        getPage().getPanelMenu().toggleFirstLevelGroup(0);
        getPage().getPanelMenu().toggleFirstLevelGroup(0);
    }

    @Test
    public void testInit() {
        int numOfGroups = getPage().getPanelMenu().getNumberOfFirstLevelGroups();
        assertEquals(numOfGroups, 3);
        for (int i = 0; i < numOfGroups; i++) {
            assertFalse(getPage().getPanelMenu().isFirstLevelGroupExpanded(i));
        }
    }

    @Test(groups = {"RF-12146"})
    public void testSelectFirstItemFromFristGroup() {
        getPage().getPanelMenu().toggleFirstLevelGroup(0);
        getPage().getPanelMenu().selectSecondLevelItem(0, 0);
        Graphene.waitAjax().withMessage("The current selection doesn't match.").until(
            new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver arg0) {
                    return getPage().getCurrentSelection().equals("Item_1_1 selected");
                }
            });
    }

    @Override
    protected PanelMenuPage getPage() {
        return page;
    }

}
