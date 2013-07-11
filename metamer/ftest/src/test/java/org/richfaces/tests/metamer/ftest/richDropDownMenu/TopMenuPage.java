/**
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
 */
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.dropDownMenuAttributes;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StopWatch;
import org.richfaces.tests.page.fragments.impl.dropDownMenu.internal.RichFacesDropDownMenuInternal;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TopMenuPage extends MetamerPage {

    @FindBy(jquery = ".rf-tb-itm:eq(0)")
    private RichFacesDropDownMenuInternal fileDropDownMenu;

    @FindBy(jquery = ".rf-ddm-lbl-dec:eq(0)")
    private WebElement target1;

    @FindBy(jquery = "span[id$=output]")
    private WebElement output;

    @FindBy(css = "div.rf-ddm-lst")
    private WebElement dropDownMenuContent;

    @FindBy(jquery = "img:contains('File')")
    private WebElement fileMenuLabel;

    @FindBy(jquery = "div[id$=menu1]")
    private WebElement fileMenu;

    @FindBy(jquery = "div[id$=menu1_list]")
    private WebElement fileMenuList;

    @FindBy(jquery = "div[id$=menuGroup4]")
    private WebElement group;

    @FindBy(jquery = "div[id$=menuGroup4_list]")
    private WebElement groupList;

    @FindBy(jquery = "div[id$=menu1] img.pic")
    private WebElement icon;

    @FindBy(jquery = "div[id$=menu1] span.rf-ddm-itm-ic > div.rf-ddm-emptyIcon")
    private WebElement emptyIcon;

    @FindBy(jquery = "div[id$=menuItem41]")
    private WebElement menuItem41;

    @FindBy(jquery = " div[id$=menu1] div.rf-ddm-lbl-dec")
    private WebElement fileMenuLabelOriginal;

    @FindBy(tagName = "body")
    private WebElement body;

    private static final double SHOW_DELAY_TOLERANCE_MODIFIER = 0.5;

    public WebElement getBody() {
        return body;
    }

    public WebElement getDropDownMenuContent() {
        return dropDownMenuContent;
    }

    public WebElement getEmptyIcon() {
        return emptyIcon;
    }

    public RichFacesDropDownMenuInternal getFileDropDownMenu() {
        return fileDropDownMenu;
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

    public String returnPopupWidth(String minWidth) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.popupWidth, minWidth);
        fileDropDownMenu.invoke(target1);
        return dropDownMenuContent.getCssValue("min-width");
    }

    public int getActualShowDelay(final int showDelay) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.showDelay, showDelay);
        return StopWatch.watchTimeSpentInAction(new StopWatch.PerformableAction() {
            @Override
            public void perform() {
                fileDropDownMenu.invoke(target1);
            }
        }).inMillis().intValue();
    }

    public void checkShowDelay(int expected) {
        assertEquals(getActualShowDelay(expected), expected, expected * SHOW_DELAY_TOLERANCE_MODIFIER);
    }

}
