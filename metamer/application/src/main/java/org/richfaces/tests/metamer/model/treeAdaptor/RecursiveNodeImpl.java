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
package org.richfaces.tests.metamer.model.treeAdaptor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.richfaces.tests.metamer.bean.rich.RichTreeModelRecursiveAdaptorBean;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version$Revision: 22493$
 */
public class RecursiveNodeImpl extends AbstractNode implements RecursiveNode {
    private static final long serialVersionUID = 1L;

    private static final int CHILDREN = 4;
    private static final int LEVELS = 2;
    private static final List<RecursiveNode> EMPTY_LIST = new LinkedList<RecursiveNode>();
    private static final Map<Integer, RecursiveNode> EMPTY_MAP = new HashMap<Integer, RecursiveNode>();

    int recursionLevel;
    boolean oddBranch;
    List<RecursiveNode> children = null;
    NodeMap nodeMap = new NodeMap();
    ModelNode model;

    protected RecursiveNodeImpl(boolean isRoot, int number, String parentLabel, int recursionLevel, boolean oddBranch) {
        super(isRoot, number, parentLabel);
        this.recursionLevel = recursionLevel;
        this.oddBranch = oddBranch;
    }

    public static RecursiveNode getInstance(boolean isRoot, int number, String parentLabel, int recursionLevel, boolean oddBranch) {
        RecursiveNodeImpl recursiveNode = new RecursiveNodeImpl(isRoot, number, parentLabel, recursionLevel, oddBranch);
        return LazyLoadingChecker.wrapInstance(recursiveNode);
    }

    public static List<RecursiveNode> createChildren(boolean createRoots, String parentLabel, int recursionLevel, boolean preferedOddBranch) {
        List<RecursiveNode> children = new LinkedList<RecursiveNode>();
        for (int i = 0; i < CHILDREN; i++) {
            boolean oddBranch = preferedOddBranch;
            if (createRoots) {
                oddBranch = i % 2 == 1;
            }
            RecursiveNode node = RecursiveNodeImpl.getInstance(createRoots, i, parentLabel, recursionLevel, oddBranch);
            children.add(node);
        }
        return children;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IRecursiveNode#getModel()
     */
    @Override
    public ModelNode getModel() {
        if (isLeaf() && model == null) {
            model = ModelNodeImpl.getInstance(false, 1, getLabel());
        }
        return model;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IRecursiveNode#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return getRecursionLevel() >= LEVELS + (oddBranch ? 0 : 1);
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IRecursiveNode#getRecursiveList()
     */
    @Override
    public List<RecursiveNode> getRecursiveList() {
        if (isLeaf()) {
            return getEmptyList();
        }
        if (children == null) {
            children = createChildren(false, getLabel(), recursionLevel + 1, this.oddBranch);
        }
        return children;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IRecursiveNode#getRecursiveMap()
     */
    @Override
    public Map<Integer, RecursiveNode> getRecursiveMap() {
        if (isLeaf()) {
            return getEmptyMap();
        }
        return nodeMap;
    }

    private List<RecursiveNode> getEmptyList() {
        return isNullable() ? null : EMPTY_LIST;
    }

    private Map<Integer, RecursiveNode> getEmptyMap() {
        return isNullable() ? null : EMPTY_MAP;
    }

    private boolean isNullable() {
        return RichTreeModelRecursiveAdaptorBean.getFacesContext().isLeafChildrenNullable();
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IRecursiveNode#getLabel()
     */
    @Override
    public String getLabel() {
        String parentLabel = (isRoot() ? "" : getParentLabel() + "-") + "R-";
        String recursionLabel = (isRecursionRoot() ? parentLabel : getParentLabel() + ".") + getNumber();
        return recursionLabel;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IRecursiveNode#getRecursionLevel()
     */
    @Override
    public int getRecursionLevel() {
        return recursionLevel;
    }

    private boolean isRecursionRoot() {
        return recursionLevel == 1;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public class NodeMap implements Map<Integer, RecursiveNode>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int size() {
            return getRecursiveList().size();
        }

        @Override
        public boolean isEmpty() {
            return getRecursiveList().isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public RecursiveNode get(Object key) {
            if (key instanceof Integer) {
                return getRecursiveList().get((Integer) key);
            }
            throw new IllegalStateException("there is no value for the key '" + key + "' (type "
                + key.getClass().getName() + ")");
        }

        @Override
        public RecursiveNode put(Integer key, RecursiveNode value) {
            throw new UnsupportedOperationException("not supported");
        }

        @Override
        public RecursiveNode remove(Object key) {
            throw new UnsupportedOperationException("not supported");
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends RecursiveNode> m) {
            throw new UnsupportedOperationException("not supported");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("not supported");
        }

        @Override
        public Set<Integer> keySet() {
            HashSet<Integer> set = new HashSet<Integer>();
            for (int i = 0; i < getRecursiveList().size(); i++) {
                set.add(i);
            }
            return set;
        }

        @Override
        public Collection<RecursiveNode> values() {
            return getRecursiveList();
        }

        @Override
        public Set<java.util.Map.Entry<Integer, RecursiveNode>> entrySet() {
            HashSet<Map.Entry<Integer, RecursiveNode>> set = new HashSet<Map.Entry<Integer, RecursiveNode>>();
            int i = 0;
            for (RecursiveNode node : getRecursiveList()) {
                set.add(new MapEntry(i++, node));
            }
            return set;
        }

    }

    public class MapEntry implements Map.Entry<Integer, RecursiveNode> {

        int key;
        RecursiveNode node;

        public MapEntry(int key, RecursiveNode node) {
            this.key = key;
            this.node = node;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public RecursiveNode getValue() {
            return node;
        }

        @Override
        public RecursiveNode setValue(RecursiveNode value) {
            throw new UnsupportedOperationException("not supported");
        }
    }
}