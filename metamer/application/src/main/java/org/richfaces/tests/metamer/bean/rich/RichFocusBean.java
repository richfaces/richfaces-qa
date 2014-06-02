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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.application.ServiceTracker;
import org.richfaces.component.UIFocus;
import org.richfaces.focus.FocusManager;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:focus.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richFocusBean")
@SessionScoped
public class RichFocusBean implements Serializable {

    private static final long serialVersionUID = 111475400679809L;
    private static Logger logger;
    private Attributes attributes;

    private String name;
    private Long age;
    private String address;

    private boolean preserve = false;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIFocus.class, getClass());

        attributes.setAttribute("ajaxRendered", true);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("validationAware", true);
        attributes.setAttribute("preserve", false);
        attributes.setAttribute("delayed", false);

        // TODO will be tested in another way
        attributes.remove("converter");
        attributes.remove("value");
    }

    public void preRenderView() {
        FocusManager focusManager = ServiceTracker.getService(FocusManager.class);
        focusManager.focus("age");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPreserve() {
        return preserve;
    }

    public void setPreserve(boolean preserve) {
        this.preserve = preserve;
    }
}
