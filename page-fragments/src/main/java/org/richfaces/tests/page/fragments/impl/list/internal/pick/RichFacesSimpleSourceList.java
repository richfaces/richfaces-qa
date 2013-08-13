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
package org.richfaces.tests.page.fragments.impl.list.internal.pick;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;

import org.richfaces.tests.page.fragments.impl.list.internal.common.SelectableListItems;

/**
 * Implementation of simple source list of r:pickList.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesSimpleSourceList extends RichFacesSourceList<RichFacesSimplePickListItem> {

    public SelectableListItems<RichFacesSimplePickListItem> deselectItemsByVisibleText(String first, String... other) {
        return getItemsByVisibleText(first, other).deselectAll();
    }

    private SelectableListItems<RichFacesSimplePickListItem> getItemsByVisibleText(String first, String... other) {
        if (first == null) {
            throw new IllegalArgumentException("the index cannot be null");
        }
        SelectableListItems<RichFacesSimplePickListItem> items = instantiateListItems();
        ArrayList<String> list = Lists.newArrayList(other);
        list.add(first);

        for (RichFacesSimplePickListItem item : getItems()) {
            for (String valueToSelect : list) {
                if (item.getText().equals(valueToSelect)) {
                    items.add(item);
                    list.remove(valueToSelect);
                    break;
                }
            }
        }
        return items;
    }

    @Override
    protected Class<RichFacesSimplePickListItem> getListItemType() {
        return RichFacesSimplePickListItem.class;
    }

    public SelectableListItems<RichFacesSimplePickListItem> selectItemsByVisibleText(String first, String... other) {
        return getItemsByVisibleText(first, other).selectAll();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Iterator<RichFacesSimplePickListItem> it = getItems().iterator(); it.hasNext();) {
            RichFacesSimplePickListItem item = it.next();
            sb.append(item.getText());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
