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
import org.jodah.typetools.TypeResolver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.panel.AbstractPanel;
import org.richfaces.tests.page.fragments.impl.popupPanel.PopupPanel.ResizerLocation;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class RichFacesPopupPanel<HEADER, HEADERCONTROLS, BODY> extends AbstractPanel<HEADER, BODY> implements PopupPanel<HEADER, BODY> {

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

    private final AdvancedInteractions interactions = new AdvancedInteractions();

    public AdvancedInteractions advanced() {
        return interactions;
    }

    public HEADERCONTROLS getHeaderControlsContent() {
        Class<HEADERCONTROLS> containerClass = (Class<HEADERCONTROLS>) TypeResolver.resolveRawArguments(RichFacesPopupPanel.class, getClass())[1];
        return Graphene.createPageFragment(containerClass, getBodyElement());
    }

    @Override
    protected WebElement getBodyElement() {
        return contentElement;
    }

    @Override
    protected WebElement getHeaderElement() {
        return headerElement;
    }

    public class AdvancedInteractions {

        public WebElement getBodyElement() {
            return contentElement;
        }

        public WebElement getContentElement() {
            return contentElement;
        }

        public WebElement getContentScrollerElement() {
            return contentScrollerElement;
        }

        public WebElement getHeaderContentElement() {
            return headerContentElement;
        }

        public WebElement getHeaderControlsElement() {
            return headerControlsElement;
        }

        public WebElement getHeaderElement() {
            return headerElement;
        }

        public Locations getLocations() {
            return Utils.getLocations(RichFacesPopupPanel.this.getRootElement());
        }

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

        public WebElement getRootElement() {
            return RichFacesPopupPanel.this.getRootElement();
        }

        public WebElement getShadowElement() {
            return shadowElement;
        }

        public AdvancedInteractions moveByOffset(int xOffset, int yOffset) {
            new Actions(driver).dragAndDropBy(headerElement, xOffset, yOffset).perform();
            return this;
        }

        public AdvancedInteractions resizeFromLocation(ResizerLocation location, int byXPixels, int byYPixels) {
            new Actions(driver).dragAndDropBy(getResizerElement(location), byXPixels, byYPixels).perform();
            return this;
        }

        public void waitUntilIsNotVisible() {
            Graphene.waitModel().until().element(getRootElement()).is().not().visible();
        }

        public void waitUntilIsVisible() {
            Graphene.waitModel().until().element(getRootElement()).is().visible();
        }
    }
}
