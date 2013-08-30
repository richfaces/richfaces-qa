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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIPopupPanel;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:popupPanel.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22630 $
 */
@ManagedBean(name = "richPopupPanelBean")
@ViewScoped
public class RichPopupPanelBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RichPopupPanelBean.class);
    private Attributes attributes;
    private String value1;
    private String value2;
    private String value3;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        LOGGER.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIPopupPanel.class, getClass());

        attributes.setAttribute("header", "popup panel header");
        attributes.setAttribute("height", 300);
        attributes.setAttribute("left", "auto");
        attributes.setAttribute("maxHeight", 500);
        attributes.setAttribute("maxWidth", 500);
        attributes.setAttribute("minHeight", 300);
        attributes.setAttribute("minWidth", 300);
        attributes.setAttribute("moveable", true);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("resizeable", true);
        attributes.setAttribute("top", "auto");
        attributes.setAttribute("trimOverlayedElements", true);
        attributes.setAttribute("width", 500);
        attributes.setAttribute("zindex", 4);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String save() {
        LOGGER.warn("1:  " + value1);
        LOGGER.warn("2:  " + value2);
        LOGGER.warn("3:  " + value3);
        return null;
    }
    
    public void waitingAction(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
