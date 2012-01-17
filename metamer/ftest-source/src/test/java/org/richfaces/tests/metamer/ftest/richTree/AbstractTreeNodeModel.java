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
package org.richfaces.tests.metamer.ftest.richTree;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.tests.metamer.ftest.model.AbstractModel;
import org.richfaces.tests.metamer.ftest.model.ModelIterable;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public class AbstractTreeNodeModel extends AbstractModel<JQueryLocator> {
    
    static JQueryLocator treeNode = jq("div.rf-tr-nd");
    static JQueryLocator treeNodeExpanded = jq("div.rf-tr-nd-exp");
    static JQueryLocator treeNodeCollapsed = jq("div.rf-tr-nd-colps");
    static JQueryLocator treeNodeSelected = jq("div.rf-tr-nd:has(> .rf-trn > .rf-trn-sel)");
    
    protected TreeModel tree;
    ReferencedLocator<JQueryLocator> nodes = ref(root, "> " + treeNode.getRawLocator());
    ReferencedLocator<JQueryLocator> anyNodes = ref(root, treeNode.getRawLocator());
    ReferencedLocator<JQueryLocator> nodesCollapsed = ref(root, "> " + treeNodeCollapsed.getRawLocator());
    ReferencedLocator<JQueryLocator> nodesExpanded = ref(root, "> " + treeNodeExpanded.getRawLocator());
    ReferencedLocator<JQueryLocator> anyNodesSelected = ref(root, treeNodeSelected.getRawLocator());
    
    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();
    
    public AbstractTreeNodeModel(JQueryLocator root) {
        super(root);
    }

    public Iterable<TreeNodeModel> getNodes() {
        Iterable<TreeNodeModel> result = new ModelIterable<JQueryLocator, TreeNodeModel>(nodes.iterator(),
            TreeNodeModel.class, new Class[] { TreeModel.class }, new Object[] { tree });
        return result;
    }

    public Iterable<TreeNodeModel> getAnyNodes() {
        Iterable<TreeNodeModel> result = new ModelIterable<JQueryLocator, TreeNodeModel>(anyNodes.iterator(),
            TreeNodeModel.class, new Class[] { TreeModel.class }, new Object[] { tree });
        return result;
    }

    public TreeNodeModel getNode(int index) {
        TreeNodeModel trn = new TreeNodeModel(nodes.get(index), tree);
        return trn;
    }

    public Iterable<TreeNodeModel> getExpandedNodes() {
        Iterable<TreeNodeModel> result = new ModelIterable<JQueryLocator, TreeNodeModel>(
            nodesExpanded.iterator(), TreeNodeModel.class, new Class[] { TreeModel.class },
            new Object[] { tree });
        return result;
    }

    public Iterable<TreeNodeModel> getCollapsedNodes() {
        Iterable<TreeNodeModel> result = new ModelIterable<JQueryLocator, TreeNodeModel>(
            nodesCollapsed.iterator(), TreeNodeModel.class, new Class[] { TreeModel.class },
            new Object[] { tree });
        return result;
    }

    public Iterable<TreeNodeModel> getAnySelectedNodes() {
        Iterable<TreeNodeModel> result = new ModelIterable<JQueryLocator, TreeNodeModel>(
            anyNodesSelected.iterator(), TreeNodeModel.class, new Class[] { TreeModel.class },
            new Object[] { tree });
        return result;
    }
    
    public int getNodesCount() {
        return selenium.getCount(nodes);
    }

    public int getExpandedNodesCount() {
        return selenium.getCount(nodesExpanded);
    }

    public int getCollapsedNodesCount() {
        return selenium.getCount(nodesCollapsed);
    }

    public int getAnySelectedNodesCount() {
        return selenium.getCount(anyNodesSelected);
    }
}
