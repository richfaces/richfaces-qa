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
package org.richfaces.tests.metamer.ftest.richValidator;

import java.util.EnumMap;
import java.util.Map;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.validation.AssertFalseBean;
import org.richfaces.tests.metamer.validation.AssertTrueBean;
import org.richfaces.tests.metamer.validation.DecimalMinMaxBean;
import org.richfaces.tests.metamer.validation.DigitsBean;
import org.richfaces.tests.metamer.validation.FutureBean;
import org.richfaces.tests.metamer.validation.MaxBean;
import org.richfaces.tests.metamer.validation.MinBean;
import org.richfaces.tests.metamer.validation.MinMaxBean;
import org.richfaces.tests.metamer.validation.NotEmptyBean;
import org.richfaces.tests.metamer.validation.NotNullBean;
import org.richfaces.tests.metamer.validation.PastBean;
import org.richfaces.tests.metamer.validation.PatternBean;
import org.richfaces.tests.metamer.validation.SizeBean;
import org.richfaces.tests.metamer.validation.StringSizeBean;
import org.richfaces.tests.metamer.validator.StringRichFacesValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * Abstract class with selenium test for validators
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22997 $
 */
public abstract class AbstractValidatorsTest extends AbstractWebDriverTest {

    protected enum ID {

        /**
         * Boolean, true
         */
        assertTrue,
        /**
         * Boolean false
         */
        assertFalse,
        /**
         * Decimal from 2.5 to 9.688
         */
        decimalMinMax,
        /**
         * Decimal 3 digits and 4 fract
         */
        digits,
        /**
         * Integer max 10
         */
        max,
        /**
         * Integer min 2
         */
        min,
        /**
         * Integer from 2 to 10
         */
        minMax,
        /**
         * Text, not empty
         */
        notEmpty,
        /**
         * Text, not null
         */
        notNull,
        /**
         * Text, pattern '[a-z].*'
         */
        pattern,
        /**
         * custom validator
         */
        custom,
        /**
         * custom regExp validator
         */
        regexp,
        /**
         * date past
         */
        past,
        /**
         * date future
         */
        future,
        /**
         * String size from 2 to 4
         */
        stringSize,
        /**
         * Selection size
         */
        size
    }
    protected Map<ID, String> messages = new EnumMap<AbstractValidatorsTest.ID, String>(AbstractValidatorsTest.ID.class);
    private Map<ID, Object> wrongValue = new EnumMap<AbstractValidatorsTest.ID, Object>(AbstractValidatorsTest.ID.class);

    @Page
    protected ValidatorSimplePage page;

    private String future;
    private String past;
    private boolean firstRun = true;

    @BeforeClass(groups = "smoke")
    public void init() {
        messages.put(ID.assertTrue, AssertTrueBean.VALIDATION_MSG);
        messages.put(ID.assertFalse, AssertFalseBean.VALIDATION_MSG);
        messages.put(ID.decimalMinMax, DecimalMinMaxBean.VALIDATION_MSG);
        messages.put(ID.digits, DigitsBean.VALIDATION_MSG);
        messages.put(ID.max, MaxBean.VALIDATION_MSG);
        messages.put(ID.min, MinBean.VALIDATION_MSG);
        messages.put(ID.minMax, MinMaxBean.VALIDATION_MSG);
        messages.put(ID.notEmpty, NotEmptyBean.VALIDATION_MSG);
        messages.put(ID.notNull,NotNullBean.VALIDATION_MSG);
        messages.put(ID.pattern, PatternBean.VALIDATION_MSG);
        messages.put(ID.custom, StringRichFacesValidator.VALIDATION_ERROR_MSG);
        messages.put(ID.regexp, "Regex pattern of '\\d{3}' not matched");
        messages.put(ID.past, PastBean.VALIDATION_MSG);
        messages.put(ID.future, FutureBean.VALIDATION_MSG);
        messages.put(ID.stringSize, StringSizeBean.VALIDATION_MSG);
        messages.put(ID.size, SizeBean.VALIDATION_MSG); // RF-11035

        wrongValue.put(ID.assertTrue, Boolean.FALSE);
        wrongValue.put(ID.assertFalse, Boolean.TRUE);
        wrongValue.put(ID.decimalMinMax, "10.688");
        wrongValue.put(ID.digits, "15.627123");
        wrongValue.put(ID.max, "122");
        wrongValue.put(ID.min, "-544");
        wrongValue.put(ID.minMax, "-5");
        wrongValue.put(ID.notEmpty, "");
        wrongValue.put(ID.notNull, null);
        wrongValue.put(ID.pattern, "@@@");
        wrongValue.put(ID.custom, "@@@");
        wrongValue.put(ID.regexp, "@@@");
        wrongValue.put(ID.stringSize, "JSF 2");
        wrongValue.put(ID.size, "F"); // RF-11035
    }

