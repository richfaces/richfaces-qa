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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inputNumberSpinnerAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSpinner/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
@RegressionTest("https://issues.jboss.org/browse/RF-12365")
public class TestInputNumberSpinnerAttributes extends AbstractInputNumberSpinnerTest {

    @FindBy(css = "span.rf-insp-inc-dis")
    WebElement disabledIncreaseBtn;
    @FindBy(css = "span.rf-insp-dec-dis")
    WebElement disabledDecreaseBtn;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSpinner/simple.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11315")
    @Templates(value = "plain")
    public void testAccesskey() {
        testHTMLAttribute(spinner.getInput().advanced().getInput(), inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.accesskey, "x");
    }

    @Test
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
        testDir(spinner.getRootElement());
    }

    @Test
    public void testDisabled() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.disabled, Boolean.TRUE);
        String attribute = spinner.getInput().advanced().getInput().getAttribute("disabled");
        assertTrue(attribute.equals("disabled") || attribute.equals("true"), "Input should be disabled");
        assertNotPresent(spinner.getButtonDecreaseElement(), "Decrease button should not be present on page");
        assertNotPresent(spinner.getButtonIncreaseElement(), "Increase button should not be present on page");
        assertPresent(disabledDecreaseBtn, "Disabled decrease button should be present on page");
        assertPresent(disabledIncreaseBtn, "Disabled increase button should be present on page");
    }

    @Test
    public void testEnableManualInput() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.enableManualInput, Boolean.FALSE);
        String attribute = spinner.getInput().advanced().getInput().getAttribute("readonly");
        assertTrue(attribute.equals("readonly") || attribute.equals("true"), "Input should be readonly");
        assertPresent(spinner.getButtonDecreaseElement(), "Decrease button should be present on page");
        assertPresent(spinner.getButtonIncreaseElement(), "Increase button should be present on page");
    }

    @Test
    public void testImmediate() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.immediate, Boolean.TRUE);
        String testedValue = "4";
        String listenerMsg = "value changed: " + DEFAULT_VALUE + " -> " + testedValue;
        typeToInput(testedValue);
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, listenerMsg);
    }

    @Test
    public void testIncrease() {
        super.testIncrease();
    }

    @Test
    public void testInit() {
        assertVisible(spinner.getRootElement(), "Spinner is not present on the page.");
        assertVisible(spinner.getInput().advanced().getInput(), "Spinner's input is not visible.");
        assertVisible(spinner.getButtonDecreaseElement(), "Spinner's decrease button is not visible.");
        assertVisible(spinner.getButtonIncreaseElement(), "Spinner's increase button is not visible.");
    }

    @Test
    @Templates(value = "plain")
    public void testInputClass() {
        testStyleClass(spinner.getInput().advanced().getInput(), BasicAttributes.inputClass);
    }

    @Test
    @Templates(value = "plain")
    public void testInputSize() {
        String testedValue = "3";
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.inputSize, testedValue);
        assertEquals(spinner.getInput().advanced().getInput().getAttribute("size"), testedValue, "Input's size attribute.");
        testedValue = "40";
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.inputSize, testedValue);
        assertEquals(spinner.getInput().advanced().getInput().getAttribute("size"), testedValue, "Input's size attribute.");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testAttributeLang(spinner.getRootElement());
    }

    @Test
    public void testMaxValueClick() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.maxValue, 13);
        increase(9);
        assertEquals(getInputText(), "11", "Input was not updated.");//2+9
        assertEquals(getOutputText(), "11", "Output was not updated.");//2+9
        increase(2);
        assertEquals(getInputText(), "13", "Input was not updated.");//2+9+2
        assertEquals(getOutputText(), "13", "Output was not updated.");//2+9+2
        increase(2);
        assertEquals(getInputText(), "13", "Input was not updated.");//2+9+2+2, but maximum is 13
        assertEquals(getOutputText(), "13", "Output was not updated.");//2+9+2+2, but maximum is 13
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
        assertEquals(getInputText(), "13", "Input should stay same as before.");//max is 13
        assertEquals(getOutputText(), "13", "Output should stay same as before.");//max is 13
    }

    @Test
    public void testMinValueClick() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.minValue, -13);
        decrease(9);
        assertEquals(getInputText(), "-7", "Input was not updated.");//2-9
        assertEquals(getOutputText(), "-7", "Output was not updated.");//2-9
        decrease(6);
        assertEquals(getInputText(), "-13", "Input was not updated.");//2-9-6
        assertEquals(getOutputText(), "-13", "Output was not updated.");//2-9-6
        decrease(2);
        assertEquals(getInputText(), "-13", "Input was not updated.");//2-9-6-2, but minimum is -13
        assertEquals(getOutputText(), "-13", "Output was not updated.");//2-9-6-2, but minimum is -13
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
        assertEquals(getInputText(), "-13", "Intput should stay same as before.");//min is -13
        assertEquals(getOutputText(), "-13", "Output should stay same as before.");//min is -13
    }

    @Test
    @Templates(value = "plain")
    public void testOnblur() {
        testFireEvent(inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onblur,
                new Actions(driver).click(spinner.getInput().advanced().getInput()).click(page.getRequestTimeElement()).build());
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
        testFireEvent(inputNumberSpinnerAttributes,
                InputNumberSpinnerAttributes.onchange, new Action() {
            @Override
            public void perform() {
                typeToInput("10");
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(inputNumberSpinnerAttributes,
                InputNumberSpinnerAttributes.onclick,
                new Actions(driver).click(spinner.getRootElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(inputNumberSpinnerAttributes,
                InputNumberSpinnerAttributes.ondblclick,
                new Actions(driver).doubleClick(spinner.getRootElement()).build());
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
                new Actions(driver).click(spinner.getInput().advanced().getInput()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEvent(inputNumberSpinnerAttributes,
                InputNumberSpinnerAttributes.oninputclick,
                new Actions(driver).click(spinner.getInput().advanced().getInput()).build());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9568")
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEvent(inputNumberSpinnerAttributes,
                InputNumberSpinnerAttributes.oninputdblclick,
                new Actions(driver).doubleClick(spinner.getInput().advanced().getInput()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.KEYDOWN,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.KEYPRESS,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.KEYUP,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputkeyup);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.MOUSEDOWN,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputmousedown);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousemove() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.MOUSEMOVE,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputmousemove);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.MOUSEOUT,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.MOUSEOVER,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputmouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(), Event.MOUSEUP,
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.oninputmouseup);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onkeyup);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onmousedown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onmousemove);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onmouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(spinner.getRootElement(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onmouseup);
    }

    @Test
    @Templates(value = "plain")
    public void testOnselect() {
        testFireEventWithJS(spinner.getInput().advanced().getInput(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.onselect);
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
        assertNotPresent(spinner.getRootElement(), "Spinner should not be rendered when rendered=false.");
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
        testStyle(spinner.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(spinner.getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "plain")
    public void testTabindex() {
        testHTMLAttribute(spinner.getInput().advanced().getInput(),
                inputNumberSpinnerAttributes, InputNumberSpinnerAttributes.tabindex, "57");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(spinner.getRootElement());
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    public void testTypeIntoInputBig() {
        super.testTypeIntoInputBig();
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    public void testTypeIntoInputCorrect() {
        super.testTypeIntoInputCorrect();
    }

    @Test
    @Use(field = "number", value = "decimalNumbers")
    public void testTypeIntoInputDecimal() {
        super.testTypeIntoInputDecimal();
    }

    @Test
    public void testTypeIntoInputNotNumber() {
        super.testTypeIntoInputNotNumber();
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    public void testTypeIntoInputSmall() {
        super.testTypeIntoInputSmall();
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    public void testValueBig() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.value, number);

        assertEquals(getOutputText(), number, "Output was not updated.");
        assertEquals(getInputText(), "10", "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    public void testValueCorrect() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.value, number);
        assertEquals(getOutputText(), number, "Output was not updated.");
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    public void testValueSmall() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.value, number);

        assertEquals(getOutputText(), number, "Output was not updated.");
        assertEquals(getInputText(), "-10", "Input was not updated.");
    }

    private void typeToInput(String value, WaitRequestType type) {
        MetamerPage.waitRequest(spinner.getInput().advanced().clear(ClearType.JS).sendKeys(value), type).advanced().trigger("blur");
    }

    private void typeToInput(String value) {
        typeToInput(value, WaitRequestType.XHR);
    }
}
