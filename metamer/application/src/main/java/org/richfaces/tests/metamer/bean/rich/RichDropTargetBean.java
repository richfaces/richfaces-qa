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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIDropTarget;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.drag.DragValue;
import org.richfaces.tests.metamer.model.drag.DropValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richDropTargetBean")
@ViewScoped
public class RichDropTargetBean implements Serializable {

    private static final long serialVersionUID = 4008175400649809L;
    private static Logger logger;
    private Attributes attributes;

    private Map<DropValue, DragValue> droppedValues1 = new LinkedHashMap<DropValue, DragValue>();
    private Map<DropValue, DragValue> droppedValues2 = new LinkedHashMap<DropValue, DragValue>();
    private DropValue dropValue;
    private int dropValueCounter = 1;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIDropTarget.class, getClass());

        attributes.setAttribute("acceptedTypes", "drg1, drg2");
        attributes.setAttribute("render", "droppable1 droppable2");
        attributes.setAttribute("rendered", true);

        attributes.remove("actionExpression");
        attributes.remove("action");
        attributes.remove("actionListener");
        attributes.remove("dropListener");
        attributes.remove("dropValue");

        increaseDropValue();
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Map<DropValue, DragValue> getDroppedValues1() {
        return droppedValues1;
    }

    public Map<DropValue, DragValue> getDroppedValues2() {
        return droppedValues2;
    }

    public void increaseDropValue() {
        dropValue = new DropValue(dropValueCounter++);
    }

    public DropValue getDropValue() {
        return dropValue;
    }

    public List<Entry<DropValue, DragValue>> getDroppedEntries1() {
        List<Entry<DropValue, DragValue>> list = new LinkedList<Entry<DropValue, DragValue>>(droppedValues1.entrySet());
        Collections.reverse(list);
        return list;
    }

    public List<Entry<DropValue, DragValue>> getDroppedEntries2() {
        List<Entry<DropValue, DragValue>> list = new LinkedList<Entry<DropValue, DragValue>>(droppedValues2.entrySet());
        Collections.reverse(list);
        return list;
    }
}
