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
package org.richfaces.tests.page.fragments.impl.select;

import com.google.common.base.Optional;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public class RichFacesSelect implements Select {

    @Drone
    private WebDriver driver;

    @FindBy(className = "rf-sel-inp")
    private TextInputComponentImpl input;

    private static final ByJQuery GLOBAL_POPUP = ByJQuery.jquerySelector("div.rf-sel-shdw:visible");
    private static final ScrollingType DEFAULT_SCROLLING_TYPE = ScrollingType.BY_MOUSE;

    private AdvancedInteractions interactions;
    private SelectSuggestionsImpl selectSuggestions = new SelectSuggestionsImpl();
    private ScrollingType scrollingType;

    public AdvancedInteractions advanced() {
        if (interactions == null) {
            interactions = new AdvancedInteractions();
        }
        return interactions;
    }

    private SelectSuggestionsImpl getSuggestions() {
        advanced().waitUntilSuggestionsAreVisible();
        if (selectSuggestions == null) {
            selectSuggestions = new SelectSuggestionsImpl();
        }
        return selectSuggestions;
    }

    private List<WebElement> getSuggestionsElements() {
        return driver.findElement(GLOBAL_POPUP).findElements(By.className("rf-sel-opt"));
    }

    @Override
    public SelectSuggestions openSelect() {
        if (!Utils.isVisible(driver, GLOBAL_POPUP)) {
            input.getInput().click();
        }
        return getSuggestions();
    }

    @Override
    public SelectSuggestions type(String text) {
        input.clear(ClearType.DELETE).fillIn(text);
        return getSuggestions();
    }

    public class SelectSuggestionsImpl implements SelectSuggestions {

        @Override
        public void select(ChoicePicker picker) {
            WebElement foundValue = picker.pick(getSuggestionsElements());
            if (foundValue == null) {
                throw new RuntimeException("The value was not found by " + picker.toString());
            }

            if (advanced().getScrollingType() == ScrollingType.BY_KEYS) {
                selectWithKeys(foundValue);
            } else {
                foundValue.click();
            }
            advanced().waitUntilSuggestionsAreNotVisible();
            input.trigger("blur");
        }

        @Override
        public void select(String match) {
            select(ChoicePickerHelper.byVisibleText().match(match));
        }

        @Override
        public void select(int index) {
            select(ChoicePickerHelper.byIndex().index(index));
        }

        private void selectWithKeys(WebElement foundValue) {
            // if selectFirst attribute is set, we don't have to press arrow down key for first item
            boolean skip = getSuggestionsElements().get(0).getAttribute("class").contains("rf-sel-sel");
            int index = Utils.getIndexOfElement(foundValue);
            int steps = index + (skip ? 0 : 1);
            Actions actions = new Actions(driver);
            for (int i = 0; i < steps; i++) {
                actions.sendKeys(Keys.ARROW_DOWN);
            }
            actions.sendKeys(foundValue, Keys.RETURN).perform();
        }

    }

    public class AdvancedInteractions {

        public TextInputComponent getInput() {
            return input;
        }

        public ScrollingType getScrollingType() {
            return Optional.fromNullable(scrollingType).or(DEFAULT_SCROLLING_TYPE);
        }

        public List<WebElement> getSuggestions() {
            return Collections.unmodifiableList(getSuggestionsElements());
        }

        public void setScrollingType(ScrollingType type) {
            scrollingType = type;
        }

        public void waitUntilSuggestionsAreNotVisible() {
            Graphene.waitGui().until().element(GLOBAL_POPUP).is().not().present();
        }

        public void waitUntilSuggestionsAreVisible() {
            Graphene.waitGui().until().element(GLOBAL_POPUP).is().present();
        }
    }
}
