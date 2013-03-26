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
package org.richfaces.tests.page.fragments.impl.input.inplace;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.input.inplace.InplaceComponent.State;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractInplaceComponent<T extends EditingState> implements InplaceComponent {

    @Root
    protected WebElement root;
    @Drone
    protected WebDriver driver;
    @FindBy(css = "span[id$=Label]")
    protected WebElement label;
    @FindBy(css = "span[id$=Edit] > input[id$=Input]")
    protected WebElement editInputElement;
    @FindBy(css = "span[id$=Edit] span[id$=Btn]")
    private RichFacesInplaceComponentControls controls;

    @Override
    public T editBy(OpenBy event) {
        Utils.triggerJQ(event.getEventName(), root);
        return instantiateFragment();
    }

    @Override
    public InplaceComponentControls getControls() {
        return controls;
    }

    protected abstract String getCssClassForState(State state);

    public WebElement getEditInputElement() {
        return editInputElement;
    }

    @Override
    public String getEditValue() {
        return editInputElement.getAttribute("value").trim();
    }

    protected abstract T instantiateFragment();

    public WebElement getLabelInputElement() {
        return label;
    }

    @Override
    public String getLabelValue() {
        return label.getText().trim();
    }

    @Override
    public WebElement getRootElement() {
        return root;
    }

    @Override
    public boolean is(State state) {
        return root.getAttribute("class").contains(getCssClassForState(state));
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
