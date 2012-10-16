/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.tabPanelAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTabPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23036 $
 */
public class TestRichTabPanel extends AbstractGrapheneTest {

    private JQueryLocator panel = pjq("div[id$=tabPanel]");
    private JQueryLocator[] items = { pjq("div[id$=tab1]"), pjq("div[id$=tab2]"), pjq("div[id$=tab3]"),
        pjq("div[id$=tab4]"), pjq("div[id$=tab5]") };
    private JQueryLocator[] itemContents = { pjq("div[id$=tab1] > div.rf-tab-cnt"),
        pjq("div[id$=tab2] > div.rf-tab-cnt"), pjq("div[id$=tab3] > div.rf-tab-cnt"),
        pjq("div[id$=tab4] > div.rf-tab-cnt"), pjq("div[id$=tab5] > div.rf-tab-cnt") };
    private JQueryLocator[] activeHeaders = { pjq("td[id$=tab1:header:active]"), pjq("td[id$=tab2:header:active]"),
        pjq("td[id$=tab3:header:active]"), pjq("td[id$=tab4:header:active]"), pjq("td[id$=tab5:header:active]") };
    private JQueryLocator[] inactiveHeaders = { pjq("td[id$=tab1:header:inactive]"),
        pjq("td[id$=tab2:header:inactive]"), pjq("td[id$=tab3:header:inactive]"), pjq("td[id$=tab4:header:inactive]"),
        pjq("td[id$=tab5:header:inactive]") };
    private JQueryLocator[] disabledHeaders = { pjq("td[id$=tab1:header:disabled]"),
        pjq("td[id$=tab2:header:disabled]"), pjq("td[id$=tab3:header:disabled]"), pjq("td[id$=tab4:header:disabled]"),
        pjq("td[id$=tab5:header:disabled]") };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Tab Panel", "Simple");
    }

    @Test
    @Templates(exclude = { "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable" })
    public void testHeaderAlignment() {
        JQueryLocator spaceLeft = jq(panel.getRawLocator() + " *.rf-tab-hdr-spcr:eq(0)");
        JQueryLocator spaceRight = jq(panel.getRawLocator() + " *.rf-tab-hdr-spcr:eq(6)");

        assertEquals(selenium.getStyle(spaceLeft, CssProperty.WIDTH), "0px",
                "The header should be aligned to the left, but it isn't.");
        assertTrue(Integer.parseInt(selenium.getStyle(spaceRight, CssProperty.WIDTH).replace("px", "")) > 100,
                "The header should be aligned to the left, but it isn't.");

        tabPanelAttributes.set(TabPanelAttributes.headerAlignment, "left");

        assertEquals(selenium.getStyle(spaceLeft, CssProperty.WIDTH), "0px",
                "The header should be aligned to the left, but it isn't.");
        assertTrue(Integer.parseInt(selenium.getStyle(spaceRight, CssProperty.WIDTH).replace("px", "")) > 100,
                "The header should be aligned to the left, but it isn't.");

        tabPanelAttributes.set(TabPanelAttributes.headerAlignment, "right");

        assertTrue(Integer.parseInt(selenium.getStyle(spaceLeft, CssProperty.WIDTH).replace("px", "")) > 100,
                "The header should be aligned to the right, but it isn't.");
        assertEquals(selenium.getStyle(spaceRight, CssProperty.WIDTH), "0px",
                "The header should be aligned to the right, but it isn't.");
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11550")
    @Templates(value = { "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable" })
    public void testHeaderAlignmentIterationComponents() {
        testHeaderAlignment();
    }

    @Test
    public void testHeaderPosition() {
        JQueryLocator bottomVersion = jq(panel.getRawLocator() + " .rf-tab-hdr-tabline-vis.rf-tab-hdr-tabline-btm");
        JQueryLocator topVersion = jq(panel.getRawLocator() + " .rf-tab-hdr-tabline-vis.rf-tab-hdr-tabline-top");

        tabPanelAttributes.set(TabPanelAttributes.headerPosition, "top");
        assertTrue(selenium.isElementPresent(topVersion), "The header should be placed on the top position ("
                + topVersion.getRawLocator() + ")");

        tabPanelAttributes.set(TabPanelAttributes.headerPosition, "bottom");
        assertTrue(selenium.isElementPresent(bottomVersion), "The header should be placed on the bottom position ("
                + bottomVersion.getRawLocator() + ")");
    }

    @Test
    public void testInit() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Tab panel is not present on the page.");

        displayed = selenium.isVisible(activeHeaders[0]);
        assertTrue(displayed, "Header of tab1 should be active.");
        for (int i = 1; i < 5; i++) {
            displayed = selenium.isVisible(activeHeaders[i]);
            assertFalse(displayed, "Header of tab " + (i + 1) + " should not be active.");
        }

        displayed = selenium.isVisible(inactiveHeaders[0]);
        assertFalse(displayed, "Header of tab1 should not be inactive.");
        displayed = selenium.isVisible(inactiveHeaders[1]);
        assertTrue(displayed, "Header of tab2 should be inactive.");

        displayed = selenium.isVisible(disabledHeaders[3]);
        assertTrue(displayed, "Header of tab4 should be disabled.");
        for (int i = 0; i < 3; i++) {
            displayed = selenium.isVisible(disabledHeaders[i]);
            assertFalse(displayed, "Header of tab " + (i + 1) + " should not be disabled.");
        }

        displayed = selenium.isVisible(itemContents[0]);
        assertTrue(displayed, "Content of item1 should be visible.");

        for (int i = 1; i < 5; i++) {
            displayed = selenium.isVisible(items[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10351")
    public void testActiveItem() {
        tabPanelAttributes.set(TabPanelAttributes.activeItem, "tab5");

        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Tab panel is not present on the page.");

        for (int i = 0; i < 4; i++) {
            displayed = selenium.isVisible(activeHeaders[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s header should not be active.");
        }

        displayed = selenium.isVisible(itemContents[4]);
        assertTrue(displayed, "Content of tab5 should be visible.");

        for (int i = 0; i < 4; i++) {
            displayed = selenium.isVisible(items[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s content should not be visible.");
        }

        tabPanelAttributes.set(TabPanelAttributes.activeItem, "tab4");

        for (int i = 1; i < 5; i++) {
            displayed = selenium.isVisible(activeHeaders[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s header should not be active.");
        }

        for (int i = 1; i < 5; i++) {
            displayed = selenium.isVisible(items[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    public void testCycledSwitching() {
        String panelId = selenium.getEval(new JavaScript("window.testedComponentId"));
        String result = null;

        // RichFaces.$('form:tabPanel').nextItem('tab4') will be null
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').nextItem('tab4')"));
        assertEquals(result, "null", "Result of function nextItem('tab4')");

        // RichFaces.$('form:tabPanel').prevItem('tab1') will be null
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').prevItem('tab1')"));
        assertEquals(result, "null", "Result of function prevItem('tab1')");

        tabPanelAttributes.set(TabPanelAttributes.cycledSwitching, Boolean.TRUE);

        // RichFaces.$('form:tabPanel').nextItem('tab5') will be item1
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').nextItem('tab5')"));
        assertEquals(result, "tab1", "Result of function nextItem('tab5')");

        // RichFaces.$('form:tabPanel').prevItem('tab1') will be item5
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').prevItem('tab1')"));
        assertEquals(result, "tab5", "Result of function prevItem('tab1')");
    }

    @Test
    public void testDir() {
        super.testDir(panel);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        tabPanelAttributes.set(TabPanelAttributes.immediate, Boolean.TRUE);

        selenium.click(inactiveHeaders[2]);
        waitGui.failWith("Tab 3 is not displayed.").until(
                elementPresent.locator(jq(itemContents[2].getRawLocator() + ":visible")));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: tab1 -> tab3");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10523")
    public void testItemChangeListener() {
        selenium.click(inactiveHeaders[2]);
        waitGui.failWith("Item 3 is not displayed.").until(
                elementPresent.locator(jq(itemContents[2].getRawLocator() + ":visible")));
        phaseInfo.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: tab1 -> tab3");
    }

    @Test
    public void testLang() {
        testLang(panel);
    }

    @Test
    public void testOnbeforeitemchange() {
        tabPanelAttributes.set(TabPanelAttributes.onbeforeitemchange, "metamerEvents += \"onbeforeitemchange \"");

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        waitGui.failWith("onbeforeitemchange attribute does not work correctly").until(
                new EventFiredCondition(new Event("beforeitemchange")));
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10165")
    public void testItemchangeEvents() {
        tabPanelAttributes.set(TabPanelAttributes.onbeforeitemchange, "metamerEvents += \"beforeitemchange \"");
        tabPanelAttributes.set(TabPanelAttributes.onitemchange, "metamerEvents += \"itemchange \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 2, "Two events should be fired - beforeitemchange and itemchange.");
        assertEquals(events[0], "beforeitemchange", "Attribute onbeforeitemchange doesn't work");
        assertEquals(events[1], "itemchange", "Attribute onitemchange doesn't work");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, panel);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, panel);
    }

    @Test
    public void testOnitemchange() {
        tabPanelAttributes.set(TabPanelAttributes.onitemchange, "metamerEvents += \"onitemchange \"");

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        waitGui.failWith("onitemchange attribute does not work correctly").until(
                new EventFiredCondition(new Event("itemchange")));
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, panel);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, panel);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, panel);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, panel);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, panel);
    }

    @Test
    public void testRendered() {
        tabPanelAttributes.set(TabPanelAttributes.rendered,Boolean.FALSE);
        assertFalse(selenium.isElementPresent(panel), "Tab panel should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(panel);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(panel);
    }

    @Test
    public void testSwitchTypeNull() {
        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardXhr(selenium).click(inactiveHeaders[index]);
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(
                    elementVisible.locator(itemContents[index]));
        }
    }

    @Test
    public void testSwitchTypeAjax() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "client");

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardNoRequest(selenium).click(inactiveHeaders[index]);
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(
                    elementVisible.locator(itemContents[index]));
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchTypeServer() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "server");

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardHttp(selenium).click(inactiveHeaders[index]);
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(
                    elementVisible.locator(itemContents[index]));
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    public void testTabActiveHeaderClass() {
        tabPanelAttributes.set(TabPanelAttributes.tabActiveHeaderClass, "metamer-ftest-class");

        for (JQueryLocator loc : activeHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabActiveHeaderClass does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabActiveHeaderClass does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabActiveHeaderClass does not work");
        }
    }

    @Test
    public void testTabContentClass() {
        final String value = "metamer-ftest-class";

        tabPanelAttributes.set(TabPanelAttributes.tabContentClass, value);
        selenium.type(pjq("input[id$=tabContentClassInput]"), value);
        selenium.waitForPageToLoad();

        assertTrue(selenium.belongsClass(itemContents[0], value), "tabContentClass does not work");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    public void testTabDisabledHeaderClass() {
        tabPanelAttributes.set(TabPanelAttributes.tabDisabledHeaderClass, "metamer-ftest-class");

        for (JQueryLocator loc : activeHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabDisabledHeaderClass does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabDisabledHeaderClass does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabDisabledHeaderClass does not work");
        }
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9309", "https://issues.jboss.org/browse/RF-11549" })
    public void testTabHeaderClass() {
        tabPanelAttributes.set(TabPanelAttributes.tabHeaderClass, "metamer-ftest-class");

        for (JQueryLocator loc : activeHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClass does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClass does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClass does not work");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    public void testTabInactiveHeaderClass() {
        tabPanelAttributes.set(TabPanelAttributes.tabInactiveHeaderClass, "metamer-ftest-class");

        for (JQueryLocator loc : activeHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabInactiveHeaderClass does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabInactiveHeaderClass does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabInactiveHeaderClass does not work");
        }
    }

    @Test
    public void testTitle() {
        testTitle(panel);
    }
}
