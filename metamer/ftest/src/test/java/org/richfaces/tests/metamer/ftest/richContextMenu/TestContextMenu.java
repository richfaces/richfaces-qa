/**
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
 */
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.contextMenuAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.richfaces.component.Positioning;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StopWatch;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StopWatch.PerformableAction;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
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

    @Page
    private ContextMenuSimplePage page;
    @Inject
    @Use(empty = false)
    private Integer delay;
    @Inject
    @Use(empty = false)
    private Positioning positioning;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richContextMenu/simple.xhtml");
    }

    private void updateShowAction() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setInvoker(page.getContextMenu().advanced().LEFT_CLICK_INVOKER);
    }

    @Test
    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testHideDelay() {
        updateShowAction();
        double tolerance = delay * 0.5;
        // set hideDelay
        contextMenuAttributes.set(ContextMenuAttributes.hideDelay, delay);

        List<Integer> times = new ArrayList<Integer>(3);
        PerformableAction openSecondContextMenu = new StopWatch.PerformableAction() {
            @Override
            public void perform() {
                // blur >>> menu will disappear after delay
                page.clickOnSecondPanel(driverType);
                assertTrue(page.getContextMenuContent().isDisplayed());
                // wait until menu hides
                page.waitUntilContextMenuHides();
            }
        };

        for (int i = 0; i < 3; i++) {
            // show context menu
            page.clickOnFirstPanel(driverType);
            // check whether the context menu is displayed
            page.waitUntilContextMenuAppears();
            // save the time
            times.add(i, StopWatch.watchTimeSpentInAction(openSecondContextMenu).inMillis().intValue());
        }

        double average = (times.get(0) + times.get(1) + times.get(2)) / 3;
        assertEquals(average, delay, tolerance, "The measured delay was far from set value.");
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
    public void testDir() {
        updateShowAction();
        String expected = "rtl";
        contextMenuAttributes.set(ContextMenuAttributes.dir, expected);

        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        String directionCSS = page.getContextMenu().advanced().getItems().get(0).getCssValue("direction");
        assertEquals(directionCSS, expected, "The direction attribute was not applied correctly!");
    }

    @Test
    @Use(field = "positioning", enumeration = true)
    public void testDirection() {
        driver.manage().window().setSize(new Dimension(1280, 1024));// for stabilizing job in all templates
        int tolerance = 10;// px
        String msg = "The actual menu locations should be same as shifted default locations.";
        // setting up the right panel because then the context menu will fit on the page
        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel2");
        contextMenuAttributes.set(ContextMenuAttributes.direction, "bottomRight");

        Locations defaultLocations = page.getContextMenuLocations();// bottom right
        Locations actMenuLocation = page.getContextMenuLocationsWhenPosition(positioning);

        int defaultWidth = defaultLocations.getWidth();
        int defaultHeight = defaultLocations.getHeight();
        int shiftX = 0;
        int shiftY = 0;

        assertEquals(actMenuLocation.getHeight(), defaultHeight, tolerance, "Height of context menu should be same as before.");
        assertEquals(actMenuLocation.getWidth(), defaultWidth, tolerance, "Width of context menu should be same as before.");
        switch (positioning) {
            case auto:
            case bottomRight:
            case autoRight:
            case bottomAuto:
                // no shifting
                break;
            case autoLeft:
            case bottomLeft:
                shiftX = -defaultWidth;
                break;
            case topAuto:
            case topRight:
                shiftY = -defaultHeight;
                break;
            case topLeft:
                shiftX = -defaultWidth;
                shiftY = -defaultHeight;
                break;
            default:
                throw new IllegalArgumentException("Uknown switch " + positioning);
        }
        // the actual menu locations should be same as shifted default locations
        Utils.tolerantAssertLocationsEquals(defaultLocations.moveAllBy(shiftX, shiftY), actMenuLocation, tolerance, tolerance,
            msg);
    }

    @Test
    public void testLang() {
        updateShowAction();
        String langVal = "cs";
        contextMenuAttributes.set(ContextMenuAttributes.lang, langVal);
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());

        assertEquals(page.getContextMenu().advanced().getLangAttribute(), langVal, "The lang attribute was not set correctly!");
    }

    @Test
    public void testMode() {
        updateShowAction();
        // ajax
        contextMenuAttributes.set(ContextMenuAttributes.mode, "ajax");
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        guardAjax(page.getContextMenu().advanced().getItems().get(0)).click();
        assertEquals(page.getOutput().getText(), "Open", "Menu action was not performed.");

        // server
        contextMenuAttributes.set(ContextMenuAttributes.mode, "server");
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        guardHttp(page.getContextMenu().advanced().getItems().get(8)).click();
        assertEquals(page.getOutput().getText(), "Exit", "Menu action was not performed.");

        // client
        contextMenuAttributes.set(ContextMenuAttributes.mode, "client");
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        guardNoRequest(page.getContextMenu().advanced().getItems().get(0)).click();
    }

    @Test
    public void testDisabled() {
        updateShowAction();
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        assertTrue(page.getContextMenuContent().isDisplayed());

        contextMenuAttributes.set(ContextMenuAttributes.disabled, true);

        try {
            page.getContextMenu().advanced().invoke(page.getTargetPanel1());
            fail("The context menu should not be invoked when disabled!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    @Test
    public void testHorizontalOffset() {
        updateShowAction();
        int offset = 11;

        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        Point positionBefore = page.getContextMenuContent().getLocation();

        contextMenuAttributes.set(ContextMenuAttributes.horizontalOffset, offset);
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());

        Point positionAfter = page.getContextMenuContent().getLocation();

        assertEquals(positionAfter.getX(), positionBefore.getX() + offset);
    }

    @Test
    public void testStyle() {
        updateShowAction();
        String color = "yellow";
        String styleVal = "background-color: " + color + ";";
        contextMenuAttributes.set(ContextMenuAttributes.style, styleVal);
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        String backgroundColor = page.getContextMenuRoot().getCssValue("background-color");
        // webdriver retrieves the color in rgba format
        assertEquals(ContextMenuSimplePage.trimTheRGBAColor(backgroundColor), "rgba(255,255,0,1)",
            "The style was not applied correctly!");
    }

    @Test
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
        page.getContextMenu().advanced().invoke(page.getTargetPanel2());

        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel1");
        assertFalse(page.getContextMenuContent().isDisplayed());
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
    }

    @Test
    public void testTitle() {
        updateShowAction();
        String titleVal = "test title";
        contextMenuAttributes.set(ContextMenuAttributes.title, titleVal);
        assertEquals(page.getContextMenuRoot().getAttribute("title"), titleVal);
    }

    @Test
    public void testRendered() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.rendered, Boolean.FALSE);
        try {
            page.getContextMenu().advanced().invoke(page.getTargetPanel1());
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
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        String style = page.getContextMenuContent().getCssValue("min-width");
        assertEquals(style, minWidth + "px");
    }

    @Test
    public void testOnshow() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onshow, new Actions(driver).click(page.getTargetPanel1()).build());
    }

    @Test
    public void testOnclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                page.getContextMenu().advanced().getItems().get(1).click();
            }
        });
    }

    @Test
    public void testOndblclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                new Actions(driver).doubleClick(page.getContextMenu().advanced().getItems().get(1)).build().perform();
            }
        });
    }

    @Test
    public void testOnitemclick() {
        updateShowAction();
        testOnclick();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12792")
    public void testOnkeydown() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onkeydown, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                new Actions(driver).keyDown(page.getContextMenu().advanced().getItems().get(1), Keys.CONTROL)
                    .keyUp(page.getContextMenu().advanced().getItems().get(1), Keys.CONTROL).build().perform();
            }
        });
    }

    @Test(groups = "Future")
    // false negative
    public void testOnkeyup() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onkeyup, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                new Actions(driver).keyDown(page.getContextMenu().advanced().getItems().get(0), Keys.ALT)
                    .keyUp(page.getContextMenu().advanced().getItems().get(0), Keys.ALT).build().perform();
            }
        });
    }

    @Test
    public void testOnkeypress() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onkeypress, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
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
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseDown(((Locatable) page.getContextMenu().advanced().getItems().get(1)).getCoordinates());
            }
        });
    }

    @Test
    public void testOnmouseup() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseUp(((Locatable) page.getContextMenu().advanced().getItems().get(1)).getCoordinates());
            }
        });
    }

    @Test
    public void testOnmousemove() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmousemove, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItems().get(1)).build().perform();
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
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItems().get(2)).build().perform();
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
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItems().get(2)).build().perform();
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
                page.getContextMenu().advanced().invoke(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItems().get(2)).build().perform();
                waitGui().until().element(page.getGroupList()).is().visible();
                new Actions(driver).click(page.getContextMenu().advanced().getItems().get(1)).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    @Test
    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testShowDelay() {
        updateShowAction();
        double tolerance = delay * 0.5;

        List<Integer> times = new ArrayList<Integer>(3);

        for (int i = 0; i < 3; i++) {
            // save the time
            times.add(i, page.getActualShowDelay(delay));
            // show context menu
            page.clickOnSecondPanel(driverType);
            // check whether the context menu is displayed
            page.waitUntilContextMenuHides();
        }

        double average = (times.get(0) + times.get(1) + times.get(2)) / 3;
        assertEquals(average, delay, tolerance, "The measured delay was far from set value.");
    }

    @Test
    public void testShowEvent() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "hover");
        page.getContextMenu().advanced().setInvoker(page.getContextMenu().advanced().HOVER_INVOKER);
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());

        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setInvoker(page.getContextMenu().advanced().LEFT_CLICK_INVOKER);
        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
    }

    @Test
    public void testTargetSelector() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.targetSelector, "div[id*=targetPanel]");

        page.getContextMenu().advanced().invoke(page.getTargetPanel1());
        page.getContextMenu().advanced().dismiss();
    }
}
