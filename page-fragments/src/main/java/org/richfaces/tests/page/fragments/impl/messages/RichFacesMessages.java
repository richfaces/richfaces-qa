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
package org.richfaces.tests.page.fragments.impl.messages;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.message.Message;
import org.richfaces.tests.page.fragments.impl.message.Message.MessageType;

/**
 * Component for rich:messages.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesMessages implements Messages {

    @Root
    private WebElement root;
    //
    @FindBy(xpath = "./span")
    private List<RichFacesMessagesMessage> messages;
    @FindBy(css = "span.rf-msgs-err")
    private List<RichFacesMessagesMessage> errorMessages;
    @FindBy(css = "span.rf-msgs-ftl")
    private List<RichFacesMessagesMessage> fatalMessages;
    @FindBy(css = "span.rf-msgs-inf")
    private List<RichFacesMessagesMessage> infoMessages;
    @FindBy(css = "span.rf-msgs-ok")
    private List<RichFacesMessagesMessage> okMessages;
    @FindBy(css = "span.rf-msgs-wrn")
    private List<RichFacesMessagesMessage> warnMessages;
    //
    @Drone
    private WebDriver driver;

    @Override
    public List<Message> getAllMessagesOfType(MessageType type) {
        switch (type) {
            case OK:
                return Lists.<Message>newArrayList(okMessages);
            case INFORMATION:
                return Lists.<Message>newArrayList(infoMessages);
            case WARNING:
                return Lists.<Message>newArrayList(warnMessages);
            case ERROR:
                return Lists.<Message>newArrayList(errorMessages);
            case FATAL:
                return Lists.<Message>newArrayList(fatalMessages);
            default:
                throw new UnsupportedOperationException("Unknown type " + type);
        }
    }

    @Override
    public Message getMessageAtIndex(int index) {
        return messages.get(index);
    }

    @Override
    public List<Message> getMessagesForInput(String inputID) {
        List<Message> result = Lists.newArrayList();
        for (Message component : this) {
            if (component.getRoot().getAttribute("id").contains(inputID)) {
                result.add(component);
            }
        }
        return result;
    }

    @Override
    public WebElement getRoot() {
        return root;
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public Iterator<Message> iterator() {
        return new MessagesIterator(messages.iterator());
    }

    @Override
    public int size() {
        return messages.size();
    }

    private static class MessagesIterator implements Iterator<Message> {

        private final Iterator<RichFacesMessagesMessage> iterator;

        public MessagesIterator(Iterator<RichFacesMessagesMessage> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Message next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }
}
