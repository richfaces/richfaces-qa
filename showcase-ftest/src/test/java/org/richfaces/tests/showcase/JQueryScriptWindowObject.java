package org.richfaces.tests.showcase;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.arquillian.ajocado.javascript.JavaScript;


public class JQueryScriptWindowObject extends JavaScript {

	public JQueryScriptWindowObject(String selector) {
        super(format("jQuery(window).{0}",
            StringEscapeUtils.escapeJavaScript(selector)));
    }
}
