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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

/**
 * Decorates {@link WebElement} to try to avoid throwing {@link StaleElementReferenceException}.
 * When the exception is thrown, the mechanism tries to locate element again.
 *
 * Also decorates List of WebElements.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 *
 * @see
 * http://www.brimllc.com/2011/01/extending-selenium-2-0-webdriver-to-support-ajax/
 */
public class StaleReferenceAwareFieldDecorator extends DefaultFieldDecorator {

    private final int numberOfTries;
    private static final int WAIT_TIME = 100;//ms

    /**
     * Creates a new instance of the decorator
     *
     * @param factory locator factory
     * @param numberOfTries number of tries to locate element
     */
    public StaleReferenceAwareFieldDecorator(ElementLocatorFactory factory, int numberOfTries) {
        super(factory);
        this.numberOfTries = numberOfTries;
    }

    @Override
    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new StaleReferenceAwareElementLocator(locator);

        WebElement proxy = (WebElement) Proxy.newProxyInstance(loader, new Class[]{WebElement.class,
                    WrapsElement.class}, handler);
        return proxy;
    }

    /**
     * Works only for List methods get and iterator.
     */
    @Override
    protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new StaleReferenceAwareElementsLocator(locator);
        List<WebElement> proxy = (List<WebElement>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
        return proxy;
    }

    private class StaleReferenceAwareElementLocator extends LocatingElementHandler {

        private final ElementLocator locator;

        public StaleReferenceAwareElementLocator(ElementLocator locator) {
            super(locator);
            this.locator = locator;
        }

        public Object invoke(Object object, Method method, Object[] objects) throws Throwable {

            WebElement element = null;
            for (int i = 0; i < numberOfTries; i++) {
                element = locator.findElement();
                if ("getWrappedElement".equals(method.getName())) {
                    return element;
                }
                try {
                    return invokeMethod(method, element, objects);
                } catch (StaleElementReferenceException ignored) {
                    waitSomeTime();
                }
            }
            throw new RuntimeException("Cannot invoke " + method.getName() + " on element " + element
                    + ". Cannot find it");
        }

        private Object invokeMethod(Method method, WebElement element, Object[] objects) throws Throwable {
            try {
                return method.invoke(element, objects);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            } catch (IllegalArgumentException e) {
                throw e.getCause();
            } catch (IllegalAccessException e) {
                throw e.getCause();
            }
        }
    }

    private void waitSomeTime() {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException ignored) {
        }
    }

    private class StaleReferenceAwareElementsLocator extends LocatingElementListHandler {

        private final ElementLocator locator;
        private final String GET = "get";
        private final String ITERATOR = "iterator";

        public StaleReferenceAwareElementsLocator(ElementLocator locator) {
            super(locator);
            this.locator = locator;
        }

        /**
         * Checks web element if it is ok. If it is not, then an exception is
         * thrown.
         */
        private void testElement(final WebElement we) throws StaleElementReferenceException {
            we.getLocation();
            we.getText();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            List<WebElement> elements = locator.findElements();
            if (GET.equals(method.getName())) {//method get is invoked
                for (int i = 0; i < numberOfTries; i++) {
                    try {//try to test the chosen element
                        testElement(elements.get((Integer) args[0]));
                        return method.invoke(elements, args);//element is ok and returned
                    } catch (StaleElementReferenceException ignored) {
                        //element not found, wait some time and try it again
                        waitSomeTime();
                        elements = locator.findElements();
                    }
                }
                throw new RuntimeException("Cannot invoke " + method.getName() + " on elements " + elements
                        + ". Cannot find it");
            } else if (ITERATOR.equals(method.getName())) {//method iterator is invoked
                for (int i = 0; i < numberOfTries; i++) {
                    try { //go through all elements and test them
                        for (WebElement webElement : elements) {
                            testElement(webElement);
                        }
                        return method.invoke(elements, args);//all elements should be ok
                    } catch (StaleElementReferenceException ignored) {
                        //some element not found, wait some time and try it again
                        waitSomeTime();
                        elements = locator.findElements();
                    }
                }
                throw new RuntimeException("Cannot invoke " + method.getName() + " on elements " + elements
                        + ". Cannot find it");
            }
            return method.invoke(elements, args);
        }
    }
}
