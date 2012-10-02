package org.richfaces.tests.metamer.bean.issues.rf12291;

public class PaginatedTableBean {

    private int scrollerPage = 1;
    private int rowsPerPage = 10;

    public int getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(int scrollerPage) {
        this.scrollerPage = scrollerPage;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

}