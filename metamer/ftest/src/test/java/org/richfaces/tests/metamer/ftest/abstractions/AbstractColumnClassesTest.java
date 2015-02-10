/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.abstractions;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractColumnClassesTest extends AbstractWebDriverTest {

    private static final String CLASS = "class";
    private static final String COL_CLASSNAME = "col";
    private static final char SEPARATOR_COMMA = ',';
    private static final char SEPARATOR_SPACE = ' ';

    protected final Attributes<DataTableAttributes> attributes = getAttributes();

    private String generateSeparatedColumnClasses(int count, char separator) {
        StringBuilder sb = new StringBuilder(30);
        for (int i = 1; i <= count; i++) {
            sb.append(COL_CLASSNAME).append(i).append(separator);
        }
        return sb.substring(0, sb.length() - 1);// get rid of the last separator
    }

    public abstract TableAdapter getAdaptedComponent();

    public void performAfterSettingOfAttributes() {
    }

    private void testColumnClasses(final char separator, final int generatedClassNames) {
        String expectedColumnClass;
        String retrievedColumnClass;
        TableAdapter table = getAdaptedComponent();

        int numberOfColumns = table.getNumberOfColumns();
        final String testedColumnClasses = generateSeparatedColumnClasses(generatedClassNames, separator);
        final String[] testedColumnClassesArray = testedColumnClasses.split(String.valueOf(separator));

        attributes.set(DataTableAttributes.columnClasses, testedColumnClasses);
        performAfterSettingOfAttributes();
        switch (separator) {
            case SEPARATOR_SPACE:
                for (int rowIndex = 0; rowIndex < table.getNumberOfVisibleRows(); rowIndex++) {
                    for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                        retrievedColumnClass = table.getColumnWithData(rowIndex, columnIndex).getAttribute(CLASS);
                        // check column contains all space-separated classes
                        assertTrue(retrievedColumnClass.contains(testedColumnClasses),
                            format("Column at [{0}][{1}] should contain all classes <{2}>. It contains <{3}>.", rowIndex, columnIndex, testedColumnClasses, retrievedColumnClass));
                    }
                }
                return;
            case SEPARATOR_COMMA:
                int mod;
                for (int rowIndex = 0; rowIndex < table.getNumberOfVisibleRows(); rowIndex++) {
                    for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                        mod = columnIndex % generatedClassNames;
                        expectedColumnClass = testedColumnClassesArray[mod];
                        retrievedColumnClass = table.getColumnWithData(rowIndex, columnIndex).getAttribute(CLASS);
                        // check that column contains correct class
                        assertTrue(retrievedColumnClass.contains(expectedColumnClass),
                            format("Column at [{0}][{1}] should contain class <{2}>. It contains <{3}>.", rowIndex, columnIndex, expectedColumnClass, retrievedColumnClass));
                        // check that column does not contain other columnClasses
                        for (String columnClass : testedColumnClassesArray) {
                            if (!columnClass.equals(expectedColumnClass)) {
                                assertFalse(retrievedColumnClass.contains(columnClass),
                                    format("Column at [{0}][{1}] should not contain class <{2}>. It contains <{3}>.", rowIndex, columnIndex, columnClass, retrievedColumnClass));
                            }
                        }
                    }
                }
                return;
            default:
                throw new UnsupportedOperationException(format("not supported separator <{0}>.", separator));
        }
    }

    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated() {
        testColumnClasses(SEPARATOR_COMMA, getAdaptedComponent().getNumberOfColumns());
    }

    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated() {
        testColumnClasses(SEPARATOR_SPACE, getAdaptedComponent().getNumberOfColumns());
    }

    public void testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated() {
        testColumnClasses(SEPARATOR_COMMA, getAdaptedComponent().getNumberOfColumns() + 2);
    }

    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated() {
        testColumnClasses(SEPARATOR_COMMA, getAdaptedComponent().getNumberOfColumns() - 1);
    }

    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated() {
        testColumnClasses(SEPARATOR_SPACE, getAdaptedComponent().getNumberOfColumns() - 1);
    }

    public void testColumnClasses_oneColumnClass() {
        testColumnClasses(SEPARATOR_SPACE, 1);
    }

    public interface TableAdapter {

        int getNumberOfColumns();

        int getNumberOfVisibleRows();

        WebElement getColumnWithData(final int r, final int c);
    }
}
