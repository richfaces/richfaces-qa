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
package org.richfaces.tests.metamer.ftest;

import static org.jboss.arquillian.ajocado.Graphene.alertPresent;
import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.id;
import static org.jboss.arquillian.ajocado.Graphene.jq;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.dom.Event.CLICK;
import static org.jboss.arquillian.ajocado.dom.Event.DBLCLICK;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEMOVE;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOUT;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOVER;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEUP;
import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.basicAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.jboss.arquillian.ajocado.browser.BrowserType;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.test.selenium.ScreenshotInterceptor;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Abstract test case used as a basis for majority of test cases.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22749 $
 */
public abstract class AbstractGrapheneTest extends AbstractMetamerTest {

    @Drone
    protected GrapheneSelenium selenium;
    protected ScreenshotInterceptor screenshotInterceptor = new ScreenshotInterceptor();
    protected PhaseInfo phaseInfo = new PhaseInfo();

    final String group = "span.rf-tab-lbl:contains({0})";
    final String component = "li.rf-ulst-itm a:contains({0})";
    final String page = "div.links a:contains({0})";

    /**
     * Opens the tested page. If templates is not empty nor null, it appends url parameter with templates.
     *
     * @param templates
     *            templates that will be used for test, e.g. "red_div"
     */
    @BeforeMethod(alwaysRun = true)
    public void loadPage(Object[] templates) {
        if (selenium == null) {
            throw new SkipException("selenium isn't initialized");
        }

        // selenium.open(buildUrl(getTestUrl() + "?templates=" + template.toString()));
        selenium.open(buildUrl("http://localhost:8080/portal/classic/metamer"));
        selenium.waitForPageToLoad(TIMEOUT);
        /*
        MetamerNavigation navigation = getPath2ComponentExample();

        selenium.click(jq(SimplifiedFormat.format(group, navigation.getGroup())));
        selenium.waitForPageToLoad(TIMEOUT);

        selenium.click(jq(SimplifiedFormat.format(component, navigation.getComponent())));
        selenium.waitForPageToLoad(TIMEOUT);

        selenium.click(jq(SimplifiedFormat.format(page, navigation.getPage())));
        selenium.waitForPageToLoad(TIMEOUT);
        */
    }

    @Parameters("takeScreenshots")
    @BeforeMethod(alwaysRun = true, dependsOnMethods = { "loadPage" })
    public void enableScreenshots(@Optional("false") String takeScreenshots, Method method) {
        if (!"false".equals(takeScreenshots)) {
            screenshotInterceptor.setMethod(method);
            selenium.getCommandInterceptionProxy().registerInterceptor(screenshotInterceptor);
        }
    }

    /**
     * Invalidates session by clicking on a button on tested page.
     */
    @AfterMethod(alwaysRun = true)
    public void invalidateSession() {
        selenium.deleteAllVisibleCookies();
    }

    /**
     * Forces the current thread sleep for given time.
     *
     * @param millis
     *            number of miliseconds for which the thread will sleep
     */
    protected void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do a full page refresh (regular HTTP request) by triggering a command with no action bound.
     */
    public void fullPageRefresh() {
        waitGui.until(elementPresent.locator(fullPageRefreshIcon));
        guardHttp(selenium).click(fullPageRefreshIcon);
    }

    /**
     * Rerender all content of the page (AJAX request) by trigerring a command with no action but render bound.
     */
    public void rerenderAll() {
        waitGui.until(elementPresent.locator(rerenderAllIcon));
        guardXhr(selenium).click(rerenderAllIcon);
    }

    /**
     * A helper method for testing javascripts events. It sets alert('testedevent') to the input field for given event
     * and fires the event. Then it checks the message in the alert dialog.
     *
     * @param event
     *            JavaScript event to be tested
     * @param element
     *            locator of tested element
     */
    protected void testFireEvent(Event event, ElementLocator<?> element) {
        testFireEvent(event, element, event.getEventName());
    }

