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
package org.richfaces.tests.metamer.ftest.richValidator;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 */
public class ValidatorSimplePage extends MetamerPage {

    @FindByJQuery("input[id$=a4jButton]")
    private WebElement a4jCommandBtn;
    @FindByJQuery("input[id$=hButton]")
    private WebElement hCommandBtn;

    @FindByJQuery(value = "input[id$=assertFalse]")
    private WebElement inputAssertFalse;
    @FindByJQuery("input[id$=assertTrue]")
    private WebElement inputAssertTrue;
    @FindByJQuery("input[id$=custom]")
    private WebElement inputCustom;
    @FindByJQuery("input[id$=decimalMinMax]")
    private WebElement inputDecimalMinMax;
    @FindByJQuery("input[id$=digits]")
    private WebElement inputDigits;
    @FindByJQuery(value = "input[id$=future]")
    private WebElement inputFuture;
    @FindByJQuery("input[id$=max]")
    private WebElement inputMax;
    @FindByJQuery("input[id$=min]")
    private WebElement inputMin;
    @FindByJQuery("input[id$=minMax]")
    private WebElement inputMinMax;
    @FindByJQuery("input[id$=notEmpty]")
    private WebElement inputNotEmpty;
    @FindByJQuery("input[id$=notNull]")
    private WebElement inputNotNull;
    @FindByJQuery(value = "input[id$=past]")
    private WebElement inputPast;
    @FindByJQuery("input[id$=pattern]")
    private WebElement inputPattern;
    @FindByJQuery("input[id$=regexp]")
    private WebElement inputRegexp;
    @FindByJQuery("input[id$=size]")
    private WebElement inputSize;
    @FindByJQuery("input[id$=stringSize]")
    private WebElement inputStringSize;

    @FindByJQuery("span[id$=assertFalseMsg]")
    private RichFacesMessage msgAssertFalse;
    @FindByJQuery("span[id$=assertTrueMsg]")
    private RichFacesMessage msgAssertTrue;
    @FindByJQuery("span[id$=customMsg]")
    private RichFacesMessage msgCustom;
    @FindByJQuery("span[id$=decimalMinMaxMsg]")
    private RichFacesMessage msgDecimalMinMax;
    @FindByJQuery("span[id$=digitsMsg]")
    private RichFacesMessage msgDigits;
    @FindByJQuery(value = "span[id$=futureMsg]")
    private RichFacesMessage msgFuture;
    @FindByJQuery("span[id$=maxMsg]")
    private RichFacesMessage msgMax;
    @FindByJQuery("span[id$=minMsg]")
    private RichFacesMessage msgMin;
    @FindByJQuery("span[id$=minMaxMsg]")
    private RichFacesMessage msgMinMax;
    @FindByJQuery("span[id$=notEmptyMsg]")
    private RichFacesMessage msgNotEmpty;
    @FindByJQuery("span[id$=notNullMsg]")
    private RichFacesMessage msgNotNull;
    @FindByJQuery(value = "span[id$=pastMsg]")
    private RichFacesMessage msgPast;
    @FindByJQuery("span[id$=patternMsg]")
    private RichFacesMessage msgPattern;
    @FindByJQuery("span[id$=regexpMsg]")
    private RichFacesMessage msgRegexp;
    @FindByJQuery(value = "span[id$=sizeMsg]")
    private RichFacesMessage msgSize;
    @FindByJQuery("span[id$=stringSizeMsg]")
    private RichFacesMessage msgStringSize;

    @FindByJQuery("input[id$=setCorrectValuesButton]")
    private WebElement setCorrectBtn;
    @FindByJQuery(value = "input[id$=setWrongValuesButton]")
    private WebElement setWrongBtn;
    @FindBy(className = "rf-msg-det")
    private WebElement simpleErrorMessage;
    @FindByJQuery(value = "input[type=text]")
    private WebElement simpleInput;
    @FindByJQuery(value = "input[type=submit]")
    private WebElement toggleButton;

    /**
     * @return the a4jCommandBtn
     */
    public WebElement getA4jCommandBtn() {
        return a4jCommandBtn;
    }

    /**
     * @return the inputAssertFalse
     */
    public WebElement getInputAssertFalse() {
        return inputAssertFalse;
    }

    /**
     * @return the inputAssertTrue
     */
    public WebElement getInputAssertTrue() {
        return inputAssertTrue;
    }

    /**
     * @return the inputCustom
     */
    public WebElement getInputCustom() {
        return inputCustom;
    }

