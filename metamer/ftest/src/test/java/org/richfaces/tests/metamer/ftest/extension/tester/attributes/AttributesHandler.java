/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.extension.tester.attributes;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.JavaScriptEnricher;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.ActionWrapper;
import org.richfaces.tests.metamer.ftest.extension.tester.attributes.MultipleAttributesSetter.MultipleAttributesToValueSetter;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.TestResourcesProvider;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AttributesHandler implements AttributesGetter, MultipleAttributesSetter, MultipleAttributesToValueSetter, Action {

    private static final String EMPTY_STRING = "";
    private static final String FOR_STRING = "FOR";
    private static final int TRIES = 5;

    @JavaScript
    private AttributesSetter jsInterface;

    private Entry<String, String> proposal;
    private final TestResourcesProvider resourcesProvider;
    private final JavaScriptEnricher scriptEnricher;

    private final Action submitAction = new ActionWrapper(new Action() {
        @Override
        public void perform() {
            getJsInterface().submit();
        }
    });

    private final Map<String, String> toSet = Maps.newLinkedHashMap();

    public AttributesHandler(TestResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        scriptEnricher = new JavaScriptEnricher();
    }

    private static String lowerCaseForIfDetected(String attributeName) {
        return attributeName.equals(FOR_STRING) ? attributeName.toLowerCase() : attributeName;
    }

    private void _perform() {
        // set
        for (Entry<String, String> entry : toSet.entrySet()) {
            getJsInterface().setAttribute(entry.getKey(), entry.getValue());
        }
        if (getJsInterface().isDirty()) {
            // submit
            Graphene.guardHttp(submitAction).perform();
            // check
            for (Entry<String, String> entry : toSet.entrySet()) {
                assertTrue(getJsInterface().checkAttributeIsSetToValue(entry.getKey(), entry.getValue()));
            }
        }
//        slower implementation:
//        final String[] names = toSet.keySet().toArray(new String[toSet.size()]);
//        final String[] values = toSet.values().toArray(new String[toSet.size()]);
//        getJsInterface().setAttributes(names, values);
//        if (getJsInterface().isDirty()) {
//            // submit
//            Graphene.guardHttp(submitAction).perform();
//            // check
//            assertTrue(getJsInterface().checkAttributesAreSetToValues(names, values));
//        }
    }

    @Override
    public Action asSingleAction() {
        return this;
    }

    @Override
    public MultipleAttributesSetter clear() {
        toSet.clear();
        proposal = null;
        return this;
    }

    @Override
    public String getAttribute(String attributeName) {
        return getAttribute(attributeName, EMPTY_STRING);
    }

    @Override
    public String getAttribute(Object attributeName) {
        return getAttribute(String.valueOf(attributeName));
    }

    @Override
    public String getAttribute(String attributeName, String attributeTableID) {
        return getJsInterface().getAttribute(format("{0}:{1}", attributeTableID, lowerCaseForIfDetected(attributeName)));
    }

    @Override
    public String getAttribute(Object attributeName, String attributeTableID) {
        return getAttribute(String.valueOf(attributeName), attributeTableID);
    }

    public AttributesSetter getJsInterface() {
        if (jsInterface == null) {
            scriptEnricher.enrich(resourcesProvider.getWebDriver().findElement(Utils.BY_BODY), this);
        }
        return jsInterface;
    }

    @Override
    public void perform() {
        Throwable t = null;
        for (int i = 0; i < TRIES; i++) {
            try {
                _perform();
                return;
            } catch (Throwable ex) {
                t = ex;
            }
        }
        throw new AttributeNotSetException(t);
    }

    @Override
    public MultipleAttributesToValueSetter setAttribute(String attributeName) {
        return setAttribute(attributeName, EMPTY_STRING);
    }

    @Override
    public MultipleAttributesToValueSetter setAttribute(String attributeName, String attributeTableID) {
        proposal = new SimpleEntry<String, String>(format("{0}:{1}", attributeTableID, lowerCaseForIfDetected(attributeName)), null);
        return this;
    }

    @Override
    public MultipleAttributesToValueSetter setAttribute(Object attributeName) {
        return setAttribute(String.valueOf(attributeName));
    }

    @Override
    public MultipleAttributesToValueSetter setAttribute(Object attributeName, String attributeTableID) {
        return setAttribute(String.valueOf(attributeName), attributeTableID);
    }

    @Override
    public MultipleAttributesSetter toValue(Object value) {
        return toValue(String.valueOf(value));
    }

    @Override
    public MultipleAttributesSetter toValue(String value) {
        if (proposal == null) {
            throw new IllegalStateException();
        }
        proposal.setValue(String.valueOf(value));
        toSet.put(proposal.getKey(), proposal.getValue());
        proposal = null;
        return this;
    }

    @JavaScript(value = "Metamer.AttributesSetter")
    public interface AttributesSetter {

        boolean checkAttributeIsSetToValue(String name, String value);

        boolean checkAttributesAreSetToValues(String[] names, String[] values);

        String getAttribute(String name);

        boolean isDirty();

        void setAttribute(String name, String value);

        void setAttributes(String[] names, String[] values);

        void submit();
    }
}
