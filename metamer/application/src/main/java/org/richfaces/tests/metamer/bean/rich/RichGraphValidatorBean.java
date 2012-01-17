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
import org.richfaces.component.UIGraphValidator;
import org.richfaces.tests.metamer.Attribute;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.validation.groups.ValidationGroupAllComponents;
import org.richfaces.tests.metamer.validation.groups.ValidationGroupBooleanInputs;
import org.richfaces.tests.metamer.validation.groups.ValidationGroupNumericInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed Bean for rich:graphValidator
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22498 $
 */
@ManagedBean(name = "richGraphValidatorBean")
@ViewScoped
public class RichGraphValidatorBean implements Serializable, Cloneable {
    
    /** Valid value of numeric inputs */
    public static final int REQUIRED_INT_VALUE = 10;
    
    /** Generated UID */
    private static final long serialVersionUID = -960575870621302059L;
    
    private static Logger logger;
    private final String smile = ":-)";
    private Attributes attributes;
    
    private List<SelectItem> selectItems;
    
    @NotNull
    @NotEmpty
    private String autocompleteInput = smile;    
    @NotNull
    @NotEmpty
    private String inplaceSelect = smile;    
    @NotNull
    @NotEmpty
    private String inplaceInput = smile;    
    @NotNull
    @NotEmpty
    private String select = smile;    
    private Integer inputNumberSlider = new Integer(REQUIRED_INT_VALUE);
    private Integer inputNumberSpinner = new Integer(REQUIRED_INT_VALUE);
    @NotNull
    @NotEmpty
    private String inputText = smile;    
    @NotNull
    @NotEmpty
    private String inputSecret = smile;
    @NotNull
    @NotEmpty
    private String inputTextarea = smile;
    private Boolean selectBooleanCheckbox = Boolean.TRUE;
    @NotEmpty
    private List<SelectItem> selectManyCheckbox = createSelectItems();
    @NotNull
    @NotEmpty
    private String selectOneListbox = smile;
    @NotEmpty
    private List<SelectItem> selectManyListbox = createSelectItems();
    @NotNull
    @NotEmpty
    private String selectOneMenu = smile;
    @NotEmpty
    private List<SelectItem> selectManyMenu = createSelectItems();
    @NotNull
    @NotEmpty
    private String selectOneRadio = smile;
    @NotNull
    private Date calendar = new Date(System.currentTimeMillis());
    
    @PostConstruct
    public void init(){
        logger = LoggerFactory.getLogger(getClass());
        logger.info("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIGraphValidator.class, getClass());
        
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("type", "org.richfaces.BeanValidator");
    }
    
    @AssertTrue(message = "One of following inputs doesn't contain smile or numeric value "
                    + REQUIRED_INT_VALUE + " or date is from future!",
                groups = {Default.class, ValidationGroupAllComponents.class})
    public boolean isAllInputsCorrect() {
        
        return autocompleteInput.contains(smile)
            && inplaceSelect.contains(smile)
            && inplaceInput.contains(smile)
            && select.contains(smile)
            && inputNumberSlider.equals(new Integer(REQUIRED_INT_VALUE))
            && inputNumberSpinner.equals(new Integer(REQUIRED_INT_VALUE))
            && inputText.contains(smile)
            && inputSecret.contains(smile)
            && inputTextarea.contains(smile)
            && selectBooleanCheckbox.booleanValue()
            && selectManyCheckbox.contains(smile)
            && selectOneListbox.contains(smile)
            && selectManyListbox.contains(smile)
            && selectOneMenu.contains(smile)
            && selectManyMenu.contains(smile)
            && selectOneRadio.contains(smile)
            && !calendar.after(new Date(System.currentTimeMillis()));
    }
    
    @AssertTrue(message = "One of following numeric inputs doesn't contain value " 
                    + REQUIRED_INT_VALUE + "!",
                groups = {ValidationGroupNumericInputs.class})
    public boolean isAllTextInputsCorrect() {
        return inputNumberSlider.equals(new Integer(REQUIRED_INT_VALUE))
            && inputNumberSpinner.equals(new Integer(REQUIRED_INT_VALUE));
    }
    
    @AssertTrue(message = "Select Boolean Checkbox isn't checked!",
        groups = {ValidationGroupBooleanInputs.class})
    public boolean isAllBooleanInputsCorrect() {
        return selectBooleanCheckbox.booleanValue();
    }
    
    public void anotherActionOnAllComponents() {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Action sucessfully done!", "Action sucessfully done!"));
    }
    
    private List<SelectItem> createSelectItems() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("a", "Abcd"));
        result.add(new SelectItem("b", "Bcde"));
        result.add(new SelectItem("c", "Cdef"));
        result.add(new SelectItem("d", "Defg"));
        result.add(new SelectItem(smile, smile));
        
