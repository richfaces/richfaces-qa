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
package org.richfaces.tests.metamer.ftest;

import java.net.MalformedURLException;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumImpl;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.openqa.selenium.WebDriverCommandProcessor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractAjocadoTest extends AbstractWebDriverTest {

    private AjaxSeleniumImpl selenium;
    
    @BeforeClass(dependsOnMethods={"initializeWebDriver"}, alwaysRun = true)
    public void initializeAjocado() throws MalformedURLException {
        WebDriverCommandProcessor commandProcessor = new WebDriverCommandProcessor(getContextRoot(), getWebDriver());
        selenium = new AjaxSeleniumImpl(commandProcessor);
        AjocadoConfigurationContext.set(new SystemPropertiesConfiguration());
        AjaxSeleniumContext.set(selenium);
        selenium.configureBrowser();
//        selenium.initializeSeleniumExtensions();
//        selenium.initializePageExtensions();
        System.err.println("ajocado init: " + selenium);
    }

    protected AjaxSelenium getSelenium() {
        return selenium;
    }
    
    @BeforeMethod(alwaysRun = true)
    public void loadPage() {
        getSelenium().open(URLUtils.buildUrl(getPath()));
    }
    
}
