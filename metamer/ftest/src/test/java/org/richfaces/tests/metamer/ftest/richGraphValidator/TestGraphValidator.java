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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;

import java.net.URL;

import org.richfaces.tests.metamer.bean.abstractions.StringInputValidationBean;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test for page /faces/components/richGraphValidator/all.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestGraphValidator extends AbstractGraphValidatorTest {

    private Group group;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richGraphValidator/all.xhtml");
    }

    @Test
    @UseWithField(field = "group", valuesFrom = FROM_ENUM, value = "")
    public void testGroups() {
        graphValidatorAttributes.set(GraphValidatorAttributes.groups, group.toString());
        Assert.assertFalse(graphValidatorGlobalMessages.advanced().isVisible(),
            "Global messages should not be visible.");
        Assert.assertFalse(graphValidatorMessages.advanced().isVisible(), "Messages should be visible.");
        applyChanges();

        if (group == Group.DEFAULT || group == Group.NULL) {
            // a message for empty input secret should be displayed, it is validated by @NotNull
            Assert.assertTrue(inputSecretMsg.advanced().isVisible(), "Messages for input secret should be visible.");
            String summary = inputSecretMsg.getDetail();
            if (!summary.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG)
                && summary.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG2)
                && summary.equals(StringInputValidationBean.NOT_EMPTY_VALIDATION_MSG3)) {
                throw new AssertionError("The message summary is not equal to some of expected values.");
            }
        }
        setInputSecretCorrect();// all inputs are correct now, not submitted yet

        // set some of group inputs to bad value
        setWrongSettingForGroup(group);
        // graph validator message should be displayed, validation of group should fail
        Assert.assertFalse(graphValidatorGlobalMessages.advanced().isVisible(),
            "Global messages should not be visible.");
        Assert.assertTrue(graphValidatorMessages.advanced().isVisible(), "Graph validator messages should be visible.");
        Assert.assertEquals(graphValidatorMessages.size(), 1, "There should be one message.");
        Assert.assertEquals(graphValidatorMessages.getItem(0).getSummary(), getMessageForGroup(group),
            "Summary of message.");
        setCorrectSettingForGroup(group);
        checkGraphValidatorSuccessMessage();

        // check that groups-only inputs are validated and are not influenced by other inputs (but they must also pass
        // bean validation)
        switch (group) {
            case DEFAULT:
            case NULL:
            case ValidationGroupAllComponents:
                // no testing, this groups covers all inputs, cannot set value of non-group input
                return;
            case ValidationGroupBooleanInputs:
                // this should not influence the validation of this group
                setWrongSettingForGroup(Group.ValidationGroupNumericInputs);
                setWrongSettingForGroup(Group.ValidationGroupAllComponents);// no number input components in setting
                break;
            case ValidationGroupNumericInputs:
                // this should not influence the validation of this group
                setWrongSettingForGroup(Group.ValidationGroupBooleanInputs);
                setWrongSettingForGroup(Group.ValidationGroupAllComponents);// no number input components in setting
                break;
            default:
                throw new UnsupportedOperationException("Unknown group " + group);
        }
        checkGraphValidatorSuccessMessage();
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        assertPresent(panel, "Panel should be present.");
        graphValidatorAttributes.set(GraphValidatorAttributes.rendered, Boolean.FALSE);
        assertNotPresent(panel, "Panel should not be present.");
    }
}
