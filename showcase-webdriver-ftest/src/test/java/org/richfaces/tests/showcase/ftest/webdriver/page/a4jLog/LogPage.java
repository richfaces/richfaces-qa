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
package org.richfaces.tests.showcase.ftest.webdriver.page.a4jLog;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class LogPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//button")
    private WebElement clear;
    @FindBy(xpath = "//*[@class='example-cnt']//input[@type='text']")
    private WebElement input;
    @FindBy(xpath = "//*[@class='example-cnt']//div[@class='rf-log-contents']")
    private WebElement loggingArea;
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'out')]")
    private WebElement output;
    @FindBy(xpath = "//*[@class='example-cnt']//select")
    private WebElement severitySelect;
    @FindBy(xpath = "//*[@class='example-cnt']//input[@type='submit']")
    private WebElement submit;

    @Override
    public String getDemoName() {
        return "log";
    }

    @Override
    public String getSampleName() {
        return "log";
    }

    public WebElement getClear() {
        return clear;
    }

    public WebElement getInput() {
        return input;
    }

    public WebElement getLoggingArea() {
        return loggingArea;
    }

    public WebElement getOutput() {
        return output;
    }

    public WebElement getSeveritySelect() {
        return severitySelect;
    }

    public WebElement getSubmit() {
        return submit;
    }

    public static enum Severity {
        DEBUG(0), INFO(1), WARN(2), ERROR(3);
        private int index;

        private Severity(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public String toString() {
            return super.toString().toLowerCase();
        }
    }

}
