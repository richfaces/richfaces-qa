/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/** Only works with JSF 2.2+
 * Tests @FlowScoped backing bean with RF stateful component (tabPanel)
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestJSF22FlowScopedAnnotation extends AbstractWebDriverTest {

    private final String firstInput = "Voodoo string";
    private final String secondInput = "Black magic";

    @FindByJQuery(value = "div[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;

    @FindByJQuery(value = "input[id$='indexPageButton']")
    private WebElement startingPageButton;

    @FindByJQuery(value = "input[id$='tabPageButton']")
    private WebElement tabPageButton;

    @FindByJQuery(value = "input[id$='secondInputPageButton']")
    private WebElement goToSummaryButton;

    @FindByJQuery(value = "input[id$='backToTabButton']")
    private WebElement backToTabPageButton;

    @FindByJQuery(value = "input[id$='exitFlowButton']")
    private WebElement exitFlowButton;

    @FindByJQuery(value = "input[id$='tabInput']")
    private WebElement firstFlowPageInput;

    @FindByJQuery(value = "input[id$='secondInput']")
    private WebElement secondFlowPageInput;

    @FindByJQuery(value = "[id$='firstOutput']")
    private WebElement firstOutput;

    @FindByJQuery(value = "[id$='secondOutput']")
    private WebElement secondOutput;

    @Override
    public String getComponentTestPagePath() {
        return "richTabPanel/jsfFlowScoped.xhtml";
    }

    @Test
    @Templates(value = "plain")
    // TODO add annotation to run with JSF 2.2+ ONLY
    public void testFlowScopedAnnotation() {

        // verify that first page is index page
        assertTrue(startingPageButton.isDisplayed());

        // start the flow
        Graphene.guardHttp(startingPageButton).click();
        Graphene.waitGui(driver).until().element(tabPageButton).is().visible();

        // verify that there is a tab panel and that the first tab is active
        assertTrue(tabPanel.advanced().getVisibleContent().isDisplayed(), "Tab panel should be present on the page");
        assertTrue(tabPanel.advanced().getActiveHeaderElement().getText().equals("First tab"), "First tab should be active");

        // switch tabs and send input
        tabPanel.switchTo("Second tab");
        Graphene.waitGui().until().element(tabPanel.advanced().getActiveHeaderElement()).text().contains("Second tab");
        assertTrue(firstFlowPageInput.isDisplayed(), "Tab panel input should be visible now");
        firstFlowPageInput.sendKeys(firstInput);

        // go to next flow page
        Graphene.guardHttp(tabPageButton).click();
        Graphene.waitModel().until().element(secondFlowPageInput).is().present();

        // verify first input value
        assertTrue(firstOutput.getText().contains(firstInput), "First input should be changed by now");

        // go to previous page, verify second tab is active, go to next page
        Graphene.guardHttp(backToTabPageButton).click();
        Graphene.waitGui().until().element(tabPanel.advanced().getActiveHeaderElement()).text().contains("Second tab");
        Graphene.guardHttp(tabPageButton).click();
        Graphene.waitModel().until().element(secondFlowPageInput).is().present();

        // enter second input and go forward
        secondFlowPageInput.sendKeys(secondInput);
        Graphene.guardHttp(goToSummaryButton).click();
        Graphene.waitGui().until().element(exitFlowButton).is().present();

        // verify both inputs are there
        assertTrue(firstOutput.getText().contains(firstInput), "First input was not changed correctly");
        assertTrue(secondOutput.getText().contains(secondInput), "Second input was not changed correctly");

        // exit the flow and verify you landed on index page again
        Graphene.guardHttp(exitFlowButton).click();
        Graphene.waitGui(driver).until().element(startingPageButton).is().visible();
    }
}
