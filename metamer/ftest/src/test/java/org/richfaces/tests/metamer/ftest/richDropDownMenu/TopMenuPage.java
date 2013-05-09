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
    public RichFacesDropDownMenuInternal fileDropDownMenu;

    @FindBy(jquery = ".rf-ddm-lbl-dec:eq(0)")
    public WebElement target1;

    @FindBy(jquery = "span[id$=output]")
    public WebElement output;

    @FindBy(css = "div.rf-ddm-lst")
    public WebElement dropDownMenuContent;

    @FindBy(jquery = "img:contains('File')")
    public WebElement fileMenuLabel;

    @FindBy(jquery = "div[id$=menu1]")
    public WebElement fileMenu;

    @FindBy(jquery = "div[id$=menu1_list]")
    public WebElement fileMenuList;

    @FindBy(jquery = "div[id$=menuGroup4]")
    public WebElement group;

    @FindBy(jquery = "div[id$=menuGroup4_list]")
    public WebElement groupList;

    @FindBy(jquery = "div[id$=menu1] img.pic")
    public WebElement icon;

    @FindBy(jquery = "div[id$=menu1] span.rf-ddm-itm-ic > div.rf-ddm-emptyIcon")
    public WebElement emptyIcon;

    @FindBy(jquery = "div[id$=menuItem41]")
    public WebElement menuItem41;

    @FindBy(jquery = " div[id$=menu1] div.rf-ddm-lbl-dec")
    public WebElement fileMenuLabelOriginal;

    @FindBy(tagName = "body")
    public WebElement body;

    private static final long SHOW_DELAY_TOLERANCE = 600;

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
        assertEquals(getActualShowDelay(expected), expected, SHOW_DELAY_TOLERANCE);
    }
}
