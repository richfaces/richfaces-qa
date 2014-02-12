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
package org.richfaces.tests.showcase.ftest.webdriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
* @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
*/
public abstract class AbstractWebDriverTest extends AbstractShowcaseTest {

    @Drone
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
     * @throws MalformedURLException
     */
    @BeforeMethod(alwaysRun = true)
    public void initializePageUrl() throws MalformedURLException {
        if (getConfiguration().isVerbose()) {
            System.out.println("opening " + getPath().toString());
        }
        webDriver.get(getPath().toString());
    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenShotOnFailure(ITestResult result) throws WebDriverException, IOException {
        if (!result.isSuccess() && getWebDriver() instanceof TakesScreenshot) {
            TakesScreenshot screenShotter = (TakesScreenshot) (getWebDriver());
            FileUtils.copyFile(screenShotter.getScreenshotAs(OutputType.FILE), new File("target" + File.separator + "screenshots" + File.separator + result.getTestClass().getName().replace(".", File.separator) + result.getMethod().getMethodName() + ".png"));
        }
    }

    @Override
    protected String getDemoName() {
        return getPage().getDemoName();
    }

    @Override
    protected URL getDeployedURL() throws MalformedURLException {
        if (!(webDriver instanceof AndroidDriver) || getConfiguration().getContextRoot() != null) {
            return super.getDeployedURL();
        } else {
            return new URL(super.getDeployedURL().toString().replace(super.getDeployedURL().getHost(), "10.0.2.2"));
        }
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

    protected void sendKeysToInputCarefully(final WebElement input, final String text) {
        input.click();
        final String valueBefore = input.getAttribute("value") == null ? "" : input.getAttribute("value");
        input.sendKeys(text);
        if (getConfiguration().isVerbose()) {
            System.out.println("The text <" + text + "> has been typed into the given input.");
        }
        Graphene.waitAjax()
                .withMessage("The text can't be typed into the given input.")
            .until(new WebElementConditionFactory(input).attribute("value").equalTo(valueBefore + text));
    }

    protected abstract ShowcasePage getPage();
}
