/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.bean.a4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.ajax.ajax.AjaxBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:ajax.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision: 22769 $
 */
@ManagedBean(name = "a4jAjaxBean")
@SessionScoped
public class A4JAjaxBean implements Serializable {

    private static final long serialVersionUID = -546567867L;
    private static final int ACTION_STRING_LENGTH = 6;
    private static Logger logger;
    private Attributes attributes;
    private String input;
    private String simpleText1;
    private String simpleText2;
    private String autocomplete1;
    private String autocomplete2;
    private String editor1;
    private String editor2;
    private String inplaceInput1;
    private String inplaceInput2;
    private String inplaceSelect1;
    private String inplaceSelect2;
    private String inputNumberSlider1;
    private String inputNumberSlider2;
    private String inputNumberSpinner1;
    private String inputNumberSpinner2;
    private Date calendar1;
    private Date calendar2;
    private List<?> inputs;
    private List<String> cars;
    private String car;
    private boolean boolVal;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getBehaviorAttributesFromFacesConfig(AjaxBehavior.class, getClass());

        // the 'event' attribute for behavior tag must be a literal
        attributes.remove("event");

        attributes.setAttribute("render", "output1, output2");
        attributes.setAttribute("execute", "@form");

        cars = new ArrayList<String>();
        cars.add("Ferrari");
        cars.add("Lexus");
        car = "Ferrari";
    }

    /**
     * Getter for attributes.
     *
     * @return A map containing all attributes of tested component. Name of the
     * component is key in the map.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * Setter for attributes.
     *
     * @param attributes map containing all attributes of tested component. Name
     * of the component is key in the map.
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * Getter for input.
     *
     * @return content of the input field on the page
     */
    public String getInput() {
        return input;
    }

    /**
     * Setter for input.
     *
     * @param input new content of the input field on the page
     */
    public void setInput(String input) {
        this.input = input;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public List<String> getCars() {
        return cars;
    }

    public void setCars(List<String> cars) {
        this.cars = cars;
    }

    public boolean isBoolVal() {
        return boolVal;
    }

    public void setBoolVal(boolean boolVal) {
        this.boolVal = boolVal;
    }

    /**
     * An action listener that takes the first six characters from input and
     * stores it to input.
     *
     * @param event an event representing the activation of a user interface
     * component (not used)
     */
    public void first6CharsListener(AjaxBehaviorEvent event) {
        if (input == null) {
            input = "";
        } else {
            int endIndex = input.length() > ACTION_STRING_LENGTH ? ACTION_STRING_LENGTH : input.length();
            input = (String) input.subSequence(0, endIndex);
        }
    }

    /**
     * An action listener that takes user's input, doubles it and stores it to
     * input.
     *
     * @param event an event representing the activation of a user interface
     * component (not used)
     */
    public void doubleStringListener(AjaxBehaviorEvent event) {
        if (input == null) {
            input = "";
        } else {
            input = input.concat(input);
        }
    }

    /**
     * An action listener that takes user's input, converts it to upper case and
     * stores it to input3.
     *
     * @param event an event representing the activation of a user interface
     * component (not used)
     */
    public void toUpperCaseListener(AjaxBehaviorEvent event) {
        if (input == null) {
            input = "";
        } else {
            input = input.toUpperCase();
        }
    }

    /**
     * An action listener causing error. Suitable for testing onerror attribute.
     *
     * @param event an event representing the activation of a user interface
     * component (not used)
     */
    public void causeErrorListener(AjaxBehaviorEvent event) {
        throw new FacesException("Ajax request caused an error. This is intentional behavior.");
    }

    public List<?> getInputs() {
        return inputs;
    }

    public void setInputs(List<?> inputs) {
        this.inputs = inputs;
    }

    public String getAutocomplete1() {
        return autocomplete1;
    }

    public void setAutocomplete1(String autocomplete1) {
        this.autocomplete1 = autocomplete1;
    }

    public String getAutocomplete2() {
        return autocomplete2;
    }

    public void setAutocomplete2(String autocomplete2) {
        this.autocomplete2 = autocomplete2;
    }

    public Date getCalendar1() {
        return calendar1;
    }

    public void setCalendar1(Date calendar1) {
        this.calendar1 = calendar1;
    }

    public Date getCalendar2() {
        return calendar2;
    }

    public void setCalendar2(Date calendar2) {
        this.calendar2 = calendar2;
    }

    public String getEditor1() {
        return editor1;
    }

    public void setEditor1(String editor1) {
        this.editor1 = editor1;
    }

    public String getEditor2() {
        return editor2;
    }

    public void setEditor2(String editor2) {
        this.editor2 = editor2;
    }

    public String getInplaceInput1() {
        return inplaceInput1;
    }

    public void setInplaceInput1(String inplaceInput1) {
        this.inplaceInput1 = inplaceInput1;
    }

    public String getInplaceInput2() {
        return inplaceInput2;
    }

    public void setInplaceInput2(String inplaceInput2) {
        this.inplaceInput2 = inplaceInput2;
    }

    public String getInplaceSelect1() {
        return inplaceSelect1;
    }

    public void setInplaceSelect1(String inplaceSelect1) {
        this.inplaceSelect1 = inplaceSelect1;
    }

    public String getInplaceSelect2() {
        return inplaceSelect2;
    }

    public void setInplaceSelect2(String inplaceSelect2) {
        this.inplaceSelect2 = inplaceSelect2;
    }

    public String getInputNumberSlider1() {
        return inputNumberSlider1;
    }

    public void setInputNumberSlider1(String inputNumberSlider1) {
        this.inputNumberSlider1 = inputNumberSlider1;
    }

    public String getInputNumberSlider2() {
        return inputNumberSlider2;
    }

    public void setInputNumberSlider2(String inputNumberSlider2) {
        this.inputNumberSlider2 = inputNumberSlider2;
    }

    public String getInputNumberSpinner1() {
        return inputNumberSpinner1;
    }

    public void setInputNumberSpinner1(String inputNumberSpinner1) {
        this.inputNumberSpinner1 = inputNumberSpinner1;
    }

    public String getInputNumberSpinner2() {
        return inputNumberSpinner2;
    }

    public void setInputNumberSpinner2(String inputNumberSpinner2) {
        this.inputNumberSpinner2 = inputNumberSpinner2;
    }

    public String getSimpleText1() {
        return simpleText1;
    }

    public void setSimpleText1(String simpleText1) {
        this.simpleText1 = simpleText1;
    }

    public String getSimpleText2() {
        return simpleText2;
    }

    public void setSimpleText2(String simpleText2) {
        this.simpleText2 = simpleText2;
    }
}
