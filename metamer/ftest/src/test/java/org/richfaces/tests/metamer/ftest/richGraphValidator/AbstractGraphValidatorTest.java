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
package org.richfaces.tests.metamer.ftest.richGraphValidator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.tests.metamer.bean.rich.RichGraphValidatorBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * Test for page /faces/components/richGraphValidator/all.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractGraphValidatorTest extends AbstractWebDriverTest {

    private static final String CORRECT_INT_VALUE = "10";
    private static final String CORRECT_STRING_VALUE = ":-)";
    private static final String WRONG_INT_VALUE = "11";
    private static final String WRONG_STRING_VALUE = ":-(";

    protected final Attributes<GraphValidatorAttributes> graphValidatorAttributes = getAttributes();

    private Group group;

    @Page
    private GraphValidatorPage page;

    protected void applyChanges() {
        Graphene.guardAjax(page.getApplyChangesBtn()).click();
    }

    protected void checkGraphValidatorSuccessMessage() {
        //now all inputs are correct
        //there should be graph validator successfull message, which is not bound to any input > will be global
        assertFalse(page.getGraphValidatorMessages().advanced().isVisible(), "Graph validator messages should not be visible.");
        RichFacesMessages gMsgs = page.getGraphValidatorGlobalMessages();
        assertTrue(gMsgs.advanced().isVisible(), "Global messages should be visible.");
        assertEquals(gMsgs.size(), 1, "There should be one message.");
        assertEquals(gMsgs.getItem(0).getSummary(), RichGraphValidatorBean.SUCCESSFULL_ACTION_MSG, "Summary of message.");
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

    public GraphValidatorPage getPage() {
        return page;
    }

    protected void setCorrectSettingForGroup(Group g) {
        switch (g) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                page.getAutocomplete().advanced().clear(ClearType.JS).sendKeys(CORRECT_STRING_VALUE);
                page.getInputText().advanced().clear(ClearType.JS).sendKeys(CORRECT_STRING_VALUE);
                page.getInputTextarea().advanced().clear(ClearType.JS).sendKeys(CORRECT_STRING_VALUE);
                break;
            case ValidationGroupBooleanInputs:
                page.getBooleanCheckbox().check();
                break;
            case ValidationGroupNumericInputs:
                page.getSpinner().advanced().getInput().advanced().clear(ClearType.JS).sendKeys(CORRECT_INT_VALUE);
                page.getSlider().advanced().getInput().advanced().clear(ClearType.JS).sendKeys(CORRECT_INT_VALUE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        applyChanges();
    }

    protected void setInputSecretCorrect() {
        // only inputSecret doesn't keep entered value after submit
        page.getInputSecret().advanced().clear(ClearType.JS).sendKeys(RichGraphValidatorBean.SMILE);
    }

    protected void setWrongSettingForGroup(Group g) {
        switch (g) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                page.getAutocomplete().advanced().clear(ClearType.JS).sendKeys(WRONG_STRING_VALUE);
                page.getInputText().advanced().clear(ClearType.JS).sendKeys(WRONG_STRING_VALUE);
                page.getInputTextarea().advanced().clear(ClearType.JS).sendKeys(WRONG_STRING_VALUE);
                break;
            case ValidationGroupBooleanInputs:
                page.getBooleanCheckbox().uncheck();
                break;
            case ValidationGroupNumericInputs:
                page.getSpinner().advanced().getInput().advanced().clear(ClearType.JS).sendKeys(WRONG_INT_VALUE);
                page.getSlider().advanced().getInput().advanced().clear(ClearType.JS).sendKeys(WRONG_INT_VALUE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        applyChanges();
    }

    protected enum Group {

        DEFAULT("Default"), NULL("null"), ValidationGroupAllComponents, ValidationGroupBooleanInputs, ValidationGroupNumericInputs;

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
}
