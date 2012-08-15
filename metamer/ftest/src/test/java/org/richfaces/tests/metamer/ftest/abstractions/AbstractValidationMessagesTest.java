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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractValidationMessagesTest extends AbstractWebDriverTest<AbstractValidationMessagesTest.ValidationPage> {

    private ElementPresent elementPresent = ElementPresent.getInstance();
    protected static final String MSG_ATT = "Custom validator error message from attribute of component.";
    private static final String MSG_BEAN = "Custom validator error message from bean.";
    private static final String MSG_BUNDLE_JSR = "Custom validator error message from bundle (JSR-303).";
    private static final String MSG_BUNDLE_JSF_CUSTOM = "Custom validator error message from bundle (JSF).";
    private static final String MSG_BUNDLE_JSF_DEFAULT = "form:jsf-inBundle: Validation Error: Specified attribute is not between the expected values of 2 and 9.";
    private final String JS_STATE_VARIABLE = "document.valuesSettingState";
    private final String JS_COMPLETED_STATE_STRING = "completed";
    private final String component;

    public AbstractValidationMessagesTest(String component) {
        this.component = component;
    }

    @Override
    protected ValidationPage createPage() {
        return new ValidationPage();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + component + "/validationMessages.xhtml");
    }

    public void testInit() {
        page.setCorrectValuesAndSubmitJSF();
        assertTrue(page.noErrorMessagesDisplayed(), "No error messages should now be on page.");
        page.setCorrectValuesAndSubmitRF();
        assertTrue(page.noErrorMessagesDisplayed(), "No error messages should now be on page.");
    }

    @AfterClass(alwaysRun = true)
    public void setDefault() {
        page.deactivateCustomMessages();
    }

    @BeforeMethod(alwaysRun = true)
    public void resetPage() {
        setDefault();
        //inits document JavaScript variable, which will hold state of setting of all inputs
        executeJS(JS_STATE_VARIABLE + "=''");
    }

    /**
     * Tests correctness of JSR-303 validation messages using h:commandButton
     * for submitting. Tests messages set in attribute of component, in
     * annotations of bean and in message bundle.
     */
    public void testJSR303MessagesJSF() {
        page.setWrongValuesAndSubmitJSF();
        assertEquals(page.jsr303InAttMsg.getText(), MSG_ATT);
        assertEquals(page.jsr303InBeanMsg.getText(), MSG_BEAN);
        assertEquals(page.jsr303InBundleMsg.getText(), MSG_BUNDLE_JSR);
    }

    /**
     * Tests correctness of JSR-303 validation messages using a4j:commandButton
     * for submitting. Tests messages set in attribute of component, in
     * annotations of bean and in message bundle.
     */
    public void testJSR303MessagesRF() {
        page.setWrongValuesAndSubmitRF();
        assertEquals(page.jsr303InAttMsg.getText(), MSG_ATT);
        assertEquals(page.jsr303InBeanMsg.getText(), MSG_BEAN);
        assertEquals(page.jsr303InBundleMsg.getText(), MSG_BUNDLE_JSR);
    }

    /**
     * Tests correctness of CSV validation messages using h:commandButton for
     * submitting. Tests messages set in attribute of component, in annotations
     * of bean and in message bundle.
     */
    public void testCSVMessagesJSF() {
        page.setWrongValuesAndSubmitJSF();
        assertEquals(page.csvInAttMsg.getText(), MSG_ATT);
        assertEquals(page.csvInBeanMsg.getText(), MSG_BEAN);
        assertEquals(page.csvInBundleMsg.getText(), MSG_BUNDLE_JSR);
    }

    /**
     * Tests correctness of CSV validation messages using a4j:commandButton for
     * submitting. Tests messages set in attribute of component, in annotations
     * of bean and in message bundle.
     */
    public void testCSVMessagesRF() {
        page.setWrongValuesAndSubmitRF();
        assertEquals(page.csvInAttMsg.getText(), MSG_ATT);
        assertEquals(page.csvInBeanMsg.getText(), MSG_BEAN);
        assertEquals(page.csvInBundleMsg.getText(), MSG_BUNDLE_JSR);
    }

    /**
     * Tests correctness of JSF validation messages using h:commandButton for
     * submitting. Tests messages set in attribute of component and in message
     * bundle.
     */
    public void testJSFMessagesJSF() {
        page.setWrongValuesAndSubmitJSF();
        assertEquals(page.jsfInAttMsg.getText(), MSG_ATT);
        assertEquals(page.jsfInBundleMsg.getText(), MSG_BUNDLE_JSF_DEFAULT);
        page.activateCustomMessages();
        page.setWrongValuesAndSubmitJSF();
        assertEquals(page.jsfInAttMsg.getText(), MSG_ATT);
        assertEquals(page.jsfInBundleMsg.getText(), MSG_BUNDLE_JSF_CUSTOM);
    }

    /**
     * Tests correctness of JSF validation messages using a4j:commandButton for
     * submitting. Tests messages set in attribute of component and in message
     * bundle.
     */
    public void testJSFMessagesRF() {
        page.setWrongValuesAndSubmitRF();
        assertEquals(page.jsfInAttMsg.getText(), MSG_ATT);
        assertEquals(page.jsfInBundleMsg.getText(), MSG_BUNDLE_JSF_DEFAULT);
        page.activateCustomMessages();
        page.setWrongValuesAndSubmitRF();
        assertEquals(page.jsfInAttMsg.getText(), MSG_ATT);
        assertEquals(page.jsfInBundleMsg.getText(), MSG_BUNDLE_JSF_CUSTOM);
    }

    protected class ValidationPage extends MetamerPage {

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
            waitRequest(jsfSubmitBtn, WaitRequestType.HTTP).click();
        }

        /**
         * Sets correct values to all inputs on page with waiting for all inputs
         * are set and submits a form with a4j:commandButton.
         */
        public void setCorrectValuesAndSubmitRF() {
            setCorrectValuesButton.click();
            waitForSetting();
            waitRequest(rfSubmitBtn, WaitRequestType.XHR).click();
        }

        /**
         * Sets wrong values to all inputs on page with waiting for all inputs
         * are set and submits a form with h:commandButton.
         */
        public void setWrongValuesAndSubmitJSF() {
            setWrongValuesButton.click();
            waitForSetting();
            waitRequest(jsfSubmitBtn, WaitRequestType.HTTP).click();
        }

        /**
         * Sets wrong values to all inputs on page with waiting for all inputs
         * are set and submits a form with a4j:commandButton.
         */
        public void setWrongValuesAndSubmitRF() {
            setWrongValuesButton.click();
            waitForSetting();
            waitRequest(rfSubmitBtn, WaitRequestType.XHR).click();
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
            if (elementPresent.element(csvInAttMsg).apply(driver)
                    || elementPresent.element(csvInBeanMsg).apply(driver)
                    || elementPresent.element(csvInBundleMsg).apply(driver)
                    || elementPresent.element(jsfInAttMsg).apply(driver)
                    || elementPresent.element(jsfInBundleMsg).apply(driver)
                    || elementPresent.element(jsr303InAttMsg).apply(driver)
                    || elementPresent.element(jsr303InBeanMsg).apply(driver)
                    || elementPresent.element(jsr303InBundleMsg).apply(driver)) {
                return false;
            }
            return true;
        }

        /**
         * Activates custom validation messages
         */
        public void activateCustomMessages() {
            setCorrectValuesAndSubmitJSF();
            waitRequest(activateButton, WaitRequestType.XHR).click();
        }

        /**
         * Deactivates custom validation messages.
         */
        public void deactivateCustomMessages() {
            setCorrectValuesAndSubmitJSF();
            waitRequest(deactivateButton, WaitRequestType.XHR).click();
        }
    }
}
