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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Implementation of ListItemsFilterBuilder.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of ListItem
 */
public class ListItemsFilterBuilderImpl<T extends ListItem> implements ListItemsFilterBuilder<T> {

    protected List<Predicate<T>> filters = Lists.newArrayList();

    @Override
    public ListItemsFilterBuilder<T> addFilter(Predicate<T> filter) {
        ListItemsFilterBuilderImpl<T> copy = copy();
        copy.filters.add(filter);
        return copy;
    }

    @Override
    public Predicate<T> build() {
        Preconditions.checkArgument(!filters.isEmpty(), "No filters specified. Cannot create filter.");
        return new Predicate<T>() {
            @Override
            public boolean apply(T input) {
                //apply all filters
                for (Predicate<T> predicate : filters) {
                    if (!predicate.apply(input)) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            }
        };
    }

    protected ListItemsFilterBuilderImpl<T> copy() {
        ListItemsFilterBuilderImpl<T> copy = new ListItemsFilterBuilderImpl<T>();
        copy.filters.addAll(this.filters);
        return copy;
    }
}
