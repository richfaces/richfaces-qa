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
package org.richfaces.tests.metamer.ftest;

import java.util.HashMap;
import java.util.Map;

import org.jboss.test.selenium.utils.testng.TestLoggingUtils;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class ParallelConsoleStatusListener extends TestListenerAdapter {

    private final Map<String, String> messageMap = new HashMap<String, String>();

    @Override
    public void onTestStart(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logStatus(result);
    }

    /**
     * This method will output method name and status on the standard output
     *
     * @param result from the fine-grained listener's method such as onTestFailure(ITestResult)
     */
    private void logStatus(ITestResult result) {
        String message = getMessage(result);
        String id = result.getTestName();
        String previousMsg = messageMap.get(id);
        if (previousMsg == null) {
            messageMap.put(id, message);
        } else {
            System.out.println(previousMsg);
            System.out.println(message);
            messageMap.remove(id);
        }
        if (result.getStatus() != ITestResult.STARTED) {
            System.out.println();
        }
    }

    protected String getMessage(ITestResult result) {
        return TestLoggingUtils.getTestDescription(result);
    }
}
