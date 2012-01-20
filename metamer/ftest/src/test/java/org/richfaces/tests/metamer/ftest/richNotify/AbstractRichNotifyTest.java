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
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;

/**
 * Abstract test case for pages faces/components/notify/
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractRichNotifyTest extends AbstractAjocadoTest {

    /**
     * attributes for rich:notifyMessages tag
     */
    protected final Attributes<NotifyMessagesAttributes> attributesMessages = new Attributes<NotifyMessagesAttributes>(pjq("table.attributes[id$=attributesNotifyMessages]"));

    /**
     * attributes for rich:notify tag
     */
    protected final Attributes<NotifyAttributes> attributesNotify = new Attributes<NotifyAttributes>(pjq("table.attributes[id$=attributesNotify]"));

    /**
     * attributes for rich:notifyStack tag
     */
    protected final Attributes<NotifyStackAttributes> attributesStack = new Attributes<NotifyStackAttributes>(pjq("table.attributes[id$=attributesNotifyStack]"));

    /**
     * attributes for test
     */
    protected final Attributes<NotifyTestAttributes> attributesTest = new Attributes<NotifyTestAttributes>(pjq("table.attributes[id$=attributesBean]"));

    /**
     * notify locator which matches on all notifies and messages
     */
    protected final JQueryLocator notify = jq("div.rf-ntf");

    /**
     * error message locator
     */
    protected final JQueryLocator notifyError = jq("div.rf-ntf-err");

    /**
     * fatal message locator
     */
    protected final JQueryLocator notifyFatal = jq("div.rf-ntf-ftl");

    /**
     * history of notifies and notify messages
     */
    protected final JQueryLocator notifyHistory = jq("div.rf-ntf-hstr-hdr");

    /**
     * 'all' button in the history menu
     */
    protected final JQueryLocator notifyHistoryAll = jq("button.rf-ntf-hstr-all");

    /**
     * 'last' button in the history menu
     */
    protected final JQueryLocator notifyHistoryLast = jq("button.rf-ntf-hstr-last");

    /**
     * info message locator
     */
    protected final JQueryLocator notifyInfo = jq("div.rf-ntf-inf");

    /**
     * warn message locator
     */
    protected final JQueryLocator notifyWarn = jq("div.rf-ntf-wrn");

    /**
     * It closes a message defined by the given locator
     *
     * @param locator the message locator
     */
    protected void close(final JQueryLocator locator) {
        final int before = selenium.getCount(locator);
        selenium.click(getFirstCloseButton(locator));
        waitGui
            .failWith("Message " + locator.getRawLocator() + " can't be closed.")
            .until(countEquals.locator(locator).count(before - 1));
    }

    /**
     * It closes all message defined by the given locator
     *
     * @param locator the message locator
     */
    protected void closeAll(final JQueryLocator locator) {
        while(selenium.isElementPresent(locator)) {
            close(locator);
        }
    }

    /**
     * It delays for the given number of ms. If there is any exception thrown, it's logged by logger of this class.
     *
     * @param ms number of miliseconds
     */
    protected void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, e.toString());
        }
    }

    /**
     * It returns a close button of a message defined by the given locator
     *
     * @param message the message locator
     * @return the close button locator
     */
    protected JQueryLocator getCloseButton(JQueryLocator message) {
        return jq(message.getRawLocator() + " > div.rf-ntf-cnt > div.rf-ntf-cls > span.rf-ntf-cls-ico");
    }

    /**
     * It returns a detail element of a message defined by the given locator
     *
     * @param message the message locator
     * @return the detail locator
     */
    protected JQueryLocator getDetail(JQueryLocator message) {
        return jq(notify.getRawLocator() + " > div.rf-ntf-cnt > div.rf-ntf-det");
    }

    /**
     * It returns the first available close button of a message defined by the given locator
     *
     * @param message the message locator
     * @return the close button locator
     */
    protected JQueryLocator getFirstCloseButton(JQueryLocator message) {
        return jq(getCloseButton(message).getRawLocator() + ":first");
    }

    /**
     * It returns a number of notifes (only notifies, not messages) in the given stack.
     *
     * @return number of notifies
     */
    protected int getNumberOfNotifies() {
        return selenium.getCount(notify) - (selenium.getCount(notifyError) + selenium.getCount(notifyFatal) /*+ selenium.getCount(notifyInfo)*/ + selenium.getCount(notifyWarn));
    }

    /**
     * It returns a summary element of a message defined by the given locator
     *
     * @param message the message locator
     * @return the summary locator
     */
    protected JQueryLocator getSummary(JQueryLocator message) {
        return jq(notify.getRawLocator() + " > div.rf-ntf-cnt > div.rf-ntf-sum");
    }

    /**
     * It produces a message defined by the given message locator and the given type.
     * It waits until the <expected> number of message is produced.
     *
     * @param message the message locator
     * @param type the name of the message type (started with capital letter)
     * @param expected expected number of produced messages
     */
    protected void produceMessage(final JQueryLocator message, String type, final int expected) {
        final int before = selenium.getCount(message);
        selenium.click(pjq("input[id$=produce" + type + "]"));
        waitGui
            .failWith("Number of produced messages has been set to <" + expected + "> but <" + selenium.getCount(message) + "> " + type + " messages is present (" + message.getRawLocator() + ").")
            .until(countEquals.locator(message).count(expected + before));
    }

}
