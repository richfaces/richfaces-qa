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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;

public class Attributes<T extends AttributeEnum> {

    protected WebDriver driver = GrapheneContext.getProxy();

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
     * Retrieve current attribute value
     *
     * @param attribute
     * @return current attribute value
     */
    public String get(T attribute) {
        return getProperty(attribute.toString());
    }

    /*
     * protected void set(String propertyName, Object value) {
     *
     * }
     */
    protected void setProperty(String propertyName, Object value) {
        String valueAsString = value.toString();
        String cssLocator;
        String xpathLocator = "//*[contains(@id, '" + propertyName + "Input')]";
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
        //wait until footer is displayed
        waitForFooter();
    }

    protected String getProperty(String propertyName) {
//        final ReferencedLocator<JQueryLocator> locator = propertyLocator.format(propertyName, "");
//        if (selenium.getCount(locator) > 1) {
//            return selenium.getAttribute(propertyLocator.format(propertyName, "[checked]").getAttribute(VALUE));
//        }
//        return selenium.getValue(locator);
        throw new UnsupportedOperationException();
    }

    protected void applyText(String xpathLocator, String value) {
        driver.findElement(By.xpath(xpathLocator)).clear();
        waitForFooter();
        driver.findElement(By.xpath(xpathLocator)).sendKeys(value);
        driver.findElement(By.xpath(xpathLocator)).submit();
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
            if (webElement.getAttribute("value").equals(value)) {
                webElement.click();
                break;
            }
        }
    }

    /**
     * Wait for rendering of footer (whole page rendered now?)
     */
    private void waitForFooter() {
        /**
         * FIXME : better would be waiting for whole page render as in
         * waitForPageToLoad(), but obtaining of JSExecutor from proxy of
         * webdriver isn't now possible, it throws ClassCastException
         */
        for (int i = 0; i < 3; i++) {
            try {
                new WebDriverWait(driver, 5).until(ElementDisplayed.getInstance().
                        element(driver.findElement(By.cssSelector("span[id=browserVersion]"))));
                waiting(500);
                return;
            } catch (NoSuchElementException ignored) {
            }
        }
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
     * Wait for whole page rendered. Not functioning, throws classCastException
     * during executeJS.
     */
    protected void waitForPageToLoad() {
        for (int i = 0; i < 3; i++) {
            try {
                Object result = executeJS("return document['readyState'] ? 'complete' == document.readyState : true");
                if (result instanceof Boolean) {
                    Boolean b = (Boolean) result;
                    if (b.equals(Boolean.TRUE)) {
                        return;
                    }
                }
                waiting(1000);
            } catch (Exception e) {
                e.printStackTrace();
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
}
