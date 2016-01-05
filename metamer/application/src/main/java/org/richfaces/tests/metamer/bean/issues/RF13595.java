/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "rf13595")
@SessionScoped
public class RF13595 implements Serializable {

    public static final String VALIDATION_MESSAGE = "Must not be after current time!";
    private static final long serialVersionUID = 1L;

    private Date input;
    private boolean inputVisible;

    public void changeInputVisibility(ActionEvent event) {
        System.out.println("Visibility change event");
        inputVisible = !inputVisible;
    }

    public Date getInput() {
        return input;
    }

    public boolean isInputVisible() {
        return inputVisible;
    }

    public void setInput(Date input) {
        this.input = input;
    }

    public void validateInput(FacesContext ctx, UIComponent comp, Object value) throws ValidatorException {
        if (value instanceof Date) {
            Date text = (Date) value;
            System.out.println("Validating input: " + text);
            // create a yesterday Date and validate that selected date is after this new date (the test selects current date)
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_MONTH, -1);
            if (text.after(c.getTime())) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, VALIDATION_MESSAGE,
                    null));
            }
        } else {
            System.out.println("Input value is no String!");
        }
    }

}
