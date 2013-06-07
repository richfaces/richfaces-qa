/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.input.select;

import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RichFacesSelectPopup extends AbstractOptionList {

    @Drone
    private WebDriver driver;
    @FindBy(className = "rf-sel-opt")
    private List<WebElement> options;

    @Override
    public Option select(Option option) {
        return selectByIndex(option.getIndex());
    }

    @Override
    public Option select(Option option, Selection selection) {
        return selectByIndex(option.getIndex(), selection);
    }

    @Override
    public Option selectByIndex(int index) {
        return selectByIndex(index, Selection.BY_MOUSE);
    }

    @Override
    public Option selectByIndex(int index, Selection selection) {
        WebElement element = options.get(index);
        Option option = new SimpleOption(index, element.getText());
        switch (selection) {
            case BY_MOUSE:
                element.click();
                break;
            case BY_KEYS:
                Actions a = new Actions(driver);
                // this cycle simulates the list browsing
                for (int i = 0; i <= index; i++) {// +1 x keypress
                    a.sendKeys(Keys.ARROW_DOWN);
                }
                a.sendKeys(Keys.RETURN).perform();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported selection type: " + selection);
        }
        return option;
    }

    @Override
    public Option selectByVisibleText(String text) {
        return selectByVisibleText(text, Selection.BY_MOUSE);
    }

    @Override
    public Option selectByVisibleText(String text, Selection selection) {
        Option option = getOptionByVisibleText(text);
        if (option == null) {
            throw new IllegalArgumentException("There is no option with visible text '" + text + "'.");
        }
        return selectByIndex(option.getIndex(), selection);
    }

    @Override
    protected List<WebElement> getOptionElements() {
        return options;
    }
}
