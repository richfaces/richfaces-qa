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
package org.richfaces.tests.page.fragments.impl.list;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesListItemsFilterBuilder implements ListItemsFilterBuilder {

    private List<Predicate<ListItem>> filters = Lists.newArrayList();

    private RichFacesListItemsFilterBuilder(RichFacesListItemsFilterBuilder tfb) {
        this.filters = Lists.newArrayList(tfb.filters);
    }

    @Override
    public Predicate<ListItem> build() {
        Preconditions.checkArgument(!filters.isEmpty(), "No filters specified. Cannot create filter.");
        return new Predicate<ListItem>() {
            @Override
            public boolean apply(ListItem input) {
                //apply all filters
                for (Predicate<ListItem> predicate : filters) {
                    if (!predicate.apply(input)) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            }
        };
    }

    @Override
    public RichFacesListItemsFilterBuilder filterToContentContains(String content) {
        RichFacesListItemsFilterBuilder tfb = new RichFacesListItemsFilterBuilder(this);
        tfb.filters.add(new ContentContainsPredicate(content));
        return tfb;
    }

    @Override
    public RichFacesListItemsFilterBuilder filterToContentNotContains(String content) {
        RichFacesListItemsFilterBuilder tfb = new RichFacesListItemsFilterBuilder(this);
        tfb.filters.add(new ContentNotContainsPredicate(content));
        return tfb;
    }

    private static class ContentContainsPredicate implements Predicate<ListItem> {

        protected final String content;

        public ContentContainsPredicate(String content) {
            this.content = content;
        }

        @Override
        public boolean apply(ListItem input) {
            return input.getText().contains(content);
        }
    }

    private static class ContentNotContainsPredicate extends ContentContainsPredicate {

        public ContentNotContainsPredicate(String content) {
            super(content);
        }

        @Override
        public boolean apply(ListItem input) {
            return !super.apply(input);
        }
    }
}
