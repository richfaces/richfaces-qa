/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 *
 * @author manovotn
 */
public class TestTabPanelAddTab3 extends AbstractWebDriverTest {

    private final Attributes<TabPanelAttributes> tabPanelAttributes = getAttributes();

    @Page
    private TabPanelSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/addTab3.xhtml");
    }

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

    @Test
    public void testAddWithA4j() {
        createAndVerifyTab(page.getCreateTabButtonA4j());
    }

    @Test
    public void testAddWithH() {
        createAndVerifyTab(page.getCreateTabButtonHButton());
    }
}
