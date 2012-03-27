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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.listClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
public class TestRichSelect extends AbstractAjocadoTest {

    private JQueryLocator select = pjq("div[id$=select]");
    private JQueryLocator input = pjq("input.rf-sel-inp[id$=selectInput]");
    private AttributeLocator<?> inputValue = input.getAttribute(Attribute.VALUE);
    private JQueryLocator popup = jq("div.rf-sel-lst-cord");
    private JQueryLocator options = jq("div.rf-sel-opt:eq({0})"); // 00..49
    private JQueryLocator button = pjq("span.rf-sel-btn");
    private JQueryLocator output = pjq("span[id$=output]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(select), "Inplace select is not on the page.");
        assertTrue(selenium.isElementPresent(button), "Button should be present on the page.");
        assertEquals(selenium.getAttribute(inputValue), "Click here to edit", "Default label");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed on the page.");
    }

    @Test
    public void testSelectWithMouse() {
        guardNoRequest(selenium).mouseDown(button);
        selenium.mouseUp(button);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
        }

        String[] selectOptions = { "Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota" };
        for (int i = 0; i < 50; i += 10) {
            assertEquals(selenium.getText(options.format(i)), selectOptions[i / 10], "Select option nr. " + i);
        }

        guardNoRequest(selenium).click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        assertTrue(selenium.belongsClass(options.format(10), "rf-sel-sel"));

        waitGui.failWith("Bean was not updated").until(textEquals.locator(output).text("Hawaii"));
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    public void testSelectWithKeyboard() {
        guardNoRequest(selenium).focus(input);
        guardNoRequest(selenium).click(input);
        guardNoRequest(selenium).keyPressNative(40); // arrow down
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
        }

        String[] selectOptions = { "Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota" };
        for (int i = 0; i < 50; i += 10) {
            assertEquals(selenium.getText(options.format(i)), selectOptions[i / 10], "Select option nr. " + i);
        }

        for (int i = 0; i < 11; i++) {
            selenium.keyPressNative(40); // arrow down
        }
        // FIXME
        // guardNoRequest(selenium).keyDown(options.format(10), "\\13"); // enter
        // guardXhr(selenium).fireEvent(input, Event.BLUR);
        // assertTrue(selenium.belongsClass(options.format(10), "rf-sel-sel"));
        //
        // waitGui.failWith("Bean was not updated").until(textEquals.locator(output).text("Hawaii"));
        // phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11320")
    public void testFiltering() {
        selenium.focus(input);
        selenium.typeKeys(input, "a");
        guardNoRequest(selenium).fireEvent(input, Event.KEYDOWN);
        guardNoRequest(selenium).fireEvent(input, Event.KEYPRESS); // keydown works in Chrome and keypress in Firefox

        assertEquals(selenium.getCount(jq("div.rf-sel-opt")), 4, "Count of filtered options ('a')");

        String[] selectOptions = { "Alabama", "Alaska", "Arizona", "Arkansas" };
        for (int i = 0; i < 4; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
            assertEquals(selenium.getText(options.format(i)), selectOptions[i], "Select option nr. " + i);
        }

        for (int i = 0; i < 3; i++) {
            selenium.keyPressNative(40); // arrow down
        }

        assertTrue(selenium.belongsClass(options.format(2), "rf-sel-sel"));
    }

    @Test
    public void testDefaultLabel() {
        selenium.type(pjq("input[type=text][id$=defaultLabelInput]"), "new label");
        selenium.waitForPageToLoad();
        assertEquals(selenium.getAttribute(inputValue), "new label", "Default label should change");

        selenium.type(pjq("input[type=text][id$=defaultLabelInput]"), "");
        selenium.waitForPageToLoad();

        assertTrue(selenium.isElementPresent(select), "Inplace select is not on the page.");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed on the page.");
        if (selenium.isAttributePresent(inputValue)) {
            assertEquals(selenium.getAttribute(inputValue), "", "Default value should be empty");
        }
    }

    @Test
    public void testDisabled() {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=valueInput]"), "Hawaii");
        selenium.waitForPageToLoad();

        assertTrue(selenium.isElementPresent(select), "Inplace input is not on the page.");
        assertTrue(selenium.isElementPresent(button), "Button should be present on the page.");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertEquals(selenium.getValue(input), "Hawaii", "Value of input");
        assertFalse(selenium.isVisible(popup), "Popup should not be visible on the page.");
        assertTrue(selenium.belongsClass(button, "rf-sel-btn-dis"), "Button should contain class rf-sel-btn-dis.");
        AttributeLocator<?> disabledAttr = input.getAttribute(new Attribute("disabled"));
        assertTrue(selenium.isAttributePresent(disabledAttr), "Input should be disabled.");
        assertEquals(selenium.getAttribute(disabledAttr), "true", "Input should be disabled.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9855")
    public void testEnableManualInput() {
        selenium.click(pjq("input[type=radio][name$=enableManualInputInput][value=false]"));
        selenium.waitForPageToLoad();

        AttributeLocator<?> readonlyAttr = input.getAttribute(new Attribute("readonly"));
        assertEquals(selenium.getAttribute(readonlyAttr), "true", "Input should be read-only");

        selenium.mouseDown(button);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        selenium.fireEvent(input, Event.BLUR);
        selenium.fireEvent(input, Event.BLUR); // blur has to be fired twice for Firefox - just Selenium hack
        assertTrue(selenium.belongsClass(options.format(10), "rf-sel-sel"));

        waitGui.failWith("Bean was not updated").until(textEquals.locator(output).text("Hawaii"));
    }

