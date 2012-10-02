package org.richfaces.tests.metamer.bean.issues.rf12291;

import java.util.Arrays;
import java.util.List;

public class RegularTableBean {

    public static final String BEAN_NAME = "regularTableBean";

    private List<String> tableData = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    public List<String> getTableData() {
        return tableData;
    }

}