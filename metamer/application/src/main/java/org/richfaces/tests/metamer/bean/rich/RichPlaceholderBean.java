/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
import javax.validation.constraints.Min;
import org.richfaces.component.UIPlaceholder;
import org.richfaces.tests.metamer.Attributes;
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
    @Min(value = 3)
    private Integer intValue;

    @PostConstruct
    public void init() {
        LOG.info("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIPlaceholder.class, getClass());

        attributes.setAttribute("value", "Watermark text");
        attributes.setAttribute("rendered", "true");
        attributes.setAttribute("styleClass", "customPlaceholderClass");
        attributes.setAttribute("selector", "[id$=input1]");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Object getInputValue1() {
        return inputValue1;
    }

    public Object getInputValue2() {
        return inputValue2;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setInputValue1(Object inputValue1) {
        this.inputValue1 = inputValue1;
    }

    public void setInputValue2(Object inputValue2) {
        this.inputValue2 = inputValue2;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }
}
