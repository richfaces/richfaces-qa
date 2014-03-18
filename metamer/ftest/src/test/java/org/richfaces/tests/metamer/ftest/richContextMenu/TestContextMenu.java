/**
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
 */
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.ui.common.Positioning;
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

    @Page
    private ContextMenuSimplePage page;

    private Integer delay;
    private Integer[] delays = { 1500, 2000, 2500 };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richContextMenu/simple.xhtml");
    }

    public Locations getContextMenuLocationsWhenPosition(Positioning positioning) {
        contextMenuAttributes.set(ContextMenuAttributes.direction, positioning);
        page.getContextMenu().advanced().show(page.getTargetPanel2());
        Locations contextMenuLocations = Utils.getLocations(page.getContextMenuContent());
        page.getContextMenu().advanced().hide();
        return contextMenuLocations;
    }

    private void updateShowAction() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setupShowEvent(Event.CLICK);
    }

    @Test
    @UseWithField(field = "delay", valuesFrom = FROM_FIELD, value = "delays")
    public void testHideDelay() {
        updateShowAction();
        page.getContextMenu().advanced().setupHideDelay(delay);
        testDelay(new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                page.waitUntilContextMenuAppears();
            }
        }, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().setupTimeoutForPopupMenuToBeNotVisible(3000);
                page.getContextMenu().advanced().hide();
            }
        }, "hideDelay", delay);
    }

    @Test
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
    public void testVerticalOffset() {
        updateShowAction();
        int offset = 11;
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // get position before offset is set
        Point before = page.getContextMenuContent().getLocation();
        // set verticalOffset
        contextMenuAttributes.set(ContextMenuAttributes.verticalOffset, offset);
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // get position after offset is set
        Point after = page.getContextMenuContent().getLocation();
        // check offset
        assertEquals(before.getY(), after.getY() - offset);
    }

    @Test
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
    @Templates("plain")
    public void testLang() {
        updateShowAction();
        String langVal = "cs";
        contextMenuAttributes.set(ContextMenuAttributes.lang, langVal);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        assertEquals(page.getContextMenu().advanced().getLangAttribute(), langVal, "The lang attribute was not set correctly!");
    }

    @Test
    public void testMode() {
        updateShowAction();
        // ajax
        contextMenuAttributes.set(ContextMenuAttributes.mode, "ajax");
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        guardAjax(page.getContextMenu().advanced().getItemsElements().get(0)).click();
        assertEquals(page.getOutput().getText(), "Open", "Menu action was not performed.");

        // server
        contextMenuAttributes.set(ContextMenuAttributes.mode, "server");
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        guardHttp(page.getContextMenu().advanced().getItemsElements().get(8)).click();
        assertEquals(page.getOutput().getText(), "Exit", "Menu action was not performed.");

        // client
        contextMenuAttributes.set(ContextMenuAttributes.mode, "client");
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        guardNoRequest(page.getContextMenu().advanced().getItemsElements().get(0)).click();
    }

    @Test
    public void testDisabled() {
        updateShowAction();
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        assertTrue(page.getContextMenuContent().isDisplayed());

        contextMenuAttributes.set(ContextMenuAttributes.disabled, true);

        try {
            page.getContextMenu().advanced().show(page.getTargetPanel1());
            fail("The context menu should not be showd when disabled!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    @Test
    public void testHorizontalOffset() {
        updateShowAction();
        int offset = 11;

        page.getContextMenu().advanced().show(page.getTargetPanel1());
        Point positionBefore = page.getContextMenuContent().getLocation();

        contextMenuAttributes.set(ContextMenuAttributes.horizontalOffset, offset);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        Point positionAfter = page.getContextMenuContent().getLocation();

        assertEquals(positionAfter.getX(), positionBefore.getX() + offset);
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        updateShowAction();
        String color = "yellow";
        String styleVal = "background-color: " + color + ";";
        contextMenuAttributes.set(ContextMenuAttributes.style, styleVal);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String backgroundColor = page.getContextMenuRoot().getCssValue("background-color");
        // webdriver retrieves the color in rgba format
        assertEquals(ContextMenuSimplePage.trimTheRGBAColor(backgroundColor), "rgba(255,255,0,1)",
            "The style was not applied correctly!");
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        updateShowAction();
        String styleClassVal = "test-style-class";
        contextMenuAttributes.set(ContextMenuAttributes.styleClass, styleClassVal);
        String styleClass = page.getContextMenuRoot().getAttribute("class");
        assertTrue(styleClass.contains(styleClassVal));
    }

    @Test
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
    @Templates("plain")
    public void testTitle() {
        updateShowAction();
        String titleVal = "test title";
        contextMenuAttributes.set(ContextMenuAttributes.title, titleVal);
        assertEquals(page.getContextMenuRoot().getAttribute("title"), titleVal);
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.rendered, Boolean.FALSE);
        try {
            page.getContextMenu().advanced().show(page.getTargetPanel1());
            fail("The context menu should not be invoked when rendered == false!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    @Test
    public void testPopupWidth() {
        updateShowAction();
        String minWidth = "333";
        contextMenuAttributes.set(ContextMenuAttributes.popupWidth, minWidth);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String style = page.getContextMenuContent().getCssValue("min-width");
        assertEquals(style, minWidth + "px");
    }

    @Test
    public void testOnshow() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onshow, new Actions(driver).click(page.getTargetPanel1())
            .build());
    }

    @Test
    @Templates("plain")
    public void testOnclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                page.getContextMenu().advanced().getItemsElements().get(1).click();
            }
        });
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).doubleClick(page.getContextMenu().advanced().getItemsElements().get(1)).build().perform();
            }
        });
    }

    @Test
    public void testOnitemclick() {
        updateShowAction();
        testOnclick();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12792")
    public void testOnkeydown() {
        updateShowAction();
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        testFireEventWithJS(page.getContextMenu().advanced().getItemsElements().get(1), Event.KEYDOWN, contextMenuAttributes,
            ContextMenuAttributes.onkeydown);
    }

    @Test
    public void testOnkeyup() {
        updateShowAction();
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        testFireEventWithJS(page.getContextMenu().advanced().getItemsElements().get(1), Event.KEYUP, contextMenuAttributes,
            ContextMenuAttributes.onkeyup);
    }

    @Test
    @Templates("plain")
    public void testOnkeypress() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onkeypress, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).sendKeys("a").build().perform();
            }
        });
    }

    @Test
    public void testOnmousedown() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseDown(((Locatable) page.getContextMenu().advanced().getItemsElements().get(1)).getCoordinates());
            }
        });
        // release mouse button - necessary since Selenium 2.35
        new Actions(driver).release().perform();
    }

    @Test
    public void testOnmouseup() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                Coordinates coords = ((Locatable) page.getContextMenu().advanced().getItemsElements().get(1)).getCoordinates();
                mouse.mouseDown(coords);
                mouse.mouseUp(coords);
            }
        });
    }

    @Test
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

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12854")
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
    public void testOnmouseover() {
        updateShowAction();
        testOnmousemove();
    }

    @Test
    public void testOngroupshow() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ongroupshow, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(2)).build().perform();
                waitGui().until().element(page.getGroupList()).is().visible();
            }
        });
    }

    @Test
    public void testOngrouphide() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ongrouphide, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(2)).build().perform();
                waitGui().until().element(page.getGroupList()).is().visible();
                new Actions(driver).click(page.getContextMenu().advanced().getItemsElements().get(1)).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    @Test
    @UseWithField(field = "delay", valuesFrom = FROM_FIELD, value = "delays")
    public void testShowDelay() {
        updateShowAction();
        page.getContextMenu().advanced().setupShowDelay(delay);
        testDelay(new Action() {
            @Override
            public void perform() {
                try {
                    page.getContextMenu().advanced().hide();
                } catch (IllegalStateException ignored) {
                }
                page.waitUntilContextMenuHides();
            }
        }, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().setupTimeoutForPopupMenuToBeVisible(3000);
                page.getContextMenu().advanced().show(page.getTargetPanel1());
            }
        }, "showDelay", delay);
    }

    @Test
    public void testShowEvent() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "hover");
        page.getContextMenu().advanced().setupShowEvent(Event.MOUSEOVER);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setupShowEvent(Event.CLICK);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
    }

    @Test
    public void testTargetSelector() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.targetSelector, "div[id*=targetPanel]");

        page.getContextMenu().advanced().show(page.getTargetPanel1());
        page.getContextMenu().advanced().hide();
    }
}
