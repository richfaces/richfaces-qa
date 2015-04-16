/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.extension.attributes.coverage.collect;

import static java.text.MessageFormat.format;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.finder.ClassFinder;
import org.richfaces.tests.metamer.ftest.extension.finder.MethodFinder;
import org.richfaces.tests.metamer.ftest.extension.finder.SimpleClassFinder;
import org.richfaces.tests.metamer.ftest.extension.finder.SimpleMethodFinder;
import org.testng.annotations.Test;

import com.google.common.collect.Maps;

public class SimpleAttributesCoverageCollector implements AttributesCoverageCollector {

    public static final String A4J = "a4j";
    public static final String A4J_COMPONENT_TEST_PACKAGE = "org.richfaces.tests.metamer.ftest.a4j";
    public static final String ATTRIBUTES = "Attributes";
    public static final String EMPTY_STRING = "";
    public static final String RICH = "rich";
    public static final String RICH_COMPONENT_TEST_PACKAGE = "org.richfaces.tests.metamer.ftest.rich";
    public static final String SRC_TEST_JAVA = "src/test/java";

    private final Set<Method> checkedMethods = new HashSet<Method>(1000);
    private final ClassFinder classesFinder = new SimpleClassFinder();
    private final Map<Class<? extends Enum>, EnumSet> map = new HashMap<Class<? extends Enum>, EnumSet>(100);
    private final MethodFinder methodsFinder = new SimpleMethodFinder();
    private final Map<Package, Class<? extends Enum>> pkgClassMap = Maps.newHashMap();

    private static String getEnumFQNFromPackageName(String packageName) {
        return packageName + packageName.substring(packageName.lastIndexOf('.'))
            .replaceAll(A4J, EMPTY_STRING)
            .replaceAll(RICH, EMPTY_STRING) + ATTRIBUTES;
    }

    @Override
    public Map<Class<? extends Enum>, EnumSet> collect(String pkg) {
        collectAll(
            methodsFinder.findAllMethodsFrom(classesFinder.findAllClassesFrom(SRC_TEST_JAVA).fromPackageStartingWith(pkg).scan()
            ).withAnnotation(Test.class).scan()
        );
        return map;
    }

    private void collect(Method original) {
        collect(original, original);
    }

    private void collect(Method original, Method parentMethod) {
        if (!checkedMethods.contains(original)) {
            CoversAttributes annotation = parentMethod.getAnnotation(CoversAttributes.class);
            if (annotation != null) {
                checkedMethods.add(original);
                handleCoversAttribute(annotation, original);
            } else {
                // try to scan for annotations in parent classes for methods with same name as this @Test method
                for (Method methodInParent : parentMethod.getDeclaringClass().getSuperclass().getDeclaredMethods()) {
                    if (methodInParent.getName().equals(original.getName())) {
                        collect(original, methodInParent);
                        return;
                    }
                }
            }
        }
    }

    private void collectAll(Collection<Method> methods) {
        for (Method m : methods) {
            collect(m);
        }
    }

    private Class<? extends Enum> getEnumClassFromMethod(Method m) {
        Package aPackage = m.getDeclaringClass().getPackage();
        String packageName = aPackage.getName();

        if (pkgClassMap.containsKey(aPackage)) {
            return pkgClassMap.get(aPackage);
        }

        String expectedFQN = getEnumFQNFromPackageName(packageName);

        // do not scan for enum in abstract test superclass of multiple components, e.g. AbstractValidationMessagesTest
        Class<? extends Enum> result;
        if (expectedFQN.startsWith(A4J_COMPONENT_TEST_PACKAGE) || expectedFQN.startsWith(RICH_COMPONENT_TEST_PACKAGE)) {
            try {
                result = (Class<? extends Enum>) Class.forName(expectedFQN);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Class " + expectedFQN + " was not found", ex);
            }
        } else {
            result = null;
        }
        pkgClassMap.put(aPackage, result);
        return result;
    }

    /**
     * Adds covered attribute(s) to the inner map
     */
    private void handleCoversAttribute(CoversAttributes annotation, Method original) {
        Class<? extends Enum> attributeClass = getEnumClassFromMethod(original);
        if (attributeClass != null) {
            EnumSet set;
            if (map.containsKey(attributeClass)) {
                set = map.get(attributeClass);
            } else {
                set = EnumSet.noneOf(attributeClass);
            }
            for (String attributeName : annotation.value()) {
                try {
                    set.add(Enum.valueOf(attributeClass, attributeName));
                } catch (IllegalArgumentException ex) {
                    System.err.println(
                        format("No such enum constant <{0}> in class <{1}>. Detected in method <{2}>",
                            attributeName, attributeClass.getName(), original));
                }
            }
            map.put(attributeClass, set);
        }
    }
}
