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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnAndRowClassesTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.AbstractCollapsibleSubTableTest.EmployeeRecord;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.TestCollapsibleSubTableFiltering.DataTableWithCSTWithFilteringHeader;
import org.richfaces.tests.metamer.ftest.webdriver.utils.LazyLoadedCachedValue;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestCollapsibleSubTableColumnAndRowClasses extends AbstractColumnAndRowClassesTest {

    private final TableAdapter[] subTables = new TableAdapter[] { new SubTableAdapter(0), new SubTableAdapter(1) };

    @UseForAllTests(valuesFrom = ValuesFrom.FROM_FIELD, value = "subTables")
    private TableAdapter subTable;

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithFilteringHeader table;

    public TableAdapter getAdaptedComponent() {
        return subTable;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richCollapsibleSubTable/filtering.xhtml";
    }

    @Test
    @CoversAttributes("columnClasses")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_oneColumnClass() {
        super.testColumnClasses_oneColumnClass();
    }

    @Test
    @CoversAttributes("rowClasses")
    public void testRowClasses_numberOfRowClassesEqualsToRows_commaSeparated() {
        super.testRowClasses_numberOfRowClassesEqualsToRows_commaSeparated();
    }

    @Test
    @CoversAttributes("rowClasses")
    public void testRowClasses_numberOfRowClassesEqualsToRows_spaceSeparated() {
        super.testRowClasses_numberOfRowClassesEqualsToRows_spaceSeparated();
    }

    @Test
    @CoversAttributes("rowClasses")
    public void testRowClasses_numberOfRowClassesGreaterThanRows_commaSeparated() {
        super.testRowClasses_numberOfRowClassesGreaterThanRows_commaSeparated();
    }

    @Test
    @CoversAttributes("rowClasses")
    public void testRowClasses_numberOfRowClassesLesserThanRows_commaSeparated() {
        super.testRowClasses_numberOfRowClassesLesserThanRows_commaSeparated();
    }

    @Test
    @CoversAttributes("rowClasses")
    public void testRowClasses_numberOfRowClassesLesserThanRows_spaceSeparated() {
        super.testRowClasses_numberOfRowClassesLesserThanRows_spaceSeparated();
    }

    @Test
    @CoversAttributes("rowClasses")
    public void testRowClasses_oneRowClass() {
        super.testRowClasses_oneRowClass();
    }

    private class SubTableAdapter implements TableAdapter {

        private final LazyLoadedCachedValue<Integer> menTableRecords = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                return table.getRow(subTableIndex).getAllRows().size();
            }
        };
        private final int subTableIndex;

        public SubTableAdapter(int subTableIndex) {
            this.subTableIndex = subTableIndex;
        }

        @Override
        public WebElement getCellWithData(int r, int c) {
            EmployeeRecord row = table.getRow(subTableIndex).getRow(r);
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
            return menTableRecords.getValue();
        }

        @Override
        public WebElement getRowWithData(int r) {
            return table.getRow(subTableIndex).getRow(r).getRootElement();
        }

        @Override
        public String toString() {
            return subTableIndex == 1 ? "men" : "women";
        }
    }
}
