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
package org.richfaces.tests.metamer.validator;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.richfaces.tests.metamer.bean.rich.RichCalendarBean;

/**
 * Custom date validator that accepts only last year's dates.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22493 $
 */
@FacesValidator("org.richfaces.DateRangeValidator")
public class DateRangeValidator implements Validator {

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.Object)f
     */
    public void validate(final FacesContext ctx, final UIComponent component, final Object value)
        throws ValidatorException {
        FacesContext context = FacesContext.getCurrentInstance();
        RichCalendarBean bean = (RichCalendarBean) context.getApplication().evaluateExpressionGet(context,
            "#{richCalendarBean}", RichCalendarBean.class);

        Date date = (Date) value;
        if (bean.getValue() != null && date != null)
            if (!date.after(bean.getValue())) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "End Date must be after Start Date!!.\n", "End Date must be after Start Date!!\n"));

            }
    }

}
