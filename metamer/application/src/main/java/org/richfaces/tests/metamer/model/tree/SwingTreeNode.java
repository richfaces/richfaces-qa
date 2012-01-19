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
package org.richfaces.tests.metamer.model.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.richfaces.tests.metamer.model.Labeled;

import com.google.common.collect.Iterators;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SwingTreeNode<Content extends Labeled> implements TreeNode, TreeNodeWithContent<Content>, Serializable {

    private static final long serialVersionUID = 1L;
    private Content content;
    private List<TreeNode> children = new ArrayList<TreeNode>();
    private TreeNode parent;
    private String type;
    
    public void addChild(TreeNode child) {
        children.add(child);
    }
    
    @Override
    public Content getContent() {
        return content;
    }
    
    @Override
    public void setContent(Content content) {
        this.content = content;        
    }

    @Override
    public Enumeration<TreeNode> children() {
        return Iterators.asEnumeration(children.iterator());
    }

    @Override
    public boolean getAllowsChildren() {
        return isLeaf();
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }
    
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }    
    
    public static TreeNodeWithContentFactory<TreeNodeWithContent<Labeled>> createFactory() {
        return new TreeNodeWithContentFactory<TreeNodeWithContent<Labeled>>() {
            @Override
            public SwingTreeNode<Labeled> createTreeNode(TreeNodeWithContent<Labeled> parent, Labeled content) {
                SwingTreeNode<Labeled> treeNode = new SwingTreeNode<Labeled>();
                treeNode.setContent(content);
                if (parent != null) {
                    SwingTreeNode<Labeled> castedParent = (SwingTreeNode<Labeled>) parent;
                    castedParent.addChild(treeNode);
                    treeNode.setParent(castedParent);
                }
                return treeNode;
            }
        };
    }

    @Override
    public String toString() {
        return content == null ? super.toString() : content.toString();
    }
    
}
