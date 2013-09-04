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
package org.richfaces.tests.showcase.messages;

import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.page.fragments.impl.message.Message;
import org.richfaces.tests.page.fragments.impl.message.Message.MessageType;
import org.richfaces.tests.page.fragments.impl.messages.MessageItem;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.messages.page.MessagesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestMessages extends AbstractWebDriverTest {

    @Page
    private MessagesPage page;

    @Test
    public void testCorrectValues() {
        page.fillCorrectValues();
        page.validate();
        Assert.assertTrue(page.getMessages().getItems(MessageType.ERROR).isEmpty(), "No message should be present.");
    }

    @Test
    public void testLessThanMinimum() {
        page.fillShorterValues();
        page.validate();

        Assert.assertEquals(page.getMessages().getItems(MessageType.ERROR).size(), 4, "4 messages should be present.");

        assertErrorIsPresent("Name", MessagesPage.NAME_ERROR_LESS_THAN_MINIMUM);
        assertErrorIsPresent("Job", MessagesPage.JOB_ERROR_LESS_THAN_MINIMUM);
        assertErrorIsPresent("Zip", MessagesPage.ZIP_ERROR_LESS_THAN_MINIMUM);
        assertErrorIsPresent("Address", MessagesPage.ADDRESS_ERROR_LESS_THAN_MINIMUM);
    }

    @Test
    public void testEmptyInputs() {
        page.eraseAll();
        page.validate();

        Assert.assertEquals(page.getMessages().getItems(MessageType.ERROR).size(), 4, "4 messages should be present.");

        assertErrorIsPresent("Name", MessagesPage.NAME_ERROR_VALUE_REQUIRED);
        assertErrorIsPresent("Job", MessagesPage.JOB_ERROR__VALUE_REQUIRED);
        assertErrorIsPresent("Zip", MessagesPage.ZIP_ERROR__VALUE_REQUIRED);
        assertErrorIsPresent("Address", MessagesPage.ADDRESS_ERROR__VALUE_REQUIRED);
    }

    @Test
    public void testGreaterThanMaximum() {
        page.fillCorrectValues();
        page.validate();

        Assert.assertTrue(page.getMessages().getItems(MessageType.ERROR).isEmpty(), "No message should be present.");

        page.fillLongerJob();
        page.validate();
        assertErrorIsPresent("Job", MessagesPage.JOB_ERROR_GREATER_THAN_MAXIMUM);

        page.fillLongerZip();
        page.validate();
        assertErrorIsPresent("Zip", MessagesPage.ZIP_ERROR_GREATER_THAN_MAXIMUM);
    }

    public void assertErrorIsPresent(String fieldName, String expected) {
        List<? extends MessageItem> messages = page.getMessages().getItems(MessageType.ERROR);
        for (Message message : messages) {
            if (message.getSummary().contains(expected)) {
                return;
            }
        }
        Assert.fail("There is no message with summary '" + expected + "' for field " + fieldName + ".");
    }
}
