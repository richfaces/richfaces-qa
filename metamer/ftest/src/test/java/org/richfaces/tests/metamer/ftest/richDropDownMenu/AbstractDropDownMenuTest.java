/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.dropDownMenuAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.richContextMenu.ContextMenuSimplePage;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StopWatch;

import com.beust.jcommander.internal.Lists;

/**
 * Abstract test used for testing both drop down menus - top and side
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public abstract class AbstractDropDownMenuTest extends AbstractWebDriverTest {

    private void updateDropDownMenuInvoker() {
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced()
            .setInvoker(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().HOVER_INVOKER);
    }

    @Page
    DropDownMenuPage page;

    public void testInit() {
        page.fullPageRefresh();
        updateDropDownMenuInvoker();
        assertPresent(page.getFileMenu(), "Drop down menu \"File\" should be present on the page.");
        assertVisible(page.getFileMenu(), "Drop down menu \"File\" should be visible on the page.");

        assertPresent(page.getGroup(), "Menu group \"Save As...\" should be present on the page.");
        assertNotVisible(page.getGroup(), "Menu group \"Save As...\" should not be visible on the page.");

        assertNotVisible(page.getFileMenuList(), "Menu should not be expanded.");
        guardNoRequest(page.getFileDropDownMenu(driver.getCurrentUrl())).advanced().invoke(page.getTarget1());
        assertVisible(page.getFileMenuList(), "Menu should be expanded.");

        assertPresent(page.getGroup(), "Menu group \"Save As...\" should be present on the page.");
        assertVisible(page.getGroup(), "Menu group \"Save As...\" should be visible on the page.");

        assertPresent(page.getMenuItem41(), "Menu item \"Save\" should be present on the page.");
        assertNotVisible(page.getMenuItem41(), "Menu item \"Save\" should not be visible on the page.");

        assertNotVisible(page.getGroupList(), "Submenu should not be expanded.");
        guardNoRequest(
            new Actions(driver).moveToElement(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(3))
                .build()).perform();
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
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced()
            .setInvoker(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().RIGHT_CLICK_INVOKER);
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());

        dropDownMenuAttributes.set(DropDownMenuAttributes.showEvent, "hover");
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced()
            .setInvoker(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().HOVER_INVOKER);
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());

        dropDownMenuAttributes.set(DropDownMenuAttributes.showEvent, "click");
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced()
            .setInvoker(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().LEFT_CLICK_INVOKER);
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
    }

    public void testStyle() {
        updateDropDownMenuInvoker();
        String color = "yellow";
        String styleVal = "background-color: " + color + ";";
        dropDownMenuAttributes.set(DropDownMenuAttributes.style, styleVal);
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        String backgroundColor = page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getTopLvlElement()
            .getCssValue("background-color");
        // webdriver retrieves the color in rgba format
        assertEquals(ContextMenuSimplePage.trimTheRGBAColor(backgroundColor), "rgba(255,255,0,1)",
            "The style was not applied correctly!");
    }

    public void testStyleClass() {
        updateDropDownMenuInvoker();
        String styleClassVal = "test-style-class";
        dropDownMenuAttributes.set(DropDownMenuAttributes.styleClass, styleClassVal);
        String styleClass = page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getTopLvlElement()
            .getAttribute("class");
        assertTrue(styleClass.contains(styleClassVal));
    }

    public void testDir() {
        updateDropDownMenuInvoker();
        String expected = "rtl";
        dropDownMenuAttributes.set(DropDownMenuAttributes.dir, expected);

        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        String directionCSS = page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(0)
            .getCssValue("direction");
        assertEquals(directionCSS, expected, "The direction attribute was not applied correctly!");
    }

    public void testLang() {
        updateDropDownMenuInvoker();
        String langVal = "cs";
        dropDownMenuAttributes.set(DropDownMenuAttributes.lang, langVal);
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());

        assertEquals(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getLangAttribute(), langVal,
            "The lang attribute was not set correctly!");
    }

    public void testMode() {
        updateDropDownMenuInvoker();
        // ajax
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "ajax");
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        guardAjax(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(0)).click();
        assertEquals(page.getOutput().getText(), "New", "Menu action was not performed.");

        // server
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "server");
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        guardHttp(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(8)).click();
        assertEquals(page.getOutput().getText(), "Close", "Menu action was not performed.");

        // client
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "client");
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        guardNoRequest(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(0)).click();

        // null
        dropDownMenuAttributes.set(DropDownMenuAttributes.mode, "server");
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        guardHttp(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(8)).click();
        assertEquals(page.getOutput().getText(), "Close", "Menu action was not performed.");
    }

    public void testDisabled() {
        updateDropDownMenuInvoker();
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());

        dropDownMenuAttributes.set(DropDownMenuAttributes.disabled, true);

        try {
            page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
            fail("The context menu should not be invoked when disabled!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testHideDelay(int delay) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.hideDelay, delay);
        dropDownMenuAttributes.set(DropDownMenuAttributes.showDelay, 0);
        double tolerance = delay * 0.5;
        updateDropDownMenuInvoker();

        List<Integer> times = Lists.newArrayList(3);
        for (int i = 0; i < 3; i++) {
            page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
            times.add(StopWatch.watchTimeSpentInAction(new StopWatch.PerformableAction() {
                @Override
                public void perform() {
                    page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().dismiss();
                }
            }).inMillis().intValue());
        }
        double average = (times.get(0) + times.get(1) + times.get(2)) / 3;
        assertEquals(average, delay, tolerance, "The measured delay was far from set value.");

    }

    public void testPopupWidth() {
        updateDropDownMenuInvoker();
        String minWidth = "333";
        assertEquals(page.returnPopupWidth(minWidth, page.getFileDropDownMenu(driver.getCurrentUrl())), minWidth + "px");

        minWidth = "250";
        assertEquals(page.returnPopupWidth(minWidth, page.getFileDropDownMenu(driver.getCurrentUrl())), minWidth + "px");

        minWidth = "250";
        assertEquals(page.returnPopupWidth(minWidth, page.getFileDropDownMenu(driver.getCurrentUrl())), minWidth + "px");
    }

    public void testRendered() {
        updateDropDownMenuInvoker();
        dropDownMenuAttributes.set(DropDownMenuAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.getFileMenuLabel(), "The drop down menu for file can not be rendered!");
    }

    public void testShowDelay(int delay) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.hideDelay, 0);
        dropDownMenuAttributes.set(DropDownMenuAttributes.showDelay, delay);
        double tolerance = delay * 0.5;
        updateDropDownMenuInvoker();

        List<Integer> times = Lists.newArrayList(3);
        for (int i = 0; i < 3; i++) {
            times.add(StopWatch.watchTimeSpentInAction(new StopWatch.PerformableAction() {
                @Override
                public void perform() {
                    page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                }
            }).inMillis().intValue());
            page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().dismiss();
        }

        double average = (times.get(0) + times.get(1) + times.get(2)) / 3;
        assertEquals(average, delay, tolerance, "The measured delay was far from set value.");
    }

    public void testTitle() {
        updateDropDownMenuInvoker();
        String titleVal = "test title";
        dropDownMenuAttributes.set(DropDownMenuAttributes.title, titleVal);
        assertEquals(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getTopLvlElement().getAttribute("title"),
            titleVal);
    }

    public void testOnclick() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onclick, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(1).click();
            }
        });
    }

    public void testOndblclick() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                new Actions(driver).doubleClick(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(2))
                    .build().perform();
            }
        });
    }

    public void testOngrouphide() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.ongrouphide, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                new Actions(driver)
                    .moveToElement(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(3)).build()
                    .perform();
                waitGui().until().element(page.getGroupList()).is().visible();
                new Actions(driver).click(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(1))
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
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                new Actions(driver)
                    .moveToElement(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(3)).build()
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
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().dismiss();
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
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                new Actions(driver)
                    .keyDown(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(2), Keys.CONTROL)
                    .keyUp(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(2), Keys.CONTROL).build()
                    .perform();
            }
        });
    }

    public void testOnkeypress() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onkeypress, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                new Actions(driver).sendKeys("a").build().perform();
            }
        });
    }

    public void testOnkeyup() {
        testFireEventWithJS(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getMenuPopup(), dropDownMenuAttributes,
            DropDownMenuAttributes.onkeyup);
    }

    public void testOnmousedown() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseDown(((Locatable) page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(1))
                    .getCoordinates());
            }
        });
    }

    public void testOnmousemove() {
        testFireEventWithJS(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getMenuPopup(), dropDownMenuAttributes,
            DropDownMenuAttributes.onmousemove);
    }

    public void testOnmouseout() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                new Actions(driver)
                    .moveToElement(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(3)).build()
                    .perform();
                waitModel().until().element(page.getGroupList()).is().visible();
                new Actions(driver).moveToElement(page.getRequestTimeElement()).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    public void testOnmouseover() {
        testFireEventWithJS(page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getMenuPopup(), dropDownMenuAttributes,
            DropDownMenuAttributes.onmouseover);
    }

    public void testOnmouseup() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseUp(((Locatable) page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().getItems().get(1))
                    .getCoordinates());
            }
        });
    }

    public void testOnshow() {
        updateDropDownMenuInvoker();
        testFireEvent(dropDownMenuAttributes, DropDownMenuAttributes.onshow, new Action() {
            @Override
            public void perform() {
                page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
            }
        });
    }

    public void testHorizontalOffset() {
        updateDropDownMenuInvoker();
        int offset = 11;

        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        Point positionBefore = page.getDropDownMenuContent().getLocation();

        dropDownMenuAttributes.set(DropDownMenuAttributes.horizontalOffset, offset);
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());

        Point positionAfter = page.getDropDownMenuContent().getLocation();

        assertEquals(positionAfter.getX(), positionBefore.getX() + offset);
    }

    public void testVerticalOffset() {
        updateDropDownMenuInvoker();
        int offset = 11;

        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());
        Point positionBefore = page.getDropDownMenuContent().getLocation();

        dropDownMenuAttributes.set(DropDownMenuAttributes.verticalOffset, offset);
        page.getFileDropDownMenu(driver.getCurrentUrl()).advanced().invoke(page.getTarget1());

        Point positionAfter = page.getDropDownMenuContent().getLocation();

        assertEquals(positionAfter.getY(), positionBefore.getY() + offset);
    }
}
