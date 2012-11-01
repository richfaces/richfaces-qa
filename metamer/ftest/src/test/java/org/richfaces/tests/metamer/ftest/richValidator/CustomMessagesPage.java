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
package org.richfaces.tests.metamer.ftest.richValidator;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class CustomMessagesPage extends MetamerPage {

    public static final String CUSTOM_MESSAGE_TEXT = "Custom message";

    @FindBy(id="setCorrectValuesButton")
    private WebElement setAllCorrectButton;
    @FindBy(id="setWrongValuesButton1")
    private WebElement setAllWrongButton1;
    @FindBy(id="setWrongValuesButton2")
    private WebElement setAllWrongButton2;

    @FindBy(css="input[id$='hButton']")
    private WebElement hButton;
    @FindBy(css="input[id$='a4jButton']")
    private WebElement a4jButton;

    @FindBy(css="span[id$=doubleRangeMessage] > span > span")
    private WebElement doubleRangeMessage;
    @FindBy(css="span[id$=longRangeMessage] > span > span")
    private WebElement longRangeMessage;
    @FindBy(css="span[id$=lengthMessage] > span > span")
    private WebElement lengthMessage;
    @FindBy(css="span[id$=regexpMessage] > span > span")
    private WebElement regexpMessage;
    @FindBy(css="span[id$=requiredMessage] > span > span")
    private WebElement requiredMessage;
    @FindBy(css="span[id$=customMessage] > span > span")
    private WebElement customMessage;

    public void setAllCorrect() {
        setAllCorrectButton.click();
    }

    public void setAllWrong1() {
        setAllWrongButton1.click();
    }

    public void setAllWrong2() {
        setAllWrongButton2.click();
    }

    public String getDoubleRangeMessageText() {
        return doubleRangeMessage.getText();
    }

    public String getLongRangeMessageText() {
        return longRangeMessage.getText();
    }

    public String getLengthMessageText() {
        return lengthMessage.getText();
    }

    public String getRegexpMessageText() {
        return regexpMessage.getText();
    }

    public String getRequiredMessageText() {
        return requiredMessage.getText();
    }

    public String getCustomMessageText() {
        return customMessage.getText();
    }

    public void submitByHButton() {
        hButton.click();
        waitForMessages(Graphene.waitModel());
    }

    public void submitByA4jButton() {
        a4jButton.click();
        waitForMessages(Graphene.waitAjax());
    }

    private void waitForMessages(WebDriverWait wait) {
        wait.until(Graphene.element(doubleRangeMessage).isPresent());
    }
}
