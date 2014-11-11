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
package org.richfaces.tests.metamer.ftest.extension.configurator.use;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.richfaces.tests.metamer.ftest.extension.configurator.ConfiguratorUtils;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.Config;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.ConfiguratorExtension;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.SimpleConfig;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.utils.ReflectionUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class UseForAllTestsConfigurator implements ConfiguratorExtension {

    protected List<Config> createConfigurationForField(Field fieldToInjectTo, Object testInstance) {
        UseForAllTests annotation = fieldToInjectTo.getAnnotation(UseForAllTests.class);
        switch (annotation.valuesFrom()) {
            case FROM_FIELD:
                return getConfigurationFromField(annotation, fieldToInjectTo, testInstance);
            case FROM_METHOD:
                return getConfigurationFromMethod(annotation, fieldToInjectTo, testInstance);
            case STRINGS:
                return getConfigurationFromStrings(annotation, fieldToInjectTo, testInstance);
            case FROM_ENUM:
                return getConfigurationForEnum(annotation, fieldToInjectTo, testInstance);
        }
        throw new IllegalStateException("No configuration created");
    }

    @Override
    public List<Config> createConfigurations(Method m, Object testInstance) {
        List<List<Config>> configs = Lists.newArrayList();
        for (Field field : ReflectionUtils.getAllFieldsAnnotatedWith(UseForAllTests.class, testInstance)) {
            configs.add(createConfigurationForField(field, testInstance));
        }
        ConfiguratorUtils.mergeAllConfigsToOne(configs);
        return (configs.isEmpty() ? Collections.EMPTY_LIST : configs.get(0));
    }

    private List<Config> getConfigurationForEnum(UseForAllTests annotation, Field fieldToInjectTo, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        if (!fieldToInjectTo.getType().isEnum()) {
            throw new IllegalArgumentException("Uncompatible types. Field should be of type: Enum.");
        }
        for (Object value : fieldToInjectTo.getType().getEnumConstants()) {
            result.add(new SimpleConfig(testInstance, fieldToInjectTo, value));
        }
        return result;
    }

    protected List<Config> getConfigurationFromField(UseForAllTests annotation, Field fieldToInjectTo, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        if (annotation.value()[0].isEmpty()) {
            throw new IllegalArgumentException("Value attribute of the UseForAllTests annotation is empty.");
        }
        Field fieldWithValues = ReflectionUtils.getFirstFieldWithName(annotation.value()[0], testInstance);
        if (fieldWithValues == null) {
            throw new IllegalArgumentException("No field with name: " + annotation.value()[0] + " found.");
        }
        if (fieldWithValues.getType().isArray()) {
            if (ClassUtils.isAssignable(fieldToInjectTo.getType(), fieldWithValues.getType().getComponentType(), true)) {
                for (Object val : (Object[]) ReflectionUtils.getFieldValue(fieldWithValues, testInstance)) {
                    result.add(new SimpleConfig(testInstance, fieldToInjectTo, val));
                }
            } else {
                throw new IllegalArgumentException("Uncompatible types.");
            }
        } else {
            throw new IllegalArgumentException("Field is not an array.");
        }
        return result;
    }

    protected List<Config> getConfigurationFromMethod(UseForAllTests annotation, Field fieldToInjectTo, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        if (annotation.value()[0].isEmpty()) {
            throw new IllegalArgumentException("Value attribute of the UseWithField annotation is empty.");
        }
        Method methodWithValues = ReflectionUtils.getFirstMethodWithName(annotation.value()[0], testInstance);
        if (methodWithValues == null) {
            throw new IllegalArgumentException("No method with name: " + annotation.value()[0] + " found.");
        }
        if (methodWithValues.getReturnType().isArray() && fieldToInjectTo.getType().isAssignableFrom(methodWithValues.getReturnType().getComponentType())) {
            for (Object val : (Object[]) ReflectionUtils.getMethodValue(methodWithValues, testInstance)) {
                result.add(new SimpleConfig(testInstance, fieldToInjectTo, val));
            }
        } else {
            throw new IllegalArgumentException("Uncompatible types. Method returning values does not return an array. Field to inject to is not assignable from method with values.");
        }
        return result;
    }

    protected List<Config> getConfigurationFromStrings(UseForAllTests annotation, Field fieldToInjectTo, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        if (!fieldToInjectTo.getType().equals(String.class)) {
            throw new IllegalArgumentException("Uncompatible types. Field should be of type: String.");
        }
        for (String string : annotation.value()) {
            result.add(new SimpleConfig(testInstance, fieldToInjectTo, string));
        }
        return result;
    }

    @Override
    public boolean skipIfEmpty() {
        return Boolean.FALSE;
    }
}
