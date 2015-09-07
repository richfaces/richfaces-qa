/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RegressionTest("https://issues.jboss.org/browse/RF-11842")
public class TestContextMenuTextSubstitutions extends AbstractWebDriverTest {

    private static final String GROUP_TEMPLATE = "actions for {0} -- {1}";
    private static final String MENU_ITEM_TEMPLATE = "{0} details";
    private static final String SUB_MENU_ITEM_TEMPLATE = "sub menu: {0} action";

    @FindBy(className = "labelCapital")
    private List<WebElement> capitalCells;
    @FindBy(css = "[id$='contextMenu']")
    private RichFacesContextMenu menu;
    @FindBy(css = "[id$='output']")
    private WebElement output;
    @FindBy(className = "labelState")
    private List<WebElement> stateCells;

    @Override
    public String getComponentTestPagePath() {
        return "richContextMenu/textSubstitutions.xhtml";
    }

    @BeforeMethod
    private void setUpMenuInvokeEventAndCheckInitialState() {
        assertEquals(stateCells.size(), 10);
        assertEquals(capitalCells.size(), 10);

        menu.advanced().setShowEvent(Event.CLICK);
    }

    @Test
    public void testMenuItemsSelection() {
        String optionText;
        WebElement menuItem;
        for (int rowIndex : new int[] { 0, 5 }) {// check in rows
            for (int itemIndex : new int[] { 0, 1, 3, 4 }) {// check each menu item (except group at index 2)
                // invoke menu on different column for odd/even rowIndex
                menu.advanced().show(rowIndex % 2 == 0 ? stateCells.get(rowIndex) : capitalCells.get(rowIndex));
                if (itemIndex > 2) {
                    // show sub menu when checking item from sub menu
                    menu.expandGroup(2);
                }
                menuItem = menu.advanced().getMenuItemElements().get(itemIndex);
                optionText = menuItem.getText();
                // select item
                Graphene.guardAjax(menuItem).click();
                menu.advanced().waitUntilIsNotVisible().perform();
                // check output
                assertEquals(output.getText(), optionText);
                waiting(500);// stabilization wait time
            }
        }
    }

    @Test
    public void testTextIsReplaced() {
        String stateText, capitalText;
        List<WebElement> menuItemElements;
        for (int rowIndex : new int[] { 0, 4, 9 }) {// check in rows
            // save replaced text
            stateText = stateCells.get(rowIndex).getText();
            capitalText = capitalCells.get(rowIndex).getText();

            // invoke menu on different column for odd/even rowIndex
            menu.advanced().show(rowIndex % 2 == 0 ? stateCells.get(rowIndex) : capitalCells.get(rowIndex));

            // check replaced texts
            menuItemElements = menu.advanced().getMenuItemElements();
            assertEquals(menuItemElements.size(), 5);
            assertEquals(menuItemElements.get(0).getText(), format(MENU_ITEM_TEMPLATE, stateText));
            assertEquals(menuItemElements.get(1).getText(), format(MENU_ITEM_TEMPLATE, capitalText));
            assertEquals(menuItemElements.get(2).getText(), format(GROUP_TEMPLATE, stateText, capitalText));
            // show sub menu
            menu.expandGroup(2);
            assertEquals(menuItemElements.get(3).getText(), format(SUB_MENU_ITEM_TEMPLATE, stateText));
            assertEquals(menuItemElements.get(4).getText(), format(SUB_MENU_ITEM_TEMPLATE, capitalText));

            // hide the menu
            getMetamerPage().getRequestTimeElement().click();
            menu.advanced().waitUntilIsNotVisible().perform();
        }
    }
}
