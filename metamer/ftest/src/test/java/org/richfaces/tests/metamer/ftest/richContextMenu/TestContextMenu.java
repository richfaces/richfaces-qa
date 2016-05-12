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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.richfaces.component.Positioning;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test for rich:contextMenu component at faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @since 4.2.1.Final
 */
public class TestContextMenu extends AbstractWebDriverTest {

    private final Attributes<ContextMenuAttributes> contextMenuAttributes = getAttributes();

    private Integer delay;
    private Integer[] delays = { 1500, 2000, 2500 };
    @Page
    private ContextMenuSimplePage page;

    @Override
    public String getComponentTestPagePath() {
        return "richContextMenu/simple.xhtml";
    }

    public Locations getContextMenuLocationsWhenPosition(Positioning positioning) {
        contextMenuAttributes.set(ContextMenuAttributes.direction, positioning);
        page.getContextMenu().advanced().show(page.getTargetPanel2());
        Locations contextMenuLocations = Utils.getLocations(page.getContextMenuContent());
        page.getContextMenu().advanced().hide();
        return contextMenuLocations;
    }

    @Test
    @Templates("richAccordion")
    @RegressionTest("https://issues.jboss.org/browse/RF-12979")
    public void testContextMenuIsNotHiddenUnderAccordion() {
        // set direction to force menu to show in the needed position (under/over accordion)
        contextMenuAttributes.set(ContextMenuAttributes.direction, Positioning.topRight);
        // remove default padding to verify issue
        Utils.jQ("css('padding','0')", driver.findElement(By.cssSelector("[id$=padding]")));
        // check, the item cannot be clicked when it is under the accordion
        Graphene.guardHttp(page.getContextMenu()).selectItem("Open", page.getTargetPanel1());
        assertEquals(page.getOutput().getText(), "Open");

        // perform another check
        // remove default padding to verify issue
        Utils.jQ("css('padding','0')", driver.findElement(By.cssSelector("[id$=padding]")));
        // check, the item cannot be clicked when it is under the accordion
        Graphene.guardHttp(page.getContextMenu()).selectItem("Close", page.getTargetPanel1());
        assertEquals(page.getOutput().getText(), "Close");
    }

    @Test
    @CoversAttributes("dir")
    @Templates("plain")
    public void testDir() {
        updateShowAction();
        String expected = "rtl";
        contextMenuAttributes.set(ContextMenuAttributes.dir, expected);

        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String directionCSS = page.getContextMenu().advanced().getItemsElements().get(0).getCssValue("direction");
        assertEquals(directionCSS, expected, "The direction attribute was not applied correctly!");
    }

