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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jStatus;

import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jStatus.ViewUsagePage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jStatusSimple extends AbstractWebDriverTest<ViewUsagePage>{

    @Test
    public void testSubmitSearch() {
        getPage().getSearchInput().click();
        getPage().getSearchInput().sendKeys("something");
        getPage().getSearchSubmit().click();
        new WebDriverWait(getWebDriver())
        .failWith("After submitting the search, the request image should be present.")
        .until(ElementPresent.getInstance().element(getPage().getRequestImage()));            
    }
    
    @Test
    public void testSubmitUser() {
        getPage().getUsernameInput().click();
        getPage().getUsernameInput().sendKeys("something");
        getPage().getAddressInput().click();
        getPage().getAddressInput().sendKeys("something");
        getPage().getUserSubmit().click();
        new WebDriverWait(getWebDriver())
            .failWith("After submitting the username and the address, the request image should be present.")
            .until(ElementPresent.getInstance().element(getPage().getRequestImage()));        
        new WebDriverWait(getWebDriver())
            .failWith("After submitting the username and the address, the output text should be present.")
            .until(ElementPresent.getInstance().element(getPage().getUserOutput()));
        assertEquals(getPage().getUserOutput().getText(), "User stored succesfully");
    }    
    
    @Test
    public void testTypeAddress() {
        getPage().getAddressInput().click();
        getPage().getAddressInput().sendKeys("something");
        new WebDriverWait(getWebDriver())
            .failWith("After typing the address, the request image should be present.")
            .until(ElementPresent.getInstance().element(getPage().getRequestImage()));        
    }
    
    @Test
    public void testTypeSearch() {
        getPage().getSearchInput().click();
        getPage().getSearchInput().sendKeys("something");
        assertTrue(ElementNotPresent.getInstance().element(getPage().getRequestImage()).apply(getWebDriver()), "After typing a text into the search input field, the request image shouldn't be present.");
    }
    
    @Test
    public void testTypeUsername() {
        getPage().getUsernameInput().click();
        getPage().getUsernameInput().sendKeys("something");
        new WebDriverWait(getWebDriver())
            .failWith("After typing the username, the request image should be present.")
            .until(ElementPresent.getInstance().element(getPage().getRequestImage()));
    }
    
    @Override
    protected ViewUsagePage createPage() {
        return new ViewUsagePage();
    }

}
