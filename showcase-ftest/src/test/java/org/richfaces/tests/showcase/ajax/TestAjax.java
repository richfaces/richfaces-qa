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
package org.richfaces.tests.showcase.ajax;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestAjax extends AbstractGrapheneTest {

    /* *************************************************************************************
     * Locators*************************************************************************************
     */
    protected JQueryLocator input = jq("fieldset input:text");
    protected JQueryLocator ajaxOutput = jq("fieldset form span");

    /* ***************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testTypeSomeStringToTheInputAndCheckTheOutput() {

        String testString = "Test string";

        /*
         * Simulates user input, key by key
         */
        for (int i = 0; i < testString.length(); i++) {
            if (i == 0) {
                selenium.type(input, String.valueOf(testString.charAt(0)));
            } else {
                selenium.type(input, testString.subSequence(0, i + 1).toString());
            }

            guardXhr(selenium).fireEvent(input, Event.KEYUP);

            if (i == 0) {
                assertTrue((testString.charAt(0) == selenium.getText(ajaxOutput).charAt(0)), "The ajax output "
                    + "should be equal to the given input");
            } else {
                assertEquals((testString.substring(0, i + 1).trim()), selenium.getText(ajaxOutput).trim(),
                    "The ajax output " + "should be equal to the given input");
            }
        }
    }

    @Test
    public void testEraseStringFromInputAndCheckTheOutput() {

        selenium.typeKeys(input, "This will be erased");
        guardXhr(selenium).fireEvent(input, Event.KEYUP);

        eraseInput(input);
        guardXhr(selenium).fireEvent(input, Event.KEYUP);

        String expectedOutput = "";

        assertEquals(selenium.getText(ajaxOutput), expectedOutput, "The ajax output should be deleted");
    }

}
