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
package org.richfaces.tests.page.fragments.impl.message;

import com.google.common.base.Predicate;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;

/**
 * Abstract base for message component.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessage implements Message {

    @Root
    private WebElement root;
    private final AdvancedMessageInteractions interactions = new AdvancedMessageInteractionsImpl();

    @Override
    public AdvancedMessageInteractions advanced() {
        return interactions;
    }

    protected abstract String getCssClass(MessageType type);

    @Override
    public String getDetail() {
        return getMessageDetailElement().getText();
    }

    protected abstract WebElement getMessageDetailElement();

    protected abstract WebElement getMessageSummaryElement();

    protected GrapheneElement getRoot() {
        return new GrapheneElement(root);
    }

    @Override
    public String getSummary() {
        return getMessageSummaryElement().getText();
    }

    @Override
    public MessageType getType() {
        String attribute = getRoot().getAttribute("class");
        for (MessageType type : MessageType.values()) {
            if (attribute.contains(getCssClass(type))) {
                return type;
            }
        }
        return null;
    }

    public class AdvancedMessageInteractionsImpl implements AdvancedMessageInteractions {

        @Override
        public WebElement getDetailElement() {
            return getMessageDetailElement();
        }

        @Override
        public WebElement getRootElement() {
            return root;
        }

        @Override
        public WebElement getSummaryElement() {
            return getMessageSummaryElement();
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getSummaryElement()) || Utils.isVisible(getDetailElement());
        }

        @Override
        public void waitUntilIsNotVisible() {
            Graphene.waitModel().until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return !isVisible();
                }
            });
        }

        @Override
        public void waitUntilIsVisible() {
            Graphene.waitModel().until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return isVisible();
                }
            });
        }
    }
}
