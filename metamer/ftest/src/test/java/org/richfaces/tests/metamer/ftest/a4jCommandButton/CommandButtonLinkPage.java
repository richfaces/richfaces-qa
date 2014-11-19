/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

    @FindBy(css = "input[id$=input]")
    private WebElement inputElement;
    @FindBy(css = "input[id$=a4jCommandButton]")
    private WebElement buttonElement;
    @FindBy(css = "a[id$=a4jCommandLink]")
    private WebElement linkElement;
    @FindBy(css = "span[id$=a4jCommandLink]")
    private WebElement disabledLinkElement;
    @FindBy(css = "span[id$=output1]")
    private WebElement output1Element;
    @FindBy(css = "span[id$=output2]")
    private WebElement output2Element;
    @FindBy(css = "span[id$=output3]")
    private WebElement output3Element;

    /**
     * @return the buttonElement
     */
    public WebElement getButtonElement() {
        return buttonElement;
    }

    /**
     * @return the disabledLinkElement
     */
    public WebElement getDisabledLinkElement() {
        return disabledLinkElement;
    }

    /**
     * @return the inputElement
     */
    public WebElement getInputElement() {
        return inputElement;
    }

    /**
     * @return the linkElement
     */
    public WebElement getLinkElement() {
        return linkElement;
    }

    /**
     * @return the output1Element
     */
    public WebElement getOutput1Element() {
        return output1Element;
    }

    /**
     * @return the output2Element
     */
    public WebElement getOutput2Element() {
        return output2Element;
    }

    /**
     * @return the output3Element
     */
    public WebElement getOutput3Element() {
        return output3Element;
    }

    public void typeToInput(String s) {
        getInputElement().clear();
        getInputElement().sendKeys(s);
    }

    public void submitByButton() {
        Graphene.guardAjax(getButtonElement()).click();
    }

    public void submitByLink() {
        Graphene.guardAjax(getLinkElement()).click();
    }

    public void waitUntilOutput1Changes(String expectedText) {
        Graphene.waitAjax().until().element(getOutput1Element()).text().equalTo(expectedText);
    }

    public void waitUntilOutput2ChangesToText(String expectedText) {
        Graphene.waitModel().until().element(getOutput2Element()).text().equalTo(expectedText);
    }

    public void typeToInputAndSubmitWithoutRequest(String s) {
        getInputElement().clear();
        getInputElement().sendKeys(s);
        Graphene.guardNoRequest(getButtonElement()).click();
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
        verifyOutputText(getOutput1Element(), s);
    }

    public void verifyOutput2Text(String s) {
        verifyOutputText(getOutput2Element(), s);
    }

    public void verifyOutput3Text(String s) {
        verifyOutputText(getOutput3Element(), s);
    }

    private void verifyOutputText(WebElement elem, String text) {
        assertEquals(elem.getText(), text);
    }
}
