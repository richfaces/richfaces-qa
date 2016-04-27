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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.inplaceInput.ConfirmOrCancel;
import org.richfaces.fragment.inplaceInput.InplaceComponentState;
import org.richfaces.fragment.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceSelect/simple.xhtml.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class TestInplaceSelectAttributes extends AbstractWebDriverTest {

    private static final int GUARD_TIME = 2000;

    private final Attributes<InplaceSelectAttributes> inplaceSelectAttributes = getAttributes();

    private final By listBy = By.cssSelector("span.rf-is-lst-cord");
    private final By listHeightBy = By.cssSelector("span.rf-is-lst-scrl");
    private final By listWidthBy = By.cssSelector("span.rf-is-lst-pos");

    @FindBy(css = "body > span.rf-is-lst-cord")
    private WebElement globalPopup;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=inplaceSelect] span.rf-is-lst-cord")
    private WebElement popup;
    @FindBy(css = "[id$=inplaceSelect]")
    private RichFacesInplaceSelect select;

    @Override
    public String getComponentTestPagePath() {
        return "richInplaceSelect/simple.xhtml";
    }

    private String getOutputText() {
        return output.getText().trim();
    }

    @BeforeMethod(groups = "smoke")
    public void initFragment() {
        select.advanced().setSaveOnSelect(Boolean.TRUE);
    }

    @Test
    @CoversAttributes("activeClass")
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

        guardAjax(select).select(10);
        assertFalse(select.advanced().getRootElement().getAttribute("class").contains(testedClass),
            "Select should not contain " + testedClass);
    }

    @Test
    @CoversAttributes("changedClass")
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    @Templates(value = "plain")
    public void testChangedClass() {
        String testedClass = "metamer-ftest-class";
        inplaceSelectAttributes.set(InplaceSelectAttributes.changedClass, testedClass);

        assertFalse(new WebElementConditionFactory(select.advanced().getRootElement()).attribute("class").contains(testedClass)
            .apply(driver), "Inplace select should not have class metamer-ftest-class.");

        guardAjax(select).select(10);
        assertTrue(new WebElementConditionFactory(select.advanced().getRootElement()).attribute("class").contains(testedClass)
            .apply(driver), "Inplace select should have class metamer-ftest-class.");
    }

    @Test(groups = { "smoke" })
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

        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test(groups = "smoke")
    @CoversAttributes("showControls")
    @Templates(exclude = { "richAccordion", "richTabPanel" })
    public void testClickCancelButton() {
        attsSetter()
            .setAttribute(InplaceSelectAttributes.showControls).toValue(true)
            .setAttribute(InplaceSelectAttributes.saveOnSelect).toValue(false)
            .asSingleAction().perform();
        select.advanced().setSaveOnSelect(Boolean.FALSE);

        ConfirmOrCancel confirmOrCancel = Graphene.guardNoRequest(select).select(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        Graphene.guardAjax(confirmOrCancel).confirmByControlls();
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
        Graphene.guardNoRequest(select.select(11)).cancelByControlls();
        assertEquals(getOutputText(), "Hawaii", "Output should contain previously selected value.");
    }

    @Test
    @CoversAttributes("showControls")
    @Templates(exclude = { "richAccordion", "richTabPanel" })
    public void testClickOkButton() {
        attsSetter()
            .setAttribute(InplaceSelectAttributes.showControls).toValue(true)
            .setAttribute(InplaceSelectAttributes.saveOnSelect).toValue(false)
            .asSingleAction().perform();
        ConfirmOrCancel confOrCancl = Graphene.guardNoRequest(select).select(10);
        assertEquals(getOutputText(), "", "Output should be empty.");
        Graphene.guardAjax(confOrCancl).confirmByControlls();
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @CoversAttributes("defaultLabel")
    @RegressionTest("https://issues.jboss.org/browse/RF-10739")
    @Templates("plain")
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
    @CoversAttributes("disabled")
    public void testDisabled() {
        attsSetter()
            .setAttribute(InplaceSelectAttributes.disabled).toValue(true)
            .setAttribute(InplaceSelectAttributes.value).toValue("Hawaii")
            .asSingleAction().perform();

        assertPresent(select.advanced().getRootElement(), "Inplace input is not on the page.");
        assertPresent(select.advanced().getLabelInputElement(), "Default label should be present on the page.");
        assertEquals(select.advanced().getLabelValue(), "Hawaii", "Label");
        assertNotPresent(select.advanced().getEditInputElement(), "Input should not be present on the page.");
        assertNotPresent(select.advanced().getConfirmButtonElement(), "OK button should not be present on the page.");
        assertNotPresent(select.advanced().getCancelButtonElement(), "Cancel button should not be present on the page.");
    }

    @Test
    @CoversAttributes("disabledClass")
    @Templates(value = "plain")
    public void testDisabledClass() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.disabled, Boolean.TRUE);
        testStyleClass(select.advanced().getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    @CoversAttributes("editEvent")
    public void testEditEvent() {
        for (Event event : new Event[] { Event.MOUSEUP, Event.DBLCLICK, Event.CLICK }) {
            inplaceSelectAttributes.set(InplaceSelectAttributes.editEvent, event);
            select.advanced().setEditByEvent(event);
            select.advanced().switchToEditingState();
            assertVisible(globalPopup, "Popup should be displayed.");
            Utils.performUniversalBlur(driver);
        }
    }

    @Test
    @CoversAttributes("immediate")
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testImmediate() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.immediate, Boolean.TRUE);

        guardAjax(select).select("Hawaii");

        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: -> Hawaii");
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
    @CoversAttributes("inputWidth")
    @Templates(value = "plain")
    public void testInputWidth() {
        int tolerance = 10;
        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "300px");
        select.advanced().switchToEditingState();
        int width = Utils.getLocations(select.advanced().getEditInputElement()).getWidth();
        assertEquals(width, 300, tolerance, "Width of input did not change.");

        inplaceSelectAttributes.set(InplaceSelectAttributes.inputWidth, "");
        select.advanced().switchToEditingState();
        width = Utils.getLocations(select.advanced().getEditInputElement()).getWidth();
        assertEquals(width, 80, tolerance, "Default width of input should be around 80px.");
    }

    @Test
    @CoversAttributes("itemClass")
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
    @CoversAttributes("listClass")
    @RegressionTest("https://issues.jboss.org/browse/RF-9845")
    @Templates(value = "plain")
    public void testListClass() {
        testStyleClass(driver.findElement(listBy), BasicAttributes.listClass);
    }

    @Test
    @CoversAttributes("listHeight")
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
    @CoversAttributes("listWidth")
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
    @CoversAttributes("onblur")
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testOnblur() {
        testFireEvent("blur", new Action() {
            @Override
            public void perform() {
                select.select(10);
                getMetamerPage().getRequestTimeElement().click();
            }
        });
    }

    @Test
    @CoversAttributes("onchange")
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
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onfocus")
    @RegressionTest("https://issues.jboss.org/browse/RF-9849")
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent("focus", new Action() {
            @Override
            public void perform() {
                select.advanced().getLabelInputElement().click();
            }
        });
    }

    @Test
    @CoversAttributes("oninputclick")
    @Templates(value = "plain")
    public void testOninputclick() {
        testFireEvent(Event.CLICK, select.advanced().getEditInputElement(), "inputclick");
    }

    @Test
    @CoversAttributes("oninputdblclick")
    @Templates(value = "plain")
    public void testOninputdblclick() {
        testFireEvent(Event.DBLCLICK, select.advanced().getEditInputElement(), "inputdblclick");
    }

    @Test
    @CoversAttributes("oninputkeydown")
    @Templates(value = "plain")
    public void testOninputkeydown() {
        testFireEvent(Event.KEYDOWN, select.advanced().getEditInputElement(), "inputkeydown");
    }

    @Test
    @CoversAttributes("oninputkeypress")
    @Templates(value = "plain")
    public void testOninputkeypress() {
        testFireEvent(Event.KEYPRESS, select.advanced().getEditInputElement(), "inputkeypress");
    }

    @Test
    @CoversAttributes("oninputkeyup")
    @Templates(value = "plain")
    public void testOninputkeyup() {
        testFireEvent(Event.KEYUP, select.advanced().getEditInputElement(), "inputkeyup");
    }

    @Test
    @CoversAttributes("oninputmousedown")
    @Templates(value = "plain")
    public void testOninputmousedown() {
        testFireEvent(Event.MOUSEDOWN, select.advanced().getEditInputElement(), "inputmousedown");
    }

    @Test
    @CoversAttributes("oninputmousemove")
    @Templates(value = "plain")
    public void testOninputmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.advanced().getEditInputElement(), "inputmousemove");
    }

    @Test
    @CoversAttributes("oninputmouseout")
    @Templates(value = "plain")
    public void testOninputmouseout() {
        testFireEvent(Event.MOUSEOUT, select.advanced().getEditInputElement(), "inputmouseout");
    }

    @Test
    @CoversAttributes("oninputmouseover")
    @Templates(value = "plain")
    public void testOninputmouseover() {
        testFireEvent(Event.MOUSEOVER, select.advanced().getEditInputElement(), "inputmouseover");
    }

    @Test
    @CoversAttributes("oninputmouseup")
    @Templates(value = "plain")
    public void testOninputmouseup() {
        testFireEvent(Event.MOUSEUP, select.advanced().getEditInputElement(), "inputmouseup");
    }

    @Test
    @CoversAttributes("oninputselect")
    @Templates(value = "plain")
    public void testOninputselect() {
        testFireEvent(Event.SELECT, select.advanced().getEditInputElement(), "inputselect");
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onlistclick")
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
    @CoversAttributes("onlistdblclick")
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
    @Skip
    @CoversAttributes("onlisthide")
    @IssueTracking("https://issues.jboss.org/browse/RF-11768")
    public void testOnlisthide() {
        testFireEvent(inplaceSelectAttributes, InplaceSelectAttributes.onlisthide, new Action() {
            @Override
            public void perform() {
                // select will trigger popup menu and close it after selection
                select.select("Hawaii");
            }
        });
    }

    @Test
    @CoversAttributes("onlistkeydown")
    @Templates(value = "plain")
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, popup, "listkeydown");
    }

    @Test
    @CoversAttributes("onlistkeypress")
    @Templates(value = "plain")
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, popup, "listkeypress");
    }

    @Test
    @CoversAttributes("onlistkeyup")
    @Templates(value = "plain")
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, popup, "listkeyup");
    }

    @Test
    @CoversAttributes("onlistmousedown")
    @Templates(value = "plain")
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, popup, "listmousedown");
    }

    @Test
    @CoversAttributes("onlistmousemove")
    @Templates(value = "plain")
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, popup, "listmousemove");
    }

    @Test
    @CoversAttributes("onlistmouseout")
    @Templates(value = "plain")
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, popup, "listmouseout");
    }

    @Test
    @CoversAttributes("onlistmouseover")
    @Templates(value = "plain")
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, popup, "listmouseover");
    }

    @Test
    @CoversAttributes("onlistmouseup")
    @Templates(value = "plain")
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, popup, "listmouseup");
    }

    @Test
    @Skip
    @CoversAttributes("onlistshow")
    @IssueTracking("https://issues.jboss.org/browse/RF-11768")
    public void testOnlistshow() {
        testFireEvent(inplaceSelectAttributes, InplaceSelectAttributes.onlistshow, new Action() {
            @Override
            public void perform() {
                // clicking the root element of select will trigger the popup window
                select.advanced().getRootElement().click();
                select.advanced().waitForPopupToShow();
            }
        });
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onselectitem")
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
    @CoversAttributes("openOnEdit")
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
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.rendered, Boolean.FALSE);
        assertNotVisible(select, "Component should not be rendered when rendered=false.");
    }

    @Test
    @CoversAttributes({ "saveOnBlur", "saveOnSelect" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10739")
    public void testSaveOnBlurSelectFalseFalse() {
        attsSetter()
            .setAttribute(InplaceSelectAttributes.saveOnSelect).toValue(false)
            .setAttribute(InplaceSelectAttributes.saveOnBlur).toValue(false)
            .asSingleAction().perform();
        select.advanced().setSaveOnSelect(Boolean.FALSE);
        // select
        MetamerPage.requestTimeNotChangesWaiting(select, GUARD_TIME).select(10);// Graphene.guardNoRequest waits too long
        assertEquals(getOutputText(), "", "Output should be empty.");
        // blur
        MetamerPage.requestTimeNotChangesWaiting(getMetamerPage().getBlurButton(), GUARD_TIME).click();// Graphene.guardNoRequest waits too long
        assertEquals(getOutputText(), "", "Output should be empty.");
        // with confirmation
        guardAjax(select.select(10)).confirm();
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @CoversAttributes({ "saveOnBlur", "saveOnSelect" })
    public void testSaveOnBlurSelectFalseTrue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        // select
        guardAjax(select).select(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @CoversAttributes({ "saveOnBlur", "saveOnSelect" })
    public void testSaveOnBlurSelectTrueFalse() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        select.advanced().setSaveOnSelect(Boolean.FALSE);
        // select
        MetamerPage.requestTimeNotChangesWaiting(select, GUARD_TIME).select(10);// Graphene.guardNoRequest waits too long
        assertEquals(getOutputText(), "", "Output should be empty.");
        blur(WaitRequestType.XHR);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @CoversAttributes({ "saveOnBlur", "saveOnSelect" })
    public void testSaveOnBlurSelectTrueTrue() {
        // select
        guardAjax(select).select(10);
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
    }

    @Test
    @CoversAttributes("selectItemClass")
    @RegressionTest("https://issues.jboss.org/browse/RF-9896")
    @Templates(value = "plain")
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
    @CoversAttributes("showControls")
    @RegressionTest("https://issues.jboss.org/browse/RF-12609")
    public void testShowControls() {
        // check initial state
        assertNotVisible(select.advanced().getConfirmButtonElement(), "Confirm button should not be displayed");
        assertNotVisible(select.advanced().getCancelButtonElement(), "Cancel button should not be displayed");
        // switch to editing state
        select.advanced().switchToEditingState();
        assertNotVisible(select.advanced().getConfirmButtonElement(), "Confirm button should not be displayed");
        assertNotVisible(select.advanced().getCancelButtonElement(), "Cancel button should not be displayed");

        // set @showControls=true
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        // check initial state, RF-12609
        assertNotVisible(select.advanced().getConfirmButtonElement(), "Confirm button should not be displayed");
        assertNotVisible(select.advanced().getCancelButtonElement(), "Cancel button should not be displayed");
        // switch to editing state
        select.advanced().switchToEditingState();
        assertVisible(select.advanced().getConfirmButtonElement(), "Confirm button should be displayed");
        assertVisible(select.advanced().getCancelButtonElement(), "Cancel button should be displayed");
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleclass() {
        testStyleClass(select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("tabindex")
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
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(select.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("value")
    @Templates("plain")
    public void testValue() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.value, "North Carolina");
        assertEquals(select.advanced().getLabelValue(), "North Carolina", "Label should contain selected value.");
    }

    @Test
    @CoversAttributes("valueChangeListener")
    public void testValueChangeListener() {
        guardAjax(select).select("Alaska");

        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Alaska");
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
    }
}
