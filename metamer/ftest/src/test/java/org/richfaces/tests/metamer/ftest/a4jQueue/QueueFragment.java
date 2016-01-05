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
package org.richfaces.tests.metamer.ftest.a4jQueue;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class QueueFragment {

    private static final String DOM_UPDATES = "domUpdates";
    private static final String EVENT1_COUNT = "event1Count";
    private static final String EVENT2_COUNT = "event2Count";
    private static final int MAXIMUM_WAIT_TIME_IN_SECS = 7;
    private static final String REQUEST_COUNT = "requestCount";
    private static final String SPACE = " ";
    private static final long serialVersionUID = 1L;

    @Drone
    private WebDriver browser;

    private Boolean event2Present = null;
    @FindBy(css = "span[id$=events1]")
    private WebElement events1;
    @FindBy(css = "span[id$=events2]")
    private WebElement events2;
    @FindBy(css = "input[id$=input1]")
    private WebElement input1;
    @FindBy(css = "input[id$=input2]")
    private WebElement input2;
    @FindBy(css = "span[id$=outtext]")
    private WebElement repeatedText;
    @FindBy(css = "span[id$=requests]")
    private WebElement requests;
    @FindBy(css = "[id$='timeWatcher:grid']")
    private ClientTimeWatcherFragment timeWatcher;
    @FindBy(css = "span[id$=updates]")
    private WebElement updates;

    public void checkDelay(int delayIndex, long expected, long tolerance) {
        timeWatcher.checkDelay(delayIndex, expected, tolerance);
    }

    public void checkDelayAtIndexIs(int delayIndex, long expected) {
        timeWatcher.checkDelayAtIndexIs(delayIndex, expected);
    }

    public void checkLastDelay(long expected, long tolerance) {
        timeWatcher.checkLastDelay(expected, tolerance);
    }

    public void checkLastDelay(long expected) {
        timeWatcher.checkLastDelay(expected);
    }

    public void checkMedian(long expected, long tolerance) {
        timeWatcher.checkMedian(expected, tolerance);
    }

    public void checkMedian(long expected) {
        timeWatcher.checkMedian(expected);
    }

    /**
     * Fire event(s) on specified input.
     * @param input input where the events be triggered on
     * @param countOfEvents count of events to be triggered
     */
    public void fireEvent(final Input input, final int countOfEvents) {
        for (int i = 0; i < countOfEvents; i++) {
            if (input == Input.FIRST) {
                input1.sendKeys(SPACE);
            } else {
                input2.sendKeys(SPACE);
            }
            waitFor(10);// wait some time between requests
        }
    }

    /**
     * Fire event(s) on first input.
     * @param countOfEvents count of events to be triggered
     */
    public void fireEvents(final int countOfEvents) {
        fireEvent(Input.FIRST, countOfEvents);
    }

    public Integer getDOMUpdateCount() {
        return getTextAsInteger(updates);
    }

    public Integer getEvent1Count() {
        return getTextAsInteger(events1);
    }

    public Integer getEvent2Count() {
        return getTextAsInteger(events2);
    }

    public WebElement getInput1() {
        return input1;
    }

    public WebElement getInput2() {
        return input2;
    }

    public String getRepeatedText() {
        return repeatedText.getText();
    }

    public WebElement getRepeatedTextElement() {
        return repeatedText;
    }

    public Integer getRequestCount() {
        return getTextAsInteger(requests);
    }

    public Integer getTextAsInteger(final WebElement element) {
        return Integer.parseInt(element.getText());
    }

    private boolean isEvent2Present() {
        if (event2Present == null) {
            event2Present = new WebElementConditionFactory(input2).isPresent().apply(browser);
            Boolean event2TimePresent = new WebElementConditionFactory(timeWatcher.getEvent2TimeElement()).isPresent().apply(browser);
            assertEquals(event2Present, event2TimePresent);
        }
        return event2Present;
    }

    public void resetDelays() {
        timeWatcher.resetDelays();
    }

    /**
     * Type text to the first input.
     */
    public void type(final String text) {
        input1.sendKeys(text);
    }

    public void waitAndCheckEventsCounts(final int events1, final int requests, final int domUpdates) {
        waitForCountToEqual(new Event1Retriever(), events1, EVENT1_COUNT);
        waitForCountToEqual(new RequestsRetriever(), requests, REQUEST_COUNT);
        waitForCountToEqual(new DOMUpdatesRetriever(), domUpdates, DOM_UPDATES);
    }

    public void waitAndCheckEventsCounts(final int events1, final int events2, final int requests, final int domUpdates) {
        QueueFragment.this.waitAndCheckEventsCounts(events1, requests, domUpdates);
        if (isEvent2Present()) {
            waitForCountToEqual(new Event2Retriever(), events2, EVENT2_COUNT);
        }
    }

    private void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    public void waitForChange(final String oldValue, final WebElement elementToChange) {
        waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return !oldValue.equals(elementToChange.getText());
            }
        });
    }

    private void waitForCountToEqual(final Retriever retrieveCount, final Integer eventCount, final String eventType) {
        Graphene.waitModel().withTimeout(MAXIMUM_WAIT_TIME_IN_SECS, TimeUnit.SECONDS).withMessage(eventType)
            .until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver t) {
                    return retrieveCount.get().equals(eventCount);
                }
            });
    }

    public void waitForDelayIs(int delayIndex, long expected) {
        timeWatcher.waitForDelayIs(delayIndex, expected);
    }

    public void waitForLastDelayIs(long expected) {
        timeWatcher.waitForLastDelayIs(expected);
    }

    public void waitForNumberOfDelaysEqualsTo(long expected) {
        timeWatcher.waitForNumberOfDelaysEqualsTo(expected);
    }

    public static enum Input {

        FIRST, SECOND
    }

    public interface Retriever {

        Integer get();
    }

    public class DOMUpdatesRetriever implements Retriever {

        @Override
        public Integer get() {
            return getDOMUpdateCount();
        }
    }

    public class Event1Retriever implements Retriever {

        @Override
        public Integer get() {
            return getEvent1Count();
        }
    }

    public class Event2Retriever implements Retriever {

        @Override
        public Integer get() {
            return getEvent2Count();
        }
    }

    public class RequestsRetriever implements Retriever {

        @Override
        public Integer get() {
            return getRequestCount();
        }
    }
}
