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
import javax.faces.bean.ViewScoped;

import org.richfaces.component.html.HtmlInputNumberSpinner;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.abstractions.NumberInputValidationBean;
import org.richfaces.tests.metamer.bean.abstractions.NumberInputValidationBeanImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:inputNumberSpinner.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richInputNumberSpinnerBean")
@ViewScoped
public class RichInputNumberSpinnerBean extends NumberInputValidationBeanImpl implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger = LoggerFactory.getLogger(RichInputNumberSpinnerBean.class);
    private double valueLocaleDE;
    private double valueLocaleEN;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(HtmlInputNumberSpinner.class, getClass());

        attributes.setAttribute("enableManualInput", true);
        attributes.setAttribute("maxValue", 10);
        attributes.setAttribute("minValue", -10);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("required", true);
        attributes.setAttribute("requiredMessage", NumberInputValidationBean.REQUIRED_VALIDATION_MSG);
        attributes.setAttribute("step", 1);
        attributes.setAttribute("value", 2);

        // will be tested in another way
        attributes.remove("converter");
        attributes.remove("converterMessage");
        attributes.remove("validator");
        attributes.remove("validatorMessage");
        attributes.remove("valueChangeListener");
    }

    public double getValueLocaleDE() {
        return valueLocaleDE;
    }

    public double getValueLocaleEN() {
        return valueLocaleEN;
    }

    public void setValueLocaleDE(double valueLocaleDE) {
        this.valueLocaleDE = valueLocaleDE;
    }

    public void setValueLocaleEN(double valueLocaleEN) {
        this.valueLocaleEN = valueLocaleEN;
    }
}
