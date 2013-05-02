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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.richfaces.model.SwingTreeNodeDataModelImpl;
import org.richfaces.model.TreeDataModel;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.LabeledComparator;
import org.richfaces.tests.metamer.bean.RichBean;
import org.richfaces.tests.metamer.model.CompactDisc;
import org.richfaces.tests.metamer.model.Company;
import org.richfaces.tests.metamer.model.Labeled;
import org.richfaces.tests.metamer.model.State;
import org.richfaces.tests.metamer.model.tree.CompactDiscXmlDescriptor;
import org.richfaces.tests.metamer.model.tree.RichFacesTreeNode;
import org.richfaces.tests.metamer.model.tree.SwingTreeNode;
import org.richfaces.tests.metamer.model.tree.TreeNodeWithContent;
import org.richfaces.tests.metamer.model.tree.TreeNodeWithContentFactory;
import org.richfaces.ui.drag.dropTarget.DropEvent;
import org.richfaces.ui.iteration.tree.UITree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23123 $
 */
@ManagedBean(name = "richTreeBean")
@ViewScoped
public class RichTreeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger logger;
    private Attributes attributes;
    private RichFacesTreeNode<Labeled> richfacesTreeNodeRoot;
    private TreeDataModel<?> richFacesTreeDataModelRoot;
    private List<SwingTreeNode<Labeled>> swingTreeNodeRoot;
    private Collection<? extends Serializable> selection;

    @ManagedProperty("#{model.compactDiscs}")
    private List<CompactDiscXmlDescriptor> compactDiscs;
    private Map<State, Set<Company>> companiesCache;
    private Map<Company, Set<CompactDisc>> cdCache;

    private boolean testLoadingFacet = false;
    private boolean delayedRender = false;

    private Map<TreeNodeWithContent<Labeled>, Boolean> expanded = new HashMap<TreeNodeWithContent<Labeled>, Boolean>();

    private Comparator<Labeled> labeledComparator = new LabeledComparator();

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UITree.class, getClass());
        attributes.get("rendered").setValue(true);
        attributes.get("toggleType").setValue("ajax");
        attributes.get("selectionType").setValue("ajax");

        // attributes which needs to be tested another way
        attributes.remove("selectionChangeListener");
        attributes.remove("toggleListener");
        attributes.remove("value");
        attributes.remove("var");
        attributes.remove("rowKeyVar");
        attributes.remove("stateVar");
        attributes.remove("nodeType");

        // build cache
        companiesCache = new TreeMap<State, Set<Company>>(labeledComparator);
        cdCache = new HashMap<Company, Set<CompactDisc>>();
        for (CompactDiscXmlDescriptor descriptor : compactDiscs) {
            createCompactDisc(descriptor);
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<CompactDiscXmlDescriptor> getCompactDiscs() {
        return compactDiscs;
    }

    public void setCompactDiscs(List<CompactDiscXmlDescriptor> compactDiscs) {
        this.compactDiscs = compactDiscs;
    }

    public void processDragging(DropEvent dropEvent) {
        RichBean.logToPage("* dropListener");
    }

    public TreeDataModel<?> getRichFacesTreeDataModelRoot() {
        if (richFacesTreeDataModelRoot == null) {
            richFacesTreeDataModelRoot = new SwingTreeNodeDataModelImpl();
            richFacesTreeDataModelRoot.setWrappedData(getSwingTreeNodeRoot());
        }
        return richFacesTreeDataModelRoot;
    }

    public RichFacesTreeNode<Labeled> getRichFacesTreeNodeRoot() {
        if (richfacesTreeNodeRoot == null) {
            List<RichFacesTreeNode<Labeled>> richfacesTreeNodeList = (List<RichFacesTreeNode<Labeled>>) (List<?>) buildTree(RichFacesTreeNode.createFactory());
            richfacesTreeNodeRoot = new RichFacesTreeNode<Labeled>();
            int i=0;
            for (RichFacesTreeNode<?> node : richfacesTreeNodeList) {
                richfacesTreeNodeRoot.addChild(i, node);
                i++;
            }
        }
        return richfacesTreeNodeRoot;
    }

    public List<SwingTreeNode<Labeled>> getSwingTreeNodeRoot() {
        if (swingTreeNodeRoot == null) {
            List<SwingTreeNode<Labeled>> swingTreeNodeList = (List<SwingTreeNode<Labeled>>) (List<?>) buildTree(SwingTreeNode.createFactory());
            swingTreeNodeRoot = swingTreeNodeList;
        }
        return swingTreeNodeRoot;
    }

    public Collection<? extends Serializable> getSelection() {
        return selection;
    }

    public void setSelection(Collection<? extends Serializable> selection) {
        this.selection = selection;
    }

    public boolean isTestLoadingFacet() {
        return testLoadingFacet;
    }

    public void setTestLoadingFacet(boolean testLoadingFacet) {
        this.testLoadingFacet = testLoadingFacet;
    }

    public boolean isDelayedRender() {
        return delayedRender;
    }

    public void setDelayedRender(boolean delayedRender) {
        this.delayedRender = delayedRender;
    }

    public void preRenderView(ComponentSystemEvent event) {
        if (delayedRender) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }
    }

    public Map<TreeNodeWithContent<Labeled>, Boolean> getExpanded() {
        return expanded;
    }

    public void expandAll() {
        for (Entry<TreeNodeWithContent<Labeled>, Boolean> entry : expanded.entrySet()) {
            entry.setValue(true);
        }
    }

    public void collapseAll() {
        for (Entry<TreeNodeWithContent<Labeled>, Boolean> entry : expanded.entrySet()) {
            entry.setValue(false);
        }
    }

    private List<TreeNodeWithContent<Labeled>> buildTree(TreeNodeWithContentFactory<TreeNodeWithContent<Labeled>> treeNodeFactory) {
        List<TreeNodeWithContent<Labeled>> firstLevelNodes = new ArrayList<TreeNodeWithContent<Labeled>>();
        for(State state : companiesCache.keySet()) {
            TreeNodeWithContent<Labeled> stateTreeNode = treeNodeFactory.createTreeNode(null, state);
            stateTreeNode.setType("country");
            for(Company company : companiesCache.get(state)) {
                TreeNodeWithContent<Labeled> companyTreeNode = treeNodeFactory.createTreeNode(stateTreeNode, company);
                companyTreeNode.setType("company");
                for (CompactDisc cd : cdCache.get(company)) {
                    TreeNodeWithContent<Labeled> cdTreeNode = treeNodeFactory.createTreeNode(companyTreeNode, cd);
                    cdTreeNode.setType("cd");
                    expanded.put(cdTreeNode, false);
                }
                expanded.put(companyTreeNode, false);
            }
            expanded.put(stateTreeNode, false);
            firstLevelNodes.add(stateTreeNode);
        }
        return firstLevelNodes;
    }

    private CompactDisc createCompactDisc(CompactDiscXmlDescriptor descriptor) {
        final Company company = findOrCreateCompany(descriptor);
        CompactDisc cd = new CompactDisc(descriptor.getTitle(), descriptor.getArtist());
        cdCache.get(company).add(cd);
        return cd;
    }

    private Company findOrCreateCompany(CompactDiscXmlDescriptor descriptor) {
        final State country = findOrCreateCountry(descriptor);
        Company company = new Company(descriptor.getCompany());
        if (!cdCache.containsKey(company)) {
            cdCache.put(company, new TreeSet<CompactDisc>(labeledComparator));
            companiesCache.get(country).add(company);
        }
        return company;
    }

    private State findOrCreateCountry(CompactDiscXmlDescriptor descriptor) {
        String countryName = descriptor.getCountry();
        State country = new State();
        country.setName(countryName);
        if (!companiesCache.containsKey(country)) {
            companiesCache.put(country, new TreeSet<Company>(labeledComparator));
        }
        return country;
    }
}
