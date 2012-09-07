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

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jLog.LogPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jLogSimple extends AbstractWebDriverTest<LogPage> {

    @Test
    public void testLogAndClear() {
        Select select = new Select(getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.INFO.getIndex());
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After setting severity to <info> and submitting, the logging area should contain a message with severity <info>.")
            .until(Graphene.element(getPage().getLoggingArea()).textContains("info"));
        getPage().getClear().click();
        Graphene.waitAjax()
            .withMessage("After setting severity to <info>, submitting and clicking on the clear button, the logging area should be empty.")
            .until(Graphene.element(getPage().getLoggingArea()).textEquals(""));
    }

    @Test(groups = { "RF-11479" })
    public void testLogDebug() {
        Select select = new Select(getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.DEBUG.getIndex());
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After setting severity to <debug> and submitting, the logging area should contain a message with severity <debug>.")
            .until(Graphene.element(getPage().getLoggingArea()).textContains("debug"));
        Graphene.waitAjax()
            .withMessage("After setting severity to <debug> and submitting, the logging area should contain a message with severity <info>.")
            .until(Graphene.element(getPage().getLoggingArea()).textContains("info"));
    }

    @Test(groups = { "RF-11479" })
    public void testLogError() {
        Select select = new Select(getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.ERROR.getIndex());
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After setting severity to <error> and submitting, the logging area should contain no message.")
            .until(Graphene.element(getPage().getLoggingArea()).textEquals(""));
    }

    @Test
    public void testLogInfo() {
        Select select = new Select(getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.INFO.getIndex());
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After setting severity to <info> and submitting, the logging area should contain a message with severity <info>.")
            .until(Graphene.element(getPage().getLoggingArea()).textContains("info"));
    }

    @Test(groups = { "RF-11479" })
    public void testLogWarn() {
        Select select = new Select(getPage().getSeveritySelect());
        select.selectByIndex(LogPage.Severity.WARN.getIndex());
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After setting severity to <warn> and submitting, the logging area should contain no message.")
            .until(Graphene.element(getPage().getLoggingArea()).textEquals(""));
    }

    @Test
    public void testSubmitEmpty() {
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After submitting empty input, the output should contain nothing.")
            .until(Graphene.element(getPage().getOutput()).textEquals(""));
    }

    @Test(groups = {"RF-12146"})
    public void testSubmitSomething() {
        getPage().getInput().click();
        getPage().getInput().sendKeys("something");
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After submitting the input, the content of the output should match.")
            .until(Graphene.element(getPage().getOutput()).textEquals("Hello something!"));
    }

    @Override
    protected LogPage createPage() {
        return new LogPage();
    }

}
