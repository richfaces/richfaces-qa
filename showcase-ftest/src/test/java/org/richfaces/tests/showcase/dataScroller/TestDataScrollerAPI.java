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
package org.richfaces.tests.showcase.dataScroller;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.dataScroller.page.DataScrollerAPIPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestDataScrollerAPI extends AbstractWebDriverTest {

    @Page
    private DataScrollerAPIPage page;


    /* *************************************************************************************************************************
     * Tests
     * *************************************************************************************************************
     * *************
     */

    @Test
    public void testNumberOfPagesButtons() {

        checkNumberOfPagesButtons(1);

        checkNumberOfPagesButtons(2);

        checkNumberOfPagesButtons(3);

    }

    @Test
    public void testAPINextPrevious() {

        int currentNumberOfThePage = page.getNumberOfCurrentPage();

        if (currentNumberOfThePage > 1) {
            Graphene.guardXhr(page.previousButton).click();
            Graphene.guardXhr(page.previousButton).click();
        }

        String srcBeforeClicking = getSrcOfFirstImage();

        Graphene.guardXhr(page.nextButton).click();

        String srcAfterClicking = getSrcOfFirstImage();

        assertFalse(srcBeforeClicking.equals(srcAfterClicking), "The data should be different on he different pages!");

        int numberOfThePageAfterClicking = page.getNumberOfCurrentPage();

        assertEquals(numberOfThePageAfterClicking, currentNumberOfThePage + 1,
            "The current number of the page should be higher");

        Graphene.guardXhr(page.previousButton).click();

        numberOfThePageAfterClicking = page.getNumberOfCurrentPage();

        assertEquals(numberOfThePageAfterClicking, currentNumberOfThePage,
            "The current number of the page should be less");
    }

    /* ***************************************************************************************************************************************
     * Help methods
     * ******************************************************************************************************
     * **********************************
     */

    /**
     * Checking the buttons which have number of pages
     */
    private void checkNumberOfPagesButtons(int numberOfPage) {

        String imgSrcBeforeClick = null;

        try {
            WebElement checkingButton = webDriver
                    .findElement(ByJQuery.jquerySelector("a[class*='" + page.CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:contains('"
                            + numberOfPage + "')"));
            imgSrcBeforeClick = getSrcOfFirstImage();
            Graphene.guardXhr(checkingButton).click();
        } catch (NoSuchElementException ignored) {
            WebElement inactiveButton = webDriver
                    .findElement(ByJQuery.jquerySelector("a[class*='" + page.CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:first"));
            imgSrcBeforeClick = getSrcOfFirstImage();
            Graphene.guardXhr(inactiveButton).click();
            numberOfPage = page.getNumberOfCurrentPage();
        }

        String imgSrcAfterClick = getSrcOfFirstImage();

        assertFalse(imgSrcAfterClick.equals(imgSrcBeforeClick), "The data should be different on the different pages!");

        int actualCurrentNumberOfPage = page.getNumberOfCurrentPage();

        assertEquals(actualCurrentNumberOfPage, numberOfPage, "We should be on the " + numberOfPage + ". page");
    }

    /**
     * Gets the src attribute of the first image on the page
     */
    private String getSrcOfFirstImage() {
        return page.firstImgOnThePage.getAttribute("src");
    }

}
