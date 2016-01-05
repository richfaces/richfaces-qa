/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.component.UIPanelMenu;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.RichBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:panelMenu.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richPanelMenuBean")
@SessionScoped
public class RichPanelMenuBean implements Serializable {

    private static Logger logger;
    private static final long serialVersionUID = -1L;

    private Attributes attributes;
    private String currentItem;
    private Map<String, Boolean> expanded = new HashMap<String, Boolean>();

    public Attributes getAttributes() {
        return attributes;
    }

    public String getCurrentItem() {
        return currentItem;
    }

    public Map<String, Boolean> getExpanded() {
        return expanded;
    }

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIPanelMenu.class, getClass());

        attributes.setAttribute("rendered", true);
        attributes.setAttribute("style", "width: 200px;");

        // set default values for group and item mode, needed for MyFaces -- https://issues.jboss.org/browse/RF-10608
        attributes.setAttribute("itemMode", "ajax");
        attributes.setAttribute("groupMode", "client");

        // will be tested in another way
        attributes.remove("itemChangeListener");
        attributes.remove("converter");
    }

    /**
     * An item change listener that logs to the page old and new value.
     *
     * @param event an event representing the activation of a user interface component
     */
    public void itemChangeListener(ItemChangeEvent event) {
        currentItem = event.getNewItemName();
        RichBean.logToPage("* item changed: " + (event.getOldItem() == null ? null : event.getOldItem().getId())
            + " -> " + event.getNewItem().getId());
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
