package org.richfaces.tests.metamer;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

import org.richfaces.component.SortOrder;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.model.SortMode;

public abstract class ColumnSortingMap extends TreeMap<String, ColumnSortingMap.ColumnSorting> {

    private static final long serialVersionUID = 1L;

    public ColumnSorting get(Object key) {
        if (key instanceof String && !containsKey(key)) {
            String columnName = (String) key;
            put(columnName, new ColumnSorting(columnName));
        }
        return super.get(key);
    }

    protected abstract UIDataTableBase getBinding();

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
            SortMode mode = getBinding().getSortMode();

            Object sortOrderObject = getAttributes().get("sortPriority").getValue();
            Collection<String> sortPriority;
            if (sortOrderObject instanceof Collection) {
                sortPriority = (Collection<String>) sortOrderObject;
            } else {
                throw new IllegalStateException("sortOrder attribute have to be Collection");
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