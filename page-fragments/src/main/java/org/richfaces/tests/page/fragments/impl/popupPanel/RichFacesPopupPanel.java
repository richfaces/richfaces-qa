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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;

public class RichFacesPopupPanel implements PopupPanel {

    @Root
    private WebElement rootElement;
    @FindBy(css = "div.rf-pp-hndlr-br")
    private WebElement resizerBottomRight;
    @FindBy(css = "div.rf-pp-hdr")
    private WebElement headerElement;
    //
    private WebDriver driver = GrapheneContext.getProxy();

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
    public WebElement getRootElement() {
        return rootElement;
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
    public PopupPanel resize(int byXPixels, int byYPixels) {
        if (isNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("Popup is not visible, cannot interact with it.");
        }
        new Actions(driver).dragAndDropBy(resizerBottomRight, byXPixels, byYPixels).perform();
        return this;
    }
}
