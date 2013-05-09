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

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.list.common.SelectableList;
import org.richfaces.tests.page.fragments.impl.list.common.SelectableListItem;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of SelectableListItem
 * @see RichFacesOrderingList
 * @see RichFacesSimpleOrderingList
 */
public interface OrderingList<T extends SelectableListItem> extends SelectableList<T> {

    /**
     * Clicks the bottom button. This should move the selected items to the bottom.
     */
    OrderingList<T> bottom();

    @Override
    OrderingList<T> deselectItemsByIndex(Integer first, Integer... other);

    /**
     * Clicks the down button. This should move the selected items one place down.
     */
    OrderingList<T> down();

    WebElement getBottomButtonElement();

    WebElement getDownButtonElement();

    WebElement getTopButtonElement();

    WebElement getUpButtonElement();

    @Override
    OrderingList<T> selectItemsByIndex(Integer first, Integer... other);

    /**
     * Clicks the top button. This should move the selected items to the top.
     */
    OrderingList<T> top();

    /**
     * Clicks the up button. This should move the selected items one place up.
     */
    OrderingList<T> up();
}
