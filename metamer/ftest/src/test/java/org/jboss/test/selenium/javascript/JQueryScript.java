/*
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
 */
package org.jboss.test.selenium.javascript;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 21424 $
 */
public class JQueryScript extends JavaScript {

    public JQueryScript(String selector) {
        super(format("jQuery('{0}', selenium.browserbot.getCurrentWindow().document)",
            StringEscapeUtils.escapeJavaScript(selector)));
    }

    public JQueryScript(String selector, String jqueryScript) {
        super(format("jQuery('{0}', selenium.browserbot.getCurrentWindow().document).{1}",
            StringEscapeUtils.escapeJavaScript(selector), jqueryScript));
    }

    public JQueryScript(ExtendedLocator<JQueryLocator> jQueryLocator) {
        super(format("jQuery('{0}', selenium.browserbot.getCurrentWindow().document)",
            StringEscapeUtils.escapeJavaScript(jQueryLocator.getRawLocator())));
    }

    public JQueryScript(ExtendedLocator<JQueryLocator> jQueryLocator, String jqueryScript) {
        super(format("jQuery('{0}', selenium.browserbot.getCurrentWindow().document).{1}",
            StringEscapeUtils.escapeJavaScript(jQueryLocator.getRawLocator()), jqueryScript));
    }

    public static JQueryScript jqObject(String selector) {
        return new JQueryScript(selector);
    }

    public static JQueryScript jqObject(JQueryLocator jQueryLocator) {
        return new JQueryScript(jQueryLocator);
    }

    public static JQueryScript jqScript(String selector, String jqueryScript) {
        return new JQueryScript(selector, jqueryScript);
    }

    public static JQueryScript jqScript(ExtendedLocator<JQueryLocator> jQueryLocator, String jqueryScript) {
        return new JQueryScript(jQueryLocator, jqueryScript);
    }
}
