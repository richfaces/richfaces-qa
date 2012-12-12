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
package org.richfaces.tests.showcase.autocomplete;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestClientFilter extends AbstractGrapheneTest {

    /* ********************************************************************
     * Locators********************************************************************
     */

    protected JQueryLocator input = jq("input[type=text]");
    protected JQueryLocator selection = jq("div.rf-au-itm");

    /* *********************************************************************
     * Tests*********************************************************************
     */

    @Test
    public void testClientFilterFunctionContains() {

        selenium.focus(input);

        selenium.type(input, "ska");

        guardNoRequest(selenium).fireEvent(input, Event.KEYPRESS);

        assertTrue(selenium.isVisible(selection),
            "The selection should be visible, since there is correct sequence of chars!");

        selenium.keyPressNative(KeyEvent.VK_ENTER);

        String actualValueOfInput = selenium.getValue(input);
        assertEquals(actualValueOfInput, "Alaska", "The content should be Alaska, since it contains string ska");
    }

    protected String getSampleLabel() {
        return "Custom Client Filter";
    }
}
