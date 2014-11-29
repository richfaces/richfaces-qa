/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.extension.tester.html;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.FutureTarget;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.FutureWebElement;
import org.richfaces.tests.metamer.ftest.extension.tester.Tester;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.BasicTestChainImpl;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.MultipleTester;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.TestResourcesProvider;
import org.richfaces.tests.metamer.ftest.extension.tester.events.TriggerSingleEventTestChainImpl;
import org.richfaces.tests.metamer.ftest.extension.tester.events.TriggerSingleEventTestChainImpl.ObjectWrapper;
import org.richfaces.tests.metamer.ftest.extension.tester.html.HTMLAttributeTestChain.HTMLAttributeTestChainOn;
import org.richfaces.tests.metamer.ftest.extension.tester.html.HTMLAttributeTestChain.HTMLAttributeTestChainValue;
import org.richfaces.tests.metamer.ftest.extension.tester.html.HTMLAttributeTestChain.HTMLAttributesTestChainAdditionalConfig;

import com.google.common.base.Predicate;

public class HTMLAttributeTestChainImpl extends BasicTestChainImpl<HTMLAttributeTestChain> implements HTMLAttributeTestChain, HTMLAttributeTestChainValue, HTMLAttributeTestChainOn, HTMLAttributesTestChainAdditionalConfig {

    private static final String CLASS = "class";
    private static final String CLASSES = "classes";
    private static final String STYLE = "style";
    private static final String STYLECLASS = "styleClass";
    private static final String TESTED_STYLE = "background-color: red;";
    private static final String TESTED_STYLECLASS = "metamer-ftest-class";
    private static final String TESTED_TITLE = "RichFaces 4.5";
    private static final String TITLE = "title";

