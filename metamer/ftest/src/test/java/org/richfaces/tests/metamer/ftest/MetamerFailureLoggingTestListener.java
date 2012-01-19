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

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.test.selenium.listener.FailureLoggingTestListener;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.ITestResult;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22728 $
 */
public class MetamerFailureLoggingTestListener extends FailureLoggingTestListener {
    @Override
    protected String getFilenameIdentification(ITestResult result) {
        return MetamerTestInfo.getAssociatedFilename(result);
    }

    @Override
    protected String getSeleniumLogIdentification(ITestResult result) {
        String id = super.getSeleniumLogIdentification(result);
        return id + " " + MetamerTestInfo.getConfigurationInfoInParenthesses();
    }

    @Override
    protected void onFailure(ITestResult result) {
        super.onFailure(result);
        
        List<String> issueList = new LinkedList<String>();
        //IssueTracking issueTracking = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(IssueTracking.class);
        IssueTracking issueTracking = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(IssueTracking.class);
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
}
