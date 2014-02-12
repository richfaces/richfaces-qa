/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import javax.naming.directory.NoSuchAttributeException;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class AttributeContains implements ExpectedCondition<Boolean> {

    private WebElement element;
    private String attribute;
    private String attrValue;

    public static AttributeContains getInstance() {
        return new AttributeContains();
    }

    public Boolean apply(WebDriver driver) {
        try {
            String tempAttribute = element.getAttribute(attribute);
            if (tempAttribute != null) {
                return tempAttribute.contains(attrValue);
            } else {
                throw new NoSuchAttributeException("@" + attribute + " not found" );
            }
        } catch(NoSuchElementException ignored) {
            return false;
        } catch (NoSuchAttributeException ignored2) {
            return false;
        }
    }

    public AttributeContains element(WebElement element) {
        AttributeContains copy = copy();
        copy.element = element;
        return copy;
    }

    public AttributeContains attributeName(String attributeName) {
        AttributeContains copy = copy();
        copy.attribute = attributeName;
        return copy;
    }

    public AttributeContains attributeValue(String attributeValue) {
        AttributeContains copy = copy();
        copy.attrValue = attributeValue;
        return copy;
    }

    private AttributeContains copy() {
        AttributeContains copy = new AttributeContains();
        copy.element = this.element;
        copy.attribute = this.attribute;
        copy.attrValue = this.attrValue;
        return copy;
    }
}
