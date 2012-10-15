package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

public class RF12304Page extends MetamerPage {

    @FindBy(css = "#firstTable tr:nth-of-type(3)")
    private WebElement thirdRowFirstTable;

    @FindBy(css = "#secondTable tr:nth-of-type(3)")
    private WebElement thirdRowSecondTable;

    public boolean isSelected(WebElement row) {
        String clazz = row.getAttribute("class");

        return clazz.contains("rf-edt-r-sel");
    }

    public WebElement getThirdRowFirstTable() {
        return thirdRowFirstTable;
    }

    public WebElement getThirdRowSecondTable() {
        return thirdRowSecondTable;
    }

}
