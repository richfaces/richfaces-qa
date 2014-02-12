/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 * For beans which uses Number inputs, e.g.: inputNumberSlider, inputNumberSpinner.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class NumberInputValidationBeanImpl implements NumberInputValidationBean, Serializable {

    private static final long serialVersionUID = -1L;
    protected Attributes attributes;
    // properties for jsr303 validations
    private int customValue = CUSTOM_VALUE_DEFAULT;
    private int maxValue = MAX_VALUE_DEFAULT;
    private int minValue = MIN_VALUE_DEFAULT;
    private int requiredValue = REQUIRED_VALUE_DEFAULT;

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public int getCustomValue() {
        return customValue;
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public int getMinValue() {
        return minValue;
    }

    @Override
    public int getRequiredValue() {
        return requiredValue;
    }

    @Override
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public void setCustomValue(int customValue) {
        this.customValue = customValue;
    }

    @Override
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    @Override
    public void setRequiredValue(int value) {
        this.requiredValue = value;
    }
}