    /**
     * @return the inputDecimalMinMax
     */
    public WebElement getInputDecimalMinMax() {
        return inputDecimalMinMax;
    }

    /**
     * @return the inputDigits
     */
    public WebElement getInputDigits() {
        return inputDigits;
    }

    /**
     * @return the inputFuture
     */
    public WebElement getInputFuture() {
        return inputFuture;
    }

    /**
     * @return the inputMax
     */
    public WebElement getInputMax() {
        return inputMax;
    }

    /**
     * @return the inputMin
     */
    public WebElement getInputMin() {
        return inputMin;
    }

    /**
     * @return the inputMinMax
     */
    public WebElement getInputMinMax() {
        return inputMinMax;
    }

    /**
     * @return the inputNotEmpty
     */
    public WebElement getInputNotEmpty() {
        return inputNotEmpty;
    }

    /**
     * @return the inputNotNull
     */
    public WebElement getInputNotNull() {
        return inputNotNull;
    }

    /**
     * @return the inputPast
     */
    public WebElement getInputPast() {
        return inputPast;
    }

    /**
     * @return the inputPattern
     */
    public WebElement getInputPattern() {
        return inputPattern;
    }

    /**
     * @return the inputRegexp
     */
    public WebElement getInputRegexp() {
        return inputRegexp;
    }

    /**
     * @return the inputSize
     */
    public WebElement getInputSize() {
        return inputSize;
    }

    /**
     * @return the inputStringSize
     */
    public WebElement getInputStringSize() {
        return inputStringSize;
    }

    /**
     * @return the msgAssertFalse
     */
    public RichFacesMessage getMsgAssertFalse() {
        return msgAssertFalse;
    }

    /**
     * @return the msgAssertTrue
     */
    public RichFacesMessage getMsgAssertTrue() {
        return msgAssertTrue;
    }

    /**
     * @return the msgCustom
     */
    public RichFacesMessage getMsgCustom() {
        return msgCustom;
    }

    /**
     * @return the msgDecimalMinMax
     */
    public RichFacesMessage getMsgDecimalMinMax() {
        return msgDecimalMinMax;
    }

    /**
     * @return the msgDigits
     */
    public RichFacesMessage getMsgDigits() {
        return msgDigits;
    }

    /**
     * @return the msgFuture
     */
    public RichFacesMessage getMsgFuture() {
        return msgFuture;
    }

    /**
     * @return the msgMax
     */
    public RichFacesMessage getMsgMax() {
        return msgMax;
    }

    /**
     * @return the msgMin
     */
    public RichFacesMessage getMsgMin() {
        return msgMin;
    }

    /**
     * @return the msgMinMax
     */
    public RichFacesMessage getMsgMinMax() {
        return msgMinMax;
    }

    /**
     * @return the msgNotEmpty
     */
    public RichFacesMessage getMsgNotEmpty() {
        return msgNotEmpty;
    }

    /**
     * @return the msgNotNull
     */
    public RichFacesMessage getMsgNotNull() {
        return msgNotNull;
    }

    /**
     * @return the msgPast
     */
    public RichFacesMessage getMsgPast() {
        return msgPast;
    }

    /**
     * @return the msgPattern
     */
    public RichFacesMessage getMsgPattern() {
        return msgPattern;
    }

    /**
     * @return the msgRegexp
     */
    public RichFacesMessage getMsgRegexp() {
        return msgRegexp;
    }

    /**
     * @return the msgSize
     */
    public RichFacesMessage getMsgSize() {
        return msgSize;
    }

    /**
     * @return the msgStringSize
     */
    public RichFacesMessage getMsgStringSize() {
        return msgStringSize;
    }

    public WebElement getSelectionItemByLabel(String label) {
        return driver.findElement(By.cssSelector(String.format("table[id$=size] tr > td > input[value='%s']", label)));
    }

    /**
     * @return the setCorrectBtn
     */
    public WebElement getSetCorrectBtn() {
        return setCorrectBtn;
    }

    /**
     * @return the setWrongBtn
     */
    public WebElement getSetWrongBtn() {
        return setWrongBtn;
    }

    /**
     * @return the simpleErrorMessage
     */
    public WebElement getSimpleErrorMessage() {
        return simpleErrorMessage;
    }

    /**
     * @return the simpleInput
     */
    public WebElement getSimpleInput() {
        return simpleInput;
    }

    /**
     * @return the toggleButton
     */
    public WebElement getToggleButton() {
        return toggleButton;
    }

    /**
     * @return the hCommandBtn
     */
    public WebElement gethCommandBtn() {
        return hCommandBtn;
    }
}
