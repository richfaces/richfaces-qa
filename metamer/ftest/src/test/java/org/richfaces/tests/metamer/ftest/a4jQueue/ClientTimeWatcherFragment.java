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

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Predicate;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ClientTimeWatcherFragment {

    private static final String COMMA_SPACE = ", ";
    private static final int DELAY_TOLERANCE_DEFAULT = 500;
    private static final int MAXIMUM_WAIT_TIME_IN_SECS = 7;
    private static final int MEDIAN_TOLERANCE_DEFAULT = 400;
    private static final String TITLE = "title";
    private static final String VALUE = "value";

    @FindBy(css = "span[id$='begin:outputTime']")
    private WebElement beginTimeElement;
    @FindBy(css = "span[id$='complete:outputTime']")
    private WebElement completeTimeElement;
    @FindBy(css = "input[id$='delay']")
    private WebElement delayElement;
    @FindBy(css = "span[id$='event1:outputTime']")
    private WebElement event1TimeElement;
    @FindBy(css = "span[id$='event2:outputTime']")
    private WebElement event2TimeElement;
    @FindBy(css = "input[id$='median']")
    private WebElement medianElement;
    @FindByJQuery("span.reset:eq(0)")
    private WebElement resetDelaysElement;
    @FindByJQuery("span.reset:eq(1)")
    private WebElement resetMedianElement;

    public void checkDelay(final int delayIndex, final long expected, final long tolerance) {
        assertEquals(getDelayTime(delayIndex), expected, tolerance);
    }

    public void checkDelayAtIndexIs(final int delayIndex, final long expected) {
        checkDelay(delayIndex, expected, Math.max(expected / 2, DELAY_TOLERANCE_DEFAULT));
    }

    public void checkLastDelay(final long expected, final long tolerance) {
        checkDelay(0, expected, tolerance);
    }

    public void checkLastDelay(final long expected) {
        checkDelayAtIndexIs(0, expected);
    }

    public void checkMedian(final long expected, final long tolerance) {
        assertEquals(getMedian(), expected, tolerance);
    }

    public void checkMedian(final long expected) {
        assertEquals(getMedian(), expected, Math.max(expected / 2, MEDIAN_TOLERANCE_DEFAULT));
    }

    private Long getAttributeTitleAsLong(final WebElement element) {
        return Long.parseLong(element.getAttribute(TITLE));
    }

    public Long getBeginTime() {
        return getAttributeTitleAsLong(beginTimeElement);
    }

    public WebElement getBeginTimeElement() {
        return beginTimeElement;
    }

    public Long getCompleteTime() {
        return getAttributeTitleAsLong(completeTimeElement);
    }

    public WebElement getCompleteTimeElement() {
        return completeTimeElement;
    }

    public WebElement getDelayElement() {
        return delayElement;
    }

    public long getDelayTime() {
        return getDelayTime(0);
    }

    public long getDelayTime(final int index) {
        List<Long> delays = getDelays();
        if (index < 0 || index >= delays.size()) {
            throw new RuntimeException("There is no such delay time yet");
        }
        return delays.get(index);
    }

    private List<Long> getDelays() {
        String[] split = getDelayElement().getAttribute(VALUE).split(COMMA_SPACE);
        List<Long> result = new ArrayList(split.length);
        for (String number : split) {
            result.add(Long.parseLong(number));
        }
        return result;
    }

    public long getDelaysMedian() {
        return Long.parseLong(medianElement.getAttribute(VALUE));
    }

    public Long getEvent1Time() {
        return getAttributeTitleAsLong(event1TimeElement);
    }

    public WebElement getEvent1TimeElement() {
        return event1TimeElement;
    }

    public Long getEvent2Time() {
        return getAttributeTitleAsLong(event2TimeElement);
    }

    public WebElement getEvent2TimeElement() {
        return event2TimeElement;
    }

    public long getMedian() {
        return new Double(getMedianElement().getAttribute(VALUE)).longValue();
    }

    public WebElement getMedianElement() {
        return medianElement;
    }

    public WebElement getResetDelaysElement() {
        return resetDelaysElement;
    }

    public WebElement getResetMedianElement() {
        return resetMedianElement;
    }

    public void resetDelays() {
        getResetDelaysElement().click();
    }

    public void waitForDelayIs(final int delayIndex, final long expected) {
        Graphene.waitModel()
            .ignoring(AssertionError.class, RuntimeException.class)
            .withMessage(format("waiting for delay #{0} to be near <{1}>.", delayIndex, expected))
            .until(new Predicate<WebDriver>() {
                public boolean apply(WebDriver t) {
                    checkDelayAtIndexIs(delayIndex, expected);
                    return true;
                }
            });
    }

    public void waitForLastDelayIs(final long expected) {
        waitForDelayIs(0, expected);
    }

    public void waitForNumberOfDelaysEqualsTo(final long expected) {
        Graphene.waitModel().ignoring(NumberFormatException.class).withTimeout(MAXIMUM_WAIT_TIME_IN_SECS, TimeUnit.SECONDS).until(new Predicate<WebDriver>() {
            private int size;

            @Override
            public boolean apply(WebDriver t) {
                size = getDelays().size();
                return size == expected;
            }

            @Override
            public String toString() {
                return format("presence of <{0}> delays. <{1}> delays were/was present.", expected, size);
            }
        });
    }
}
