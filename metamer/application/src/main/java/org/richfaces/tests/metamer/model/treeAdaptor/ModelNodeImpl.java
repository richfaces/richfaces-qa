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
package org.richfaces.tests.metamer.model.treeAdaptor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22493 $
 */
public class ModelNodeImpl extends AbstractNode implements ModelNode {
    private static final long serialVersionUID = 1L;

    private static final int BS = 3;
    private static final int KS = 4;
    List<B> bs;
    Map<K, V> map;
    List<RecursiveNode> rs;

    protected ModelNodeImpl(boolean isRoot, int number, String parentLabel) {
        super(isRoot, number, parentLabel);
    }

    public static ModelNode getInstance(boolean isRoot, int number, String parentLabel) {
        ModelNodeImpl modelNode = new ModelNodeImpl(isRoot, number, parentLabel);
        return LazyLoadingChecker.wrapInstance(modelNode);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IModelNode#getLabel()
     */
    @Override
    public String getLabel() {
        return isRoot() ? "M" : getParentLabel() + "-M";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IModelNode#getValue()
     */
    @Override
    public A getValue() {
        return new A(getLabel());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IModelNode#getList()
     */
    @Override
    public List<B> getList() {
        if (bs == null) {
            bs = new LinkedList<B>();
            for (int i = 0; i < BS; i++) {
                bs.add(new B(getLabel(), i));
            }
        }
        return bs;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IModelNode#getMap()
     */
    @Override
    public Map<K, V> getMap() {
        if (map == null) {
            map = new LinkedHashMap<K, V>();
            for (int i = 0; i < KS; i++) {
                map.put(new K(getLabel(), i), new V(getLabel(), i));
            }
        }
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.tests.metamer.model.treeAdaptor.IModelNode#getRecursive()
     */
    @Override
    public List<RecursiveNode> getRecursive() {
        if (rs == null) {
            rs = RecursiveNodeImpl.createChildren(false, getLabel(), 1, true);
        }
        return rs;
    }

    public static class A implements Serializable {
        private static final long serialVersionUID = 1L;

        String parentLabel;

        public A(String parentLabel) {
            this.parentLabel = parentLabel;
        }

        public String getLabel() {
            return parentLabel + "-A";
        }
    }

    public static class B implements Serializable {
        private static final long serialVersionUID = 1L;

        int number;
        String parentLabel;

        public B(String parentLabel, int number) {
            this.parentLabel = parentLabel;
            this.number = number;
        }

        public String getLabel() {
            return parentLabel + "-B-" + number;
        }
    }

    public static class K implements Serializable, Comparable<K> {
        private static final long serialVersionUID = 1L;

        int number;
        String parentLabel;

        public K(String parentLabel, int number) {
            this.parentLabel = parentLabel;
            this.number = number;
        }

        public String getLabel() {
            return parentLabel + "-K-" + number;
        }

        @Override
        public int compareTo(K o) {
            return this.getLabel().compareTo(o.getLabel());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            K rhs = (K) obj;
            return new EqualsBuilder().append(getLabel(), rhs.getLabel()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(getLabel()).toHashCode();

        }

        @Override
        public String toString() {
            return Integer.toString(number);
        }
    }

    public static class V implements Serializable {
        private static final long serialVersionUID = 1L;

        int number;
        String parentLabel;

        public V(String parentLabel, int number) {
            this.parentLabel = parentLabel;
            this.number = number;
        }

        public String getLabel() {
            return parentLabel + "-V-" + number;
        }
    }

    @Override
    public String toString() {
        return getLabel();
    }
}