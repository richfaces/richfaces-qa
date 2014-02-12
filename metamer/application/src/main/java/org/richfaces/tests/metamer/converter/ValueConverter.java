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
package org.richfaces.tests.metamer.converter;

import java.lang.reflect.Constructor;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22493 $
 */
public final class ValueConverter<T> {

    Class<T> type;

    private ValueConverter(Class<?> type) {
        this.type = (Class<T>) type;
    }

    public static <T> ValueConverter<T> getInstance(Class<T> type) {
        return new ValueConverter<T>(type);
    }

    public T convert(Object value) {
        if (value == null) {
            return null;
        } else if (type.isAssignableFrom(String.class)) {
            return (T) value;
        } else if (value instanceof String) {
            return construct(value);
        } else {
            throw new UnsupportedOperationException("Can't convert the object of '" + value.getClass().getName()
                + "' to '" + type.getName() + "'");
        }
    }

    private T construct(Object value) {
        try {
            return getConstructor().newInstance(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Can't construct the object of class '" + type.getName()
                + "' from String");
        }

    }

    public Constructor<T> getConstructor() {
        try {
            return type.getConstructor(String.class);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Can't construct the object of class '" + type.getName()
                + "' from String");
        }
    }

}
