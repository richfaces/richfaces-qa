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
package org.richfaces.tests.metamer.ftest.richGraphValidator;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.graphValidatorAttributes;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.abstractions.StringInputValidationBean;
import org.richfaces.tests.metamer.bean.rich.RichGraphValidatorBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.input.CheckboxInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.input.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.tests.page.fragments.impl.input.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;
import org.richfaces.tests.page.fragments.impl.messages.RichFacesMessages;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test for page /faces/components/richGraphValidator/all.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichGraphValidator extends AbstractWebDriverTest {

    private static final String CORRECT_STRING_VALUE = ":-)";
    private static final String CORRECT_INT_VALUE = "10";
    private static final String WRONG_STRING_VALUE = ":-(";
    private static final String WRONG_INT_VALUE = "11";
    @FindBy(css = "input[id$=applyChanges]")
    private WebElement applyChangesBtn;
    @FindBy(css = "[id$=graphValidatorPanel]")
    private WebElement panel;
    //
    @FindBy(css = "input[id$=autocompleteInput]")
    private TextInputComponentImpl autocomplete;
    @FindBy(css = "[id$=selectBooleanCheckbox]")
    private CheckboxInputComponentImpl booleanCheckbox;
    @FindBy(css = "input[id$=inputSecret]")
    private TextInputComponentImpl inputSecret;
    @FindBy(css = "input[id$=inputText]")
    private TextInputComponentImpl inputText;
    @FindBy(css = "textarea[id$=inputTextarea]")
    private TextInputComponentImpl inputTextarea;
    @FindBy(css = "span[id$=inputNumberSlider]")
    private RichFacesInputNumberSlider slider;
    @FindBy(css = "span[id$=inputNumberSpinner]")
    private RichFacesInputNumberSpinner spinner;
    //
    @FindBy(css = "span[id$=inputSecret]")
    private RichFacesMessage inputSecretMsg;
    @FindBy(css = "span[id$=graphValidatorGlobalMessages]")
    private RichFacesMessages graphValidatorGlobalMessages;
    @FindBy(css = "span[id$=graphValidatorMessages]")
    private RichFacesMessages graphValidatorMessages;
    @Inject
    @Use(empty = false)
    private Group group;

    private enum Group {

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

    private void applyChanges() {
        MetamerPage.waitRequest(applyChangesBtn, WaitRequestType.XHR).click();
    }

    private void checkGraphValidatorSuccessMessage() {
        //now all inputs are correct
        //there should be graph validator successfull message, which is not bound to any input > will be global
        Assert.assertFalse(graphValidatorMessages.isVisible(), "Graph validator messages should not be visible.");
        Assert.assertTrue(graphValidatorGlobalMessages.isVisible(), "Global messages should be visible.");
        Assert.assertEquals(graphValidatorGlobalMessages.size(), 1, "There should be one message.");
        Assert.assertEquals(graphValidatorGlobalMessages.getMessage(0).getSummary(),
                RichGraphValidatorBean.SUCCESSFULL_ACTION_MSG, "Summary of message.");
    }

    private String getMessageForGroup(Group g) {
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

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richGraphValidator/all.xhtml");
    }

    private void setCorrectSettingForGroup(Group g) {
        switch (g) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                autocomplete.clear(ClearType.JS).fillIn(CORRECT_STRING_VALUE);
                inputText.clear(ClearType.JS).fillIn(CORRECT_STRING_VALUE);
                inputTextarea.clear(ClearType.JS).fillIn(CORRECT_STRING_VALUE);
                break;
            case ValidationGroupBooleanInputs:
                booleanCheckbox.check();
                break;
            case ValidationGroupNumericInputs:
                spinner.getInput().clear(ClearType.JS).fillIn(CORRECT_INT_VALUE);
                slider.getInput().clear(ClearType.JS).fillIn(CORRECT_INT_VALUE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        applyChanges();
    }

    private void setInputSecretCorrect() {
        // only inputSecret doesn't keep entered value after submit
        inputSecret.clear(ClearType.JS).fillIn(RichGraphValidatorBean.SMILE);
    }

    private void setWrongSettingForGroup(Group g) {
        switch (g) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                autocomplete.clear(ClearType.JS).fillIn(WRONG_STRING_VALUE);
                inputText.clear(ClearType.JS).fillIn(WRONG_STRING_VALUE);
                inputTextarea.clear(ClearType.JS).fillIn(WRONG_STRING_VALUE);
                break;
            case ValidationGroupBooleanInputs:
                booleanCheckbox.uncheck();
                break;
            case ValidationGroupNumericInputs:
                spinner.getInput().clear(ClearType.JS).fillIn(WRONG_INT_VALUE);
                slider.getInput().clear(ClearType.JS).fillIn(WRONG_INT_VALUE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        applyChanges();
    }

    @Test
    @Use(field = "group", enumeration = true)
    public void testGroups() {
        graphValidatorAttributes.set(GraphValidatorAttributes.groups, group.toString());
        Assert.assertFalse(graphValidatorGlobalMessages.isVisible(), "Global messages should not be visible.");
        Assert.assertFalse(graphValidatorMessages.isVisible(), "Messages should be visible.");
        applyChanges();
        //a message for empty input secret should be displayed, it is validated by @NotNull
        Assert.assertTrue(inputSecretMsg.isVisible(), "Messages for input secret should be visible.");
        String summary = inputSecretMsg.getDetail();
        if (!summary.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG)
                && summary.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG2)
                && summary.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG3)) {
            throw new AssertionError("The message summary is not equal to some of expected values.");
        }
        setInputSecretCorrect();//all inputs are correct now, not submitted yet

        //set some of group inputs to bad value
        setWrongSettingForGroup(group);
        //graph validator message should be displayed, validation of group should fail
        Assert.assertFalse(graphValidatorGlobalMessages.isVisible(), "Global messages should not be visible.");
        Assert.assertTrue(graphValidatorMessages.isVisible(), "Graph validator messages should be visible.");
        Assert.assertEquals(graphValidatorMessages.size(), 1, "There should be one message.");
        Assert.assertEquals(graphValidatorMessages.getMessage(0).getSummary(),
                getMessageForGroup(group), "Summary of message.");
        setCorrectSettingForGroup(group);
        checkGraphValidatorSuccessMessage();

        //check that groups-only inputs are validated and are not influenced by other inputs (but they must also pass bean validation)
        switch (group) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                //no testing, this groups covers all inputs, cannot set value of non-group input
                return;
            case ValidationGroupBooleanInputs:
                //this should not influence the validation of this group
                setWrongSettingForGroup(Group.ValidationGroupNumericInputs);
                setWrongSettingForGroup(Group.ValidationGroupAllComponents);//no number input components in setting
                break;
            case ValidationGroupNumericInputs:
                //this should not influence the validation of this group
                setWrongSettingForGroup(Group.ValidationGroupBooleanInputs);
                setWrongSettingForGroup(Group.ValidationGroupAllComponents);//no number input components in setting
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        checkGraphValidatorSuccessMessage();
    }

    @Test
    public void testSummary() {
        String msg = "My own validation message!";
        graphValidatorAttributes.set(GraphValidatorAttributes.summary, msg);

        setInputSecretCorrect();//all inputs are correct now, not submitted yet
        setWrongSettingForGroup(Group.ValidationGroupAllComponents);
        applyChanges();

        Assert.assertFalse(graphValidatorGlobalMessages.isVisible(), "Global messages should not be visible.");
        Assert.assertTrue(graphValidatorMessages.isVisible(), "Graph validator messages should be visible.");
        Assert.assertEquals(graphValidatorMessages.size(), 1, "There should be one message.");
        Assert.assertEquals(graphValidatorMessages.getMessage(0).getSummary(), msg, "Summary of message.");
    }

    @Test
    public void testRendered() {
        assertPresent(panel, "Panel should be present.");
        graphValidatorAttributes.set(GraphValidatorAttributes.rendered, Boolean.FALSE);
        assertNotPresent(panel, "Panel should not be present.");
    }
}
