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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richDataScroller;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.model.Car;
import org.richfaces.tests.showcase.ftest.webdriver.page.richDataScroller.SimpleScrollingPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichDataScrollerSimple extends AbstractWebDriverTest<SimpleScrollingPage> {

    private static final Car FIRST_CAR_FIRST_PAGE = new Car("Chevrolet", "Corvette");
    private static final Car FIRST_CAR_SECOND_PAGE = new Car("Chevrolet", "Malibu");
    private static final Car LAST_CAR_FIRST_PAGE = new Car("Chevrolet", "Malibu");
    private static final Car LAST_CAR_SECOND_PAGE = new Car("Ford", "Taurus");
    private static final Car ONLY_CAR_LAST_PAGE = new Car("Infiniti", "EX35");

    @Test
    public void testInit() {
        testFirstPage();
    }

    @Test(groups = {"RF-12146"})
    public void testNextAndPreviousPage() {
        hackScrolling();
        // move to the second page
        getPage().next();
        // move back to the first page
        getPage().previous();
        // test
        testFirstPage();
    }

    @Test(groups = {"RF-12146"})
    public void testNextPage() {
        hackScrolling();
        // move to the second page
        getPage().next();
        // current page number
        assertEquals(getPage().getNumberOfCurrentPage(), 2, "The current page number should be <2>.");
        // page numbers
        for(int page=1; page<=5; page++) {
            assertTrue(getPage().isPageNumberPresent(page), "The page number <" + page + "> should be present.");
        }
        // control buttons
        assertFalse(getPage().isFirstPageButtonDisabled(), "The first page button shouldn't be disabled.");
        assertFalse(getPage().isPreviousButtonDisabled(), "The previous button shouldn't be disabled.");
        assertFalse(getPage().isPreviousFastButtonDisabled(), "The fast previous button shouldn't be disabled.");

        assertFalse(getPage().isLastPageButtonDisabled(), "The last page button shouldn't be disabled.");
        assertFalse(getPage().isNextButtonDisabled(), "The next button shouldn't be disabled.");
        assertFalse(getPage().isNextFastButtonDisabled(), "The fast next button shouldn't be disabled.");
        // test table content
        testFirstCar(FIRST_CAR_SECOND_PAGE);
        testLastCar(LAST_CAR_SECOND_PAGE);
    }

    @Test(groups = {"RF-12146"})
    public void testLastAndFirstPage() {
        hackScrolling();
        // move to the last page
        getPage().last();
        // move back to the first page
        getPage().first();
        // test
        testFirstPage();
    }

    @Test(groups = {"RF-12146"})
    public void testLastPage() {
        hackScrolling();
        // HACK: because of the scrolling
        if (getConfiguration().isMobile()) {
            getPage().nextFast();
            getPage().nextFast();
        }
        // move to the last page
        getPage().last();
        // current page number
        assertEquals(getPage().getNumberOfCurrentPage(), 13, "The current page number should be <13>.");
        // page numbers
        for(int page=9; page<=13; page++) {
            assertTrue(getPage().isPageNumberPresent(page), "The page number <" + page + "> should be present.");
        }
        // control buttons
        assertFalse(getPage().isFirstPageButtonDisabled(), "The first page button shouldn't be disabled.");
        assertFalse(getPage().isPreviousButtonDisabled(), "The previous button shouldn't be disabled.");
        assertFalse(getPage().isPreviousFastButtonDisabled(), "The fast previous button shouldn't be disabled.");

        assertTrue(getPage().isLastPageButtonDisabled(), "The last page button should be disabled.");
        assertTrue(getPage().isNextButtonDisabled(), "The next button should be disabled.");
        assertTrue(getPage().isNextFastButtonDisabled(), "The fast next button should be disabled.");

        // test table content
        testFirstCar(ONLY_CAR_LAST_PAGE);
        testLastCar(ONLY_CAR_LAST_PAGE);
    }

    @Override
    protected SimpleScrollingPage createPage() {
        return new SimpleScrollingPage(getWebDriver());
    }

    private void hackScrolling() {
        if (getConfiguration().isMobile()) {
            // HACK: because of the scrolling
            getPage().page(4);
            getPage().first();
        }
    }

    private void testFirstCar(Car expected) {
        assertEquals(getPage().getFirstCar(), expected, "The first car doesn't match.");
    }

    private void testLastCar(Car expected) {
        assertEquals(getPage().getLastCar(), expected, "The last car doesn't match.");
    }

    private void testFirstPage() {
        // current page number
        assertEquals(getPage().getNumberOfCurrentPage(), 1, "The current page number should be <1>.");
        // page numbers
        for(int page=1; page<=5; page++) {
            assertTrue(getPage().isPageNumberPresent(page), "The page number <" + page + "> should be present.");
        }
        // control buttons
        assertTrue(getPage().isFirstPageButtonDisabled(), "The first page button should be disabled.");
        assertTrue(getPage().isPreviousButtonDisabled(), "The previous button should be disabled.");
        assertTrue(getPage().isPreviousFastButtonDisabled(), "The fast previous button should be disabled.");

        assertFalse(getPage().isLastPageButtonDisabled(), "The last page button shouldn't be disabled.");
        assertFalse(getPage().isNextButtonDisabled(), "The next button shouldn't be disabled.");
        assertFalse(getPage().isNextFastButtonDisabled(), "The fast next button shouldn't be disabled.");
        // test table content
        testFirstCar(FIRST_CAR_FIRST_PAGE);
        testLastCar(LAST_CAR_FIRST_PAGE);
    }

}
