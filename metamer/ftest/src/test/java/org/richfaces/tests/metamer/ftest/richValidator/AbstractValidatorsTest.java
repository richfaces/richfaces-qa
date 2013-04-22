/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import java.util.EnumMap;
import java.util.Map;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
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
public abstract class AbstractValidatorsTest extends AbstractGrapheneTest {

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
    private JQueryLocator inputFormat = pjq("input[id$=:{0}]");
    private JQueryLocator setWrongBtn = pjq("input[id$=setWrongValuesButton]");
    private JQueryLocator setCorrectBtn = pjq("input[id$=setCorrectValuesButton]");
    private JQueryLocator hCommandBtn = pjq("input[id$=hButton]");
    private JQueryLocator a4jCommandBtn = pjq("input[id$=a4jButton]");
    private JQueryLocator selectionItem = pjq("table[id$=:size] tr > td > input[value={0}]");
    private JQueryLocator msgFormat = pjq("span[id$={0}Msg] span.rf-msg-det");
    private String future;
    private String past;
    private boolean firstRun = true;

    @BeforeClass
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
        selenium.click(setCorrectBtn);
        if (selenium.isElementPresent(inputFormat.format(ID.future))
                && selenium.isElementPresent(inputFormat.format(ID.past))) {
            selenium.type(inputFormat.format(ID.past), past);
            selenium.type(inputFormat.format(ID.future), future);
        }
    }

    /**
     * Must set the dates this way beacause of problems with other locale than eng.
     */
    @BeforeMethod(alwaysRun = true)
    public void setDates() {
        if (firstRun) {
            if (selenium.isElementPresent(inputFormat.format(ID.future))
                    && selenium.isElementPresent(inputFormat.format(ID.past))) {
                past = selenium.getValue(inputFormat.format(ID.past));
                future = selenium.getValue(inputFormat.format(ID.future));
                wrongValue.put(AbstractValidatorsTest.ID.past, future);
                wrongValue.put(AbstractValidatorsTest.ID.future, past);
                firstRun = false;
            }
        }
    }

    private void clickWrongButton() {
        selenium.click(setWrongBtn);
        if (selenium.isElementPresent(inputFormat.format(ID.future))
                && selenium.isElementPresent(inputFormat.format(ID.past))) {
            selenium.type(inputFormat.format(ID.past), future);
            selenium.type(inputFormat.format(ID.future), past);
        }
    }

    public void verifyAllWrongWithAjaxSubmit() {
        clickWrongButton();

        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.assertTrue)).text(messages.get(ID.assertTrue)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.assertFalse)).text(messages.get(ID.assertFalse)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.decimalMinMax)).text(messages.get(ID.decimalMinMax)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.digits)).text(messages.get(ID.digits)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.max)).text(messages.get(ID.max)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.min)).text(messages.get(ID.min)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.minMax)).text(messages.get(ID.minMax)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.notEmpty)).text(messages.get(ID.notEmpty)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.notNull)).text(messages.get(ID.notNull)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.pattern)).text(messages.get(ID.pattern)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.custom)).text(messages.get(ID.custom)));

        if (selenium.isElementPresent(inputFormat.format(ID.regexp))) {
            // regExp validator isn't present in JSR303 validation
            waitGui.until(textEquals.locator(msgFormat.format(ID.regexp)).text(messages.get(ID.regexp)));
        }
        waitGui.until(textEquals.locator(msgFormat.format(ID.past)).text(messages.get(ID.past)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.future)).text(messages.get(ID.future)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.stringSize)).text(messages.get(ID.stringSize)));
        // TODO JJa 2011-06-06: remove comment when fixed issue with manyCheckbox validation
        // waitGui.until(textEquals.locator(msgFormat.format(ID.size)).text(messages.get(ID.size)));
    }

    public void verifyAllWrongWithJSFSubmit() {
        clickWrongButton();

        selenium.click(hCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.assertTrue)).text(messages.get(ID.assertTrue)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.assertFalse)).text(messages.get(ID.assertFalse)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.decimalMinMax)).text(messages.get(ID.decimalMinMax)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.digits)).text(messages.get(ID.digits)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.max)).text(messages.get(ID.max)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.min)).text(messages.get(ID.min)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.minMax)).text(messages.get(ID.minMax)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.notEmpty)).text(messages.get(ID.notEmpty)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.notNull)).text(messages.get(ID.notNull)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.pattern)).text(messages.get(ID.pattern)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.custom)).text(messages.get(ID.custom)));

        if (selenium.isElementPresent(inputFormat.format(ID.regexp))) {
            // regExp validator isn't present in JSR303 validation
            waitGui.until(textEquals.locator(msgFormat.format(ID.regexp)).text(messages.get(ID.regexp)));
        }
        waitGui.until(textEquals.locator(msgFormat.format(ID.past)).text(messages.get(ID.past)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.future)).text(messages.get(ID.future)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.stringSize)).text(messages.get(ID.stringSize)));
        waitGui.until(textEquals.locator(msgFormat.format(ID.size)).text(messages.get(ID.size)));
    }

    /**
     * Boolean input, verify true
     */
    public void verifyBooleanTrue() {

        clickCorrectButton();

        // checkBoolean to true
        selenium.check(inputFormat.format(ID.assertTrue), (Boolean) wrongValue.get(ID.assertTrue));

        // guardNoRequest(selenium).click(a4jCommandBtn);
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.assertTrue)).text(messages.get(ID.assertTrue)));
    }

    /**
     * Boolean input, verify false
     */
    public void verifyBooleanFalse() {

        clickCorrectButton();

        // checkBoolean to false
        selenium.check(inputFormat.format(ID.assertFalse), (Boolean) wrongValue.get(ID.assertFalse));
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.assertFalse)).text(messages.get(ID.assertFalse)));
    }

    /**
     * Decimal input, verify from 2.5 to 9.688
     */
    protected void verifyDecimalMinMax() {

        clickCorrectButton();

        // Decimal input
        selenium.type(inputFormat.format(ID.decimalMinMax), wrongValue.get(ID.decimalMinMax).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.decimalMinMax)).text(messages.get(ID.decimalMinMax)));
    }

    /**
     * Decimal input, verify digits
     */
    protected void verifyDecimalDigits() {

        clickCorrectButton();

        // decimal input digits
        selenium.type(inputFormat.format(ID.digits), wrongValue.get(ID.digits).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.decimalMinMax)).text(messages.get(ID.decimalMinMax)));
    }

    /**
     * Integer input, verify max
     */
    protected void verifyMax() {

        clickCorrectButton();

        // integer input max
        selenium.type(inputFormat.format(ID.max), wrongValue.get(ID.max).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.max)).text(messages.get(ID.max)));
    }

    /**
     * Integer input, verify min
     */
    protected void verifyMin() {

        clickCorrectButton();

        // integer input min
        selenium.type(inputFormat.format(ID.min), wrongValue.get(ID.min).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.min)).text(messages.get(ID.min)));
    }

    /**
     * Integer input, verify min max
     */
    protected void verifyMinMax() {

        clickCorrectButton();

        // integer input min and max
        selenium.type(inputFormat.format(ID.minMax), wrongValue.get(ID.minMax).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.minMax)).text(messages.get(ID.minMax)));
    }

    /**
     * Integer input, verify not empty
     */
    protected void verifyNotEmpty() {

        clickCorrectButton();

        // string input not empty
        selenium.type(inputFormat.format(ID.notEmpty), wrongValue.get(ID.notEmpty).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.notEmpty)).text(messages.get(ID.notEmpty)));
    }

    /**
     * Integer input, verify not null
     */
    protected void verifyNotNull() {

        clickCorrectButton();

        // string input not null
        selenium.type(inputFormat.format(ID.notNull), "");
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.notNull)).text(messages.get(ID.notNull)));
    }

    /**
     * Integer input, verify string pattern
     */
    protected void verifyPattern() {

        clickCorrectButton();

        // string input custom pattern
        selenium.type(inputFormat.format(ID.pattern), wrongValue.get(ID.pattern).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.pattern)).text(messages.get(ID.pattern)));
    }

    /**
     * Integer input, verify custom string
     */
    protected void verifyCustom() {

        clickCorrectButton();

        // string input custom string
        selenium.type(inputFormat.format(ID.custom), wrongValue.get(ID.custom).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.custom)).text(messages.get(ID.custom)));
    }

    /**
     * Integer input, verify regExp
     */
    protected void verifyRegExp() {

        clickCorrectButton();

        // string input regExp pattern
        selenium.type(inputFormat.format(ID.regexp), wrongValue.get(ID.regexp).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.regexp)).text(messages.get(ID.regexp)));
    }

    /**
     * Integer input, verify date in past
     */
    protected void verifyDatePast() {

        clickCorrectButton();

        // date input past
        selenium.type(inputFormat.format(ID.past), wrongValue.get(ID.past).toString());
        guardXhr(selenium).click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.past)).text(messages.get(ID.past)));
    }

    /**
     * Integer input, verify date in future
     */
    protected void verifyDateFuture() {

        clickCorrectButton();

        // date input future
        selenium.type(inputFormat.format(ID.future), wrongValue.get(ID.future).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.future)).text(messages.get(ID.future)));
    }

    /**
     * Integer input, verify string size
     */
    protected void verifyStringSize() {

        clickCorrectButton();

        // string input string size
        selenium.type(inputFormat.format(ID.stringSize), wrongValue.get(ID.stringSize).toString());
        selenium.click(a4jCommandBtn);

        waitGui.until(textEquals.locator(msgFormat.format(ID.stringSize)).text(messages.get(ID.stringSize)));
    }

    /**
     * Integer input, verify selection size
     */
    protected void verifySelectionSize() {

        clickCorrectButton();
        clickWrongButton();

        // many checkBox input selection size
        // selenium.type(inputFormat.format(ID.size), wrongValue.get(ID.size).toString());
        selenium.check(selectionItem.format(wrongValue.get(ID.size)), true);

        selenium.click(a4jCommandBtn);
        waitGui.until(textEquals.locator(msgFormat.format(ID.size)).text(messages.get(ID.size)));
    }
}
