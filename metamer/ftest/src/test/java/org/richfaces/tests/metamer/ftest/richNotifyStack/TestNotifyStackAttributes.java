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
package org.richfaces.tests.metamer.ftest.richNotifyStack;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.util.Locale;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.notify.NotifyMessage;
import org.richfaces.fragment.notify.RichFacesNotify;
import org.richfaces.fragment.notify.NotifyMessage.NotifyMessagePosition;
import org.richfaces.fragment.notify.RichFacesNotify.NotifyMessageItemImpl;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNotifyStackAttributes extends AbstractWebDriverTest {

    private final Attributes<NotifyStackAttributes> notifyStackAttributes = getAttributes();

    @FindBy(css = "input[id$='generateMessage']")
    private WebElement generateMessageButton;
    @FindBy(css = "input[id$='rerenderStack']")
    private WebElement rerenderStackButton;
    @FindBy(tagName = "body")
    private RichFacesNotify notify;
    @Use
    @Inject
    private Position position;
    @Use
    @Inject
    private Direction direction;
    @Use
    @Inject
    private Method method;

    private enum Direction {

        HORIZONTAL, VERTICAL;

        /**
         * Usable when position is set to topRight and method is first.
         */
        private static boolean isAfter(Direction d, Locations l1, Locations l2) {
            Point topRight1 = l1.getTopRight();
            Point topRight2 = l2.getTopRight();
            if (d.equals(Direction.HORIZONTAL)) {
                return (topRight1.x < topRight2.x);
            } else if (d.equals(Direction.VERTICAL)) {
                return (topRight1.y > topRight2.y);
            } else {
                throw new UnsupportedOperationException("Uknown direction " + d);
            }
        }

        /**
         * Usable when position is set to topRight and method is first.
         */
        public boolean isAfter(Locations l1, Locations l2) {
            return Direction.isAfter(this, l1, l2);
        }
    }

    private enum Method {

        FIRST, LAST;
    }

    private enum Position {

        BOTTOM_LEFT("bottomLeft"), BOTTOM_RIGHT("bottomRight"), TOP_LEFT("topLeft"), TOP_RIGHT("topRight");
        private final String value;

        private Position(String value) {
            this.value = value;
        }

        public NotifyMessagePosition getPosition() {
            return NotifyMessagePosition.valueOf(this.name());
        }
    }

    private void generateMessagesWithWait(int numberOfMessages) {
        for (int i = 0; i < numberOfMessages; i++) {
            final int expectedNumberOfMessages = i + 1;
            MetamerPage.waitRequest(generateMessageButton, WaitRequestType.XHR).click();
            Graphene.waitModel().until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver from) {
                    return notify.size() == expectedNumberOfMessages;
                }
            });
        }
    }

    private NotifyMessageItemImpl getMessageWithNumber(int number) {
        for (NotifyMessageItemImpl m : notify.getItems()) {
            if (m.getSummary().contains(String.valueOf(number))) {
                return m;
            }
        }
        return null;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richNotifyStack/simple.xhtml");
    }

    @Test
    @Uses({
        @Use(field = "direction", enumeration = true),
        @Use(field = "method", enumeration = true)
    })
    public void testDirectionAndMethod() {
        notifyStackAttributes.set(NotifyStackAttributes.direction, direction.name().toLowerCase(Locale.ENGLISH));
        notifyStackAttributes.set(NotifyStackAttributes.method, method.name().toLowerCase(Locale.ENGLISH));
        notifyStackAttributes.set(NotifyStackAttributes.position, "topRight");
        waiting(500); // workaround for document not ready error

        generateMessagesWithWait(3);
        NotifyMessage firstMessage = getMessageWithNumber(1);
        NotifyMessage secondMessage = getMessageWithNumber(2);
        NotifyMessage thirdMessage = getMessageWithNumber(3);
        Locations locationsM1 = Utils.getLocations(firstMessage.advanced().getRootElement());
        Locations locationsM2 = Utils.getLocations(secondMessage.advanced().getRootElement());
        Locations locationsM3 = Utils.getLocations(thirdMessage.advanced().getRootElement());
        if (method.equals(Method.FIRST)) {
            Assert.assertTrue(direction.isAfter(locationsM1, locationsM2));
            Assert.assertTrue(direction.isAfter(locationsM2, locationsM3));
        } else if (method.equals(Method.LAST)) {
            Assert.assertFalse(direction.isAfter(locationsM1, locationsM2));
            Assert.assertFalse(direction.isAfter(locationsM2, locationsM3));
        } else {
            throw new UnsupportedOperationException("Uknown method " + method);
        }
    }

    @Test
    @Use(field = "position", enumeration = true)
    public void testPosition() {
        notifyStackAttributes.set(NotifyStackAttributes.position, position.value);
        waiting(500); // workaround for document not ready error

        generateMessagesWithWait(3);
        NotifyMessage firstMessage = getMessageWithNumber(1);
        NotifyMessage secondMessage = getMessageWithNumber(2);
        NotifyMessage thirdMessage = getMessageWithNumber(3);
        Assert.assertEquals(firstMessage.advanced().getPosition(), position.getPosition());
        Assert.assertEquals(secondMessage.advanced().getPosition(), position.getPosition());
        Assert.assertEquals(thirdMessage.advanced().getPosition(), position.getPosition());
    }

    @Test
    public void testRendered() {
        notifyStackAttributes.set(NotifyStackAttributes.rendered, Boolean.TRUE);
        generateMessagesWithWait(1);
        assertVisible(getMessageWithNumber(1).getRootElement(), "Message should be visible");

        notifyStackAttributes.set(NotifyStackAttributes.rendered, Boolean.FALSE);
        try {
            generateMessagesWithWait(1);
        } catch (TimeoutException e) {
            Assert.assertEquals(notify.size(), 0, "There should be no message.");
            return; //OK
        }
        Assert.fail("There should be no message when @rendered=false");
    }

    @Test
    public void testRerenderStack() {
        waiting(500); // wait for notify to initialize

        generateMessagesWithWait(3);
        Assert.assertEquals(notify.size(), 3, "There should be 3 messages.");
        MetamerPage.waitRequest(rerenderStackButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver from) {
                return notify.size() == 0;
            }
        });
        Assert.assertEquals(notify.size(), 0, "There should be no messages visible after rerendering the stack.");
    }
}
