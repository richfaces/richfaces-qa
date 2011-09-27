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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jAjax;

import org.jboss.test.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractAndroidTest;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jAjaxSimple extends AbstractAndroidTest {

    private static final By INPUT = By.xpath("//*[@class='example-cnt']//input[@type='text']");
    private static final By OUTPUT = By.xpath("//*[@class='example-cnt']//span");
    
    @Override
    protected String getDemoName() {
        return "ajax";
    }

    @Override
    protected String getSampleName() {
        return "ajax";
    }

    @Test
    public void testType() {
        getWebDriver().findElement(INPUT).sendKeys("something");
        
        new WebDriverWait(getWebDriver(), 10).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                try {
                    return d.findElement(OUTPUT).getText().equals("something");
                } catch(StaleElementReferenceException ignored) {
                    return false;
                }
            }            
        });
    }

}
