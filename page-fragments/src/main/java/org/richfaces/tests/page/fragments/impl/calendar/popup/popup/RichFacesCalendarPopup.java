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
package org.richfaces.tests.page.fragments.impl.calendar.popup.popup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.DayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.RichFacesCalendarEditor;
import org.richfaces.tests.page.fragments.impl.calendar.inline.RichFacesCalendarInlineComponent;
import org.richfaces.tests.page.fragments.impl.calendar.popup.RichFacesCalendarPopupComponent;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesCalendarPopup extends RichFacesCalendarInlineComponent implements CalendarPopup {

    private RichFacesCalendarPopupComponent calendar;
    //
    @FindBy(css = "td[id$=Footer]")
    private RichFacesPopupFooterControls popupFooterControls;
    @FindBy(css = "td[id$=Header]")
    private RichFacesPopupHeaderControls popupHeaderControls;

    @Override
    public RichFacesPopupFooterControls getFooterControls() {
        popupFooterControls.setCalendarEditor(calendarEditor);
        return popupFooterControls;
    }

    @Override
    public PopupHeaderControls getHeaderControls() {
        popupHeaderControls.setCalendarEditor(calendarEditor);
        return popupHeaderControls;
    }

    @Override
    public DayPicker getProxiedDayPicker() {
        return (DayPicker) Proxy.newProxyInstance(DayPicker.class.getClassLoader(),
                new Class[]{ DayPicker.class }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(calendar.openPopup().getDayPicker(), args);
            }
        });
    }

    @Override
    public PopupFooterControls getProxiedFooterControls() {
        return (PopupFooterControls) Proxy.newProxyInstance(PopupFooterControls.class.getClassLoader(),
                new Class[]{ PopupFooterControls.class }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(calendar.openPopup().getFooterControls(), args);
            }
        });
    }

    @Override
    public PopupHeaderControls getProxiedHeaderControls() {
        return (PopupHeaderControls) Proxy.newProxyInstance(PopupHeaderControls.class.getClassLoader(),
                new Class[]{ PopupHeaderControls.class }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(calendar.openPopup().getHeaderControls(), args);
            }
        });
    }

    public void setCalendar(RichFacesCalendarPopupComponent calendar) {
        this.calendar = calendar;
    }

    public void setCalendarEditor(RichFacesCalendarEditor calendarEditor) {
        this.calendarEditor = calendarEditor;
    }

    @Override
    public void setDateTime(DateTime dt) {
        super.setDateTime(dt);
        if (Graphene.element(getFooterControls().getApplyButtonElement()).isVisible().apply(driver)) {
            getFooterControls().getApplyButtonElement().click();
        }
    }
}
