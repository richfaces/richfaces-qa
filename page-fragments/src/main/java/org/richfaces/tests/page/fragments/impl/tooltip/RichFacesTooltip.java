/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.page.fragments.impl.tooltip;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.component.Mode;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichFacesTooltip {

    @Root
    public WebElement root;

    private Mode mode = Mode.ajax;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void recall(WebElement target) {
        recall(target, 5, 5);
    }

    public void recall(WebElement target, int x, int y) {
        if (root.isDisplayed()) {
            // guard(selenium, getRequestType()).mouseMoveAt(target, new Point(x, y));
            Action mouseMoveAt = new Actions(GrapheneContext.getProxy()).moveToElement(target, x, y).build();
            mouseMoveAt.perform();
        } else {
            // guard(selenium, getRequestType()).mouseOverAt(target, new Point(x, y));
            Action mouseMoveAt = new Actions(GrapheneContext.getProxy()).moveToElement(target, x, y).build();
            mouseMoveAt.perform();
        }
        Graphene.waitGui().until(Graphene.element(root).isVisible());
    }

    public void hide(WebElement target) {
        hide(target, -1, -1);
    }

    private void hide(WebElement target, int x, int y) {
        // guard(selenium, getRequestType()).mouseOutAt(target, new Point(x, y));
        Action mouseOutAt = new Actions(GrapheneContext.getProxy()).moveToElement(target).moveByOffset(x, y).build();
        getGuardTypeForMode(mouseOutAt, mode).perform();
    }

    private static <T> T getGuardTypeForMode(T target, Mode mode) {
        switch (mode) {
            case ajax:
                return Graphene.guardXhr(target);
            case server:
                return Graphene.guardHttp(target);
            case client:
                return Graphene.guardNoRequest(target);
            default:
                return Graphene.guardNoRequest(target);
        }
    }
}
