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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.ElementConditionFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.bean.rich.RichInplaceInputBean;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class JSR303Page extends MetamerPage {

    private static final String NOT_EMPTY_VALIDATION_MSG = RichInplaceInputBean.NOT_EMPTY_VALIDATION_MSG;
    private static final String CORRECT_NOT_EMPTY = "xyz";

    private static final String WRONG_REG_EXP = "1a^";
    private static final String CORRECT_REG_EXP = "a2^E";
    private static final String REGEXP_VALIDATION_MSG = RichInplaceInputBean.REGEXP_VALIDATION_MSG;

    private static final String WRONG_STRING_SIZE = "x";
    private static final String CORRECT_STRING_SIZE = "abc3";
    private static final String STRING_SIZE_VALIDATION_MSG = RichInplaceInputBean.STRING_SIZE_VALIDATION_MSG;

    private static final String WRONG_CUSTOM_STRING = "rich faces";
    private static final String CORRECT_CUSTOM_STRING = "RichFaces";
    private static final String CUSTOM_STRING_VALIDATION_MSG = "string is not \"RichFaces\"";

    @FindBy(css="input.rf-au-inp[id$=input1Input]")
    private WebElement notEmptyInput;
    @FindBy(css="input.rf-au-inp[id$=input2Input]")
    private WebElement regExpPatternInput;
    @FindBy(css="input.rf-au-inp[id$=input3Input]")
    private WebElement stringSizeInput;
    @FindBy(css="input.rf-au-inp[id$=input4Input]")
    private WebElement customStringInput;

    @FindBy(css="input[id$=hButton]")
    private WebElement hCommandButton;
    @FindBy(css="input[id$=a4jButton]")
    private WebElement a4jCommandButton;

    @FindBy(css="span[id$=output1]")
    private WebElement output1;
    @FindBy(css="span[id$=output2]")
    private WebElement output2;
    @FindBy(css="span[id$=output3]")
    private WebElement output3;
    @FindBy(css="span[id$=output4]")
    private WebElement output4;

    @FindBy(css="span.rf-msg[id$=inputMsg1] span.rf-msg-det")
    private WebElement input1Message;
    @FindBy(css="span.rf-msg[id$=inputMsg2] span.rf-msg-det")
    private WebElement input2Message;
    @FindBy(css="span.rf-msg[id$=inputMsg3] span.rf-msg-det")
    private WebElement input3Message;
    @FindBy(css="span.rf-msg[id$=inputMsg4] span.rf-msg-det")
    private WebElement input4Message;

    @FindBy(id="locale")
    private WebElement locale;

    public WebElement getA4jCommandButton() {
        return a4jCommandButton;
    }

    public WebElement getHCommandButton() {
        return hCommandButton;
    }

    public boolean isNotEmptyInputMessageVisible() {
        return getNotEmptyInputMessageConditionFactory().isVisible().apply(GrapheneContext.getProxy());
    }

    public boolean isRegExpPatternInputMessageVisible() {
        return getRegExpPatternInputMessageConditionFactory().isVisible().apply(GrapheneContext.getProxy());
    }

    public boolean isStringSizeInputMessageVisible() {
        return getStringSizeInputMessageConditionFactory().isVisible().apply(GrapheneContext.getProxy());
    }

    public boolean isCustomStringInputMessageVisible() {
        return getCustomStringInputMessageConditionFactory().isVisible().apply(GrapheneContext.getProxy());
    }

    public void setNotEmptyInputCorrectly(boolean blur) {
        setValue(notEmptyInput, CORRECT_NOT_EMPTY, blur);
    }

    public void setRegExpPatternInputCorrectly(boolean blur) {
        setValue(regExpPatternInput, CORRECT_REG_EXP, blur);
    }

    public void setStringSizeInputCorrectly(boolean blur) {
        setValue(stringSizeInput, CORRECT_STRING_SIZE, blur);
    }

    public void setCustomStringInputCorrectly(boolean blur) {
        setValue(customStringInput, CORRECT_CUSTOM_STRING, blur);
    }

    public void setNotEmptyInputWrongly(boolean blur) {
        setValue(notEmptyInput, "", blur);
    }

    public void setRegExpPatternInputWrongly(boolean blur) {
        setValue(regExpPatternInput, WRONG_REG_EXP, blur);
    }

    public void setStringSizeInputWrongly(boolean blur) {
        setValue(stringSizeInput, WRONG_STRING_SIZE, blur);
    }

    public void setCustomStringInputWrongly(boolean blur) {
        setValue(customStringInput, WRONG_CUSTOM_STRING, blur);
    }

    public void setAllCorrectly(boolean blur) {
        setNotEmptyInputCorrectly(blur);
        setRegExpPatternInputCorrectly(blur);
        setStringSizeInputCorrectly(blur);
        setCustomStringInputCorrectly(blur);
    }

    public void setAllWrongly(boolean blur) {
        setNotEmptyInputWrongly(blur);
        setRegExpPatternInputWrongly(blur);
        setStringSizeInputWrongly(blur);
        setCustomStringInputWrongly(blur);
    }

    public void waitForNotEmptyInputMessage(WebDriverWait wait) {
        wait.until(getNotEmptyInputMessageConditionFactory().textEquals(NOT_EMPTY_VALIDATION_MSG));
    }

    public void waitForRegExpPatternInputMessage(WebDriverWait wait) {
        wait.until(getRegExpPatternInputMessageConditionFactory().textEquals(REGEXP_VALIDATION_MSG));
    }

    public void waitForStringSizeInputMessage(WebDriverWait wait) {
        wait.until(getStringSizeInputMessageConditionFactory().textEquals(STRING_SIZE_VALIDATION_MSG));
    }

    public void waitForCustomStringInputMessage(WebDriverWait wait) {
        wait.until(getCustomStringInputMessageConditionFactory().textEquals(CUSTOM_STRING_VALIDATION_MSG));
    }

    public void waitForNotEmptyInputWithoutMessage(WebDriverWait wait) {
        wait.until(getNotEmptyInputMessageConditionFactory().not().isPresent());
    }

    public void waitForRegExpPatternInputWithoutMessage(WebDriverWait wait) {
        wait.until(getRegExpPatternInputMessageConditionFactory().not().isPresent());
    }

    public void waitForStringSizeInputWithoutMessage(WebDriverWait wait) {
        wait.until(getStringSizeInputMessageConditionFactory().not().isPresent());
    }

    public void waitForCustomStringInputWithoutMessage(WebDriverWait wait) {
        wait.until(getCustomStringInputMessageConditionFactory().not().isPresent());
    }


    protected void blur() {
        locale.click();
    }

    protected ElementConditionFactory getNotEmptyInputMessageConditionFactory() {
        return Graphene.element(input1Message);
    }

    protected ElementConditionFactory getRegExpPatternInputMessageConditionFactory() {
        return Graphene.element(input2Message);
    }

    protected ElementConditionFactory getStringSizeInputMessageConditionFactory() {
        return Graphene.element(input3Message);
    }

    protected ElementConditionFactory getCustomStringInputMessageConditionFactory() {
        return Graphene.element(input4Message);
    }

    protected void setValue(WebElement input, String value, boolean blur) {
        input.click();
        input.clear();
        input.sendKeys(value);
        if (blur) {
            blur();
        }
    }
}
