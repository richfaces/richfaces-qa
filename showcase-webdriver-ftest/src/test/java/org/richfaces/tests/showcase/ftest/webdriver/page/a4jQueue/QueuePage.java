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
package org.richfaces.tests.showcase.ftest.webdriver.page.a4jQueue;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class QueuePage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='DOM updates count:']/../td/span")
    private WebElement domUpdates;
    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='Events count:']/../td/span")
    private WebElement events;
    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='Type here:']/../td/input")
    private WebElement input;
    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='Repeated text:']/../td/span")
    private WebElement output;
    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='Request delay:']/../td/input")
    private WebElement requestDelay;
    @FindBy(xpath = "//*[@class='example-cnt']//td[text()='Requests count:']/../td/span")
    private WebElement requests;
    @Override
    public String getDemoName() {
        return "queue";
    }

    @Override
    public String getSampleName() {
        return "queue";
    }

    public WebElement getInput() {
        return input;
    }

    public WebElement getEvents() {
        return events;
    }

    public WebElement getRequests() {
        return requests;
    }

    public WebElement getUpdates() {
        return domUpdates;
    }

    public WebElement getOutput() {
        return output;
    }

    public void setRequestDelay(int delay) {
        requestDelay.click();
        requestDelay.clear();
        requestDelay.sendKeys(String.valueOf(delay));
    }

}