    @Test
    public void testImmediate() {
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        selenium.mouseDown(button);
        selenium.mouseUp(button);
        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: -> Hawaii");
    }

    @Test
    public void testItemClass() {
        final String value = "metamer-ftest-class";
        selenium.type(pjq("input[type=text][id$=itemClassInput]"), value);
        selenium.waitForPageToLoad();

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.belongsClass(options.format(i), value),
                "Select option " + selenium.getText(options.format(i)) + " does not contain class " + value);
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9735")
    public void testListClass() {
        testStyleClass(popup, listClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9737")
    public void testListHeight() {
        selenium.type(pjq("input[type=text][id$=listHeightInput]"), "300px");
        selenium.waitForPageToLoad();

        String height = selenium.getStyle(jq("div.rf-sel-lst-scrl"), CssProperty.HEIGHT);
        assertEquals(height, "300px", "Height of list did not change");

        selenium.type(pjq("input[type=text][id$=listHeightInput]"), "");
        selenium.waitForPageToLoad();

        selenium.mouseDown(button);
        selenium.mouseUp(button);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        assertEquals(selenium.getElementHeight(jq("div.rf-sel-lst-scrl")), 100, "Height of list did not change");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9737")
    public void testListWidth() {
        selenium.type(pjq("input[type=text][id$=listWidthInput]"), "300px");
        selenium.waitForPageToLoad();

        selenium.mouseUp(button);
        String width = selenium.getStyle(jq("div.rf-sel-lst-scrl"), CssProperty.WIDTH);
        assertEquals(width, "300px", "Width of list did not change");

        selenium.type(pjq("input[type=text][id$=listWidthInput]"), "");
        selenium.waitForPageToLoad();

        selenium.mouseUp(button);
        width = selenium.getStyle(jq("div.rf-sel-lst-scrl"), CssProperty.WIDTH);
        assertEquals(width, "200px", "Width of list did not change");
    }

    @Test
    public void testMaxListHeight() {
        selenium.type(pjq("input[type=text][id$=maxListHeightInput]"), "300px");
        selenium.waitForPageToLoad();

        String height = selenium.getStyle(jq("div.rf-sel-lst-scrl"), new CssProperty("max-height"));
        assertEquals(height, "300px", "Height of list did not change");

        selenium.type(pjq("input[type=text][id$=maxListHeightInput]"), "");
        selenium.waitForPageToLoad();

        selenium.mouseDown(button);
        selenium.mouseUp(button);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        assertEquals(selenium.getElementHeight(jq("div.rf-sel-lst-scrl")), 100, "Height of list did not change");
    }

    @Test
    public void testMinListHeight() {
        selenium.type(pjq("input[type=text][id$=minListHeightInput]"), "300px");
        selenium.waitForPageToLoad();

        String height = selenium.getStyle(jq("div.rf-sel-lst-scrl"), new CssProperty("min-height"));
        assertEquals(height, "300px", "Height of list did not change");

        selenium.type(pjq("input[type=text][id$=minListHeightInput]"), "");
        selenium.waitForPageToLoad();

        selenium.mouseDown(button);
        selenium.mouseUp(button);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        assertEquals(selenium.getElementHeight(jq("div.rf-sel-lst-scrl")), 100, "Height of list did not change");
    }

    @Test
    public void testOnblur() {
        testFireEvent(Event.BLUR, input);
    }

    @Test
    public void testOnchange() {
        selenium.type(pjq("input[type=text][id$=onchangeInput]"), "metamerEvents += \"change \"");
        selenium.waitForPageToLoad();

        selenium.click(button);
        selenium.click(options.format(10));
        selenium.fireEvent(input, Event.BLUR);

        waitGui.failWith("Attribute onchange does not work correctly").until(new EventFiredCondition(Event.CHANGE));
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, input);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, input);
    }

    @Test
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, input);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, input);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, input);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, input);
    }

    @Test
    public void testOnlistclick() {
        testFireEvent(Event.CLICK, popup, "listclick");
    }

    @Test
    public void testOnlistdblclick() {
        testFireEvent(Event.DBLCLICK, popup, "listdblclick");
    }

    @Test
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, popup, "listkeydown");
    }

    @Test
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, popup, "listkeypress");
    }

    @Test
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, popup, "listkeyup");
    }

    @Test
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, popup, "listmousedown");
    }

    @Test
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, popup, "listmousemove");
    }

    @Test
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, popup, "listmouseout");
    }

    @Test
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, popup, "listmouseover");
    }

    @Test
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, popup, "listmouseup");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, input);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, input);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, input);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, input);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, input);
    }

    @Test
    public void testOnselectitem() {
        selenium.type(pjq("input[type=text][id$=onselectitemInput]"), "metamerEvents += \"selectitem \"");
        selenium.waitForPageToLoad();

        selenium.click(button);
        selenium.click(options.format(10));

        waitGui.failWith("Attribute onchange does not work correctly").until(
            new EventFiredCondition(new Event("selectitem")));
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(select), "Component should not be rendered when rendered=false.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11320")
    public void testSelectFirst() {
        selenium.click(pjq("input[type=radio][name$=selectFirstInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.focus(input);
        selenium.typeKeys(input, "a");
        selenium.fireEvent(input, Event.KEYDOWN);
        selenium.fireEvent(input, Event.KEYPRESS); // keydown works in Chrome and keypress in Firefox

        waitFor(3000);
        assertEquals(selenium.getCount(jq("div.rf-sel-opt")), 4, "Count of filtered options ('a')");

        String[] selectOptions = { "Alabama", "Alaska", "Arizona", "Arkansas" };
        for (int i = 0; i < 4; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
            assertEquals(selenium.getText(options.format(i)), selectOptions[i], "Select option nr. " + i);
        }

        assertTrue(selenium.belongsClass(options.format(0), "rf-sel-sel"), "First filtered option should be selected");
    }

    @Test
    public void testSelectItemClass() {
        selenium.type(pjq("input[type=text][id$=selectItemClassInput]"), "metamer-ftest-class");
        selenium.waitForPageToLoad();

        selenium.focus(input);
        selenium.click(input);
        selenium.keyPressNative(40); // arrow down
        selenium.keyPressNative(40); // arrow down
        waitModel.withDelay(true).failWith("Popup did not show").until(elementVisible.locator(popup));

        assertTrue(selenium.belongsClass(options.format(0), "metamer-ftest-class"),
            "Selected item does not contain defined class.");
        for (int i = 1; i < 50; i++) {
            assertFalse(selenium.belongsClass(options.format(i), "metamer-ftest-class"), "Not selected item " + i
                + " should not contain defined class.");
        }
    }

    @Test
    public void testShowButton() {
        selenium.click(pjq("input[type=radio][name$=showButtonInput][value=false]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        if (selenium.isElementPresent(button)) {
            assertFalse(selenium.isVisible(button), "Button should not be visible.");
        }

        selenium.focus(input);
        selenium.click(input);
        selenium.keyPressNative(40); // arrow down
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
        }

        String[] selectOptions = { "Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota" };
        for (int i = 0; i < 50; i += 10) {
            assertEquals(selenium.getText(options.format(i)), selectOptions[i / 10], "Select option nr. " + i);
        }

        // FIXME
        // for (int i = 0; i < 11; i++) {
        // selenium.keyPressNative(40); // arrow down
        // }
        // selenium.keyDown(input, "\\13"); // enter
        // guardXhr(selenium).fireEvent(input, Event.BLUR);
        // assertTrue(selenium.belongsClass(options.format(10), "rf-sel-sel"));
        //
        // waitGui.failWith("Bean was not updated").until(textEquals.locator(output).text("Hawaii"));
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9663")
    public void testShowButtonClick() {
        selenium.click(pjq("input[type=radio][name$=showButtonInput][value=false]"));
        selenium.waitForPageToLoad();

        if (selenium.isElementPresent(button)) {
            assertFalse(selenium.isVisible(button), "Button should not be visible.");
        }

        selenium.mouseDown(input);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
        }

        String[] selectOptions = { "Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota" };
        for (int i = 0; i < 50; i += 10) {
            assertEquals(selenium.getText(options.format(i)), selectOptions[i / 10], "Select option nr. " + i);
        }

        selenium.click(options.format(10));
        selenium.fireEvent(input, Event.BLUR);
        guardXhr(selenium).fireEvent(input, Event.BLUR); // some Selenium issue
        assertTrue(selenium.belongsClass(options.format(10), "rf-sel-sel"));

        waitGui.failWith("Bean was not updated").until(textEquals.locator(output).text("Hawaii"));
    }

    @Test
    public void testValue() {
        selenium.type(pjq("input[type=text][id$=valueInput]"), "North Carolina");
        selenium.waitForPageToLoad();

        assertEquals(selenium.getValue(input), "North Carolina", "Input should contain selected value.");
    }
}
