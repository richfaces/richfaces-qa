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
import org.jboss.arquillian.drone.api.annotation.Drone;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class AbstractPopupMenu {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private Actions actions;

    private int showDelay = 50;

    private PopupMenuInvoker invoker = RIGHT_CLICK;

    private WebElement target;

    /**
     * The right click invoker of popup menu
     */
    public static final PopupMenuInvoker RIGHT_CLICK = new RightClickPopupMenuInvoker();

    /**
     * The left click invoker of popup menu
     */
    public static final PopupMenuInvoker LEFT_CLICK = new LeftClickPopupMenuInvoker();

    /**
     * The hover invoker of popup menu
     */
    public static final PopupMenuInvoker HOVER = new HoverPopupMenuInvoker();

    /* ************************************************************************************************
     * Abstract methods
     */
    /**
     * Returns the popup element of the menu
     *
     * @return
     */
    public abstract WebElement getMenuPopup();

    /**
     * Returns all elements of this menu
     *
     * @return
     */
    public abstract List<WebElement> getMenuItemElements();

    /**
     * Returns the name of the actual page fragment.
     * @return
     */
    public abstract String getNameOfFragment();

    /* ************************************************************************************************
     * API
     */
    /**
     * Dismisses currently displayed popup menu. If no popup menu is currently displayed an exception is thrown.
     *
     * @throws IllegalStateException when no popup menu is displayed in the time of invoking
     */
    public void dismiss() {
        if (!getMenuPopup().isDisplayed()) {
            throw new IllegalStateException("You are attemting to dismiss the " + getNameOfFragment() + ", however, no "
                    + getNameOfFragment() + " is displayed at the moment!");
        }
        browser.findElement(By.tagName("body")).click();
        waitModel().until(element(getMenuPopup()).not().isVisible());
    }

    /**
     * Returns menu items elements. One needs to invoke popup menu in order to work with them.
     * Note that some of the elements may not become visible by just invoking the popup menu (e.g. popup menu items with sub items)
     *
     * @return the popup menu items
     */
    public List<WebElement> getItems() {
        return getMenuItemElements();
    }

    /**
     * Invokes the popup menu and selects from it a given item. By default it is presumed that popup menu is invoked by right
     * click. To change this behavior use <code>setInvoker()</code> method.
     *
     * @param item to be selected from popup menu
     * @see #setInvoker(PopupMenuInvoker)
     */
    public void selectItem(PopupMenuItem item, WebElement givenTarget) {
        invoke(givenTarget);

        for (WebElement itemElement : getMenuItemElements()) {
            String currentItemText = itemElement.getText();

            if (item.getText().equals(currentItemText.trim())) {
                itemElement.click();
            }
        }
    }

    /**
     * Invokes popup menu and selects from it given item. By default it is presumed that popup menu is invoked by right
     * click. To change this behavior use <code>setInvoker()</code> method.
     *
     * @param item
     * @see #setInvoker(PopupMenuInvoker)
     * @see #setTarget(WebElement)
     */
    public void selectItem(PopupMenuItem item) {
        checkWhetherTargetIsSet();

        selectItem(item, this.target);
    }

    /**
     * Invokes popup menu in the middle of the given target. By default it is presumed that popup menu is invoked by right click. To change this behavior use
     * <code>setInvoker()</code> method. It also works with the default value of <code>showDelay == 50ms</code>. Use <code>#setShowDelay</code> if this value is different for this menu.
     *
     * @param givenTarget
     * @see #setInvoker(PopupMenuInvoker)
     * @see #setShowDelay(int)
     */
    public void invoke(WebElement givenTarget) {
        actions.moveToElement(givenTarget).build().perform();
        invoker.invoke(givenTarget);

        waitUntilIsVisible();
    }

    /**
     * Waits until the popup menu is visible. It takes into account the <code>showDelay</code> which has default value 50ms.
     *
     * @see #setShowDelay(int)
     */
    public void waitUntilIsVisible() {
        Graphene.waitModel().withTimeout(showDelay + 4000, TimeUnit.MILLISECONDS)
                .withMessage("The " + getNameOfFragment() + " did not show in the given timeout!")
                .until(element(getMenuPopup()).isVisible());
    }

    /**
     * Invokes popup menu on a given point within the given target. By default it is presumed that popup
     * menu is invoked by right click. To change this behavior use
     * <code>setInvoker()</code> method.
     *
     * @param givenTarget
     * @param location
     * @see #setInvoker(PopupMenuInvoker)
     */
    public void invoke(WebElement givenTarget, Point location) {
        throw new UnsupportedOperationException("File a feature request to have this, or even better implement it:)");
    }

    /**
     * Invokes popup menu in the middle of the currently set target. By default it is presumed that popup menu is invoked by right click.
     * To change this behavior use <code>setInvoker()</code> method. You have to have a target set before invocation of this method.
     *
     * @see #setInvoker(PopupMenuInvoker)
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

    public PopupMenuInvoker getInvoker() {
        return invoker;
    }

    public void setInvoker(PopupMenuInvoker invoker) {
        if (invoker == null) {
            throw new IllegalArgumentException("Parameter invoker can not be null!");
        }
        this.invoker = invoker;
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
     * Nested classes
     */

    public static final class LeftClickPopupMenuInvoker implements PopupMenuInvoker {
        @Override
        public void invoke(WebElement target) {
            target.click();
        }
    }

    public static final class RightClickPopupMenuInvoker implements PopupMenuInvoker {
        @Override
        public void invoke(WebElement target) {
            GrapheneContext context = ((GrapheneProxyInstance) target).getContext();
            Actions builder = new Actions(context.getWebDriver());
            builder.contextClick(target).build().perform();
        }
    }

    public static final class HoverPopupMenuInvoker implements PopupMenuInvoker {
        @Override
        public void invoke(WebElement target) {
            GrapheneContext context = ((GrapheneProxyInstance) target).getContext();
            Actions builder = new Actions(context.getWebDriver());
            builder.moveToElement(target).build().perform();
        }
    }

    /* ****************************************************************************************************
     * Help Methods
     */

    private void checkWhetherTargetIsSet() {
        if (target == null) {
            throw new IllegalStateException("The " + getNameOfFragment()
                    + " target has to be set before this operation! See setTarget() method.");
        }
    }
}
