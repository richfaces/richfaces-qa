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
package org.richfaces.tests.page.fragments.ftest.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.ftest.AbstractTest;
import org.richfaces.tests.page.fragments.impl.message.MessageComponentImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestMessageComponent extends AbstractTest {

    //components
    @FindBy(css = "span[id$=msgComplete]")
    private MessageComponentImpl completeMessage;
    @FindBy(css = "span[id$=msgDetail]")
    private MessageComponentImpl detailMessage;
    @FindBy(css = "span[id$=msgSummary]")
    private MessageComponentImpl summaryMessage;
    //inputs/buttons
    @FindBy(css = "input[id$=input]")
    private WebElement input;
    @FindBy(css = "input[id$=submit]")
    private WebElement submitBtn;
    //others
    private WebDriver driver = GrapheneContext.getProxy();
    private static final String STRING_FOR_CREATING_MSGS = "1234";

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return createDeployment(TestMessageComponent.class).addClass(ValueBean.class);
    }

    @BeforeMethod(alwaysRun=true)
    private void generateMessages() {
        input.clear();
        input.sendKeys(STRING_FOR_CREATING_MSGS);
        Graphene.guardXhr(submitBtn).click();
        Graphene.waitAjax().until(completeMessage.isVisibleCondition());
    }

    @Test
    public void testMessageDetail() {
        String messageDetail = completeMessage.getDetail();
        assertFalse(messageDetail.isEmpty());
        assertEquals(detailMessage.getDetail(), messageDetail);
    }

    @Test
    public void testMessageSummary() {
        String messageSummary = completeMessage.getSummary();
        assertFalse(messageSummary.isEmpty());
        assertEquals(summaryMessage.getSummary(), messageSummary);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testNotExistingMessageDetail_throwsRuntimeException() {
        summaryMessage.getDetail();
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testNotExistingMessageSummary_throwsRuntimeException() {
        detailMessage.getSummary();
    }

    @Test
    public void testShow() {
        assertTrue(completeMessage.isVisibleCondition().apply(driver));
        assertFalse(completeMessage.isNotVisibleCondition().apply(driver));
        assertTrue(completeMessage.isVisible());
        assertTrue(completeMessage.isDetailVisibleCondition().apply(driver));
        assertFalse(completeMessage.isDetailNotVisibleCondition().apply(driver));
        assertTrue(completeMessage.isDetailVisible());
        assertTrue(completeMessage.isSummaryVisibleCondition().apply(driver));
        assertFalse(completeMessage.isSummaryNotVisibleCondition().apply(driver));
        assertTrue(completeMessage.isSummaryVisible());

        assertTrue(detailMessage.isVisibleCondition().apply(driver));
        assertFalse(detailMessage.isNotVisibleCondition().apply(driver));
        assertTrue(completeMessage.isVisible());
        assertTrue(detailMessage.isDetailVisibleCondition().apply(driver));
        assertFalse(detailMessage.isDetailNotVisibleCondition().apply(driver));
        assertTrue(detailMessage.isDetailVisible());
        assertTrue(detailMessage.isSummaryNotVisibleCondition().apply(driver));
        assertFalse(detailMessage.isSummaryVisibleCondition().apply(driver));
        assertFalse(detailMessage.isSummaryVisible());

        assertTrue(summaryMessage.isVisibleCondition().apply(driver));
        assertFalse(summaryMessage.isNotVisibleCondition().apply(driver));
        assertTrue(completeMessage.isVisible());
        assertTrue(summaryMessage.isDetailNotVisibleCondition().apply(driver));
        assertFalse(summaryMessage.isDetailVisibleCondition().apply(driver));
        assertFalse(summaryMessage.isDetailVisible());
        assertTrue(summaryMessage.isSummaryVisibleCondition().apply(driver));
        assertFalse(summaryMessage.isSummaryNotVisibleCondition().apply(driver));
        assertTrue(summaryMessage.isSummaryVisible());
    }
}
