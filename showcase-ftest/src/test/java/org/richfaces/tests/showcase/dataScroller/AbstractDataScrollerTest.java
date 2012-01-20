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

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractDataScrollerTest extends AbstractDataIterationWithCars {

    /* *******************************************************************************************************
     * Constants*******************************************************************************************************
     */

    protected final String CLASS_OF_BUTTON_FIRST = "rf-ds-btn rf-ds-btn-first";
    protected final String CLASS_OF_BUTTON_FAST_RWD = "rf-ds-btn rf-ds-btn-fastrwd";
    protected final String CLASS_OF_BUTTON_PREV = "rf-ds-btn rf-ds-btn-prev";
    protected final String CLASS_OF_BUTTON_NEXT = "rf-ds-btn rf-ds-btn-next";
    protected final String CLASS_OF_BUTTON_FAST_FWD = "rf-ds-btn rf-ds-btn-fastfwd";
    protected final String CLASS_OF_BUTTON_LAST = "rf-ds-btn rf-ds-btn-last";
    protected final String CLASS_OF_BUTTON_DIS = "rf-ds-dis";

    protected final String CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER = "rf-ds-nmb-btn";
    protected final String CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER = CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + " rf-ds-act";

    /* ***********************************************************************************************************
     * Locators
     * ***********************************************************************************************************
     */

    protected JQueryLocator nextButton = jq("a[class='" + CLASS_OF_BUTTON_NEXT + "']");
    protected JQueryLocator nextButtonDis = jq("span[class='" + CLASS_OF_BUTTON_NEXT + " " + CLASS_OF_BUTTON_DIS + "']");

    protected JQueryLocator previousButton = jq("a[class='" + CLASS_OF_BUTTON_PREV + "']");
    protected JQueryLocator previousButtonDis = jq("span[class='" + CLASS_OF_BUTTON_PREV + " " + CLASS_OF_BUTTON_DIS
        + "']");

    protected JQueryLocator firstPageButton = jq("a[class='" + CLASS_OF_BUTTON_FIRST + "']");
    protected JQueryLocator firstPageButtonDis = jq("span[class='" + CLASS_OF_BUTTON_FIRST + " " + CLASS_OF_BUTTON_DIS
        + "']");

    protected JQueryLocator lastPageButton = jq("a[class='" + CLASS_OF_BUTTON_LAST + "']");
    protected JQueryLocator lastPageButtonDis = jq("span[class='" + CLASS_OF_BUTTON_LAST + " " + CLASS_OF_BUTTON_DIS
        + "']");

    protected JQueryLocator fastPrevButton = jq("a[class='" + CLASS_OF_BUTTON_FAST_RWD + "']");
    protected JQueryLocator fastPrevButtonDis = jq("span[class='" + CLASS_OF_BUTTON_FAST_RWD + " "
        + CLASS_OF_BUTTON_DIS + "']");

    protected JQueryLocator fastNextButton = jq("a[class='" + CLASS_OF_BUTTON_FAST_FWD + "']");
    protected JQueryLocator fastNextButtonDis = jq("span[class='" + CLASS_OF_BUTTON_FAST_FWD + " "
        + CLASS_OF_BUTTON_DIS + "']");

    protected JQueryLocator buttonWithNumberOfPageActive = jq("span[class*='" + CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER
        + "']");

    /* *********************************************************************************************************************************
     * Help methods
     * ******************************************************************************************************
     * *****************************
     */

    /**
     * Gets the number of the current page
     *
     * @return number of the current page
     */
    protected int getNumberOfCurrentPage() {

        String currentPage = selenium.getText(buttonWithNumberOfPageActive).trim();
        int currentPageInt = Integer.valueOf(currentPage).intValue();

        return currentPageInt;
    }
}
