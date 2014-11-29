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

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.FutureTarget;
import org.richfaces.tests.metamer.ftest.extension.tester.Tester;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.BasicTestChain;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface HTMLAttributeTestChain extends BasicTestChain<HTMLAttributeTestChain> {

    Tester testDir(WebElement visibleElement);

    Tester testDir(SearchContext context, By by);

    Tester testDir(FutureTarget<WebElement> visibleAfterSomeActionElement);

    HTMLAttributeTestChainValue testHTMLAttribute(Object attribute);

    HTMLAttributeTestChainValue testHTMLAttribute(String attributeName);

    Tester testStyle(WebElement visibleElement);

    Tester testStyle(SearchContext context, By by);

    Tester testStyle(FutureTarget<WebElement> visibleAfterSomeActionElement);

    Tester testStyleClass(WebElement visibleElement);

    Tester testStyleClass(SearchContext context, By by);

    Tester testStyleClass(FutureTarget<WebElement> visibleAfterSomeActionElement);

    Tester testTitle(WebElement visibleElement);

    Tester testTitle(SearchContext context, By by);

    Tester testTitle(FutureTarget<WebElement> visibleAfterSomeActionElement);

    public interface HTMLAttributeTestChainValue {

        HTMLAttributeTestChainOn withValue(Object value);

        HTMLAttributeTestChainOn withValue(String value);
    }

    public interface HTMLAttributeTestChainOn {

        HTMLAttributesTestChainAdditionalConfig onElement(WebElement visibleElement);

        HTMLAttributesTestChainAdditionalConfig onElement(SearchContext context, By by);

        HTMLAttributesTestChainAdditionalConfig onElement(FutureTarget<WebElement> visibleAfterSomeActionElement);

    }

    public interface HTMLAttributesTestChainAdditionalConfig extends BasicTestChain<HTMLAttributeTestChain> {

        HTMLAttributeTestChain attributeHasDifferentNameOnPage(Object value);

        HTMLAttributeTestChain attributeHasDifferentNameOnPage(String value);

        HTMLAttributeTestChain valueWillBeChangedTo(Object expectedValue);

        HTMLAttributeTestChain valueWillBeChangedTo(String expectedValue);

    }
}
