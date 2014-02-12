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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.misc.jquery.UIJQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:jQuery.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>, Nick Belaevski
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richJQueryBean")
@ViewScoped
public class RichJQueryBean implements Serializable {

    private static final long serialVersionUID = 111475400649809L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RichJQueryBean.class);
    private Attributes attributes;
    private List<String> componentsDataList;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        LOGGER.debug("initializing bean " + getClass().getName());

        componentsDataList = new ArrayList<String>();
        addComponent();

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIJQuery.class, getClass());

        attributes.setAttribute("event", "click");
        attributes.setAttribute("name", "bubu");
        attributes.setAttribute("query", "metamerEvents += \"immediate attachment \"");
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("selector", "#jQueryTestButton");
        attributes.setAttribute("timing", "immediate");

        // TODO must be tested in other way
        attributes.remove("name");
    }

    public void addComponent() {
        componentsDataList.add(UUID.randomUUID().toString());
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public List<String> getComponentsDataList() {
        return componentsDataList;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
