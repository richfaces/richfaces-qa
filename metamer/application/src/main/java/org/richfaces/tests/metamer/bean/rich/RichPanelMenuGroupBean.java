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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.RichBean;
import org.richfaces.ui.toggle.panelMenu.UIPanelMenuGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:panelMenu.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richPanelMenuGroupBean")
@ViewScoped
public class RichPanelMenuGroupBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    private Map<String, Boolean> expanded = new HashMap<String, Boolean>();

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIPanelMenuGroup.class, getClass());

        attributes.setAttribute("rendered", true);
        attributes.setAttribute("selectable", true);
        attributes.setAttribute("mode", "ajax");

        // already defined in source directly
        attributes.remove("name");
        attributes.remove("label");
        attributes.remove("changeExpandListener");
        attributes.remove("action");
        attributes.remove("actionListener");

        // expanded needs to be set separately
        attributes.remove("expanded");
        expanded.put("group2", true);
        expanded.put("group23", true);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Map<String, Boolean> getExpanded() {
        return expanded;
    }

    public void changeExpandListener(Object event) {
        RichBean.logToPage("* group expanded");
    }
}
