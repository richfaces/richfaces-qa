/**
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
 */
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.Size;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.misc.placeholder.UIPlaceholder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "richPlaceholderBean")
@ViewScoped
public class RichPlaceholderBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(RichPlaceholderBean.class);
    private Attributes attributes;
    private Object inputValue1;
    private Object inputValue2;
    @Size(min = 3, max = 6)
    private String validatedInput;

    @PostConstruct
    public void init() {
        LOG.info("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIPlaceholder.class, getClass());

        attributes.setAttribute("value", "Watermark text");
        attributes.setAttribute("rendered", "true");
        attributes.setAttribute("styleClass", "customPlaceholderClass");
        attributes.setAttribute("selector", "");

        attributes.remove("converter");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Object getInputValue1() {
        return inputValue1;
    }

    public void setInputValue1(Object inputValue1) {
        this.inputValue1 = inputValue1;
    }

    public Object getInputValue2() {
        return inputValue2;
    }

    public void setInputValue2(Object inputValue2) {
        this.inputValue2 = inputValue2;
    }

    public String getValidatedInput() {
        return validatedInput;
    }

    public void setValidatedInput(String validatedInput) {
        this.validatedInput = validatedInput;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}