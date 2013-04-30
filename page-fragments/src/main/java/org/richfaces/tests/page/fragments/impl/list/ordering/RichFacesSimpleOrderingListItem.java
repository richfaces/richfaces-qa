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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.list.AbstractListItem;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesSimpleOrderingListItem extends AbstractListItem implements OrderingListItem {

    private static final String SELECTED_ITEM_CLASS = "rf-ord-sel";
    @Root
    private WebElement item;

    @Override
    public void deselect() {
        if (isSelected()) {
            item.click();
        }
        Graphene.waitGui().until().element(item).attribute("class").not().contains(SELECTED_ITEM_CLASS);
    }

    @Override
    public WebElement getItemElement() {
        return item;
    }

    public String getText() {
        return item.getText();
    }

    @Override
    public boolean isSelected() {
        return item.getAttribute("class").contains(SELECTED_ITEM_CLASS);
    }

    @Override
    public void select() {
        if (!isSelected()) {
            item.click();
        }
        Graphene.waitGui().until().element(item).attribute("class").contains(SELECTED_ITEM_CLASS);
    }
}
