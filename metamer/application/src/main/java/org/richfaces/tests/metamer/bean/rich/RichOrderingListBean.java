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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.convert.Converter;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.ui.select.UIOrderingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple bean for rich:orderingList component example.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@ManagedBean(name = "richOrderingListBean")
@ViewScoped
public class RichOrderingListBean implements Serializable {

    private static final long serialVersionUID = 5868941019675985273L;
    private static Logger logger;
    @ManagedProperty("#{model.capitals}")
    private List<Capital> capitals;
    private Attributes attributes;
    private Collection<String> hiddenAttributes = new ArrayList<String>();
    @ManagedProperty("#{capitalConverter}")
    private Converter converter;

    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.info("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIOrderingList.class, getClass());

        attributes.setAttribute("columnVar", "capital");
        attributes.setAttribute("converter", getConverter());
        attributes.setAttribute("downText", "Down");
        attributes.setAttribute("downBottomText", "Last");
        attributes.setAttribute("listWidth", 300);
        attributes.setAttribute("listHeight", 500);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("var", "capital");
        attributes.setAttribute("upText", "Up");
        attributes.setAttribute("upTopText", "First");

        String[] attrsToHide = new String[] { "itemLabel", "itemValue", "value", "var",
            // TODO has to be tested in another way
            "converter", "converterMessage", "validator", "validatorMessage", "valueChangeListener" };
        for (String attribute : attrsToHide) {
            hiddenAttributes.add(attribute);
            attributes.remove(attribute);
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public List<Capital> getCapitals() {
        return capitals;
    }

    public Converter getConverter() {
        return converter;
    }

    public Collection<String> getHiddenAttributes() {
        return hiddenAttributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public void setHiddenAttributes(Collection<String> hiddenAttributes) {
        this.hiddenAttributes = hiddenAttributes;
    }

}
