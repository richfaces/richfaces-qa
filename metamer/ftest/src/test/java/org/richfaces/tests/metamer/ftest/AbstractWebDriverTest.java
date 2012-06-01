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
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.basicAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.iphone.IPhoneDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractWebDriverTest extends AbstractMetamerTest {

    @Drone
    protected WebDriver driver;
    protected static final int WAIT_TIME = 5;// s
    protected static final int MINOR_WAIT_TIME = 200;// ms
    private static final int NUMBER_OF_TRIES = 5;
    private FieldDecorator fieldDecorator;
    protected DriverType driverType;

    public enum DriverType {

        FireFox(FirefoxDriver.class),
        InternetExplorer(InternetExplorerDriver.class),
        Chrome(ChromeDriver.class),
        HTMLUnit(HtmlUnitDriver.class),
//        Opera(OperaDriver.class),
        IPhone(IPhoneDriver.class),
        Android(AndroidDriver.class);
        private final Class clazz;

        private DriverType(Class clazz) {
            this.clazz = clazz;
        }

        public static DriverType getCurrentType(WebDriver wd) {
            for (DriverType type : values()) {
                if (type.clazz.isInstance(wd)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown Driver");
        }
    }

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
        driverType = DriverType.getCurrentType(driver);
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
     * Testing of HTMLAttribute (e.g. type).
     *
     * E.g. testHTMLAttribute(page.link, mediaOutputAttributes,
     * MediaOutputAttributes.type, "text/html");
     *
     * @param element WebElement which will be checked for containment of tested
     * attribute
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     * @param value testing value of attribute
     */
    protected <T extends AttributeEnum> void testHTMLAttribute(WebElement element, Attributes<T> attributes, T testedAttribute, String value) {
        attributes.set(testedAttribute, value);
        assertEquals(element.getAttribute(testedAttribute.toString()), value, "Attribute " + testedAttribute.toString() + " does not work.");
    }

    /**
     * Tests lang attribute of chosen component in Metamer. Page must contain an
     * input for this component's attribute.
     *
     * @param element WebElement representing component.
     */
    protected void testAttributeLang(WebElement element) {
        final String TESTVALUE = "cz";
        String attLang;

        // get attribute lang
        attLang = (driver instanceof FirefoxDriver ? element.getAttribute("lang")
                : element.getAttribute("xml:lang"));//FIXME not sure if "xml:lang" is necessary inspired from AbstractGrapheneTest
        //lang should be empty/null
        assertTrue("".equals(attLang) || "null".equals(attLang), "Attribute xml:lang should not be present.");

        // set lang to TESTVALUE
        basicAttributes.set(BasicAttributes.lang, TESTVALUE);

        //get attribute lang of element
        attLang = (driver instanceof FirefoxDriver ? element.getAttribute("lang")
                : element.getAttribute("xml:lang"));//FIXME not sure if "xml:lang" is necessary inspired from AbstractGrapheneTest

        assertEquals(attLang, TESTVALUE, "Attribute xml:lang should be present.");
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
