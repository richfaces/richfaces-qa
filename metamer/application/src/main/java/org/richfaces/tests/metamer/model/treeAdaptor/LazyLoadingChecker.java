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
package org.richfaces.tests.metamer.model.treeAdaptor;

import static java.lang.reflect.Modifier.isStatic;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>
 * Wraps {@link LazyLoadable} entity with proxy which performs notification by {@link LazyLoadable#notifyLoaded()} to
 * let know about accessing methods of that wrapped instance.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version$Revision: 22493$
 */
public final class LazyLoadingChecker<T extends Serializable & LazyLoadable> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 1L;

    T instance;

    private LazyLoadingChecker(T instance) {
        this.instance = instance;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable & LazyLoadable> T wrapInstance(T instance) {
        return (T) Proxy.newProxyInstance(instance.getClass().getClassLoader(), instance.getClass().getInterfaces(),
            new LazyLoadingChecker<T>(instance));
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (!isStatic(m.getModifiers())) {
            instance.notifyLoaded();
        }
        Object result = m.invoke(instance, args);
        return result;
    }
}
