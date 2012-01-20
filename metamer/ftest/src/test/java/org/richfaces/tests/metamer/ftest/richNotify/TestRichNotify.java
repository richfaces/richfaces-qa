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
import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.jboss.test.selenium.waiting.NegationCondition;
import org.richfaces.tests.metamer.bean.rich.RichNotifyBean;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Test case for pages faces/components/notify/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichNotify extends AbstractRichNotifyTest {

    /**
     * notify stack locator
     */
    private JQueryLocator notifyStack = jq("div.rf-ntf");

    /**
     * Map containing locators of all tested messages. Type of the message is a key in the map.
     */
    private Map<String, JQueryLocator> messages = new HashMap<String, JQueryLocator>();

    @BeforeClass
    public void init() {
        messages.put("Error", notifyError);
        messages.put("Fatal", notifyFatal);
//        messages.put("Info", notifyInfo);
        messages.put("Warn", notifyWarn);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richNotify/simple.xhtml");
    }

    @Test
    public void testAttributeDelay() {
        // set the delay to <2000>
        attributesNotify.set(NotifyAttributes.delay, 2000);
        // wait for <1000>
        delay(1000);
        assertFalse(selenium.isElementPresent(notify), "The delay is set to 1000 and after some little waiting the notify shouldn't be present.");
        // wait for <2000>
        delay(2000);
        assertTrue(selenium.isElementPresent(notify), "The delay is set to 1000 and after some waiting the notify should be present.");
    }

    @Test
    public void testAttributeRendered() {
        // set the rendered to <false>
        attributesNotify.set(NotifyAttributes.rendered, false);
        assertFalse(selenium.isElementPresent(notify), "The attribute rendered is set to <false> but the notify is still present.");
    }

    @Test
    public void testAttributeShowCloseButton() {
        // set the showCloseButton to <false>
        attributesNotify.set(NotifyAttributes.showCloseButton, false);
        assertTrue(selenium.getStyle(getCloseButton(notify), new CssProperty("visibility")).contains("hidden"), "The showCloseButton is set to <false> and therefore the close button shouldn't be displayed.");
    }

    @Test
    public void testAttributeShowHistory() {
        // check whether the history is not present (showHistory is set to <false>)
        assertFalse(selenium.isElementPresent(notifyHistory), "The showHistory is set to <false> and there shouldn't be any history menu.");
        // set the showHistory to <true>
        attributesNotify.set(NotifyAttributes.showHistory, true);
        assertTrue(selenium.isElementPresent(notifyHistory), "The showHistory is set to <true> and one notify is present, but there is no history menu.");
        close(notify);
        // click on the All button in history menu
        selenium.click(notifyHistoryAll);
        waitGui
            .failWith("After clicking on <All> in history menu there should be all notifies.")
            .until(elementPresent.locator(notify));
        close(notify);
        // click on the Last button in the history menu
        selenium.click(notifyHistoryLast);
        waitModel
            .failWith("After clicking on <Last> in history menu there should be last notify.")
            .until(elementPresent.locator(notify));
    }

    @Test
    public void testAttributesStayTime() {
        // stayTime is set to a very high number
        delay(1000);
        assertTrue(selenium.isElementPresent(notify), "The stayTime is set to very high number and after some little delay the notify is not present.");
        // set the stayTime to <500>
        attributesNotify.set(NotifyAttributes.stayTime, 500);
        // wait for <1000>
        delay(1000);
        assertFalse(selenium.isElementPresent(notify), "The stayTime is set to 500 but after some delay the notify is still present.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11433")
    public void testAttributeSticky() {
        // set the stayTime to <0>
        attributesNotify.set(NotifyAttributes.stayTime, 0);
        // set the sticky to <false>
        attributesNotify.set(NotifyAttributes.sticky, false);
        waitGui
            .failWith("The stayTime is set to <0> and sticky to <false>, so the notify shouldn't be present.")
            .until(NegationCondition.getInstance().condition(elementPresent.locator(notify)));
        // produce messages and close them
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            close(messages.get(type));
        }
        waitGui
            .failWith("The stayTime is set to <0> and sticky to <false>, so the notify shouldn't be present.")
            .until(NegationCondition.getInstance().condition(elementPresent.locator(notify)));
        // set the sticky to <true>
        attributesNotify.set(NotifyAttributes.sticky, true);
        waitGui
            .failWith("The stayTime is set to <0> and sticky to <true>, so the notify should be present.")
            .until(elementPresent.locator(notify));
        // produce messages and close them
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            close(messages.get(type));
        }
        waitGui
            .failWith("The stayTime is set to <0> and sticky to <true>, so the notify should be present.")
            .until(elementPresent.locator(notify));
    }

    @Test
    public void testAttributeStyleClass() {
        attributesNotify.set(NotifyAttributes.styleClass, "someStyleClass");
        assertTrue(selenium.belongsClass(notify, "someStyleClass"), "The cssStyle has been set but notify doesn't belong to the set class.");
    }

    @Test
    public void testAttributeMessagesDelay() {
        // set the delay to <1000>
        attributesMessages.set(NotifyMessagesAttributes.delay, 1000);
        // check the delay for each message type
        for(String type : messages.keySet()) {
            selenium.click(pjq("input[id$=produce" + type + "]"));
            delay(500);
            assertFalse(selenium.isElementPresent(messages.get(type)), "The delay is set to 1000 and after some little waiting the " + type + " message shouldn't be present.");
            delay(1000);
            assertTrue(selenium.isElementPresent(messages.get(type)), "The delay is set to 1000 and after some waiting the " + type + " message should be present.");
        }
    }

    @Test
    public void testAttributeMessagesRendered() {
        // set the rendered to <false>
        attributesMessages.set(NotifyMessagesAttributes.rendered, false);
        // check the message isn't displayed for each message type
        for(String type : messages.keySet()) {
            String before = selenium.getText(jq("span[id=requestTime]"));
            selenium.click(pjq("input[id$=produce" + type + "]"));
            waitGui.until(NegationCondition.getInstance().condition(textEquals.locator(jq("span[id=requestTime]")).text(before)));
            assertFalse(selenium.isElementPresent(messages.get(type)), "The attribute rendered is set to <false> but the " + type + " message is still present.");
        }
    }

    @Test(enabled=false)
    public void testAttributeMessagesShowCloseButton() {
        // set the showCloseButton to <false>
        attributesMessages.set(NotifyMessagesAttributes.showCloseButton, false);
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            assertTrue(selenium.getStyle(jq(messages.get(type) + " > div.rf-ntf-co > div.rf-ntf-cls"), new CssProperty("visibility")).contains("hidden"), "The showCloseButton is set to <false> and therefore the close button shouldn't be displayed.");
        }
    }

    @Test(enabled=false)
    public void testAttributeMessagesShowDetail() {
        // produce messages and check whether the detail isn't displayed (showDetail is set to <false>)
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            // FIXME:
            assertTrue(selenium.getStyle(jq(messages.get(type) + " > div.rf-ny-co > div.rf-ny-te"), new CssProperty("display")).contains("none"));
        }
        // set showDetail to <true>
        selenium.click(pjq("input[type=radio][name*='attributesNotifyMessages:showDetail'][value=true]"));
        selenium.waitForPageToLoad();
        for(String type : messages.keySet()) {
            // FIXME:
            assertFalse(selenium.getStyle(jq(messages.get(type) + " > div.rf-ny-co > div.rf-ny-te"), new CssProperty("display")).contains("none"));
        }
    }

    @Test
    public void testAttributeMessagesShowHistory() {
        // check whether the history isn't present (showHistory is set to <false>)
        assertFalse(selenium.isElementPresent(notifyHistory), "The showHistory is set to <false> and there shouldn't be any history menu.");
        // set the showHistory to <true>
        attributesMessages.set(NotifyMessagesAttributes.showHistory, true);
        close(notify);
        // check whether the history isn't displayed (there is no message in the history)
        assertFalse(selenium.isElementPresent(notifyHistory), "The showHistory is set to <true>, but there is no message, so there shouldn't be any history menu.");
        // produce messages and close them
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            close(messages.get(type));
        }
        // click on All button in the history menu
        selenium.click(notifyHistoryAll);
        final int expected = messages.size();
        waitModel
            .failWith("After clicking on <All> in history menu there should be all messages. Expected <" + expected + ">, found <" + selenium.getCount(notify) + ">")
            .until(new SeleniumCondition() {
                @Override
                public boolean isTrue() {
                    return selenium.getCount(notify) == expected;
                }
            });
        for(int i=0; i<expected; i++) {
            close(notify);
        }
        // click on Last button in the history menu
        selenium.click(notifyHistoryLast);
        waitModel
            .failWith("After clicking on <Last> in history menu there should be last message. Expected <1>, found <" + selenium.getCount(notify)  + ">")
            .until(new SeleniumCondition() {
                @Override
                public boolean isTrue() {
                    return selenium.getCount(notify) == 1;
                }
            });
    }

    @Test
    public void testAttributeMessagesStayTime() {
        // produce messages and check whether the stay at least 1000 ms (stayTime is set to a very high number)
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            delay(1000);
            assertTrue(selenium.isElementPresent(messages.get(type)), "The stayTime is set to very high number and after some little delay the " + type + " message is not present.");
            close(messages.get(type));
        }
        // set the stayTime to <500>
        attributesMessages.set(NotifyMessagesAttributes.stayTime, 500);
        // produce messages and check whether the stay less then 1000 ms
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            delay(1000);
            assertFalse(selenium.isElementPresent(messages.get(type)), "The stayTime is set to 500 but after some delay the " + type + " message is still present.");
        }
    }

    @Test
    public void testAttributeMessagesSticky() {
        // set the stayTime to <0>
        attributesMessages.set(NotifyMessagesAttributes.stayTime, 0);
        // set the sticky to <false>
        attributesMessages.set(NotifyMessagesAttributes.sticky, false);
        // produce messages and check whether they disappear quickly
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            waitGui
                .failWith("The stayTime is set to <0> and sticky to <false>, so the " + type + " message shouldn't be present.")
                .until(NegationCondition.getInstance().condition(elementPresent.locator(messages.get(type))));
            closeAll(notify);
        }
        // set the sticky to <true>
        selenium.click(pjq("input[type=radio][name*='attributesNotifyMessages:sticky'][value=true]"));
        selenium.waitForPageToLoad();
        for(String type : messages.keySet()) {
            // produce message
            produceMessage(messages.get(type), type, 1);
            delay(500);
            // check whether the messages are still present
            waitGui
                .failWith("The stayTime is set to <0> and sticky to <true>, so the " + type + " message should be present.")
                .until(elementPresent.locator(messages.get(type)));
            closeAll(notify);
        }
    }

    @Test
    public void testAttributeMessagesStyleClass() {
        attributesMessages.set(NotifyMessagesAttributes.styleClass, "someStyleClass");
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            assertTrue(selenium.belongsClass(messages.get(type), "someStyleClass"), "The cssStyle has been set but " + type + " message doesn't belong to the set class.");
        }
    }

    @Test
    public void testAttributeStackRendered() {
        // set the rendered to <false>
        attributesStack.set(NotifyStackAttributes.rendered, false);
        assertFalse(selenium.isElementPresent(notifyStack), "The attribute rendered is set to <false> but messages in the notify stack are still present.");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(notify), "There is no notify message after page is loaded.");
        assertEquals(selenium.getText(getDetail(notify)), RichNotifyBean.DEFAULT_DETAIL, "The notify detail doesn't match.");
        assertEquals(selenium.getText(getSummary(notify)), RichNotifyBean.DEFAULT_SUMMARY, "The notify summary doesn't match.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11433")
    public void testCloseAndProduceNotify() {
        // check closing the notify
        close(notify);
        waitGui
            .failWith("After closing notify message the message is stil present.")
            .until(NegationCondition.getInstance().condition(elementPresent.locator(notify)));
        // check notify is not produced
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            close(messages.get(type));
        }
        waitGui
            .failWith("After producing messages there should be no notify. Expected <0>, was <" + selenium.getCount(notify)  + ">")
            .until(countEquals.locator(notify).count(0));
    }

    @Test
    public void testProduceAndCloseMessages() {
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, 1);
            close(messages.get(type));
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11433")
    public void testProduceMoreMessages() {
        // set the messageCount to <testedNumber>
        int testedNumber = 3;
        attributesTest.set(NotifyTestAttributes.messageCount, testedNumber);
        close(notify);
        for(String type : messages.keySet()) {
            produceMessage(messages.get(type), type, testedNumber);
            assertEquals(getNumberOfNotifies(), 0, "Number of produced messages has been set to <" + testedNumber + "> but number of notifies should be still <0>.");
            closeAll(messages.get(type));
        }
    }
}