        return result;
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
    
    public Class<?>[] getValidationGroups() throws ClassNotFoundException {
        Attribute groups = attributes.get("groups");        
        Class<?> group;
        if (groups.getValue() != null && !"".equals(groups.getValue())) {
            group = Class.forName(groups.getValue().toString());
        } else {
            group = Default.class;
        }
        return new Class[]{group};        
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getAutocompleteInput() {
        return autocompleteInput;
    }

    public void setAutocompleteInput(String autocompleteInput) {
        this.autocompleteInput = autocompleteInput;
    }

    public String getInplaceSelect() {
        return inplaceSelect;
    }

    public void setInplaceSelect(String inplaceSelect) {
        this.inplaceSelect = inplaceSelect;
    }

    public String getInplaceInput() {
        return inplaceInput;
    }

    public void setInplaceInput(String inplaceInput) {
        this.inplaceInput = inplaceInput;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public List<SelectItem> getSelectItems() {
        if (selectItems == null ) {
            selectItems = createSelectItems();
        }
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public Integer getInputNumberSlider() {
        return inputNumberSlider;
    }

    public void setInputNumberSlider(Integer inputNumberSlider) {
        this.inputNumberSlider = inputNumberSlider;
    }

    public Integer getInputNumberSpinner() {
        return inputNumberSpinner;
    }

    public void setInputNumberSpinner(Integer inputNumberSpinner) {
        this.inputNumberSpinner = inputNumberSpinner;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getInputSecret() {
        return inputSecret;
    }

    public void setInputSecret(String inputSecret) {
        this.inputSecret = inputSecret;
    }

    public String getInputTextarea() {
        return inputTextarea;
    }

    public void setInputTextarea(String inputTextarea) {
        this.inputTextarea = inputTextarea;
    }

    public Boolean getSelectBooleanCheckbox() {
        return selectBooleanCheckbox;
    }

    public void setSelectBooleanCheckbox(Boolean selectBooleanCheckbox) {
        this.selectBooleanCheckbox = selectBooleanCheckbox;
    }

    public List<SelectItem> getSelectManyCheckbox() {
        return selectManyCheckbox;
    }

    public void setSelectManyCheckbox(List<SelectItem> selectManyCheckbox) {
        this.selectManyCheckbox = selectManyCheckbox;
    }

    public String getSelectOneListbox() {
        return selectOneListbox;
    }

    public void setSelectOneListbox(String selectOneListbox) {
        this.selectOneListbox = selectOneListbox;
    }

    public List<SelectItem> getSelectManyListbox() {
        return selectManyListbox;
    }

    public void setSelectManyListbox(List<SelectItem> selectManyListbox) {
        this.selectManyListbox = selectManyListbox;
    }

    public String getSelectOneMenu() {
        return selectOneMenu;
    }

    public void setSelectOneMenu(String selectOneMenu) {
        this.selectOneMenu = selectOneMenu;
    }

    public List<SelectItem> getSelectManyMenu() {
        return selectManyMenu;
    }

    public void setSelectManyMenu(List<SelectItem> selectManyMenu) {
        this.selectManyMenu = selectManyMenu;
    }

    public String getSelectOneRadio() {
        return selectOneRadio;
    }

    public void setSelectOneRadio(String selectOneRadio) {
        this.selectOneRadio = selectOneRadio;
    }

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }
}
