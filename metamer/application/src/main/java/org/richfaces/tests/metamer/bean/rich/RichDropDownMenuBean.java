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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.richfaces.component.UIDropDownMenu;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:dropDownMenu.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version$Revision: 23042$
 */
@ManagedBean(name = "richDropDownMenuBean")
@ViewScoped
public class RichDropDownMenuBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    private String current;

    private List<SelectItemGroup> dynamicMenuGroups = new ArrayList<SelectItemGroup>();

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIDropDownMenu.class, getClass());

        attributes.setAttribute("mode", "ajax");
        attributes.setAttribute("rendered", true);
        initializeDynamicMenuGroups();
    }

    /**
     * This method/actionListener change list of available tabs (from empty to initialized and back)
     */
    public void changeTabList() {
        if (dynamicMenuGroups.isEmpty()) {
            initializeDynamicMenuGroups();
        } else {
            dynamicMenuGroups = new ArrayList<SelectItemGroup>();
        }
    }

    private void initializeDynamicMenuGroups() {
        dynamicMenuGroups.add(new SelectItemGroup("Group1", "Menu Group 1", false, new SelectItem[] {}));
        dynamicMenuGroups.add(new SelectItemGroup("Group2", "Menu Group 2", false, new SelectItem[] {}));
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getCurrent() {
        return this.current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String doNew() {
        this.current = "New";
        return null;
    }

    public String doOpen() {
        this.current = "Open";
        return null;
    }

    public String doClose() {
        this.current = "Close";
        return null;
    }

    public String doSave() {
        this.current = "Save";
        return null;
    }

    public String doSaveAll() {
        this.current = "Save All";
        return null;
    }

    public String doExit() {
        this.current = "Exit";
        return null;
    }

    public List<SelectItemGroup> getDynamicMenuGroups() {
        return dynamicMenuGroups;
    }

    public void setDynamicMenuGroups(List<SelectItemGroup> dynamicMenuGroups) {
        this.dynamicMenuGroups = dynamicMenuGroups;
    }
}
