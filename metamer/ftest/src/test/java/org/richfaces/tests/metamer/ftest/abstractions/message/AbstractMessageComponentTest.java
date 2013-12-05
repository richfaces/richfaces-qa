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
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.message.Message;

/**
 * Base for testing of message components (rich:message, rich:notifyMessage)
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessageComponentTest extends AbstractMessageComponentCommonTest {

    private final Attributes<MessageAttributes> messageAttributes = getAttributes();

    private final Action generateMessagesAction = new GenerateMessagesAction();

    public void checkAjaxRendered() {
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForFirstInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSecondInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should be visible.");

        messageAttributes.set(MessageAttributes.ajaxRendered, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertFalse(getMessageComponentForFirstInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSecondInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should not be visible.");
        //submit with h:commandbutton
        MetamerPage.waitRequest(getPage().gethCommandButton(), WaitRequestType.HTTP).click();
        assertTrue(getMessageComponentForFirstInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSecondInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should be visible.");
    }

    public void checkNoShowDetailNoShowSummary() {
        messageAttributes.set(MessageAttributes.showSummary, Boolean.FALSE);
        messageAttributes.set(MessageAttributes.showDetail, Boolean.FALSE);

        generateValidationMessages();
        submitWithA4jBtn();

        assertFalse(getMessageComponentForFirstInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSecondInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should not be visible.");
    }

    public void checkFor() {
        // firstly, remove value from attribute @for and generate message
        messageAttributes.set(MessageAttributes.FOR, "");
        generateValidationMessages();
        submitWithA4jBtn();
        assertFalse(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should not be visible.");

        // now set for attribute back to "simpleInput2"
        messageAttributes.set(MessageAttributes.FOR, "simpleInput2");
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should be visible.");
    }

    public void checkRendered() {
        messageAttributes.set(MessageAttributes.rendered, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForFirstInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSecondInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should be visible.");
        messageAttributes.set(MessageAttributes.rendered, Boolean.FALSE);
        generateValidationMessages();
        submitWithA4jBtn();
        assertFalse(getMessageComponentForFirstInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSecondInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should not be visible.");
    }

    public void checkShowDetail() {
        messageAttributes.set(MessageAttributes.showSummary, Boolean.TRUE);
        messageAttributes.set(MessageAttributes.showDetail, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertVisible(getMessageComponentForFirstInput().advanced().getDetailElement(), "Message should be visible.");
        assertVisible(getMessageComponentForSecondInput().advanced().getDetailElement(), "Message should be visible.");
        assertVisible(getMessageComponentForSelectableInput().advanced().getDetailElement(), "Message should be visible.");
        messageAttributes.set(MessageAttributes.showDetail, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertNotVisible(getMessageComponentForFirstInput().advanced().getDetailElement(), "Message should not be visible.");
        assertNotVisible(getMessageComponentForSecondInput().advanced().getDetailElement(), "Message should not be visible.");
        assertNotVisible(getMessageComponentForSelectableInput().advanced().getDetailElement(), "Message should not be visible.");
    }

    public void checkShowSummary() {
        messageAttributes.set(MessageAttributes.showDetail, Boolean.TRUE);
        messageAttributes.set(MessageAttributes.showSummary, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertVisible(getMessageComponentForFirstInput().advanced().getSummaryElement(), "Message should be visible.");
        assertVisible(getMessageComponentForSecondInput().advanced().getSummaryElement(), "Message should be visible.");
        assertVisible(getMessageComponentForSelectableInput().advanced().getSummaryElement(), "Message should be visible.");
        messageAttributes.set(MessageAttributes.showSummary, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertNotVisible(getMessageComponentForFirstInput().advanced().getSummaryElement(), "Message should not be visible.");
        assertNotVisible(getMessageComponentForSecondInput().advanced().getSummaryElement(), "Message should not be visible.");
        assertNotVisible(getMessageComponentForSelectableInput().advanced().getSummaryElement(), "Message should not be visible.");
    }

    public void checkSimple() {
        //generate messages
        generateValidationMessagesWithWait();
        assertTrue(getMessageComponentForFirstInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSecondInput().advanced().isVisible(), "Message should be visible.");
        assertTrue(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should be visible.");
        //hide all messages
        setCorrectValuesWithWaiting();
        assertFalse(getMessageComponentForFirstInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSecondInput().advanced().isVisible(), "Message should not be visible.");
        assertFalse(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should not be visible.");
        //type bad value to first input
        getPage().getSimpleInput1().clear().sendKeys("bad value");
        submitWithA4jBtn();
        assertTrue(getMessageComponentForFirstInput().advanced().isVisible(), "Message should be visible.");
        assertFalse(getMessageComponentForSecondInput().advanced().isVisible(), "Message should not be visible.");
        assertTrue(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should be visible.");
        //hide all messages
        setCorrectValuesWithWaiting();
        //type bad value to second input
        getPage().getSimpleInput2().advanced().clear(ClearType.JS).sendKeys("bad value");
        submitWithA4jBtn();
        assertFalse(getMessageComponentForFirstInput().advanced().isVisible(), "Message should not be visible.");
        assertTrue(getMessageComponentForSecondInput().advanced().isVisible(), "Message should be visible.");
        assertFalse(getMessageComponentForSelectableInput().advanced().isVisible(), "Message should not be visible.");
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
        getMessageComponentForFirstInput().advanced().waitUntilMessageIsNotVisible().perform();
        getMessageComponentForSecondInput().advanced().waitUntilMessageIsNotVisible().perform();
        getMessageComponentForSelectableInput().advanced().waitUntilMessageIsNotVisible().perform();
    }

    protected class GenerateMessagesAction implements Action {

        @Override
        public void perform() {
            generateValidationMessagesWithWait();
        }
    }
}
