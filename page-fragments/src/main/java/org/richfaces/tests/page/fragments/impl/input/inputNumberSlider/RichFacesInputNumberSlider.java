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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesInputNumberSlider implements VisibleComponent, InputNumberSlider {

    @Root
    private WebElement root;
    //
    @FindBy(css = "span.rf-insl-inp-cntr > input.rf-insl-inp")
    TextInputComponentImpl input;
    @FindBy(css = "span.rf-insl-trc")
    RichFacesSliderComponent numberSlider;
    //
    @FindBy(className = "rf-insl-inc")
    WebElement arrowIncrease;
    @FindBy(className = "rf-insl-dec")
    WebElement arrowDecrease;
    @FindBy(className = "rf-insl-mn")
    WebElement min;
    @FindBy(className = "rf-insl-mx")
    WebElement max;
    @FindBy(className = "rf-insl-tt")
    WebElement tooltip;

    @Drone
    WebDriver driver;

    @Override
    public void decreaseWithArrows() {
        if (Graphene.element(arrowDecrease).not().isVisible().apply(driver)) {
            throw new RuntimeException("arrow for decreasing value is not visible.");
        }
        arrowDecrease.click();
    }

    @Override
    public WebElement getArrowDecrease() {
        return arrowDecrease;
    }

    @Override
    public WebElement getArrowIncrease() {
        return arrowIncrease;
    }

    @Override
    public TextInputComponentImpl getInput() {
        return input;
    }

    @Override
    public int getMaximum() {
        if (Graphene.element(max).not().isVisible().apply(driver)) {
            throw new RuntimeException("Maximum is not visible.");
        }
        return Integer.parseInt(max.getText());
    }

    @Override
    public WebElement getMaximumElement() {
        return max;
    }

    @Override
    public int getMinimum() {
        if (Graphene.element(min).not().isVisible().apply(driver)) {
            throw new RuntimeException("Minimum iss not visible.");
        }
        return Integer.parseInt(min.getText());
    }

    @Override
    public WebElement getMinimumElement() {
        return min;
    }

    @Override
    public SliderComponent getNumberSlider() {
        return numberSlider;
    }

    @Override
    public WebElement getRoot() {
        return root;
    }

    @Override
    public WebElement getTooltipElement() {
        return tooltip;
    }

    @Override
    public void increaseWithArrows() {
        if (Graphene.element(arrowIncrease).not().isVisible().apply(driver)) {
            throw new RuntimeException("Arrow for increasing value is not visible.");
        }
        arrowIncrease.click();
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
}
