package org.richfaces.tests.archetypes.kitchensink.ftest.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MembersTable {

    @FindBy(xpath = "//*[@class='rf-dt-b']/tr")
    private WebElement table;

    public WebElement getTable() {
        return table;
    }

    public void setTable(WebElement table) {
        this.table = table;
    }


}
