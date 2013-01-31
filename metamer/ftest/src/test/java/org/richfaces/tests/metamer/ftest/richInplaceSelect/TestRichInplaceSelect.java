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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.disabledClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.listClass;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.inplaceSelectAttributes;
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
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceSelect/simple.xhtml.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision: 23054 $
 */
public class TestRichInplaceSelect extends AbstractGrapheneTest {

    private JQueryLocator select = pjq("span[id$=inplaceSelect]");
    private JQueryLocator label = pjq("span.rf-is-lbl");
    private JQueryLocator input = pjq("input[id$=inplaceSelectInput]");
    private JQueryLocator popup = jq("span.rf-is-lst-cord");
    private JQueryLocator edit = pjq("span.rf-is-fld-cntr");
    private JQueryLocator options = jq("span.rf-is-opt:eq({0})"); // 00..49
    private JQueryLocator okButton = jq("input.rf-is-btn[id$=Okbtn]");
    private JQueryLocator cancelButton = jq("input.rf-is-btn[id$=Cancelbtn]");
    private JQueryLocator output = pjq("span[id$=output]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(select), "Inplace select is not on the page.");
        assertTrue(selenium.isElementPresent(label), "Default label should be present on the page.");
        assertEquals(selenium.getText(label), "Click here to edit", "Default label");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed on the page.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11227")
    public void testClick() {
        guardNoRequest(selenium).click(select);
        assertFalse(selenium.belongsClass(edit, "rf-is-none"),
                "Edit should not contain class rf-is-none when popup is open.");
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
        }

        String[] selectOptions = {"Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota"};
        for (int i = 0; i < 50; i += 10) {
            assertEquals(selenium.getText(options.format(i)), selectOptions[i / 10], "Select option nr. " + i);
        }

        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        waitGui.failWith("Output did not change.").until(textEquals.locator(output).text("Hawaii"));

        assertTrue(selenium.belongsClass(select, "rf-is-chng"), "New class should be added to inplace select.");
        assertTrue(selenium.belongsClass(edit, "rf-is-none"),
                "Edit should contain class rf-is-none when popup is closed.");

        assertEquals(selenium.getText(label), "Hawaii", "Label should contain selected value.");

        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11227")
    public void testChangedClass() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.changedClass, "metamer-ftest-class");

        selenium.click(select);
        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        waitGui.failWith("Output did not change.").until(textEquals.locator(output).text("Hawaii"));

