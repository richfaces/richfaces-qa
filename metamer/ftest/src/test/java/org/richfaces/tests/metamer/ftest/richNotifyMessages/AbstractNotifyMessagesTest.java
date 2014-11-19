/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richNotifyMessages;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.abstractions.message.AbstractMessagesComponentTest;
import org.richfaces.tests.metamer.ftest.richMessages.MessagesAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.Assert;

/**
 * Common test case for rich:messages component
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractNotifyMessagesTest extends AbstractMessagesComponentTest {

    private final Attributes<MessagesAttributes> messagesAttributes = getAttributes();

    @Page
    private NotifyMessagesPage page;

    @Override
    public void checkFor(int expectedMessages) {
        messagesAttributes.set(MessagesAttributes.FOR, "");
        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();

        Assert.assertFalse(getPage().getMessagesComponentWithFor().advanced().isVisible());

        // now set for attribute to "simpleInput1"
        messagesAttributes.set(MessagesAttributes.FOR, "simpleInput1");
        generateValidationMessagesWithWait();

        Assert.assertTrue(getPage().getMessagesComponentWithFor().advanced().isVisible());
        Assert.assertEquals(getPage().getMessagesComponentWithFor().size(), expectedMessages);

        // now set for attribute back to "simpleInput2"
        messagesAttributes.set(MessagesAttributes.FOR, "simpleInput2");
        generateValidationMessagesWithWait();

        Assert.assertTrue(getPage().getMessagesComponentWithFor().advanced().isVisible());
        Assert.assertEquals(getPage().getMessagesComponentWithFor().size(), expectedMessages);
    }

    @Override
    public void checkGlobalOnly(int expectedMessagesPerInput) {
        messagesAttributes.set(MessagesAttributes.globalOnly, Boolean.FALSE);
        generateValidationMessagesWithWait();
        //messages for both inputs should appear
        Assert.assertTrue(getPage().getMessagesComponentWithGlobal().advanced().isVisible());
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().size(), expectedMessagesPerInput * 2);

        messagesAttributes.set(MessagesAttributes.globalOnly, Boolean.TRUE);
        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();
        //no messages should appear, because validation messages are bound to inputs not to 'null'
        Assert.assertFalse(getPage().getMessagesComponentWithGlobal().advanced().isVisible());
    }

    @Override
    protected NotifyMessagesPage getPage() {
        return page;
    }

    @Override
    protected FutureTarget<WebElement> getTestedElementRoot() {
        return new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement();
            }
        };
    }
}
