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
package org.richfaces.tests.page.fragments.impl.list.common;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.list.AbstractListFragment;

/**
 * Base for any list that has selectable items.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of SelectableListItem
 * @param <L> type of SelectableListLayout
 * @see org.richfaces.tests.page.fragments.impl.list.pick.RichFacesSourceList
 * @see org.richfaces.tests.page.fragments.impl.list.ordering.RichFacesOrderingListBase
 */
public abstract class AbstractSelectableListBase<T extends SelectableListItem, L extends SelectableListLayout> extends AbstractListFragment<T, SelectableListItems<T>> implements SelectableList<T> {

    private L layout;

    @Override
    public String getCaption() {
        return getLayout().getCaptionElement().getText();
    }

    @Override
    public WebElement getHeaderElement() {
        return getLayout().getHeaderElement();
    }

    @Override
    public SelectableListItems<T> getItems() {
        return createItems(getLayout().getItems());
    }

    protected SelectableListItems<T> getItemsByIndex(Integer first, Integer... rest) {
        if (first == null) {
            throw new IllegalArgumentException("the index cannot be null");
        }
        SelectableListItems<T> items1 = getItems();
        SelectableListItems<T> items2 = instantiateListItems();
        items2.add(items1.get(first));
        for (Integer i : rest) {
            if (i != null) {
                items2.add(items1.get(i));
            }
        }
        return items2;
    }

    protected L getLayout() {
        if (layout == null) {
            layout = Graphene.createPageFragment(getLayoutType(), root);
        }
        return layout;
    }

    protected abstract Class<L> getLayoutType();

    @Override
    public WebElement getListAreaElement() {
        return getLayout().getListAreaElement();
    }

    @Override
    public SelectableListItems<T> getSelectedItems() {
        return createItems(getLayout().getSelectedItems());
    }

    @Override
    protected SelectableListItems<T> instantiateListItems() {
        return new RichFacesSelectableListItems<T>();
    }
}
