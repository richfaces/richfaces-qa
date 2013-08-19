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
package org.richfaces.tests.metamer.ftest.abstractions.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.richMessages.MessagesAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.validation.MultipleValidationRulesBean;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.message.Message;
import org.richfaces.tests.page.fragments.impl.message.Message.MessageType;
import org.richfaces.tests.page.fragments.impl.messages.MessagesBase;
import org.testng.Assert;

/**
 * Base for testing of message components (rich:messages, rich:notifyMessages)
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessagesComponentTest extends AbstractMessageComponentCommonTest {

    private final Action generateMessagesAction = new GenerateMessagesAction();

    public void checkAjaxRendered() {
        generateValidationMessagesWithWait();
        assertTrue(getPage().getMessagesComponentWithFor().isVisible());
        assertTrue(getPage().getMessagesComponentWithGlobal().isVisible());

        AttributeList.messagesAttributes.set(MessagesAttributes.ajaxRendered, Boolean.FALSE);
        generateValidationMessagesWithoutWait();

        assertFalse(getPage().getMessagesComponentWithFor().isVisible());
        assertFalse(getPage().getMessagesComponentWithGlobal().isVisible());

        //submit with h:commandbutton
        MetamerPage.waitRequest(getPage().hCommandButton, WaitRequestType.HTTP).click();
        assertTrue(getPage().getMessagesComponentWithFor().isVisible());
        assertTrue(getPage().getMessagesComponentWithGlobal().isVisible());
    }

    public abstract void checkFor(int expectedMessages);

    /**
     * globalOnly change behavior of displaying messages.
     * When <b>true</b> only messages not bound to any input are displayed
     *      <b>false</b> all messages are displayed
     * This attribute cannot be set with <i>for</i> attribute.
     *
     * In this case, messages component messagesGlobal is relevant.
     */
    public abstract void checkGlobalOnly(int expectedMessagesPerInput);

    public void checkMessagesTypes() {
        generateAllKindsOfMessagesWithWait();
        assertEquals(getPage().getMessagesComponentWithFor().size(), 4);
        getPage().getMessagesComponentWithFor().getMessageAtIndex(0).isType(MessageType.FATAL);
        getPage().getMessagesComponentWithFor().getMessageAtIndex(1).isType(MessageType.ERROR);
        getPage().getMessagesComponentWithFor().getMessageAtIndex(2).isType(MessageType.WARNING);
        getPage().getMessagesComponentWithFor().getMessageAtIndex(3).isType(MessageType.INFORMATION);
    }

    public void checkNoShowDetailNoShowSummary() {
        AttributeList.messagesAttributes.set(MessagesAttributes.showSummary, Boolean.FALSE);
        AttributeList.messagesAttributes.set(MessagesAttributes.showDetail, Boolean.FALSE);

        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();

        assertFalse(getPage().getMessagesComponentWithGlobal().isVisible());
        assertFalse(getPage().getMessagesComponentWithFor().isVisible());
    }

    public void checkRendered() {
        AttributeList.messagesAttributes.set(MessagesAttributes.rendered, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(getPage().getMessagesComponentWithGlobal().isVisible());
        assertTrue(getPage().getMessagesComponentWithFor().isVisible());

        AttributeList.messagesAttributes.set(MessagesAttributes.rendered, Boolean.FALSE);
        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();

        assertFalse(getPage().getMessagesComponentWithGlobal().isVisible());
        assertFalse(getPage().getMessagesComponentWithFor().isVisible());
    }

    public void checkShowDetail() {
        AttributeList.messagesAttributes.set(MessagesAttributes.showSummary, Boolean.TRUE);
        AttributeList.messagesAttributes.set(MessagesAttributes.showDetail, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(getPage().getMessagesComponentWithGlobal().getMessageAtIndex(0).isDetailVisible());
        assertTrue(getPage().getMessagesComponentWithFor().getMessageAtIndex(0).isDetailVisible());

        AttributeList.messagesAttributes.set(MessagesAttributes.showDetail, Boolean.FALSE);
        generateValidationMessagesWithWait();

        assertFalse(getPage().getMessagesComponentWithGlobal().getMessageAtIndex(0).isDetailVisible());
        assertFalse(getPage().getMessagesComponentWithFor().getMessageAtIndex(0).isDetailVisible());
    }

    public void checkShowSummary() {
        AttributeList.messagesAttributes.set(MessagesAttributes.showDetail, Boolean.TRUE);
        AttributeList.messagesAttributes.set(MessagesAttributes.showSummary, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(getPage().getMessagesComponentWithGlobal().getMessageAtIndex(0).isSummaryVisible());
        assertTrue(getPage().getMessagesComponentWithFor().getMessageAtIndex(0).isSummaryVisible());

        AttributeList.messagesAttributes.set(MessagesAttributes.showSummary, Boolean.FALSE);
        generateValidationMessagesWithWait();

        assertFalse(getPage().getMessagesComponentWithGlobal().getMessageAtIndex(0).isSummaryVisible());
        assertFalse(getPage().getMessagesComponentWithFor().getMessageAtIndex(0).isSummaryVisible());
    }

    public void checkSimple(int expectedNumberOfMessages) {
        String validationMessage1 = MultipleValidationRulesBean.VALIDATION_MSG_DIGITS;
        String validationMessage2 = MultipleValidationRulesBean.VALIDATION_MSG_MAX;
        String validationMessage3 = "Validation Error: Specified attribute is not between the expected values of 5 and 150.";
        String validationMessage4 = "Validation Error: Length is greater than allowable maximum of '2'";
        String[] validationMessages = { validationMessage1, validationMessage2, validationMessage3, validationMessage4 };
        //generate messages
        generateValidationMessagesWithWait();
        Assert.assertTrue(getPage().getMessagesComponentWithFor().isVisible());
        Assert.assertTrue(getPage().getMessagesComponentWithGlobal().isVisible());
        Assert.assertEquals(getPage().getMessagesComponentWithFor().size(), expectedNumberOfMessages, "Number of messages for message component with @for attribute.");
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().size(), expectedNumberOfMessages * 2, "Number of messages for message component with @globalOnly attribute.");
        MessagesBase<? extends Message> messagesComponentWithFor = getPage().getMessagesComponentWithFor();
        for (Message message : messagesComponentWithFor) {
            boolean wasFound = Boolean.FALSE;
            for (String s : validationMessages) {
                if (message.getSummary().contains(s)) {
                    wasFound = Boolean.TRUE;
                    break;
                }
            }
            Assert.assertTrue(wasFound, "Validation message text was not expected: '" + message.getSummary() + "'");
        }

        //hide all messages
        setCorrectValuesWithWaiting();
        Assert.assertFalse(getPage().getMessagesComponentWithFor().isVisible());
        Assert.assertFalse(getPage().getMessagesComponentWithGlobal().isVisible());
        //type bad value to first input
        getPage().simpleInput1.advanced().clear(ClearType.JS).sendKeys("bad value");
        submitWithHBtn();

        Assert.assertTrue(getPage().getMessagesComponentWithFor().isVisible());
        Assert.assertTrue(getPage().getMessagesComponentWithGlobal().isVisible());
        Assert.assertEquals(getPage().getMessagesComponentWithFor().size(), 1, "Number of messages for message component with @for attribute.");
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().size(), 1, "Number of messages for message component with @globalOnly attribute.");
        Assert.assertTrue(getPage().getMessagesComponentWithFor().getMessageAtIndex(0).getSummary().contains("'bad value' must be a number consisting of one or more digits."));
        Assert.assertTrue(getPage().getMessagesComponentWithGlobal().getMessageAtIndex(0).getSummary().contains("'bad value' must be a number consisting of one or more digits."));
        //hide all messages
        setCorrectValuesWithWaiting();
        //type bad value to second input
        getPage().simpleInput2.advanced().clear(ClearType.JS).sendKeys("bad value");
        submitWithHBtn();

        Assert.assertFalse(getPage().getMessagesComponentWithFor().isVisible());
        Assert.assertTrue(getPage().getMessagesComponentWithGlobal().isVisible());
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().size(), 1, "Number of messages for message component with @globalOnly attribute.");
        Assert.assertTrue(getPage().getMessagesComponentWithGlobal().getMessageAtIndex(0).getSummary().contains("'bad value' must be a number consisting of one or more digits."));
    }

    @Override
    protected Action getGenerateMessagesAction() {
        return generateMessagesAction;
    }

    protected void generateAllKindsOfMessagesWithWait() {
        setCorrectValues();
        submitWithA4jBtn();
        MetamerPage.waitRequest(getPage().generateAllMsgsButton, WaitRequestType.XHR).click();
        Graphene.waitAjax().until(getPage().getMessagesComponentWithGlobal().isVisibleCondition());
    }

    @Override
    public void generateValidationMessagesWithWait() {
        generateValidationMessagesWithoutWait();
        waitingForValidationMessagesToShow();
    }

    protected void generateValidationMessagesWithoutWait() {
        executeJS("window.valuesSettingState=''");
        getPage().wrongValuesButton.click();
        waitForValuesSetting();
    }

    private String getIDOfElement(WebElement element) {
        return element.getAttribute("id");
    }

    @Override
    protected abstract MessagesComponentTestPage<? extends Message> getPage();

    protected String getSimpleInput1ID() {
        return getIDOfElement(getPage().simpleInput1.advanced().getInput());
    }

    protected String getSimpleInput2ID() {
        return getIDOfElement(getPage().simpleInput2.advanced().getInput());
    }

    @Override
    protected void waitingForValidationMessagesToHide() {
        submitWithHBtn();
        Graphene.waitGui().until(getPage().getMessagesComponentWithFor().isNotVisibleCondition());
        Graphene.waitGui().until(getPage().getMessagesComponentWithGlobal().isNotVisibleCondition());
    }

    private class GenerateMessagesAction implements Action {

        @Override
        public void perform() {
            generateValidationMessagesWithWait();
        }
    }
}
