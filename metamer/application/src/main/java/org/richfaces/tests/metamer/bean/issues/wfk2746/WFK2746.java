package org.richfaces.tests.metamer.bean.issues.wfk2746;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.richfaces.component.UIExtendedDataTable;

@Named("extended")
@SessionScoped
public class WFK2746 implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<ExtendedDatatableTO> arrList = new ArrayList();

    public ArrayList<ExtendedDatatableTO> getArrList1() {
        return this.arrList1;
    }

    public void setArrList1(ArrayList<ExtendedDatatableTO> arrList1) {
        this.arrList1 = arrList1;
    }

    private ArrayList<ExtendedDatatableTO> arrList1 = new ArrayList();
    private UIExtendedDataTable displayItems = new UIExtendedDataTable();
    private Collection<Object> selection;
    private ExtendedDatatableTO currentItem = new ExtendedDatatableTO();
    private String retrievedValue;

    public String getRetrievedValue() {
        return this.retrievedValue;
    }

    public void setRetrievedValue(String retrievedValue) {
        this.retrievedValue = retrievedValue;
    }

    public ExtendedDatatableTO getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(ExtendedDatatableTO currentItem) {
        this.currentItem = currentItem;
    }

    public Collection<Object> getSelection() {
        return this.selection;
    }

    public void setSelection(Collection<Object> selection) {
        this.selection = selection;
    }

    public UIExtendedDataTable getDisplayItems() {
        return this.displayItems;
    }

    public void setDisplayItems(UIExtendedDataTable displayItems) {
        this.displayItems = displayItems;
    }

    public ArrayList<ExtendedDatatableTO> getArrList() {
        return this.arrList;
    }

    public void setArrList(ArrayList<ExtendedDatatableTO> arrList) {
        this.arrList = arrList;
    }

    public void onLoad() {
        this.arrList.clear();
        this.arrList1.clear();

        System.out.println("Inside onlOad");
        ExtendedDatatableTO obj = new ExtendedDatatableTO();
        obj.setStrDeliveryNo("123456");
        obj.setStrLRNo("987456");
        this.arrList.add(obj);

        ExtendedDatatableTO obj1 = new ExtendedDatatableTO();
        obj1.setStrDeliveryNo("dxvfsdgdf");
        obj1.setStrLRNo("987dfgdsfg456");
        this.arrList.add(obj1);
    }

    public void cancel() {
        this.arrList1.clear();
    }

    public void cancelWhichWorks() {
        this.arrList1.clear();
        this.selection = null;
    }

    public void selectionListener(AjaxBehaviorEvent event) {
        System.out.println("Inside selectionListener");
        UIExtendedDataTable datatable = (UIExtendedDataTable) event.getComponent();

        Collection<Object> iterator = datatable.getSelection();
        for (Object currentRow : iterator) {
            datatable.setRowKey(currentRow);
            System.out.println("Data table row key ::" + currentRow);
            if (datatable.isRowAvailable()) {
                this.currentItem = ((ExtendedDatatableTO) datatable.getRowData());
                System.out.println("Current Item::: " + this.currentItem.getStrDeliveryNo());
            }
        }
        this.arrList1.clear();
        if (this.currentItem.getStrDeliveryNo().equalsIgnoreCase("123456")) {
            ExtendedDatatableTO obj2 = new ExtendedDatatableTO();
            obj2.setStrInvoiceNo("vgdfgvb");
            obj2.setStrShipToPartyCode("bcfbdf");
            this.arrList1.add(obj2);

            ExtendedDatatableTO obj3 = new ExtendedDatatableTO();
            obj3.setStrInvoiceNo("dxvfdgvsdsdgdf");
            obj3.setStrShipToPartyCode("987dfgddgdfsfg456");
            this.arrList1.add(obj3);
        } else {
            ExtendedDatatableTO obj2 = new ExtendedDatatableTO();
            obj2.setStrInvoiceNo("123dfgdf456");
            obj2.setStrShipToPartyCode("987fdgdfg456");
            this.arrList1.add(obj2);

            ExtendedDatatableTO obj3 = new ExtendedDatatableTO();
            obj3.setStrInvoiceNo("dxeftefgvfsdgdf");
            obj3.setStrShipToPartyCode("987dfgdsfdfgdfgg456");
            this.arrList1.add(obj3);
        }
    }

}
