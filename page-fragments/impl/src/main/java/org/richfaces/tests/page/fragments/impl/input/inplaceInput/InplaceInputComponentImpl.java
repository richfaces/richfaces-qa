/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.input.inplaceInput;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Component implementation of rich:inplaceInput
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class InplaceInputComponentImpl implements InplaceInputComponent {

    @Root
    private WebElement root;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    //
    @FindBy(css = "span[id$=Label]")
    private WebElement label;
    @FindBy(css = "span[id$=Edit] > input.rf-ii-fld")
    private WebElement editInputElement;
    @FindBy(css = "span[id$=Edit] span[id$=Btn]")
    private ControlsImpl controls;

    @Override
    public EditingState editBy(OpenBy event) {
        String elementID = root.getAttribute("id");
        String jQueryCmd = String.format("$(\"[id='%s']\").trigger('%s')", elementID, event.eventName);
        ((JavascriptExecutor) driver).executeScript(jQueryCmd);
        // TODO: add a waiting here with exception when input is not in editing state?
        // Graphene.waitGui().until(Graphene.attribute(root, "class").valueContains(State.active.cssClass));
        return new EditingStateImpl(editInputElement, controls);
    }

    @Override
    public ControlsImpl getControls() {
        return controls;
    }

    public WebElement getEditInputElement() {
        return editInputElement;
    }

    @Override
    public WebElement getRoot() {
        return root;
    }

    @Override
    public String getEditValue() {
        return editInputElement.getAttribute("value");
    }

    @Override
    public String getLabelValue() {
        return label.getText();
    }

    @Override
    public boolean is(State state) {
        return root.getAttribute("class").contains(state.cssClass);
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
