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
package org.richfaces.tests.metamer.ftest.extension.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ReflectionUtils {

    public static List<Field> getAllFieldsAnnotatedWith(Class<? extends Annotation>[] annoClasses, Object instance) {
        List<Field> result = Lists.newArrayList();
        Class<?> klass = instance.getClass();
        while (!klass.equals(Object.class)) {
            for (Field field : klass.getDeclaredFields()) {
                if (hasAllAnnotations(field, annoClasses)) {
                    result.add(field);
                }
            }
            klass = klass.getSuperclass();
        }
        return result;
    }

    public static List<Field> getAllFieldsAnnotatedWith(Class<? extends Annotation> annoClass, Object instance) {
        return getAllFieldsAnnotatedWith(new Class[]{ annoClass }, instance);
    }

    public static List<Method> getAllMethodsAnnotatedWith(Class<? extends Annotation>[] annoClasses, Object instance) {
        List<Method> result = Lists.newArrayList();
        Class<?> klass = instance.getClass();
        while (!klass.equals(Object.class)) {
            for (Method m : klass.getDeclaredMethods()) {
                if (hasAllAnnotations(m, annoClasses)) {
                    result.add(m);
                }
            }
            klass = klass.getSuperclass();
        }
        return result;
    }

    public static List<Field> getAllFieldsWithName(String name, Object instance) {
        List<Field> result = Lists.newArrayList();
        Class<?> klass = instance.getClass();
        while (!klass.equals(Object.class)) {
            for (Field field : klass.getDeclaredFields()) {
                if (field.getName().equals(name)) {
                    result.add(field);
                }
            }
            klass = klass.getSuperclass();
        }
        return result;
    }

    public static List<Method> getAllMethodsAnnotatedWith(Class<? extends Annotation> annoClass, Object instance) {
        return getAllMethodsAnnotatedWith(new Class[]{ annoClass }, instance);
    }

    public static Field getFirstFieldAnnotatedWith(Class<? extends Annotation>[] annoClasses, Object instance) {
        Class<?> klass = instance.getClass();
        while (!klass.equals(Object.class)) {
            for (Field field : klass.getDeclaredFields()) {
                if (hasAllAnnotations(field, annoClasses)) {
                    return field;
                }
            }
            klass = klass.getSuperclass();
        }
        throw new IllegalArgumentException("No field annotated with " + Arrays.asList(annoClasses) + " found.");
    }

    public static Object getFieldValue(Field field, Object instance) {
        Object result;
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            result = field.get(instance);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (!accessible) {
            field.setAccessible(false);
        }
        return result;
    }

    public static Field getFirstFieldAnnotatedWith(Class<? extends Annotation> annoClass, Object instance) {
        return getFirstFieldAnnotatedWith(new Class[]{ annoClass }, instance);
    }

    public static Method getFirstMethodAnnotatedWith(Class<? extends Annotation>[] annoClasses, Object instance) {
        Class<?> klass = instance.getClass();
        while (!klass.equals(Object.class)) {
            for (Method method : klass.getDeclaredMethods()) {
                if (hasAllAnnotations(method, annoClasses)) {
                    return method;
                }
            }
            klass = klass.getSuperclass();
        }
        throw new IllegalArgumentException("No method annotated with " + Arrays.asList(annoClasses) + " found.");
    }

    public static Field getFirstFieldWithName(String name, Object instance) {
        Class<?> klass = instance.getClass();
        while (!klass.equals(Object.class)) {
            for (Field field : klass.getDeclaredFields()) {
                if (field.getName().equals(name)) {
                    return field;
                }
            }
            klass = klass.getSuperclass();
        }
        throw new IllegalArgumentException("No field with name " + name + " found.");
    }

    public static Method getFirstMethodAnnotatedWith(Class<? extends Annotation> annoClass, Object instance) {
        return getFirstMethodAnnotatedWith(new Class[]{ annoClass }, instance);
    }

    public static Method getFirstMethodWithName(String methodName, Object instance) {
        Class<?> klass = instance.getClass();
        while (!klass.equals(Object.class)) {
            for (Method method : klass.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
            klass = klass.getSuperclass();
        }
        throw new IllegalArgumentException("No method with name " + methodName + " found.");
    }

    public static Object getMethodValue(Method method, Object instance) {
        Object result;
        boolean accessible = method.isAccessible();
        if (!accessible) {
            method.setAccessible(true);
        }
        try {
            result = method.invoke(instance);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (!accessible) {
            method.setAccessible(false);
        }
        return result;
    }

    private static boolean hasAllAnnotations(AccessibleObject o, Class<? extends Annotation>[] annoClasses) {
        for (Class<? extends Annotation> annoClass : annoClasses) {
            if (o.getAnnotation(annoClass) == null) {
                return false;
            }
        }
        return true;
    }

    public static void setFieldValue(Field field, Object value, Object instance) {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            field.set(instance, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (!accessible) {
            field.setAccessible(false);
        }
    }
}
