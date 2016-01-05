/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnAndRowClassesTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.FilteringDT;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.FilteringDTRow;
import org.richfaces.tests.metamer.ftest.webdriver.utils.LazyLoadedCachedValue;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestDataTableColumnAndRowClasses extends AbstractColumnAndRowClassesTest {

    private final TableAdapter adapter = new DataTableAdapter();

    @FindByJQuery(value = "table.rf-dt[id$=richDataTable]")
    private FilteringDT table;

    public TableAdapter getAdaptedComponent() {
        return adapter;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/filtering.xhtml";
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

    private class DataTableAdapter implements TableAdapter {

        private final LazyLoadedCachedValue<Integer> visibleRows = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                return table.advanced().getNumberOfVisibleRows();
            }
        };

        @Override
        public WebElement getCellWithData(int r, int c) {
            FilteringDTRow row = table.getRow(r);
            switch (c) {
                case 0:
                    return row.getSexColumnElement();
                case 1:
                    return row.getNameColumnElement();
                case 2:
                    return row.getTitleColumnElement();
                case 3:
                    return row.getNumberOfKids1ColumnElement();
                case 4:
                    return row.getNumberOfKids2ColumnElement();
                default:
                    throw new UnsupportedOperationException("Table does not have so many rows!");
            }
        }

        @Override
        public int getNumberOfColumns() {
            return 5;
        }

        @Override
        public int getNumberOfVisibleRows() {
            return visibleRows.getValue();
        }

        @Override
        public WebElement getRowWithData(int r) {
            return table.getRow(r).getRootElement();
        }
    }
}
