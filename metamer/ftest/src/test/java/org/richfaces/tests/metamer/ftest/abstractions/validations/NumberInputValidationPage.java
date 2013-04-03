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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.abstractions.NumberInputValidationBean;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class NumberInputValidationPage extends InputValidationPage {

    public static final String MESSAGE_CUSTOM_NAME = "messageCustom";
    public static final String MESSAGE_MAX_NAME = "messageMax";
    public static final String MESSAGE_MIN_NAME = "messageMin";
    public static final String MESSAGE_REQUIRED_NAME = "messageRequired";
    static final String[] ALL_MESSAGE_CASES = { MESSAGE_CUSTOM_NAME,
        MESSAGE_MAX_NAME, MESSAGE_MIN_NAME, MESSAGE_REQUIRED_NAME };
    //
    @FindBy(css = "span[id$=customMsg]")
    private RichFacesMessage messageCustom;
    @FindBy(css = "span[id$=maxMsg] ")
    private RichFacesMessage messageMax;
    @FindBy(css = "span[id$=minMsg]")
    private RichFacesMessage messageMin;
    @FindBy(css = "span[id$=requiredMsg]")
    private RichFacesMessage messageRequired;
    //
    @FindBy(css = "input[id$=customCorrect]")
    private WebElement setCustomCorrectButton;
    @FindBy(css = "input[id$=customWrong]")
    private WebElement setCustomWrongButton;
    @FindBy(css = "input[id$=maxCorrect]")
    private WebElement setMaxCorrectButton;
    @FindBy(css = "input[id$=maxWrong]")
    private WebElement setMaxWrongButton;
    @FindBy(css = "input[id$=minCorrect]")
    private WebElement setMinCorrectButton;
    @FindBy(css = "input[id$=minWrong]")
    private WebElement setMinWrongButton;
    @FindBy(css = "input[id$=requiredCorrect]")
    private WebElement setRequiredCorrectButton;
    @FindBy(css = "input[id$=requiredWrong]")
    private WebElement setRequiredWrongButton;
    //
    @FindBy(css = "span[id$=customOutput]")
    private WebElement customOutput;
    @FindBy(css = "span[id$=maxOutput]")
    private WebElement maxOutput;
    @FindBy(css = "span[id$=minOutput]")
    private WebElement minOutput;
    @FindBy(css = "span[id$=requiredOutput]")
    private WebElement requiredOutput;

    @Override
    protected void initCustomMessages() {
        messageCases.put(MESSAGE_CUSTOM_NAME,
                new ValidationMessageCase(MESSAGE_CUSTOM_NAME, messageCustom,
                setCustomCorrectButton, setCustomWrongButton,
                customOutput, "2", String.valueOf(NumberInputValidationBean.CUSTOM_VALUE_DEFAULT),
                Sets.newHashSet(NumberInputValidationBean.CUSTOM_VALIDATION_MSG)));
        messageCases.put(MESSAGE_MAX_NAME,
                new ValidationMessageCase(MESSAGE_MAX_NAME, messageMax,
                setMaxCorrectButton, setMaxWrongButton,
                maxOutput, "2", String.valueOf(NumberInputValidationBean.MAX_VALUE_DEFAULT),
                Sets.newHashSet(NumberInputValidationBean.MAX_VALIDATION_MSG)));
        messageCases.put(MESSAGE_MIN_NAME,
                new ValidationMessageCase(MESSAGE_MIN_NAME, messageMin,
                setMinCorrectButton, setMinWrongButton,
                minOutput, "2", String.valueOf(NumberInputValidationBean.MIN_VALUE_DEFAULT),
                Sets.newHashSet(NumberInputValidationBean.MIN_VALIDATION_MSG)));
        messageCases.put(MESSAGE_REQUIRED_NAME,
                new ValidationMessageCase(MESSAGE_REQUIRED_NAME, messageRequired,
                setRequiredCorrectButton, setRequiredWrongButton,
                requiredOutput, "2", String.valueOf(NumberInputValidationBean.REQUIRED_VALUE_DEFAULT),
                Sets.newHashSet(NumberInputValidationBean.REQUIRED_VALIDATION_MSG)));
    }
}
