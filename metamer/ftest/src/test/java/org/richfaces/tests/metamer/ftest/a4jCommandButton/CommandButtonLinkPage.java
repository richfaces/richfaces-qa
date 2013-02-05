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
package org.richfaces.tests.metamer.ftest.a4jCommandButton;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class CommandButtonLinkPage extends MetamerPage {

    public static final String STRING_RF1 = "RichFaces 4";
    public static final String STRING_RF1_X2 = "RichFaces 4RichFaces 4";
    public static final String STRING_RF2 = "RichFa";//first 6 characters
    public static final String STRING_RF3 = "RICHFACES 4";
    public static final String STRING_RF_UNICODE = "RichFaces 4š";
    public static final String STRING_RF_UNICODE_UPPERCASE = "RICHFACES 4Š";
    public static final String STRING_UNICODE1 = "ľščťžýáíéňô";
    public static final String STRING_UNICODE2 = "ľščťžý";
    public static final String STRING_UNICODE3 = "ĽŠČŤŽÝÁÍÉŇÔ";
    public static final String STRING_ACTIONLISTENER_MSG = "action listener invoked";
    public static final String STRING_ACTION_MSG = "action invoked";
    public static final String STRING_EXECUTE_CHECKER_MSG = "executeChecker";

    @FindBy(css = "input[id$=input]")
    public WebElement input;
    @FindBy(css = "input[id$=a4jCommandButton]")
    public WebElement button;
    @FindBy(css = "a[id$=a4jCommandLink]")
    public WebElement link;
    @FindBy(css = "span[id$=a4jCommandLink]")
    public WebElement disabledLink;
    @FindBy(css = "span[id$=output1]")
    public WebElement output1;
    @FindBy(css = "span[id$=output2]")
    public WebElement output2;
    @FindBy(css = "span[id$=output3]")
    public WebElement output3;

    public void typeToInput(String s) {
        input.clear();
        input.sendKeys(s);
    }

    public void submitByButton() {
        Graphene.guardXhr(button).click();
    }

    public void submitByLink() {
        Graphene.guardXhr(link).click();
    }

    public void waitUntilOutput1Changes(String expectedText) {
        Graphene.waitAjax().until().element(output1).text().equalTo(expectedText);
    }

    public void waitUntilOutput2ChangesToText(String expectedText) {
        Graphene.waitModel().until().element(output2).text().equalTo(expectedText);
    }

    public void typeToInputAndSubmitWithoutRequest(String s) {
        input.clear();
        input.sendKeys(s);
        Graphene.guardNoRequest(button).click();
    }

    public void verifyOutput1Text() {
        verifyOutput1Text(STRING_RF1);
    }

    public void verifyOutput2Text() {
        verifyOutput2Text(STRING_RF2);
    }

    public void verifyOutput3Text() {
        verifyOutput3Text(STRING_RF3);
    }

    public void verifyOutput1Text(String s) {
        verifyOutputText(output1, s);
    }

    public void verifyOutput2Text(String s) {
        verifyOutputText(output2, s);
    }

    public void verifyOutput3Text(String s) {
        verifyOutputText(output3, s);
    }

    private void verifyOutputText(WebElement elem, String text) {
        assertEquals(elem.getText(), text);
    }
}
