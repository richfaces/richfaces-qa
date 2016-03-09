/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface AttributesGetter {

    /**
     * Returns attribute value on page. Empty values from selects and radios are returned as "null" String. Empty value from
     * text inputs are returned as empty String .
     */
    String getAttribute(String name);

    /**
     * Returns attribute value on page. Empty values from selects and radios are returned as "null" String. Empty value from
     * text inputs are returned as empty String .
     */
    String getAttribute(String name, String attributeTableID);

    /**
     * Returns attribute value on page. Empty values from selects and radios are returned as "null" String. Empty value from
     * text inputs are returned as empty String .
     */
    String getAttribute(Object name);

    /**
     * Returns attribute value on page. Empty values from selects and radios are returned as "null" String. Empty value from
     * text inputs are returned as empty String .
     */
    String getAttribute(Object name, String attributeTableID);

    /**
     * Returns true if attribute is found on the page.
     */
    boolean hasAttribute(String name);

    /**
     * Returns true if attribute is found on the page.
     */
    boolean hasAttribute(String name, String attributeTableID);

    /**
     * Returns true if attribute is found on the page.
     */
    boolean hasAttribute(Object name);

    /**
     * Returns true if attribute is found on the page.
     */
    boolean hasAttribute(Object name, String attributeTableID);

}
