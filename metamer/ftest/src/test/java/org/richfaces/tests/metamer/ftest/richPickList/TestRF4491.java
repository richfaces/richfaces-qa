/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPickList;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.orderingList.SelectableListItem;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RegressionTest("https://issues.jboss.org/browse/RF-4491")
public class TestRF4491 extends AbstractWebDriverTest {

    private static final String DISABLED_CLASS = "rf-pick-opt-dis";

    @FindBy(css = "[id$=addAll]")
    private WebElement addAllButtonJSAPI;
    @FindBy(css = "[id$=list]")
    private RichFacesPickList list;
    @FindBy(css = "[id$=output]")
    private WebElement output;

    @Override
    public String getComponentTestPagePath() {
        return "richPickList/rf-4491.xhtml";
    }

    @Test(expectedExceptions = TimeoutException.class)
    public void testDisabledItemCannotBeSelected() {
        // following line click on the first disabled item and waits for the item to contain specific class marking the item as
        // selected, such waiting should lead to TimeoutException
        list.add(0);
    }

    @Test
    public void testDisabledItemsCannotBeAddedToTargetListByAddAllButton() {
        // check output
        assertEquals(output.getText(), "[]");
        // check source list items
        List<? extends SelectableListItem> items = list.advanced().getSourceList().getItems();
        assertEquals(items.size(), 5);
        for (int i = 0; i < 5; i++) {
            SelectableListItem item = items.get(i);
            if (i % 2 == 0) {// every odd element is disabled (the items starts with 'first' item)
                assertTrue(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
            } else {
                assertFalse(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
            }
        }

        // add all items by the addAll button
        Graphene.guardAjax(list).addAll();
        // check only disabled items are in the source list
        items = list.advanced().getSourceList().getItems();
        assertEquals(items.size(), 3);
        for (int i = 0; i < 3; i++) {
            SelectableListItem item = items.get(i);
            assertTrue(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
        }
        // check only enabled items are in the target list
        items = list.advanced().getTargetList().getItems();
        assertEquals(items.size(), 2);
        for (int i = 0; i < 2; i++) {
            SelectableListItem item = items.get(i);
            assertFalse(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
        }
        // check output
        assertEquals(output.getText(), "[second, fourth]");

        // remove all items by the removeAll button
        Graphene.guardAjax(list).removeAll();
        // check output
        assertEquals(output.getText(), "[]");
        // check source list
        items = list.advanced().getSourceList().getItems();
        assertEquals(items.size(), 5);
        for (int i = 0; i < 5; i++) {
            SelectableListItem item = items.get(i);
            if (i <= 2) {// first three items are disabled
                assertTrue(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
            } else {
                assertFalse(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
            }
        }
    }

    @Test
    public void testDisabledItemsCannotBeAddedWithJSAPI() {
        // add all items by the addAll JS API button
        Graphene.guardAjax(addAllButtonJSAPI).click();
        // check only disabled items are in the source list
        List<? extends SelectableListItem> items = list.advanced().getSourceList().getItems();
        assertEquals(items.size(), 3);
        for (int i = 0; i < 3; i++) {
            SelectableListItem item = items.get(i);
            assertTrue(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
        }
        // check only enabled items are in the target list
        items = list.advanced().getTargetList().getItems();
        assertEquals(items.size(), 2);
        for (int i = 0; i < 2; i++) {
            SelectableListItem item = items.get(i);
            assertFalse(item.getRootElement().getAttribute("class").contains(DISABLED_CLASS));
        }
        // check output
        assertEquals(output.getText(), "[second, fourth]");
    }
}
