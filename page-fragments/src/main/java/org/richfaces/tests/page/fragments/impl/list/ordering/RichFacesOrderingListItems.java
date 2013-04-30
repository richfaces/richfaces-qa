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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import org.richfaces.tests.page.fragments.impl.list.ListItem;
import org.richfaces.tests.page.fragments.impl.list.ListItemsFilterBuilder;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of OrderingListItem
 */
public class RichFacesOrderingListItems<T extends OrderingListItem> extends ArrayList<T> implements OrderingListItems<T> {

    private static final long serialVersionUID = 1L;
    private final WebDriver driver = GrapheneContext.getProxy();

    public RichFacesOrderingListItems() {
    }

    public RichFacesOrderingListItems(Collection<? extends T> c) {
        super(c);
    }

    public RichFacesOrderingListItems(Iterable<? extends T> it) {
        this.addAll(Lists.newArrayList(it));
    }

    @Override
    public OrderingListItems deselectAll() {
        for (T item : this) {
            item.deselect();
        }
        return this;
    }

    @Override
    public OrderingListItems<T> filter(ListItemsFilterBuilder builder) {
        return new RichFacesOrderingListItems(Iterables.filter(this, builder.build()));
    }

    @Override
    public int indexOf(Object o) {
        return ((ListItem) o).getIndex();
    }

    @Override
    public OrderingListItems<T> selectAll() {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL);
        for (T item : this) {
            if (!item.isSelected()) {
                actions.click(item.getItemElement());
            }
        }
        actions.keyUp(Keys.CONTROL).perform();
        return this;
    }
}
