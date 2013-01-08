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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inputNumberSliderAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSlider/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSliderAttributes extends AbstractSliderTest {

    @FindBy(css = "span.rf-insl > br")
    WebElement br;
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

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/simple.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11315")
    public void testAccesskey() {
        testHTMLAttribute(slider.getInput().getInput(), inputNumberSliderAttributes, InputNumberSliderAttributes.accesskey);
    }

    @Test
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
    public void testDecreaseClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        testStyleClass(slider.getArrowDecrease(), BasicAttributes.decreaseClass);
    }

    @Test
    public void testDecreaseSelectedClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, 4000);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.decreaseSelectedClass, value);
        fireEvent(slider.getArrowDecrease(), Event.MOUSEDOWN);
        Graphene.waitGui().withMessage("decreaseSelectedClass does not work")
                .until(Graphene.attribute(slider.getArrowDecrease(), "class")
                .contains(value));
        fireEvent(slider.getArrowIncrease(), Event.MOUSEUP);

    }

    @Test
    @Use(field = "delay", ints = { 800, 1250, 1900 })
    public void testDelay() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, delay);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        verifyDelay(slider.getArrowDecrease(), delay);
        verifyDelay(slider.getArrowIncrease(), delay);
    }

    @Test
    public void testDir() {
        testDir(slider.getRoot());
    }

    @Test
    public void testDisabled() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.disabled, Boolean.TRUE);
        assertVisible(slider.getNumberSlider().getDisabledHandleElement(), "Disabled handle is not on the page.");
        assertNotVisible(slider.getNumberSlider().getHandleElement(), "Slider's handle is not present on the page.");
        assertEquals(slider.getInput().getInput().getAttribute("disabled"), "true", "Slider's input should be disabled.");
    }

    @Test
    public void testEnableManualInput() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.enableManualInput, Boolean.FALSE);
        assertEquals(slider.getInput().getInput().getAttribute("readonly"), "true", "Slider's input should be readonly.");
        testMoveWithSlider();
    }

    @Test
    public void testHandleClass() {
        testStyleClass(slider.getNumberSlider().getHandleElement(), BasicAttributes.handleClass);
    }

    @Test
    public void testHandleSelectedClass() {
        String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.handleSelectedClass, value);
        fireEvent(slider.getNumberSlider().getHandleElement(), Event.MOUSEDOWN);
        assertTrue(slider.getNumberSlider().getHandleElement().getAttribute("class").contains(value), "handleSelectedClass does not work");
    }

    @Test
    public void testImmediate() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.immediate, Boolean.TRUE);

        MetamerPage.waitRequest(slider.getInput().clear(ClearType.JS).fillIn("-10"), WaitRequestType.XHR).trigger("blur");

        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: 2 -> -10");

        assertEquals(output.getText(), "-10", "Output was not updated.");
    }

    @Test
    public void testIncreaseClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        testStyleClass(slider.getArrowIncrease(), BasicAttributes.increaseClass);
    }

    @Test
    public void testIncreaseSelectedClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, 4000);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.increaseSelectedClass, value);
        fireEvent(slider.getArrowIncrease(), Event.MOUSEDOWN);
        Graphene.waitGui().withMessage("increaseSelectedClass does not work")
                .until(Graphene.attribute(slider.getArrowIncrease(), "class")
                .contains(value));
        fireEvent(slider.getArrowIncrease(), Event.MOUSEUP);
    }

    @Test
    public void testInit() {
        assertTrue(slider.isVisible(), "Slider is not present on the page.");
        assertTrue(slider.getInput().isVisible(), "Slider's input is not present on the page.");
        assertVisible(slider.getMinimumElement(), "Slider's min value is not present on the page.");
        assertVisible(slider.getMaximumElement(), "Slider's max value is not present on the page.");
        assertVisible(slider.getNumberSlider().getTrackElement(), "Slider's track is not present on the page.");
        assertVisible(slider.getNumberSlider().getHandleElement(), "Slider's handle is not present on the page.");
        assertNotPresent(slider.getArrowDecrease(), "Slider's left arrow should not be present on the page.");
        assertNotPresent(slider.getArrowIncrease(), "Slider's right arrow should not be present on the page.");
        assertNotPresent(slider.getTooltipElement(), "Slider's tooltip should not be present on the page.");
    }

    @Test
    public void testInputClass() {
        testStyleClass(slider.getInput().getInput(), BasicAttributes.inputClass);
    }

    @Test
    @Use(field = "position", enumeration = true)
    public void testInputPosition() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputPosition, position.value);
        Point inputPosition = Utils.getLocations(slider.getInput().getInput()).getTopLeft();
        Point trackPosition = Utils.getLocations(slider.getNumberSlider().getTrackElement()).getTopLeft();
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
    public void testInputSize() {
        int testedSize = 2;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputSize, testedSize);
        assertEquals(slider.getInput().getInput().getAttribute("size"),
                String.valueOf(testedSize), "Input's size attribute.");

        testedSize = 40;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputSize, testedSize);
        assertEquals(slider.getInput().getInput().getAttribute("size"),
                String.valueOf(testedSize), "Input's size attribute.");
    }

    @Test
    public void testLang() {
        testAttributeLang(slider.getRoot());
    }

    @Test
    public void testMaxValueByHandleMoving() {
        int maxnum = 15;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxValue, maxnum);

        moveWithSliderActionWithWaitRequest(slider.getNumberSlider().getWidth()).perform();
        assertEquals(output.getText(), String.valueOf(maxnum), "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), String.valueOf(maxnum), "Input was not updated.");
    }

    @Test
    public void testMaxValueByTyping() {
        int maxnum = 13;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxValue, maxnum);
        String num = "10";

        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), num, "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), num, "Input was not updated.");

        num = "14";
        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), String.valueOf(maxnum), "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), String.valueOf(maxnum), "Input was not updated.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9860")
    public void testMaxlength() {
        String testedLength = "5";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxlength, testedLength);
        assertEquals(slider.getInput().getInput().getAttribute("maxlength"), "5", "Attribute maxlength of input.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxlength, "");
        assertEquals(slider.getInput().getInput().getAttribute("maxlength"), null, "Attribute maxlength should not be present.");
    }

    @Test
    public void testMinValueByHandleMoving() {
        int value = -15;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.minValue, value);

        moveWithSliderActionWithWaitRequest(0).perform();
        assertEquals(output.getText(), String.valueOf(value), "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), String.valueOf(value), "Input was not updated.");
    }

    @Test
    public void testMinValueByTyping() {
        int minValue = -13;
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.minValue, minValue);
        String num = "-10";

        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), num, "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), num, "Input was not updated.");

        num = "-14";
        typeToInputActionWithXHRWaitRequest(num).perform();
        assertEquals(output.getText(), String.valueOf(minValue), "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), String.valueOf(minValue), "Input was not updated.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11314")
    @Override
    public void testMoveWithSlider() {
        super.testMoveWithSlider();
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnblur() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes, InputNumberSliderAttributes.onblur);
    }

    @Test
    public void testOnchangeByArrowsClicking() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onchange, new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(slider, WaitRequestType.XHR).decreaseWithArrows();
            }
        });
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onchange,
                new Action() {
                    @Override
                    public void perform() {
                        MetamerPage.waitRequest(slider, WaitRequestType.XHR).increaseWithArrows();
                    }
                });
    }

    @Test
    public void testOnchangeByTyping() {
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onchange, typeToInputActionWithXHRWaitRequest("5"));
    }

    @Test
    public void testOnclick() {
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.onclick,
                new Actions(driver).click(slider.getRoot()).build());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(inputNumberSliderAttributes, InputNumberSliderAttributes.ondblclick,
                new Actions(driver).doubleClick(slider.getRoot()).build());
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnfocus() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onfocus);
    }

    @Test
    public void testOnkeydown() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onkeydown);
    }

    @Test
    public void testOnkeypress() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onkeypress);
    }

    @Test
    public void testOnkeyup() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onkeyup);
    }

    @Test
    public void testOnmousedown() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onmousedown);
    }

    @Test
    public void testOnmousemove() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onmousemove);
    }

    @Test
    public void testOnmouseout() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onmouseover);
    }

    @Test
    public void testOnmouseup() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onmouseup);
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnselect() {
        testFireEventWithJS(slider.getRoot(), inputNumberSliderAttributes,
                InputNumberSliderAttributes.onselect);
    }

    @Test
    public void testRendered() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.rendered, Boolean.FALSE);
        assertFalse(slider.isVisible(), "Slider should not be rendered when rendered=false.");
    }

    @Test
    public void testShowArrows() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        assertVisible(slider.getArrowDecrease(), "Slider's left arrow should be on the page.");
        assertVisible(slider.getArrowIncrease(), "Slider's right arrow should be on the page.");
    }

    @Test
    public void testShowBoundaryValues() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showBoundaryValues, Boolean.FALSE);

        assertNotVisible(slider.getMinimumElement(), "Slider's min value should not be present on the page.");
        assertNotVisible(slider.getMaximumElement(), "Slider's max value should not be present on the page.");
    }

    @Test
    public void testShowInput() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showInput, Boolean.FALSE);
        assertFalse(slider.getInput().isVisible(), "Input should not be visible on the page.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11314")
    public void testShowTooltip() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showTooltip, Boolean.TRUE);
        String startValue = slider.getInput().getStringValue();

        fireEvent(slider.getNumberSlider().getHandleElement(), Event.MOUSEDOWN);
        assertVisible(slider.getTooltipElement(), "Tooltip is not visible");
        assertEquals(slider.getTooltipElement().getText(), startValue, "Tooltip's text.");

        fireEvent(slider.getNumberSlider().getHandleElement(), Event.MOUSEUP);
        assertNotVisible(slider.getTooltipElement(), "Tooltip should not be visible");
    }

    @Test
    public void testStep() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, 100);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.step, 7);
        String startValue = slider.getInput().getStringValue();

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).increaseWithArrows();
        assertEquals(output.getText(), "9", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).increaseWithArrows();
        assertEquals(output.getText(), "10", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).decreaseWithArrows();
        assertEquals(output.getText(), "3", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).decreaseWithArrows();
        assertEquals(output.getText(), "-4", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).decreaseWithArrows();
        assertEquals(output.getText(), "-10", "Wrong output");

        MetamerPage.waitRequest(slider, WaitRequestType.XHR).increaseWithArrows();
        assertEquals(output.getText(), "-3", "Wrong output");
    }

    @Test
    public void testStyle() {
        testStyle(slider.getRoot());
    }

    @Test
    public void testStyleClass() {
        testStyleClass(slider.getRoot());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    public void testTabindex() {
        String value = "55";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.tabindex, value);

        assertEquals(slider.getNumberSlider().getRoot().getAttribute("tabindex"), value, "Attribute tabindex of track.");
        assertEquals(slider.getInput().getInput().getAttribute("tabindex"), "55", "Attribute tabindex of input.");
    }

    @Test
    public void testTitle() {
        testTitle(slider.getRoot());
    }

    @Test
    public void testTooltipClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showTooltip, Boolean.TRUE);

        testStyleClass(slider.getTooltipElement(), BasicAttributes.tooltipClass);
    }

    @Test
    public void testTrackClass() {
        testStyleClass(slider.getNumberSlider().getTrackElement(), BasicAttributes.trackClass);
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
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: 2 -> 5");
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    public void testValueBig() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);
        String max = inputNumberSliderAttributes.get(InputNumberSliderAttributes.maxValue);

        assertEquals(output.getText(), number, "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), max, "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    public void testValueCorrect() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);

        assertEquals(output.getText(), number, "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), number, "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    public void testValueSmall() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);
        String min = inputNumberSliderAttributes.get(InputNumberSliderAttributes.minValue);

        assertEquals(output.getText(), number, "Output was not updated.");
        assertEquals(slider.getInput().getStringValue(), min, "Input was not updated.");
    }

    /**
     * Clicks on slider's arrow and verifies delay.
     *
     * @param arrow slider's left or right arrow element
     * @param delay delay of request
     */
    private void verifyDelay(WebElement arrow, int delay) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss.SSS");
        long delta = (long) (delay * 0.5);
        int numberOfValues = 5;
        List<String> timesList = new ArrayList<String>(numberOfValues);

        String beforeTime = page.requestTime.getText();
        fireEvent(arrow, Event.MOUSEDOWN);//starts with increasing/decreasing of the value
        for (int i = 0; i < numberOfValues; i++) {
            Graphene.waitModel().until(Graphene.element(page.requestTime).not().text().equalTo(beforeTime));
            beforeTime = page.requestTime.getText();
            timesList.add(beforeTime);
        }
        fireEvent(arrow, Event.MOUSEUP);//stops with increasing/decreasing of the value

        DateTime[] timesArray = new DateTime[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            timesArray[i] = dtf.parseDateTime(timesList.get(i));
        }

        long average = countAverage(timesArray);
        assertTrue(Math.abs(average - delay) < delta, "Average delay " + average + " is too far from set value ("
                + delay + "). Delta was: " + delta + "[ms].");
    }

    private long countAverage(DateTime[] times) {
        long total = 0L;
        for (int i = 0; i < times.length - 1; i++) {
            total += (times[i].getMillis() - times[i + 1].getMillis());
        }
        return Math.abs(total / (times.length - 1));
    }
}
