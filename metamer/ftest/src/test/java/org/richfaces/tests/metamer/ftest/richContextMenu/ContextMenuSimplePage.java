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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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

    @FindBy(css = "div[id$=targetPanel1]")
    public WebElement targetPanel1;
    @FindBy(css = "div[id$=targetPanel2]")
    public WebElement targetPanel2;
    @FindBy(css = "div.rf-ctx-lst")
    public WebElement contextMenuContent;

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

    public void waitUntilContextMenuAppears() {
        Graphene.waitModel().withMessage("Context menu did not show.")
                .until(Graphene.element(contextMenuContent).isVisible());
    }

    public void waitUntilContextMenuHides() {
        Graphene.waitModel().withMessage("Context menu should not be visible.")
                .until(Graphene.element(contextMenuContent).not().isVisible());
    }
}
