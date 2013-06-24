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
package org.richfaces.tests.metamer.ftest.richTogglePanelItem;

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.togglePanelItemAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richTogglePanelItem/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichTogglePanelItem extends AbstractGrapheneTest {

    private JQueryLocator item1 = pjq("div[id$=item1]");
    private JQueryLocator item2 = pjq("div[id$=item2]");
    private JQueryLocator item3 = pjq("div[id$=item3]");
    private JQueryLocator link1 = pjq("a[id$=tcLink1]");
    private JQueryLocator link2 = pjq("a[id$=tcLink2]");
    private JQueryLocator link3 = pjq("a[id$=tcLink3]");
    private JQueryLocator linkCustom = pjq("a[id$=tcLinkCustom]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTogglePanelItem/simple.xhtml");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertTrue(selenium.isElementPresent(item1), "Item 1 should be present on the page.");
        assertTrue(selenium.isElementPresent(item2), "Item 2 should be present on the page.");
        assertTrue(selenium.isElementPresent(item3), "Item 3 should be present on the page.");

        assertTrue(selenium.isVisible(item1), "Item 1 should be visible.");
        assertFalse(selenium.isVisible(item2), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(item3), "Item 3 should not be visible.");
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        testDir(item1);
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testLang(item1);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testName() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.name, "metamer");

        selenium.click(link3);
        waitGui.failWith("Item 3 was not displayed.").until(elementVisible.locator(item3));

        selenium.click(linkCustom);
        waitGui.failWith("Item 1 was not displayed.").until(elementVisible.locator(item1));
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, item1);
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, item1);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9895 https://issues.jboss.org/browse/RF-10488")
    public void testOnenter() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.onenter, "metamerEvents += \"enter \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(link2);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));
        guardXhr(selenium).click(link1);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 1, "Exactly one event should be fired.");
        assertEquals(events[0], "enter", "Attribute onenter doesn't work");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9895")
    public void testOnleave() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.onleave, "metamerEvents += \"leave \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(link3);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 1, "Exactly one event should be fired.");
        assertEquals(events[0], "leave", "Attribute onleave doesn't work");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, item1);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, item1);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, item1);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, item1);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, item1);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9894")
    @Templates(value = "plain")
    public void testRendered() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.rendered, Boolean.FALSE);

        assertFalse(selenium.isElementPresent(item1), "Tab should not be rendered when rendered=false.");
        assertTrue(selenium.isVisible(item2), "Item 2 should be displayed when item 1 is not rendered.");

        String timeValue = selenium.getText(time);
        guardXhr(selenium).click(link3);
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));
        assertTrue(selenium.isVisible(item3), "Item 3 was not displayed.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(item1);
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(item1);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testSwitchTypeNull() {
        guardXhr(selenium).click(link2);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(item2));

        guardXhr(selenium).click(link1);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(item1));
    }

    @Test
    @Templates(value = "plain")
    public void testSwitchTypeAjax() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.switchType, "client");

        guardXhr(selenium).click(link2);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(item2));

        guardNoRequest(selenium).click(link1);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(item1));
    }

    @Test
    public void testSwitchTypeServer() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.switchType, "server");

        guardXhr(selenium).click(link2);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(item2));

        guardHttp(selenium).click(link1);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(item1));
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(item1);
    }
}
