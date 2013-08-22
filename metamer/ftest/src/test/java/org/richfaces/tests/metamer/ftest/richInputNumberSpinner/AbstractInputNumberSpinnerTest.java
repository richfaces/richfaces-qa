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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inputNumberSpinnerAttributes;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.inputNumberSpinner.RichFacesInputNumberSpinner;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
public abstract class AbstractInputNumberSpinnerTest extends AbstractWebDriverTest {

    @Page
    MetamerPage page;
    @FindBy(css = "span[id$=output]")
    WebElement output;
    @FindBy(css = "span[id$=spinner]")
    protected RichFacesInputNumberSpinner spinner;
    //
    protected static final String DEFAULT_MAX_VALUE = "10";
    protected static final String DEFAULT_MIN_VALUE = "-10";
    protected static final String DEFAULT_VALUE = "2";
    protected static final int DEFAULT_VALUE_INT = 2;
    //
    protected String[] correctNumbers = { "-10", "-5", "-1", "0", "1", "5", "10" };
    protected String[] smallNumbers = { "-11", "-15", "-100" };
    protected String[] bigNumbers = { "11", "15", "100" };
    protected String[] decimalNumbers = { "1.4999", "5.6", "7.0001", "-5.50001", "-9.9", "1.222e0", "0e0", "-5.50001e0" };
    @Inject
    @Use(empty = true)
    protected String number;

    protected void decrease(int count) {
        Double minValue = Double.parseDouble(inputNumberSpinnerAttributes
                .get(InputNumberSpinnerAttributes.minValue));
        Boolean cycled = Boolean.parseBoolean(inputNumberSpinnerAttributes
                .get(InputNumberSpinnerAttributes.cycled));
        Double actValue;
        WaitRequestType type;
        for (int i = 0; i < count; i++) {
            actValue = Double.parseDouble(spinner.advanced().getInput().getStringValue());
            type = (actValue > minValue || cycled ? WaitRequestType.XHR : WaitRequestType.NONE);
            MetamerPage.waitRequest(spinner, type).decrease();
        }
    }

    /**
     * Getter for value that is displayed in spinner input.
     * @return spinner input value
     */
    protected String getInputText() {
        return spinner.advanced().getInput().getStringValue();
    }

    protected String getOutputText() {
        return output.getText();
    }

    protected void increase(int count) {
        Double maxValue = Double.parseDouble(inputNumberSpinnerAttributes
                .get(InputNumberSpinnerAttributes.maxValue));
        Boolean cycled = Boolean.parseBoolean(inputNumberSpinnerAttributes
                .get(InputNumberSpinnerAttributes.cycled));
        Double actValue;
        WaitRequestType type;
        for (int i = 0; i < count; i++) {
            actValue = Double.parseDouble(spinner.advanced().getInput().getStringValue());
            type = (actValue < maxValue || cycled ? WaitRequestType.XHR : WaitRequestType.NONE);
            MetamerPage.waitRequest(spinner, type).increase();
        }
    }

    protected void testDecrease() {
        int clicks = 5;
        increase(clicks);
        String expected = String.valueOf(DEFAULT_VALUE_INT + clicks);
        assertEquals(getOutputText(), expected, "Input was not updated.");
        assertEquals(getInputText(), expected, "Output was not updated.");

        increase(clicks);// >>> 12, but 10 is maximum
        expected = DEFAULT_MAX_VALUE;
        assertEquals(getOutputText(), expected, "Input was not updated.");
        assertEquals(getInputText(), expected, "Output was not updated.");
    }

    protected void testIncrease() {
        int clicks = 7;
        decrease(clicks);
        String expected = String.valueOf(DEFAULT_VALUE_INT - clicks);
        assertEquals(getOutputText(), expected, "Input was not updated.");
        assertEquals(getInputText(), expected, "Output was not updated.");

        decrease(clicks);// >>> -12, but -10 is maximum
        expected = DEFAULT_MIN_VALUE;
        assertEquals(getOutputText(), expected, "Input was not updated.");
        assertEquals(getInputText(), expected, "Output was not updated.");
    }

    protected void testTypeIntoInputBig() {
        typeToInput();

        assertEquals(getOutputText(), DEFAULT_MAX_VALUE, "Output was not updated.");
        assertEquals(getInputText(), DEFAULT_MAX_VALUE, "Input was not updated.");
    }

    protected void testTypeIntoInputCorrect() {
        typeToInput();

        assertEquals(getOutputText(), number, "Output was not updated.");
        assertEquals(getInputText(), number, "Input was not updated.");
    }

    protected void testTypeIntoInputDecimal() {
        typeToInput();

        Double newNumber = new Double(number);

        assertEquals(getOutputText(), newNumber == 0 ? "0" : newNumber.toString(), "Output was not updated.");
        assertEquals(getInputText(), newNumber == 0 ? "0" : newNumber.toString(), "Input was not updated.");
    }

    protected void testTypeIntoInputNotNumber() {
        number = "RichFaces";
        typeToInput(WaitRequestType.NONE);

        assertEquals(getOutputText(), DEFAULT_VALUE, "Output should not be updated.");
        assertEquals(getInputText(), DEFAULT_VALUE, "Input should not be updated.");
    }

    protected void testTypeIntoInputSmall() {
        typeToInput();

        assertEquals(getOutputText(), DEFAULT_MIN_VALUE, "Output was not updated.");
        assertEquals(getInputText(), DEFAULT_MIN_VALUE, "Input was not updated.");
    }

    private void typeToInput() {
        typeToInput(WaitRequestType.XHR);
    }

    private void typeToInput(WaitRequestType type) {
        MetamerPage.waitRequest(spinner.advanced().getInput().advanced().clear(ClearType.JS).sendKeys(number).advanced(), type).trigger("blur");
    }
}
