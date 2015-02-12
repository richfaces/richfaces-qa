/**
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
package org.jboss.test.selenium.listener;

import static java.text.MessageFormat.format;

import static org.jboss.test.selenium.utils.testng.TestInfo.getPackageClassMethodName;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.google.common.collect.Sets;

/**
 * This class is used as ITestListener in testNG tests to save browser console output to a file after each test method.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BrowserConsoleLogSaverListener extends TestListenerAdapter {

    private static final boolean APPEND = true;
    private static final File BUILD_DIRECTORY = new File(System.getProperty("maven.project.build.directory", "./target/"));
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final Set<Level> LOOK_FOR_MESSAGES_LEVEL = Sets.newHashSet(Level.INFO, Level.WARNING, Level.SEVERE);
    private static final String NEW_LINE = "\n";
    private static final File OUTPUT_FILE = new File(BUILD_DIRECTORY, "browserConsole.log");
    private static final String[] POSSIBLE_CONSOLE_ERRORS = new String[] { "error", "undefined" };

    private int errorsCount = 0;

    private WebDriver getWebDriver() {
        try {
            return GrapheneContext.getContextFor(Default.class).getWebDriver();
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        if (errorsCount > 0) {
            System.out.println();
            System.out.println(format("Encountered <{0}> {1} in browser console during testing. You can check the log at <{2}>.",
                errorsCount, errorsCount > 1 ? "errors" : "error", OUTPUT_FILE.getAbsolutePath()));
            System.out.println();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        saveLog(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        saveLog(result);
    }

    /**
     * Save the browser console log to file ${maven.project.build.directory}/browserConsole.log
     */
    private void saveLog(ITestResult result) {
        WebDriver wd = getWebDriver();
        if (wd == null) {
            System.err.println("Can't save the browser console logs, because there is no driver available.");
            return;
        }
        LogEntries logEntries = wd.manage().logs().get(LogType.BROWSER);
        String msg;
        try {
            FileUtils.forceMkdir(OUTPUT_FILE.getParentFile());
            for (LogEntry entry : logEntries) {
                if (LOOK_FOR_MESSAGES_LEVEL.contains(entry.getLevel())) {
                    msg = format("{0} method: {1}, level: {2}, message: {3}", DATE_FORMAT.format(new Date(entry.getTimestamp())),
                        getPackageClassMethodName(result), entry.getLevel(), entry.getMessage());
                    for (String error : POSSIBLE_CONSOLE_ERRORS) {
                        if (msg.toLowerCase().contains(error)) {
                            errorsCount++;
                            break;
                        }
                    }
                    if (!msg.endsWith("\n")) {
                        msg = msg.concat(NEW_LINE);
                    }
                    FileUtils.writeStringToFile(OUTPUT_FILE, msg, APPEND);
                }
            }
        } catch (IOException ex) {
            System.err.println("Can't save the browser console logs: " + ex.getMessage());
        }
    }
}
