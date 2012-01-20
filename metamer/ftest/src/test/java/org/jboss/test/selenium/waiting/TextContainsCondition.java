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
package org.jboss.test.selenium.waiting;

import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;

/**
 *
 * <p>
 * Implementation of Condition for waiting if element given by elementLocator has text containing the given text.
 * </p>
 *
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TextContainsCondition implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    /** The element locator. */
    private ElementLocator<?> elementLocator;

    /** The text. */
    private String text;

    /**
     * Instantiates a new text equals.
     */
    protected TextContainsCondition() {
    }


    @Override
    public JavaScript getJavaScriptCondition() {
        String escapedLocator = escapeJavaScript(this.elementLocator.toString());
        String escapedText = escapeJavaScript(this.text);
        return js(format("selenium.isElementPresent('{0}') && (selenium.getText('{0}').indexOf('{1}') != -1)", escapedLocator,
            escapedText));
    }

    /**
     * Factory method.
     *
     * @return single instance of TextContainsCondition
     */
    public static TextContainsCondition getInstance() {
        return new TextContainsCondition();
    }

    @Override
    public boolean isTrue() {
        Validate.notNull(elementLocator);
        Validate.notNull(text);
        return selenium.getText(elementLocator).contains(text);
    }

    /**
     * <p>
     * Returns the TextContainsCondition instance with given elementLocator set.
     * </p>
     *
     * <p>
     * From this locator will be obtained the text.
     * </p>
     *
     * @param elementLocator
     *            the element locator
     * @return the TextContainsCondition object with preset locator
     */
    public TextContainsCondition locator(ElementLocator<?> elementLocator) {
        Validate.notNull(elementLocator);

        TextContainsCondition copy = copy();
        copy.elementLocator = elementLocator;

        return copy;
    }

    /**
     * <p>
     * Returns the TextContainsCondition instance with text set.
     * </p>
     *
     * @param text
     *            it should wait for the containg
     * @return the TextContainsCondition object with preset text
     */
    public TextContainsCondition text(String text) {
        Validate.notNull(text);

        TextContainsCondition copy = copy();
        copy.text = text;

        return copy;
    }

    /**
     * Returns the exact copy of this ElementPresent object.
     *
     * @return the copy of this TextContainsCondition object
     */
    private TextContainsCondition copy() {
        TextContainsCondition copy = new TextContainsCondition();
        copy.elementLocator = elementLocator;
        copy.text = text;
        return copy;
    }

}
