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
package org.richfaces.tests.metamer.bean.abstractions;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SelectValidationBean extends StringInputValidationBeanImpl implements Serializable {

    private static final long serialVersionUID = -1L;
    private List<SelectItem> validationOptionsCustom = Lists.newArrayList(
            new SelectItem("rich faces", "rich faces"),
            new SelectItem("richfaces", "richfaces"),
            new SelectItem(CUSTOM_VALUE_DEFAULT, CUSTOM_VALUE_DEFAULT));
    private List<SelectItem> validationOptionsNotEmpty = Lists.newArrayList(
            new SelectItem("", ""),
            new SelectItem("not empty", "not empty"),
            new SelectItem(NOTEMPTY_VALUE_DEFAULT, NOTEMPTY_VALUE_DEFAULT));
    private List<SelectItem> validationOptionsPattern = Lists.newArrayList(
            new SelectItem("^(%1", "^(%1"),
            new SelectItem("abcd", "abcd"),
            new SelectItem(PATTERN_VALUE_DEFAULT, PATTERN_VALUE_DEFAULT));
    private List<SelectItem> validationOptionsRequired = Lists.newArrayList(
            new SelectItem("", ""),
            new SelectItem("required 2", "required 2"),
            new SelectItem(REQUIRED_VALUE_DEFAULT, REQUIRED_VALUE_DEFAULT));
    private List<SelectItem> validationOptionsSize = Lists.newArrayList(
            new SelectItem("12", "12"),
            new SelectItem("123", "123"),
            new SelectItem(SIZE_VALUE_DEFAULT, SIZE_VALUE_DEFAULT));

    public List<SelectItem> getValidationOptionsCustom() {
        return validationOptionsCustom;
    }

    public List<SelectItem> getValidationOptionsNotEmpty() {
        return validationOptionsNotEmpty;
    }

    public List<SelectItem> getValidationOptionsPattern() {
        return validationOptionsPattern;
    }

    public List<SelectItem> getValidationOptionsRequired() {
        return validationOptionsRequired;
    }

    public List<SelectItem> getValidationOptionsSize() {
        return validationOptionsSize;
    }
}
