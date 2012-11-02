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
package org.richfaces.tests.page.fragments.impl.calendar.common;

import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth.YearAndMonthEditor;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface HeaderControls extends VisibleComponent {

    WebElement getNextMonthElement();

    WebElement getNextYearElement();

    WebElement getPreviousMonthElement();

    WebElement getPreviousYearElement();

    /**
     * Returns year and month visible in this component.
     */
    DateTime getYearAndMonth();

    /**
     * Returns Year and month editor without opening it.
     */
    YearAndMonthEditor getYearAndMonthEditor();

    WebElement getYearAndMonthEditorOpenerElement();

    /**
     * Clicks on nextMonrh button with waiting.
     */
    void nextMonth();

    /**
     * Clicks on nextYear button with waiting.
     */
    void nextYear();

    /**
     * Opens YearAndMonthEditor with waiting.
     */
    YearAndMonthEditor openYearAndMonthEditor();

    /**
     * Clicks on previousMonth button with waiting.
     */
    void previousMonth();

    /**
     * Clicks on previousYear button with waiting.
     */
    void previousYear();
}
