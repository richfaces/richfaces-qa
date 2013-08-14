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
package org.richfaces.tests.page.fragments.impl.orderingList;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.list.ListComponent;
import org.richfaces.tests.page.fragments.impl.list.ListComponentImpl;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByIndexChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.MultipleChoicePicker;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesOrderingList implements OrderingList {

    @FindBy(css = "button.rf-ord-dn")
    private WebElement downButtonElement;
    @FindBy(css = "button.rf-ord-up-tp")
    private WebElement topButtonElement;
    @FindBy(css = "button.rf-ord-dn-bt")
    private WebElement bottomButtonElement;
    @FindBy(css = "button.rf-ord-up")
    private WebElement upButtonElement;

    @FindBy(className = "rf-ord-opt")
    private List<WebElement> items;
    @FindBy(className = "rf-ord-sel")
    private List<WebElement> selectedItems;

    @FindBy(css = "div.rf-ord-lst-scrl > div[id$=Items]")
    private ListComponentImpl list;

    @Drone
    private WebDriver driver;

    private final AdvancedInteractions interactions = new AdvancedInteractions();
    private final PuttingSelectedItem puttingSelectedItem = new PuttingSelectedItemImpl();
    private final OrderingInteraction orderingInteraction = new OrderingInteractionImpl();

    public AdvancedInteractions advanced() {
        return interactions;
    }

    @Override
    public PuttingSelectedItem select(String visibleText) {
        return select(ChoicePickerHelper.byVisibleText().match(visibleText));
    }

    @Override
    public PuttingSelectedItem select(Integer index) {
        return select(ChoicePickerHelper.byIndex().index(index));

    }

    @Override
    public PuttingSelectedItem select(ChoicePicker picker) {
        unselectAll();
        selectItem(picker.pick(items));
        return puttingSelectedItem;
    }

    private void selectItem(final WebElement item) {
        new Actions(driver)
            .keyDown(Keys.CONTROL).click(item).keyUp(Keys.CONTROL)
            .addAction(new Action() {

                @Override
                public void perform() {
                    Graphene.waitGui().until().element(item).attribute("class").contains("rf-ord-sel");
                }
            })
            .perform();
    }

    private void selectItems(List<WebElement> list) {
        for (WebElement webElement : list) {
            selectItem(webElement);
        }
    }

    private void unselectAll() {
        if (!selectedItems.isEmpty()) {
            new Actions(driver)
                .click(items.get(0))
                .keyDown(Keys.CONTROL).click(items.get(0)).keyUp(Keys.CONTROL)
                .addAction(new Action() {

                    @Override
                    public void perform() {
                        Graphene.waitGui().until().element(items.get(0)).attribute("class").not().contains("rf-ord-sel");
                    }
                })
                .perform();
            if (!selectedItems.isEmpty()) {
                throw new RuntimeException("The unselection was not successfull.");
            }
        }
    }

    private class PuttingSelectedItemImpl implements PuttingSelectedItem {

        private void putAction(int positionSource, int positionTarget, int differenceToEnd) {
            if (positionSource != positionTarget) {
                int differenceBetween = positionTarget - positionSource;
                int absBetween = Math.abs(differenceBetween);
                int min = Math.min(absBetween, Math.min(positionTarget, differenceToEnd));
                if (min == absBetween) {
                    singleStepMove(differenceBetween);
                } else if (min == positionTarget) {
                    orderingInteraction.top();
                    if (positionTarget != 0) {
                        singleStepMove(positionTarget);
                    }
                } else {
                    orderingInteraction.bottom();
                    if (differenceToEnd != 0) {
                        singleStepMove(-differenceToEnd);
                    }
                }
            }
        }

        @Override
        public void putItAfter(ChoicePicker picker) {
            int indexOfTargetItem = Utils.getIndexOfElement(picker.pick(items)) + 1;
            putAction(Utils.getIndexOfElement(selectedItems.get(0)), indexOfTargetItem, items.size() - indexOfTargetItem);
        }

        @Override
        public void putItAfter(int index) {
            putItAfter(ChoicePickerHelper.byIndex().index(index));
        }

        @Override
        public void putItAfter(String match) {
            putItAfter(ChoicePickerHelper.byVisibleText().match(match));
        }

        @Override
        public void putItBefore(ChoicePicker picker) {
            int indexOfTargetItem = Utils.getIndexOfElement(picker.pick(items));
            putAction(Utils.getIndexOfElement(selectedItems.get(0)), indexOfTargetItem, items.size() - indexOfTargetItem);
        }

        @Override
        public void putItBefore(int index) {
            putItBefore(ChoicePickerHelper.byIndex().index(index));
        }

        @Override
        public void putItBefore(String match) {
            putItBefore(ChoicePickerHelper.byVisibleText().match(match));
        }

        private void singleStepMove(int difference) {
            if (difference == 0) {// no operation
            } else if (difference > 0) {
                orderingInteraction.down(Math.abs(difference));
            } else {
                orderingInteraction.up(Math.abs(difference));
            }
        }
    }

    private class OrderingInteractionImpl implements OrderingInteraction {

        private void checkIfActionPosibleAndPerform(WebElement button, int times) {
            if (!button.isEnabled()) {// button is enabled only when some items are selected.
                throw new RuntimeException("No items are selected or button is disabled.");
            }
            for (int i = 0; i < times; i++) {
                button.click();
            }
        }

        @Override
        public void bottom() {
            checkIfActionPosibleAndPerform(bottomButtonElement, 1);
        }

        @Override
        public void down(int times) {
            checkIfActionPosibleAndPerform(downButtonElement, times);
        }

        @Override
        public void top() {
            checkIfActionPosibleAndPerform(topButtonElement, 1);
        }

        @Override
        public void up(int times) {
            checkIfActionPosibleAndPerform(upButtonElement, times);
        }
    }

    public class AdvancedInteractions {

        public ListComponent getList() {
            return list;
        }

        public OrderingInteraction select(String visibleText, String... otherTexts) {
            ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().allRulesMustPass(false).match(visibleText);
            for (String string : otherTexts) {
                picker.match(string);
            }
            return select(picker);
        }

        public OrderingInteraction select(Integer index, Integer... otherIndexes) {
            ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().index(index);
            for (Integer integer : otherIndexes) {
                picker.index(integer);
            }
            return select(picker);
        }

        public OrderingInteraction select(MultipleChoicePicker picker) {
            unselectAll();
            selectItems(picker.pickMultiple(items));
            return orderingInteraction;
        }
    }
}
