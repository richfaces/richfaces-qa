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

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesContextMenu extends AbstractPopupMenu {

    @FindBy(className = "rf-ctx-itm")
    private List<WebElement> menuItemsElements;

    @FindBy(css = "div.rf-ctx-lst")
    private WebElement contextMenuPopup;

    @FindBy(jquery = "script:last")
    private WebElement script;

    private AdvancedInteractions advancedInteractions;

    @Override
    protected WebElement getMenuPopupInternal() {
        return contextMenuPopup;
    }

    @Override
    protected List<WebElement> getMenuItemElementsInternal() {
        return menuItemsElements;
    }

    @Override
    protected String getNameOfFragment() {
        return RichFacesContextMenu.class.getName();
    }

    @Override
    protected WebElement getScript() {
        return script;
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions extends AbstractPopupMenu.AdvancedInteractions {
        public String getLangAttribute() {
            return root.getAttribute("lang");
        }
    }
}
