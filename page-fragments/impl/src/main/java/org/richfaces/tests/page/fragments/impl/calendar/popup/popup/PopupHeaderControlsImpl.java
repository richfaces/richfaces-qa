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

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.calendar.common.HeaderControlsImpl;

/**
 * Component for header controls of calendar.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class PopupHeaderControlsImpl extends HeaderControlsImpl implements PopupHeaderControls {

    @FindBy(xpath = "//td[contains(@id,'calendarHeader')] //td[6] /div")
    private WebElement closeButtonElement;

    @Override
    public void closePopup() {
        if (!isVisible() || Graphene.element(closeButtonElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Cannot interact with close button. "
                    + "Ensure that calendar popup and header controls are displayed.");
        }
        closeButtonElement.click();
    }

    @Override
    public WebElement getCloseButtonElement() {
        return closeButtonElement;
    }
}
