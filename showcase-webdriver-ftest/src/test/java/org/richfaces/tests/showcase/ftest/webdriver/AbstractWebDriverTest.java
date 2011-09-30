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
package org.richfaces.tests.showcase.ftest.webdriver;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
* @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
*/
public abstract class AbstractWebDriverTest extends AbstractShowcaseTest {
  
    private WebDriver webDriver;
    
    /**
     * Creates a new instance of {@link AbstractWebDriverTest}
     * with the default configuration {@link PropertyTestConfiguration}
     */
    public AbstractWebDriverTest() {
        this(new PropertyTestConfiguration());
    }
    
    /**
     * Creates a new instance of {@link AbstractWebDriverTest} with
     * the given configuration
     * 
     * @param configuration
     */
    public AbstractWebDriverTest(TestConfiguration configuration) {
        super(configuration);
    }
    
    /**
     * Initializes web driver instance
     * 
     * @throws MalformedURLException
     */
    @BeforeClass(alwaysRun = true)
    public void initializeWebDriver() throws MalformedURLException {
        if (getConfiguration().isAndroid()) {
            webDriver = new AndroidDriver(getConfiguration().getWebDriverHost());
        }
        else {
            webDriver = new HtmlUnitDriver(getConfiguration().getWebDriverCapabilities());
        }
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    /**
     * Initializes web driver to open a test page 
     */
    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        webDriver.get(getPath());        
    }
    
    /**
     * Returns a web driver
     * 
     * @return web driver
     */
    protected WebDriver getWebDriver() {
        return webDriver;
    }
}
 