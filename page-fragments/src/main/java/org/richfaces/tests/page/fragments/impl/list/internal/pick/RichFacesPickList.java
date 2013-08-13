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

import com.google.common.base.Predicate;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.list.internal.common.SelectableList;
import org.richfaces.tests.page.fragments.impl.list.internal.common.SelectableListItem;
import org.richfaces.tests.page.fragments.impl.list.internal.ordering.OrderingList;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of SelectableListItem
 * @param <SL> type of RichFacesSourceList
 * @param <TL> type of RichFacesTargetList
 */
public abstract class RichFacesPickList<T extends SelectableListItem, SL extends RichFacesSourceList<T>, TL extends RichFacesTargetList<T>> implements PickList<T> {

    @Root
    private WebElement root;
    @Drone
    private WebDriver driver;
    @FindBy(className = "rf-pick-add-all")
    private WebElement addAllButtonElement;
    @FindBy(className = "rf-pick-add")
    private WebElement addButtonElement;
    @FindBy(className = "rf-pick-rem-all")
    private WebElement removeAllButtonElement;
    @FindBy(className = "rf-pick-rem")
    private WebElement removeButtonElement;
    @FindBy(className = "rf-pick-src")
    private WebElement sourceListRoot;
    @FindBy(css = "table[id$=Target]")
    private WebElement targetListRoot;

    @Override
    public PickList<T> add() {
        final int sourceListSize = source().getItems().size();
        final int selectedItemsSize = source().getSelectedItems().size();
        final int targetListSize = target().getItems().size();
        this.addButtonElement.click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return target().getItems().size() == (targetListSize + selectedItemsSize)
                        && source().getItems().size() == (sourceListSize - selectedItemsSize);
            }
        });
        return this;
    }

    @Override
    public PickList<T> addAll() {
        final int sourceListSize = source().getItems().size();
        final int targetListSize = target().getItems().size();
        this.addAllButtonElement.click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return target().getItems().size() == (targetListSize + sourceListSize)
                        && source().getItems().size() == 0;
            }
        });
        return this;
    }

    private SelectableList<T> createSourceList() {
        return Graphene.createPageFragment(getSourceListType(), sourceListRoot);
    }

    private OrderingList<T> createTargetList() {
        return Graphene.createPageFragment(getTargetListType(), targetListRoot);
    }

    @Override
    public WebElement getAddAllButtonElement() {
        return addAllButtonElement;
    }

    @Override
    public WebElement getAddButtonElement() {
        return addButtonElement;
    }

    protected abstract Class<SL> getSourceListType();

    protected abstract Class<TL> getTargetListType();

    @Override
    public WebElement getRemoveAllButtonElement() {
        return removeAllButtonElement;
    }

    @Override
    public WebElement getRemoveButtonElement() {
        return removeButtonElement;
    }

    @Override
    public WebElement getRootElement() {
        return root;
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    @Override
    public PickList<T> remove() {
        final int selectedItemsSize = target().getSelectedItems().size();
        if (selectedItemsSize == 0) {
            return this;
        }
        final int targetListSize = target().getItems().size();
        final int sourceListSize = source().getItems().size();
        this.removeButtonElement.click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return source().getItems().size() == (sourceListSize + selectedItemsSize)
                        && target().getItems().size() == (targetListSize - selectedItemsSize);
            }
        });
        return this;
    }

    @Override
    public PickList<T> removeAll() {
        final int sourceListSize = source().getItems().size();
        final int targetListSize = target().getItems().size();
        this.removeAllButtonElement.click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return source().getItems().size() == (targetListSize + sourceListSize)
                        && target().getItems().size() == 0;
            }
        });
        return this;
    }

    @Override
    public SelectableList<T> source() {
        return createSourceList();
    }

    @Override
    public OrderingList<T> target() {
        return createTargetList();
    }
}
