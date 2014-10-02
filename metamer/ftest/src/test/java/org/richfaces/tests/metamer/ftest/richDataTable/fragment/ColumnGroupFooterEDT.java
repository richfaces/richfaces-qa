package org.richfaces.tests.metamer.ftest.richDataTable.fragment;

import java.util.List;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.ColumnGroupFooterInterface;

public class ColumnGroupFooterEDT implements ColumnGroupFooterInterface{

    @Root
    private WebElement rootElement;

    @FindBy(tagName = "td")
    private WebElement footer;
    @FindBy(className = "rf-edt-ftr")
    private List<WebElement> rows;

    @Override
    public WebElement getCell(int row, int cell) {
        return getRow(row).findElements(By.className("rf-edt-ftr-c")).get(cell);
    }

    @Override
    public String getFooterClass() {
        return footer.getAttribute("class");
    }

    @Override
    public WebElement getRootElement() {
        return rootElement;
    }

    @Override
    public WebElement getRow(int row) {
        return getRows().get(row);
    }

    @Override
    public List<WebElement> getRows() {
        return rows;
    }
}
