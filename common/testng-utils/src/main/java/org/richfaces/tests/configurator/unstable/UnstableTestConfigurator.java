/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.configurator.unstable;

import static java.text.MessageFormat.format;

import java.lang.reflect.Method;
import java.util.List;

import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.utils.ReflectionUtils;
import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;

/**
 * Configurator is used to create proxy for {@link org.testng.IHookCallBack IHookCallBack} interface to modify the test
 * execution behavior. For marked tests it will re-run the execution until the first success occurs or the maximum number of
 * retry attempts is reached (defined in {@link org.richfaces.tests.configurator.unstable.annotation.Unstable @Unstable}).
 * <br/>
 * How to use:<br/>
 * 1) override <code>run</code> method from Arquillian in your test base:<br/>
 * <code>@Override public void run(final IHookCallBack callBack, final ITestResult testResult) {</code><br/>
 * <code>    super.run(UnstableTestConfigurator.getGuardedCallback(callBack), testResult);</code><br/>
 * <code>}</code><br/>
 * 2) mark the unstable test method/class with annotation
 * {@link org.richfaces.tests.configurator.unstable.annotation.Unstable @Unstable}<br/>
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class UnstableTestConfigurator {

    /**
     * Returns unstable-guarded test callback.
     *
     * @param callBack
     * @return
     */
    public static IHookCallBack getGuardedCallback(IHookCallBack callBack) {
        return new UnstableGuardedHookCallBackProxy(callBack);
    }

    private static class UnstableGuardedHookCallBackProxy implements IHookCallBack {

        private static final String ARQUILLIAN_GROUP = "arquillian";
        private static final String CONFIGURE_METHOD_NAME = "configure";

        private final IHookCallBack callBack;

        private UnstableGuardedHookCallBackProxy(IHookCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public Object[] getParameters() {
            return callBack.getParameters();
        }

        private boolean groupInBeforeMethodIsNotInArquillianGroup(Method m) {
            for (String group : m.getAnnotation(BeforeMethod.class).groups()) {
                if (group.equals(ARQUILLIAN_GROUP)) {
                    return false;
                }
            }
            return true;
        }

        private void invokeBeforeMethods(ITestResult testResult) {
            List<Method> allMethodsAnnotatedWith = ReflectionUtils.getAllMethodsAnnotatedWith(BeforeMethod.class, testResult.getInstance());
            // TODO: this could not work as expected when there are depending methods
            for (Method m : allMethodsAnnotatedWith) {
                // invoke all before methods except method 'configure' and methods in 'arquillian' group
                // TODO: this could not work as expected when not invoking the configuration step
                if (!m.getName().equals(CONFIGURE_METHOD_NAME) && groupInBeforeMethodIsNotInArquillianGroup(m)) {
                    try {
                        m.invoke(testResult.getInstance());
                    } catch (Throwable t) {
                        throw new RuntimeException("Was not able to invoke @BeforeMethod with name " + m.getName(), t);
                    }
                }
            }
        }

        @Override
        public void runTestMethod(ITestResult testResult) {
            Method m = testResult.getMethod().getConstructorOrMethod().getMethod();
            Unstable annotationOnMethod = m.getAnnotation(Unstable.class);
            Unstable annotationOnTestClass = testResult.getInstance().getClass().getAnnotation(Unstable.class);
            Unstable annotation = (annotationOnMethod != null ? annotationOnMethod : annotationOnTestClass);
            if (annotation != null) {// is annotation present?
                runUnstableMethod(m, annotation, testResult);
            } else {// no annotation >>> call the test method normally
                callBack.runTestMethod(testResult);
            }
        }

        /**
         * Invokes test method repeatedly until maximum number of attempts or first success is reached
         *
         * @param callBack
         * @param testResult
         */
        private void runUnstableMethod(Method m, Unstable u, ITestResult testResult) {
            int retryAttempts = u.retryAttempts();
            boolean firstRun = true;
            // try to invoke the test method for the number of attempts specified in annotation
            for (int i = 1; i <= retryAttempts; i++) {
                try {
                    if (!firstRun) {
                        System.err.println(format("Trying to invoke the test method {0}, attempt #{1}.", m.getName(), i));
                        // invoke before methods
                        invokeBeforeMethods(testResult);
                    }
                    // invoke test
                    m.invoke(testResult.getInstance());
                    // success >>> set test status to SUCCESS
                    setSuccessTestResult(testResult);
                    return;
                } catch (Throwable t) {
                    firstRun = false;
                    if (i == retryAttempts) {
                        // no more retry attempts >>> set test status to FAILED
                        setFailedTestResult(testResult, t);
                        return;
                    }
                }
            }
        }

        private void setFailedTestResult(ITestResult testResult, Throwable t) {
            setTestResult(testResult, ITestResult.FAILURE, t);
        }

        private void setSuccessTestResult(ITestResult testResult) {
            setTestResult(testResult, ITestResult.SUCCESS, null);
        }

        private void setTestResult(ITestResult testResult, int status, Throwable t) {
            testResult.setEndMillis(System.currentTimeMillis());
            testResult.setThrowable(t);
            testResult.setStatus(status);
        }
    }
}
