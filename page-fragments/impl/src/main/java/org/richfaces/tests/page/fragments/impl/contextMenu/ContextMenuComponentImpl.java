/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.contextMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ContextMenuComponentImpl {

    @FindBy(className = "rf-ctx-itm-lbl")
    private List<WebElement> menuItemsElements;

    @FindBy(className = "rf-ctx-lst")
    private WebElement contextMenuPopup;

    private ContextMenuInvoker invoker;

    private WebElement target;

    /**
     * Returns menu items labels.
     * 
     * @param givenTarget the target on which the contextMenu is invoked.
     * @throws IllegalStateException if ContextMenuInvoker is not set before invocation of this method
     * @see #setInvoker(ContextMenuInvoker)
     * @return
     */
    public List<ContextMenuItem> getMenuItemsLabels(WebElement givenTarget) {
        checkWhetherContextMenuInvokerIsSet();

        invoker.invoke(givenTarget);

        List<ContextMenuItem> itemsText = new ArrayList<ContextMenuItem>();
        for (WebElement item : menuItemsElements) {
            itemsText.add(new ContextMenuItem(item.getText()));
        }

        return itemsText;
    }

    /**
     * Returns menu items labels.
     * 
     * @param givenTarget the target on which the contextMenu is invoked.
     * @throws IllegalStateException if ContextMenuInvoker or target is not set before invocation of this method
     * @see #setInvoker(ContextMenuInvoker)
     * @see #setTarget(WebElement)
     * @return
     */
    public List<ContextMenuItem> getMenuItemsLabels() {
        checkWhetherTargetIsSet();

        return getMenuItemsLabels(this.target);
    }

    /**
     * Returns menu items elements.
     * 
     * @return
     */
    public List<WebElement> getMemuItemsElements() {
        return menuItemsElements;
    }

    /**
     * Invokes context menu and selects from it given item.
     * 
     * @param item
     * @throws IllegalStateException if ContextMenuInvoker is not set before invocation of this method
     * @see #setInvoker(ContextMenuInvoker)
     */
    public void selectFromContextMenu(ContextMenuItem item, WebElement givenTarget) {
        checkWhetherContextMenuInvokerIsSet();

        invokeContextMenu(givenTarget);

        for (WebElement itemElement : menuItemsElements) {
            String currentItemText = itemElement.getText();

            if (item.getItemText().equals(currentItemText.trim())) {
                itemElement.click();
            }
        }
    }

    /**
     * Invokes context menu and selects from it given item.
     * 
     * @param item
     * @throws IllegalStateException if ContextMenuInvoker or target is not set before invocation of this method
     * @see #setInvoker(ContextMenuInvoker)
     * @see #setTarget(WebElement)
     */
    public void selectFromContextMenu(ContextMenuItem item) {
        checkWhetherTargetIsSet();

        selectFromContextMenu(item, this.target);
    }

    /**
     * Invokes context menu.
     * 
     * @param invoker
     * @throws IllegalStateException if ContextMenuInvoker is not set before invocation of this method
     * @see #setInvoker(ContextMenuInvoker)
     * @return true if context menu is displayed, false otherwise
     */
    public boolean invokeContextMenu(WebElement givenTarget) {
        checkWhetherContextMenuInvokerIsSet();

        invoker.invoke(givenTarget);

        Graphene.waitGui().withTimeout(3, TimeUnit.SECONDS)
            .withMessage("The Context Menu was not rendered in the given timeout!").until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return contextMenuPopup.isDisplayed();
                }
            });

        return contextMenuPopup.isDisplayed();
    }

    /**
     * Invokes context menu.
     * 
     * @param invoker
     * @throws IllegalStateException if ContextMenuInvoker or target is not set before invocation of this method
     * @see #setInvoker(ContextMenuInvoker)
     * @see #setTarget(WebElement)
     * @return true if context menu is displayed, false otherwise
     */
    public boolean invokeContextMenu() {
        checkWhetherTargetIsSet();

        return invokeContextMenu(this.target);
    }

    public WebElement getTarget() {
        return target;
    }

    public void setTarget(WebElement target) {
        this.target = target;
    }

    /* ****************************************************************************************************
     * Help Methods
     */

    private void checkWhetherContextMenuInvokerIsSet() {
        if (invoker == null) {
            throw new IllegalStateException(
                "The context menu invoker has to be set before this operation! Use setInvoker() method!");
        }
    }

    private void checkWhetherTargetIsSet() {
        if (target == null) {
            throw new IllegalStateException(
                "The context menu target has to be set before this operation! See setTarget() method.");
        }
    }

    public ContextMenuInvoker getInvoker() {
        return invoker;
    }

    public void setInvoker(ContextMenuInvoker invoker) {
        if (invoker == null) {
            throw new IllegalArgumentException("Parameter invoker can not be null!");
        }
        this.invoker = invoker;
    }

    public WebElement getContextMenuPopup() {
        return contextMenuPopup;
    }

    public void setContextMenuPopup(WebElement contextMenuPopup) {
        this.contextMenuPopup = contextMenuPopup;
    }
}
