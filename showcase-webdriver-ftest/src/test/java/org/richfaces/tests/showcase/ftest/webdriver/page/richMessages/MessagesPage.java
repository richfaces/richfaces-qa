/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.page.richMessages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractRichMessagePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class MessagesPage extends AbstractRichMessagePage {

    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'address')][contains(@class, 'rf-msgs-err')]")
    private WebElement addressErrorMessageArea;
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'job')][contains(@class, 'rf-msgs-err')]")
    private WebElement jobErrorMessageArea;    
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'name')][contains(@class, 'rf-msgs-err')]")
    private WebElement nameErrorMessageArea;
    @FindBy(xpath = "//*[@class='example-cnt']//span[contains(@id, 'zip')][contains(@class, 'rf-msgs-err')]")
    private WebElement zipErrorMessageArea;    
    
    @Override
    public String getDemoName() {
        return "messages";
    }

    @Override
    public String getSampleName() {
        return "messages";
    }

    public WebElement getAddressErrorMessageArea() {
        return addressErrorMessageArea;
    }

    public WebElement getJobErrorMessageArea() {
        return jobErrorMessageArea;
    }

    public WebElement getNameErrorMessageArea() {
        return nameErrorMessageArea;
    }
    
    public WebElement getZipErrorMessageArea() {
        return zipErrorMessageArea;
    }

}
