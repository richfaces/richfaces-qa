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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jboss.test.selenium.utils.testng.TestInfo;
import org.testng.ITestResult;

/**
 * @author <a href="mailto:ppitonak@redhat.com">Lukas Fryc</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision: 21424 $
 */

public final class MetamerTestInfo {
    private MetamerTestInfo() {
    }

    public static String getConfigurationInfo() {
        Map<Field, Object> configuration = MatrixConfigurator.getCurrentConfiguration();

        List<String> info = new LinkedList<String>();
        if (!configuration.isEmpty()) {
            for (Entry<Field, Object> entry : configuration.entrySet()) {
                final String name = entry.getKey().getName();
                final Object value = entry.getValue();

                if (value != null) {
                    info.add(name + ": " + value);
                }
            }
        }

        Collections.sort(info, new Comparator<String>() {
            public int compare(String o1, String o2) {
                if (o1.startsWith("template: ")) {
                    return -1;
                }
                if (o2.startsWith("template: ")) {
                    return 1;
                }
                return o1.length() - o2.length();
            }
        });

        return StringUtils.join(info, "; ");
    }

    public static String getConfigurationInfoInParenthesses() {
        return "{ " + getConfigurationInfo() + " }";
    }

    public static String getAssociatedFilename(ITestResult result) {
        String packageName = TestInfo.getContainingPackageName(result);
        String className = TestInfo.getClassName(result);
        String methodName = TestInfo.getMethodName(result);

        return getFilename(packageName, className, methodName);
    }

    public static String getAssociatedFilename(Method method) {
        String packageName = TestInfo.getContainingPackageName(method);
        String className = TestInfo.getClassName(method);
        String methodName = TestInfo.getMethodName(method);

        return getFilename(packageName, className, methodName);
    }

    private static String getFilename(String packageName, String className, String methodName) {
        String testInfo = getConfigurationInfo();
        testInfo = StringUtils.replaceChars(testInfo, "\\/*?\"<>|", "");
        testInfo = StringUtils.replaceChars(testInfo, "\r\n \t", "_");
        testInfo = StringUtils.replaceChars(testInfo, ":", "-");

        // derives template and sort it as sub-directory after other attributes
        Matcher matcher = Pattern.compile("^(template-[^;]+);(.*)$").matcher(testInfo);
        if (matcher.find()) {
            testInfo = matcher.group(2) + "/" + matcher.group(1);
        }

        return packageName + "/" + className + "/" + methodName + "/" + testInfo;
    }
}
