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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jLog;

import org.jboss.test.selenium.android.support.ui.Select;
import org.jboss.test.selenium.support.ui.TextContains;
import org.jboss.test.selenium.support.ui.TextEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractAndroidTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jLog.LogPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jLogSimple extends AbstractAndroidTest<LogPage> {

    @Test
    public void testLogAndClear() {
        Select select = new Select(getWebDriver(), getToolKit(), getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.INFO.getIndex());
        getPage().getSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After setting severity to <info> and submitting, the logging area should contain a message with severity <info>.")
            .until(TextContains.getInstance().element(getPage().getLoggingArea()).text("info"));
        getPage().getClear().click();
        new WebDriverWait(getWebDriver())
            .failWith("After setting severity to <info>, submitting and clicking on the clear button, the logging area should be empty.")
            .until(TextContains.getInstance().element(getPage().getLoggingArea()).text(""));
    }

    // https://issues.jboss.org/browse/RF-11872
    @Test(groups = { "4.Future" })
    public void testLogDebug() {
        Select select = new Select(getWebDriver(), getToolKit(), getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.DEBUG.getIndex());
        getPage().getSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After setting severity to <debug> and submitting, the logging area should contain a message with severity <debug>.")
            .until(TextContains.getInstance().element(getPage().getLoggingArea()).text("debug"));
        new WebDriverWait(getWebDriver())
            .failWith("After setting severity to <debug> and submitting, the logging area should contain a message with severity <info>.")
            .until(TextContains.getInstance().element(getPage().getLoggingArea()).text("info"));
    }

    // https://issues.jboss.org/browse/RF-11872
    @Test(groups = { "4.Future" })
    public void testLogError() {
        Select select = new Select(getWebDriver(), getToolKit(), getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.ERROR.getIndex());
        getPage().getSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After setting severity to <error> and submitting, the logging area should contain no message.")
            .until(TextContains.getInstance().element(getPage().getLoggingArea()).text(""));
    }

    @Test
    public void testLogInfo() {
        Select select = new Select(getWebDriver(), getToolKit(), getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.INFO.getIndex());
        getPage().getSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After setting severity to <info> and submitting, the logging area should contain a message with severity <info>.")
            .until(TextContains.getInstance().element(getPage().getLoggingArea()).text("info"));
    }

    // https://issues.jboss.org/browse/RF-11872
    @Test(groups = { "4.Future" })
    public void testLogWarn() {
        Select select = new Select(getWebDriver(), getToolKit(), getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.WARN.getIndex());
        getPage().getSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After setting severity to <warn> and submitting, the logging area should contain no message.")
            .until(TextContains.getInstance().element(getPage().getLoggingArea()).text(""));
    }

    @Test
    public void testSubmitEmpty() {
        getPage().getSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After submitting empty input, the output should contain nothing.")
            .until(TextEquals.getInstance().element(getPage().getOutput()).text(""));
    }

    @Test
    public void testSubmitSomething() {
        getPage().getInput().click();
        getPage().getInput().sendKeys("something");
        getPage().getSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After submitting the input, the content of the output should match.")
            .until(TextEquals.getInstance().element(getPage().getOutput()).text("Hello something!"));
    }

    @Override
    protected LogPage createPage() {
        return new LogPage();
    }

}
