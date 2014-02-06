/**
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
 */
package org.richfaces.tests.metamer.bean;

import static java.lang.Boolean.FALSE;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.richfaces.tests.metamer.converter.SwitchableFailingConverter;

/**
 * Bean used for testing of input components @converter and @converterMessage
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "converterBean")
@SessionScoped
public class ConverterBean implements Serializable {

    public static final String DEFAULT_VALUE = "";
    private static final long serialVersionUID = -1L;
    @ManagedProperty("#{switchableFailingConverter}")
    private SwitchableFailingConverter converter;

    private Object converterValue = DEFAULT_VALUE;

    private List<SelectItem> items = Lists.newArrayList(new SelectItem("", ""), new SelectItem("VALUE", "VALUE"));
    private List<String> converterList = Lists.newArrayList("", "VALUE");
    private List<String> converterValueList = Lists.newArrayList();
    private String converterMessage;
    private boolean failing = FALSE;

    public SwitchableFailingConverter getConverter() {
        return converter;
    }

    public List<String> getConverterList() {
        return converterList;
    }

    public String getConverterMessage() {
        return converterMessage;
    }

    public Object getConverterValue() {
        return converterValue;
    }

    public List<String> getConverterValueList() {
        return converterValueList;
    }

    public List<SelectItem> getItems() {
        return items;
    }

    public boolean isFailing() {
        return failing;
    }

    public void setConverter(SwitchableFailingConverter converter) {
        this.converter = converter;
    }

    public void setConverterList(List<String> converterList) {
        this.converterList = converterList;
    }

    public void setConverterMessage(String converterMessage) {
        this.converterMessage = converterMessage;
    }

    public void setConverterValue(Object converterValue) {
        this.converterValue = converterValue;
    }

    public void setConverterValueList(List<String> converterValueList) {
        this.converterValueList = converterValueList;
    }

    public void setFailing(boolean failing) {
        this.converterValue = "";//reset the value
        this.failing = failing;
        this.converter.setFailing(failing);
    }

    public void setItems(List<SelectItem> items) {
        this.items = items;
    }
}
