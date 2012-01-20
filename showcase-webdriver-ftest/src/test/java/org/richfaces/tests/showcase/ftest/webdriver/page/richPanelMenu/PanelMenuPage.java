/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.page.richPanelMenu;

import org.jboss.test.selenium.android.ToolKit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractAndroidPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class PanelMenuPage extends AbstractAndroidPage {

    @FindBy(xpath = "//*[@class='example-cnt']//div[@class='rf-pm']")
    private WebElement panelMenuElement;
    private PanelMenu panelMenu;
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'current')]")
    private WebElement selectionElement;

    public PanelMenuPage(WebDriver webDriver, ToolKit toolKit) {
        super(webDriver, toolKit);
    }

    @Override
    public String getDemoName() {
        return "panelMenu";
    }

    @Override
    public String getSampleName() {
        return "panelMenu";
    }

    public String getCurrentSelection() {
        return selectionElement.getText();
    }

    public PanelMenu getPanelMenu() {
        if (panelMenu == null) {
            panelMenu = new PanelMenu(getWebDriver(), getToolKit(), panelMenuElement);
        }
        return panelMenu;
    }

}
