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
package org.richfaces.tests.showcase.ftest.webdriver.page.richPanel;

import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractWebDriverPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SimplePage extends AbstractWebDriverPage {

    @FindBy(xpath = "//*[@class='example-cnt']//div[@class='rf-p '][1]")
    private WebElement firstPanel;
    @FindBy(xpath = "//*[@class='example-cnt']//div[@class='rf-p '][2]")
    private WebElement secondPanel;

    public SimplePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getDemoName() {
        return "panel";
    }
    @Override
    public String getSampleName() {
        return "simple";
    }

    public boolean hasFirstPanelHeader() {
        return hasPanelHeader(firstPanel);
    }

    public boolean hasSecondPanelHeader() {
        return hasPanelHeader(secondPanel);
    }

    public boolean isFirstPanelPresent() {
        return isPanelPresent(firstPanel);
    }

    public boolean isSecondPanelPresent() {
        return isPanelPresent(secondPanel);
    }

    private boolean hasPanelHeader(WebElement panel) {
        try {
            panel.findElement(By.xpath("div[@class='rf-p-hdr ']"));
            return true;
        } catch(NoSuchElementException ignored) {
            return false;
        }
    }

    private boolean isPanelPresent(WebElement panel) {
        return ElementPresent.getInstance().element(panel).apply(getWebDriver());
    }

}
