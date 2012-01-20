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
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIProgressBar;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:progressBar.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version$Revision: 22460$
 */
@ManagedBean(name = "richProgressBarBean")
@ViewScoped
public class RichProgressBarBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    private boolean buttonRendered = true;
    private Long startTime;
    private boolean initialFacetRendered = true;
    private boolean finishFacetRendered = true;
    private boolean childrenRendered = false;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIProgressBar.class, getClass());

        attributes.setAttribute("maxValue", 100);
        attributes.setAttribute("minValue", 0);
        attributes.setAttribute("interval", 500);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("value", -1);

        // attributes tested in another way
        attributes.remove("mode");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String startProcess() {
        attributes.setAttribute("enabled", true);
        buttonRendered = false;
        setStartTime(new Date().getTime());
        return null;
    }

    public Long getCurrentValue() {
        if (Boolean.TRUE.equals(attributes.get("enabled").getValue())) {
            Long current = (new Date().getTime() - startTime) / 500;
            if (current >= 100) {
                buttonRendered = true;
            } else if (current.equals(0L)) {
                return 1L;
            }
            return (new Date().getTime() - startTime) / 500;
        }
        if (startTime == null) {
            return -1L;
        } else {
            return 101L;
        }
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public boolean isButtonRendered() {
        return buttonRendered;
    }

    public void setButtonRendered(boolean buttonRendered) {
        this.buttonRendered = buttonRendered;
    }

    public boolean isFinishFacetRendered() {
        return finishFacetRendered;
    }

    public void setFinishFacetRendered(boolean finishFacetRendered) {
        this.finishFacetRendered = finishFacetRendered;
    }

    public boolean isInitialFacetRendered() {
        return initialFacetRendered;
    }

    public void setInitialFacetRendered(boolean initialFacetRendered) {
        this.initialFacetRendered = initialFacetRendered;
    }

    public boolean isChildrenRendered() {
        return childrenRendered;
    }

    public void setChildrenRendered(boolean childrenRendered) {
        this.childrenRendered = childrenRendered;
    }
}