    private void clickCorrectButton() {
        page.setCorrectBtn.click();
        if (new WebElementConditionFactory(page.inputFuture).isPresent().apply(driver)
            && new WebElementConditionFactory(page.inputPast).isPresent().apply(driver)) {
            page.inputPast.clear();
            page.inputPast.sendKeys(past);
            page.inputFuture.clear();
            page.inputFuture.sendKeys(future);
        }
    }

    /**
     * Must set the dates this way beacause of problems with other locale than eng.
     */
    @BeforeMethod(alwaysRun = true)
    public void setDates() {
        if (firstRun) {
            if (new WebElementConditionFactory(page.inputFuture).isPresent().apply(driver)
                && new WebElementConditionFactory(page.inputPast).isPresent().apply(driver)) {
                past = page.inputPast.getAttribute("value"); // sendKeys(past);
                future = page.inputFuture.getAttribute("value"); // sendKeys(future);

                wrongValue.put(AbstractValidatorsTest.ID.past, future);
                wrongValue.put(AbstractValidatorsTest.ID.future, past);
                firstRun = false;
            }
        }
    }

    private void clickWrongButton() {
        page.setWrongBtn.click();
        if (new WebElementConditionFactory(page.inputFuture).isPresent().apply(driver)
            && new WebElementConditionFactory(page.inputPast).isPresent().apply(driver)) {
            page.inputPast.clear();
            page.inputPast.sendKeys(future);
            page.inputFuture.clear();
            page.inputFuture.sendKeys(past);
        }
    }

    public void verifyAllWrongWithAjaxSubmit() {
        clickWrongButton();

        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgAssertTrue).text().equalTo(messages.get(ID.assertTrue));
        Graphene.waitGui().until().element(page.msgAssertFalse).text().equalTo(messages.get(ID.assertFalse));
        Graphene.waitGui().until().element(page.msgDecimalMinMax).text().equalTo(messages.get(ID.decimalMinMax));
        Graphene.waitGui().until().element(page.msgDigits).text().equalTo(messages.get(ID.digits));
        Graphene.waitGui().until().element(page.msgMax).text().equalTo(messages.get(ID.max));
        Graphene.waitGui().until().element(page.msgMin).text().equalTo(messages.get(ID.min));
        Graphene.waitGui().until().element(page.msgMinMax).text().equalTo(messages.get(ID.minMax));
        Graphene.waitGui().until().element(page.msgNotEmpty).text().equalTo(messages.get(ID.notEmpty));
        Graphene.waitGui().until().element(page.msgNotNull).text().equalTo(messages.get(ID.notNull));
        Graphene.waitGui().until().element(page.msgPattern).text().equalTo(messages.get(ID.pattern));
        Graphene.waitGui().until().element(page.msgCustom).text().equalTo(messages.get(ID.custom));

