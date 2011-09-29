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
package org.jboss.test.selenium.support.ui;

import org.apache.commons.lang.Validate;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.Sleeper;

import com.google.common.base.Function;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebDriverWait extends org.openqa.selenium.support.ui.WebDriverWait {

    public static final long DEFAULT_TIMEOUT = 10;    
    private String message;
    
    protected WebDriverWait(WebDriver driver, Clock clock, Sleeper sleeper, long timeOutInSeconds, long sleepTimeOut) {
        super(driver, clock, sleeper, timeOutInSeconds, sleepTimeOut);
    }

    public WebDriverWait(WebDriver driver, long timeOutInSeconds, long sleepInMillis) {
        super(driver, timeOutInSeconds, sleepInMillis);
    }

    public WebDriverWait(WebDriver driver, long timeOutInSeconds) {
        super(driver, timeOutInSeconds);
    }
    
    public WebDriverWait(WebDriver driver) {
        super(driver, DEFAULT_TIMEOUT);
    }

    public WebDriverWait failWith(String message) {
        Validate.notNull(message);
        this.message = message;
        return this;
    }
    
    public <V> V until(Function<? super WebDriver, V> isTrue) {
        if (message == null) {
            return super.until(isTrue);
        } else {
            try {
                return super.until(isTrue);
            } catch(TimeoutException e) {
                throw new TimeoutException(message, e);
            }
        }
    }
        
}
