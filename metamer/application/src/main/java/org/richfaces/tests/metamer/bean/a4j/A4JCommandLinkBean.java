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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.richfaces.component.UICommandLink;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:commandLink.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "a4jLinkBean")
@ViewScoped
public class A4JCommandLinkBean implements Serializable {

    private static final long serialVersionUID = -2556076843426776944L;
    private static final int ACTION_STRING_LENGTH = 6;
    private static Logger logger;
    private Attributes attributes;
    private String input;
    private String input2;
    private String input3;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UICommandLink.class, getClass());

        attributes.setAttribute("value", "command link");
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("action", "first6CharsAction");
        attributes.setAttribute("actionListener", "toUpperCaseActionListener");
        attributes.setAttribute("render", "output1 output2 output3");

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

    /**
     * Getter for input.
     * 
     * @return value entered by a user
     */
    public String getInput() {
        return input;
    }

    /**
     * Getter for input2.
     * 
     * @return value entered by a user modified by selected action
     */
    public String getInput2() {
        return input2;
    }

    /**
     * Getter for input3.
     * 
     * @return value entered by a user modified by selected action listener
     */
    public String getInput3() {
        return input3;
    }

    /**
     * Setter for input.
     * 
     * @param input
     *            value which user entered into text input on the page
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * An action that takes the first six characters from input and stores it to input2.
     * 
     * @return null since no navigation should be performed
     */
    public String first6CharsAction() {
        if (input == null) {
            return "";
        } else {
            int endIndex = input.length() > ACTION_STRING_LENGTH ? ACTION_STRING_LENGTH : input.length();
            input2 = (String) input.subSequence(0, endIndex);
            return null;
        }
    }

    /**
     * An action that takes user's input, doubles it and stores it to input2.
     * 
     * @return null since no navigation should be performed
     */
    public String doubleStringAction() {
        if (input == null) {
            input2 = "";
        } else {
            input2 = input.concat(input);
        }

        return null;
    }

    /**
     * An action that takes user's input, converts it to upper case and stores it to input2.
     * 
     * @return null since no navigation should be performed
     */
    public String toUpperCaseAction() {
        if (input == null) {
            return "";
        } else {
            input2 = input.toUpperCase();
            return null;
        }
    }

    /**
     * An action listener that takes the first six characters from input and stores it to input3.
     * 
     * @param event
     *            an event representing the activation of a user interface component (not used)
     */
    public void first6CharsActionListener(ActionEvent event) {
        if (input == null) {
            input3 = "";
        } else {
            int endIndex = input.length() > ACTION_STRING_LENGTH ? ACTION_STRING_LENGTH : input.length();
            input3 = (String) input.subSequence(0, endIndex);
        }
    }

    /**
     * An action listener that takes user's input, doubles it and stores it to input3.
     * 
     * @param event
     *            an event representing the activation of a user interface component (not used)
     */
    public void doubleStringActionListener(ActionEvent event) {
        if (input == null) {
            input3 = "";
        } else {
            input3 = input.concat(input);
        }
    }

    /**
     * An action listener that takes user's input, converts it to upper case and stores it to input3.
     * 
     * @param event
     *            an event representing the activation of a user interface component (not used)
     */
    public void toUpperCaseActionListener(ActionEvent event) {
        if (input == null) {
            input3 = "";
        } else {
            input3 = input.toUpperCase();
        }
    }
}
