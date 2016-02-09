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
package org.richfaces.tests.metamer.ftest.richPopupPanel;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.panel.TextualFragmentPart;
import org.richfaces.fragment.popupPanel.RichFacesPopupPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richPopupPanel.TestPopupPanel.TestedPopupPanelHeaderControls;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestRF14159 extends AbstractWebDriverTest {

    private static final int LINE_SIZE_PX = 16;

    @FindBy(css = "[id$=openPanelButton]")
    private WebElement openPanelButton;
    @FindBy(className = "rf-pp-cntr")
    private TestedPanel panel;

    @Override
    public String getComponentTestPagePath() {
        return "richPopupPanel/rf-14159.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14159")
    public void testAutosizedPanelShouldResizeAfterRerender() {
        final int tolerance = 5;
        // open popup panel
        openPanelButton.click();
        panel.advanced().waitUntilPopupIsVisible().perform();

        // save height of the panel's content
        int height = Utils.getLocations(panel.advanced().getContentScrollerElement()).getHeight();

        TestedPanelContent bodyContent = panel.getBodyContent();
        // set invalid value to the input
        bodyContent.getInput().clear().sendKeys("a");
        // submit
        Graphene.guardAjax(bodyContent.getSubmitButton()).click();
        // validation error message should appear
        bodyContent.getMsg().advanced().waitUntilMessageIsVisible().perform();
        // panel should resize <<< here is the issue
        assertEquals(Utils.getLocations(panel.advanced().getContentScrollerElement()).getHeight(), height + LINE_SIZE_PX,
            tolerance, "Panel should resize!");

        // set valid value to the input
        bodyContent.getInput().clear().sendKeys("abcd");
        // submit
        Graphene.guardAjax(bodyContent.getSubmitButton()).click();
        // popup panel should hide
        panel.advanced().waitUntilPopupIsNotVisible().perform();
    }

    public static class TestedPanel extends RichFacesPopupPanel<TextualFragmentPart, TestedPopupPanelHeaderControls, TestedPanelContent> {
    }

    public static class TestedPanelContent {

        @Root
        private WebElement root;
        @FindBy(css = "[id$=input]")
        private TextInputComponentImpl input;
        @FindBy(css = "[id$='msg']")
        private RichFacesMessage msg;
        @FindBy(css = "[id$='submit']")
        private WebElement submitButton;

        public TextInputComponentImpl getInput() {
            return input;
        }

        public RichFacesMessage getMsg() {
            return msg;
        }

        public WebElement getRoot() {
            return root;
        }

        public WebElement getSubmitButton() {
            return submitButton;
        }
    }
}
