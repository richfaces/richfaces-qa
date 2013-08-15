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
package org.richfaces.tests.page.fragments.impl.list;

import java.util.List;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.MultipleChoicePicker;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type extending ListItem
 */
public interface ListComponent<T extends ListItem> {

    /**
     * @param index
     * @return item at index or null
     */
    T getItem(int index);

    /**
     * @param text
     * @return first item which matches given text or null
     */
    T getItem(String text);

    /**
     * Returns item found by @picker.
     * @param picker for picking from the items
     * @return found item or null
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByIndexChoicePicker
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker

     */
    T getItem(ChoicePicker picker);

    /**
     * @return all items in list
     */
    List<T> getItems();

    /**
     * @param picker for picking from list
     * @return all items that returns picker
     */
    List<T> getItems(MultipleChoicePicker picker);

    /**
     * @return size of the list
     */
    int size();
}
