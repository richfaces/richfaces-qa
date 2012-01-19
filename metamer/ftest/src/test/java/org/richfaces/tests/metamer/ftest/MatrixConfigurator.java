/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.test.selenium.listener.TestMethodSelector;
import org.richfaces.tests.metamer.Template;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class MatrixConfigurator extends TestMethodSelector implements IInvokedMethodListener, IMethodInterceptor,
    ITestListener {

    static Map<Field, Object> currentConfiguration;

    Map<Class<?>, Map<Method, Configuration>> configurations = new HashMap<Class<?>, Map<Method, Configuration>>();
    LinkedList<Method> methods = new LinkedList<Method>();
    boolean methodConfigured = false;
    Field templatesField = null;

    public static Map<Field, Object> getCurrentConfiguration() {
        return currentConfiguration;
    }

    public List<IMethodInstance> intercept(List<IMethodInstance> methodInstances, ITestContext context) {
        List<IMethodInstance> result = new LinkedList<IMethodInstance>();
        for (IMethodInstance methodInstance : methodInstances) {

            for (int i = 0; i < methodInstance.getMethod().getInvocationCount(); i++) {
                methods.add(methodInstance.getMethod().getMethod());
            }
            
            if (methodInstance.getMethod().getInvocationCount() > 0) {
                result.add(methodInstance);
            }
        }
        return result;
    }

    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult testResult) {
        if (!methodConfigured) {
            if (isAfterConfiguration(invokedMethod.getTestMethod())) {
                return;
            }
            
            if (invokedMethod.getTestMethod().isBeforeSuiteConfiguration()) {
            	return;
            }

            configureMethod(testResult);

            methodConfigured = true;
        }
    }

	public void onTestStart(ITestResult result) {
        if (!methodConfigured) {
            configureMethod(result);
            methodConfigured = true;
        }
    }

    public void onTestSuccess(ITestResult result) {
        methodConfigured = false;
    }

    public void onTestFailure(ITestResult result) {
        methodConfigured = false;
    }

    public void onTestSkipped(ITestResult result) {
        if (!methodConfigured) {
            configureMethod(result);
        }
        methodConfigured = false;
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        methodConfigured = false;
    }

    private boolean isAfterConfiguration(ITestNGMethod m) {
        return m.isAfterClassConfiguration() || m.isAfterGroupsConfiguration() || m.isAfterMethodConfiguration()
            || m.isAfterSuiteConfiguration() || m.isAfterTestConfiguration();
    }

    protected Method getCurrentRealMethod() {
        return ((Queue<Method>) methods).poll();
    }

    protected void configureMethod(ITestResult testResult) {
        Method realMethod = getCurrentRealMethod();

        if (realMethod == null) {
            throw new IllegalStateException("can't find more configured methods");
        }

        Class<?> realClass = realMethod.getDeclaringClass();

        final Object testInstance = testResult.getInstance();
        Configuration configuration = configurations.get(realClass).get(realMethod);
        if (!configuration.hasNext()) {
            throw new IllegalStateException("can't find more configurations");
        }
        currentConfiguration = configuration.next();

        try {
            for (Entry<Field, Object> entry : currentConfiguration.entrySet()) {
                final Field field = entry.getKey();
                final Object assignment = entry.getValue();

                setDeclaredFieldValue(testInstance, field, assignment);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot configure method", e);
        }
    }

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod != null) {
            super.transform(annotation, testClass, testConstructor, testMethod);
            int invocationCount = createConfiguration(testMethod.getDeclaringClass(), testMethod);
            annotation.setInvocationCount(invocationCount);
        }
    }

    public int createConfiguration(Class<?> realClass, Method realMethod) {

        Map<Field, List<? extends Object>> parameters = new LinkedHashMap<Field, List<? extends Object>>();

        List<Field> unsatisfied = new LinkedList<Field>();

        // get a list of satisfied and unsatisfied parameters/injections
        Field[] allFields = getAllFields(realClass);
        for (Field field : allFields) {
            if (field.getAnnotation(Inject.class) != null) {
                if (field.getAnnotation(Use.class) != null) {
                    parameters.put(field, getUseParameter(realClass, field.getType(), field.getAnnotation(Use.class)));
                } else if (field.getAnnotation(Templates.class) != null) {
                    parameters.put(
                        field,
                        getTemplatesParameter(realClass, field.getType(), field.getAnnotation(Templates.class),
                            parameters));
                    templatesField = field;
                } else {
                    parameters.put(field, null);
                    unsatisfied.add(field);
                }
            }
        }

        if (templatesField == null) {
            throw new IllegalStateException("there is no field annotated @Templates in " + realClass.getName());
        }

        try {
            // fulfill parameters by super classes' annotations
            fulfillParametersFromAnnotations(realClass, parameters, unsatisfied,
                getAnnotationsForAllSuperClasses(realClass));

            // fulfill parameters by method annotations
            fulfillParametersFromAnnotations(realClass, parameters, unsatisfied, realMethod.getAnnotations());
        } finally {
            templatesField = null;
        }

        if (!unsatisfied.isEmpty()) {
            throw new IllegalStateException("cannot satisfy following injection points: " + unsatisfied.toString());
        }

        Configuration configuration = new Configuration(parameters);

        int count = 0;
        while (configuration.hasNext()) {
            configuration.next();
            count += 1;
        }
        configuration.reset();

        getClassConfigurations(realClass).put(realMethod, configuration);

        if (parameters.size() == 0) {
            return 1;
        }
        
        return count;
    }

    private void fulfillParametersFromAnnotations(Class<?> testClass, Map<Field, List<? extends Object>> parameters,
        List<Field> unsatisfied, Annotation... annotations) {
        for (Annotation annotation : annotations) {
            Use[] useAnnotations = null;
            if (annotation.annotationType() == Uses.class) {
                useAnnotations = ((Uses) annotation).value();
            }
            if (annotation.annotationType() == Use.class) {
                useAnnotations = new Use[] { (Use) annotation };
            }
            if (useAnnotations != null) {
                for (Use useAnnotation : useAnnotations) {
                    for (Field field : parameters.keySet()) {
                        if (field.getName().equals(useAnnotation.field())) {
                            parameters.put(field, getUseParameter(testClass, field.getType(), useAnnotation));
                            unsatisfied.remove(field);
                        }
                    }
                }
            }
            if (annotation.annotationType() == Templates.class) {
                Templates templatesAnnotation = (Templates) annotation;
                parameters.put(templatesField,
                    getTemplatesParameter(testClass, templatesField.getType(), templatesAnnotation, parameters));
            }
        }
    }

    private Field[] getAllFields(Class<?> testClass) {
        List<Field> fields = new ArrayList<Field>();

        Class<?> currentClass = testClass;
        while (currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            if (currentClass == AbstractMetamerTest.class) {
                break;
            }
            currentClass = currentClass.getSuperclass();
        }

        return fields.toArray(new Field[fields.size()]);
    }

    private Annotation[] getAnnotationsForAllSuperClasses(Class<?> testClass) {
        List<Annotation> annotations = new LinkedList<Annotation>();

        Class<?> currentClass = testClass;
        while (currentClass != Object.class) {
            annotations.addAll(Arrays.asList(currentClass.getAnnotations()));
            if (currentClass == AbstractMetamerTest.class) {
                break;
            }
            currentClass = currentClass.getSuperclass();
        }

        // needs to be returned in reversed order because of the lowel level annnotations needs to be processed first
        Collections.reverse(annotations);

        return annotations.toArray(new Annotation[annotations.size()]);
    }

    private Map<Method, Configuration> getClassConfigurations(Class<?> realClass) {
        if (!configurations.containsKey(realClass)) {
            configurations.put(realClass, new HashMap<Method, Configuration>());
        }
        return configurations.get(realClass);
    }

    private List<? extends Object> getUseParameter(Class<?> testClass, Class<?> parameterType, Use useAnnotation) {
        
        List<Object> result = new LinkedList<Object>();
        
        if (useAnnotation.empty()) {
            result.addAll(Arrays.asList(new Object[] { null }));
        }

        if (useAnnotation.ints().length > 0) {
            if (parameterType == int.class || parameterType == Integer.class) {
                result.addAll(Arrays.asList(ArrayUtils.toObject(useAnnotation.ints())));
            }
        }
        
        if (useAnnotation.decimals().length > 0) {
            if (parameterType == double.class || parameterType == Double.class) {
                result.addAll(Arrays.asList(ArrayUtils.toObject(useAnnotation.decimals())));
            }
        }
        
        if (useAnnotation.strings().length > 0) {
            if (parameterType == String.class) {
                result.addAll(Arrays.asList(useAnnotation.strings()));
            }
        }
        
        if (useAnnotation.booleans().length > 0) {
            if (parameterType == boolean.class || parameterType == Boolean.class) {
                result.addAll(Arrays.asList(ArrayUtils.toObject(useAnnotation.booleans())));
            }
        }
        
        if (useAnnotation.enumeration()) {
            if (!parameterType.isEnum()) {
                throw new IllegalArgumentException(parameterType + "have to be enumeration");
            }
            
            result.addAll(Arrays.asList(parameterType.getEnumConstants()));
        }

        // tries satisfy parameter from fields
        if (result.isEmpty()) {
            Object testInstance = getTestInstance(testClass);
            for (int i = 0; i < useAnnotation.value().length; i++) {
                boolean satisfied = false;
                String namePattern = useAnnotation.value()[i];
                namePattern = StringUtils.replace(namePattern, "*", ".+");
                namePattern = StringUtils.replace(namePattern, "?", ".");
    
                for (Field field : getAllFields(testClass)) {
                    Pattern pattern = Pattern.compile(namePattern);
                    if (pattern.matcher(field.getName()).matches()) {
                        boolean isArray = field.getType().isArray();
                        Class<?> representedType = field.getType();
                        if (!parameterType.isAssignableFrom(representedType) && isArray) {
                            representedType = field.getType().getComponentType();
                        } else {
                            isArray = false;
                        }
                        if (parameterType.isAssignableFrom(representedType)) {
                            Object[] assignments = getDeclaredFieldValues(testInstance, field, isArray);
                            for (Object assignment : assignments) {
                                result.add(assignment);
                            }
                            satisfied = true;
                        } else {
                            throw new IllegalStateException("cannot satisfy parameter with declared field '"
                                + field.getName() + "'");
                        }
                    }
                }
                if (satisfied) {
                    continue;
                }
                throw new IllegalStateException("cannot find the field satysfying injection point with name pattern: "
                    + useAnnotation.value()[i]);
            }
        }
        
        if (useAnnotation.useNull()) {
            if (parameterType.isPrimitive()) {
                throw new IllegalArgumentException("parameterType is primitive, can't use null value");
            }
            
            if (result.contains(null)) {
                result.addAll(null);
            }
        }
        
        return result;
    }

    private Object getTestInstance(Class<?> testClass) {
        try {
            return testClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void setDeclaredFieldValue(Object testInstance, Field field, Object assignment)
        throws IllegalArgumentException, IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        if (!isAccessible) {
            field.setAccessible(true);
        }
        field.set(testInstance, assignment);
        field.setAccessible(isAccessible);
    }

    private Object[] getDeclaredFieldValues(Object testInstance, Field field, boolean isArray) {
        try {
            boolean isAccessible = field.isAccessible();
            if (!isAccessible) {
                field.setAccessible(true);
            }
            Object[] result;
            if (isArray) {
                result = (Object[]) field.get(testInstance);
            } else {
                result = new Object[] { field.get(testInstance) };
            }
            field.setAccessible(isAccessible);
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private List<TemplatesList> getTemplatesParameter(Class<?> testClass, Class<?> parameterType,
        Templates templatesAnnotation, Map<Field, List<? extends Object>> parameters) {
        if (parameterType != TemplatesList.class) {
            throw new IllegalStateException("only " + Template.class.getName()
                + " fields can be annotated with @Inject @Templates");
        }

        List<TemplatesList> templates = null;

        // if templates was already set, load them
        if (templatesField != null) {
            templates = (List<TemplatesList>) parameters.get(templatesField);
        }

        // get user defined templates
        List<TemplatesList> userTemplates = MetamerProperties.getTemplates();

        // if "value" is defined, takes precedens
        if (templatesAnnotation.value().length > 0) {
            templates = MetamerProperties.parseTemplates(templatesAnnotation.value());
        }

        // include all "include" to current list
        if (templatesAnnotation.include().length > 0) {
            List<TemplatesList> includeTemplates = MetamerProperties.parseTemplates(templatesAnnotation.include());
            if (templates == null) {
                templates = includeTemplates;
            } else {
                includeTemplates.removeAll(templates);
                templates.addAll(includeTemplates);
            }
        }

        // exclude all "exclude" from current list
        if (templatesAnnotation.exclude().length > 0) {
            List<TemplatesList> excludeTemplates = MetamerProperties.parseTemplates(templatesAnnotation.exclude());
            if (templates != null) {
                templates.removeAll(excludeTemplates);
            }
        }

        // remove all templates which wasn't in user defined set
        if (userTemplates != null) {
            if (templates == null) {
                templates = userTemplates;
            } else {
                templates.retainAll(userTemplates);
            }
        }

        return templates;
    }

    public class Configuration implements Iterator<Map<Field, Object>> {
        Map<Field, List<? extends Object>> parameters;
        Map<Field, Queue<? extends Object>> queues;
        Queue<? extends Object> lastQueue;

        public Configuration(Map<Field, List<? extends Object>> parameters) {
            this.parameters = parameters;
            reset();
        }

        public void reset() {
            queues = new LinkedHashMap<Field, Queue<? extends Object>>();
            for (Entry<Field, List<? extends Object>> entry : parameters.entrySet()) {
                final Field field = entry.getKey();
                List<? extends Object> parameter = entry.getValue();

                lastQueue = new LinkedList<Object>(parameter);
                queues.put(field, lastQueue);
            }
        }

        public Map<Field, Object> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Map<Field, Object> pass = new HashMap<Field, Object>();

            for (Entry<Field, Queue<? extends Object>> entry : queues.entrySet()) {
                final Field field = entry.getKey();
                final Queue<? extends Object> queue = entry.getValue();

                pass.put(field, queue.peek());
            }

            for (Entry<Field, Queue<? extends Object>> entry : queues.entrySet()) {
                final Field field = entry.getKey();
                final Queue<? extends Object> queue = entry.getValue();

                queue.poll();

                if (queue.isEmpty() && hasNext()) {
                    List<? extends Object> parameter = parameters.get(field);
                    queues.put(field, new LinkedList<Object>(parameter));
                } else {
                    break;
                }
            }

            return pass;
        }

        public boolean hasNext() {
            return !parameters.isEmpty() && !lastQueue.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }
    
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    }
}
