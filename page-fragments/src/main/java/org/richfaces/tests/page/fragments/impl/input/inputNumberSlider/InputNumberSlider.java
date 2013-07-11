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
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface InputNumberSlider extends VisibleComponent {

    /**
     * Decreases the value by the component's arrows.
     * @throws RuntimeException if the arrows are not displayed.
     */
    void decreaseWithArrows();

    WebElement getArrowDecrease();

    WebElement getArrowIncrease();

    TextInputComponentImpl getInput();

    /**
     * Returns maximum value to which can be this input number slider set to.
     * @throws RuntimeException if the maximum is not displayed
     */
    int getMaximum();

    WebElement getMaximumElement();

    /**
     * Returns minimum value to which can be this input number slider set to.
     * @throws RuntimeException if the minimum is not displayed
     */
    int getMinimum();

    WebElement getMinimumElement();

    WebElement getRoot();

    WebElement getTooltipElement();

    /**
     * Increases the value by the component's arrows.
     * @throws RuntimeException if the arrows are not displayed.
     */
    void increaseWithArrows();

    boolean isHorizontalOriented();

    /**
     * Set slider's orientation. Default is horizontal.
     * @param isHorizontal when true then the slider has horizontal orientation.
     */
    void setOrientation(boolean isHorizontal);

    /**
     * Returns the slider component.
     */
    SliderComponent slider();
}
