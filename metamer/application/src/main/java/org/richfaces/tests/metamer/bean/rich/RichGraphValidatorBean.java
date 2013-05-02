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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.NotEmpty;
import org.richfaces.tests.metamer.Attribute;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.validation.groups.ValidationGroupAllComponents;
import org.richfaces.tests.metamer.validation.groups.ValidationGroupBooleanInputs;
import org.richfaces.tests.metamer.validation.groups.ValidationGroupNumericInputs;
import org.richfaces.ui.validation.graphValidator.UIGraphValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Managed Bean for rich:graphValidator
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "richGraphValidatorBean")
@ViewScoped
public class RichGraphValidatorBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RichGraphValidatorBean.class);
    public static final int REQUIRED_INT_VALUE = 10;
    public static final Integer REQUIRED_INTEGER_VALUE = Integer.valueOf(REQUIRED_INT_VALUE);
    public static final String SMILE = ":-)";
    public static final String SUCCESSFULL_ACTION_MSG = "Action successfully done!";
    public static final String VALIDATION_MSG_BOOLEANS = "Select Boolean Checkbox isn't checked!";
    public static final String VALIDATION_MSG_NUMERICS = "One of the numeric inputs doesn't contain required value "
            + REQUIRED_INT_VALUE + "!";
    public static final String VALIDATION_MSG_ALL = "One of the inputs doesn't contain smile or numeric value "
            + REQUIRED_INT_VALUE + ", date is from future or boolean checkbox is not checked!";
    public static final List<SelectItem> SELECT_ITEMS = Lists.newArrayList(
            new SelectItem("Abcd"),
            new SelectItem("Bcde"),
            new SelectItem("Cdef"),
            new SelectItem("Defg"),
            new SelectItem(SMILE));
    //
    private Attributes attributes;
    private List<SelectItem> selectItems = Collections.unmodifiableList(SELECT_ITEMS);
    //////////////
    //Input values
    @NotNull
    @NotEmpty
    private String autocompleteInput = SMILE;
    @NotNull
    private Date calendar = new Date(System.currentTimeMillis());
    @NotNull
    @NotEmpty
    private String inplaceInput = SMILE;
    @NotNull
    @NotEmpty
    private String inplaceSelect = SMILE;
    private Integer inputNumberSlider = REQUIRED_INTEGER_VALUE;
    private Integer inputNumberSpinner = REQUIRED_INTEGER_VALUE;
    @NotNull
    @NotEmpty
    private String inputSecret = SMILE;
    @NotNull
    @NotEmpty
    private String inputText = SMILE;
    @NotNull
    @NotEmpty
    private String inputTextarea = SMILE;
    @NotNull
    @NotEmpty
    private String select = SMILE;
    private Boolean selectBooleanCheckbox = Boolean.TRUE;
    @NotEmpty
    private List<String> selectManyCheckbox = Lists.newArrayList(SMILE);
    @NotEmpty
    private List<String> selectManyListbox = Lists.newArrayList(SMILE);
    @NotEmpty
    private List<String> selectManyMenu = Lists.newArrayList(SMILE);
    @NotNull
    @NotEmpty
    private String selectOneListbox = SMILE;
    @NotNull
    @NotEmpty
    private String selectOneMenu = SMILE;
    @NotNull
    @NotEmpty
    private String selectOneRadio = SMILE;

    @PostConstruct
    public void init() {
        LOGGER.info("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIGraphValidator.class, getClass());

        attributes.setAttribute("rendered", true);
        attributes.setAttribute("type", "org.richfaces.BeanValidator");

        //used in sample, value contains reference to this validation bean
        attributes.remove("value");
    }

    public void anotherActionOnAllComponents() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, SUCCESSFULL_ACTION_MSG, SUCCESSFULL_ACTION_MSG));
    }

    public List<String> autocomplete(String prefix) {
        ArrayList<String> result = new ArrayList<String>();
        if ((prefix == null) || (prefix.length() == 0)) {
            for (int i = 0; i < 10; i++) {
                result.add(getSelectItems().get(i).getLabel());
            }
        } else {
            Iterator<SelectItem> iterator = selectItems.iterator();
            while (iterator.hasNext()) {
                SelectItem elem = ((SelectItem) iterator.next());
                if ((elem.getLabel() != null && elem.getLabel().toLowerCase().indexOf(prefix.toLowerCase()) == 0)
                        || "".equals(prefix)) {
                    result.add(elem.getLabel());
                }
            }
        }
        return result;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public String getAutocompleteInput() {
        return autocompleteInput;
    }

    public Date getCalendar() {
        return calendar;
    }

    public String getInplaceInput() {
        return inplaceInput;
    }

    public String getInplaceSelect() {
        return inplaceSelect;
    }

    public Integer getInputNumberSlider() {
        return inputNumberSlider;
    }

    public Integer getInputNumberSpinner() {
        return inputNumberSpinner;
    }

    public String getInputSecret() {
        return inputSecret;
    }

    public String getInputText() {
        return inputText;
    }

    public String getInputTextarea() {
        return inputTextarea;
    }

    public String getSelect() {
        return select;
    }

    public Boolean getSelectBooleanCheckbox() {
        return selectBooleanCheckbox;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<String> getSelectManyCheckbox() {
        return selectManyCheckbox;
    }

    public List<String> getSelectManyListbox() {
        return selectManyListbox;
    }

    public List<String> getSelectManyMenu() {
        return selectManyMenu;
    }

    public String getSelectOneListbox() {
        return selectOneListbox;
    }

    public String getSelectOneMenu() {
        return selectOneMenu;
    }

    public String getSelectOneRadio() {
        return selectOneRadio;
    }

    public Class<?>[] getValidationGroups() throws ClassNotFoundException {
        Attribute group = attributes.get("groups");//only one group at time is used
        Class<?> groupClass;
        Object _value = group.getValue();
        String value = (_value != null ? _value.toString() : null);
        if (value != null && !value.isEmpty()) {
            groupClass = Class.forName(value);
        } else {
            groupClass = Default.class;
        }
        return new Class[]{ groupClass };
    }

    @AssertTrue(message = VALIDATION_MSG_BOOLEANS,
    groups = { ValidationGroupBooleanInputs.class })
    public boolean isAllBooleanInputsCorrect() {
        return selectBooleanCheckbox.booleanValue();
    }

    @AssertTrue(message = VALIDATION_MSG_ALL,
    groups = { Default.class, ValidationGroupAllComponents.class })
    public boolean isAllInputsCorrect() {
        return autocompleteInput.contains(SMILE)
                && inplaceSelect.contains(SMILE)
                && inplaceInput.contains(SMILE)
                && select.contains(SMILE)
                && inputNumberSlider.equals(REQUIRED_INTEGER_VALUE)
                && inputNumberSpinner.equals(REQUIRED_INTEGER_VALUE)
                && inputText.contains(SMILE)
                && inputSecret.contains(SMILE)
                && inputTextarea.contains(SMILE)
                && selectBooleanCheckbox.booleanValue()
                && selectManyCheckbox.contains(SMILE)
                && selectOneListbox.contains(SMILE)
                && selectManyListbox.contains(SMILE)
                && selectOneMenu.contains(SMILE)
                && selectManyMenu.contains(SMILE)
                && selectOneRadio.contains(SMILE)
                && !calendar.after(new Date(System.currentTimeMillis()));
    }

    @AssertTrue(message = VALIDATION_MSG_NUMERICS,
    groups = { ValidationGroupNumericInputs.class })
    public boolean isAllNumericInputsCorrect() {
        return inputNumberSlider.equals(REQUIRED_INTEGER_VALUE)
                && inputNumberSpinner.equals(REQUIRED_INTEGER_VALUE);
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setAutocompleteInput(String autocompleteInput) {
        this.autocompleteInput = autocompleteInput;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public void setInplaceInput(String inplaceInput) {
        this.inplaceInput = inplaceInput;
    }

    public void setInplaceSelect(String inplaceSelect) {
        this.inplaceSelect = inplaceSelect;
    }

    public void setInputNumberSlider(Integer inputNumberSlider) {
        this.inputNumberSlider = inputNumberSlider;
    }

    public void setInputNumberSpinner(Integer inputNumberSpinner) {
        this.inputNumberSpinner = inputNumberSpinner;
    }

    public void setInputSecret(String inputSecret) {
        this.inputSecret = inputSecret;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public void setInputTextarea(String inputTextarea) {
        this.inputTextarea = inputTextarea;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public void setSelectBooleanCheckbox(Boolean selectBooleanCheckbox) {
        this.selectBooleanCheckbox = selectBooleanCheckbox;
    }

    public void setSelectManyCheckbox(List<String> selectManyCheckbox) {
        this.selectManyCheckbox = selectManyCheckbox;
    }

    public void setSelectManyListbox(List<String> selectManyListbox) {
        this.selectManyListbox = selectManyListbox;
    }

    public void setSelectManyMenu(List<String> selectManyMenu) {
        this.selectManyMenu = selectManyMenu;
    }

    public void setSelectOneListbox(String selectOneListbox) {
        this.selectOneListbox = selectOneListbox;
    }

    public void setSelectOneMenu(String selectOneMenu) {
        this.selectOneMenu = selectOneMenu;
    }

    public void setSelectOneRadio(String selectOneRadio) {
        this.selectOneRadio = selectOneRadio;
    }
}
