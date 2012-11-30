/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inplaceInputAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import javax.faces.event.PhaseId;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.EditingState;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.EditingState.FinishEditingBy;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.InplaceInputComponent.OpenBy;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.InplaceInputComponent.State;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.InplaceInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.message.MessageComponentImpl;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceInput/simple.xhtml.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichInplaceInputAttributes extends AbstractWebDriverTest<MetamerPage> {

    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "span[id$=inplaceInput]")
    private InplaceInputComponentImpl inplaceInput;
    @FindBy(css = "span[id$=msg]")
    private MessageComponentImpl requiredMessage;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceInput/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Inplace Input", "Simple");
    }

    @Test
    public void testActiveClass() {
        String testedClass = "metamer-ftest-class";
        inplaceInputAttributes.set(InplaceInputAttributes.activeClass, testedClass);

        assertFalse(Graphene.attribute(inplaceInput.getRoot(), "class")
                .valueContains(testedClass).apply(driver),
                "Inplace input should not have class metamer-ftest-class.");

        EditingState editingState = inplaceInput.editBy(OpenBy.CLICK);
        assertTrue(Graphene.attribute(inplaceInput.getRoot(), "class")
                .valueContains(testedClass).apply(driver),
                "Inplace input should have class metamer-ftest-class.");

        editingState.cancel();

        assertFalse(Graphene.attribute(inplaceInput.getRoot(), "class")
                .valueContains(testedClass).apply(driver),
                "Inplace input should not have class metamer-ftest-class.");
    }

    @Test
    public void testChangedClass() {
        String testedClass = "metamer-ftest-class";
        inplaceInputAttributes.set(InplaceInputAttributes.changedClass, testedClass);

        assertFalse(Graphene.attribute(inplaceInput.getRoot(), "class")
                .valueContains(testedClass).apply(driver),
                "Inplace input should not have class metamer-ftest-class.");

        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK).type("s"),
                WaitRequestType.XHR).confirm();
        assertTrue(Graphene.attribute(inplaceInput.getRoot(), "class")
                .valueContains(testedClass).apply(driver),
                "Inplace input should have class metamer-ftest-class.");

    }

    @Test
    public void testClick() {
        EditingState editingState = MetamerPage.waitRequest(inplaceInput, WaitRequestType.NONE).editBy(OpenBy.CLICK);
        assertTrue(inplaceInput.is(State.active), "Input should be active.");

        String testedValue = "new value";
        MetamerPage.waitRequest(editingState.type(testedValue), WaitRequestType.XHR).confirm();

        assertTrue(inplaceInput.is(State.changed), "Input should contain class indicating a change.");
        assertEquals(inplaceInput.getLabelValue(), testedValue, "Input should contain typed text.");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: RichFaces 4 -> " + testedValue);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9872")
    public void testClickCancelButton() {
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);
        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK)
                .type("value that will be canceled"), WaitRequestType.NONE)
                .cancel(FinishEditingBy.controls);
        assertEquals(inplaceInput.getLabelValue(), "RichFaces 4", "Default value was expected.");
    }

    @Test
    public void testClickOkButton() {
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);
        String testedValue = "value that will be confirmed and changed";
        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK)
                .type(testedValue), WaitRequestType.XHR).confirm(FinishEditingBy.controls);
        assertEquals(inplaceInput.getLabelValue(), testedValue);
    }

    @Test
    public void testDefaultLabel() {
        inplaceInputAttributes.set(InplaceInputAttributes.value, "");
        String defaultLabel = inplaceInputAttributes.get(InplaceInputAttributes.defaultLabel);
        assertEquals(inplaceInput.getLabelValue(), defaultLabel, "Default label should be in input.");

        defaultLabel = "";
        inplaceInputAttributes.set(InplaceInputAttributes.defaultLabel, defaultLabel);
        assertEquals(inplaceInput.getLabelValue().trim(), defaultLabel, "Default label should change");
    }

    @Test
    public void testDisabled() {
        inplaceInputAttributes.set(InplaceInputAttributes.disabled, Boolean.TRUE);

        assertTrue(inplaceInput.isVisible(), "Inplace input is not on the page.");
        assertEquals(inplaceInput.getLabelValue(), "RichFaces 4", "Default label");
        assertFalse(Graphene.element(inplaceInput.getControls()
                .getCancelButtonElement()).isVisible().apply(driver),
                "OK button should not be present on the page.");
        assertFalse(Graphene.element(inplaceInput.getControls()
                .getCancelButtonElement()).isVisible().apply(driver),
                "Cancel button should not be present on the page.");
    }

    @Test
    public void testDisabledClass() {
        inplaceInputAttributes.set(InplaceInputAttributes.disabled, Boolean.TRUE);
        testStyleClass(inplaceInput.getRoot());
    }

    @Test
    public void testEditEvent() {
        inplaceInputAttributes.set(InplaceInputAttributes.editEvent, "mouseup");

        inplaceInput.editBy(OpenBy.CLICK);
        assertFalse(inplaceInput.is(State.active));

        inplaceInput.editBy(OpenBy.MOUSEUP);
        assertTrue(inplaceInput.is(State.active));
    }

    @Test
    public void testImmediate() {
        inplaceInputAttributes.set(InplaceInputAttributes.immediate, Boolean.TRUE);
        String value = "new value";
        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK).type(value),
                WaitRequestType.XHR).confirm();

        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: RichFaces 4 -> " + value);
        page.assertPhases(PhaseId.ANY_PHASE);
    }

    @Test
    public void testInit() {
        assertTrue(inplaceInput.isVisible(), "Inplace input is not on the page.");
        assertEquals(inplaceInput.getLabelValue(), "RichFaces 4", "Default label");
        assertEquals(inplaceInput.getEditValue(), "RichFaces 4", "Default value of input");
        assertTrue(Graphene.element(inplaceInput.getControls().
                getOkButtonElement()).not().isVisible().apply(driver),
                "OK button should not be present on the page.");
        assertTrue(Graphene.element(inplaceInput.getControls().
                getCancelButtonElement()).not().isVisible().apply(driver),
                "Cancel button should not be present on the page.");
    }

    @Test
    @IssueTracking("http://java.net/jira/browse/JAVASERVERFACES-1805")
    public void testInputWidth() {
        inplaceInputAttributes.set(InplaceInputAttributes.inputWidth, "300");
        String width = inplaceInput.getEditInputElement().getCssValue("width");
        assertEquals(width, "300px", "Width of input did not change.");

        inplaceInputAttributes.set(InplaceInputAttributes.inputWidth, "");

        // it cannot handle null because of a bug in Mojarra and Myfaces and
        // generates style="width: ; " instead of default value
        width = inplaceInput.getRoot().getCssValue("style");
        assertTrue(!width.contains("width: ;"), "Default width of input was not set (was " + width + ").");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9868")
    public void testOnblur() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onblur, new Action() {
            @Override
            public void perform() {
                inplaceInput.editBy(OpenBy.CLICK);
                fireEvent(inplaceInput.getEditInputElement(), Event.BLUR);
            }
        });
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10044")
    public void testOnchange() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onchange,
                new Action() {
                    @Override
                    public void perform() {
                        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK)
                                .type("new value"), WaitRequestType.XHR).confirm();
                    }
                });
    }

    @Test
    public void testOnclick() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onclick,
                new Actions(driver).click(inplaceInput.getRoot()).build());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.ondblclick,
                new Actions(driver).doubleClick(inplaceInput.getRoot()).build());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9868")
    public void testOnfocus() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onfocus,
                new Actions(driver).click(inplaceInput.getRoot()).build());
    }

    @Test
    public void testOninputclick() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.CLICK,
                inplaceInputAttributes, InplaceInputAttributes.oninputclick);
    }

    @Test
    public void testOninputdblclick() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.DBLCLICK,
                inplaceInputAttributes, InplaceInputAttributes.oninputdblclick);
    }

    @Test
    public void testOninputkeydown() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.KEYDOWN,
                inplaceInputAttributes, InplaceInputAttributes.oninputkeydown);
    }

    @Test
    public void testOninputkeypress() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.KEYPRESS,
                inplaceInputAttributes, InplaceInputAttributes.oninputkeypress);
    }

    @Test
    public void testOninputkeyup() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.KEYUP,
                inplaceInputAttributes, InplaceInputAttributes.oninputkeyup);
    }

    @Test
    public void testOninputmousedown() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.MOUSEDOWN,
                inplaceInputAttributes, InplaceInputAttributes.oninputmousedown);
    }

    @Test
    public void testOninputmousemove() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.MOUSEMOVE,
                inplaceInputAttributes, InplaceInputAttributes.oninputmousemove);
    }

    @Test
    public void testOninputmouseout() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.MOUSEOUT,
                inplaceInputAttributes, InplaceInputAttributes.oninputmouseout);
    }

    @Test
    public void testOninputmouseover() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.MOUSEOVER,
                inplaceInputAttributes, InplaceInputAttributes.oninputmouseover);
    }

    @Test
    public void testOninputmouseup() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.MOUSEUP,
                inplaceInputAttributes, InplaceInputAttributes.oninputmouseup);
    }

    @Test
    public void testOninputselect() {
        testFireEventWithJS(inplaceInput.getEditInputElement(), Event.SELECT,
                inplaceInputAttributes, InplaceInputAttributes.oninputselect);
    }

    @Test
    public void testOnkeydown() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onkeydown);
    }

    @Test
    public void testOnkeypress() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onkeypress);
    }

    @Test
    public void testOnkeyup() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onkeyup);

    }

    @Test
    public void testOnmousedown() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onmousedown);
    }

    @Test
    public void testOnmousemove() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onmousemove);

    }

    @Test
    public void testOnmouseout() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onmouseover);
    }

    @Test
    public void testOnmouseup() {
        testFireEventWithJS(inplaceInput.getRoot(), inplaceInputAttributes,
                InplaceInputAttributes.onmouseup);
    }

    @Test
    public void testRendered() {
        inplaceInputAttributes.set(InplaceInputAttributes.rendered, Boolean.FALSE);
        assertFalse(inplaceInput.isVisible(), "Component should not be rendered when rendered=false.");
    }

    @Test
    public void testRequired() {
        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.TRUE);
        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK).type(""),
                WaitRequestType.XHR).confirm();
        assertTrue(requiredMessage.isVisible());
        assertEquals(requiredMessage.getDetail(),
                inplaceInputAttributes.get(InplaceInputAttributes.requiredMessage));

        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.FALSE);
        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK).type(""),
                WaitRequestType.XHR).confirm();
        assertFalse(requiredMessage.isVisible());
    }

    @Test
    public void testRequiredMessage() {
        String reqMsg = "Another new and completely different required message.";
        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.TRUE);
        inplaceInputAttributes.set(InplaceInputAttributes.requiredMessage, reqMsg);
        MetamerPage.waitRequest(inplaceInput.editBy(OpenBy.CLICK).type(""),
                WaitRequestType.XHR).confirm();
        assertTrue(requiredMessage.isVisible());
        assertEquals(requiredMessage.getDetail(), reqMsg);
    }

    @Test
    public void testSaveOnBlur() {
        inplaceInputAttributes.set(InplaceInputAttributes.saveOnBlur, Boolean.FALSE);
        inplaceInput.editBy(OpenBy.CLICK).type("value that will be canceled");
        fireEvent(inplaceInput.getRoot(), Event.BLUR);
        assertEquals(inplaceInput.getLabelValue(), "RichFaces 4", "Value should not change.");
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12609")
    public void testShowControls() {
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);

        assertFalse(inplaceInput.getControls().isVisible(), "Controls should not be visible.");
        assertFalse(Graphene.element(inplaceInput.getControls()
                .getCancelButtonElement()).isVisible().apply(driver), "Cancel button should not be visible.");
        assertFalse(Graphene.element(inplaceInput.getControls()
                .getOkButtonElement()).isVisible().apply(driver), "Ok button should not be visible.");

        inplaceInput.editBy(OpenBy.CLICK);
        assertTrue(inplaceInput.getControls().isVisible(), "Controls are not visible.");
        assertTrue(Graphene.element(inplaceInput.getControls()
                .getCancelButtonElement()).isVisible().apply(driver), "Cancel button is not visible.");
        assertTrue(Graphene.element(inplaceInput.getControls()
                .getOkButtonElement()).isVisible().apply(driver), "Ok button is not visible.");
    }

    @Test
    public void testStyle() {
        testStyle(inplaceInput.getRoot());
    }

    @Test
    public void testStyleClass() {
        testStyleClass(inplaceInput.getRoot());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    public void testTabindex() {
        int testedValue = 47;
        inplaceInputAttributes.set(InplaceInputAttributes.tabindex, testedValue);
        assertEquals(Integer.parseInt(inplaceInput.getEditInputElement().getAttribute("tabindex")), testedValue);
    }

    @Test
    public void testTitle() {
        testTitle(inplaceInput.getRoot());
    }

    @Test
    public void testValue() {
        String testedValue = "new value";
        inplaceInputAttributes.set(InplaceInputAttributes.value, testedValue);

        assertEquals(inplaceInput.getLabelValue(), testedValue);
        assertEquals(inplaceInput.getEditValue(), testedValue);
    }
}
