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
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.misc.UIHotKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:hotKey.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision: 23135 $
 */
@ManagedBean(name = "richHotKeyBean")
@ViewScoped
public class RichHotKeyBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    private Attributes attributes2;
    private Collection<String> hiddenAttributes = new ArrayList<String>();

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIHotKey.class, getClass());
        attributes2 = Attributes.getComponentAttributesFromFacesConfig(UIHotKey.class, getClass());

        String[] attrsToHide = { "onkeydown", "onkeyup" };

        attributes.setAttribute("enabledInInput", true);
        attributes.setAttribute("key", "ctrl+x");
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("preventDefault", true);

        attributes2.setAttribute("enabledInInput", true);
        attributes2.setAttribute("key", "alt+x");
        attributes2.setAttribute("rendered", true);
        attributes2.setAttribute("preventDefault", true);

        for (String attrToHide : attrsToHide) {
            attributes.remove(attrToHide);
            attributes2.remove(attrToHide);
            hiddenAttributes.add(attrToHide);
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Attributes getAttributes2() {
        return attributes2;
    }

    public void setAttributes2(Attributes attributes) {
        this.attributes2 = attributes;
    }

    public Collection<String> getHiddenAttributes() {
        return hiddenAttributes;
    }

    public void setHiddenAttributes(Collection<String> hiddenAttributes) {
        this.hiddenAttributes = hiddenAttributes;
    }
}
