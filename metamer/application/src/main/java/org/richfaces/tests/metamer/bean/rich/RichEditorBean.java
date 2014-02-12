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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.richfaces.component.UIEditor;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for storing appropriate data for rich:editor component
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
@ManagedBean(name = "richEditorBean")
@ViewScoped
public class RichEditorBean implements Serializable {

    public static final Logger LOG = LoggerFactory.getLogger(RichEditorBean.class);
    /** Generated serial UID */
    private static final long serialVersionUID = -6029256813894981250L;

    private Attributes attributes;

    @PostConstruct
    public void init() {
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIEditor.class, getClass());

        attributes.setAttribute("rendered", Boolean.TRUE);
        attributes.setAttribute("requiredMessage", "Editor's value cannot be empty!");
        attributes.setAttribute("toolbar", "basic");
        attributes.setAttribute("skin", "richfaces");

        attributes.remove("config"); // config facet covered with @toolbar=custom
        attributes.remove("converter");
        attributes.remove("converterMessage");
        attributes.remove("validator");
        attributes.remove("validatorMessage");
    }

    /**
     * Test method for verify validator attribute
     * @param context
     * @param component
     * @param value
     */
    public void validateEditorValue(FacesContext context, UIComponent component, Object value) {
        String testValue = (value == null ? null : value.toString());
        if (testValue != null && testValue.contains("#")) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Editor must not contain '#' value!", "Editor must not contain '#' value!"));
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

}
