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
package org.richfaces.tests.page.fragments.impl.panelMenu;

import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichPanelMenu {

    @Root
    WebElement root;

    public static final String CSS_GROUP_DISABLED = "div[class*=rf-pm-][class*=-gr-dis]";
    public static final String CSS_GROUP_SELECTED = "div[class*=rf-pm][class*=-gr-sel]";
    public static final String CSS_GROUP_EXPANDED = "div.rf-pm-hdr-exp";
    public static final String CSS_ITEM_DISABLED = "div[class*=rf-pm-][class*=-itm-dis]";
    public static final String CSS_ITEM_SELECTED = "div[class*=rf-pm][class*=-itm-sel]";

    public static final String CLASS_TOP_GROUP = "rf-pm-top-gr";
    public static final String CLASS_GROUP = "rf-pm-gr";
    public static final String CLASS_TOP_ITEM = "rf-pm-top-itm";
    public static final String CLASS_ITEM = "rf-pm-itm";

    public WebElement getAnyDisabledGroup() {
        return root.findElement(By.cssSelector(CSS_GROUP_DISABLED));
    }

    public List<WebElement> getAllDisabledGroups() {
        return root.findElements(By.cssSelector(CSS_GROUP_DISABLED));
    }

    public List<WebElement> getAllDisabledItems() {
        return root.findElements(By.cssSelector(CSS_ITEM_DISABLED));
    }

    public List<WebElement> getAllExpandedGroups() {
        return root.findElements(By.cssSelector(CSS_GROUP_EXPANDED));
    }

    public List<WebElement> getAllSelectedItems() {
        return root.findElements(By.cssSelector(CSS_ITEM_SELECTED));
    }

    public List<WebElement> getAllSelectedGroups() {
        return root.findElements(By.cssSelector(CSS_GROUP_SELECTED));
    }

    public WebElement getRoot() {
        return root;
    }

}
