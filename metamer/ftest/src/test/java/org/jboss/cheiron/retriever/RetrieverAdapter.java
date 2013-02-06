/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.jboss.cheiron.retriever;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.waiting.conversion.Convertor;
import org.jboss.arquillian.ajocado.waiting.retrievers.Retriever;

/**
 * Adapts the value for final representation using predefined set of converters.
 *
 *
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 21424 $
 *
 * @param <T>
 *            the type of retrieved value
 * @param <C>
 *            the type of value converted from retriever value
 */
public class RetrieverAdapter<T, C> implements Retriever<C> {

    private Retriever<T> retriever;
    private Class<C> conversionType;

    public static <A> RetrieverAdapter<A, String> stringAdapter(Retriever<A> retriever) {
        RetrieverAdapter<A, String> adapter = new RetrieverAdapter<A, String>();
        adapter.retriever = retriever;
        adapter.conversionType = String.class;
        return adapter;
    }

    public static <A> RetrieverAdapter<A, Integer> integerAdapter(Retriever<A> retriever) {
        RetrieverAdapter<A, Integer> adapter = new RetrieverAdapter<A, Integer>();
        adapter.retriever = retriever;
        adapter.conversionType = Integer.class;
        return adapter;
    }

    public static <A> RetrieverAdapter<A, Long> longAdapter(Retriever<A> retriever) {
        RetrieverAdapter<A, Long> adapter = new RetrieverAdapter<A, Long>();
        adapter.retriever = retriever;
        adapter.conversionType = Long.class;
        return adapter;
    }

    public Convertor<C, T> convertor() {
        return new Convertor<C, T>() {
            public C backwardConversion(T object) {
                String converted = retriever.getConvertor().forwardConversion(object);
                if (conversionType == String.class) {
                    return (C) converted;
                } else if (conversionType == Integer.class) {
                    return (C) Integer.valueOf(converted);
                } else if (conversionType == Long.class) {
                    return (C) Long.valueOf(converted);
                }
                throw new UnsupportedOperationException();
            };

            public T forwardConversion(C object) {
                String converted;
                if (conversionType == String.class) {
                    converted = (String) object;
                } else if (conversionType == Integer.class) {
                    converted = Integer.toString((Integer) object);
                } else if (conversionType == Long.class) {
                    converted = Long.toString((Long) object);
                } else {
                    throw new UnsupportedOperationException();
                }
                return retriever.getConvertor().backwardConversion(converted);
            };
        };
    }

    public C getValue() {
        return convertor().backwardConversion(retriever.getValue());
    }

    public void setValue(C oldValue) {
        retriever.setValue(convertor().forwardConversion(oldValue));
    }

    public void initializeValue() {
        retriever.initializeValue();
    }

    public C retrieve() {
        return convertor().backwardConversion(retriever.retrieve());
    }

    public JavaScript getJavaScriptRetrieve() {
        return retriever.getJavaScriptRetrieve();
    }

    public Convertor<C, String> getConvertor() {
        return new Convertor<C, String>() {
            public C backwardConversion(String object) {
                return convertor().backwardConversion(retriever.getConvertor().backwardConversion(object));
            }

            public String forwardConversion(C object) {
                return retriever.getConvertor().forwardConversion(convertor().forwardConversion(object));
            };
        };
    }

}
