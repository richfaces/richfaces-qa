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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.faces.event.PhaseId;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StringEqualsWrapper;

import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class MetamerPage {

    protected static final int MINOR_WAIT_TIME = 50;// ms
    protected static final int TRIES = 20;// for guardListSize and expectedReturnJS
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
    private String reqTime;
    private Map<PhaseId, Set<String>> map = new LinkedHashMap<PhaseId, Set<String>>();

    public void assertPhasesContainAllOf(String... s) {
        assertTrue(checkPhasesContainAllOf(s), "Phases {" + getPhases() + "} don't contain some of " + Arrays.asList(s));
    }

    public void assertPhasesDontContainSomeOf(PhaseId... phase) {
        assertTrue(checkPhasesDontContainSomeOf(phase),
            "Phases {" + getPhases() + "} contain some of " + Arrays.asList(phase));
    }

    public boolean checkPhasesContainAllOf(String... s) {
        return new PhasesWrapper(getPhases()).containsAllOf(s);
    }

    public boolean checkPhasesDontContainSomeOf(PhaseId... phase) {
        return new PhasesWrapper(getPhases()).notContainsSomeOf(phase);
    }

    /**
     * Tries to execute JavaScript script for few times with some wait time between tries and expecting a predicted
     * result. Method waits for expected string defined in @expectedValue. Returns single trimmed String with expected
     * value or what it found or null.
     *
     * @param expectedValue
     *            expected return value of javaScript
     * @param script
     *            whole JavaScript that will be executed
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

    public List<String> getPhases() {
        List<String> result = new ArrayList<String>();
        for (WebElement webElement : phases) {
            result.add(webElement.getText());
        }
        return result;
    }

    /**
     * Method for guarding that Metamer's requestTime changes.
     *
     * @param <T>
     *            type of the given target
     * @param target
     *            object to be guarded
     * @return guarded element
     */
    public static <T> T requestTimeChangesWaiting(T target) {
        return requestTimeWaiting(target, new RequestTimeChangesWaitingInterceptor());
    }

    /**
     * Method for guarding that Metamer's requestTime not changes. Waits 1 second.
     *
     * @param <T>
     *            type of the given target
     * @param target
     *            object to be guarded
     * @return guarded element
     */
    public static <T> T requestTimeNotChangesWaiting(T target) {
        return requestTimeWaiting(target, new RequestTimeNotChangesWaitingInterceptor());
    }

    private static <T> T requestTimeWaiting(T target, Interceptor interceptor) {
        GrapheneProxyInstance proxy;
        if (GrapheneProxy.isProxyInstance(target)) {
            proxy = (GrapheneProxyInstance) ((GrapheneProxyInstance) target).copy();
        } else {
            proxy = (GrapheneProxyInstance) GrapheneProxy.getProxyForTarget(target);
        }
        proxy.registerInterceptor(interceptor);
        return (T) proxy;
    }

    /**
     * !All requests depends on Metamer`s requestTime! Generates a waiting proxy. The proxy will wait for expected @waitRequestType
     * which will be launched via interactions with @target and then it waits until Metamer's request time
     * changes(@waitRequestType is HTTP or XHR) or not changes(@waitRequestType is NONE).
     *
     * @param <T>
     *            type of the given target
     * @param target
     *            object to be guarded
     * @param waitRequestType
     *            type of expected request which will be launched
     * @return waiting proxy for target object
     */
    public static <T> T waitRequest(T target, WaitRequestType waitRequestType) {
        switch (waitRequestType) {
            case HTTP:
                return requestTimeChangesWaiting(Graphene.guardHttp(target));
            case XHR:
                return requestTimeChangesWaiting(Graphene.guardXhr(target));
            case NONE:
                return requestTimeNotChangesWaiting(Graphene.guardNoRequest(target));
            default:
                throw new UnsupportedOperationException("Not supported request: " + waitRequestType);
        }
    }

    /**
     * Wait for element to be visible.
     *
     * @param by
     *            locate by
     * @return found element
     */
    public static WebElement waitUntilElementIsVisible(final By by) {
        return new WDWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Waiting method. Waits number of milis defined by @milis
     *
     * @param milis
     */
    public static void waiting(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ignored) {
        }
    }

    // /////////////////////////////////////
    // Helper classes
    // /////////////////////////////////////

    /**
     * Wrapper for Metamer's phases list.
     */
    protected class PhasesWrapper {

        private final List<String> phases;

        public PhasesWrapper(List<String> phases) {
            this.phases = phases;
        }

        /**
         * Checks if the wrapped phases do not contain some of a PhaseIds (JSF phases).
         *
         * @param values
         *            PhasesIds that phases should not contain
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
         * @param values
         *            given values, that the wrapped phases should not contain
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
         * @param values
         *            given values, that the wrapped phases should contain
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

    private static class RequestTimeChangesWaitingInterceptor implements Interceptor {

        protected String time1;
        protected final By REQ_TIME = By.cssSelector("span[id='requestTime']");

        protected String getTime() {
            WebElement el = waitUntilElementIsVisible(REQ_TIME);
            String time = el.getText();
            return time;
        }

        protected void beforeAction() {
            time1 = getTime();
        }

        protected void afterAction() {
            Graphene.waitModel().until(Graphene.element(REQ_TIME).not().textEquals(time1));
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            beforeAction();
            Object o = method.invoke(proxy, args);
            afterAction();
            return o;
        }

        @Override
        public Object intercept(InvocationContext context) throws Throwable {
            return invoke(context.getTarget(), context.getMethod(), context.getArguments());
        }
    }

    private static class RequestTimeNotChangesWaitingInterceptor extends RequestTimeChangesWaitingInterceptor {

        private static final int waitTime = 1000;// ms

        @Override
        protected void afterAction() {
            waiting(waitTime);
            if (!getTime().equals(time1)) {
                throw new RuntimeException("No request expected, but request time has changed.");
            }
        }
    }

    /**
     * WebDriver wait which ignores StaleElementException and NoSuchElementException and is polling every 50 ms.
     */
    public static class WDWait extends WebDriverWait {

        /**
         * WebDriver wait which ignores StaleElementException and NoSuchElementException and polling every 50 ms with
         * max wait time of 5 seconds
         */
        public WDWait() {
            this(5);
        }

        /**
         * WebDriver wait which ignores StaleElementException and NoSuchElementException and polling every 50 ms with
         * max wait time set in attribute
         *
         * @param seconds
         *            max wait time
         */
        public WDWait(int seconds) {
            super(GrapheneContext.getProxy(), seconds);
            ignoring(NoSuchElementException.class);
            ignoring(StaleElementReferenceException.class);
            pollingEvery(50, TimeUnit.MILLISECONDS);
        }
    }

    public enum WaitRequestType {
        XHR, HTTP, NONE;
    }

    /**
     * Asserts that the phases has occurred in last request by the specified list.
     */
    public void assertPhases(PhaseId... expectedPhases) {
        initialize();
        if (ArrayUtils.contains(expectedPhases, PhaseId.ANY_PHASE)) {
            expectedPhases = new LinkedList<PhaseId>(PhaseId.VALUES).subList(1, 7).toArray(new PhaseId[6]);
        }
        PhaseId[] actualPhases = map.keySet().toArray(new PhaseId[map.size()]);
        assertEquals(actualPhases, expectedPhases);
    }

    /**
     * Asserts that in the given phase has occurred the listener or order producer writing the log message to phases
     * list.
     *
     * @param phaseId
     *            the phase where the listener occurred
     * @param message
     *            the part of the message which it should be looked up
     */
    public void assertListener(PhaseId phaseId, String message) {
        initialize();
        Set<String> set = map.get(phaseId);
        if (set != null && set.size() > 0) {
            for (String description : set) {
                if (description.contains(message)) {
                    return;
                }
            }
        }
        throw new AssertionError("The '" + message + "' wasn't found across messages in phase " + phaseId);
    }

    /**
     * Asserts that there is no specified message in phases list.
     *
     * @param message
     *            the part of the message which it should be looked up
     */
    public void assertNoListener(String message) {
        initialize();
        for (Entry<PhaseId, Set<String>> entry : map.entrySet()) {
            PhaseId phaseId = entry.getKey();
            Set<String> descriptions = entry.getValue();

            for (String description : descriptions) {
                if (description.contains(message)) {
                    throw new AssertionError("The '" + message + "' was found across messages in phase " + phaseId);
                }
            }
        }
    }

    /**
     * Asserts that phases contains phases: RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE
     */
    public void assertImmediatePhasesCycle() {
        initialize();
        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
    }

    /**
     * Asserts that phases contains phases: RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, RENDER_RESPONSE
     */
    public void assertBypassUpdatesPhasesCycle() {
        initialize();
        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
    }

    public SeleniumCondition getListenerCondition(final PhaseId phaseId, final String message) {
        return new SeleniumCondition() {
            @Override
            public boolean isTrue() {
                initialize();
                Set<String> set = map.get(phaseId);
                if (set != null && set.size() > 0) {
                    for (String description : set) {
                        if (description.contains(message)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    private void initialize() {
        if (reqTime == null || !reqTime.equals(requestTime.getText())) {

            reqTime = requestTime.getText();
            map.clear();
            Set<String> set = null;

            for (WebElement element : phases) {
                String description = element.getText();

                if (!description.startsWith("*")) {
                    set = new LinkedHashSet<String>();
                    map.put(getPhaseId(description), set);
                } else {
                    set.add(description.substring(2));
                }
            }
        }
    }

    private PhaseId getPhaseId(String phaseIdentifier) {
        for (PhaseId phaseId : PhaseId.VALUES) {
            if (phaseIdentifier.startsWith(phaseId.toString())) {
                return phaseId;
            }
        }
        throw new IllegalStateException("no such phase '" + phaseIdentifier + "'");
    }
}
