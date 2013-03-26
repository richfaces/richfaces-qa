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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.component.Mode;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichFacesPanelMenuItem {

    @Root
    WebElement root;

    @FindBy(css = "td[class*=rf-][class*=-itm-lbl]")
    public WebElement label;

    @FindBy(css = "td[class*=rf-][class*=-itm-ico]")
    public RichFacesPanelMenuItemIcon leftIcon;

    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico]")
    public RichFacesPanelMenuItemIcon rightIcon;

    // mode can be inherited from parent panelMenu
    Mode mode;

    public WebElement getRoot() {
        return root;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void select() {
        if (mode == null) {
            label.click();
        } else {
            getGuardTypeForMode(label, mode).click();
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
        boolean present = Graphene.element(root).isVisible().apply(GrapheneContext.getProxy());
        boolean visible = Graphene.element(root).isPresent().apply(GrapheneContext.getProxy());
        return present && visible;
    }

}
