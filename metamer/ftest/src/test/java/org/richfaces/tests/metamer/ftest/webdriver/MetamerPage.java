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
package org.richfaces.tests.metamer.ftest.webdriver;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StringEqualsWrapper;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class MetamerPage {

    protected static final int MINOR_WAIT_TIME = 50;// ms
    protected static final int TRIES = 20;//for guardListSize and expectedReturnJS

    @FindBy(css = "div#phasesPanel li")
    public List<WebElement> phases;
    @FindBy(css = "span[id$=requestTime]")
    public WebElement requestTime;
    @FindBy(css = "span[id$=statusCheckerOutput]")
    public WebElement statusCheckerOutput;
    @FindBy(css = "span[id$=renderChecker]")
    public WebElement renderCheckerOutput;
    @FindBy(css = "[id$=fullPageRefreshImage]")
    public WebElement fullPageRefreshIcon;
    @FindBy(css = "[id$=reRenderAllImage]")
    public WebElement rerenderAllIcon;

    protected ElementPresent elementPresent = ElementPresent.getInstance();
    protected WebDriver driver = GrapheneContext.getProxy();

    public List<String> getPhases() {
        List<String> result = new ArrayList<String>();
        for (WebElement webElement : phases) {
            result.add(webElement.getText());
        }
        return result;
    }

    public boolean checkPhasesContainAllOf(String... s) {
        return new PhasesWrapper(getPhases()).containsAllOf(s);
    }

    public boolean checkPhasesDontContainSomeOf(PhaseId... phase) {
        return new PhasesWrapper(getPhases()).notContainsSomeOf(phase);
    }

    public void assertPhasesContainAllOf(String... s) {
        assertTrue(checkPhasesContainAllOf(s), "Phases {" + getPhases() + "} don't contain some of " + Arrays.asList(s));
    }

    public void assertPhasesDontContainSomeOf(PhaseId... phase) {
        assertTrue(checkPhasesDontContainSomeOf(phase), "Phases {" + getPhases() + "} contain some of " + Arrays.asList(phase));
    }

    ///////////////////////////////////////
    // Helper classes
    ///////////////////////////////////////
    /**
     * Wrapper for Metamer's phases list.
     */
    protected class PhasesWrapper {

        private final List<String> phases;

        public PhasesWrapper(List<String> phases) {
            this.phases = phases;
        }

        /**
         * Checks if the wrapped phases do not contain some of a PhaseIds (JSF
         * phases).
         *
         * @param values PhasesIds that phases should not contain
         * @return false if the wrapped phases contains some PhaseId value
         */
        public boolean notContainsSomeOf(PhaseId... values) {
            if (values == null) {
                throw new IllegalArgumentException("No Phases specified.");
            }
            String[] valuesAsString = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                valuesAsString[i] = values[i].toString();
            }
            for (String value : valuesAsString) {
                if (new StringEqualsWrapper(value).isSimilarToSomeOfThis(phases)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks if the wrapped phases do not contain some of a given values.
         *
         * @param values given values, that the wrapped phases should not
         * contain
         * @return false if the wrapped phases contains some of given values
         */
        public boolean notContainsSomeOf(String... values) {
            if (values == null) {
                throw new IllegalArgumentException("No String specified.");
            }
            for (String value : values) {
                if (new StringEqualsWrapper(value).isSimilarToSomeOfThis(phases)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks if phases contains all of given values.
         *
         * @param values given values, that the wrapped phases should contain
         * @return true if phases contain all of given values
         */
        public boolean containsAllOf(String... values) {
            if (values == null) {
                throw new IllegalArgumentException("No String specified.");
            }
            for (String value : values) {
                if (!new StringEqualsWrapper(value).isSimilarToSomeOfThis(phases)) {
                    return false;
                }
            }
            return true;
        }
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
    public WebElement waitRequest(WebElement element, WaitRequestType waitRequestType) {
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
    public WebElement requestTimeChangesWaiting(WebElement element) {
        return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                new Class[]{ WebElement.class }, new RequestTimeChangesHandler(element));
    }

    /**
     * Method for guarding that Metamer's requestTime not changes. Waits for 2 seconds.
     * @param element element which action should not resolve in Metamer's requestTime change
     * @return guarded element
     */
    public WebElement requestTimeNotChangesWaiting(WebElement element) {
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

    public enum WaitRequestType {
        XHR,
        HTTP,
        NONE;
    }

}
