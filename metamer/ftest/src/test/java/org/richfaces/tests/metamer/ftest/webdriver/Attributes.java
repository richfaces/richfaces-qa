/**
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
 */
package org.richfaces.tests.metamer.ftest.webdriver;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StringEqualsWrapper;
import org.richfaces.tests.metamer.ftest.webdriver.utils.WebElementProxyUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Attributes<T extends AttributeEnum> {

    protected WebDriver driver = GrapheneContext.getProxy();
    private final String attributesID;
    private static final String PROPERTY_CSS_SELECTOR = "[id$='%s:%sInput']";
    private static final String NULLSTRING = "null";
    private static final String[] NULLSTRINGOPTIONS = { "null", "", " " };
    private static final int WAITTIME = 100;
    private static final int NUMBEROFTRIES = 5;

    public Attributes() {
        this.attributesID = "";
    }

    public Attributes(String attributesID) {
        this.attributesID = attributesID;
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
     * Sets attribute and checks if it was really set (waits for page
     * re-render).
     *
     * @param propertyName
     * @param value
     */
    protected void setProperty(String propertyName, Object value) {
        String valueAsString = (value == null ? NULLSTRING : value.toString());
        //element for all types of input elements
        WebElement foundElementProxy = WebElementProxyUtils.createProxyForElement(
                getCssSelectorForProperty(propertyName));
        //handle the property by the tagname of the input element
        Tag tag = Tag.getTag(foundElementProxy);
        switch (tag) {
            case input:
                applyText(propertyName, valueAsString);
                break;
            case checkbox:
                throw new UnsupportedOperationException("Checkboxes are not supported");
            case radio:
                applyRadio(tag, valueAsString);
                break;
            case select:
                applySelect(tag, valueAsString);
                break;
            default:
                throw new IllegalArgumentException("Unknown property");
        }
        checkIfPropertyWasSet(propertyName, valueAsString);
    }

    /**
     * Sets text property. Cleans input field, if there is something.
     *
     * @param propertyName name of the property that should change
     * @param value value to be set
     */
    private void applyText(String propertyName, String value) {
        WebElement input = WebElementProxyUtils.createProxyForElement(
                getCssSelectorForProperty(propertyName));
        String text = input.getAttribute("value");
        if (!value.equals(text)) {
            if (!text.isEmpty()) {
                ((JavascriptExecutor) driver).executeScript("$(\"input[id$=':" + propertyName + "Input']\").val('')");
            }
            input.sendKeys(value);
            MetamerPage.waitRequest(input, WaitRequestType.HTTP).submit();
        }
    }

    private void applyRadio(Tag tag, String valueToBeSet) {
        Validate.notEmpty(tag.radioElements, "No options from which can be selected.");

        for (WebElement element : tag.radioElements) {
            String attributeValue = element.getAttribute("value");
            if (valueToBeSet.equals(NULLSTRING)) {
                if (new StringEqualsWrapper(attributeValue).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                    if (!element.isSelected()) {
                        MetamerPage.waitRequest(element, WaitRequestType.HTTP).click();
                    }
                    return;
                }
            } else if (valueToBeSet.equals(attributeValue)) {
                if (!element.isSelected()) {
                    MetamerPage.waitRequest(element, WaitRequestType.HTTP).click();
                }
                return;
            } else if (attributeValue.contains(valueToBeSet)) {
                //for image selection radios, which value contains a source url of the image
                if (!element.isSelected()) {
                    MetamerPage.waitRequest(element, WaitRequestType.HTTP).click();
                }
                return;
            }
        }
        throw new IllegalArgumentException("No property with value " + valueToBeSet + " was found");
    }

    private void applySelect(Tag tag, String value) {
        Validate.notEmpty(tag.selection.getOptions(), "No options from which can be selected.");

        if (value.equals(NULLSTRING)) {
            for (WebElement element : tag.selection.getOptions()) {
                String val = element.getAttribute("value");
                if (new StringEqualsWrapper(val).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                    if (!element.isSelected()) {
                        MetamerPage.waitRequest(element, WaitRequestType.HTTP).click();
                    }
                    return;
                }
            }
        } else {
            tag.selection.selectByValue(value);
            return;
        }
        throw new IllegalArgumentException("No property with value " + value + " was found");
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

    private By getCssSelectorForProperty(String property) {
        return By.cssSelector(String.format(PROPERTY_CSS_SELECTOR, attributesID, property));
    }

    /**
     * Gets String representation of attribute value set in page.
     *
     * @param propertyName string name of attribute
     * @return
     */
    private String getProperty(String propertyName) {
        WebElement foundElementProxy = WebElementProxyUtils.createProxyForElement(
                getCssSelectorForProperty(propertyName));
        //handle the property by the tagname of the input element
        Tag tag = Tag.getTag(foundElementProxy);
        switch (tag) {
            case input:
                return foundElementProxy.getAttribute("value");
            case checkbox:
                throw new UnsupportedOperationException("Checkboxes are not supported");
            case radio:
                return getValueFromRadio(tag);
            case select:
                return getValueFromSelect(tag);
            default:
                throw new IllegalArgumentException("Unknown property: " + propertyName);
        }
    }

    private String getValueFromRadio(Tag tag) {
        Validate.notEmpty(tag.radioElements, "No inputs for this attribute found");

        WebElement nullSelectionOption = null;
        for (WebElement webElement : tag.radioElements) {
            String value = webElement.getAttribute("value");
            if (new StringEqualsWrapper(value).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                nullSelectionOption = webElement;
            }
            if (webElement.isSelected()) {
                return webElement.getAttribute("value");
            }
        }
        if (nullSelectionOption != null) {
            //workaround for String options with value="" , used in attributes like
            //action, actionListener, model...
            //they do not preserve its selected state
            return NULLSTRING;
        }
        throw new IllegalArgumentException("No selected choice for this attribute found.");
    }

    private String getValueFromSelect(Tag tag) {
        Validate.notEmpty(tag.selection.getOptions(), "No inputs for this attribute found");

        WebElement nullSelectionOption = null;
        for (WebElement webElement : tag.selection.getAllSelectedOptions()) {
            String value = webElement.getAttribute("value");
            if (new StringEqualsWrapper(value).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                nullSelectionOption = webElement;
            }
            if (webElement.isSelected()) {
                return webElement.getAttribute("value");
            }
        }
        if (nullSelectionOption != null) {
            //workaround for String options with value="" , used in attributes like
            //action, actionListener, model...
            //they do not preserve its selected state
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
    private void checkIfPropertyWasSet(String propertyName, String value) {
        String property;
        for (int i = 0; i < NUMBEROFTRIES; i++) {
            try {
                property = getProperty(propertyName);
                if (value.equals(NULLSTRING)) {
                    if (new StringEqualsWrapper(property).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                        return;
                    }
                } else if (property.contains(value)) {
                    return;
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

    private enum Tag {

        input("input"),
        radio("table"),
        checkbox("table"),
        select("select"),
        unknown("");
        private final String tagname;
        private List<WebElement> radioElements;
        private List<WebElement> checkboxElements;
        private Select selection;

        Tag(String tagname) {
            this.tagname = tagname;
        }

        public static Tag getTag(WebElement foundElement) {
            String elementTag = foundElement.getTagName();
            for (Tag t : values()) {
                if (t.tagname.equals(elementTag)) {
                    if (t.equals(radio) || t.equals(checkbox)) {
                        List<WebElement> foundElements = WebElementProxyUtils
                                .createProxyForElements(By.tagName("input"), foundElement);
                        String inputType = foundElements.get(0).getAttribute("type");
                        if ("radio".equals(inputType)) {
                            radio.radioElements = foundElements;
                            return radio;
                        } else if ("checkbox".equals(inputType)) { //not supported
                            checkbox.checkboxElements = foundElements;
                            return checkbox;
                        }
                        return t;
                    } else if (t.equals(select)) {
                        select.selection = new Select(foundElement);
                        return select;
                    } else {
                        return t;
                    }
                }
            }
            return unknown;
        }
    }
}
