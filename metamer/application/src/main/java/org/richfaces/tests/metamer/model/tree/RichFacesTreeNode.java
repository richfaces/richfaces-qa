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
package org.richfaces.tests.metamer.model.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.richfaces.model.TreeNode;
import org.richfaces.tests.metamer.model.Labeled;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RichFacesTreeNode<Content extends Labeled> implements TreeNode, TreeNodeWithContent<Content>, Serializable {

    private static final long serialVersionUID = 1L;
    private Map<Object, TreeNode> children = new HashMap<Object, TreeNode>();
    private List<Object> keys = new ArrayList<Object>();
    private Content content;
    private String type;

    @Override
    public void addChild(Object key, TreeNode child) {
        children.put(key, child);
        keys.add(key);
    }

    @Override
    public Content getContent() {
        return content;
    }

    @Override
    public TreeNode getChild(Object key) {
        return children.get(key);
    }

    @Override
    public Iterator<Object> getChildrenKeysIterator() {
        return keys.iterator();
    }

    @Override
    public int indexOf(Object key) {
        return keys.indexOf(key);
    }

    @Override
    public void insertChild(int index, Object key, TreeNode child) {
        children.put(key, child);
        keys.add(index, key);
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public void removeChild(Object key) {
        children.remove(key);
        keys.remove(key);
    }

    @Override
    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return content == null ? super.toString() : content.toString();
    }

    public static TreeNodeWithContentFactory<TreeNodeWithContent<Labeled>> createFactory() {
        return new TreeNodeWithContentFactory<TreeNodeWithContent<Labeled>>() {
            @Override
            public RichFacesTreeNode<Labeled> createTreeNode(TreeNodeWithContent<Labeled> parent, Labeled content) {
                RichFacesTreeNode<Labeled> treeNode = new RichFacesTreeNode<Labeled>();
                treeNode.setContent(content);
                if (parent != null) {
                    RichFacesTreeNode<Labeled> castedParent = (RichFacesTreeNode<Labeled>) parent;
                    castedParent.addChild(treeNode.getContent().getLabel(), treeNode);
                }
                return treeNode;
            }
        };
    }
}
