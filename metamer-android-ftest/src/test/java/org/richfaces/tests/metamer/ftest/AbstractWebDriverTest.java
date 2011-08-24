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
package org.richfaces.tests.metamer.ftest;

import java.net.MalformedURLException;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.android.AndroidDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractWebDriverTest extends AbstractMetamerTest {
  
    private WebDriver webDriver;
    
    @Drone
    WebDriver webDriverFromDrone;
    
    @BeforeClass
    public void initializeWebDriver() throws MalformedURLException{
        if (isAndroid()) {
            webDriver = new AndroidDriver(System.getProperty("webdriver.android.url", "http://localhost:4444/wd/hub"));
        }
        else {
            // TODO
//            webDriver = webDriverFromDrone;
        }
    }
    
    @BeforeMethod(alwaysRun = true)
    public void loadPage() {
        getWebDriver().get(getContextRoot() + "/" + getContextPath() + "/" + getTestUrl());
    }
   
    @Override
    protected String getContextRoot() {
        if (isAndroid()) {
            return super.getContextRoot().replaceFirst("localhost", "10.0.2.2");
        }
        else {
            return super.getContextRoot();
        }
    }
    
    protected WebDriver getWebDriver() {
        return webDriver;
    }
    
    private boolean isAndroid() {
        return System.getProperty("webdriver.android") != null;
    }
    
}
