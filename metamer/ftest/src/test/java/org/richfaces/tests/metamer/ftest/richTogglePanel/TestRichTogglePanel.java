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
package org.richfaces.tests.metamer.ftest.richTogglePanel;

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.togglePanelAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import javax.faces.event.PhaseId;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richTogglePanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichTogglePanel extends AbstractGrapheneTest {

    private JQueryLocator panel = pjq("div[id$=richTogglePanel]");
    private JQueryLocator item1 = pjq("div[id$=item1]");
    private JQueryLocator item2 = pjq("div[id$=item2]");
    private JQueryLocator item3 = pjq("div[id$=item3]");
    // toggle controls
    private JQueryLocator tc1 = pjq("a[id$=tcLink1]");
    private JQueryLocator tc2 = pjq("a[id$=tcLink2]");
    private JQueryLocator tc3 = pjq("a[id$=tcLink3]");
    private JQueryLocator tcFirst = pjq("a[id$=tcFirst]");
    private JQueryLocator tcPrev = pjq("a[id$=tcPrev]");
    private JQueryLocator tcNext = pjq("a[id$=tcNext]");
    private JQueryLocator tcLast = pjq("a[id$=tcLast]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTogglePanel/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(panel), "Toggle panel is not present on the page.");
        assertTrue(selenium.isVisible(panel), "Toggle panel is not visible.");
        assertTrue(selenium.isElementPresent(item1), "Item 1 is not present on the page.");
        assertTrue(selenium.isVisible(item1), "Item 1 is not visible.");
        assertTrue(selenium.isElementPresent(item2), "Item 2 is not present on the page.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertTrue(selenium.isElementPresent(item3), "Item 3 is not present on the page.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");
    }

    @Test
    public void testFirstLastPrevNextSwitchNull() {
        guardXhr(selenium).click(tcNext);
        waitGui.failWith("Next item (2) is not displayed.").until(elementVisible.locator(item2));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");

        guardXhr(selenium).click(tcPrev);
        waitGui.failWith("Previous item (1) is not displayed.").until(elementVisible.locator(item1));
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");

        guardXhr(selenium).click(tcLast);
        waitGui.failWith("Last item (3) is not displayed.").until(elementVisible.locator(item3));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");

        guardXhr(selenium).click(tcFirst);
        waitGui.failWith("First item (1) is not displayed.").until(elementVisible.locator(item1));
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");
    }

    @Test
    public void testFirstLastPrevNextSwitchAjax() {
        togglePanelAttributes.set(TogglePanelAttributes.switchType, "ajax");
        testFirstLastPrevNextSwitchNull();
    }

    @Test
    public void testFirstLastPrevNextSwitchClient() {
       togglePanelAttributes.set(TogglePanelAttributes.switchType, "client");

        guardNoRequest(selenium).click(tcNext);
        waitGui.failWith("Next item (2) is not displayed.").until(elementVisible.locator(item2));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");

        guardNoRequest(selenium).click(tcPrev);
        waitGui.failWith("Previous item (1) is not displayed.").until(elementVisible.locator(item1));
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");

        guardNoRequest(selenium).click(tcLast);
        waitGui.failWith("Last item (3) is not displayed.").until(elementVisible.locator(item3));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");

        guardNoRequest(selenium).click(tcFirst);
        waitGui.failWith("First item (1) is not displayed.").until(elementVisible.locator(item1));
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testFirstLastPrevNextSwitchServer() {
        togglePanelAttributes.set(TogglePanelAttributes.switchType, "server");

        guardHttp(selenium).click(tcNext);
        assertTrue(selenium.isVisible(item2), "Next item (2) should be visible.");
        assertFalse(selenium.isVisible(item1), "Item 1 should not be displayed.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be displayed.");

        guardHttp(selenium).click(tcPrev);
        assertTrue(selenium.isVisible(item1), "Previous item (2) should be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be displayed.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be displayed.");

        guardHttp(selenium).click(tcLast);
        assertTrue(selenium.isVisible(item3), "Last item (3) should be visible.");
        assertFalse(selenium.isVisible(item1), "Item 1 should not be displayed.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be displayed.");

        guardHttp(selenium).click(tcFirst);
        assertTrue(selenium.isVisible(item1), "First item (1) should be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be displayed.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be displayed.");
    }

    @Test
    public void testActiveItem() {
        togglePanelAttributes.set(TogglePanelAttributes.activeItem, "item3");

        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Toggle panel is not present on the page.");

        displayed = selenium.isVisible(item1);
        assertFalse(displayed, "Content of item1 should not be visible.");

        displayed = selenium.isVisible(item2);
        assertFalse(displayed, "Content of item2 should not be visible.");

        displayed = selenium.isVisible(item3);
        assertTrue(displayed, "Content of item3 should be visible.");
    }

    @Test
    public void testCycledSwitching() {
        togglePanelAttributes.set(TogglePanelAttributes.cycledSwitching, Boolean.TRUE);
        guardXhr(selenium).click(tcPrev);
        waitGui.failWith("Previous item (3) is not displayed.").until(elementVisible.locator(item3));
        guardXhr(selenium).click(tcNext);
        waitGui.failWith("Next item (1) is not displayed.").until(elementVisible.locator(item1));
    }

    @Test
    public void testDir() {
        super.testDir(panel);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        togglePanelAttributes.set(TogglePanelAttributes.immediate, Boolean.TRUE);
        selenium.click(tc3);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(item3));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: item1 -> item3");
    }

    @Test
    public void testItemChangeListener() {
        selenium.click(tc3);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(item3));

        phaseInfo.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: item1 -> item3");
    }

    @Test
    public void testLang() {
        testLang(panel);
    }

    @Test
    public void testOnbeforeitemchange() {
        togglePanelAttributes.set(TogglePanelAttributes.onbeforeitemchange, "metamerEvents += \"onbeforeitemchange \"");
        guardXhr(selenium).click(tc2);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(item2));

        waitGui.failWith("onbeforeitemchange attribute does not work correctly").until(new EventFiredCondition(new Event("beforeitemchange")));
    }

    @Test
    public void testItemchangeEvents() {
        togglePanelAttributes.set(TogglePanelAttributes.onbeforeitemchange, "metamerEvents += \"beforeitemchange \"");
        togglePanelAttributes.set(TogglePanelAttributes.onitemchange, "metamerEvents += \"itemchange \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(tc2);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "beforeitemchange", "Attribute onbeforeitemchange doesn't work");
        assertEquals(events[1], "itemchange", "Attribute onbeforeitemchange doesn't work");
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
        togglePanelAttributes.set(TogglePanelAttributes.onitemchange, "metamerEvents += \"onitemchange \"");

        guardXhr(selenium).click(tc2);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(item2));

        waitGui.failWith("onitemchange attribute does not work correctly").until(new EventFiredCondition(new Event("itemchange")));
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
        togglePanelAttributes.set(TogglePanelAttributes.rendered, Boolean.FALSE);
        assertFalse(selenium.isElementPresent(panel), "Toggle panel should not be rendered when rendered=false.");
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
        guardXhr(selenium).click(tc3);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(item3));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");

        guardXhr(selenium).click(tc2);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(item2));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");

        guardXhr(selenium).click(tc1);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(item1));
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");
    }

    @Test
    public void testSwitchTypeAjax() {
        togglePanelAttributes.set(TogglePanelAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        togglePanelAttributes.set(TogglePanelAttributes.switchType, "client");

        guardNoRequest(selenium).click(tc3);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(item3));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");

        guardNoRequest(selenium).click(tc2);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(item2));
        assertFalse(selenium.isVisible(item1), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");

        guardNoRequest(selenium).click(tc1);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(item1));
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchTypeServer() {
        togglePanelAttributes.set(TogglePanelAttributes.switchType, "server");

        guardHttp(selenium).click(tc3);
        assertTrue(selenium.isVisible(item3), "Item 3 should be visible.");
        assertFalse(selenium.isVisible(item1), "Item 1 should not be displayed.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be displayed.");

        guardHttp(selenium).click(tc2);
        assertTrue(selenium.isVisible(item2), "Item 2 should be visible.");
        assertFalse(selenium.isVisible(item1), "Item 1 should not be displayed.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be displayed.");

        guardHttp(selenium).click(tc1);
        assertTrue(selenium.isVisible(item1), "Item 1 should be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be displayed.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be displayed.");
    }

    @Test
    public void testTitle() {
        testTitle(panel);
    }
}
