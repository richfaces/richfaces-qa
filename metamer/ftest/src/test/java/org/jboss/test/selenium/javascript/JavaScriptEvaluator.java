/*
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
 */
package org.jboss.test.selenium.javascript;

import static org.jboss.arquillian.ajocado.utils.PrimitiveUtils.asBoolean;
import static org.jboss.arquillian.ajocado.utils.PrimitiveUtils.asDouble;
import static org.jboss.arquillian.ajocado.utils.PrimitiveUtils.asFloat;
import static org.jboss.arquillian.ajocado.utils.PrimitiveUtils.asInteger;
import static org.jboss.arquillian.ajocado.utils.PrimitiveUtils.asLong;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.waiting.conversion.Convertor;

/**
 * Simplifies manipulation with results of evaluated JavaScript.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 21424 $
 */
public final class JavaScriptEvaluator {

    private static final AjaxSelenium SELENIUM = AjaxSeleniumContext.getProxy();

    private JavaScriptEvaluator() {
    }

    /**
     * Returns the evaluated JavaScript result.
     *
     * @param script
     *            to evaluate
     * @return the evaluated JavaScript result.
     */
    public static String evaluateString(JavaScript script) {
        return checkNull(SELENIUM.getEval(script));
    }

    /**
     * Returns the evaluated JavaScript result as Boolean.
     *
     * @param script
     *            to evaluate
     * @return the evaluated JavaScript result as Boolean.
     */
    public static Boolean evaluateBoolean(JavaScript script) {
        return asBoolean(checkNull(SELENIUM.getEval(script)));
    }

    /**
     * Returns the evaluated JavaScript result as Float.
     *
     * @param script
     *            to evaluate
     * @return the evaluated JavaScript result as Float.
     */
    public static Float evaluateFloat(JavaScript script) {
        return asFloat(checkNull(SELENIUM.getEval(script)));
    }

    /**
     * Returns the evaluated JavaScript result as Double.
     *
     * @param script
     *            to evaluate
     * @return the evaluated JavaScript result as Double.
     */
    public static Double evaluateDouble(JavaScript script) {
        return asDouble(checkNull(SELENIUM.getEval(script)));
    }

    /**
     * Returns the evaluated JavaScript result as Long.
     *
     * @param script
     *            to evaluate
     * @return the evaluated JavaScript result as Long.
     */
    public static Long evaluateLong(JavaScript script) {
        return asLong(checkNull(SELENIUM.getEval(script)));
    }

    /**
     * Returns the evaluated JavaScript result as Integer.
     *
     * @param script
     *            to evaluate
     * @return the evaluated JavaScript result as Integer.
     */
    public static Integer evaluateInteger(JavaScript script) {
        return asInteger(checkNull(SELENIUM.getEval(script)));
    }

    /**
     * Returns the evaluated JavaScript result converted to T by specified converter.
     *
     * @param script
     *            the script to evaluate
     * @return the evaluated JavaScript result converted to T by specified converter.
     */
    public static <T> T evaluate(JavaScript script, Convertor<String, T> converter) {
        String result = checkNull(SELENIUM.getEval(script));
        if (result == null) {
            return null;
        }
        return converter.forwardConversion(result);
    }

    private static String checkNull(String evaluatedResult) {
        if ("null".equals(evaluatedResult) || "undefined".equals(evaluatedResult)) {
            return null;
        }
        return evaluatedResult;
    }
}
