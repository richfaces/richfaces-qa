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
package org.richfaces.tests.metamer.ftest.richMessage;

import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.messageAttributes;
import static org.richfaces.tests.metamer.ftest.richMessage.MessageAttributes.FOR;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;


/**
 * Abstract class with list of tests appropriate for rich:message component
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22748 $
 */
public abstract class RichMessageTest extends AbstractRichMessageTest {

    // locator for main rich:message component (tested element)
    protected static JQueryLocator mainMessage = pjq("span[id$=simpleInputMsg]");
    protected JQueryLocator summary = getTestElemLocator().getDescendant(jq("span.rf-msg-sum"));
    protected JQueryLocator detail = getTestElemLocator().getDescendant(jq("span.rf-msg-det"));

    /**
     * Attribute 'for' change behavior: only messages bound to element with
     * id specified in 'for' should be displayed
     */
    public void testFor() {

        // firstly, remove value from attribute for and generate message
        messageAttributes.setLower(FOR, "");

        generateValidationMessages(false);
        // assertFalse(selenium.isElementPresent(getTestElemLocator()));
        waitGui.until(elementPresent.locator(jq(getTestElemLocator().getRawLocator() + ":empty")));

        // now set for attribute back to "simpleInput2"
        messageAttributes.setLower(FOR, "simpleInput2");

        generateValidationMessages(false);
        waitGui.until(elementPresent.locator(getTestElemLocator()));
    }
}
