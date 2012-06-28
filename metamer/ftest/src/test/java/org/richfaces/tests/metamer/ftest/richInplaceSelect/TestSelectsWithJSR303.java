/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.arquillian.ajocado.Graphene.attributeEquals;
import static org.jboss.arquillian.ajocado.Graphene.attributePresent;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.TextContainsCondition;
import org.richfaces.tests.metamer.bean.rich.RichInplaceSelectBean;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;

/**
 * Test for component with JSF-303 validators
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22640 $
 */
public abstract class TestSelectsWithJSR303 extends AbstractGrapheneTest {

    private static final String WRONG_NOT_EMPTY = "";
    private static final String NOT_EMPTY_VALIDATION_MSG = "may not be empty";
    private static final String CORRECT_NOT_EMPTY = RichInplaceSelectBean.NOT_EMPTY_VALIDATION_MSG;

    private static final String WRONG_REG_EXP = "Alaska";
    private static final String CORRECT_REG_EXP = "richfaces";
    private static final String REGEXP_VALIDATION_MSG = RichInplaceSelectBean.REGEXP_VALIDATION_MSG;

    private static final String WRONG_STRING_SIZE = "richfaces";
    private static final String CORRECT_STRING_SIZE = "Alaska";
    private static final String STRING_SIZE_VALIDATION_MSG = RichInplaceSelectBean.STRING_SIZE_VALIDATION_MSG;

    private static final String WRONG_CUSTOM_STRING = "richfaces";
    private static final String CORRECT_CUSTOM_STRING = "RichFaces";
    private static final String CUSTOM_STRING_VALIDATION_MSG = "string is not \"RichFaces\"";

    private static final String WRONG_REQUIRED_STRING = "";
    private static final String CORRECT_REQUIRED_STRING = "richfaces";
    private static final String REQUIRED_VALIDATION_MSG = "value is required";

    private static final String NOT_EMPTY_ID        = "input1";
    private static final String REG_EXP_PATTERN_ID  = "input2";
    private static final String STRING_SIZE_ID      = "input3";
    private static final String CUSTOM_STRING_ID    = "input4";
    private static final String REQUIRED_ID         = "input5";

    private JQueryLocator selectFormat = jq("span[id$={0}Items]");

    private JQueryLocator notEmptySelect = selectFormat.format(NOT_EMPTY_ID);
    private JQueryLocator regExpPatternSelect = selectFormat.format(REG_EXP_PATTERN_ID);
    private JQueryLocator stringSizeSelect = selectFormat.format(STRING_SIZE_ID);
    private JQueryLocator customStringSelect = selectFormat.format(CUSTOM_STRING_ID);
    private JQueryLocator requiredSelect = selectFormat.format(REQUIRED_ID);

    private JQueryLocator option = jq("span.rf-is-opt:contains({0})");
    private JQueryLocator optionEmpty = jq("span.rf-is-opt:eq(51)");

    private JQueryLocator inputFormat = pjq("input[id$=:{0}Input]");

    private JQueryLocator notEmptyInput = inputFormat.format(NOT_EMPTY_ID);
    private JQueryLocator regExpPatternInput = inputFormat.format(REG_EXP_PATTERN_ID);
    private JQueryLocator stringSizeInput = inputFormat.format(STRING_SIZE_ID);
    private JQueryLocator customStringInput = inputFormat.format(CUSTOM_STRING_ID);
    private JQueryLocator requiredInput = inputFormat.format(REQUIRED_ID);


    private JQueryLocator hCommandButton = pjq("input[id$=:hButton]");
    private JQueryLocator a4jCommandButton = pjq("input[id$=:a4jButton]");

    private JQueryLocator outputFormat = pjq("span[id$=:{0}]");

    private JQueryLocator inputMsgFormat = pjq("span.rf-msg-err[id$=:{0}] > span.rf-msg-det");

    private JQueryLocator input1Msg = inputMsgFormat.format(NOT_EMPTY_ID);
    private JQueryLocator input2Msg = inputMsgFormat.format(REG_EXP_PATTERN_ID);
    private JQueryLocator input3Msg = inputMsgFormat.format(STRING_SIZE_ID);
    private JQueryLocator input4Msg = inputMsgFormat.format(CUSTOM_STRING_ID);
    private JQueryLocator input5Msg = inputMsgFormat.format(REQUIRED_ID);

    protected void verifyNotEmpty() {
        setAllCorrect();

        selenium.click(notEmptyInput);
        selenium.click(notEmptySelect.getDescendant(optionEmpty));
        selenium.fireEvent(notEmptyInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input1Msg).text(NOT_EMPTY_VALIDATION_MSG));
        selenium.click(a4jCommandButton);
        waitAjax.until(TextContainsCondition.getInstance().locator(input1Msg).text(NOT_EMPTY_VALIDATION_MSG));

