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

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13127 extends AbstractWebDriverTest {

    @FindBy(css = "input[id$='mileage']")
    private List<WebElement> mileageInputs;
    @FindBy(css = "input[id$='vin']")
    private List<WebElement> vinInputs;

    private void blurCurrentInput(boolean willSendAjax) {
        blur(willSendAjax ? WaitRequestType.XHR : WaitRequestType.NONE);
    }

    private void checkInputsValidation(List<WebElement> inputs, boolean sendsAjaxOnBlur) {
        Set<Integer> unchangedIndexes = Sets.newHashSet(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Set<Integer> invalidIndexes = Sets.newHashSet();
        int[] testedIndexes = new int[] { 5, 9, 1 };
        WebElement element;

        for (int index : testedIndexes) {
            element = inputs.get(index);
            element.sendKeys("a");
            blurCurrentInput(sendsAjaxOnBlur);
            unchangedIndexes.remove(index);
            invalidIndexes.add(index);

            for (int i : invalidIndexes) {
                element = inputs.get(i);
                assertTrue(isChanged(element), format("Input at index <{0}> should be changed.", i));
                assertTrue(isInvalid(element), format("Input at index <{0}> should be invalid.", i));
            }
            for (int i : unchangedIndexes) {
                element = inputs.get(i);
                assertFalse(isChanged(element), format("Input at index <{0}> should not be changed.", i));
                assertFalse(isInvalid(element), format("Input at index <{0}> should not be invalid.", i));
                assertFalse(isValid(element), format("Input at index <{0}> should not be valid.", i));
            }
        }
        Set<Integer> validIndexes = Sets.newHashSet();
        for (int index : testedIndexes) {
            element = inputs.get(index);
            element.sendKeys(Keys.BACK_SPACE);
            blurCurrentInput(sendsAjaxOnBlur);
            validIndexes.add(index);
            invalidIndexes.remove(index);

            for (int i : invalidIndexes) {
                element = inputs.get(i);
                assertTrue(isChanged(element), format("Input at index <{0}> should be changed.", i));
                assertTrue(isInvalid(element), format("Input at index <{0}> should be invalid.", i));
            }
            for (int i : unchangedIndexes) {
                element = inputs.get(i);
                assertFalse(isChanged(element), format("Input at index <{0}> should not be changed.", i));
                assertFalse(isInvalid(element), format("Input at index <{0}> should not be invalid.", i));
                assertFalse(isValid(element), format("Input at index <{0}> should not be valid.", i));
            }
            for (int i : validIndexes) {
                element = inputs.get(i);
                assertTrue(isChanged(element), format("Input at index <{0}> should not be changed.", i));
                assertTrue(isValid(element), format("Input at index <{0}> should be valid.", i));
            }
        }
    }

    @Override
    public String getComponentTestPagePath() {
        return "richValidator/rf-13127.xhtml";
    }

    private boolean isChanged(WebElement input) {
        return input.getAttribute("class").contains("changed");
    }

    private boolean isInvalid(WebElement input) {
        return input.getAttribute("class").contains("invalid");
    }

    private boolean isValid(WebElement input) {
        String klass = input.getAttribute("class");
        return !klass.contains("invalid") && klass.contains("valid");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13127")
    public void testCorrectElementIsUsed_clientSideValidatedInputs() {
        checkInputsValidation(vinInputs, false);
    }

    @Test
    public void testCorrectElementIsUsed_serverSideValidatedInputs() {
        checkInputsValidation(mileageInputs, true);
    }
}
