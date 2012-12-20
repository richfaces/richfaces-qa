/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth;

import java.util.List;
import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface YearAndMonthEditor {

    /**
     * Clicks on 'Cancel' button.  Waits for editor to close.
     */
    void cancelDate();

    /**
     * Clicks on 'OK' button. Waits for editor to close.
     */
    void confirmDate();

    WebElement getCancelButtonElement();

    /**
     * Returns DateTime instance with current month and year selection.
     */
    DateTime getDate();

    /**
     * Collects all numbers of visible years for selection.
     */
    List<Integer> getDisplayedYears();

    WebElement getNextDecadeButtonElement();

    WebElement getOkButtonElement();

    WebElement getPreviousDecadeButtonElement();

    /**
     * Returns selected month number. <1;12>
     */
    Integer getSelectedMonth();

    /**
     * Returns selected year number.
     */
    Integer getSelectedYear();

    /**
     * Collects all short labels of months.
     */
    List<String> getShortMonthsLabels();

    /**
     * Condition for not-visibility of calendar's year and month editor popup.
     */
    ExpectedCondition<Boolean> isNotVisibleCondition();

    /**
     * Checks if calendar's year and month editor's popup is visible.
     */
    boolean isVisible();

    /**
     * Condition for visibility of calendar's year and month editor popup.
     */
    ExpectedCondition<Boolean> isVisibleCondition();

    /**
     * Clicks on nextDecadeButton '>>>'. Waits for change.
     */
    void nextDecade();

    /**
     * Clicks on previousDecadeButton '<<<'.  Waits for change.
     */
    void previousDecade();

    /**
     * Selects a month and year in this editor. Returns same instance to be easily
     * confirmed or canceled. Waits for change.
     * @param date date with year and month to be set
     * @return
     */
    RichFacesYearAndMonthEditor selectDate(DateTime date);
}
