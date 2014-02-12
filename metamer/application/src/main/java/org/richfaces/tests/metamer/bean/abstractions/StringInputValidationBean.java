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

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.richfaces.tests.metamer.validator.StringRichFacesValidator;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface StringInputValidationBean extends AttributesHolder {

    String CUSTOM_VALUE_DEFAULT = StringRichFacesValidator.RF;
    String NOTEMPTY_VALUE_DEFAULT = "RF4";
    String PATTERN_VALUE_DEFAULT = "xyz";
    String SIZE_VALUE_DEFAULT = "asdf";
    String REQUIRED_VALUE_DEFAULT = "required";
    //
    String CUSTOM_VALIDATION_MSG = StringRichFacesValidator.VALIDATION_ERROR_MSG;
    String NOT_EMPTY_VALIDATION_MSG = "may not be empty";
    String NOT_EMPTY_VALIDATION_MSG2 = "may not be null";
    String NOT_EMPTY_VALIDATION_MSG3 = "size must be between 1 and 2147483647";
    String REGEXP_VALIDATION_MSG = "must match \"[a-z].*\"";
    String STRING_SIZE_VALIDATION_MSG = "size must be between 3 and 6";
    String REQUIRED_VALIDATION_MSG = "value is required";

    String getCustomValue();

    @NotEmpty(message = NOT_EMPTY_VALIDATION_MSG)
    String getNotEmptyValue();

    @Pattern(regexp = "[a-z].*", message = REGEXP_VALIDATION_MSG)
    String getPatternValue();

    @Size(min = 3, max = 6, message = STRING_SIZE_VALIDATION_MSG)
    String getSizeValue();

    String getRequiredValue();

    void setCustomValue(String customValue);

    void setNotEmptyValue(String notEmptyValue);

    void setPatternValue(String patternValue);

    void setSizeValue(String sizeValue);

    void setRequiredValue(String requiredValue);
}
