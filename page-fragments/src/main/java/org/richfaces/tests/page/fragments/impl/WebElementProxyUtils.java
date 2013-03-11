/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl;

import java.util.List;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Utilities for creating proxies of WebElements. Reusing Arquillian-Graphene's
 * enricher methods.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class WebElementProxyUtils {

    private static final WebElementProxier proxier = new WebElementProxier();

    /**
     * Creates proxy for WebElement found by @by. Proxy protects from
     * StaleElementReferenceExceptions.
     *
     * @param by locator of element
     * @return proxied WebElement
     */
    public static WebElement createProxyForElement(By by) {
        return proxier.proxyForWebElement(by);
    }

    /**
     * Creates proxy for WebElement found by @by from @context. Proxy protects
     * from StaleElementReferenceExceptions.
     *
     * @param by locator of element
     * @param context context from which will be the element searched
     * @return proxied WebElement
     */
    public static WebElement createProxyForElement(By by, SearchContext context) {
        return proxier.proxyForWebElement(by, context);
    }

    /**
     * Creates proxy for list of proxied WebElements found by @by. Proxy protects
     * from StaleElementReferenceExceptions.
     *
     * @param by locator of elements
     * @return proxied list of WebElements
     */
    public static List<WebElement> createProxyForElements(By by) {
        return proxier.proxyForWebElements(by);
    }

    /**
     * Creates proxy for list of WebElements found by @by from @context. Proxy
     * protects from StaleElementReferenceExceptions.
     *
     * @param by locator of elements
     * @param context context from which will be the elements searched
     * @return proxied list of WebElements
     */
    public static List<WebElement> createProxyForElements(By by, SearchContext context) {
        return proxier.proxyForWebElements(by, context);
    }

    private static class WebElementProxier {

        public final WebElement proxyForWebElement(By by) {
            return WebElementUtils.findElementLazily(by, GrapheneContext.getProxy());
        }

        protected final WebElement proxyForWebElement(By by, SearchContext searchContext) {
            return WebElementUtils.findElementLazily(by, searchContext);
        }

        public List<WebElement> proxyForWebElements(By by) {
            return WebElementUtils.findElementsLazily(by, GrapheneContext.getProxy());
        }

        public List<WebElement> proxyForWebElements(By by, SearchContext searchContext) {
            return WebElementUtils.findElementsLazily(by, searchContext);
        }
    }
}
