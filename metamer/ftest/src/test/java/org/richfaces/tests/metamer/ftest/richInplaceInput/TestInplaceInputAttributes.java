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
package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.inplaceInput.ConfirmOrCancel;
import org.richfaces.fragment.inplaceInput.InplaceComponentState;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
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

    @Page
    private MetamerPage page;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "span[id$=inplaceInput]")
    private RichFacesInplaceInput inplaceInput;
    @FindBy(css = "span[id$=msg]")
    private RichFacesMessage requiredMessage;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceInput/simple.xhtml");
    }

    @Test(groups = "smoke")
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

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: RichFaces 4 -> " + testedValue);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9872")
    public void testClickCancelButton() {
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);
        Graphene.guardNoRequest(inplaceInput.type("value that will be canceled")).cancelByControlls();
        assertEquals(inplaceInput.advanced().getLabelValue(), "RichFaces 4", "Default value was expected.");
    }

    @Test
    public void testClickOkButton() {
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);
        String testedValue = "value that will be confirmed and changed";
        Graphene.guardAjax(inplaceInput.type(testedValue)).confirm();
        assertEquals(inplaceInput.advanced().getLabelValue(), testedValue);
    }

    @Test
    public void testDefaultLabel() {
        inplaceInputAttributes.set(InplaceInputAttributes.value, "");
        String defaultLabel = inplaceInputAttributes.get(InplaceInputAttributes.defaultLabel);
        assertEquals(inplaceInput.advanced().getLabelValue(), defaultLabel, "Default label should be in input.");

        defaultLabel = "";
        inplaceInputAttributes.set(InplaceInputAttributes.defaultLabel, defaultLabel);
        assertEquals(inplaceInput.advanced().getLabelValue().trim(), defaultLabel, "Default label should change");
    }

    @Test
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
    @Templates(value = "plain")
    public void testDisabledClass() {
        inplaceInputAttributes.set(InplaceInputAttributes.disabled, Boolean.TRUE);
        testStyleClass(inplaceInput.advanced().getRootElement());
    }

    @Test
    public void testEditEvent() {
        inplaceInputAttributes.set(InplaceInputAttributes.editEvent, "dblclick");

        inplaceInput.advanced().setupEditByEvent(Event.KEYPRESS);
        try {
            inplaceInput.type(" ");
            fail("The test should throw an exception! The editEvent is wrongly set!");
        } catch (IllegalStateException ex) {
            assertTrue(ex.getMessage().contains("editBy"));
        }

        inplaceInput.advanced().setupEditByEvent(Event.DBLCLICK);
        inplaceInput.type(" ");
        assertTrue(inplaceInput.advanced().isInState(InplaceComponentState.ACTIVE));
    }

    @Test
    public void testImmediate() {
        inplaceInputAttributes.set(InplaceInputAttributes.immediate, Boolean.TRUE);
        String value = "new value";
        MetamerPage.waitRequest(inplaceInput.type(value), WaitRequestType.XHR).confirm();

        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: RichFaces 4 -> " + value);
        page.assertPhases(PhaseId.ANY_PHASE);
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
    @RegressionTest("http://java.net/jira/browse/JAVASERVERFACES-1805")
    public void testInputWidth() {
        inplaceInputAttributes.set(InplaceInputAttributes.inputWidth, "300");
        String width = inplaceInput.advanced().getEditInputElement().getCssValue("width");
        assertEquals(width, "300px", "Width of input did not change.");

        inplaceInputAttributes.set(InplaceInputAttributes.inputWidth, "");

        width = inplaceInput.advanced().getEditInputElement().getCssValue("width");
        Integer widthI = Integer.parseInt(width.substring(0, width.indexOf("px")));
        assertEquals(widthI, 66, 20, "Default width of input was not set.");
    }

    @Test
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
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onclick,
            new Actions(driver).click(inplaceInput.advanced().getRootElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.ondblclick,
            new Actions(driver).doubleClick(inplaceInput.advanced().getRootElement()).build());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9868")
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(inplaceInputAttributes, InplaceInputAttributes.onfocus,
            new Actions(driver).click(inplaceInput.advanced().getRootElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.CLICK, inplaceInputAttributes,
            InplaceInputAttributes.oninputclick);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.DBLCLICK, inplaceInputAttributes,
            InplaceInputAttributes.oninputdblclick);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.KEYDOWN, inplaceInputAttributes,
            InplaceInputAttributes.oninputkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.KEYPRESS, inplaceInputAttributes,
            InplaceInputAttributes.oninputkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.KEYUP, inplaceInputAttributes,
            InplaceInputAttributes.oninputkeyup);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEDOWN, inplaceInputAttributes,
            InplaceInputAttributes.oninputmousedown);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousemove() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEMOVE, inplaceInputAttributes,
            InplaceInputAttributes.oninputmousemove);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEOUT, inplaceInputAttributes,
            InplaceInputAttributes.oninputmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEOVER, inplaceInputAttributes,
            InplaceInputAttributes.oninputmouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.MOUSEUP, inplaceInputAttributes,
            InplaceInputAttributes.oninputmouseup);
    }

    @Test
    @Templates(value = "plain")
    public void testOninputselect() {
        testFireEventWithJS(inplaceInput.advanced().getEditInputElement(), Event.SELECT, inplaceInputAttributes,
            InplaceInputAttributes.oninputselect);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onkeydown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onkeypress);
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onkeyup);

    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes,
            InplaceInputAttributes.onmousedown);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes,
            InplaceInputAttributes.onmousemove);

    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onmouseout);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes,
            InplaceInputAttributes.onmouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(inplaceInput.advanced().getRootElement(), inplaceInputAttributes, InplaceInputAttributes.onmouseup);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        inplaceInputAttributes.set(InplaceInputAttributes.rendered, Boolean.FALSE);
        assertNotVisible(inplaceInput.advanced().getRootElement(), "Component should not be rendered when rendered=false.");
    }

    @Test
    public void testRequired() {
        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.TRUE);
        MetamerPage.waitRequest(inplaceInput.type(""), WaitRequestType.XHR).confirm();
        assertTrue(requiredMessage.advanced().isVisible());
        assertEquals(requiredMessage.getDetail(), inplaceInputAttributes.get(InplaceInputAttributes.requiredMessage));

        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.FALSE);
        MetamerPage.waitRequest(inplaceInput.type(""), WaitRequestType.XHR).confirm();
        assertFalse(requiredMessage.advanced().isVisible());
    }

    @Test
    public void testRequiredMessage() {
        String reqMsg = "Another new and completely different required message.";
        inplaceInputAttributes.set(InplaceInputAttributes.required, Boolean.TRUE);
        inplaceInputAttributes.set(InplaceInputAttributes.requiredMessage, reqMsg);
        MetamerPage.waitRequest(inplaceInput.type(""), WaitRequestType.XHR).confirm();
        assertTrue(requiredMessage.advanced().isVisible());
        assertEquals(requiredMessage.getDetail(), reqMsg);
    }

    @Test
    public void testSaveOnBlur() {
        inplaceInputAttributes.set(InplaceInputAttributes.saveOnBlur, Boolean.FALSE);
        inplaceInput.type("value that will be canceled");
        fireEvent(inplaceInput.advanced().getRootElement(), Event.BLUR);
        assertEquals(inplaceInput.advanced().getLabelValue(), "RichFaces 4", "Value should not change.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12609")
    public void testShowControls() {
        assertNotVisible(inplaceInput.advanced().getCancelButtonElement(), "Cancel button should not be visible.");
        assertNotVisible(inplaceInput.advanced().getConfirmButtonElement(), "Ok button should not be visible.");

        inplaceInputAttributes.set(InplaceInputAttributes.showControls, Boolean.TRUE);

        inplaceInput.type(" ");
        assertVisible(inplaceInput.advanced().getCancelButtonElement(), "Cancel button is not visible.");
        assertVisible(inplaceInput.advanced().getCancelButtonElement(), "Ok button is not visible.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(inplaceInput.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(inplaceInput.advanced().getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "plain")
    public void testTabindex() {
        int testedValue = 47;
        inplaceInputAttributes.set(InplaceInputAttributes.tabindex, testedValue);
        assertEquals(Integer.parseInt(inplaceInput.advanced().getEditInputElement().getAttribute("tabindex")), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(inplaceInput.advanced().getRootElement());
    }

    @Test
    public void testValue() {
        String testedValue = "new value";
        inplaceInputAttributes.set(InplaceInputAttributes.value, testedValue);

        assertEquals(inplaceInput.advanced().getLabelValue(), testedValue);
        assertEquals(inplaceInput.getTextInput().getStringValue(), testedValue);
    }
}
