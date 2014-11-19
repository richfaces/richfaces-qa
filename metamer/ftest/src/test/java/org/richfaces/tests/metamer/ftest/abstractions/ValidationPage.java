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
package org.richfaces.tests.metamer.ftest.abstractions;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class ValidationPage extends MetamerPage {

    public static final String JS_COMPLETED_STATE_STRING = "completed";
    public static final String JS_STATE_VARIABLE = "document.valuesSettingState";

    @FindBy(id = "setCorrectValuesButton")
    private WebElement setCorrectValuesButtonElement;
    @FindBy(id = "setWrongValuesButton")
    private WebElement setWrongValuesButtonElement;

    @FindBy(css = "span[id$='jsr-303-inBean-msg'] span[class='rf-msg-det']")
    private WebElement jsr303InBeanMsgElement;
    @FindBy(css = "span[id$='jsr-303-inAtt-msg'] span[class='rf-msg-det']")
    private WebElement jsr303InAttMsgElement;
    @FindBy(css = "span[id$='jsr-303-inBundle-msg'] span[class='rf-msg-det']")
    private WebElement jsr303InBundleMsgElement;

    @FindBy(css = "span[id$='csv-inBean-msg'] span[class='rf-msg-det']")
    private WebElement csvInBeanMsgElement;
    @FindBy(css = "span[id$='csv-inAtt-msg'] span[class='rf-msg-det']")
    private WebElement csvInAttMsgElement;
    @FindBy(css = "span[id$='csv-inBundle-msg'] span[class='rf-msg-det']")
    private WebElement csvInBundleMsgElement;

    @FindBy(css = "span[id$='jsf-inAtt-msg'] span[class='rf-msg-det']")
    private WebElement jsfInAttMsgElement;
    @FindBy(css = "span[id$='jsf-inBundle-msg'] span[class='rf-msg-det']")
    private WebElement jsfInBundleMsgElement;
    @FindBy(css = "input[id$=hButton]")

    private WebElement jsfSubmitBtnElement;
    @FindBy(css = "input[id$=a4jButton]")
    private WebElement rfSubmitBtnElement;

    @FindBy(css = "input[id$=activateButton]")
    private WebElement activateButtonElement;
    @FindBy(css = "input[id$=deactivateButton]")
    private WebElement deactivateButtonElement;

    @FindBy(css = "span[id$=currRB]")
    private WebElement currentRBElement;

    /**
     * @return the activateButtonElement
     */
    public WebElement getActivateButtonElement() {
        return activateButtonElement;
    }

    /**
     * @return the csvInAttMsgElement
     */
    public WebElement getCsvInAttMsgElement() {
        return csvInAttMsgElement;
    }

    /**
     * @return the csvInBeanMsgElement
     */
    public WebElement getCsvInBeanMsgElement() {
        return csvInBeanMsgElement;
    }

    /**
     * @return the csvInBundleMsgElement
     */
    public WebElement getCsvInBundleMsgElement() {
        return csvInBundleMsgElement;
    }

    /**
     * @return the currentRBElement
     */
    public WebElement getCurrentRBElement() {
        return currentRBElement;
    }

    /**
     * @return the deactivateButtonElement
     */
    public WebElement getDeactivateButtonElement() {
        return deactivateButtonElement;
    }

    /**
     * @return text of validation message for component jsf-inAtt (component
     * using jsf validator with message set in attribute of component)
     */
    public String getJSFInAttMSG() {
        return this.getJsfInAttMsgElement().getText();
    }

    /**
     * @return the jsfInAttMsgElement
     */
    public WebElement getJsfInAttMsgElement() {
        return jsfInAttMsgElement;
    }

    /**
     * @return the jsfInBundleMsgElement
     */
    public WebElement getJsfInBundleMsgElement() {
        return jsfInBundleMsgElement;
    }

    /**
     * @return the jsfSubmitBtnElement
     */
    public WebElement getJsfSubmitBtnElement() {
        return jsfSubmitBtnElement;
    }

    /**
     * @return the jsr303InAttMsgElement
     */
    public WebElement getJsr303InAttMsgElement() {
        return jsr303InAttMsgElement;
    }

    /**
     * @return the jsr303InBeanMsgElement
     */
    public WebElement getJsr303InBeanMsgElement() {
        return jsr303InBeanMsgElement;
    }

    /**
     * @return the jsr303InBundleMsgElement
     */
    public WebElement getJsr303InBundleMsgElement() {
        return jsr303InBundleMsgElement;
    }

    /**
     * @return the rfSubmitBtnElement
     */
    public WebElement getRfSubmitBtnElement() {
        return rfSubmitBtnElement;
    }

    /**
     * @return the setCorrectValuesButtonElement
     */
    public WebElement getSetCorrectValuesButtonElement() {
        return setCorrectValuesButtonElement;
    }

    /**
     * @return the setWrongValuesButtonElement
     */
    public WebElement getSetWrongValuesButtonElement() {
        return setWrongValuesButtonElement;
    }

    /**
     * Sets correct values to all inputs on page with waiting for all inputs
     * are set and submits a form with h:commandButton.
     */
    public void setCorrectValuesAndSubmitJSF() {
        getSetCorrectValuesButtonElement().click();
        waitForSetting();
        submitHTTP();
    }

    /**
     * Sets correct values to all inputs on page with waiting for all inputs
     * are set and submits a form with a4j:commandButton.
     */
    public void setCorrectValuesAndSubmitRF() {
        getSetCorrectValuesButtonElement().click();
        waitForSetting();
        submitAjax();
    }

    /**
     * Sets wrong values to all inputs on page with waiting for all inputs
     * are set and submits a form with h:commandButton.
     */
    public void setWrongValuesAndSubmitJSF() {
        getSetWrongValuesButtonElement().click();
        waitForSetting();
        submitHTTP();
    }

    /**
     * Sets wrong values to all inputs on page with waiting for all inputs
     * are set and submits a form with a4j:commandButton.
     */
    public void setWrongValuesAndSubmitRF() {
        getSetWrongValuesButtonElement().click();
        waitForSetting();
        submitAjax();
    }

    private void submitAjax() {
        waitRequest(getRfSubmitBtnElement(), WaitRequestType.XHR).click();
    }

    private void submitHTTP() {
        waitRequest(getJsfSubmitBtnElement(), WaitRequestType.HTTP).click();
    }

    /**
     * Waits for setting of all inputs. Executes JavaScript demanding a
     * variable with state of setting.
     */
    private void waitForSetting() {
        expectedReturnJS(JS_STATE_VARIABLE, JS_COMPLETED_STATE_STRING);
    }

    /**
     * Sets wrong values to all inputs in page via JavaScript enhanced
     * button.
     */
    public void setWrongValues() {
        getSetWrongValuesButtonElement().click();
    }

    /**
     * Checks if some error messages are displayed on page.
     *
     * @return false if some error message is there, else true
     */
    public boolean noErrorMessagesDisplayed() {
        return !(new WebElementConditionFactory(csvInAttMsgElement).isVisible().apply(driver)
            || new WebElementConditionFactory(csvInBeanMsgElement).isVisible().apply(driver)
            || new WebElementConditionFactory(csvInBundleMsgElement).isVisible().apply(driver)
            || new WebElementConditionFactory(jsfInAttMsgElement).isVisible().apply(driver)
            || new WebElementConditionFactory(jsfInBundleMsgElement).isVisible().apply(driver)
            || new WebElementConditionFactory(jsr303InAttMsgElement).isVisible().apply(driver)
            || new WebElementConditionFactory(jsr303InBeanMsgElement).isVisible().apply(driver)
            || new WebElementConditionFactory(jsr303InBundleMsgElement).isVisible().apply(driver));
    }

    /**
     * Activates custom validation messages
     */
    public void activateCustomMessages() {
        submitHTTP();
        setCorrectValuesAndSubmitJSF();
        waitRequest(getActivateButtonElement(), WaitRequestType.XHR).click();
        Graphene.waitAjax().until().element(getCurrentRBElement()).text().equalTo("Current message resource bundle: CustomErrorMessages.");
    }

    /**
     * Deactivates custom validation messages.
     */
    public void deactivateCustomMessages() {
        submitHTTP();
        setCorrectValuesAndSubmitJSF();
        waitRequest(getDeactivateButtonElement(), WaitRequestType.XHR).click();
        Graphene.waitAjax().until().element(getCurrentRBElement()).text().equalTo("Current message resource bundle: DefaultErrorMessages.");
    }
}
