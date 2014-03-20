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
package org.richfaces.tests.metamer.ftest.extension.consoleLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.TestResult.Status;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.Test;
import org.jboss.arquillian.test.spi.event.suite.TestEvent;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.tests.metamer.ftest.extension.consoleLogger.annotations.LogThis;
import org.richfaces.tests.metamer.ftest.extension.utils.ReflectionUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ConsoleLoggerExtension {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm:ss");
    private static final Logger LOGGER = Logger.getLogger(ConsoleLoggerExtension.class.getName());

    private static final String COLOR_RED = "\u001B[31m";
    private static final String COLOR_GREEN = "\u001B[32m";
    private static final String COLOR_YELLOW = "\u001B[33m";
    private static final String COLOR_END = "\u001B[0m";

    private static final String TEST_METHOD_DELIMITER = "=====================================================================================";

    private String value;

    public void printBefore(@Observes(precedence = Integer.MAX_VALUE) Test event) {
        System.out.println(TEST_METHOD_DELIMITER);
        System.out.println(String.format("[%s] %s: %s %s",
            new DateTime().toString(FORMAT),
            "STARTED", getTestName(event),
            getAdditionalLogging(event)));
    }

    public void printAfter(@Observes After event, TestResult result) {
        value = String.format("[%s] %s: %s %s",
            new DateTime().toString(FORMAT),
            result.getStatus(),
            getTestName(event),
            getAdditionalLogging(event));
        if (result.getStatus().equals(Status.PASSED)) {
            value = COLOR_GREEN + value;
        } else if (result.getStatus().equals(Status.SKIPPED)) {
            value = COLOR_YELLOW + value;
        } else {
            value = COLOR_RED + value;
        }
        System.out.println(value + COLOR_END);
    }

    private String getAdditionalLogging(TestEvent event) {
        Object testInstance = event.getTestInstance();
        List<String> additionalMessages = Lists.newArrayList();
        Object fieldValue;
        LogThis annotation;
        String fieldValueAsString;

        for (Field field : ReflectionUtils.getAllFieldsAnnotatedWith(LogThis.class, testInstance)) {
            annotation = field.getAnnotation(LogThis.class);
            fieldValue = ReflectionUtils.getFieldValue(field, testInstance);
            if (fieldValue != null) {
                try {
                    Method toStringMethod = fieldValue.getClass().getDeclaredMethod(annotation.objectLogMethod());
                    fieldValueAsString = toStringMethod.invoke(fieldValue).toString();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Was not able to call method {0}().", annotation.objectLogMethod());
                    LOGGER.log(Level.WARNING, ex.toString());
                    fieldValueAsString = null;
                }
                if (fieldValueAsString == null) {
                    if (annotation.logEmptyValue()) {
                        additionalMessages.add(String.format(annotation.logFormat(), fieldValueAsString));
                    }
                } else {
                    additionalMessages.add(String.format(annotation.logFormat(), fieldValueAsString));
                }
            } else if (annotation.logEmptyValue()) {
                additionalMessages.add(String.format(annotation.logFormat(), null));
            }
        }
        return additionalMessages.toString();
    }

    private static String getTestName(TestEvent event) {
        String name = event.getTestClass().getName().replace("org.richfaces.tests.metamer.ftest.", "");
        return name + '.' + event.getTestMethod().getName();
    }
}
