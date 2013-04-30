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
package org.richfaces.tests.page.fragments.impl.list.ordering;

import java.util.List;
import org.jboss.arquillian.graphene.Graphene;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.list.AbstractListFragment;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of OrderingListItem
 */
public abstract class RichFacesOrderingList<T extends OrderingListItem> extends AbstractListFragment<T, OrderingListItems<T>> implements OrderingList<T> {

    @FindBy(css = "button.rf-ord-dn.rf-ord-btn")
    private WebElement downButtonElement;
    @FindBy(css = "button.rf-ord-up-tp.rf-ord-btn")
    private WebElement topButtonElement;
    @FindBy(css = "button.rf-ord-dn-bt.rf-ord-btn")
    private WebElement bottomButtonElement;
    @FindBy(css = "button.rf-ord-up.rf-ord-btn")
    private WebElement upButtonElement;
    @FindBy(className = "rf-ord-opt")
    private List<WebElement> items;
    @FindBy(className = "rf-ord-sel")
    private List<WebElement> selectedItems;
    @FindBy(css = "thead.rf-ord-lst-hdr > tr.rf-ord-hdr")
    private WebElement headerElement;
    @FindBy(className = "rf-ord-lst-scrl")
    private WebElement listAreaElement;
    @FindBy(className = "rf-ord-cptn")
    private WebElement captionElement;

    @Override
    public OrderingList<T> bottom() {
        bottomButtonElement.click();
        return this;
    }

    @Override
    public OrderingList<T> deselectItemsByIndex(Integer first, Integer... other) {
        getItemsByIndex(first, other).deselectAll();
        return this;
    }

    @Override
    public OrderingList<T> down() {
        downButtonElement.click();
        return this;
    }

    @Override
    public WebElement getBottomButtonElement() {
        return bottomButtonElement;
    }

    @Override
    public String getCaption() {
        if (Graphene.element(captionElement).isVisible().apply(driver)) {
            return captionElement.getText();
        }
        return "";
    }

    @Override
    public WebElement getDownButtonElement() {
        return downButtonElement;
    }

    @Override
    public WebElement getTopButtonElement() {
        return topButtonElement;
    }

    @Override
    public WebElement getHeaderElement() {
        return headerElement;
    }

    @Override
    public OrderingListItems<T> getItems() {
        return createItems(items);
    }

    private OrderingListItems<T> getItemsByIndex(Integer first, Integer... rest) {
        if (first == null) {
            throw new IllegalArgumentException("the index cannot be null");
        }
        OrderingListItems<T> items1 = getItems();
        OrderingListItems<T> items2 = new RichFacesOrderingListItems<T>();
        items2.add(items1.get(first));
        for (Integer i : rest) {
            if (i != null) {
                items2.add(items1.get(i));
            }
        }
        return items2;
    }

    public WebElement getListAreaElement() {
        return listAreaElement;
    }

    @Override
    public OrderingListItems<T> getSelectedItems() {
        return createItems(selectedItems);
    }

    @Override
    public WebElement getUpButtonElement() {
        return upButtonElement;
    }

    @Override
    protected OrderingListItems<T> instantiateListItems() {
        return new RichFacesOrderingListItems<T>();
    }

    @Override
    public OrderingList<T> selectItemsByIndex(Integer first, Integer... rest) {
        getItemsByIndex(first, rest).selectAll();
        return this;
    }

    @Override
    public OrderingList<T> top() {
        topButtonElement.click();
        return this;
    }

    @Override
    public OrderingList<T> up() {
        upButtonElement.click();
        return this;
    }
}
