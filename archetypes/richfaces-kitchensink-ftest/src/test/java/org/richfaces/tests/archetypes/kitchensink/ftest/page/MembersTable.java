package org.richfaces.tests.archetypes.kitchensink.ftest.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MembersTable {

    @FindBy(xpath = "//*[@class='rf-dt-b']")
    private WebElement table;
    
    @FindBy(xpath = "//*[@class='rf-dt-b']/tr")
    private WebElement tableRow;
    
    @FindBy(xpath = "//*[@class='rf-dt-b']/tr")
    private List<WebElement> tableRows;
    
    public List<WebElement> getTableRows() {
        return tableRows;
    }

    public void setTableRows(List<WebElement> tableRows) {
        this.tableRows = tableRows;
    }

    public WebElement getTable() {
        return table;
    }

    public void setTable(WebElement table) {
        this.table = table;
    }

    public WebElement getTableRow() {
        return tableRow;
    }

    public void setTableRow(WebElement tableRow) {
        this.tableRow = tableRow;
    }

    public int getNumberOfRows() {
        return table.findElements(By.xpath("//*[@class='rf-dt-b']/tr")).size();
    }

    public void waitUntilNumberOfRowsChanged(int timeoutInSeconds, WebDriver webDriver, final int numberOfRowsBefore) {
        (new WebDriverWait(webDriver, timeoutInSeconds)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {

                int numberOfRowsAfter = getNumberOfRows();
                return (numberOfRowsBefore < numberOfRowsAfter);
            }
        });
    }

}
