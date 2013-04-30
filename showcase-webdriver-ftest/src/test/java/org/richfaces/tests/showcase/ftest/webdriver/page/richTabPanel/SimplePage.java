/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
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
 */
package org.richfaces.tests.showcase.ftest.webdriver.page.richTabPanel;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractWebDriverPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimplePage extends AbstractWebDriverPage {

    @FindBy(xpath = "//div[@class='rf-tab'][1]")
    private WebElement firstPanel;
    @FindBy(xpath = "//div[@class='rf-tab'][2]")
    private WebElement secondPanel;
    @FindBy(xpath = "//td[@class='rf-tab-hdr rf-tab-hdr-act rf-tab-hdr-top'][1]")
    private WebElement firstTabActive;
    @FindBy(xpath = "//td[@class='rf-tab-hdr rf-tab-hdr-inact rf-tab-hdr-top'][1]")
    private WebElement firstTabInActive;
    @FindBy(xpath = "//td[@class='rf-tab-hdr rf-tab-hdr-act rf-tab-hdr-top'][2]")
    private WebElement secondTabActive;
    @FindBy(xpath = "//td[@class='rf-tab-hdr rf-tab-hdr-inact rf-tab-hdr-top'][2]")
    private WebElement secondTabInActive;

    @Override
    public String getDemoName() {
        return "tabPanel";
    }

    @Override
    public String getSampleName() {
        return "simple";
    }

    public boolean isFirstPanelOpened() {
        return isPanelOpened(firstPanel);
    }

    public boolean isSecondPanelOpened() {
        return isPanelOpened(secondPanel);
    }

    public boolean isFirstTabFocused() {
        return isTabFocused(firstTabActive);
    }

    public boolean isSecondTabFocused() {
        return isTabFocused(secondTabActive);
    }

    public void openFirstPanel() {
        openPanel(firstPanel, firstTabInActive);
    }

    public void openSecondPanel() {
        openPanel(secondPanel, secondTabInActive);
    }

    private boolean isPanelOpened(WebElement panel) {
        return Graphene.element(panel).isVisible().apply(getWebDriver());
    }

    private boolean isTabFocused(WebElement tab) {
        return Graphene.element(tab).isVisible().apply(getWebDriver());
    }

    private void openPanel(WebElement panel, WebElement tab) {
        tab.click();
        Graphene.waitAjax()
                .withMessage("The panel can't be opened.")
                .until(Graphene.element(panel).isVisible());
    }
}
