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

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Capabilities;

public class PropertyTestConfiguration implements TestConfiguration {

    public static final String DEFAULT_CONTEXT_PATH = "showcase";
    public static final String DEFAULT_CONTEXT_ROOT = "http://localhost:8080";
    public static final String DEFAULT_CONTEXT_ROOT_ANDROID = "http://10.0.2.2:8080";
    public static final String DEFAULT_SKIN_NAME = "blueSky";
    public static final String DEFAULT_WEBDRIVER_HOST = "";
    public static final String DEFAULT_WEBDRIVER_HOST_ANDROID = "http://localhost:4444/wd/hub";
    public static final Capabilities DEFAULT_WEBDRIVER_CAPABALITIES = DesiredCapabilities.firefox();
    
    private static Map<String, Capabilities> availableCapabilities;
    
    public String getWebDriverHost() {
        if (isAndroid()) {
            return System.getProperty("webdriver.host", DEFAULT_WEBDRIVER_HOST_ANDROID);
        }
        else {
            return System.getProperty("webdriver.host", DEFAULT_WEBDRIVER_HOST);
        }
        
    }

    public String getContextPath() {
        return System.getProperty("context.path", DEFAULT_CONTEXT_PATH);
    }

    public String getContextRoot() {
        if (isAndroid()) {
            return System.getProperty("context.root", DEFAULT_CONTEXT_ROOT_ANDROID);
        }
        else {
            return System.getProperty("context.root", DEFAULT_CONTEXT_ROOT);
        }
    }

    public String getSkinName() {
        return System.getProperty("skin.name", DEFAULT_SKIN_NAME);
    }
    
    @Override
    public Capabilities getWebDriverCapabilities() {
        if (availableCapabilities == null) {
            availableCapabilities = new HashMap<String, Capabilities>();
            availableCapabilities.put("android", DesiredCapabilities.android());
            availableCapabilities.put("chrome", DesiredCapabilities.chrome());
            availableCapabilities.put("firefox", DesiredCapabilities.firefox());
            availableCapabilities.put("internetExplorer", DesiredCapabilities.internetExplorer());
            availableCapabilities.put("iphone", DesiredCapabilities.iphone());
            availableCapabilities.put("opera", DesiredCapabilities.opera());
        }
        
        if (System.getProperty("webdriver.capabilities") == null) {
            return DEFAULT_WEBDRIVER_CAPABALITIES;  
        }
        else {
            if (availableCapabilities.get(System.getProperty("webdriver.capabilities")) == null) {
                throw new IllegalStateException("the capabalities called [" + System.getProperty("webdriver.capabilities")  + "] are not available. Available are " + availableCapabilities.keySet() + ".");
            }
            return availableCapabilities.get(System.getProperty("webdriver.capabilities"));
        }
    }    
    
    public boolean isAndroid() {
        return System.getProperty("webdriver.android") != null;
    }

    public boolean isMobile() {
        if (System.getProperty("showcase.layout") != null) {
            return System.getProperty("showcase.layout") == "mobile";
        }
        return isAndroid();
    }
   
}
 