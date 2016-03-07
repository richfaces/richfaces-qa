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

import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Map;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.popupPanel.PopupPanel;
import org.richfaces.fragment.status.Status.StatusState;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
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

import com.google.common.base.Predicate;

/**
 * Abstract class with selenium test for validators
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 */
public abstract class AbstractValidatorsTest extends AbstractWebDriverTest {

    private final Map<ID, String[]> messages = new EnumMap<AbstractValidatorsTest.ID, String[]>(AbstractValidatorsTest.ID.class);
    private final Map<ID, String> wrongValue = new EnumMap<AbstractValidatorsTest.ID, String>(AbstractValidatorsTest.ID.class);

    @Page
    private ValidatorSimplePage page;

    private void checkAllErrorMessagesAreVisibleAndCorrect() {
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.assertFalse);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.assertTrue);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.custom);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.decimalMinMax);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.digits);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.future);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.max);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.min);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.minMax);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.notEmpty);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.notNull);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.past);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.pattern);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.size);
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.stringSize);

        if (new WebElementConditionFactory(getPage().getInputRegexp()).isPresent().apply(driver)) {
            // regExp validator isn't present in JSR303 validation
            waitUtilMessageWithIDIsVisibleAndCorrect(ID.regexp);
        }
    }

    private void clickCorrectButton() {
        getMetamerPage().performJSClickOnButton(getPage().getSetCorrectBtn(), WaitRequestType.NONE);
    }

    private void clickWrongButton() {
        getMetamerPage().performJSClickOnButton(getPage().getSetWrongBtn(), WaitRequestType.NONE);
    }

    protected RichFacesMessage getMessageForID(ID id) {
        switch (id) {
            case assertFalse:
                return getPage().getMsgAssertFalse();
            case assertTrue:
                return getPage().getMsgAssertTrue();
            case custom:
                return getPage().getMsgCustom();
            case decimalMinMax:
                return getPage().getMsgDecimalMinMax();
            case digits:
                return getPage().getMsgDigits();
            case future:
                return getPage().getMsgFuture();
            case max:
                return getPage().getMsgMax();
            case min:
                return getPage().getMsgMin();
            case minMax:
                return getPage().getMsgMinMax();
            case notEmpty:
                return getPage().getMsgNotEmpty();
            case notNull:
                return getPage().getMsgNotNull();
            case past:
                return getPage().getMsgPast();
            case pattern:
                return getPage().getMsgPattern();
            case regexp:
                return getPage().getMsgRegexp();
            case size:
                return getPage().getMsgSize();
            case stringSize:
                return getPage().getMsgStringSize();
            default:
                throw new UnsupportedOperationException("Unsupported id " + id);
        }
    }

    /**
     * @return the page
     */
    public ValidatorSimplePage getPage() {
        return page;
    }

    @BeforeClass(groups = "smoke")
    public void init() {
        messages.put(ID.assertTrue, new String[] { AssertTrueBean.VALIDATION_MSG });
        messages.put(ID.assertFalse, new String[] { AssertFalseBean.VALIDATION_MSG });
        messages.put(ID.decimalMinMax, new String[] { DecimalMinMaxBean.VALIDATION_MSG });
        messages.put(ID.digits, new String[] { DigitsBean.VALIDATION_MSG });
        messages.put(ID.future, new String[] { FutureBean.VALIDATION_MSG });
        messages.put(ID.max, new String[] { MaxBean.VALIDATION_MSG });
        messages.put(ID.min, new String[] { MinBean.VALIDATION_MSG });
        messages.put(ID.minMax, new String[] { MinMaxBean.VALIDATION_MSG });
        messages.put(ID.notEmpty, new String[] { NotEmptyBean.VALIDATION_MSG });
        messages.put(ID.notNull, new String[] { NotNullBean.VALIDATION_MSG });
        messages.put(ID.past, new String[] { PastBean.VALIDATION_MSG });
        messages.put(ID.pattern, new String[] { PatternBean.VALIDATION_MSG });
        messages.put(ID.custom, new String[] { StringRichFacesValidator.VALIDATION_ERROR_MSG });
        messages.put(ID.regexp, new String[] { "Regex pattern of '\\d{3}' not matched", "Validation Error: Value not according to pattern '\\d{3}'" });
        messages.put(ID.stringSize, new String[] { StringSizeBean.VALIDATION_MSG });
        messages.put(ID.size, new String[] { SizeBean.VALIDATION_MSG }); // RF-11035

        wrongValue.put(ID.assertTrue, "false");
        wrongValue.put(ID.assertFalse, "true");
        wrongValue.put(ID.decimalMinMax, "10.688");
        wrongValue.put(ID.digits, "15.627123");
        wrongValue.put(ID.future, "1 Jan 2013");
        wrongValue.put(ID.max, "122");
        wrongValue.put(ID.min, "-544");
        wrongValue.put(ID.minMax, "-5");
        wrongValue.put(ID.notEmpty, "");
        wrongValue.put(ID.notNull, null);
        wrongValue.put(ID.past, "1 Jan 3013");
        wrongValue.put(ID.pattern, "@@@");
        wrongValue.put(ID.custom, "@@@");
        wrongValue.put(ID.regexp, "@@@");
        wrongValue.put(ID.stringSize, "JSF 2");
        wrongValue.put(ID.size, "B"); // RF-11035
    }

    protected void preventViewExpiredException() {
        getMetamerPage().getResponseDelayElement().click();
        waiting(500);
        getMetamerPage().getStatus().advanced().waitUntilStatusStateChanges(StatusState.STOP).perform();
    }

    @BeforeMethod(alwaysRun = true)
    public void resizePopup() {
        if (isInPopupTemplate()) {
            jsUtils.scrollToView(popupTemplate.advanced().getContentElement());
            popupTemplate.advanced().resizeFromLocation(PopupPanel.ResizerLocation.S, 100, 0);
        }
    }

    protected void submitAjax() {
        getMetamerPage().performJSClickOnButton(getPage().getA4jCommandBtn(), WaitRequestType.XHR);
    }

    public void verifyAllWrongWithAjaxSubmit() {
        clickWrongButton();
        submitAjax();
        checkAllErrorMessagesAreVisibleAndCorrect();
    }

    public void verifyAllWrongWithJSFSubmit() {
        clickWrongButton();
        waiting(3000);// stabilization wait time, wait for all ajax requests are completed
        getMetamerPage().getStatus().advanced().waitUntilStatusStateChanges(StatusState.STOP).perform();
        getMetamerPage().performJSClickOnButton(getPage().gethCommandBtn(), WaitRequestType.HTTP);
        checkAllErrorMessagesAreVisibleAndCorrect();
    }

    /**
     * Boolean input, verify false
     */
    public void verifyBooleanFalse() {
        clickCorrectButton();

        // checkBoolean to false
        getPage().getInputAssertFalse().click();
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.assertFalse);
    }

    /**
     * Boolean input, verify true
     */
    public void verifyBooleanTrue() {
        clickCorrectButton();

        // checkBoolean to true
        getPage().getInputAssertTrue().click();
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.assertTrue);
    }

    /**
     * Integer input, verify custom string
     */
    protected void verifyCustom() {
        clickCorrectButton();

        // string input custom string
        getPage().getInputCustom().clear();
        getPage().getInputCustom().sendKeys(wrongValue.get(ID.custom));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.custom);
        preventViewExpiredException();
    }

    /**
     * Integer input, verify date in future
     */
    protected void verifyDateFuture() {
        clickCorrectButton();

        // date input future
        getPage().getInputFuture().clear();
        getPage().getInputFuture().sendKeys(wrongValue.get(ID.future));
        submitAjax();
        waitUtilMessageWithIDIsVisibleAndCorrect(ID.future);
        preventViewExpiredException();
    }

    /**
     * Integer input, verify date in past
     */
    protected void verifyDatePast() {
        clickCorrectButton();

        // date input past
        getPage().getInputPast().clear();
        getPage().getInputPast().sendKeys(wrongValue.get(ID.past));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.past);
        preventViewExpiredException();
    }

    /**
     * Decimal input, verify digits
     */
    protected void verifyDecimalDigits() {
        clickCorrectButton();

        // decimal input digits
        getPage().getInputDigits().clear();
        getPage().getInputDigits().sendKeys(wrongValue.get(ID.digits));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.digits);
    }

    /**
     * Decimal input, verify from 2.5 to 9.688
     */
    protected void verifyDecimalMinMax() {
        clickCorrectButton();

        // Decimal input
        getPage().getInputDecimalMinMax().clear();
        getPage().getInputDecimalMinMax().sendKeys(wrongValue.get(ID.decimalMinMax));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.decimalMinMax);
        preventViewExpiredException();
    }

    /**
     * Integer input, verify max
     */
    protected void verifyMax() {
        clickCorrectButton();

        // integer input max
        getPage().getInputMax().clear();
        getPage().getInputMax().sendKeys(wrongValue.get(ID.max));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.max);
    }

    /**
     * Integer input, verify min
     */
    protected void verifyMin() {
        clickCorrectButton();

        // integer input min
        // selenium.type(inputFormat.format(ID.min), wrongValue.get(ID.min));
        getPage().getInputMin().clear();
        getPage().getInputMin().sendKeys(wrongValue.get(ID.min));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.min);
    }

    /**
     * Integer input, verify min max
     */
    protected void verifyMinMax() {
        clickCorrectButton();

        // integer input min and max
        // selenium.type(inputFormat.format(ID.minMax), wrongValue.get(ID.minMax));
        getPage().getInputMinMax().clear();
        getPage().getInputMinMax().sendKeys(wrongValue.get(ID.minMax));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.minMax);
    }

    /**
     * Integer input, verify not empty
     */
    protected void verifyNotEmpty() {
        clickCorrectButton();

        // string input not empty
        // selenium.type(inputFormat.format(ID.notEmpty), wrongValue.get(ID.notEmpty));
        getPage().getInputNotEmpty().clear();
        getPage().getInputNotEmpty().sendKeys(wrongValue.get(ID.notEmpty));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.notEmpty);
    }

    /**
     * Integer input, verify not null
     */
    protected void verifyNotNull() {
        clickCorrectButton();

        // string input not null
        getPage().getInputNotNull().clear();
        getPage().getInputNotNull().sendKeys("");
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.notNull);
    }

    /**
     * Integer input, verify string pattern
     */
    protected void verifyPattern() {
        clickCorrectButton();

        // string input custom pattern
        getPage().getInputPattern().clear();
        getPage().getInputPattern().sendKeys(wrongValue.get(ID.pattern));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.pattern);
    }

    /**
     * Integer input, verify regExp
     */
    protected void verifyRegExp() {
        clickCorrectButton();

        // string input regExp pattern
        getPage().getInputRegexp().clear();
        getPage().getInputRegexp().sendKeys(wrongValue.get(ID.regexp));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.regexp);
    }

    /**
     * Integer input, verify selection size
     */
    protected void verifySelectionSize() {
        clickCorrectButton();

        // many checkBox input selection size
        WebElement selectionItemByLabel = getPage().getSelectionItemByLabel(wrongValue.get(ID.size));
        jsUtils.scrollToView(selectionItemByLabel);
        selectionItemByLabel.click();
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.size);
    }

    /**
     * Integer input, verify string size
     */
    protected void verifyStringSize() {
        clickCorrectButton();

        // string input string size
        getPage().getInputStringSize().clear();
        getPage().getInputStringSize().sendKeys(wrongValue.get(ID.stringSize));
        submitAjax();

        waitUtilMessageWithIDIsVisibleAndCorrect(ID.stringSize);
    }

    protected void waitUtilMessageWithIDIsVisibleAndCorrect(final ID id) {
        Graphene
            .waitGui()
            .until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver t) {
                    // 'endsWith' because of MyFaces is inserting the input's label before the message
                    for (String expectedMsg : messages.get(id)) {
                        if (getMessageForID(id).getDetail().endsWith(expectedMsg)) {
                            return Boolean.TRUE;
                        }
                    }
                    return Boolean.FALSE;
                }

                @Override
                public String toString() {
                    return MessageFormat.format("message with ID: {0}, to be visible and contain expected text: {1}. Actual text: {2}", id, messages.get(id), getMessageForID(id).getDetail());
                }
            });
    }

    protected enum ID {

        /**
         * Boolean, true
         */
        assertTrue, /**
         * Boolean false
         */
        assertFalse, /**
         * Decimal from 2.5 to 9.688
         */
        decimalMinMax, /**
         * Decimal 3 digits and 4 fract
         */
        digits, /**
         * Integer max 10
         */
        max, /**
         * Integer min 2
         */
        min, /**
         * Integer from 2 to 10
         */
        minMax, /**
         * Text, not empty
         */
        notEmpty, /**
         * Text, not null
         */
        notNull, /**
         * Text, pattern '[a-z].*'
         */
        pattern, /**
         * custom validator
         */
        custom, /**
         * custom regExp validator
         */
        regexp, /**
         * date past
         */
        past, /**
         * date future
         */
        future, /**
         * String size from 2 to 4
         */
        stringSize, /**
         * Selection size
         */
        size
    }
}
