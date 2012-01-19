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
package org.jboss.test.selenium;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.jboss.arquillian.ajocado.guard.RequestGuardInterceptor;
import org.jboss.arquillian.ajocado.request.RequestType;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class RequestTypeModelGuard implements MethodHandler {
    private RequestGuardInterceptor guard;

    private RequestTypeModelGuard(RequestGuardInterceptor guard) {
        this.guard = guard;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Model> T guard(T model, RequestType requestExpected, boolean interlayed) {
    	RequestGuardInterceptor guard = new RequestGuardInterceptor(requestExpected, interlayed);

        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(model.getClass());
        Class<T> c = f.createClass();

        T newInstance;
        try {
            newInstance = (T) c.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        ((ProxyObject) newInstance).setHandler(new RequestTypeModelGuard(guard));
        return newInstance;
    }

    @Override
    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
        guard.doBeforeCommand();
        Object result = proceed.invoke(self, args);
        guard.doAfterCommand();

        return result;
    }

    /**
     * Shortcut for registering a XMLHttpRequest on given selenium object.
     * 
     * @param selenium
     *            where should be registered XMLHttpRequest guard
     * @return the selenium guarded to use XMLHttpRequest
     */
    public static <T extends Model> T guardXhr(T model) {
        return guard(model, RequestType.XHR, false);
    }

    /**
     * Shortcut for registering a regular HTTP request on given selenium object.
     * 
     * @param selenium
     *            where should be registered regular HTTP request guard
     * @return the selenium guarded to use regular HTTP requests
     */
    public static <T extends Model> T guardHttp(T model) {
        return guard(model, RequestType.HTTP, false);
    }

    /**
     * Shortcut for registering a guard for no request on given selenium object.
     * 
     * @param selenium
     *            where should be registered no request guard
     * @return the selenium guarded to use no request during interaction
     */
    public static <T extends Model> T guardNoRequest(T model) {
        return guard(model, RequestType.NONE, false);
    }

    /**
     * Shortcut for registering guard waiting for interception of XHR type request
     * 
     * @param selenium
     *            where should be the guard registered
     * @return the selenium waiting for interception of XHR type request
     */
    public static <T extends Model> T waitXhr(T model) {
        return guard(model, RequestType.XHR, true);
    }

    /**
     * Shortcut for registering guard waiting for interception of HTTP type request
     * 
     * @param selenium
     *            selenium where should be the guard registered
     * @return the selenium waitinf for interception of HTTP type request
     */
    public static <T extends Model> T waitHttp(T model) {
        return guard(model, RequestType.HTTP, true);
    }

    public interface Model {
    }

}
