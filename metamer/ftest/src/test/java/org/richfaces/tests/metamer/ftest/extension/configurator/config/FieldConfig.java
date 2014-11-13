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

import java.lang.reflect.Field;

import org.richfaces.tests.metamer.ftest.extension.utils.ReflectionUtils;

/**
 * Configuration which stores fields and values to inject.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class FieldConfig implements Config {

    private final Field field;
    private final Object originalValue;

    private final Object testInstance;
    private final Object value;

    public FieldConfig(Object testInstance, Object value, Field field) {
        this.testInstance = testInstance;
        this.value = value;
        this.field = field;
        this.originalValue = ReflectionUtils.getFieldValue(field, testInstance);
    }

    private FieldConfig(FieldConfig config) {
        this.testInstance = config.getTestInstance();
        this.value = config.getValue();
        this.field = config.getField();
        this.originalValue = config.getOriginalValue();
    }

    @Override
    public void configure() {
        ReflectionUtils.setFieldValue(getField(), getValue(), getTestInstance());
    }

    @Override
    public Config copy() {
        return new FieldConfig(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FieldConfig other = (FieldConfig) obj;
        if (this.testInstance != other.testInstance && (this.testInstance == null || !this.testInstance.equals(other.testInstance))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        if (this.field != other.field && (this.field == null || !this.field.equals(other.field))) {
            return false;
        }
        return true;
    }

    public Field getField() {
        return field;
    }

    public Object getOriginalValue() {
        return originalValue;
    }

    public Object getTestInstance() {
        return testInstance;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.testInstance != null ? this.testInstance.hashCode() : 0);
        hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 67 * hash + (this.field != null ? this.field.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return field.getName() + "=" + value;
    }

    @Override
    public void unconfigure() {
        ReflectionUtils.setFieldValue(getField(), getOriginalValue(), getTestInstance());
    }
}
