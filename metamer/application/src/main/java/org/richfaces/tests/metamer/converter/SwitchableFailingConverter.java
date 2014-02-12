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
package org.richfaces.tests.metamer.converter;

import static java.lang.Boolean.FALSE;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.faces.event.PhaseId;

/**
 * Converter for testing of input components @converter and @converterMessage.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ViewScoped
@FacesConverter(value = "switchableFailingConverter")
@ManagedBean(name = "switchableFailingConverter")
public class SwitchableFailingConverter implements Converter {

    public static final String MESSAGE_TEMPLATE = "Cannot convert parameter '%s'.";
    private boolean failing = FALSE;

    private FacesMessage creatConverterMessage(Object value) {
        String msg = String.format(MESSAGE_TEMPLATE, (value == null ? "" : value.toString()));
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (failing) {
            throw new ConverterException(creatConverterMessage(value));
        }
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (failing
                && context.getCurrentPhaseId().equals(PhaseId.PROCESS_VALIDATIONS)// because of f:selectItems conversion
                ) {
            throw new ConverterException(creatConverterMessage(value));
        }
        return (value == null ? "" : value.toString());
    }

    public boolean isFailing() {
        return failing;
    }

    public void setFailing(boolean failing) {
        this.failing = failing;
    }
}
