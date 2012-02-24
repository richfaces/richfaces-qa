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
package org.richfaces.tests.showcase.ftest.webdriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.jboss.test.selenium.android.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
* @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
*/
public abstract class AbstractWebDriverTest<Page extends ShowcasePage> extends AbstractShowcaseTest {

    private FieldDecorator fieldDecorator;
    private Page page;
    private WebDriver webDriver;

    /**
     * Creates a new instance of {@link AbstractWebDriverTest}
     * with the default configuration {@link PropertyTestConfiguration}
     */
    public AbstractWebDriverTest() {
        this(new PropertyTestConfiguration());
    }

    /**
     * Creates a new instance of {@link AbstractWebDriverTest} with
     * the given configuration
     *
     * @param configuration
     */
    public AbstractWebDriverTest(TestConfiguration configuration) {
        super(configuration);
    }

    /**
     * Initializes web driver instance
     *
     * @throws MalformedURLException
     */
    @BeforeClass(alwaysRun = true)
    public void initializeWebDriver() throws MalformedURLException {
        if (getConfiguration().isAndroid()) {
            webDriver = new AndroidDriver(getConfiguration().getWebDriverHost());
        }
        else {
            webDriver = new HtmlUnitDriver(getConfiguration().getWebDriverCapabilities());
        }
        webDriver.manage().timeouts().implicitlyWait(getConfiguration().getWebDriverTimeout(), TimeUnit.SECONDS);
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

    @AfterMethod(alwaysRun = true)
    public void takeScreenShotOnFailure(ITestResult result) throws WebDriverException, IOException {
        if (!result.isSuccess() && getWebDriver() instanceof TakesScreenshot) {
            TakesScreenshot screenShotter = (TakesScreenshot) (getWebDriver());
            FileUtils.copyFile(screenShotter.getScreenshotAs(OutputType.FILE), new File("target" + File.separator + "screenshots" + File.separator + result.getTestClass().getName().replace(".", File.separator) + result.getMethod().getMethodName() + ".png"));
        }
    }

    protected ElementLocatorFactory createLocatorFactory() {
        return new DefaultElementLocatorFactory(getWebDriver());
//        return new AjaxElementLocatorFactory(getWebDriver(), getConfiguration().getWebDriverTimeout());
    }

    @Override
    protected String getDemoName() {
        return getPage().getDemoName();
    }

    protected Page getPage() {
        if (page == null) {
            page = createPage();
        }
        return page;
    }

    @Override
    protected String getSampleName() {
        return getPage().getSampleName();
    }

    /**
     * Returns a web driver
     *
     * @return web driver
     */
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

    protected abstract Page createPage();
}
