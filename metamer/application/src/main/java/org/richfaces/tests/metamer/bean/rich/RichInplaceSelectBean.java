/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

import org.richfaces.component.UIInplaceSelect;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.bean.RichBean;
import org.richfaces.tests.metamer.model.Capital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:inplaceSelect.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richInplaceSelectBean")
@ViewScoped
public class RichInplaceSelectBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    // FIXME: @ManagedProperty(value = "#{model.capitals}")
    private List<Capital> capitals = Model.unmarshallCapitals();
    private List<SelectItem> capitalsOptions = null;
    private List<SelectItem> validationOptions = null;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
    private String requiredMessage = "value is required";

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        capitalsOptions = new ArrayList<SelectItem>();
        validationOptions = new ArrayList<SelectItem>();
        for (Capital capital : capitals) {
            capitalsOptions.add(new SelectItem(capital.getState(), capital.getState()));
            validationOptions.add(new SelectItem(capital.getState(), capital.getState()));
        }
        validationOptions.add(new SelectItem("@@", "@@"));
        validationOptions.add(new SelectItem("", ""));
        validationOptions.add(new SelectItem("RichFaces", "RichFaces"));
        validationOptions.add(new SelectItem("richfaces", "richfaces"));

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIInplaceSelect.class, getClass());

        attributes.setAttribute("defaultLabel", "Click here to edit");
        attributes.setAttribute("editEvent", "click");
        attributes.setAttribute("listHeight", "200px");
        attributes.setAttribute("listWidth", "200px");
        attributes.setAttribute("openOnEdit", true);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("saveOnBlur", true);
        attributes.setAttribute("saveOnSelect", true);

        // TODO has to be tested in another way
        attributes.remove("converter");
        attributes.remove("converterMessage");
        attributes.remove("required");
        attributes.remove("requiredMessage");
        attributes.remove("validator");
        attributes.remove("validatorMessage");
        attributes.remove("valueChangeListener");

        // TODO remove as soon as RF-10411 is resolved
        attributes.setAttribute("changedStateClass", null);
        attributes.setAttribute("disabledStateClass", null);
        attributes.setAttribute("editStateClass", null);
        attributes.setAttribute("readyStateClass", null);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public List<SelectItem> getCapitalsOptions() {
        return capitalsOptions;
    }

    public void setCapitalsOptions(List<SelectItem> capitalsOptions) {
        this.capitalsOptions = capitalsOptions;
    }

    public List<SelectItem> getValidationOptions() {
        return validationOptions;
    }

    public void setValidationOptions(List<SelectItem> validationOptions) {
        this.validationOptions = validationOptions;
    }

    @NotEmpty
    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    @Pattern(regexp = "[a-z].*")
    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @Size(min = 3, max = 6)
    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public String getValue5() {
        return value5;
    }

    public void setValue5(String value5) {
        this.value5 = value5;
    }

    public String getRequiredMessage() {
        return requiredMessage;
    }

    public void setRequiredMessage(String requiredMessage) {
        this.requiredMessage = requiredMessage;
    }
    
    public void listener(ValueChangeEvent event) {
        RichBean.logToPage("* value changed: " + event.getOldValue() + " -> " + event.getNewValue());
    }
}
