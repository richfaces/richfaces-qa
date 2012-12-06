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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.bean.abstractions.StringInputValidationBean;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class InputValidationPage extends MetamerPage {

    private static final String JS_COMPLETED_STATE_STRING = "completed";
    private static final String JS_STATE_VARIABLE = "document.valuesSettingState";
    private static final String CUSTOM_STRING_VALIDATION_MSG = "string is not \"RichFaces\"";
    //
    @FindBy(css = "input[id$=hButton]")
    private WebElement hCommandButton;
    @FindBy(css = "input[id$=a4jButton]")
    private WebElement a4jCommandButton;
    //
    @FindBy(css = "span[id$=notEmptyMsg]")
    private RichFacesMessage messageNotEmpty;
    @FindBy(css = "span[id$=patternMsg] ")
    private RichFacesMessage messageRegExpPattern;
    @FindBy(css = "span[id$=sizeMsg]")
    private RichFacesMessage messageStringSize;
    @FindBy(css = "span[id$=customMsg]")
    private RichFacesMessage messageCustomString;
    //
    @FindBy(css = "input[id$=setAllCorrectButton]")
    private WebElement setAllCorrectButton;
    @FindBy(css = "input[id$=setAllWrongButton]")
    private WebElement setAllWrongButton;
    @FindBy(css = "input[id$=notEmptyCorrect]")
    private WebElement setNotEmptyCorrectButton;
    @FindBy(css = "input[id$=notEmptyWrong]")
    private WebElement setNotEmptyWrongButton;
    @FindBy(css = "input[id$=patternCorrect]")
    private WebElement setPatternCorrectButton;
    @FindBy(css = "input[id$=patternWrong]")
    private WebElement setPatternWrongButton;
    @FindBy(css = "input[id$=sizeCorrect]")
    private WebElement setSizeCorrectButton;
    @FindBy(css = "input[id$=sizeWrong]")
    private WebElement setSizeWrongButton;
    @FindBy(css = "input[id$=customCorrect]")
    private WebElement setCustomCorrectButton;
    @FindBy(css = "input[id$=customWrong]")
    private WebElement setCustomWrongButton;
    //

    public WebElement getA4jCommandButton() {
        return a4jCommandButton;
    }

    public WebElement getHCommandButton() {
        return hCommandButton;
    }

    public boolean isCustomStringInputMessageVisible() {
        return messageCustomString.isVisible();
    }

    public boolean isNotEmptyInputMessageVisible() {
        return messageNotEmpty.isVisible();
    }

    public boolean isRegExpPatternInputMessageVisible() {
        return messageRegExpPattern.isVisible();
    }

    public boolean isStringSizeInputMessageVisible() {
        return messageStringSize.isVisible();
    }

    private void clickJSSetValueButton(WebElement btn) {
        btn.click();
        waitForSetting();
    }

    public void setAllCorrectly() {
        clickJSSetValueButton(setAllCorrectButton);
    }

    public void setAllWrongly() {
        clickJSSetValueButton(setAllWrongButton);
    }

    public void setCustomStringInputCorrectly() {
        clickJSSetValueButton(setCustomCorrectButton);
    }

    public void setCustomStringInputWrongly() {
        clickJSSetValueButton(setCustomWrongButton);
    }

    public void setNotEmptyInputCorrectly() {
        clickJSSetValueButton(setNotEmptyCorrectButton);
    }

    public void setNotEmptyInputWrongly() {
        clickJSSetValueButton(setNotEmptyWrongButton);
    }

    public void setRegExpPatternInputCorrectly() {
        clickJSSetValueButton(setPatternCorrectButton);
    }

    public void setRegExpPatternInputWrongly() {
        clickJSSetValueButton(setPatternWrongButton);
    }

    public void setStringSizeInputCorrectly() {
        clickJSSetValueButton(setSizeCorrectButton);
    }

    public void setStringSizeInputWrongly() {
        clickJSSetValueButton(setSizeWrongButton);
    }

    public void waitForCustomStringInputMessage(WebDriverWait wait) {
        wait.until(messageCustomString.isDetailVisibleCondition());
        assertEquals(messageCustomString.getDetail(), CUSTOM_STRING_VALIDATION_MSG);
    }

    public void waitForCustomStringInputWithoutMessage(WebDriverWait wait) {
        wait.until(messageCustomString.isNotVisibleCondition());
    }

    public void waitForNotEmptyInputMessage(WebDriverWait wait) {
        wait.until(messageNotEmpty.isDetailVisibleCondition());
        String detail = messageNotEmpty.getDetail();
        boolean forAssertion = detail.equals(
                StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG)
                || detail.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG2)
                || detail.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG3);
        assertTrue(forAssertion, "Message did show with incorrect message " + detail);
    }

    public void waitForNotEmptyInputWithoutMessage(WebDriverWait wait) {
        wait.until(messageNotEmpty.isNotVisibleCondition());
    }

    public void waitForRegExpPatternInputMessage(WebDriverWait wait) {
        wait.until(messageRegExpPattern.isDetailVisibleCondition());
        assertEquals(messageRegExpPattern.getDetail(),
                StringInputValidationBean.REGEXP_VALIDATION_MSG);
    }

    public void waitForRegExpPatternInputWithoutMessage(WebDriverWait wait) {
        wait.until(messageRegExpPattern.isNotVisibleCondition());
    }

    public void waitForStringSizeInputMessage(WebDriverWait wait) {
        wait.until(messageStringSize.isDetailVisibleCondition());
        assertEquals(messageStringSize.getDetail(),
                StringInputValidationBean.STRING_SIZE_VALIDATION_MSG);
    }

    public void waitForStringSizeInputWithoutMessage(WebDriverWait wait) {
        wait.until(messageStringSize.isNotVisibleCondition());
    }

    /**
     * Waits for client side update of changed inputs by JavaScript
     */
    private void waitForSetting() {
        expectedReturnJS(JS_STATE_VARIABLE, JS_COMPLETED_STATE_STRING);
    }
}
