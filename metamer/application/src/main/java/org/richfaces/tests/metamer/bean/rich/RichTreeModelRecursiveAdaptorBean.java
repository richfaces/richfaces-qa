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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.richfaces.component.UITreeModelRecursiveAdaptor;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.treeAdaptor.RecursiveNode;
import org.richfaces.tests.metamer.model.treeAdaptor.RecursiveNodeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version$Revision: 22460$
 */
@ManagedBean(name = "richTreeModelRecursiveAdaptorBean")
@ViewScoped
public class RichTreeModelRecursiveAdaptorBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger;
    private static List<RecursiveNode> rootNodes;

    private Attributes attributes;
    private AtomicReference<Boolean> leafChildrenNullable = new AtomicReference<Boolean>(true);
    private boolean useMapModel;
    private Map<String, Boolean> expanded = new TreeMap<String, Boolean>();
    private boolean rootNodesInitialized = false;

    /*
     * Nodes which was loaded lazily in the current request
     */
    private Set<String> lazyInitializedNodes = new LinkedHashSet<String>();

    public Set<String> getLazyInitializedNodes() {
        return lazyInitializedNodes;
    }

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UITreeModelRecursiveAdaptor.class, getClass());

        attributes.get("rendered").setValue(true);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<RecursiveNode> getRootNodes() {
        if (!rootNodesInitialized) {
            rootNodes = RecursiveNodeImpl.createChildren(true, "", 1, false);
        }
        return rootNodes;
    }

    public boolean isLeafChildrenNullable() {
        return leafChildrenNullable.get();
    }

    public void setLeafChildrenNullable(boolean leafChildrenNullable) {
        this.leafChildrenNullable.set(leafChildrenNullable);
    }

    public boolean isUseMapModel() {
        return useMapModel;
    }

    public void setUseMapModel(boolean useMapModel) {
        this.useMapModel = useMapModel;
    }

    public Map<String, Boolean> getExpanded() {
        return expanded;
    }

    public static List<RecursiveNode> getRootNodesStatically() {
        return rootNodes;
    }

    public static RichTreeModelRecursiveAdaptorBean getFacesContext() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (RichTreeModelRecursiveAdaptorBean) elContext.getELResolver().getValue(elContext, null, "richTreeModelRecursiveAdaptorBean");
    }
}
