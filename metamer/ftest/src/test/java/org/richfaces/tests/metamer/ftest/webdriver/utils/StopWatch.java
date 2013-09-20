/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.webdriver.utils;

import org.openqa.selenium.interactions.Action;

/**
 * Simple utility for measuring time spent in some action.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public final class StopWatch {

    final long timeSpentInNanos;

    private StopWatch(long timeSpentInNanos) {
        this.timeSpentInNanos = timeSpentInNanos;
    }

    public TimeSpent inMillis() {
        return new TimeSpent(Math.round(timeSpentInNanos / 1000000.0));
    }

    public TimeSpent inNanos() {
        return new TimeSpent(timeSpentInNanos);
    }

    public TimeSpent inSeconds() {
        return new TimeSpent(Math.round(timeSpentInNanos / 1000000000.0));
    }

    public static StopWatch watchTimeSpentInAction(Action a) {
        long time = System.nanoTime();
        a.perform();
        return new StopWatch(System.nanoTime() - time);
    }

    public static class TimeSpent extends Number {

        private static final long serialVersionUID = 1L;
        private final long value;

        private TimeSpent(long value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return (int) value;
        }

        @Override
        public long longValue() {
            return (long) value;
        }

        @Override
        public float floatValue() {
            return (float) value;
        }

        @Override
        public double doubleValue() {
            return (double) value;
        }

        @Override
        public String toString() {
            return Long.valueOf(value).toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof Number) {
                Number other = (Number) obj;
                return value == other.longValue();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Long.valueOf(value).hashCode();
        }
    }
}
