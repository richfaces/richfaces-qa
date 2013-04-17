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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Abstract base for message component.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessage implements Message {

    @Root
    protected WebElement root;

    @Drone
    protected WebDriver driver;

    protected abstract String getCssClass(MessageType type);

    @Override
    public String getDetail() {
        if (isDetailNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("The message detail is not visible");
        }
        return getMessageDetailElement().getText();
    }

    @Override
    public WebElement getRoot() {
        return root;
    }

    @Override
    public String getSummary() {
        if (isSummaryNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("The message summary is not visible");
        }
        return getMessageSummaryElement().getText();
    }

    @Override
    public ExpectedCondition<Boolean> isDetailNotVisibleCondition() {
        return Graphene.element(getMessageDetailElement()).not().isVisible();
    }

    @Override
    public boolean isDetailVisible() {
        return isDetailVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isDetailVisibleCondition() {
        return Graphene.element(getMessageDetailElement()).isVisible();
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public ExpectedCondition<Boolean> isSummaryNotVisibleCondition() {
        return Graphene.element(getMessageSummaryElement()).not().isVisible();
    }

    @Override
    public boolean isSummaryVisible() {
        return isSummaryVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isSummaryVisibleCondition() {
        return Graphene.element(getMessageSummaryElement()).isVisible();
    }

    @Override
    public boolean isType(MessageType type) {
        return getRoot().getAttribute("class").contains(getCssClass(type));
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }
}
