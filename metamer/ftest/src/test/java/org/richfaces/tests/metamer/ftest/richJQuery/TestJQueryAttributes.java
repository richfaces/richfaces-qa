/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richJQuery;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.awt.Color;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.support.color.ColorUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestJQueryAttributes extends AbstractWebDriverTest {

    private final Attributes<JQueryAttributes> jQueryAttributes = getAttributes();

    @FindBy(css = "#jQueryTestButton")
    private WebElement button;
    @FindBy(css = "#rebindOneClickButton")
    private WebElement rebind;
    @FindBy(css = "input[id$=addComponentButton]")
    private WebElement addLiveComponent;
    @FindBy(css = "div.liveTestComponent")
    private List<WebElement> liveTestComponent;

    private Color getButtonColor() {
        return ColorUtils.convertToAWTColor(button.getCssValue("color"));
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richJQuery/simple.xhtml");
    }

    private void setupDomReadyTypeAttributes() {
        jQueryAttributes.reset(JQueryAttributes.event);
        jQueryAttributes.set(JQueryAttributes.query, "css('color', 'red')");
        jQueryAttributes.set(JQueryAttributes.selector, "#jQueryTestButton");
        jQueryAttributes.set(JQueryAttributes.timing, "domready");
    }

    private void setupImmediateTypeAttributes() {
        jQueryAttributes.set(JQueryAttributes.event, "click");
        jQueryAttributes.set(JQueryAttributes.query, "$(this).css('color', 'red')");
        jQueryAttributes.set(JQueryAttributes.selector, "#jQueryTestButton");
        jQueryAttributes.set(JQueryAttributes.timing, "immediate");
    }

    @Test
    public void testAttachTypeLive() {
        for (int count = 1; count <= 4; count++) {
            if (count > 1) {
                addLiveComponent.click();
                Graphene.waitAjax().until(new ListSizeEqualsWaiting(count, liveTestComponent));
            }
            for (int i = 1; i <= count; i++) {
                executeJS("metamerEvents=''");
                WebElement component = liveTestComponent.get(i - 1);
                String message = component.getText();
                component.click();
                String events = expectedReturnJS("return metamerEvents", message);
                assertEquals(events, message);
            }
        }
    }

    @Test(groups = "smoke")
    public void testAttachTypeOne() {
        setupImmediateTypeAttributes();
        jQueryAttributes.set(JQueryAttributes.attachType, "one");
        jQueryAttributes.set(JQueryAttributes.query, "metamerEvents+=\"first \"");

        button.click();
        String events = expectedReturnJS("return metamerEvents", "first");
        assertEquals(events, "first");
        button.click();
        button.click();
        events = expectedReturnJS("return metamerEvents", "first");
        assertEquals(events, "first", "There should be only one event fired when @attachType is set to 'one'.");
        for (int i = 0; i < 3; i++) {
            executeJS("metamerEvents=''");
            rebind.click();
            button.click();
            events = expectedReturnJS("return metamerEvents", "one attachType rebound event");
            assertEquals(events, "one attachType rebound event");
        }
    }

    @Test
    public void testDefaultTiming() {
        setupDomReadyTypeAttributes();
        jQueryAttributes.reset(JQueryAttributes.timing);
        Graphene.waitAjax().until(new ColorEqualsWaiting(Color.RED));
    }

    @Test
    public void testTimingDomReady() {
        setupDomReadyTypeAttributes();
        Graphene.waitAjax().until(new ColorEqualsWaiting(Color.RED));
    }

    @Test
    public void testTimingImmediate() {
        setupImmediateTypeAttributes();
        button.click();
        Graphene.waitAjax().until(new ColorEqualsWaiting(Color.RED));
    }

    private static class ListSizeEqualsWaiting implements Predicate<WebDriver> {

        private final int size;
        private int sizeReturned;
        private final List<WebElement> list;

        public ListSizeEqualsWaiting(int size, List<WebElement> list) {
            this.size = size;
            this.list = list;
        }

        @Override
        public boolean apply(WebDriver input) {
            return (sizeReturned = list.size()) == size;
        }

        @Override
        public String toString() {
            return String.format("list size to be equal to <%d>, got <%d>.", size, sizeReturned);
        }
    }

    private class ColorEqualsWaiting implements Predicate<WebDriver> {

        private final Color color;
        private Color colorReturned;

        public ColorEqualsWaiting(Color color) {
            this.color = color;
        }

        @Override
        public boolean apply(WebDriver input) {
            return (colorReturned = getButtonColor()).equals(color);
        }

        @Override
        public String toString() {
            return String.format("color to be equal to <%s>, got <%s>.", color, colorReturned);
        }
    }
}
