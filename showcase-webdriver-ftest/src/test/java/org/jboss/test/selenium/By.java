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
package org.jboss.test.selenium;

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class By extends org.openqa.selenium.By {

    private String rawLocator;
    private org.openqa.selenium.By wrapped;
    
    protected By(org.openqa.selenium.By wrapped, String rawLocator) {
        this.rawLocator = rawLocator;
        this.wrapped = wrapped;
    }
    
    /**
     * Finds elements based on the value of the "class" attribute. If an element has many classes
     * then this will match against each of them. For example if the value is "one two onone", then the
     * following "className"s will match: "one" and "two"
     *
     * @param className The value of the "class" attribute to search for
     * @return a By which locates elements by the value of the "class" attribute.
     */
    public static By className(final String className) {
        return new By(org.openqa.selenium.By.className(className), className);
    }
    
    /**
     * Finds elements via the driver's underlying W3 Selector engine. If the browser does not
     * implement the Selector API, a best effort is made to emulate the API. In this case,
     * we strive for at least CSS2 support, but offer no guarantees.
     */
    public static By cssSelector(final String cssSelector) {
        return new By(org.openqa.selenium.By.cssSelector(cssSelector), cssSelector);
    }    
    
    /**
     * @param id The value of the "id" attribute to search for
     * @return a By which locates elements by the value of the "id" attribute.
     */    
    public static By id(final String id) {
        return new By(org.openqa.selenium.By.id(id), id);
    }    
    
    /**
     * @param linkText The exact text to match against
     * @return a By which locates A elements by the exact text it displays
     */    
    public static By linkText(final String linkText) {
        return new By(org.openqa.selenium.By.linkText(linkText), linkText);
    }    
    
    /**
     * @param name The value of the "name" attribute to search for
     * @return a By which locates elements by the value of the "name" attribute.
     */
    public static By name(final String name) {
        return new By(org.openqa.selenium.By.name(name), name);
    }    

    /**
     * @param linkText The text to match against
     * @return a By which locates A elements that contain the given link text
     */
    public static By partialLinkText(final String partialLinkText) {
        return new By(org.openqa.selenium.By.partialLinkText(partialLinkText), partialLinkText);
    }      

    /**
     * @param name The element's tagName
     * @return a By which locates elements by their tag name
     */
    public static By tagName(final String tagName) {
        return new By(org.openqa.selenium.By.tagName(tagName), tagName);
    }  
    
    /**
     * @param xpathExpression The xpath to use
     * @return a By which locates elements via XPath
     */
    public static By xpath(final String xpathExpression) {
        return new By(org.openqa.selenium.By.xpath(xpathExpression), xpathExpression);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return wrapped.findElements(context);
    }    
    
    /**
     * @return the raw locator which has been used to create this element descriptor
     */
    public String getRawLocator() {
        return rawLocator;
    }
    
    public String toString() {
        return wrapped.toString();
    }
}
