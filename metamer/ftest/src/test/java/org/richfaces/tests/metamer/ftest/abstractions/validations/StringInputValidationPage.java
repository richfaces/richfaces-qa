/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.abstractions.validations;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.bean.abstractions.StringInputValidationBean;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class StringInputValidationPage extends InputValidationPage {

    public static final String MESSAGE_CUSTOM_NAME = "messageCustom";
    public static final String MESSAGE_STRING_NAME = "messageStringSize";
    public static final String MESSAGE_REG_EXP_PATTERN_NAME = "messageRegExpPattern";
    public static final String MESSAGE_NOT_EMPTY_NAME = "messageNotEmpty";
    public static final String MESSAGE_REQUIRED_NAME = "messageRequired";
    static final String[] ALL_MESSAGE_CASES = { MESSAGE_CUSTOM_NAME,
        MESSAGE_STRING_NAME, MESSAGE_REG_EXP_PATTERN_NAME, MESSAGE_NOT_EMPTY_NAME, MESSAGE_REQUIRED_NAME };
    //
    @FindBy(css = "span[id$=customMsg]")
    private RichFacesMessage messageCustomString;
    @FindBy(css = "span[id$=notEmptyMsg]")
    private RichFacesMessage messageNotEmpty;
    @FindBy(css = "span[id$=patternMsg] ")
    private RichFacesMessage messageRegExpPattern;
    @FindBy(css = "span[id$=sizeMsg]")
    private RichFacesMessage messageStringSize;
    @FindBy(css = "span[id$=requiredMsg]")
    private RichFacesMessage messageRequired;
    //
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
    @FindBy(css = "input[id$=requiredCorrect]")
    private WebElement setRequiredCorrectButton;
    @FindBy(css = "input[id$=requiredWrong]")
    private WebElement setRequiredWrongButton;
    //
    @FindBy(css = "span[id$=notEmptyOutput]")
    private WebElement notEmptyOutput;
    @FindBy(css = "span[id$=patternOutput]")
    private WebElement patternOutput;
    @FindBy(css = "span[id$=sizeOutput]")
    private WebElement sizeOutput;
    @FindBy(css = "span[id$=customOutput]")
    private WebElement customOutput;
    @FindBy(css = "span[id$=requiredOutput]")
    private WebElement requiredOutput;

    @Override
    public void initCustomMessages() {
        messageCases.put(MESSAGE_CUSTOM_NAME,
            new ValidationMessageCase(MESSAGE_CUSTOM_NAME, messageCustomString,
            setCustomCorrectButton, setCustomWrongButton,
            customOutput, "richfaces", StringInputValidationBean.CUSTOM_VALUE_DEFAULT,
            Sets.newHashSet(StringInputValidationBean.CUSTOM_VALIDATION_MSG)));
        messageCases.put(MESSAGE_STRING_NAME,
            new ValidationMessageCase(MESSAGE_STRING_NAME, messageStringSize,
            setSizeCorrectButton, setSizeWrongButton,
            sizeOutput, "123", StringInputValidationBean.SIZE_VALUE_DEFAULT,
            Sets.newHashSet(StringInputValidationBean.STRING_SIZE_VALIDATION_MSG)));
        messageCases.put(MESSAGE_REG_EXP_PATTERN_NAME,
            new ValidationMessageCase(MESSAGE_REG_EXP_PATTERN_NAME, messageRegExpPattern,
            setPatternCorrectButton, setPatternWrongButton,
            patternOutput, "abcd", StringInputValidationBean.PATTERN_VALUE_DEFAULT,
            Sets.newHashSet(StringInputValidationBean.REGEXP_VALIDATION_MSG)));
        messageCases.put(MESSAGE_NOT_EMPTY_NAME,
            new ValidationMessageCase(MESSAGE_NOT_EMPTY_NAME, messageNotEmpty,
            setNotEmptyCorrectButton, setNotEmptyWrongButton,
            notEmptyOutput, "not empty", StringInputValidationBean.NOTEMPTY_VALUE_DEFAULT,
            Sets.newHashSet(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG,
            StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG2,
            StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG3)));
        messageCases.put(MESSAGE_REQUIRED_NAME,
            new ValidationMessageCase(MESSAGE_REQUIRED_NAME, messageRequired,
            setRequiredCorrectButton, setRequiredWrongButton,
            requiredOutput, "required 2", StringInputValidationBean.REQUIRED_VALUE_DEFAULT,
            Sets.newHashSet(StringInputValidationBean.REQUIRED_VALIDATION_MSG)));
    }
}
