/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.CheckboxInputComponentImpl;
import org.richfaces.fragment.common.picker.ChoicePicker;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ColumnControl {

    @FindBy(tagName = "label")
    private List<WebElement> optionsElements;
    @Root
    private WebElement rootElement;

    private LabelWithCheckBox getLabel(ChoicePicker picker) {
        WebElement picked = picker.pick(optionsElements);
        if (picked == null) {
            throw new NullPointerException("There is no such option!");
        }
        return Graphene.createPageFragment(LabelWithCheckBox.class, picked);
    }

    public List<String> getOptionsLabels() {
        List<String> result = Lists.newArrayList();
        for (WebElement option : optionsElements) {
            result.add(option.getText());
        }
        return result;
    }

    public void hideAllColumns() {
        for (WebElement label : optionsElements) {
            Graphene.createPageFragment(LabelWithCheckBox.class, label).getCheckbox().uncheck();
        }
    }

    public boolean areAllColumnsHidden() {
        boolean result = true;
        for (WebElement label : optionsElements) {
            result &= !Graphene.createPageFragment(LabelWithCheckBox.class, label).getCheckbox().isChecked();
        }
        return result;
    }

    public ColumnControl hideColumn(ChoicePicker picker) {
        return setValue(picker, false);
    }

    public boolean isColumnChecked(ChoicePicker choicePicker) {
        return getLabel(choicePicker).getCheckbox().isChecked();
    }

    public boolean isVisible() {
        return rootElement.isDisplayed();
    }

    private ColumnControl setValue(ChoicePicker p, boolean value) {
        CheckboxInputComponentImpl checkbox = getLabel(p).getCheckbox();
        if (value) {
            checkbox.check();
        } else {
            checkbox.uncheck();
        }
        return this;
    }

    public ColumnControl showColumn(ChoicePicker picker) {
        return setValue(picker, true);
    }

    public void waitUntilIsVisible() {
        Graphene.waitGui().until().element(rootElement).is().visible();
    }

    public static class LabelWithCheckBox {

        @FindBy(tagName = "input")
        private CheckboxInputComponentImpl checkbox;

        public CheckboxInputComponentImpl getCheckbox() {
            return checkbox;
        }
    }
}
