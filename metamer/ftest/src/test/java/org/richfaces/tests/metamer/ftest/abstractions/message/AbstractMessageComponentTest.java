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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.richfaces.tests.page.fragments.impl.message.Message;
import org.testng.Assert;

/**
 * Base for testing of message components (rich:message, rich:notifyMessage)
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessageComponentTest extends AbstractMessageComponentCommonTest {

    private final Action generateMessagesAction = new GenerateMessagesAction();

    public void checkAjaxRendered() {
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForFirstInput().isVisible());
        assertTrue(getMessageComponentForSecondInput().isVisible());
        assertTrue(getMessageComponentForSelectableInput().isVisible());
        AttributeList.messageAttributes.set(MessageAttributes.ajaxRendered, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertFalse(getMessageComponentForFirstInput().isVisible());
        assertFalse(getMessageComponentForSecondInput().isVisible());
        assertFalse(getMessageComponentForSelectableInput().isVisible());
        //submit with h:commandbutton
        MetamerPage.waitRequest(getPage().hCommandButton, WaitRequestType.HTTP).click();
        assertTrue(getMessageComponentForFirstInput().isVisible());
        assertTrue(getMessageComponentForSecondInput().isVisible());
        assertTrue(getMessageComponentForSelectableInput().isVisible());
    }

    public void checkNoShowDetailNoShowSummary() {
        AttributeList.messageAttributes.set(MessageAttributes.showSummary, Boolean.FALSE);
        AttributeList.messageAttributes.set(MessageAttributes.showDetail, Boolean.FALSE);

        generateValidationMessages();
        submitWithA4jBtn();

        assertFalse(getPage().getMessageComponentForFirstInput().isVisible());
        assertFalse(getPage().getMessageComponentForSecondInput().isVisible());
        assertFalse(getPage().getMessageComponentForSelectableInput().isVisible());
    }

    public void checkFor() {
        // firstly, remove value from attribute @for and generate message
        AttributeList.messageAttributes.setLower(MessageAttributes.FOR, "");
        generateValidationMessages();
        submitWithA4jBtn();
        assertFalse(getMessageComponentForSelectableInput().isVisible());
        // now set for attribute back to "simpleInput2"
        AttributeList.messageAttributes.setLower(MessageAttributes.FOR, "simpleInput2");
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForSelectableInput().isVisible());
    }

    public void checkRendered() {
        AttributeList.messageAttributes.set(MessageAttributes.rendered, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForFirstInput().isVisible());
        assertTrue(getMessageComponentForSecondInput().isVisible());
        assertTrue(getMessageComponentForSelectableInput().isVisible());
        AttributeList.messageAttributes.set(MessageAttributes.rendered, Boolean.FALSE);
        generateValidationMessages();
        submitWithA4jBtn();
        assertFalse(getMessageComponentForFirstInput().isVisible());
        assertFalse(getMessageComponentForSecondInput().isVisible());
        assertFalse(getMessageComponentForSelectableInput().isVisible());
    }

    public void checkShowDetail() {
        AttributeList.messageAttributes.set(MessageAttributes.showSummary, Boolean.TRUE);
        AttributeList.messageAttributes.set(MessageAttributes.showDetail, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForFirstInput().isDetailVisible());
        assertTrue(getMessageComponentForSecondInput().isDetailVisible());
        assertTrue(getMessageComponentForSelectableInput().isDetailVisible());
        AttributeList.messageAttributes.set(MessageAttributes.showDetail, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertFalse(getMessageComponentForFirstInput().isDetailVisible());
        assertFalse(getMessageComponentForSecondInput().isDetailVisible());
        assertFalse(getMessageComponentForSelectableInput().isDetailVisible());
    }

    public void checkShowSummary() {
        AttributeList.messageAttributes.set(MessageAttributes.showDetail, Boolean.TRUE);
        AttributeList.messageAttributes.set(MessageAttributes.showSummary, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForFirstInput().isSummaryVisible());
        assertTrue(getMessageComponentForSecondInput().isSummaryVisible());
        assertTrue(getMessageComponentForSelectableInput().isSummaryVisible());
        AttributeList.messageAttributes.set(MessageAttributes.showSummary, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertFalse(getMessageComponentForFirstInput().isSummaryVisible());
        assertFalse(getMessageComponentForSecondInput().isSummaryVisible());
        assertFalse(getMessageComponentForSelectableInput().isSummaryVisible());
    }

    public void checkSimple() {
        //generate messages
        generateValidationMessagesWithWait();
        Assert.assertTrue(getPage().getMessageComponentForFirstInput().isVisible());
        Assert.assertTrue(getPage().getMessageComponentForSecondInput().isVisible());
        Assert.assertTrue(getPage().getMessageComponentForSelectableInput().isVisible());
        //hide all messages
        setCorrectValuesWithWaiting();
        Assert.assertFalse(getPage().getMessageComponentForFirstInput().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSecondInput().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSelectableInput().isVisible());
        //type bad value to first input
        getPage().simpleInput1.clear(ClearType.JS).fillIn("bad value");
        submitWithA4jBtn();
        Assert.assertTrue(getPage().getMessageComponentForFirstInput().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSecondInput().isVisible());
        Assert.assertTrue(getPage().getMessageComponentForSelectableInput().isVisible());
        //hide all messages
        setCorrectValuesWithWaiting();
        //type bad value to second input
        getPage().simpleInput2.clear(ClearType.JS).fillIn("bad value");
        submitWithA4jBtn();
        Assert.assertFalse(getPage().getMessageComponentForFirstInput().isVisible());
        Assert.assertTrue(getPage().getMessageComponentForSecondInput().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSelectableInput().isVisible());
    }

    @Override
    protected Action getGenerateMessagesAction() {
        return generateMessagesAction;
    }

    private Message getMessageComponentForFirstInput() {
        return getPage().getMessageComponentForFirstInput();
    }

    private Message getMessageComponentForSecondInput() {
        return getPage().getMessageComponentForSecondInput();
    }

    private Message getMessageComponentForSelectableInput() {
        return getPage().getMessageComponentForSelectableInput();
    }

    @Override
    protected abstract MessageComponentTestPage getPage();

    @Override
    protected void waitingForValidationMessagesToHide() {
        submitWithHBtn();
        Graphene.waitGui().until(getPage().getMessageComponentForFirstInput().isNotVisibleCondition());
        Graphene.waitGui().until(getPage().getMessageComponentForSecondInput().isNotVisibleCondition());
        Graphene.waitGui().until(getPage().getMessageComponentForSelectableInput().isNotVisibleCondition());
    }

    protected class GenerateMessagesAction implements Action {

        @Override
        public void perform() {
            generateValidationMessagesWithWait();
        }
    }
}
