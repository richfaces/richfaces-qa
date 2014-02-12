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
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestTabPanelJSApi extends AbstractWebDriverTest {

    private final Attributes<TabPanelAttributes> tabPanelAttributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/addTab2.xhtml");
    }

    @Page
    private TabPanelSimplePage page;

    @FindByJQuery("input[id*=switchButton]")
    private List<WebElement> switchToButtons;

    private String JsPrevItem() {
        return (String) executeJS("return RichFaces.component('" + page.getPanelTabAsWebElement().getAttribute("id")
            + "').prevItem()");
    }

    private String JsNextItem() {
        return (String) executeJS("return RichFaces.component('" + page.getPanelTabAsWebElement().getAttribute("id")
            + "').nextItem()");
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsSwitchTo() {
        // assert initial settings - 5 tabs, so 5 elements in list should be present
        assertEquals(switchToButtons.size(), page.getPanelTab().getNumberOfTabs());

        // switch to certain panel and assert it's content is visible
        guardAjax(switchToButtons.get(4)).click();
        assertVisible(page.getPanelTab().advanced().getAllTabContentsElements().get(4), "Content of the last tab should be visible");

        guardAjax(switchToButtons.get(0)).click();
        assertVisible(page.getPanelTab().advanced().getAllTabContentsElements().get(0), "Content of the first tab should be visible");

        guardAjax(switchToButtons.get(2)).click();
        assertVisible(page.getPanelTab().advanced().getAllTabContentsElements().get(2), "Content of the third tab should be visible");

        guardAjax(switchToButtons.get(1)).click();
        assertVisible(page.getPanelTab().advanced().getAllTabContentsElements().get(1), "Content of the second tab should be visible");

        guardNoRequest(switchToButtons.get(3)).click();
        assertNotVisible(page.getPanelTab().advanced().getAllTabContentsElements().get(3),
            "Content of the fourth tab should not be visible");
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsGetItems() {
        // Using JS API get IDs and isSelected attributes and assert
        for (int i = 0; i < 5; i++) {
            String id = (String) executeJS("return RichFaces.component('" + page.getPanelTabAsWebElement().getAttribute("id")
                + "').getItems()['" + i + "'].id");
            Boolean selected = (Boolean) executeJS("return RichFaces.component('"
                + page.getPanelTabAsWebElement().getAttribute("id") + "').getItems()['" + i + "'].isSelected()");

            assertTrue(page.getPanelTab().advanced().getAllActiveHeadersElements().get(i).getAttribute("id").contains(id));
            if (i == 0) {
                // first tab is selected
                assertTrue(selected);
            } else {
                assertFalse(selected);
            }
        }
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsGetItemsNames() {
        @SuppressWarnings("unchecked")
        List<String> result = (ArrayList<String>) executeJS("return RichFaces.component('"
            + page.getPanelTabAsWebElement().getAttribute("id") + "').getItemsNames()");
        assertTrue(result.size() == 5);
        assertEquals("tab1, tab2, tab3, tab4, tab5", result.toString().substring(1, result.toString().length() - 1));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsFirstItem() {
        String result = (String) executeJS("return RichFaces.component('" + page.getPanelTabAsWebElement().getAttribute("id")
            + "').firstItem()");
        assertEquals("tab1", result);
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsPrevItem() {
        // switch to first, use JS function and assert its null
        page.getPanelTab().switchTo(0);
        String result = JsPrevItem();
        assertNull(result);

        // move to second tab and assert tab1 is previous
        page.getPanelTab().switchTo(1);
        result = JsPrevItem();
        assertEquals("tab1", result);

        // turn on cycled switching
        tabPanelAttributes.set(TabPanelAttributes.cycledSwitching, true);

        // assert with cycled switching
        page.getPanelTab().switchTo(0);
        result = JsPrevItem();
        assertEquals("tab5", result);

        page.getPanelTab().switchTo(2);
        result = JsPrevItem();
        assertEquals("tab2", result);
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsNextItem() {
        page.getPanelTab().switchTo(0);
        String result = JsNextItem();
        assertEquals("tab2", result);

        page.getPanelTab().switchTo(4);
        result = JsNextItem();
        assertNull(result);

        // turn on cycled switching
        tabPanelAttributes.set(TabPanelAttributes.cycledSwitching, true);

        result = JsNextItem();
        assertEquals("tab1", result);

        page.getPanelTab().switchTo(1);
        result = JsNextItem();
        assertEquals("tab3", result);
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsLastItem() {
        String result = (String) executeJS("return RichFaces.component('" + page.getPanelTabAsWebElement().getAttribute("id")
            + "').lastItem()");
        assertEquals("tab5", result);
    }
}
