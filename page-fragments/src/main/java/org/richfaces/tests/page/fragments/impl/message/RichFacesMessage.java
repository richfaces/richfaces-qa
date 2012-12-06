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
package org.richfaces.tests.page.fragments.impl.message;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Component for rich: message.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesMessage implements Message {

    @Root
    private WebElement root;
    @FindBy(className = "rf-msg-dtl")
    private WebElement messageDetailElement;
    //Workaround for RF-12514, delete after issue is fixed
    @FindBy(className = "rf-msg-det")
    private WebElement messageDetailRF12514;
    @FindBy(className = "rf-msg-sum")
    private WebElement messageSummaryElement;
    private WebDriver driver = GrapheneContext.getProxy();

    //delete this after RF-12514 is fixed
    private ExpectedCondition<Boolean> _isDetailNotVisibleCondition() {
        return Graphene.element(messageDetailElement).not().isVisible();
    }

    //delete this after RF-12514 is fixed
    private ExpectedCondition<Boolean> _isDetailRF12514NotVisibleCondition() {
        return Graphene.element(messageDetailRF12514).not().isVisible();
    }

    @Override
    public String getDetail() {
        ///Workaround for RF-12514
        if (_isDetailNotVisibleCondition().apply(driver)) {
            if (_isDetailRF12514NotVisibleCondition().apply(driver)) {
                throw new RuntimeException("The message detail is not visible");
            }
            return messageDetailRF12514.getText();
        }
        return messageDetailElement.getText();
        //delete block before and uncomment this after RF-12514 is fixed
//        if (isDetailNotVisibleCondition().apply(driver)) {
//            throw new RuntimeException("The message detail is not visible");
//        }
//        return messageDetail.getText();
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
    public WebElement getRoot() {
        return root;
    }

    @Override
    public String getSummary() {
        if (isSummaryNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("The message summary is not visible");
        }
        return messageSummaryElement.getText();
    }

    @Override
    public ExpectedCondition<Boolean> isDetailNotVisibleCondition() {
        //Workaround for RF-12514
        ExpectedCondition<Boolean> condition1, condition2;
        condition1 = _isDetailNotVisibleCondition();
        if (condition1.apply(driver)) {//if rf-msg-dtl is not visible
            condition2 = _isDetailRF12514NotVisibleCondition();
            if (!condition2.apply(driver)) {//check if rf-msg-det is visible
                return condition2;
            }
        }
        return condition1;
        //delete block before and uncomment this after RF-12514 is fixed
        //return Graphene.element(messageDetail).not().isVisible();
    }

    @Override
    public boolean isDetailVisible() {
        return isDetailVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isDetailVisibleCondition() {
        //Workaround for RF-12514
        ExpectedCondition<Boolean> condition1, condition2;
        condition1 = Graphene.element(messageDetailElement).isVisible();
        if (!condition1.apply(driver)) {//if rf-msg-dtl is not visible
            condition2 = Graphene.element(messageDetailRF12514).isVisible();
            if (condition2.apply(driver)) {//check if rf-msg-det is visible
                return condition2;
            }
        }
        return condition1;
        //delete block before and uncomment this after RF-12514 is fixed
        //return Graphene.element(messageDetail).isVisible();
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public ExpectedCondition<Boolean> isSummaryNotVisibleCondition() {
        return Graphene.element(messageSummaryElement).not().isVisible();
    }

    @Override
    public boolean isSummaryVisible() {
        return isSummaryVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isSummaryVisibleCondition() {
        return Graphene.element(messageSummaryElement).isVisible();
    }

    @Override
    public boolean isType(MessageType type) {
        return this.getRoot().getAttribute("class").contains(getCssClass(type));
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    protected String getCssClass(MessageType type) {
        switch(type) {
            case ERROR:
                return "rf-msg-err";
            case FATAL:
                return "rf-msg-ftl";
            case INFORMATION:
                return "rf-msg-inf";
            case OK:
                return "rf-msg-ok";
            case WARNING:
                return "rf-msg-wrn";
            default:
                throw new UnsupportedOperationException();
        }
    }
}
