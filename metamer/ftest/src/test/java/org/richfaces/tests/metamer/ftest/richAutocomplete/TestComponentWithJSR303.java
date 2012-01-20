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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;

/**
 * Test for component with JSF-303 validators
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22534 $
 */
public abstract class TestComponentWithJSR303 extends AbstractAjocadoTest {

    private static final String NOT_EMPTY_VALIDATION_MSG = "may not be empty";
    private static final String CORRECT_NOT_EMPTY = "xyz";

    private static final String WRONG_REG_EXP = "1a^";
    private static final String CORRECT_REG_EXP = "a2^E";
    private static final String REGEXP_VALIDATION_MSG = "must match \"[a-z].*\"";


    private static final String WRONG_STRING_SIZE = "x";
    private static final String CORRECT_STRING_SIZE = "abc3";
    private static final String STRING_SIZE_VALIDATION_MSG = "size must be between 3 and 6";

    private static final String WRONG_CUSTOM_STRING = "rich faces";
    private static final String CORRECT_CUSTOM_STRING = "RichFaces";
    private static final String CUSTOM_STRING_VALIDATION_MSG = "string is not \"RichFaces\"";

    private JQueryLocator notEmptyInput = pjq("input[id$=:input1Input]");
    private JQueryLocator regExpPatternInput = pjq("input[id$=:input2Input]");
    private JQueryLocator stringSizeInput = pjq("input[id$=:input3Input]");
    private JQueryLocator customStringInput = pjq("input[id$=:input4Input]");

    private JQueryLocator hCommandButton = pjq("input[id$=:hButton]");
    private JQueryLocator a4jCommandButton = pjq("input[id$=:a4jButton]");

    private JQueryLocator output1 = pjq("span[id$=:output1]");
    private JQueryLocator output2 = pjq("span[id$=:output2]");
    private JQueryLocator output3 = pjq("span[id$=:output3]");
    private JQueryLocator output4 = pjq("span[id$=:output4]");

    private JQueryLocator input1Msg = pjq("span.rf-msg-err[id$=:input1]");
    private JQueryLocator input2Msg = pjq("span.rf-msg-err[id$=:input2]");
    private JQueryLocator input3Msg = pjq("span.rf-msg-err[id$=:input3]");
    private JQueryLocator input4Msg = pjq("span.rf-msg-err[id$=:input4]");

    protected void verifyNotEmpty() {
        setAllCorrect(false);
        selenium.type(notEmptyInput, "");
        selenium.click(a4jCommandButton);

        waitGui.until(textEquals.locator(input1Msg.getChild(jq("span.rf-msg-det"))).text(NOT_EMPTY_VALIDATION_MSG));

        setAllCorrect(false);
        selenium.type(notEmptyInput, "");
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(textEquals.locator(input1Msg.getChild(jq("span.rf-msg-det"))).text(NOT_EMPTY_VALIDATION_MSG));
    }

    protected void verifyRegExpPattern() {

        setAllCorrect(false);
        selenium.type(regExpPatternInput, WRONG_REG_EXP);
        selenium.click(a4jCommandButton);

        waitGui.until(textEquals.locator(input2Msg.getChild(jq("span.rf-msg-det"))).text(REGEXP_VALIDATION_MSG));

        setAllCorrect(false);
        selenium.type(regExpPatternInput, WRONG_REG_EXP);
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(textEquals.locator(input2Msg.getChild(jq("span.rf-msg-det"))).text(REGEXP_VALIDATION_MSG));
    }

    protected void verifyStringSize() {
        setAllCorrect(false);
        selenium.type(stringSizeInput, WRONG_STRING_SIZE);
        selenium.click(a4jCommandButton);

        waitGui.until(textEquals.locator(input3Msg.getChild(jq("span.rf-msg-det"))).text(STRING_SIZE_VALIDATION_MSG));

        setAllCorrect(false);
        selenium.type(stringSizeInput, WRONG_STRING_SIZE);
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(textEquals.locator(input3Msg.getChild(jq("span.rf-msg-det"))).text(STRING_SIZE_VALIDATION_MSG));
    }

