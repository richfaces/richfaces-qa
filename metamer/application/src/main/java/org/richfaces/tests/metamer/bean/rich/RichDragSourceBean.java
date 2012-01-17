/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIDragSource;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.drag.DragValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richDragSourceBean")
@ViewScoped
public class RichDragSourceBean implements Serializable {

    private static final int DRAG_VALUES = 3;
    private static final long serialVersionUID = 4008175400649809L;
    private static Logger logger;
    private Attributes attributes;

    private List<DragValue> dragValues = new ArrayList<DragValue>();

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIDragSource.class, getClass());

        attributes.setAttribute("dragIndicator", "indicator");
        attributes.setAttribute("type", "drg1");
        attributes.setAttribute("rendered", true);
        
        attributes.remove("dragValue");
        attributes.remove("event");
        

        for (int i = 0; i < DRAG_VALUES; i++) {
            dragValues.add(new DragValue(i + 1));
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<DragValue> getDragValues() {
        return dragValues;
    }

    public void setDragValues(List<DragValue> dragValues) {
        this.dragValues = dragValues;
    }
}
