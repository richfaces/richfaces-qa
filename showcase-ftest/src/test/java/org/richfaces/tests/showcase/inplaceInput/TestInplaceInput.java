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
package org.richfaces.tests.showcase.inplaceInput;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceInput/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22196 $
 */
public class TestInplaceInput extends AbstractAjocadoTest {

    /* *******************************************************************************
     * Locators*******************************************************************************
     */

    protected JQueryLocator nameInputLabel = jq("span.rf-ii-lbl:eq(0)");
    protected JQueryLocator emailInputLabel = jq("span.rf-ii-lbl:eq(1)");
    protected JQueryLocator nameInput = jq("input[id$=Input]:eq(0)");
    protected JQueryLocator emailInput = jq("input[id$=Input]:eq(1)");

    /* ********************************************************************************
     * Tests********************************************************************************
     */

    @Test
    public void testEnterSomethingToNameInput() {
        enterSomethingToInputAndCheck(nameInput, nameInputLabel);
    }

    @Test
    public void testEnterSomethingToEmail() {
        enterSomethingToInputAndCheck(emailInput, emailInputLabel);
    }

    /* ***********************************************************************************
     * Help methods***********************************************************************************
     */

    private void enterSomethingToInputAndCheck(JQueryLocator input, JQueryLocator label) {
        selenium.click(input);

        String expectedString = "Test string";
        selenium.type(input, expectedString);
        selenium.focus(input);
        selenium.keyPressNative(KeyEvent.VK_ENTER);

        String actualString = selenium.getText(label);

        assertEquals(actualString, expectedString, "The value in the input is not what have been typed there!");
    }

}
