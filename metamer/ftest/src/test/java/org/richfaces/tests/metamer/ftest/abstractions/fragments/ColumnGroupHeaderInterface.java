package org.richfaces.tests.metamer.ftest.abstractions.fragments;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface ColumnGroupHeaderInterface {

    WebElement getCell(int row, int cell);

    WebElement getRootElement();

    WebElement getRow(int row);

    List<WebElement> getRows();
}
