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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.AndExpression;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * Only works with JSF 2.2+.
 *
 * Tests @FlowScoped backing bean with RF stateful component (tabPanel)
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestJSF22FlowScopedAnnotation extends AbstractWebDriverTest {

    private static final String TEXT_1 = "Voodoo string";
    private static final String TEXT_2 = "Black magic";

    @FindBy(css = "input[id$='exitFlowButton']")
    private WebElement exitFlowButton;
    @FindBy(css = "input[id$='tabInput']")
    private WebElement firstFlowPageInput;
    @FindBy(css = "[id$='firstOutput']")
    private WebElement firstOutput;
    @FindBy(css = "input[id$='secondInputPageButton']")
    private WebElement goToSummaryButton;
    @FindBy(css = "input[id$='tabPageButton']")
    private WebElement nextPageButton;
    @FindBy(css = "input[id$='backToTabButton']")
    private WebElement previousPageButton;
    @FindBy(css = "input[id$='secondInput']")
    private WebElement secondFlowPageInput;
    @FindBy(css = "[id$='secondOutput']")
    private WebElement secondOutput;
    @FindBy(css = "input[id$='indexPageButton']")
    private WebElement startFlowButton;
    @FindBy(css = "div[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;

    @Override
    public String getComponentTestPagePath() {
        return "richTabPanel/jsfFlowScoped.xhtml";
    }

    @Test
    @Templates(value = "plain")
    @Skip(expressions = {
        @AndExpression(On.JSF.VersionLowerThan22.class),
        @AndExpression(On.JSF.MyFaces.class)// https://issues.jboss.org/browse/RFPL-3999
    })
    public void testFlowScopedAnnotation() {
        // verify that you are on the first page
        assertTrue(startFlowButton.isDisplayed());

        // start the flow
        Graphene.guardHttp(startFlowButton).click();
        Graphene.waitGui(driver).until().element(nextPageButton).is().visible();

        // verify that there is a tab panel and that the first tab is active
        assertTrue(tabPanel.advanced().getVisibleContent().isDisplayed(), "Tab panel should be present on the page");
        assertEquals(tabPanel.advanced().getActiveHeaderElement().getText().trim(), "First tab", "First tab should be active");

        // switch tabs and send input
        tabPanel.switchTo("Second tab");
        Graphene.waitGui().until().element(tabPanel.advanced().getActiveHeaderElement()).text().contains("Second tab");
        assertTrue(firstFlowPageInput.isDisplayed(), "Tab panel input should be visible now");
        firstFlowPageInput.clear();
        firstFlowPageInput.sendKeys(TEXT_1);

        // go to next flow page
        Graphene.guardHttp(nextPageButton).click();
        Graphene.waitModel().until().element(secondFlowPageInput).is().present();

        // verify first input value
        assertEquals(firstOutput.getText().trim(), TEXT_1, "First input should be changed by now");

        // go to previous page, verify second tab is active, go to next page
        Graphene.guardHttp(previousPageButton).click();
        Graphene.waitGui().until().element(tabPanel.advanced().getActiveHeaderElement()).text().contains("Second tab");
        Graphene.guardHttp(nextPageButton).click();
        Graphene.waitModel().until().element(secondFlowPageInput).is().present();

        // enter second input and go forward
        secondFlowPageInput.clear();
        secondFlowPageInput.sendKeys(TEXT_2);
        Graphene.guardHttp(goToSummaryButton).click();
        Graphene.waitGui().until().element(exitFlowButton).is().present();

        // verify both inputs are there
        assertEquals(firstOutput.getText().trim(), TEXT_1, "First input was not changed correctly");
        assertEquals(secondOutput.getText().trim(), TEXT_2, "Second input was not changed correctly");

        // exit the flow and verify you landed on index page again
        Graphene.guardHttp(exitFlowButton).click();
        Graphene.waitGui(driver).until().element(startFlowButton).is().visible();
    }
}
