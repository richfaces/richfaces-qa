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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inplaceSelectAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.inplaceInput.ConfirmOrCancel;
import org.richfaces.tests.page.fragments.impl.inplaceInput.InplaceComponentState;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceSelect/simple.xhtml.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision: 23054 $
 */
public class TestInplaceSelectAttributes extends AbstractWebDriverTest {

    private By listBy = By.cssSelector("span.rf-is-lst-cord");
    private By listHeightBy = By.cssSelector("span.rf-is-lst-scrl");
    private By listWidthBy = By.cssSelector("span.rf-is-lst-pos");
    //
    @FindBy(css = "[id$=inplaceSelect]")
    private RichFacesInplaceSelect select;
    @FindBy(css = "[id$=inplaceSelect] span.rf-is-lst-cord")
    private WebElement popup;
    @FindBy(css = "body > span.rf-is-lst-cord")
    private WebElement globalPopup;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @Page
    private MetamerPage page;
    @ArquillianResource
    private JavascriptExecutor executor;

    private String getOutputText() {
        return output.getText().trim();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/simple.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    @Templates(value = "plain")
    public void testActiveClass() {
        String testedClass = "metamer-ftest-class";
        assertFalse(select.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Select should not contain " + testedClass);

        inplaceSelectAttributes.set(InplaceSelectAttributes.activeClass, testedClass);
        assertFalse(select.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Select should not contain " + testedClass);

        select.advanced().switchToEditingState();
        assertTrue(select.advanced().getRootElement().getAttribute("class").contains(testedClass), "Select should contain "
            + testedClass);

        guardAjax(select).select("Hawaii");
        assertFalse(select.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Select should not contain " + testedClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    @Templates(value = "plain")
    public void testChangedClass() {
        String testedClass = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.changedClass, testedClass);

        assertFalse(new WebElementConditionFactory(select.advanced().getRootElement()).attribute("class").contains(testedClass).apply(driver),
            "Inplace select should not have class metamer-ftest-class.");

        guardAjax(select).select(10);
        assertTrue(new WebElementConditionFactory(select.advanced().getRootElement()).attribute("class").contains(testedClass).apply(driver),
            "Inplace select should have class metamer-ftest-class.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testClick() {
        select.advanced().switchToEditingState();
        List<WebElement> options = select.advanced().getOptions();

        assertTrue(select.advanced().isInState(InplaceComponentState.ACTIVE), "Select should be active.");
        assertFalse(select.advanced().isInState(InplaceComponentState.CHANGED), "Select should not have changed value.");
        assertVisible(globalPopup, "Popup should be displayed.");

        assertEquals(options.size(), 50, "50 options should be displayed.");

        List<Capital> capitals = Model.unmarshallCapitals();
        for (int i = 0; i < options.size(); i++) {
            assertEquals(options.get(i).getText(), capitals.get(i).getState());
        }

        guardAjax(select).select(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
        assertEquals(select.advanced().getLabelValue(), "Hawaii", "Label should contain selected value.");
        assertTrue(select.advanced().isInState(InplaceComponentState.CHANGED), "Select should have changed value.");
        assertFalse(select.advanced().isInState(InplaceComponentState.ACTIVE), "Select should not be active.");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    public void testClickCancelButton() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);

        ConfirmOrCancel confirmOrCancel = select.select(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        confirmOrCancel.confirmByControlls();
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
        select.select(11).cancelByControlls();
        assertEquals(getOutputText(), "Hawaii", "Output should contain previously selected value.");
    }

    @Test
    public void testClickOkButton() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        ConfirmOrCancel confOrCancl = select.select(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        confOrCancl.confirmByControlls();
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10739")
    public void testDefaultLabel() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.defaultLabel, "new label");
        assertEquals(select.advanced().getLabelValue(), "new label", "Default label should change");

        inplaceSelectAttributes.set(InplaceSelectAttributes.defaultLabel, "");
        assertEquals(select.advanced().getLabelValue().trim(), "", "Default label should change");

        assertPresent(select.advanced().getRootElement(), "Inplace select is not on the page.");
        assertPresent(select.advanced().getEditInputElement(), "Input should be present on the page.");
        assertPresent(select.advanced().getLabelInputElement(), "Default label should be present on the page.");
        assertNotVisible(globalPopup, "Popup should not be displayed on the page.");
    }

    @Test
    public void testDisabled() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.disabled, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.value, "Hawaii");

        assertPresent(select.advanced().getRootElement(), "Inplace input is not on the page.");
        assertPresent(select.advanced().getLabelInputElement(), "Default label should be present on the page.");
        assertEquals(select.advanced().getLabelValue(), "Hawaii", "Label");
        assertNotPresent(select.advanced().getEditInputElement(), "Input should not be present on the page.");
        assertNotPresent(select.advanced().getConfirmButtonElement(), "OK button should not be present on the page.");
        assertNotPresent(select.advanced().getCancelButtonElement(), "Cancel button should not be present on the page.");
    }

    @Test
    @Templates(value = "plain")
    public void testDisabledClass() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.disabled, Boolean.TRUE);
        testStyleClass(select.advanced().getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    public void testEditEvent() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.editEvent, "mouseup");
        try {
            select.advanced().switchToEditingState();
        } catch (TimeoutException e) {// ok
        }
        assertNotVisible(globalPopup, "Popup should not be displayed.");
        select.advanced().setupEditByEvent(Event.MOUSEUP);
        select.advanced().switchToEditingState();
        assertVisible(globalPopup, "Popup should be displayed.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testImmediate() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.immediate, Boolean.TRUE);

        guardAjax(select).select("Hawaii");

        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: -> Hawaii");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertPresent(select.advanced().getRootElement(), "Inplace select is not on the page.");
        assertPresent(select.advanced().getEditInputElement(), "Input should be present on the page.");
        assertPresent(select.advanced().getLabelInputElement(), "Default label should be present on the page.");
        assertEquals(select.advanced().getLabelValue(), "Click here to edit", "Default label");
        assertNotVisible(globalPopup, "Popup should not be displayed on the page.");
    }

    @Test
    public void testInputWidth() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "300px");
        String width = select.advanced().getEditInputElement().getCssValue("width");
        assertEquals(width, "300px", "Width of input did not change.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "");

        assertEquals(select.advanced().getEditInputElement().getAttribute("style"), "",
            "Input should not have attribute style.");
    }

    @Test
    @Templates(value = "plain")
    public void testItemClass() {
        final String value = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.itemClass, value);

        select.advanced().switchToEditingState();
        for (WebElement element : select.advanced().getOptions()) {
            assertTrue(element.getAttribute("class").contains(value), "Select option " + element.getText()
                + " does not contain class " + value);
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9845")
    @Templates(value = "plain")
    public void testListClass() {
        testStyleClass(driver.findElement(listBy), BasicAttributes.listClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9647")
    @Templates(value = "plain")
    public void testListHeight() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.listHeight, "300px");
        String height = select.advanced().getRootElement().findElement(listHeightBy).getCssValue("height");
        assertEquals(height, "300px", "Height of list did not change correctly.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.listHeight, "");
        height = select.advanced().getRootElement().findElement(listHeightBy).getCssValue("height");
        assertEquals(height, "100px", "Height of list did not change correctly.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9647")
    @Templates(value = "plain")
    public void testListWidth() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.listWidth, "300px");

        String width = select.advanced().getRootElement().findElement(listWidthBy).getCssValue("width");
        assertEquals(width, "300px", "Width of list did not change.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.listWidth, "");
        width = select.advanced().getRootElement().findElement(listWidthBy).getCssValue("width");
        assertEquals(width, "200px", "Width of list did not change.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testOnblur() {
        testFireEvent("blur", new Action() {
            @Override
            public void perform() {
                select.select(10);
                page.getRequestTimeElement().click();
            }
        });
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testOnchange() {
        testFireEvent("change", new Action() {
            @Override
            public void perform() {
                guardAjax(select).select(5);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, select.advanced().getRootElement());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9849")
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, select.advanced().getEditInputElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEvent(Event.CLICK, select.advanced().getEditInputElement(), "inputclick");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEvent(Event.DBLCLICK, select.advanced().getEditInputElement(), "inputdblclick");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEvent(Event.KEYDOWN, select.advanced().getEditInputElement(), "inputkeydown");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEvent(Event.KEYPRESS, select.advanced().getEditInputElement(), "inputkeypress");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEvent(Event.KEYUP, select.advanced().getEditInputElement(), "inputkeyup");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEvent(Event.MOUSEDOWN, select.advanced().getEditInputElement(), "inputmousedown");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.advanced().getEditInputElement(), "inputmousemove");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEvent(Event.MOUSEOUT, select.advanced().getEditInputElement(), "inputmouseout");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEvent(Event.MOUSEOVER, select.advanced().getEditInputElement(), "inputmouseover");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEvent(Event.MOUSEUP, select.advanced().getEditInputElement(), "inputmouseup");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputselect() {
        testFireEvent(Event.SELECT, select.advanced().getEditInputElement(), "inputselect");
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistclick() {
        testFireEvent("listclick", new Action() {
            @Override
            public void perform() {
                select.advanced().switchToEditingState();
                new Actions(driver).click(globalPopup).perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistdblclick() {
        testFireEvent("listdblclick", new Action() {
            @Override
            public void perform() {
                select.advanced().switchToEditingState();
                new Actions(driver).doubleClick(globalPopup).perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, popup, "listkeydown");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, popup, "listkeypress");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, popup, "listkeyup");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, popup, "listmousedown");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, popup, "listmousemove");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, popup, "listmouseout");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, popup, "listmouseover");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, popup, "listmouseup");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, select.advanced().getRootElement());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9849")
    public void testOnselectitem() {
        testFireEvent("selectitem", new Action() {
            @Override
            public void perform() {
                guardAjax(select).select(5);
            }
        });
    }

    @Test
    public void testOpenOnEdit() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.openOnEdit, Boolean.FALSE);

        select.advanced().getRootElement().click();
        assertTrue(select.advanced().isInState(InplaceComponentState.ACTIVE), "Inplace select should be active.");
        assertPresent(popup, "Popup should not be displayed.");
        assertNotVisible(globalPopup, "Popup should not be displayed.");

        select.advanced().getEditInputElement().click();
        assertNotPresent(popup, "Popup should be displayed.");
        assertVisible(globalPopup, "Popup should be displayed.");
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.rendered, Boolean.FALSE);
        assertNotPresent(select.advanced().getRootElement(), "Component should not be rendered when rendered=false.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10739")
    public void testSaveOnBlurSelectFalseFalse() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        // select
        select.select(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        // blur
        String requestTime = page.getRequestTimeElement().getText().trim();
        Utils.triggerJQ(executor, "blur", select.advanced().getEditInputElement());
        waiting(2000L);
        assertEquals(page.getRequestTimeElement().getText().trim(), requestTime, "Request time shouldn't change.");
        assertEquals(getOutputText(), "", "Output should be empty.");
        // with confirmation
        guardAjax(select.select(10)).confirm();
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectFalseTrue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        // select
        guardAjax(select).select(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectTrueFalse() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        // select
        select.select(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        // blur
        String requestTime = page.getRequestTimeElement().getText();
        Utils.triggerJQ(executor, "blur", select.advanced().getEditInputElement());
        Graphene.waitAjax().until().element(page.getRequestTimeElement()).text().not().equalTo(requestTime);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectTrueTrue() {
        // select
        guardAjax(select).select(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9896")
    public void testSelectItemClass() {
        String testedStyleClass = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.selectItemClass, testedStyleClass);

        guardAjax(select).select(0);
        select.advanced().switchToEditingState();
        List<WebElement> list = select.advanced().getOptions();

        assertTrue(select.advanced().getSelectedOption().getAttribute("class").contains(testedStyleClass),
            "Selected item should contain class " + testedStyleClass);
        for (int i = 1; i < list.size(); i++) {
            assertFalse(list.get(i).getAttribute("class").contains(testedStyleClass), "Selected item should not contain class "
                + testedStyleClass);
        }
    }

    @Test
    public void testShowControls() {
        select.advanced().switchToEditingState();
        assertFalse(new WebElementConditionFactory(select.advanced().getConfirmButtonElement()).isVisible().apply(driver));

        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        select.advanced().switchToEditingState();
        assertTrue(new WebElementConditionFactory(select.advanced().getConfirmButtonElement()).isVisible().apply(driver));
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(select.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleclass() {
        testStyleClass(select.advanced().getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "plain")
    public void testTabindex() {
        String testedIndex = "47";
        inplaceSelectAttributes.set(InplaceSelectAttributes.tabindex, testedIndex);
        assertEquals(select.advanced().getEditInputElement().getAttribute("tabindex"), testedIndex,
            "Attribute tabindex should contain \"47\".");
        testedIndex = "101";
        inplaceSelectAttributes.set(InplaceSelectAttributes.tabindex, testedIndex);
        assertEquals(select.advanced().getEditInputElement().getAttribute("tabindex"), testedIndex,
            "Attribute tabindex should contain \"47\".");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(select.advanced().getRootElement());
    }

    @Test
    public void testValue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.value, "North Carolina");
        assertEquals(select.advanced().getLabelValue(), "North Carolina", "Label should contain selected value.");
    }
}
