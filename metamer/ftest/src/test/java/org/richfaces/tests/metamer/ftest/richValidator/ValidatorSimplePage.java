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

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 */
public class ValidatorSimplePage extends MetamerPage {

    @FindBy(jquery = "input[id$=future]")
    public WebElement inputFuture;

    @FindBy(jquery = "input[id$=past]")
    public WebElement inputPast;

    @FindBy(jquery = "input[id$=assertTrue]")
    public WebElement inputAssertTrue;

    @FindBy(jquery = "input[id$=assertFalse]")
    public WebElement inputAssertFalse;

    @FindBy(jquery = "input[id$=decimalMinMax]")
    public WebElement inputDecimalMinMax;

    @FindBy(jquery = "input[id$=digits]")
    public WebElement inputDigits;

    @FindBy(jquery = "input[id$=max]")
    public WebElement inputMax;

    @FindBy(jquery = "input[id$=min]")
    public WebElement inputMin;

    @FindBy(jquery = "input[id$=minMax]")
    public WebElement inputMinMax;

    @FindBy(jquery = "input[id$=notEmpty]")
    public WebElement inputNotEmpty;

    @FindBy(jquery = "input[id$=notNull]")
    public WebElement inputNotNull;

    @FindBy(jquery = "input[id$=pattern]")
    public WebElement inputPattern;

    @FindBy(jquery = "input[id$=custom]")
    public WebElement inputCustom;

    @FindBy(jquery = "input[id$=regexp]")
    public WebElement inputRegexp;

    @FindBy(jquery = "input[id$=stringSize]")
    public WebElement inputStringSize;

    @FindBy(jquery = "input[id$=size]")
    public WebElement inputSize;

    @FindBy(jquery = "input[id$=setWrongValuesButton]")
    public WebElement setWrongBtn;
    @FindBy(jquery = "input[id$=setCorrectValuesButton]")
    public WebElement setCorrectBtn;
    @FindBy(jquery = "input[id$=hButton]")
    public WebElement hCommandBtn;
    @FindBy(jquery = "input[id$=a4jButton]")
    public WebElement a4jCommandBtn;

    @FindBy(jquery = "input[type=submit]")
    public WebElement toggleButton;

    @FindBy(jquery = "input[type=text]")
    public WebElement simpleInput;

    @FindBy(className = "rf-msg-det")
    public WebElement simpleErrorMessage;

    public WebElement getSelectionItemByLabel(String label) {
        return driver.findElement(By.cssSelector(format("table[id$=size] tr > td > input[value='{0}']", label)));
    }

    @FindBy(jquery = "span[id$=futureMsg] span.rf-msg-det")
    public WebElement msgFuture;

    @FindBy(jquery = "span[id$=pastMsg] span.rf-msg-det")
    public WebElement msgPast;

    @FindBy(jquery = "span[id$=assertTrueMsg] span.rf-msg-det")
    public WebElement msgAssertTrue;

    @FindBy(jquery = "span[id$=assertFalseMsg] span.rf-msg-det")
    public WebElement msgAssertFalse;

    @FindBy(jquery = "span[id$=decimalMinMaxMsg] span.rf-msg-det")
    public WebElement msgDecimalMinMax;

    @FindBy(jquery = "span[id$=digitsMsg] span.rf-msg-det")
    public WebElement msgDigits;

    @FindBy(jquery = "span[id$=maxMsg] span.rf-msg-det")
    public WebElement msgMax;

    @FindBy(jquery = "span[id$=minMsg] span.rf-msg-det")
    public WebElement msgMin;

    @FindBy(jquery = "span[id$=minMaxMsg] span.rf-msg-det")
    public WebElement msgMinMax;

    @FindBy(jquery = "span[id$=notEmptyMsg] span.rf-msg-det")
    public WebElement msgNotEmpty;

    @FindBy(jquery = "span[id$=notNullMsg] span.rf-msg-det")
    public WebElement msgNotNull;

    @FindBy(jquery = "span[id$=patternMsg] span.rf-msg-det")
    public WebElement msgPattern;

    @FindBy(jquery = "span[id$=customMsg] span.rf-msg-det")
    public WebElement msgCustom;

    @FindBy(jquery = "span[id$=regexpMsg] span.rf-msg-det")
    public WebElement msgRegexp;

    @FindBy(jquery = "span[id$=stringSizeMsg] span.rf-msg-det")
    public WebElement msgStringSize;

    @FindBy(jquery = "span[id$=sizeMsg] span.rf-msg-det")
    public WebElement msgSize;

}
