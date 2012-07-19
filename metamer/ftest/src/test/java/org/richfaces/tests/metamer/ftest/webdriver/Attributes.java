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
package org.richfaces.tests.metamer.ftest.webdriver;

import java.util.List;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;

public class Attributes<T extends AttributeEnum> {

    protected WebDriver driver = GrapheneContext.getProxy();
    private static final String NULLSTRING = "null";
    private static final String[] NULLSTRINGOPTIONS = {"null", "", " "};
    private static final int WAITTIME = 200;
    private static final int NUMBEROFTRIES = 5;

    public Attributes() {
    }

    public void set(T attribute, String string) {
        setProperty(attribute.toString(), string);
    }

    /**
     * Setter for special cases. For example, 'for' is reserved java word, but
     * valid richfaces attribute as well. So we use this attribute name in upper
     * case in enum, and then set by special method to avoid overload by
     * toLowerCase-in over the whole world of richfaces attributes
     *
     * @param attribute
     * @param string
     */
    public void setLower(T attribute, String string) {
        setProperty(attribute.toString().toLowerCase(), string);
    }

    // TODO jjamrich 2011-09-02: make sure that this resolve to correct string representation of number given as attr
    public void set(T attribute, Number no) {
        setProperty(attribute.toString(), no);
    }

    public void set(T attribute, Boolean bool) {
        setProperty(attribute.toString(), bool);
    }

    public void set(T attribute, JavaScript js) {
        setProperty(attribute.toString(), js);
    }

    public void set(T attribute, JQueryLocator locator) {
        setProperty(attribute.toString(), locator.getRawLocator());
    }

    public void set(T attribute, Enum<?> item) {
        setProperty(attribute.toString(), item.toString());
    }

    public void set(T attribute, Event event) {
        setProperty(attribute.toString(), event.getEventName());
    }

    public void reset(T attribute) {
        setProperty(attribute.toString(), "");
    }

    /**
     * Sets attribute and checks if it was realy set (waits for page rerender).
     *
     * @param propertyName
     * @param value
     */
    protected void setProperty(String propertyName, Object value) {
        String valueAsString = (value == null ? NULLSTRING : value.toString());
        String cssLocator;
        String xpathLocator = "//*[contains(@id, ':" + propertyName + "Input')]";
        new WebDriverWait(driver).until(ElementDisplayed.getInstance().
                element(driver.findElement(By.xpath(xpathLocator))));
        WebElement foundElement = driver.findElement(By.xpath(xpathLocator));
        String tagName = foundElement.getTagName();
        if ("input".equals(tagName)) {//text
            applyText(xpathLocator, valueAsString);
        } else if ("table".equals(tagName)) {//radio, ?checkboxes?
            cssLocator = "input[id*=" + propertyName + "Input]";
            List<WebElement> foundElements = driver.findElements(By.cssSelector(cssLocator));
            if (foundElements.isEmpty()) {
                throw new IllegalArgumentException("No inputs for this attribute found");
            }
            String inputType = foundElements.get(0).getAttribute("type");
            if ("radio".equals(inputType)) {
                applyRadio(foundElements, valueAsString);
            } else if ("checkbox".equals(inputType)) { //not supported
                applyCheckbox(foundElements, valueAsString);
            }
        } else if ("select".equals(tagName)) {//select
            cssLocator = "select[id$=" + propertyName + "Input] option";
            List<WebElement> foundOptions = driver.findElements(By.cssSelector(cssLocator));
            applySelect(foundOptions, valueAsString);
        }
        waitForPageRerenderAndCheckIfPropertyWasSet(propertyName, valueAsString);
    }

    protected void applyText(String xpathLocator, String value) {
        driver.findElement(By.xpath(xpathLocator)).clear();
        waitForPageToLoad();
        driver.findElement(By.xpath(xpathLocator)).sendKeys(value);
        driver.findElement(By.xpath(xpathLocator)).submit();
        waitForPageToLoad();
    }

    protected void applyCheckbox(List<WebElement> elements, String value) {
        throw new UnsupportedOperationException();
    }

    protected void applyRadio(List<WebElement> elements, String value) {
        applyOnSelectionInputs(elements, value);
    }

    protected void applySelect(List<WebElement> elements, String value) {
        applyOnSelectionInputs(elements, value);
    }

    private void applyOnSelectionInputs(List<WebElement> elements, String value) {
        for (WebElement webElement : elements) {
            String attributeValue = webElement.getAttribute("value");
            if (attributeValue.equalsIgnoreCase(value)) {
                webElement.click();
                break;
            }
            if (value.equals(NULLSTRING)) {
                if (new StringEqualsWrapper(value).similarToSomeOfThis(NULLSTRINGOPTIONS)) {
                    webElement.click();
                    break;
                }
            }
        }
        waitForPageToLoad();
    }

