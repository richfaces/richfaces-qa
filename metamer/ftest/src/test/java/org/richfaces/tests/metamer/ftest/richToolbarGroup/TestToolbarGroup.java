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
package org.richfaces.tests.metamer.ftest.richToolbarGroup;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemStyle;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.test.selenium.support.ui.AttributeContains;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.richToolbar.ToolbarPage;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richToolbarGroup/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @since 4.3.4.Final
 */
public class TestToolbarGroup extends AbstractWebDriverTest {

    private final Attributes<ToolbarGroupAttributes> toolbarGroupAttributes = getAttributes();

    @Page
    private ToolbarPage page;

    AttributeContains attrContains = AttributeContains.getInstance();

    private String[] separators = { "disc", "grid", "line", "square" };

    private By[] itemsBy = new By[]{ By.cssSelector("td[id$=createDocument_itm]"), By.cssSelector("td[id$=createFolder_itm]"),
        By.cssSelector("td[id$=copy_itm]"), By.cssSelector("td[id$=save_itm]"), By.cssSelector("td[id$=saveAs_itm]"),
        By.cssSelector("td[id$=saveAll_itm]") };

    @Inject
    @Use(empty = true)
    private String itemSeparator;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richToolbarGroup/simple.xhtml");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertTrue(new WebElementConditionFactory(page.getToolbar()).isPresent().apply(driver), "Toolbar should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getToolbar()).isVisible().apply(driver), "Toolbar should be visible.");
        assertFalse(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "No item separator should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getItemInput()).isPresent().apply(driver), "Input should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getItemInput()).isVisible().apply(driver), "Input should be visible.");
        assertTrue(new WebElementConditionFactory(page.getItemButton()).isPresent().apply(driver), "Button should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getItemButton()).isVisible().apply(driver), "Button should be visible.");
    }

    @Test
    public void testInitItems() {
        for (By itemBy : itemsBy) {
            assertTrue(new WebElementConditionFactory(driver.findElement(itemBy)).isPresent().apply(driver), "Item (" + itemBy + ") should be present on the page.");
            assertTrue(new WebElementConditionFactory(driver.findElement(itemBy)).isVisible().apply(driver), "Item (" + itemBy + ") should be visible.");
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9976")
    @Templates(value = "plain")
    public void testItemClass() {
        for (By itemBy : itemsBy) {
            testStyleClass(driver.findElement(itemBy), itemClass);
        }
    }

    @Test
    @Use(field = "itemSeparator", value = "separators")
    public void testItemSeparatorCorrect() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, itemSeparator);

        List<WebElement> separatorDivs = driver.findElements(By.cssSelector("div.rf-tb-sep-" + itemSeparator));

        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "Item separator should be present on the page.");
        assertEquals(page.getSeparators().size(), 5, "Number of separators.");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getIconByName(itemSeparator)).isPresent().apply(driver), "Item separator does not work correctly.");
        assertEquals(separatorDivs.size(), 5, "Number of separators.");
    }

    @Test
    public void testItemSeparatorNone() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "none");
        assertFalse(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "No item separator should be present on the page.");

        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "null");
        assertFalse(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "No item separator should be present on the page.");
    }

    @Test
    public void testItemSeparatorCustom() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "star");

        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "Item separator should be present on the page.");
        assertEquals(page.getSeparators().size(), 5, "Number of separators.");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getImgIcon()).isPresent().apply(driver), "Item separator do not work correctly.");
        assertEquals(page.getSeparatorsImages().size(), 5, "Number of separators.");

        assertTrue(page.getSeparator().getImgIcon().getAttribute("src").contains("star.png"),
            "Separator's image should link to picture star.png.");
    }

    @Test
    public void testItemSeparatorNonExisting() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "non-existing");

        assertTrue(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "Item separators should be present on the page.");
        assertEquals(page.getSeparators().size(), 5, "Number of separators.");
        assertTrue(new WebElementConditionFactory(page.getSeparator().getImgIcon()).isPresent().apply(driver), "Item separators do not work correctly.");
        assertEquals(page.getSeparatorsImages().size(), 5, "Number of separators.");

        assertTrue(page.getSeparator().getImgIcon().getAttribute("src").contains("non-existing"),
            "Separator's image should link to \"non-existing\".");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9976")
    @Templates(value = "plain")
    public void testItemStyle() {
        for (By itemBy : itemsBy) {
            testStyle(driver.findElement(itemBy), itemStyle);
        }
    }

    @Test
    public void testLocation() {
        assertFalse(attrContains.element(page.getItems().get(0)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");
        assertTrue(attrContains.element(page.getItems().get(6)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");

        toolbarGroupAttributes.set(ToolbarGroupAttributes.location, "right");

        assertTrue(attrContains.element(page.getItems().get(0)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the right.");
        assertFalse(attrContains.element(page.getItems().get(6)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the right.");

        toolbarGroupAttributes.set(ToolbarGroupAttributes.location, "wrong");

        assertFalse(attrContains.element(page.getItems().get(0)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");
        assertTrue(attrContains.element(page.getItems().get(6)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemclick() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemclick,
                new Actions(driver).click(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemdblclick() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemdblclick,
                new Actions(driver).doubleClick(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemkeydown() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.KEYDOWN, toolbarGroupAttributes, ToolbarGroupAttributes.onitemkeydown);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemkeypress() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.KEYPRESS, toolbarGroupAttributes, ToolbarGroupAttributes.onitemkeypress);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemkeyup() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.KEYUP, toolbarGroupAttributes, ToolbarGroupAttributes.onitemkeyup);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmousedown() {
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy), Event.MOUSEDOWN, toolbarGroupAttributes, ToolbarGroupAttributes.onitemmousedown);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmousemove() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemmousemove,
                new Actions(driver).moveToElement(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmouseout() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy),
                Event.MOUSEOUT, toolbarGroupAttributes, ToolbarGroupAttributes.onitemmouseout);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmouseover() {
        for (By itemBy : itemsBy) {
            testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemmouseover,
                new Actions(driver).moveToElement(driver.findElement(itemBy)).build());
        }
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemmouseup() {
        for (By itemBy : itemsBy) {
            testFireEventWithJS(driver.findElement(itemBy), Event.MOUSEUP, toolbarGroupAttributes, ToolbarGroupAttributes.onitemmouseup);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.rendered, Boolean.FALSE);

        assertTrue(new WebElementConditionFactory(page.getToolbar()).isPresent().apply(driver), "Toolbar should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getToolbar()).isVisible().apply(driver), "Toolbar should be visible.");
        assertFalse(new WebElementConditionFactory(page.getSeparator().getRoot()).isPresent().apply(driver), "No item separator should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getItemInput()).isPresent().apply(driver), "Input should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getItemInput()).isVisible().apply(driver), "Input should be visible.");
        assertTrue(new WebElementConditionFactory(page.getItemButton()).isPresent().apply(driver), "Button should be present on the page.");
        assertTrue(new WebElementConditionFactory(page.getItemButton()).isVisible().apply(driver), "Button should be visible.");

        assertFalse(page.getItemCreateDoc().isPresent(), "Item 'create document' should not be rendered.");
        assertFalse(page.getItemCreateFolder().isPresent(), "Item 'create folder' should not be rendered.");
        assertFalse(page.getItemCopy().isPresent(), "Item 'copy' should not be rendered.");
        assertFalse(page.getItemSave().isPresent(), "Item 'save' should not be rendered.");
        assertFalse(page.getItemSaveAs().isPresent(), "Item 'save as' should not be rendered.");
        assertFalse(page.getItemSaveAll().isPresent(), "Item 'save all' should not be rendered.");
    }

}
