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

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.component.Mode;
import org.richfaces.tests.page.fragments.impl.panelMenuGroup.PanelMenuGroup;
import org.richfaces.tests.page.fragments.impl.panelMenuItem.PanelMenuItem;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichPanelMenu {

    @Root
    WebElement root;

    @FindBy(css = "div[id$=item3]")
    public PanelMenuItem item3;

    @FindBy(css = "div[id$=item4]")
    public PanelMenuItem item4;

    @FindBy(css = "div[id$=group1]")
    public PanelMenuGroup group1;

    @FindBy(css = "div[id$=group2]")
    public PanelMenuGroup group2;

    @FindBy(css = "div[id$=group3]")
    public PanelMenuGroup group3;

    @FindBy(css = "div[id$=item22]")
    public PanelMenuItem item22;

    @FindBy(css = "div[id$=item25]")
    public PanelMenuItem item25;

    @FindBy(css = "div[id$=group24]")
    public PanelMenuGroup group24;

    @FindBy(css = "div[id$=group26]")
    public PanelMenuGroup group26;

    @FindBy(css = "div[id$=item242]")
    public PanelMenuItem item242;

    @FindBy(css = "div[id$=group4]")
    public PanelMenuGroup group4;

    Mode groupMode;
    Mode itemMode;

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

    public void setGroupMode(Mode groupMode) {
        this.groupMode = groupMode;

        // panelMenuGroup inherits mode from panelMenu until set different
        group1.setMode(groupMode);
        group2.setMode(groupMode);
        group24.setMode(groupMode);
        group26.setMode(groupMode);
        group4.setMode(groupMode);
    }

    public void setItemMode(Mode itemMode) {
        this.itemMode = itemMode;

        // panelMenuItem inherits from panelMenu until set different
        item22.setMode(itemMode);
        item242.setMode(itemMode);
        item25.setMode(itemMode);
        item3.setMode(itemMode);
        item4.setMode(itemMode);
    }

    public WebElement getRoot() {
        return root;
    }

}
