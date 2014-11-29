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

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.FutureTarget;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.BasicTestChain;

/**
 * Test chain for triggering single event in Metamer application. Could be used for testing standard DOM events (like focus,
 * click) or custom events (e.g. ajax events begin, complete). This chain main responsibility is to setup event attribute in
 * Metamer, trigger the event, check whether event was triggered.
 * <br/>
 * Examples:<br/>
 * To trigger 'click' event on 'someElement' using WebDriver's actions, then, only if previsous step failed, try to trigger the
 * event with JavaScript using jQuery.<br/>
 * <code>testEvent(Event.CLICK).onElement(someElement).triggerEventWithWDIfFailedThenJS().test();</code><br/>
 * Or a less verbose variant, since the triggerEventWithWDIfFailedThenJS() is default value:<br/>
 * <code>testEvent(Event.CLICK).onElement(someElement).test();</code><br/>
 * Trigger the 'click' event only with JavaScript:<br/>
 * <code>testEvent(Event.CLICK).onElement(someElement).triggerEventByJS().test();</code><br/>
 * Trigger the 'click' event only with WebDriver's actions:<br/>
 * <code>testEvent(Event.CLICK).onElement(someElement).triggerEventByWD().test();</code><br/>
 * Trigger the 'click' event with custom action:<br/>
 * <code>testEvent(Event.CLICK).withCustomAction(customTriggeringAction).test();</code>
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface TriggerSingleEventTestChain extends BasicTestChain<TriggerSingleEventTestChain> {

    /**
     * Case insensitive event name. Could contain the 'on' prefix. Could be used even for events like 'onrowclick',
     * 'headerclick', 'itemdblclick', etc.
     */
    EventTesterOn testEvent(String eventName);

    /**
     * One of the events from org.richfaces.fragment.common.Event.
     */
    EventTesterOn testEvent(Event e);

    /**
     * Use this if other testEvent methods does not suite you.
     */
    EventTesterOn testEvent(Event e, String attributeName);

    /**
     * The event name will be obtained from the toString method of the given parameter.
     */
    EventTesterOn testEvent(Object event);

    /**
     * Obtain the event name from test method name (from stack trace). The method must follow name conventions: the name must
     * start with 'testOn' and the name must be followed by the event name, e.g. testOnclick, testOheaderndblclick, testOnfocus.
     */
    EventTesterOn testEventObtainedFromTestMethodName();

    public interface EventTesterOn {

        /**
         * Set the element on which the triggering action will be performed.
         */
        EventTesterBasicConfig onElement(FutureTarget<WebElement> visibleAfterSomeActionElement);

        /**
         * Set the element on which the triggering action will be performed.
         */
        EventTesterBasicConfig onElement(WebElement visibleElement);

        /**
         * Set the element on which the triggering action will be performed.
         */
        EventTesterBasicConfig onElement(SearchContext context, By by);

        /**
         * Specify triggering action by yourself.
         */
        BasicTestChain<TriggerSingleEventTestChain> withCustomAction(Action customAction);
    }

    public interface EventTesterBasicConfig extends BasicTestChain<TriggerSingleEventTestChain> {

        /**
         * The triggering action will be performed only with jQuery.
         */
        TriggerSingleEventTestChain triggerEventByJS();

        /**
         * The triggering action will be performed only with WebDriver's actions.
         */
        TriggerSingleEventTestChain triggerEventByWD();

        /**
         * Default option. First try to trigger the tested event with WebDriver's actions, then, only if previous step failed,
         * try to trigger the event with JavaScript using jQuery.
         */
        TriggerSingleEventTestChain triggerEventWithWDIfFailedThenJS();
    }
}
