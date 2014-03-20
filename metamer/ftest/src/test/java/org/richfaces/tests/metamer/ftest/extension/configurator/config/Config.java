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
import java.util.List;

import org.richfaces.tests.metamer.ftest.extension.utils.ReflectionUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface Config {

    /**
     * Injects values to fields.
     */
    void configure();

    /**
     * Creates a copy of this configuration.
     */
    Config copy();

    /**
     * Gets all fields configurations.
     */
    List<FieldConfiguration> getConfigurations();

    public static class FieldConfiguration {

        private final Object testInstance;
        private final Object value;
        private final Field field;

        public FieldConfiguration(Object testInstance, Object value, Field field) {
            this.testInstance = testInstance;
            this.value = value;
            this.field = field;
        }

        public Field getField() {
            return field;
        }

        public Object getTestInstance() {
            return testInstance;
        }

        public Object getValue() {
            return value;
        }

        public void injectValueToField() {
            ReflectionUtils.setFieldValue(getField(), getValue(), getTestInstance());
        }

        @Override
        public String toString() {
            return field.getName() + "=" + value;
        }
    }
}
