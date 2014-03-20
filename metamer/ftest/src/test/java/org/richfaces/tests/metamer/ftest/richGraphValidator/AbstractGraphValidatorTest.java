/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richGraphValidator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.CheckboxInputComponentImpl;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.tests.metamer.bean.rich.RichGraphValidatorBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;

/**
 * Test for page /faces/components/richGraphValidator/all.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractGraphValidatorTest extends AbstractWebDriverTest {

    final Attributes<GraphValidatorAttributes> graphValidatorAttributes = getAttributes();

    static final String CORRECT_STRING_VALUE = ":-)";
    static final String CORRECT_INT_VALUE = "10";
    static final String WRONG_STRING_VALUE = ":-(";
    static final String WRONG_INT_VALUE = "11";

    @FindBy(css = "input[id$=applyChanges]")
    WebElement applyChangesBtn;
    @FindBy(css = "[id$=graphValidatorPanel]")
    WebElement panel;

    @FindBy(css = "input[id$=autocompleteInput]")
    TextInputComponentImpl autocomplete;
    @FindBy(css = "[id$=selectBooleanCheckbox]")
    CheckboxInputComponentImpl booleanCheckbox;
    @FindBy(css = "input[id$=inputSecret]")
    TextInputComponentImpl inputSecret;
    @FindBy(css = "input[id$=inputText]")
    TextInputComponentImpl inputText;
    @FindBy(css = "textarea[id$=inputTextarea]")
    TextInputComponentImpl inputTextarea;
    @FindBy(css = "span[id$=inputNumberSlider]")
    RichFacesInputNumberSlider slider;
    @FindBy(css = "span[id$=inputNumberSpinner]")
    RichFacesInputNumberSpinner spinner;

    @FindBy(css = "span[id$=inputSecret]")
    RichFacesMessage inputSecretMsg;
    @FindBy(css = "span[id$=graphValidatorGlobalMessages]")
    RichFacesMessages graphValidatorGlobalMessages;
    @FindBy(css = "span[id$=graphValidatorMessages]")
    RichFacesMessages graphValidatorMessages;

    Group group;

    enum Group {

        DEFAULT("Default"), NULL("null"),
        ValidationGroupAllComponents,
        ValidationGroupBooleanInputs,
        ValidationGroupNumericInputs;
        private final String group;

        private Group(String group) {
            this.group = group;
        }

        private Group() {
            this.group = name();
        }

        @Override
        public String toString() {
            return group;
        }
    }

    protected void applyChanges() {
        MetamerPage.waitRequest(applyChangesBtn, WaitRequestType.XHR).click();
    }

    protected void checkGraphValidatorSuccessMessage() {
        //now all inputs are correct
        //there should be graph validator successfull message, which is not bound to any input > will be global
        Assert.assertFalse(graphValidatorMessages.advanced().isVisible(), "Graph validator messages should not be visible.");
        Assert.assertTrue(graphValidatorGlobalMessages.advanced().isVisible(), "Global messages should be visible.");
        Assert.assertEquals(graphValidatorGlobalMessages.size(), 1, "There should be one message.");
        Assert.assertEquals(graphValidatorGlobalMessages.getItem(0).getSummary(),
            RichGraphValidatorBean.SUCCESSFULL_ACTION_MSG, "Summary of message.");
    }

    protected String getMessageForGroup(Group g) {
        switch (g) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                return RichGraphValidatorBean.VALIDATION_MSG_ALL;
            case ValidationGroupBooleanInputs:
                return RichGraphValidatorBean.VALIDATION_MSG_BOOLEANS;
            case ValidationGroupNumericInputs:
                return RichGraphValidatorBean.VALIDATION_MSG_NUMERICS;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
    }

    protected void setCorrectSettingForGroup(Group g) {
        switch (g) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                autocomplete.advanced().clear(ClearType.JS).sendKeys(CORRECT_STRING_VALUE);
                inputText.advanced().clear(ClearType.JS).sendKeys(CORRECT_STRING_VALUE);
                inputTextarea.advanced().clear(ClearType.JS).sendKeys(CORRECT_STRING_VALUE);
                break;
            case ValidationGroupBooleanInputs:
                booleanCheckbox.check();
                break;
            case ValidationGroupNumericInputs:
                spinner.advanced().getInput().advanced().clear(ClearType.JS).sendKeys(CORRECT_INT_VALUE);
                slider.advanced().getInput().advanced().clear(ClearType.JS).sendKeys(CORRECT_INT_VALUE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        applyChanges();
    }

    protected void setInputSecretCorrect() {
        // only inputSecret doesn't keep entered value after submit
        inputSecret.advanced().clear(ClearType.JS).sendKeys(RichGraphValidatorBean.SMILE);
    }

    protected void setWrongSettingForGroup(Group g) {
        switch (g) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                autocomplete.advanced().clear(ClearType.JS).sendKeys(WRONG_STRING_VALUE);
                inputText.advanced().clear(ClearType.JS).sendKeys(WRONG_STRING_VALUE);
                inputTextarea.advanced().clear(ClearType.JS).sendKeys(WRONG_STRING_VALUE);
                break;
            case ValidationGroupBooleanInputs:
                booleanCheckbox.uncheck();
                break;
            case ValidationGroupNumericInputs:
                spinner.advanced().getInput().advanced().clear(ClearType.JS).sendKeys(WRONG_INT_VALUE);
                slider.advanced().getInput().advanced().clear(ClearType.JS).sendKeys(WRONG_INT_VALUE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        applyChanges();
    }
}
