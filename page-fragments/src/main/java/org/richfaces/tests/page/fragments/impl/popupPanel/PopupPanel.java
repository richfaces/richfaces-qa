/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.popupPanel;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface PopupPanel extends VisibleComponent {

    /**
     * Returns locations of this popup panel.
     *
     * @throws RuntimeException when popup is not visible
     */
    Locations getLocations();

    /**
     * Moves panel by dragging it with header. Returns same instance.
     *
     * @throws MovementOutOfBoundsException when WebDriver cannot scroll to view
     * @throws RuntimeException when popup is not visible
     */
    PopupPanel moveByOffset(int xOffset, int yOffset);

    /**
     * Resizes panel by resize handle in bottom-right corner. Returns same instance.
     *
     * @throws MovementOutOfBoundsException when WebDriver cannot scroll to view
     * @throws RuntimeException when popup is not visible
     */
    PopupPanel resize(int byXPixels, int byYPixels);

    WebElement getHeaderElement();

    WebElement getRootElement();
}
