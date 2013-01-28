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
package org.richfaces.tests.page.fragments.impl.calendar.inline;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.calendar.common.FooterControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.HeaderControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.RichFacesFooterControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.RichFacesHeaderControls;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.DayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.RichFacesDayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.RichFacesCalendarEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesCalendarInlineComponent implements CalendarInlineComponent {

    @Root
    protected WebElement root;
    //
    protected WebDriver driver = GrapheneContext.getProxy();
    //
    @FindBy(css = "td[id$=Header]")
    protected RichFacesHeaderControls headerControls;
    @FindBy(css = "td[id$=Footer]")
    protected RichFacesFooterControls footerControls;
    @FindBy(css = "table[id$=Content] > tbody")
    protected RichFacesDayPicker dayPicker;
    @FindBy(css = "table[id$=Editor]")
    protected RichFacesCalendarEditor calendarEditor;
    //

    @Override
    public DayPicker getDayPicker() {
        return dayPicker;
    }

    @Override
    public FooterControls getFooterControls() {
        footerControls.setCalendarEditor(calendarEditor);
        footerControls.setDayPicker(dayPicker);
        return footerControls;
    }

    @Override
    public HeaderControls getHeaderControls() {
        headerControls.setCalendarEditor(calendarEditor);
        return headerControls;
    }

    @Override
    public Locations getLocations() {
        return Utils.getLocations(root);
    }

    @Override
    public WebElement getRoot() {
        return root;
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    @Override
    public void setDateTime(DateTime dt) {
        getHeaderControls().openYearAndMonthEditor().selectDate(dt).confirmDate();
        getDayPicker().selectDayInMonth(dt);
        if (Graphene.element(getFooterControls().getTimeEditorOpenerElement()).isVisible().apply(driver)) {
            getFooterControls().openTimeEditor().setTime(dt, TimeEditor.SetValueBy.TYPING).confirmTime();
        }
    }
}
