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
package org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.spinner;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor.SetValueBy;

/**
 * Abstract class for spinner component.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class Spinner<T> implements VisibleComponent {

    @Root
    protected WebElement root;
    @FindBy(css = "input.rf-cal-sp-inp")
    protected WebElement inputElement;
    @FindBy(className = "rf-cal-sp-up")
    protected WebElement buttonUpElement;
    @FindBy(className = "rf-cal-sp-down")
    protected WebElement buttonDownElement;
    protected WebDriver driver = GrapheneContext.getProxy();

    public WebElement getButtonDownElement() {
        return buttonDownElement;
    }

    public WebElement getButtonUpElement() {
        return buttonUpElement;
    }

    public WebElement getInputElement() {
        return inputElement;
    }

    /**
     * Returns value set in this spinner
     * @return
     */
    public abstract T getValue();

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    protected boolean isSameValueAreadySet(T value) {
        return getValue().equals(value);
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    public void setValueBy(T value, SetValueBy by) {
        switch (by) {
            case buttons:
                setValueByButtons(value);
                break;
            case typing:
                setValueByTyping(value);
                break;
            default:
                throw new RuntimeException("Unknown switch.");
        }
    }

    /**
     * Sets spinner's value by clicking on the buttons
     * @param value value to be set
     */
    public abstract void setValueByButtons(T value);

    /**
     * Sets spinner value by direct input writing
     * @param value value to be set
     */
    public void setValueByTyping(T value) {
        if (!isSameValueAreadySet(value)) {
            new Actions(driver).click(inputElement)
                    .sendKeys(Keys.BACK_SPACE)//delete 1 character from value in input
                    .sendKeys(Keys.BACK_SPACE)//delete 1 character from value in input, the input should be clear now
                    .sendKeys(value.toString())//set desired value
                    .build().perform();
        }
    }
}
