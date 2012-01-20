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
package org.jboss.test.selenium.locator.utils;

import org.codehaus.plexus.util.StringUtils;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;

public class LocatorEscaping {

    public static final JQueryLocator jq(String locator) {

        String escapedString = locator;

        if (locator.indexOf("[") != -1) {
            escapedString = escapeSelector(locator);
        }

        return new JQueryLocator(escapedString);
    }

    private static String escapeSelector(String jquerySelector) {

        // find the text between []
        int indexOpen = jquerySelector.indexOf("[");
        String escapedString = null;

        if (indexOpen != -1) {
            int indexClose = jquerySelector.indexOf("]");
            int indexEqualsSign = indexOpen + (jquerySelector.substring(indexOpen)).indexOf("=");

            String onlyThisNeedToBeEscaped = jquerySelector.substring(indexEqualsSign + 1, indexClose);

            // see the http://api.jquery.com/category/selectors/ NOTE that I left {, }, ' since they are used in
            // attribute value
            // and do not need escaping
            char[] escapedChars = { '!', '"', '#', '$', '%', '&', '(', ')', '*', '+', ',', '.', '/', ':', ';', '<',
                '=', '>', '?', '@', '[', '\\', ']', '^', '`', '|', '~' };

            escapedString = StringUtils.escape(onlyThisNeedToBeEscaped, escapedChars, '\\');

            escapedString = jquerySelector.replace(onlyThisNeedToBeEscaped, escapedString);

            String checkFurther = jquerySelector.substring(indexClose + 1);

            if (checkFurther.indexOf("[") != -1) {

                String furtherEscapedPart = escapeSelector(checkFurther);
                escapedString = escapedString.replace(checkFurther, furtherEscapedPart);
            }
        }

        return escapedString;
    }
}
