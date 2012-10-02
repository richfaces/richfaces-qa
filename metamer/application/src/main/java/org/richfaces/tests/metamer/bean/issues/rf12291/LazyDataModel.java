package org.richfaces.tests.metamer.bean.issues.rf12291;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;

public class LazyDataModel<T> extends ExtendedDataModel<T> {

    public static final String BEAN_NAME = "lazyDataModel";

    private int rowIndex;
    private Object rowKey;
    private final DataProvider<T> dataProvider;

    /** represents the total number of rows and not just the the current page */
    private Integer rowCount;
    private List<Object> pageRowKeys;
    private Map<Object, T> pageKeyedRows;

    public LazyDataModel(DataProvider<T> dataProvider) {
        super();
        this.dataProvider = dataProvider;
    }

    @Override
    public void setRowKey(Object key) {
        rowKey = key;
    }

    @Override
    public Object getRowKey() {
        return rowKey;
    }

    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        SequenceRange sequenceRange = (SequenceRange) range;
        List<T> data = dataProvider.getItemsByRange(sequenceRange.getFirstRow(),
            sequenceRange.getFirstRow() + sequenceRange.getRows());

        rowIndex = 0;
        pageRowKeys = new ArrayList<Object>();
        pageKeyedRows = new HashMap<Object, T>();

        for (T t : data) {
            Object id = getId(t);
            pageRowKeys.add(id);
            pageKeyedRows.put(id, t);
            visitor.process(context, id, argument);
            rowIndex++;
        }
    }

    @Override
    public boolean isRowAvailable() {
        return rowKey != null;
    }

    @Override
    public int getRowCount() {
        // Store the row count locally since this method will be called by the framework multiple times for a single page
        // request.
        if (rowCount == null) {
            rowCount = dataProvider.getRowCount();
        }
        return rowCount;
    }

    @Override
    public T getRowData() {
        // No need to go to the data provider to get the current row data.
        // The rows were already retrieved in the walk() method and stored in the pageKeyedRows map.
        if (rowKey == null) {
            return null;
        } else {
            T rowData = pageKeyedRows.get(rowKey);
            if (rowData == null) {
                rowData = dataProvider.getItemByKey(rowKey);
                pageKeyedRows.put(rowKey, rowData);
            }
            return rowData;
        }
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException(); // not used by the framework
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException(); // not used by the framework
    }

    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException(); // not used by the framework
    }

    public Object getId(T t) {
        return dataProvider.getKey(t);
    }

    public List<Object> getPageRowKeys() {
        return pageRowKeys;
    }

    public Map<Object, T> getPageKeyedRows() {
        return pageKeyedRows;
    }

    public Object getRowKeyByIndex(int index) {
        if (pageRowKeys != null && index > -1 && index < pageRowKeys.size()) {
            return pageRowKeys.get(index);
        }
        return null;
    }

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

}
