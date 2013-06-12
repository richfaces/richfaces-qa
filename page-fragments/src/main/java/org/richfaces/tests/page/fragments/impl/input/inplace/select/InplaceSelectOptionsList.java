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

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class InplaceSelectOptionsList implements OptionsList {

    private final ArrayList<Option> options = Lists.newArrayList();

    public InplaceSelectOptionsList(List<WebElement> options) {
        for (int i = 0; i < options.size(); i++) {
            this.options.add(new InplaceSelectOption(options.get(i), i));
        }
    }

    @Override
    public Option get(int index) {
        return this.options.get(index);
    }

    @Override
    public Option getSelectedOption() {
        for (Option option : this) {
            if (option.isSelected()) {
                return option;
            }
        }
        return null;
    }

    @Override
    public Iterator<Option> iterator() {
        return this.options.iterator();
    }

    @Override
    public int size() {
        return this.options.size();
    }
}
