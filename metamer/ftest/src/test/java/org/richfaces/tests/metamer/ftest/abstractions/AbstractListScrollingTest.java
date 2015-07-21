/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Keyboard;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.utils.MetamerJavascriptUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractListScrollingTest extends AbstractWebDriverTest {

    @JavaScript
    private MetamerJavascriptUtils jsUtils;
    @ArquillianResource
    private Keyboard keyboard;

    protected void checkScrollingWithKeyboard(WebElement focusElement, List<WebElement> listOfItems) {
        checkScrollingWithKeyboard(focusElement, listOfItems, null);
    }

    @SuppressWarnings("unchecked")
    protected void checkScrollingWithKeyboard(WebElement focusElement, List<WebElement> listOfItems, Action actionBefore) {
        // to stabilize test in all templates
        getUnsafeAttributes("").set("listHeight", 60);

        if (actionBefore != null) {
            actionBefore.perform();
        }

        WebElement lastItem = ChoicePickerHelper.byIndex().last().pick(listOfItems);
        WebElement firstItem = listOfItems.get(0);

        lastItem.click();// focus on last item
        assertFalse(jsUtils.isElementInViewPort(firstItem), "First item should not be visible!");
        assertTrue(jsUtils.isElementInViewPort(lastItem), "Last item should be visible!");

        jsUtils.scrollToView(focusElement);
        keyboard.sendKeys(Keys.DOWN);
        assertTrue(jsUtils.isElementInViewPort(firstItem), "First item should be visible!");
        assertFalse(jsUtils.isElementInViewPort(lastItem), "Last item should not be visible!");

        keyboard.sendKeys(Keys.UP);
        assertFalse(jsUtils.isElementInViewPort(firstItem), "First item should not be visible!");
        assertTrue(jsUtils.isElementInViewPort(lastItem), "Last item should be visible!");

        for (int i = 0; i < 10; i++) {
            keyboard.sendKeys(Keys.UP);
        }
        assertFalse(jsUtils.isElementInViewPort(firstItem), "First item should not be visible!");
        assertFalse(jsUtils.isElementInViewPort(lastItem), "Last item should not be visible!");
    }
}
