package org.richfaces.tests.metamer.bean.issues.rf12291;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondTabDataProvider implements DataProvider<String> {

    private static final long serialVersionUID = 2040425084009811377L;

    private List<String> tableData = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    @Override
    public int getRowCount() {
        return tableData.size();
    }

    @Override
    public List<String> getItemsByRange(int firstRow, int endRow) {
        List<String> itemsInRange = new ArrayList<String>();
        for (int i = firstRow; i < endRow && i < tableData.size(); i++) {
            itemsInRange.add(tableData.get(i));
        }
        return itemsInRange;
    }

    @Override
    public String getItemByKey(Object key) {
        return tableData.get((Integer) key);
    }

    @Override
    public Object getKey(String item) {
        for (int i = 0; i < tableData.size(); i++) {
            if (tableData.get(i).equals(item)) {
                return i;
            }
        }
        return -1; // invalid item
    }

}