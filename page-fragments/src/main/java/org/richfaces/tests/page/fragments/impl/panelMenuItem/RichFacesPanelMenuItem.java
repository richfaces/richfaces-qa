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
package org.richfaces.tests.page.fragments.impl.panelMenuItem;

import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.ATTR_CLASS;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.CSS_DISABLED_SUFFIX;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.CSS_HOVERED_SUFFIX;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.CSS_SELECTED_SUFFIX;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.getGuardTypeForMode;

import com.google.common.base.Predicate;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.ui.common.Mode;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichFacesPanelMenuItem {

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @FindBy(css = "td[class*=rf-][class*=-itm-lbl]")
    private WebElement label;

    @FindBy(css = "td[class*=rf-][class*=-itm-ico]")
    private RichFacesPanelMenuItemIcon leftIcon;

    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico]")
    private RichFacesPanelMenuItemIcon rightIcon;

    // mode can be inherited from parent panelMenu
    protected Mode mode;

    public WebElement getRoot() {
        return root;
    }

    public RichFacesPanelMenuItemIcon getLeftIcon() {
        return leftIcon;
    }

    public RichFacesPanelMenuItemIcon getRightIcon() {
        return rightIcon;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void select() {
        boolean wasSelected = isSelected();
        if (mode == null) {
            label.click();
        } else {
            getGuardTypeForMode(label, mode).click();
        }
        if (!isDisabled()) {
            if (wasSelected) {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return !isSelected();
                    }

                    @Override
                    public String toString() {
                        return "item to be not selected.";
                    }
                });
            } else {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return isSelected();
                    }

                    @Override
                    public String toString() {
                        return "item to be selected.";
                    }
                });
            }
        }
    }

    public boolean isSelected() {
        return root.getAttribute(ATTR_CLASS).contains(CSS_SELECTED_SUFFIX);
    }

    public boolean isHovered() {
        return root.getAttribute(ATTR_CLASS).contains(CSS_HOVERED_SUFFIX);
    }

    public boolean isDisabled() {
        return root.getAttribute(ATTR_CLASS).contains(CSS_DISABLED_SUFFIX);
    }

    public boolean isVisible() {
        boolean present = Graphene.element(root).isVisible().apply(browser);
        boolean visible = Graphene.element(root).isPresent().apply(browser);
        return present && visible;
    }
}
