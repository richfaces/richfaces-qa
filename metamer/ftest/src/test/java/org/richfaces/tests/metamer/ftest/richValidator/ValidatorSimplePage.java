/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

    @FindByJQuery("span[id$=assertFalseMsg] span.rf-msg-det")
    private WebElement msgAssertFalse;
    @FindByJQuery("span[id$=assertTrueMsg] span.rf-msg-det")
    private WebElement msgAssertTrue;
    @FindByJQuery("span[id$=customMsg] span.rf-msg-det")
    private WebElement msgCustom;
    @FindByJQuery("span[id$=decimalMinMaxMsg] span.rf-msg-det")
    private WebElement msgDecimalMinMax;
    @FindByJQuery("span[id$=digitsMsg] span.rf-msg-det")
    private WebElement msgDigits;
    @FindByJQuery(value = "span[id$=futureMsg] span.rf-msg-det")
    private WebElement msgFuture;
    @FindByJQuery("span[id$=maxMsg] span.rf-msg-det")
    private WebElement msgMax;
    @FindByJQuery("span[id$=minMsg] span.rf-msg-det")
    private WebElement msgMin;
    @FindByJQuery("span[id$=minMaxMsg] span.rf-msg-det")
    private WebElement msgMinMax;
    @FindByJQuery("span[id$=notEmptyMsg] span.rf-msg-det")
    private WebElement msgNotEmpty;
    @FindByJQuery("span[id$=notNullMsg] span.rf-msg-det")
    private WebElement msgNotNull;
    @FindByJQuery(value = "span[id$=pastMsg] span.rf-msg-det")
    private WebElement msgPast;
    @FindByJQuery("span[id$=patternMsg] span.rf-msg-det")
    private WebElement msgPattern;
    @FindByJQuery("span[id$=regexpMsg] span.rf-msg-det")
    private WebElement msgRegexp;
    @FindByJQuery(value = "span[id$=sizeMsg] span.rf-msg-det")
    private WebElement msgSize;
    @FindByJQuery("span[id$=stringSizeMsg] span.rf-msg-det")
    private WebElement msgStringSize;

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
    public WebElement getMsgAssertFalse() {
        return msgAssertFalse;
    }

    /**
     * @return the msgAssertTrue
     */
    public WebElement getMsgAssertTrue() {
        return msgAssertTrue;
    }

    /**
     * @return the msgCustom
     */
    public WebElement getMsgCustom() {
        return msgCustom;
    }

    /**
     * @return the msgDecimalMinMax
     */
    public WebElement getMsgDecimalMinMax() {
        return msgDecimalMinMax;
    }

    /**
     * @return the msgDigits
     */
    public WebElement getMsgDigits() {
        return msgDigits;
    }

    /**
     * @return the msgFuture
     */
    public WebElement getMsgFuture() {
        return msgFuture;
    }

    /**
     * @return the msgMax
     */
    public WebElement getMsgMax() {
        return msgMax;
    }

    /**
     * @return the msgMin
     */
    public WebElement getMsgMin() {
        return msgMin;
    }

    /**
     * @return the msgMinMax
     */
    public WebElement getMsgMinMax() {
        return msgMinMax;
    }

    /**
     * @return the msgNotEmpty
     */
    public WebElement getMsgNotEmpty() {
        return msgNotEmpty;
    }

    /**
     * @return the msgNotNull
     */
    public WebElement getMsgNotNull() {
        return msgNotNull;
    }

    /**
     * @return the msgPast
     */
    public WebElement getMsgPast() {
        return msgPast;
    }

    /**
     * @return the msgPattern
     */
    public WebElement getMsgPattern() {
        return msgPattern;
    }

    /**
     * @return the msgRegexp
     */
    public WebElement getMsgRegexp() {
        return msgRegexp;
    }

    /**
     * @return the msgSize
     */
    public WebElement getMsgSize() {
        return msgSize;
    }

    /**
     * @return the msgStringSize
     */
    public WebElement getMsgStringSize() {
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
