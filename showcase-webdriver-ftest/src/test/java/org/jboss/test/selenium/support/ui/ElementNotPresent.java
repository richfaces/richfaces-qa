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

import org.jboss.test.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ElementNotPresent implements ExpectedCondition<Boolean> {

    private By locator;
    
    public static ElementNotPresent getInstance() {
        return new ElementNotPresent();
    }
    
    public Boolean apply(WebDriver driver) {
        try {
            return !driver.findElement(locator).isDisplayed();
        } catch(StaleElementReferenceException ignored) {
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }    
    }
    
    public ElementNotPresent locator(By locator) {
        ElementNotPresent copy = copy();
        copy.locator = locator;
        return copy;
    }
    
    private ElementNotPresent copy() {
        ElementNotPresent copy = new ElementNotPresent();
        copy.locator = this.locator;
        return copy;
    }    
    
}
