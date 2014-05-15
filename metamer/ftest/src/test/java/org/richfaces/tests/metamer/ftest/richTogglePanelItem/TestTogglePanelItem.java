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
package org.richfaces.tests.metamer.ftest.richTogglePanelItem;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTogglePanelItem/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTogglePanelItem extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=item1]")
    private GrapheneElement item1;
    @FindBy(css = "div[id$=item2]")
    private GrapheneElement item2;
    @FindBy(css = "div[id$=item3]")
    private GrapheneElement item3;
    @FindBy(css = "a[id$=tcLink1]")
    private GrapheneElement link1;
    @FindBy(css = "a[id$=tcLink2]")
    private GrapheneElement link2;
    @FindBy(css = "a[id$=tcLink3]")
    private GrapheneElement link3;
    @FindBy(css = "a[id$=tcLinkCustom]")
    private GrapheneElement linkCustom;

    private final Attributes<TogglePanelItemAttributes> togglePanelItemAttributes = getAttributes();

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
        return buildUrl(contextPath, "faces/components/richTogglePanelItem/simple.xhtml");
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        testDir(item1);
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertTrue(item1.isPresent(), "Item 1 should be present on the page.");
        assertTrue(item2.isPresent(), "Item 2 should be present on the page.");
        assertTrue(item3.isPresent(), "Item 3 should be present on the page.");

        assertTrue(item1.isDisplayed(), "Item 1 should be visible.");
        assertFalse(item2.isDisplayed(), "Item 2 should not be visible.");
        assertFalse(item3.isDisplayed(), "Item 3 should not be visible.");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testLang(item1);
    }

    @Test(groups = "smoke")
    @IssueTracking("https://issues.jboss.org/browse/RF-10488")
    public void testName() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.name, "nameThatIsNotUsedInComponentControlInFirstLink");

        Graphene.guardAjax(link3).click();
        assertTrue(item3.isDisplayed(), "Item 3 should be visible.");

        Graphene.guardNoRequest(link1).click();// item will not change, componentControl is bound to old name
        assertTrue(item3.isDisplayed(), "Item 3 should be visible.");

        Graphene.guardAjax(linkCustom).click();
        assertTrue(item1.isDisplayed(), "Item 1 should be visible.");
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
        testFireEvent("enter", new Action() {
            @Override
            public void perform() {
                Graphene.guardAjax(link3).click();
                Graphene.guardAjax(link1).click();
            }
        });
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9895")
    public void testOnleave() {
        testFireEvent("leave", new Action() {
            @Override
            public void perform() {
                Graphene.guardAjax(link3).click();
            }
        });
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

        assertFalse(item1.isPresent(), "Tab should not be rendered when rendered=false.");
        assertTrue(item2.isDisplayed(), "Item 2 should be displayed when item 1 is not rendered.");

        Graphene.guardAjax(link3).click();
        assertTrue(item3.isDisplayed(), "Item 3 was not displayed.");
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
    @UseWithField(field = "switchType", valuesFrom = STRINGS, value = { "null", "ajax", "client", "server" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10488")
    public void testSwitchType() {
        togglePanelItemAttributes.set(TogglePanelItemAttributes.switchType, switchType);

        Graphene.guardAjax(link2).click();
        assertFalse(item1.isDisplayed(), "Item 1 should not be visible.");
        assertTrue(item2.isDisplayed(), "Item 2 should be visible.");
        assertFalse(item3.isDisplayed(), "Item 3 should not be visible.");

        getGuardedLink(link1).click();
        assertTrue(item1.isDisplayed(), "Item 1 should be visible.");
        assertFalse(item2.isDisplayed(), "Item 2 should not be visible.");
        assertFalse(item3.isDisplayed(), "Item 3 should not be visible.");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(item1);
    }
}
