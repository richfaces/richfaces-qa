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

import com.google.common.base.Preconditions;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
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
    WebElement root;
    //
    @FindBy(className = "rf-insl-hnd")
    WebElement handle;
    @FindBy(className = "rf-insl-hnd-dis")
    WebElement disabledHandle;
    //
    WebDriver driver = GrapheneContext.getProxy();

    @Override
    public void decrease() {
        new Actions(driver).sendKeys(handle, Keys.LEFT).build().perform();
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
        return Utils.getLocations(root).getHeight();
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
        return Utils.getLocations(root).getWidth();
    }

    @Override
    public void increase() {
        new Actions(driver).sendKeys(handle, Keys.RIGHT).build().perform();
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
    public void moveHandleToPointInTraceHorizontally(int where) {
        Preconditions.checkArgument(where >= 0 && where <= getWidth(), "Cannot slide outside the trace");
        if (!isVisible()) {
            throw new RuntimeException("Trace is not visible.");
        }
        new Actions(driver).clickAndHold(handle).moveToElement(root, where, 0).release(handle).build().perform();
    }

    @Override
    public void moveHandleToPointInTraceVertically(int where) {
        Preconditions.checkArgument(where >= 0 && where <= getHeight(), "Cannot slide outside the trace");
        if (!isVisible()) {
            throw new RuntimeException("Trace is not visible.");
        }
        new Actions(driver).clickAndHold(handle).moveToElement(root, 0, where).release(handle).build().perform();
    }

    @Override
    public void slideLeftRightWithHandle(int byPixels) {
        new SlideAction(0, byPixels).perform();
    }

    @Override
    public void slideUpDownWithHandle(int byPixels) {
        new SlideAction(byPixels, 0).perform();
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
            new Actions(driver).clickAndHold(handle).moveByOffset(offsetLeftRight, offsetUpDown).release().build().perform();
        }
    }
}
