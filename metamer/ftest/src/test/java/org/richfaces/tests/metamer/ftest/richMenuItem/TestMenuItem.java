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
package org.richfaces.tests.metamer.ftest.richMenuItem;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.dropDownMenu.RichFacesDropDownMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richMenuItem/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestMenuItem extends AbstractWebDriverTest {

    private final Attributes<MenuItemAttributes> menuItemAttributes = getAttributes();

    @FindBy(css = "div[id$=menuItem1] > span.rf-ddm-itm-ic > span.rf-ddm-emptyIcon")
    private WebElement emptyIcon;
    @FindByJQuery(".rf-tb-itm:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenu;
    @FindBy(css = "div[id$=menu1]")
    private WebElement fileMenu;
    @FindBy(css = "div[id$=menu1] div.rf-ddm-lbl-dec")
    private WebElement fileMenuLabel;
    @FindBy(css = "div[id$=menu1_list]")
    private WebElement fileMenuList;
    @FindBy(css = "div[id$=menuItem1] > span.rf-ddm-itm-ic > img")
    private WebElement icon;
    @FindBy(css = "div[id$=menuItem1] > span.rf-ddm-itm-lbl ")
    private WebElement label;
    @FindBy(css = "div[id$=menuItem1]")
    private WebElement menuItem1;
    @FindByJQuery(value = ".rf-ddm-lbl-dec:eq(0)")
    private WebElement target1;

    private final Action openMenuAndClickOnTheMenuItemAction = new Action() {
        @Override
        public void perform() {
            openMenuAndClickOnTheItem();
        }
    };

    @Override
    public String getComponentTestPagePath() {
        return "richMenuItem/simple.xhtml";
    }

    private void openMenu() {
        fileDropDownMenu.advanced().show(target1);
        Graphene.waitGui().until().element(fileMenuLabel).is().visible();
    }

    private void openMenuAndClickOnTheItem() {
        openMenuAndClickOnTheItem(WaitRequestType.XHR);
    }

    private void openMenuAndClickOnTheItem(WaitRequestType type) {
        openMenu();
        MetamerPage.waitRequest(menuItem1, type).click();
    }

    @Test
    @CoversAttributes("action")
    public void testAction() {
        openMenu();
        MetamerPage.waitRequest(menuItem1, WaitRequestType.XHR).click();
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
    }

    @Test(groups = "smoke")
    @CoversAttributes("actionListener")
    public void testActionListener() {
        openMenu();
        MetamerPage.waitRequest(menuItem1, WaitRequestType.XHR).click();
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
    }

    @Test
    @CoversAttributes("bypassUpdates")
    public void testBypassUpdates() {
        menuItemAttributes.set(MenuItemAttributes.bypassUpdates, Boolean.TRUE);
        openMenuAndClickOnTheItem();
        getMetamerPage().assertBypassUpdatesPhasesCycle();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "action invoked");
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "action listener invoked");
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(openMenuAndClickOnTheMenuItemAction);
    }

    @Test
    @CoversAttributes("dir")
    @Templates("plain")
    public void testDir() {
        testDir(menuItem1);
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        menuItemAttributes.set(MenuItemAttributes.disabled, Boolean.TRUE);

        assertTrue(menuItem1.getAttribute("class").contains("rf-ddm-itm-dis"),
            "Menu item should have class 'rf-ddm-itm-dis'.");
        assertNotPresent(icon, "Icon should not be present.");
        assertPresent(emptyIcon, "Empty icon should be present.");
    }

    @Test(groups = "smoke")
    @CoversAttributes("execute")
    public void testExecute() {
        menuItemAttributes.set(MenuItemAttributes.execute, "@this executeChecker");
        openMenuAndClickOnTheItem();
        getMetamerPage().assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test
    @CoversAttributes("icon")
    @Templates(value = "plain")
    public void testIcon() {
        menuItemAttributes.set(MenuItemAttributes.icon, "star");
        assertTrue(icon.getAttribute("src").contains("star.png"),
            "Image's src attribute should contain 'star.png'.");

        menuItemAttributes.set(MenuItemAttributes.icon, "nonexisting");
        assertTrue(icon.getAttribute("src").contains("nonexisting"),
            "Image's src attribute should contain 'nonexisting'.");

        menuItemAttributes.set(MenuItemAttributes.icon, "null");
        assertNotPresent(icon, "Icon should not be present.");
        assertPresent(emptyIcon, "Empty icon should be present.");
    }

    @Test
    @CoversAttributes("iconDisabled")
    @Templates(value = "plain")
    public void testIconDisabled() {
        menuItemAttributes.set(MenuItemAttributes.disabled, Boolean.TRUE);

        assertNotPresent(icon, "Icon should not be present.");
        assertPresent(emptyIcon, "Empty icon should be present.");

        menuItemAttributes.set(MenuItemAttributes.iconDisabled, "star");
        openMenu();
        assertTrue(icon.getAttribute("src").contains("star.png"),
            "Image's src attribute should contain 'star.png'.");

        menuItemAttributes.set(MenuItemAttributes.iconDisabled, "nonexisting");
        openMenu();
        assertTrue(icon.getAttribute("src").contains("nonexisting"),
            "Image's src attribute should contain 'nonexisting'.");
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        menuItemAttributes.set(MenuItemAttributes.immediate, Boolean.TRUE);
        openMenuAndClickOnTheItem(WaitRequestType.XHR);
        getMetamerPage().assertImmediatePhasesCycle();
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "action invoked");
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "action listener invoked");
    }

    @Test(groups = "smoke")
    public void testInit() {
        // in some circumstances the menu is visible on page load (perhaps the mouse pointer ends up on the menu element from previous step),
        // this should move the mouse out of the menu and reload the page
        getMetamerPage().fullPageRefresh();

        assertPresent(fileMenu, "Drop down menu \"File\" should be present on the page");
        assertVisible(fileMenu, "Drop down menu \"File\" should be visible on the page");
        assertVisible(fileMenuLabel, "Label of menu should be visible on the page");
        assertEquals(fileMenuLabel.getText(), "File", "Label of the menu");

        assertNotVisible(fileMenuList, "Menu should not be expanded");
        Graphene.guardNoRequest(fileDropDownMenu).advanced().show(target1);
        assertVisible(fileMenuList, "Menu should be expanded");

        assertVisible(icon, "Icon of menu item should be visible on the page");
        assertTrue(icon.getAttribute("src").contains("create_doc.gif"), "Default menu item icon should be 'create_doc.gif'");

        assertVisible(label, "Label of menu item should be visible on the page");
        assertEquals(label.getText(), "New", "Label of the menu item");
    }

    @Test
    @CoversAttributes("label")
    @Templates("plain")
    public void testLabel() {
        testLabelChanges(menuItem1, menuItemAttributes, MenuItemAttributes.label, new Action() {
            @Override
            public void perform() {
                openMenu();
            }
        });
    }

    @Test
    @CoversAttributes("lang")
    @Templates("plain")
    public void testLang() {
        testLang(menuItem1);
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        menuItemAttributes.set(MenuItemAttributes.limitRender, Boolean.TRUE);
        openMenu();
        Graphene.guardAjax(MetamerPage.requestTimeNotChangesWaiting(menuItem1, 2L)).click();
    }

    @Test
    @CoversAttributes("mode")
    public void testMode() {
        menuItemAttributes.set(MenuItemAttributes.mode, "ajax");
        openMenuAndClickOnTheItem(WaitRequestType.XHR);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");

        menuItemAttributes.set(MenuItemAttributes.mode, "server");
        openMenuAndClickOnTheItem(WaitRequestType.HTTP);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        testFireEvent("beforedomupdate", openMenuAndClickOnTheMenuItemAction);
    }

    @Test
    @CoversAttributes("onbegin")
    public void testOnbegin() {
        testFireEvent("begin", openMenuAndClickOnTheMenuItemAction);
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, menuItem1);
    }

    @Test
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        testFireEvent("complete", openMenuAndClickOnTheMenuItemAction);
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, menuItem1);
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, menuItem1);
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, menuItem1);
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, menuItem1);
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, menuItem1);
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, menuItem1);
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, menuItem1);
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, menuItem1);
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, menuItem1);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        testRenderWithSwitchTypeOrMode(openMenuAndClickOnTheMenuItemAction);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        menuItemAttributes.set(MenuItemAttributes.rendered, Boolean.FALSE);
        assertNotPresent(menuItem1, "Menu item should not be rendered when rendered=false.");
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        testStatus(openMenuAndClickOnTheMenuItemAction);
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(menuItem1);
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(menuItem1);
    }

    @Test
    @CoversAttributes("title")
    @Templates("plain")
    public void testTitle() {
        testTitle(menuItem1);
    }

    @BeforeMethod(groups = "smoke")
    private void updateDropDownMenuInvoker() {
        fileDropDownMenu.advanced().setShowEvent(Event.MOUSEOVER);
    }
}
