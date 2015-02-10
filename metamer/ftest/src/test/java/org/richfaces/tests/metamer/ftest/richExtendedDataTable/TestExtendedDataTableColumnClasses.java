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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnClassesTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.FilteringDTRow;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.FilteringEDT;
import org.richfaces.tests.metamer.ftest.webdriver.utils.LazyLoadedCachedValue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestExtendedDataTableColumnClasses extends AbstractColumnClassesTest {

    private final TableAdapter adapter = new ExtendedDataTableAdapter();

    @FindByJQuery("div.rf-edt[id$=richEDT]")
    private FilteringEDT table;

    public TableAdapter getAdaptedComponent() {
        return adapter;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/filtering.xhtml");
    }

    @BeforeMethod
    public void setupRows() {
        attributes.set(DataTableAttributes.rows, 10);
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

    private class ExtendedDataTableAdapter implements TableAdapter {

        private final LazyLoadedCachedValue<Integer> visibleRows = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                return table.advanced().getNumberOfVisibleRows();
            }
        };

        @Override
        public WebElement getColumnWithData(int r, int c) {
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

    }
}
