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

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.ftest.webdriver.IWEAvailabilityCondition;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;

public abstract class AbstractWebDriverTest extends AbstractMetamerTest {

    @Drone
    protected WebDriver driver;
    private boolean first = true;
    protected static final int WAIT_TIME = 5;// s
    private static final int LAST_CHECK_WAIT_TIME = 500;// ms
    private static final int NUMBER_OF_TRIES = 5;
    private FieldDecorator fieldDecorator;

    /**
     * Opens the tested page. If templates is not empty nor null, it appends url parameter with templates.
     *
     * @param templates
     *            templates that will be used for test, e.g. "red_div"
     */
    @BeforeMethod(alwaysRun = true)
    public void loadPage(Object[] templates) {
        //        addFirebug();
        if (driver == null) {
            throw new SkipException("webDriver isn't initialized");
        }
        driver.get(buildUrl(getTestUrl() + "?templates=" + template.toString()).toExternalForm());
        // webDriver.manage().timeouts().pageLoadTimeout(WAITTIME, TimeUnit.SECONDS);
    }

    /**
     * Adds firebug to firefox.
     */
    //    public void addFirebug() {
    //
    //        if (first && driver instanceof FirefoxDriver) {
    //            File file = null;
    //            try {
    //                file = new File(AbstractWebDriverTest.class.getResource("firebug-1.9.1.xpi").toURI());
    //            } catch (Exception ex) {
    //                ex.printStackTrace();
    //            }
    //            FirefoxProfile firefoxProfile = new FirefoxProfile();
    //            try {
    //                firefoxProfile.addExtension(file);
    //                firefoxProfile.setPreference("extensions.firebug.currentVersion", "1.9.1"); // Avoid startup screen
    //            } catch (IOException ex) {
    //                ex.printStackTrace();
    //            }
    //            driver = new FirefoxDriver(firefoxProfile);
    //            first = false;
    //        }
    //    }

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
     * @param script
     *            whole command that will be executed
     * @param args
     * @return may return a value
     */
    public Object executeJS(String script, Object... args) {
        waiting(LAST_CHECK_WAIT_TIME);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Types text to input component and then submits it.
     *
     * @param cssSelector
     *            selector of component
     * @param toSend
     *            text to be typed in input component
     */
    public void sendAndSubmit(String cssSelector, String toSend) {
        waitForEnabledWE(cssSelector).sendKeys(toSend);
        waitForEnabledWE(cssSelector).submit();
    }

    /**
     * Waits for list of WebElements by the specified selector. Expects 1 element.
     *
     * @param by
     *            selenium locator of root element
     * @return TimeoutException if found nothing or list with found elements.
     */
    public List<WebElement> waitForWEList(final By by) {
        return waitForWEList(by, WAIT_TIME, 1);
    }

    /**
     * Waits for list of WebElements by the specified selector. Expects 1 WebElement.
     *
     * @param cssSelector
     *            CSS selector
     * @return TimeoutException if found nothing or list with found elements.
     */
    public List<WebElement> waitForWEList(String cssSelector) {
        return waitForWEList(By.cssSelector(cssSelector), WAIT_TIME, 1);
    }

    /**
     * Waits for list of WebElements by the specified selector and with specified expected size.
     *
     * @param by
     *            selenium locator of elements
     * @param expectedSize
     *            expected list size
     * @return TimeoutException if found nothing or list with found elements.
     */
    public List<WebElement> waitForWEList(final By by, final int expectedSize) {
        return waitForWEList(by, WAIT_TIME, expectedSize);
    }

    /**
     * Waits for list of WebElements by the specified selector, with specified expected size.
     *
     * @param cssSelector
     *            cssSelector of elements
     * @param expectedSize
     *            expected list size
     * @return expected list of webElements or null
     */
    public List<WebElement> waitForWEList(String cssSelector, final int expectedSize) {
        return waitForWEList(By.cssSelector(cssSelector), WAIT_TIME, expectedSize);
    }

    /**
     * Waits for list of WebElements by the specified selector, with specified expected size and for maximum number of
     * seconds
     *
     * @param by
     *            selenium locator of elements
     * @param seconds
     *            maximum amount of seconds that it will wait
     * @param expectedSize
     *            expected list size
     * @return expected list of webElements or null
     */
    public List<WebElement> waitForWEList(final By by, int seconds, final int expectedSize) {
        List<WebElement> list = (new WebDriverWait(driver, seconds)).until(new ExpectedCondition<List<WebElement>>() {

            private boolean lastCheckMade = false;

            private List<WebElement> lastCheck(WebDriver d) {
                try {
                    Thread.sleep(LAST_CHECK_WAIT_TIME);
                } catch (InterruptedException ex) {
                }
                lastCheckMade = true;
                return apply(d);

            }

            @Override
            public List<WebElement> apply(WebDriver d) {
                List<WebElement> ll = d.findElements(by);
                if (ll.size() == expectedSize) {
                    if (expectedSize == 0) {// last check
                        if (!lastCheckMade) {
                            ll = lastCheck(d);
                        }
                    }
                    return ll;
                } else if (ll.isEmpty()) {
                    return null;
                } else if (ll.size() < expectedSize) {
                    if (!lastCheckMade) {
                        ll = lastCheck(d);
                    }
                }
                return ll;
            }
        });
        return list;
    }

    /**
     * Waits for WebElement with specific css selector which contains expected text.
     *
     * @param cssSelector
     *            css selector
     * @param excpectedString
     *            expected text that will WebElement.getText() return
     * @return WebElement with specific css selector which contains expected text
     */
    public WebElement waitForWEWithExpectedText(String cssSelector, String excpectedString) {
        return waitForAvailableWE(By.cssSelector(cssSelector), WAIT_TIME, new ExpectedStringCondition(excpectedString));
    }

    /**
     * Waits for WebElement with specific css selector which is visible and enabled.
     *
     * @param cssSelector
     *            css selector
     * @return visible and enabled web element with specified css selector
     */
    public WebElement waitForEnabledVisibleWE(String cssSelector) {
        return waitForAvailableWE(By.cssSelector(cssSelector), WAIT_TIME, new VisibleElementCondition(),
            new EnabledElementCondition());
    }

    /**
     * Waits for WebElement with specific css selector which is visible.
     *
     * @param cssSelector
     *            css selector
     * @return WebElement with specific css selector which is visible
     */
    public WebElement waitForVisibleWE(String cssSelector) {
        return waitForAvailableWE(By.cssSelector(cssSelector), WAIT_TIME, new VisibleElementCondition());
    }

    /**
     * Waits for WebElement with specific css selector which is enabled.
     *
     * @param cssSelector
     *            css selector
     * @return WebElement with specific css selector which is enabled
     */
    public WebElement waitForEnabledWE(String cssSelector) {
        return waitForAvailableWE(By.cssSelector(cssSelector), WAIT_TIME, new EnabledElementCondition());
    }

    public WebElement waitForAvailableWE(final By by, final IWEAvailabilityCondition... condition) {
        return waitForAvailableWE(by, WAIT_TIME, condition);
    }

    public WebElement waitForAvailableWE(final By by, int seconds, final IWEAvailabilityCondition... condition) {

        WebElement we = (new WebDriverWait(driver, seconds)).until(new ExpectedCondition<WebElement>() {

            @Override
            public WebElement apply(WebDriver d) {
                WebElement element = d.findElement(by);
                for (IWEAvailabilityCondition iAvailableElementCondition : condition) {
                    if (!iAvailableElementCondition.isValid(element)) {
                        return null;
                    }
                }
                return element;
            }
        });
        return we;
    }

    protected void injectWebElementsToPage(Object page) {
        if (fieldDecorator == null) {
            fieldDecorator = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(driver),
                NUMBER_OF_TRIES);
        }
        PageFactory.initElements(fieldDecorator, page);
    }

    private class EnabledElementCondition implements IWEAvailabilityCondition {

        @Override
        public boolean isValid(WebElement we) {
            if (we.isEnabled()) {
                return true;
            }
            return false;
        }
    }

    private class ExpectedStringCondition implements IWEAvailabilityCondition {

        private final String expected;

        public ExpectedStringCondition(String expected) {
            this.expected = expected;
        }

        @Override
        public boolean isValid(WebElement we) {
            if (we.getText() != null && we.getText().equals(expected)) {
                return true;
            }
            return false;
        }
    }

    private class VisibleElementCondition implements IWEAvailabilityCondition {

        @Override
        public boolean isValid(WebElement we) {
            if (we.isDisplayed()) {
                return true;
            }
            return false;
        }
    }
}
