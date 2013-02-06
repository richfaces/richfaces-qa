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

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.validatorAttributes;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richValidator/wrapping.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestWrappingValidator extends AbstractValidatorsTest {

    /** component page locator */
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richValidator/wrapping.xhtml");
    }

    @Test
    public void testAllWrong() {
        verifyAllWrongWithAjaxSubmit();
    }

    @Test(groups = { "4.Future" })
    @IssueTracking(value = "https://issues.jboss.org/browse/RF-11035")
    public void testAllWrongJSF() {
        verifyAllWrongWithJSFSubmit();
    }

    @Test
    public void testBooleanTrue() {
        verifyBooleanTrue();
    }

    @Test
    public void testBooleanFalse() {
        verifyBooleanFalse();
    }

    @Test
    public void testDecimalMinMax() {
        verifyDecimalMinMax();
    }

    @Test
    public void testIntegerMax() {
        verifyMax();
    }

    @Test
    public void testIntegerMin() {
        verifyMin();
    }

    @Test
    public void testIntegerMinMax() {
        verifyMinMax();
    }

    @Test
    public void testTextNotEmpty() {
        verifyNotEmpty();
    }

    @Test
    public void testTextNotNull() {
        verifyNotNull();
    }

    @Test
    public void testTextPattern() {
        verifyPattern();
    }

    @Test
    public void testTextCustomPattern() {
        verifyCustom();
    }

    @Test
    public void testDatePast() {
        verifyDatePast();
    }

    @Test
    public void testDateFuture() {
        verifyDateFuture();
    }

    @Test
    public void testStringSize() {
        verifyStringSize();
    }

    /**
     * Test of attribute @disabled. Testing of client side validation on one input field.
     */
    @Test
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-12154")
    public void testDisabled() {
        JQueryLocator integerMax10Field = pjq("input[type=text][id$=max]");
        JQueryLocator errorMsgLocator = pjq("span[id$=max] > span[class=rf-msg-dtl]");
        String invalidValue = "11";
        //set disabled to false
        validatorAttributes.set(ValidatorAttributes.disabled, Boolean.FALSE);
        //put in an invalid value
        guardNoRequest(selenium).type(integerMax10Field, invalidValue);
        //check error message from validator
        assertTrue(selenium.isElementPresent(errorMsgLocator), "No error message from validator.");

        //set disabled to true
        validatorAttributes.set(ValidatorAttributes.disabled, Boolean.TRUE);
        //put in an invalid value
        guardNoRequest(selenium).type(integerMax10Field, invalidValue);
        //check error message from validator
        assertFalse(selenium.isElementPresent(errorMsgLocator), "No error message should be here. Validator is disabled and still works.");
    }

    @Test(groups = { "4.Future" })
    @IssueTracking(value = "https://issues.jboss.org/browse/RF-11035")
    public void testSelectionSize() {
        verifySelectionSize();
    }

}
