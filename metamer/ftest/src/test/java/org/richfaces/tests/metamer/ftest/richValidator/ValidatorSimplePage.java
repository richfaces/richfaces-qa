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
package org.richfaces.tests.metamer.ftest.richValidator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 */
public class ValidatorSimplePage extends MetamerPage {

    @FindBy(css = "input[id$=a4jButton]")
    private WebElement a4jCommandBtn;
    @FindBy(css = "input[id$=hButton]")
    private WebElement hCommandBtn;

    @FindBy(css = "input[id$=assertFalse]")
    private WebElement inputAssertFalse;
    @FindBy(css = "input[id$=assertTrue]")
    private WebElement inputAssertTrue;
    @FindBy(css = "input[id$=custom]")
    private WebElement inputCustom;
    @FindBy(css = "input[id$=decimalMinMax]")
    private WebElement inputDecimalMinMax;
    @FindBy(css = "input[id$=digits]")
    private WebElement inputDigits;
    @FindBy(css = "input[id$=future]")
    private WebElement inputFuture;
    @FindBy(css = "input[id$=max]")
    private WebElement inputMax;
    @FindBy(css = "input[id$=min]")
    private WebElement inputMin;
    @FindBy(css = "input[id$=minMax]")
    private WebElement inputMinMax;
    @FindBy(css = "input[id$=notEmpty]")
    private WebElement inputNotEmpty;
    @FindBy(css = "input[id$=notNull]")
    private WebElement inputNotNull;
    @FindBy(css = "input[id$=past]")
    private WebElement inputPast;
    @FindBy(css = "input[id$=pattern]")
    private WebElement inputPattern;
    @FindBy(css = "input[id$=regexp]")
    private WebElement inputRegexp;
    @FindBy(css = "input[id$=size]")
    private WebElement inputSize;
    @FindBy(css = "input[id$=stringSize]")
    private WebElement inputStringSize;

    @FindBy(css = "span[id$=assertFalseMsg]")
    private RichFacesMessage msgAssertFalse;
    @FindBy(css = "span[id$=assertTrueMsg]")
    private RichFacesMessage msgAssertTrue;
    @FindBy(css = "span[id$=customMsg]")
    private RichFacesMessage msgCustom;
    @FindBy(css = "span[id$=decimalMinMaxMsg]")
    private RichFacesMessage msgDecimalMinMax;
    @FindBy(css = "span[id$=digitsMsg]")
    private RichFacesMessage msgDigits;
    @FindBy(css = "span[id$=futureMsg]")
    private RichFacesMessage msgFuture;
    @FindBy(css = "span[id$=maxMsg]")
    private RichFacesMessage msgMax;
    @FindBy(css = "span[id$=minMsg]")
    private RichFacesMessage msgMin;
    @FindBy(css = "span[id$=minMaxMsg]")
    private RichFacesMessage msgMinMax;
    @FindBy(css = "span[id$=notEmptyMsg]")
    private RichFacesMessage msgNotEmpty;
    @FindBy(css = "span[id$=notNullMsg]")
    private RichFacesMessage msgNotNull;
    @FindBy(css = "span[id$=pastMsg]")
    private RichFacesMessage msgPast;
    @FindBy(css = "span[id$=patternMsg]")
    private RichFacesMessage msgPattern;
    @FindBy(css = "span[id$=regexpMsg]")
    private RichFacesMessage msgRegexp;
    @FindBy(css = "span[id$=sizeMsg]")
    private RichFacesMessage msgSize;
    @FindBy(css = "span[id$=stringSizeMsg]")
    private RichFacesMessage msgStringSize;

    @FindBy(css = "input[id$=setCorrectValuesButton]")
    private WebElement setCorrectBtn;
    @FindBy(css = "input[id$=setWrongValuesButton]")
    private WebElement setWrongBtn;

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
     * @return the hCommandBtn
     */
    public WebElement gethCommandBtn() {
        return hCommandBtn;
    }
}
