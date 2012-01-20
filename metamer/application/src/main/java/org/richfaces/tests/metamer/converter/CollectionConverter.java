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
package org.richfaces.tests.metamer.converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version$Revision: 22330$
 */
public final class CollectionConverter<E, C extends Collection<E>> {

    Class<C> collectionClass;
    Class<E> memberClass;

    public CollectionConverter(Class<?> collectionClass, Class<?> memberClass) {
        this.collectionClass = (Class<C>) collectionClass;
        this.memberClass = (Class<E>) memberClass;
    }

    public static <E, C extends Collection<E>> CollectionConverter<E, Collection<E>> getInstance(
        Class<E> collectionClass, Class<C> memberClass) {
        return new CollectionConverter<E, Collection<E>>(collectionClass, memberClass);
    }

    public C convert(Object value) {
        Collection<?> collection = getTransformedValue(value);

        if (collection.isEmpty()) {
            return createCollection();
        }

        ValueConverter<E> memberConverter = ValueConverter.getInstance(memberClass);

        C result = createCollection();

        for (Object object : collection) {
            result.add(memberConverter.convert(object));
        }

        return result;
    }

    private Collection<? extends Object> getTransformedValue(Object value) {
        if (value == null) {
            return Collections.EMPTY_LIST;
        } else if (value instanceof String) {
            String[] splitted = StringUtils.split((String) value, ",[] ");
            return Arrays.asList(splitted);
        } else if (value instanceof Collection) {
            return (Collection) value;
        } else {
            throw new UnsupportedOperationException("the value '" + value + "' of class " + value.getClass()
                + " is not supported in collection transformer");
        }
    }

    private C createCollection() {
        try {
            return getConcreteClass().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("wasn't able to construct new collection of given type ("
                + collectionClass.getName() + ")");
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends C> getConcreteClass() {
        if (!collectionClass.isInterface()) {
            return collectionClass;
        } else {
            if (Set.class.isAssignableFrom(collectionClass)) {
                return (Class<? extends C>) (Object) TreeSet.class;
            }
            if (Collection.class.isAssignableFrom(collectionClass)) {
                return (Class<? extends C>) (Object) LinkedList.class;
            }
            throw new IllegalStateException("Unsupported collection of interface '" + collectionClass.getName() + "'");
        }
    }
}
