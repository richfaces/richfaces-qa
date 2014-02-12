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

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIOrderingList;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.Capital;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(RichOrderingListBean.class);
    @ManagedProperty("#{model.capitals}")
    private List<Capital> capitals;
    private List<Capital> emptyCapitals = Lists.newArrayList();
    private Attributes attributes;
    private Collection<String> hiddenAttributes = new ArrayList<String>();
    private String validatorMessage;

    @PostConstruct
    public void init() {
        LOGGER.debug("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIOrderingList.class, getClass());

        attributes.setAttribute("downText", "Down");
        attributes.setAttribute("downBottomText", "Bottom");
        attributes.setAttribute("listWidth", 300);
        attributes.setAttribute("listHeight", 500);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("upText", "Up");
        attributes.setAttribute("upTopText", "Top");
        attributes.setAttribute("required", false);

        // TODO has to be tested in another way
        String[] attrsToHide = new String[]{"collectionType", "itemLabel", "itemValue", "value", "var",
            "converter", "converterMessage", "validator", "validatorMessage", "valueChangeListener"};
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

    public List<Capital> getEmptyCapitals() {
        return emptyCapitals;
    }

    public Collection<String> getHiddenAttributes() {
        return hiddenAttributes;
    }

    public String getValidatorMessage() {
        return validatorMessage;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public void setEmptyCapitals(List<Capital> emptyCapitals) {
        this.emptyCapitals = emptyCapitals;
    }

    public void setHiddenAttributes(Collection<String> hiddenAttributes) {
        this.hiddenAttributes = hiddenAttributes;
    }

    public void setValidatorMessage(String validatorMessage) {
        this.validatorMessage = validatorMessage;
    }
}
