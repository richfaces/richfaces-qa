/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.RequestTimeChangesHandler;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.WaitRequestType;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.WDWait;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class ValidationPage extends MetamerPage {

    public static final String JS_COMPLETED_STATE_STRING = "completed";
    public static final String JS_STATE_VARIABLE = "document.valuesSettingState";
    protected static final int MINOR_WAIT_TIME = 50;// ms
    protected static final int TRIES = 20;//for guardListSize and expectedReturnJS

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

    private ElementPresent elementPresent = ElementPresent.getInstance();
    private WebDriver driver = GrapheneContext.getProxy();

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

    /**
     * !All requests depends on Metamer`s requestTime!
     * Temporary method before https://issues.jboss.org/browse/ARQGRA-200 is
     * resolved. Generates a waiting proxy, which will wait for page rendering
     * after expected @waitRequestType which will be launched via
     * communicating with @element.
     *
     * @param element WebElement which will launch a request (e.g. with methods
     * click(), submit()...) after invoking it.
     * @param waitRequestType type of expected request which will be launched
     * @return waiting proxy for input element
     */
    protected WebElement waitRequest(WebElement element, WaitRequestType waitRequestType) {
        switch (waitRequestType) {
            case HTTP:
                return requestTimeChangesWaiting(Graphene.guardHttp(element));
            case XHR:
                return requestTimeChangesWaiting(Graphene.guardXhr(element));
            case NONE:
                return requestTimeNotChangesWaiting(Graphene.guardNoRequest(element));
            default:
                throw new UnsupportedOperationException("Not supported request: " + waitRequestType);
        }
    }

    /**
     * Method for guarding that Metamer's requestTime changes.
     * @param element element which action should resolve in Metamer's requestTime change
     * @return guarded element
     */
    protected WebElement requestTimeChangesWaiting(WebElement element) {
        return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                new Class[]{ WebElement.class }, new RequestTimeChangesHandler(element));
    }

    /**
     * Method for guarding that Metamer's requestTime not changes. Waits for 2 seconds.
     * @param element element which action should not resolve in Metamer's requestTime change
     * @return guarded element
     */
    protected WebElement requestTimeNotChangesWaiting(WebElement element) {
        return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                new Class[]{ WebElement.class }, new RequestTimeNotChangesHandler(element, 2));
    }

    /**
     * Waits for change of requestTime in Metamer.
     */
    public class RequestTimeChangesHandler implements InvocationHandler {

        protected final WebElement element;
        protected String time1;
        protected final By REQ_TIME = By.cssSelector("span[id='requestTime']");

        public RequestTimeChangesHandler(WebElement element) {
            this.element = element;
        }

        protected String getTime() {
            WebElement el = waitUntilElementIsVisible(By.cssSelector("span[id='requestTime']"));
            String time = el.getText();
            return time;
        }

        protected void beforeAction() {
            time1 = getTime();
        }

        protected void afterAction() {
            new WDWait().until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return !input.findElement(REQ_TIME).getText().equals(time1);
                }
            });
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            beforeAction();
            Object o = method.invoke(element, args);
            afterAction();
            return o;
        }
    }

    /**
     * Waits number of seconds and checks if requestTime was not changed,
     */
    public class RequestTimeNotChangesHandler extends RequestTimeChangesHandler {

        private final int waitTime;

        public RequestTimeNotChangesHandler(WebElement element, int waitTime) {
            super(element);
            this.waitTime = waitTime;
        }

        @Override
        protected void afterAction() {
            waiting(waitTime);
            if (!getTime().equals(time1)) {
                throw new RuntimeException("No request expected, but request time has changed.");
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            beforeAction();
            Object o = method.invoke(element, args);
            afterAction();
            return o;
        }
    }

    /**
     * Tries to execute JavaScript script for few times with some wait time
     * between tries and expecting a predicted result. Method waits for expected
     * string defined in @expectedValue. Returns single trimmed String with
     * expected value or what it found or null.
     *
     * @param expectedValue expected return value of javaScript
     * @param script whole JavaScript that will be executed
     * @param args
     * @return single and trimmed string or null
     */
    protected String expectedReturnJS(String script, String expectedValue, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String result = null;
        for (int i = 0; i < TRIES; i++) {
            Object executeScript = js.executeScript(script, args);
            if (executeScript != null) {
                result = ((String) js.executeScript(script, args)).trim();
                if (result.equals(expectedValue)) {
                    break;
                }
            }
            waiting(MINOR_WAIT_TIME);
        }
        return result;
    }

    /**
     * Waiting method. Waits number of milis defined by @milis
     *
     * @param milis
     */
    protected static void waiting(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * WebDriver wait which ignores StaleElementException and
     * NoSuchElementException and is polling every 50 ms.
     */
    public class WDWait extends WebDriverWait {

        /**
         * WebDriver wait which ignores StaleElementException and
         * NoSuchElementException and polling every 50 ms with max wait time of
         * 5 seconds
         */
        public WDWait() {
            this(5);
        }

        /**
         * WebDriver wait which ignores StaleElementException and
         * NoSuchElementException and polling every 50 ms with max wait time set
         * in attribute
         *
         * @param seconds max wait time
         */
        public WDWait(int seconds) {
            super(driver, seconds);
            ignoring(NoSuchElementException.class);
            ignoring(StaleElementReferenceException.class);
            pollingEvery(50, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Wait for element to be visible.
     *
     * @param by locate by
     * @return found element
     */
    protected WebElement waitUntilElementIsVisible(final By by) {
        return new WDWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
