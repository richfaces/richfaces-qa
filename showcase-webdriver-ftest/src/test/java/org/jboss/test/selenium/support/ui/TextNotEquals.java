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
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TextNotEquals implements ExpectedCondition<Boolean> {

    private By locator;
    private String text;

    public static TextNotEquals getInstance() {
        return new TextNotEquals();
    }    
    
    @Override
    public Boolean apply(WebDriver driver) {
        try {
            return !driver.findElement(locator).getText().equals(text);
        } catch(StaleElementReferenceException ignored) {
            return false;
        }
    } 

    public TextNotEquals locator(By locator) {
        TextNotEquals copy = copy();
        copy.locator = locator;
        return copy;
    }

    public TextNotEquals text(String text) {
        TextNotEquals copy = copy();
        copy.text = text;
        return copy;
    }    
    
    private TextNotEquals copy() {
        TextNotEquals copy = new TextNotEquals();
        copy.locator = locator;
        copy.text = text;
        return copy;
    }

}
