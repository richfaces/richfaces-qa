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
package org.richfaces.tests.metamer.ftest.richMessages;

import static org.jboss.arquillian.ajocado.Ajocado.countEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.messageAttributes;
import static org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes.FOR;
import static org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes.globalOnly;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.richMessage.AbstractRichMessageTest;

/**
 * Abstract class with list of tests appropriate for rich:messages component
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22748 $
 */
public abstract class RichMessagesTest extends AbstractRichMessageTest {

    // locator for main rich:message component (tested element)
    protected static JQueryLocator mainMsg1 = pjq("span[id$=msgs1]");
    protected static JQueryLocator mainMsg2 = pjq("span[id$=msgs2]");
    // locators for summary and detail in container
    protected JQueryLocator summary = getTestElemLocator().getDescendant(jq("span.rf-msgs-sum"));
    protected JQueryLocator detail = getTestElemLocator().getDescendant(jq("span.rf-msgs-det"));
    protected JQueryLocator generateMsgsBtn = pjq("input[id$=generateMsgsBtn]");

    // Methods for error and warning message locators
    private JQueryLocator getErrorMsg(JQueryLocator testElem) {
        return testElem.getDescendant(jq("span.rf-msgs-err"));
    }

    private JQueryLocator getWarnMsg(JQueryLocator testElem) {
        return testElem.getDescendant(jq("span.rf-msgs-wrn"));
    }

    private JQueryLocator getErrorMsg() {
        return getErrorMsg(getTestElemLocator());
    }

    private JQueryLocator getWarnMsg() {
        return getWarnMsg(getTestElemLocator());
    }

    /**
     * Attribute 'for' change behavior: only messages bound to element with
     * id specified in 'for' should be displayed
     */
    public void testFor() {

        // firstly reset to null
        messageAttributes.setLower(FOR, "");

        // generate faces message by btn
        selenium.click(generateMsgsBtn);

        // no messages for simpleInput1 or simpleInput2 should appear
        waitGui.until(countEquals.count(0).locator(getTestElemLocator().getChild(jq("span[id$=msgs1:form:simpleInput1]"))));
        waitGui.until(countEquals.count(0).locator(getTestElemLocator().getChild(jq("span[id$=msgs1:form:simpleInput2]"))));

        messageAttributes.setLower(FOR, "simpleInput1");
        // generate faces messages by btn
        selenium.click(generateMsgsBtn);
        // only messages for simpleInput1 should appear:
        waitModel.until(countEquals.count(2).locator(getTestElemLocator().getChild(jq("span[id$=msgs1:form:simpleInput1]"))));
        // one type error
        waitModel.until(countEquals.count(1).locator(getErrorMsg()));
        // one type warning
        waitModel.until(countEquals.count(1).locator(getWarnMsg()));

        messageAttributes.setLower(FOR, "simpleInput2");
        // generate faces messages by btn
        selenium.click(generateMsgsBtn);
        // only 2 messages for simpleInput2
        waitModel.until(countEquals.count(2).locator(getTestElemLocator().getChild(jq("span[id$=msgs1:form:simpleInput2]"))));
        // only 2 messages should appear
        waitModel.until(countEquals.count(1).locator(getErrorMsg()));
        waitModel.until(countEquals.count(1).locator(getWarnMsg()));
    }

    /**
     * globalOnly change behavior of displaying messages.
     * When <b>true</b> only messages not bound to any input are displayed
     *      <b>false</b> all messages are displayed
     * This attribute cannot be set with <i>for</i> attribute.
     * 
     * In this case, second messages component is relevant 
     * (with label "Messages2 - without 'for' but 'globalOnly' attribute instead")
     * This is the reason why used "mainMsg2" instead of  getTestElemLocator()
     */
    public void testGlobalOnly() {
        // firstly set for attribute to null
        messageAttributes.setLower(FOR, "");

        // then set globalOnly attribute
        messageAttributes.set(globalOnly, Boolean.FALSE);

        selenium.click(generateMsgsBtn);

        // All messages should appear: 
        //  for simpleInput1
        waitModel.until(countEquals.count(2).locator(mainMsg2.getChild(jq("span[id$=:msgs2:form:simpleInput1]"))));
        //  for simpleInput2
        waitModel.until(countEquals.count(2).locator(mainMsg2.getChild(jq("span[id$=:msgs2:form:simpleInput2]"))));
        //  global messages
        waitModel.until(countEquals.count(2).locator(mainMsg2.getChild(jq("span[id$=:msgs2:]"))));
        // three type error
        waitModel.until(countEquals.count(3).locator(getErrorMsg(mainMsg2)));
        // three type warning
        waitModel.until(countEquals.count(3).locator(getWarnMsg(mainMsg2)));
    }
}
