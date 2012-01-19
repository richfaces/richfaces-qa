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
package org.jboss.cheiron.retriever;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.waiting.conversion.Convertor;
import org.jboss.arquillian.ajocado.waiting.conversion.PassOnConvertor;
import org.jboss.arquillian.ajocado.waiting.retrievers.AbstractRetriever;
import org.jboss.arquillian.ajocado.waiting.retrievers.Retriever;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 21424 $
 */
public class ScriptEvaluationRetriever extends AbstractRetriever<String> implements Retriever<String> {

    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    private JavaScript javaScript;

    public ScriptEvaluationRetriever() {
    }

    public String retrieve() {
        Validate.notNull(javaScript);

        return selenium.getEval(javaScript);
    }

    public JavaScript getJavaScriptRetrieve() {
        return js(format("selenium.getEval('{0}')", javaScript.getAsString()));
    }

    public static ScriptEvaluationRetriever getInstance() {
        return new ScriptEvaluationRetriever();
    }

    public ScriptEvaluationRetriever script(JavaScript javaScript) {
        Validate.notNull(javaScript);

        ScriptEvaluationRetriever copy = copy();
        copy.javaScript = javaScript;

        return copy;
    }

    /**
     * Returns a copy of this textRetriever with exactly same settings.
     * 
     * Keeps the immutability of this class.
     * 
     * @return the exact copy of this textRetriever
     */
    private ScriptEvaluationRetriever copy() {
        ScriptEvaluationRetriever copy = new ScriptEvaluationRetriever();
        copy.javaScript = javaScript;
        return copy;
    }

    /**
     * Uses {@link PassOnConvertor} to pass the JavaScript result to result value.
     */
    public Convertor<String, String> getConvertor() {
        return new PassOnConvertor<String>();
    }
}
