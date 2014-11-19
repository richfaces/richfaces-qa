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
package org.richfaces.tests.metamer.ftest.a4jJSFunction;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class JSFunctionPage extends MetamerPage {

    @FindBy(css = "a[id$=callFunctionLink]")
    private WebElement linkElement;
    @FindBy(css = "span[id$=time1]")
    private WebElement time1Element;
    @FindBy(css = "span[id$=time2]")
    private WebElement time2Element;
    @FindBy(css = "span[id$=year]")
    private WebElement yearElement;
    @FindBy(css = "span[id$=autoTime]")
    private WebElement ajaxRenderedTimeElement;

    /**
     * @return the ajaxRenderedTimeElement
     */
    public WebElement getAjaxRenderedTimeElement() {
        return ajaxRenderedTimeElement;
    }

    /**
     * @return the linkElement
     */
    public WebElement getLinkElement() {
        return linkElement;
    }

    /**
     * @return the time1Element
     */
    public WebElement getTime1Element() {
        return time1Element;
    }

    /**
     * @return the time2Element
     */
    public WebElement getTime2Element() {
        return time2Element;
    }

    /**
     * @return the yearElement
     */
    public WebElement getYearElement() {
        return yearElement;
    }

}
