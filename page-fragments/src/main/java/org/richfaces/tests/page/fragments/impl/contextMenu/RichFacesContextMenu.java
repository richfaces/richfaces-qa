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
package org.richfaces.tests.page.fragments.impl.contextMenu;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesContextMenu {

    @Root
    protected WebElement root;

    @FindBy(className = "rf-ctx-itm")
    private List<WebElement> menuItemsElements;

    @FindBy(css = "div.rf-ctx-lst")
    private WebElement contextMenuPopup;

    private int showDelay = 50;

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

    /**
     * The hover invoker of context menu
     */
    public static final ContextMenuInvoker HOVER = new LeftClickContextMenuInvoker();

    /* ************************************************************************************************
     * API
     */
    /**
     * Dismisses currently displayed context menu. If no context menu is currently displayed an exception is thrown.
     *
     * @throws IllegalStateException when no context menu is displayed in the time of invoking
     */
    public void dismiss() {
        if (!contextMenuPopup.isDisplayed()) {
            throw new IllegalStateException(
                    "You are attemting to dismiss the context menu, however, no context menu is displayed at the moment!");
        }
        GrapheneContext.getProxy().findElement(By.tagName("body")).click();
        waitModel().until(element(contextMenuPopup).not().isVisible());
    }

    /**
     * Returns menu items elements. One needs to invoke context menu in order to work with them.
     * Note that some of the elements may not become visible by just invoking the context menu (e.g. context menu items with sub items)
     *
     * @return the context menu items
     */
    public List<WebElement> getMemuItemsElements() {
        return menuItemsElements;
    }

    /**
     * Invokes the context menu and selects from it a given item. By default it is presumed that context menu is invoked by right
     * click. To change this behavior use <code>setInvoker()</code> method.
     *
     * @param item to be selected from context menu
     * @see #setInvoker(ContextMenuInvoker)
     */
    public void selectItem(ContextMenuItem item, WebElement givenTarget) {
        invoke(givenTarget);

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
    public void selectItem(ContextMenuItem item) {
        checkWhetherTargetIsSet();

        selectItem(item, this.target);
    }

    /**
     * Invokes context menu in the middle of the given target. By default it is presumed that context menu is invoked by right click. To change this behavior use
     * <code>setInvoker()</code> method. It also works with the default value of <code>showDelay == 50ms</code>. Use <code>#setShowDelay</code> if this value is different for this menu.
     *
     * @param givenTarget
     * @see #setInvoker(ContextMenuInvoker)
     * @see #setShowDelay(int)
     */
    public void invoke(WebElement givenTarget) {
        new Actions(GrapheneContext.getProxy()).moveToElement(givenTarget);
        invoker.invoke(givenTarget);

        waitUntilContextMenuIsVisible();
    }

    /**
     * Waits until the context menu is visible. It takes into account the <code>showDelay</code> which has default value 50ms.
     *
     * @see #setShowDelay(int)
     */
    public void waitUntilContextMenuIsVisible() {
        Graphene.waitModel().withTimeout(showDelay + 4000, TimeUnit.MILLISECONDS)
                .withMessage("The Context Menu did not show in the given timeout!")
                .until(element(contextMenuPopup).isVisible());
    }

    /**
     * Invokes context menu on a given point within the given target. By default it is presumed that context
     * menu is invoked by right click. To change this behavior use
     * <code>setInvoker()</code> method.
     *
     * @param givenTarget
     * @param location
     * @see #setInvoker(ContextMenuInvoker)
     */
    public void invoke(WebElement givenTarget, Point location) {
        throw new UnsupportedOperationException("File a feature request to have this, or even better implement it:)");
    }

    /**
     * Invokes context menu in the middle of the currently set target. By default it is presumed that context menu is invoked by right click.
     * To change this behavior use <code>setInvoker()</code> method. You have to have a target set before invocation of this method.
     *
     * @see #setInvoker(ContextMenuInvoker)
     * @see #setTarget(WebElement)
     */
    public void invoke() {
        checkWhetherTargetIsSet();

        invoke(this.target);
    }

    public WebElement getTarget() {
        return target;
    }

    public void setTarget(WebElement target) {
        this.target = target;
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

    public int getShowDelay() {
        return showDelay;
    }

    /**
     * Sets the delay which is between showevent observing and the menu opening
     * @param showDelay
     */
    public void setShowDelay(int showDelay) {
        if (showDelay < 0) {
            throw new IllegalArgumentException("Can not be negative!");
        }
        this.showDelay = showDelay;
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

    public static final class HoverContextMenuInvoker implements ContextMenuInvoker {
        @Override
        public void invoke(WebElement target) {
            Actions builder = new Actions(GrapheneContext.getProxy());
            builder.moveToElement(target).build().perform();
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
}