    protected void verifyCustomString() {
        setAllCorrect(false);
        selenium.type(customStringInput, WRONG_CUSTOM_STRING);
        selenium.click(a4jCommandButton);

        waitGui.until(textEquals.locator(input4Msg.getChild(jq("span.rf-msg-det"))).text(CUSTOM_STRING_VALIDATION_MSG));

        setAllCorrect(false);
        selenium.type(customStringInput, WRONG_CUSTOM_STRING);
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(textEquals.locator(input4Msg.getChild(jq("span.rf-msg-det"))).text(CUSTOM_STRING_VALIDATION_MSG));
    }

    protected void verifyAllInputsWrong() {
        setAllWrong(false);
        selenium.click(a4jCommandButton);

        waitGui.until(textEquals.locator(input1Msg.getChild(jq("span.rf-msg-det"))).text(NOT_EMPTY_VALIDATION_MSG));
        waitGui.until(textEquals.locator(input2Msg.getChild(jq("span.rf-msg-det"))).text(REGEXP_VALIDATION_MSG));
        waitGui.until(textEquals.locator(input3Msg.getChild(jq("span.rf-msg-det"))).text(STRING_SIZE_VALIDATION_MSG));
        waitGui.until(textEquals.locator(input4Msg.getChild(jq("span.rf-msg-det"))).text(CUSTOM_STRING_VALIDATION_MSG));

        setAllCorrect(false);
        setAllWrong(false);
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(textEquals.locator(input1Msg.getChild(jq("span.rf-msg-det"))).text(NOT_EMPTY_VALIDATION_MSG));
        waitGui.until(textEquals.locator(input2Msg.getChild(jq("span.rf-msg-det"))).text(REGEXP_VALIDATION_MSG));
        waitGui.until(textEquals.locator(input3Msg.getChild(jq("span.rf-msg-det"))).text(STRING_SIZE_VALIDATION_MSG));
        waitGui.until(textEquals.locator(input4Msg.getChild(jq("span.rf-msg-det"))).text(CUSTOM_STRING_VALIDATION_MSG));

    }

    protected void verifyAllInputsCorrect() {

        // with full form submit
        setAllWrong(true);
        setAllCorrect(false);
        selenium.click(hCommandButton);
        selenium.waitForPageToLoad();

        waitGui.until(textEquals.locator(output1).text(CORRECT_NOT_EMPTY));
        waitGui.until(textEquals.locator(output2).text(CORRECT_REG_EXP));
        waitGui.until(textEquals.locator(output3).text(CORRECT_STRING_SIZE));
        waitGui.until(textEquals.locator(output4).text(CORRECT_CUSTOM_STRING));

        // with ajax (no need click a4j:commandButton)
        setAllWrong(true);
        setAllCorrect(true);

        waitGui.until(textEquals.locator(output1).text(CORRECT_NOT_EMPTY));
        waitGui.until(textEquals.locator(output2).text(CORRECT_REG_EXP));
        waitGui.until(textEquals.locator(output3).text(CORRECT_STRING_SIZE));
        waitGui.until(textEquals.locator(output4).text(CORRECT_CUSTOM_STRING));
    }

    private void setAllWrong(boolean withBlur) {
        selenium.type(notEmptyInput, "");
        if (withBlur) { selenium.fireEvent(notEmptyInput, Event.BLUR); }

        selenium.type(regExpPatternInput, WRONG_REG_EXP);
        if (withBlur) { selenium.fireEvent(regExpPatternInput, Event.BLUR); }

        selenium.type(stringSizeInput, WRONG_STRING_SIZE);
        if (withBlur) { selenium.fireEvent(stringSizeInput, Event.BLUR); }

        selenium.type(customStringInput, WRONG_CUSTOM_STRING);
        if (withBlur) { selenium.fireEvent(customStringInput, Event.BLUR); }
    }

    private void setAllCorrect(boolean withBlur) {
        selenium.type(notEmptyInput, CORRECT_NOT_EMPTY);
        if (withBlur) { selenium.fireEvent(notEmptyInput, Event.BLUR); }

        selenium.type(regExpPatternInput, CORRECT_REG_EXP);
        if (withBlur) { selenium.fireEvent(regExpPatternInput, Event.BLUR); }

        selenium.type(stringSizeInput, CORRECT_STRING_SIZE);
        if (withBlur) { selenium.fireEvent(stringSizeInput, Event.BLUR); }

        selenium.type(customStringInput, CORRECT_CUSTOM_STRING);
        if (withBlur) { selenium.fireEvent(customStringInput, Event.BLUR); }
    }

}
