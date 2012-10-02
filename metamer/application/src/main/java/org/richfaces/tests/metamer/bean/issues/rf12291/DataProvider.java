package org.richfaces.tests.metamer.bean.issues.rf12291;

import java.io.Serializable;
import java.util.List;

public interface DataProvider<T> extends Serializable {

    /**
     * Get number of all rows.
     *
     * @return number of rows.
     */
    int getRowCount();

    /**
     * Loads elements from given range. Starting from startRow, and up to but excluding endRow.
     *
     * @param firstRow first row to load
     * @param endRow end row to load
     * @return element list
     */
    List<T> getItemsByRange(int firstRow, int endRow);

    /**
     * Load single element by given key.
     *
     * @param key element key to be loaded.
     * @return element or null, if not found
     */
    T getItemByKey(Object key);

    /**
     * Get element key. If key is not instance of Integer or org.richfaces.model.ScrollableTableDataModel.SimpleRowKey, it is
     * necessary to implement javax.faces.convert.Converter for key type.
     *
     * @param item element, which key to be get
     * @return element key
     */
    Object getKey(T item);

}