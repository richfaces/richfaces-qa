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
package org.richfaces.tests.metamer.ftest.webdriver;

import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.FutureTarget;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StringEqualsWrapper;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Attributes<T extends AttributeEnum> {

    private FutureTarget<WebDriver> driver;
    private final String attributesID;
    private static final String PROPERTY_CSS_SELECTOR = "[id$='%s:%sInput']";
    private static final String NULLSTRING = "null";
    private static final String[] NULLSTRINGOPTIONS = { NULLSTRING, "", " " };

    @Deprecated
    public Attributes() {
        this("");
    }

    @Deprecated
    public Attributes(String attributesID) {
        this(null, attributesID);
    }

    public Attributes(FutureTarget<WebDriver> driver, String attributesID) {
        this.attributesID = attributesID;
        this.driver = driver;
    }

    public static <T extends AttributeEnum> Attributes<T> getAttributesFor(FutureTarget<WebDriver> driver) {
        return getAttributesFor(driver, "");
    }

    public static <T extends AttributeEnum> Attributes<T> getAttributesFor(FutureTarget<WebDriver> driver, String attributeTableID) {
        return new Attributes<T>(driver, attributeTableID);
    }

    private void applyRadio(List<WebElement> radioElements, String valueToBeSet) {
        for (WebElement element : radioElements) {
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

    private void applySelect(WebElement selectElement, String valueToBeSet) {
        Select select = new Select(selectElement);
        if (valueToBeSet.equals(NULLSTRING)) {
            for (WebElement element : select.getOptions()) {
                String val = element.getAttribute("value");
                if (new StringEqualsWrapper(val).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                    if (!element.isSelected()) {
                        MetamerPage.waitRequest(element, WaitRequestType.HTTP).click();
                    }
                    return;
                }
            }
        } else {
            if (isSelectOptionsContainingValue(valueToBeSet, select.getOptions())) {
                select.selectByValue(valueToBeSet);
            } else {
                select.selectByVisibleText(valueToBeSet);
            }
            return;
        }
        throw new IllegalArgumentException("No property with value " + valueToBeSet + " was found");
    }

    /**
     * Sets text property. Cleans input field, if there is something.
     *
     * @param propertyName name of the property that should change
     * @param value value to be set
     */
    private void applyText(WebElement input, String value) {
        String text = input.getAttribute("value");
        if (!value.equals(text)) {
            Utils.jQ((JavascriptExecutor) getDriver(), "val(\"" + value + "\")", input);
            MetamerPage.waitRequest(input, WaitRequestType.HTTP).submit();
        }
    }

    /**
     * Wait for page to load after attribute was set and then ckecks if it
     * really was set. If it was not set, then IllegalStateException is thrown.
     *
     * @param propertyName string value of attribute
     * @param value value that the attribute should have
     */
    private void checkIfPropertyWasSet(final String propertyName, final String value) {
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            private String foundValue;

            @Override
            public boolean apply(WebDriver input) {
                foundValue = getProperty(propertyName);
                if (value.equals(NULLSTRING)) {
                    if (new StringEqualsWrapper(foundValue).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                        return true;
                    }
                } else if (foundValue.contains(value)) {
                    return true;
                }
                return false;
            }

            @Override
            public String toString() {
                return "property '" + propertyName + "' to be set to value '" + value + "'. Actual value '" + foundValue + "'.";
            }
        });
    }

    /**
     * Retrieve current attribute value
     *
     * @param attribute
     * @return current attribute value
     */
    public String get(T attribute) {
        // convert to lowercase if needed
        // For example, 'for' is reserved java word, but also a
        // valid richfaces attribute. So we use this attribute name in upper case in enum
        String propertyName = attribute.toString();
        String propertyNameCorrect = (propertyName.equals(propertyName.toUpperCase()) ? propertyName.toLowerCase() : propertyName);
        return getProperty(propertyNameCorrect);
    }

    private By getCssSelectorForProperty(String property) {
        return By.cssSelector(String.format(PROPERTY_CSS_SELECTOR, attributesID, property));
    }

    private WebDriver getDriver() {
        if (driver == null) {
            return GrapheneContext.getContextFor(Default.class).getWebDriver(JavascriptExecutor.class);
        }
        return driver.getTarget();
    }

    /**
     * Gets String representation of attribute value set in page.
     *
     * @param propertyName string name of attribute
     * @return
     */
    private String getProperty(String propertyName) {
        By by = getCssSelectorForProperty(propertyName);
        WebElement element = getDriver().findElement(by);
        Graphene.waitModel().until().element(element).is().visible();
        SearchResult result = SearchResult.getResultForElement(element);
        switch (result.getTag()) {
            case input:
                return result.getElements().get(0).getAttribute("value");
            case checkbox:
                throw new UnsupportedOperationException("Checkboxes are not supported");
            case radio:
                return getValueFromRadio(result.getElements());
            case select:
                return getValueFromSelect(result.getElements().get(0));
            default:
                throw new IllegalArgumentException("Unknown property: " + propertyName);
        }
    }

    private String getValueFromList(List<WebElement> list) {
        boolean foundNullSelectionOption = false;
        for (WebElement webElement : list) {
            String value = webElement.getAttribute("value");
            if (new StringEqualsWrapper(value).isSimilarToSomeOfThis(NULLSTRINGOPTIONS)) {
                foundNullSelectionOption = true;
            }
            if (webElement.isSelected()) {
                return webElement.getAttribute("value");
            }
        }
        if (foundNullSelectionOption) {
            //workaround for String options with value="" , used in attributes like
            //action, actionListener, model...
            //they do not preserve its selected state
            return NULLSTRING;
        }
        throw new IllegalArgumentException("No selected choice for this attribute found.");
    }

    private String getValueFromRadio(List<WebElement> radioElements) {
        return getValueFromList(radioElements);
    }

    private String getValueFromSelect(WebElement selectElement) {
        return getValueFromList(new Select(selectElement).getAllSelectedOptions());
    }

    private boolean isSelectOptionsContainingValue(String value, List<WebElement> options) {
        for (Iterator<WebElement> i = options.iterator(); i.hasNext();) {
            if (value.equals(i.next().getAttribute("value"))) {
                return true;
            }
        }
        return false;
    }

    public void reset(T attribute) {
        set(attribute.toString(), "");
    }

    public void set(T attribute, String string) {
        set(attribute.toString(), string);
    }

    protected void set(String attribute, Object value) {
        setProperty(attribute, value);
    }

    // TODO jjamrich 2011-09-02: make sure that this resolve to correct string representation of number given as attr
    public void set(T attribute, Number no) {
        set(attribute.toString(), no);
    }

    public void set(T attribute, Boolean bool) {
        set(attribute.toString(), bool);
    }

    public void set(T attribute, Enum<?> item) {
        set(attribute.toString(), item.toString());
    }

    public void set(T attribute, Event event) {
        set(attribute.toString(), event.getEventName());
    }

    /**
     * Sets attribute and checks if it was really set (waits for page
     * re-render).
     *
     * @param propertyName
     * @param value
     */
    protected void setProperty(String propertyName, Object value) {
        // convert to lowercase if needed
        // For example, 'for' is reserved java word, but also a
        // valid richfaces attribute. So we use this attribute name in upper case in enum
        String propertyNameCorrect = (propertyName.equals(propertyName.toUpperCase()) ? propertyName.toLowerCase() : propertyName);

        String valueAsString = (value == null ? NULLSTRING : value.toString());
        valueAsString = valueAsString.replaceAll("\"", "'");

        //element for all types of input elements
        By by = getCssSelectorForProperty(propertyNameCorrect);
        WebElement element = getDriver().findElement(by);
        Graphene.waitModel().until().element(element).is().visible();
        SearchResult result = SearchResult.getResultForElement(element);
        switch (result.getTag()) {
            case input:
                applyText(result.getElements().get(0), valueAsString);
                break;
            case checkbox:
                throw new UnsupportedOperationException("Checkboxes are not supported");
            case radio:
                applyRadio(result.getElements(), valueAsString);
                break;
            case select:
                applySelect(result.getElements().get(0), valueAsString);
                break;
            default:
                throw new IllegalArgumentException("Unknown property " + result.getTag());
        }
        checkIfPropertyWasSet(propertyNameCorrect, valueAsString);
    }

    private abstract static class SearchResult {

        private final Tag tag;
        protected final Optional<List<WebElement>> elements;

        public SearchResult(Tag tag, List<WebElement> elements) {
            this.tag = tag;
            this.elements = Optional.fromNullable(elements);
        }

        public List<WebElement> getElements() {
            checkIfItIsCorrect();
            return elements.get();
        }

        public Tag getTag() {
            checkIfItIsCorrect();
            return tag;
        }

        public abstract void checkIfItIsCorrect();

        public static SearchResult getResultForElement(WebElement element) {
            String elementTag = element.getTagName();
            List<WebElement> foundElements;
            for (Tag t : Tag.values()) {
                if (t.getTagname().equals(elementTag)) {
                    switch (t) {
                        case checkbox:
                        case radio:
                            foundElements = element.findElements(By.tagName("input"));
                            return new NotEmptySearchResult(t, foundElements);
                        case input:
                        case select:
                            foundElements = Lists.newArrayList(element);
                            return new SingleElementSearchResult(t, foundElements);
                    }
                }
            }
            return new NotEmptySearchResult(null, null);
        }
    }

    private static class NotEmptySearchResult extends SearchResult {

        public NotEmptySearchResult(Tag tag, List<WebElement> elements) {
            super(tag, elements);
        }

        @Override
        public void checkIfItIsCorrect() {
            if (elements.get().isEmpty()) {// null reference is handled by Optional
                throw new RuntimeException("The found result is empty. " + elements.toString());
            }
        }
    }

    private static class SingleElementSearchResult extends SearchResult {

        public SingleElementSearchResult(Tag tag, List<WebElement> elements) {
            super(tag, elements);
        }

        @Override
        public void checkIfItIsCorrect() {
            if (elements.get().size() != 1) {// null reference is handled by Optional
                throw new RuntimeException("The results size is not equal to 1. " + elements.toString());
            }
        }
    }

    private enum Tag {

        input("input"),
        radio("table"),
        checkbox("table"),
        select("select");

        private final String tagname;

        Tag(String tagname) {
            this.tagname = tagname;
        }

        public String getTagname() {
            return tagname;
        }
    }
}
