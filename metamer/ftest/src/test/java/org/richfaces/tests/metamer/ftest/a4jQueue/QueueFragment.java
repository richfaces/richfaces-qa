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
package org.richfaces.tests.metamer.ftest.a4jQueue;

import com.google.common.base.Predicate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.richfaces.tests.metamer.ftest.a4jQueue.QueueFragment.Input.FIRST;

public class QueueFragment {

    List<Long> deviations = new ArrayList<Long>();
//    LocatorReference<JQueryLocator> form = new LocatorReference<JQueryLocator>(null);

    @FindByJQuery("input:text[id$=input1]")
    private WebElement input1;

    @FindByJQuery("input:text[id$=input2]")
    private WebElement input2;

    @FindByJQuery("input:submit[id$=actionButton]")
    private WebElement button;

    @FindByJQuery("span[id$=outtext]")
    private WebElement repeatedText;

    @FindByJQuery("span[id$=events1]")
    private WebElement events1;

    @FindByJQuery("span[id$=events2]")
    private WebElement events2;

    @FindByJQuery("span[id$=requests]")
    private WebElement requests;

    @FindByJQuery("span[id$=updates]")
    private WebElement updates;

    @FindByJQuery("span[id$=event1\\:outputTime]")
    private WebElement event1Time;

    @FindByJQuery("span[id$=event2\\:outputTime]")
    private WebElement event2Time;

    @FindByJQuery("span[id$=begin\\:outputTime]")
    private WebElement beginTime;

    @FindByJQuery("span[id$=complete\\:outputTime]")
    private WebElement completeTime;

    @FindByJQuery("input[id$=actionButton]")
    private WebElement actionButton;

    @ArquillianResource
    private JavascriptExecutor js;

    public Integer getTextAsInteger(WebElement element) {
        return Integer.parseInt(element.getText());
    }

    public Integer getRequestCount() {
        return getTextAsInteger(requests);
    }

    public Integer getEvent1Count() {
        return getTextAsInteger(events1);
    }

    public Integer getEvent2Count() {
        return getTextAsInteger(events2);
    }

    public Integer getDOMUpdateCount() {
        return getTextAsInteger(updates);
    }

    private Long getAttributeTitleAsLong(WebElement element) {
        return Long.parseLong(element.getAttribute("title"));
    }

    public Long getEvent1Time() {
        return getAttributeTitleAsLong(event1Time);
    }

    public Long getEvent2Time() {
        return getAttributeTitleAsLong(event2Time);
    }

    public Long getBeginTime() {
        return getAttributeTitleAsLong(beginTime);
    }

    public Long getCompleteTime() {
        return getAttributeTitleAsLong(completeTime);
    }

    @Drone
    private WebDriver browser;

    private Boolean event2Present = null;

