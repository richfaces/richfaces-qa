/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static java.text.MessageFormat.format;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.testng.AssertJUnit.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.SkipOnResultsCache;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestTabPanelAddTab3 extends AbstractWebDriverTest {

    @Page
    private TabPanelSimplePage page;

    private void createAndVerifyTab(WebElement buttonToClick) {
        // create 3 pages
        int latestTabNumber = 6;
        for (int i = 0; i < 3; i++, latestTabNumber++) {
            if (buttonToClick.getAttribute("name").contains("a4j")) {
                guardAjax(buttonToClick).click();
            } else {
                guardHttp(buttonToClick).click();
            }
            // verify number of tabs
            assertEquals(latestTabNumber, page.getTabPanel().getNumberOfTabs());
            // switch and assert active element has correct header
            String createdTabHeaderText = format("tab{0} header", latestTabNumber);
            if (SkipOnResultsCache.getInstance().getResultFor(On.JSF.MyFaces.class)) {
                // in MyFaces, the tab is always added before the already defined tabs
                guardAjax(page.getTabPanel()).switchTo(createdTabHeaderText);
            } else {
                // in Mojarra, the tab is added after the already defined tabs
                guardAjax(page.getTabPanel()).switchTo(latestTabNumber - 1);
            }
            assertEquals(createdTabHeaderText, page.getTabPanel().advanced().getActiveHeaderElement().getText().trim());
        }
    }

    @Override
    public String getComponentTestPagePath() {
        return "richTabPanel/addTab3.xhtml";
    }

    @Test
    @Templates(exclude = { "extendedDataTable", "collapsibleSubTable", "dataGrid", "a4jRepeat", "uiRepeat" })
    public void testAddWithA4j() {
        createAndVerifyTab(page.getCreateTabButtonA4j());
    }

    @Test
    @Skip
    @Templates(value = { "extendedDataTable", "collapsibleSubTable", "dataGrid", "a4jRepeat", "uiRepeat" })
    @IssueTracking("https://issues.jboss.org/browse/RF-14006")
    public void testAddWithA4j_RF14006() {
        testAddWithA4j();
    }

    @Test
    @Templates(exclude = { "extendedDataTable", "collapsibleSubTable", "dataGrid", "a4jRepeat", "uiRepeat" })
    public void testAddWithH() {
        createAndVerifyTab(page.getCreateTabButtonHButton());
    }

    @Test
    @Skip
    @Templates(value = { "extendedDataTable", "collapsibleSubTable", "dataGrid", "a4jRepeat", "uiRepeat" })
    @IssueTracking("https://issues.jboss.org/browse/RF-14006")
    public void testAddWithH_RF14006() {
        testAddWithH();
    }
}
