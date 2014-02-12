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
    //
    @FindBy(id = "setCorrectValuesButton")
    WebElement setCorrectValuesButton;
    @FindBy(id = "setWrongValuesButton")
    WebElement setWrongValuesButton;
    @FindBy(css = "span[id$='jsr-303-inBean-msg'] span[class='rf-msg-det']")
    WebElement jsr303InBeanMsg;
    @FindBy(css = "span[id$='jsr-303-inAtt-msg'] span[class='rf-msg-det']")
    WebElement jsr303InAttMsg;
    @FindBy(css = "span[id$='jsr-303-inBundle-msg'] span[class='rf-msg-det']")
    WebElement jsr303InBundleMsg;
    @FindBy(css = "span[id$='csv-inBean-msg'] span[class='rf-msg-det']")
    WebElement csvInBeanMsg;
    @FindBy(css = "span[id$='csv-inAtt-msg'] span[class='rf-msg-det']")
    WebElement csvInAttMsg;
    @FindBy(css = "span[id$='csv-inBundle-msg'] span[class='rf-msg-det']")
    WebElement csvInBundleMsg;
    @FindBy(css = "span[id$='jsf-inAtt-msg'] span[class='rf-msg-det']")
    WebElement jsfInAttMsg;
    @FindBy(css = "span[id$='jsf-inBundle-msg'] span[class='rf-msg-det']")
    WebElement jsfInBundleMsg;
    @FindBy(css = "input[id$=hButton]")
    WebElement jsfSubmitBtn;
    @FindBy(css = "input[id$=a4jButton]")
    WebElement rfSubmitBtn;
    @FindBy(css = "input[id$=activateButton]")
    WebElement activateButton;
    @FindBy(css = "input[id$=deactivateButton]")
    WebElement deactivateButton;
    @FindBy(css = "span[id$=currRB]")
    WebElement currentRB;

    /**
     * @return text of validation message for component jsf-inAtt (component
     * using jsf validator with message set in attribute of component)
     */
    public String getJSFInAttMSG() {
        return this.jsfInAttMsg.getText();
    }

    /**
     * Sets correct values to all inputs on page with waiting for all inputs
     * are set and submits a form with h:commandButton.
     */
    public void setCorrectValuesAndSubmitJSF() {
        setCorrectValuesButton.click();
        waitForSetting();
        submitHTTP();
    }

    /**
     * Sets correct values to all inputs on page with waiting for all inputs
     * are set and submits a form with a4j:commandButton.
     */
    public void setCorrectValuesAndSubmitRF() {
        setCorrectValuesButton.click();
        waitForSetting();
        submitAjax();
    }

    /**
     * Sets wrong values to all inputs on page with waiting for all inputs
     * are set and submits a form with h:commandButton.
     */
    public void setWrongValuesAndSubmitJSF() {
        setWrongValuesButton.click();
        waitForSetting();
        submitHTTP();
    }

    /**
     * Sets wrong values to all inputs on page with waiting for all inputs
     * are set and submits a form with a4j:commandButton.
     */
    public void setWrongValuesAndSubmitRF() {
        setWrongValuesButton.click();
        waitForSetting();
        submitAjax();
    }

    private void submitAjax() {
        waitRequest(rfSubmitBtn, WaitRequestType.XHR).click();
    }

    private void submitHTTP() {
        waitRequest(jsfSubmitBtn, WaitRequestType.HTTP).click();
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
        setWrongValuesButton.click();
    }

    /**
     * Checks if some error messages are displayed on page.
     *
     * @return false if some error message is there, else true
     */
    public boolean noErrorMessagesDisplayed() {
        if (new WebElementConditionFactory(csvInAttMsg).isVisible().apply(driver)
                || new WebElementConditionFactory(csvInBeanMsg).isVisible().apply(driver)
                || new WebElementConditionFactory(csvInBundleMsg).isVisible().apply(driver)
                || new WebElementConditionFactory(jsfInAttMsg).isVisible().apply(driver)
                || new WebElementConditionFactory(jsfInBundleMsg).isVisible().apply(driver)
                || new WebElementConditionFactory(jsr303InAttMsg).isVisible().apply(driver)
                || new WebElementConditionFactory(jsr303InBeanMsg).isVisible().apply(driver)
                || new WebElementConditionFactory(jsr303InBundleMsg).isVisible().apply(driver)) {
            return false;
        }
        return true;
    }

    /**
     * Activates custom validation messages
     */
    public void activateCustomMessages() {
        submitHTTP();
        setCorrectValuesAndSubmitJSF();
        waitRequest(activateButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().until().element(currentRB).text().equalTo("Current message resource bundle: CustomErrorMessages.");
    }

    /**
     * Deactivates custom validation messages.
     */
    public void deactivateCustomMessages() {
        submitHTTP();
        setCorrectValuesAndSubmitJSF();
        waitRequest(deactivateButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().until().element(currentRB).text().equalTo("Current message resource bundle: DefaultErrorMessages.");
    }
}
