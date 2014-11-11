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

import org.richfaces.tests.metamer.ftest.extension.configurator.config.Config;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.SimpleConfig;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.ConfiguratorExtension;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.utils.ReflectionUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class UseWithFieldConfigurator implements ConfiguratorExtension {

    protected List<Config> createConfig(UseWithField annotation, Object testInstance) {
        switch (annotation.valuesFrom()) {
            case FROM_FIELD:
                return getConfigurationFromField(annotation, testInstance);
            case FROM_METHOD:
                return getConfigurationFromMethod(annotation, testInstance);
            case STRINGS:
                return getConfigurationForStrings(annotation, testInstance);
            case FROM_ENUM:
                return getConfigurationForEnum(annotation, testInstance);
        }
        throw new IllegalStateException("No configuration created");
    }

    @Override
    public List<Config> createConfigurations(Method m, Object testInstance) {
        UseWithField annotation = m.getAnnotation(UseWithField.class);
        if (annotation == null) {
            return Collections.EMPTY_LIST;
        }
        if (!annotation.field().isEmpty()) {
            return createConfig(annotation, testInstance);
        } else {
            throw new IllegalArgumentException("Field attribute of the UseWithField annotation is empty");
        }
    }

    private List<Config> getConfigurationForEnum(UseWithField methodUseAnnotation, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        Field fieldToInjectTo = ReflectionUtils.getFirstFieldWithName(methodUseAnnotation.field(), testInstance);
        if (!fieldToInjectTo.getType().isEnum()) {
            throw new IllegalArgumentException("Uncompatible types. Field should be of type: Enum.");
        }
        for (Object value : fieldToInjectTo.getType().getEnumConstants()) {
            result.add(new SimpleConfig(testInstance, fieldToInjectTo, value));
        }
        return result;
    }

    protected List<Config> getConfigurationForStrings(UseWithField methodUseAnnotation, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        Field fieldToInjectTo = ReflectionUtils.getFirstFieldWithName(methodUseAnnotation.field(), testInstance);
        if (!fieldToInjectTo.getType().equals(String.class)) {
            throw new IllegalArgumentException("Uncompatible types. Field should be of type: String.");
        }
        for (String string : methodUseAnnotation.value()) {
            result.add(new SimpleConfig(testInstance, fieldToInjectTo, string));
        }
        return result;
    }

    protected List<Config> getConfigurationFromField(UseWithField methodUseAnnotation, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        Field fieldToInjectTo = ReflectionUtils.getFirstFieldWithName(methodUseAnnotation.field(), testInstance);
        if (methodUseAnnotation.value()[0].isEmpty()) {
            throw new IllegalArgumentException("Value attribute of the UseWithField annotation is empty.");
        }
        Field fieldWithValues = ReflectionUtils.getFirstFieldWithName(methodUseAnnotation.value()[0], testInstance);
        if (fieldWithValues == null) {
            throw new IllegalArgumentException("No field with name: " + methodUseAnnotation.value()[0] + " found.");
        }
        if (fieldWithValues.getType().isArray() && fieldToInjectTo.getType().isAssignableFrom(fieldWithValues.getType().getComponentType())) {
            for (Object val : (Object[]) ReflectionUtils.getFieldValue(fieldWithValues, testInstance)) {
                result.add(new SimpleConfig(testInstance, fieldToInjectTo, val));
            }
        } else {
            throw new IllegalArgumentException("Uncompatible types. Field with values is not an array. Field to inject to is not assignable from field with values.");
        }
        return result;
    }

    protected List<Config> getConfigurationFromMethod(UseWithField methodUseAnnotation, Object testInstance) {
        List<Config> result = Lists.newLinkedList();
        Field fieldToInjectTo = ReflectionUtils.getFirstFieldWithName(methodUseAnnotation.field(), testInstance);
        if (methodUseAnnotation.value()[0].isEmpty()) {
            throw new IllegalArgumentException("Value attribute of the UseWithField annotation is empty.");
        }
        Method methodWithValues = ReflectionUtils.getFirstMethodWithName(methodUseAnnotation.value()[0], testInstance);
        if (methodWithValues == null) {
            throw new IllegalArgumentException("No method with name: " + methodUseAnnotation.value()[0] + " found.");
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

    @Override
    public boolean skipIfEmpty() {
        return Boolean.FALSE;
    }
}
