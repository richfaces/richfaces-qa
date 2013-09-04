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

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 */
public class ValidatorSimplePage extends MetamerPage {

    @FindByJQuery("input[id$=future]")
    public WebElement inputFuture;

    @FindByJQuery("input[id$=past]")
    public WebElement inputPast;

    @FindByJQuery("input[id$=assertTrue]")
    public WebElement inputAssertTrue;

    @FindByJQuery("input[id$=assertFalse]")
    public WebElement inputAssertFalse;

    @FindByJQuery("input[id$=decimalMinMax]")
    public WebElement inputDecimalMinMax;

    @FindByJQuery("input[id$=digits]")
    public WebElement inputDigits;

    @FindByJQuery("input[id$=max]")
    public WebElement inputMax;

    @FindByJQuery("input[id$=min]")
    public WebElement inputMin;

    @FindByJQuery("input[id$=minMax]")
    public WebElement inputMinMax;

    @FindByJQuery("input[id$=notEmpty]")
    public WebElement inputNotEmpty;

    @FindByJQuery("input[id$=notNull]")
    public WebElement inputNotNull;

    @FindByJQuery("input[id$=pattern]")
    public WebElement inputPattern;

    @FindByJQuery("input[id$=custom]")
    public WebElement inputCustom;

    @FindByJQuery("input[id$=regexp]")
    public WebElement inputRegexp;

    @FindByJQuery("input[id$=stringSize]")
    public WebElement inputStringSize;

    @FindByJQuery("input[id$=size]")
    public WebElement inputSize;

    @FindByJQuery("input[id$=setWrongValuesButton]")
    public WebElement setWrongBtn;
    @FindByJQuery("input[id$=setCorrectValuesButton]")
    public WebElement setCorrectBtn;
    @FindByJQuery("input[id$=hButton]")
    public WebElement hCommandBtn;
    @FindByJQuery("input[id$=a4jButton]")
    public WebElement a4jCommandBtn;

    @FindByJQuery("input[type=submit]")
    public WebElement toggleButton;

    @FindByJQuery("input[type=text]")
    public WebElement simpleInput;

    @FindBy(className = "rf-msg-det")
    public WebElement simpleErrorMessage;

    public WebElement getSelectionItemByLabel(String label) {
        return driver.findElement(By.cssSelector(format("table[id$=size] tr > td > input[value='{0}']", label)));
    }

    @FindByJQuery("span[id$=futureMsg] span.rf-msg-det")
    public WebElement msgFuture;

    @FindByJQuery("span[id$=pastMsg] span.rf-msg-det")
    public WebElement msgPast;

    @FindByJQuery("span[id$=assertTrueMsg] span.rf-msg-det")
    public WebElement msgAssertTrue;

    @FindByJQuery("span[id$=assertFalseMsg] span.rf-msg-det")
    public WebElement msgAssertFalse;

    @FindByJQuery("span[id$=decimalMinMaxMsg] span.rf-msg-det")
    public WebElement msgDecimalMinMax;

    @FindByJQuery("span[id$=digitsMsg] span.rf-msg-det")
    public WebElement msgDigits;

    @FindByJQuery("span[id$=maxMsg] span.rf-msg-det")
    public WebElement msgMax;

    @FindByJQuery("span[id$=minMsg] span.rf-msg-det")
    public WebElement msgMin;

    @FindByJQuery("span[id$=minMaxMsg] span.rf-msg-det")
    public WebElement msgMinMax;

    @FindByJQuery("span[id$=notEmptyMsg] span.rf-msg-det")
    public WebElement msgNotEmpty;

    @FindByJQuery("span[id$=notNullMsg] span.rf-msg-det")
    public WebElement msgNotNull;

    @FindByJQuery("span[id$=patternMsg] span.rf-msg-det")
    public WebElement msgPattern;

    @FindByJQuery("span[id$=customMsg] span.rf-msg-det")
    public WebElement msgCustom;

    @FindByJQuery("span[id$=regexpMsg] span.rf-msg-det")
    public WebElement msgRegexp;

    @FindByJQuery("span[id$=stringSizeMsg] span.rf-msg-det")
    public WebElement msgStringSize;

    @FindByJQuery("span[id$=sizeMsg] span.rf-msg-det")
    public WebElement msgSize;

}
