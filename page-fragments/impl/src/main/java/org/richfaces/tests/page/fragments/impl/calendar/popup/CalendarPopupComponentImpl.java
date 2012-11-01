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
package org.richfaces.tests.page.fragments.impl.calendar.popup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.CalendarEditor;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopup;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopupImpl;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CalendarPopupComponentImpl implements CalendarPopupComponent {

    @Root
    private WebElement root;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    //
    @FindBy(className = "rf-cal-inp")
    private WebElement input;
    @FindBy(className = "rf-cal-btn")
    private WebElement popupButton;
    @FindBy(css = "table[id$=Content]")
    private CalendarPopupImpl calendarPopup;
    @FindBy(css = "table[id$=Editor]")
    private CalendarEditor calendarEditor;

    private CalendarPopupImpl _openPopup(OpenedBy by) {
        if (calendarPopup.isVisible()) {
            return calendarPopup;
        }
        switch (by) {
            case inputClicking:
                if (Graphene.element(input).not().isVisible().apply(driver)) {
                    throw new RuntimeException("input is not displayed");
                }
                input.click();
                Graphene.waitGui().until(calendarPopup.isVisibleCondition());
                break;
            case openButtonClicking:
                if (Graphene.element(popupButton).not().isVisible().apply(driver)) {
                    throw new RuntimeException("popup button is not displayed");
                }
                popupButton.click();
                Graphene.waitGui().until(calendarPopup.isVisibleCondition());
                break;
            default:
                throw new IllegalArgumentException();
        }
        return calendarPopup;
    }

    @Override
    public void closePopup() {
        closePopup(OpenedBy.openButtonClicking);
    }

    @Override
    public void closePopup(OpenedBy by) {
        if (calendarPopup.isVisible()) {
            switch (by) {
                case inputClicking:
                    input.click();
                    break;
                case openButtonClicking:
                    popupButton.click();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        Graphene.waitGui().until(calendarPopup.isNotVisibleCondition());
    }

    @Override
    public WebElement getInput() {
        return input;
    }

    @Override
    public String getInputValue() {
        return input.getAttribute("value");
    }

    @Override
    public Locations getLocations() {
        return Utils.getLocations(root);
    }

    @Override
    public CalendarPopup getPopup() {
        calendarPopup.setCalendar(this);
        calendarPopup.setCalendarEditor(calendarEditor);
        return calendarPopup;
    }

    @Override
    public WebElement getPopupButton() {
        return popupButton;
    }

    @Override
    public CalendarPopup getProxiedPopup(final OpenedBy by) {
        return (CalendarPopup) Proxy.newProxyInstance(CalendarPopupImpl.class.getClassLoader(),
                new Class[]{ CalendarPopup.class }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(openPopup(by), args);
            }
        });
    }

    @Override
    public CalendarPopup getProxiedPopup() {
        return getProxiedPopup(OpenedBy.inputClicking);
    }

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
    public CalendarPopup openPopup(OpenedBy by) {
        CalendarPopupImpl popup = _openPopup(by);
        popup.setCalendarEditor(calendarEditor);
        popup.setCalendar(this);
        return popup;
    }

    @Override
    public CalendarPopup openPopup() {
        return openPopup(OpenedBy.inputClicking);
    }

    @Override
    public void setDateTime(DateTime dt) {
        openPopup().setDateTime(dt);
    }
}
