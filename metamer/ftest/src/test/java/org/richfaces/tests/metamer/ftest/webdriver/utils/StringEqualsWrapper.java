/**
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
 */
package org.richfaces.tests.metamer.ftest.webdriver.utils;

import java.util.List;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
/**
 * Wrapper for String for 'equals' methods. Useful for using multiple
 * equals in conditions.
 */
public class StringEqualsWrapper {

    private final String value;

    public StringEqualsWrapper(String value) {
        this.value = value;
    }

    /**
     * For all values specified in attribute @values runs method 'equals'
     * and returns true if some of @values is equal to the wrapped value.
     *
     * @param values values to be compared with the wrapped value
     * @return true if some value is equal to the wrapped value.
     */
    public boolean equalsToSomeOfThis(String... values) {
        if (values == null) {
            throw new IllegalArgumentException("No Strings specified.");
        }
        for (String string : values) {
            if (value.equals(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * For all values specified in attribute @values runs method 'equals'
     * and returns true if some of @values is equal to the wrapped value.
     *
     * @param values values to be compared with the wrapped value
     * @return true if some value is equal to the wrapped value.
     */
    public boolean equalsToSomeOfThis(List<String> values) {
        if (values == null) {
            throw new IllegalArgumentException("No Strings specified.");
        }
        for (String string : values) {
            if (value.equals(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inverts the return of method equalsToSomeOfThis(). Checks all @values
     * that they are not equal to the wrapped value.
     *
     * @param values values to be compared with the wrapped value
     * @return false if some value is equal to the wrapped value.
     */
    public boolean notEqualsToSomeOfThis(String... values) {
        return !equalsToSomeOfThis(values);
    }

    /**
     * Inverts the return of method equalsToSomeOfThis(). Checks all @values
     * that they are not equal to the wrapped value.
     *
     * @param values values to be compared with the wrapped value
     * @return false if some value is equal to the wrapped value.
     */
    public boolean notEqualsToSomeOfThis(List<String> values) {
        return !equalsToSomeOfThis(values);
    }

    /**
     * Almost same as equalsToSomeOfThis(), but uses equalsIgnoreCase()
     * rather than equals().
     *
     * @param values values to be compared with the wrapped value
     * @return true if some value is equal(ignore case) to the wrapped value
     */
    public boolean isSimilarToSomeOfThis(String... values) {
        if (values == null) {
            throw new IllegalArgumentException("No Strings specified.");
        }
        for (String string : values) {
            if (value.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Almost same as equalsToSomeOfThis(), but uses equalsIgnoreCase()
     * rather than equals().
     *
     * @param values values to be compared with the wrapped value
     * @return true if some value is equal(ignore case) to the wrapped value
     */
    public boolean isSimilarToSomeOfThis(List<String> values) {
        if (values == null) {
            throw new IllegalArgumentException("No Strings specified.");
        }
        for (String string : values) {
            if (value.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inverts the return of method isSimilarToSomeOfThis(). Checks all
     * @values that they are not equal(ignore case) to the wrapped value.
     *
     * @param values values to be compared with the wrapped value
     * @return false if some value is equal to the wrapped value.
     */
    public boolean isNotSimilarToSomeOfThis(String... values) {
        return !isSimilarToSomeOfThis(values);
    }

    /**
     * Inverts the return of method isSimilarToSomeOfThis(). Checks all
     * @values that they are not equal(ignore case) to the wrapped value.
     *
     * @param values values to be compared with the wrapped value
     * @return false if some value is equal to the wrapped value.
     */
    public boolean isNotSimilarToSomeOfThis(List<String> values) {
        return !isSimilarToSomeOfThis(values);
    }
}
