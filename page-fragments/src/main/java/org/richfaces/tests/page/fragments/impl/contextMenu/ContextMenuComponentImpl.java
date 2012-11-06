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

import static org.jboss.arquillian.graphene.Graphene.element;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ContextMenuComponentImpl {

    @FindBy(className = "rf-ctx-itm-lbl")
    private List<WebElement> menuItemsElements;

    @FindBy(className = "rf-ctx-lst")
    private WebElement contextMenuPopup;

    private ContextMenuInvoker invoker = RIGHT_CLICK;

    private WebElement target;

    /**
     * The right click invoker of context menu
     */
    public static final ContextMenuInvoker RIGHT_CLICK = new RightClickContextMenuInvoker();

    /**
     * The left click invoker of context menu
     */
    public static final ContextMenuInvoker LEFT_CLICK = new LeftClickContextMenuInvoker();

    /* ************************************************************************************************
     * API
     */

    /**
     * Returns menu items labels. By default it is presumed that context menu is invoked by right click. To change this behavior
     * use <code>setInvoker()</code> method.
     *
     * @param givenTarget the target on which the contextMenu is invoked.
     * @see #setInvoker(ContextMenuInvoker)
     * @return
     */
    public List<ContextMenuItem> getMenuItemsLabels(WebElement givenTarget) {
        invoker.invoke(givenTarget);

        List<ContextMenuItem> itemsText = new ArrayList<ContextMenuItem>();
        for (WebElement item : menuItemsElements) {
            itemsText.add(new ContextMenuItem(item.getText()));
        }

        return itemsText;
    }

    /**
     * Returns menu items labels. By default it is presumed that context menu is invoked by right click. To change this behavior
     * use <code>setInvoker()</code> method.
     *
     * @param givenTarget the target on which the contextMenu is invoked.
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
     * Invokes context menu and selects from it given item. By default it is presumed that context menu is invoked by right
     * click. To change this behavior use <code>setInvoker()</code> method.
     *
     * @param item
     * @see #setInvoker(ContextMenuInvoker)
     */
    public void selectFromContextMenu(ContextMenuItem item, WebElement givenTarget) {
        invokeContextMenu(givenTarget);

        for (WebElement itemElement : menuItemsElements) {
            String currentItemText = itemElement.getText();

            if (item.getItemText().equals(currentItemText.trim())) {
                itemElement.click();
            }
        }
    }

    /**
     * Invokes context menu and selects from it given item. By default it is presumed that context menu is invoked by right
     * click. To change this behavior use <code>setInvoker()</code> method.
     *
     * @param item
     * @see #setInvoker(ContextMenuInvoker)
     * @see #setTarget(WebElement)
     */
    public void selectFromContextMenu(ContextMenuItem item) {
        checkWhetherTargetIsSet();

        selectFromContextMenu(item, this.target);
    }

    /**
     * Invokes context menu. By default it is presumed that context menu is invoked by right click. To change this behavior use
     * <code>setInvoker()</code> method.
     *
     * @param invoker
     * @see #setInvoker(ContextMenuInvoker)
     * @return true if context menu is displayed, false otherwise
     */
    public boolean invokeContextMenu(WebElement givenTarget) {
        invoker.invoke(givenTarget);

        Graphene.waitGui().withTimeout(3, TimeUnit.SECONDS)
            .withMessage("The Context Menu was not rendered in the given timeout!")
            .until(element(contextMenuPopup).isVisible());

        return contextMenuPopup.isDisplayed();
    }

    /**
     * Invokes context menu. By default it is presumed that context menu is invoked by right click. To change this behavior use
     * <code>setInvoker()</code> method.
     *
     * @param invoker
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
     * Neste classes
     */

    public static final class LeftClickContextMenuInvoker implements ContextMenuInvoker {

        @Override
        public void invoke(WebElement target) {
            target.click();
        }

    }

    public static final class RightClickContextMenuInvoker implements ContextMenuInvoker {

        @Override
        public void invoke(WebElement target) {
            Actions builder = new Actions(GrapheneContext.getProxy());

            builder.contextClick(target).build().perform();
        }

    }

    /* ****************************************************************************************************
     * Help Methods
     */

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
