/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.input.inputNumberSlider;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface SliderComponent extends VisibleComponent {

    /**
     * Decrease value by keys.
     */
    void decrease();

    WebElement getDisabledHandleElement();

    WebElement getHandleElement();

    /**
     * Returns the height of the trace
     */
    int getHeight();

    WebElement getRoot();

    WebElement getTrackElement();

    /**
     * Returns the width of the trace
     */
    int getWidth();

    /**
     * Increase value by keys.
     */
    void increase();

    /**
     * Moves the handle to pixel in trace. From 0 (left) to getWidth() (right). Works only with
     * horizontal sliders.
     * @see SliderComponent#getWidth()
     * @throws RuntimeException if the trace is not visible
     */
    void moveHandleToPointInTraceHorizontally(int where);

    /**
     * Moves the handle to pixel in trace. From 0 (top) to getHeight() (bottom). Works only with
     * vertical sliders.
     * @see SliderComponent#getHeight()
     * @throws RuntimeException if the trace is not visible
     */
    void moveHandleToPointInTraceVertically(int where);

    /**
     * Slides the slider with the handle to left(+value) or right(-value) by value in pixels.
     * @see SliderComponent#moveHandleToPointInTrace
     */
    void slideLeftRightWithHandle(int byPixels);

    /**
     * Slides the slider with the handle to top(-value) or bottom(+value) by value in pixels.
     * @see SliderComponent#moveHandleToPointInTrace
     */
    void slideUpDownWithHandle(int byPixels);
}
