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
package org.richfaces.tests.metamer.ftest;

import static java.util.Arrays.asList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jboss.arquillian.ajocado.browser.BrowserType;
import org.jboss.arquillian.ajocado.framework.GrapheneConfigurationContext;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.network.NetworkTrafficType;
import org.jboss.test.selenium.utils.testng.TestInfo;
import org.jboss.test.selenium.utils.testng.TestLoggingUtils;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.thoughtworks.selenium.SeleniumException;

/**
 * Test listener which provides the methods injected in lifecycle of test case to catch the additional information in
 * context of test failure.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @authot <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision$
 */
public class MetamerFailureLoggingTestListener extends TestListenerAdapter {

//    protected static final Logger LOGGER = Logger.getLogger(FailureLoggingTestListener.class.getName());
    protected File mavenProjectBuildDirectory = new File(System.getProperty("maven.project.build.directory",
        "./target/"));
    protected File failuresOutputDir = new File(mavenProjectBuildDirectory, "failures");
    protected GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

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
        onFailureForSelenium1(result);

        List<String> issueList = new LinkedList<String>();
        IssueTracking issueTracking = result.getMethod().getConstructorOrMethod().getMethod()
            .getAnnotation(IssueTracking.class);
        if (issueTracking != null) {
            issueList.addAll(asList(issueTracking.value()));
        }
        issueTracking = (IssueTracking) result.getMethod().getRealClass().getAnnotation(IssueTracking.class);
        if (issueTracking != null) {
            issueList.addAll(asList(issueTracking.value()));
        }

        if (!issueList.isEmpty()) {
            String issues = StringUtils.join(issueList, "\n");
            String filenameIdentification = getFilenameIdentification(result);
            File issueTrackingOutputFile = new File(failuresOutputDir, filenameIdentification + "/issues.txt");
            try {
                FileUtils.writeStringToFile(issueTrackingOutputFile, issues);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onFailureForSelenium1(ITestResult result) {
        if (!selenium.isStarted()) {
            return;
        }

        Throwable throwable = result.getThrowable();
        String stacktrace = null;

        if (throwable != null) {
            stacktrace = ExceptionUtils.getStackTrace(throwable);
        }

        String filenameIdentification = getFilenameIdentification(result);

        String traffic;
        try {
            traffic = selenium.captureNetworkTraffic(NetworkTrafficType.PLAIN).getTraffic();
        } catch (SeleniumException e) {
            traffic = ExceptionUtils.getFullStackTrace(e);
        }

        BrowserType browser = GrapheneConfigurationContext.getProxy().getBrowser().getType();
        BufferedImage screenshot = null;

        if (browser == BrowserType.FIREFOX) {
            screenshot = selenium.captureEntirePageScreenshot();
        }

        String htmlSource = selenium.getHtmlSource();

        File stacktraceOutputFile = new File(failuresOutputDir, filenameIdentification + "/stacktrace.txt");
        File imageOutputFile = new File(failuresOutputDir, filenameIdentification + "/screenshot.png");
        File trafficOutputFile = new File(failuresOutputDir, filenameIdentification + "/network-traffic.txt");
        // File logOutputFile = new File(failuresOutputDir, filenameIdentification + "/selenium-log.txt");
        File htmlSourceOutputFile = new File(failuresOutputDir, filenameIdentification + "/html-source.html");

        try {
            File directory = imageOutputFile.getParentFile();
            FileUtils.forceMkdir(directory);

            FileUtils.writeStringToFile(stacktraceOutputFile, stacktrace);
            if (browser == BrowserType.FIREFOX) {
                ImageIO.write(screenshot, "PNG", imageOutputFile);
            }
            FileUtils.writeStringToFile(trafficOutputFile, traffic);
            // FileUtils.writeLines(logOutputFile, methodLog);
            FileUtils.writeStringToFile(htmlSourceOutputFile, htmlSource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getSeleniumLogIdentification(ITestResult result) {
        final String failure = TestInfo.STATUSES.get(result.getStatus()).toUpperCase();
        final String started = TestInfo.STATUSES.get(ITestResult.STARTED).toUpperCase();
        String testDescription = TestLoggingUtils.getTestDescription(result);
        testDescription = testDescription.replaceFirst(failure, started);
        testDescription = testDescription.replaceFirst("\\[[^\\]]+\\] ", "");
        return testDescription + " " + MetamerTestInfo.getConfigurationInfoInParenthesses();
    }

    protected String getFilenameIdentification(ITestResult result) {
        return MetamerTestInfo.getAssociatedFilename(result);
    }

}
