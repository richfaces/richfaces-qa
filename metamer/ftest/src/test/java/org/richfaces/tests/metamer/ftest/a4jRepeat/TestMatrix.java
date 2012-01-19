/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.arquillian.ajocado.guard.RequestGuardFactory.guard;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.Vector;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class TestMatrix extends AbstractAjocadoTest {

    Vector<Vector<Integer>> matrix;
    Coordinate coordinate;

    JQueryLocator inputRow = pjq("div[id$=matrixInput] > table > tbody > tr");
    JQueryLocator outputRow = pjq("div[id$=matrixOutput] > table > tbody > tr");
    JQueryLocator column = jq("div.cell");

    int rows;
    int columns;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jRepeat/matrix.xhtml");
    }

    @BeforeMethod
    public void initializeTest() {
        initializeMatrixProperties();
        initializeMatrix();
    }

    @Test
    public void testIncrementing() {
        coordinate = new Coordinate(2, rows - 1);
        coordinate.increase();
        coordinate.increase();

        coordinate = new Coordinate(columns, 2);
        coordinate.increase();
        coordinate.increase();
        coordinate.increase();
        coordinate.increase();

        checkMatrix();
    }

    @Test
    public void testDecrementing() {
        coordinate = new Coordinate(2, 1);
        coordinate.decrease();
        coordinate.decrease();

        coordinate = new Coordinate(columns - 1, rows - 1);
        coordinate.decrease();
        coordinate.decrease();
        coordinate.decrease();

        coordinate = new Coordinate(columns, rows);
        coordinate.decrease();

        checkMatrix();
    }

    @Test
    public void testChanging() {
        new Coordinate(1, rows).change(42);
        new Coordinate(2, 2).change(-127);
        new Coordinate(1, rows).change(89);

        checkMatrix();
    }

    @Test
    public void testClear() {
        coordinate = new Coordinate(columns, 1);
        coordinate.change(5);

        coordinate = new Coordinate(1, rows);
        coordinate.change(63);
        coordinate.clear();

        coordinate = new Coordinate(columns - 1, 1);
        coordinate.change(-87);
        coordinate.clear();

        checkMatrix();
    }

    @Test
    public void testMatrixAfterRerender() {
        testChanging();

        rerenderAll();

        checkMatrix();
    }

    @Test
    public void testMatrixAfterRefresh() {
        testChanging();

        selenium.refresh();
        selenium.waitForPageToLoad();

        initializeMatrix();
        checkMatrix();
    }

    private void initializeMatrixProperties() {
        rows = selenium.getCount(inputRow);
        assertTrue(rows > 3, format("there should be more at least 4 rows in input matrix, but found {0}", rows));

        columns = selenium.getCount(inputRow.get(1).getDescendant(column));
        assertTrue(columns > 3,
            format("there should be more at least 4 columns in input matrix, but found {0}", columns));

        int cells = selenium.getCount(inputRow.getDescendant(column));
        assertEquals(cells, rows * columns,
            format("there should be totally {0} cells, but {1} was found", rows * columns, cells));

        assertEquals(selenium.getCount(outputRow), rows,
            "there should be same number of rows in outputMatrix like in outputMatrix");

        assertEquals(selenium.getCount(outputRow.getDescendant(column)), cells,
            "there should be same number of cells in outputMatrix like in outputMatrix");
    }

    private void initializeMatrix() {
        matrix = new Vector<Vector<Integer>>(columns);
        for (int x = 0; x < columns; x++) {
            matrix.add(x, new Vector<Integer>(rows));
            for (int y = 0; y < rows; y++) {
                matrix.get(x).add(y, 0);
            }
        }
    }

    private void checkMatrix() {
        new MatrixCommand() {
            public void processCell(int x, int y) {
                coordinate = new Coordinate(x, y);
                coordinate.check();
            }
        }.processAll();
    }

    private class Coordinate {
        private int x;
        private int y;

        private JQueryLocator input = jq("input:text[id$=valueInput]");
        private JQueryLocator increaseLink = jq("a[id$=increaseLink]");
        private JQueryLocator decreaseLink = jq("a[id$=decreaseLink]");
        private JQueryLocator clearLink = jq("a[id$=clearLink]");

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void increase() {
             guard(selenium, RequestType.XHR).click(getInputCell().getDescendant(increaseLink));
            addValue(1);
            check();
        }

        public void decrease() {
             guard(selenium, RequestType.XHR).click(getInputCell().getDescendant(decreaseLink));
            addValue(-1);
            check();
        }

        public void clear() {
             guard(selenium, RequestType.XHR).click(getInputCell().getDescendant(clearLink));
            setValue(0);
            check();
        }

        public void change(int newValue) {
            changeInputValue(newValue);
            setValue(newValue);
            check();
        }

        public int obtainOutputValue() {
            return Integer.valueOf(selenium.getText(getOutputCell()));
        }

        public int obtainInputValue() {
            return Integer.valueOf(selenium.getValue(getInputCell().getDescendant(input)));
        }

        public void changeInputValue(int newValue) {
            JQueryLocator locator = getInputCell().getDescendant(input);
             guard(selenium, RequestType.XHR).type(locator, Integer.toString(newValue));
        }

        public void check() {
            checkInputValue();
            checkOutputValue();
        }

        public void checkInputValue() {
            assertEquals(obtainInputValue(), getValue(),
                format("The input value on coordinates x:{0}, y:{1} does not match: ", x, y));
        }

        public void checkOutputValue() {
            assertEquals(obtainOutputValue(), getValue(),
                format("The output value on coordinates x:{0}, y:{1} does not match: ", x, y));
        }

        public int getValue() {
            return matrix.get(x - 1).get(y - 1);
        }

        public void setValue(int value) {
            matrix.get(x - 1).set(y - 1, value);
        }

        public void addValue(int delta) {
            matrix.get(x - 1).set(y - 1, getValue() + delta);
        }

        private JQueryLocator getInputCell() {
            return inputRow.get(y).getDescendant(column).get(x);
        }

        private JQueryLocator getOutputCell() {
            return outputRow.get(y).getDescendant(column).get(x);
        }
    }

    private abstract class MatrixCommand {
        public abstract void processCell(int x, int y);

        public void processAll() {
            for (int x = 1; x <= columns; x++) {

                for (int y = 1; y <= rows; y++) {

                    processCell(x, y);
                }
            }
        }
    }
}
