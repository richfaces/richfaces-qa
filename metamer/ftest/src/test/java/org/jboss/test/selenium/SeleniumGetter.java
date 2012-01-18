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
package org.jboss.test.selenium;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.utils.PrimitiveUtils;
import org.jboss.arquillian.ajocado.waiting.conversion.Convertor;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22728 $
 * 
 * @param <P>
 *            type of parameter
 */
public abstract class SeleniumGetter<P> {
    protected AjaxSelenium selenium = AjaxSeleniumContext.getProxy();
    protected P parameter;

    public SeleniumGetter(P parameter) {
        this.parameter = parameter;
    }

    protected abstract String obtainValue();

    public Boolean asBoolean() {
        return PrimitiveUtils.asBoolean(obtainValue());
    }

    public Integer asInteger() {
        return PrimitiveUtils.asInteger(obtainValue());
    }

    public Long asLong() {
        return PrimitiveUtils.asLong(obtainValue());
    }

    public Float asFloat() {
        return PrimitiveUtils.asFloat(obtainValue());
    }

    public Double asDouble() {
        return PrimitiveUtils.asDouble(obtainValue());
    }

    public <C> C convert(Convertor<String, C> converter) {
        return converter.forwardConversion(obtainValue());
    }

    public static ValueGetter getValue(ElementLocator<?> elementLocator) {
        return new ValueGetter(elementLocator);
    }

    public static TextGetter getText(ElementLocator<?> elementLocator) {
        return new TextGetter(elementLocator);
    }

    public static Evaluator getEval(JavaScript script) {
        return new Evaluator(script);
    }

    public static class ValueGetter extends SeleniumGetter<ElementLocator<?>> {

        public ValueGetter(ElementLocator<?> parameter) {
            super(parameter);
        }

        @Override
        protected String obtainValue() {
            return selenium.getValue(parameter);
        }

    }

    public static class TextGetter extends SeleniumGetter<ElementLocator<?>> {

        public TextGetter(ElementLocator<?> parameter) {
            super(parameter);
        }

        @Override
        protected String obtainValue() {
            return selenium.getText(parameter);
        }
    }

    public static class Evaluator extends SeleniumGetter<JavaScript> {

        public Evaluator(JavaScript parameter) {
            super(parameter);
        }

        @Override
        protected String obtainValue() {
            String evaluated = selenium.getEval(parameter);
            evaluated = checkNull(evaluated);
            return evaluated;
        }
    }

    private static String checkNull(String evaluatedResult) {
        if ("null".equals(evaluatedResult) || "undefined".equals(evaluatedResult)) {
            return null;
        }
        return evaluatedResult;
    }
}
