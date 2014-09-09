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
package org.richfaces.tests.qa.plugin.utils;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TolerantContainsList extends ForwardingList<String> {

    private final List<String> delegate;

    public TolerantContainsList(List<String> delegate) {
        this.delegate = delegate;
    }

    public TolerantContainsList(Collection<String> delegate) {
        this.delegate = Lists.newArrayList(delegate);
    }

    public TolerantContainsList(String... values) {
        this.delegate = Lists.newArrayList(values);
    }

    @Override
    protected List<String> delegate() {
        return delegate;
    }

    @Override
    public boolean contains(Object o) {
        String toFind = o.toString().toLowerCase();
        for (String string : this) {
            if (string.toLowerCase().contains(toFind)) {
                return true;
            }
        }
        return false;
    }
}
