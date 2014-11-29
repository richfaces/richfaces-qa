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
package org.richfaces.tests.metamer.ftest.extension.tester.events;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.FutureTarget;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.FutureWebElement;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.BasicTestChainImpl;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.MultipleTester;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.TestResourcesProvider;
import org.richfaces.tests.metamer.ftest.extension.tester.events.TriggerSingleEventTestChain.EventTesterBasicConfig;
import org.richfaces.tests.metamer.ftest.extension.tester.events.TriggerSingleEventTestChain.EventTesterOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TriggerSingleEventTestChainImpl extends BasicTestChainImpl<TriggerSingleEventTestChain> implements TriggerSingleEventTestChain, EventTesterOn, EventTesterBasicConfig {

    private static final String BY_CUSTOM_ACTION = "byCustomAction";
    public static final String CLEAN_METAMER_EVENTS = "sessionStorage.setItem(\"metamerEvents\", \"\" );";
    private static final Logger LOG = LoggerFactory.getLogger("TriggerSingleEventTestChain");
    private static final String[] POSSIBLE_EVENT_PREFIXES = { "header", "input", "row", "list", "item", "down", "up", "source",
        "target", "mask" };
    private static final String PREFIX_ON = "on";
    public static final String RETURN_METAMER_EVENTS = "return sessionStorage.getItem(\"metamerEvents\");";
    public static final String TESTED_ATTRIBUTE_VALUE_TEMPLATE = "sessionStorage.setItem(\"metamerEvents\", sessionStorage.getItem(\"metamerEvents\") + \"{0} \");";
    private static final String TEST_METHOD_PREFIX = "teston";

    private String attributeName;

    private final Action checkAction = new Action() {
        private final ObjectWrapper<String> wrappedString = new ObjectWrapper<String>();

        @Override
        public void perform() {
            try {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    Object executedScriptResult;

                    @Override
                    public boolean apply(WebDriver t) {
                        executedScriptResult = getTestResources().getJSExecutor().executeScript(RETURN_METAMER_EVENTS);
                        if (executedScriptResult != null) {
                            wrappedString.setObject(((String) executedScriptResult).trim());
                            LOG.debug(format("This was triggered: <{0}>.", wrappedString.getObject()));
                            return wrappedString.getObject().equals(attributeName);
                        }
                        return Boolean.FALSE;
                    }
                });
            } catch (TimeoutException e) {
                assertEquals(wrappedString.getObject(), attributeName, format("Event {0} does not work.", event));
            }
        }
    };

    private final Action cleanEventsAction = new Action() {

        @Override
        public void perform() {
            getTestResources().getJSExecutor().executeScript(CLEAN_METAMER_EVENTS);
        }
    };
    private Event event;
    private FutureTarget<WebElement> onElement;
    private boolean testByWDOtherwiseJS = Boolean.TRUE;
    private Action triggeringAction;
    private final Action triggeringJSAction = new Action() {

        @Override
        public void perform() {
            new Actions(getTestResources().getWebDriver()).triggerEventByJS(event, onElement.getTarget()).perform();
        }

        @Override
        public String toString() {
            return "byJavaScript";
        }
    };
    private final Action triggeringWDAction = new Action() {

        @Override
        public void perform() {
            new Actions(getTestResources().getWebDriver()).triggerEventByWD(event, onElement.getTarget()).perform();
        }

        @Override
        public String toString() {
            return "byWebDriver";
        }
    };

    public TriggerSingleEventTestChainImpl(TestResourcesProvider provider) {
        super(provider);
    }

    private String getEventNameFromStackTrace() {
        String methodName;
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();// faster than Threat.currentThread().getStackTrace()
        for (StackTraceElement ste : stackTrace) {
            methodName = ste.getMethodName();
            if (methodName.toLowerCase().startsWith(TEST_METHOD_PREFIX)) {
                return methodName.substring(TEST_METHOD_PREFIX.length());
            }
        }
        throw new RuntimeException("Was not able to obtain event name");
    }

    @Override
    public EventTesterBasicConfig onElement(FutureTarget<WebElement> visibleAfterSomeActionElement) {
        onElement = visibleAfterSomeActionElement;
        return this;
    }

    @Override
    public EventTesterBasicConfig onElement(WebElement visibleElement) {
        return onElement(FutureWebElement.of(visibleElement));
    }

    @Override
    public EventTesterBasicConfig onElement(final SearchContext context, final By by) {
        return onElement(FutureWebElement.of(by, context));
    }

    private Event parseEventFromString(String s) {
        String eventName = s.toLowerCase();
        if (eventName.startsWith(PREFIX_ON)) {
            eventName = eventName.substring(PREFIX_ON.length());
        }
        for (String prefix : POSSIBLE_EVENT_PREFIXES) {
            if (eventName.startsWith(prefix)) {
                return new Event(eventName.substring(prefix.length()));
            }
        }
        return new Event(eventName);
    }

    @Override
    public void test() {
        if (testByWDOtherwiseJS) {
            MultipleTester.successOfAtLeastOneTester(
                new TriggerSingleEventTestChainImpl(getTestResources())
                .testEvent(event)
                .onElement(onElement)
                .triggerEventByWD(),
                new TriggerSingleEventTestChainImpl(getTestResources())
                .testEvent(event)
                .onElement(onElement)
                .triggerEventByJS()
            ).test();
        } else {
            setupTriggeringAction().replaceAllActionsByAction(triggeringAction);
            super.test();
        }
    }

    @Override
    public EventTesterOn testEvent(String eventName) {
        String attName = eventName.toLowerCase();
        if (!attName.startsWith(PREFIX_ON)) {
            attName = format("on{0}", attName);
        }
        return testEvent(parseEventFromString(eventName), attName);
    }

    @Override
    public EventTesterOn testEvent(Event e) {
        return testEvent(e, format("on{0}", e.getEventName()));
    }

    @Override
    public EventTesterOn testEvent(Event e, String attributeName) {
        this.event = e;
        this.attributeName = attributeName;
        setupAttributes()
            .setAttribute(attributeName).toValue(format(TESTED_ATTRIBUTE_VALUE_TEMPLATE, attributeName));
        setupActionsBeforeTriggering()
            .addLast(cleanEventsAction);
        setupCheckingAction()
            .addLast(checkAction);
        return this;
    }

    @Override
    public EventTesterOn testEvent(Object event) {
        return testEvent(String.valueOf(event));
    }

    @Override
    public EventTesterOn testEventObtainedFromTestMethodName() {
        return testEvent(getEventNameFromStackTrace());
    }

    @Override
    public String toString() {
        String actionName = (triggeringAction == triggeringJSAction || triggeringAction == triggeringWDAction)
            ? triggeringAction.toString()
            : BY_CUSTOM_ACTION;
        return "TriggerSingleEventTestChainImpl{" + "attributeName=" + attributeName + ", triggeringAction=" + actionName + '}';
    }

    @Override
    public TriggerSingleEventTestChain triggerEventByJS() {
        this.testByWDOtherwiseJS = Boolean.FALSE;
        this.triggeringAction = triggeringJSAction;
        return this;
    }

    @Override
    public TriggerSingleEventTestChain triggerEventByWD() {
        this.testByWDOtherwiseJS = Boolean.FALSE;
        this.triggeringAction = triggeringWDAction;
        return this;
    }

    @Override
    public TriggerSingleEventTestChain triggerEventWithWDIfFailedThenJS() {
        this.testByWDOtherwiseJS = Boolean.TRUE;
        this.triggeringAction = null;
        return this;
    }

    @Override
    public TriggerSingleEventTestChain withCustomAction(Action customAction) {
        this.testByWDOtherwiseJS = Boolean.FALSE;
        this.triggeringAction = customAction;
        return this;
    }

    public static class ObjectWrapper<T> {

        private T object;

        public T getObject() {
            return object;
        }

        public ObjectWrapper<T> setObject(T object) {
            this.object = object;
            return this;
        }
    }
}
