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
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.dropDownMenu.RichFacesDropDownMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richContextMenu.ContextMenuSimplePage;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * Abstract test used for testing both drop down menus - top and side
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public abstract class AbstractDropDownMenuTest extends AbstractWebDriverTest {

    private final Attributes<DropDownMenuAttributes> dropDownMenuAttributes = getAttributes();
    protected Integer delay;
    protected Integer[] delays = { 1000, 1500, 1900 };

    private RichFacesDropDownMenu getCurrentMenu() {
        return page.getFileDropDownMenu(driver.getCurrentUrl());
    }

    public String returnPopupWidth(String minWidth, RichFacesDropDownMenu dropDownMenu) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.popupWidth, minWidth);
        dropDownMenu.advanced().show(page.getTarget1());
        return page.getDropDownMenuContent().getCssValue("min-width");
    }

    private void updateDropDownMenuInvoker() {
        getCurrentMenu().advanced().setupShowEvent(Event.MOUSEOVER);
    }

    @Page
    private DropDownMenuPage page;

    public void testInit() {
        page.fullPageRefresh();
        updateDropDownMenuInvoker();
        assertPresent(page.getFileMenu(), "Drop down menu \"File\" should be present on the page.");
        assertVisible(page.getFileMenu(), "Drop down menu \"File\" should be visible on the page.");

        assertPresent(page.getGroup(), "Menu group \"Save As...\" should be present on the page.");
        assertNotVisible(page.getGroup(), "Menu group \"Save As...\" should not be visible on the page.");

        assertNotVisible(page.getFileMenuList(), "Menu should not be expanded.");
        guardNoRequest(getCurrentMenu()).advanced().show(page.getTarget1());
        assertVisible(page.getFileMenuList(), "Menu should be expanded.");

        assertPresent(page.getGroup(), "Menu group \"Save As...\" should be present on the page.");
        assertVisible(page.getGroup(), "Menu group \"Save As...\" should be visible on the page.");

        assertPresent(page.getMenuItem41(), "Menu item \"Save\" should be present on the page.");
        assertNotVisible(page.getMenuItem41(), "Menu item \"Save\" should not be visible on the page.");

        assertNotVisible(page.getGroupList(), "Submenu should not be expanded.");
        guardNoRequest(
            new Actions(driver).moveToElement(
                getCurrentMenu().advanced().getItemsElements().get(3)).build())
            .perform();
        assertVisible(page.getGroupList(), "Submenu should be expanded.");

        assertPresent(page.getMenuItem41(), "Menu item \"Save\" should be present on the page.");
        assertVisible(page.getMenuItem41(), "Menu item \"Save\" should be visible on the page.");

        assertPresent(page.getIcon(), "Icon of menu group should not be present on the page.");

        assertPresent(page.getFileMenuLabelOriginal(), "Label of menu should be present on the page.");
        assertVisible(page.getFileMenuLabelOriginal(), "Label of menu should be visible on the page.");

        assertEquals(page.getFileMenuLabelOriginal().getText(), "File", "Label of the menu");
    }

    public void testShowEvent() {
        dropDownMenuAttributes.set(DropDownMenuAttributes.showEvent, "contextmenu");
        getCurrentMenu().advanced().setupShowEvent(Event.CONTEXTCLICK);
        getCurrentMenu().advanced().show(page.getTarget1());

        dropDownMenuAttributes.set(DropDownMenuAttributes.showEvent, "hover");
        getCurrentMenu().advanced().setupShowEvent(Event.MOUSEOVER);
        getCurrentMenu().advanced().show(page.getTarget1());

        dropDownMenuAttributes.set(DropDownMenuAttributes.showEvent, "click");
        getCurrentMenu().advanced().setupShowEvent(Event.CLICK);
        getCurrentMenu().advanced().show(page.getTarget1());
    }

    public void testStyle() {
        updateDropDownMenuInvoker();
        String color = "yellow";
        String styleVal = "background-color: " + color + ";";
        dropDownMenuAttributes.set(DropDownMenuAttributes.style, styleVal);
        getCurrentMenu().advanced().show(page.getTarget1());
        String backgroundColor = getCurrentMenu().advanced().getTopLevelElement()
            .getCssValue("background-color");
        // webdriver retrieves the color in rgba format
        assertEquals(ContextMenuSimplePage.trimTheRGBAColor(backgroundColor), "rgba(255,255,0,1)",
            "The style was not applied correctly!");
    }

    public void testStyleClass() {
        updateDropDownMenuInvoker();
        String styleClassVal = "test-style-class";
        dropDownMenuAttributes.set(DropDownMenuAttributes.styleClass, styleClassVal);
        String styleClass = getCurrentMenu().advanced().getTopLevelElement()
            .getAttribute("class");
        assertTrue(styleClass.contains(styleClassVal));
    }

    public void testDir() {
        updateDropDownMenuInvoker();
        String expected = "rtl";
        dropDownMenuAttributes.set(DropDownMenuAttributes.dir, expected);

        getCurrentMenu().advanced().show(page.getTarget1());
        String directionCSS = getCurrentMenu().advanced().getItemsElements().get(0)
            .getCssValue("direction");
        assertEquals(directionCSS, expected, "The direction attribute was not applied correctly!");
    }

    public void testLang() {
        updateDropDownMenuInvoker();
        String langVal = "cs";
        dropDownMenuAttributes.set(DropDownMenuAttributes.lang, langVal);
        getCurrentMenu().advanced().show(page.getTarget1());

        assertEquals(getCurrentMenu().advanced().getLangAttribute(), langVal,
            "The lang attribute was not set correctly!");
    }

    public void testMode() {
        updateDropDownMenuInvoker();
        // ajax
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "ajax");
        getCurrentMenu().advanced().show(page.getTarget1());
        guardAjax(getCurrentMenu().advanced().getItemsElements().get(0)).click();
        assertEquals(page.getOutput().getText(), "New", "Menu action was not performed.");

        // server
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "server");
        getCurrentMenu().advanced().show(page.getTarget1());
        guardHttp(getCurrentMenu().advanced().getItemsElements().get(8)).click();
        assertEquals(page.getOutput().getText(), "Close", "Menu action was not performed.");

        // client
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "client");
        getCurrentMenu().advanced().show(page.getTarget1());
        guardNoRequest(getCurrentMenu().advanced().getItemsElements().get(0)).click();

        // null
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "server");
        getCurrentMenu().advanced().show(page.getTarget1());
        guardHttp(getCurrentMenu().advanced().getItemsElements().get(8)).click();
        assertEquals(page.getOutput().getText(), "Close", "Menu action was not performed.");
    }

    public void testDisabled() {
        updateDropDownMenuInvoker();
        getCurrentMenu().advanced().show(page.getTarget1());

        dropDownMenuAttributes.set(DropDownMenuAttributes.disabled, true);

        try {
            getCurrentMenu().advanced().show(page.getTarget1());
            fail("The context menu should not be invoked when disabled!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    public void testHideDelay(int delay) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.showDelay, 0);
        updateDropDownMenuInvoker();
        getCurrentMenu().advanced().setupHideDelay(delay);

        testDelay(new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
            }
        }, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().hide();
            }
        }, "hideDelay", delay);
    }

    public void testPopupWidth() {
        updateDropDownMenuInvoker();
        String minWidth = "333";
        assertEquals(returnPopupWidth(minWidth, getCurrentMenu()), minWidth + "px");

        minWidth = "250";
        assertEquals(returnPopupWidth(minWidth, getCurrentMenu()), minWidth + "px");

        minWidth = "250";
        assertEquals(returnPopupWidth(minWidth, getCurrentMenu()), minWidth + "px");
    }

    public void testRendered() {
        updateDropDownMenuInvoker();
        dropDownMenuAttributes.set(DropDownMenuAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.getFileMenuLabel(), "The drop down menu for file can not be rendered!");
    }

    public void testShowDelay(int delay) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.hideDelay, 0);
        updateDropDownMenuInvoker();
        getCurrentMenu().advanced().setupShowDelay(delay);

        testDelay(new Action() {
            @Override
            public void perform() {
                try {
                    getCurrentMenu().advanced().hide();
                } catch (IllegalStateException ignored) {
                }
            }
        }, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
            }
        }, "showDelay", delay);
    }

    public void testTitle() {
        updateDropDownMenuInvoker();
        String titleVal = "test title";
        dropDownMenuAttributes.set(DropDownMenuAttributes.title, titleVal);
        assertEquals(
            getCurrentMenu().advanced().getTopLevelElement().getAttribute("title"),
            titleVal);
    }

    public void testOnclick() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onclick, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                getCurrentMenu().advanced().getItemsElements().get(1).click();
            }
        });
    }

    public void testOndblclick() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                new Actions(driver)
                    .doubleClick(getCurrentMenu().advanced().getItemsElements().get(2))
                    .build().perform();
            }
        });
    }

    public void testOngrouphide() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.ongrouphide, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                new Actions(driver)
                    .moveToElement(
                        getCurrentMenu().advanced().getItemsElements().get(3)).build()
                    .perform();
                waitGui().until().element(page.getGroupList()).is().visible();
                new Actions(driver)
                    .click(getCurrentMenu().advanced().getItemsElements().get(1))
                    .build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    public void testOngroupshow() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.ongroupshow, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                new Actions(driver)
                    .moveToElement(
                        getCurrentMenu().advanced().getItemsElements().get(3)).build()
                    .perform();
                waitGui().until().element(page.getGroupList()).is().visible();
            }
        });
    }

    public void testOnhide() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onhide, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                getCurrentMenu().advanced().hide();
            }
        });
    }

    public void testOnitemclick() {
        updateDropDownMenuInvoker();
        testOnclick();
    }

    public void testOnkeydown() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onkeydown, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                new Actions(driver)
                    .keyDown(getCurrentMenu().advanced().getItemsElements().get(2),
                        Keys.CONTROL)
                    .keyUp(getCurrentMenu().advanced().getItemsElements().get(2),
                        Keys.CONTROL).build().perform();
            }
        });
    }

    public void testOnkeypress() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onkeypress, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                new Actions(driver).sendKeys("a").build().perform();
            }
        });
    }

    public void testOnkeyup() {
        testFireEventWithJS(getCurrentMenu().advanced().getMenuPopup(),
            dropDownMenuAttributes, DropDownMenuAttributes.onkeyup);
    }

    public void testOnmousedown() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseDown(((Locatable) getCurrentMenu().advanced()
                    .getItemsElements().get(1)).getCoordinates());
            }
        });
        new Actions(driver).release().perform();
    }

    public void testOnmousemove() {
        testFireEventWithJS(getCurrentMenu().advanced().getMenuPopup(),
            dropDownMenuAttributes, DropDownMenuAttributes.onmousemove);
    }

    public void testOnmouseout() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                new Actions(driver)
                    .moveToElement(
                        getCurrentMenu().advanced().getItemsElements().get(3)).build()
                    .perform();
                waitModel().until().element(page.getGroupList()).is().visible();
                new Actions(driver).moveToElement(page.getRequestTimeElement()).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    public void testOnmouseover() {
        testFireEventWithJS(getCurrentMenu().advanced().getMenuPopup(),
            dropDownMenuAttributes, DropDownMenuAttributes.onmouseover);
    }

    public void testOnmouseup() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                getCurrentMenu().advanced().show(page.getTarget1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                Coordinates coords = ((Locatable) getCurrentMenu().advanced()
                    .getItemsElements().get(1)).getCoordinates();
                mouse.mouseDown(coords);
                mouse.mouseUp(coords);
            }
        });
    }

    public void testOnshow() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onshow, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getTarget1()).perform();
//                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
            }
        });
        new Actions(driver).moveToElement(page.getRequestTimeElement()).perform();
    }

    public void testHorizontalOffset() {
        updateDropDownMenuInvoker();
        int offset = 11;

        getCurrentMenu().advanced().show(page.getTarget1());
        Point positionBefore = page.getDropDownMenuContent().getLocation();

        dropDownMenuAttributes.set(DropDownMenuAttributes.horizontalOffset, offset);
        getCurrentMenu().advanced().show(page.getTarget1());

        Point positionAfter = page.getDropDownMenuContent().getLocation();

        assertEquals(positionAfter.getX(), positionBefore.getX() + offset);
    }

    public void testVerticalOffset() {
        updateDropDownMenuInvoker();
        int offset = 11;

        getCurrentMenu().advanced().show(page.getTarget1());
        Point positionBefore = page.getDropDownMenuContent().getLocation();

        dropDownMenuAttributes.set(DropDownMenuAttributes.verticalOffset, offset);
        getCurrentMenu().advanced().show(page.getTarget1());

        Point positionAfter = page.getDropDownMenuContent().getLocation();

        assertEquals(positionAfter.getY(), positionBefore.getY() + offset);
    }
}
