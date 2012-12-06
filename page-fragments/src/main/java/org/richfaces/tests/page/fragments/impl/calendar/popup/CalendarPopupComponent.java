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

import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;
import org.richfaces.tests.page.fragments.impl.calendar.popup.popup.CalendarPopup;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface CalendarPopupComponent extends VisibleComponent {

    public enum OpenedBy {

        INPUT_CLICKING,
        OPEN_BUTTON_CLICKING,;
    }

    void closePopup();

    void closePopup(OpenedBy by);

    WebElement getInput();

    String getInputValue();

    Locations getLocations();

    /**
     * Returns popup component without opening it.
     */
    CalendarPopup getPopup();

    WebElement getPopupButton();

    /**
     * Returns proxy for CalendarPopup. The proxy will always open popup if needed.
     */
    CalendarPopup getProxiedPopup();

    /**
     * Returns proxy for CalendarPopup. The proxy will always open popup if needed.
     * @param by the popup will be opened by
     */
    CalendarPopup getProxiedPopup(final OpenedBy by);

    /**
     * Opens popup if needed.
     */
    CalendarPopup openPopup();

    /**
     * Opens popup if needed.
     * @param by the popup will be opened by
     */
    CalendarPopup openPopup(OpenedBy by);

    void setDateTime(DateTime dt);
}