    private String attributeName;
    private String attributeOnPageName;
    private final Action checkAction = new Action() {
        private final ObjectWrapper<String> wrappedString = new TriggerSingleEventTestChainImpl.ObjectWrapper<String>();

        @Override
        public void perform() {
            try {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver t) {
                        wrappedString.setObject(onElement.getTarget().getAttribute(getHTMLAttributeName()));
                        return wrappedString.getObject().toLowerCase().contains(getExpectedValue().toLowerCase());
                    }
                });
            } catch (TimeoutException e) {
                assertTrue(wrappedString.getObject().toLowerCase().contains(getExpectedValue().toLowerCase()),
                    format("Attribute <{0}> does not work. Tested attribute should contain value <{1}>, but contained: <{2}>.",
                        attributeName, getExpectedValue(), wrappedString.getObject()));
            }

        }

        @Override
        public String toString() {
            return "checkAction";
        }
    };
    private String expectedValue;
    private FutureTarget<WebElement> onElement;
    private String testedValue;

    public HTMLAttributeTestChainImpl(TestResourcesProvider provider) {
        super(provider);
    }

    @Override
    public HTMLAttributeTestChain attributeHasDifferentNameOnPage(Object value) {
        return attributeHasDifferentNameOnPage(String.valueOf(value));
    }

    @Override
    public HTMLAttributeTestChain attributeHasDifferentNameOnPage(String value) {
        this.attributeOnPageName = value;
        return this;
    }

    private String getExpectedValue() {
        return expectedValue == null ? testedValue : expectedValue;
    }

    private String getHTMLAttributeName() {
        if (attributeOnPageName == null) {
            attributeOnPageName = attributeName;
        }
        return attributeOnPageName;
    }

    @Override
    public HTMLAttributesTestChainAdditionalConfig onElement(WebElement visibleElement) {
        return onElement(FutureWebElement.of(visibleElement));
    }

    @Override
    public HTMLAttributesTestChainAdditionalConfig onElement(SearchContext context, By by) {
        return onElement(FutureWebElement.of(by, context));
    }

    @Override
    public HTMLAttributesTestChainAdditionalConfig onElement(FutureTarget<WebElement> visibleAfterSomeActionElement) {
        this.onElement = visibleAfterSomeActionElement;
        setupCheckingAction().addLast(checkAction);
        return this;
    }

    @Override
    public Tester testDir(final WebElement visibleElement) {
        return testDir(FutureWebElement.of(visibleElement));
    }

    @Override
    public Tester testDir(SearchContext context, By by) {
        return testDir(FutureWebElement.of(by, context));
    }

    @Override
    public Tester testDir(FutureTarget<WebElement> visibleAfterSomeActionElement) {
        // test values: null, rtl, ltr
        return MultipleTester.successOfAllTesters(
            new HTMLAttributeTestChainImpl(getTestResources())
            .testHTMLAttribute("dir")
            .withValue("null")
            .onElement(visibleAfterSomeActionElement)
            .valueWillBeChangedTo(""),
            new HTMLAttributeTestChainImpl(getTestResources())
            .testHTMLAttribute("dir")
            .withValue("rtl")
            .onElement(visibleAfterSomeActionElement),
            new HTMLAttributeTestChainImpl(getTestResources())
            .testHTMLAttribute("dir")
            .withValue("ltr")
            .onElement(visibleAfterSomeActionElement)
        );
    }

    @Override
    public HTMLAttributeTestChainValue testHTMLAttribute(Object attribute) {
        return testHTMLAttribute(String.valueOf(attribute));
    }

    @Override
    public HTMLAttributeTestChainValue testHTMLAttribute(String attributeName) {
        this.attributeName = attributeName;
        return this;
    }

    @Override
    public Tester testStyle(WebElement visibleElement) {
        return testStyle(FutureWebElement.of(visibleElement));

    }

    @Override
    public Tester testStyle(SearchContext context, By by) {
        return testStyle(FutureWebElement.of(by, context));
    }

    @Override
    public Tester testStyle(FutureTarget<WebElement> visibleAfterSomeActionElement) {
        return testHTMLAttribute(STYLE)
            .withValue(TESTED_STYLE)
            .onElement(visibleAfterSomeActionElement);
    }

    @Override
    public Tester testStyleClass(WebElement visibleElement) {
        return testStyleClass(FutureWebElement.of(visibleElement));
    }

    @Override
    public Tester testStyleClass(SearchContext context, By by) {
        return testStyleClass(FutureWebElement.of(by, context));
    }

    @Override
    public Tester testStyleClass(FutureTarget<WebElement> visibleAfterSomeActionElement) {
        return testHTMLAttribute(STYLECLASS)
            .withValue(TESTED_STYLECLASS)
            .onElement(visibleAfterSomeActionElement)
            .attributeHasDifferentNameOnPage(CLASS);
    }

    @Override
    public Tester testTitle(WebElement visibleElement) {
        return testTitle(FutureWebElement.of(visibleElement));
    }

    @Override
    public Tester testTitle(SearchContext context, By by) {
        return testTitle(FutureWebElement.of(by, context));
    }

    @Override
    public Tester testTitle(FutureTarget<WebElement> visibleAfterSomeActionElement) {
        return testHTMLAttribute(TITLE)
            .withValue(TESTED_TITLE)
            .onElement(visibleAfterSomeActionElement);
    }

    @Override
    public String toString() {
        return "HTMLAttributeTestChainImpl{" + "attributeName=" + attributeName + ", expectedValue=" + expectedValue + ", testedValue=" + testedValue + '}';
    }

    @Override
    public HTMLAttributeTestChain valueWillBeChangedTo(String expectedValue) {
        this.expectedValue = expectedValue;
        return this;
    }

    @Override
    public HTMLAttributeTestChain valueWillBeChangedTo(Object expectedValue) {
        return valueWillBeChangedTo(String.valueOf(expectedValue));
    }

    @Override
    public HTMLAttributeTestChainOn withValue(Object value) {
        return withValue(String.valueOf(value));
    }

    @Override
    public HTMLAttributeTestChainOn withValue(String value) {
        this.testedValue = value;
        setupAttributes()
            .setAttribute(attributeName).toValue(value);
        return this;
    }
}
