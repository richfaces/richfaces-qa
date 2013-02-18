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
package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.AbstractGrapheneTest.pjq;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.RequestTypeModelGuard.Model;

/**
 * Object representing ordering list and all its elements
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class OrderingList implements Model {

    private final JQueryLocator buttonBottom = jq("button.rf-ord-dn-bt");
    private final JQueryLocator buttonDown = jq("button.rf-ord-dn");
    private final JQueryLocator buttonTop = jq("button.rf-ord-up-tp");
    private final JQueryLocator buttonUp = jq("button.rf-ord-up");
    private final JQueryLocator header = pjq("thead.rf-ord-lst-hdr > tr");
    private final JQueryLocator item = pjq("tbody[id$=orderingListItems] > tr.rf-ord-opt");
    private final JQueryLocator itemSelected = pjq("tbody[id$=orderingListItems] > tr.rf-ord-opt.rf-ord-sel");
    private final JQueryLocator list = pjq("div[id$=orderingList] div.rf-ord-lst-dcrtn");
    private final JQueryLocator listArea = pjq("div[id$=orderingList] td:eq(0)");
    private final JQueryLocator orderingList = pjq("div[id$=orderingList]");
    private final JQueryLocator scrollableArea = pjq("div.rf-ord-lst-scrl");
    private int numberOfItems = -1;
    private int numberOfColumns = -1;
    private final GrapheneSelenium selenium;

    public OrderingList() {
        this(GrapheneSeleniumContext.getProxy());
    }

    public OrderingList(GrapheneSelenium selenium) {
        Validate.notNull(selenium);
        this.selenium = selenium;
    }

    public JQueryLocator getHeader() {
        return header;
    }

    public int getIndexOfSelectedItem() {
        if (!isItemSelected()) {
            throw new IllegalStateException("There is no selected item.");
        }
        return getNumberOfItems() - selenium.getCount(jq(itemSelected.getRawLocator() + " ~ tr")) - 1;
    }

    public JQueryLocator getItem(int index) {
        if (index < 0 || index >= getNumberOfItems()) {
            throw new IndexOutOfBoundsException("The index <" + index + "> is out of the range [0, "
                + getNumberOfItems() + "].");
        }
        return jq(item.getRawLocator() + ":eq(" + index + ")");
    }

    public JQueryLocator getItemColumn(int indexItem, int indexColumn) {
        if (indexColumn < 0 || indexColumn >= getNumberOfColumns()) {
            throw new IndexOutOfBoundsException("The index of column <" + indexColumn + "> is out of the range [0, "
                + getNumberOfColumns() + "]");
        }
        return jq(getItem(indexItem).getRawLocator() + " > .rf-ord-c:eq(" + indexColumn + ")");
    }

    public String getItemColumnValue(int indexItem, int indexColumn) {
        return selenium.getText(getItemColumn(indexItem, indexColumn));
    }

    public JQueryLocator getList() {
        return list;
    }

    public JQueryLocator getListArea() {
        return listArea;
    }

    public JQueryLocator getLocator() {
        return orderingList;
    }

    public int getNumberOfColumns() {
        if (numberOfColumns == -1) {
            numberOfColumns = selenium.getCount(jq(getItem(0).getRawLocator() + " > .rf-ord-c"));
        }
        return numberOfColumns;
    }

    public int getNumberOfItems() {
        if (numberOfItems == -1) {
            numberOfItems = selenium.getCount(item);
        }
        return numberOfItems;
    }

    public JQueryLocator getScrollableArea() {
        return scrollableArea;
    }

    public boolean isButtonBottomEnabled() {
        return isButtonEnabled(buttonBottom);
    }

    public boolean isButtonDownEnabled() {
        return isButtonEnabled(buttonDown);
    }

    public boolean isButtonTopEnabled() {
        return isButtonEnabled(buttonTop);
    }

    public boolean isButtonUpEnabled() {
        return isButtonEnabled(buttonUp);
    }

    public boolean isButtonBottomPresent() {
        return selenium.isElementPresent(buttonBottom);
    }

    public boolean isButtonDownPresent() {
        return selenium.isElementPresent(buttonDown);
    }

    public boolean isButtonTopPresent() {
        return selenium.isElementPresent(buttonTop);
    }

    public boolean isButtonUpPresent() {
        return selenium.isElementPresent(buttonUp);
    }

    public boolean isItemSelected() {
        return selenium.isElementPresent(itemSelected);
    }

    public boolean isOrderingListPresent() {
        return selenium.isElementPresent(orderingList);
    }

    public void moveBottom() {
        move(buttonBottom);
    }

    public void moveDown() {
        move(buttonDown);
    }

    public void moveTop() {
        move(buttonTop);
    }

    public void moveUp() {
        move(buttonUp);
    }

    public void selectItem(int index) {
        selenium.mouseDown(getItem(index));
        selenium.mouseUp(getItem(index));
        selenium.click(getItem(index));
    }

    private boolean isButtonEnabled(JQueryLocator button) {
        return !selenium.isElementPresent(jq(button.getRawLocator() + ".rf-ord-btn-dis"));
    }

    private void move(JQueryLocator button) {
        if (!selenium.isElementPresent(itemSelected)) {
            throw new IllegalStateException("No item is selected, so the moving action can't be proceeded.");
        }
        if (!isButtonEnabled(button)) {
            throw new IllegalStateException(
                "The moving action can't be proceeded because the moving button is disabled.");
        }
        selenium.click(button);
    }

}
