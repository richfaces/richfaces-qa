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
package org.richfaces.tests.showcase.region;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import javax.swing.event.DocumentEvent.ElementChange;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.Element;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestRegion extends AbstractGrapheneTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator firstUserName = jq("input[type=text]:eq(0)");
    protected JQueryLocator firstEmail = jq("input[type=text]:odd:eq(1)");
    protected JQueryLocator firstEnteredName = jq("table[id$=echopanel1] tr:eq(1) td:eq(1)");
    protected JQueryLocator firstEnteredEmail = jq("table[id$=echopanel1] tr:eq(2) td:eq(1)");
    protected JQueryLocator firstWrongSubmit = jq("input[type=submit]:eq(0)");
    protected JQueryLocator secondUserName = jq("input[type=text]:even:eq(1)");
    protected JQueryLocator secondEmail = jq("input[type=text]:odd:eq(1)");
    protected JQueryLocator secondEnteredName = jq("table[id$=echopanel2] tr:eq(1) td:eq(1)");
    protected JQueryLocator secondEnteredEmail = jq("table[id$=echopanel2] tr:eq(2) td:eq(1)");
    protected JQueryLocator secondRightSubmit = jq("input[type=submit]:eq(1)");

    @Test
    public void testFirstWrongSubmitUserName() {

        typeSomethingToInputTestWhetherOutputIsEmpty(firstUserName, firstEnteredName, firstWrongSubmit, true);
    }

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testFirstWrongSubmitEmail() {

        typeSomethingToInputTestWhetherOutputIsEmpty(firstEmail, firstEnteredEmail, firstWrongSubmit, true);
    }

    @Test
    public void testFirstWrongSubmitUserNameAndEmail() {

        typeSomethingToTwoInputsTestWhetherOuputIsEmpty(firstUserName, firstEmail, firstEnteredName, firstEnteredEmail,
            firstWrongSubmit, true);
    }

    @Test
    public void testSecondRightSubmitUserName() {

        typeSomethingToInputTestWhetherOutputIsEmpty(secondUserName, secondEnteredName, secondRightSubmit, false);
    }

    @Test
    public void testSecondRightSubmitEmail() {

        typeSomethingToInputTestWhetherOutputIsEmpty(secondEmail, secondEnteredEmail, secondRightSubmit, false);
    }

    @Test
    public void testSecondRightSubmitUserNameAndEmail() {

        typeSomethingToTwoInputsTestWhetherOuputIsEmpty(secondUserName, secondEmail, secondEnteredName,
            secondEnteredEmail, secondRightSubmit, false);
    }

    /* ********************************************************************************************************
     * Help methods *********************************************************************
     * ***********************************
     */

    /**
     * Types some string to the input and checks whether the output is empty string
     *
     * @param input
     *            input where string will be typed
     * @param output
     *            output which should be empty
     */
    private void typeSomethingToInputTestWhetherOutputIsEmpty(JQueryLocator input, JQueryLocator output,
        JQueryLocator submit, boolean shouldBeEmpty) {

        String testString = "Test String";

        selenium.type(input, testString);

        guardXhr(selenium).click(submit);

        String actualString = selenium.getText(output).trim();

        if (shouldBeEmpty) {
            assertEquals(actualString, "", "The string should be empty!");
        } else {
            assertEquals(actualString, testString);
        }
    }

    /**
     * Types something to two inputs and checks outputs whether they are empty or not
     *
     * @param input1
     *            first input where something will be typed
     * @param input2
     *            second input where something will be typed
     * @param output1
     *            first checked output
     * @param output2
     *            second checked output
     * @param submit
     *            submit button where will be clicked
     * @param shouldBeEmpty
     *            should be outputs empty?
     */
    private void typeSomethingToTwoInputsTestWhetherOuputIsEmpty(JQueryLocator input1, JQueryLocator input2,
        JQueryLocator output1, JQueryLocator output2, JQueryLocator submit, boolean shouldBeEmpty) {

        String testStringUserName = "Test String user name";
        String testStringEmail = "Test String email";

        selenium.type(input1, testStringUserName);
        selenium.type(input2, testStringEmail);

        guardXhr(selenium).click(submit);

        String actualName = selenium.getText(output1).trim();
        String actualEmail = selenium.getText(output2).trim();

        if (shouldBeEmpty) {
            assertEquals(actualName, "", "The string retrieved from entered name should be empty!");
            assertEquals(actualEmail, "", "The string retrieved from entered emial should be empty!");
        } else {
            assertEquals(actualName, testStringUserName);
            assertEquals(actualEmail, testStringEmail);
        }

    }
}
