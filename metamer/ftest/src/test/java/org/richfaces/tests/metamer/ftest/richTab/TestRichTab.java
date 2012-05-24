/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.tabAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richTab/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22963 $
 */
public class TestRichTab extends AbstractGrapheneTest {

    private static final JQueryLocator PHASE_FORMAT = jq("div#phasesPanel li:eq({0})");
    private static final String ACTION_LOG = "* action invoked";
    private static final String ACTION_LISTENER_LOG = "* action listener invoked";

    private JQueryLocator panel = pjq("div[id$=tabPanel]");
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
    private JQueryLocator tab1 = pjq("div[id$=tab1]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTab/simple.xhtml");
    }

    @Test(groups = { "4.3" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11427")
    public void testAction() {
        retrieveRequestTime.initializeValue();
        selenium.click(pjq("input[id$=nextTabButton]"));
        waitAjax.waitForChange(retrieveRequestTime);

        retrieveRequestTime.initializeValue();
        selenium.click(pjq("input[id$=prevTabButton]"));
        waitAjax.waitForChange(retrieveRequestTime);

        waitGui.failWith("Expected <" + ACTION_LOG + ">, found <" + selenium.getText(PHASE_FORMAT.format(6)) + ">")
            .until(textEquals.locator(PHASE_FORMAT.format(6)).text(ACTION_LOG));
    }

    @Test(groups = { "4.3" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11427")
    public void testActionListener() {
        retrieveRequestTime.initializeValue();
        selenium.click(pjq("input[id$=nextTabButton]"));
        waitAjax.waitForChange(retrieveRequestTime);

        retrieveRequestTime.initializeValue();
        selenium.click(pjq("input[id$=prevTabButton]"));
        waitAjax.waitForChange(retrieveRequestTime);

        waitGui.failWith(
            "Expected <" + ACTION_LISTENER_LOG + ">, found <" + selenium.getText(PHASE_FORMAT.format(5)) + ">").until(
            textEquals.locator(PHASE_FORMAT.format(5)).text(ACTION_LISTENER_LOG));
    }

    @Test
    public void testInit() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Tab panel is not present on the page.");

        displayed = selenium.isVisible(activeHeaders[0]);
        assertTrue(displayed, "Header of tab1 should be active.");
        displayed = selenium.isVisible(inactiveHeaders[0]);
        assertFalse(displayed, "Header of tab1 should not be inactive.");
        displayed = selenium.isVisible(disabledHeaders[3]);
        assertTrue(displayed, "Header of tab4 should be disabled.");

        String text = selenium.getText(activeHeaders[0]);
        assertEquals(text, "tab1 header");
        text = selenium.getText(inactiveHeaders[1]);
        assertEquals(text, "tab2 header");
        text = selenium.getText(disabledHeaders[3]);
        assertEquals(text, "tab4 header");
        text = selenium.getText(itemContents[0]);
        assertEquals(text, "content of tab 1");

    }

    @Test
    public void testContentClass() {
        tabAttributes.set(TabAttributes.contentClass,"metamer-ftest-class" );
        assertTrue(selenium.belongsClass(itemContents[0], "metamer-ftest-class"), "contentClass does not work");
    }

    @Test
    public void testDir() {
        testDir(tab1);
    }

    @Test
    public void testDisabled() {
        // disable the first tab
        tabAttributes.set(TabAttributes.disabled,Boolean.TRUE );

        boolean displayed = selenium.isVisible(activeHeaders[0]);
        assertFalse(displayed, "Header of tab1 should not be active.");
        displayed = selenium.isVisible(inactiveHeaders[0]);
        assertFalse(displayed, "Header of tab1 should not be inactive.");
        displayed = selenium.isVisible(disabledHeaders[0]);
        assertTrue(displayed, "Header of tab1 should be disabled.");

        String text = selenium.getText(disabledHeaders[0]);
        assertEquals(text, "tab1 header");

        // enable the first tab
        tabAttributes.set(TabAttributes.disabled,Boolean.FALSE );

        displayed = selenium.isVisible(activeHeaders[0]);
        assertTrue(displayed, "Header of tab1 should not be active.");
        displayed = selenium.isVisible(inactiveHeaders[0]);
        assertFalse(displayed, "Header of tab1 should not be inactive.");
        displayed = selenium.isVisible(disabledHeaders[0]);
        assertFalse(displayed, "Header of tab1 should be disabled.");

        text = selenium.getText(activeHeaders[0]);
        assertEquals(text, "tab1 header");
    }

    @Test
    public void testHeader() {
        tabAttributes.set(TabAttributes.header,"new header" );
        assertEquals(selenium.getText(activeHeaders[0]), "new header", "Header of the first tab did not change.");

        tabAttributes.set(TabAttributes.header,"ľščťťžžôúňď ацущьмщфзщйцу" );
        assertEquals(selenium.getText(activeHeaders[0]), "ľščťťžžôúňď ацущьмщфзщйцу",
            "Header of the first tab did not change.");
    }

    @Test
    public void testHeaderActiveClass() {
        tabAttributes.set(TabAttributes.headerActiveClass,"metamer-ftest-class" );

        assertTrue(selenium.belongsClass(activeHeaders[0], "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(selenium.belongsClass(inactiveHeaders[0], "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(selenium.belongsClass(disabledHeaders[0], "metamer-ftest-class"), "headerActiveClass does not work");

        assertFalse(selenium.belongsClass(activeHeaders[1], "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(selenium.belongsClass(inactiveHeaders[1], "metamer-ftest-class"), "headerActiveClass does not work");
        assertFalse(selenium.belongsClass(disabledHeaders[1], "metamer-ftest-class"), "headerActiveClass does not work");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11549")
    public void testHeaderClass() {
        tabAttributes.set(TabAttributes.headerClass,"metamer-ftest-class" );

        assertTrue(selenium.belongsClass(activeHeaders[0], "metamer-ftest-class"), "tabHeaderClass does not work");
        assertTrue(selenium.belongsClass(inactiveHeaders[0], "metamer-ftest-class"), "tabHeaderClass does not work");
        assertTrue(selenium.belongsClass(disabledHeaders[0], "metamer-ftest-class"), "tabHeaderClass does not work");

        assertFalse(selenium.belongsClass(activeHeaders[1], "metamer-ftest-class"), "tabHeaderClass does not work");
        assertFalse(selenium.belongsClass(inactiveHeaders[1], "metamer-ftest-class"), "tabHeaderClass does not work");
        assertFalse(selenium.belongsClass(disabledHeaders[1], "metamer-ftest-class"), "tabHeaderClass does not work");
    }

    @Test
    public void testHeaderDisabledClass() {
        tabAttributes.set(TabAttributes.headerDisabledClass,"metamer-ftest-class" );

        assertFalse(selenium.belongsClass(activeHeaders[0], "metamer-ftest-class"), "headerDisabledClass does not work");
        assertFalse(selenium.belongsClass(inactiveHeaders[0], "metamer-ftest-class"),
            "headerDisabledClass does not work");
        assertTrue(selenium.belongsClass(disabledHeaders[0], "metamer-ftest-class"),
            "headerDisabledClass does not work");

        assertFalse(selenium.belongsClass(activeHeaders[1], "metamer-ftest-class"), "headerDisabledClass does not work");
        assertFalse(selenium.belongsClass(inactiveHeaders[1], "metamer-ftest-class"),
            "headerDisabledClass does not work");
        assertFalse(selenium.belongsClass(disabledHeaders[1], "metamer-ftest-class"),
            "headerDisabledClass does not work");
    }

    @Test
    public void testHeaderInactiveClass() {
        tabAttributes.set(TabAttributes.headerInactiveClass,"metamer-ftest-class" );

        assertFalse(selenium.belongsClass(activeHeaders[0], "metamer-ftest-class"), "headerInactiveClass does not work");
        assertTrue(selenium.belongsClass(inactiveHeaders[0], "metamer-ftest-class"),
            "headerInactiveClass does not work");
        assertFalse(selenium.belongsClass(disabledHeaders[0], "metamer-ftest-class"),
            "headerInactiveClass does not work");

        assertFalse(selenium.belongsClass(activeHeaders[1], "metamer-ftest-class"), "headerInactiveClass does not work");
        assertFalse(selenium.belongsClass(inactiveHeaders[1], "metamer-ftest-class"),
            "headerInactiveClass does not work");
        assertFalse(selenium.belongsClass(disabledHeaders[1], "metamer-ftest-class"),
            "headerInactiveClass does not work");
    }

    @Test
    public void testHeaderStyle() {
        final String value = "background-color: yellow; font-size: 1.5em;";
        tabAttributes.set(TabAttributes.headerStyle, value );
        AttributeLocator<?> styleAttr = activeHeaders[0].getAttribute(Attribute.STYLE);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute style should contain \"" + value + "\"");
    }

    @Test
    public void testLang() {
        testLang(tab1);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testName() {
        tabAttributes.set(TabAttributes.name,"metamer" );

        selenium.click(pjq("input[id$=lastTabButton]"));
        waitGui.failWith("Item 3 was not displayed.").until(elementPresent.locator(jq(itemContents[4].getRawLocator() + ":visible")));

        selenium.click(pjq("input[id$=customTabButton]"));
        waitGui.failWith("Item 1 was not displayed.").until(elementPresent.locator(jq(itemContents[0].getRawLocator() + ":visible")));
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, tab1);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, tab1);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9537 https://issues.jboss.org/browse/RF-10488")
    public void testOnenter() {
        tabAttributes.set(TabAttributes.onenter,"metamerEvents += \"enter \"" );
        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));
        guardXhr(selenium).click(inactiveHeaders[0]);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "enter", "Attribute onenter doesn't work");
    }

    @Test
    public void testOnheaderclick() {
        testFireEvent(Event.CLICK, activeHeaders[0], "headerclick");
    }

    @Test
    public void testOnheaderdblclick() {
        testFireEvent(Event.DBLCLICK, activeHeaders[0], "headerdblclick");
    }

    @Test
    public void testOnheadermousedown() {
        testFireEvent(Event.MOUSEDOWN, activeHeaders[0], "headermousedown");
    }

    @Test
    public void testOnheadermousemove() {
        testFireEvent(Event.MOUSEMOVE, activeHeaders[0], "headermousemove");
    }

    @Test
    public void testOnheadermouseup() {
        testFireEvent(Event.MOUSEUP, activeHeaders[0], "headermouseup");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9537")
    public void testOnleave() {
        tabAttributes.set(TabAttributes.onleave,"metamerEvents += \"leave \"" );

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "leave", "Attribute onleave doesn't work");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, tab1);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, tab1);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, tab1);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, tab1);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, tab1);
    }

    @Test
    public void testRendered() {
        tabAttributes.set(TabAttributes.rendered,Boolean.FALSE );
        assertFalse(selenium.isElementPresent(activeHeaders[0]), "Tab should not be rendered when rendered=false.");
        assertFalse(selenium.isElementPresent(inactiveHeaders[0]), "Tab should not be rendered when rendered=false.");
        assertFalse(selenium.isElementPresent(disabledHeaders[0]), "Tab should not be rendered when rendered=false.");
        assertFalse(selenium.isElementPresent(tab1), "Tab should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(tab1);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(tab1);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testSwitchTypeNull() {
        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        guardXhr(selenium).click(inactiveHeaders[0]);
        waitGui.failWith("Tab 1 is not displayed.").until(elementVisible.locator(itemContents[0]));
    }

    @Test
    public void testSwitchTypeAjax() {
        tabAttributes.set(TabAttributes.switchType,"ajax" );
        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        tabAttributes.set(TabAttributes.switchType,"client" );

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        guardNoRequest(selenium).click(inactiveHeaders[0]);
        waitGui.failWith("Tab 1 is not displayed.").until(elementVisible.locator(itemContents[0]));
    }

    @Test
    public void testSwitchTypeServer() {
        tabAttributes.set(TabAttributes.switchType,"server" );

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        guardHttp(selenium).click(inactiveHeaders[0]);
        waitGui.failWith("Tab 1 is not displayed.").until(elementVisible.locator(itemContents[0]));
    }

    @Test
    public void testTitle() {
        testTitle(tab1);
    }
}
