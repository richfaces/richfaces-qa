package org.richfaces.tests.metamer.bean.issues;

public class RowFiller {

    private int recordNumber;
    private String text1;
    private String selectVal;
    private String text2;

    public RowFiller() {
        super();
    }

    /**
     * @return the text1
     */
    public String getText1() {
        return text1;
    }

    /**
     * @param text1 the text1 to set
     */
    public void setText1(String text1) {
        this.text1 = text1;
    }

    /**
     * @return the selectVal
     */
    public String getSelectVal() {
        return selectVal;
    }

    /**
     * @param selectVal the selectVal to set
     */
    public void setSelectVal(String selectVal) {
        this.selectVal = selectVal;
    }

    /**
     * @return the text2
     */
    public String getText2() {
        return text2;
    }

    /**
     * @param text2 the text2 to set
     */
    public void setText2(String text2) {
        this.text2 = text2;
    }

    /**
     * @return the recordNumber
     */
    public int getRecordNumber() {
        return recordNumber;
    }

    /**
     * @param recordNumber the recordNumber to set
     */
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
}
