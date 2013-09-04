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
package org.richfaces.tests.showcase.ftest.webdriver.page.richCollapsiblePanel;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractWebDriverPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimplePage extends AbstractWebDriverPage {

    @FindBy(xpath = "//div[@class='rf-cp-lbl-colps'][contains(text(), 'Overview')]")
    private WebElement clientModePanelCollapsed;
    @FindBy(xpath = "//div[@class='rf-cp-lbl-exp'][contains(text(), 'Overview')]")
    private WebElement clientModePanelExpanded;
    @FindBy(xpath = "//div[@class='rf-cp-b'][contains( text(), 'RichFaces is a component library for JSF and an advanced framework')]")
    private WebElement clientModePanelContent;
    @FindBy(xpath = "//div[@class='rf-cp-b']")
    private List<WebElement> contentPanels;
    @FindBy(xpath = "//div[@class='rf-cp-lbl-colps'][contains(text(), 'JSF 2 and RichFaces 4')]")
    private WebElement ajaxModePanelCollapsed;
    @FindBy(xpath = "//div[@class='rf-cp-lbl-exp'][contains(text(), 'JSF 2 and RichFaces 4')]")
    private WebElement ajaxModePanelExpanded;

    @Override
    public String getDemoName() {
        return "collapsiblePanel";
    }

    @Override
    public String getSampleName() {
        return "simple";
    }

    public boolean isClientModePanelExpanded() {
        return isPanelHere(clientModePanelExpanded);
    }

    public boolean isClientModePanelCollapsed() {
        return isPanelHere(clientModePanelCollapsed);
    }

    public boolean isContentOfClientModePanelThere() {
        return isPanelHere(clientModePanelContent);
    }

    public boolean isContentOfAjaxModePanelThere() {
        return isAjaxPanelHere();
    }

    public boolean isAjaxModePanelExpanded() {
        return isPanelHere(ajaxModePanelExpanded);
    }

    public boolean isAjaxModePanelCollapsed() {
        return isPanelHere(ajaxModePanelCollapsed);
    }

    public void openClientModePanel() {
        openPanel(clientModePanelExpanded, clientModePanelCollapsed);
    }

    public void closeClientModePanel() {
        openPanel(clientModePanelCollapsed, clientModePanelExpanded);
    }

    public void openAjaxModePanel() {
        openAjaxPanel(ajaxModePanelExpanded, ajaxModePanelCollapsed);
    }

    public void closeAjaxModePanel() {
        openPanel(ajaxModePanelCollapsed, ajaxModePanelExpanded);
    }

    private boolean isAjaxPanelHere() {
        //there should be 2 panels if the ajax one is opened. The text comparation may be unnecessary.
        if (contentPanels.size() == 2 && contentPanels.get(1).getText().contains("We are working hard on RichFaces 4.0")) {
            return true;
        }
        return false;
        // this does not work :  return (ElementPresent.getInstance().element(panel).apply(getWebDriver()) ? new WebElementConditionFactory(panel).apply(getWebDriver()) : false);
    }

    private boolean isPanelHere(WebElement panel) {
        return new WebElementConditionFactory(panel).isVisible().apply(getWebDriver());
    }

    private void openPanel(WebElement panel, WebElement tab) {
        tab.click();
        Graphene.waitAjax()
                .withMessage("The panel can't be opened.")
                .until(new WebElementConditionFactory(panel).isVisible());
    }

    private void openAjaxPanel(WebElement panel, WebElement tab) {
        tab.click();
        Graphene.waitAjax()
                .withMessage("The panel can't be opened.")
                .until(new WebElementConditionFactory(panel).isVisible());
    }
}
