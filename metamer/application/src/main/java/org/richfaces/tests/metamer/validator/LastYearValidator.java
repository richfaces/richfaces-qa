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
package org.richfaces.tests.metamer.validator;

import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Custom date validator that accepts only last year's dates.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22493 $
 */
@FacesValidator("org.richfaces.LastYearValidator")
public class LastYearValidator implements Validator {

    public static final String VALIDATOR_ERROR_MSG = "has to contain last year's date";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, VALIDATOR_ERROR_MSG,
                VALIDATOR_ERROR_MSG);

        if (value == null || !(value instanceof Date)) {
            throw new ValidatorException(msg);
        }

        int todaysYear = Calendar.getInstance().get(Calendar.YEAR);
        Calendar param = Calendar.getInstance();
        param.setTime((Date) value);

        if (todaysYear - 1 != param.get(Calendar.YEAR)) {
            throw new ValidatorException(msg);
        }
    }
}
