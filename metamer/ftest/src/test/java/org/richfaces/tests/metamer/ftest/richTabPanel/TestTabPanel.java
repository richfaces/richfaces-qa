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
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTabPanel/simple.xhtml
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestTabPanel extends AbstractWebDriverTest {

    private final Attributes<TabPanelAttributes> tabPanelAttributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/simple.xhtml");
    }

    @Page
    private TabPanelSimplePage page;

    @FindBy(xpath = "//input[contains(@id, 'switchButton1')]")
    private WebElement moveTo;

    @Test
    @Templates(exclude = { "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable" })
    public void testHeaderAlignment() {
        // assert initial setting, header should be on the left

        assertPresent(driver.findElement(By.xpath("//td[contains(@style, 'padding-right: 5px; width: 100%')]")),
            "Header is not on the left!");

        // move header to right and assert
        tabPanelAttributes.set(TabPanelAttributes.headerAlignment, "right");
        assertPresent(driver.findElement(By.xpath("//td[contains(@style, 'padding-left: 5px; width:100%')]")),
            "Header is not on the right!");

        // move the header back to left and assert
        tabPanelAttributes.set(TabPanelAttributes.headerAlignment, "left");
        assertPresent(driver.findElement(By.xpath("//td[contains(@style, 'padding-right: 5px; width: 100%')]")),
            "Header is not on the left!");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11550")
    @Templates(value = { "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable" })
    public void testHeaderAlignmentIterationComponents() {
        testHeaderAlignment();
    }

    @Test
    public void testHeaderPosition() {
        // assert initial settings
        assertPresent(driver.findElement(By.xpath("//div[@class = 'rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-top']")),
            "Header should be in the top!");

        // move to the bottom
        tabPanelAttributes.set(TabPanelAttributes.headerPosition, "bottom");
        assertPresent(driver.findElement(By.xpath("//div[@class = 'rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-btm']")),
            "Header should be in the top!");

        // move back to top and assert
        tabPanelAttributes.set(TabPanelAttributes.headerPosition, "top");
        assertPresent(driver.findElement(By.xpath("//div[@class = 'rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-top']")),
            "Header should be in the top!");
    }

    @Test
    public void testInit() {
        // assert panel tab visibility
        assertVisible(page.getPanelTabAsWebElement(), "Panel tab is not visible!");

        // assert there are 5 ACTIVE headers out of which only first is visible
        List<WebElement> allActiveHeaders = page.getPanelTab().advanced().getAllActiveHeadersElements();
        assertEquals(allActiveHeaders.size(), 5);
        assertVisible(allActiveHeaders.get(0), "First tab should be visible");

        // assert there are 5 INACTIVE headers, 3 visible
        List<WebElement> allInactiveHeaders = page.getPanelTab().advanced().getAllInactiveHeadersElements();
        assertEquals(allInactiveHeaders.size(), 5);
        assertVisible(allInactiveHeaders.get(1), "Second inactive tab should be visible!");
        assertVisible(allInactiveHeaders.get(2), "Third inactive tab should be visible!");
        assertVisible(allInactiveHeaders.get(4), "Fifth inactive tab should be visible!");

        // assert there are 5 DISABLED headers, 1 visible
        List<WebElement> allDisabledHeaders = page.getPanelTab().advanced().getAllDisabledHeadersElements();
        assertEquals(allDisabledHeaders.size(), 5);
        assertVisible(allDisabledHeaders.get(3), "Fourth disabled tab should be visible!");

        // assert 5 visible WebElements in total
        List<WebElement> allHeaders = new ArrayList<WebElement>();
        allHeaders.addAll(allInactiveHeaders);
        allHeaders.addAll(allActiveHeaders);
        allHeaders.addAll(allDisabledHeaders);
        int visible = 0;
        for (WebElement elem : allHeaders) {
            if (new WebElementConditionFactory(elem).isVisible().apply(driver)) {
                visible++;
            }
        }
        assertEquals(visible, 5);

        // assert only content of first tab is displayed
        List<WebElement> tabContents = page.getPanelTab().advanced().getAllTabContentsElements();
        for (int i = 1; i < 5; i++) {
            assertNotVisible(tabContents.get(i), "Contents of tab number " + (i + 1) + " should not be visible!");
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10351")
    public void testActiveItem() {
        // assert tab panel is visible
        assertVisible(page.getPanelTabAsWebElement(), "Panel Tab is not visible!");

        // switch tabs and check which are visible
        tabPanelAttributes.set(TabPanelAttributes.activeItem, "tab5");
        List<WebElement> allActiveHeaders = page.getPanelTab().advanced().getAllActiveHeadersElements();
        for (int i = 0; i < 4; i++) {
            assertNotVisible(allActiveHeaders.get(i), "Header on position " + (i + 1) + " should not be visible!");
        }
        assertVisible(allActiveHeaders.get(4), "Fourth Header should be visible!");

        // try switching to disabled tab
        tabPanelAttributes.set(TabPanelAttributes.activeItem, "tab4");
        for (int i = 1; i < 5; i++) {
            assertNotVisible(allActiveHeaders.get(i), "Header of tab number " + (i + 1) + " should not be visible!");
        }

        List<WebElement> tabContents = page.getPanelTab().advanced().getAllTabContentsElements();
        for (int i = 1; i < 5; i++) {
            assertNotVisible(tabContents.get(i), "Contents of tab number " + (i + 1) + " should not be visible!");
        }
    }

    @Test
    public void testCycledSwitching() {
        String panelId = (String) executeJS("return testedComponentId");
        String result = "someString";
        result = (String) executeJS("return RichFaces.component('" + panelId + "').nextItem('tab5')");

        assertEquals(result, null);

        result = (String) executeJS("return RichFaces.component('" + panelId + "').prevItem('tab1')");
        assertEquals(result, null);

        // turn cycled switching on
        tabPanelAttributes.set(TabPanelAttributes.cycledSwitching, Boolean.TRUE);
        result = (String) executeJS("return RichFaces.component('" + panelId + "').nextItem('tab5')");
        assertEquals(result, "tab1");

        result = (String) executeJS("return RichFaces.component('" + panelId + "').prevItem('tab1')");
        assertEquals(result, "tab5");
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        super.testDir(page.getPanelTabAsWebElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        tabPanelAttributes.set(TabPanelAttributes.immediate, Boolean.TRUE);
        WebElement thirdPanel;
        thirdPanel = page.getPanelTab().advanced().getAllInactiveHeadersElements().get(2);
        thirdPanel.click();
        waitModel().until().element(page.getPanelTab().advanced().getAllActiveHeadersElements().get(2)).is().visible();
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: tab1 -> tab3");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10523")
    public void testItemChangeListener() {
        page.getPanelTab().advanced().getAllInactiveHeadersElements().get(2).click();
        waitGui().until().element(page.getPanelTab().advanced().getAllActiveHeadersElements().get(2)).is().visible();
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: tab1 -> tab3");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        String testString = "someLanguage";
        tabPanelAttributes.set(TabPanelAttributes.lang, testString);
        assertEquals(page.getPanelTabAsWebElement().getAttribute("lang"), testString);
    }

    @Test
    public void testOnbeforeitemchange() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onbeforeitemchange, new Action() {
            @Override
            public void perform() {
                new Actions(driver).click(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(2)).build().perform();
            }
        });
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10165")
    public void testItemchangeEvents() {
        tabPanelAttributes.set(TabPanelAttributes.onbeforeitemchange, "metamerEvents += \"beforeitemchange \"");
        tabPanelAttributes.set(TabPanelAttributes.onitemchange, "metamerEvents += \"itemchange \"");
        executeJS("window.metamerEvents = \"\";");
        WebElement thirdPanel;
        thirdPanel = page.getPanelTab().advanced().getAllInactiveHeadersElements().get(2);
        thirdPanel.click();
        waitModel().until().element(page.getPanelTab().advanced().getAllActiveHeadersElements().get(2)).is().visible();

        Object obj = executeJS("return window.metamerEvents");
        String[] events = ((String) obj).split(" ");

        assertEquals(events.length, 2, "Two events should be fired - beforeitemchange and itemchange.");
        assertEquals(events[0], "beforeitemchange", "Attribute onbeforeitemchange doesn't work");
        assertEquals(events[1], "itemchange", "Attribute onitemchange doesn't work");
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onclick, new Action() {
            @Override
            public void perform() {
                new Actions(driver).click(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(2)).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                new Actions(driver).doubleClick(page.getPanelTabAsWebElement()).build().perform();
            }
        });
    }

    @Test
    public void testOnitemchange() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onitemchange, new Action() {
            @Override
            public void perform() {
                new Actions(driver).click(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(2)).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                new Actions(driver).clickAndHold(page.getPanelTabAsWebElement()).release().build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmousemove, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getPanelTabAsWebElement()).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getPanelTabAsWebElement()).moveToElement(moveTo).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmouseover, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getPanelTabAsWebElement()).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(tabPanelAttributes, TabPanelAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(moveTo).clickAndHold().moveToElement(page.getPanelTabAsWebElement())
                    .release().build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        tabPanelAttributes.set(TabPanelAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.getPanelTabAsWebElement(), "TabPanel should not be rendered!");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getPanelTabAsWebElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getPanelTabAsWebElement());
    }

    @Test
    public void testSwitchTypeNull() {
        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardAjax(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(index)).click();
            waitAjax().until().element(page.getPanelTab().advanced().getAllActiveHeadersElements().get(index)).is().visible();
        }
    }

    @Test
    @Templates(value = "plain")
    public void testSwitchTypeAjax() {
        // since ajax is default choice, null type test will test this too
        tabPanelAttributes.set(TabPanelAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "client");

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardNoRequest(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(index)).click();
            waitGui().until().element(page.getPanelTab().advanced().getAllActiveHeadersElements().get(index)).is().visible();
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchTypeServer() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "server");

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardHttp(page.getPanelTab().advanced().getAllInactiveHeadersElements().get(index)).click();
            waitGui().until().element(page.getPanelTab().advanced().getAllActiveHeadersElements().get(index)).is().visible();
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    @Templates(value = "plain")
    public void testTabActiveHeaderClass() {
        String headerClass = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabActiveHeaderClass, headerClass);

        for (WebElement elem : page.getPanelTab().advanced().getAllActiveHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(headerClass), "tabActiveHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllDisabledHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(headerClass), "tabActiveHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllInactiveHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(headerClass), "tabActiveHeaderClass does not work");
        }
    }

    @Test
    @Templates(value = "plain")
    public void testTabContentClass() {
        String contentClass = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabContentClass, contentClass);
        assertTrue(driver.findElement(By.xpath("//div[contains(text(), 'content of tab 1')] ")).getAttribute("class")
            .contains(contentClass));
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    @Templates(value = "plain")
    public void testTabDisabledHeaderClass() {
        String headerClass = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabDisabledHeaderClass, headerClass);

        for (WebElement elem : page.getPanelTab().advanced().getAllActiveHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(headerClass), "tabDisabledHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllDisabledHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(headerClass), "tabDisabledHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllInactiveHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(headerClass), "tabDisabledHeaderClass does not work");
        }
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9309", "https://issues.jboss.org/browse/RF-11549" })
    @Templates(value = "plain")
    public void testTabHeaderClass() {
        String testString = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabHeaderClass, testString);

        for (WebElement elem : page.getPanelTab().advanced().getAllActiveHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(testString), "tabHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllDisabledHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(testString), "tabHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllInactiveHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(testString), "tabHeaderClass does not work");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    @Templates(value = "plain")
    public void testTabInactiveHeaderClass() {
        String testString = "metamer-ftest-class";
        tabPanelAttributes.set(TabPanelAttributes.tabInactiveHeaderClass, testString);

        for (WebElement elem : page.getPanelTab().advanced().getAllActiveHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(testString), "tabInactiveHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllDisabledHeadersElements()) {
            assertFalse(elem.getAttribute("class").contains(testString), "tabInactiveHeaderClass does not work");
        }

        for (WebElement elem : page.getPanelTab().advanced().getAllInactiveHeadersElements()) {
            assertTrue(elem.getAttribute("class").contains(testString), "tabInactiveHeaderClass does not work");
        }
    }

    @Test
    public void testTitle() {
        testTitle(page.getPanelTabAsWebElement());
    }
}
