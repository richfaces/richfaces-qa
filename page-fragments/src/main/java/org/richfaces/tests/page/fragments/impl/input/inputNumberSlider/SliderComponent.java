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
     * Decrease value by pressing LEFT/DOWN keyboard key.
     */
    void decrease();

    WebElement getDisabledHandleElement();

    WebElement getHandleElement();

    /**
     * Returns the height of the trace.
     */
    int getHeight();

    WebElement getRoot();

    WebElement getTrackElement();

    /**
     * Returns the width of the trace.
     */
    int getWidth();

    /**
     * Increase value by pressing RIGHT/UP keyboard key.
     */
    void increase();

    /**
     * Scrolls to view and moves the handle to a pixel in trace. From 0 (left/down) to traces width or height, depending on the slider orientation.
     * @see SliderComponent#getWidth()
     * @see SliderComponent#getHeight()
     * @throws RuntimeException if the trace is not visible
     * @throws IllegalArgumentException when the pixelInTrace is out of interval &lt;0,getWidth()/getHeight&gt;.
     */
    void dragHandleToPointInTrace(int pixelInTrace);

    /**
     * Scrolls to view and moves the handle to a percentage of the trace's size.
     * Possible values interval is &lt;0,1&gt;.
     * @throws RuntimeException if the trace is not visible
     * @throws IllegalArgumentException when the percentage is out of interval &lt;0,1&gt;.
     */
    void dragHandleToPointInTrace(double percentageOfTracesSize);

    /**
     * Scrolls to view and drags the handle to left/up(-value) or right/down(+value) by value in pixels.
     */
    void slideWithHandle(int byPixels);
}
