/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.commandLink;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.commandButton.AbstractTestA4jCommand;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestCommandLink extends AbstractTestA4jCommand {

    /* *****************************************************************************
     * Locators*****************************************************************************
     */

    protected JQueryLocator commandLink = jq("fieldset form a");

    /* ******************************************************************************
     * Tests******************************************************************************
     */

    @Test
    public void testClickOnTheLinkWhileInputIsEmpty() {

        /*
         * click on the link, the output should be empty string
         */
        guardXhr(selenium).click(commandLink);

        String expectedOutput = "Hello !";
        assertEquals(selenium.getText(outHello).trim(), expectedOutput, "The output should be " + expectedOutput);
    }

    @Test
    public void testTypeSomeCharactersAndClickOnTheLink() {

        /*
         * type a string and click on the link, check the outHello
         */
        String testString = "Test string";

        selenium.typeKeys(input, testString);

        guardXhr(selenium).click(commandLink);

        String expectedOutput = "Hello " + testString + " !";
        assertEquals(selenium.getText(outHello), expectedOutput, "The output should be: " + expectedOutput);
    }

    @Test
    public void testEraseSomeStringAndClickOnTheLink() {

        /*
         * erases string and check the output is empty string
         */

        String testString = "Test string";

        selenium.typeKeys(input, testString);

        guardXhr(selenium).click(commandLink);

        eraseInput(input);

        guardXhr(selenium).click(commandLink);

        String expectedOutput = "Hello !";
        waitAjax.until(textEquals.locator(outHello).text(expectedOutput));
    }

}
