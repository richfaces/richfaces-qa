/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.richfaces.component.html.HtmlInputNumberSpinner;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:inputNumberSpinner.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richInputNumberSpinnerBean")
@ViewScoped
public class RichInputNumberSpinnerBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    private int value1;
    private int value2;
    private int value3;
    private double valueLocaleDE;
    private double valueLocaleEN;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(HtmlInputNumberSpinner.class, getClass());

        attributes.setAttribute("enableManualInput", true);
        attributes.setAttribute("maxValue", 10);
        attributes.setAttribute("minValue", -10);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("requiredMessage", "value is required");
        attributes.setAttribute("step", 1);
        attributes.setAttribute("value", 2);

        // will be tested in another way
        attributes.remove("valueChangeListener");
    }

    public double getValueLocaleDE() {
        return valueLocaleDE;
    }

    public void setValueLocaleDE(double valueLocaleDE) {
        this.valueLocaleDE = valueLocaleDE;
    }

    public double getValueLocaleEN() {
        return valueLocaleEN;
    }

    public void setValueLocaleEN(double valueLocaleEN) {
        this.valueLocaleEN = valueLocaleEN;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Min(2)
    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    @Max(2)
    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }
}
