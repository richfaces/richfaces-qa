/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;



/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public abstract class DelayTester {

    private static final int ITERATION_COUNT = 10;
    private static final int ONE_PASS_MINIMUM = 250;

    long actualDelay;
    long expectedDelay;
    
    List<Long> deviations = new ArrayList<Long>();

    public DelayTester(long expectedDelay) {
        this.expectedDelay = expectedDelay;
    }

    public abstract void action();

    public void beforeTest() {
    }

    public void beforeAction() {
    }

    public void afterAction() {
    }

    public void afterTest() {
    }

    public void run() {
        beforeTest();
        for (int i = 0; i < ITERATION_COUNT; i++) {
            beforeAction();
            actualDelay = System.currentTimeMillis();
            action();
            actualDelay = System.currentTimeMillis() - actualDelay;
            validateOnePass();
            afterAction();
        }
        checkDeviationMedian();
        afterTest();
    }

    protected long getMaximumSingleDeviation() {
        return Math.max(ONE_PASS_MINIMUM, 2 * actualDelay);
    }

    protected long getMaximumDeviationMedian() {
        return getMinMax(300, expectedDelay / 4, 500);
    }

    private void validateOnePass() {
        long deviation = Math.abs(expectedDelay - actualDelay);
        long maxDelay = getMaximumSingleDeviation();

        if (AjocadoConfigurationContext.getProxy().isSeleniumDebug()) {
            System.out.println(format("deviation for preset delay {0}: {1} (delay {2})", expectedDelay, deviation, actualDelay));
        }

        assertTrue(deviation <= maxDelay,
            format("Deviation ({0}) is greater than defined maximum ({1})", deviation, maxDelay));

        deviations.add(deviation);
    }

    private void checkDeviationMedian() {
        long maximumDeviationMedian = getMaximumDeviationMedian();
        long deviationMedian = getMedian(deviations);
        if (AjocadoConfigurationContext.getProxy().isSeleniumDebug()) {
            System.out.println("deviationMedian: " + deviationMedian);
        }
        assertTrue(
            deviationMedian <= maximumDeviationMedian,
            format("Deviation median ({0}) should not be greater than defined maximum {1}", deviationMedian,
                maximumDeviationMedian));
    }

    private <T extends Comparable<T>> T getMedian(List<T> list) {
        List<T> listCopy = new ArrayList<T>(list);
        Collections.sort(listCopy);
        return listCopy.get(listCopy.size() / 2);
    }

    protected long getMinMax(long min, long value, long max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
