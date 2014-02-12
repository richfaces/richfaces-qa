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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.orderingList.RichFacesOrderingList;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestOrderingListFragment extends AbstractWebDriverTest {

    @FindBy(css = "div.rf-ord")
    private RichFacesOrderingList orderingList;

    private static final int LIST_SIZE = 50;

    private List<String> getFirstXItemsNames(int x) {
        List<String> result = Lists.newArrayList();
        for (int i = 0; i < x; i++) {
            result.add(getItemText(i));
        }
        return result;
    }

    private String getItemText(int index) {
        return orderingList.advanced().getList().getItem(index).getText();
    }

    private List<String> getLastXItemsNames(int x) {
        List<String> result = Lists.newArrayList();
        int fromIndex = LIST_SIZE - x;
        int toIndex = LIST_SIZE;
        for (int i = fromIndex; i < toIndex; i++) {
            result.add(getItemText(i));
        }
        return result;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/simple.xhtml");
    }

    @Test
    @Templates("plain")
    public void testPutAfterFirst() {
        List<String> itemNamesBefore = getFirstXItemsNames(3);
        List<String> itemNamesExpected = Lists.newArrayList(itemNamesBefore.get(0), itemNamesBefore.get(2), itemNamesBefore.get(1));
        orderingList
            .select(ChoicePickerHelper.byIndex().index(2))
            .putItAfter(0);
        assertEquals(getFirstXItemsNames(3), itemNamesExpected);
    }

    @Test
    @Templates("plain")
    public void testPutAfterLast() {
        List<String> itemNamesBefore = getLastXItemsNames(3);
        List<String> itemNamesExpected = Lists.newArrayList(itemNamesBefore.get(1), itemNamesBefore.get(2), itemNamesBefore.get(0));
        orderingList
            .select(ChoicePickerHelper.byIndex().beforeLast(2))
            .putItAfter(ChoicePickerHelper.byIndex().last());
        assertEquals(getLastXItemsNames(3), itemNamesExpected);
    }

    @Test
    @Templates("plain")
    public void testPutBeforeFirst() {
        List<String> itemNamesBefore = getFirstXItemsNames(3);
        List<String> itemNamesExpected = Lists.newArrayList(itemNamesBefore.get(2), itemNamesBefore.get(0), itemNamesBefore.get(1));
        orderingList
            .select(2)
            .putItBefore(0);
        assertEquals(getFirstXItemsNames(3), itemNamesExpected);
    }

    @Test
    @Templates("plain")
    public void testPutBeforeLast() {
        List<String> itemNamesBefore = getLastXItemsNames(3);
        List<String> itemNamesExpected = Lists.newArrayList(itemNamesBefore.get(1), itemNamesBefore.get(0), itemNamesBefore.get(2));
        orderingList
            .select(LIST_SIZE - 3)
            .putItBefore(ChoicePickerHelper.byIndex().last());
        assertEquals(getLastXItemsNames(3), itemNamesExpected);
    }

    @Test
    @Templates("plain")
    public void testPutDenverAfterFirst() {
        List<String> itemNamesBefore = getFirstXItemsNames(3);
        List<String> itemNamesExpected = Lists.newArrayList(itemNamesBefore.get(0), "Denver", itemNamesBefore.get(1), itemNamesBefore.get(2));
        orderingList
            .select(ChoicePickerHelper.byVisibleText().contains("Denver"))
            .putItAfter(0);
        assertEquals(getFirstXItemsNames(4), itemNamesExpected);
    }

    @Test
    @Templates("plain")
    public void testPutDenverAfterSacramento() {// no operation
        List<String> itemNamesBefore = getFirstXItemsNames(6);
        orderingList
            .select(ChoicePickerHelper.byVisibleText().contains("Denver"))
            .putItAfter(ChoicePickerHelper.byVisibleText().contains("Sacramento"));
        assertEquals(getFirstXItemsNames(6), itemNamesBefore);
    }

    @Test
    @Templates("plain")
    public void testPutDenverBeforeFirst() {
        List<String> itemNamesBefore = getFirstXItemsNames(3);
        List<String> itemNamesExpected = Lists.newArrayList("Denver", itemNamesBefore.get(0), itemNamesBefore.get(1), itemNamesBefore.get(2));
        orderingList
            .select(ChoicePickerHelper.byVisibleText().contains("Denver"))
            .putItBefore(0);
        assertEquals(getFirstXItemsNames(4), itemNamesExpected);
    }

    @Test
    @Templates("plain")
    public void testPutDenverBeforeSacramento() {
        List<String> itemNamesBefore = getFirstXItemsNames(6);
        List<String> itemNamesExpected = Lists.newArrayList(
            itemNamesBefore.get(0), itemNamesBefore.get(1), itemNamesBefore.get(2),
            itemNamesBefore.get(3), itemNamesBefore.get(5), itemNamesBefore.get(4));
        orderingList
            .select(ChoicePickerHelper.byVisibleText().contains("Denver"))
            .putItBefore(ChoicePickerHelper.byVisibleText().contains("Sacramento"));
        assertEquals(getFirstXItemsNames(6), itemNamesExpected);
    }

    @Test
    @Templates("plain")
    public void testBottom() {
        List<String> itemNamesBefore = getFirstXItemsNames(4);
        orderingList.advanced().select(0, 1).bottom();
        orderingList.advanced().select(ChoicePickerHelper.byIndex().last()).top();
        orderingList.advanced().select(ChoicePickerHelper.byIndex().last()).top();
        assertEquals(getFirstXItemsNames(4), itemNamesBefore);
    }

    @Test
    @Templates("plain")
    public void testDown() {
        List<String> itemNamesBefore = getFirstXItemsNames(4);
        orderingList.advanced().select(0).down(3);
        orderingList.advanced().select(0, 1, 2).down(1);
        assertEquals(getFirstXItemsNames(4), itemNamesBefore);
    }

    @Test
    @Templates("plain")
    public void testTop() {
        List<String> itemNamesBefore = getFirstXItemsNames(4);
        orderingList.advanced().select(1, 2).top();
        orderingList.advanced().select(2).top();
        assertEquals(getFirstXItemsNames(4), itemNamesBefore);
    }

    @Test
    @Templates("plain")
    public void testUp() {
        List<String> itemNamesBefore = getFirstXItemsNames(4);
        orderingList.advanced().select(1, 2, 3).up(1);
        orderingList.advanced().select(3).up(3);
        assertEquals(getFirstXItemsNames(4), itemNamesBefore);
    }
}
