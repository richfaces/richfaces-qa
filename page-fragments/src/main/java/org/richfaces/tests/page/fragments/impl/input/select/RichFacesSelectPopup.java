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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RichFacesSelectPopup extends AbstractOptionList{

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
        switch(selection) {
            case BY_MOUSE:
                WebElement element = options.get(index);
                Option option = new SimpleOption(index, element.getText());
                element.click();
                return option;
            default:
                throw new UnsupportedOperationException("Unsupported selection type: " + selection);
        }
    }

    @Override
    public Option selectByVisibleText(String text) {
        return select(null, Selection.BY_MOUSE);
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
