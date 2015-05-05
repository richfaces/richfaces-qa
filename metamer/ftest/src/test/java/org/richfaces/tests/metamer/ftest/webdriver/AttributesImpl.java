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
package org.richfaces.tests.metamer.ftest.webdriver;

import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.extension.tester.attributes.AttributesHandler;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.TestResourcesProvider;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T>
 */
public class AttributesImpl<T extends AttributeEnum> implements Attributes<T>, UnsafeAttributes {

    private final AttributesHandler attributesHandler;
    private final String attributesID;

    public AttributesImpl(TestResourcesProvider testResourcesProvider, String attributesID) {
        this.attributesID = attributesID;
        this.attributesHandler = new AttributesHandler(testResourcesProvider);
    }

    public static <T extends AttributeEnum> Attributes<T> getAttributesFor(TestResourcesProvider testResourcesProvider) {
        return getAttributesFor(testResourcesProvider, "");
    }

    public static <T extends AttributeEnum> Attributes<T> getAttributesFor(TestResourcesProvider testResourcesProvider, String attributeTableID) {
        return new AttributesImpl<T>(testResourcesProvider, attributeTableID);
    }

    /**
     * Retrieve attribute value
     */
    @Override
    public String get(T attribute) {
        return get(attribute.toString());
    }

    /**
     * Retrieve attribute value
     */
    @Override
    public String get(String attribute) {
        return attributesHandler.getAttribute(attribute, attributesID);
    }

    @Override
    public void reset(T attribute) {
        set(attribute.toString(), "");
    }

    @Override
    public void set(T attribute, String string) {
        set(attribute.toString(), string);
    }

    @Override
    public void set(String attribute, Object value) {
        attributesHandler
            .clear()
            .setAttribute(attribute, attributesID)
            .toValue(value)
            .asSingleAction().perform();
    }

    @Override
    public void set(T attribute, Number no) {
        set(attribute.toString(), no);
    }

    @Override
    public void set(T attribute, Boolean bool) {
        set(attribute.toString(), bool);
    }

    @Override
    public void set(T attribute, Enum<?> item) {
        set(attribute.toString(), item.toString());
    }

    @Override
    public void set(T attribute, Event event) {
        set(attribute.toString(), event.getEventName());
    }
}
