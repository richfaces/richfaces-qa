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
package org.richfaces.tests.metamer.ftest.extension.configurator.config;

import java.text.MessageFormat;

/**
 * Configuration with value, name and noop configure/unconfigure methods.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T>
 */
public class NoOpNameValueConfig<T> implements Config {

    private final String name;
    private final T value;

    public NoOpNameValueConfig(String name, T value) {
        this.name = name;
        this.value = value;
    }

    private NoOpNameValueConfig(NoOpNameValueConfig<T> config) {
        this.name = config.getName();
        this.value = config.getValue();
    }

    @Override
    public void configure() {// no operation
    }

    @Override
    public Config copy() {
        return new NoOpNameValueConfig(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NoOpNameValueConfig<?> other = (NoOpNameValueConfig<?>) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 53 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}={1}", getName(), getValue());
    }

    @Override
    public void unconfigure() {// no operation
    }
}
