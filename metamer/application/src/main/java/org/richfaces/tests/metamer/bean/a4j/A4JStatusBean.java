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
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIStatus;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:status.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "a4jStatusBean")
@ViewScoped
public class A4JStatusBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    private String facetStartValue = "START";
    private String facetStopValue = "STOP";
    private String facetErrorValue = "ERROR";

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIStatus.class, getClass());
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("startText", "start text");
        attributes.setAttribute("stopText", "stop text");
        attributes.setAttribute("errorText", "error text");

        // hidden attributes
        attributes.remove("converter");
        attributes.remove("localValue");
        attributes.remove("value");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getFacetStartValue() {
        return facetStartValue;
    }

    public void setFacetStartValue(String facetStartValue) {
        this.facetStartValue = facetStartValue;
    }

    public String getFacetStopValue() {
        return facetStopValue;
    }

    public void setFacetStopValue(String facetStopValue) {
        this.facetStopValue = facetStopValue;
    }

    public String getFacetErrorValue() {
        return facetErrorValue;
    }

    public void setFacetErrorValue(String facetErrorValue) {
        this.facetErrorValue = facetErrorValue;
    }
}
