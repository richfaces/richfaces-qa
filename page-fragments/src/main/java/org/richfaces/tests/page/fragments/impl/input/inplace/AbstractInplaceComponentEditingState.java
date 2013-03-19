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

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractInplaceComponentEditingState implements EditingState {

    protected WebDriver driver = GrapheneContext.getProxy();
    protected final WebElement inplaceComponentRoot;
    protected final WebElement input;
    protected final InplaceComponentControls controls;

    public AbstractInplaceComponentEditingState(WebElement root, WebElement input, InplaceComponentControls controls) {
        this.inplaceComponentRoot = root;
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
                throw new UnsupportedOperationException("Unknown switch " + by);
        }
        waitAfterConfirmOrCancel();
    }

    private void cancelByControls() {
        getControls().cancel();
    }

    private void cancelByKeys() {
        input.sendKeys(Keys.chord(Keys.CONTROL, Keys.ESCAPE));
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
        waitAfterConfirmOrCancel();
    }

    private void confirmByControls() {
        getControls().ok();
    }

    private void confirmByKeys() {
        new Actions(driver).sendKeys(Keys.chord(Keys.CONTROL, Keys.RETURN)).perform();
    }

    @Override
    public InplaceComponentControls getControls() {
        return controls;
    }

    protected void waitAfterConfirmOrCancel() {//empty
    }
}
