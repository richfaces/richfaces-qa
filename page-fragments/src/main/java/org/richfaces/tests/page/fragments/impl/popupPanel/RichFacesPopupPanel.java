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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class RichFacesPopupPanel<C extends PopupPanelControls, L extends PopupPanelContent> implements PopupPanel<C, L> {

    @Root
    private WebElement rootElement;
    @Drone
    private WebDriver driver;
    @FindBy(css = "div.rf-pp-hndlr-t")
    private WebElement resizerN;
    @FindBy(css = "div.rf-pp-hndlr-r")
    private WebElement resizerE;
    @FindBy(css = "div.rf-pp-hndlr-b")
    private WebElement resizerS;
    @FindBy(css = "div.rf-pp-hndlr-l")
    private WebElement resizerW;
    @FindBy(css = "div.rf-pp-hndlr-tr")
    private WebElement resizerNE;
    @FindBy(css = "div.rf-pp-hndlr-tl")
    private WebElement resizerNW;
    @FindBy(css = "div.rf-pp-hndlr-br")
    private WebElement resizerSE;
    @FindBy(css = "div.rf-pp-hndlr-bl")
    private WebElement resizerSW;
    @FindBy(css = "div.rf-pp-hdr")
    private WebElement headerElement;
    @FindBy(css = "div.rf-pp-hdr-cnt")
    private WebElement headerContentElement;
    @FindBy(css = "div.rf-pp-hdr-cntrls")
    private WebElement headerControlsElement;
    @FindBy(css = "div.rf-pp-cnt-scrlr")
    private WebElement contentScrollerElement;
    @FindBy(css = "div.rf-pp-cnt")
    private WebElement contentElement;
    @FindBy(css = "div.rf-pp-shdw")
    private WebElement shadowElement;

    @Override
    public L content() {
        return Graphene.createPageFragment(getContentType(), contentElement);
    }

    @Override
    public C controls() {
        return Graphene.createPageFragment(getControlsType(), headerControlsElement);
    }

    @Override
    public WebElement getContentElement() {
        return contentElement;
    }

    @Override
    public WebElement getContentScrollerElement() {
        return contentScrollerElement;
    }

    protected abstract Class<L> getContentType();

    protected abstract Class<C> getControlsType();

    @Override
    public WebElement getHeaderContentElement() {
        return headerContentElement;
    }

    @Override
    public WebElement getHeaderControlsElement() {
        return headerControlsElement;
    }

    @Override
    public WebElement getHeaderElement() {
        return headerElement;
    }

    @Override
    public Locations getLocations() {
        if (isNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("Popup is not visible, cannot interact with it.");
        }
        return Utils.getLocations(rootElement);
    }

    @Override
    public WebElement getResizerElement(ResizerLocation resizerLocation) {
        switch (resizerLocation) {
            case N:
                return resizerN;
            case E:
                return resizerE;
            case S:
                return resizerS;
            case W:
                return resizerW;
            case NE:
                return resizerNE;
            case SE:
                return resizerSE;
            case SW:
                return resizerSW;
            case NW:
                return resizerNW;
            default:
                throw new UnsupportedOperationException("Unknown switch " + resizerLocation);
        }
    }

    @Override
    public WebElement getRootElement() {
        return rootElement;
    }

    @Override
    public WebElement getShadowElement() {
        return shadowElement;
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(rootElement).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(rootElement).isVisible();
    }

    @Override
    public PopupPanel moveByOffset(int xOffset, int yOffset) {
        if (isNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("Popup is not visible, cannot interact with it.");
        }
        new Actions(driver).dragAndDropBy(headerElement, xOffset, yOffset).perform();
        return this;
    }

    @Override
    public PopupPanel resizeFromLocation(ResizerLocation location, int byXPixels, int byYPixels) {
        if (isNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("Popup is not visible, cannot interact with it.");
        }
        new Actions(driver).dragAndDropBy(getResizerElement(location), byXPixels, byYPixels).perform();
        return this;
    }
}