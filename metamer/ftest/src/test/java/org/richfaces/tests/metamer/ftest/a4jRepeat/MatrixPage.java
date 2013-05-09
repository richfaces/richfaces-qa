/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Vector;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class MatrixPage extends MetamerPage {

    public static final int ROWS_COUNT = 4;
    public static final int COLUMNS_COUNT = 4;

    @FindBy(css = "div[id$=matrixInput] > table > tbody > tr")
    public List<WebElement> inputRows;
    @FindBy(css = "div[id$=matrixOutput] > table > tbody > tr")
    public List<WebElement> outputRows;

    public By cell = By.cssSelector("div.cell");
    private By input = By.cssSelector("input[type=text]");
    private By incrementLink = By.cssSelector("a[id$=increaseLink]");
    private By decrementLink = By.cssSelector("a[id$=decreaseLink]");
    private By clearLink = By.cssSelector("a[id$=clearLink]");

    private Vector<Vector<Integer>> matrix;

    public void incrementValue(int row, int column) {
        Graphene.guardAjax(inputRows.get(row).findElements(cell).get(column).findElement(incrementLink)).click();

        Integer oldValue = matrix.get(row).get(column);
        matrix.get(row).set(column, oldValue + 1);
    }

    public void decrementValue(int row, int column) {
        Graphene.guardAjax(inputRows.get(row).findElements(cell).get(column).findElement(decrementLink)).click();

        Integer oldValue = matrix.get(row).get(column);
        matrix.get(row).set(column, oldValue - 1);
    }

    public void changeValue(int row, int column, int newValue) {
        WebElement inputElement = inputRows.get(row).findElements(cell).get(column).findElement(input);
        inputElement.clear();
        Graphene.guardAjax(inputElement).sendKeys(Integer.toString(newValue), Keys.RETURN);

        matrix.get(row).set(column, newValue);
    }

    public void clearValue(int row, int column) {
        Graphene.guardAjax(inputRows.get(row).findElements(cell).get(column).findElement(clearLink)).click();

        matrix.get(row).set(column, 0);
    }

    public Integer obtainOutputValue(int row, int column) {
        return Integer.valueOf(outputRows.get(row).findElements(cell).get(column).getText());
    }

    public Integer obtainInputValue(int row, int column) {
        return Integer.valueOf(inputRows.get(row).findElements(cell).get(column).findElement(input)
            .getAttribute("value"));
    }

    public void checkMatrix() {
        for (int row = 0; row < ROWS_COUNT; row++) {
            for (int column = 0; column < COLUMNS_COUNT; column++) {
                Integer expectedValue = matrix.get(row).get(column);
                assertEquals(obtainInputValue(row, column), expectedValue,
                    format("The input value on coordinates row:{0}, column:{1} does not match: ", row, column));
                assertEquals(obtainOutputValue(row, column), expectedValue,
                    format("The output value on coordinates row:{0}, column:{1} does not match: ", row, column));
            }
        }
    }

    public void initializeMatrix() {
        matrix = new Vector<Vector<Integer>>(COLUMNS_COUNT);
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            matrix.add(x, new Vector<Integer>(ROWS_COUNT));
            for (int y = 0; y < ROWS_COUNT; y++) {
                matrix.get(x).add(y, 0);
            }
        }
    }
}
