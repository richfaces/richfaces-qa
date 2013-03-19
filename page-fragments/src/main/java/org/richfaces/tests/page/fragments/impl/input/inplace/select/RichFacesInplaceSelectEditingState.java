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
package org.richfaces.tests.page.fragments.impl.input.inplace.select;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.WebElementProxyUtils;
import org.richfaces.tests.page.fragments.impl.input.inplace.AbstractInplaceComponentEditingState;
import org.richfaces.tests.page.fragments.impl.input.inplace.InplaceComponentControls;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesInplaceSelectEditingState extends AbstractInplaceComponentEditingState implements InplaceSelectEditingState {

    private final List<WebElement> options;
    private final WebElement globalList;
    private final WebElement localList;
    private final WebElement script;

    public RichFacesInplaceSelectEditingState(WebElement root, WebElement input, InplaceComponentControls controls) {
        super(root, input, controls);
        localList = WebElementProxyUtils.createProxyForElement(By.cssSelector("span.rf-is-lst-cord"), inplaceComponentRoot);
        globalList = WebElementProxyUtils.createProxyForElement(By.cssSelector("body > span.rf-is-lst-cord"));
        waitForPopupShow();
        script = WebElementProxyUtils.createProxyForElement(By.cssSelector("script"), inplaceComponentRoot);
        options = WebElementProxyUtils.createProxyForElements(By.className("rf-is-opt"), driver);
    }

    @Override
    public void cancel(FinishEditingBy by) {
        if (!isSaveOnSelect()) {
            super.cancel(by);
        }
    }

    @Override
    public InplaceSelectEditingState changeToValue(String newValue) {
        for (Option option : getOptions()) {
            if (option.getText().equals(newValue)) {
                option.getElement().click();
                if (isSaveOnSelect()) {
                    waitForPopupHide();
                }
                return this;
            }
        }
        throw new RuntimeException("Cannot change value to: " + newValue + ". There is no such option");
    }

    @Override
    public InplaceSelectEditingState changeToValueAtIndex(int index) {
        getOptions().get(index).getElement().click();
        if (isSaveOnSelect()) {
            waitForPopupHide();
        }
        return this;
    }

    @Override
    public void confirm(FinishEditingBy by) {
        if (!isSaveOnSelect()) {
            super.confirm(by);
        }
    }

    @Override
    public OptionsList getOptions() {
        return new InplaceSelectOptionsList(options);
    }

    private boolean isSaveOnSelect() {
        String text = Utils.returningJQ("text()", script);//getting text from hidden element
        if (text.contains("\"saveOnSelect\":false")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    protected void waitAfterConfirmOrCancel() {
        waitForPopupHide();
    }

    private void waitForPopupHide() {
        Graphene.waitModel().until().element(localList).is().present();
        Graphene.waitModel().until().element(globalList).is().not().visible();
    }

    private void waitForPopupShow() {
        Graphene.waitModel().until().element(localList).is().not().present();
        Graphene.waitModel().until().element(globalList).is().visible();
    }
}
