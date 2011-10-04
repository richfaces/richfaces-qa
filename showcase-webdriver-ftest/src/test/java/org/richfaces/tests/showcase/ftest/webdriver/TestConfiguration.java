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

import org.jboss.test.selenium.android.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.support.pagefactory.AjaxElementLocator;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface TestConfiguration {

    /**
     * Context path will be used to retrieve pages from right URL. Don't hesitate to use it in cases of building absolute
     * URLs.
     * 
     * @return context path
     */    
    String getContextPath();
    
    /**
     * Context root can be used to obtaining full URL paths, is set to actual tested application's context root
     * 
     * @return context root
     */        
    String getContextRoot();

    String getSkinName();
    
    /**
     * Returns web driver capabalities
     * 
     * @return web driver capabalities
     */
    Capabilities getWebDriverCapabilities();
    
    /**
     * URL which is used to control web driver
     *  
     * @return webdriver host URL
     */
    String getWebDriverHost();    
    
    /**
     * Returns number of tries which is used for {@link StaleReferenceAwareFieldDecorator}
     * 
     * @return number of tries
     */
    int getWebDriverElementTries();

    /**
     * Returns timout in seconds which is used for {@link AjaxElementLocator}
     * and other timout settings
     * 
     * @return timout in seconds
     */
    int getWebDriverTimeout();    
    
    /**
     * Checks whether the tests are executed on an android web driver
     * 
     * @return true if the android web driver is enabled, otherwise false
     */
    boolean isAndroid(); 
    
    /**
     * Checks whether the tests should use mobile version of the Showcase
     * 
     * @return true if the Showcase should be in mobile version
     */
    boolean isMobile();
    
}
