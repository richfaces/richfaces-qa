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

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
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
    @Drone
    private WebDriver webDriver;

    // Menu groups in left menu
    Map<String, String> groups;

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
    @BeforeMethod(alwaysRun = true)
    public void initializeWebDriver() throws MalformedURLException {
        webDriver.manage().timeouts().implicitlyWait(getConfiguration().getWebDriverTimeout(), TimeUnit.SECONDS);
    }

    @BeforeMethod(alwaysRun = true, dependsOnMethods = { "initializeWebDriver" })
    public void initializePage() {
        initializePage(getPage());
    }

    /**
     * @throws MalformedURLException
     */
    @BeforeMethod(alwaysRun = true, dependsOnMethods = { "initializePage" })
    public void initializePageUrl() throws MalformedURLException {
        if (runInPortalEnv) {
            String url = format("{0}://{1}:{2}/{3}", getDeployedURL().getProtocol(), getDeployedURL().getHost(),
                getDeployedURL().getPort(), "portal/classic/showcase");
            if (getConfiguration().isVerbose()) {
                System.out.println("opening " + url);
            }
            webDriver.get(url);
            openComponentExamplePageInPortal();
        } else {
            if (getConfiguration().isVerbose()) {
                System.out.println("opening " + getPath().toString());
            }
            webDriver.get(getPath().toString());
        }
    }

    protected void openComponentExamplePageInPortal() {
        // System.out.println(" DemoName: " + getDemoName() + "\n SampleName: " + getSampleName() + "\n Page: " + getPage());
        webDriver.findElement(By.xpath(format("td[@class=rf-pm-top-gr-lbl][text()='{0}']", groups.get(getDemoName())))).click();
        webDriver.findElement(By.partialLinkText(format("{0}", getDemoName()))).click();
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

    protected void sendKeysToInputCarefully(final WebElement input, final String text) {
        input.click();
        final String valueBefore = input.getAttribute("value") == null ? "" : input.getAttribute("value");
        input.sendKeys(text);
        if (getConfiguration().isVerbose()) {
            System.out.println("The text <" + text + "> has been typed into the given input.");
        }
        Graphene.waitAjax()
                .withMessage("The text can't be typed into the given input.")
                .until(Graphene.attribute(input, "value").valueEquals(valueBefore + text));
    }

    private FieldDecorator getFieldDecorator() {
        if (fieldDecorator == null) {
            fieldDecorator = new StaleReferenceAwareFieldDecorator(createLocatorFactory(), getConfiguration().getWebDriverElementTries());
        }
        return fieldDecorator;
    }

    @BeforeClass
    private void initComponentMenuGroups() {
        if (groups == null ) {
            groups = new HashMap<String, String>();
            groups.put("ajax", "Ajax Action");
            groups.put("commandButton", "Ajax Action");
            groups.put("commandLink", "Ajax Action");
            groups.put("actionListener", "Ajax Action");
            groups.put("jsFunction", "Ajax Action");
            groups.put("poll", "Ajax Action");
            groups.put("param", "Ajax Action");

            groups.put("queue", "Ajax Queue");
            groups.put("attachQueue", "Ajax Queue");

            groups.put("outputPanel", "Ajax Output/Containers");
            groups.put("status", "Ajax Output/Containers");
            groups.put("region", "Ajax Output/Containers");
            groups.put("mediaOutput", "Ajax Output/Containers");
            groups.put("log", "Ajax Output/Containers");

            groups.put("Client Side Validation", "Validation");
            groups.put("graphValidator", "Validation");
            groups.put("message", "Validation");
            groups.put("messages", "Validation");
            groups.put("notify", "Validation");

            groups.put("repeat", "Data Iteration");
            groups.put("dataTable", "Data Iteration");
            groups.put("extendedDataTable", "Data Iteration");
            groups.put("collapsibleSubTable", "Data Iteration");
            groups.put("dataScroller", "Data Iteration");
            groups.put("list", "Data Iteration");
            groups.put("dataGrid", "Data Iteration");

            groups.put("tree", "Trees");
            groups.put("Tree Adaptors", "Trees");

            groups.put("panel", "Output/Panels");
            groups.put("togglePanel", "Output/Panels");
            groups.put("tabPanel", "Output/Panels");
            groups.put("collapsiblePanel", "Output/Panels");
            groups.put("accordion", "Output/Panels");
            groups.put("popupPanel", "Output/Panels");
            groups.put("progressBar", "Output/Panels");
            groups.put("tooltip", "Output/Panels");

            groups.put("panelMenu", "Menus");
            groups.put("toolbar", "Menus");
            groups.put("contextMenu", "Menus");
            groups.put("dropDownMenu", "Menus");

            groups.put("autocomplete", "Inputs");
            groups.put("calendar", "Inputs");
            groups.put("editor", "Inputs");
            groups.put("inputNumberSlider", "Inputs");
            groups.put("inputNumberSpinner", "Inputs");
            groups.put("InplaceInput", "Inputs");
            groups.put("fileUpload", "Inputs");

            groups.put("inplaceSelect", "Selects");
            groups.put("select", "Selects");
            groups.put("orderingList", "Selects");
            groups.put("pickList", "Selects");

            groups.put("Drag and Drop", "Drag and Drop");

            groups.put("Standard elements skinning", "Miscellaneous");
            groups.put("RichFaces functions", "Miscellaneous");
            groups.put("componentControl", "Miscellaneous");
            groups.put("hashParam", "Miscellaneous");
            groups.put("hotKey", "Miscellaneous");
            groups.put("jQuery", "Miscellaneous");
        }
    }

    protected abstract Page createPage();
}
