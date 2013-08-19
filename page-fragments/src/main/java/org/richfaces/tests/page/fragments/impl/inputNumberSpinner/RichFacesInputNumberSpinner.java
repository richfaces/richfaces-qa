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
package org.richfaces.tests.page.fragments.impl.inputNumberSpinner;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponent;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesInputNumberSpinner implements InputNumberSpinner {

    @Root
    private WebElement root;
    //
    @FindBy(css = "input.rf-insp-inp")
    TextInputComponentImpl input;
    @FindBy(css = "span.rf-insp-btns > span.rf-insp-inc")
    WebElement buttonIncrease;
    @FindBy(css = "span.rf-insp-btns > span.rf-insp-dec")
    WebElement buttonDecrease;

    @Drone
    WebDriver driver;

    @Override
    public void decrease() {
        if (Graphene.element(buttonDecrease).not().isVisible().apply(driver)) {
            throw new RuntimeException("Button for increasing value is not visible.");
        }
        buttonDecrease.click();
    }

    @Override
    public WebElement getButtonDecreaseElement() {
        return buttonDecrease;
    }

    @Override
    public WebElement getButtonIncreaseElement() {
        return buttonIncrease;
    }

    @Override
    public TextInputComponentImpl getInput() {
        return input;
    }

    @Override
    public WebElement getRootElement() {
        return root;
    }

    @Override
    public void increase() {
        if (Graphene.element(buttonIncrease).not().isVisible().apply(driver)) {
            throw new RuntimeException("Button for increasing value is not visible.");
        }
        buttonIncrease.click();
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
