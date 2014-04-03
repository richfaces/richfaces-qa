package org.richfaces.tests.showcase.dataTable;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataTable.RichFacesDataTable;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.testng.annotations.Test;

public class TestDataTableFragment extends AbstractWebDriverTest {

    @FindBy(css = ".rf-dt")
    private TableWithCar table;

    @Test
    public void test() {
        webDriver.get(contextRoot.toExternalForm()
            + "richfaces/component-sample.jsf?demo=dataTable&sample=tableFiltering&skin=blueSky");

        for (CarRow row : table.getAllRows()) {
            System.out.println(row.getPriceCell().getText());
        }
    }

    public class TableWithCar extends RichFacesDataTable<CarRow> {

    }
}