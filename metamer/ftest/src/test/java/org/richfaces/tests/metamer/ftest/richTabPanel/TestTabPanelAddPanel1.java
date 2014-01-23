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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.AssertJUnit.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTabPanel/addTab2.xhtml
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestTabPanelAddPanel1 extends AbstractWebDriverTest {

    private final Attributes<TabPanelAttributes> tabPanelAttributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/addTab2.xhtml");
    }

    @FindBy(xpath = "//td[contains(@id, 'dynamic:') and contains(@id, 'tab:header:inactive')]/span[@class = 'rf-tab-lbl']/a")
    private List<WebElement> dynamicHeaders;

    @FindBy(xpath = "//div[contains(@id, 'tabPanel')]//*[@class='rf-tab-cnt']")
    private List<WebElement> activeContent;

    @Page
    private TabPanelSimplePage page;

    private WebElement getActiveContent() {
        for (WebElement e : activeContent) {
            if (e.isDisplayed()) {
                return e;
            }
        }
        return null;
    }

    private void createAndVerifyTab(WebElement buttonToClick) {
        // create 3 pages
        for (int i = 0; i < 3; i++) {
            if (buttonToClick.getAttribute("name").contains("a4j")) {
                guardAjax(buttonToClick).click();
            } else {
                guardHttp(buttonToClick).click();
            }
            assertEquals(i + 1, dynamicHeaders.size());
        }

    }

    /**
     * Create new tab by clicking on h:commandButton
     */
    @Test
    public void testCreateTabJSF() {
        createAndVerifyTab(page.getCreateTabButtonHButton());
    }

    /**
     * Create new tab by clicking on a4j:commandButton
     */
    @Test(groups = "smoke")
    public void testCreateTabAjax() {
        createAndVerifyTab(page.getCreateTabButtonA4j());
    }

    /**
     * Delete newly created tabs
     */
    @Test(groups = "smoke")
    public void testRemoveTab() {
        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 2; i >= 0; i--) {
            guardAjax(dynamicHeaders.get(i)).click();
            assertEquals(i, dynamicHeaders.size());
        }
    }

    /**
     * Verify that all tabs displays correct content when switch tab
     */
    @Test(groups = "smoke")
    public void testContentOfDynamicTab() {
        int basicTabCount = page.getPanelTab().getNumberOfTabs();

        createAndVerifyTab(page.getCreateTabButtonA4j());
        assertEquals(8, page.getPanelTab().getNumberOfTabs());

        for (int i = 0; i < 3; i++) {
            page.getPanelTab().switchTo(i + basicTabCount);
            assertEquals("Content of dynamicaly created tab" + (basicTabCount + i + 1), getActiveContent().getText());
        }
    }

    /**
     * Test plan: 1. click on 'create tab' btn 3 time and verify that new tabs appeared 2. verify that switch between newly
     * created tabs still works as in previous tabs (staticaly created) 3. verify a4j ajax btn to create new tabs
     */
    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11081", "https://issues.jboss.org/browse/RF-12945" })
    public void testSwitchTypeNull() {
        int basicTabCount = page.getPanelTab().getNumberOfTabs();
        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 0; i < 3; i++) {
            guardAjax(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(basicTabCount + i)).click();
            assertEquals("Content of dynamicaly created tab" + (basicTabCount + i + 1), getActiveContent().getText());
        }
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11081", "https://issues.jboss.org/browse/RF-12945" })
    public void testSwitchTypeAjax() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "ajax");
        page.fullPageRefresh();
        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "client");
        int basicTabCount = page.getPanelTab().getNumberOfTabs();
        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 0; i < 3; i++) {
            List<WebElement> elems = page.getPanelTab().advanced().getAllInactiveHeadersElements();
            WebElement elem = elems.get(basicTabCount + i);
            guardNoRequest(elem).click();
            assertEquals("Content of dynamicaly created tab" + (basicTabCount + i + 1), getActiveContent().getText());
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11054")
    public void testSwitchTypeServer() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "server");
        int basicTabCount = page.getPanelTab().getNumberOfTabs();
        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 0; i < 3; i++) {
            guardHttp(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(basicTabCount + i)).click();
            assertEquals("Content of dynamicaly created tab" + (basicTabCount + i + 1), getActiveContent().getText());
        }
    }
}
