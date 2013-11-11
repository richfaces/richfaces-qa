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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.list.ListItem;
import org.richfaces.tests.page.fragments.impl.list.RichFacesListItem;
import org.richfaces.tests.page.fragments.impl.orderingList.RichFacesOrderingList;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

/**
 * Abstract test case for pages faces/components/richOrderingList/
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractOrderingListTest extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=orderingList]")
    protected RichFacesOrderingList orderingList;
    @FindBy(css = "input[id$=submitButton]")
    protected WebElement submitButton;

    protected final Attributes<OrderingListAttributes> attributes = getAttributes();

    private TwoColumnListItem convertItem(ListItem item) {
        return convertItem(item.getRootElement());
    }

    private TwoColumnListItem convertItem(WebElement itemRoot) {
        return Graphene.createPageFragment(TwoColumnListItem.class, itemRoot);
    }

    protected void assertButtonDisabled(WebElement element, String buttonName) {
        assertFalse(element.isEnabled(), "The button [" + buttonName + "] should be disabled.");
    }

    protected void assertButtonEnabled(WebElement element, String buttonName) {
        assertTrue(element.isEnabled(), "The button [" + buttonName + "] should be enabled.");
    }

    protected void checkButtonsStateBottom() {
        assertButtonEnabled(orderingList.advanced().getBottomButtonElement(), "botom");
        assertButtonEnabled(orderingList.advanced().getDownButtonElement(), "down");
        assertButtonEnabled(orderingList.advanced().getTopButtonElement(), "top");
        assertButtonEnabled(orderingList.advanced().getUpButtonElement(), "up");
    }

    protected void checkButtonsStateMiddle() {
        assertButtonEnabled(orderingList.advanced().getBottomButtonElement(), "botom");
        assertButtonEnabled(orderingList.advanced().getDownButtonElement(), "down");
        assertButtonEnabled(orderingList.advanced().getTopButtonElement(), "top");
        assertButtonEnabled(orderingList.advanced().getUpButtonElement(), "up");
    }

    protected void checkButtonsStateTop() {
        assertButtonEnabled(orderingList.advanced().getBottomButtonElement(), "botom");
        assertButtonEnabled(orderingList.advanced().getDownButtonElement(), "down");
        assertButtonEnabled(orderingList.advanced().getTopButtonElement(), "top");
        assertButtonEnabled(orderingList.advanced().getUpButtonElement(), "up");
    }

    private void checkColumnValuesMoved(int indexA, String stateA, String cityA, int indexB, String stateB, String cityB) {
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexB + (int) Math.signum(indexA - indexB))).state(), stateB, "The rows weren't moved succesfully after moving.");
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexB + (int) Math.signum(indexA - indexB))).city(), cityB, "The rows weren't moved succesfully after moving.");
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexB)).state(), stateA, "The rows weren't moved succesfully after moving.");
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexB)).city(), cityA, "The rows weren't moved succesfully after moving.");
    }

    private void checkColumnValuesSwapped(int indexA, String stateA, String cityA, int indexB, String stateB, String cityB) {
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexA)).state(), stateB, "The rows weren't swapped succesfully after moving.");
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexA)).city(), cityB, "The rows weren't swapped succesfully after moving.");
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexB)).state(), stateA, "The rows weren't swapped succesfully after moving.");
        assertEquals(convertItem(orderingList.advanced().getList().getItem(indexB)).city(), cityA, "The rows weren't swapped succesfully after moving.");
    }

    private int getFirstSelectedItemIndex() {
        return Utils.getIndexOfElement(orderingList.advanced().getSelectedItemsElements().get(0));
    }

    protected void moveSelectedDown() {
        int beforeIndex = getFirstSelectedItemIndex();
        TwoColumnListItem itemA = convertItem(orderingList.advanced().getSelectedItemsElements().get(0));
        TwoColumnListItem itemB = convertItem(orderingList.advanced().getItemsElements().get(beforeIndex + 1));

        String stateA = itemA.state();
        String cityA = itemA.city();
        String stateB = itemB.state();
        String cityB = itemB.city();

        orderingList.advanced().getDownButtonElement().click();
        int afterIndex = getFirstSelectedItemIndex();
        assertEquals(afterIndex, beforeIndex + 1, "The index of selected item doesn't match.");
        checkColumnValuesSwapped(beforeIndex, stateA, cityA, afterIndex, stateB, cityB);
        if (afterIndex < orderingList.advanced().getItemsElements().size() - 1) {
            checkButtonsStateMiddle();
        } else {
            checkButtonsStateBottom();
        }
    }

    protected void moveSelectedToBottom() {
        int beforeIndex = getFirstSelectedItemIndex();
        TwoColumnListItem itemA = convertItem(orderingList.advanced().getSelectedItemsElements().get(0));
        TwoColumnListItem itemB = convertItem(orderingList.advanced().getList().getItem(ChoicePickerHelper.byIndex().last()));

        String stateA = itemA.state();
        String cityA = itemA.city();
        String stateB = itemB.state();
        String cityB = itemB.city();

        orderingList.advanced().getBottomButtonElement().click();
        int after = getFirstSelectedItemIndex();
        assertEquals(after, orderingList.advanced().getItemsElements().size() - 1, "The index of selected item doesn't match.");
        checkColumnValuesMoved(beforeIndex, stateA, cityA, orderingList.advanced().getItemsElements().size() - 1, stateB, cityB);
        checkButtonsStateBottom();
    }

    protected void moveSelectedToTop() {
        int beforeIndex = getFirstSelectedItemIndex();
        TwoColumnListItem itemA = convertItem(orderingList.advanced().getSelectedItemsElements().get(0));
        TwoColumnListItem itemB = convertItem(orderingList.advanced().getItemsElements().get(0));

        String stateA = itemA.state();
        String cityA = itemA.city();
        String stateB = itemB.state();
        String cityB = itemB.city();

        orderingList.advanced().getTopButtonElement().click();
        int afterIndex = getFirstSelectedItemIndex();
        assertEquals(afterIndex, 0, "The index of selected item doesn't match.");
        checkColumnValuesMoved(beforeIndex, stateA, cityA, afterIndex, stateB, cityB);
        checkButtonsStateTop();
    }

    protected void moveSelectedUp() {
        int beforeIndex = getFirstSelectedItemIndex();
        TwoColumnListItem itemA = convertItem(orderingList.advanced().getSelectedItemsElements().get(0));
        TwoColumnListItem itemB = convertItem(orderingList.advanced().getItemsElements().get(beforeIndex - 1));
        String stateA = itemA.state();
        String cityA = itemA.city();
        String stateB = itemB.state();
        String cityB = itemB.city();

        orderingList.advanced().getUpButtonElement().click();
        int afterIndex = getFirstSelectedItemIndex();
        assertEquals(afterIndex, beforeIndex - 1, "The index of selected item doesn't match.");
        checkColumnValuesSwapped(beforeIndex, stateA, cityA, afterIndex, stateB, cityB);
        if (afterIndex > 0) {
            checkButtonsStateMiddle();
        }
    }

    protected void submit() {
        MetamerPage.waitRequest(submitButton, WaitRequestType.HTTP).click();
    }

    public static class TwoColumnListItem extends RichFacesListItem {

        @FindBy(tagName = "td")
        private List<WebElement> columns;

        public String state() {
            return columns.get(0).getText().trim();
        }

        public String city() {
            return columns.get(1).getText().trim();
        }
    }
}
