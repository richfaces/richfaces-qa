/**
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
 */
package org.richfaces.tests.metamer.ftest;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.basicAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import com.google.common.base.Predicate;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.iphone.IPhoneDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StringEqualsWrapper;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractWebDriverTest<Page extends MetamerPage> extends AbstractMetamerTest {

    @Drone
    protected WebDriver driver;
    protected Page page;
    protected static final int WAIT_TIME = 5;// s
    private static final int NUMBER_OF_TRIES = 5;
    protected static final int MINOR_WAIT_TIME = 50;// ms
    protected static final int TRIES = 20;//for guardListSize and expectedReturnJS
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
        private final Class<?> clazz;

        private DriverType(Class<?> clazz) {
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

    @BeforeMethod(alwaysRun = true, dependsOnMethods = { "loadPage" })
    public void initializePage() {
        injectWebElementsToPage(getPage());
    }

    protected Page getPage() {
        if (page == null) {
            page = createPage();
        }
        return page;
    }

    protected abstract Page createPage();

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
     * @return may return a value or null (if expected (non-returning script) or
     * if returning script fails)
     */
    protected Object executeJS(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Tries to execute JavaScript script for few times with some wait time
     * between tries and expecting a predicted result. Method waits for expected
     * string defined in @expectedValue. Returns single trimmed String with
     * expected value or what it found or null.
     *
     * @param expectedValue expected return value of javaScript
     * @param script whole JavaScript that will be executed
     * @param args
     * @return single and trimmed string or null
     */
    protected String expectedReturnJS(String script, String expectedValue, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String result = null;
        for (int i = 0; i < TRIES; i++) {
            Object executeScript = js.executeScript(script, args);
            if (executeScript != null) {
                result = ((String) js.executeScript(script, args)).trim();
                if (result.equals(expectedValue)) {
                    break;
                }
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
     * @param value tested value of attribute
     */
    protected <T extends AttributeEnum> void testHTMLAttribute(WebElement element, Attributes<T> attributes, T testedAttribute, String value) {
        attributes.set(testedAttribute, value);
        String attString = Attribute2StringDecoder.decodeAttribute(testedAttribute);
        String valueOnPage = element.getAttribute(attString);
        if (new StringEqualsWrapper(value).equalsToSomeOfThis(null, "", "null")) {
            if (new StringEqualsWrapper(valueOnPage).notEqualsToSomeOfThis(null, "", "null")) {
                fail("Attribute " + testedAttribute.toString() + " does not work properly,");
            }
        } else if (new StringEqualsWrapper(valueOnPage).isNotSimilarToSomeOfThis(value)) {//Attribute has not been set correctly
            fail("Attribute " + testedAttribute.toString() + " does not work properly,");
        }
    }

    /**
     * Testing of HTMLAttribute (e.g. type). Expects that if an attribute is set
     * to @value, then the value will be set to @anotherValue (e.g.
     * null -> submit for a4j:commandButton)
     *
     * E.g. testHTMLAttribute(page.link, mediaOutputAttributes,
     * MediaOutputAttributes.type, "text/html");
     *
     * @param element WebElement which will be checked for containment of tested
     * attribute
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     * @param value tested value of attribute
     * @param anotherValue value that will replace @value
     */
    protected <T extends AttributeEnum> void testHTMLAttribute(WebElement element, Attributes<T> attributes, T testedAttribute, String value, String anotherValue) {
        attributes.set(testedAttribute, value);
        String attString = Attribute2StringDecoder.decodeAttribute(testedAttribute);
        String valueOnPage = element.getAttribute(attString);
        if (new StringEqualsWrapper(value).equalsToSomeOfThis(null, "", "null")) {
            if (new StringEqualsWrapper(anotherValue).isNotSimilarToSomeOfThis(valueOnPage)) {
                fail("Attribute " + testedAttribute.toString() + " does not work properly,");
            }
        } else if (new StringEqualsWrapper(anotherValue).isNotSimilarToSomeOfThis(value)) {//Attribute has not been set correctly
            fail("Attribute " + testedAttribute.toString() + " does not work properly,");
        }
    }

    /**
     * Testing of HTMLAttribute. The tested value is RichFaces 4.
     *
     * @param element WebElement which will be checked for containment of tested
     * attribute
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     */
    protected <T extends AttributeEnum> void testHTMLAttribute(WebElement element, Attributes<T> attributes, T testedAttribute) {
        testHTMLAttribute(element, attributes, testedAttribute, "RichFaces 4");
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
                : element.getAttribute("xml:lang"));//FIXME not sure if "xml:lang" is necessary, inspired from AbstractGrapheneTest
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
     * A helper method for testing attribute "dir". It tries null, ltr and rtl.
     *
     * @param element WebElement reference of tested element
     */
    protected void testDir(WebElement element) {
        testHTMLAttribute(element, basicAttributes, BasicAttributes.dir, "null");
        testHTMLAttribute(element, basicAttributes, BasicAttributes.dir, "ltr");
        testHTMLAttribute(element, basicAttributes, BasicAttributes.dir, "rtl");
    }

    /**
     * A helper method for testing JavaScripts events. It sets "metamerEvents +=
     * "testedAttribute" to the input field and fires the event using jQuery.
     * Then it checks if the event was fired. This method should only be used
     * for attributes consistent with DOM events (e.g. (on)click,
     * (on)change...).
     *
     * @param element WebElement which will be checked for containment of tested
     * attribute
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     */
    protected <T extends AttributeEnum> void testFireEventWithJS(WebElement element, Attributes<T> attributes, T testedAttribute) {
        attributes.set(testedAttribute, "metamerEvents += \"" + testedAttribute.toString() + " \"");
        executeJS("window.metamerEvents = \"\";");
        Event e = new Event(testedAttribute.toString().substring(2));//remove prefix "on"
        fireEvent(element, e);
        String returnedString = expectedReturnJS("return window.metamerEvents", testedAttribute.toString());
        assertEquals(returnedString, testedAttribute.toString(), "Event " + e + " does not work.");
    }

    /**
     * A helper method for testing events. It sets "metamerEvents +=
     * "@testedAttribute" to the input field and fires the event using Actions.
     * Then it checks if the event was fired.
     *
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     * @param eventFiringAction selenium action which leads to launch the
     * tested
     * event,
     */
    protected <T extends AttributeEnum> void testFireEvent(Attributes<T> attributes, T testedAttribute, Action eventFiringAction) {
        attributes.set(testedAttribute, "metamerEvents += \"" + testedAttribute.toString() + " \"");
        executeJS("window.metamerEvents = \"\";");
        eventFiringAction.perform();
        String returnedString = expectedReturnJS("return window.metamerEvents", testedAttribute.toString());
        assertEquals(returnedString, testedAttribute.toString(), "Event " + testedAttribute.toString() + " does not work.");
    }

    /**
     * Method for firing JavaScript events on given element.
     *
     * @param element
     * @param event
     */
    protected void fireEvent(WebElement element, Event event) {
        String elementID = element.getAttribute("id");
        String eventname = event.getEventName();
        String jQueryCmd = String.format("$(\"[id='%s']\").trigger('%s')", elementID, eventname);
        executeJS(jQueryCmd);
    }

    /**
     * A helper method for testing attribute "style" or similar. It sets
     * "background-color: yellow; font-size: 1.5em;" to the input field and
     * checks that it was changed on the page.
     *
     * @param element WebElement reference of tested element
     * @param attribute name of the attribute that will be set (e.g. style,
     * headerStyle, itemContentStyle)
     */
    protected void testStyle(final WebElement element, BasicAttributes attribute) {
        final String value = "background-color: yellow; font-size: 1.5em;";
        testHTMLAttribute(element, basicAttributes, attribute, value);
    }

    /**
     * A helper method for testing attribute "style". It sets "background-color:
     * yellow; font-size: 1.5em;" to the input field and checks that it was
     * changed on the page.
     *
     * @param element WebElement reference of tested element
     */
    protected void testStyle(final WebElement element) {
        testStyle(element, BasicAttributes.style);
    }

    /**
     * A helper method for testing attribute "class" or similar. It sets
     * "metamer-ftest-class" to the input field and checks that it was changed
     * on the page.
     *
     * @param element WebElement reference of tested element
     * @param attribute name of the attribute that will be set (e.g. styleClass,
     * headerClass, itemContentClass)
     */
    protected void testStyleClass(WebElement element, BasicAttributes attribute) {
        final String styleClass = "metamer-ftest-class";
        testHTMLAttribute(element, basicAttributes, attribute, styleClass);
    }

    /**
     * A helper method for testing attribute "class". It sets
     * "metamer-ftest-class" to the input field and checks that it was changed
     * on the page.
     *
     * @param element locator of tested element
     */
    protected void testStyleClass(WebElement element) {
        testStyleClass(element, BasicAttributes.styleClass);
    }

    /**
     * A helper method for testing attribute "title".
     *
     * @param element WebElement reference of tested element
     */
    protected void testTitle(WebElement element) {
        final String testTitle = "RichFaces 4";
        testHTMLAttribute(element, basicAttributes, BasicAttributes.title, testTitle);
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
        for (int i = 0; i < TRIES; i++) {
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
        HTTP,
        NONE;
    }

    /**
     * !All requests depends on Metamer`s requestTime!
     * Temporary method before https://issues.jboss.org/browse/ARQGRA-67 is
     * resolved. Generates a waiting proxy, which will wait for page rendering
     * after expected @waitRequestType which will be launched via
     * communicating with @element.
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
                        new Class[]{ WebElement.class }, new RequestTimeChangesHandler(element));
            case XHR:
                return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                        new Class[]{ WebElement.class }, new RequestTimeChangesHandler(element));
            case NONE:
                return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                        new Class[]{ WebElement.class }, new RequestTimeNotChangesHandler(element, 2));
            default:
                throw new UnsupportedOperationException("Not supported request: " + waitRequestType);
        }
    }

    /**
     * Waits for change of requestTime in Metamer.
     */
    private class RequestTimeChangesHandler implements InvocationHandler {

        protected final WebElement element;
        protected String time1;
        protected final By REQ_TIME = By.cssSelector("span[id='requestTime']");

        public RequestTimeChangesHandler(WebElement element) {
            this.element = element;
        }

        protected String getTime() {
            WebElement el = waitUntilElementIsVisible(By.cssSelector("span[id='requestTime']"));
            String time = el.getText();
            return time;
        }

        protected void beforeAction() {
            time1 = getTime();
        }

        protected void afterAction() {
            new WDWait().until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return !input.findElement(REQ_TIME).getText().equals(time1);
                }
            });
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            beforeAction();
            Object o = method.invoke(element, args);
            afterAction();
            return o;
        }
    }

    /**
     * Waits number of seconds and checks if requestTime was not changed,
     */
    private class RequestTimeNotChangesHandler extends RequestTimeChangesHandler {

        private final int waitTime;

        public RequestTimeNotChangesHandler(WebElement element, int waitTime) {
            super(element);
            this.waitTime = waitTime;
        }

        @Override
        protected void afterAction() {
            waiting(waitTime);
            if (!getTime().equals(time1)) {
                throw new RuntimeException("No request expected, but request time has changed.");
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

    /**
     * Decoder for Attributes. Converts given Attribute to String. If Attribute
     * is contained in inner mapping, then this value is used instead of
     * returning toString() method on attribute
     */
    private static class Attribute2StringDecoder {

        private static final Map<String, String> mapping = new HashMap<String, String>();

        static {//put here any mappings
            mapping.put(BasicAttributes.styleClass.toString(), "class");
        }

        public static <T extends AttributeEnum> String decodeAttribute(T testedAttribute) {
            String key = testedAttribute.toString();
            if (mapping.containsKey(key)) {
                return mapping.get(key);
            }
            return key;
        }
    }

    /**
     * Wait for element to be visible.
     *
     * @param by locate by
     * @return found element
     */
    protected WebElement waitUntilElementIsVisible(final By by) {
        return new WDWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * WebDriver wait which ignores StaleElementException and
     * NoSuchElementException and is polling every 50 ms.
     */
    protected class WDWait extends WebDriverWait {

        /**
         * WebDriver wait which ignores StaleElementException and
         * NoSuchElementException and polling every 50 ms with max wait time of
         * 5 seconds
         */
        public WDWait() {
            this(5);
        }

        /**
         * WebDriver wait which ignores StaleElementException and
         * NoSuchElementException and polling every 50 ms with max wait time set
         * in attribute
         *
         * @param seconds max wait time
         */
        public WDWait(int seconds) {
            super(driver, seconds);
            ignoring(NoSuchElementException.class);
            ignoring(StaleElementReferenceException.class);
            pollingEvery(50, TimeUnit.MILLISECONDS);
        }
    }
}
