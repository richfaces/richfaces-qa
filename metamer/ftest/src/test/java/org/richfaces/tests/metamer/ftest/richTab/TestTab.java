/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTab;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.richfaces.fragment.switchable.SwitchType.CLIENT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTab/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 * @author <a href="mailto:manovotn@redhat.com">Juraj Huska</a>
 * @version $Revision: 22963 $
 */
public class TestTab extends AbstractWebDriverTest {

    @Page
    private TabSimplePage page;

    private final Action switchToTabAction = new Action() {

        @Override
        public void perform() {
            page.getTabPanel().switchTo(0);
        }
    };
    private final Attributes<TabAttributes> tabAttributes = getAttributes();

    private boolean belongsClass(WebElement elem, String className) {
        return elem.getAttribute("class").contains(className);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richTab/simple.xhtml";
    }

    @Test
    @CoversAttributes({ "action", "actionListener" })
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11427", "https://issues.jboss.org/browse/RF-13748" })
    public void testActionAndActionListener() {
        page.getTabPanel().switchTo(2);

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: tab1 -> tab3");

        page.getTabPanel().switchTo(0);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: tab3 -> tab1");
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
    }

    @Test
    @CoversAttributes("bypassUpdates")
    public void testBypassUpdates() {
        tabAttributes.set(TabAttributes.bypassUpdates, true);

        page.getTabPanel().switchTo(0);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "action invoked");
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "action listener invoked");

        tabAttributes.set(TabAttributes.bypassUpdates, false);
        page.getTabPanel().switchTo(0);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
    }

    @Test
    @CoversAttributes("contentClass")
    @Templates(value = "plain")
    public void testContentClass() {
        tabAttributes.set(TabAttributes.contentClass, "metamer-ftest-class");
        assertTrue(belongsClass(page.getItemContents().get(0), "metamer-ftest-class"));
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(switchToTabAction);
    }

    @Test
    @CoversAttributes("dir")
    @Templates(value = "plain")
    public void testDir() {
        testDir(page.getFirstTabContentParentElement());
    }

    @Test
    @CoversAttributes("disabled")
    @Templates(value = "plain")
    public void testDisabled() {
        // enable the first tab, disabled is by default only 4th tab
        tabAttributes.set(TabAttributes.disabled, Boolean.FALSE);

        List<WebElement> disabledHeaders = page.getDisabledHeaders();
        assertEquals(disabledHeaders.size(), 1);
        assertEquals(disabledHeaders.get(0).getText(), "tab4 header");

        // disable the first tab, now there should 2 disabled headers
        tabAttributes.set(TabAttributes.disabled, Boolean.TRUE);
        disabledHeaders = page.getDisabledHeaders();
        assertEquals(disabledHeaders.size(), 2);
        assertEquals(disabledHeaders.get(0).getText(), "tab1 header");

        page.getTabPanel().advanced().setSwitchType(CLIENT);
        page.getTabPanel().switchTo("tab2 header");
        try {
            page.getTabPanel().switchTo("tab1 header");
            fail("It should not be possible to switch to disabled tab");
        } catch (TimeoutException ex) {
            //OK
        }
        assertEquals(page.getTabPanel().advanced().getActiveHeaderElement().getText(), "tab2 header");
    }

    @Test
    @CoversAttributes("execute")
    public void testExecute() {
        tabAttributes.set(TabAttributes.execute, "@this executeChecker");
        page.getTabPanel().switchTo(0);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, MetamerPage.STRING_EXECUTE_CHECKER_MSG);
    }

    @Test
    @CoversAttributes("header")
    @Templates(value = "plain")
    public void testHeader() {
        tabAttributes.set(TabAttributes.header, "new header");
        assertEquals(page.getTabPanel().advanced().getActiveHeaderElement().getText(), "new header", "Header of the first tab did not change.");

        tabAttributes.set(TabAttributes.header, "ľščťťžžôúňď ацущьмщфзщйцу");
        assertEquals(page.getTabPanel().advanced().getActiveHeaderElement().getText(), "ľščťťžžôúňď ацущьмщфзщйцу",
            "Header of the first tab did not change.");
    }

    @Test
    @CoversAttributes("headerActiveClass")
    @Templates(value = "plain")
    public void testHeaderActiveClass() {
        tabAttributes.set(TabAttributes.headerActiveClass, "metamer-ftest-class");

        assertTrue(belongsClass(page.getTabPanel().advanced().getActiveHeaderElement(), "metamer-ftest-class"),
            "headerActiveClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(0), "metamer-ftest-class"),
            "headerActiveClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(1), "metamer-ftest-class"),
            "headerActiveClass does not work");
        assertFalse(belongsClass(page.getInactiveHeaders().get(2), "metamer-ftest-class"),
            "headerActiveClass does not work");
        assertFalse(belongsClass(page.getDisabledHeaders().get(0), "metamer-ftest-class"),
            "headerActiveClass does not work");
    }

    @Test
    @CoversAttributes("headerClass")
    @IssueTracking("https://issues.jboss.org/browse/RF-11549")
    @Templates(value = "plain")
    public void testHeaderClass() {
        tabAttributes.set(TabAttributes.headerClass, "metamer-ftest-class");
        assertTrue(belongsClass(page.getTabPanel().advanced().getActiveHeaderElement(), "metamer-ftest-class"), "tabHeaderClass does not work");
    }

    @Test
    @CoversAttributes("headerDisabledClass")
    @Templates(value = "plain")
    public void testHeaderDisabledClass() {
        tabAttributes.set(TabAttributes.headerDisabledClass, "metamer-ftest-class");
        assertFalse(belongsClass(page.getDisabledHeaders().get(0), "metamer-ftest-class"),
            "headerDisabledClass does not work");
        tabAttributes.set(TabAttributes.disabled, true);
        assertTrue(belongsClass(page.getDisabledHeaders().get(0), "metamer-ftest-class"),
            "headerDisabledClass does not work");
    }

    @Test
    @CoversAttributes("headerInactiveClass")
    @Templates(value = "plain")
    public void testHeaderInactiveClass() {
        tabAttributes.set(TabAttributes.headerInactiveClass, "metamer-ftest-class");
        assertFalse(belongsClass(page.getTabPanel().advanced().getAllVisibleHeadersElements()
            .get(0), "metamer-ftest-class"),
            "headerInactiveClass does not work");
        page.getTabPanel().switchTo(1);
        assertTrue(belongsClass(page.getInactiveHeaders().get(0), "metamer-ftest-class"),
            "headerInactiveClass does not work");
    }

    @Test
    @CoversAttributes("headerStyle")
    @Templates(value = "plain")
    public void testHeaderStyle() {
        testStyle(page.getTabPanel().advanced().getActiveHeaderElement(), BasicAttributes.headerStyle);
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        tabAttributes.set(TabAttributes.immediate, true);
        page.getTabPanel().switchTo(0);
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "action invoked");
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "action listener invoked");

        tabAttributes.set(TabAttributes.immediate, false);
        page.getTabPanel().switchTo(0);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
    }

    @Test
    public void testInit() {
        assertVisible(page.getTabPanel().advanced().getRootElement(), "Tab panel is not present on the page.");

        assertVisible(page.getTabPanel().advanced().getActiveHeaderElement(), "Header of tab1 should be active.");
        assertVisible(page.getDisabledHeaders().get(0), "Header of tab4 should be disabled.");

        String text = page.getTabPanel().advanced().getActiveHeaderElement().getText();
        assertEquals(text, "tab1 header");
        text = page.getInactiveHeaders().get(0).getText();
        assertEquals(text, "tab2 header");
        text = page.getDisabledHeaders().get(0).getText();
        assertEquals(text, "tab4 header");
        text = page.getItemContents().get(0).getText();
        assertEquals(text, "content of tab 1");
    }

    @Test
    @CoversAttributes("lang")
    @Templates(value = "plain")
    public void testLang() {
        testLang(page.getFirstTabContentParentElement());
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        testLimitRenderWithSwitchTypeOrMode(switchToTabAction);
    }

    @Test
    @CoversAttributes("name")
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testName() {
        tabAttributes.set(TabAttributes.name, "metamer");

        page.getTabPanel().switchTo(4);
        waitGui(driver).until().element(ByJQuery.selector("div[id$=tab5] > div.rf-tab-cnt:visible")).is().present();

        executeJS("RichFaces.component('" + page.getTabAsWebElement().getAttribute("id") + "').switchToItem('metamer')");
        waitGui(driver).until().element(ByJQuery.selector("div[id$=tab1] > div.rf-tab-cnt:visible")).is().present();
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    @Templates(value = "plain")
    public void testOnbeforedomupdate() {
        testFireEvent("onbeforedomupdate", switchToTabAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14076")
    @CoversAttributes("onbegin")
    @Templates(value = "plain")
    public void testOnbegin() {
        testFireEvent("onbegin", switchToTabAction);
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        Action action = new Actions(driver).click(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onclick, action);
    }

    @Test
    @CoversAttributes("oncomplete")
    @Templates(value = "plain")
    public void testOncomplete() {
        testFireEvent("oncomplete", switchToTabAction);
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        Action action = new Actions(driver).doubleClick(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.ondblclick, action);
    }

    @Test(groups = "smoke")
    @CoversAttributes("onenter")
    @IssueTracking("https://issues.jboss.org/browse/RF-9537 https://issues.jboss.org/browse/RF-10488")
    public void testOnenter() {
        testFireEvent(tabAttributes, TabAttributes.onenter, new Action() {
            @Override
            public void perform() {
                Graphene.guardAjax(page.getTabPanel()).switchTo(1);
                Graphene.guardAjax(page.getTabPanel()).switchTo(0);
            }
        });
    }

    @Test
    @CoversAttributes("onheaderclick")
    @Templates(value = "plain")
    public void testOnheaderclick() {
        Action action = new Actions(driver).click(page.getTabPanel().advanced().getActiveHeaderElement()).build();
        testFireEvent(tabAttributes, TabAttributes.onheaderclick, action);
    }

    @Test
    @CoversAttributes("onheaderdblclick")
    @Templates(value = "plain")
    public void testOnheaderdblclick() {
        Action action = new Actions(driver).doubleClick(page.getTabPanel().advanced().getActiveHeaderElement()).build();
        testFireEvent(tabAttributes, TabAttributes.onheaderdblclick, action);
    }

    @Test
    @CoversAttributes("onheadermousedown")
    @Templates(value = "plain")
    public void testOnheadermousedown() {
        Action action = new Actions(driver).clickAndHold(page.getTabPanel().advanced().getActiveHeaderElement()).build();
        testFireEvent(tabAttributes, TabAttributes.onheadermousedown, action);
    }

    @Test
    @CoversAttributes("onheadermousemove")
    @Templates(value = "plain")
    public void testOnheadermousemove() {
        Action action = new Actions(driver).moveToElement(page.getInactiveHeaders().get(2))
            .moveToElement(page.getTabPanel().advanced().getActiveHeaderElement()).build();
        testFireEvent(tabAttributes, TabAttributes.onheadermousemove, action);
    }

    @Test
    @CoversAttributes("onheadermouseup")
    @Templates(value = "plain")
    public void testOnheadermouseup() {
        Action action = new Actions(driver).clickAndHold(page.getItemContents().get(0))
            .moveToElement(page.getTabPanel().advanced().getActiveHeaderElement()).release().build();
        testFireEvent(tabAttributes, TabAttributes.onheadermouseup, action);
    }

    @Test
    @CoversAttributes("onleave")
    @IssueTracking("https://issues.jboss.org/browse/RF-9537")
    public void testOnleave() {
        Action action = new Actions(driver).click(page.getInactiveHeaders().get(2)).build();
        testFireEvent(tabAttributes, TabAttributes.onleave, action);
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        Action action = new Actions(driver).clickAndHold(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmousedown, action);
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        Action action = new Actions(driver).moveToElement(page.getInactiveHeaders().get(2))
            .moveToElement(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmousemove, action);
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        Action action = new Actions(driver).moveToElement(page.getItemContents().get(0))
            .moveToElement(page.getInactiveHeaders().get(2)).build();
        testFireEvent(tabAttributes, TabAttributes.onmouseout, action);
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        Action action = new Actions(driver).moveToElement(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmouseover, action);
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        new Actions(driver).clickAndHold(page.getItemContents().get(0)).perform();
        Action action = new Actions(driver).release(page.getItemContents().get(0)).build();
        testFireEvent(tabAttributes, TabAttributes.onmouseup, action);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        testRenderWithSwitchTypeOrMode(switchToTabAction);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        tabAttributes.set(TabAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.getFirstTabContentParentElement(), "Tab should not be rendered when rendered=false.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14076")
    @CoversAttributes("status")
    public void testStatus() {
        testStatus(switchToTabAction);
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getFirstTabContentParentElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getFirstTabContentParentElement());
    }

    @Test(groups = "smoke")
    @CoversAttributes("switchType")
    @Templates(value = "plain")
    public void testSwitchTypeAjax() {
        tabAttributes.set(TabAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test(groups = "smoke")
    @CoversAttributes("switchType")
    public void testSwitchTypeClient() {
        tabAttributes.set(TabAttributes.switchType, "client");

        guardAjax(page.getInactiveHeaders().get(1)).click();
        waitGui(driver).withMessage("Tab 2 is not displayed.").until().element(page.getItemContents().get(1)).is()
            .visible();

        guardNoRequest(page.getInactiveHeaders().get(0)).click();
        waitGui(driver).withMessage("Tab 1 is not displayed.").until().element(page.getItemContents().get(0)).is()
            .visible();
    }

    @Test
    @CoversAttributes("switchType")
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testSwitchTypeNull() {
        guardAjax(page.getInactiveHeaders().get(1)).click();
        waitGui(driver).withMessage("Tab 2 is not displayed.").until().element(page.getItemContents().get(1)).is()
            .visible();

        guardAjax(page.getInactiveHeaders().get(0)).click();
        waitGui(driver).withMessage("Tab 1 is not displayed.").until().element(page.getItemContents().get(0)).is()
            .visible();
    }

    @Test
    @CoversAttributes("switchType")
    public void testSwitchTypeServer() {
        tabAttributes.set(TabAttributes.switchType, "server");

        guardAjax(page.getInactiveHeaders().get(1)).click();
        waitGui(driver).withMessage("Tab 2 is not displayed.").until().element(page.getItemContents().get(1)).is()
            .visible();

        guardHttp(page.getInactiveHeaders().get(0)).click();
        waitGui(driver).withMessage("Tab 1 is not displayed.").until().element(page.getItemContents().get(0)).is()
            .visible();
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        htmlAttributeTester().testTitle(page.getFirstTabContentParentElement()).test();
    }
}
