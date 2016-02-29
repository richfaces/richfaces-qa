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
package org.richfaces.tests.metamer.ftest.richGraphValidator;

import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.CheckboxInputComponentImpl;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.tests.metamer.ftest.webdriver.utils.MetamerJavascriptUtils;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class GraphValidatorPage {

    @FindBy(css = "input[id$=applyChanges]")
    private WebElement applyChangesBtn;
    @FindBy(css = "input[id$=autocompleteInput]")
    private TextInputComponentImpl autocomplete;
    @FindBy(css = "[id$=selectBooleanCheckbox]")
    private CheckboxInputComponentImpl booleanCheckbox;
    @FindBy(css = "[id$=selectBooleanCheckbox]")
    private WebElement booleanCheckboxRoot;
    @FindBy(css = "span[id$=graphValidatorGlobalMessages]")
    private RichFacesMessages graphValidatorGlobalMessages;
    @FindBy(css = "span[id$=graphValidatorMessages]")
    private RichFacesMessages graphValidatorMessages;
    @FindBy(css = "input[id$=inputSecret]")
    private TextInputComponentImpl inputSecret;
    @FindBy(css = "span[id$=inputSecret]")
    private RichFacesMessage inputSecretMsg;
    @FindBy(css = "input[id$=inputText]")
    private TextInputComponentImpl inputText;
    @FindBy(css = "textarea[id$=inputTextarea]")
    private TextInputComponentImpl inputTextarea;
    @JavaScript
    private MetamerJavascriptUtils jsUtils;
    @FindBy(css = "[id$=graphValidatorPanel]")
    private WebElement panel;
    @FindBy(css = "span[id$=inputNumberSlider]")
    private RichFacesInputNumberSlider slider;
    @FindBy(css = "span[id$=inputNumberSpinner]")
    private RichFacesInputNumberSpinner spinner;

    public WebElement getApplyChangesBtn() {
        jsUtils.scrollToView(applyChangesBtn);
        return applyChangesBtn;
    }

    public TextInputComponentImpl getAutocomplete() {
        jsUtils.scrollToView(autocomplete.advanced().getInputElement());
        return autocomplete;
    }

    public CheckboxInputComponentImpl getBooleanCheckbox() {
        jsUtils.scrollToView(booleanCheckboxRoot);
        return booleanCheckbox;
    }

    public RichFacesMessages getGraphValidatorGlobalMessages() {
        return graphValidatorGlobalMessages;
    }

    public RichFacesMessages getGraphValidatorMessages() {
        return graphValidatorMessages;
    }

    public TextInputComponentImpl getInputSecret() {
        jsUtils.scrollToView(inputSecret.advanced().getInputElement());
        return inputSecret;
    }

    public RichFacesMessage getInputSecretMsg() {
        return inputSecretMsg;
    }

    public TextInputComponentImpl getInputText() {
        jsUtils.scrollToView(inputText.advanced().getInputElement());
        return inputText;
    }

    public TextInputComponentImpl getInputTextarea() {
        jsUtils.scrollToView(inputTextarea.advanced().getInputElement());
        return inputTextarea;
    }

    public WebElement getPanel() {
        return panel;
    }

    public RichFacesInputNumberSlider getSlider() {
        jsUtils.scrollToView(slider.advanced().getRootElement());
        return slider;
    }

    public RichFacesInputNumberSpinner getSpinner() {
        jsUtils.scrollToView(spinner.advanced().getRootElement());
        return spinner;
    }
}
