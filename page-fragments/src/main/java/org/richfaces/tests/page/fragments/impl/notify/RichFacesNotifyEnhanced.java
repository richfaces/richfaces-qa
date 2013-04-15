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
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.message.Message.MessageType;

/**
 * Always use with @FindBy(tagName="body"). Global component. Can filter out
 * messages which don't contain a marker styleClass (use #setStyleClassToContain(String styleClassToContain)).
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesNotifyEnhanced implements Notify {

    @Root
    private WebElement rootElement;
    @Drone
    private WebDriver driver;
    private String styleClassToContain = "";

    @Override
    public List<NotifyMessage> getAllMessagesOfType(MessageType type) {
        switch (type) {
            case OK:
                throw new UnsupportedOperationException("Notify messages does not support messages of type 'OK'.");
            case INFORMATION:
                return Lists.<NotifyMessage>newArrayList(getInfoMessages());
            case WARNING:
                return Lists.<NotifyMessage>newArrayList(getWarnMessages());
            case ERROR:
                return Lists.<NotifyMessage>newArrayList(getErrorMessages());
            case FATAL:
                return Lists.<NotifyMessage>newArrayList(getFatalMessages());
            default:
                throw new UnsupportedOperationException("Unknown type " + type);
        }
    }

    private String getConditionForStyleClassOrEmpty(String styleClass) {
        return (styleClass.isEmpty() ? "" : "[contains(@class, '" + styleClass + "')]");
    }

    private List<RichFacesNotifyMessage> getErrorMessages() {
        List<WebElement> roots = driver.findElements(
                By.xpath("//div"
                + getConditionForStyleClassOrEmpty("rf-ntf")
                + getConditionForStyleClassOrEmpty("rf-ntf-err")
                + getConditionForStyleClassOrEmpty(styleClassToContain)));
        return createListOfFragments(roots);

    }

    private List<RichFacesNotifyMessage> getFatalMessages() {
        List<WebElement> roots = driver.findElements(
                By.xpath("//div"
                + getConditionForStyleClassOrEmpty("rf-ntf")
                + getConditionForStyleClassOrEmpty("rf-ntf-ftl")
                + getConditionForStyleClassOrEmpty(styleClassToContain)));
        return createListOfFragments(roots);

    }

    private List<RichFacesNotifyMessage> getInfoMessages() {
        List<WebElement> roots = driver.findElements(
                By.xpath("//div"
                + getConditionForStyleClassOrEmpty("rf-ntf")
                + getConditionForStyleClassOrEmpty("rf-ntf-inf")
                + getConditionForStyleClassOrEmpty(styleClassToContain)));
        return createListOfFragments(roots);
    }

    @Override
    public NotifyMessage getMessageAtIndex(int index) {
        return getMessages().get(index);
    }

    private List<RichFacesNotifyMessage> getMessages() {
        List<WebElement> roots = driver.findElements(
                By.xpath("//div"
                + getConditionForStyleClassOrEmpty("rf-ntf")
                + getConditionForStyleClassOrEmpty(styleClassToContain)));
        return createListOfFragments(roots);
    }

    private List<RichFacesNotifyMessage> createListOfFragments(List<WebElement> roots) {
        List<RichFacesNotifyMessage> list = Lists.newArrayList();
        for (WebElement e : roots) {
            list.add(Graphene.createPageFragment(RichFacesNotifyMessage.class, e));
        }
        return list;
    }

    @Override
    public WebElement getRoot() {
        return rootElement;
    }

    private List<RichFacesNotifyMessage> getWarnMessages() {
        List<WebElement> roots = driver.findElements(
                By.xpath("//div"
                + getConditionForStyleClassOrEmpty("rf-ntf")
                + getConditionForStyleClassOrEmpty("rf-ntf-wrn")
                + getConditionForStyleClassOrEmpty(styleClassToContain)));
        return createListOfFragments(roots);
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getMessages().isEmpty();
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
                return !getMessages().isEmpty();
            }
        };
    }

    @Override
    public Iterator<NotifyMessage> iterator() {
        return new NotifyMessagesIterator(getMessages().iterator());
    }

    public void setStyleClassToContain(String styleClassToContain) {
        this.styleClassToContain = styleClassToContain;
    }

    @Override
    public int size() {
        return getMessages().size();
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
