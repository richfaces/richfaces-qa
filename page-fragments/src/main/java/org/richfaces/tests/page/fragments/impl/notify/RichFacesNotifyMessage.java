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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.message.AbstractMessage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesNotifyMessage extends AbstractMessage implements NotifyMessage {

    @FindBy(className = "rf-ntf-det")
    private WebElement messageDetailElement;
    @FindBy(className = "rf-ntf-sum")
    private WebElement messageSummaryElement;
    @FindBy(className = "rf-ntf-cls")
    private WebElement closeElement;
    @FindBy(className = "rf-ntf-cls-ico")
    private WebElement closeIconElement;
    @FindBy(className = "rf-ntf-shdw")
    private WebElement shadowElement;

    @Override
    public void close() {
        new Actions(driver).moveToElement(root).perform();
        Graphene.waitGui().withTimeout(3, TimeUnit.SECONDS).until().element(closeIconElement).is().visible();
        final List<WebElement> messages = driver.findElements(By.xpath("//div[contains(@class, 'rf-ntf-cnt')]"));
        final int sizeBefore = messages.size();
        new Actions(driver).click(closeIconElement).perform();
        Graphene.waitGui().withTimeout(3, TimeUnit.SECONDS).withMessage("The message did not disappear.").until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return messages.size() == (sizeBefore - 1);
            }
        });
    }

    @Override
    public WebElement getCloseElement() {
        return closeElement;
    }

    @Override
    public WebElement getCloseIconElement() {
        return closeIconElement;
    }

    @Override
    protected String getCssClass(MessageType type) {
        switch (type) {
            case ERROR:
                return "rf-ntf-err";
            case FATAL:
                return "rf-ntf-ftl";
            case INFORMATION:
                return "rf-ntf-inf";
            case OK:
                return "rf-ntf-ok";
            case WARNING:
                return "rf-ntf-wrn";
            default:
                throw new UnsupportedOperationException("Unknown message type " + type);
        }
    }

    @Override
    public WebElement getMessageDetailElement() {
        return messageDetailElement;
    }

    @Override
    public WebElement getMessageSummaryElement() {
        return messageSummaryElement;
    }

    @Override
    public NotifyMessagePosition getPosition() {
        return RichFacesNotifyMessagePosition.getPositionFromElement(root);
    }

    @Override
    public WebElement getShadowElement() {
        return shadowElement;
    }
}
