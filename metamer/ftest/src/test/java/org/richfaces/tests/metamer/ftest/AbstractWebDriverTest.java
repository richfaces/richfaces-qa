/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractWebDriverTest extends AbstractMetamerTest {

    @Drone
    protected WebDriver driver;
    protected static final int WAIT_TIME = 5;// s
    private static final int LAST_CHECK_WAIT_TIME = 500;// ms
    private static final int NUMBER_OF_TRIES = 5;
    private FieldDecorator fieldDecorator;

    /**
     * Opens the tested page. If templates is not empty nor null, it appends url
     * parameter with templates.
     *
     * @param templates templates that will be used for test, e.g. "red_div"
     */
    @BeforeMethod(alwaysRun = true)
    public void loadPage(Object[] templates) {
        if (driver == null) {
            throw new SkipException("webDriver isn't initialized");
        }
        driver.get(buildUrl(getTestUrl() + "?templates=" + template.toString()).toExternalForm());
        driver.manage().timeouts().pageLoadTimeout(WAIT_TIME, TimeUnit.SECONDS);
    }

    /**
     * Waiting.
     *
     * @param milis
     */
    private void waiting(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Waits a little time and executes JavaScript script.
     *
     * @param script whole command that will be executed
     * @param args
     * @return may return a value
     */
    public Object executeJS(String script, Object... args) {
        waitForFooter();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    protected void injectWebElementsToPage(Object page) {
        if (fieldDecorator == null) {
            fieldDecorator = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(driver),
                    NUMBER_OF_TRIES);
        }
        PageFactory.initElements(fieldDecorator, page);
    }

    /**
     * Wait for rendering of footer (whole page rendered now?)
     */
    protected void waitForFooter() {
        for (int i = 0; i < 3; i++) {
            try {
                new WebDriverWait(driver, 5).until(ElementPresent.getInstance().
                        element(driver.findElement(By.cssSelector("div.footer"))));
                return;
            } catch (NoSuchElementException ignored) {
            }
        }
    }
}
