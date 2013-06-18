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

import java.io.Serializable;

import org.richfaces.tests.metamer.Attributes;

/**
 * For beans which uses String inputs, e.g.: autocomplete, inplaceInput, inplaceSelect, select
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class StringInputValidationBeanImpl implements StringInputValidationBean, Serializable {

    private static final long serialVersionUID = -1L;
    protected Attributes attributes;
    // properties for jsr303 validations
    private String customValue = CUSTOM_VALUE_DEFAULT;
    private String notEmptyValue = NOTEMPTY_VALUE_DEFAULT;
    private String patternValue = PATTERN_VALUE_DEFAULT;
    private String sizeValue = SIZE_VALUE_DEFAULT;
    private String requiredValue = REQUIRED_VALUE_DEFAULT;

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public String getCustomValue() {
        return customValue;
    }

    @Override
    public String getNotEmptyValue() {
        return notEmptyValue;
    }

    @Override
    public String getPatternValue() {
        return patternValue;
    }

    @Override
    public String getSizeValue() {
        return sizeValue;
    }

    @Override
    public String getRequiredValue() {
        return requiredValue;
    }

    @Override
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    @Override
    public void setNotEmptyValue(String notEmptyValue) {
        this.notEmptyValue = notEmptyValue;
    }

    @Override
    public void setPatternValue(String patternValue) {
        this.patternValue = patternValue;
    }

    @Override
    public void setSizeValue(String sizeValue) {
        this.sizeValue = sizeValue;
    }

    @Override
    public void setRequiredValue(String requiredValue) {
        this.requiredValue = requiredValue;
    }
}
