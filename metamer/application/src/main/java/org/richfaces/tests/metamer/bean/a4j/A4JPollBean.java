/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.bean.a4j;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.ajax.poll.UIPoll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:poll.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "a4jPollBean")
@ViewScoped
public class A4JPollBean implements Serializable {

    private static final long serialVersionUID = 4810889475400649809L;
    private static Logger logger;
    private Attributes attributes;
    private int counter;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIPoll.class, getClass());

        attributes.setAttribute("enabled", true);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("interval", 2500);
        attributes.setAttribute("action", "increaseCounterAction");
        attributes.setAttribute("actionListener", "increaseCounterActionListener");
    }

    /**
     * Getter for attributes.
     *
     * @return A map containing all attributes of tested component. Name of the component is key in the map.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * Setter for attributes.
     *
     * @param attributes
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Date getDate() {
        return new Date();
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String increaseCounterAction() {
        counter++;
        return null;
    }

    public String decreaseCounterAction() {
        counter--;
        return null;
    }

    public void increaseCounterActionListener(ActionEvent event) {
        counter++;
    }

    public void decreaseCounterActionListener(ActionEvent event) {
        counter--;
    }
}
