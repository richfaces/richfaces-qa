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
package org.richfaces.tests.page.fragments.impl.list.simple;

import com.google.common.base.Predicate;

import org.richfaces.tests.page.fragments.impl.list.ListItemsFilterBuilderImpl;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesSimpleListItemsFilterBuilder extends ListItemsFilterBuilderImpl<RichFacesSimpleListItem> {

    @Override
    protected RichFacesSimpleListItemsFilterBuilder copy() {
        RichFacesSimpleListItemsFilterBuilder copy = new RichFacesSimpleListItemsFilterBuilder();
        copy.filters.addAll(this.filters);
        return copy;
    }

    @Override
    public RichFacesSimpleListItemsFilterBuilder addFilter(Predicate<RichFacesSimpleListItem> filter) {
        return (RichFacesSimpleListItemsFilterBuilder) super.addFilter(filter);
    }

    public RichFacesSimpleListItemsFilterBuilder filterToContentContains(String content) {
        return addFilter(new ContentContainsPredicate(content));
    }

    public RichFacesSimpleListItemsFilterBuilder filterToContentNotContains(String content) {
        return addFilter(new ContentNotContainsPredicate(content));
    }

    private static class ContentContainsPredicate implements Predicate<RichFacesSimpleListItem> {

        protected final String content;

        public ContentContainsPredicate(String content) {
            this.content = content;
        }

        @Override
        public boolean apply(RichFacesSimpleListItem input) {
            return input.getText().contains(content);
        }
    }

    private static class ContentNotContainsPredicate extends ContentContainsPredicate {

        public ContentNotContainsPredicate(String content) {
            super(content);
        }

        @Override
        public boolean apply(RichFacesSimpleListItem input) {
            return !super.apply(input);
        }
    }
}
