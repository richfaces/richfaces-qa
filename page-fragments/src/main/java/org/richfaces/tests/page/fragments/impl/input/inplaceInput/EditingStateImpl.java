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

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class EditingStateImpl implements EditingState {

    private final WebElement input;
    private final Controls controls;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    private Actions action = new Actions(driver);

    public EditingStateImpl(WebElement input, Controls controls) {
        this.input = input;
        this.controls = controls;
    }

    @Override
    public void cancel() {
        cancel(FinishEditingBy.KEYS);
    }

    @Override
    public void cancel(FinishEditingBy by) {
        switch (by) {
            case CONTROLS:
                cancelByControls();
                break;
            case KEYS:
                cancelByKeys();
                break;
            default:
                throw new UnsupportedOperationException("Unknown switch.");
        }
    }

    private void cancelByKeys() {
        input.sendKeys(Keys.chord(Keys.CONTROL, Keys.ESCAPE));
    }

    private void cancelByControls() {
        getControls().cancel();
    }

    @Override
    public void confirm() {
        confirm(FinishEditingBy.KEYS);
    }

    @Override
    public void confirm(FinishEditingBy by) {
        switch (by) {
            case CONTROLS:
                confirmByControls();
                break;
            case KEYS:
                confirmByKeys();
                break;
            default:
                throw new UnsupportedOperationException("Unknown switch.");
        }
    }

    private void confirmByKeys() {
        action.sendKeys(Keys.RETURN).build().perform();
    }

    private void confirmByControls() {
        getControls().ok();
    }

    @Override
    public Controls getControls() {
        return controls;
    }

    @Override
    public EditingState type(String newValue) {
        input.clear();
        input.sendKeys(newValue);
        return this;
    }
}
