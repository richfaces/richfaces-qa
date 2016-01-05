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
package org.richfaces.tests.metamer.bean.abstractions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.richfaces.tests.metamer.model.Capital;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean
@SessionScoped
public class ValidatorBean {

    public static final String DEFAULT_VALIDATOR_ERROR_MSG = "Text input component should have 'Phoenix' as the value or at the first place. Number input component should have value lesser than 10.";
    private static final FacesMessage MESSAGE = new FacesMessage(FacesMessage.SEVERITY_ERROR, DEFAULT_VALIDATOR_ERROR_MSG,
        DEFAULT_VALIDATOR_ERROR_MSG);
    private static final String PHOENIX = "Phoenix";
    private static final String PHOENIX_IN_PARAGRAPH = "<p>Phoenix</p>";

    @ManagedProperty(value = "#{model.capitals}")
    private List<Capital> capitals;

    private List<SelectItem> capitalsSelectItems = null;
    private List<String> capitalsStrings = null;

    private String validatorMessage;
    private Object value;
    private Integer valueNumber;

    public List<Capital> getCapitals() {
        return capitals;
    }

    public List<SelectItem> getCapitalsSelectItems() {
        return capitalsSelectItems;
    }

    public List<String> getCapitalsStrings() {
        return capitalsStrings;
    }

    public String getDefaultMessage() {
        return DEFAULT_VALIDATOR_ERROR_MSG;
    }

    public String getValidatorMessage() {
        return validatorMessage;
    }

    public Object getValue() {
        return value;
    }

    public Integer getValueNumber() {
        return valueNumber;
    }

    @PostConstruct
    public void init() {
        int size = capitals.size();
        capitalsSelectItems = new ArrayList<SelectItem>(size);
        capitalsStrings = new ArrayList<String>(size);

        for (Capital capital : capitals) {
            capitalsSelectItems.add(new SelectItem(capital.getName(), capital.getName()));
            capitalsStrings.add(capital.getName());
        }

        valueNumber = 9;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public void setValidatorMessage(String validatorMessage) {
        this.validatorMessage = validatorMessage;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setValueNumber(Integer valueNumber) {
        this.valueNumber = valueNumber;
    }

    public void validateNumber(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || !(value instanceof Number)) {
            throw new ValidatorException(MESSAGE);
        }
        Number val = (Number) value;
        if (val.longValue() > 9) {
            throw new ValidatorException(MESSAGE);
        }
    }

    public void validateString(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || !((value instanceof List) ^ (value instanceof String))) {
            throw new ValidatorException(MESSAGE);
        }
        if (value instanceof List) {
            List<Capital> val = (List<Capital>) value;
            if (!PHOENIX.equals(val.get(0).getName())) {
                throw new ValidatorException(MESSAGE);
            }
        } else if (value instanceof String) {
            String val = ((String) value).trim();
            if (!(PHOENIX.equals(val) || PHOENIX_IN_PARAGRAPH.equals(val))) {
                throw new ValidatorException(MESSAGE);
            }
        }
    }
}
