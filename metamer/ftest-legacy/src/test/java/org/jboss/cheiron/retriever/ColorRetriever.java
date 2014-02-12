/*
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
package org.jboss.cheiron.retriever;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import java.awt.Color;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.utils.ColorUtils;
import org.jboss.arquillian.ajocado.waiting.conversion.Convertor;
import org.jboss.arquillian.ajocado.waiting.conversion.PassOnConvertor;
import org.jboss.arquillian.ajocado.waiting.retrievers.AbstractRetriever;
import org.jboss.arquillian.ajocado.waiting.retrievers.Retriever;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public class ColorRetriever extends AbstractRetriever<Color> implements Retriever<Color> {

    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    private ElementLocator<?> elementLocator;

    public ColorRetriever() {
    }

    public Color retrieve() {
        Validate.notNull(elementLocator);

        String value = selenium.getStyle(elementLocator, CssProperty.COLOR);
        return ColorUtils.convertToAWTColor(value);
    }

    public JavaScript getJavaScriptRetrieve() {
        final String colorPropertyName = CssProperty.COLOR.getPropertyName();
        return js(format("selenium.getStyle('{0}', '{1}')", elementLocator.getRawLocator(), colorPropertyName));
    }

    public static ColorRetriever getInstance() {
        return new ColorRetriever();
    }

    public ColorRetriever locator(ElementLocator<?> elementLocator) {
        Validate.notNull(elementLocator);

        ColorRetriever copy = copy();
        copy.elementLocator = elementLocator;

        return copy;
    }

    /**
     * Returns a copy of this textRetriever with exactly same settings.
     *
     * Keeps the immutability of this class.
     *
     * @return the exact copy of this textRetriever
     */
    private ColorRetriever copy() {
        ColorRetriever copy = new ColorRetriever();
        copy.elementLocator = elementLocator;
        return copy;
    }

    /**
     * Uses {@link PassOnConvertor} to pass the JavaScript result to result value.
     */
    public Convertor<Color, String> getConvertor() {
        return new ColorConvertor();
    }

    public static class ColorConvertor implements Convertor<Color, String> {
        @Override
        public String forwardConversion(Color object) {
            throw new UnsupportedOperationException();
        }

        public Color backwardConversion(String object) {
            return ColorUtils.convertToAWTColor(object);
        }
    }
}
