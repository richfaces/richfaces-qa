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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.decreaseClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.handleClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.increaseClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.inputClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.tooltipClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.trackClass;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.inputNumberSliderAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSlider/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision: 23011 $
 */
public class TestRichSlider extends AbstractSliderTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/simple.xhtml");
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    @Override
    public void testTypeIntoInputCorrect() {
        super.testTypeIntoInputCorrect();
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    @Override
    public void testTypeIntoInputSmall() {
        super.testTypeIntoInputSmall();
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    @Override
    public void testTypeIntoInputBig() {
        super.testTypeIntoInputBig();
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
    @Override
    public void testClickLeft() {
        super.testClickLeft();
    }

    @Test
    @Override
    public void testClickRight() {
        super.testClickRight();
    }

    @Test
    @Templates(exclude = {"richPopupPanel", "richAccordion"})
    @Override
    public void testClick() {
        super.testClick();
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-11314")
    @Templates(value = {"richPopupPanel"})
    public void testClickInPopupPanel() {
        super.testClick();
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-11314")
    @Templates(value = {"richAccordion"})
    public void testClickInAccordion() {
        super.testClick();
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isVisible(slider), "Slider is not present on the page.");
        assertTrue(selenium.isVisible(input), "Slider's input is not present on the page.");
        assertFalse(selenium.isElementPresent(left), "Slider's left arrow should not be present on the page.");
        assertFalse(selenium.isElementPresent(right), "Slider's right arrow should not be present on the page.");
        assertTrue(selenium.isVisible(minBoundary), "Slider's min value is not present on the page.");
        assertTrue(selenium.isVisible(maxBoundary), "Slider's max value is not present on the page.");
        assertTrue(selenium.isVisible(track), "Slider's track is not present on the page.");
        assertTrue(selenium.isVisible(handle), "Slider's handle is not present on the page.");
        assertFalse(selenium.isElementPresent(tooltip), "Slider's tooltip should not be present on the page.");
    }

    @Test
    @Templates(exclude = {"richPopupPanel"})
    public void testAccesskey() {
        testHtmlAttribute(input, "accesskey", "x");
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-11315")
    @Templates(value = {"richPopupPanel"})
    public void testAccesskeyInPopupPanel() {
        testHtmlAttribute(input, "accesskey", "x");
    }

    @Test
    public void testDecreaseClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        testStyleClass(left, decreaseClass);
    }

    @Test
    public void testDecreaseSelectedClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        final String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.decreaseSelectedClass, value);

        selenium.mouseDown(left);
        assertTrue(selenium.belongsClass(left, value), "decreaseSelectedClass does not work");
        waitFor(500); // FIXME workaround
    }

    @Test
    @Use(field = "delay", ints = {800, 1250, 3700})
    public void testDelay() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, delay);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        try {
            verifyDelay(left, delay);
            verifyDelay(right, delay);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testDir() {
        testDir(slider);
    }

    @Test
    public void testDisabled() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.disabled, Boolean.TRUE);

        AttributeLocator<?> disabledAttribute = input.getAttribute(new Attribute("disabled"));
        assertEquals(selenium.getAttribute(disabledAttribute), "true", "Input should be disabled.");

        assertFalse(selenium.isElementPresent(handle), "Handle should not be present on the page.");

        JQueryLocator handleDisabled = pjq("span.rf-insl-hnd-dis");
        assertTrue(selenium.isElementPresent(handleDisabled), "An disabled handle should be displayed.");
        assertTrue(selenium.isVisible(handleDisabled), "An disabled handle should be displayed.");
    }

    @Test
    @Templates(exclude = {"richPopupPanel", "richAccordion"})
    public void testEnableManualInput() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.enableManualInput, Boolean.FALSE);

        AttributeLocator<?> readonlyAttribute = input.getAttribute(new Attribute("readonly"));
        assertEquals(selenium.getAttribute(readonlyAttribute), "true", "Input should be read-only.");

        testClick();
    }

    @Test(groups = {"4.Future"})
    @Templates(value = {"richPopupPanel"})
    public void testEnableManualInputInPopupPanel() {
        testEnableManualInput();
    }

    @Test(groups = {"4.Future"})
    @Templates(value = {"richAccordion"})
    public void testEnableManualInputInAccordion() {
        testEnableManualInput();
    }

    @Test
    public void testHandleClass() {
        testStyleClass(handle, handleClass);
    }

    @Test
    public void testHandleSelectedClass() {
        final String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.handleSelectedClass, value);

        selenium.mouseDown(handle);
        assertTrue(selenium.belongsClass(handle, value), "handleSelectedClass does not work");
    }

    @Test
    public void testImmediate() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.immediate, Boolean.TRUE);

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "-10");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        // Remove first 2 preceding chars from correct message (used to remove usual '* ' token)
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: 2 -> -10");

        assertEquals(selenium.getText(output), "-10", "Output was not updated.");
    }

    @Test
    public void testIncreaseClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        testStyleClass(right, increaseClass);
    }

    @Test
    public void testIncreaseSelectedClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        final String value = "metamer-ftest-class";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.increaseSelectedClass, value);

        selenium.mouseDown(right);
        assertTrue(selenium.belongsClass(right, value), "increaseSelectedClass does not work");
    }

    @Test
    public void testInputClass() {
        testStyleClass(input, inputClass);
    }

    @Test
    public void testInputPosition() {
        JQueryLocator br = pjq("span[id$=slider] br");
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputPosition, "bottom");

        int inputPosition = selenium.getElementPositionTop(input);
        int trackPosition = selenium.getElementPositionTop(track);
        assertTrue(trackPosition < inputPosition, "Track should be above input on the page.");
        assertTrue(selenium.isElementPresent(br), "Track and input should not be on the same line.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputPosition, "top");
        inputPosition = selenium.getElementPositionTop(input);
        trackPosition = selenium.getElementPositionTop(track);
        assertTrue(trackPosition > inputPosition, "Track should be below input on the page.");
        assertTrue(selenium.isElementPresent(br), "Track and input should not be on the same line.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputPosition, "right");
        inputPosition = selenium.getElementPositionLeft(input);
        trackPosition = selenium.getElementPositionLeft(track);
        assertTrue(trackPosition < inputPosition, "Track should be on the left of input on the page.");
        assertFalse(selenium.isElementPresent(br), "Track and input should be on the same line.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputPosition, "left");
        inputPosition = selenium.getElementPositionLeft(input);
        trackPosition = selenium.getElementPositionLeft(track);
        assertTrue(trackPosition > inputPosition, "Track should be on the right of input on the page.");
        assertFalse(selenium.isElementPresent(br), "Track and input should be on the same line.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputPosition, "null");
        inputPosition = selenium.getElementPositionLeft(input);
        trackPosition = selenium.getElementPositionLeft(track);
        assertTrue(trackPosition < inputPosition, "Track should be on the left of input on the page.");
        assertFalse(selenium.isElementPresent(br), "Track and input should be on the same line.");
    }

    @Test
    public void testInputSize() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputSize, 2);

        AttributeLocator<?> sizeAttribute = input.getAttribute(new Attribute("size"));
        assertEquals(selenium.getAttribute(sizeAttribute), "2", "Input's size attribute.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.inputSize, 40);
        assertEquals(selenium.getAttribute(sizeAttribute), "40", "Input's size attribute.");
    }

    @Test
    public void testLang() {
        testLang(slider);
    }

    @Test
    public void testMaxValueType() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxValue, 13);

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "11");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "11", "Output was not updated.");

        reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "13");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "13", "Output was not updated.");
    }

    @Test
    @Templates(exclude = {"richPopupPanel", "richAccordion"})
    public void testMaxValueClick() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxValue, 20);

        String reqTime = selenium.getText(time);
        reqTime = selenium.getText(time);
        guardXhr(selenium).mouseDownAt(track, new Point(170, 0));
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "17", "Output was not updated.");
        int margin = selenium.getElementPositionLeft(handle) - selenium.getElementPositionLeft(track);
        assertTrue(margin >= 167 && margin <= 173, "Left margin of handle should be between 167 and 173 (was " + margin
                + ").");

        reqTime = selenium.getText(time);
        guardXhr(selenium).mouseDownAt(track, new Point(195, 0));
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "20", "Output was not updated.");
        margin = selenium.getElementPositionLeft(handle) - selenium.getElementPositionLeft(track);
        assertTrue(margin >= 192 && margin <= 198, "Left margin of handle should be between 192 and 198 (was " + margin
                + ").");
    }

    @Test(groups = {"4.Future"})
    @Templates(value = {"richAccordion"})
    public void testMaxValueClickInAccordion() {
        testMaxValueClick();
    }

    @Test(groups = {"4.Future"})
    @Templates(value = {"richPopupPanel"})
    public void testMaxValueClickInPopupPanel() {
        testMaxValueClick();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9860")
    public void testMaxlength() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxlength, 5);

        AttributeLocator<?> attr = input.getAttribute(Attribute.MAXLENGTH);
        assertEquals(selenium.getAttribute(attr), "5", "Attribute maxlength of input.");

        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxlength, "");

        assertFalse(selenium.isAttributePresent(attr), "Attribute maxlength should not be present.");
    }

    @Test
    public void testMinValueType() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.minValue, -13);

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "-11");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "-11", "Output was not updated.");

        reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "-13");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "-13", "Output was not updated.");
    }

    @Test
    @Templates(exclude = {"richPopupPanel", "richAccordion"})
    public void testMinValueClick() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.minValue, -20);

        String reqTime = selenium.getText(time);
        guardXhr(selenium).mouseDownAt(track, new Point(28, 0));
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "-16", "Output was not updated.");
        int margin = selenium.getElementPositionLeft(handle) - selenium.getElementPositionLeft(track);
        assertTrue(margin >= 25 && margin <= 31, "Left margin of handle should be between 25 and 31 (was " + margin
                + ").");

        reqTime = selenium.getText(time);
        guardXhr(selenium).mouseDownAt(track, new Point(0, 0));
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "-20", "Output was not updated.");
        margin = selenium.getElementPositionLeft(handle) - selenium.getElementPositionLeft(track);
        assertTrue(margin <= 3, "Left margin of handle should be between 0 and 3 (was " + margin + ").");
    }

    @Test(groups = {"4.Future"})
    @Templates(value = {"richAccordion"})
    public void testMinValueClickInAccordion() {
        testMinValueClick();
    }

    @Test(groups = {"4.Future"})
    @Templates(value = {"richPopupPanel"})
    public void testMinValueClickInPopupPanel() {
        testMinValueClick();
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnblur() {
        testFireEvent(Event.BLUR, slider);
    }

    @Test
    public void testOnchangeType() {
        String value = "metamerEvents += \"change \"";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.onchange, value);

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "4");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "change", "Attribute onchange doesn't work");
        assertEquals(events.length, 1, "Only one event should be fired");
    }

    @Test
    @Templates(exclude = {"richPopupPanel"})
    public void testOnchangeClick() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        String value = "metamerEvents += \"change \"";
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.onchange, value);

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        // click on track, left arrow and right arrow
        selenium.mouseDownAt(track, new Point(30, 0));
        selenium.mouseUpAt(track, new Point(30, 0));
        selenium.runScript(clickRight);
        selenium.runScript(clickLeft);

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");
        assertEquals(events[0], "change", "Attribute onchange doesn't work");
        assertEquals(events[1], "change", "Attribute onchange doesn't work.");
        assertEquals(events[1], "change", "Attribute onchange doesn't work.");
        assertEquals(events.length, 3, "Three events should be fired.");
    }

    @Test(groups = {"4.Future"})
    @Templates(value = {"richPopupPanel"})
    public void testOnchangeClickInPopupPanel() {
        testOnchangeClick();
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, slider);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, slider);
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, slider);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, slider);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, slider);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, slider);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, slider);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, slider);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, slider);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, slider);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, slider);
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-10829")
    public void testOnselect() {
        testFireEvent(Event.SELECT, slider);
    }

    @Test
    public void testRendered() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.rendered, Boolean.FALSE);

        assertFalse(selenium.isElementPresent(slider), "Slider should not be rendered when rendered=false.");
    }

    @Test
    public void testShowArrows() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);

        assertTrue(selenium.isElementPresent(left), "Left arrow should be present on the page.");
        assertTrue(selenium.isVisible(left), "Left arrow should be visible.");
        assertTrue(selenium.isElementPresent(right), "Right arrow should be present on the page.");
        assertTrue(selenium.isVisible(right), "Right arrow should be visible.");
    }

    @Test
    public void testShowBoundaryValues() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showBoundaryValues, Boolean.FALSE);

        assertFalse(selenium.isElementPresent(minBoundary), "Boundary values should not be present on the page.");
        assertFalse(selenium.isElementPresent(maxBoundary), "Boundary values should not be present on the page.");
    }

    @Test
    public void testShowInput() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showInput, Boolean.FALSE);

        if (selenium.isElementPresent(input)) {
            assertFalse(selenium.isVisible(input), "Input should not be visible on the page.");
        }
    }

    @Test
    @Templates(exclude = {"richPopupPanel", "richAccordion"})
    public void testShowTooltip() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showTooltip, Boolean.TRUE);

        assertTrue(selenium.isElementPresent(tooltip), "Tooltip should be present on the page.");
        assertFalse(selenium.isVisible(tooltip), "Tooltip should not be visible.");

        selenium.mouseDownAt(track, new Point(0, 0));
        assertTrue(selenium.isVisible(tooltip), "Tooltip should be visible.");
        assertEquals(selenium.getText(tooltip), "-10", "Value of tooltip.");

        selenium.mouseUpAt(track, new Point(0, 0));
        assertFalse(selenium.isVisible(tooltip), "Tooltip should not be visible.");
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-11314")
    @Templates(value = {"richAccordion"})
    public void testShowTooltipInAccordion() {
        testShowTooltip();
    }

    @Test(groups = {"4.Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-11314")
    @Templates(value = {"richPopupPanel"})
    public void testShowTooltipInPopupPanel() {
        testShowTooltip();
    }

    @Test
    public void testStep() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showArrows, Boolean.TRUE);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.delay, 100);
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.step, 7);

        clickArrow(right, 1);
        assertEquals(selenium.getText(output), "9", "Wrong output");

        clickArrow(right, 1);
        assertEquals(selenium.getText(output), "10", "Wrong output");

        clickArrow(left, 1);
        assertEquals(selenium.getText(output), "3", "Wrong output");

        clickArrow(left, 1);
        assertEquals(selenium.getText(output), "-4", "Wrong output");

        clickArrow(left, 1);
        assertEquals(selenium.getText(output), "-10", "Wrong output");

        clickArrow(right, 1);
        assertEquals(selenium.getText(output), "-3", "Wrong output");
    }

    @Test
    public void testStyle() {
        testStyle(slider);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(slider);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    public void testTabindex() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.tabindex, 55);

        AttributeLocator<?> attr = track.getAttribute(new Attribute("tabindex"));
        assertTrue(selenium.isAttributePresent(attr), "Attribute tabindex of track is not present.");
        assertEquals(selenium.getAttribute(attr), "55", "Attribute tabindex of track.");

        attr = input.getAttribute(new Attribute("tabindex"));
        assertTrue(selenium.isAttributePresent(attr), "Attribute tabindex of input is not present.");
        assertEquals(selenium.getAttribute(attr), "55", "Attribute tabindex of input.");
    }

    @Test
    public void testTooltipClass() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.showTooltip, Boolean.TRUE);

        testStyleClass(tooltip, tooltipClass);
    }

    @Test
    public void testTrackClass() {
        testStyleClass(track, trackClass);
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    public void testValueCorrect() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);

        assertEquals(selenium.getText(output), number, "Output was not updated.");
        assertEquals(selenium.getValue(input), number, "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    public void testValueSmall() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);

        assertEquals(selenium.getText(output), number, "Output was not updated.");
        assertEquals(selenium.getValue(input), "-10", "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    public void testValueBig() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.value, number);

        assertEquals(selenium.getText(output), number, "Output was not updated.");
        assertEquals(selenium.getValue(input), "10", "Input was not updated.");
    }

    /**
     * Clicks on slider's arrow.
     *
     * @param arrow slider's left or right arrow locator
     * @param clicks how many times should it be clicked
     */
    private void clickArrow(ElementLocator<?> arrow, int clicks) {
        String reqTime = null;

        for (int i = 0; i < clicks; i++) {
            reqTime = selenium.getText(time);
            guardXhr(selenium).runScript(
                    new JavaScript("jQuery(\"" + arrow.getRawLocator() + "\").mousedown().mouseup()"));

            waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        }
    }

    /**
     * Clicks on slider's arrow and verifies delay.
     *
     * @param arrow slider's left or right arrow locator
     * @param delay awaited delay between ajax requests
     */
    private void verifyDelay(JQueryLocator arrow, int delay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
        long delta = (long) (delay * 0.5);
        Set<String> timesSet = new TreeSet<String>();

        selenium.mouseDown(arrow);

        for (int i = 0; i < 14; i++) {
            timesSet.add(selenium.getText(time));
            waitFor(delta);
        }

        selenium.mouseUp(arrow);

        Date[] timesArray = new Date[timesSet.size()];
        List<String> timesList = new ArrayList<String>(timesSet);

        for (int i = 1; i < timesList.size(); i++) {
            timesArray[i] = sdf.parse(timesList.get(i));
        }

        delta = (long) (delay * 0.5);
        long average = countAverage(timesArray);
        assertTrue(Math.abs(average - delay) < delta, "Average delay " + average + " is too far from set value ("
                + delay + ")");
    }

    private long countAverage(Date[] times) {
        long total = 0L;
        for (int i = 1; i < times.length - 1; i++) {
            total += (times[i].getTime() - times[i + 1].getTime());
        }

        return Math.abs(total / (times.length - 2));
    }
}
