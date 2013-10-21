/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.jboss.test.selenium.listener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.test.selenium.utils.testng.TestInfo;
import org.jboss.test.selenium.utils.testng.TestLoggingUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Test listener which provides the methods injected in lifecycle of test case to catch the additional information in
 * context of test failure.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @authot <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision$
 */
public class FailureLoggingTestListener extends TestListenerAdapter {

    // protected static final Logger LOGGER = Logger.getLogger(FailureLoggingTestListener.class.getName());
    protected File mavenProjectBuildDirectory = new File(System.getProperty("maven.project.build.directory",
        "./target/"));
    protected File failuresOutputDir = new File(mavenProjectBuildDirectory, "failures");

    @Override
    public void onStart(ITestContext testContext) {
        try {
            FileUtils.forceMkdir(failuresOutputDir);
            // FIXME it should clean directory only if it is the first test suite
            // FileUtils.cleanDirectory(failuresOutputDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        onFailure(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        onFailure(result);
    }

    public void onFailure(ITestResult result) {
        if (getWebDriver() == null) {
            System.out.println("Can't take a screenshot and save HTML, because there is no driver available.");
            return;
        }

        Throwable throwable = result.getThrowable();
        String stacktrace = null;

        if (throwable != null) {
            stacktrace = ExceptionUtils.getStackTrace(throwable);
        }

        String filenameIdentification = getFilenameIdentification(result);

        // TODO traffic can be captured using BrowserMob Proxy
        // String traffic;
        // try {
        // traffic = selenium.captureNetworkTraffic(NetworkTrafficType.PLAIN).getTraffic();
        // } catch (SeleniumException e) {
        // traffic = ExceptionUtils.getFullStackTrace(e);
        // }

        try {
            File screenshot = null;
            if (((GrapheneProxyInstance) getWebDriver()).unwrap() instanceof TakesScreenshot) {
                screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.FILE);
            }

            String htmlSource = getWebDriver().getPageSource();

            File stacktraceOutputFile = new File(failuresOutputDir, filenameIdentification + "/stacktrace.txt");
            File imageOutputFile = new File(failuresOutputDir, filenameIdentification + "/screenshot.png");
            // File trafficOutputFile = new File(failuresOutputDir, filenameIdentification + "/network-traffic.txt");
            // File logOutputFile = new File(failuresOutputDir, filenameIdentification + "/selenium-log.txt");
            File htmlSourceOutputFile = new File(failuresOutputDir, filenameIdentification + "/html-source.html");

            File directory = imageOutputFile.getParentFile();
            FileUtils.forceMkdir(directory);

            FileUtils.writeStringToFile(stacktraceOutputFile, stacktrace);
            if (!HtmlUnitDriver.class.isInstance(getWebDriver())) {
                FileUtils.copyFile(screenshot, imageOutputFile);
            }

            // FileUtils.writeStringToFile(trafficOutputFile, traffic);
            // FileUtils.writeLines(logOutputFile, methodLog);
            FileUtils.writeStringToFile(htmlSourceOutputFile, htmlSource);
        } catch (Exception e) {
            System.err.println("Can't take a screenshot/save HTML source: " + e.getMessage());
            e.printStackTrace(System.err);
            // LOGGER.log(Level.WARNING, "Can't take a screenshot/save HTML source.", e);
        }
    }

    protected String getSeleniumLogIdentification(ITestResult result) {
        final String failure = TestInfo.STATUSES.get(result.getStatus()).toUpperCase();
        final String started = TestInfo.STATUSES.get(ITestResult.STARTED).toUpperCase();
        String testDescription = TestLoggingUtils.getTestDescription(result);
        testDescription = testDescription.replaceFirst(failure, started);
        testDescription = testDescription.replaceFirst("\\[[^\\]]+\\] ", "");
        return testDescription;
    }

    protected String getFilenameIdentification(ITestResult result) {
        return TestInfo.getClassMethodName(result);
    }

    protected WebDriver getWebDriver() {
        return GrapheneContext.getContextFor(Default.class).getWebDriver(TakesScreenshot.class);
    }
}
