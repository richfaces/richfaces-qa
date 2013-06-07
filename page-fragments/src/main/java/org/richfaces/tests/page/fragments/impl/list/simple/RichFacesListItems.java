/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.list.simple;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;

import org.richfaces.tests.page.fragments.impl.list.ListItem;
import org.richfaces.tests.page.fragments.impl.list.ListItems;
import org.richfaces.tests.page.fragments.impl.list.ListItemsFilterBuilder;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of ListItem
 */
class RichFacesListItems<T extends ListItem> extends ArrayList<T> implements ListItems<T> {

    private static final long serialVersionUID = 1L;

    public RichFacesListItems() {
    }

    public RichFacesListItems(Collection<? extends T> c) {
        super(c);
    }

    public RichFacesListItems(Iterable<? extends T> it) {
        this.addAll(Lists.newArrayList(it));
    }

    @Override
    public int indexOf(Object o) {
        return ((ListItem) o).getIndex();
    }

    @Override
    public ListItems<T> filter(ListItemsFilterBuilder<T> builder) {
        return new RichFacesListItems<T>(Iterables.filter(this, builder.build()));
    }
}
