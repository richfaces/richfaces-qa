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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.inplaceInput.InplaceComponentState;
import org.richfaces.fragment.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for JavaScript API on page faces/components/richInplaceSelect/simple.xhtml.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInplaceSelectJSApi extends AbstractWebDriverTest {

    private static final String HAWAII = "Hawaii";
    private final Attributes<InplaceSelectAttributes> inplaceSelectAttributes = getAttributes();

    @FindBy(id = "cancel")
    private WebElement cancelButton;
    @FindBy(id = "getInput")
    private WebElement getInputButton;
    @FindBy(id = "getLabel")
    private WebElement getLabelButton;
    @FindBy(id = "getValue")
    private WebElement getValueButton;
    @FindBy(css = "body > span.rf-is-lst-cord")
    private WebElement globalPopup;
    @FindBy(id = "hidePopup")
    private WebElement hidePopup;
    @FindBy(css = "span[id$=inplaceSelect]")
    private RichFacesInplaceSelect inplaceSelect;
    @FindBy(id = "isEditState")
    private WebElement isEditStateButton;
    @FindBy(id = "isValueChanged")
    private WebElement isValueChangedButton;
    @FindBy(css = "input[id$=':value']")
    private WebElement output;
    @FindBy(css = "[id$=inplaceSelect] span.rf-is-lst-cord")
    private WebElement popup;
    @FindBy(id = "save")
    private WebElement saveButton;
    @FindBy(id = "setLabel")
    private WebElement setLabelButton;
    @FindBy(id = "setValue")
    private WebElement setValueButton;
    @FindBy(id = "showPopup")
    private WebElement showPopup;

    @Test
    public void cancel() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        String defaultText = inplaceSelect.advanced().getLabelValue();
        inplaceSelect.select(HAWAII);
        Graphene.guardNoRequest(cancelButton).click();
        assertEquals(inplaceSelect.advanced().getLabelValue(), defaultText);
        assertFalse(inplaceSelect.advanced().isInState(InplaceComponentState.CHANGED));
    }

    @Test
    public void getInput() {
        // the button has onclick action, it selects the input and clicks on it
        // so the state of inplace input is set to active
        getInputButton.click();
        inplaceSelect.advanced().waitForPopupToShow().perform();
        inplaceSelect.advanced().isInState(InplaceComponentState.ACTIVE);
    }

    @Test
    public void getLabel() {
        getLabelButton.click();
        assertEquals(getValueFromOutput(), inplaceSelect.advanced().getLabelValue(), "Default value.");
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/simple.xhtml");
    }

    @Test
    public void getValue() {
        inplaceSelect.advanced().setSaveOnSelect(Boolean.TRUE);

        getValueButton.click();
        assertEquals(getValueFromOutput(), inplaceSelect.getTextInput().getStringValue(), "Default value.");
        Graphene.guardAjax(inplaceSelect).select(HAWAII);
        getValueButton.click();
        assertEquals(getValueFromOutput(), inplaceSelect.getTextInput().getStringValue());
    }

    private String getValueFromOutput() {
        return this.output.getAttribute("value");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12853")
    public void hidePopup() {
        inplaceSelect.advanced().switchToEditingState();
        assertVisible(globalPopup, "Popup should be visible.");
        assertNotPresent(popup, "Local popup should not be present.");
        fireEvent(hidePopup, Event.MOUSEOVER);
        assertNotVisible(globalPopup, "Popup should not be visible.");
        assertPresent(popup, "Local popup should be present.");
    }

    @Test
    public void isEditState() {
        fireEvent(isEditStateButton, Event.MOUSEOVER);
        assertEquals(getValueFromOutput(), "false");

        inplaceSelect.advanced().switchToEditingState();
        fireEvent(isEditStateButton, Event.MOUSEOVER);
        assertEquals(getValueFromOutput(), "true");
    }

    @Test
    public void isValueChangedButton() {
        inplaceSelect.advanced().setSaveOnSelect(Boolean.TRUE);

        isValueChangedButton.click();
        assertEquals(getValueFromOutput(), "false");
        Graphene.guardAjax(inplaceSelect).select(HAWAII);
        isValueChangedButton.click();
        assertEquals(getValueFromOutput(), "true");
    }

    @Test
    public void save() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);
        inplaceSelect.select(HAWAII);
        inplaceSelect.advanced().waitForPopupToHide().perform();
        Graphene.guardAjax(saveButton).click();
        assertEquals(inplaceSelect.advanced().getLabelValue(), HAWAII);
        assertTrue(inplaceSelect.advanced().isInState(InplaceComponentState.CHANGED));
    }

    @Test
    public void setLabel() {
        String expected = "Completely different label";
        Graphene.guardNoRequest(setLabelButton).click();
        assertEquals(inplaceSelect.advanced().getLabelValue(), expected);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12853")
    public void setValue() {
        Graphene.guardAjax(setValueButton).click();
        assertEquals(inplaceSelect.advanced().getLabelValue(), HAWAII);
    }

    @Test
    public void showPopup() {
        fireEvent(showPopup, Event.MOUSEOVER);
        assertVisible(globalPopup, "Popup should be visible.");
        assertNotPresent(popup, "Local popup should not be present.");
    }
}
