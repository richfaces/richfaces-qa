/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.messages;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.message.Message;

/**
 * Component for rich:messages.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesMessages implements Iterable<Message> {

    @Root
    private WebElement root;
    //
    @FindBy(xpath= "./span")
    private List<RichFacesMessagesMessage> messages;
    //
    private WebDriver driver = GrapheneContext.getProxy();

    public Message getMessage(int index) {
        return messages.get(index);
    }

    public List<Message> getMessagesForInput(String inputID) {
        List<Message> result = Lists.newArrayList();
        for (Message component : this) {
            if (component.getRoot().getAttribute("id").contains(inputID)) {
                result.add(component);
            }
        }
        return result;
    }

    public WebElement getRoot() {
        return root;
    }

    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public Iterator<Message> iterator() {
        final Iterator<RichFacesMessagesMessage> iterator = messages.iterator();
        return new Iterator<Message>() {
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
                iterator.remove();
            }
        };
    }

    public int size() {
        return messages.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("rootID=").append(getRoot().getAttribute("id")).append(";\n");
        for (Message component : this) {
            sb.append("element with id=[").append(component.getRoot().getAttribute("id")).append("], visible=").append(component.isVisible()).append(";\n");
        }
        return sb.toString();
    }
}
