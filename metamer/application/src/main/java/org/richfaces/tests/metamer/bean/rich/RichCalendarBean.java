/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.richfaces.component.UICalendar;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.RichBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:calendar.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22820 $
 */
@ManagedBean(name = "richCalendarBean")
@ViewScoped
public class RichCalendarBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = LoggerFactory.getLogger(RichCalendarBean.class);
    private Attributes attributes;
    private Date date = new Date();
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
    private TimeZone tz = TimeZone.getTimeZone("GMT");
    private Date date1;
    private Date date2;
    private Date date3;
    private Date currentDate;
    private Date value;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UICalendar.class, getClass());

        attributes.setAttribute("datePattern", "MMM d, yyyy HH:mm");
        attributes.setAttribute("direction", "bottomRight");
        attributes.setAttribute("jointPoint", "bottomLeft");
        attributes.setAttribute("popup", true);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("requiredMessage", "value is required");
        attributes.setAttribute("showApplyButton", true);
        attributes.setAttribute("showHeader", true);
        attributes.setAttribute("showFooter", true);
        attributes.setAttribute("showInput", true);
        attributes.setAttribute("showWeeksBar", true);
        attributes.setAttribute("showWeekDaysBar", true);

        // TODO has to be tested in another way
        attributes.remove("converter");
        attributes.remove("currentDate");
        attributes.remove("dataModel");
        attributes.remove("validator");
        attributes.remove("valueChangeListener");
        attributes.remove("timeZone");
        attributes.remove("value");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TimeZone getTimeZone() {
        return TIME_ZONE;
    }

    public TimeZone getTz() {
        return tz;
    }

    public void setTz(TimeZone tz) {
        this.tz = tz;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    @Past
    @NotNull
    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    @Future
    @NotNull
    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    /**
     * A value change listener that logs to the page old and new value.
     *
     * @param event
     *            an event representing the activation of a user interface component
     */
    public void valueChangeListener(ValueChangeEvent event) {
        SimpleDateFormat sdf = new SimpleDateFormat(attributes.get("datePattern").getValue().toString());
        sdf.setTimeZone(TIME_ZONE);

        String oldDate = "null";
        String newDate = "null";

        if (event.getOldValue() != null) {
            if (event.getOldValue() instanceof String) {
                oldDate = (String) event.getOldValue();
            } else {
                oldDate = sdf.format((Date) event.getOldValue());
            }
        }
        if (event.getNewValue() != null) {
            if (event.getNewValue() instanceof String) {
                newDate = (String) event.getNewValue();
            } else {
                newDate = sdf.format((Date) event.getNewValue());
            }
        }

        RichBean.logToPage("* value changed: " + oldDate + " -> " + newDate);
    }

    public void validateDate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) value);
        int componentYear = cal.get(Calendar.YEAR);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date too far in the past.",
                "Select a date from year 1991 or newer.");

        if (componentYear < 1991) {
            FacesContext.getCurrentInstance().addMessage("form:calendar", message);
        }
    }

    // for preloadedDates.xhtml

    public Date getCurrentDateForPreloadedDates() {
        Calendar calendar = Calendar.getInstance(tz);
        calendar.set(Calendar.MONTH, 3);
        return calendar.getTime();
    }

    public Date getPreloadedDateRangeBegin() {
        Calendar calendar = Calendar.getInstance(tz);
        calendar.set(2012, 0, 1);
        return calendar.getTime();
    }

    public Date getPreloadedDateRangeEnd() {
        Calendar calendar = Calendar.getInstance(tz);
        calendar.set(2013, 1, 1);
        return calendar.getTime();
    }
}
