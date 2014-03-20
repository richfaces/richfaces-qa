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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.richInputNumberSpinner.AbstractInputNumberSpinnerTest.DEFAULT_MAX_VALUE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSpinner/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
@RegressionTest("https://issues.jboss.org/browse/RF-12365")
public class TestInputNumberSpinnerAttributes extends AbstractInputNumberSpinnerTest {

    private final Attributes<InputNumberSpinnerAttributes> inputNumberSpinnerAttributes = getAttributes();

    @FindBy(css = "span.rf-insp-inc-dis")
    private WebElement disabledIncreaseBtn;
    @FindBy(css = "span.rf-insp-dec-dis")
    private WebElement disabledDecreaseBtn;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSpinner/simple.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11315")
    @Templates(value = "plain")
    public void testAccesskey() {
        testHTMLAttribute(spinner.advanced().getInput().advanced().getInputElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.accesskey, "x");
    }

    @Test(groups = "smoke")
    public void testCycled() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.cycled, Boolean.TRUE);
        typeToInput(DEFAULT_MAX_VALUE);
        // test that value change to min value (10 -> -10)
        increase(1);
        assertEquals(getOutputText(), DEFAULT_MIN_VALUE, "Output was not updated.");
        assertEquals(getInputText(), DEFAULT_MIN_VALUE, "Input was not updated.");
        // test that value change to max value (-10 -> 10)
        decrease(1);
        assertEquals(getOutputText(), DEFAULT_MAX_VALUE, "Output was not updated.");
        assertEquals(getInputText(), DEFAULT_MAX_VALUE, "Input was not updated.");
    }

    @Test
    public void testDecrease() {
        super.testDecrease();
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        testDir(spinner.advanced().getRootElement());
    }

    @Test
    public void testDisabled() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.disabled, Boolean.TRUE);
        String attribute = spinner.advanced().getInput().advanced().getInputElement().getAttribute("disabled");
        assertTrue(attribute.equals("disabled") || attribute.equals("true"), "Input should be disabled");
        assertNotPresent(spinner.advanced().getArrowDecreaseElement(), "Decrease button should not be present on page");
        assertNotPresent(spinner.advanced().getArrowIncreaseElement(), "Increase button should not be present on page");
        assertPresent(disabledDecreaseBtn, "Disabled decrease button should be present on page");
        assertPresent(disabledIncreaseBtn, "Disabled increase button should be present on page");
    }

    @Test
    public void testEnableManualInput() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.enableManualInput, Boolean.FALSE);
        String attribute = spinner.advanced().getInput().advanced().getInputElement().getAttribute("readonly");
        assertTrue(attribute.equals("readonly") || attribute.equals("true"), "Input should be readonly");
        assertPresent(spinner.advanced().getArrowDecreaseElement(), "Decrease button should be present on page");
        assertPresent(spinner.advanced().getArrowIncreaseElement(), "Increase button should be present on page");
    }

    @Test
    public void testImmediate() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.immediate, Boolean.TRUE);
        String testedValue = "4";
        String listenerMsg = "value changed: " + DEFAULT_VALUE + " -> " + testedValue;
        typeToInput(testedValue);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, listenerMsg);
    }

    @Test(groups = "smoke")
    public void testIncrease() {
        super.testIncrease();
    }

    @Test
    public void testInit() {
        assertVisible(spinner.advanced().getRootElement(), "Spinner is not present on the page.");
        assertVisible(spinner.advanced().getInput().advanced().getInputElement(), "Spinner's input is not visible.");
        assertVisible(spinner.advanced().getArrowDecreaseElement(), "Spinner's decrease button is not visible.");
        assertVisible(spinner.advanced().getArrowIncreaseElement(), "Spinner's increase button is not visible.");
    }

    @Test
    @Templates(value = "plain")
    public void testInputClass() {
        testStyleClass(spinner.advanced().getInput().advanced().getInputElement(), BasicAttributes.inputClass);
    }

    @Test
    @Templates(value = "plain")
    public void testInputSize() {
        String testedValue = "3";
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.inputSize, testedValue);
        assertEquals(spinner.advanced().getInput().advanced().getInputElement().getAttribute("size"), testedValue,
            "Input's size attribute.");
        testedValue = "40";
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.inputSize, testedValue);
        assertEquals(spinner.advanced().getInput().advanced().getInputElement().getAttribute("size"), testedValue,
            "Input's size attribute.");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testAttributeLang(spinner.advanced().getRootElement());
    }

    @Test
    public void testMaxValueClick() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.maxValue, 13);
        increase(9);
        assertEquals(getInputText(), "11", "Input was not updated.");// 2+9
        assertEquals(getOutputText(), "11", "Output was not updated.");// 2+9
        increase(2);
        assertEquals(getInputText(), "13", "Input was not updated.");// 2+9+2
        assertEquals(getOutputText(), "13", "Output was not updated.");// 2+9+2
        increase(2);
        assertEquals(getInputText(), "13", "Input was not updated.");// 2+9+2+2, but maximum is 13
        assertEquals(getOutputText(), "13", "Output was not updated.");// 2+9+2+2, but maximum is 13
    }

    @Test
    public void testMaxValueType() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.maxValue, 13);
        typeToInput("11");
        assertEquals(getInputText(), "11", "Input was not updated.");
        assertEquals(getOutputText(), "11", "Output was not updated.");
        typeToInput("13");
        assertEquals(getInputText(), "13", "Input was not updated.");
        assertEquals(getOutputText(), "13", "Output was not updated.");
        typeToInput("15", WaitRequestType.NONE);
        assertEquals(getInputText(), "13", "Input should stay same as before.");// max is 13
        assertEquals(getOutputText(), "13", "Output should stay same as before.");// max is 13
    }

    @Test(groups = "smoke")
    public void testMinValueClick() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.minValue, -13);
        decrease(9);
        assertEquals(getInputText(), "-7", "Input was not updated.");// 2-9
        assertEquals(getOutputText(), "-7", "Output was not updated.");// 2-9
        decrease(6);
        assertEquals(getInputText(), "-13", "Input was not updated.");// 2-9-6
        assertEquals(getOutputText(), "-13", "Output was not updated.");// 2-9-6
        decrease(2);
        assertEquals(getInputText(), "-13", "Input was not updated.");// 2-9-6-2, but minimum is -13
        assertEquals(getOutputText(), "-13", "Output was not updated.");// 2-9-6-2, but minimum is -13
    }

    @Test
    public void testMinValueType() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.minValue, -13);
        typeToInput("-11");
        assertEquals(getInputText(), "-11", "Input was not updated.");
        assertEquals(getOutputText(), "-11", "Output was not updated.");
        typeToInput("-13");
        assertEquals(getInputText(), "-13", "Input was not updated.");
        assertEquals(getOutputText(), "-13", "Output was not updated.");
        typeToInput("-15", WaitRequestType.NONE);
        assertEquals(getInputText(), "-13", "Intput should stay same as before.");// min is -13
        assertEquals(getOutputText(), "-13", "Output should stay same as before.");// min is -13
    }

    @Test
    @Templates(value = "plain")
    public void testOnblur() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onblur,
            new Actions(driver).click(spinner.advanced().getInput().advanced().getInputElement()).click(getMetamerPage().getRequestTimeElement())
            .build());
    }

    @Test
    public void testOnchangeClick() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onchange, new Action() {
            @Override
            public void perform() {
                increase(1);
            }
        });
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onchange, new Action() {
            @Override
            public void perform() {
                decrease(1);
            }
        });
    }

    @Test
    public void testOnchangeType() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onchange, new Action() {
            @Override
            public void perform() {
                typeToInput("10");
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onclick,
            new Actions(driver).click(spinner.advanced().getRootElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.ondblclick,
            new Actions(driver).doubleClick(spinner.advanced().getRootElement()).build());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10581")
    public void testOndownclick() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.ondownclick, new Action() {
            @Override
            public void perform() {
                decrease(1);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onfocus,
            new Actions(driver).click(spinner.advanced().getInput().advanced().getInputElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputclick,
            new Actions(driver).click(spinner.advanced().getInput().advanced().getInputElement()).build());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9568")
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputdblclick, new Actions(driver)
            .doubleClick(spinner.advanced().getInput().advanced().getInputElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.KEYDOWN, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.KEYPRESS, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.KEYUP, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputkeyup);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.MOUSEDOWN, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputmousedown);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousemove() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.MOUSEMOVE, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputmousemove);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.MOUSEOUT, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.MOUSEOVER, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputmouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), Event.MOUSEUP, inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.oninputmouseup);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onkeyup);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onmousedown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onmousemove);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onmouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(spinner.advanced().getRootElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onmouseup);
    }

    @Test
    @Templates(value = "plain")
    public void testOnselect() {
        testFireEventWithJS(spinner.advanced().getInput().advanced().getInputElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.onselect);
    }

    @Test
    public void testOnupclick() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onupclick, new Action() {
            @Override
            public void perform() {
                increase(1);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.rendered, Boolean.FALSE);
        assertNotPresent(spinner.advanced().getRootElement(), "Spinner should not be rendered when rendered=false.");
    }

    @Test
    public void testStep() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.step, 5);
        increase(1);
        assertEquals(getOutputText(), "7", "Wrong output");
        increase(1);
        assertEquals(getOutputText(), "10", "Wrong output");
        decrease(3);
        assertEquals(getOutputText(), "-5", "Wrong output");
        decrease(1);
        assertEquals(getOutputText(), "-10", "Wrong output");
        decrease(1);
        assertEquals(getOutputText(), "-10", "Wrong output");
        increase(1);
        assertEquals(getOutputText(), "-5", "Wrong output");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(spinner.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(spinner.advanced().getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "plain")
    public void testTabindex() {
        testHTMLAttribute(spinner.advanced().getInput().advanced().getInputElement(), inputNumberSpinnerAttributes,
            InputNumberSpinnerAttributes.tabindex, "57");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(spinner.advanced().getRootElement());
    }

    @Test
    @UseWithField(field = "number", valuesFrom = FROM_FIELD, value = "bigNumbers")
    public void testTypeIntoInputBig() {
        super.testTypeIntoInputBig();
    }

    @Test
    @UseWithField(field = "number", valuesFrom = FROM_FIELD, value = "correctNumbers")
    public void testTypeIntoInputCorrect() {
        super.testTypeIntoInputCorrect();
    }

    @Test
    @UseWithField(field = "number", valuesFrom = FROM_FIELD, value = "decimalNumbers")
    public void testTypeIntoInputDecimal() {
        super.testTypeIntoInputDecimal();
    }

    @Test
    public void testTypeIntoInputNotNumber() {
        super.testTypeIntoInputNotNumber();
    }

    @Test
    @UseWithField(field = "number", valuesFrom = FROM_FIELD, value = "smallNumbers")
    public void testTypeIntoInputSmall() {
        super.testTypeIntoInputSmall();
    }

    @Test
    @UseWithField(field = "number", valuesFrom = FROM_FIELD, value = "bigNumbers")
    public void testValueBig() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.value, number);

        assertEquals(getOutputText(), number, "Output was not updated.");
        assertEquals(getInputText(), "10", "Input was not updated.");
    }

    @Test
    @UseWithField(field = "number", valuesFrom = FROM_FIELD, value = "correctNumbers")
    public void testValueCorrect() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.value, number);
        assertEquals(getOutputText(), number, "Output was not updated.");
    }

    @Test
    @UseWithField(field = "number", valuesFrom = FROM_FIELD, value = "smallNumbers")
    public void testValueSmall() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.value, number);

        assertEquals(getOutputText(), number, "Output was not updated.");
        assertEquals(getInputText(), "-10", "Input was not updated.");
    }

    private void typeToInput(String value, WaitRequestType type) {
        MetamerPage.waitRequest(spinner.advanced().getInput().advanced().clear(ClearType.JS).sendKeys(value).advanced(), type)
            .trigger("blur");
    }

    private void typeToInput(String value) {
        typeToInput(value, WaitRequestType.XHR);
    }
}
