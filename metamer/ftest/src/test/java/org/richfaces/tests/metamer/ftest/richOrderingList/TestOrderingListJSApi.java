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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.bean.rich.RichOrderingListBean;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestOrderingListJSApi extends AbstractOrderingListTest {

    @FindBy
    private WebElement add;
    @FindBy
    private WebElement getOrderedElements;
    @FindBy
    private WebElement getOrderedKeys;
    @FindBy
    private WebElement getSelected;
    @FindBy
    private WebElement isSelected;
    @FindBy
    private WebElement moveDown;
    @FindBy
    private WebElement moveUp;
    @FindBy
    private WebElement moveFirst;
    @FindBy
    private WebElement moveLast;
    @FindBy
    private WebElement remove;
    @FindBy
    private WebElement selectAll;
    @FindBy
    private WebElement selectItem;
    @FindBy
    private WebElement unSelectAll;
    @FindBy
    private WebElement unSelectItem;

    @FindBy(css = "[id$='value']")
    private TextInputComponentImpl output;

    private void assertOutputValue(Object value) {
        assertEquals(output.getStringValue(), value.toString());
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/jsApi.xhtml");
    }

    @Test
    public void testAdd() {
        add.click();// adds new item to list

        // workaround for https://github.com/richwidgets/richwidgets/issues/101
        orderingList.select(1);

        assertEquals(orderingList.advanced().getItemsElements().size(), RichOrderingListBean.LIST_SIZE + 1);
    }

    @Test
    public void testGetOrderedElements() {
        getOrderedElements.click();// returns number of items
        assertOutputValue(7);
    }

    @Test
    public void testGetOrderedKeys() {
        getOrderedKeys.click();
        assertOutputValue("Montgomery,Juneau,Phoenix,Little Rock,Sacramento,Denver,Hartford");
    }

    @Test
    public void testGetSelected() {
        getSelected.click();// returns number of selected items
        assertOutputValue(0);
        orderingList.advanced().select(0, 1, 2, 3, 4);
        getSelected.click();
        assertOutputValue(5);
    }

    @Test
    public void testIsSelected() {
        isSelected.click();// returns true if first item is selected
        assertOutputValue(false);
        orderingList.select(0);
        isSelected.click();
        assertOutputValue(true);
    }

    @Test
    public void testMoveDown() {
        orderingList.select(0);
        String text = orderingList.advanced().getList().getItem(0).getText();
        moveDown.click();
        assertTrue(orderingList.advanced().getList().getItem(1).isSelected(), "Second item should be selected");
        assertEquals(orderingList.advanced().getList().getItem(1).getText(), text);

    }

    @Test
    public void testMoveUp() {
        orderingList.select(1);
        String text = orderingList.advanced().getList().getItem(1).getText();
        moveUp.click();
        assertTrue(orderingList.advanced().getList().getItem(0).isSelected(), "First item should be selected");
        assertEquals(orderingList.advanced().getList().getItem(0).getText(), text);
    }

    @Test
    public void testMoveFirst() {
        orderingList.select(ChoicePickerHelper.byIndex().last());
        String text = orderingList.advanced().getList().getItem(ChoicePickerHelper.byIndex().last()).getText();
        moveFirst.click();
        assertTrue(orderingList.advanced().getList().getItem(0).isSelected(), "First item should be selected");
        assertEquals(orderingList.advanced().getList().getItem(0).getText(), text);
    }

    @Test
    public void testMoveLast() {
        orderingList.select(0);
        String text = orderingList.advanced().getList().getItem(0).getText();
        moveLast.click();
        assertTrue(orderingList.advanced().getList().getItem(ChoicePickerHelper.byIndex().last()).isSelected(), "Last item should be selected");
        assertEquals(orderingList.advanced().getList().getItem(ChoicePickerHelper.byIndex().last()).getText(), text);
    }

    @Test
    public void testRemove() {
        remove.click();// removes first item
        assertEquals(orderingList.advanced().getList().size(), RichOrderingListBean.LIST_SIZE - 1);
    }

    @Test
    public void testSelectAll() {
        selectAll.click();
        assertEquals(orderingList.advanced().getList()
            .getItems(ChoicePickerHelper.byWebElement().attribute("class").contains("ui-selected")).size(),
            RichOrderingListBean.LIST_SIZE);
    }

    @Test
    public void testSelectItem() {
        selectItem.click();// selects first item
        assertTrue(orderingList.advanced().getList().getItem(0).isSelected(), "First item should be selected");
    }

    @Test
    public void testUnSelectAll() {
        testSelectAll();
        unSelectAll.click();
        assertTrue(orderingList.advanced().getList().getItems(ChoicePickerHelper.byWebElement().attribute("class").contains("ui-selected")).isEmpty(), "No item should be selected");
    }

    @Test
    public void testUnSelectItem() {
        testSelectItem();
        unSelectItem.click();// unselects first item
        assertFalse(orderingList.advanced().getList().getItem(0).isSelected(), "First item should be selected");
    }
}