    /**
     * A helper method for testing javascripts events. It sets alert('testedevent') to the input field for given event
     * and fires the event. Then it checks the message in the alert dialog.
     *
     * @param event
     *            JavaScript event to be tested
     * @param element
     *            locator of tested element
     * @param attributeName
     *            name of the attribute that should be set
     */
    protected void testFireEvent(Event event, ElementLocator<?> element, String attributeName) {
        ElementLocator<?> eventInput = pjq("input[id$=on" + attributeName + "Input]");
        String value = "metamerEvents += \"" + event.getEventName() + " \"";

        guardHttp(selenium).type(eventInput, value);

        selenium.fireEvent(element, event);

        waitGui.failWith("Attribute on" + attributeName + " does not work correctly").until(
            new EventFiredCondition(event));
    }

    /**
     * Returns the locale of the tested page
     *
     * @return the locale of the tested page
     */
    public Locale getLocale() {
        String localeString = selenium.getText(id("locale"));
        return LocaleUtils.toLocale(localeString);
    }

    /**
     * A helper method for testing attribute "style" or similar. It sets "background-color: yellow; font-size: 1.5em;"
     * to the input field and checks that it was changed on the page.
     *
     * @param element
     *            locator of tested element
     * @param attribute
     *            name of the attribute that will be set (e.g. style, headerStyle, itemContentStyle)
     */
    protected void testStyle(ElementLocator<?> element, BasicAttributes attribute) {
        final String value = "background-color: yellow; font-size: 1.5em;";

        basicAttributes.set(attribute, value);

        AttributeLocator<?> styleAttr = element.getAttribute(Attribute.STYLE);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute style should contain \"" + value + "\"");
    }

    /**
     * A helper method for testing attribute "style". It sets "background-color: yellow; font-size: 1.5em;" to the input
     * field and checks that it was changed on the page.
     *
     * @param element
     *            locator of tested element
     */
    protected void testStyle(ElementLocator<?> element) {
        testStyle(element, BasicAttributes.style);
    }

    /**
     * A helper method for testing attribute "class" or similar. It sets "metamer-ftest-class" to the input field and
     * checks that it was changed on the page.
     *
     * @param element
     *            locator of tested element
     * @param attribute
     *            name of the attribute that will be set (e.g. styleClass, headerClass, itemContentClass)
     */
    protected void testStyleClass(ExtendedLocator<JQueryLocator> element, BasicAttributes attribute) {
        final String styleClass = "metamer-ftest-class";

        basicAttributes.set(attribute, styleClass);

        JQueryLocator elementWhichHasntThatClass = jq(element.getRawLocator() + ":not(.{0})").format(styleClass);
        assertTrue(selenium.isElementPresent(element));
        assertFalse(selenium.isElementPresent(elementWhichHasntThatClass));
    }

    /**
     * A helper method for testing attribute "class". It sets "metamer-ftest-class" to the input field and checks that
     * it was changed on the page. This method is wrapping {@link #testStyleClass(ExtendedLocator, BasicAttributes)}
     *
     * @param element
     *            locator of tested element
     */
    protected void testStyleClass(ExtendedLocator<JQueryLocator> element) {
        testStyleClass(element, BasicAttributes.styleClass);
    }

    /**
     * Tests onrequest (e.g. onsubmit, onrequest...) events by using javascript functions. First fills Metamer's input
     * for according component attribute with testing value, then does an action, which should end by throwing a testing
     * event and then wait for the event if it was really launched
     *
     * @param eventAttribute
     *            event attribute (e.g. onsubmit, onrequest, onbeforedomupdate...)
     * @param action
     *            action wich leads to launching an event
     */
    public void testRequestEvent(AttributeEnum eventAttribute, IEventLaunchAction action) {
        testRequestEventBefore(eventAttribute);
        action.launchAction();
        testRequestEventAfter(eventAttribute);
    }

    public void testRequestEventsBefore(String... events) {
        for (String event : events) {
            ReferencedLocator<JQueryLocator> input = ref(attributesRoot, "input[type=text][id$=on{0}Input]");
            input = input.format(event);
            selenium.type(input, format("metamerEvents += \"{0} \"", event));
            selenium.waitForPageToLoad();
        }

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
    }

    public void testRequestEventBefore(AttributeEnum eventAttribute) {
        ReferencedLocator<JQueryLocator> input = ref(attributesRoot, "input[type=text][id$={0}Input]");
        input = input.format(eventAttribute.toString().trim());
        selenium.type(input, format("metamerEvents += \"{0} \"", eventAttribute.toString()));
        selenium.waitForPageToLoad();
        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
    }

    public void testRequestEventsBeforeByAlert(String... events) {
        for (String event : events) {
            ReferencedLocator<JQueryLocator> input = ref(attributesRoot, "input[type=text][id$=on{0}Input]");
            input = input.format(event);
            selenium.type(input, format("alert('{0}')", event));
            selenium.waitForPageToLoad();
        }
    }

    public void testRequestEventsAfter(String... events) {
        String[] actualEvents = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");
        assertEquals(
            actualEvents,
            events,
            format("The events ({0}) don't came in right order ({1})", Arrays.deepToString(actualEvents),
                Arrays.deepToString(events)));
    }

    public void testRequestEventAfter(AttributeEnum eventAttribute) {
        waitGui.failWith("Attribute on" + eventAttribute + " does not work correctly").until(
            new EventFiredCondition(new Event(eventAttribute.toString())));
    }

    public void testRequestEventsAfterByAlert(String... events) {
        List<String> list = new LinkedList<String>();

        for (int i = 0; i < events.length; i++) {
            waitGui.dontFail().until(alertPresent);
            if (selenium.isAlertPresent()) {
                list.add(selenium.getAlert());
            }
        }

        String[] actualEvents = list.toArray(new String[list.size()]);
        assertEquals(
            actualEvents,
            events,
            format("The events ({0}) don't came in right order ({1})", Arrays.deepToString(actualEvents),
                Arrays.deepToString(events)));
    }

    /**
     * A helper method for testing attribute "dir". It tries null, ltr and rtl.
     *
     * @param element
     *            locator of tested element
     */
    protected void testDir(ElementLocator<?> element) {
        ElementLocator<?> ltrInput = ref(attributesRoot, "input[type=radio][name$=dirInput][value=ltr]");
        ElementLocator<?> rtlInput = ref(attributesRoot, "input[type=radio][name$=dirInput][value=rtl]");

        AttributeLocator<?> dirAttribute = element.getAttribute(new Attribute("dir"));

        // dir = null
        assertFalse(selenium.isAttributePresent(dirAttribute), "Attribute dir should not be present.");

        // dir = ltr
        selenium.click(ltrInput);
        selenium.waitForPageToLoad();
        assertTrue(selenium.isAttributePresent(dirAttribute), "Attribute dir should be present.");
        String value = selenium.getAttribute(dirAttribute);
        assertEquals(value.toLowerCase(), "ltr", "Attribute dir");

        // dir = rtl
        selenium.click(rtlInput);
        selenium.waitForPageToLoad();
        assertTrue(selenium.isAttributePresent(dirAttribute), "Attribute dir should be present.");
        value = selenium.getAttribute(dirAttribute);
        assertEquals(value.toLowerCase(), "rtl", "Attribute dir");
    }

    /**
     * A helper method for testing attribute "lang".
     *
     * @param element
     *            locator of tested element
     */
    protected void testLang(ElementLocator<?> element) {
        JavaScript getAttributeLang = null;
        SystemPropertiesConfiguration config = new SystemPropertiesConfiguration();

        if (config.getBrowser().getType() == BrowserType.FIREFOX) {
            getAttributeLang = new JavaScript("window.jQuery('" + element.getRawLocator() + "').attr('lang')");
        } else {
            getAttributeLang = new JavaScript("window.jQuery('" + element.getRawLocator() + "').attr('xml:lang')");
        }

        // lang = null
        String langAttr = selenium.getEval(getAttributeLang);
        assertTrue("null".equals(langAttr) || "".equals(langAttr), "Attribute xml:lang should not be present.");

        selenium.type(pjq("input[type=text][id$=langInput]"), "sk");
        selenium.waitForPageToLoad();

        // lang = sk
        assertEquals(selenium.getEval(getAttributeLang), "sk", "Attribute xml:lang should be present.");
    }

    /**
     * A helper method for testing attribute "title".
     *
     * @param element
     *            locator of tested element
     */
    protected void testTitle(ElementLocator<?> element) {
        ElementLocator<?> input = ref(attributesRoot, "input[type=text][id$=titleInput]");
        AttributeLocator<?> attribute = element.getAttribute(new Attribute("title"));

        // title = null
        assertFalse(selenium.isAttributePresent(attribute), "Attribute title should not be present.");

        // title = "RichFaces 4"
        selenium.type(input, "RichFaces 4");
        selenium.waitForPageToLoad(TIMEOUT);

        assertTrue(selenium.isAttributePresent(attribute), "Attribute title should be present.");
        String value = selenium.getAttribute(attribute);
        assertEquals(value, "RichFaces 4", "Attribute title");
    }

    /**
     * A helper method for testing standard HTML attributes (RichFaces attributes that are directly put into markup),
     * e.g. hreflang.
     *
     * @param element
     *            locator of tested element
     * @param attribute
     *            tested attribute, e.g. "hreflang"
     * @param value
     *            value that should be set, e.g. "cs"
     */
    protected void testHtmlAttribute(ElementLocator<?> element, String attribute, String value) {
        AttributeLocator<?> attr = element.getAttribute(new Attribute(attribute));

        selenium.type(pjq("input[id$=" + attribute + "Input]"), value);
        selenium.waitForPageToLoad();

        assertTrue(selenium.getAttribute(attr).contains(value), "Attribute " + attribute + " should contain \"" + value
            + "\".");
    }

    /**
     * Hides header, footer and inputs for attributes.
     */
    protected void hideControls() {
        selenium.getEval(new JavaScript("window.hideControls()"));
    }

    /**
     * Shows header, footer and inputs for attributes.
     */
    protected void showControls() {
        selenium.getEval(new JavaScript("window.showControls()"));
    }

    protected void fireEventNatively(ElementLocator<?> target, Event event) {
        if (event == CLICK) {
            selenium.click(target);
        } else if (event == DBLCLICK) {
            selenium.doubleClick(target);
        } else if (event == MOUSEMOVE) {
            selenium.mouseMove(target);
        } else if (event == MOUSEDOWN) {
            selenium.mouseDown(target);
        } else if (event == MOUSEUP) {
            selenium.mouseUp(target);
        } else if (event == MOUSEOVER) {
            selenium.mouseOver(target);
        } else if (event == MOUSEOUT) {
            selenium.mouseOut(target);
        } else {
            selenium.fireEvent(target, event);
        }
    }

    /**
     * Abstract ReloadTester for testing
     *
     * @param <T>
     *            the type of input values which will be set, sent and then verified
     */
    public abstract class ReloadTester<T> {

        public abstract void doRequest(T inputValue);

        public abstract void verifyResponse(T inputValue);

        public abstract T[] getInputValues();

        public void testRerenderAll() {
            for (T inputValue : getInputValues()) {
                doRequest(inputValue);
                verifyResponse(inputValue);
                AbstractGrapheneTest.this.rerenderAll();
                verifyResponse(inputValue);
            }
        }

        public void testFullPageRefresh() {
            for (T inputValue : getInputValues()) {
                doRequest(inputValue);
                verifyResponse(inputValue);
                AbstractGrapheneTest.this.fullPageRefresh();
                verifyResponse(inputValue);
            }
        }
    }

    protected interface IEventLaunchAction {

        void launchAction();
    }

    /**
     * Metamer structure for component examples is like:
     *  <li>group (such as "A4J" or "Rich")</li>
     *  <li>component (such as "Rich Calendar") </li>
     *  <li>page (such as "Simple" or "RF-11111")</li>
     *
     * @author jjamrich
     *
     */
    public class MetamerNavigation {
        private String group;
        private String component;
        private String page;

        public MetamerNavigation(String group, String component, String page){
            this.group = group;
            this.component = component;
            this.page = page;
        }

        public String getGroup() {
            return group;
        }

        public String getComponent() {
            return component;
        }

        public String getPage() {
            return page;
        }
    }

}
