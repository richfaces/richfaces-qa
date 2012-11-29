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
package org.richfaces.tests.metamer.ftest.a4jPoll;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;
import static org.jboss.arquillian.ajocado.utils.PrimitiveUtils.asLong;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.pollAttributes;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.framework.GrapheneConfigurationContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Tests the a4j:poll component.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22681 $
 */
public class TestPollInterval extends AbstractGrapheneTest {

    private static final int ITERATION_COUNT = 5;

    @Inject
    int interval;

    JQueryLocator time = pjq("span[id$=time]");
    AttributeLocator<?> clientTime = pjq("span[id$=event1:outputTime]").getAttribute(Attribute.TITLE);

    long startTime;

    long deviationTotal = 0;
    long deviationCount = 0;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPoll/simple.xhtml?enabled=false");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("A4J", "A4J Poll", "Simple");
    }

    @BeforeMethod
    public void resetDeviations() {
        deviationTotal = 0;
        deviationCount = 0;
    }

    @Test
    @Use(field = "interval", ints = { 1000 })
    public void testClientAllTemplates() throws IOException {
        testClient();
    }

    @Test
    @Use(field = "interval", ints = { 500, 5000 })
    @Templates(value = "plain")
    public void testClientDifferentIntervals() throws IOException {
        testClient();
    }

    /**
     * <p>
     * Test the progress of polling for 3 different values from client side.
     * </p>
     *
     * <p>
     * It defines the new interval value first for each iteration and then enable polling.
     * </p>
     *
     * <p>
     * Then, it waits for first poll event (zero iteration).
     * </p>
     *
     * <p>
     * For 5 following poll events it checks that runtime visible from client (output from JavaScript's new
     * Date().getTime()) between the events haven't greater deviation from defined interval than defined
     * MAX_DEVIATION.
     * </p>
     *
     * <p>
     * Test also computes average value of deviation and checks that the average value of all obtained particular
     * deviations isn't greater than MAX_AVERAGE_DEVIATION.
     * </p>
     */
    public void testClient() {
        pollAttributes.set(PollAttributes.enabled, true);
        pollAttributes.set(PollAttributes.interval, interval);

        waitForPoll();
        for (int i = 0; i < ITERATION_COUNT; i++) {
            startInterval();
            waitForPoll();
            validateInterval();
        }

        validateAverageDeviation();
    }

    private void startInterval() {
        startTime = System.currentTimeMillis();
    }

    private void waitForPoll() {
        selenium.getPageExtensions().install();
        selenium.getRequestGuard().clearRequestDone();
        selenium.getRequestGuard().waitForRequest();
    }

    private void validateInterval() {
        long runTime = getClientTime() - startTime;
        long deviation = Math.abs(interval - runTime);

        if (GrapheneConfigurationContext.getProxy().isSeleniumDebug()) {
            System.out.println(format("deviation for interval {0}: {1}", interval, deviation));
        }

        assertTrue(deviation <= (2 * interval),
            format("Deviation ({0}) is greater than two intervals (2 * {1})", deviation, interval));

        deviationTotal += deviation;
        deviationCount += 1;
    }

    private void validateAverageDeviation() {
        long maximumAvgDeviation = Math.max(300, Math.min(interval / 3, 1000));
        long averageDeviation = deviationTotal / deviationCount;

        if (GrapheneConfigurationContext.getProxy().isSeleniumDebug()) {
            System.out.println("averageDeviation: " + averageDeviation);
        }
        assertTrue(
            averageDeviation <= maximumAvgDeviation,
            format("Average deviation for all the intervals ({0}) should not be greater than defined maximum {1}",
                averageDeviation, maximumAvgDeviation));
    }

    /**
     * Returns the time of poll event (the time when arrived the response from server)
     *
     * @return the time of poll event (the time when arrived the response from server)
     */
    private long getClientTime() {
        return asLong(selenium.getAttribute(clientTime));
    }
}
