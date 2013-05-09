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
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.page.fragments.impl.list.AbstractListItem;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class SimpleSelectableListItem extends AbstractListItem implements SelectableListItem {

    @Root
    protected WebElement item;

    private Action getAction(WebDriver driver) {
        return new Actions(driver).keyDown(Keys.CONTROL).click(item).keyUp(Keys.CONTROL).build();
    }

    @Override
    public void deselect() {
        if (isSelected()) {
            getAction(GrapheneContext.getProxy()).perform();
        }
        Graphene.waitGui().until().element(item).attribute("class").not().contains(getClassForSelectedItem());
    }

    protected abstract String getClassForSelectedItem();

    @Override
    public WebElement getItemElement() {
        return item;
    }

    public String getText() {
        return item.getText();
    }

    @Override
    public boolean isSelected() {
        return item.getAttribute("class").contains(getClassForSelectedItem());
    }

    @Override
    public void select() {
        if (!isSelected()) {
            getAction(GrapheneContext.getProxy()).perform();
        }
        Graphene.waitGui().until().element(item).attribute("class").contains(getClassForSelectedItem());
    }

    @Override
    public String toString() {
        return getText();
    }
}
