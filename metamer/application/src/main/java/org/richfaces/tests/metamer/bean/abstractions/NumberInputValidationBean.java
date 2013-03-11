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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.richfaces.tests.metamer.validator.PositiveNumberValidator;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface NumberInputValidationBean extends AttributesHolder {

    int CUSTOM_VALUE_DEFAULT = 1;
    int MAX_VALUE_DEFAULT = 1;
    int MIN_VALUE_DEFAULT = 10;
    //
    String MIN_VALIDATION_MSG = "must be greater than or equal to 2";
    String MAX_VALIDATION_MSG = "must be less than or equal to 2";
    String CUSTOM_VALIDATION_MSG = PositiveNumberValidator.VALIDATOR_ERROR_MSG;

    int getCustomValue();

    @Max(value = 2, message = MAX_VALIDATION_MSG)
    int getMaxValue();

    @Min(value = 2, message = MIN_VALIDATION_MSG)
    int getMinValue();

    void setCustomValue(int value);

    void setMaxValue(int value);

    void setMinValue(int value);
}
