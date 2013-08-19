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
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inplaceSelectAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.faces.event.PhaseId;
<<<<<<< HEAD

import org.jboss.arquillian.ajocado.dom.Event;
=======
>>>>>>> InplaceInput page fragment refactored
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
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
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.InplaceSelectEditingState;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.Option;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.OptionsList;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.EditingState.FinishEditingBy;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.InplaceComponent.OpenBy;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.InplaceComponent.State;
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
        String elem = select.getRootElement().getAttribute("class");
        elem.isEmpty();
        assertFalse(select.getRootElement().getAttribute("class").contains(testedClass), "Select should not contain "
            + testedClass);

        inplaceSelectAttributes.set(InplaceSelectAttributes.activeClass, testedClass);
        assertFalse(select.getRootElement().getAttribute("class").contains(testedClass), "Select should not contain "
            + testedClass);

        select.editBy(OpenBy.CLICK);
        assertTrue(select.getRootElement().getAttribute("class").contains(testedClass), "Select should contain " + testedClass);

        select.editBy(OpenBy.CLICK).changeToValue("Hawaii");
        waitAjax(driver).until().element(driver.findElement(By.xpath("//*[@class='rf-is-lbl']"))).text().equals("Hawaii");
        assertTrue(select.getRootElement().getAttribute("class").contains(testedClass), "Select should not contain "
            + testedClass);

        driver.findElement(By.xpath("//form/input[@name = 'form:hButton']")).click();
        assertFalse(select.getRootElement().getAttribute("class").contains(testedClass), "Select should not contain "
            + testedClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    @Templates(value = "plain")
    public void testChangedClass() {
        String testedClass = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.changedClass, testedClass);

        assertFalse(Graphene.attribute(select.getRootElement(), "class").contains(testedClass).apply(driver),
            "Inplace select should not have class metamer-ftest-class.");

        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValueAtIndex(10);
        assertTrue(Graphene.attribute(select.getRootElement(), "class").contains(testedClass).apply(driver),
            "Inplace select should have class metamer-ftest-class.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testClick() {
        OptionsList options = MetamerPage.waitRequest(select, WaitRequestType.NONE, 1000).editBy(OpenBy.CLICK).getOptions();
        assertTrue(select.is(State.ACTIVE), "Select should be active.");
        assertFalse(select.is(State.CHANGED), "Select should not have changed value.");
        assertVisible(globalPopup, "Popup should be displayed.");

        assertEquals(options.size(), 50, "50 options should be displayed.");

        List<Capital> capitals = Model.unmarshallCapitals();
        for (int i = 0; i < options.size(); i++) {
            assertEquals(options.get(i).getText(), capitals.get(i).getState());
        }

        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValueAtIndex(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
        assertEquals(select.getLabelValue(), "Hawaii", "Label should contain selected value.");
        assertTrue(select.is(State.CHANGED), "Select should have changed value.");
        assertFalse(select.is(State.ACTIVE), "Select should not be active.");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    public void testClickCancelButton() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);

        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.NONE).changeToValueAtIndex(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK).changeToValueAtIndex(10), WaitRequestType.XHR).confirm(
            FinishEditingBy.CONTROLS);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK).changeToValueAtIndex(11), WaitRequestType.NONE).cancel(
            FinishEditingBy.CONTROLS);
        assertEquals(getOutputText(), "Hawaii", "Output should contain previously selected value.");
    }

    @Test
    public void testClickOkButton() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.NONE).changeToValueAtIndex(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK).changeToValueAtIndex(10), WaitRequestType.XHR).confirm(
            FinishEditingBy.CONTROLS);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10739")
    public void testDefaultLabel() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.defaultLabel, "new label");
        assertEquals(select.getLabelValue(), "new label", "Default label should change");

        inplaceSelectAttributes.set(InplaceSelectAttributes.defaultLabel, "");
        assertEquals(select.getLabelValue().trim(), "", "Default label should change");

        assertPresent(select.getRootElement(), "Inplace select is not on the page.");
        assertPresent(select.getEditInputElement(), "Input should be present on the page.");
        assertPresent(select.getLabelInputElement(), "Default label should be present on the page.");
        assertNotVisible(globalPopup, "Popup should not be displayed on the page.");
    }

    @Test
    public void testDisabled() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.disabled, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.value, "Hawaii");

        assertPresent(select.getRootElement(), "Inplace input is not on the page.");
        assertPresent(select.getLabelInputElement(), "Default label should be present on the page.");
        assertEquals(select.getLabelValue(), "Hawaii", "Label");
        assertNotPresent(select.getEditInputElement(), "Input should not be present on the page.");
        assertNotPresent(select.getControls().getOkButtonElement(), "OK button should not be present on the page.");
        assertNotPresent(select.getControls().getCancelButtonElement(), "Cancel button should not be present on the page.");
    }

    @Test
    @Templates(value = "plain")
    public void testDisabledClass() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.disabled, Boolean.TRUE);
        testStyleClass(select.getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    public void testEditEvent() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.editEvent, "mouseup");
        try {
            select.editBy(OpenBy.CLICK);
        } catch (TimeoutException e) {// ok
        }
        assertNotVisible(globalPopup, "Popup should not be displayed.");
        select.editBy(OpenBy.MOUSEUP);
        assertVisible(globalPopup, "Popup should be displayed.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testImmediate() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.immediate, Boolean.TRUE);

        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValue("Hawaii");

        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: -> Hawaii");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertPresent(select.getRootElement(), "Inplace select is not on the page.");
        assertPresent(select.getEditInputElement(), "Input should be present on the page.");
        assertPresent(select.getLabelInputElement(), "Default label should be present on the page.");
        assertEquals(select.getLabelValue(), "Click here to edit", "Default label");
        assertNotVisible(globalPopup, "Popup should not be displayed on the page.");
    }

    @Test
    public void testInputWidth() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "300px");
        String width = select.getEditInputElement().getCssValue("width");
        assertEquals(width, "300px", "Width of input did not change.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "");

        assertEquals(select.getEditInputElement().getAttribute("style"), "", "Input should not have attribute style.");
    }

    @Test
    @Templates(value = "plain")
    public void testItemClass() {
        final String value = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.itemClass, value);

        for (Option o : select.editBy(OpenBy.CLICK).getOptions()) {
            assertTrue(o.getElement().getAttribute("class").contains(value), "Select option " + o + " does not contain class "
                + value);
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
        String height = select.getRootElement().findElement(listHeightBy).getCssValue("height");
        assertEquals(height, "300px", "Height of list did not change correctly.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.listHeight, "");
        height = select.getRootElement().findElement(listHeightBy).getCssValue("height");
        assertEquals(height, "100px", "Height of list did not change correctly.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9647")
    @Templates(value = "plain")
    public void testListWidth() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.listWidth, "300px");

        String width = select.getRootElement().findElement(listWidthBy).getCssValue("width");
        assertEquals(width, "300px", "Width of list did not change.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.listWidth, "");
        width = select.getRootElement().findElement(listWidthBy).getCssValue("width");
        assertEquals(width, "200px", "Width of list did not change.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testOnblur() {
        testFireEvent("blur", new Action() {
            @Override
            public void perform() {
                select.editBy(OpenBy.CLICK).changeToValueAtIndex(10);
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
                MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValueAtIndex(5);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, select.getRootElement());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9849")
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, select.getEditInputElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEvent(Event.CLICK, select.getEditInputElement(), "inputclick");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEvent(Event.DBLCLICK, select.getEditInputElement(), "inputdblclick");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEvent(Event.KEYDOWN, select.getEditInputElement(), "inputkeydown");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEvent(Event.KEYPRESS, select.getEditInputElement(), "inputkeypress");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEvent(Event.KEYUP, select.getEditInputElement(), "inputkeyup");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEvent(Event.MOUSEDOWN, select.getEditInputElement(), "inputmousedown");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.getEditInputElement(), "inputmousemove");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEvent(Event.MOUSEOUT, select.getEditInputElement(), "inputmouseout");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEvent(Event.MOUSEOVER, select.getEditInputElement(), "inputmouseover");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEvent(Event.MOUSEUP, select.getEditInputElement(), "inputmouseup");
    }

    @Test
    @Templates(value = "plain")
    public void testOninputselect() {
        testFireEvent(Event.SELECT, select.getEditInputElement(), "inputselect");
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistclick() {
        testFireEvent("listclick", new Action() {
            @Override
            public void perform() {
                select.editBy(OpenBy.CLICK);
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
                select.editBy(OpenBy.CLICK);
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
        testFireEvent(Event.MOUSEDOWN, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, select.getRootElement());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9849")
    public void testOnselectitem() {
        testFireEvent("selectitem", new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValueAtIndex(5);
            }
        });
    }

    @Test
    public void testOpenOnEdit() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.openOnEdit, Boolean.FALSE);

        select.getRootElement().click();
        assertTrue(select.is(State.ACTIVE), "Inplace select should be active.");
        assertPresent(popup, "Popup should not be displayed.");
        assertNotVisible(globalPopup, "Popup should not be displayed.");

        select.getEditInputElement().click();
        assertNotPresent(popup, "Popup should be displayed.");
        assertVisible(globalPopup, "Popup should be displayed.");
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.rendered, Boolean.FALSE);
        assertNotPresent(select.getRootElement(), "Component should not be rendered when rendered=false.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10739")
    public void testSaveOnBlurSelectFalseFalse() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        // select
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.NONE).changeToValueAtIndex(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        // blur
        String requestTime = page.getRequestTimeElement().getText().trim();
        Utils.triggerJQ(executor, "blur", select.getEditInputElement());
        waiting(2000L);
        assertEquals(page.getRequestTimeElement().getText().trim(), requestTime, "Request time shouldn't change.");
        assertEquals(getOutputText(), "", "Output should be empty.");
        // with confirmation
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK).changeToValueAtIndex(10), WaitRequestType.XHR).confirm();
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectFalseTrue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        // select
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValueAtIndex(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectTrueFalse() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        // select
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.NONE).changeToValueAtIndex(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        // blur
        String requestTime = page.getRequestTimeElement().getText();
        Utils.triggerJQ(executor, "blur", select.getEditInputElement());
        Graphene.waitAjax().until().element(page.getRequestTimeElement()).text().not().equalTo(requestTime);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectTrueTrue() {
        // select
        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValueAtIndex(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9896")
    public void testSelectItemClass() {
        String testedStyleClass = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.selectItemClass, testedStyleClass);

        MetamerPage.waitRequest(select.editBy(OpenBy.CLICK), WaitRequestType.XHR).changeToValueAtIndex(0);
        OptionsList list = select.editBy(OpenBy.CLICK).getOptions();

        assertTrue(list.getSelectedOption().getElement().getAttribute("class").contains(testedStyleClass),
            "Selected item should contain class " + testedStyleClass);
        for (int i = 1; i < list.size(); i++) {
            assertFalse(list.get(i).getElement().getAttribute("class").contains(testedStyleClass),
                "Selected item should not contain class " + testedStyleClass);
        }
    }

    @Test
    public void testShowControls() {
        InplaceSelectEditingState editBy = select.editBy(OpenBy.CLICK);
        assertFalse(editBy.getControls().isVisible());

        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        editBy = select.editBy(OpenBy.CLICK);
        assertTrue(editBy.getControls().isVisible());
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(select.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleclass() {
        testStyleClass(select.getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10980")
    @Templates(value = "plain")
    public void testTabindex() {
        String testedIndex = "47";
        inplaceSelectAttributes.set(InplaceSelectAttributes.tabindex, testedIndex);
        assertEquals(select.getEditInputElement().getAttribute("tabindex"), testedIndex,
            "Attribute tabindex should contain \"47\".");
        testedIndex = "101";
        inplaceSelectAttributes.set(InplaceSelectAttributes.tabindex, testedIndex);
        assertEquals(select.getEditInputElement().getAttribute("tabindex"), testedIndex,
            "Attribute tabindex should contain \"47\".");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(select.getRootElement());
    }

    @Test
    public void testValue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.value, "North Carolina");
        assertEquals(select.getLabelValue(), "North Carolina", "Label should contain selected value.");
    }
}
