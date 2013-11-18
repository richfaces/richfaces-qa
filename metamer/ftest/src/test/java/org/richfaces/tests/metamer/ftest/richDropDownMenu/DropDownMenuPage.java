/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.dropDownMenuAttributes;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dropDownMenu.RichFacesDropDownMenu;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * Universal page for both - side and top drop down menu testing
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class DropDownMenuPage extends MetamerPage {
    @FindByJQuery(".rf-tb-itm:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenuTop;

    @FindByJQuery(".optionList:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenuSide;

    @FindByJQuery(".rf-ddm-lbl-dec:eq(0)")
    private WebElement target1;

    @FindByJQuery("span[id$=output]")
    private WebElement output;

    @FindBy(css = "div.rf-ddm-lst")
    private WebElement dropDownMenuContent;

    @FindByJQuery("img:contains('File')")
    private WebElement fileMenuLabel;

    @FindByJQuery("div[id$=menu1]")
    private WebElement fileMenu;

    @FindByJQuery("div[id$=menu1_list]")
    private WebElement fileMenuList;

    @FindByJQuery("div[id$=menuGroup4]")
    private WebElement group;

    @FindByJQuery("div[id$=menuGroup4_list]")
    private WebElement groupList;

    @FindByJQuery("div[id$=menu1] img.pic")
    private WebElement icon;

    @FindByJQuery("div[id$=menu1] span.rf-ddm-itm-ic > div.rf-ddm-emptyIcon")
    private WebElement emptyIcon;

    @FindByJQuery("div[id$=menuItem41]")
    private WebElement menuItem41;

    @FindByJQuery(" div[id$=menu1] div.rf-ddm-lbl-dec")
    private WebElement fileMenuLabelOriginal;

    @FindBy(tagName = "body")
    private WebElement body;

    public WebElement getBody() {
        return body;
    }

    public WebElement getDropDownMenuContent() {
        return dropDownMenuContent;
    }

    public WebElement getEmptyIcon() {
        return emptyIcon;
    }

    /**
     * Returns RichFacesDropDownMenu page fragment based on current url. This is because locators on both pages slightly differ.
     *
     * @param testedPage Adress of tested page as string
     * @return Drop down menu page fragment
     */
    public RichFacesDropDownMenu getFileDropDownMenu(String testedPage) {
        if (testedPage.contains("sideMenu.xhtml")) {
            return fileDropDownMenuSide;
        } else {
            return fileDropDownMenuTop;
        }
    }

    public WebElement getFileMenu() {
        return fileMenu;
    }

    public WebElement getFileMenuLabel() {
        return fileMenuLabel;
    }

    public WebElement getFileMenuLabelOriginal() {
        return fileMenuLabelOriginal;
    }

    public WebElement getFileMenuList() {
        return fileMenuList;
    }

    public WebElement getGroup() {
        return group;
    }

    public WebElement getGroupList() {
        return groupList;
    }

    public WebElement getIcon() {
        return icon;
    }

    public WebElement getMenuItem41() {
        return menuItem41;
    }

    public WebElement getOutput() {
        return output;
    }

    public WebElement getTarget1() {
        return target1;
    }

    public String returnPopupWidth(String minWidth, RichFacesDropDownMenu dropDownMenu) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.popupWidth, minWidth);
        dropDownMenu.advanced().show(target1);
        return dropDownMenuContent.getCssValue("min-width");
    }
}
