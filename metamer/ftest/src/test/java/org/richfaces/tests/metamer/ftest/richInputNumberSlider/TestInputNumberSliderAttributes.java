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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSlider/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSliderAttributes extends AbstractSliderTest {

    private final Attributes<InputNumberSliderAttributes> inputNumberSliderAttributes = getAttributes();

    @FindBy(css = "span.rf-insl > br")
    private WebElement br;

    @Inject
    @Use(empty = false)
    private Position position;

    public enum Position {

        top("top"),
        bottom("bottom"),
        right("right"),
        left("left"),
        NULL("null");
        final String value;

        private Position(String value) {
            this.value = value;
        }
    }

    private final Action delayTestAction = new Action() {
        private String timeBefore = "";

        @Override
        public void perform() {
            Graphene.waitModel().until().element(getMetamerPage().getRequestTimeElement()).text().not().equalTo(timeBefore);
            timeBefore = getMetamerPage().getRequestTimeElement().getText();
        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/simple.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11315")
    @Templates(value = "plain")
    public void testAccesskey() {
        testHTMLAttribute(slider.advanced().getInput().advanced().getInputElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.accesskey);
    }

    @Test(groups = "smoke")
    @Override
    public void testClickLeftArrow() {
        super.testClickLeftArrow();
    }

    @Test
    @Override
    public void testClickRightArrow() {
        super.testClickRightArrow();
    }

    @Test
    @Templates(value = "plain")
    public void testDecreaseClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        testStyleClass(slider.advanced().getArrowDecreaseElement(), BasicAttributes.decreaseClass);
    }

    @Test
    @Templates(value = "plain")
    public void testDecreaseSelectedClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, 4000);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.decreaseSelectedClass, value);
        fireEvent(slider.advanced().getArrowDecreaseElement(), Event.MOUSEDOWN);
        Graphene.waitGui().withMessage("decreaseSelectedClass does not work")
            .until().element(slider.advanced().getArrowDecreaseElement()).attribute("class").contains(value);
        fireEvent(slider.advanced().getArrowIncreaseElement(), Event.MOUSEUP);

    }

    @Test
    @Use(field = "delay", ints = { 1000, 2000 })
    public void testDelay() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, delay);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        fireEvent(slider.advanced().getArrowIncreaseElement(), Event.MOUSEDOWN);// starts with increasing/decreasing of the value
        testDelay(delayTestAction, delayTestAction, "delay", delay);
        fireEvent(slider.advanced().getArrowIncreaseElement(), Event.MOUSEUP);
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        testDir(slider.advanced().getRootElement());
    }

    @Test
    public void testDisabled() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.disabled, Boolean.TRUE);
        assertVisible(slider.advanced().getDisabledHandleElement(), "Disabled handle is not on the page.");
        assertNotVisible(slider.advanced().getHandleElement(), "Slider's handle is not present on the page.");
        assertEquals(slider.advanced().getInput().advanced().getInputElement().getAttribute("disabled"), "true",
            "Slider's input should be disabled.");
    }

    @Test(groups = "smoke")
    public void testEnableManualInput() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.enableManualInput, Boolean.FALSE);
        assertEquals(slider.advanced().getInput().advanced().getInputElement().getAttribute("readonly"), "true",
            "Slider's input should be readonly.");
        testMoveWithSlider();
    }

    @Test
    @Templates(value = "plain")
    public void testHandleClass() {
        testStyleClass(slider.advanced().getHandleElement(), BasicAttributes.handleClass);
    }

    @Test
    @Templates(value = "plain")
    public void testHandleSelectedClass() {
        String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.handleSelectedClass, value);
        fireEvent(slider.advanced().getHandleElement(), Event.MOUSEDOWN);
        assertTrue(slider.advanced().getHandleElement().getAttribute("class").contains(value),
            "handleSelectedClass does not work");
    }

    @Test
    public void testImmediate() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.immediate, Boolean.TRUE);

        slider.advanced().getInput().advanced().clear(ClearType.JS).sendKeys("-10");
        guardAjax(slider.advanced().getInput().advanced()).trigger("blur");

        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: 2 -> -10");

        assertEquals(output.getText(), "-10", "Output was not updated.");
    }

    @Test
    @Templates(value = "plain")
    public void testIncreaseClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        testStyleClass(slider.advanced().getArrowIncreaseElement(), BasicAttributes.increaseClass);
    }

    @Test
    @Templates(value = "plain")
    public void testIncreaseSelectedClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, 4000);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.increaseSelectedClass, value);
        fireEvent(slider.advanced().getArrowIncreaseElement(), Event.MOUSEDOWN);
        Graphene.waitGui().withMessage("increaseSelectedClass does not work")
            .until().element(slider.advanced().getArrowIncreaseElement()).attribute("class").contains(value);
        fireEvent(slider.advanced().getArrowIncreaseElement(), Event.MOUSEUP);
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertVisible(slider.advanced().getInput().advanced().getInputElement(), "Slider's input is not present on the page.");
        assertVisible(slider.advanced().getMinimumElement(), "Slider's min value is not present on the page.");
        assertVisible(slider.advanced().getMinimumElement(), "Slider's max value is not present on the page.");
        assertVisible(slider.advanced().getTrackElement(), "Slider's track is not present on the page.");
        assertVisible(slider.advanced().getHandleElement(), "Slider's handle is not present on the page.");
        assertNotPresent(slider.advanced().getArrowDecreaseElement(), "Slider's left arrow should not be present on the page.");
        assertNotPresent(slider.advanced().getArrowIncreaseElement(), "Slider's right arrow should not be present on the page.");
        assertNotPresent(slider.advanced().getTooltipElement(), "Slider's tooltip should not be present on the page.");
    }

    @Test
    @Templates(value = "plain")
    public void testInputClass() {
        testStyleClass(slider.advanced().getInput().advanced().getInputElement(), BasicAttributes.inputClass);
    }

    @Test
    @Use(field = "position", enumeration = true)
    public void testInputPosition() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputPosition, position.value);
        Point inputPosition = Utils.getLocations(slider.advanced().getInput().advanced().getInputElement()).getTopLeft();
        Point trackPosition = Utils.getLocations(slider.advanced().getTrackElement()).getTopLeft();
        switch (position) {
            case left:
                assertTrue(trackPosition.x > inputPosition.x, "Track should be on the right of input on the page.");
                assertNotPresent(br, "Track and input should be on the same line.");
                break;
            case NULL:
            case right:
                assertTrue(trackPosition.x < inputPosition.x, "Track should be on the left of input on the page.");
                assertNotPresent(br, "Track and input should be on the same line.");
                break;
            case bottom:
                assertTrue(trackPosition.y < inputPosition.y, "Track should be above input on the page.");
                assertPresent(br, "Track and input should not be on the same line.");
                break;
            case top:
                assertTrue(trackPosition.y > inputPosition.y, "Track should be below input on the page.");
                assertPresent(br, "Track and input should not be on the same line.");
                break;
        }
    }

    @Test
    @Templates(value = "plain")
    public void testInputSize() {
        int testedSize = 2;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputSize, testedSize);
        assertEquals(slider.advanced().getInput().advanced().getInputElement().getAttribute("size"), String.valueOf(testedSize),
            "Input's size attribute.");

        testedSize = 40;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputSize, testedSize);
        assertEquals(slider.advanced().getInput().advanced().getInputElement().getAttribute("size"), String.valueOf(testedSize),
            "Input's size attribute.");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testAttributeLang(slider.advanced().getRootElement());
    }

    @Test
    public void testMaxValueByHandleMoving() {
        int maxnum = 15;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxValue, maxnum);

        moveWithSliderActionWithWaitRequest(slider.advanced().getWidth()).perform();
        assertEquals(output.getText(), String.valueOf(maxnum), "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), String.valueOf(maxnum), "Input was not updated.");
    }

    @Test
    public void testMaxValueByTyping() {
        int maxnum = 13;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxValue, maxnum);
        String num = "10";

        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), num, "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), num, "Input was not updated.");

        num = "14";
        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), String.valueOf(maxnum), "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), String.valueOf(maxnum), "Input was not updated.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9860")
    @Templates(value = "plain")
    public void testMaxlength() {
        String testedLength = "5";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxlength, testedLength);
        assertEquals(slider.advanced().getInput().advanced().getInputElement().getAttribute("maxlength"), "5",
            "Attribute maxlength of input.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxlength, "");
        assertEquals(slider.advanced().getInput().advanced().getInputElement().getAttribute("maxlength"), null,
            "Attribute maxlength should not be present.");
    }

    @Test
    public void testMinValueByHandleMoving() {
        int value = -15;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.minValue, value);

        moveWithSliderActionWithWaitRequest(0).perform();
        assertEquals(output.getText(), String.valueOf(value), "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), String.valueOf(value), "Input was not updated.");
    }

    @Test
    public void testMinValueByTyping() {
        int minValue = -13;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.minValue, minValue);
        String num = "-10";

        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), num, "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), num, "Input was not updated.");

        num = "-14";
        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), String.valueOf(minValue), "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), String.valueOf(minValue), "Input was not updated.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11314")
    @Override
    public void testMoveWithSlider() {
        super.testMoveWithSlider();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnblur() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes, InputNumberSliderAttributes.onblur);
    }

    @Test
    public void testOnchangeByArrowsClicking() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onchange, new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(slider, WaitRequestType.XHR).decrease();
            }
        });
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onchange, new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(slider, WaitRequestType.XHR).increase();
            }
        });
    }

    @Test
    public void testOnchangeByTyping() {
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onchange,
            typeToInputActionWithXHRWaitRequest("5"));
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onclick,
            new Actions(driver).click(slider.advanced().getRootElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.ondblclick,
            new Actions(driver).doubleClick(slider.advanced().getRootElement()).build());
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnfocus() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onfocus);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onkeyup);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onmousedown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onmousemove);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onmouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onmouseup);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnselect() {
        testFireEventWithJS(slider.advanced().getRootElement(), inputNumberSliderAttributes,
            InputNumberSliderAttributes.onselect);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.rendered, Boolean.FALSE);
        assertNotVisible(slider.advanced().getRootElement(), "Slider should not be rendered when rendered=false.");
    }

    @Test
    public void testShowArrows() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        assertVisible(slider.advanced().getArrowDecreaseElement(), "Slider's left arrow should be on the page.");
        assertVisible(slider.advanced().getArrowIncreaseElement(), "Slider's right arrow should be on the page.");
    }

    @Test
    public void testShowBoundaryValues() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showBoundaryValues, Boolean.FALSE);

        assertNotVisible(slider.advanced().getMinimumElement(), "Slider's min value should not be present on the page.");
        assertNotVisible(slider.advanced().getMinimumElement(), "Slider's max value should not be present on the page.");
    }

    @Test
    public void testShowInput() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showInput, Boolean.FALSE);
        assertFalse(new WebElementConditionFactory(slider.advanced().getInput().advanced().getInputElement()).isVisible()
            .apply(driver), "Input should not be visible on the page.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11314")
    public void testShowTooltip() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showTooltip, Boolean.TRUE);
        String startValue = slider.advanced().getInput().getStringValue();

        fireEvent(slider.advanced().getHandleElement(), Event.MOUSEDOWN);
        assertVisible(slider.advanced().getTooltipElement(), "Tooltip is not visible");
        assertEquals(slider.advanced().getTooltipElement().getText(), startValue, "Tooltip's text.");

        fireEvent(slider.advanced().getHandleElement(), Event.MOUSEUP);
        assertNotVisible(slider.advanced().getTooltipElement(), "Tooltip should not be visible");
    }

    @Test
    public void testStep() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, 100);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.step, 7);

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).increase();
        assertEquals(output.getText(), "9", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).increase();
        assertEquals(output.getText(), "10", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).decrease();
        assertEquals(output.getText(), "3", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).decrease();
        assertEquals(output.getText(), "-4", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).decrease();
        assertEquals(output.getText(), "-10", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).increase();
        assertEquals(output.getText(), "-3", "Wrong output");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(slider.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(slider.advanced().getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "plain")
    public void testTabindex() {
        String value = "55";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.tabindex, value);

        assertEquals(slider.advanced().getSliderElement().getAttribute("tabindex"), value, "Attribute tabindex of track.");
        assertEquals(slider.advanced().getInput().advanced().getInputElement().getAttribute("tabindex"), value,
            "Attribute tabindex of input.");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(slider.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testTooltipClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showTooltip, Boolean.TRUE);

        testStyleClass(slider.advanced().getTooltipElement(), BasicAttributes.tooltipClass);
    }

    @Test
    @Templates(value = "plain")
    public void testTrackClass() {
        testStyleClass(slider.advanced().getTrackElement(), BasicAttributes.trackClass);
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    @Override
    public void testTypeIntoInputBig() {
        super.testTypeIntoInputBig();
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    @Override
    public void testTypeIntoInputCorrect() {
        super.testTypeIntoInputCorrect();
    }

    @Test
    @Use(field = "number", value = "decimalNumbers")
    @Override
    public void testTypeIntoInputDecimal() {
        super.testTypeIntoInputDecimal();
    }

    @Test
    @Override
    public void testTypeIntoInputNotNumber() {
        super.testTypeIntoInputNotNumber();
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    @Override
    public void testTypeIntoInputSmall() {
        super.testTypeIntoInputSmall();
    }

    @Test
    public void testValueChangeListener() {
        typeToInputActionWithXHRWaitRequest("5").perform();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: 2 -> 5");
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    public void testValueBig() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);
        String max = inputNumberSliderAttributes.get(InputNumberSliderAttributes.maxValue);

        assertEquals(output.getText(), number, "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), max, "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    public void testValueCorrect() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);

        assertEquals(output.getText(), number, "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), number, "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    public void testValueSmall() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);
        String min = inputNumberSliderAttributes.get(InputNumberSliderAttributes.minValue);

        assertEquals(output.getText(), number, "Output was not updated.");
        assertEquals(slider.advanced().getInput().getStringValue(), min, "Input was not updated.");
    }
}
