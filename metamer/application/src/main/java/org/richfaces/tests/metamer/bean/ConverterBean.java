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
package org.richfaces.tests.metamer.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.IntegerConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.richfaces.component.UICalendar;
import org.richfaces.component.UIOrderingList;
import org.richfaces.component.UIPickList;
import org.richfaces.component.html.HtmlInputNumberSlider;
import org.richfaces.component.html.HtmlInputNumberSpinner;
import org.richfaces.tests.metamer.bean.rich.RichCalendarBean;
import org.richfaces.tests.metamer.converter.CapitalConverter;
import org.richfaces.tests.metamer.model.Capital;

/**
 * Bean used for testing of input components @converter and @converterMessage
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "converterBean")
@SessionScoped
public class ConverterBean implements Serializable {

    private static final DateTimeZone DATETIME_ZONE = DateTimeZone.forTimeZone(RichCalendarBean.TIME_ZONE);
    public static final String ERROR_MSG = "Intentional failure of the converter";
    private static final long serialVersionUID = -1L;

    private List<Capital> capitals;
    private SwitchableFailingConverter converter;

    private String converterMessage;
    private boolean failing;
    @ManagedProperty(value = "#{model.capitals}")
    private List<Capital> originalCapitals;

    private Capital valueCapital;
    private Date valueDate;
    private List<Capital> valueListOfCapitals;
    private Number valueNumber;

    public List<Capital> getCapitals() {
        return capitals;
    }

    public Converter getConverter() {
        return converter;
    }

    public String getConverterMessage() {
        return converterMessage;
    }

    public List<Capital> getOriginalCapitals() {
        return originalCapitals;
    }

    public Object getOutput() {
        switch (converter.preferedOutput) {
            case CAPITAL:
                return valueCapital;
            case DATE:
                return new DateTime(valueDate).withZone(DATETIME_ZONE).toString(DateTimeFormat.mediumDateTime());
            case LIST_OF_CAPITALS:
                return valueListOfCapitals;
            case NUMBER:
                return valueNumber;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Capital getValueCapital() {
        return valueCapital;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public List<Capital> getValueListOfCapitals() {
        return valueListOfCapitals;
    }

    public Number getValueNumber() {
        return valueNumber;
    }

    @PostConstruct
    public void init() {
        converter = new SwitchableFailingConverter();
        capitals = originalCapitals.subList(0, 10);
        valueCapital = originalCapitals.get(0);
        valueDate = new Date(0);
        valueListOfCapitals = originalCapitals.subList(0, 1);
        valueNumber = 5;
    }

    public boolean isFailing() {
        return failing;
    }

    public void setConverterMessage(String converterMessage) {
        this.converterMessage = converterMessage;
    }

    public void setFailing(boolean failing) {
        this.failing = failing;
    }

    public void setOriginalCapitals(List<Capital> originalCapitals) {
        this.originalCapitals = originalCapitals;
    }

    public void setValueCapital(Capital valueCapital) {
        this.valueCapital = valueCapital;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public void setValueListOfCapitals(List<Capital> valueListOfCapitals) {
        this.valueListOfCapitals = valueListOfCapitals;
    }

    public void setValueNumber(Number valueNumber) {
        this.valueNumber = valueNumber;
    }

    private enum PreferedOutput {

        DATE, LIST_OF_CAPITALS, NUMBER, CAPITAL
    }

    private class SwitchableFailingConverter implements Converter {

        private PreferedOutput preferedOutput;
        private final DateTimeConverter dateTimeConverter = new DateTimeConverter();
        private final CapitalConverter capitalConverter = new CapitalConverter();
        private final IntegerConverter integerConverter = new IntegerConverter();
        private final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_MSG, ERROR_MSG);

        public SwitchableFailingConverter() {
            dateTimeConverter.setPattern(UICalendar.DEFAULT_DATE_PATTERN);
            dateTimeConverter.setTimeZone(RichCalendarBean.TIME_ZONE);
        }

        private Converter getConverter() {
            switch (preferedOutput) {
                case CAPITAL:
                    return capitalConverter;
                case DATE:
                    return dateTimeConverter;
                case LIST_OF_CAPITALS:
                    return capitalConverter;
                case NUMBER:
                    return integerConverter;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        @Override
        public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
            initPreferedOutput(uic);
            if (failing) {
                throw new ConverterException(message);
            }
            return getConverter().getAsObject(fc, uic, trimParagraph(string));
        }

        @Override
        public String getAsString(FacesContext fc, UIComponent uic, Object o) {
            initPreferedOutput(uic);
            if (failing) {
                throw new ConverterException(message);
            }
            return getConverter().getAsString(fc, uic, o);
        }

        private String trimParagraph(String value) {
            return value.replace("<p>", "").replace("</p>", "").trim();
        }

        private void initPreferedOutput(UIComponent c) {
            if (c instanceof UICalendar) {
                preferedOutput = PreferedOutput.DATE;
            } else if (c instanceof UIPickList || c instanceof UIOrderingList) {
                preferedOutput = PreferedOutput.LIST_OF_CAPITALS;
            } else if (c instanceof HtmlInputNumberSpinner || c instanceof HtmlInputNumberSlider) {
                preferedOutput = PreferedOutput.NUMBER;
            } else {
                preferedOutput = PreferedOutput.CAPITAL;
            }
        }
    }
}
