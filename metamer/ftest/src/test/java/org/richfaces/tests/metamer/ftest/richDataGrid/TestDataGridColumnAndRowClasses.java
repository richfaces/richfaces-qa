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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnAndRowClassesTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDataGrid.fragment.GridWithStates;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestDataGridColumnAndRowClasses extends AbstractColumnAndRowClassesTest {

    private final TableAdapter adapter = new DataGridAdapter();

    @FindBy(css = "table.rf-dg[id$=richDataGrid]")
    private GridWithStates dataGrid;

    @Override
    public TableAdapter getAdaptedComponent() {
        return adapter;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataGrid/simple.xhtml";
    }

    @BeforeMethod
    public void setupAttributes() {
        setAttribute("elements", "21");
        setAttribute("columns", "3");
    }

    @Test
    @CoversAttributes("columnClasses")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    public void testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated();
    }

    @Test
    @CoversAttributes("columnClasses")
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

    private class DataGridAdapter implements TableAdapter {

        @Override
        public WebElement getCellWithData(int r, int c) {
            return dataGrid.getRecordsInRow(r).get(c).getRootElement();
        }

        @Override
        public int getNumberOfColumns() {
            return dataGrid.getNumberOfColumns();
        }

        @Override
        public int getNumberOfVisibleRows() {
            return dataGrid.getNumberOfRows();
        }

        @Override
        public WebElement getRowWithData(int r) {
            return dataGrid.advanced().getRowElements().get(r);
        }
    }
}
