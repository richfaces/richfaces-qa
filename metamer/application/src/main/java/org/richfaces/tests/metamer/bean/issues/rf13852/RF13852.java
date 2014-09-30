package org.richfaces.tests.metamer.bean.issues.rf13852;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.tests.metamer.bean.issues.RowFiller;
import org.richfaces.component.UISelect;

@ViewScoped
@ManagedBean(name = "rf13852")
public class RF13852 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<String> selectLOV;
    private List<RowFiller> rowFiller = new ArrayList<RowFiller>();
    private RowFiller currentRow = new RowFiller();
    private Collection<Object> selection;
    private int currentRecordNumber = 1;

    @PostConstruct
    public void postContruct() {
        selectLOV = new ArrayList<String>();
        selectLOV.add("One");
        selectLOV.add("Two");
        selectLOV.add("Three");
    }

    public void newRowBtnAction(AjaxBehaviorEvent event) {

        RowFiller newRow = new RowFiller();
        newRow.setRecordNumber(currentRecordNumber++);
        if (rowFiller.size() == 0) {
            currentRow = newRow;
        }
        rowFiller.add(newRow);

    }

    public void clearAllBtnAction(AjaxBehaviorEvent event) {

        if (rowFiller != null) {
            rowFiller.clear();
        }
    }

    public void valueChanged(AjaxBehaviorEvent event) {
        String strValue = (String) ((UISelect) event.getComponent()).getValue();

        if (currentRow != null) {
            currentRow.setSelectVal(strValue);
            currentRow.setText2("Current Selected Record(" + currentRow.getRecordNumber() + ") :Colum 1 = "
                + currentRow.getText1() + ", Column 2 = " + strValue);
        }
    }

    public void selectionListener(AjaxBehaviorEvent event) {

        UIExtendedDataTable dataTable = (UIExtendedDataTable) event.getComponent();

        Object originalKey = dataTable.getRowKey();

        if (getSelection() != null) {

            for (Object selectionKey : getSelection()) {

                dataTable.setRowKey(selectionKey);

                if (dataTable.isRowAvailable()) {

                    currentRow = (RowFiller) dataTable.getRowData();

                }// end if data row is available check

            }// end key loop

            dataTable.setRowKey(originalKey);

        }// end selection object existence verification

    }

    public void textOneAction(AjaxBehaviorEvent event) {

    }

    /**
     * @return the selectLOV
     */
    public List<String> getSelectLOV() {
        return selectLOV;
    }

    /**
     * @param selectLOV the selectLOV to set
     */
    public void setSelectLOV(List<String> selectLOV) {
        this.selectLOV = selectLOV;
    }

    /**
     * @return the rowFiller
     */
    public List<RowFiller> getRowFiller() {
        return rowFiller;
    }

    /**
     * @param rowFiller the rowFiller to set
     */
    public void setRowFiller(List<RowFiller> rowFiller) {
        this.rowFiller = rowFiller;
    }

    /**
     * @return the currentRow
     */
    public RowFiller getCurrentRow() {
        return currentRow;
    }

    /**
     * @param currentRow the currentRow to set
     */
    public void setCurrentRow(RowFiller currentRow) {
        this.currentRow = currentRow;
    }

    /**
     * @return the selection
     */
    public Collection<Object> getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    public void setSelection(Collection<Object> selection) {
        this.selection = selection;
    }
}
