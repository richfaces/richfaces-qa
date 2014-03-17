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
package org.richfaces.tests.metamer.ftest.richTogglePanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTogglePanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTogglePanel extends AbstractWebDriverTest {

    private final Attributes<TogglePanelAttributes> togglePanelAttributes = getAttributes();

    @FindBy(css = "div[id$=richTogglePanel]")
    private GrapheneElement panel;
    @FindBy(css = "div[id$=item1]")
    private GrapheneElement item1;
    @FindBy(css = "div[id$=item2]")
    private GrapheneElement item2;
    @FindBy(css = "div[id$=item3]")
    private GrapheneElement item3;
    // toggle controls
    @FindBy(css = "a[id$=tcLink1]")
    private GrapheneElement tc1;
    @FindBy(css = "a[id$=tcLink2]")
    private GrapheneElement tc2;
    @FindBy(css = "a[id$=tcLink3]")
    private GrapheneElement tc3;
    @FindBy(css = "a[id$=tcFirst]")
    private GrapheneElement tcFirst;
    @FindBy(css = "a[id$=tcPrev]")
    private GrapheneElement tcPrev;
    @FindBy(css = "a[id$=tcNext]")
    private GrapheneElement tcNext;
    @FindBy(css = "a[id$=tcLast]")
    private GrapheneElement tcLast;
    @FindBy(css = "[id$=getItems]")
    private GrapheneElement getItems;
    @FindBy(css = "[id$=getItemsNames]")
    private GrapheneElement getItemsNames;
    @FindBy(css = "[id$=value]")
    private GrapheneElement jsApiOutput;

    @Inject
    @Use
    private String switchType;

    private GrapheneElement getGuardedLink(GrapheneElement button) {
        if ("null".equals(switchType) || "ajax".equals(switchType)) {
            return Graphene.guardAjax(button);
        } else if ("client".equals(switchType)) {
            return Graphene.guardNoRequest(button);
        } else {
            return Graphene.guardHttp(button);
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTogglePanel/simple.xhtml");
    }

    @Test(groups = "smoke")
    public void testActiveItem() {
        togglePanelAttributes.set(TogglePanelAttributes.activeItem, "item3");
        assertFalse(item1.isDisplayed(), "Item 1 should not be displayed");
        assertFalse(item2.isDisplayed(), "Item 2 should not be displayed");
        assertTrue(item3.isDisplayed(), "Item 3 should be displayed");

        togglePanelAttributes.set(TogglePanelAttributes.activeItem, "item2");
        assertFalse(item1.isDisplayed(), "Item 1 should not be displayed");
        assertTrue(item2.isDisplayed(), "Item 2 should be displayed");
        assertFalse(item3.isDisplayed(), "Item 3 should not be displayed");
    }

    @Test(groups = "smoke")
    public void testCycledSwitching() {
        togglePanelAttributes.set(TogglePanelAttributes.cycledSwitching, Boolean.FALSE);
        assertTrue(item1.isDisplayed(), "Item 1 should be displayed");
        Graphene.guardAjax(tcLast).click();
        assertTrue(item3.isDisplayed(), "Item 3 should be displayed");
        Graphene.guardNoRequest(tcNext).click();
        assertTrue(item3.isDisplayed(), "Item 3 should be displayed");

        togglePanelAttributes.set(TogglePanelAttributes.cycledSwitching, Boolean.TRUE);
        Graphene.guardAjax(tcFirst).click();
        assertTrue(item1.isDisplayed(), "Item 1 should be displayed");
        Graphene.guardAjax(tcPrev).click();
        assertTrue(item3.isDisplayed(), "Item 3 should be displayed");
        Graphene.guardAjax(tcNext).click();
        assertTrue(item1.isDisplayed(), "Item 1 should be displayed");
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        super.testDir(panel);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        togglePanelAttributes.set(TogglePanelAttributes.immediate, Boolean.TRUE);
        Graphene.guardAjax(tc3).click();
        assertTrue(item3.isDisplayed(), "Item 3 should be displayed");

        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: item1 -> item3");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertTrue(panel.isPresent(), "Toggle panel is not present on the page.");
        assertTrue(panel.isDisplayed(), "Toggle panel is not visible.");
        assertTrue(item1.isPresent(), "Item 1 is not present on the page.");
        assertTrue(item1.isDisplayed(), "Item 1 is not visible.");
        assertTrue(item2.isPresent(), "Item 2 is not present on the page.");
        assertFalse(item2.isDisplayed(), "Item 2 should not be visible.");
        assertTrue(item3.isPresent(), "Item 3 is not present on the page.");
        assertFalse(item3.isDisplayed(), "Item 3 should not be visible.");
    }

    @Test
    public void testItemChangeListener() {
        Graphene.guardAjax(tc3).click();
        assertTrue(item3.isDisplayed(), "Item 3 should be displayed");
        getMetamerPage().assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: item1 -> item3");
    }

    @Test
    public void testJSApiGetItems() {
        getItems.click();
        assertEquals(jsApiOutput.getAttribute("value"), "3");
    }

    @Test
    public void testJSApiGetItemsNames() {
        getItemsNames.click();
        assertEquals(jsApiOutput.getAttribute("value"), "item1,item2,item3");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testAttributeLang(panel);
    }

    @Test
    @Templates("plain")
    public void testOnbeforeitemchangeOnitemChange() {
        testRequestEventsBefore("beforeitemchange", "itemchange");
        Graphene.guardAjax(tc2).click();
        assertTrue(item2.isDisplayed(), "Item 2 should be displayed");

        testRequestEventsAfter("beforeitemchange", "itemchange");
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, panel);
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, panel);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, panel);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, panel);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, panel);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, panel);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, panel);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        togglePanelAttributes.set(TogglePanelAttributes.rendered, Boolean.FALSE);
        assertFalse(panel.isPresent(), "Toggle panel should not be rendered when rendered=false.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(panel);
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(panel);
    }

    @Test
    @Use(field = "switchType", strings = { "null", "ajax", "client", "server" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchType() {
        togglePanelAttributes.set(TogglePanelAttributes.switchType, switchType);
        getGuardedLink(tcNext).click();
        assertFalse(item1.isDisplayed(), "Item 1 should not be visible.");
        assertTrue(item2.isDisplayed(), "Item 2 should be visible.");
        assertFalse(item3.isDisplayed(), "Item 3 should not be visible.");

        getGuardedLink(tcPrev).click();
        assertTrue(item1.isDisplayed(), "Item 1 should be visible.");
        assertFalse(item2.isDisplayed(), "Item 2 should not be visible.");
        assertFalse(item3.isDisplayed(), "Item 3 should not be visible.");

        getGuardedLink(tcLast).click();
        assertFalse(item1.isDisplayed(), "Item 1 should not be visible.");
        assertFalse(item2.isDisplayed(), "Item 2 should not be visible.");
        assertTrue(item3.isDisplayed(), "Item 3 should be visible.");

        getGuardedLink(tcFirst).click();
        assertTrue(item1.isDisplayed(), "Item 1 should be visible.");
        assertFalse(item2.isDisplayed(), "Item 2 should not be visible.");
        assertFalse(item3.isDisplayed(), "Item 3 should not be visible.");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(panel);
    }
}
