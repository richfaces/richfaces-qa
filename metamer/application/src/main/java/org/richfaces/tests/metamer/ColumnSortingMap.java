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
package org.richfaces.tests.metamer;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

import org.richfaces.model.SortMode;
import org.richfaces.model.SortOrder;
import org.richfaces.ui.iteration.UIDataTableBase;

public abstract class ColumnSortingMap extends TreeMap<String, ColumnSortingMap.ColumnSorting> {

    private static final long serialVersionUID = 1L;

    public ColumnSorting get(Object key) {
        if (key instanceof String && !containsKey(key)) {
            String columnName = (String) key;
            put(columnName, new ColumnSorting(columnName));
        }
        return super.get(key);
    }

    protected abstract Attributes getAttributes();

    public class ColumnSorting implements Serializable {
        private static final long serialVersionUID = 1L;

        private String columnName;
        private SortOrder order = SortOrder.unsorted;

        public ColumnSorting(String key) {
            this.columnName = key;
        }

        public SortOrder getOrder() {
            return order;
        }

        public void setOrder(SortOrder order) {
            this.order = order;
        }

        @SuppressWarnings("unchecked")
        public void reverseOrder() {
            Object sortModeAttribute = getAttributes().get("sortMode").getValue();
            SortMode mode;
            if(sortModeAttribute != null) {
                if(sortModeAttribute instanceof String) {
                    mode = SortMode.valueOf((String) sortModeAttribute);
                } else {
                    mode = (SortMode) sortModeAttribute;
                }
            } else {
                throw new IllegalArgumentException("sortMode attribute has to be set correctly!");
            }

            Object sortOrderObject = getAttributes().get("sortPriority").getValue();
            Collection<String> sortPriority;
            if (sortOrderObject instanceof Collection) {
                sortPriority = (Collection<String>) sortOrderObject;
            } else {
                throw new IllegalStateException("sortOrder attribute has to be Collection");
            }

            if (SortMode.single.equals(mode)) {
                ColumnSortingMap.this.clear();
                ColumnSortingMap.this.put(columnName, this);

                sortPriority.clear();
            } else {
                sortPriority.remove(columnName);
            }

            sortPriority.add(columnName);

            if (SortOrder.ascending.equals(order)) {
                order = SortOrder.descending;
            } else {
                order = SortOrder.ascending;
            }
        }

    }
}