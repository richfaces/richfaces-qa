/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.dataScroller.page.SimpleScrollingPage;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimpleScrolling extends AbstractDataIterationWithCars {

    @Page
    private SimpleScrollingPage page;

    /* **********************************************************************************************************
     * Tests**********************************************************************************************************
     */

    @Test
    public void testFirstPageButton() {
        // starting from the first page
        // click on the last page
        Graphene.guardAjax(page.lastPageButton).click();
        Car carBeforeClick = retrieveCarFromRow(page.firstRowOfTable, 0, 4);
        // click on the first page
        Graphene.guardAjax(page.firstPageButton).click();
        Car carAfterClick = retrieveCarFromRow(page.firstRowOfTable, 0, 4);

        // check
        assertFalse(carBeforeClick.equals(carAfterClick), "The data from table should be different, "
            + "when clicking on the on the the first page button");

        assertTrue(isElementPresent(page.firstPageButtonDis), "The first page button should be disabled");

        assertTrue(isElementPresent(page.fastPrevButtonDis), "The fast previous page button should be disabled");

        assertTrue(isElementPresent(page.previousButtonDis), "The previous button should be disabled");
    }

    @Test
    public void testLastPageButton() {
        // starting from the last page
        Graphene.guardAjax(page.lastPageButton).click();
        // click on the first page
        Graphene.guardAjax(page.firstPageButton).click();
        Car carBeforeClick = retrieveCarFromRow(page.firstRowOfTable, 0, 4);
        // click on the last page
        Graphene.guardAjax(page.lastPageButton).click();
        Car carAfterClick = retrieveCarFromRow(page.firstRowOfTable, 0, 4);

        // check
        assertFalse(carBeforeClick.equals(carAfterClick), "The data from table should be different, "
            + "when clicking on the on the the last page button");

        assertTrue(isElementPresent(page.lastPageButtonDis), "The last page button should be disabled");

        assertTrue(isElementPresent(page.fastNextButtonDis), "The fast next page button should be disabled");

        assertTrue(isElementPresent(page.nextButtonDis), "The next button should be disabled");
    }

    @Test
    public void testPreviousAndNextPageButton() {
        testFastAndNormalButtons(page.nextButton, page.previousButton, false);
    }

    // in this demo the fasts button have the same functionality as the next/previous buttons
    @Test
    public void testPreviousAndNextPageButtonFast() {
        testFastAndNormalButtons(page.fastNextButton, page.fastPrevButton, true);
    }

    @Test
    public void testNumberOfPagesButtons() {

        try {
            page.firstPageButtonDis.isDisplayed();
        } catch (NoSuchElementException ignored) {
            Graphene.guardAjax(page.firstPageButton).click();
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

        WebElement checkingButton = webDriver
                .findElement(ByJQuery.jquerySelector("a[class*='" + page.CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:contains('"
                        + numberOfPage + "'):first"));

        Car carBeforeClicking = retrieveCarFromRow(page.firstRowOfTable, 0, 4);

        Graphene.guardAjax(checkingButton).click();

        Car carAfterClicking = retrieveCarFromRow(page.firstRowOfTable, 0, 4);

        assertFalse(carBeforeClicking.equals(carAfterClicking), "The data should be different on the different pages!");

        int actualCurrentNumberOfPage = page.getNumberOfCurrentPage();

        assertEquals(actualCurrentNumberOfPage, numberOfPage, "We should be on the " + numberOfPage + ". page");
    }

    /**
     * Tests fast and normal buttons
     *
     * @param nextButton
     * @param previousButton
     */
    private void testFastAndNormalButtons(WebElement nextButton, WebElement previousButton, boolean fast) {
        // starting on the first page
        int numberOfPageAtBeginning = page.getNumberOfCurrentPage();
        Car carBeforeClick = retrieveCarFromRow(page.firstRowOfTable, 0, 4);
        // click on the next button
        Graphene.guardAjax(nextButton).click();
        Car carAfterClick = retrieveCarFromRow(page.firstRowOfTable, 0, 4);
        // check
        //  -- car
        Assert.assertNotEquals(carBeforeClick, carAfterClick, "The data from table should be different, "
            + "when clicking on the on the " + (fast ? "fast " : "") + "next button");
        //  -- page
        int numberOfPageAfterClickOnNext = page.getNumberOfCurrentPage();
        assertEquals(numberOfPageAtBeginning+1, numberOfPageAfterClickOnNext,
                "Previous button or " + (fast ? "fast " : "") + " previous button does not work");
        // click on the previous button
        Graphene.guardAjax(previousButton).click();
        // check
        //  -- car
        carAfterClick = retrieveCarFromRow(page.firstRowOfTable, 0, 4);
        assertEquals(carBeforeClick, carAfterClick, "The data from table should be the same as in the beginning, "
            + "when clicking on the on " + (fast ? "fast " : "") + " the previous");
        //  -- page
        int currentNumberOfPage = page.getNumberOfCurrentPage();
        assertEquals(currentNumberOfPage, numberOfPageAtBeginning, "The " + (fast ? "fast " : "") + " previous button does not work");
    }

}
