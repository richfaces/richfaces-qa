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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.AssertJUnit.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
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
        for (int i = 0; i < 3; i++) {
            if (buttonToClick.getAttribute("name").contains("a4j")) {
                guardAjax(buttonToClick).click();
            } else {
                guardHttp(buttonToClick).click();
            }
            int latestTabNumber = 5 + i + 1;
            // verify number of tabs
            assertEquals(latestTabNumber, page.getTabPanel().getNumberOfTabs());
            // switch and assert active element has correct header
            guardAjax(page.getTabPanel()).switchTo(latestTabNumber - 1);
            assertEquals("tab" + latestTabNumber + " header", page.getTabPanel().advanced().getActiveHeaderElement().getText().trim());
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/addTab3.xhtml");
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
