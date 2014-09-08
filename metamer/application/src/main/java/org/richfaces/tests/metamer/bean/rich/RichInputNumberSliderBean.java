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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.component.html.HtmlInputNumberSlider;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.abstractions.NumberInputValidationBean;
import org.richfaces.tests.metamer.bean.abstractions.NumberInputValidationBeanImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:inputNumberSlider.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "richInputNumberSliderBean")
@SessionScoped
public class RichInputNumberSliderBean extends NumberInputValidationBeanImpl implements Serializable {

    private static final long serialVersionUID = 595154649809L;
    private static Logger logger = LoggerFactory.getLogger(RichInputNumberSliderBean.class);

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(HtmlInputNumberSlider.class, getClass());

        attributes.setAttribute("enableManualInput", true);
        attributes.setAttribute("inputSize", 3);
        attributes.setAttribute("maxlength", 10);
        attributes.setAttribute("maxValue", 10);
        attributes.setAttribute("minValue", -10);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("required", true);
        attributes.setAttribute("requiredMessage", NumberInputValidationBean.REQUIRED_VALIDATION_MSG);
        attributes.setAttribute("showBoundaryValues", true);
        attributes.setAttribute("showInput", true);
        attributes.setAttribute("step", 1);
        attributes.setAttribute("value", 2);
        attributes.setAttribute("delay", 200);

        // will be tested in another way
        attributes.remove("valueChangeListener");
    }
}
