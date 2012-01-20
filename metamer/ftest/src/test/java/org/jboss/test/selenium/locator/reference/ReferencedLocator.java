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
package org.jboss.test.selenium.locator.reference;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.locator.element.AbstractElementLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 21424 $
 *
 * @param <T>
 *            type of referenced locator
 */
public final class ReferencedLocator<T extends ExtendedLocator<T>> extends AbstractElementLocator<T> implements
    ExtendedLocator<T> {

    private LocatorReference<ExtendedLocator<T>> reference;
    private String addition;

    private ReferencedLocator(LocatorReference<ExtendedLocator<T>> reference, String addition) {
        super("not-used");
        this.reference = reference;
        this.addition = addition;
    }

    public static <N extends ExtendedLocator<N>> ReferencedLocator<N> ref(LocatorReference<N> reference, String locator) {
        LocatorReference<ExtendedLocator<N>> castReference = (LocatorReference<ExtendedLocator<N>>) reference;
        ReferencedLocator<N> result = new ReferencedLocator<N>(castReference, locator);
        return result;
    }

    public static <N extends ExtendedLocator<N>> ReferencedLocator<N> referenceInferred(
        LocatorReference<ExtendedLocator<N>> reference, String locator) {
        ReferencedLocator<N> result = new ReferencedLocator<N>(reference, locator);
        result.reference = reference;
        return result;
    }

    public static <N extends ExtendedLocator<N>> ReferencedLocator<N> ref(ReferencedLocator<N> referencedLocator,
        String locator) {
        LocatorReference<ExtendedLocator<N>> reference = new LocatorReference<ExtendedLocator<N>>(referencedLocator);
        return referenceInferred(reference, locator);
    }

    public T getReferenced() {
        ExtendedLocator<T> referencedLocator = reference.getLocator();
        @SuppressWarnings("unchecked")
        Class<T> tClass = (Class<T>) referencedLocator.getClass();

        try {
            if (tClass.isAssignableFrom(ReferencedLocator.class)) {
                ReferencedLocator<T> ancestor = (ReferencedLocator<T>) referencedLocator;
                referencedLocator = ancestor.getReferenced();
                tClass = (Class<T>) referencedLocator.getClass();
            }

            T newInstance = tClass.getConstructor(String.class).newInstance(addition);
            T result = referencedLocator.getDescendant(newInstance);
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getRawLocator() {
        return getReferenced().getRawLocator();
    }

    public ElementLocationStrategy getLocationStrategy() {
        return reference.getLocator().getLocationStrategy();
    }

    public T getChild(T elementLocator) {
        return getReferenced().getChild(elementLocator);
    }

    public T getDescendant(T elementLocator) {
        return getReferenced().getDescendant(elementLocator);
    }

    public ReferencedLocator<T> format(Object... args) {
        String newAddition = SimplifiedFormat.format(addition, args);
        try {
            return (ReferencedLocator<T>) new ReferencedLocator<T>(this.reference, newAddition);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public T get(int index) {
        return getReferenced().get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return getReferenced().iterator();
    }

    @Override
    public int size() {
        return getReferenced().size();
    }
}
