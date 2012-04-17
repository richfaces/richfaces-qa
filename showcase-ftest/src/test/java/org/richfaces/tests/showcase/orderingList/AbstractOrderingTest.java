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
package org.richfaces.tests.showcase.orderingList;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class AbstractOrderingTest extends AbstractGrapheneTest {

    /* **********************************************************************
     * Locators **********************************************************************
     */

    protected JQueryLocator firstButton = jq(".rf-ord-up-tp:eq({0})");
    protected JQueryLocator upButton = jq(".rf-ord-up:eq({0})");
    protected JQueryLocator downButton = jq(".rf-ord-dn:eq({0})");
    protected JQueryLocator lastButton = jq(".rf-ord-dn-bt:eq({0})");

    /* ******************************************************************************
     * Constants ***************************************************************** **************
     */

    protected String DISABLED_BUTTON_CLASS = "rf-ord-btn-dis";

    /* ********************************************************************************************
     * Abstract methods ************************************************************** ******************************
     */

    /**
     * @param optionLocator
     *            this should be something that determines all options, there should be place for calling method format
     *            on that so it will be possible to add there e.g. :eq(1) or :last JQuery selector parameter
     * @param firstButton
     */
    protected void checkFirstButton(JQueryLocator optionLocator, JQueryLocator firstButton) {

        checkButton(optionLocator.format(":eq(2)"), firstButton, optionLocator.format(":first"));

        checkButton(optionLocator.format(":eq(10)"), firstButton, optionLocator.format(":first"));
    }

    /**
     * @param optionLocator
     *            this should be something that determines all options, there should be place for calling method format
     *            on that so it will be possible to add there e.g. :eq(1) or :last JQuery selector parameter
     * @param lastButton
     */
    protected void checkLastButton(JQueryLocator optionLocator, JQueryLocator lastButton) {

        checkButton(optionLocator.format(":eq(2)"), lastButton, optionLocator.format(":last"));

        checkButton(optionLocator.format(":eq(10)"), lastButton, optionLocator.format(":last"));
    }

    /**
     *
     * @param optionLocator
     *            this should be something that determines all options, there should be place for calling method format
     *            on that so it will be possible to add there e.g. :eq(1) or :last JQuery selector parameter
     * @param upButton
     */
    protected void checkUpButton(JQueryLocator optionLocator, JQueryLocator upButton) {

        checkButton(optionLocator.format(":eq(2)"), upButton, optionLocator.format(":eq(1)"));

        checkButton(optionLocator.format(":eq(10)"), upButton, optionLocator.format(":eq(9)"));
    }

    /**
     *
     * @param optionLocator
     *            this should be something that determines all options, there should be place for calling method format
     *            on that so it will be possible to add there e.g. :eq(1) or :last JQuery selector parameter
     * @param downButton
     */
    protected void checkDownButton(JQueryLocator optionLocator, JQueryLocator downButton) {

        checkButton(optionLocator.format(":eq(2)"), downButton, optionLocator.format(":eq(3)"));

        checkButton(optionLocator.format(":eq(10)"), downButton, optionLocator.format(":eq(11)"));
    }

    /**
     *
     * @param optionLocator
     *            optionLocator this should be something that determines all options, there should be place for calling
     *            method format on that so it will be possible to add there e.g. :eq(1) or :last JQuery selector
     *            parameter
     * @param buttonToPress
     * @param buttonsToBeDisabled
     */
    protected void checkFirstAndUpButtonsDisabled(JQueryLocator optionLocator, JQueryLocator buttonToPress,
        JQueryLocator... buttonsToBeDisabled) {

        checkDisableButtonWhenSuitable(optionLocator.format(":eq(1)"), buttonToPress, buttonsToBeDisabled);
    }

    /**
     *
     * @param optionLocator
     *            optionLocator this should be something that determines all options, there should be place for calling
     *            method format on that so it will be possible to add there e.g. :eq(1) or :last JQuery selector
     *            parameter
     * @param buttonToPress
     * @param buttonsToBeDisabled
     */
    protected void checkDownAndLastButtonsDisabled(JQueryLocator optionLocator, JQueryLocator buttonToPress,
        JQueryLocator... buttonsToBeDisabled) {

        int howManyOptions = selenium.getCount(optionLocator.format(""));

        checkDisableButtonWhenSuitable(optionLocator.format(":eq(" + (howManyOptions - 2) + ")"), buttonToPress,
            buttonsToBeDisabled);
    }

    /* *********************************************************************************************************************
     * Help methods **************************************************************
     * *******************************************************
     */

    /**
     * Checks particular button, select option whichOptionToMove, uses button and checks that it moved at position
     * whereItShouldBe
     *
     * @param whichOptionToMove
     * @param button
     * @param whereItShouldBe
     */
    private void checkButton(JQueryLocator whichOptionToMove, JQueryLocator button, JQueryLocator whereItShouldBe) {

        String textOptionToMove = selenium.getText(whichOptionToMove);

        selectSomthingAndPressButton(whichOptionToMove, button);

        String textWhereItShouldBe = selenium.getText(whereItShouldBe);

        assertEquals(textWhereItShouldBe, textOptionToMove, "The option: " + whichOptionToMove
            + "should be now at the position: " + whereItShouldBe);
    }

    /**
     * Selects a option and presses the particular button
     *
     * @param whichOptionToMove
     * @param button
     */
    private void selectSomthingAndPressButton(JQueryLocator whichOptionToMove, JQueryLocator button) {

        selenium.click(whichOptionToMove);

        selenium.click(button);
    }

    /**
     * Checks buttons that they are disabled after pressing particular button, e.g. the first option is selected, then
     * First button together with Up button should be disabled
     *
     * @param whichOptionToMove
     * @param buttonToPress
     * @param buttonsToBeDisabled
     */
    private void checkDisableButtonWhenSuitable(JQueryLocator whichOptionToMove, JQueryLocator buttonToPress,
        JQueryLocator... buttonsToBeDisabled) {

        selectSomthingAndPressButton(whichOptionToMove, buttonToPress);

        for (JQueryLocator i : buttonsToBeDisabled) {

            String clazz = selenium.getAttribute(i.getAttribute(Attribute.CLASS));

            assertTrue(clazz.contains(DISABLED_BUTTON_CLASS));
        }
    }

}
