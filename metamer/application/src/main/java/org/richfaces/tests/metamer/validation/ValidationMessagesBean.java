/**
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
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
 */
package org.richfaces.tests.metamer.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean
@ViewScoped
public class ValidationMessagesBean implements Serializable {

    private static final long serialVersionUID = -8259550540482799013L;
    private final long pastTime = System.currentTimeMillis() - 4 * 24 * 3600 * 1000;
    private final long futureTime = System.currentTimeMillis() + 4 * 24 * 3600 * 1000;
    private Date pastDate = new Date(pastTime);
    private Date futureDate = new Date(futureTime);
    public static final String MSG = "Custom validator error message from bean.";
    //JSR
    @Min(value = 2, message = MSG)
    @Max(value = 9, message = MSG)
    private Integer valueWithMessagesJSR = new Integer(5);
    @Min(2)
    @Max(9)
    private Integer valueWithoutMessagesJSR = new Integer(5);
    @Min(2)
    @Max(9)
    private Integer valueWithoutMessages2JSR = new Integer(5);
    @Past(message = MSG)
    private Date dateWithMessagesJSR = new Date(pastTime);
    @Past
    private Date dateWithoutMessagesJSR = new Date(pastTime);
    @Past
    private Date dateWithoutMessages2JSR = new Date(pastTime);
    //CSV
    @Min(value = 2, message = MSG)
    @Max(value = 9, message = MSG)
    private Integer valueWithMessagesCSV = new Integer(5);
    @Min(2)
    @Max(9)
    private Integer valueWithoutMessagesCSV = new Integer(5);
    @Min(2)
    @Max(9)
    private Integer valueWithoutMessages2CSV = new Integer(5);
    @Past(message = MSG)
    private Date dateWithMessagesCSV = new Date(pastTime);
    @Past
    private Date dateWithoutMessagesCSV = new Date(pastTime);
    @Past
    private Date dateWithoutMessages2CSV = new Date(pastTime);
    //JSF
    private Integer valueJSF = new Integer(5);
    private Integer value2JSF = new Integer(5);
    private Date dateJSF = new Date(pastTime);
    private Date date2JSF = new Date(pastTime);
    private final Converter converter = FacesContext.getCurrentInstance().getApplication().createConverter(Integer.class);
    private List<SelectItem> validationOptions = null;

    @PostConstruct
    public void init() {
        validationOptions = new ArrayList<SelectItem>();
        validationOptions.add(new SelectItem(5));
        validationOptions.add(new SelectItem(10));
    }

    public List<SelectItem> getValidationOptions() {
        return Collections.unmodifiableList(validationOptions);
    }

    public Converter getConverter() {
        return converter;
    }

    public String getNumberInputDescription() {
        return "All inputs expect: number from 2 to 9.";
    }

    public String getDateInputDescription() {
        return "All inputs expect: date from past.";
    }

    public void activate() {
        FacesContext.getCurrentInstance().getApplication().setMessageBundle("CustomErrorMessages");
    }

    public void deactivate() {
        FacesContext.getCurrentInstance().getApplication().setMessageBundle("DefaultErrorMessages");
    }

    public Integer getValueJSF() {
        return valueJSF;
    }

    public void setValueJSF(Integer valueJSF) {
        this.valueJSF = valueJSF;
    }

    public Integer getValueWithMessagesCSV() {
        return valueWithMessagesCSV;
    }

    public void setValueWithMessagesCSV(Integer valueWithMessagesCSV) {
        this.valueWithMessagesCSV = valueWithMessagesCSV;
    }

    public Integer getValueWithMessagesJSR() {
        return valueWithMessagesJSR;
    }

    public void setValueWithMessagesJSR(Integer valueWithMessagesJSR) {
        this.valueWithMessagesJSR = valueWithMessagesJSR;
    }

    public Integer getValueWithoutMessages2CSV() {
        return valueWithoutMessages2CSV;
    }

    public void setValueWithoutMessages2CSV(Integer valueWithoutMessages2CSV) {
        this.valueWithoutMessages2CSV = valueWithoutMessages2CSV;
    }

    public Integer getValueWithoutMessages2JSR() {
        return valueWithoutMessages2JSR;
    }

    public void setValueWithoutMessages2JSR(Integer valueWithoutMessages2JSR) {
        this.valueWithoutMessages2JSR = valueWithoutMessages2JSR;
    }

    public Integer getValueWithoutMessagesCSV() {
        return valueWithoutMessagesCSV;
    }

    public void setValueWithoutMessagesCSV(Integer valueWithoutMessagesCSV) {
        this.valueWithoutMessagesCSV = valueWithoutMessagesCSV;
    }

    public Integer getValueWithoutMessagesJSR() {
        return valueWithoutMessagesJSR;
    }

    public void setValueWithoutMessagesJSR(Integer valueWithoutMessagesJSR) {
        this.valueWithoutMessagesJSR = valueWithoutMessagesJSR;
    }

    public Integer getValue2JSF() {
        return value2JSF;
    }

    public void setValue2JSF(Integer value2JSF) {
        this.value2JSF = value2JSF;
    }

    public String printCurrentResourceBundle() {
        return FacesContext.getCurrentInstance().getApplication().getMessageBundle();
    }

    public Date getDate2JSF() {
        return date2JSF;
    }

    public void setDate2JSF(Date date2JSF) {
        this.date2JSF = date2JSF;
    }

    public Date getDateJSF() {
        return dateJSF;
    }

    public void setDateJSF(Date dateJSF) {
        this.dateJSF = dateJSF;
    }

    public Date getDateWithMessagesCSV() {
        return dateWithMessagesCSV;
    }

    public void setDateWithMessagesCSV(Date dateWithMessagesCSV) {
        this.dateWithMessagesCSV = dateWithMessagesCSV;
    }

    public Date getDateWithMessagesJSR() {
        return dateWithMessagesJSR;
    }

    public void setDateWithMessagesJSR(Date dateWithMessagesJSR) {
        this.dateWithMessagesJSR = dateWithMessagesJSR;
    }

    public Date getDateWithoutMessages2CSV() {
        return dateWithoutMessages2CSV;
    }

    public void setDateWithoutMessages2CSV(Date dateWithoutMessages2CSV) {
        this.dateWithoutMessages2CSV = dateWithoutMessages2CSV;
    }

    public Date getDateWithoutMessages2JSR() {
        return dateWithoutMessages2JSR;
    }

    public void setDateWithoutMessages2JSR(Date dateWithoutMessages2JSR) {
        this.dateWithoutMessages2JSR = dateWithoutMessages2JSR;
    }

    public Date getDateWithoutMessagesCSV() {
        return dateWithoutMessagesCSV;
    }

    public void setDateWithoutMessagesCSV(Date dateWithoutMessagesCSV) {
        this.dateWithoutMessagesCSV = dateWithoutMessagesCSV;
    }

    public Date getDateWithoutMessagesJSR() {
        return dateWithoutMessagesJSR;
    }

    public void setDateWithoutMessagesJSR(Date dateWithoutMessagesJSR) {
        this.dateWithoutMessagesJSR = dateWithoutMessagesJSR;
    }

    public Date getFutureDate() {
        return futureDate;
    }

    public Date getPastDate() {
        return pastDate;
    }

    public void setFutureDate(Date futureDate) {
        this.futureDate = futureDate;
    }

    public void setPastDate(Date pastDate) {
        this.pastDate = pastDate;
    }

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Date d = (Date) value;
        if (d.getTime() > System.currentTimeMillis()) {//must be in past
            throw new ValidatorException(new FacesMessage("Own default message."));
        }
    }
}