        setAllCorrect();

        selenium.click(notEmptyInput);
        selenium.click(notEmptySelect.getDescendant(optionEmpty));
        selenium.fireEvent(notEmptyInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input1Msg).text(NOT_EMPTY_VALIDATION_MSG));
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();
        waitGui.until(TextContainsCondition.getInstance().locator(input1Msg).text(NOT_EMPTY_VALIDATION_MSG));
    }

    protected void verifyRegExpPattern() {
        setAllCorrect();

        selenium.click(regExpPatternInput);
        selenium.click(regExpPatternSelect.getDescendant(option.format(WRONG_REG_EXP)));
        selenium.fireEvent(regExpPatternInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input2Msg).text(REGEXP_VALIDATION_MSG));
        selenium.click(a4jCommandButton);
        waitGui.until(TextContainsCondition.getInstance().locator(input2Msg).text(REGEXP_VALIDATION_MSG));

        setAllCorrect();

        selenium.click(regExpPatternInput);
        selenium.click(regExpPatternSelect.getDescendant(option.format(WRONG_REG_EXP)));
        selenium.fireEvent(regExpPatternInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input2Msg).text(REGEXP_VALIDATION_MSG));
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();
        waitGui.until(TextContainsCondition.getInstance().locator(input2Msg).text(REGEXP_VALIDATION_MSG));
    }

    protected void verifyStringSize() {
        setAllCorrect();

        selenium.click(stringSizeInput);
        selenium.click(stringSizeSelect.getDescendant(option.format(WRONG_STRING_SIZE)));
        selenium.fireEvent(stringSizeInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input3Msg).text(STRING_SIZE_VALIDATION_MSG));
        selenium.click(a4jCommandButton);
        waitGui.until(TextContainsCondition.getInstance().locator(input3Msg).text(STRING_SIZE_VALIDATION_MSG));

        setAllCorrect();

        selenium.click(stringSizeInput);
        selenium.click(stringSizeSelect.getDescendant(option.format(WRONG_STRING_SIZE)));
        selenium.fireEvent(stringSizeInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input3Msg).text(STRING_SIZE_VALIDATION_MSG));
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();
        waitGui.until(TextContainsCondition.getInstance().locator(input3Msg).text(STRING_SIZE_VALIDATION_MSG));
    }

    protected void verifyCustomString() {
        setAllCorrect();

        selenium.click(customStringInput);
        selenium.click(customStringSelect.getDescendant(option.format(WRONG_CUSTOM_STRING)));
        selenium.fireEvent(customStringInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input4Msg).text(CUSTOM_STRING_VALIDATION_MSG));
        selenium.click(a4jCommandButton);
        waitGui.until(TextContainsCondition.getInstance().locator(input4Msg).text(CUSTOM_STRING_VALIDATION_MSG));

        setAllCorrect();

        selenium.click(customStringInput);
        selenium.click(customStringSelect.getDescendant(option.format(WRONG_CUSTOM_STRING)));
        selenium.fireEvent(customStringInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input4Msg).text(CUSTOM_STRING_VALIDATION_MSG));
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();
        waitGui.until(TextContainsCondition.getInstance().locator(input4Msg).text(CUSTOM_STRING_VALIDATION_MSG));
    }

    protected void verifyRequired() {
        setAllCorrect();

        selenium.click(requiredInput);
        selenium.click(requiredSelect.getDescendant(optionEmpty));
        selenium.fireEvent(requiredInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        // waitGui.until(TextContainsCondition.getInstance().locator(input5Msg).text(REQUIRED_VALIDATION_MSG));
        selenium.click(a4jCommandButton);
        waitGui.until(TextContainsCondition.getInstance().locator(input5Msg).text(REQUIRED_VALIDATION_MSG));

        setAllCorrect();

        selenium.click(requiredInput);
        selenium.click(requiredSelect.getDescendant(optionEmpty));
        selenium.fireEvent(requiredInput, Event.BLUR);
        // give selenium time set new value to appropriate field before submit
        waitGui.until(TextContainsCondition.getInstance().locator(input5Msg).text(REQUIRED_VALIDATION_MSG));
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();
        waitGui.until(TextContainsCondition.getInstance().locator(input5Msg).text(REQUIRED_VALIDATION_MSG));

    }

    protected void verifyAllInputsWrong() {
        setAllCorrect();
        setAllWrong();
        selenium.click(a4jCommandButton);

        waitGui.until(TextContainsCondition.getInstance().locator(input1Msg).text(NOT_EMPTY_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input2Msg).text(REGEXP_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input3Msg).text(STRING_SIZE_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input4Msg).text(CUSTOM_STRING_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input5Msg).text(REQUIRED_VALIDATION_MSG));

        setAllCorrect();
        setAllWrong();
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(TextContainsCondition.getInstance().locator(input1Msg).text(NOT_EMPTY_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input2Msg).text(REGEXP_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input3Msg).text(STRING_SIZE_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input4Msg).text(CUSTOM_STRING_VALIDATION_MSG));
        waitGui.until(TextContainsCondition.getInstance().locator(input5Msg).text(REQUIRED_VALIDATION_MSG));
    }

    protected void verifyAllInputsCorrect() {

        // with full form submit

        // set all to correct first is required to correct working function to set all wrong
        setAllCorrect();
        setAllWrong();
        setAllCorrect();
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(NOT_EMPTY_ID))
            .text(CORRECT_NOT_EMPTY));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(REG_EXP_PATTERN_ID))
            .text(CORRECT_REG_EXP));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(STRING_SIZE_ID))
            .text(CORRECT_STRING_SIZE));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(CUSTOM_STRING_ID))
            .text(CORRECT_CUSTOM_STRING));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(REQUIRED_ID))
            .text(CORRECT_REQUIRED_STRING));

        // with ajax form submit
        setAllWrong();
        setAllCorrect();
        // no submit button click need, values in output fields are updated

        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(NOT_EMPTY_ID))
            .text(CORRECT_NOT_EMPTY));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(REG_EXP_PATTERN_ID))
            .text(CORRECT_REG_EXP));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(STRING_SIZE_ID))
            .text(CORRECT_STRING_SIZE));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(CUSTOM_STRING_ID))
            .text(CORRECT_CUSTOM_STRING));
        waitGui.until(TextContainsCondition.getInstance().locator(outputFormat.format(REQUIRED_ID))
            .text(CORRECT_REQUIRED_STRING));
    }

    private void setAllWrong() {

        selenium.click(notEmptyInput);
        selenium.click(notEmptySelect.getDescendant(optionEmpty));
        waitGui.until(TextContainsCondition.getInstance().locator(notEmptyInput).text(WRONG_NOT_EMPTY));
        selenium.fireEvent(notEmptyInput, Event.BLUR);

        selenium.click(regExpPatternInput);
        selenium.click(regExpPatternSelect.getDescendant(option.format(WRONG_REG_EXP)));
        selenium.fireEvent(regExpPatternInput, Event.BLUR);

        selenium.click(stringSizeInput);
        selenium.click(stringSizeSelect.getDescendant(option.format(WRONG_STRING_SIZE)));
        selenium.fireEvent(stringSizeInput, Event.BLUR);

        selenium.click(customStringInput);
        selenium.click(customStringSelect.getDescendant(option.format(WRONG_CUSTOM_STRING)));
        selenium.fireEvent(customStringInput, Event.BLUR);

        selenium.click(requiredInput);
        selenium.click(requiredSelect.getDescendant(optionEmpty));
        selenium.fireEvent(requiredInput, Event.BLUR);

        waitGui.until(TextContainsCondition.getInstance().locator(input5Msg).text(REQUIRED_VALIDATION_MSG));

    }

    private void setAllCorrect() {

        // selenium.click(notEmptySelect);
        selenium.click(notEmptySelect.getDescendant(option.format(CORRECT_NOT_EMPTY)));
        selenium.fireEvent(notEmptyInput, Event.BLUR);

        // selenium.click(regExpPatternInput);
        selenium.click(regExpPatternSelect.getDescendant(option.format(CORRECT_REG_EXP)));
        selenium.fireEvent(regExpPatternInput, Event.BLUR);

        // selenium.click(stringSizeInput);
        selenium.click(stringSizeSelect.getDescendant(option.format(CORRECT_STRING_SIZE)));
        selenium.fireEvent(stringSizeInput, Event.BLUR);

        // selenium.click(customStringInput);
        selenium.click(customStringSelect.getDescendant(option.format(CORRECT_CUSTOM_STRING)));
        selenium.fireEvent(customStringInput, Event.BLUR);

        // selenium.click(reguiredInput);
        selenium.click(requiredSelect.getDescendant(option.format(CORRECT_REQUIRED_STRING)));
        selenium.fireEvent(requiredInput, Event.BLUR);

        waitGui.until(attributePresent.locator(jq("input[id$=input5selValue]").getAttribute(new Attribute("value"))));
        waitGui.until(attributeEquals.locator(jq("input[id$=input5selValue]").getAttribute(new Attribute("value")))
            .text(CORRECT_REQUIRED_STRING));

        // System.out.println(" leaving setAllCorrect()");

    }

}
