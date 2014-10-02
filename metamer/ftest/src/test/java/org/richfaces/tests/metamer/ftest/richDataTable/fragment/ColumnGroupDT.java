package org.richfaces.tests.metamer.ftest.richDataTable.fragment;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataTable.RichFacesDataTable;
import org.richfaces.tests.metamer.ftest.richColumn.AbstractColumnTest.StateCapitalRow;

public class ColumnGroupDT extends RichFacesDataTable<ColumnGroupHeaderDT,StateCapitalRow,ColumnGroupFooterDT>{
    @FindBy(css = ".rf-dt-sftr>td")
    private WebElement columnFooterElement;
    @FindBy(css = ".rf-dt-shdr>th")
    private WebElement columnHeaderElement;

    public WebElement getColumnFooterElement() {
        return columnFooterElement;
    }

    public WebElement getColumnHeaderElement() {
        return columnHeaderElement;
    }
}
