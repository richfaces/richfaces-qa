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
package org.richfaces.tests.metamer.ftest.richMenuItem;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.dropDownMenu.RichFacesDropDownMenu;
import org.richfaces.tests.page.fragments.impl.utils.Event;
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

    @FindByJQuery(".rf-tb-itm:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenu;
    @FindByJQuery(".rf-ddm-lbl-dec:eq(0)")
    private WebElement target1;
    @FindBy(css = "div[id$=menu1] div.rf-ddm-lbl-dec")
    private WebElement fileMenuLabel;
    @FindBy(css = "div[id$=menu1]")
    private WebElement fileMenu;
    @FindBy(css = "div[id$=menu1_list]")
    private WebElement fileMenuList;
    @FindBy(css = "div[id$=menuItem1] > span.rf-ddm-itm-lbl ")
    private WebElement label;
    @FindBy(css = "div[id$=menuItem1] > span.rf-ddm-itm-ic > img")
    private WebElement icon;
    @FindBy(css = "div[id$=menuItem1] > span.rf-ddm-itm-ic > span.rf-ddm-emptyIcon")
    private WebElement emptyIcon;
    @FindBy(css = "div[id$=menuItem1]")
    private WebElement menuItem1;
    @FindBy(css = "span[id$=status] > span")
    private WebElement statusText;
    @Page
    private MetamerPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMenuItem/simple.xhtml");
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
    public void testAction() {
        openMenu();
        MetamerPage.waitRequest(menuItem1, WaitRequestType.XHR).click();
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
    }

    @Test
    public void testActionListener() {
        openMenu();
        MetamerPage.waitRequest(menuItem1, WaitRequestType.XHR).click();
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
    }

    @Test
    public void testBypassUpdates() {
        menuItemAttributes.set(MenuItemAttributes.bypassUpdates, Boolean.TRUE);
        openMenuAndClickOnTheItem();
        page.assertBypassUpdatesPhasesCycle();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "action invoked");
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "action listener invoked");
    }

    @Test
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                openMenuAndClickOnTheItem();
            }
        });
    }

    @Test
    public void testDir() {
        testDir(menuItem1);
    }

    @Test
    public void testDisabled() {
        menuItemAttributes.set(MenuItemAttributes.disabled, Boolean.TRUE);

        assertTrue(menuItem1.getAttribute("class").contains("rf-ddm-itm-dis"),
            "Menu item should have class 'rf-ddm-itm-dis'.");
        assertNotPresent(icon, "Icon should not be present.");
        assertPresent(emptyIcon, "Empty icon should be present.");
    }

    @Test
    public void testExecute() {
        menuItemAttributes.set(MenuItemAttributes.execute, "@this executeChecker");
        openMenuAndClickOnTheItem();
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test
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
    public void testImmediate() {
        menuItemAttributes.set(MenuItemAttributes.immediate, Boolean.TRUE);
        openMenuAndClickOnTheItem(WaitRequestType.XHR);
        page.assertImmediatePhasesCycle();
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "action invoked");
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "action listener invoked");
    }

    @Test
    public void testInit() {
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
    public void testLabel() {
        testLabelChanges(menuItem1, menuItemAttributes, MenuItemAttributes.label, new Action() {
            @Override
            public void perform() {
                openMenu();
            }
        });
    }

    @Test
    public void testLang() {
        testAttributeLang(menuItem1);
    }

    @Test
    public void testLimitRender() {
        menuItemAttributes.set(MenuItemAttributes.limitRender, Boolean.TRUE);
        openMenu();
        Graphene.guardAjax(MetamerPage.requestTimeNotChangesWaiting(menuItem1, 2L)).click();
    }

    @Test
    public void testMode() {
        menuItemAttributes.set(MenuItemAttributes.mode, "ajax");
        openMenuAndClickOnTheItem(WaitRequestType.XHR);
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");

        menuItemAttributes.set(MenuItemAttributes.mode, "server");
        openMenuAndClickOnTheItem(WaitRequestType.HTTP);
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
    }

    @Test
    public void testOnbeforedomupdate() {
        testFireEvent("beforedomupdate", new Action() {
            @Override
            public void perform() {
                openMenuAndClickOnTheItem();
            }
        });
    }

    @Test
    public void testOnbegin() {
        testFireEvent("begin", new Action() {
            @Override
            public void perform() {
                openMenuAndClickOnTheItem();
            }
        });
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, menuItem1);
    }

    @Test
    public void testOncomplete() {
        testFireEvent("complete", new Action() {
            @Override
            public void perform() {
                openMenuAndClickOnTheItem();
            }
        });
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, menuItem1);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, menuItem1);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, menuItem1);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, menuItem1);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, menuItem1);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, menuItem1);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, menuItem1);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, menuItem1);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, menuItem1);
    }

    @Test
    public void testRendered() {
        menuItemAttributes.set(MenuItemAttributes.rendered, Boolean.FALSE);
        assertNotPresent(menuItem1, "Menu item should not be rendered when rendered=false.");
    }

    @Test
    public void testStatus() {
        testStatus(new Action() {
            @Override
            public void perform() {
                openMenuAndClickOnTheItem();
            }
        });
    }

    @Test
    public void testStyle() {
        testStyle(menuItem1);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(menuItem1);
    }

    @Test
    public void testTitle() {
        testTitle(menuItem1);
    }

    @BeforeMethod
    private void updateDropDownMenuInvoker() {
        fileDropDownMenu.advanced().setupShowEvent(Event.MOUSEOVER);
    }
}
