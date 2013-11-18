/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General private License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General private License for more details.
 *
 * You should have received a copy of the GNU Lesser General private
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenu;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenuGroup;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenuItem;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class PanelMenuPage extends MetamerPage {

    @FindBy(css = "div.rf-pm[id$=panelMenu]")
    private RichFacesPanelMenu panelMenu;

    @FindBy(css = "div[id$=item3]")
    private RichFacesPanelMenuItem item3;

    @FindBy(css = "div[id$=item4]")
    private RichFacesPanelMenuItem item4;

    @FindBy(css = "div[id$=group1]")
    private RichFacesPanelMenuGroup group1;

    @FindBy(css = "div[id$=group2]")
    private RichFacesPanelMenuGroup group2;

    @FindBy(css = "div[id$=group3]")
    private RichFacesPanelMenuGroup group3;

    @FindBy(css = "div[id$=item22]")
    private RichFacesPanelMenuItem item22;

    @FindBy(css = "div[id$=item25]")
    private RichFacesPanelMenuItem item25;

    @FindBy(css = "div[id$=group24]")
    private RichFacesPanelMenuGroup group24;

    @FindBy(css = "div[id$=group26]")
    private RichFacesPanelMenuGroup group26;

    @FindBy(css = "div[id$=item242]")
    private RichFacesPanelMenuItem item242;

    @FindBy(css = "div[id$=group4]")
    private RichFacesPanelMenuGroup group4;

    @FindBy(css = "input[id$=expandAll]")
    private WebElement expandAll;

    @FindBy(css = "input[id$=expandAllBtn1]")
    private WebElement expandAllBtn1;

    @FindBy(css = "input[id$=collapseAll]")
    private WebElement collapseAll;

    @FindBy(css = "input[id$=collapseAllBtn1]")
    private WebElement collapseAllBtn1;

    @FindBy(css = "input[id$=selectItem]")
    private WebElement selecItem;

    @FindBy(css = "input[id$=selectItemBtn1]")
    private WebElement selectItemBtn1;

    @FindBy(css = "span[id$=current]")
    private WebElement selectedItem;

    public RichFacesPanelMenu getPanelMenu() {
        return panelMenu;
    }

    public RichFacesPanelMenuItem getItem3() {
        return item3;
    }

    public RichFacesPanelMenuItem getItem4() {
        return item4;
    }

    public RichFacesPanelMenuGroup getGroup1() {
        return group1;
    }

    public RichFacesPanelMenuGroup getGroup2() {
        return group2;
    }

    public RichFacesPanelMenuGroup getGroup3() {
        return group3;
    }

    public RichFacesPanelMenuItem getItem22() {
        return item22;
    }

    public RichFacesPanelMenuItem getItem25() {
        return item25;
    }

    public RichFacesPanelMenuGroup getGroup24() {
        return group24;
    }

    public RichFacesPanelMenuGroup getGroup26() {
        return group26;
    }

    public RichFacesPanelMenuItem getItem242() {
        return item242;
    }

    public RichFacesPanelMenuGroup getGroup4() {
        return group4;
    }

    public WebElement getExpandAll() {
        return expandAll;
    }

    public WebElement getExpandAllBtn1() {
        return expandAllBtn1;
    }

    public WebElement getCollapseAll() {
        return collapseAll;
    }

    public WebElement getCollapseAllBtn1() {
        return collapseAllBtn1;
    }

    public WebElement getSelecItem() {
        return selecItem;
    }

    public WebElement getSelectItemBtn1() {
        return selectItemBtn1;
    }

    public WebElement getSelectedItem() {
        return selectedItem;
    }
}
