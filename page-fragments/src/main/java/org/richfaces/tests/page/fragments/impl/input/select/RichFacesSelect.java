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
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RichFacesSelect implements Select {

    private static final ByJQuery GLOBAL_POPUP = ByJQuery.jquerySelector("div.rf-sel-shdw:visible");

    @FindBy(className = "rf-sel-opt")
    private List<WebElement> options;
    @FindBy(className = "rf-sel-inp")
    private TextInputComponentImpl input;
    @FindBy(className = "rf-sel-shdw")
    private WebElement localPopup;

    @Drone
    private WebDriver browser;

    @Override
    public OptionList callPopup() {
        if (isPopupPresent()) {
            return getPopup();
        } else {
            getInput().getInput().click();
            Graphene.waitAjax()
                    .until()
                    .element(GLOBAL_POPUP)
                    .is()
                    .present();
            return getPopup();
        }
    }

    @Override
    public TextInputComponent getInput() {
        return input;
    }

    @Override
    public Option getSelectedOption() {
        if (isPopupPresent()) {
            return null;
        }
        int index = 0;
        for (WebElement option: options) {
            if (option.getAttribute("class").contains("rf-sel-sel")) {
                return new SimpleOption(index, input.getStringValue());
            }
            index++;
        }
        return null;
    }

    @Override
    public boolean isPopupPresent() {
        return isElementPresent(GLOBAL_POPUP) && !isElementPresent(localPopup);
    }

    protected OptionList getPopup() {
        return Graphene.createPageFragment(RichFacesSelectPopup.class, browser.findElement(GLOBAL_POPUP));
    }

    protected boolean isElementPresent(By element) {
        try {
            browser.findElement(element);
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    protected boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

}
