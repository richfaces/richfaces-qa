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
package org.richfaces.tests.page.fragments.impl.list.internal.ordering;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.list.internal.common.AbstractSelectableListBase;
import org.richfaces.tests.page.fragments.impl.list.internal.common.OrderingListLayout;
import org.richfaces.tests.page.fragments.impl.list.internal.common.SelectableListItem;

/**
 * Abstract base for common ordering list.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of SelectableListItem
 * @param <L> type of OrderingListLayout
 * @see RichFacesOrderingList
 * @see RichFacesSimpleOrderingList
 */
public abstract class AbstractOrderingListBase<T extends SelectableListItem, L extends OrderingListLayout> extends AbstractSelectableListBase<T, L> implements OrderingList<T> {

    @Override
    public OrderingList<T> bottom() {
        getLayout().getBottomButtonElement().click();
        return this;
    }

    @Override
    public OrderingList<T> deselectItemsByIndex(Integer first, Integer... other) {
        getItemsByIndex(first, other).deselectAll();
        return this;
    }

    @Override
    public OrderingList<T> down() {
        getLayout().getDownButtonElement().click();
        return this;
    }

    @Override
    public WebElement getBottomButtonElement() {
        return getLayout().getBottomButtonElement();
    }

    @Override
    public WebElement getDownButtonElement() {
        return getLayout().getDownButtonElement();
    }

    @Override
    public WebElement getTopButtonElement() {
        return getLayout().getTopButtonElement();
    }

    @Override
    public WebElement getUpButtonElement() {
        return getLayout().getUpButtonElement();
    }

    @Override
    public OrderingList<T> selectItemsByIndex(Integer first, Integer... rest) {
        getItems().deselectAll();
        getItemsByIndex(first, rest).selectAll();
        return this;
    }

    @Override
    public OrderingList<T> top() {
        getLayout().getTopButtonElement().click();
        return this;
    }

    @Override
    public OrderingList<T> up() {
        getLayout().getUpButtonElement().click();
        return this;
    }
}
