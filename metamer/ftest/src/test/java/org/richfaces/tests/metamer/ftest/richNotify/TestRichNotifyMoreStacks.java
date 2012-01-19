/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richNotify;

import static org.jboss.arquillian.ajocado.Ajocado.countEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;



/**
 * Test case for pages faces/components/notify/moreStacks.xhtml
 * 
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichNotifyMoreStacks extends AbstractRichNotifyTest {
    
    /**
     * locator of the first stack 
     */
    private final JQueryLocator stack1 = jq("div.tr");
    
    /**
     * locator of the second stack
     */
    private final JQueryLocator stack2 = jq("div.br");
    
    /**
     * locator of the input which is associated with messages for the first stack
     */
    private final JQueryLocator number1 = pjq("input[id$=number1]");
    
    /**
     * locator of the input which is associated with messages for the second stack
     */
    private final JQueryLocator number2 = pjq("input[id$=number2]");
    
    /**
     * locator of the h:commnadButton submit 
     */
    private final JQueryLocator submitHCommandButton = pjq("input[id$=hCommandButtonSubmit]");
    
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richNotify/moreStacks.xhtml");
    }    
    
    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(inStack(stack1, notify)), "The notify in the first stack is not present.");
        assertTrue(selenium.isElementPresent(inStack(stack2, notify)), "The notify in the second stack is not present.");
        assertEquals(1, getNumberOfNotifies(stack1), "The initial number of notifies in the first stack should be <1>, found <" + getNumberOfNotifies(stack1) + ">.");
        assertEquals(1, getNumberOfNotifies(stack2), "The initial number of notifies in the second stack should be <1>, found <" + getNumberOfNotifies(stack2) + ">.");
    }
    
    @Test
    public void testValidationErrorInTwoStacks() {
        // notify in stacks
        JQueryLocator message1 = inStack(stack1, notifyError);
        JQueryLocator message2 = inStack(stack2, notifyError);
        // try to fill the number 1 field (wrong)
        selenium.type(number1, "1");
        selenium.type(number2, "5");
        selenium.click(submitHCommandButton);
        selenium.waitForPageToLoad();
        waitGui
            .failWith("After wrong filling the first form field the number of messages in the first stack should be <1>, found <" + selenium.getCount(message1) + ">")
            .until(countEquals.locator(message1).count(1));
        waitGui
            .failWith("After wrong filling the first form field the number of messages in the second stack should be <0>, found <" + selenium.getCount(message2) + ">")
            .until(countEquals.locator(message2).count(0));
        close(message1);
        // try to fill the number 2 field (wrong)
        selenium.type(number1, "10");
        selenium.type(number2, "1");
        selenium.click(submitHCommandButton);
        selenium.waitForPageToLoad();
        waitGui
            .failWith("After wrong filling the second form field the number of messages in the second stack should be <1>, found <" + selenium.getCount(message2) + ">")
            .until(countEquals.locator(message2).count(1));
        waitGui
            .failWith("After wrong filling the second form field the number of messages in the first stack should be <0>, found <" + selenium.getCount(message1) + ">")
            .until(countEquals.locator(message1).count(0));        
    }
    
    /**
     * It returns a number of notifes (only notifies, not messages) in the given stack
     * 
     * @param stack the stack where the notifies should be present
     * @return number of notifies
     */
    private int getNumberOfNotifies(JQueryLocator stack) {
        return selenium.getCount(inStack(stack, notify)) - (selenium.getCount(inStack(stack, notifyError)) + selenium.getCount(inStack(stack, notifyFatal)) + selenium.getCount(inStack(stack, notifyInfo)) + selenium.getCount(inStack(stack, notifyWarn)));
    }
    
    /**
     * It returns a new locator of the given message in the given stack
     * @param stack
     * @param message
     * @return a new locator of the given message in the given stack
     */
    private JQueryLocator inStack(JQueryLocator stack, JQueryLocator message) {
        String[] splittedMessageLocator = message.getRawLocator().split("\\.");
        String[] splittedStackLocator = stack.getRawLocator().split("\\.");
        String locator = "div[class*=" + splittedStackLocator[splittedStackLocator.length - 1] + "][class*=" + splittedMessageLocator[splittedMessageLocator.length - 1] + "]";
        return jq(locator);
    }
    
}
