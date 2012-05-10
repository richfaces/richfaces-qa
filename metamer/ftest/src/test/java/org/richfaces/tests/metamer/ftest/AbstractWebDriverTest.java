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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractWebDriverTest extends AbstractMetamerTest {

    @Drone
    protected WebDriver driver;
    protected static final int WAIT_TIME = 5;// s
    protected static final int MINOR_WAIT_TIME = 200;// ms
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
     * Waiting method. Waits number of milis defined by @milis
     *
     * @param milis
     */
    protected static void waiting(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Executes JavaScript script.
     *
     * @param script whole command that will be executed
     * @param args
     * @return may return a value
     */
    protected Object executeJS(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Executes JavaScript script. Returns single and trimmed string. Tries to
     * execute script few times with waiting for expected string defined by
     * @expectedValue.
     *
     * @param script whole command that will be executed
     * @param args
     * @return single and trimmed string
     */
    protected String expectedReturnJS(String script, String expectedValue, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String result = null;
        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            result = ((String) js.executeScript(script, args)).trim();
            if (result.equals(expectedValue)) {
                break;
            }
            waiting(MINOR_WAIT_TIME);
        }
        return result;
    }

    protected void injectWebElementsToPage(Object page) {
        if (fieldDecorator == null) {
            fieldDecorator = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(driver),
                    NUMBER_OF_TRIES);
        }
        PageFactory.initElements(fieldDecorator, page);
    }

    /**
     * Wait for whole page rendered
     */
    protected void waitForPageToLoad() {
        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                Object result = executeJS("return document['readyState'] ? 'complete' == document.readyState : true");
                if (result instanceof Boolean) {
                    Boolean b = (Boolean) result;
                    if (b.equals(Boolean.TRUE)) {
                        return;
                    }
                }
                waiting(MINOR_WAIT_TIME);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Tries to check and wait for correct size (@size) of list. Depends on list
     * of WebElements decorated with StaleReferenceAwareFieldDecorator.
     *
     * @param list input list
     * @param size expected size of list
     * @return list with or without expected size
     */
    protected List<WebElement> guardListSize(List<WebElement> list, int size) {
        boolean lastCheckWithModifications;
        int checkedSize = list.size();
        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            if (checkedSize < list.size()) {
                checkedSize = list.size();
                lastCheckWithModifications = true;
            } else {
                lastCheckWithModifications = false;
            }
            if (checkedSize >= size && !lastCheckWithModifications) {
                //last check
                waiting(MINOR_WAIT_TIME);
                list.size();
                return list;
            }
            waiting(MINOR_WAIT_TIME);
        }
        return list;
    }

    protected enum WaitRequestType {

        XHR,
        HTTP;
    }

    /**
     * Generates a waiting proxy, which will wait for page rendering after
     * expected @waitRequestType which will be launched via communicating with
     * @element.
     *
     * @param element WebElement which will launch a request (e.g. with methods
     * click(), submit()...) after invoking it.
     * @param waitRequestType type of expected request which will be launched
     * @return waiting proxy for input element
     */
    protected WebElement waitRequest(WebElement element, WaitRequestType waitRequestType) {
        switch (waitRequestType) {
            case HTTP:
                return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                        new Class[]{ WebElement.class }, new HTTPWaitHandler(element));
            case XHR:
                return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                        new Class[]{ WebElement.class }, new XHRWaitHandler(element));
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * Waits for page to full load after method is invoked.
     */
    private class HTTPWaitHandler implements InvocationHandler {

        private final WebElement element;

        public HTTPWaitHandler(WebElement element) {
            this.element = element;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object o = method.invoke(element, args);
            waitForPageToLoad();
            return o;
        }
    }

    /**
     * Waits for change of requestTime in Metamer.
     */
    private class XHRWaitHandler implements InvocationHandler {

        private final WebElement element;
        private Date time1;

        public XHRWaitHandler(WebElement element) {
            this.element = element;
        }

        private Date getDate() {
            new WebDriverWait(driver).until(ElementDisplayed.getInstance().
                    element(driver.findElement(By.cssSelector("span[id=requestTime]"))));
            String time = driver.findElement(By.cssSelector("span[id=requestTime]")).getText();
            DateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
            Date d = null;
            try {
                d = sdf.parse(time);
            } catch (ParseException ex) {
            }
            return d;
        }

        private void beforeAction() {
            time1 = getDate();
        }

        private void afterAction() {
            for (int i = 0; i < NUMBER_OF_TRIES; i++) {
                try {
                    if (getDate().equals(time1)) {
                        waiting(MINOR_WAIT_TIME);
                    } else {
                        waitForPageToLoad();//unnecessary?
                        return;
                    }
                } catch (Exception e) {
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            beforeAction();
            Object o = method.invoke(element, args);
            afterAction();
            return o;
        }
    }
}
