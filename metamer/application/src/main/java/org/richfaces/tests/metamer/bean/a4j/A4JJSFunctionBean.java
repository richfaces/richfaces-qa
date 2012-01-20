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
package org.richfaces.tests.metamer.bean.a4j;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.richfaces.component.UIFunction;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:jsFunction.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version$Revision: 22460$
 */
@ManagedBean(name = "a4jJSFunctionBean")
// should not be view-scoped (see https://jira.jboss.org/browse/RF-9287)
@ViewScoped
public class A4JJSFunctionBean implements Serializable {

    private static final long serialVersionUID = 48333649809L;
    private static Logger logger;
    private Attributes attributes;
    private int year;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());
        year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIFunction.class, getClass());

        attributes.setAttribute("name", "metamerFunction");
        attributes.setAttribute("render", "time1, time2, year");
        attributes.setAttribute("rendered", true);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String increaseYearAction() {
        year++;
        return null;
    }

    public String decreaseYearAction() {
        year--;
        return null;
    }

    public void increaseYearActionListener(ActionEvent event) {
        year++;
    }

    public void decreaseYearActionListener(ActionEvent event) {
        year--;
    }
}
