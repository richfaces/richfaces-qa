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
package org.richfaces.tests.metamer.ftest.extension.finder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.richfaces.tests.metamer.ftest.extension.finder.MethodFinder.Finder;
import org.testng.collections.Sets;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class SimpleMethodFinder implements MethodFinder, Finder {

    private Collection<Class<?>> classes;
    private final List<Predicate<Method>> filters = Lists.newArrayList();
    private final Set<Method> foundMethods = Sets.newHashSet();

    @Override
    public Finder findAllMethodsFrom(Collection<Class<?>> classes) {
        this.classes = classes;
        return this;
    }

    private List<Method> getAndFilterMethods() {
        for (Class<?> klass : classes) {
            foundMethods.addAll(Lists.newArrayList(klass.getDeclaredMethods()));
        }
        Iterable<Method> result = foundMethods;
        for (Predicate<Method> filter : filters) {
            result = Iterables.filter(result, filter);
        }
        return Lists.newArrayList(result);
    }

    @Override
    public Set<Method> scan() {
        return Sets.newHashSet(getAndFilterMethods());
    }

    @Override
    public Finder withAnnotation(final Class<? extends Annotation> annotationClass) {
        filters.add(new Predicate<Method>() {
            @Override
            public boolean apply(Method t) {
                return t.getAnnotation(annotationClass) != null;
            }
        });
        return this;
    }

    @Override
    public Finder withNameMatching(final String match) {
        filters.add(new Predicate<Method>() {
            @Override
            public boolean apply(Method t) {
                return t.getName().matches(match);
            }
        });
        return this;
    }

}
