/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML Adapter for converting primitive types and class types to instances of Class<?>.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21299 $
 */
public class JavaTypeAdapter extends XmlAdapter<String, Class<?>> {

    @Override
    public Class<?> unmarshal(String v) throws Exception {
        if ("byte".equals(v)) {
            return byte.class;
        }
        if ("short".equals(v)) {
            return short.class;
        }
        if ("int".equals(v)) {
            return int.class;
        }
        if ("long".equals(v)) {
            return long.class;
        }
        if ("float".equals(v)) {
            return float.class;
        }
        if ("double".equals(v)) {
            return double.class;
        }
        if ("boolean".equals(v)) {
            return boolean.class;
        }
        if ("char".equals(v)) {
            return char.class;
        }

        return Class.forName(v);
    }

    @Override
    public String marshal(Class<?> v) throws Exception {
        return v.getName();
    }
}
