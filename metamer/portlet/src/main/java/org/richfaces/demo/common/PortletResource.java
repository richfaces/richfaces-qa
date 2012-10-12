/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.demo.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.MimeResponse;
import javax.portlet.ResourceURL;

/**
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */
@ManagedBean(name = "portletRes")
public class PortletResource implements Map<String, String> {

    @Override
    public void clear() {
    }

    @Override
    public boolean containsKey(Object arg0) {
        return true;
    }

    @Override
    public boolean containsValue(Object arg0) {
        return true;
    }

    @Override
    public Set<java.util.Map.Entry<String, String>> entrySet() {
        return Collections.emptySet();
    }

    @Override
    public String get(Object resourceKey) {
        FacesContext context = FacesContext.getCurrentInstance();
        String resourceUrl = null;

        if (null != resourceKey) {
            ExternalContext extCon = context.getExternalContext();
            MimeResponse response = (MimeResponse) extCon.getResponse();
            ResourceURL resUrl = response.createResourceURL();
            resUrl.setResourceID(resourceKey.toString());
            resourceUrl = resUrl.toString();
        }
        return resourceUrl;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }

    @Override
    public String put(String arg0, String arg1) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> arg0) {
    }

    @Override
    public String remove(Object arg0) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<String> values() {
        return Collections.emptySet();
    }

}
