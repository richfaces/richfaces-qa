/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.menu.UIContextMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:contextMenu
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
@ManagedBean(name = "richContextMenuBean")
@ViewScoped
public class RichContextMenuBean implements Serializable {

    /** Generated serial version UID */
    private static final long serialVersionUID = 1323565239250527128L;
    private static Logger log;

    private Attributes attributes;
    private String current;

    @PostConstruct
    public void init() {
        log = LoggerFactory.getLogger(getClass());
        log.debug("ContextMenu attributes initialized.");
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIContextMenu.class, getClass());
        attributes.setAttribute("disabled", "false");
        attributes.setAttribute("rendered", "true");
        attributes.setAttribute("hideDelay", "600");
        attributes.setAttribute("target", "targetPanel1");
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

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

}