    public void waitForChange(final String oldValue, final WebElement elementToChange) {
        waitGui().until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver t) {
                return !oldValue.equals(elementToChange.getText());
            }
        });
    }

    private boolean isEvent2Present() {
        if (event2Present == null) {
            event2Present = new WebElementConditionFactory(input2).isPresent().apply(browser);
            Boolean event2TimePresent = new WebElementConditionFactory(event2Time).isPresent().apply(browser);
            assertEquals(event2Present, event2TimePresent);
        }
        return event2Present;
    }

    public void fireEvent(int countOfEvents) {
        fireEvent(Input.FIRST, countOfEvents);
    }

    public void fireEvent(Input input, int countOfEvents) {
        for (int i = 0; i < countOfEvents; i++) {
            if (input == Input.FIRST) {
                input1.sendKeys(" ");
            } else {
                input2.sendKeys(" ");
            }
        }
    }

    public void clickButton() {
        button.click();
    }

    public void type(String text) {
        input1.sendKeys(text);
    }

    public String getRepeatedText() {
        return repeatedText.getText();
    }

    public void checkCounts(int events1, int requests, int domUpdates) {
        assertChangeIfNotEqualToOldValue(new Event1Retriever(), events1, "event1Count");
        assertChangeIfNotEqualToOldValue(new RequestsRetriever(), requests, "requestCount");
        assertChangeIfNotEqualToOldValue(new DOMUpdatesRetriever(), domUpdates, "domUpdates");
    }

    public void checkCounts(int events1, int events2, int requests, int domUpdates) {
        assertChangeIfNotEqualToOldValue(new Event1Retriever(), events1, "event1Count");
        assertChangeIfNotEqualToOldValue(new RequestsRetriever(), requests, "requestCount");
        assertChangeIfNotEqualToOldValue(new DOMUpdatesRetriever(), domUpdates, "domUpdates");
        if (isEvent2Present()) {
            assertChangeIfNotEqualToOldValue(new Event2Retriever(), events2, "event2Count");
        }
    }

    private void assertChangeIfNotEqualToOldValue(final Retriever retrieveCount, final Integer eventCount, final String eventType) {
        if (!eventCount.equals(retrieveCount.retrieve())) {
            Graphene.waitAjax().withTimeout(12, TimeUnit.SECONDS).withMessage(eventType).until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver t) {
                    return retrieveCount.retrieve().equals(eventCount);
                }
            });
        } else {
            assertEquals(retrieveCount.retrieve(), eventCount, eventType);
        }
    }

    public interface Retriever {

        Integer retrieve();
    }

    public class Event1Retriever implements Retriever {

        @Override
        public Integer retrieve() {
            return getEvent1Count();
        }
    }

    public class Event2Retriever implements Retriever {

        @Override
        public Integer retrieve() {
            return getEvent2Count();
        }
    }

    public class DOMUpdatesRetriever implements Retriever {

        @Override
        public Integer retrieve() {
            return getDOMUpdateCount();
        }
    }

    public class RequestsRetriever implements Retriever {

        @Override
        public Integer retrieve() {
            return getRequestCount();
        }
    }

    public void initializeTimes() {
        deviations.clear();
    }

    public void checkTimes(long requestDelay) {
        checkTimes(FIRST, requestDelay);
    }

    public void checkTimes(Input input, long requestDelay) {
        long eventTimeLong = input == FIRST ? getAttributeTitleAsLong(event1Time) : getAttributeTitleAsLong(event2Time);
        long beginTimeLong = waitForChangeAndReturn(beginTime, true);

        long actualDelay = beginTimeLong - eventTimeLong;
        long deviation = Math.abs(actualDelay - requestDelay);
        long maxDeviation = Math.max(300, requestDelay / 2);
        checkDeviation(deviation, maxDeviation);

        deviations.add(deviation);
    }

    private long waitForChangeAndReturn(final WebElement timeElement, boolean canFail) {
        final long before = getAttributeTitleAsLong(timeElement);
        try {
            waitAjax().pollingEvery(1, TimeUnit.MICROSECONDS)
                    .withTimeout(12, TimeUnit.SECONDS)
                    .until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver t) {
                            long now = getAttributeTitleAsLong(timeElement);
                            return now != before;
                        }
                    });
        } catch (RuntimeException ex) {
            if (!canFail) {
                throw ex;
            }
        }
        long result = getAttributeTitleAsLong(timeElement);
        return result;
    }

    public void checkNoDelayBetweenEvents() {
        long event1Time = getEvent1Time();
        long event2Time = getEvent2Time();
        long actualDelay = Math.abs(event1Time - event2Time);

        checkDeviation(actualDelay, 150);
    }

    public void checkDeviation(long deviation, long maxDeviation) {
        assertTrue(deviation <= maxDeviation,
                String.format("Deviation (%d) is greater than maxDeviation (%d)", deviation, maxDeviation));
    }

    public void checkDeviationMedian(long requestDelay) {
        long maximumDeviationMedian = Math.max(25, Math.min(50, requestDelay / 4));
        long deviationMedian = getMedian(deviations);
        assertTrue(
                deviationMedian <= maximumDeviationMedian,
                String.format("Deviation median (%d) should not be greater than defined maximum %d", deviationMedian,
                        maximumDeviationMedian));
    }

    private <T extends Comparable<T>> T getMedian(List<T> list) {
        List<T> listCopy = new ArrayList<T>(list);
        Collections.sort(listCopy);
        return listCopy.get(listCopy.size() / 2);
    }

    public static enum Input {

        FIRST, SECOND;
    }

    public WebElement getInput1Element() {
        return input1;
    }

    public WebElement getInput2Element() {
        return input2;
    }

    public WebElement getButtonElement() {
        return button;
    }

    public WebElement getRepeatedTextElement() {
        return repeatedText;
    }

    public WebElement getEvents1Element() {
        return events1;
    }

    public WebElement getEvents2Element() {
        return events2;
    }

    public WebElement getRequestsElement() {
        return requests;
    }

    public WebElement getUpdatesElement() {
        return updates;
    }

    public WebElement getEvent1TimeElement() {
        return event1Time;
    }

    public WebElement getEvent2TimeElement() {
        return event2Time;
    }

    public WebElement getBeginTimeElement() {
        return beginTime;
    }

    public WebElement getCompleteTimeElement() {
        return completeTime;
    }

    public JavascriptExecutor getJs() {
        return js;
    }

    public WebElement getActionButtonElement() {
        return actionButton;
    }
}
