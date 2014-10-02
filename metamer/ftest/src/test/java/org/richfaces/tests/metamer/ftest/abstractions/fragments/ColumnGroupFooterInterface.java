package org.richfaces.tests.metamer.ftest.abstractions.fragments;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface ColumnGroupFooterInterface {

    WebElement getCell(int row, int cell);

    String getFooterClass();

    WebElement getRootElement();

    WebElement getRow(int row);

    List<WebElement> getRows();
}