        JQueryLocator elementWhichHasntThatClass = jq(select.getRawLocator() + ":not(.metamer-ftest-class)");
        assertTrue(selenium.isElementPresent(select));
        assertFalse(selenium.isElementPresent(elementWhichHasntThatClass));
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10739")
    public void testDefaultLabel() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.defaultLabel, "new label");
        assertEquals(selenium.getText(label), "new label", "Default label should change");

        inplaceSelectAttributes.set(InplaceSelectAttributes.defaultLabel, "");
        assertEquals(selenium.getText(label), "", "Default label should change");

        assertTrue(selenium.isElementPresent(select), "Inplace select is not on the page.");
        assertTrue(selenium.isElementPresent(label), "Default label should be present on the page.");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed on the page.");
    }

    @Test
    public void testDisabled() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.disabled, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.value, "Hawaii");
        assertTrue(selenium.isElementPresent(select), "Inplace input is not on the page.");
        assertTrue(selenium.isElementPresent(label), "Default label should be present on the page.");
        assertEquals(selenium.getText(label), "Hawaii", "Label");
        assertFalse(selenium.isElementPresent(input), "Input should not be present on the page.");
        assertFalse(selenium.isElementPresent(okButton), "OK button should not be present on the page.");
        assertFalse(selenium.isElementPresent(cancelButton), "Cancel button should not be present on the page.");
        assertFalse(selenium.isElementPresent(edit), "Edit should not be present on the page.");
    }

    @Test
    public void testDisabledClass() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.disabled, Boolean.TRUE);
        testStyleClass(select, disabledClass);
    }

    @Test
    public void testEditEvent() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.editEvent, "mouseup");
        selenium.mouseDown(select);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
        selenium.mouseUp(select);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11227")
    public void testActiveClass() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.activeClass, "metamer-ftest-class");

        assertFalse(selenium.belongsClass(select, "metamer-ftest-class"),
                "Inplace input should not have class metamer-ftest-class.");

        selenium.click(select);
        assertTrue(selenium.belongsClass(select, "metamer-ftest-class"),
                "Inplace input should have class metamer-ftest-class.");

        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        assertFalse(selenium.belongsClass(select, "metamer-ftest-class"),
                "Inplace input should not have class metamer-ftest-class.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11227")
    public void testImmediate() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.immediate, Boolean.TRUE);

        String reqTime = selenium.getText(time);
        selenium.click(select);
        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: -> Hawaii");
    }

    @Test
    public void testInputWidth() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "300px");

        String width = selenium.getStyle(input, CssProperty.WIDTH);
        assertEquals(width, "300px", "Width of input did not change.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "");

        assertFalse(selenium.isAttributePresent(input.getAttribute(Attribute.STYLE)),
                "Input should not have attribute style.");
    }

    @Test
    public void testItemClass() {
        final String value = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.itemClass, value);

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.belongsClass(options.format(i), value),
                    "Select option " + selenium.getText(options.format(i)) + " does not contain class " + value);
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9845")
    public void testListClass() {
        testStyleClass(popup, listClass);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9647")
    public void testListHeight() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.listHeight, "300px");

        String height = selenium.getStyle(jq("span.rf-is-lst-scrl"), CssProperty.HEIGHT);
        assertEquals(height, "300px", "Height of list did not change correctly.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.listHeight, "");
        // it cannot handle null because of a bug in Mojarra and Myfaces and
        // generates style="height: ; " instead of default value
        height = selenium.getStyle(jq("span.rf-is-lst-scrl"), CssProperty.HEIGHT);
        assertEquals(height, "100px", "Height of list did not change correctly.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9647")
    public void testListWidth() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.listWidth, "300px");

        String width = selenium.getStyle(jq("span.rf-is-lst-pos"), CssProperty.WIDTH);
        assertEquals(width, "300px", "Width of list did not change");

        inplaceSelectAttributes.set(InplaceSelectAttributes.listWidth, "");
        // it cannot handle null because of a bug in Mojarra and Myfaces and
        // generates style="width: ; " instead of default value
        width = selenium.getStyle(jq("span.rf-is-lst-pos"), CssProperty.WIDTH);
        assertEquals(width, "200px", "Width of list did not change");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11227")
    public void testOnblur() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.onblur, "metamerEvents += \"blur \"");

        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);

        waitGui.failWith("Attribute onblur does not work correctly").until(new EventFiredCondition(Event.BLUR));
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11227")
    public void testOnchange() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.onchange, "metamerEvents += \"change \"");

        selenium.click(select);
        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);

        waitGui.failWith("Attribute onchange does not work correctly").until(new EventFiredCondition(Event.CHANGE));
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, select);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, select);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9849")
    public void testOnfocus() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.onfocus, "metamerEvents += \"focus \"");
        selenium.click(select);
        waitGui.failWith("Attribute onfocus does not work correctly").until(new EventFiredCondition(Event.FOCUS));
    }

    @Test
    public void testOninputclick() {
        testFireEvent(Event.CLICK, input, "inputclick");
    }

    @Test
    public void testOninputdblclick() {
        testFireEvent(Event.DBLCLICK, input, "inputdblclick");
    }

    @Test
    public void testOninputkeydown() {
        testFireEvent(Event.KEYDOWN, input, "inputkeydown");
    }

    @Test
    public void testOninputkeypress() {
        testFireEvent(Event.KEYPRESS, input, "inputkeypress");
    }

    @Test
    public void testOninputkeyup() {
        testFireEvent(Event.KEYUP, input, "inputkeyup");
    }

    @Test
    public void testOninputmousedown() {
        testFireEvent(Event.MOUSEDOWN, input, "inputmousedown");
    }

    @Test
    public void testOninputmousemove() {
        testFireEvent(Event.MOUSEMOVE, input, "inputmousemove");
    }

    @Test
    public void testOninputmouseout() {
        testFireEvent(Event.MOUSEOUT, input, "inputmouseout");
    }

    @Test
    public void testOninputmouseover() {
        testFireEvent(Event.MOUSEOVER, input, "inputmouseover");
    }

    @Test
    public void testOninputmouseup() {
        testFireEvent(Event.MOUSEUP, input, "inputmouseup");
    }

    @Test
    public void testOninputselect() {
        testFireEvent(Event.SELECT, input, "inputselect");
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, select);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, select);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, select);
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
        testFireEvent(Event.MOUSEDOWN, select);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, select);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, select);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, select);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, select);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9849")
    public void testOnselectitem() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.onselectitem, "metamerEvents += \"selectitem \"");

        selenium.click(select);
        selenium.click(options.format(10));

        waitGui.failWith("Attribute onselectitem does not work correctly").until(
                new EventFiredCondition(new Event("selectitem")));
    }

    @Test
    public void testOpenOnEdit() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.openOnEdit, Boolean.FALSE);

        selenium.click(select);
        assertFalse(selenium.belongsClass(edit, "rf-is-none"),
                "Edit should not contain class rf-is-none when popup is open.");
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        selenium.click(input);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");
    }

    @Test
    public void testRendered() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.rendered, Boolean.FALSE);

        assertFalse(selenium.isElementPresent(select), "Component should not be rendered when rendered=false.");
    }

    @Test
    public void testSaveOnBlurSelectTrueTrue() {
        selenium.click(select);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
        assertEquals(selenium.getValue(input), "Hawaii", "Input should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectTrueFalse() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);

        selenium.click(select);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        guardXhr(selenium).fireEvent(input, Event.BLUR);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
        assertEquals(selenium.getValue(input), "Hawaii", "Input should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectFalseTrue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);

        selenium.click(select);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
        assertEquals(selenium.getValue(input), "Hawaii", "Input should contain selected value.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10739")
    public void testSaveOnBlurSelectFalseFalse() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);

        selenium.click(select);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        guardNoRequest(selenium).fireEvent(input, Event.BLUR);
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9896")
    public void testSelectItemClass() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.selectItemClass, "metamer-ftest-class");

        selenium.click(select);
        selenium.mouseOver(options.format(0));

        assertTrue(selenium.belongsClass(options.format(0), "metamer-ftest-class"),
                "Selected item does not contain defined class.");
        for (int i = 1; i < 50; i++) {
            assertFalse(selenium.belongsClass(options.format(i), "metamer-ftest-class"), "Not selected item " + i
                    + " should not contain defined class.");
        }
    }

    @Test
    public void testShowControls() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);

        selenium.click(select);
        assertTrue(selenium.isVisible(okButton), "OK button should be visible.");
        assertTrue(selenium.isVisible(cancelButton), "Cancel button should be visible.");
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");
    }

    @Test
    public void testClickOkButton() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);

        selenium.click(select);
        selenium.click(options.format(10));
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        selenium.mouseDown(okButton);

        if (selenium.isElementPresent(popup)) {
            assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
        }
        waitGui.failWith("Label should contain selected value.").until(textEquals.locator(label).text("Hawaii"));
    }

    @Test
    public void testClickCancelButton() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);

        selenium.click(select);
        selenium.click(options.format(10));
        assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");

        selenium.click(cancelButton);
        waitModel.failWith("Default label is not displayed.").until(elementPresent.locator(label));
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
        if (selenium.isElementPresent(popup)) {
            assertFalse(selenium.isVisible(popup), "Popup should not be displayed.");
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    public void testTabindex() {
        AttributeLocator<?> attr = input.getAttribute(new Attribute("tabindex"));

        inplaceSelectAttributes.set(InplaceSelectAttributes.tabindex, 47);

        assertTrue(selenium.getAttribute(attr).contains("47"), "Attribute tabindex should contain \"47\".");
    }

    @Test
    public void testValue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.value, "North Carolina");

        assertTrue(selenium.belongsClass(edit, "rf-is-none"),
                "Edit should contain class rf-is-none when popup is closed.");
        assertEquals(selenium.getText(label), "North Carolina", "Label should contain selected value.");
    }
}