    /**
     * Retrieve current attribute value
     *
     * @param attribute
     * @return current attribute value
     */
    public String get(T attribute) {
        return getProperty(attribute.toString());
    }

    /**
     * Gets String representation of attribute value set in page.
     *
     * @param propertyName string name of attribute
     * @return
     */
    protected String getProperty(String propertyName) {
        String cssLocator;
        String xpathLocator = "//*[contains(@id, ':" + propertyName + "Input')]";
        new WebDriverWait(driver).until(ElementDisplayed.getInstance().
                element(driver.findElement(By.xpath(xpathLocator))));
        WebElement foundElement = driver.findElement(By.xpath(xpathLocator));
        String tagName = foundElement.getTagName();
        if ("input".equals(tagName)) {//text
            return foundElement.getAttribute("value");
        } else if ("table".equals(tagName)) {//radio, ?checkboxes?
            cssLocator = "input[id*=" + propertyName + "Input]";
            List<WebElement> foundElements = driver.findElements(By.cssSelector(cssLocator));
            if (foundElements.isEmpty()) {
                throw new IllegalArgumentException("No inputs for this attribute found");
            }
            String inputType = foundElements.get(0).getAttribute("type");
            if ("radio".equals(inputType)) {
                return getValueFromSelection(foundElements);
            } else if ("checkbox".equals(inputType)) { //not supported
                throw new UnsupportedOperationException("Getting value from checkboxes is not implemented");
            }
        } else if ("select".equals(tagName)) {//select
            cssLocator = "select[id$=" + propertyName + "Input] option";
            List<WebElement> foundOptions = driver.findElements(By.cssSelector(cssLocator));
            return getValueFromSelection(foundOptions);
        }
        throw new UnsupportedOperationException("Unknown property");
    }

    protected String getValueFromSelection(List<WebElement> list) {
        WebElement nullSelectionOption = null;
        for (WebElement webElement : list) {
            String value = webElement.getAttribute("value");
            if (new StringEqualsWrapper(value).similarToSomeOfThis(NULLSTRINGOPTIONS)) {
                nullSelectionOption = webElement;
            }
            if (webElement.isSelected()) {
                return webElement.getAttribute("value");
            }
        }
        if (nullSelectionOption != null) {
            //workaround for String options with value="" , used in attributes like
            //action, actionListener, model...
            return NULLSTRING;
        }

        throw new IllegalArgumentException("No selected choice for this attribute found.");
    }

    /**
     * Wait for page to load after attribute was set and then ckecks if it
     * really was set. If it was not set, then IllegalStateException is thrown.
     *
     * @param propertyName string value of attribute
     * @param value value that the attribute should have
     */
    private void waitForPageRerenderAndCheckIfPropertyWasSet(String propertyName, String value) {
        waitForPageToLoad();
        String property;
        for (int i = 0; i < NUMBEROFTRIES; i++) {
            try {
                property = getProperty(propertyName);
                if (property.equals(value)) {
                    return;
                }
                if (value.equals(NULLSTRING)) {
                    if (new StringEqualsWrapper(property).similarToSomeOfThis(NULLSTRINGOPTIONS)) {
                        return;
                    }
                }
                waiting(WAITTIME);
            } catch (Exception ignored) {
                waiting(WAITTIME);
            }
        }
        throw new IllegalStateException("Property " + propertyName + " was not changed.");
    }

    /**
     * Waiting.
     *
     * @param milis
     */
    protected void waiting(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Wait for whole page rendered.
     */
    protected void waitForPageToLoad() {
        for (int i = 0; i < NUMBEROFTRIES; i++) {
            try {
                Object result = executeJS("return document['readyState'] ? 'complete' == document.readyState : true");
                if (result instanceof Boolean) {
                    Boolean b = (Boolean) result;
                    if (b.equals(Boolean.TRUE)) {
                        return;
                    }
                }
                waiting(WAITTIME);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Executes JavaScript script.
     *
     * @param script whole command that will be executed
     * @param args
     * @return may return a value
     */
    public Object executeJS(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    private class StringEqualsWrapper {

        private final String string;

        public StringEqualsWrapper(String string) {
            this.string = string;
        }

        public boolean similarToSomeOfThis(String... others) {
            for (String other : others) {
                if (other.equalsIgnoreCase(this.string)) {
                    return true;
                }
            }
            return false;
        }
    }
}
