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
package org.richfaces.tests.page.fragments.impl.inputNumberSlider;

import com.google.common.base.Preconditions;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.Utils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesSliderComponent implements SliderComponent {

    @Root
    private WebElement root;

    @FindBy(className = "rf-insl-hnd-cntr")
    private WebElement handleContainer;
    @FindBy(className = "rf-insl-hnd")
    private WebElement handle;
    @FindBy(className = "rf-insl-hnd-dis")
    private WebElement disabledHandle;

    private boolean isHorizontalOriented;// handled by parent

    @Drone
    private WebDriver driver;

    @Override
    public void decrease() {
        new Actions(driver).sendKeys(handle, (isHorizontalOriented ? Keys.LEFT : Keys.DOWN)).build().perform();
    }

    @Override
    public WebElement getDisabledHandleElement() {
        return disabledHandle;
    }

    @Override
    public WebElement getHandleElement() {
        return handle;
    }

    @Override
    public int getHeight() {
        return Utils.getLocations(handleContainer).getHeight();
    }

    @Override
    public WebElement getRoot() {
        return root;
    }

    @Override
    public WebElement getTrackElement() {
        return root;
    }

    @Override
    public int getWidth() {
        return Utils.getLocations(handleContainer).getWidth();
    }

    @Override
    public void increase() {
        new Actions(driver).sendKeys(handle, (isHorizontalOriented ? Keys.RIGHT : Keys.UP)).build().perform();
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
    public void dragHandleToPointInTrace(int pixelInTrace) {
        Preconditions.checkArgument(pixelInTrace >= 0 && pixelInTrace <= (isHorizontalOriented ? getWidth() : getHeight()), "Cannot slide outside the trace.");
        if (!isVisible()) {
            throw new RuntimeException("Trace is not visible.");
        }
        scrollToView();
        Actions actions = new Actions(driver).clickAndHold(handle);
        if (isHorizontalOriented) {
            actions.moveToElement(root, pixelInTrace, 0);
        } else {
            actions.moveToElement(root, 0, pixelInTrace);
        }
        actions.release(handle).build().perform();
    }

    @Override
    public void dragHandleToPointInTrace(double percentageOfTracecSize) {
        dragHandleToPointInTrace((int) (percentageOfTracecSize * (isHorizontalOriented ? getWidth() : getHeight())));
    }

    private void scrollToView() {
        new Actions(driver).moveToElement(root).perform();
    }

    public void setOrientation(boolean isHorizontalOriented) {
        this.isHorizontalOriented = isHorizontalOriented;
    }

    @Override
    public void slideWithHandle(int byPixels) {
        if (isHorizontalOriented) {
            new SlideAction(0, byPixels).perform();
        } else {
            new SlideAction(byPixels, 0).perform();
        }
    }

    private class SlideAction implements Action {

        final int offsetUpDown;
        final int offsetLeftRight;

        public SlideAction(int offsetUpDown, int offsetLeftRight) {
            this.offsetUpDown = offsetUpDown;
            this.offsetLeftRight = offsetLeftRight;
        }

        @Override
        public void perform() {
            scrollToView();
            new Actions(driver).clickAndHold(handle).moveByOffset(offsetLeftRight, offsetUpDown).release().build().perform();
        }
    }
}
