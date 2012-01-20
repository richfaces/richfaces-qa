/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.archetypes;

import org.jboss.test.selenium.webdriver.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractWebDriverTest<P extends Page> extends AbstractTest {

    private FieldDecorator fieldDecorator;
    private P page;
    public WebDriver webDriver;

    protected AbstractWebDriverTest(TestConfiguration configuration) {
        super(configuration);
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = { "initializeWebDriver" })
    public void initializePage() {
        initializePage(getPage());
    }

    /**
     * Initializes web driver to open a test page
     */
    @BeforeMethod(alwaysRun = true)
    public void initializePageUrl() {
        webDriver.get(getPath());
    }

    /**
     * Initializes web driver instance
     */
    @BeforeClass(alwaysRun = true)
    public void initializeWebDriver() {
        webDriver = new HtmlUnitDriver(DesiredCapabilities.chrome());
    }

    protected P getPage() {
        if (page == null) {
            page = createPage();
        }
        return page;
    }

    protected String getTestUrl() {
        return getPage().getUrl();
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    protected void initializePage(Object page) {
        PageFactory.initElements(getFieldDecorator(), page);
    }

    private FieldDecorator getFieldDecorator() {
        if (fieldDecorator == null) {
            fieldDecorator = new StaleReferenceAwareFieldDecorator(createLocatorFactory(), getConfiguration().getWebDriverElementTries());
        }
        return fieldDecorator;
    }

    private ElementLocatorFactory createLocatorFactory() {
        return new DefaultElementLocatorFactory(getWebDriver());
    }

    protected abstract P createPage();

}
