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
package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.inplaceInput.ConfirmOrCancel;
import org.richfaces.fragment.inplaceInput.InplaceComponentState;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceInput/simple.xhtml.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInplaceInputAttributes extends AbstractWebDriverTest {

    private final Attributes<InplaceInputAttributes> inplaceInputAttributes = getAttributes();

    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "span[id$=inplaceInput]")
    private RichFacesInplaceInput inplaceInput;
    @FindBy(css = "span[id$=msg]")
    private RichFacesMessage requiredMessage;

    @Override
    public String getComponentTestPagePath() {
        return "richInplaceInput/simple.xhtml";
    }

    @Test(groups = "smoke")
    @CoversAttributes("activeClass")
    @Templates(value = "plain")
    public void testActiveClass() {
        String testedClass = "metamer-ftest-class";
        inplaceInputAttributes.set(InplaceInputAttributes.activeClass, testedClass);

        assertFalse(inplaceInput.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Inplace input should not have class metamer-ftest-class.");

        ConfirmOrCancel inputCotrolls = inplaceInput.type(" ");
        assertTrue(inplaceInput.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Inplace input should have class metamer-ftest-class.");

        inputCotrolls.cancel();
        assertFalse(inplaceInput.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Inplace input should not have class metamer-ftest-class.");
    }

    @Test(groups = "smoke")
    @CoversAttributes("changedClass")
    @Templates(value = "plain")
    public void testChangedClass() {
        String testedClass = "metamer-ftest-class";
        inplaceInputAttributes.set(InplaceInputAttributes.changedClass, testedClass);

        assertFalse(inplaceInput.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Inplace input should not have class metamer-ftest-class.");

        MetamerPage.waitRequest(inplaceInput.type("s"), WaitRequestType.XHR).confirm();
        assertTrue(inplaceInput.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Inplace input should have class metamer-ftest-class.");

    }

    @Test
    public void testClick() {
        inplaceInput.type(" ");
        assertTrue(inplaceInput.advanced().isInState(InplaceComponentState.ACTIVE), "Input should be active.");

        String testedValue = "new value";
        MetamerPage.waitRequest(inplaceInput.type(testedValue), WaitRequestType.XHR).confirm();

        assertTrue(inplaceInput.advanced().isInState(InplaceComponentState.CHANGED),
            "Input should contain class indicating a change.");
        assertEquals(inplaceInput.advanced().getLabelValue(), testedValue, "Input should contain typed text.");

        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: RichFaces 4 -> " + testedValue);
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
    }

    @Test
    @CoversAttributes("showControls")
    @RegressionTest("https://issues.jboss.org/browse/RF-9872")
    public void testClickCancelButton() {
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);
        Graphene.guardNoRequest(inplaceInput.type("value that will be canceled")).cancelByControlls();
        assertEquals(inplaceInput.advanced().getLabelValue(), "RichFaces 4", "Default value was expected.");
    }

    @Test
    @CoversAttributes("showControls")
    public void testClickOkButton() {
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);
        String testedValue = "value that will be confirmed and changed";
        Graphene.guardAjax(inplaceInput.type(testedValue)).confirm();
        assertEquals(inplaceInput.advanced().getLabelValue(), testedValue);
    }

    @Test
    @CoversAttributes("defaultLabel")
    @Templates("plain")
    public void testDefaultLabel() {
        inplaceInputAttributes.set(InplaceInputAttributes.value, "");
        String defaultLabel = inplaceInputAttributes.get(InplaceInputAttributes.defaultLabel);
        assertEquals(inplaceInput.advanced().getLabelValue(), defaultLabel, "Default label should be in input.");

        defaultLabel = "";
        inplaceInputAttributes.set(InplaceInputAttributes.defaultLabel, defaultLabel);
        assertEquals(inplaceInput.advanced().getLabelValue().trim(), defaultLabel, "Default label should change");
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        inplaceInputAttributes.set(InplaceInputAttributes.disabled, Boolean.TRUE);

        assertTrue(new WebElementConditionFactory(inplaceInput.advanced().getRootElement()).isVisible().apply(driver),
            "Inplace input is not on the page.");
        assertEquals(inplaceInput.advanced().getLabelValue(), "RichFaces 4", "Default label");
        assertFalse(new WebElementConditionFactory(inplaceInput.advanced().getCancelButtonElement()).isVisible().apply(driver),
            "OK button should not be present on the page.");
        assertFalse(new WebElementConditionFactory(inplaceInput.advanced().getCancelButtonElement()).isVisible().apply(driver),
            "Cancel button should not be present on the page.");
    }

    @Test
    @CoversAttributes("disabledClass")
    @Templates(value = "plain")
    public void testDisabledClass() {
        inplaceInputAttributes.set(InplaceInputAttributes.disabled, Boolean.TRUE);
        testStyleClass(inplaceInput.advanced().getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    @CoversAttributes("editEvent")
    public void testEditEvent() {
        inplaceInputAttributes.set(InplaceInputAttributes.editEvent, "dblclick");

        inplaceInput.advanced().setEditByEvent(Event.KEYPRESS);
        try {
            inplaceInput.type(" ");
            fail("The test should throw an exception! The editEvent is wrongly set!");
        } catch (IllegalStateException ex) {
            assertTrue(ex.getMessage().contains("editBy"));
        }

        inplaceInput.advanced().setEditByEvent(Event.DBLCLICK);
        inplaceInput.type(" ");
        assertTrue(inplaceInput.advanced().isInState(InplaceComponentState.ACTIVE));
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        inplaceInputAttributes.set(InplaceInputAttributes.immediate, Boolean.TRUE);
        String value = "new value";
        MetamerPage.waitRequest(inplaceInput.type(value), WaitRequestType.XHR).confirm();

        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: RichFaces 4 -> " + value);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
    }

    @Test
    public void testInit() {
        assertVisible(inplaceInput.advanced().getRootElement(), "Inplace input is not on the page.");
        assertEquals(inplaceInput.advanced().getLabelValue(), "RichFaces 4", "Default label");
        assertEquals(inplaceInput.getTextInput().getStringValue(), "RichFaces 4", "Default value of input");
        assertNotVisible(inplaceInput.advanced().getConfirmButtonElement(), "OK button should not be present on the page.");
        assertNotVisible(inplaceInput.advanced().getCancelButtonElement(), "Cancel button should not be present on the page.");
    }

    @Test
    @CoversAttributes("inputWidth")
    @RegressionTest("http://java.net/jira/browse/JAVASERVERFACES-1805")
    @Templates(value = "plain")
    public void testInputWidth() {
        int tolerance = 10;
        inplaceInputAttributes.set(InplaceInputAttributes.inputWidth, "300px");
        inplaceInput.advanced().switchToEditingState();
        int width = Utils.getLocations(inplaceInput.advanced().getEditInputElement()).getWidth();
        assertEquals(width, 300, tolerance, "Width of input did not change.");

        inplaceInputAttributes.set(InplaceInputAttributes.inputWidth, "");
        inplaceInput.advanced().switchToEditingState();
        width = Utils.getLocations(inplaceInput.advanced().getEditInputElement()).getWidth();
        assertEquals(width, 66, tolerance, "Default width of input should be around 66px.");
    }

    @Test
    @CoversAttributes("onblur")
    @RegressionTest("https://issues.jboss.org/browse/RF-9868")
    public void testOnblur() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onblur, new Action() {
            @Override
            public void perform() {
                inplaceInput.type(" ");
                fireEvent(inplaceInput.advanced().getEditInputElement(), Event.BLUR);
            }
        });
    }

    @Test
    @CoversAttributes("onchange")
    @RegressionTest("https://issues.jboss.org/browse/RF-10044")
    public void testOnchange() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onchange, new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(inplaceInput.type("new value"), WaitRequestType.XHR).confirm();
            }
        });
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onclick,
            new Actions(driver).click(inplaceInput.advanced().getRootElement()).build());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.ondblclick,
            new Actions(driver).doubleClick(inplaceInput.advanced().getRootElement()).build());
    }

    @Test
    @CoversAttributes("onfocus")
    @RegressionTest("https://issues.jboss.org/browse/RF-9868")
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onfocus,
            new Actions(driver).click(inplaceInput.advanced().getRootElement()).build());
    }

    @Test
    @CoversAttributes("oninputclick")
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.CLICK, inplaceInputAttributes,
            InplaceInputAttributes.oninputclick);
    }

    @Test
    @CoversAttributes("oninputdblclick")
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.DBLCLICK, inplaceInputAttributes,
            InplaceInputAttributes.oninputdblclick);
    }

    @Test
    @CoversAttributes("oninputkeydown")
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.KEYDOWN, inplaceInputAttributes,
            InplaceInputAttributes.oninputkeydown);
    }

    @Test
    @CoversAttributes("oninputkeypress")
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.KEYPRESS, inplaceInputAttributes,
            InplaceInputAttributes.oninputkeypress);
    }

    @Test
    @CoversAttributes("oninputkeyup")
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.KEYUP, inplaceInputAttributes,
            InplaceInputAttributes.oninputkeyup);
    }

    @Test
    @CoversAttributes("oninputmousedown")
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEDOWN, inplaceInputAttributes,
            InplaceInputAttributes.oninputmousedown);
    }

    @Test
    @Templates(value = "plain")
    @CoversAttributes("oninputmousemove")
    public void testOninputmousemove() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEMOVE, inplaceInputAttributes,
            InplaceInputAttributes.oninputmousemove);
    }

    @Test
    @CoversAttributes("oninputmouseout")
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEOUT, inplaceInputAttributes,
            InplaceInputAttributes.oninputmouseout);
    }

    @Test
    @CoversAttributes("oninputmouseover")
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEOVER, inplaceInputAttributes,
            InplaceInputAttributes.oninputmouseover);
    }

    @Test
    @CoversAttributes("oninputmouseup")
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEUP, inplaceInputAttributes,
            InplaceInputAttributes.oninputmouseup);
    }

    @Test
    @CoversAttributes("oninputselect")
    @Templates(value = "plain")
    public void testOninputselect() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.SELECT, inplaceInputAttributes,
            InplaceInputAttributes.oninputselect);
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onkeydown);
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onkeypress);
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onkeyup);

    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes,
            InplaceInputAttributes.onmousedown);
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes,
            InplaceInputAttributes.onmousemove);

    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onmouseout);
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes,
            InplaceInputAttributes.onmouseover);
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onmouseup);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        inplaceInputAttributes.set(InplaceInputAttributes.rendered, Boolean.FALSE);
        assertNotVisible(inplaceInput.advanced().getRootElement(), "Component should not be rendered when rendered=false.");
    }

    @Test
    @CoversAttributes("required")
    public void testRequired() {
        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.TRUE);
        Graphene.guardAjax(inplaceInput.type("")).confirm();
        assertTrue(requiredMessage.advanced().isVisible());
        assertEquals(requiredMessage.getDetail(), inplaceInputAttributes.get(InplaceInputAttributes.requiredMessage));

        Graphene.guardAjax(inplaceInput.type("value that will be deleted in next step")).confirm();

        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.FALSE);
        Graphene.guardAjax(inplaceInput.type("")).confirm();
        assertFalse(requiredMessage.advanced().isVisible());
    }

    @Test
    @CoversAttributes("requiredMessage")
    public void testRequiredMessage() {
        String reqMsg = "Another new and completely different required message.";
        attsSetter()
            .setAttribute(InplaceInputAttributes.required).toValue(true)
            .setAttribute(InplaceInputAttributes.requiredMessage).toValue(reqMsg)
            .asSingleAction().perform();
        MetamerPage.waitRequest(inplaceInput.type(""), WaitRequestType.XHR).confirm();
        assertTrue(requiredMessage.advanced().isVisible());
        assertEquals(requiredMessage.getDetail(), reqMsg);
    }

    @Test
    @CoversAttributes("saveOnBlur")
    public void testSaveOnBlur() {
        inplaceInputAttributes.set(InplaceInputAttributes.saveOnBlur, Boolean.FALSE);
        inplaceInput.type("value that will be canceled");
        fireEvent(inplaceInput.advanced().getRootElement(), Event.BLUR);
        assertEquals(inplaceInput.advanced().getLabelValue(), "RichFaces 4", "Value should not change.");
    }

    @Test
    @CoversAttributes("showControls")
    @RegressionTest("https://issues.jboss.org/browse/RF-12609")
    public void testShowControls() {
        // check initial state
        assertNotVisible(inplaceInput.advanced().getCancelButtonElement(), "Cancel button should not be displayed.");
        assertNotVisible(inplaceInput.advanced().getConfirmButtonElement(), "Confirm button should not be displayed.");
        // switch to editing state
        inplaceInput.type(" ");
        assertNotVisible(inplaceInput.advanced().getCancelButtonElement(), "Cancel button should not be displayed.");
        assertNotVisible(inplaceInput.advanced().getConfirmButtonElement(), "Confirm button should not be displayed.");

        // set @showControls=true
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);
        // check initial state, RF-12609
        assertNotVisible(inplaceInput.advanced().getCancelButtonElement(), "Cancel button should not be displayed.");
        assertNotVisible(inplaceInput.advanced().getConfirmButtonElement(), "Confirm button should not be displayed.");

        // switch to editing state
        inplaceInput.type(" ");
        assertVisible(inplaceInput.advanced().getCancelButtonElement(), "Cancel button should be displayed.");
        assertVisible(inplaceInput.advanced().getConfirmButtonElement(), "Confirm button should be displayed.");
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(inplaceInput.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    @CoversAttributes("styleClass")
    public void testStyleClass() {
        testStyleClass(inplaceInput.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("tabindex")
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "plain")
    public void testTabindex() {
        int testedValue = 47;
        inplaceInputAttributes.set(InplaceInputAttributes.tabindex, testedValue);
        assertEquals(Integer.parseInt(inplaceInput.advanced().getEditInputElement().getAttribute("tabindex")), testedValue);
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        htmlAttributeTester().testTitle(inplaceInput.advanced().getRootElement()).test();
    }

    @Test
    @CoversAttributes("value")
    @Templates("plain")
    public void testValue() {
        String testedValue = "new value";
        inplaceInputAttributes.set(InplaceInputAttributes.value, testedValue);

        assertEquals(inplaceInput.advanced().getLabelValue(), testedValue);
        assertEquals(inplaceInput.getTextInput().getStringValue(), testedValue);
    }

    @Test
    @CoversAttributes("valueChangeListener")
    public void testValueChangeListener() {
        String value = "new value";
        guardAjax(inplaceInput.type(value)).confirm();

        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: RichFaces 4 -> " + value);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
    }
}