        if (new WebElementConditionFactory(page.inputRegexp).isPresent().apply(driver)) {
            // regExp validator isn't present in JSR303 validation
            Graphene.waitGui().until().element(page.msgRegexp).text().equalTo(messages.get(ID.regexp));
        }
        Graphene.waitGui().until().element(page.msgPast).text().equalTo(messages.get(ID.past));
        Graphene.waitGui().until().element(page.msgFuture).text().equalTo(messages.get(ID.future));
        Graphene.waitGui().until().element(page.msgStringSize).text().equalTo(messages.get(ID.stringSize));
        Graphene.waitGui().until().element(page.msgSize).text().equalTo(messages.get(ID.size));
    }

    public void verifyAllWrongWithJSFSubmit() {
        clickWrongButton();

        page.hCommandBtn.click();

        Graphene.waitGui().until().element(page.msgAssertTrue).text().equalTo(messages.get(ID.assertTrue));
        Graphene.waitGui().until().element(page.msgAssertFalse).text().equalTo(messages.get(ID.assertFalse));
        Graphene.waitGui().until().element(page.msgDecimalMinMax).text().equalTo(messages.get(ID.decimalMinMax));
        Graphene.waitGui().until().element(page.msgDigits).text().equalTo(messages.get(ID.digits));
        Graphene.waitGui().until().element(page.msgMax).text().equalTo(messages.get(ID.max));
        Graphene.waitGui().until().element(page.msgMin).text().equalTo(messages.get(ID.min));
        Graphene.waitGui().until().element(page.msgMinMax).text().equalTo(messages.get(ID.minMax));
        Graphene.waitGui().until().element(page.msgNotEmpty).text().equalTo(messages.get(ID.notEmpty));
        Graphene.waitGui().until().element(page.msgNotNull).text().equalTo(messages.get(ID.notNull));
        Graphene.waitGui().until().element(page.msgPattern).text().equalTo(messages.get(ID.pattern));
        Graphene.waitGui().until().element(page.msgCustom).text().equalTo(messages.get(ID.custom));

        if (new WebElementConditionFactory(page.inputRegexp).isPresent().apply(driver)) {
            // regExp validator isn't present in JSR303 validation
            Graphene.waitGui().until().element(page.msgRegexp).text().equalTo(messages.get(ID.regexp));
        }
        Graphene.waitGui().until().element(page.msgPast).text().equalTo(messages.get(ID.past));
        Graphene.waitGui().until().element(page.msgFuture).text().equalTo(messages.get(ID.future));
        Graphene.waitGui().until().element(page.msgStringSize).text().equalTo(messages.get(ID.stringSize));
        Graphene.waitGui().until().element(page.msgSize).text().equalTo(messages.get(ID.size));
    }

    /**
     * Boolean input, verify true
     */
    public void verifyBooleanTrue() {

        clickCorrectButton();

        // checkBoolean to true
        page.inputAssertTrue.click();
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgAssertTrue).text().equalTo(messages.get(ID.assertTrue));
    }

    /**
     * Boolean input, verify false
     */
    public void verifyBooleanFalse() {

        clickCorrectButton();

        // checkBoolean to false
        page.inputAssertFalse.click();
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgAssertFalse).text().equalTo(messages.get(ID.assertFalse));
    }

    /**
     * Decimal input, verify from 2.5 to 9.688
     */
    protected void verifyDecimalMinMax() {

        clickCorrectButton();

        // Decimal input
        page.inputDecimalMinMax.clear();
        page.inputDecimalMinMax.sendKeys(wrongValue.get(ID.decimalMinMax).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgDecimalMinMax).text().equalTo(messages.get(ID.decimalMinMax));
    }

    /**
     * Decimal input, verify digits
     */
    protected void verifyDecimalDigits() {

        clickCorrectButton();

        // decimal input digits
        page.inputDigits.clear();
        page.inputDigits.sendKeys(wrongValue.get(ID.digits).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgDigits).text().equalTo(messages.get(ID.digits));
    }

    /**
     * Integer input, verify max
     */
    protected void verifyMax() {

        clickCorrectButton();

        // integer input max
        page.inputMax.clear();
        page.inputMax.sendKeys(wrongValue.get(ID.max).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgMax).text().equalTo(messages.get(ID.max));
    }

    /**
     * Integer input, verify min
     */
    protected void verifyMin() {

        clickCorrectButton();

        // integer input min
        // selenium.type(inputFormat.format(ID.min), wrongValue.get(ID.min).toString());
        page.inputMin.clear();
        page.inputMin.sendKeys(wrongValue.get(ID.min).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgMin).text().equalTo(messages.get(ID.min));
    }

    /**
     * Integer input, verify min max
     */
    protected void verifyMinMax() {

        clickCorrectButton();

        // integer input min and max
        // selenium.type(inputFormat.format(ID.minMax), wrongValue.get(ID.minMax).toString());
        page.inputMinMax.clear();
        page.inputMinMax.sendKeys(wrongValue.get(ID.minMax).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgMinMax).text().equalTo(messages.get(ID.minMax));
    }

    /**
     * Integer input, verify not empty
     */
    protected void verifyNotEmpty() {

        clickCorrectButton();

        // string input not empty
        // selenium.type(inputFormat.format(ID.notEmpty), wrongValue.get(ID.notEmpty).toString());
        page.inputNotEmpty.clear();
        page.inputNotEmpty.sendKeys(wrongValue.get(ID.notEmpty).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgNotEmpty).text().equalTo(messages.get(ID.notEmpty));
    }

    /**
     * Integer input, verify not null
     */
    protected void verifyNotNull() {

        clickCorrectButton();

        // string input not null
        page.inputNotNull.clear();
        page.inputNotNull.sendKeys("");
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgNotNull).text().equalTo(messages.get(ID.notNull));
    }

    /**
     * Integer input, verify string pattern
     */
    protected void verifyPattern() {

        clickCorrectButton();

        // string input custom pattern
        page.inputPattern.clear();
        page.inputPattern.sendKeys(wrongValue.get(ID.pattern).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgPattern).text().equalTo(messages.get(ID.pattern));
    }

    /**
     * Integer input, verify custom string
     */
    protected void verifyCustom() {

        clickCorrectButton();

        // string input custom string
        page.inputCustom.clear();
        page.inputCustom.sendKeys(wrongValue.get(ID.custom).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgCustom).text().equalTo(messages.get(ID.custom));
    }

    /**
     * Integer input, verify regExp
     */
    protected void verifyRegExp() {

        clickCorrectButton();

        // string input regExp pattern
        page.inputRegexp.clear();
        page.inputRegexp.sendKeys(wrongValue.get(ID.regexp).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgRegexp).text().equalTo(messages.get(ID.regexp));
    }

    /**
     * Integer input, verify date in past
     */
    protected void verifyDatePast() {

        clickCorrectButton();

        // date input past
        page.inputPast.clear();
        page.inputPast.sendKeys(wrongValue.get(ID.past).toString());
        Graphene.guardAjax(page.a4jCommandBtn).click();

        Graphene.waitGui().until().element(page.msgPast).text().equalTo(messages.get(ID.past));
    }

    /**
     * Integer input, verify date in future
     */
    protected void verifyDateFuture() {

        clickCorrectButton();

        // date input future
        page.inputFuture.clear();
        page.inputFuture.sendKeys(wrongValue.get(ID.future).toString());
        page.a4jCommandBtn.click();
        Graphene.waitGui().until().element(page.msgFuture).text().equalTo(messages.get(ID.future));
    }

    /**
     * Integer input, verify string size
     */
    protected void verifyStringSize() {

        clickCorrectButton();

        // string input string size
        page.inputStringSize.clear();
        page.inputStringSize.sendKeys(wrongValue.get(ID.stringSize).toString());
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgStringSize).text().equalTo(messages.get(ID.stringSize));
    }

    /**
     * Integer input, verify selection size
     */
    protected void verifySelectionSize() {

        clickCorrectButton();
        clickWrongButton();

        // many checkBox input selection size
        page.getSelectionItemByLabel(wrongValue.get(ID.size).toString()).click();
        page.a4jCommandBtn.click();

        Graphene.waitGui().until().element(page.msgSize).text().equalTo(messages.get(ID.size));
    }
}
