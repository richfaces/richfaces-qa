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
package org.richfaces.tests.metamer.ftest.richTab;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.tabAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTab/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 * @version $Revision: 22963 $
 */
public class TestRichTab extends AbstractWebDriverTest {

    @Page
    TabSimplePage page;

    @FindByJQuery(value = "div[id$=phasesPanel] span")
    private WebElement requestTime;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTab/simple.xhtml");
    }

    private boolean belongsClass(WebElement elem, String className) {
        return elem.getAttribute("class").contains(className);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11427")
    public void testAction() {
        String timeWhenStarted = requestTime.getText();
        guardAjax(page.getInactiveHeaders().get(2)).click();
        // assert request time has changed
        assertFalse(timeWhenStarted.equals(requestTime.getText()));
        // there should be 6 elements in phases list - meaning no action and no action listener
        assertEquals(driver.findElements(ByJQuery.selector("div[id$=phasesPanel] ul li")).size(), 6);

        timeWhenStarted = requestTime.getText();
        // switch to first tab - this SHOULD evoke action and action listener
        guardAjax(page.getInactiveHeaders().get(0)).click();
        // assert request time has changed
        assertFalse(timeWhenStarted.equals(requestTime.getText()));
        // there should be 8 elements in phases list - meaning action and action listener should be present
        assertEquals(driver.findElements(ByJQuery.selector("div[id$=phasesPanel] ul li")).size(), 8);
        assertEquals(driver.findElement(ByJQuery.selector("div[id$=phasesPanel] ul li:nth-child(6)")).getText(),
            "* action listener invoked");
        assertEquals(driver.findElement(ByJQuery.selector("div[id$=phasesPanel] ul li:nth-child(7)")).getText(),
            "* action invoked");
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11427")
    public void testActionListener() {
        // test is similar to testAction() because both, action and action listener should be invoked in the same time
        testAction();
    }

    @Test
    public void testInit() {
        assertVisible(page.getTab().advanced().getRootElement(), "Tab panel is not present on the page.");

        assertVisible(page.getActiveHeaders().get(0), "Header of tab1 should be active.");
        assertNotVisible(page.getInactiveHeaders().get(0), "Header of tab1 should not be inactive.");
        assertVisible(page.getDisabledHeaders().get(3), "Header of tab4 should be disabled.");

        String text = page.getActiveHeaders().get(0).getText();
        assertEquals(text, "tab1 header");
        text = page.getInactiveHeaders().get(1).getText();
        assertEquals(text, "tab2 header");
        text = page.getDisabledHeaders().get(3).getText();
        assertEquals(text, "tab4 header");
        text = page.getItemContents().get(0).getText();
        assertEquals(text, "content of tab 1");
    }

    @Test
    @Templates(value = "plain")
    public void testContentClass() {
        tabAttributes.set(TabAttributes.contentClass, "metamer-ftest-class");
        assertTrue(belongsClass(page.getItemContents().get(0), "metamer-ftest-class"));
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        testDir(page.getFirstTabContentParentElement());
    }

    @Test
    @Templates(value = "plain")
    public void testDisabled() {
        // disable the first tab
        tabAttributes.set(TabAttributes.disabled, Boolean.TRUE);

        assertNotVisible(page.getActiveHeaders().get(0), "Header of tab1 should not be active.");
        assertNotVisible(page.getInactiveHeaders().get(0), "Header of tab1 should not be inactive.");
        assertVisible(page.getDisabledHeaders().get(0), "Header of tab1 should be disabled.");

        String text = page.getDisabledHeaders().get(0).getText();
        assertEquals(text, "tab1 header");

        // enable the first tab
        tabAttributes.set(TabAttributes.disabled, Boolean.FALSE);

        assertNotVisible(page.getActiveHeaders().get(0), "Header of tab1 should not be active.");
        assertVisible(page.getInactiveHeaders().get(0), "Header of tab1 should be inactive.");
        assertNotVisible(page.getDisabledHeaders().get(0), "Header of tab1 should not be disabled.");

        text = page.getInactiveHeaders().get(0).getText();
        assertEquals(text, "tab1 header");
    }

    @Test
    @Templates(value = "plain")
    public void testHeader() {
        tabAttributes.set(TabAttributes.header, "new header");
        assertEquals(page.getActiveHeaders().get(0).getText(), "new header", "Header of the first tab did not change.");

        tabAttributes.set(TabAttributes.header, "ľščťťžžôúňď ацущьмщфзщйцу");
        assertEquals(page.getActiveHeaders().get(0).getText(), "ľščťťžžôúňď ацущьмщфзщйцу",
            "Header of the first tab did not change.");
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderActiveClass() {
        tabAttributes.set(TabAttributes.headerActiveClass, "metamer-ftest-class");

        assertTrue(belongsClass(page.getActiveHeaders().get(0), "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(0), "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(belongsClass(page.getDisabledHeaders().get(0), "metamer-ftest-class"), "headerActiveClass does not work");

        assertFalse(belongsClass(page.getActiveHeaders().get(1), "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(1), "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(belongsClass(page.getDisabledHeaders().get(1), "metamer-ftest-class"), "headerActiveClass does not work");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11549")
    public void testHeaderClass() {
        tabAttributes.set(TabAttributes.headerClass, "metamer-ftest-class");

        assertTrue(belongsClass(page.getActiveHeaders().get(0), "metamer-ftest-class"), "tabHeaderClass does not work");
        assertTrue(belongsClass(page.getInactiveHeaders().get(0), "metamer-ftest-class"), "tabHeaderClass does not work");
        assertTrue(belongsClass(page.getDisabledHeaders().get(0), "metamer-ftest-class"), "tabHeaderClass does not work");

        assertFalse(belongsClass(page.getActiveHeaders().get(1), "metamer-ftest-class"), "tabHeaderClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(1), "metamer-ftest-class"), "tabHeaderClass does not work");
        assertFalse(belongsClass(page.getDisabledHeaders().get(1), "metamer-ftest-class"), "tabHeaderClass does not work");
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderDisabledClass() {
        tabAttributes.set(TabAttributes.headerDisabledClass, "metamer-ftest-class");

        assertFalse(belongsClass(page.getActiveHeaders().get(0), "metamer-ftest-class"), "headerDisabledClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(0), "metamer-ftest-class"), "headerDisabledClass does not work");
        assertTrue(belongsClass(page.getDisabledHeaders().get(0), "metamer-ftest-class"), "headerDisabledClass does not work");

        assertFalse(belongsClass(page.getActiveHeaders().get(1), "metamer-ftest-class"), "headerDisabledClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(1), "metamer-ftest-class"), "headerDisabledClass does not work");
        assertFalse(belongsClass(page.getDisabledHeaders().get(1), "metamer-ftest-class"), "headerDisabledClass does not work");
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderInactiveClass() {
        tabAttributes.set(TabAttributes.headerInactiveClass, "metamer-ftest-class");

        assertFalse(belongsClass(page.getActiveHeaders().get(0), "metamer-ftest-class"), "headerInactiveClass does not work");
        assertTrue(belongsClass(page.getInactiveHeaders().get(0), "metamer-ftest-class"), "headerInactiveClass does not work");
        assertFalse(belongsClass(page.getDisabledHeaders().get(0), "metamer-ftest-class"), "headerInactiveClass does not work");

        assertFalse(belongsClass(page.getActiveHeaders().get(1), "metamer-ftest-class"), "headerInactiveClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(1), "metamer-ftest-class"), "headerInactiveClass does not work");
        assertFalse(belongsClass(page.getDisabledHeaders().get(1), "metamer-ftest-class"), "headerInactiveClass does not work");
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderStyle() {
        final String value = "background-color: yellow; font-size: 1.5em;";
        tabAttributes.set(TabAttributes.headerStyle, value);
        String styleAttr = page.getActiveHeaders().get(0).getAttribute("style");
        assertTrue(styleAttr.contains(value), "Attribute style should contain \"" + value + "\"");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testAttributeLang(page.getFirstTabContentParentElement());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testName() {
        tabAttributes.set(TabAttributes.name, "metamer");

        page.getTab().switchTo(4);
        waitGui(driver).until().element(ByJQuery.selector("div[id$=tab5] > div.rf-tab-cnt:visible")).is().present();

        executeJS("RichFaces.component('" + page.getTabAsWebElement().getAttribute("id") + "').switchToItem('metamer')");
        waitGui(driver).until().element(ByJQuery.selector("div[id$=tab1] > div.rf-tab-cnt:visible")).is().present();
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        Action action = new Actions(driver).click(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onclick, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        Action action = new Actions(driver).doubleClick(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.ondblclick, action);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9537 https://issues.jboss.org/browse/RF-10488")
    public void testOnenter() {
        Action action = new Actions(driver).click(page.getInactiveHeaders().get(1)).click(page.getInactiveHeaders().get(0))
            .build();
        testFireEvent(tabAttributes, TabAttributes.onenter, action);
        // tabAttributes.set(TabAttributes.onenter, "metamerEvents += \"enter \"");
        // selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        // String time1Value = selenium.getText(time);
        //
        // guardXhr(selenium).click(inactiveHeaders[1]);
        // waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));
        // guardXhr(selenium).click(inactiveHeaders[0]);
        // waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));
        //
        // String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");
        //
        // assertEquals(events[0], "enter", "Attribute onenter doesn't work");
    }

    @Test
    @Templates(value = "plain")
    public void testOnheaderclick() {
        Action action = new Actions(driver).click(page.getActiveHeaders().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onheaderclick, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnheaderdblclick() {
        Action action = new Actions(driver).doubleClick(page.getActiveHeaders().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onheaderdblclick, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnheadermousedown() {
        Action action = new Actions(driver).click(page.getInactiveHeaders().get(2))
            .clickAndHold(page.getInactiveHeaders().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onheadermousedown, action);
        new Actions(driver).release().build().perform();
    }

    @Test
    @Templates(value = "plain")
    public void testOnheadermousemove() {
        Action action = new Actions(driver).moveToElement(page.getInactiveHeaders().get(2))
            .moveToElement(page.getActiveHeaders().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onheadermousemove, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnheadermouseup() {
        Action action = new Actions(driver).clickAndHold(page.getItemContents().get(0))
            .moveToElement(page.getActiveHeaders().get(0)).release().build();
        testFireEvent(tabAttributes, TabAttributes.onheadermouseup, action);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9537")
    public void testOnleave() {
        Action action = new Actions(driver).click(page.getInactiveHeaders().get(2)).build();
        testFireEvent(tabAttributes, TabAttributes.onleave, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        Action action = new Actions(driver).clickAndHold(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmousedown, action);
        new Actions(driver).release().build().perform();
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        Action action = new Actions(driver).moveToElement(page.getInactiveHeaders().get(2))
            .moveToElement(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmousemove, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        Action action = new Actions(driver).moveToElement(page.getItemContents().get(0))
            .moveToElement(page.getInactiveHeaders().get(2)).build();
        testFireEvent(tabAttributes, TabAttributes.onmouseout, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        Action action = new Actions(driver).moveToElement(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmouseover, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        Action action = new Actions(driver).clickAndHold(page.getDisabledHeaders().get(0))
            .release(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmouseup, action);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        tabAttributes.set(TabAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.getFirstTabContentParentElement(), "Tab should not be rendered when rendered=false.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getFirstTabContentParentElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getFirstTabContentParentElement());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testSwitchTypeNull() {
        guardAjax(page.getInactiveHeaders().get(1)).click();
        waitGui(driver).withMessage("Tab 2 is not displayed.").until().element(page.getItemContents().get(1)).is().visible();

        guardAjax(page.getInactiveHeaders().get(0)).click();
        waitGui(driver).withMessage("Tab 1 is not displayed.").until().element(page.getItemContents().get(0)).is().visible();
    }

    @Test
    @Templates(value = "plain")
    public void testSwitchTypeAjax() {
        tabAttributes.set(TabAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        tabAttributes.set(TabAttributes.switchType, "client");

        guardAjax(page.getInactiveHeaders().get(1)).click();
        waitGui(driver).withMessage("Tab 2 is not displayed.").until().element(page.getItemContents().get(1)).is().visible();

        guardNoRequest(page.getInactiveHeaders().get(0)).click();
        waitGui(driver).withMessage("Tab 1 is not displayed.").until().element(page.getItemContents().get(0)).is().visible();
    }

    @Test
    public void testSwitchTypeServer() {
        tabAttributes.set(TabAttributes.switchType, "server");

        guardAjax(page.getInactiveHeaders().get(1)).click();
        waitGui(driver).withMessage("Tab 2 is not displayed.").until().element(page.getItemContents().get(1)).is().visible();

        guardHttp(page.getInactiveHeaders().get(0)).click();
        waitGui(driver).withMessage("Tab 1 is not displayed.").until().element(page.getItemContents().get(0)).is().visible();
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(page.getFirstTabContentParentElement());
    }
}
