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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jCommandLink;

import org.jboss.test.selenium.By;
import org.jboss.test.selenium.support.ui.TextEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jCommandLinkSimple extends AbstractWebDriverTest {

    private static final By INPUT = By.xpath("//*[@class='example-cnt']//input[@type='text']");
    private static final By LINK = By.xpath("//*[@class='example-cnt']//a[text()='Say Hello']");
    private static final By OUTPUT = By.xpath("//*[@class='example-cnt']//span[@class='outhello']");
    
    @Override
    protected String getDemoName() {
        return "commandLink";
    }

    @Override
    protected String getSampleName() {
        return "commandLink";
    }

    @Test
    public void testTypeAndSend() {
        WebElement input = getWebDriver().findElement(INPUT);
        input.click();
        input.sendKeys("something");
        
        getWebDriver().findElement(LINK).click();
        
        new WebDriverWait(getWebDriver())
            .failWith("After typing something into the input and clicking on the command link, the text should appear in the output area.")
            .until(TextEquals.getInstance().locator(OUTPUT).text("Hello something !"));  
    }
    
}