    @Test
    @CoversAttributes("direction")
    @Templates("plain")
    @UseWithField(field = "positioning", valuesFrom = FROM_ENUM, value = "")
    public void testDirection() {
        updateShowAction();
        testDirection(new ShowElementAndReturnAction() {
            @Override
            public WebElement perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                return page.getContextMenuContent();
            }
        });
    }

    @Test
    @CoversAttributes({ "attached",// whole test class uses @attached=true (default value)
        "disabled" })
    public void testDisabled() {
        contextMenuAttributes.set(ContextMenuAttributes.disabled, false);
        // the context menu content should be present
        Graphene.waitGui().until().element(page.getContextMenuContent()).is().present();

        updateShowAction();
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        assertTrue(page.getContextMenuContent().isDisplayed());

        contextMenuAttributes.set(ContextMenuAttributes.disabled, true);
        // the context menu content should not be present
        Graphene.waitGui().until().element(page.getContextMenuContent()).is().not().present();
        try {
            page.getContextMenu().advanced().show(page.getTargetPanel1());
            fail("The context menu cannot be displayed when disabled!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    @Test
    @CoversAttributes("hideDelay")
    @UseWithField(field = "delay", valuesFrom = FROM_FIELD, value = "delays")
    @Templates("plain")
    public void testHideDelay() {
        new MenuDelayTester().testHideDelay(page.getContextMenuRoot(), delay, new Event[] { Event.MOUSEOVER, Event.MOUSEOUT }, page.getContextMenuContent());
    }

    @Test
    @CoversAttributes("horizontalOffset")
    @Templates("plain")
    public void testHorizontalOffset() {
        updateShowAction();
        testHorizontalOffset(new ShowElementAndReturnAction() {
            @Override
            public WebElement perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                return page.getContextMenu().advanced().getMenuPopup();
            }
        });
    }

    @Test
    @CoversAttributes("lang")
    @Templates("plain")
    public void testLang() {
        updateShowAction();
        String langVal = "cs";
        contextMenuAttributes.set(ContextMenuAttributes.lang, langVal);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        assertEquals(page.getContextMenu().advanced().getLangAttribute(), langVal, "The lang attribute was not set correctly!");
    }

    @Test(groups = "smoke")
    @CoversAttributes("mode")
    public void testMode() {
        updateShowAction();
        // ajax
        contextMenuAttributes.set(ContextMenuAttributes.mode, "ajax");
        jsUtils.scrollToView(page.getTargetPanel1());
        guardAjax(page.getContextMenu()).selectItem("New", page.getTargetPanel1());
        assertEquals(page.getOutput().getText(), "New", "Menu action was not performed.");

        // server
        contextMenuAttributes.set(ContextMenuAttributes.mode, "server");
        jsUtils.scrollToView(page.getTargetPanel1());
        guardHttp(page.getContextMenu()).selectItem("Exit", page.getTargetPanel1());
        assertEquals(page.getOutput().getText(), "Exit", "Menu action was not performed.");

        // client
        contextMenuAttributes.set(ContextMenuAttributes.mode, "client");
        jsUtils.scrollToView(page.getTargetPanel1());
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        page.getContextMenu().selectItem("Open", page.getTargetPanel1());
    }

    @Test
    @Templates("plain")
    @RegressionTest("https://issues.jboss.org/browse/RF-12415")
    public void testNoResourceErrorPresent() {
        checkNoResourceErrorPresent(new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
            }
        });
    }

    @Test
    @CoversAttributes("onclick")
    @Templates("plain")
    public void testOnclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                page.getContextMenu().advanced().getItemsElements().get(2).click();
            }
        });
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates("plain")
    public void testOndblclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).doubleClick(page.getContextMenu().advanced().getItemsElements().get(2)).build().perform();
            }
        });
    }

    @Test
    @CoversAttributes("ongrouphide")
    public void testOngrouphide() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ongrouphide, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(3)).build().perform();
                waitGui().until().element(page.getGroupList()).is().visible();
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(0)).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    @Test
    @CoversAttributes("ongroupshow")
    public void testOngroupshow() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ongroupshow, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(3)).build().perform();
                waitGui().until().element(page.getGroupList()).is().visible();
            }
        });
    }

    @Test(groups = "smoke")
    @CoversAttributes("onhide")
    public void testOnhide() {
        updateShowAction();
        final String VALUE = "hide";
        // set onhide
        contextMenuAttributes.set(ContextMenuAttributes.onhide, "metamerEvents += \"" + VALUE + "\"");
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // lose focus >>> menu will disappear
        page.clickOnSecondPanel(driverType);
        // check whether the context menu isn't displayed
        page.waitUntilContextMenuHides();
        assertEquals(expectedReturnJS("return metamerEvents", VALUE), VALUE);
    }

    @Test
    @CoversAttributes("onitemclick")
    @Templates(value = "plain")
    public void testOnitemclick() {
        updateShowAction();
        testOnclick();
    }

    @Test
    @CoversAttributes("onkeydown")
    @RegressionTest("https://issues.jboss.org/browse/RF-12792")
    @Templates(value = "plain")
    public void testOnkeydown() {
        updateShowAction();
        testFireEventWithJS(page.getContextMenu().advanced().getItemsElements().get(0), contextMenuAttributes,
            ContextMenuAttributes.onkeydown);
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates("plain")
    public void testOnkeypress() {
        updateShowAction();
        testFireEventWithJS(page.getContextMenu().advanced().getMenuItemElements().get(0), contextMenuAttributes,
            ContextMenuAttributes.onkeypress);
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        updateShowAction();
        testFireEventWithJS(page.getContextMenu().advanced().getItemsElements().get(0), contextMenuAttributes,
            ContextMenuAttributes.onkeyup);
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseDown(((Locatable) page.getContextMenu().advanced().getItemsElements().get(2)).getCoordinates());
            }
        });
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates("plain")
    public void testOnmousemove() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmousemove, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(1)).build().perform();
            }
        });
    }

    @Test
    @Skip
    @CoversAttributes("onmouseout")
    @IssueTracking("https://issues.jboss.org/browse/RF-12854")
    @Templates(value = "plain")
    public void testOnmouseout() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(2)).build().perform();
                waitModel().until().element(page.getGroupList()).is().visible();
                new Actions(driver).moveToElement(page.getRequestTimeElement()).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        updateShowAction();
        testOnmousemove();
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                Coordinates coords = ((Locatable) page.getContextMenu().advanced().getItemsElements().get(2)).getCoordinates();
                mouse.mouseDown(coords);
                mouse.mouseUp(coords);
            }
        });
    }

    @Test(groups = "smoke")
    @CoversAttributes("onshow")
    public void testOnshow() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onshow, new Actions(driver).click(page.getTargetPanel1())
            .build());
    }

    @Test
    @CoversAttributes("popupWidth")
    @Templates(value = "plain")
    public void testPopupWidth() {
        updateShowAction();
        String minWidth = "333";
        contextMenuAttributes.set(ContextMenuAttributes.popupWidth, minWidth);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String style = page.getContextMenuContent().getCssValue("min-width");
        assertEquals(style, minWidth + "px");
    }

    @Test
    @CoversAttributes("rendered")
    @Templates("plain")
    public void testRendered() {
        Graphene.waitGui().withTimeout(1, TimeUnit.SECONDS).until().element(page.getContextMenuRoot()).is().present();
        contextMenuAttributes.set(ContextMenuAttributes.rendered, Boolean.FALSE);
        Graphene.waitGui().withTimeout(1, TimeUnit.SECONDS).until().element(page.getContextMenuRoot()).is().not().present();
    }

    @Test
    @CoversAttributes("showDelay")
    @UseWithField(field = "delay", valuesFrom = FROM_FIELD, value = "delays")
    @Templates("plain")
    public void testShowDelay() {
        updateShowAction();
        new MenuDelayTester().testShowDelay(page.getContextMenuRoot(), delay, Event.CLICK, page.getTargetPanel1());
    }

    @Test
    @CoversAttributes("showEvent")
    public void testShowEvent() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "mouseover");
        page.getContextMenu().advanced().setShowEvent(Event.MOUSEOVER);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setShowEvent(Event.CLICK);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
    }

    @Test
    @CoversAttributes("style")
    @Templates("plain")
    public void testStyle() {
        updateShowAction();
        String color = "yellow";
        String styleVal = "background-color: " + color + ";";
        contextMenuAttributes.set(ContextMenuAttributes.style, styleVal);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String backgroundColor = page.getContextMenu().advanced().getMenuPopup().getCssValue("background-color");
        // webdriver retrieves the color in rgba format
        assertEquals(ContextMenuSimplePage.trimTheRGBAColor(backgroundColor), "rgba(255,255,0,1)",
            "The style was not applied correctly!");
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(page.getContextMenu().advanced().getMenuPopup());
    }

    @Test(groups = "smoke")
    public void testSubMenuOpening() {
        updateShowAction();

        Graphene.guardHttp(page.getContextMenu().expandGroup("Save As...", page.getTargetPanel1()))
            .selectItem("Save All");
        assertEquals(page.getOutput().getText(), "Save All");
    }

    @Test
    @CoversAttributes("target")
    public void testTarget() {
        updateShowAction();
        // contextMenu element is present always. Check if is displayed
        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel2");
        assertFalse(page.getContextMenuContent().isDisplayed());
        page.getContextMenu().advanced().show(page.getTargetPanel2());

        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel1");
        assertFalse(page.getContextMenuContent().isDisplayed());
        page.getContextMenu().advanced().show(page.getTargetPanel1());
    }

    @Test
    @CoversAttributes("targetSelector")
    public void testTargetSelector() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.targetSelector, "div[id*=targetPanel]");

        page.getContextMenu().advanced().show(page.getTargetPanel1());
        page.getContextMenu().advanced().hide();
    }

    @Test
    @CoversAttributes("title")
    @Templates("plain")
    public void testTitle() {
        updateShowAction();
        String titleVal = "test title";
        contextMenuAttributes.set(ContextMenuAttributes.title, titleVal);
        assertEquals(page.getContextMenu().advanced().getMenuPopup().getAttribute("title"), titleVal);
    }

    @Test
    @CoversAttributes("verticalOffset")
    @Templates("plain")
    public void testVerticalOffset() {
        updateShowAction();
        testVerticalOffset(new ShowElementAndReturnAction() {
            @Override
            public WebElement perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                return page.getContextMenu().advanced().getMenuPopup();
            }
        });
    }

    private void updateShowAction() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setShowEvent(Event.CLICK);
    }
}
