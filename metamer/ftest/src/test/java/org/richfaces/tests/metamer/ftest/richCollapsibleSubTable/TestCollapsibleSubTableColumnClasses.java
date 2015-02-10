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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnClassesTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.AbstractCollapsibleSubTableTest.EmployeeRecord;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.TestCollapsibleSubTableFiltering.DataTableWithCSTWithFilteringHeader;
import org.richfaces.tests.metamer.ftest.webdriver.utils.LazyLoadedCachedValue;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTableColumnClasses extends AbstractColumnClassesTest {

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithFilteringHeader table;

    private final TableAdapter tableAdapter = new CollapsibleSubTableAdapter();

    public TableAdapter getAdaptedComponent() {
        return tableAdapter;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/filtering.xhtml");
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_oneColumnClass() {
        super.testColumnClasses_oneColumnClass();
    }

    private class CollapsibleSubTableAdapter implements TableAdapter {

        private final LazyLoadedCachedValue<Integer> menTableRecords = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                return table.getRow(0).getAllRows().size();
            }
        };
        private final LazyLoadedCachedValue<Integer> visibleRows = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                return table.getRow(0).advanced().getNumberOfVisibleRows() + table.getRow(1).advanced().getNumberOfVisibleRows();
            }

        };

        @Override
        public WebElement getColumnWithData(int r, int c) {
            int correctTable = r > (menTableRecords.getValue() - 1) ? 1 : 0;
            int correctRow = (correctTable == 1 ? r - menTableRecords.getValue() : r);
            EmployeeRecord row = table.getRow(correctTable).getRow(correctRow);
            switch (c) {
                case 0:
                    return row.getNameElement();
                case 1:
                    return row.getTitleElement();
                case 2:
                    return row.getBirthdateElement();
                default:
                    throw new UnsupportedOperationException("Table does not have so many rows!");
            }
        }

        @Override
        public int getNumberOfColumns() {
            return 3;
        }

        @Override
        public int getNumberOfVisibleRows() {
            return visibleRows.getValue();
        }

    }
}
