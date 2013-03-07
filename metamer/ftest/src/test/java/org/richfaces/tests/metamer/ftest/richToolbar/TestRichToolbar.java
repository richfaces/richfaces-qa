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
package org.richfaces.tests.metamer.ftest.richToolbar;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemStyle;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.toolbarAttributes;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richToolbar/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23119 $
 */
public class TestRichToolbar extends AbstractWebDriverTest {

    @Page
    ToolbarPage page;

    private WebElement[] items;
    private String[] separators = { "disc", "grid", "line", "square" };

    private By[] itemsBy = new By[] {By.cssSelector("td[id$=createDocument_itm]"), By.cssSelector("td[id$=createFolder_itm]"),
        By.cssSelector("td[id$=copy_itm]"), By.cssSelector("td[id$=save_itm]"), By.cssSelector("td[id$=saveAs_itm]"),
        By.cssSelector("td[id$=saveAll_itm]"), By.cssSelector("td[id$=input_itm]"), By.cssSelector("td[id$=button_itm]")};

    @Inject
    @Use(empty=true)
    private By itemBy;

    @Inject
    @Use(empty = true)
    private String itemSeparator;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richToolbar/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(Graphene.element(page.toolbar).isPresent().apply(driver), "Toolbar should be present on the page.");
        assertTrue(Graphene.element(page.toolbar).isVisible().apply(driver), "Toolbar should be visible.");
        assertTrue(Graphene.element(page.separator).not().isPresent().apply(driver),
            "Item separator should not be present on the page.");
        // just test of inverse logic could be applied as replace
        assertFalse(Graphene.element(page.separator).isPresent().apply(driver),
            "Item separator should not be present on the page.");
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testInitItems() {
        assertTrue(Graphene.element(itemBy).isPresent().apply(driver), "Item (" + itemBy + ") should be present on the page.");
        assertTrue(Graphene.element(itemBy).isVisible().apply(driver), "Item (" + itemBy + ") should be visible.");
    }

    @Test
    public void testHeight() {
        AttributeConditionFactory styleAttr = Graphene.element(page.toolbar).attribute("style");
        assertTrue(styleAttr.isPresent().apply(driver),
            "Attribute style should be present.");
        assertTrue(styleAttr.contains("height: 28px").apply(driver), "Height in attribute style should be 28px");

        toolbarAttributes.set(ToolbarAttributes.height, "50px");
        assertTrue(styleAttr.contains("height: 50px").apply(driver), "Attribute style should have height 50px.");
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testItemClass() {
        testStyleClass(page.toolbar.findElement(itemBy), itemClass);
    }

    @Test
    @Use(field = "itemSeparator", value = "separators")
    public void testItemSeparatorCorrect() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, itemSeparator);
        WebElement separatorDiv = page.separator.findElement(By.cssSelector("div.rf-tb-sep-" + itemSeparator));

        assertTrue(Graphene.element(page.separator).isPresent().apply(driver), "Item separator should be present on the page.");
        assertTrue(Graphene.element(separatorDiv).isPresent().apply(driver), "Item separator does not work correctly.");
    }

    @Test
    public void testItemSeparatorNone() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "none");
        assertTrue(Graphene.element(page.separator).not().isPresent().apply(driver), "Item separator should not be present on the page.");

        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "null");
        assertTrue(Graphene.element(page.separator).not().isPresent().apply(driver), "Item separator should not be present on the page.");
    }

    @Test
    public void testItemSeparatorCustom() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "star");

        assertTrue(Graphene.element(page.separator).isPresent().apply(driver), "Item separator should be present on the page.");
        assertTrue(Graphene.element(getSeparatorImg()).isPresent().apply(driver), "Item separator does not work correctly.");

        assertTrue(Graphene.attribute(getSeparatorImg(), "src").contains("star.png").apply(driver),
            "Separator's image should link to picture star.png.");
    }

    @Test
    public void testItemSeparatorNonExisting() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "non-existing");

        assertTrue(Graphene.element(page.separator).isPresent().apply(driver), "Item separator should be present on the page.");
        assertTrue(Graphene.element(getSeparatorImg()).isPresent().apply(driver), "Item separator does not work correctly.");

        assertTrue(Graphene.attribute(getSeparatorImg(), "src").contains("non-existing").apply(driver),
            "Separator's image should link to \"non-existing\".");
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testItemStyle() {
        super.testStyle(GrapheneProxy.getProxyForTarget(driver.findElement(By.cssSelector("td[id$=createDocument_itm]"))), itemStyle);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemclick() {
        Action click = new Actions(driver).click(page.toolbar.findElement(itemBy)).build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemclick, click);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemdblclick() {
        Action dblClick = new Actions(driver).doubleClick(page.toolbar.findElement(itemBy)).build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemdblclick, dblClick);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemkeydown() {
        Action keyDown = new Actions(driver).keyDown(page.toolbar.findElement(itemBy), Keys.ALT).build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemkeydown, keyDown);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemkeypress() {
        Action keyPress = new Actions(driver).moveToElement(page.toolbar.findElement(itemBy)).sendKeys("a").build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemkeypress, keyPress);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemkeyup() {
        Action keyup = new Actions(driver).keyUp(page.toolbar.findElement(itemBy), Keys.ALT).build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemkeyup, keyup);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemmousedown() {
        Action mouseDown = new Actions(driver).clickAndHold(page.toolbar.findElement(itemBy)).build();
        Action mouseDown2 = new Action() {
            @Override
            public void perform() {
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseDown(((Locatable) page.toolbar.findElement(itemBy)).getCoordinates());
            }
        };
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmousedown, mouseDown);
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmousedown, mouseDown2);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemmousemove() {
        Action mouseMove = new Actions(driver).moveToElement(page.toolbar.findElement(itemBy)).moveByOffset(3, 3).build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmousemove, mouseMove);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemmouseout() {
        Action mouseOut = new Actions(driver).moveToElement(page.toolbar.findElement(itemBy), 2, 2).moveByOffset(-4, -4).build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmouseout, mouseOut);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemmouseover() {
        Action mouseOver = new Actions(driver).moveToElement(page.toolbar.findElement(itemBy)).moveByOffset(1, 1).build();
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmouseover, mouseOver);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testOnitemmouseup() {
        Action mouseUp = new Action() {
            @Override
            public void perform() {
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseUp(((Locatable) page.toolbar.findElement(itemBy)).getCoordinates());
            }
        };
        testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmouseup, mouseUp);
    }

    @Test
    public void testRendered() {
        toolbarAttributes.set(ToolbarAttributes.rendered, Boolean.FALSE);
        assertTrue(Graphene.element(page.toolbar).not().isPresent().apply(driver), "Toolbar should not be rendered when rendered=false.");
        assertFalse(Graphene.element(page.toolbar).isPresent().apply(driver), "Toolbar should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        super.testStyle(page.toolbar);
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass(page.toolbar);
    }

    @Test
    public void testWidth() {
        AttributeConditionFactory styleAttr = Graphene.attribute(page.toolbar, "style");
        assertTrue(styleAttr.isPresent().apply(driver), "Attribute style should be present.");
        assertTrue(styleAttr.contains("width: 100%").apply(driver),
            "Attribute style should have 100% width when it is not set.");

        toolbarAttributes.set(ToolbarAttributes.width, "500px");
        styleAttr = Graphene.attribute(page.toolbar, "style");
        assertTrue(styleAttr.contains("width: 500px").apply(driver), "Attribute style should have width 500px.");
    }

    private WebElement getSeparatorImg() {
        return page.separator.findElement(By.tagName("img"));
    }
}
