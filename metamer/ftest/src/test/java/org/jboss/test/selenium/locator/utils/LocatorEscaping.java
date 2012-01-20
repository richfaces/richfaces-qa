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
