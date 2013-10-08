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
package org.richfaces.tests.metamer.ftest.richMessages;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.abstractions.message.AbstractMessagesComponentTest;
import org.richfaces.tests.metamer.ftest.abstractions.message.MessagesComponentTestPage;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.richfaces.tests.page.fragments.impl.message.Message;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.testng.Assert;

/**
 * Common test case for rich:messages component
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessagesTest extends AbstractMessagesComponentTest {

    @Page
    protected MessagesPage page;

    @Override
    protected MessagesComponentTestPage getPage() {
        return page;
    }

    @Override
    public void checkFor(int expectedMessages) {
        AttributeList.messagesAttributes.set(MessagesAttributes.FOR, "");
        generateValidationMessagesWithoutWait();
        submitWithHBtn();

        Assert.assertFalse(page.getMessagesComponentWithFor().advanced().isVisible());

        // now set @for attribute to "simpleInput1"
        AttributeList.messagesAttributes.set(MessagesAttributes.FOR, "simpleInput1");
        generateValidationMessagesWithoutWait();
        submitWithHBtn();

        Assert.assertTrue(page.getMessagesComponentWithFor().advanced().isVisible());
        Assert.assertEquals(page.getMessagesComponentWithFor().size(), expectedMessages);
        Assert.assertEquals(page.getMessagesComponentWithFor().getItems(ChoicePickerHelper.byWebElement().attribute("id").contains(getSimpleInput1ID())).size(), expectedMessages, expectedMessages + " messages for input 1 were expected.");
        Assert.assertEquals(page.getMessagesComponentWithFor().getItems(ChoicePickerHelper.byWebElement().attribute("id").contains(getSimpleInput2ID())).size(), 0, "No messages for input 2 were expected.");

        // now set @for attribute to "simpleInput2"
        AttributeList.messagesAttributes.set(MessagesAttributes.FOR, "simpleInput2");
        generateValidationMessagesWithoutWait();
        submitWithHBtn();

        Assert.assertTrue(page.getMessagesComponentWithFor().advanced().isVisible());
        Assert.assertEquals(page.getMessagesComponentWithFor().size(), expectedMessages);
        Assert.assertEquals(page.getMessagesComponentWithFor().getItems(ChoicePickerHelper.byWebElement().attribute("id").contains(getSimpleInput1ID())).size(), 0, "No messages for input 1 were expected.");
        Assert.assertEquals(page.getMessagesComponentWithFor().getItems(ChoicePickerHelper.byWebElement().attribute("id").contains(getSimpleInput2ID())).size(), expectedMessages, expectedMessages + " messages for input 2 were expected.");
    }

    @Override
    public void checkGlobalOnly(int expectedMessagesPerInput) {
        AttributeList.messagesAttributes.set(MessagesAttributes.globalOnly, Boolean.FALSE);
        generateValidationMessagesWithWait();
        //messages for both inputs should appear
        Assert.assertTrue(page.getMessagesComponentWithGlobal().advanced().isVisible());
        Assert.assertEquals(page.getMessagesComponentWithGlobal().size(), expectedMessagesPerInput * 2);//for both inputs
        Assert.assertEquals(page.getMessagesComponentWithGlobal().getItems(ChoicePickerHelper.byWebElement().attribute("id").contains(getSimpleInput1ID())).size(), expectedMessagesPerInput, expectedMessagesPerInput + " messages for input 1 were expected.");
        Assert.assertEquals(page.getMessagesComponentWithGlobal().getItems(ChoicePickerHelper.byWebElement().attribute("id").contains(getSimpleInput2ID())).size(), expectedMessagesPerInput, expectedMessagesPerInput + " messages for input 2 were expected.");

        AttributeList.messagesAttributes.set(MessagesAttributes.globalOnly, Boolean.TRUE);
        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();
        //no messages should appear, because validation messages are bound to inputs not to 'null'
        Assert.assertFalse(page.getMessagesComponentWithGlobal().advanced().isVisible());
    }

    @Override
    protected FutureTarget<WebElement> getTestedElementRoot() {
        return new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return page.getMessagesComponentWithGlobal().getRoot();
            }
        };
    }
}
