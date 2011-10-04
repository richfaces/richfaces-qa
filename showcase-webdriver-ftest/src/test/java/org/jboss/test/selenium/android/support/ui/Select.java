/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.jboss.test.selenium.android.support.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.android.Key;
import org.jboss.test.selenium.android.ToolKit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Select extends AbstractComponent {

    private List<Option> options;
    
    public Select(ToolKit toolKit, WebElement webElement) {
        super(toolKit, webElement);
    }
    
    public int getIndexByValue(String value) {
        Validate.notNull(value);
        int index = 0;
        for (Option option : getOptions()) {
            if (option.getValue().equals(value)) {
                return index;
            }
            index++;
        }
        throw new IllegalStateException("The option with the given value[" + value + "] doesn't exist.");
    }
    
    public List<Option> getOptions() {
        if (options == null) {
            List<WebElement> elements = getWebElement().findElements(By.tagName("option"));
            List<Option> newOptions = new ArrayList<Option>(elements.size());
            for (WebElement el : elements) {
                newOptions.add(new Option(getToolKit(), el));
            }
            options = Collections.unmodifiableList(newOptions);
        }
        return options;
    }    
    
    public int getSelectedIndex() {
        int index = 0;
        for(Option option : getOptions()) {
            if (option.isSelected()) {
                return index;
            }
            index++;
        }
        throw new IllegalStateException("No option is selected.");
    }
    
    public Option getSelectedOption() {
        return getOptions().get(getSelectedIndex());
    }

    public void selectByIndex(int index) {
        if (index < 0 || index >= getOptions().size()) {
            throw new IndexOutOfBoundsException("The index is out of the range [0, " + getOptions().size() + "]");
        }
        int selectedIndex = getSelectedIndex();
        if (index == selectedIndex) {
            return;
        }
        Key key;
        if (index > selectedIndex) {
             key = Key.DOWN_ARROW;
        }
        else {
            key = Key.UP_ARROW; 
        }
        getWebElement().click();
        try {
            for(int i=0; i<Math.abs(index - selectedIndex); i++) {
                getToolKit().sendKey(key);
            }
            getToolKit().sendKey(Key.ENTER);
        } catch(IOException e) {
            throw new IllegalStateException("The option can't be selected.", e);
        }        
    }
    
    public void selectByValue(String value) {
        selectByIndex(getIndexByValue(value));
    }
    
}
