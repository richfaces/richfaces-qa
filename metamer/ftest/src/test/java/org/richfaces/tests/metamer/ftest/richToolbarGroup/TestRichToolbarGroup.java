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
 ******************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richToolbarGroup;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemStyle;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.toolbarGroupAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.AttributeContains;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.richToolbar.ToolbarPage;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richToolbarGroup/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichToolbarGroup extends AbstractWebDriverTest {

    @Page
    ToolbarPage page;

    AttributeContains attrContains = AttributeContains.getInstance();

    private String[] separators = { "disc", "grid", "line", "square" };

    private By[] itemsBy = new By[] {By.cssSelector("td[id$=createDocument_itm]"), By.cssSelector("td[id$=createFolder_itm]"),
        By.cssSelector("td[id$=copy_itm]"), By.cssSelector("td[id$=save_itm]"), By.cssSelector("td[id$=saveAs_itm]"),
        By.cssSelector("td[id$=saveAll_itm]")};

    @Inject
    @Use(empty = true)
    private String itemSeparator;

    @Inject
    @Use(empty=true)
    private By itemBy;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richToolbarGroup/simple.xhtml");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertTrue(Graphene.element(page.toolbar).isPresent().apply(driver), "Toolbar should be present on the page.");
        assertTrue(Graphene.element(page.toolbar).isVisible().apply(driver), "Toolbar should be visible.");
        assertFalse(Graphene.element(page.separator.root).isPresent().apply(driver), "No item separator should be present on the page.");
        assertTrue(Graphene.element(page.itemInput).isPresent().apply(driver), "Input should be present on the page.");
        assertTrue(Graphene.element(page.itemInput).isVisible().apply(driver), "Input should be visible.");
        assertTrue(Graphene.element(page.itemButton).isPresent().apply(driver), "Button should be present on the page.");
        assertTrue(Graphene.element(page.itemButton).isVisible().apply(driver), "Button should be visible.");
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    public void testInitItems() {
        assertTrue(Graphene.element(itemBy).isPresent().apply(driver), "Item (" + itemBy + ") should be present on the page.");
        assertTrue(Graphene.element(itemBy).isVisible().apply(driver), "Item (" + itemBy + ") should be visible.");
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @RegressionTest("https://issues.jboss.org/browse/RF-9976")
    @Templates(value = "plain")
    public void testItemClass() {
        testStyleClass(driver.findElement(itemBy) , itemClass);
    }

    @Test
    @Use(field = "itemSeparator", value = "separators")
    public void testItemSeparatorCorrect() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, itemSeparator);

        List<WebElement> separatorDivs = driver.findElements(By.cssSelector("div.rf-tb-sep-" + itemSeparator));

        assertTrue(Graphene.element(page.separator.root).isPresent().apply(driver), "Item separator should be present on the page.");
        assertEquals(page.separators.size(), 5, "Number of separators.");
        assertTrue(Graphene.element(page.separator.getIconByName(itemSeparator)).isPresent().apply(driver), "Item separator does not work correctly.");
        assertEquals(separatorDivs.size(), 5, "Number of separators.");
    }

    @Test
    public void testItemSeparatorNone() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "none");
        assertFalse(Graphene.element(page.separator.root).isPresent().apply(driver), "No item separator should be present on the page.");

        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "null");
        assertFalse(Graphene.element(page.separator.root).isPresent().apply(driver), "No item separator should be present on the page.");
    }

    @Test
    public void testItemSeparatorCustom() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "star");

        assertTrue(Graphene.element(page.separator.root).isPresent().apply(driver), "Item separator should be present on the page.");
        assertEquals(page.separators.size(), 5, "Number of separators.");
        assertTrue(Graphene.element(page.separator.imgIcon).isPresent().apply(driver), "Item separator do not work correctly.");
        assertEquals(page.separatorsImages.size(), 5, "Number of separators.");

        assertTrue(Graphene.attribute(page.separator.imgIcon, "src").contains("star.png").apply(driver),
            "Separator's image should link to picture star.png.");
    }

    @Test
    public void testItemSeparatorNonExisting() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.itemSeparator, "non-existing");

        assertTrue(Graphene.element(page.separator.root).isPresent().apply(driver), "Item separators should be present on the page.");
        assertEquals(page.separators.size(), 5, "Number of separators.");
        assertTrue(Graphene.element(page.separator.imgIcon).isPresent().apply(driver), "Item separators do not work correctly.");
        assertEquals(page.separatorsImages.size(), 5, "Number of separators.");

        assertTrue(Graphene.attribute(page.separator.imgIcon, "src").contains("non-existing").apply(driver),
            "Separator's image should link to \"non-existing\".");
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @RegressionTest("https://issues.jboss.org/browse/RF-9976")
    @Templates(value = "plain")
    public void testItemStyle() {
        testStyle(driver.findElement(itemBy), itemStyle);
    }

    @Test
    public void testLocation() {
        assertFalse(attrContains.element(page.items.get(0)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");
        assertTrue(attrContains.element(page.items.get(6)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");

        toolbarGroupAttributes.set(ToolbarGroupAttributes.location, "right");

        assertTrue(attrContains.element(page.items.get(0)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the right.");
        assertFalse(attrContains.element(page.items.get(6)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the right.");

        toolbarGroupAttributes.set(ToolbarGroupAttributes.location, "wrong");

        assertFalse(attrContains.element(page.items.get(0)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");
        assertTrue(attrContains.element(page.items.get(6)).attributeName("class").attributeValue("rf-tb-emp").apply(driver),
            "Toolbar group should  be located on the left.");
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemclick() {
        Action click = new Actions(driver).click(driver.findElement(itemBy)).build();
        testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemclick, click);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemdblclick() {
        Action dblClick = new Actions(driver).doubleClick(driver.findElement(itemBy)).build();
        testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemdblclick, dblClick);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemkeydown() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        testFireEventWithJS(driver.findElement(itemBy),
            Event.KEYDOWN, toolbarGroupAttributes, ToolbarGroupAttributes.onitemkeydown);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemkeypress() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        testFireEventWithJS(driver.findElement(itemBy),
            Event.KEYPRESS, toolbarGroupAttributes, ToolbarGroupAttributes.onitemkeypress);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemkeyup() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        testFireEventWithJS(driver.findElement(itemBy),
            Event.KEYUP, toolbarGroupAttributes, ToolbarGroupAttributes.onitemkeyup);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemmousedown() {
        Action mouseDown = new Actions(driver).clickAndHold(driver.findElement(itemBy)).build();
        testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemmousedown, mouseDown);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemmousemove() {
        Action mouseMove = new Actions(driver).moveToElement(driver.findElement(itemBy)).build();
        testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemmousemove, mouseMove);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemmouseout() {
        // TODO JJa 2013-03-14: Doesn't work for now with Action, rewrite if it changes
        testFireEventWithJS(driver.findElement(itemBy),
            Event.MOUSEOUT, toolbarGroupAttributes, ToolbarGroupAttributes.onitemmouseout);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemmouseover() {
        Action mouseOver = new Actions(driver).moveToElement(driver.findElement(itemBy)).build();
        testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemmouseover, mouseOver);
    }

    @Test
    @Use(field = "itemBy", value = "itemsBy")
    @Templates(value = "plain")
    public void testOnitemmouseup() {
        WebElement item = driver.findElement(itemBy);
        Action mouseUp = new Actions(driver).clickAndHold(item).release(item).build();
        testFireEvent(toolbarGroupAttributes, ToolbarGroupAttributes.onitemmouseup, mouseUp);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        toolbarGroupAttributes.set(ToolbarGroupAttributes.rendered, Boolean.FALSE);

        assertTrue(Graphene.element(page.toolbar).isPresent().apply(driver), "Toolbar should be present on the page.");
        assertTrue(Graphene.element(page.toolbar).isVisible().apply(driver), "Toolbar should be visible.");
        assertFalse(Graphene.element(page.separator.root).isPresent().apply(driver), "No item separator should be present on the page.");
        assertTrue(Graphene.element(page.itemInput).isPresent().apply(driver), "Input should be present on the page.");
        assertTrue(Graphene.element(page.itemInput).isVisible().apply(driver), "Input should be visible.");
        assertTrue(Graphene.element(page.itemButton).isPresent().apply(driver), "Button should be present on the page.");
        assertTrue(Graphene.element(page.itemButton).isVisible().apply(driver), "Button should be visible.");

        for (int i = 0; i < 6; i++) {
            // assertFalse(selenium.isElementPresent(items[i]), "Item " + (i + 1) + " should not be rendered.");
            assertFalse(Graphene.element(itemsBy[i]).isPresent().apply(driver), "Item " + (i + 1) + " should not be rendered.");
        }
    }

}
