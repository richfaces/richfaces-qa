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
package org.richfaces.tests.metamer.bean.a4j;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIRegion;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:region.
 *
 * @author Exadel, Nick Belaevski, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22620 $
 */
@ManagedBean(name = "a4jRegionBean")
@SessionScoped
public class A4JRegionBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final SelectItem[] AVAILABLE_EXECUTE_OPTIONS = new SelectItem[]{new SelectItem(null, "default"),
        new SelectItem("@all"), new SelectItem("@form"), new SelectItem("@region"), new SelectItem("@this"),
        new SelectItem("outerValueInput", "Outer"), new SelectItem("regionValueInput", "Region"),
        new SelectItem("nestedRegionValueInput", "Nested region"),
        new SelectItem("decorationValueInput", "Decoration"), new SelectItem("insertionValueInput", "Insertion")};
    private static Logger logger;
    // for page simple.xhtml
    private Attributes attributes;
    private Employee user1;
    private Employee user2;
    // for page nested.xhtml
    private String execute;
    private String nestedExecute;
    private String outerExecute;
    private String decorationExecute;
    private String decorationValue;
    private String insertionExecute;
    private String outerValue;
    private String regionValue;
    private String nestedRegionValue;
    private String lastExecutedButtonValue;
    private String insertionValue;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        user1 = new Employee();
        user2 = new Employee();

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIRegion.class, getClass());
        attributes.setAttribute("rendered", true);
    }

    // getters and setters for page simple.xhtml
    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Employee getUser1() {
        return user1;
    }

    public void setUser1(Employee user1) {
        this.user1 = user1;
    }

    public Employee getUser2() {
        return user2;
    }

    public void setUser2(Employee user2) {
        this.user2 = user2;
    }

    // getters and setters for page nested.xhtml
    public SelectItem[] getExecuteOptions() {
        return AVAILABLE_EXECUTE_OPTIONS;
    }

    public String getOuterValue() {
        return outerValue;
    }

    public void setOuterValue(String outerValue) {
        this.outerValue = outerValue;
    }

    public String getRegionValue() {
        return regionValue;
    }

    public void setRegionValue(String value) {
        this.regionValue = value;
    }

    public String getNestedRegionValue() {
        return nestedRegionValue;
    }

    public void setNestedRegionValue(String nestedValue) {
        this.nestedRegionValue = nestedValue;
    }

    public String getExecute() {
        return execute;
    }

    public void setExecute(String execute) {
        this.execute = execute;
    }

    public String getNestedExecute() {
        return nestedExecute;
    }

    public void setNestedExecute(String nestedExecute) {
        this.nestedExecute = nestedExecute;
    }

    public String getOuterExecute() {
        return outerExecute;
    }

    public void setOuterExecute(String outerExecute) {
        this.outerExecute = outerExecute;
    }

    public String getInsertionExecute() {
        return insertionExecute;
    }

    public void setInsertionExecute(String insertionExecute) {
        this.insertionExecute = insertionExecute;
    }

    public String getInsertionValue() {
        return insertionValue;
    }

    public void setInsertionValue(String insertionValue) {
        this.insertionValue = insertionValue;
    }

    public String getDecorationExecute() {
        return decorationExecute;
    }

    public void setDecorationExecute(String decorationExecute) {
        this.decorationExecute = decorationExecute;
    }

    public String getDecorationValue() {
        return decorationValue;
    }

    public void setDecorationValue(String decorationValue) {
        this.decorationValue = decorationValue;
    }

    public void handleBehavior(AjaxBehaviorEvent event) {
        lastExecutedButtonValue = (String) event.getComponent().getAttributes().get("value");
    }

    public String getLastExecutedButtonValue() {
        return lastExecutedButtonValue;
    }

    public void handleDefaultsValueChange(ValueChangeEvent event) {
        String newValue = (String) event.getNewValue();

        setOuterExecute(newValue);
        setExecute(newValue);
        setDecorationExecute(newValue);
        setInsertionExecute(newValue);
        setNestedExecute(newValue);
    }
}
