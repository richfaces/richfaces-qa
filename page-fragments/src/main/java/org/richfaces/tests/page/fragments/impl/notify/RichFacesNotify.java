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
package org.richfaces.tests.page.fragments.impl.notify;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.message.Message.MessageType;

/**
 * Always use with @FindBy(tagName="body"). Global component.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesNotify implements Notify {

    @Root
    private WebElement rootElement;
    @Drone
    private WebDriver driver;
    //
    @FindBy(css = "div.rf-ntf")
    private List<RichFacesNotifyMessage> messages;
    @FindBy(css = "div.rf-ntf.rf-ntf-err")
    private List<RichFacesNotifyMessage> errorMessages;
    @FindBy(css = "div.rf-ntf.rf-ntf-ftl")
    private List<RichFacesNotifyMessage> fatalMessages;
    @FindBy(css = "div.rf-ntf.rf-ntf-inf")
    private List<RichFacesNotifyMessage> infoMessages;
    @FindBy(css = "div.rf-ntf.rf-ntf-wrn")
    private List<RichFacesNotifyMessage> warnMessages;

    @Override
    public List<NotifyMessage> getAllMessagesOfType(MessageType type) {
        switch (type) {
            case OK:
                throw new UnsupportedOperationException("Notify messages does not support messages of type 'OK'.");
            case INFORMATION:
                return Lists.<NotifyMessage>newArrayList(infoMessages);
            case WARNING:
                return Lists.<NotifyMessage>newArrayList(warnMessages);
            case ERROR:
                return Lists.<NotifyMessage>newArrayList(errorMessages);
            case FATAL:
                return Lists.<NotifyMessage>newArrayList(fatalMessages);
            default:
                throw new UnsupportedOperationException("Unknown type " + type);
        }
    }

    @Override
    public NotifyMessage getMessageAtIndex(int index) {
        return messages.get(index);
    }

    @Override
    public WebElement getRoot() {
        return rootElement;
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return messages.isEmpty();
            }
        };
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !messages.isEmpty();
            }
        };
    }

    @Override
    public Iterator<NotifyMessage> iterator() {
        return new NotifyMessagesIterator(messages.iterator());
    }

    @Override
    public int size() {
        return messages.size();
    }

    private static class NotifyMessagesIterator implements Iterator<NotifyMessage> {

        private final Iterator<RichFacesNotifyMessage> iterator;

        public NotifyMessagesIterator(Iterator<RichFacesNotifyMessage> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public NotifyMessage next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }
}
