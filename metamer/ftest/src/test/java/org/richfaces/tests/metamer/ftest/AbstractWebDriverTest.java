/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.basicAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.iphone.IPhoneDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StringEqualsWrapper;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractWebDriverTest extends AbstractMetamerTest {

    @Drone
    protected WebDriver driver;
    protected static final int WAIT_TIME = 5;// s
    protected static final int MINOR_WAIT_TIME = 50;// ms
    protected static final int TRIES = 20;//for guardListSize and expectedReturnJS
    protected DriverType driverType;

    public enum DriverType {

        FireFox(FirefoxDriver.class),
        InternetExplorer(InternetExplorerDriver.class),
        Chrome(ChromeDriver.class),
        HTMLUnit(HtmlUnitDriver.class),
        //        Opera(OperaDriver.class),
        IPhone(IPhoneDriver.class),
        Android(AndroidDriver.class),
        Remote(RemoteWebDriver.class);
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

    /**
     * Waiting method. Waits number of milis defined by @milis
     *
     * @param milis
     */
    protected static void waiting(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ignored) {
        }
    }

    protected void assertNotPresent(WebElement element, String msg) {
        assertTrue(Graphene.element(element).not().isPresent().apply(driver), msg);
    }

    protected void assertNotVisible(WebElement element, String msg) {
        assertTrue(Graphene.element(element).not().isVisible().apply(driver), msg);
    }

    protected void assertPresent(WebElement element, String msg) {
        assertTrue(Graphene.element(element).isPresent().apply(driver), msg);
    }

    protected void assertVisible(WebElement element, String msg) {
        assertTrue(Graphene.element(element).isVisible().apply(driver), msg);
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
     * Tries to execute JavaScript script for few times
     * and expects a @expectedValue as result. Returns single trimmed String
     * with expected value or what it found or null.
     *
     * @param expectedValue expected return value of javaScript
     * @param script whole JavaScript that will be executed
     * @param args
     * @return single and trimmed string or null
     */
    protected String expectedReturnJS(String script, String expectedValue, Object... args) {
        String result = null;
        for (int i = 0; i < TRIES; i++) {
            Object executedScriptResult = executeJS(script, args);
            if (executedScriptResult != null) {
                result = ((String) executedScriptResult).trim();
                if (result.equals(expectedValue)) {
                    return result;
                }
            }
        }
        return result;
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
        } else if (!valueOnPage.contains(value)) {//Attribute has not been set correctly
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
     * "testedAttribute" to the input field of the tested attribute and fires
     * the event @event using jQuery on the given element @element.
     * Then it checks if the event was fired. This method should only be used
     * for attributes consistent with DOM events (e.g. (on)click,
     * (on)change...).
     *
     * @param element WebElement on which will be the event triggered
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     */
    protected <T extends AttributeEnum> void testFireEventWithJS(WebElement element, Attributes<T> attributes, T testedAttribute) {
        attributes.set(testedAttribute, "metamerEvents += \"" + testedAttribute.toString() + " \"");
        executeJS("metamerEvents = \"\";");
        Event e = new Event(testedAttribute.toString().substring(2));//remove prefix "on"
        fireEvent(element, e);
        String returnedString = expectedReturnJS("return metamerEvents", testedAttribute.toString());
        assertEquals(returnedString, testedAttribute.toString(), "Event " + e + " does not work.");
    }

    /**
     * A helper method for testing JavaScripts events. It sets "metamerEvents +=
     * "testedAttribute" to the input field of the tested attribute and fires
     * the event @event using jQuery on the element @element.
     * Then it checks if the event was fired.
     *
     * @see testFireEventWithJS(WebElement element, Attributes<T> attributes, T testedAttribute)
     * @param element WebElement on which will be the event triggered
     * @param event event wich will be triggered
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     */
    protected <T extends AttributeEnum> void testFireEventWithJS(WebElement element, Event event, Attributes<T> attributes, T testedAttribute) {
        attributes.set(testedAttribute, "metamerEvents += \"" + testedAttribute.toString() + " \"");
        executeJS("metamerEvents = \"\";");
        fireEvent(element, event);
        String returnedString = expectedReturnJS("return metamerEvents", testedAttribute.toString());
        assertEquals(returnedString, testedAttribute.toString(), "Event " + event + " does not work.");
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
        executeJS("metamerEvents = \"\";");
        eventFiringAction.perform();
        String returnedString = expectedReturnJS("return metamerEvents", testedAttribute.toString());
        assertEquals(returnedString, testedAttribute.toString(), "Event " + testedAttribute.toString() + " does not work.");
    }

    /**
     * A helper method for testing JavaScripts events.
     *
     * @param event JavaScript event to be tested
     * @param element element on which will be the event triggered
     */
    protected void testFireEvent(Event event, WebElement element) {
        testFireEvent(event, element, event.getEventName());
    }

    /**
     * A helper method for testing JavaScripts events.
     *
     * @param event JavaScript event to be tested
     * @param element element on which will be the event triggered
     * @param attributeName name of the attribute that should be set
     */
    protected void testFireEvent(final Event event, final WebElement element, String attributeName) {
        testFireEvent(attributeName, new Action() {
            @Override
            public void perform() {
                fireEvent(element, event);
            }
        });
    }

    /**
     * A helper method for testing events.
     *
     * @param attributeName name of the attribute that should be set (i.e. inputselect, select ; without the prefix 'on')
     * @param eventFiringAction action which will be performed to trigger the event
     */
    protected void testFireEvent(String attributeName, Action eventFiringAction) {
        TextInputComponentImpl eventInput = Graphene.createPageFragment(TextInputComponentImpl.class, driver.findElement(By.cssSelector("input[id$=on" + attributeName + "Input]")));
        //set attribute
        MetamerPage.waitRequest(eventInput.clear(ClearType.JS)
                .fillIn("metamerEvents += \"" + attributeName + " \""), WaitRequestType.HTTP)
                .trigger("blur");
        //clear/init events
        executeJS("metamerEvents = \"\";");
        //trigger event
        eventFiringAction.perform();
        //check
        assertEquals(expectedReturnJS("return metamerEvents", attributeName), attributeName, "Attribute " + attributeName + " does not work.");
    }

    /**
     * Method for firing JavaScript events on given element using jQuery.
     *
     * @param element
     * @param event
     */
    protected void fireEvent(WebElement element, Event event) {
        Utils.triggerJQ(event.getEventName(), element);
    }

    /**
     * Helper method for testing label's text changing. First it sets "RichFaces 4"
     * to the <code>testedAttribute</code> input, then fires <code>labelChangeAction</code>(if some),
     * then waits for the presence of <code>element</code>
     * and finally checks if the label (<code>getText()</code> method) of <code>element</code>
     * was changed as expected.
     * @param element element which <code>getText()</code> method will be used for checking of label text
     * @param attributes attributes instance which will be used for setting
     * attribute
     * @param testedAttribute attribute which will be tested
     * @param labelChangeAction action which will change the label (if no action needed use <code>null</code> or empty Action)
     */
    protected <T extends AttributeEnum> void testLabelChanges(WebElement element, Attributes<T> attributes, T testedAttribute, Action labelChangeAction) {
        String rf = "RichFaces 4";
        attributes.set(testedAttribute, rf);
        if (labelChangeAction != null) {
            labelChangeAction.perform();
        }
        Graphene.waitModel().until(Graphene.element(element).isPresent());
        Graphene.waitModel().until(testedAttribute + " does not work, label has not changed.").element(element).text().equalTo(rf);
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

    /**
     * Decoder for Attributes. Converts given Attribute to String. If Attribute
     * ends with 'class' or 'style', then it returns the correct one, when the attribute does not
     * end with none of those, then it returns toString() method of attribute
     */
    public static class Attribute2StringDecoder {

        private static final String[] ATTRIBUTES = { "class", "classes", "style" };

        public static <T extends AttributeEnum> String decodeAttribute(T testedAttribute) {
            String testedAtt = testedAttribute.toString();
            if (testedAtt.length() > 6) {
                //get the ending
                String tmp = testedAtt.toLowerCase();
                for (String string : ATTRIBUTES) {
                    if (tmp.lastIndexOf(string) > 0) {//contains an attribute to decode
                        if (string.equalsIgnoreCase(ATTRIBUTES[0]) || string.equalsIgnoreCase(ATTRIBUTES[1])) {
                            return ATTRIBUTES[0];
                        } else if (string.equalsIgnoreCase(ATTRIBUTES[2])) {
                            return ATTRIBUTES[2];
                        } else {
                            throw new RuntimeException("Cannot decode attribute " + testedAtt);
                        }
                    }
                }
            }
            return testedAtt;
        }
    }

    /**
     * Abstract ReloadTester for testing component's state after reloading the page
     *
     * @param <T>
     *            the type of input values which will be set, sent and then verified
     */
    public abstract class ReloadTester<T> {

        private MetamerPage page;

        public ReloadTester(MetamerPage page) {
            super();
            this.page = page;
        }

        public abstract void doRequest(T inputValue);

        public abstract void verifyResponse(T inputValue);

        public abstract T[] getInputValues();

        public void testRerenderAll() {
            for (T inputValue : getInputValues()) {
                doRequest(inputValue);
                verifyResponse(inputValue);
                page.rerenderAll();
                verifyResponse(inputValue);
            }
        }

        public void testFullPageRefresh() {
            for (T inputValue : getInputValues()) {
                doRequest(inputValue);
                verifyResponse(inputValue);
                page.fullPageRefresh();
                verifyResponse(inputValue);
            }
        }
    }
}
