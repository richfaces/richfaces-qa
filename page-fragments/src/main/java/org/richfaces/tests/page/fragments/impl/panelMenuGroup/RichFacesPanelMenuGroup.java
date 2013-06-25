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
package org.richfaces.tests.page.fragments.impl.panelMenuGroup;

import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.ATTR_CLASS;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.CSS_COLLAPSED_SUFFIX;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.CSS_DISABLED_SUFFIX;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.CSS_EXPANDED_SUFFIX;
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
import org.richfaces.component.Mode;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichFacesPanelMenuGroup {

    public static final String CSS_HEADER = "div[class*=rf-pm-][class*=-gr-hdr]";
    public static final String CSS_LABEL = "td[class*=rf-pm-][class*=-gr-lbl]";
    public static final String CSS_ICON_LEFT = "td[class*=rf-pm-][class*=-gr-ico]";
    public static final String CSS_ICON_RIGHT = "td[class*=rf-pm-][class*=-gr-exp-ico]";

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @FindBy(css = CSS_HEADER)
    private WebElement header;

    @FindBy(css = CSS_LABEL)
    private WebElement label;

    @FindBy(css = CSS_ICON_LEFT)
    private RichFacesPanelMenuGroupIcon leftIcon;

    @FindBy(css = CSS_ICON_RIGHT)
    private RichFacesPanelMenuGroupIcon rightIcon;

    // mode can be inherited from parent panelMenu
    protected Mode mode;

    public RichFacesPanelMenuGroupIcon getRightIcon() {
        return rightIcon;
    }

    public RichFacesPanelMenuGroupIcon getLeftIcon() {
        return leftIcon;
    }

    public WebElement getLabel() {
        return label;
    }

    public void toggle() {
        boolean wasExpanded = isExpanded();
        if (mode == null) {
            label.click();
        } else {
            getGuardTypeForMode(label, mode).click();
        }
        if (!isDisabled()) {
            if (wasExpanded) {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return isCollapsed();
                    }

                    @Override
                    public String toString() {
                        return "group to be collapsed.";
                    }
                });
            } else {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return isExpanded();
                    }

                    @Override
                    public String toString() {
                        return "group to be expanded.";
                    }
                });
            }
        }
    }

    public boolean isCollapsed() {
        return header.getAttribute(ATTR_CLASS).contains(CSS_COLLAPSED_SUFFIX);
    }

    public boolean isExpanded() {
        return header.getAttribute(ATTR_CLASS).contains(CSS_EXPANDED_SUFFIX);
    }

    public boolean isSelected() {
        return header.getAttribute(ATTR_CLASS).contains(CSS_SELECTED_SUFFIX);
    }

    public boolean isHovered() {
        return root.getAttribute(ATTR_CLASS).contains(CSS_HOVERED_SUFFIX);
    }

    public boolean isDisabled() {
        return root.getAttribute(ATTR_CLASS).contains(CSS_DISABLED_SUFFIX);
    }

    public boolean isVisible() {
        return Graphene.element(root).isPresent().apply(browser) && root.isDisplayed();
    }

    public WebElement getRoot() {
        return root;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

}
