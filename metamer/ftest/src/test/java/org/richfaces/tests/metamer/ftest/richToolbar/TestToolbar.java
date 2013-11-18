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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemStyle;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.toolbarAttributes;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richToolbar/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23119 $
 */
public class TestToolbar extends AbstractWebDriverTest {

    @Page
    private ToolbarPage page;

    private WebElement[] items;
    private String[] separators = { "disc", "grid", "line", "square" };

    private By[] itemsBy = new By[]{ By.cssSelector("td[id$=createDocument_itm]"), By.cssSelector("td[id$=createFolder_itm]"),
        By.cssSelector("td[id$=copy_itm]"), By.cssSelector("td[id$=save_itm]"), By.cssSelector("td[id$=saveAs_itm]"),
        By.cssSelector("td[id$=saveAll_itm]"), By.cssSelector("td[id$=input_itm]"), By.cssSelector("td[id$=button_itm]") };

    @Inject
    @Use(empty = true)
    private String itemSeparator;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richToolbar/simple.xhtml");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertTrue(new WebElementConditionFactory(page.getToolbar()).isPresent().apply(driver), "Toolbar should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getToolbar()).isVisible().apply(driver), "Toolbar should be visible.");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).not().isPresent().apply(driver),
            "Item separator should not be present on the page.");
        // just test of inverse logic could be applied as replace
        assertFalse(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver),
            "Item separator should not be present on the page.");
    }

    @Test
    public void testInitItems() {
        for (By itemBy : itemsBy) {
            assertTrue(new WebElementConditionFactory(driver.findElement(itemBy)).isPresent().apply(driver), "Item (" + itemBy + ") should be present on the page.");
            assertTrue(new WebElementConditionFactory(driver.findElement(itemBy)).isVisible().apply(driver), "Item (" + itemBy + ") should be visible.");
        }
    }

    @Test
    public void testHeight() {
        AttributeConditionFactory styleAttr = new WebElementConditionFactory(page.getToolbar()).attribute("style");
        assertTrue(styleAttr.isPresent().apply(driver),
            "Attribute style should be present.");
        assertTrue(styleAttr.contains("height: 28px").apply(driver), "Height in attribute style should be 28px");

        toolbarAttributes.set(ToolbarAttributes.height, "50px");
        assertTrue(styleAttr.contains("height: 50px").apply(driver), "Attribute style should have height 50px.");
    }

    @Test
    @Templates(value = "plain")
    public void testItemClass() {
        for (By itemBy : itemsBy) {
            testStyleClass(driver.findElement(itemBy), itemClass);
        }
    }

    @Test
    @Use(field = "itemSeparator", value = "separators")
    public void testItemSeparatorCorrect() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, itemSeparator);

        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "Item separator should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getIconByName(itemSeparator)).isPresent().apply(driver), "Item separator does not work correctly.");
    }

    @Test
    public void testItemSeparatorNone() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "none");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).not().isPresent().apply(driver), "Item separator should not be present on the page.");

        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "null");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).not().isPresent().apply(driver), "Item separator should not be present on the page.");
    }

    @Test
    public void testItemSeparatorCustom() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "star");

        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "Item separator should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getImgIcon()).isPresent().apply(driver), "Item separator does not work correctly.");

        assertTrue(page.getSeparator().getImgIcon().getAttribute("src").contains("star.png"),
            "Separator's image should link to picture star.png.");
    }

    @Test
    public void testItemSeparatorNonExisting() {
        toolbarAttributes.set(ToolbarAttributes.itemSeparator, "non-existing");

        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "Item separator should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getImgIcon()).isPresent().apply(driver), "Item separator does not work correctly.");

        assertTrue(new WebElementConditionFactory(page.getSeparator().getImgIcon()).attribute("src").contains("non-existing").apply(driver),
            "Separator's image should link to \"non-existing\".");
    }

    @Test
    @Templates(value = "plain")
    public void testItemStyle() {
        for (By itemBy : itemsBy) {
            testStyle(driver.findElement(itemBy), itemStyle);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemclick() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarAttributes, ToolbarAttributes.onitemclick,
                new Actions(driver).click(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemdblclick() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarAttributes, ToolbarAttributes.onitemdblclick,
                new Actions(driver).doubleClick(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemkeydown() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.KEYDOWN, toolbarAttributes, ToolbarAttributes.onitemkeydown);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemkeypress() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.KEYPRESS, toolbarAttributes, ToolbarAttributes.onitemkeypress);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemkeyup() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.KEYUP, toolbarAttributes, ToolbarAttributes.onitemkeyup);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmousedown() {
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy), Event.MOUSEDOWN, toolbarAttributes, ToolbarAttributes.onitemmousedown);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmousemove() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmousemove,
                new Actions(driver).moveToElement(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmouseout() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.MOUSEOUT, toolbarAttributes, ToolbarAttributes.onitemmouseout);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmouseover() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarAttributes, ToolbarAttributes.onitemmouseover,
                new Actions(driver).moveToElement(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmouseup() {
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy), Event.MOUSEUP, toolbarAttributes, ToolbarAttributes.onitemmouseup);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        toolbarAttributes.set(ToolbarAttributes.rendered, Boolean.FALSE);
        assertTrue(new WebElementConditionFactory(page.getToolbar()).not().isPresent().apply(driver), "Toolbar should not be rendered when rendered=false.");
        assertFalse(new WebElementConditionFactory(page.getToolbar()).isPresent().apply(driver), "Toolbar should not be rendered when rendered=false.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        super.testStyle(page.getToolbar());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        super.testStyleClass(page.getToolbar());
    }

    @Test
    public void testWidth() {
        String styleAttr = page.getToolbar().getAttribute("style");
        assertTrue(styleAttr.contains("width: 100%"),
            "Attribute style should have 100% width when it is not set.");

        toolbarAttributes.set(ToolbarAttributes.width, "500px");
         styleAttr = page.getToolbar().getAttribute("style");
        assertTrue(styleAttr.contains("width: 500px"), "Attribute style should have width 500px.");
    }

}
