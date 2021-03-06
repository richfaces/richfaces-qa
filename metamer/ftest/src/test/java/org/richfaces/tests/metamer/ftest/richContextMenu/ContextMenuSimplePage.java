/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.DriverType;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * Page object rich:contextMenu component at faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @since 4.2.1.Final
 */
public class ContextMenuSimplePage extends MetamerPage {

    public static final int SHOW_DELAY_TOLERANCE = 600;

    @FindBy(css = "div[id$=ctxMenu]")
    private RichFacesContextMenu contextMenu;
    @FindBy(css = "div.rf-ctx-lst")
    private WebElement contextMenuContent;
    @FindBy(css ="div[id$=':ctxMenu']")
    private WebElement contextMenuRoot;
    @FindBy(css ="div[id$=menuGroup4_list]")
    private WebElement groupList;
    @FindBy(css ="span[id$=output]")
    private WebElement output;
    @FindBy(css = "div[id$=targetPanel1]")
    private WebElement targetPanel1;
    @FindBy(css = "div[id$=targetPanel2]")
    private WebElement targetPanel2;

    public static String trimTheRGBAColor(String original) {
        return original.replaceAll("\\s", "");
    }

    public void clickOnFirstPanel(DriverType type) {
        if (type == DriverType.InternetExplorer) {
            targetPanel1.sendKeys(Keys.CONTROL);
        }
        targetPanel1.click();
    }

    public void clickOnSecondPanel(DriverType type) {
        if (type == DriverType.InternetExplorer) {
            targetPanel2.sendKeys(Keys.CONTROL);
        }
        targetPanel2.click();
    }

    public RichFacesContextMenu getContextMenu() {
        return contextMenu;
    }

    public WebElement getContextMenuContent() {
        return contextMenuContent;
    }

    public Locations getContextMenuLocations() {
        contextMenu.advanced().show(targetPanel2);
        Locations contextMenuLocations = Utils.getLocations(contextMenuContent);
        contextMenu.advanced().hide();
        return contextMenuLocations;
    }

    public WebElement getContextMenuRoot() {
        return contextMenuRoot;
    }

    public WebElement getGroupList() {
        return groupList;
    }

    public WebElement getOutput() {
        return output;
    }

    public WebElement getTargetPanel1() {
        return targetPanel1;
    }

    public WebElement getTargetPanel2() {
        return targetPanel2;
    }

    public void waitUntilContextMenuAppears() {
        Graphene.waitModel().withMessage("Context menu did not show.").until().element(contextMenuContent).is().visible();
    }

    public void waitUntilContextMenuHides() {
        Graphene.waitModel().withMessage("Context menu should not be visible.")
            .until().element(contextMenuContent).is().not().visible();
    }
}
