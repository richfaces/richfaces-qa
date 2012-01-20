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
package org.richfaces.tests.showcase.dataScroller;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimpleScrolling extends AbstractDataScrollerTest {

    /* ***********************************************************************************************************
     * Locators
     * ***********************************************************************************************************
     */

    private JQueryLocator firstRowOfTable = jq("tbody[class=rf-dt-b] tr:first");

    /* **********************************************************************************************************
     * Tests**********************************************************************************************************
     */

    @Test
    public void testFirstPageButton() {

        checkFirstOrLastPageButton(firstPageButton, firstPageButtonDis, lastPageButton, lastPageButtonDis,
            fastPrevButtonDis, previousButtonDis);
    }

    @Test
    public void testLastPageButton() {

        checkFirstOrLastPageButton(lastPageButton, lastPageButtonDis, firstPageButton, firstPageButtonDis,
            fastNextButtonDis, nextButtonDis);
    }

    @Test
    public void testPreviousAndNextPageButton() {

        testFastAndNormalButtons(nextButton, previousButton);
    }

    // in this demo the fasts button have the same functionality as the next/previous buttons
    @Test
    public void testFastNextPrevious() {

        testFastAndNormalButtons(fastNextButton, fastPrevButton);
    }

    @Test
    public void testNumberOfPagesButtons() {

        if (!selenium.isElementPresent(firstPageButtonDis)) {

            guardXhr(selenium).click(firstPageButton);
        }

        checkNumberOfPagesButtons(3);

        checkNumberOfPagesButtons(5);

        checkNumberOfPagesButtons(6);

        checkNumberOfPagesButtons(4);
    }

    /* **********************************************************************************************************************
     * Help methods
     * ******************************************************************************************************
     * ****************
     */

    /**
     * Checking the buttons which have number of pages
     */
    private void checkNumberOfPagesButtons(int numberOfPage) {

        JQueryLocator checkingButton = jq("a[class*='" + CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:contains('"
            + numberOfPage + "'):first");

        Car carBeforeClicking = retrieveCarFromRow(firstRowOfTable, 0, 4);

        guardXhr(selenium).click(checkingButton);

        Car carAfterClicking = retrieveCarFromRow(firstRowOfTable, 0, 4);

        assertFalse(carBeforeClicking.equals(carAfterClicking), "The data should be different on the different pages!");

        int actualCurrentNumberOfPage = getNumberOfCurrentPage();

        assertEquals(actualCurrentNumberOfPage, numberOfPage, "We should be on the " + numberOfPage + ". page");
    }

    /**
     * Tests fast and normal buttons
     *
     * @param nextButton
     * @param previousButton
     */
    private void testFastAndNormalButtons(JQueryLocator nextButton, JQueryLocator previousButton) {

        // checks where are we now, there are three possible cases:

        if (selenium.isElementPresent(firstPageButtonDis)) { // -we are on the first page

            checkButton(nextButton, previousButton, 1);

        } else if (selenium.isElementPresent(lastPageButtonDis)) { // -we are on the last page

            checkButton(previousButton, nextButton, 1);

        } else { // -we are somewhere else and therefore it does not matter which function we use

            checkButton(previousButton, nextButton, 1);
        }
    }

    /**
     * Checks the functionality of first page button or last page button, according to parameters
     *
     * @param checkingButton
     *            the button which functionality is checked
     * @param checkingButtonDis
     *            disabled checked button
     * @param otherButton
     *            the other button according to checkingButton, for example when checking button is first page button
     *            then otherButton should be last page button, and vice versa
     * @param otherButtonDis
     *            disabled other button
     * @param fastButtonOfCheckingDis
     *            disabled fast button which is between the checking button
     * @param previousOrNextButtonOfCheckingDis
     *            disabled previous or next page button
     */
    private void checkFirstOrLastPageButton(JQueryLocator checkingButton, JQueryLocator checkingButtonDis,
        JQueryLocator otherButton, JQueryLocator otherButtonDis, JQueryLocator fastButtonOfCheckingDis,
        JQueryLocator previousOrNextButtonOfCheckingDis) {

        // click on the last page button, if exists, if does not, it means that it is already on the last page
        // but for sure check for it
        if (selenium.isElementPresent(otherButton)) {

            guardXhr(selenium).click(otherButton);
        } else if (!selenium.isElementPresent(otherButtonDis)) {

            fail("The button for " + otherButtonDis.getRawLocator() + " should be there");
        }

        Car carBeforeClick = retrieveCarFromRow(firstRowOfTable, 0, 4);

        guardXhr(selenium).click(checkingButton);

        Car carAfterClick = retrieveCarFromRow(firstRowOfTable, 0, 4);

        assertFalse(carBeforeClick.equals(carAfterClick), "The data from table should be different, "
            + "when clicking on the on the " + checkingButton.getRawLocator());

        assertTrue(selenium.isElementPresent(checkingButtonDis), "The " + checkingButtonDis.getRawLocator()
            + " button should be disabled");

        assertTrue(selenium.isElementPresent(fastButtonOfCheckingDis), "The " + fastButtonOfCheckingDis.getRawLocator()
            + " page button should be disabled");

        assertTrue(selenium.isElementPresent(previousOrNextButtonOfCheckingDis), "The "
            + previousOrNextButtonOfCheckingDis + " button should be disabled");
    }

    /**
     * Checks functionality of two buttons, one is moving forward and second backward
     *
     * @param button1
     *            either forward or backward moving button
     * @param button2
     *            either forward or backward moving button
     * @param step
     *            the number of pages we are moving by pressing of the button
     */
    private void checkButton(JQueryLocator button1, JQueryLocator button2, int step) {

        int numberOfPageAtBeginning = getNumberOfCurrentPage();

        Car carBeforeClick = retrieveCarFromRow(firstRowOfTable, 0, 4);

        guardXhr(selenium).click(button1);

        Car carAfterClick = retrieveCarFromRow(firstRowOfTable, 0, 4);

        assertFalse(carBeforeClick.equals(carAfterClick), "The data from table should be different, "
            + "when clicking on the on the " + button1.getRawLocator());

        int numberOfPageAfterClickOnButton1 = getNumberOfCurrentPage();

        if (button1.equals(nextButton) || button1.equals(fastNextButton)) {
            assertEquals(numberOfPageAtBeginning, numberOfPageAfterClickOnButton1 - 1,
                "Next button or fast next button does not work");
        } else {
            assertEquals(numberOfPageAtBeginning, numberOfPageAfterClickOnButton1 + 1,
                "Previous button or fase previous button does not work");
        }

        carBeforeClick = retrieveCarFromRow(firstRowOfTable, 0, 4);

        guardXhr(selenium).click(button2);

        carAfterClick = retrieveCarFromRow(firstRowOfTable, 0, 4);

        assertFalse(carBeforeClick.equals(carAfterClick), "The data from table should be different, "
            + "when clicking on the on the " + button2.getRawLocator());

        int currentNumberOfPage = getNumberOfCurrentPage();

        if (button2.equals(previousButton) || button2.equals(fastPrevButton)) {
            assertEquals(currentNumberOfPage, numberOfPageAtBeginning,
                "Previous button or fase previous button does not work");
        } else {
            assertEquals(currentNumberOfPage, numberOfPageAtBeginning, "Next button or fast next button does not work");
        }
    }

}
